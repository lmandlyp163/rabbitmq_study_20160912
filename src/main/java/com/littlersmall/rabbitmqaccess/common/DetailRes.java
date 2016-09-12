package com.littlersmall.rabbitmqaccess.common;

/**
 * Created by littlersmall on 16/5/10.
 */
//统一返回值,可描述失败细节
public class DetailRes {
    boolean isSuccess;
    String errMsg;
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public DetailRes(boolean isSuccess, String errMsg) {
		super();
		this.isSuccess = isSuccess;
		this.errMsg = errMsg;
	}
	public DetailRes() {  
		super();  
	}
    
    
}
