package com.umiwi.ui.beans.updatebeans;

import com.umiwi.ui.beans.BaseGsonBeans;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class ColumnMessageCommitBean extends BaseGsonBeans {

    /**
     * e : 9999
     * m : 操作成功
     * r : {"id":"11"}
     */

    private String e;
    private String m;
    private RBean r;

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public RBean getR() {
        return r;
    }

    public void setR(RBean r) {
        this.r = r;
    }

    public static class RBean {
        /**
         * id : 11
         */

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
