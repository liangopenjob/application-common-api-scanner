package com.jwliang.application.api.annotation.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @ClassName: ApiStatus  
 * @Description: 标注Api响应状态码信息注解
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiStatus {
	
	/** 状态码 */
	int code();
    /** 描述信息 */
    String message();
}
