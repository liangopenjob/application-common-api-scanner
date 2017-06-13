package com.jwliang.application.api.annotation.scanner.bean;

import org.apache.commons.lang.StringUtils;

import com.jwliang.application.api.annotation.scanner.bean.mock.VoFieldMockType;
/**
 * 
 * @ClassName: ResponseVoField  
 * @Description: 定义响应vo字段实体bean
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

public class ResponseVoField {
	private String name;
	private String type;  //eg:java.util.List<com.jwliang.application.xapi.web.vo.ConnectedDeviceVo>
	private String simpleType;  //eg:List<com.jwliang.application.xapi.web.vo.ConnectedDeviceVo>
	private String showType;  //eg:List<ConnectedDeviceVo>
	// 是否可为空 0:否,1:是
	private Integer isAllowEmpty = 0;
	// 描述
	private String description = "";
	
	private VoFieldMockType voFieldMockType;

	public ResponseVoField() {

	}

	public ResponseVoField(String name) {
		this.name = name;
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
		//set show type
		if(StringUtils.isNotEmpty(type)){
			StringBuilder showTypeBuilder = new StringBuilder();
			this.genShowType(type, showTypeBuilder);
			showTypeBuilder.deleteCharAt(0);
			showTypeBuilder.deleteCharAt(showTypeBuilder.length()-1);
			this.showType = showTypeBuilder.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		}
		//change special symbol
		if(StringUtils.isNotEmpty(type)){
			type = type.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		}
		this.type = type;
	}

	public String getSimpleType() {
		return simpleType;
	}

	public void setSimpleType(String simpleType) {
		this.simpleType = simpleType;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public Integer getIsAllowEmpty() {
		return isAllowEmpty;
	}

	public void setIsAllowEmpty(Integer isAllowEmpty) {
		this.isAllowEmpty = isAllowEmpty;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	private void genShowType(String typeStr, StringBuilder showType){
		if(-1!=typeStr.indexOf("<")){
			String preStr = typeStr.substring(0, typeStr.indexOf("<"));
			showType.append("<").append(preStr.substring(preStr.lastIndexOf(".")+1));
			String tempTypeStr = typeStr.substring(typeStr.indexOf("<")+1, typeStr.length()-1);
			this.genShowType(tempTypeStr, showType);
			showType.append(">");
		}else{
			if(-1==typeStr.indexOf(".")){
				showType.append("<").append(typeStr).append(">");
    		}else{
    			showType.append("<").append(typeStr.substring(typeStr.lastIndexOf(".")+1)).append(">");
    		}
		}
	}

	public VoFieldMockType getVoFieldMockType() {
		return voFieldMockType;
	}

	public void setVoFieldMockType(VoFieldMockType voFieldMockType) {
		this.voFieldMockType = voFieldMockType;
	}
	
	
}
