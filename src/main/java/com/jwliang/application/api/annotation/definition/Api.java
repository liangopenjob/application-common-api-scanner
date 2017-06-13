package com.jwliang.application.api.annotation.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @ClassName: Api  
 * @Description: 标注此注解可以被扫描
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Api {
	
	/** 标题  */
	String title() default "";
	/** 分组名称  */
	String group() default "";
	/** 描述  */
	String description() default "";
	
}
