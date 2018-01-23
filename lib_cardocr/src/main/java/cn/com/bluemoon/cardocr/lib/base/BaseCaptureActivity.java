package cn.com.bluemoon.cardocr.lib.base;

import android.Manifest;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import Decoder.BASE64Encoder;
import cn.com.bluemoon.cardocr.lib.R;
import cn.com.bluemoon.cardocr.lib.bean.BankCardBean;
import cn.com.bluemoon.cardocr.lib.bean.BankCardBean.ItemsBean;
import cn.com.bluemoon.cardocr.lib.bean.BankInfo;
import cn.com.bluemoon.cardocr.lib.bean.DrivingLicenseBean;
import cn.com.bluemoon.cardocr.lib.bean.DrivingLicenseInfo;
import cn.com.bluemoon.cardocr.lib.bean.IDCardBackBean;
import cn.com.bluemoon.cardocr.lib.bean.IDCardFrontBean;
import cn.com.bluemoon.cardocr.lib.bean.IdCardInfo;
import cn.com.bluemoon.cardocr.lib.common.CardType;
import cn.com.bluemoon.cardocr.lib.common.YTServerAPI;
import cn.com.bluemoon.cardocr.lib.widget.LoadingDialog;


/**
 * 识别身份证照相机
 * Created by liangjiangli on 2017/7/12.
 */

