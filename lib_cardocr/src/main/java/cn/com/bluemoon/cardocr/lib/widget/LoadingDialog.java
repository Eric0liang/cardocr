package cn.com.bluemoon.cardocr.lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.bluemoon.cardocr.lib.R;


public class LoadingDialog extends Dialog {
    private LinearLayout parentLayout;
    private TextView loadingText;

    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        init(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, R.style.loading_dialog);
        init(context);
    }

    private void init(Context context){
        this.setContentView(LayoutInflater.from(context).inflate(R.layout.loading_dialog, null));
        parentLayout = (LinearLayout) findViewById(R.id.dialog_view);
        loadingText = (TextView) findViewById(R.id.text);
        this.setCancelable(false);
    }

    //设置名字和外层背景色
    public void setText(String text, int color){
        if(!TextUtils.isEmpty(text)) {
            parentLayout.setBackgroundColor(color);
            loadingText.setText(text);
            loadingText.setVisibility(View.VISIBLE);
        }else {
            loadingText.setVisibility(View.GONE);
        }
    }

    public void setText(String text){
        if(!TextUtils.isEmpty(text)) {
            loadingText.setText(text);
            loadingText.setVisibility(View.VISIBLE);
        }else {
            loadingText.setVisibility(View.GONE);
        }
    }
}
