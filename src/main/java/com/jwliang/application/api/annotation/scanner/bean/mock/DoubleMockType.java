package com.jwliang.application.api.annotation.scanner.bean.mock;

/**
 * 
 * @ClassName: DoubleMockType  
 * @Description: DoubleMock注解信息
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月27日
 *
 */

public class DoubleMockType implements VoFieldMockType {
	private String doubleStr;
    private double start;
    private double end;
    private int decimal;
    private String description;
	/** 
	 * 获取 doubleStr
	 *  
	 * @return doubleStr 
	 */
	public String getDoubleStr() {
		return doubleStr;
	}
	/** 
	 * 设置 doubleStr
	 *  
	 * @param doubleStr 
	 *            doubleStr 
	 */
	public void setDoubleStr(String doubleStr) {
		this.doubleStr = doubleStr;
	}
	/** 
	 * 获取 start
	 *  
	 * @return start 
	 */
	public double getStart() {
		return start;
	}
	/** 
	 * 设置 start
	 *  
	 * @param start 
	 *            start 
	 */
	public void setStart(double start) {
		this.start = start;
	}
	/** 
	 * 获取 end
	 *  
	 * @return end 
	 */
	public double getEnd() {
		return end;
	}
	/** 
	 * 设置 end
	 *  
	 * @param end 
	 *            end 
	 */
	public void setEnd(double end) {
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

