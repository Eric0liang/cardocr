package cn.com.bluemoon.cardocr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.com.bluemoon.cardocr.lib.base.BaseCaptureActivity;
import cn.com.bluemoon.cardocr.lib.common.CardType;

/**
 * 自定义界面
 * Created by liangjiangli on 2017/11/6.
 */

public class CoustomCaptureActivity extends BaseCaptureActivity {

    /**
     * @param context
     * @param type such as CardType.TYPE_ID_CARD_FRONT
     * @param requestCode
     */
    public static void startAction(Activity context, CardType type, int requestCode) {
        Intent intent = new Intent(context, CoustomCaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        intent.putExtra("requestCode", requestCode);
        intent.putExtras(bundle);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_coustom;
    }

    @Override
    public void initCustomView() {
        Button btnBack = (Button)findViewById(R.id.btn_back);
        Button btnTake = (Button)findViewById(R.id.btn_take);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if (id == R.id.btn_back) {
                    finish(); //关闭
                } else if (id == R.id.btn_take) {
                    identification(); //拍照
                }
            }
        };
        btnBack.setOnClickListener(listener);
        btnTake.setOnClickListener(listener);
    }
}
