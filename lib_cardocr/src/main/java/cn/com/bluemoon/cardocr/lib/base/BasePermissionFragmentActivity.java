package cn.com.bluemoon.cardocr.lib.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import cn.com.bluemoon.cardocr.lib.utils.PermissionsUtil;
import cn.com.bluemoon.cardocr.lib.widget.CommonAlertDialog;
import cn.com.bluemoon.cardocr.lib.R;


/**
 * 基础Activity，实现了权限的一些公共方法
 * Created by ljl on 2017/7/27.
 */
public abstract class BasePermissionFragmentActivity extends FragmentActivity {

    protected int PERMISSION_REQUEST_CODE = 0x333;
    private CommonAlertDialog dialog;
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    /**
     * 获取授权列表
     */
    protected String[] getPermissionGroup() {
        return null;
    }

    /**
     * 做需要权限的操作，比如打开照相机
     */
    protected void doPermissionAction() {

    }

    /**
     * 权限判断
     * return 是否需要执行下一步
     */
    final protected void checkAndDoAction() {
        String[] mPermissionGroup = getPermissionGroup();
        if (!(mPermissionGroup != null && mPermissionGroup.length > 0
                && Build.VERSION.SDK_INT >= 23
                && !handPermission(mPermissionGroup, this, PERMISSION_REQUEST_CODE))) {
            //6.0.0以下权限判断，或者6.0以上但是有禁止权限选择的手机
            doActionCatch();
        }
    }

    private void showPermissonDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = null;
        }
        CommonAlertDialog.Builder builder = new CommonAlertDialog.Builder(this);
        builder.setTitle(R.string.help);
        builder.setMessage(R.string.string_help_text);
        builder.setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= 23) {
            builder.setNegativeButton(R.string.settings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    PermissionsUtil.startAppSettings(BasePermissionFragmentActivity.this);
                }
            });
        }
        builder.setCancelable(false);
        dialog = builder.show();
    }

    /**
     * 6。0以上权限判断
     * @param mPermissionGroup
     * @param context
     * @param requestCode
     * @return
     */
    private boolean handPermission(String[] mPermissionGroup, Activity context, int requestCode) {
        // 过滤已持有的权限
        List<String> mRequestList = new ArrayList<>();
        for (String permission : mPermissionGroup) {
            if ((ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED)) {
                mRequestList.add(permission);
            }
        }
        // 申请未持有的权限
        if (Build.VERSION.SDK_INT >= 23 && !mRequestList.isEmpty()) {
            ActivityCompat.requestPermissions(context, mRequestList.toArray(
                    new String[mRequestList.size()]), requestCode);
        } else {
            // 权限都有了，就可以继续后面的操作
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults != null && grantResults.length > 0 && requestCode == PERMISSION_REQUEST_CODE) {
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    showPermissonDialog();
                    return;
                }
            }
            doActionCatch();
        }
    }

    private void doActionCatch() {
        try {
            doPermissionAction();
        } catch (Exception e) {
            showPermissonDialog();
        }
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
        super.onDestroy();
    }
}
