package com.jwliang.application.api.annotation.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @ClassName: ApiAuth  
 * @Description: 标注Api权限相关信息
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiAuth {
   
	/** 作者 */
   String author() default "";
    /** 标注接口是否需要展示在页面 */
   boolean show() default true;
    /** 表述接口是否下架 */
   boolean deprecated() default false;
   
}
