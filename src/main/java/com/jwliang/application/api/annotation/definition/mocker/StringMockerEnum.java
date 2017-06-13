package com.jwliang.application.api.annotation.definition.mocker;

/**
 * 
 * @ClassName: StringMockerEnum   
 * @Description: String注解类型
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月27日
 *
 */

public enum StringMockerEnum {
	
	RANDOM_NUM_STR(1001, "数字字符串"),
	RANDOM_LETTER_STR(1002, "52大小写英文字符串"),
	RANDOM_LETTER_UPPER_STR(1003, "52大小写英文字符串(大写)"),
	RANDOM_LETTER_LOWER_STR(1004, "52大小写英文字符串(小写)"),
	RANDOM_ALL_CHAR_STR(1005, "数字+52英文字符串"),
	RANDOM_CH_STR(1006, "中文字符串");
	
    private final int value;

    private final String desc;

    private StringMockerEnum(int value, String desc) {
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

