package com.jwliang.application.api.annotation.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @ClassName: ApiLog  
 * @Description: 定义Api变更记录注解
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiLog {
	
	/** 变更记录 */
	String logMsg();
	/** 变更人 */
	String logPerson();
    /** 变更日期 */
    String logDate();

}
