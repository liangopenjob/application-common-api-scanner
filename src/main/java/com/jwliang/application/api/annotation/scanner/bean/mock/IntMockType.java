package com.jwliang.application.api.annotation.scanner.bean.mock;

/**
 * 
 * @ClassName: IntMockType  
 * @Description: IntMock注解信息
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月27日
 *
 */

public class IntMockType implements VoFieldMockType {
	
	private String intStr;
	private String description;
	/** 
	 * 获取 intStr
	 *  
	 * @return intStr 
	 */
	public String getIntStr() {
		return intStr;
	}
	/** 
	 * 设置 intStr
	 *  
	 * @param intStr 
	 *            intStr 
	 */
	public void setIntStr(String intStr) {
		this.intStr = intStr;
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

