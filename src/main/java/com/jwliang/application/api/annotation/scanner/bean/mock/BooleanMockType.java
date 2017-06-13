package com.jwliang.application.api.annotation.scanner.bean.mock;

/**
 * 
 * @ClassName: BooleanMockType  
 * @Description: BooleanMock注解信息
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月27日
 *
 */

public class BooleanMockType implements VoFieldMockType {
	
	private String booleanStr;
	private String description;
	/** 
	 * 获取 booleanStr
	 *  
	 * @return booleanStr 
	 */
	public String getBooleanStr() {
		return booleanStr;
	}
	/** 
	 * 设置 booleanStr
	 *  
	 * @param booleanStr 
	 *            booleanStr 
	 */
	public void setBooleanStr(String booleanStr) {
		this.booleanStr = booleanStr;
	}
	/** 
	 * 获取 description
	 *  
	 * @return description 
	 */
	public String getDescription() {
		return description;
	}
	/** 
	 * 设置 description
	 *  
	 * @param description 
	 *            description 
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
}

