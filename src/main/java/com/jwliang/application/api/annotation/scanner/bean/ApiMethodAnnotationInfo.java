package com.jwliang.application.api.annotation.scanner.bean;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @ClassName: ApiMethodAnnotationInfo  
 * @Description: 定义Api方法信息实体bean
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

public class ApiMethodAnnotationInfo {

	private Integer id;
	private Integer classId;// api class id
	private String uri;// api method mapping
	private String classUri; //parent uri
	private String title;// api method note
	private String method; // api method type
	private boolean show; // whether show the api info
	private String author = ""; // the api author
	private boolean deprecated; // api is use or not
	private List<ApiStatusInfo> apiStatuses = new ArrayList<ApiStatusInfo>(); // api status info
	private List<ApiLogInfo> apiLogs = new ArrayList<ApiLogInfo>(); // api change logs
	// api method parameters
	List<ApiMethodParameter> parameters = new ArrayList<ApiMethodParameter>();
	// return type string
	private String returnType;
	// response date T string
	private String returnDataType;
	// return class type string
	private List<ResponseVoInfo> returnClsList = new ArrayList<ResponseVoInfo>();
	// this api remark
	private String remark = "";

	public ApiMethodAnnotationInfo() {

	}

	public ApiMethodAnnotationInfo(Integer id) {
		this.id = id;
	}

	public ApiMethodAnnotationInfo(Integer id, Integer classId) {
		this.id = id;
		this.classId = classId;
	}

	public Integer getId() {
		return id;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getClassUri() {
		return classUri;
	}

	public void setClassUri(String classUri) {
		this.classUri = classUri;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public List<ApiStatusInfo> getApiStatuses() {
		return apiStatuses;
	}

	public void setApiStatuses(List<ApiStatusInfo> apiStatuses) {
		this.apiStatuses = apiStatuses;
	}

	public List<ApiLogInfo> getApiLogs() {
		return apiLogs;
	}

	public void setApiLogs(List<ApiLogInfo> apiLogs) {
		this.apiLogs = apiLogs;
	}

	public List<ApiMethodParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<ApiMethodParameter> parameters) {
		this.parameters = parameters;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		String returnDataType = returnType;
		if(-1!=returnType.indexOf("<")){
			returnDataType = returnType.substring(returnType.indexOf("<")+1, returnType.length()-1);
			returnDataType = returnDataType.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		}
		this.setReturnDataType(returnDataType);
		if(-1!=returnType.indexOf("<")){
			returnType = returnType.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		}
		this.returnType = returnType;
	}
	
	public String getReturnDataType() {
		return returnDataType;
	}

	public void setReturnDataType(String returnDataType) {
		this.returnDataType = returnDataType;
	}

	public List<ResponseVoInfo> getReturnClsList() {
		return returnClsList;
	}

	public void setReturnClsList(List<ResponseVoInfo> returnClsList) {
		this.returnClsList = returnClsList;
	}

	/** 
	 * 获取 remark
	 *  
	 * @return remark 
	 */
	public String getRemark() {
		return remark;
	}

	/** 
	 * 设置 remark
	 *  
	 * @param remark 
	 *            remark 
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
