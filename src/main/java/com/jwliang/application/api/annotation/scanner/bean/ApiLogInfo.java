package com.jwliang.application.api.annotation.scanner.bean;
/**
 * 
 * @ClassName: ApiLogInfo  
 * @Description: Api变更记录信息
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

public class ApiLogInfo {
	/** 变更记录信息 */
	private String logMsg;
	/** 变更人 */
	private String logPerson;
	/** 变更日期 */
	private String logDate;
	
	public ApiLogInfo(){
		
	}
	
	public ApiLogInfo(String logMsg, String logPerson, String logDate){
		this.logMsg = logMsg;
		this.logPerson = logPerson;
		this.logDate = logDate;
	}

	public String getLogMsg() {
		return logMsg;
	}

	public void setLogMsg(String logMsg) {
		this.logMsg = logMsg;
	}

	public String getLogPerson() {
		return logPerson;
	}

	public void setLogPerson(String logPerson) {
		this.logPerson = logPerson;
	}

	public String getLogDate() {
		return logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}
}
