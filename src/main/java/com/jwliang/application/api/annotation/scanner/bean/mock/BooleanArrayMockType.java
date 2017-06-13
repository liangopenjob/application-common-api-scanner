package com.jwliang.application.api.annotation.scanner.bean.mock;

/**
 * 
 * @ClassName: BooleanArrayMockType  
 * @Description: BooleanArrayMock注解信息
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月27日
 *
 */

public class BooleanArrayMockType implements VoFieldMockType {
	
	private String booleanStr;
	private int arrayLength;
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
	 * 获取 arrayLength
	 *  
	 * @return arrayLength 
	 */
	public int getArrayLength() {
		return arrayLength;
	}
	/** 
	 * 设置 arrayLength
	 *  
	 * @param arrayLength 
	 *            arrayLength 
	 */
	public void setArrayLength(int arrayLength) {
		this.arrayLength = arrayLength;
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

