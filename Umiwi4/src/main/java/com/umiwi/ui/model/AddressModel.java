package com.umiwi.ui.model;

import cn.youmi.framework.model.BaseModel;

/**
 * Created by txy on 15/9/17.
 */
public class AddressModel extends BaseModel {

    private String userName;
    private String moblie;
    private String address;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMoblie(String moblie) {
        this.moblie = moblie;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {

        return userName;
    }

    public String getMoblie() {
        return moblie;
    }

    public String getAddress() {
        return address;
    }
}
