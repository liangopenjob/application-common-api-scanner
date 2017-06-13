package com.jwliang.application.api.annotation.scanner.bean;

import java.util.Map;
import java.util.TreeMap;
/**
 * 
 * @ClassName: ApiGroup  
 * @Description: 定义Api分组信息实体bean
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

public class ApiGroup {
	
	private String group;
	
    private Map<String,ApiResourcesInfo> resourcesMap = new TreeMap<String,ApiResourcesInfo>();
    
    public ApiGroup(){
    	
    }
    
    public ApiGroup(String group){
    	this.group = group;
    }

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Map<String, ApiResourcesInfo> getResourcesMap() {
		return resourcesMap;
	}

	public void setResourcesMap(Map<String, ApiResourcesInfo> resourcesMap) {
		this.resourcesMap = resourcesMap;
	}

}
