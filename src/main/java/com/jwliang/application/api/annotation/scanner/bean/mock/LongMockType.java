package com.jwliang.application.api.annotation.scanner.bean.mock;

/**
 * 
 * @ClassName: LongMockType  
 * @Description: LongMock注解信息
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月27日
 *
 */

public class LongMockType implements VoFieldMockType {
	private String longStr;
    private String description;
	/** 
	 * 获取 longStr
	 *  
	 * @return longStr 
	 */
	public String getLongStr() {
		return longStr;
	}
	/** 
	 * 设置 longStr
	 *  
	 * @param longStr 
	 *            longStr 
	 */
	public void setLongStr(String longStr) {
		this.longStr = longStr;
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

