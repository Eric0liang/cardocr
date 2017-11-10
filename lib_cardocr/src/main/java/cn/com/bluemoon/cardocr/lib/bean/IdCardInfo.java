package cn.com.bluemoon.cardocr.lib.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by liangjiangli on 2017/11/6.
 */

public class IdCardInfo implements Serializable {
    private String authority; //签发机关，XXX公安局
    private String validDate; //有效期限，2007.02.14-2017.02.14
    private String imageUrl; //截图保存地址，预留
    private String name; //姓名，艾米
    private String sex; //性别，女
    private String nation; //民族，汉
    private String birth; //出生，1990/12/22
    private String address; //住址，浙江省海盐县武原街工人路
    private String id; //公民身份号码，610333199012223323

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(authority)) {
            stringBuffer.append(authority);
        }
        if (!TextUtils.isEmpty(validDate)) {
            stringBuffer.append("，"+validDate);
        }
        if (!TextUtils.isEmpty(name)) {
            stringBuffer.append(name);
        }
        if (!TextUtils.isEmpty(id)) {
            stringBuffer.append("，"+id);
        }
        if (!TextUtils.isEmpty(sex)) {
            stringBuffer.append("，"+sex);
        }
        if (!TextUtils.isEmpty(nation)) {
            stringBuffer.append("，"+nation);
        }
        if (!TextUtils.isEmpty(birth)) {
            stringBuffer.append("，"+birth);
        }
        if (!TextUtils.isEmpty(address)) {
            stringBuffer.append("，"+address);
        }
        return stringBuffer.toString();
    }
}
