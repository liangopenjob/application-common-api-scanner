package ${basepackage};

<#if method == "POST"> 
import java.util.Map;
</#if>
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jwliang.application.common.http.BaseService;
import com.jwliang.application.common.http.ResponseVo;
<#list imports as import>
import ${import};
</#list>

/**  
 * @description: 此类方法主要用于请求调用第三方服务使用。可在xapi service层类继承此类,类名和结构自己调整。
 * @author: ${author}
 * @email: ${email}
 * @time: ${time} 
 */
<#if method == "POST"> 
@SuppressWarnings("rawtypes")
</#if>
public class XapiCallService extends BaseService {

<#if method == "GET">
	/**
	 * 
	 * @title: callApiByGet 
	 * @description: get方式请求数据  
	 * @author: ${author}
	 * @email: ${email}
	 * @time: ${time} 
	 * @param url
	 * @return 
	 * @return ${returnModel} 返回类型 
	 * @throws
	 */
	public ${returnModel} callApiByGet(String url){
		String resultString = this.get(url).toJSONString();
		${returnModel} result = (${returnModel})JSON.parseObject(resultString, new TypeReference<${returnModel}>(){});
		return result;
    }
	
	/**
	 * 
	 * @title: callApiByGet 
	 * @description: get方式请求数据 可传递连接超时、读超时、写超时时长控制  
	 * @author: ${author}
	 * @email: ${email}   
	 * @time: ${time}  
	 * @param url
	 * @param connectTimeOut 
	 * @param readTimeout
	 * @param writeTimeOut
	 * @return 
	 * @return ${returnModel} 返回类型 
	 * @throws
	 */
	public ${returnModel} callApiByGet(String url, Long connectTimeOut, Long readTimeout, Long writeTimeOut){
		String resultString = this.get(url, connectTimeOut, readTimeout, writeTimeOut).toJSONString();
		${returnModel} result = (${returnModel})JSON.parseObject(resultString, new TypeReference<${returnModel}>(){});
		return result;
    }
</#if>
<#if method == "POST">  
	/**
	 * 
	 * @title: callApiByPost 
	 * @description: post方式请求数据  
	 * @author: ${author}
	 * @email: ${email}   
	 * @time: ${time}  
	 * @param url
	 * @param params post请求参数
	 * @return 
	 * @return ${returnModel} 返回类型 
	 * @throws
	 */
	public ${returnModel} callApiByPost(String url, Map params){
		String resultString = this.post(url, params).toJSONString();
		${returnModel} result = (${returnModel})JSON.parseObject(resultString, new TypeReference<${returnModel}>(){});
		return result;
    }
	
	/**
	 * 
	 * @title: callApiByPost 
	 * @description: post方式请求数据 可传递连接超时、读超时、写超时时长控制  
	 * @author: ${author}
	 * @email: ${email}   
	 * @time: ${time}  
	 * @param url
	 * @param params post请求参数
	 * @param connectTimeOut
	 * @param readTimeout
	 * @param writeTimeOut
	 * @return 
	 * @return ${returnModel} 返回类型 
	 * @throws
	 */
	public ${returnModel} callApiByPost(String url, Map params, Long connectTimeOut, Long readTimeout, Long writeTimeOut){
		String resultString = this.post(url, params, connectTimeOut, readTimeout, writeTimeOut).toJSONString();
		${returnModel} result = (${returnModel})JSON.parseObject(resultString, new TypeReference<${returnModel}>(){});
		return result;
    }
</#if>

}

