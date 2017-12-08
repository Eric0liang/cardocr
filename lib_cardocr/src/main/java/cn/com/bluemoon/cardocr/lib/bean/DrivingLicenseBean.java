package cn.com.bluemoon.cardocr.lib.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tangqiwei on 2017/12/4.
 */

public class DrivingLicenseBean extends BaseBean {
    public List<Item> items;// 识别出的所有字段信息每个字段包括，item,itemcoord,words

    public static class Item {
        public String item;//	字段名称
        public String itemstring;//	字段字符串
        public ItemCoord itemcoord;//	字段在图像中的像素坐标，包括左上角坐标x,y，以及宽、高width, height
        public float itemconf;//	字段置信度(置信度阈值跟具体业务相关)


        public static class ItemCoord {
            public int x;
            public int y;
            public int width;
            public int height;
        }
    }

    public String session_id;//	保留字段，目前不使用

}
