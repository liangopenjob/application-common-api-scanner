package com.jwliang.application.api.annotation.scanner.api.mocker;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jwliang.application.api.annotation.scanner.AnnotationClassScanner;
import com.jwliang.application.api.annotation.scanner.api.DocsApi;
import com.jwliang.application.api.annotation.scanner.bean.ApiClassAnnotationInfo;
import com.jwliang.application.api.annotation.scanner.bean.ApiMethodAnnotationInfo;
import com.jwliang.application.api.annotation.scanner.common.ReturnStatus;
import com.jwliang.application.api.annotation.scanner.service.ApiScannerService;

/**
 * 
 * @ClassName: ApiHandlerMocker 
 * @Description: 以请求拦截器形式模拟请求数据
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月22日
 *
 */
public class ApiHandlerMocker implements HandlerInterceptor {
	
    private final static Logger logger = LoggerFactory.getLogger(ApiHandlerMocker.class);
    
	@Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Exception {
		try {
			if(obj instanceof HandlerMethod){
				HandlerMethod method = (HandlerMethod)obj;
				String methodName = method.getMethod().getName();
				//logger.info("api method name: " + methodName);
				String className = method.getBeanType().getName();
				//logger.info("api method class name: " + className);
			    RequestMapping requestMapping = method.getMethodAnnotation(RequestMapping.class);
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
				        //响应样例数据
				        resp.setCharacterEncoding("UTF-8");
				        resp.setContentType("application/json;charset=UTF-8");
				        resp.setStatus(ReturnStatus.SC_OK.getValue());
				        PrintWriter writer = null;
				        try {
							writer = resp.getWriter();
							writer.write(exampleJson.toJSONString());
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						} finally {
				            if (null != writer) {
				                writer.close();
				            }
				        }
				        return false;
			        }
			    }
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return true;
	}


    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object obj, ModelAndView mv) throws Exception {
    	
    }
    
	@Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object obj, Exception ex) throws Exception {

	}

}
