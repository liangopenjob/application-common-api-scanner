package com.jwliang.application.api.annotation.scanner.bean;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @ClassName: ApiClassAnnotationInfo  
 * @Description: 定义Api信息实体bean
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

public class ApiClassAnnotationInfo {
	
	private Integer id;
	private String apiClass;// api class name
	private String uri;// api class mapping
	private String title;// api class note
	private String group;// api class group
	private List<ApiMethodAnnotationInfo> apiList = new ArrayList<ApiMethodAnnotationInfo>();// store api
	
	public ApiClassAnnotationInfo(){
		
	}
	
	public ApiClassAnnotationInfo(String apiClass){
		this.apiClass = apiClass;
	}
	
	public ApiClassAnnotationInfo(Integer id, String apiClass){
		this.id = id;
		this.apiClass = apiClass;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApiClass() {
		return apiClass;
	}

	public void setApiClass(String apiClass) {
		this.apiClass = apiClass;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<ApiMethodAnnotationInfo> getApiList() {
		return apiList;
	}

	public void setApiList(List<ApiMethodAnnotationInfo> apiList) {
		this.apiList = apiList;
	}

}
