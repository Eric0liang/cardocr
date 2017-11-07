package cn.com.bluemoon.cardocr.lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.bluemoon.cardocr.lib.R;

public class CommonAlertDialog extends Dialog {

    public CommonAlertDialog(Context context) {
        super(context);
    }

    public CommonAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private Drawable icon;
        private String message;
        private Spanned messageSpanned;
        private int txtSize = -1;
        private int positiveButtonTextColor = -1;
        private int negativeButtonTextColor = -1;
        private int positiveButtonBg = -1;
        private int negativeButtonBg = -1;
        private int mainBg = -1;
        private String positiveButtonText;
        private String negativeButtonText;
        private boolean isCancelable = true;
        private boolean isDismissable = true;
        private boolean isClearPadding;
        private int titleGravity = -1;
        private int msgGravity = -1;
        private int txtGravity = -1;
        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitleGravity(int titleGravity) {
            this.titleGravity = titleGravity;
            return this;
        }

        public Builder setTitleIcon(Drawable icon) {
            this.icon = icon;
            return this;
        }

        public Builder setMessageGravity(int msgGravity) {
            this.msgGravity = msgGravity;
            return this;
        }

        public Builder setTxtGravity(int txtGravity) {
            this.txtGravity = txtGravity;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        // 增加显示html文本方法
        public Builder setMessage(Spanned message) {
            this.messageSpanned = message;
            return this;
        }

        public Builder setMessageSize(int size) {
            this.txtSize = size;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        public Builder setDismissable(boolean isDismissable) {
            this.isDismissable = isDismissable;
            return this;
        }

        public void setClearPadding(boolean isClearPadding) {
            this.isClearPadding = isClearPadding;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * 设置左边按钮的颜色
         *
         * @param color getColor(R.color.xxx)
         */
        public Builder setPositiveButtonTextColor(int color) {
            positiveButtonTextColor = color;
            return this;
        }

        /**
         * 设置右边按钮的颜色
         *
         * @param color getColor(R.color.xxx)
         */
        public Builder setNegativeButtonTextColor(int color) {
            negativeButtonTextColor = color;
            return this;
        }

        /**
         * 设置左边按钮的背景
         *
         * @param bg 如R.drawable.xxx
         */
        public Builder setPositiveButtonBg(int bg) {
            positiveButtonBg = bg;
            return this;
        }

        /**
         * 设置右边按钮的背景
         *
         * @param bg 如R.drawable.xxx
         */
        public Builder setNegativeButtonBg(int bg) {
            negativeButtonBg = bg;
            return this;
        }

        /**
         * 设置背景
         *
         * @param bg 如R.drawable.xxx
         */
        public Builder setMainBg(int bg) {
            mainBg = bg;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CommonAlertDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CommonAlertDialog dialog = new CommonAlertDialog(context,
                    R.style.Dialog);

            if (isCancelable == false)
                dialog.setCancelable(isCancelable);
            View layout = inflater.inflate(R.layout.dialog_common, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            View llMian = layout.findViewById(R.id.ll_main);
            if (mainBg != -1) {
                llMian.setBackgroundResource(mainBg);
            }

            if (title != null) {
                TextView txtTitle = ((TextView) layout.findViewById(R.id.title));
                txtTitle.setText(title);
                if (titleGravity != -1) {
                    ((LinearLayout) layout.findViewById(R.id.layout_title))
                            .setGravity(titleGravity);
                }
                if (icon != null) {
                    txtTitle.setCompoundDrawablesWithIntrinsicBounds(icon,
                            null, null, null);
                }
            } else {
                (layout.findViewById(R.id.layout_title))
                        .setVisibility(View.GONE);
            }

            if (positiveButtonText != null) {
                Button positiveBtn = ((Button) layout
                        .findViewById(R.id.positiveButton));
                positiveBtn.setText(positiveButtonText);
                if (positiveButtonTextColor != -1) {
                    positiveBtn.setTextColor(positiveButtonTextColor);
                }
                if (positiveButtonBg != -1) {
                    positiveBtn.setBackgroundResource(positiveButtonBg);
                }

                if (negativeButtonText == null) {
                    positiveBtn.setBackgroundResource(R.drawable.dialog_btn_white);
                }
                positiveBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (positiveButtonClickListener != null) {
                            positiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                            if (isDismissable) {
                                dialog.dismiss();
                            }
                        } else {
                            dialog.dismiss();
                        }

                    }
                });
            } else {
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
                layout.findViewById(R.id.line_btn).setVisibility(View.GONE);
            }
            if (negativeButtonText != null) {
                Button negativeBtn = ((Button) layout
                        .findViewById(R.id.negativeButton));
                negativeBtn.setText(negativeButtonText);

                if (negativeButtonTextColor != -1) {
                    negativeBtn.setTextColor(negativeButtonTextColor);
                }

                if (negativeButtonBg != -1) {
                    negativeBtn.setBackgroundResource(negativeButtonBg);
                }

                if (positiveButtonText == null) {
                    negativeBtn.setBackgroundResource(R.drawable.dialog_btn_white);
                }
                negativeBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (negativeButtonClickListener != null) {
                            negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                            if (isDismissable) {
                                dialog.dismiss();
                            }
                        } else {
                            dialog.dismiss();
                        }
                    }
                });
            } else {
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
                layout.findViewById(R.id.line_btn).setVisibility(View.GONE);
            }
            if (positiveButtonText == null && negativeButtonText == null) {
                layout.findViewById(R.id.line_bottom).setVisibility(View.GONE);
            }
            if (message != null || contentView != null) {
                LinearLayout lin_content = (LinearLayout) layout
                        .findViewById(R.id.content);
                if (msgGravity != -1)
                    lin_content.setGravity(msgGravity);
                if (contentView != null) {
                    if (isClearPadding) {
                        lin_content.setPadding(0, lin_content.getPaddingTop(),
                                0, 0);
                    }
                    lin_content.removeAllViews();
                    lin_content.addView(contentView, new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT));
                } else if (message != null) {
                    TextView txtMessage = ((TextView) layout
                            .findViewById(R.id.message));
                    if (message != null) {
                        txtMessage.setText(message);
                        if (txtSize != -1) {
                            txtMessage.setTextSize(txtSize);
                        }
                        if (txtGravity != -1) {
                            txtMessage.setGravity(txtGravity);
                        }
                    } else {
                        txtMessage.setVisibility(View.GONE);
                    }
                }
            } else if (messageSpanned != null || contentView != null) {
                /**
                 * message中的message中文字用html显示不同的颜色
                 */
                LinearLayout lin_content = (LinearLayout) layout
                        .findViewById(R.id.content);
                if (msgGravity != -1)
                    lin_content.setGravity(msgGravity);
                if (contentView != null) {
                    lin_content.removeAllViews();
                    lin_content
                            .addView(contentView, new LayoutParams(
                                    LayoutParams.FILL_PARENT,
                                    LayoutParams.FILL_PARENT));
                } else if (messageSpanned != null) {
                    TextView txtMessage = ((TextView) layout
                            .findViewById(R.id.message));
                    if (messageSpanned != null) {
                        txtMessage.setText(messageSpanned);
                        if (txtSize != -1) {
                            txtMessage.setTextSize(txtSize);
                        }
                        if (txtGravity != -1) {
                            txtMessage.setGravity(txtGravity);
                        }
                    } else {
                        txtMessage.setVisibility(View.GONE);
                    }
                }
            }
            dialog.setContentView(layout);
            return dialog;
        }

        public CommonAlertDialog show() {

            try {
                CommonAlertDialog dialog = create();
                dialog.show();
                return dialog;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new CommonAlertDialog(context);

        }

    }
}
