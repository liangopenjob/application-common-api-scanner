package com.jwliang.application.api.annotation.scanner.bean.mock;

/**
 * 
 * @ClassName: StringMockType  
 * @Description: StringMock注解信息
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月27日
 *
 */

public class StringMockType implements VoFieldMockType {
	
	private int mockType;
	private String stringStr;
	private int length;
	private String description;
	/** 
	 * 获取 mockType
	 *  
	 * @return mockType 
	 */
	public int getMockType() {
		return mockType;
	}
	/** 
	 * 设置 mockType
	 *  
	 * @param mockType 
	 *            mockType 
	 */
	public void setMockType(int mockType) {
		this.mockType = mockType;
	}
	/** 
	 * 获取 stringStr
	 *  
	 * @return stringStr 
	 */
	public String getStringStr() {
		return stringStr;
	}
	/** 
	 * 设置 stringStr
	 *  
	 * @param stringStr 
	 *            stringStr 
	 */
	public void setStringStr(String stringStr) {
		this.stringStr = stringStr;
	}
	/** 
	 * 获取 length
	 *  
	 * @return length 
	 */
	public int getLength() {
		return length;
	}
	/** 
	 * 设置 length
	 *  
	 * @param length 
	 *            length 
	 */
	public void setLength(int length) {
		this.length = length;
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

