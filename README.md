# cardocr 身份证、银行卡、行驶证、驾驶证识别

**使用前请阅读对应模块的文档和示例，如果有不清楚的地方，可以看源码，或者向我提问。觉得有帮助的，星星点起来！！！**

这个库的底层是使用[腾讯优图云平台](http://open.youtu.qq.com/)识别技术，所以引用包非常小，识别速度大概几秒。

## 集成
### permission与meta-data声明
```groovy
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    
    <meta-data
            android:name="OCR_APP_KEY"
            android:value="腾讯优图申请" />
    <meta-data
            android:name="OCR_SECRET_ID"
            android:value="腾讯优图申请" />
    <meta-data
            android:name="OCR_SECRET_KEY"
            android:value="腾讯优图申请" />
            
```
### Gradle
```groovy
    allprojects {
        jcenter()
    }
    
```

```groovy
    compile 'com.github.eric0liang:lib_cardocr:1.0.5'
```
### 依赖的jar添加到libs
[fastjson.jar](https://raw.githubusercontent.com/Eric0liang/cardocr/master/app/libs/fastjson-1.2.6.jar) 用于解析腾讯云平台response的json

[BASE64Decoder.jar](https://raw.githubusercontent.com/Eric0liang/cardocr/master/app/libs/sun.misc.BASE64Decoder.jar) 用于解码腾讯云平台response base64格式的截图

### 混淆
```groovy
-dontwarn cn.com.bluemoon.cardocr.**
-keep class cn.com.bluemoon.cardocr.** {*;}
```

## demo apk下载地址: 
[点击下载](https://raw.githubusercontent.com/Eric0liang/cardocr/master/app-debug.apk)

## 效果
### 1.身份证识别
<img src="https://github.com/Eric0liang/cardocr/blob/master/images/7.jpg"/></br>
<img src="https://github.com/Eric0liang/cardocr/blob/master/images/5.png" width="280px"/>  <img src="https://github.com/Eric0liang/cardocr/blob/master/images/4.png" width="280px"/></br>
### 2.银行卡识别
<img src="https://github.com/Eric0liang/cardocr/blob/master/images/9.jpg"/></br>
<img src="https://github.com/Eric0liang/cardocr/blob/master/images/3.png" width="300px"/></br>
### 3.行驶证识别
<img src="https://github.com/Eric0liang/cardocr/blob/master/images/8.jpg"/></br>
<img src="https://github.com/Eric0liang/cardocr/blob/master/images/2.png" width="300px"/>
### 4.驾驶证识别
<img src="https://github.com/Eric0liang/cardocr/blob/master/images/10.jpg"/></br>
<img src="https://github.com/Eric0liang/cardocr/blob/master/images/1.png" width="300px"/>
## 使用指南（2018.1.23更新）

### CaptureActivity 识别身份证、银行卡照相机类

#### API
startAction(Activity context, CardType type, int requestCode) </br>
startAction(Activity context, CardType type, String url, int requestCode) </br>
startAction(Activity context, CardType type, @StringRes int titleId, int requestCode)</br>

* context 调起照相机的activity类
* type 枚举类，有五个类型
```groovy
   public enum CardType {
    //身份证头像面,身份证国徽面,银行卡,行驶证,驾驶证
    TYPE_ID_CARD_FRONT, TYPE_ID_CARD_BACK,TYPE_BANK,TYPE_DRIVING_LICENSE_XINGSHI,TYPE_DRIVING_LICENSE_JIASHI
}
```
* titleId 自定义照相机顶部的title，比如<string name="txt_id_card_title">请确保身份证头像面边缘在框内</string>
* url 是否需要保存身份证的截图，传保存的文件夹路径，比如Environment.getExternalStorageDirectory() + "/images"
* requestCode onActivityResult使用

#### 使用demo MainActivity代码片段
```groovy
    @Override
    public void onClick(View v) {
        String dirPath = Environment.getExternalStorageDirectory() + "/images";
        switch (v.getId()) {
            case R.id.btn_id_card_back:
                CaptureActivity.startAction(this, CardType.TYPE_ID_CARD_BACK, checkbox.isChecked() ? dirPath : null, 0);
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
```
#### IdCardInfo

    private String authority; //签发机关，XXX公安局
    private String validDate; //有效期限，2007.02.14-2017.02.14
    private String imageUrl; //截图保存地址，预留
    private String name; //姓名，艾米
    private String sex; //性别，女
    private String nation; //民族，汉
    private String birth; //出生，1990/12/22
    private String address; //住址，浙江省海盐县武原街工人路
    private String id; //公民身份号码，610333199012223323

#### BankInfo

    private String bankName; //银行信息，农业银行
    private String cardName; //卡名字，金穗通宝卡(银联卡)
    private String cardType; //卡类型，借记卡
    private String cardNumber; //卡号，6228475757548
    
#### DrivingLicenseInfo
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

### 识别照相机界面，默认样式如下，也可自定义（参考CoustomCaptureActivity类）
<img src="https://github.com/Eric0liang/cardocr/blob/master/images/6.png" width="400px"/>

如果想自定义识别照相机界面，继承BaseCaptureActivity，并重写getLayoutId，initCustomView两个方法即可。
```groovy
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
                    identification(); //拍照识别
                }
            }
        };
        btnBack.setOnClickListener(listener);
        btnTake.setOnClickListener(listener);
    }
```

## 更新记录
- **1.0.5** 2018.8.21
    * 修复8.0奔溃问题
- **1.0.4** 2018.1.23
    * 优化行驶证、驾驶证识别
    * 添加使用说明
- **1.0.3, 1.0.2** 2017.12.8
    * 优化代码
- **1.0.1** 2017.12.8
    * 修复连续拍照触发的奔溃
    * 增加行驶证、驾驶证识别
- **1.0.0** 2017.11.7
    * first commit

### 其它问题

发现任何问题可以提issue

------

## 关于作者

#### 联系方式

* Github: <https://github.com/Eric0liang>
* Email: [liangjiangli@bluemoon.com.cn](mailto:liangjiang@bluemoon.com.cn)

------

## License

    Copyright 2017 - 2019 Jiangli Liang

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.





