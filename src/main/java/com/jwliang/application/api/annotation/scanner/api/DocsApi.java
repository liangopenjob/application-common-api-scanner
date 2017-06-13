package com.jwliang.application.api.annotation.scanner.api;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jwliang.application.api.annotation.scanner.AnnotationClassScanner;
import com.jwliang.application.api.annotation.scanner.ResponseVoScanner;
import com.jwliang.application.api.annotation.scanner.bean.ApiClassAnnotationInfo;
import com.jwliang.application.api.annotation.scanner.bean.ApiGroup;
import com.jwliang.application.api.annotation.scanner.bean.ApiMethodAnnotationInfo;
import com.jwliang.application.api.annotation.scanner.bean.ResponseVoField;
import com.jwliang.application.api.annotation.scanner.common.KeyConstant;
import com.jwliang.application.api.annotation.scanner.common.ReturnStatus;
import com.jwliang.application.api.annotation.scanner.service.ApiScannerService;
import com.jwliang.application.api.annotation.scanner.utils.ApiDocPdfUtil;
import com.jwliang.application.api.annotation.scanner.utils.ApiExportUtil;
import com.jwliang.application.api.annotation.scanner.utils.CommonUtil;
import com.jwliang.application.api.annotation.scanner.utils.DateUtil;
import com.jwliang.application.api.annotation.scanner.utils.HttpUtil;
import com.jwliang.application.api.freemarker.engine.VoBeanEngine;

