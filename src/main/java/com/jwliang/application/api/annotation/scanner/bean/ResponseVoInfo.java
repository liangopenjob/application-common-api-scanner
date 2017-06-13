package com.jwliang.application.api.annotation.scanner.bean;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @ClassName: ResponseVoInfo  
 * @Description: 定义响应vo信息实体bean
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

public class ResponseVoInfo {
	private String name;
	private String simpleName;
	private String description;
	private List<ResponseVoField> fields = new ArrayList<ResponseVoField>();

	public ResponseVoInfo() {

	}

	public ResponseVoInfo(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ResponseVoField> getFields() {
		return fields;
	}

	public void setFields(List<ResponseVoField> fields) {
		this.fields = fields;
	}
	
}
