package com.jwliang.application.api.annotation.scanner.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @ClassName: ResponseVo  
 * @Description: 定义统一返回的数据类型
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVo<T> implements Serializable {

	private static final long serialVersionUID = 3073226328007685525L;

	private long status;

	private String message;

	private T data;

	public ResponseVo() {

	}

	public ResponseVo(long status, String message) {
		this.status = status;
		this.message = message;
	}

	public ResponseVo(long status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public ResponseVo(ReturnStatus returnStatus, T data) {
		this.status = returnStatus.getValue();
		this.message = returnStatus.getDesc();
		this.data = data;
	}

	public ResponseVo(ReturnStatus returnStatus) {
		this.status = returnStatus.getValue();
		this.message = returnStatus.getDesc();
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@JsonIgnore
	public boolean isSuccess() {
		return getStatus() == 200;
	}
}
