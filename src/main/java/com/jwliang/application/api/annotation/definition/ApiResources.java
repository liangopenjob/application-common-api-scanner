package com.jwliang.application.api.annotation.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @ClassName: ApiResources  
 * @Description: 标注Api资源组信息
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResources {
	
	/** 资源组名称 */
    String name() default "";
    
}
