package cn.com.bluemoon.cardocr.lib.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by liangjiangli on 2017/11/6.
 */

public class BankInfo implements Serializable {
    private String bankInfo; //银行信息，农业银行
    private String bankName; //卡名字，金穗通宝卡(银联卡)
    private String bankType; //卡类型，借记卡
    private String bankNumber; //卡号，6228475757548

    public String getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(String bankInfo) {
        this.bankInfo = bankInfo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(bankInfo)) {
            stringBuffer.append(bankInfo);
        }
        if (!TextUtils.isEmpty(bankNumber)) {
            stringBuffer.append("，"+bankNumber);
        }
        if (!TextUtils.isEmpty(bankType)) {
            stringBuffer.append("，"+bankType);
        }
        if (!TextUtils.isEmpty(bankName)) {
            stringBuffer.append("，"+bankName);
        }
        return stringBuffer.toString();
    }
}
