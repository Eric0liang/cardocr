package cn.com.bluemoon.cardocr.lib.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by tangqiwei on 2017/12/4.
 */

public class DrivingLicenseInfo implements Serializable {
    private String licenseNumber;//车牌号码
    private String vehicleType;//车辆类型
    private String master;//所有人
    private String address;//住址
    private String function;//使用性质
    private String brandModel;//品牌型号
    private String identifyCode;//识别代码
    private String engineNumber;//发动机号
    private String registrationDate;//注册日期
    private String openingDate;//发证日期


    private String certificateNumber;//证号
    private String name;//姓名
    private String gender;//性别
    private String nationality;//国籍
    private String dateBirth;//出生日期
    private String quasiDrivingType;//准驾车型
    private String effectiveDate;//有效日期
    private String startDate;//起始日期

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public String getIdentifyCode() {
        return identifyCode;
    }

    public void setIdentifyCode(String identifyCode) {
        this.identifyCode = identifyCode;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getQuasiDrivingType() {
        return quasiDrivingType;
    }

    public void setQuasiDrivingType(String quasiDrivingType) {
        this.quasiDrivingType = quasiDrivingType;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (!TextUtils.isEmpty(licenseNumber)) {
            buffer.append(licenseNumber);
            if (!TextUtils.isEmpty(vehicleType)) {
                buffer.append(","+vehicleType);
            }
            if (!TextUtils.isEmpty(master)) {
                buffer.append(","+master);
            }
            if (!TextUtils.isEmpty(address)) {
                buffer.append(","+address);
            }
            if (!TextUtils.isEmpty(function)) {
                buffer.append(","+function);
            }
            if (!TextUtils.isEmpty(brandModel)) {
                buffer.append(","+brandModel);
            }
            if (!TextUtils.isEmpty(identifyCode)) {
                buffer.append(","+identifyCode);
            }
            if (!TextUtils.isEmpty(engineNumber)) {
                buffer.append(","+engineNumber);
            }
            if (!TextUtils.isEmpty(registrationDate)) {
                buffer.append(","+registrationDate);
            }
            if (!TextUtils.isEmpty(openingDate)) {
                buffer.append(","+openingDate);
            }
        } else if (!TextUtils.isEmpty(certificateNumber)) {
            buffer.append(certificateNumber);
            if (!TextUtils.isEmpty(name)) {
                buffer.append(","+name);
            }
            if (!TextUtils.isEmpty(gender)) {
                buffer.append(","+gender);
            }
            if (!TextUtils.isEmpty(nationality)) {
                buffer.append(","+nationality);
            }
            if (!TextUtils.isEmpty(address)) {
                buffer.append(","+address);
            }
            if (!TextUtils.isEmpty(dateBirth)) {
                buffer.append(","+dateBirth);
            }
            if (!TextUtils.isEmpty(openingDate)) {
                buffer.append(","+openingDate);
            }
            if (!TextUtils.isEmpty(quasiDrivingType)) {
                buffer.append(","+quasiDrivingType);
            }
            if (!TextUtils.isEmpty(effectiveDate)) {
                buffer.append(","+effectiveDate);
            }
            if (!TextUtils.isEmpty(startDate)) {
                buffer.append(","+startDate);
            }
        }
        return buffer.toString();
    }
}
