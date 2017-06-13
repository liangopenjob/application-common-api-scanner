package com.jwliang.application.api.freemarker.engine;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.jwliang.application.api.annotation.scanner.bean.ApiMethodAnnotationInfo;
import com.jwliang.application.api.annotation.scanner.bean.ResponseVoField;
import com.jwliang.application.api.annotation.scanner.bean.ResponseVoInfo;
import com.jwliang.application.api.annotation.scanner.utils.DateUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * @ClassName: JavaBeanEngine  
 * @Description: 生成javaBean引擎类
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年3月29日
 *
 */

@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" } )
public class VoBeanEngine {
	 private static Configuration configuration;
	 
	 static{
		 configuration = new Configuration();
		 configuration.setClassForTemplateLoading(VoBeanEngine.class, "/com/jwliang/application/api/freemarker/template");
		 configuration.setSharedVariable("upperFc", new UpFirstCharacter());
	 }
	 
	 /**
	  * 
	  * @title: generateJavaBean 
	  * @description: 打包下载指定api vo bean java类  
	  * @author: liangjunwei
	  * @email: liangjwjob_2014@sina.com  
	  * @time: 2017年3月29日 下午5:46:21  
	  * @param zipOut
	  * @param basepackage
	  * @param author
	  * @param email
	  * @param api 
	  * @return void 返回类型 
	  * @throws
	  */
	 public static void generateJavaBean(ZipOutputStream zipOut, String basepackage, String author, String email, ApiMethodAnnotationInfo api){
	 		try {
	 			//获取javaBean模板
	 			Template template = configuration.getTemplate("JavaBean.ftl");
	 			//模型数据源
				Map templateData = new HashMap();
	 			//获取vo列表
	 			List<ResponseVoInfo> voInfos = api.getReturnClsList();
	 			for(int i=0;i<voInfos.size();i++){
	 				ResponseVoInfo voInfo = voInfos.get(i);
	 				//跳过ResponseVo
	 				if("ResponseVo".equals(voInfo.getSimpleName())){
	 					continue;
	 				}
	 				//类信息
	 				templateData.clear();
	 				templateData.put("basepackage", basepackage);
	 				templateData.put("entity", voInfo.getSimpleName());
	 				templateData.put("description", voInfo.getDescription());
	 				templateData.put("author", author);
	 				templateData.put("email", email);
	 				templateData.put("time", DateUtil.getChTimeStr(new Date()));
	 				//属性信息
	 				List propertyList = new ArrayList();
	 				List importList = new ArrayList();
	 				List<ResponseVoField> fields = voInfo.getFields();
	 				for(int k=0;k<fields.size();k++){
	 					ResponseVoField field = fields.get(k);
	 					String showDataType = field.getShowType().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
						if(i==0&&k==2){
							showDataType = api.getReturnDataType().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
						}
	 					Map proMap = new HashMap();
	 		 			proMap.put("proType", showDataType);
	 		 			proMap.put("proName", field.getName());
	 		 			proMap.put("des", field.getDescription() + " 是否允许为空:" + (field.getIsAllowEmpty()==1?"是":"否"));
	 		 			propertyList.add(proMap);
	 		 			//判断proType是否为简单类型,如果是自定义类型或者list添加import语句
	 		 			VoBeanEngine.setImports(showDataType, basepackage, importList);
	 				}
	 				//添加数据
		 			templateData.put("properties", propertyList);
		 			templateData.put("imports", importList);
		 			//生成java文件
		 			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		 			template.process(templateData, new OutputStreamWriter(byteOut, "utf-8"));
		 			zipOut.putNextEntry(new ZipEntry(voInfo.getSimpleName() + ".java"));
		 			zipOut.write(byteOut.toByteArray());
		 			zipOut.closeEntry();
		 			byteOut.close();
	 			}
	 			//第三方请求调用方法类生成
	 			Template callTemplate = configuration.getTemplate("XapiCallService.ftl");
	 			Map callTemplateData = new HashMap();
	 			callTemplateData.put("basepackage", basepackage);
	 			callTemplateData.put("author", author);
	 			callTemplateData.put("email", email);
	 			callTemplateData.put("time", DateUtil.getChTimeStr(new Date()));
	 			callTemplateData.put("method", api.getMethod());
	 			String returnModel = api.getReturnType().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
	 			callTemplateData.put("returnModel", returnModel);
	 			//如果是自定义类型或者list添加import语句
	 			List importList = new ArrayList();
	 			String showDataType = api.getReturnDataType().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
	 			VoBeanEngine.setImports(showDataType, basepackage, importList);
	 			callTemplateData.put("imports", importList);
	 			//生成java文件
	 			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	 			callTemplate.process(callTemplateData, new OutputStreamWriter(byteOut, "utf-8"));
	 			zipOut.putNextEntry(new ZipEntry("XapiCallService.java"));
	 			zipOut.write(byteOut.toByteArray());
	 			zipOut.closeEntry();
	 			byteOut.close();
	 		} catch (Exception e) {
	 			e.printStackTrace();
	 		}
	 }
	 
	 private static void setImports(String showDataType, String basepackage, List importList){
		   if(!VoBeanEngine.isSimpleType(showDataType)){
				if(showDataType.startsWith("List")||showDataType.startsWith("ArrayList")){//字段是List泛型集合
					String dataType = VoBeanEngine.getGenericType(showDataType);
					if(!VoBeanEngine.isSimpleType(dataType)){
						importList.add(basepackage + "." + dataType);
					}
					if(showDataType.startsWith("List")){
						importList.add("java.util.List");
					}
					if(showDataType.startsWith("ArrayList")){
						importList.add("java.util.ArrayList");
					}
				}else if(showDataType.endsWith("[]")){//字段是数组
					String dataType = showDataType.substring(0, showDataType.length()-2);
					if(!VoBeanEngine.isSimpleType(dataType)){
						importList.add(basepackage + "." + dataType);
					}
				}else{
					importList.add(basepackage + "." + showDataType);
				}
			}
	 }
	 
	 private static boolean isSimpleType(String dataType){
		 String dataTypes = "String,Integer,int,Long,long,Float,float,Double,double,Boolean,boolean,Byte,byte";
		 return dataTypes.contains(dataType);
	 }
	 
	 private static String getGenericType(String dataType){
		 if(-1!=dataType.indexOf("<")){
			 int bfIndex = dataType.indexOf("<");
			 int afIndex = dataType.lastIndexOf(">");
			 return getGenericType(dataType.substring(bfIndex+1,afIndex));
		 }else{
			 return dataType;
		 }
	 }
	 
}

