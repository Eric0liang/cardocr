package cn.com.bluemoon.cardocr.lib.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by liangjiangli on 2017/11/6.
 */

public class BankInfo implements Serializable {
    private String bankName; //银行信息，农业银行
    private String cardName; //卡名字，金穗通宝卡(银联卡)
    private String cardType; //卡类型，借记卡
    private String cardNumber; //卡号，6228475757548

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(bankName)) {
            stringBuffer.append(bankName);
        }
        if (!TextUtils.isEmpty(cardNumber)) {
            stringBuffer.append("，"+cardNumber);
        }
        if (!TextUtils.isEmpty(cardType)) {
            stringBuffer.append("，"+cardType);
        }
        if (!TextUtils.isEmpty(cardName)) {
            stringBuffer.append("，"+cardName);
        }
        return stringBuffer.toString();
    }
}
