package com.umiwi.ui.beans;

import java.io.Serializable;

import com.google.gson.Gson;

/**
 * beans类 基本类，用于gosn
 * 
 * @author tangxiyong 2013-11-21下午6:07:47
 * 
 */
public class BaseGsonBeans implements Serializable{
	public String toJson() {
		return new Gson().toJson(this);
	}

}
