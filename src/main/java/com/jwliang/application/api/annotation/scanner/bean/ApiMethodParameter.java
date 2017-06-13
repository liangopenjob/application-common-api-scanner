package com.jwliang.application.api.annotation.scanner.bean;
/**
 * 
 * @ClassName: ApiMethodParameter  
 * @Description: 定义Api方法参数实体bean
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

public class ApiMethodParameter {
	// 参数名称
	private String name;
	// 参数类型
	private String type;
	// 参数简单类型
	private String simpleType;
	// 是否必选 0:否,1:是
	private Integer isRequired = 0;
	// 描述
	private String description;

	public ApiMethodParameter() {

	}

	public ApiMethodParameter(String name, String type, String simpleType) {
		this.name = name;
		this.type = type;
		this.simpleType = simpleType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSimpleType() {
		return simpleType;
	}

	public void setSimpleType(String simpleType) {
		this.simpleType = simpleType;
	}

	public Integer getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Integer isRequired) {
		this.isRequired = isRequired;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
