
package com.jwliang.application.api.annotation.definition.mocker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName: ByteMock   
 * @Description: byte类型mock注解
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月27日
 *
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ByteMock {
	String byteStr() default "";//可选byte数据范围,如"1,5,9,8"
	String description() default "";
}

