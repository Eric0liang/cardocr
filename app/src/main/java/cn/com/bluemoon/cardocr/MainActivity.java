package cn.com.bluemoon.cardocr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.bluemoon.cardocr.lib.CaptureActivity;
import cn.com.bluemoon.cardocr.lib.bean.BankInfo;
import cn.com.bluemoon.cardocr.lib.bean.DrivingLicenseInfo;
import cn.com.bluemoon.cardocr.lib.bean.IdCardInfo;
import cn.com.bluemoon.cardocr.lib.common.CardType;

public class MainActivity extends Activity implements View.OnClickListener{
    private TextView txtInfo;
    private ImageView imgIdCard;
    private CheckBox checkbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnIdCardBack = (Button)findViewById(R.id.btn_id_card_back);
        Button btnIdCardFront= (Button)findViewById(R.id.btn_id_card_front);
        Button btnBank= (Button)findViewById(R.id.btn_bank);
        Button btnXingshi= (Button) findViewById(R.id.btn_xingshi);
        Button btnJiashi= (Button) findViewById(R.id.btn_jiashi);
        checkbox = (CheckBox)findViewById(R.id.checkbox);
        imgIdCard = (ImageView)findViewById(R.id.img_front_id);
        txtInfo = (TextView)findViewById(R.id.txt_info);
        btnIdCardBack.setOnClickListener(this);
        btnIdCardFront.setOnClickListener(this);
        btnBank.setOnClickListener(this);
        btnXingshi.setOnClickListener(this);
        btnJiashi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String dirPath = Environment.getExternalStorageDirectory() + "/images";
        switch (v.getId()) {
            case R.id.btn_id_card_back:
                CaptureActivity.startAction(this, CardType.TYPE_ID_CARD_BACK, checkbox.isChecked() ? dirPath : null, 0);
                //CoustomCaptureActivity.startAction(this, CardType.TYPE_ID_CARD_BACK, 0);
                //CaptureActivity.startAction(this, CardType.TYPE_ID_CARD_BACK, 0);
                //CaptureActivity.startAction(this, CardType.TYPE_ID_CARD_BACK, R.string.txt_id_card_title, 0);
                break;
            case R.id.btn_id_card_front:
                CaptureActivity.startAction(this, CardType.TYPE_ID_CARD_FRONT, checkbox.isChecked() ? dirPath : null, 0);
                //CaptureActivity.startAction(this, CardType.TYPE_ID_CARD_FRONT, 0);
                //CaptureActivity.startAction(this, CardType.TYPE_ID_CARD_FRONT, R.string.txt_id_card_title, 0);
                break;
            case R.id.btn_bank:
                CaptureActivity.startAction(this, CardType.TYPE_BANK, 1);
                break;
            case R.id.btn_xingshi:
                CaptureActivity.startAction(this,CardType.TYPE_DRIVING_LICENSE_XINGSHI,2);
                break;
            case R.id.btn_jiashi:
                CaptureActivity.startAction(this,CardType.TYPE_DRIVING_LICENSE_JIASHI,2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imgIdCard.setVisibility(View.GONE);
            if (requestCode == 0) {
                IdCardInfo info = (IdCardInfo)data.getSerializableExtra(CaptureActivity.BUNDLE_DATA);
                txtInfo.setText(info.toString());
                if (!TextUtils.isEmpty(info.getImageUrl())) {
                    imgIdCard.setVisibility(View.VISIBLE);
                    imgIdCard.setImageBitmap(BitmapFactory.decodeFile(info.getImageUrl()));
                }

            } else if (requestCode == 1){
                BankInfo info = (BankInfo)data.getSerializableExtra(CaptureActivity.BUNDLE_DATA);
                txtInfo.setText(info.toString());
            } else {
                DrivingLicenseInfo info = (DrivingLicenseInfo)data.getSerializableExtra(CaptureActivity.BUNDLE_DATA);
                txtInfo.setText(info.toString());
            }
        }
    }
}
