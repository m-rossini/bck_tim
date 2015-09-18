package br.com.auster.tim.billcheckout.bscs.infobus;

import java.io.Serializable;

public class IBResponseVO implements Serializable {
	private static final long serialVersionUID = 6643748133197018314L;
	
	private int appCode;
	private String data;
	private String TID;
	
	public int getAppCode() {
		return appCode;
	}
	public void setAppCode(int appCode) {
		this.appCode = appCode;
	}

	public String getTID() {
		return TID;
	}
	public void setTID(String tid) {
		TID = tid;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	

}
