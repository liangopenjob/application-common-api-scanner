package com.jwliang.application.api.annotation.scanner.bean.mock;

/**
 * 
 * @ClassName: FloatMockType  
 * @Description: FloatMock注解信息
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月27日
 *
 */

public class FloatMockType implements VoFieldMockType {
	private String floatStr;
    private float start;
    private float end;
    private int decimal;
    private String description;
	/** 
	 * 获取 floatStr
	 *  
	 * @return floatStr 
	 */
	public String getFloatStr() {
		return floatStr;
	}
	/** 
	 * 设置 floatStr
	 *  
	 * @param floatStr 
	 *            floatStr 
	 */
	public void setFloatStr(String floatStr) {
		this.floatStr = floatStr;
	}
	/** 
	 * 获取 start
	 *  
	 * @return start 
	 */
	public float getStart() {
		return start;
	}
	/** 
	 * 设置 start
	 *  
	 * @param start 
	 *            start 
	 */
	public void setStart(float start) {
		this.start = start;
	}
	/** 
	 * 获取 end
	 *  
	 * @return end 
	 */
	public float getEnd() {
		return end;
	}
	/** 
	 * 设置 end
	 *  
	 * @param end 
	 *            end 
	 */
	public void setEnd(float end) {
		this.end = end;
	}
	/** 
	 * 获取 decimal
	 *  
	 * @return decimal 
	 */
	public int getDecimal() {
		return decimal;
	}
	/** 
	 * 设置 decimal
	 *  
	 * @param decimal 
	 *            decimal 
	 */
	public void setDecimal(int decimal) {
		this.decimal = decimal;
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

