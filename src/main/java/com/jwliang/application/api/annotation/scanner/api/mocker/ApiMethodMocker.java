package com.jwliang.application.api.annotation.scanner.api.mocker;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.jwliang.application.api.annotation.scanner.AnnotationClassScanner;
import com.jwliang.application.api.annotation.scanner.api.DocsApi;
import com.jwliang.application.api.annotation.scanner.bean.ApiClassAnnotationInfo;
import com.jwliang.application.api.annotation.scanner.bean.ApiMethodAnnotationInfo;
import com.jwliang.application.api.annotation.scanner.common.ResponseVo;
import com.jwliang.application.api.annotation.scanner.service.ApiScannerService;

/**
 * 
 * @ClassName: ApiMethodMocker 
 * @Description: 以方法拦截器形式模拟请求数据
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月22日
 *
 */
public class ApiMethodMocker implements MethodInterceptor {
	
	private final static Logger logger = LoggerFactory.getLogger(ApiMethodMocker.class);

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
				String methodName = invocation.getMethod().getName();
				//logger.info("api method name: " + methodName);
				String className = invocation.getThis().getClass().getName();
				//logger.info("api method class name: " + className);
				RequestMapping requestMapping = invocation.getMethod().getAnnotation(RequestMapping.class);
				if(null!=requestMapping){
					String uri = ((String[])requestMapping.value())[0];
					//logger.info("api method uri: " + uri);
				    ApiClassAnnotationInfo apiClassAnnotationInfo = AnnotationClassScanner.apisMap.get(className);
				    boolean flag = false;
			        if(null!=apiClassAnnotationInfo){
			        	String mockKey = apiClassAnnotationInfo.getUri()+"#"+uri;
			        	if(null!=DocsApi.apiMockMap.get(mockKey)&&1==DocsApi.apiMockMap.get(mockKey)){
			        		flag = true;
			        	}
			        }
				    if(flag){
				    	List<ApiMethodAnnotationInfo> apiList = apiClassAnnotationInfo.getApiList();
				        ApiMethodAnnotationInfo defaultApi = null;
				        for(ApiMethodAnnotationInfo api:apiList){
				        	if(api.getUri().equals(uri)){
				        		defaultApi = api;
				        		break;
				        	}
				        }
				        JSONObject exampleJson = ApiScannerService.getCommonExampleJson(defaultApi);
				        return exampleJson.toJavaObject(ResponseVo.class);
				    }
				}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return invocation.proceed();
	}

}
