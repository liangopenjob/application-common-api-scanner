package com.jwliang.application.api.annotation.definition.mocker;

/**
 * 
 * @ClassName: OtherMockerEnum   
 * @Description: mock数据配置注解类型
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月27日
 *
 */

public enum OtherMockerEnum {
	
	RANDOM_ID(0001, "身份证ID"),
	RANDOM_PHONE(0002, "手机号"),
	RANDOM_EMAIL(0003, "邮箱"),
	RANDOM_CH_NAME(0004, "中文姓名"),
    RANDOM_ADDRESS(0005, "地址");
	
    private final int value;

    private final String desc;

    private OtherMockerEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }
    
    public String getDesc() {
        return desc;
    }
    
}