/**
 * 
 * @ClassName: DocsApi  
 * @Description: 获取Api元数据信息 
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月6日
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/docsApi")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DocsApi {
	private final static Logger logger = LoggerFactory.getLogger(DocsApi.class);
	
	//控制api级别是否开启api模拟功能  key:classUri#methodUri,value:0(关闭),1(开启);没有指定配置视为关闭状态
	public static Map<String,Integer> apiMockMap = new HashMap<String,Integer>();
	
	/**
	 * 获取Api详细信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/apiInfo/{id}")
	public Map<String,Object> apiInfo(@PathVariable Integer id){
		Map<String,Object> result = new HashMap<String,Object>();
		ApiMethodAnnotationInfo apiMethodAnnotationInfo = AnnotationClassScanner.apiMethodMap.get(id);
		result.put("apiMethod", apiMethodAnnotationInfo);
		ApiClassAnnotationInfo apiClassAnnotationInfo = AnnotationClassScanner.apiClassMap.get(apiMethodAnnotationInfo.getClassId());
		result.put("apiClass", apiClassAnnotationInfo);
		//生成响应示例
		Map<String,JSONObject> returnClsJson = ApiScannerService.getExampleJson(apiMethodAnnotationInfo);
		JSONObject exampleJson = new JSONObject();
		exampleJson.put("status", 200);
		exampleJson.put("message", "成功");
		String genericTypeName = apiMethodAnnotationInfo.getReturnClsList().get(1).getName();
		if(-1==apiMethodAnnotationInfo.getReturnType().indexOf("List")){
			exampleJson.put("data", returnClsJson.get(genericTypeName));
		}else{
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(returnClsJson.get(genericTypeName));
			exampleJson.put("data", jsonArray);
		}
		result.put("exampleJson", exampleJson);
		return result;
	}
	
	/**
	 * 获取所有api返回样例数据 key:"id"+id
	 * @return
	 */
	@RequestMapping("/apiRetExamples")
	public Map<String,Object> apiRetExamples(){
		Map<String,Object> result = new HashMap<String,Object>();
		List<ApiClassAnnotationInfo> apiClassList = AnnotationClassScanner.apiClassList;
		for(ApiClassAnnotationInfo cls:apiClassList){
			List<ApiMethodAnnotationInfo> apiList = cls.getApiList();
			for(ApiMethodAnnotationInfo method:apiList){
				try {
					result.put("exampleId" + method.getId(), ApiScannerService.getCommonExampleJson(method));
				} catch (Exception e) {
					logger.error("errorMsg:{},apiMethod:{}", e.getMessage(), JSONObject.toJSONString(method));
				}
			}
		}
		return result;
	}
	
	@RequestMapping("/apiClassList")
	public List<ApiClassAnnotationInfo> getApiClassList(){
		return AnnotationClassScanner.apiClassList;
	}
	
	@RequestMapping("/apisMap")
	public Map<String,ApiClassAnnotationInfo> getApisMap(){
		return AnnotationClassScanner.apisMap;
	}
	
	@RequestMapping("/apiClassMap")
	public Map<Integer,ApiClassAnnotationInfo> getApiClassMap(){
		return AnnotationClassScanner.apiClassMap;
	}
	
	@RequestMapping("/apiMethodMap")
	public Map<Integer,ApiMethodAnnotationInfo> getApiMethodMap(){
		return AnnotationClassScanner.apiMethodMap;
	}
	
	@RequestMapping("/apiGroupsMap")
	public Map<String,ApiGroup> getApiGroupsMap(){
		return AnnotationClassScanner.apiGroupsMap;
	}
	
	@RequestMapping("/beanFieldsMap")
	public Map<String,List<ResponseVoField>> getBeanFieldsMap(){
		return ResponseVoScanner.beanFieldsMap;
	}
	
	//静态html与服务在不同项目中post请求方式保证接口支持跨域
	@RequestMapping("/simulateRequest")
	public JSONObject simulateRequest(String paramsStr, String reqType, String uriStr, HttpServletRequest request){
		String url = "http://localhost:" + request.getLocalPort() + request.getContextPath() + uriStr ;
		JSONObject jsonParams = JSONObject.parseObject(paramsStr);
		Set<String> keys = jsonParams.keySet();
		Map<String,String> params = new HashMap<String,String>();
		for(String key:keys){
			String value = jsonParams.getString(key);
			if(StringUtils.isNotEmpty(value)){
				params.put(key, value);
			}
			String pathVar = "{" + key + "}";
			if(url.contains(pathVar)){
				if(StringUtils.isNotEmpty(value)){
					url = url.replace(pathVar, value);
				}else{
					url = url.replace(pathVar, "");
				}
			}
		}
		JSONObject data;
		if("GET".equals(reqType)){
			String reqUrl = CommonUtil.getUrl(url, params);
			data = HttpUtil.doGet(reqUrl);
		}else{
			data = HttpUtil.doPost(url, params);
		}
        return data;	
	}
	
	/**
	 * 
	 * @title: setApiMock 
	 * @description: 设置指定api是否开启mock(静态html与服务在不同项目中post请求方式保证接口支持跨域)
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年2月24日 上午9:18:35  
	 * @param id apiId
	 * @param mockType 0关闭,1开启
	 * @return 
	 * @return Map 返回类型 
	 * @throws
	 */
	@RequestMapping("/apiMock/{id}/{mockType}")
	public Map setApiMock(@PathVariable Integer id, @PathVariable Integer mockType, HttpServletRequest request){
		//权限验证
		Map checkMap = this.checkAuthorization(request);
		if(!"200".equals(checkMap.get(KeyConstant.KEY_RESULT_STATUS).toString())){
			return checkMap;
		}
		//查询数据
		Map data = new HashMap();
		data.put("status", 200);
		ApiMethodAnnotationInfo apiMethodAnnotationInfo = AnnotationClassScanner.apiMethodMap.get(id);
		String mockKey = apiMethodAnnotationInfo.getClassUri()+"#"+apiMethodAnnotationInfo.getUri();
		if(mockType==1){
			DocsApi.apiMockMap.put(mockKey, mockType);
			data.put("message", "open success!");
			data.put("isAllMockSet", this.isAllMockSet());//用于选中界面全量设置开关
		}else{
			DocsApi.apiMockMap.remove(mockKey);
			data.put("message", "close success!");
			data.put("isAllMockSet", "0");//用于取消界面全量设置开关
		}
		return data;
	}
	
	/**
	 * 
	 * @title: setAllApiMock 
	 * @description: 设置所有api是否开启mock(静态html与服务在不同项目中post请求方式保证接口支持跨域)  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com  
	 * @time: 2017年2月24日 上午9:30:17  
	 * @param mockType
	 * @return 
	 * @return Map 返回类型 
	 * @throws
	 */
	@RequestMapping("/apiMock/{mockType}")
	public Map setAllApiMock(@PathVariable Integer mockType, HttpServletRequest request){
		//权限验证
		Map checkMap = this.checkAuthorization(request);
		if(!"200".equals(checkMap.get(KeyConstant.KEY_RESULT_STATUS).toString())){
			return checkMap;
		}
		//数据查询
		Map data = new HashMap();
		data.put("status", 200);
		//清除数据
		this.clearApiMockMap();
		data.put("message", "close all success!");
	    //重设数据
		if(mockType==1){
			List<ApiClassAnnotationInfo> apiClassList = AnnotationClassScanner.apiClassList;
			for(ApiClassAnnotationInfo apiClass:apiClassList){
				List<ApiMethodAnnotationInfo> apiList = apiClass.getApiList();
				for(ApiMethodAnnotationInfo api:apiList){
					DocsApi.apiMockMap.put(api.getClassUri()+"#"+api.getUri(), mockType);
				}
			}
			data.put("message", "open all success!");
		}
		return data;
	}
	
	/**
	 * 
	 * @title: isAllMock 
	 * @description: 验证是否所有接口开启mock配置  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年2月27日 上午8:38:23  
	 * @return 
	 * @return Map 返回类型 
	 * @throws
	 */
	@RequestMapping("/isAllMock")
	public Map isAllMock(HttpServletRequest request, HttpServletResponse response){
		Map data = new HashMap();
		data.put("status", 200);
		data.put("isAllMockSet", this.isAllMockSet());
		//权限验证
		Map checkMap = this.checkAuthorization(request);
		if("200".equals(checkMap.get(KeyConstant.KEY_RESULT_STATUS).toString())){
			data.put("isAllow", true);
		}else{
			data.put("isAllow", false);
		}
		//锁定状态
		data.put("isLocked", ApiScannerService.isLocked);
		return data;
	}
	
	/**
	 * 
	 * @title: getApiMockMap 
	 * @description: 查询开启mock配置信息
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年2月27日 上午8:39:10  
	 * @return 
	 * @return Map<String,Integer> 返回类型 
	 * @throws
	 */
	@RequestMapping("/apiMockMap")
	public Map<String,Integer> getApiMockMap(){
		return DocsApi.apiMockMap;
	}
	
	/**
	 * 
	 * @title: exportApiMockConfig 
	 * @description: 导出开启mock配置信息  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com  
	 * @time: 2017年2月27日 上午8:39:52  
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping("/apiMockConfig/out")
	public void exportApiMockConfig(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//导出txt文本
		String fileName = "Api-Mock配置数据-"+DateUtil.getYyMdHmsStr(new Date())+".txt";
		response.setContentType("application/octet-stream;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename="+ new String(fileName.getBytes(),"iso-8859-1"));
		OutputStream out = response.getOutputStream();
		out.write(JSONObject.toJSONString(DocsApi.apiMockMap).getBytes());
		out.flush();
		out.close();
	}
	
	/**
	 * 
	 * @title: importApiMockConfig 
	 * @description: 导入开启mock配置信息(静态html与服务在不同项目中post请求方式保证接口支持跨域) 
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年2月27日 上午8:40:20  
	 * @param request
	 * @param response
	 * @return 
	 * @return Map 返回类型 
	 * @throws
	 */
	@RequestMapping("/apiMockConfig/in")
	public Map importApiMockConfig(HttpServletRequest request,HttpServletResponse response){
		Map data = new HashMap();
		//获取上传文件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("configData");
		if(null==file||file.getSize()==0){
			data.put("message", "no file upload!");
		}else{
			try {
				InputStream is = file.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line;
				while(null!=(line=br.readLine())){
					sb.append(line);
				}
				br.close();
				is.close();
				JSONObject json = JSONObject.parseObject(sb.toString());
				DocsApi.apiMockMap.clear();
				Set<String> keySet = json.keySet();
				for(String key:keySet){
					if(1==json.getIntValue(key)){
						DocsApi.apiMockMap.put(key, json.getInteger(key));
					}
				}
				data.put("message", "import config success!");
			} catch (Exception e) {
				data.put("message", "import config failure!");
			}
		}
		return data;
	}
	
	/**
	 * 
	 * @title: searchApis 
	 * @description: 根据标题、作者、URI查询Api
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com  
	 * @time: 2017年3月7日 下午2:13:35  
	 * @param searchText
	 * @return 
	 * @return List<ApiMethodAnnotationInfo> 返回类型 
	 * @throws
	 */
	@RequestMapping("/apis")
	public List<ApiMethodAnnotationInfo> searchApis(String searchText){
		List<ApiMethodAnnotationInfo> apis = new ArrayList<ApiMethodAnnotationInfo>();
		if(!StringUtils.isEmpty(searchText)){
			searchText = searchText.toLowerCase();
			List<ApiClassAnnotationInfo> apiClassList = AnnotationClassScanner.apiClassList;
			for(ApiClassAnnotationInfo clsInfo:apiClassList){
				List<ApiMethodAnnotationInfo> methods = clsInfo.getApiList();
				for(ApiMethodAnnotationInfo method:methods){
					if(-1!=method.getTitle().toLowerCase().indexOf(searchText)){
						apis.add(method);
					}else if(null!=method.getAuthor()&&-1!=method.getAuthor().indexOf(searchText)){
						apis.add(method);
					}else{
						String uri = (method.getClassUri() + method.getUri()).toLowerCase();
						if(-1!=uri.indexOf(searchText)){
							apis.add(method);
						}
					}
				}
			}
		}
		return apis;
	}
	
	/**
	 * 
	 * @title: exportApiPdf 
	 * @description: Api Docs导出pdf  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月23日 下午7:30:02  
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping("/apiExport/pdf")
	public void exportApiPdf(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//Api Docs导出pdf
		//String fileName = "Api-Docs-"+DateUtils.getYyMdHmsStr(new Date())+".pdf";
		String fileName = ApiScannerService.docTitle + "-" + DateUtil.getYyMdHmsStr(new Date())+".pdf";
		response.setContentType("application/octet-stream;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes(), "iso-8859-1") + "\"");
		OutputStream out = response.getOutputStream();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ApiExportUtil apiExportUtil = new ApiExportUtil();
		apiExportUtil.exportPdfDocs(byteArrayOutputStream);
		out.write(byteArrayOutputStream.toByteArray());
		out.flush();
		out.close();
	}
	
	/**
	 * 
	 * @title: exportApiDoc 
	 * @description: Api Docs导出doc
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月23日 下午7:30:02  
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping("/apiExport/doc")
	public void exportApiDoc(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//Api Docs导出doc
		//String fileName = "Api-Docs-"+DateUtils.getYyMdHmsStr(new Date())+".doc";
		String fileName = ApiScannerService.docTitle + "-" + DateUtil.getYyMdHmsStr(new Date())+".doc";
		response.setContentType("application/octet-stream;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes(),"iso-8859-1") + "\"");
		OutputStream out = response.getOutputStream();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ApiDocPdfUtil apiDocPdfUtil = new ApiDocPdfUtil();
		apiDocPdfUtil.exportDocs(byteArrayOutputStream, "doc");
		out.write(byteArrayOutputStream.toByteArray());
		out.flush();
		out.close();
	}
	
	/**
	 * 
	 * @title: lockMock 
	 * @description: mock开关锁定、解锁  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月31日 上午11:21:11  
	 * @param lockType
	 * @return 0:解锁  1:锁定
	 * @return Map 返回类型 
	 * @throws
	 */
	@RequestMapping("/lockMock/{lockType}")
	public Map lockMock(@PathVariable Integer lockType, HttpServletRequest request){
		//权限验证
		Map checkMap = this.checkAuthorization(request);
		if(!"200".equals(checkMap.get(KeyConstant.KEY_RESULT_STATUS).toString())){
			return checkMap;
		}
		//数据处理
		Map data = new HashMap();
		data.put("status", 200);
		if(lockType==0){//解锁
			//解锁时恢复白名单列表
			ApiScannerService.whiteIpList.clear();
			if(StringUtils.isNotEmpty(ApiScannerService.cpWhiteIpList)){
				String[] ips = ApiScannerService.cpWhiteIpList.split(",");
				for(String ip:ips){
					ApiScannerService.whiteIpList.add(ip);
				}
			}
			//修改锁定状态
			ApiScannerService.isLocked = false;
			data.put("message", "unlock success!");
		}else{//锁定
			//备份白名单在解锁时恢复
			if(0==ApiScannerService.whiteIpList.size()){
				ApiScannerService.cpWhiteIpList = "";
			}else{
				StringBuilder ips = new StringBuilder();
				for(String ip:ApiScannerService.whiteIpList){
					ips.append(ip).append(",");
				}
				ips.deleteCharAt(ips.length()-1);
				ApiScannerService.cpWhiteIpList = ips.toString();
			}
			//设置白名单为当前ip
			String realIp = checkMap.get("realIp").toString();
			ApiScannerService.whiteIpList.clear();
			ApiScannerService.whiteIpList.add(realIp);
			//修改锁定状态
			ApiScannerService.isLocked = true;
			data.put("message", "lock success!");
		}
		return data;
	}
	
	/**
	 * 
	 * @title: docsInfo 
	 * @description: 处理文档相关信息  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月28日 下午2:33:36  
	 * @return 
	 * @return Map 返回类型 
	 * @throws
	 */
	@RequestMapping("/docsInfo")
	public Map docsInfo(){
		Map data = new HashMap();
		data.put("docTitle", ApiScannerService.docTitle);
		return data;
	}
	
	/**
	 * 
	 * @title: generateVo 
	 * @description: 打包下载指定api vo bean java类  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月29日 下午5:28:51  
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping("/generateVoCode")
	public void generateVoCode(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String id = request.getParameter("id");//api id
		String basepackage = request.getParameter("basepackage");//包路径
		String author = request.getParameter("author");
		String email = request.getParameter("email");
		ApiMethodAnnotationInfo apiMethodAnnotationInfo = AnnotationClassScanner.apiMethodMap.get(Integer.valueOf(id));
		String fileName = apiMethodAnnotationInfo.getTitle() + "-" + DateUtil.getYyMdHmsStr(new Date())+".zip";
		response.setContentType("application/octet-stream;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes(),"iso-8859-1") + "\"");
		OutputStream out = response.getOutputStream();
		ZipOutputStream zipOut = new ZipOutputStream(out);
		VoBeanEngine.generateJavaBean(zipOut, basepackage, author, email, apiMethodAnnotationInfo);
		zipOut.close();
		out.close();
	}
	
	/**
	 * 
	 * @title: commonStatus 
	 * @description: 获取公共状态编码信息  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月31日 下午6:44:46  
	 * @return 
	 * @return Map 返回类型 
	 * @throws
	 */
	@RequestMapping("/commonStatus")
	public Map commonStatus(){
		ReturnStatus[] rsArray = ReturnStatus.values();
		List<Map> rsList = new ArrayList<Map>();
		for(ReturnStatus rs:rsArray){
			Map temp = new HashMap();
			temp.put("value", rs.getValue());
			temp.put("desc", rs.getDesc());
			rsList.add(temp);
		}
		Collections.sort(rsList, new Comparator<Map>() {
			@Override
			public int compare(Map one, Map two) {
				return Integer.valueOf(one.get("value").toString()) - Integer.valueOf(two.get("value").toString());
			}
		});
		Map data = new HashMap();
		data.put("rsList", rsList);
		return data;
	}
	
	/**
	 * 
	 * @title: downloadHelpPpt 
	 * @description: Api Docs帮助文档下载
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月23日 下午7:30:02  
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping("/downloadHelpPpt")
	public void downloadHelpPpt(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String fileName = "Api-docs使用说明.pptx";
		response.setContentType("application/octet-stream;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes(),"iso-8859-1") + "\"");
		OutputStream out = response.getOutputStream();
		URL url = DocsApi.class.getClassLoader().getResource(fileName);
		InputStream is = url.openStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len=is.read(buffer))>0){
			out.write(buffer, 0, len);
		}
		out.flush();
		out.close();
		is.close();
	}
	
	private void clearApiMockMap(){
		DocsApi.apiMockMap.clear();
	}
	
	private int isAllMockSet(){
		List<ApiClassAnnotationInfo> apiClassList = AnnotationClassScanner.apiClassList;
		for(ApiClassAnnotationInfo apiClass:apiClassList){
			List<ApiMethodAnnotationInfo> apiList = apiClass.getApiList();
			for(ApiMethodAnnotationInfo api:apiList){
				String mockKey = api.getClassUri()+"#"+api.getUri();
				if(!DocsApi.apiMockMap.containsKey(mockKey)){
					return 0;
				}
			}
		}
		return 1;
	}
	
	/**
	 * 
	 * @title: checkAuthorization 
	 * @description: 检测白名单权限  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com  
	 * @time: 2017年3月31日 上午11:08:01  
	 * @param request
	 * @return 
	 * @return Map 返回类型 
	 * @throws
	 */
	public Map checkAuthorization(HttpServletRequest request){
		Map retMap = new HashMap();
		retMap.put("status", 200);
		String ipStr = request.getHeader("X-Forwarded-For");
		String realIp = null;
		if(StringUtils.isNotEmpty(ipStr)){
			realIp = ipStr.split(",")[0];
		}
		if(null==realIp){
			realIp = request.getRemoteAddr();
		}
		retMap.put("realIp", realIp);
		logger.info("ipStr:{},realIp:{}", ipStr, realIp);
		if(0!=ApiScannerService.whiteIpList.size()&&!ApiScannerService.whiteIpList.contains(realIp)){
			retMap.put(KeyConstant.KEY_RESULT_STATUS, ReturnStatus.API_NO_PERMISSION.getValue());
			retMap.put(KeyConstant.KEY_RESULT_MESSAGE, ReturnStatus.API_NO_PERMISSION.getDesc());
		}
		return retMap;
	}
	
}