public abstract class BaseCaptureActivity extends BasePermissionFragmentActivity
        implements SurfaceHolder.Callback {


    public static final String BUNDLE_DATA = "bean";
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    SurfaceView mSurfaceView;
    RelativeLayout layoutRoot;

    private YTServerAPI mServer;
    private Handler mHandler;
    protected CardType cartType;
    protected int title;
    private String url;
    private LoadingDialog mLoadingDialog;
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String CARD_TYPE = "cardType";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_capture_camera_preview);
        cartType = (CardType)getIntent().getSerializableExtra(CARD_TYPE);
        title = getIntent().getIntExtra(TITLE, 0);
        url = getIntent().getStringExtra(URL);
        initSurfaceView();

        View v = getLayoutInflater().inflate(getLayoutId(), null, false);
        layoutRoot.addView(v);
        if (getLayoutId() != 0) {
            initCustomView();
        }
    }

    /**自定义界面**/
    public abstract int getLayoutId();

    public abstract void initCustomView();
    /************/

    @Override
    protected String[] getPermissionGroup() {
        return new String[]{
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
    }

    @Override
    protected void doPermissionAction() {
        mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
        mCamera.stopPreview();
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Camera.Parameters parameters = mCamera.getParameters();
        //照相机宽高参数
        Size size = getSupportedSize(parameters.getSupportedPreviewSizes(), 0);
        if (size != null) {
            parameters.setPreviewSize(size.width, size.height);
        }
        size = getSupportedSize(parameters.getSupportedPictureSizes(), 1280);
        if (size != null) {
            parameters.setPictureSize(size.width, size.height);
        }

        parameters.set("orientation", "portrait");
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    private void initSurfaceView() {
        layoutRoot = (RelativeLayout) findViewById(R.id.layout_root);
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_surfaceView);
        mSurfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(this);

        mLoadingDialog = new LoadingDialog(this);
        mServer = new YTServerAPI(this);
        mHandler = new Handler();
        mServer.setRequestListener(new YTServerAPI.OnRequestListener() {
            @Override
            public void onSuccess(final int statusCode, final String responseBody) {
                mLoadingDialog.dismiss();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            boolean isSuccessful = false;
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            if (cartType == CardType.TYPE_BANK) {
                                isSuccessful = handleBankCardData(isSuccessful, bundle, responseBody);
                            } else if(cartType == CardType.TYPE_ID_CARD_FRONT||cartType == CardType.TYPE_ID_CARD_BACK){
                                isSuccessful = handleIdCardData(isSuccessful, bundle, responseBody);
                            } else{
                                isSuccessful = handleDrivingLicenseData(isSuccessful, bundle, responseBody);
                            }
                            if (isSuccessful) {
                                intent.putExtras(bundle);
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                certFail(statusCode);
                            }
                        } catch (Exception e) {
                            certFail(statusCode);
                        }

                    }
                });
            }

            @Override
            public void onFailure(int statusCode) {
                mLoadingDialog.dismiss();
                certFail(statusCode);
            }
        });
    }

    /**
     * 处理身份证数据
     * @param isSuccessful
     * @param bundle
     * @param responseBody
     * @return
     */
    protected boolean handleIdCardData(boolean isSuccessful, Bundle bundle, String responseBody) {
        //身份证
        IdCardInfo info = new IdCardInfo();
        //判断是否需要获取截图
        String path = null;
        if (!TextUtils.isEmpty(url)){
            File file = new File(url);
            if (!file.exists()) {
                file.mkdir();
            }
            path = url + "/" + System.currentTimeMillis() + ".png";
        }
        String image = null;
        if (cartType == CardType.TYPE_ID_CARD_FRONT) {
            IDCardFrontBean iDCardBean = JSON.parseObject(responseBody, IDCardFrontBean.class);
            if (iDCardBean.errorcode == 0) {
                info.setName(iDCardBean.name);
                info.setId(iDCardBean.id);
                info.setSex(iDCardBean.sex);
                info.setNation(iDCardBean.nation);
                info.setAddress(iDCardBean.address);
                info.setBirth(iDCardBean.birth);
                image = iDCardBean.frontimage;
                isSuccessful = true;
            }
        } else {
            IDCardBackBean iDCardBean = JSON.parseObject(responseBody, IDCardBackBean.class);
            if (iDCardBean.errorcode == 0) {
                info.setAuthority(iDCardBean.authority);
                info.setValidDate(iDCardBean.valid_date);
                image = iDCardBean.backimage;
                isSuccessful = true;
            }
        }
        if (!TextUtils.isEmpty(path)) {
            mServer.savaBitmap(image, path);
            info.setImageUrl(path);
        }
        bundle.putSerializable(BUNDLE_DATA, info);
        return isSuccessful;
    }
    /**
     * 处理银行卡数据
     * @param isSuccessful
     * @param bundle
     * @param responseBody
     * @return
     */
    protected boolean handleBankCardData(boolean isSuccessful, Bundle bundle, String responseBody) {
        //银行卡
        BankCardBean bankCardBean = JSON.parseObject(responseBody, BankCardBean.class);
        BankInfo info = new BankInfo();
        if (bankCardBean.errorcode == 0) {
            List<ItemsBean> items = bankCardBean.items;
            if (items != null && items.size() > 0) {
                for (ItemsBean item : items) {
                    if (getString(R.string.txt_card_number).equals(item.item)) {
                        info.setCardNumber(item.itemstring);
                        isSuccessful = true;
                    } else if (getString(R.string.txt_bank_info).equals(item.item)) {
                        String bankName = item.itemstring;
                        if (bankName.contains("(")) {
                            bankName = bankName.split("\\(")[0];
                        }
                        info.setBankName(bankName);
                        isSuccessful = true;
                    } else if (getString(R.string.txt_card_name).equals(item.item)) {
                        info.setCardName(item.itemstring);
                    } else if (getString(R.string.txt_card_type).equals(item.item)) {
                        info.setCardType(item.itemstring);
                    }
                }
            }
            bundle.putSerializable(BUNDLE_DATA, info);
        }
        return isSuccessful;
    }

    /**
     * 处理行驶证、驾驶证数据
     * @param isSuccessful
     * @param bundle
     * @param responseBody
     * @return
     */
    protected boolean handleDrivingLicenseData(boolean isSuccessful, Bundle bundle, String responseBody) {
        //行驶证、驾驶证
        DrivingLicenseBean drivinglicenseBean = JSON.parseObject(responseBody, DrivingLicenseBean.class);
        DrivingLicenseInfo info = new DrivingLicenseInfo();
        if (drivinglicenseBean.errorcode == 0) {
            List<DrivingLicenseBean.Item> items = drivinglicenseBean.items;
            if (items != null && items.size() > 0) {
                for (DrivingLicenseBean.Item item : items) {
                    if (getString(R.string.txt_license_number).equals(item.item)) {
                        info.setLicenseNumber(item.itemstring);
                        isSuccessful = true;
                    }else if (getString(R.string.txt_certificate_number).equals(item.item)) {
                        info.setCertificateNumber(item.itemstring);
                        isSuccessful = true;
                    }
                    if (!TextUtils.isEmpty(info.getLicenseNumber())) {
                        if (getString(R.string.txt_vehicle_type).equals(item.item)) {
                            info.setVehicleType(item.itemstring);
                        } else if (getString(R.string.txt_master).equals(item.item)) {
                            info.setMaster(item.itemstring);
                        } else if (getString(R.string.txt_address).equals(item.item)) {
                            info.setAddress(item.itemstring);
                        }else if (getString(R.string.txt_function).equals(item.item)) {
                            info.setFunction(item.itemstring);
                        }else if (getString(R.string.txt_brand_model).equals(item.item)) {
                            info.setBrandModel(item.itemstring);
                        }else if (getString(R.string.txt_identify_code).equals(item.item)) {
                            info.setIdentifyCode(item.itemstring);
                        }else if (getString(R.string.txt_engine_number).equals(item.item)) {
                            info.setEngineNumber(item.itemstring);
                        }else if (getString(R.string.txt_registration_date).equals(item.item)) {
                            info.setRegistrationDate(item.itemstring);
                        }else if (getString(R.string.txt_opening_date).equals(item.item)) {
                            info.setOpeningDate(item.itemstring);
                        }
                    } else if (!TextUtils.isEmpty(info.getCertificateNumber())) {
                        if (getString(R.string.txt_name).equals(item.item)) {
                            info.setName(item.itemstring);
                        } else if (getString(R.string.txt_gender).equals(item.item)) {
                            info.setGender(item.itemstring);
                        }else if (getString(R.string.txt_nationality).equals(item.item)) {
                            info.setNationality(item.itemstring);
                        }else if (getString(R.string.txt_address).equals(item.item)) {
                            info.setAddress(item.itemstring);
                        }else if (getString(R.string.txt_date_birth).equals(item.item)) {
                            info.setDateBirth(item.itemstring);
                        }else if (getString(R.string.txt_ling_opening_date).equals(item.item)) {
                            info.setOpeningDate(item.itemstring);
                        }else if (getString(R.string.txt_quasi_driving_type).equals(item.item)) {
                            info.setQuasiDrivingType(item.itemstring);
                        }else if (getString(R.string.txt_effective_date).equals(item.item)) {
                            info.setEffectiveDate(item.itemstring);
                        }else if (getString(R.string.txt_start_date).equals(item.item)) {
                            info.setStartDate(item.itemstring);
                        }
                    }
                }
            }
            bundle.putSerializable(BUNDLE_DATA, info);
        }
        return isSuccessful;
    }
    private void certFail(final int statusCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String toast;
                if (statusCode == HttpsURLConnection.HTTP_OK) {
                    toast = getString(R.string.card_cert_fail);
                } else {
                    if (statusCode == HttpsURLConnection.HTTP_NOT_FOUND) {
                        toast = getString(R.string.config_error);
                    } else {
                        toast = getString(R.string.server_error);
                    }
                }
                Toast.makeText(BaseCaptureActivity.this, toast, Toast.LENGTH_LONG).show();
                openCamera();
            }
        });

    }


    /**
     * 拍照上传识别
     */
    private boolean isControl = false;
    protected final void identification() {
        if (mCamera != null && !isControl) {
            isControl = true;
            try {
                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(final byte[] bytes, Camera camera) {
                        isControl = false;
                        mLoadingDialog.setText(getString(R.string.card_certing));
                        mLoadingDialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    BASE64Encoder encoder = new BASE64Encoder();
                                    String fileData = encoder.encode(bytes);
                                    if (cartType == CardType.TYPE_BANK) {
                                        mServer.bankCardOcr(fileData);
                                    } else if(cartType == CardType.TYPE_ID_CARD_FRONT||cartType == CardType.TYPE_ID_CARD_BACK){
                                        mServer.idCardOcr(fileData, cartType == CardType.TYPE_ID_CARD_FRONT ? 0 : 1);
                                    } else if(cartType == CardType.TYPE_DRIVING_LICENSE_XINGSHI||cartType == CardType.TYPE_DRIVING_LICENSE_JIASHI){
                                        mServer.drivingLicenseOcr(fileData, cartType == CardType.TYPE_DRIVING_LICENSE_XINGSHI ? 0 : 1);
                                    }
                                }  catch (Exception e) {
                                    certFail(HttpsURLConnection.HTTP_BAD_REQUEST);
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                    }
                });
            } catch (Exception e) {
                isControl = false;
            }

        }
    }

    private boolean isPause;

    @Override
    public void onResume() {
        super.onResume();
        if (isPause) {
            isPause = false;
            openCamera();
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


    /**
     * 打开摄像头
     */
    private void openCamera() {
        if (mCamera != null) {
            releaseCamera();
        }
        checkAndDoAction();
    }

    /**
     * 优先获取1280*720
     *
     * @param list
     * @return
     */
    private Size getSupportedSize(List<Size> list, int maxSize) {
        if (list == null || list.size() == 0) {
            return null;
        }
        for (Size s : list) {
            if (s.width == 1280 && s.height == 720) {
                return s;
            }
        }
        int i = 0;
        if (maxSize > 0) {
            int size = list.size();
            for (i = 0; i < size; i++) {
                Size s = list.get(i);
                if (maxSize >= s.width) {
                    return s;
                }
            }
            i--;
        }
        return list.get(i);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isPause = true;
        releaseCamera();
    }

}
