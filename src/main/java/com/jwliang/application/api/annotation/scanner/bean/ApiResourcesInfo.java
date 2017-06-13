package com.jwliang.application.api.annotation.scanner.bean;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @ClassName: ApiResourcesInfo  
 * @Description: 定义Api资源分组实体bean
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

public class ApiResourcesInfo {
	
	private String name;
	
	private List<ApiMethodAnnotationInfo> methods = new ArrayList<ApiMethodAnnotationInfo>();
	
	public ApiResourcesInfo(){
		
	}
	
	public ApiResourcesInfo(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ApiMethodAnnotationInfo> getMethods() {
		return methods;
	}

	public void setMethods(List<ApiMethodAnnotationInfo> methods) {
		this.methods = methods;
	}
    
}
