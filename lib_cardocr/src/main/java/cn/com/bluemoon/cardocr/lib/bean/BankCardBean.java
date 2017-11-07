package cn.com.bluemoon.cardocr.lib.bean;

import java.util.List;

/**
 * 银行卡识别数据
 * Created by liangjiangli on 2017/11/2.
 */

public class BankCardBean extends BaseBean{

    public String session_id;
    public List<ItemsBean> items;

    public static class ItemsBean {
        public String item;
        public ItemcoordBean itemcoord;
        public double itemconf;
        public String itemstring;
        public List<?> coords;
        public List<?> words;
        public List<?> candword;

        public static class ItemcoordBean {
            public int x;
            public int y;
            public int width;
            public int height;
        }
    }
}
