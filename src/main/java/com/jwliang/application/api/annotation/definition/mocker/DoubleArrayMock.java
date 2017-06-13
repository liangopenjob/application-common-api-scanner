package com.jwliang.application.api.annotation.definition.mocker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName: DoubleArrayMock   
 * @Description: double类型mock注解
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月27日
 *
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleArrayMock {
	String doubleStr() default "";
	double start() default 0D;
	double end() default 1D;  //[start,end)区间浮点数
	int decimal() default 2; //保留小数位数
	int arrayLength() default 3;
	String description() default "";
}

