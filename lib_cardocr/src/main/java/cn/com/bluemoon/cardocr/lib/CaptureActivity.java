package cn.com.bluemoon.cardocr.lib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.bluemoon.cardocr.lib.base.BaseCaptureActivity;
import cn.com.bluemoon.cardocr.lib.common.CardType;


/**
 * 识别身份证照相机
 * Created by liangjiangli on 2017/7/12.
 */

public class CaptureActivity extends BaseCaptureActivity {

    /**
     * @param context
     * @param type        such as CardType.TYPE_ID_CARD_FRONT
     * @param requestCode
     */
    public static void startAction(Activity context, CardType type, int requestCode) {
        Intent intent = new Intent(context, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CARD_TYPE, type);
        intent.putExtras(bundle);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * @param context
     * @param type        such as CardType.TYPE_ID_CARD_FRONT
     * @param requestCode
     * @param titleId
     */
    public static void startAction(Activity context, CardType type, @StringRes int titleId, int requestCode) {
        Intent intent = new Intent(context, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CARD_TYPE, type);
        bundle.putInt(TITLE, titleId);
        intent.putExtras(bundle);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * @param context
     * @param type        such as CardType.TYPE_ID_CARD_FRONT
     * @param requestCode
     * @param url         保存截图文件夹路径
     */
    public static void startAction(Activity context, CardType type, String url, int requestCode) {
        Intent intent = new Intent(context, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CARD_TYPE, type);
        bundle.putString(URL, url);
        intent.putExtras(bundle);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * @param context
     * @param type        such as CardType.TYPE_ID_CARD_FRONT
     * @param requestCode
     * @param titleId
     * @param url         保存截图文件夹路径
     */
    public static void startAction(Activity context, CardType type, @StringRes int titleId, String url, int requestCode) {
        Intent intent = new Intent(context, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CARD_TYPE, type);
        bundle.putInt(TITLE, titleId);
        bundle.putString(URL, url);
        intent.putExtras(bundle);
        context.startActivityForResult(intent, requestCode);
    }


    @Override
    public int getLayoutId() {
        return R.layout.layout_coustom;
    }

    @Override
    public void initCustomView() {
        final ImageView imageBack = (ImageView) findViewById(R.id.image_back);
        final View btnTakePicture = findViewById(R.id.btn_take_picture);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        int titleResourceId;
        if (title == 0) {
            switch (cartType) {
                case TYPE_BANK:
                    titleResourceId = R.string.txt_bank_card_title;
                    break;
                case TYPE_ID_CARD_FRONT:
                    titleResourceId = R.string.txt_id_card_title;
                    break;
                case TYPE_ID_CARD_BACK:
                    titleResourceId = R.string.txt_id_card_title2;
                    break;
                case TYPE_DRIVING_LICENSE_XINGSHI:
                    titleResourceId = R.string.txt_driving_license_xingshi_title;
                    break;
                case TYPE_DRIVING_LICENSE_JIASHI:
                    titleResourceId = R.string.txt_driving_license_jiashi_title;
                    break;
                default:
                    titleResourceId = R.string.txt_id_card_title;
                    break;
            }
            txtTitle.setText(titleResourceId);
        } else {
            txtTitle.setText(title);
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == imageBack) {
                    finish();
                } else if (v == btnTakePicture) {
                    identification();
                }
            }
        };
        imageBack.setOnClickListener(listener);
        btnTakePicture.setOnClickListener(listener);
    }
}
