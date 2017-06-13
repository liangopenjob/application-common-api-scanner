package com.jwliang.application.api.annotation.scanner.bean;
/**
 * 
 * @ClassName: ApiStatusInfo  
 * @Description: 定义Api方法状态码实体bean
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

public class ApiStatusInfo {
	private int code;

    private String message;
    
    public ApiStatusInfo(){
    	
    }
    
    public ApiStatusInfo(int code, String message){
    	this.code = code;
    	this.message = message;
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
