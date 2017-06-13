package com.jwliang.application.api.annotation.scanner.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @ClassName: CommonUtil  
 * @Description: 公共工具类
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年3月22日
 *
 */

public class CommonUtil {
    /**
     * 
     * @title: setHierarchyFields 
     * @description: 获取继承层级的Field  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com  
     * @time: 2017年3月22日 上午10:42:48  
     * @param clazz
     * @param fieldList 
     * @return void 返回类型 
     * @throws
     */
    public static void setHierarchyFields(Class clazz, List<Field> fieldList){
    	if(null==fieldList){
    		fieldList = new ArrayList<Field>();
    	}
    	fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
    	if(!clazz.getSuperclass().equals(Object.class)){
    		setHierarchyFields(clazz.getSuperclass(), fieldList);
    	}
    }
    
	/**
	 * get请求拼接参数
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getUrl(String url, Map params) {
		
		if(StringUtils.isEmpty(url)){
			return url;
		}
		StringBuilder urlParams = new StringBuilder(url);
		if(-1==url.indexOf("?")){
			urlParams.append("?1=1");
		}
		if (null != params) {
			for (Iterator ite = params.entrySet().iterator(); ite.hasNext();) {
				Map.Entry entry = (Map.Entry) ite.next();
				urlParams.append("&").append(entry.getKey().toString())
						.append("=").append(entry.getValue().toString());
			}
		}
		return urlParams.toString();
	}
}

