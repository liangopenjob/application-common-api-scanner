package com.jwliang.application.api.annotation.scanner.bean.mock;

/**
 * 
 * @ClassName: OtherMockType  
 * @Description: OtherMock注解信息
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月27日
 *
 */

public class OtherMockType implements VoFieldMockType {
     private int mockType;
     private String otherStr;
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
	 * 获取 otherStr
	 *  
	 * @return otherStr 
	 */
	public String getOtherStr() {
		return otherStr;
	}
	/** 
	 * 设置 otherStr
	 *  
	 * @param otherStr 
	 *            otherStr 
	 */
	public void setOtherStr(String otherStr) {
		this.otherStr = otherStr;
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

