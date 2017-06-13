package com.jwliang.application.api.annotation.scanner;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.jwliang.application.api.annotation.definition.ApiAuth;
import com.jwliang.application.api.annotation.definition.ApiLog;
import com.jwliang.application.api.annotation.definition.ApiLogs;
import com.jwliang.application.api.annotation.definition.ApiModelDescription;
import com.jwliang.application.api.annotation.definition.ApiParam;
import com.jwliang.application.api.annotation.definition.ApiRemark;
import com.jwliang.application.api.annotation.definition.ApiResources;
import com.jwliang.application.api.annotation.definition.ApiStatus;
import com.jwliang.application.api.annotation.definition.ApiStatuses;
import com.jwliang.application.api.annotation.scanner.bean.ApiClassAnnotationInfo;
import com.jwliang.application.api.annotation.scanner.bean.ApiGroup;
import com.jwliang.application.api.annotation.scanner.bean.ApiLogInfo;
import com.jwliang.application.api.annotation.scanner.bean.ApiMethodAnnotationInfo;
import com.jwliang.application.api.annotation.scanner.bean.ApiMethodParameter;
import com.jwliang.application.api.annotation.scanner.bean.ApiResourcesInfo;
import com.jwliang.application.api.annotation.scanner.bean.ApiStatusInfo;
import com.jwliang.application.api.annotation.scanner.bean.ResponseVoInfo;
import com.jwliang.application.api.annotation.scanner.utils.CommonUtil;
import com.jwliang.application.api.annotation.scanner.utils.ParameterNameUtil;
/**
 * 
 * @ClassName: AnnotationClassScanner  
 * @Description: 扫描Api主体类
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

@SuppressWarnings({"rawtypes","unchecked"})
public class AnnotationClassScanner {  
	
	private static Integer classApiIdIndex = 0;
	
	private static Integer methodApiIdIndex = 0;
      
    private static final String RESOURCE_PATTERN = "/**/*.class";  
      
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();  
      
    private List<String> packageList= new LinkedList<String>();  
      
    private List<TypeFilter> typeFilters = new LinkedList<TypeFilter>();  
    
    private List<Class<?>> classList= new ArrayList<Class<?>>();
    
    public static final List<ApiClassAnnotationInfo> apiClassList = new ArrayList<ApiClassAnnotationInfo>();
    
    public static final Map<String,ApiClassAnnotationInfo> apisMap = new HashMap<String,ApiClassAnnotationInfo>();
    
    public static final Map<Integer,ApiClassAnnotationInfo> apiClassMap = new HashMap<Integer,ApiClassAnnotationInfo>();
    
    public static final Map<Integer,ApiMethodAnnotationInfo> apiMethodMap = new HashMap<Integer,ApiMethodAnnotationInfo>();
    
    public static final String defaultGroupName = "默认分组";
    
    public static final String defaultResourcesName = "默认资源";
    
    public static final Map<String,ApiGroup> apiGroupsMap = new TreeMap<String,ApiGroup>();
    
    /** 
     * 构造函数 
     * @param packagesToScan 指定哪些包需要被扫描,支持多个包"package.a,package.b"并对每个包都会递归搜索 
     * @param filters 指定扫描包中含有特定注解标记的bean,支持多个注解 
     */  
	public AnnotationClassScanner(String[] packagesToScan, Class<? extends Annotation> ... filters){
    	this.clearData();
        if (packagesToScan != null) {  
            for (String packagePath : packagesToScan) {  
                this.packageList.add(packagePath);  
            }
        }  
        if (filters != null){  
            for (Class<? extends Annotation> annotation : filters) {  
                typeFilters.add(new AnnotationTypeFilter(annotation, false));  
            }  
        }  
    }  
      
    /** 
     * 将符合条件的Bean以Class集合的形式返回 
     * @return 
     * @throws IOException 
     * @throws ClassNotFoundException 
     */  
    public List<Class<?>> getClassList() throws IOException, ClassNotFoundException {  
        if (!this.packageList.isEmpty()) {  
                for (String pkg : this.packageList) {  
                    String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;  
                    Resource[] resources = this.resourcePatternResolver.getResources(pattern);  
                    MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);  
                    for (Resource resource : resources) {  
                        if (resource.isReadable()) {  
                            MetadataReader reader = readerFactory.getMetadataReader(resource);
                            String className = reader.getClassMetadata().getClassName();
                            if (this.matchesEntityTypeFilter(reader, readerFactory)) {  
                                this.classList.add(Class.forName(className));
                                //读取类API注解
                                ApiClassAnnotationInfo apiClassAnnotationInfo = AnnotationClassScanner.getApiClassInfo(reader, className);
                                //读取类group信息
                                String groupName = AnnotationClassScanner.getApiGroupInfo(reader);
                                //读取API方法注解
                                Method[] methods = Class.forName(className).getDeclaredMethods();
                                for(Method method:methods){
                                	if(!method.isAnnotationPresent(RequestMapping.class)){
                                		continue;
                                	}
                                	//api信息
                                	ApiMethodAnnotationInfo apiMethodAnnotationInfo = AnnotationClassScanner.getApiMethodInfo(apiClassAnnotationInfo, method);
                                    //参数信息
                                	AnnotationClassScanner.getApiMethodParameters(className, method, apiMethodAnnotationInfo);
                                	//返回类型信息
                                	AnnotationClassScanner.getApiMethodReturnType(method,apiMethodAnnotationInfo);
                                	//store api list
                                    apiClassAnnotationInfo.getApiList().add(apiMethodAnnotationInfo);
                                    //store key - method api
                                    AnnotationClassScanner.apiMethodMap.put(apiMethodAnnotationInfo.getId(), apiMethodAnnotationInfo);
                                    //添加至api group中
                                    AnnotationClassScanner.setApiGroupInfo(groupName, method, apiMethodAnnotationInfo);
                                }
                                //store API info
                                AnnotationClassScanner.apisMap.put(className, apiClassAnnotationInfo);
                                AnnotationClassScanner.apiClassList.add(apiClassAnnotationInfo);
                                //store key - class api
                                AnnotationClassScanner.apiClassMap.put(apiClassAnnotationInfo.getId(), apiClassAnnotationInfo);
                            }  
                        }  
                    }  
                }
        }  
        return this.classList;  
    }
    
    /**
     * 设置 api group信息
     * @param groupName
     * @param method
     * @param apiMethodAnnotationInfo
     */
    public static void setApiGroupInfo(String groupName, Method method, ApiMethodAnnotationInfo apiMethodAnnotationInfo){
    	ApiGroup apiGroup = AnnotationClassScanner.apiGroupsMap.get(groupName);
        String resourcesName = AnnotationClassScanner.defaultResourcesName;
        if(method.isAnnotationPresent(ApiResources.class)){
        	ApiResources apiResources = method.getAnnotation(ApiResources.class);
        	if(StringUtils.isNotEmpty(apiResources.name())){
        		resourcesName = apiResources.name();
        	}
        }
        if(!apiGroup.getResourcesMap().containsKey(resourcesName)){
        	ApiResourcesInfo apiResourcesInfo = new ApiResourcesInfo(resourcesName);
        	apiGroup.getResourcesMap().put(resourcesName, apiResourcesInfo);
        }
        apiGroup.getResourcesMap().get(resourcesName).getMethods().add(apiMethodAnnotationInfo);
    }
     
    /** 
     * 检查当前扫描到的Bean含有任何一个指定的注解标记 
     * @param reader 
     * @param readerFactory 
     * @return 
     * @throws IOException 
     */  
    private boolean matchesEntityTypeFilter(MetadataReader reader, MetadataReaderFactory readerFactory) throws IOException {  
        if (!this.typeFilters.isEmpty()) {  
            for (TypeFilter filter : this.typeFilters) {  
                if (filter.match(reader, readerFactory)) {  
                    return true;  
                }  
            }  
        }  
        return false;  
    }

    /**
     * 解析API class信息
     * @param reader
     * @param className
     * @return
     */
	public static ApiClassAnnotationInfo getApiClassInfo(MetadataReader reader,String className) {
		ApiClassAnnotationInfo apiClassAnnotationInfo = new ApiClassAnnotationInfo(++AnnotationClassScanner.classApiIdIndex,className);
		AnnotationMetadata classAnnotations = reader.getAnnotationMetadata();
		Map<String,Object> classApiAnnotationAttrs = classAnnotations.getAnnotationAttributes("com.jwliang.application.api.annotation.definition.Api");
		apiClassAnnotationInfo.setTitle(classApiAnnotationAttrs.get("title").toString());
		apiClassAnnotationInfo.setGroup(classApiAnnotationAttrs.get("group").toString());
		Map<String,Object> classMappingAnnotationAttrs = classAnnotations.getAnnotationAttributes("org.springframework.web.bind.annotation.RequestMapping");
		apiClassAnnotationInfo.setUri(((String[])classMappingAnnotationAttrs.get("value"))[0]);
		return apiClassAnnotationInfo;
	}
	
	/**
	 * 解析API group信息
	 * @param reader
	 * @return
	 */
	public static String getApiGroupInfo(MetadataReader reader) {
		AnnotationMetadata classAnnotations = reader.getAnnotationMetadata();
		Map<String,Object> classApiAnnotationAttrs = classAnnotations.getAnnotationAttributes("com.jwliang.application.api.annotation.definition.Api");
		String groupName = classApiAnnotationAttrs.get("group").toString();
		if(StringUtils.isEmpty(groupName)){
			groupName = AnnotationClassScanner.defaultGroupName;
		}
		if(!AnnotationClassScanner.apiGroupsMap.containsKey(groupName)){
			ApiGroup apiGroup = new ApiGroup(groupName);
			AnnotationClassScanner.apiGroupsMap.put(groupName, apiGroup);
		}
		return groupName;
	}

    /**
     * 解析API method信息
     * @param apiClassAnnotationInfo
     * @param method
     * @return
     */
	public static ApiMethodAnnotationInfo getApiMethodInfo(ApiClassAnnotationInfo apiClassAnnotationInfo, Method method) {
		ApiMethodAnnotationInfo apiMethodAnnotationInfo = new ApiMethodAnnotationInfo(++AnnotationClassScanner.methodApiIdIndex, apiClassAnnotationInfo.getId());
		apiMethodAnnotationInfo.setClassUri(apiClassAnnotationInfo.getUri());
		ApiAuth apiAuth = method.getAnnotation(ApiAuth.class);
		if(null==apiAuth){
			apiMethodAnnotationInfo.setShow(true);
			apiMethodAnnotationInfo.setDeprecated(false);
		}else{
			apiMethodAnnotationInfo.setShow(apiAuth.show());
			apiMethodAnnotationInfo.setAuthor(apiAuth.author());
			apiMethodAnnotationInfo.setDeprecated(apiAuth.deprecated());
		}
		ApiRemark apiRemark = method.getAnnotation(ApiRemark.class);
		if(null!=apiRemark){
			apiMethodAnnotationInfo.setRemark(apiRemark.description());
		}
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		apiMethodAnnotationInfo.setUri(((String[])requestMapping.value())[0]);
		if(StringUtils.isEmpty(requestMapping.name())){
			apiMethodAnnotationInfo.setTitle(method.getName());
		}else{
			apiMethodAnnotationInfo.setTitle(requestMapping.name());		
		}
		RequestMethod[] methodArray = (RequestMethod[])requestMapping.method();
		if(methodArray.length==0){
			apiMethodAnnotationInfo.setMethod(RequestMethod.GET.name());
		}else{
			StringBuilder methodBuilder = new StringBuilder();
			for(RequestMethod rm:methodArray){
				methodBuilder.append(rm.name()).append(",");
			}
			methodBuilder.deleteCharAt(methodBuilder.length()-1);
			apiMethodAnnotationInfo.setMethod(methodBuilder.toString());
		}
		// api状态码列表
		ApiStatuses apiStatuses = method.getAnnotation(ApiStatuses.class);
		if(null!=apiStatuses){
			for(ApiStatus apiStatus:apiStatuses.value()){
				apiMethodAnnotationInfo.getApiStatuses().add(new ApiStatusInfo(apiStatus.code(), apiStatus.message()));
			}
		}
		// api变更记录列表
		ApiLogs apiLogs = method.getAnnotation(ApiLogs.class);
		if(null!=apiLogs){
			for(ApiLog apiLog:apiLogs.value()){
				apiMethodAnnotationInfo.getApiLogs().add(new ApiLogInfo(apiLog.logMsg(), apiLog.logPerson(), apiLog.logDate()));
			}
		}
		return apiMethodAnnotationInfo;
	}  
    
	/**
	 * 解析api method 参数信息
	 * @param className
	 * @param method
	 * @param apiMethodAnnotationInfo
	 */
	public static void getApiMethodParameters(String className, Method method, ApiMethodAnnotationInfo apiMethodAnnotationInfo) {
//		String[] parameterNames = JavassistUtil.getParameterNames(className, method.getName());
		String[] parameterNames = new String[]{};
		try {
			parameterNames = ParameterNameUtil.getParameterNames(Class.forName(className), method);
		} catch (ClassNotFoundException en) {
			en.printStackTrace();
		}
		Class<?>[] parameterTypes = method.getParameterTypes();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		for (int k=0;k<parameterTypes.length;k++) {
			Class<?> cls = parameterTypes[k];
			if("javax.servlet.http.HttpServletRequest".equals(cls.getName())){
				continue;
			}
			if("javax.servlet.http.HttpServletResponse".equals(cls.getName())){
				continue;
			}
			//验证参数类型是否为简单类型
			if(AnnotationClassScanner.isSimpleParameterType(cls.getName())){
				String parameterType = cls.getName();
				String simpleParameterType = parameterType;
				if(-1!=parameterType.indexOf(".")){
					simpleParameterType = parameterType.substring(parameterType.lastIndexOf(".")+1);
				}
				String parameterName =  parameterNames[k];
				ApiMethodParameter apiMethodParameter = new ApiMethodParameter(parameterName, parameterType, simpleParameterType);
				//验证是否必选
				JSONObject parameterInfo = AnnotationClassScanner.getParameterInfo(parameterAnnotations[k], true);
				Integer isRequired = parameterInfo.getIntValue("isRequired");
				String description = parameterInfo.getString("description");
				apiMethodParameter.setIsRequired(isRequired);
				apiMethodParameter.setDescription(description);
				apiMethodAnnotationInfo.getParameters().add(apiMethodParameter);
			}else{
				try {
					Class parameterClass = Class.forName(cls.getName());
					Field[] fields = parameterClass.getDeclaredFields();
					//如果参数继承其它Class获取父级Field
					if(!parameterClass.getSuperclass().equals(Object.class)){
						List<Field> fieldList = new ArrayList<Field>();
						CommonUtil.setHierarchyFields(parameterClass, fieldList);
						fields = fieldList.toArray(new Field[fieldList.size()]);
					}
					for(Field field:fields){
						String parameterType = field.getType().getName();
						String simpleParameterType = parameterType;
						if(-1!=parameterType.indexOf(".")){
							simpleParameterType = parameterType.substring(parameterType.lastIndexOf(".")+1);
						}
						String parameterName = field.getName();
						//验证是否必选
						JSONObject parameterInfo = AnnotationClassScanner.getParameterInfo(field.getDeclaredAnnotations(), false);
						Integer isRequired = parameterInfo.getIntValue("isRequired");
						String description = parameterInfo.getString("description");
						ApiMethodParameter apiMethodParameter = new ApiMethodParameter(parameterName, parameterType, simpleParameterType);
						apiMethodParameter.setIsRequired(isRequired);
						apiMethodParameter.setDescription(description);
						apiMethodAnnotationInfo.getParameters().add(apiMethodParameter);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 解析api method 返回类型
	 * @param method
	 * @param apiMethodAnnotationInfo
	 */
	public static void getApiMethodReturnType(Method method,ApiMethodAnnotationInfo apiMethodAnnotationInfo) {
		Type genericType = method.getGenericReturnType();
		StringBuilder returnType = new StringBuilder();
		AnnotationClassScanner.getReturnType(genericType.toString(), returnType, apiMethodAnnotationInfo.getReturnClsList());
		apiMethodAnnotationInfo.setReturnType(returnType.substring(1, returnType.length()-1));
	}
  
    /**
     * 解析简单的返回类型str
     * @param fullTypeStr
     * @return
     */
    public static void getReturnType(String fullTypeStr,StringBuilder returnType,List<ResponseVoInfo> returnClsList){
    	if(fullTypeStr.startsWith("interface")){
    		fullTypeStr = fullTypeStr.replace("interface", "").trim();
    	}
    	if(fullTypeStr.startsWith("class")){
    		fullTypeStr = fullTypeStr.replace("class", "").trim();
    	}
    	if(null==returnType){
    		returnType = new StringBuilder();
    	}
    	if(null==returnClsList){
    		returnClsList = new ArrayList<ResponseVoInfo>();
    	}
    	if(-1!=fullTypeStr.indexOf("<")){
    		String preClassStr = fullTypeStr.substring(0, fullTypeStr.indexOf("<"));
    		if(!AnnotationClassScanner.isSimpleReturnType(preClassStr)){
    			ResponseVoInfo responseVoInfo = new ResponseVoInfo(preClassStr);
    			if(-1!=preClassStr.indexOf(".")){
    				responseVoInfo.setSimpleName(preClassStr.substring(preClassStr.lastIndexOf(".")+1));
    			}
    			if(null!=ResponseVoScanner.beanFieldsMap.get(preClassStr)){
    				responseVoInfo.setFields(ResponseVoScanner.beanFieldsMap.get(preClassStr));
    			}
    			//读取vo类描述信息
    			responseVoInfo.setDescription(AnnotationClassScanner.getClassDescripton(preClassStr));
    			returnClsList.add(responseVoInfo);
    			//循环递归添加实体字段中引用的自定义实体
    			AnnotationClassScanner.getReferVo(preClassStr, returnClsList);
    		}
    		//TODO 此处可以针对Map中的key、value自定义类型进行判断 //java.util.Map<java.lang.String, java.lang.String>
    		returnType.append("<").append(preClassStr.substring(preClassStr.lastIndexOf(".")+1));
    		String tempFullTypeStr = fullTypeStr.substring(fullTypeStr.indexOf("<")+1, fullTypeStr.length()-1);
    		AnnotationClassScanner.getReturnType(tempFullTypeStr, returnType, returnClsList);
    		returnType.append(">");
    	}else{
    		//处理数组返回类型
    		boolean isArray = false;
    		if(fullTypeStr.startsWith("[")){
    			isArray = true;
    			if(fullTypeStr.endsWith(";")){//自定义对象、String、Integer等对象
    				fullTypeStr = fullTypeStr.substring(2, fullTypeStr.length()-1);
    			}else{
    				if(fullTypeStr.startsWith("[I")){
    					fullTypeStr = "int";
    				}else if(fullTypeStr.startsWith("[J")){
    					fullTypeStr = "long";
    				}else if(fullTypeStr.startsWith("[F")){
    					fullTypeStr = "float";
    				}else if(fullTypeStr.startsWith("[D")){
    					fullTypeStr = "double";
    				}else if(fullTypeStr.startsWith("[Z")){
    					fullTypeStr = "boolean";
    				}else if(fullTypeStr.startsWith("[B")){
    					fullTypeStr = "byte";
    				}
    			}
    		}
    		if(!AnnotationClassScanner.isSimpleReturnType(fullTypeStr)){
    			ResponseVoInfo responseVoInfo = new ResponseVoInfo(fullTypeStr);
    			if(-1!=fullTypeStr.indexOf(".")){
    				responseVoInfo.setSimpleName(fullTypeStr.substring(fullTypeStr.lastIndexOf(".")+1));
    			}
    			if(null!=ResponseVoScanner.beanFieldsMap.get(fullTypeStr)){
    				responseVoInfo.setFields(ResponseVoScanner.beanFieldsMap.get(fullTypeStr));
    			}
    			//读取vo类描述信息
    			responseVoInfo.setDescription(AnnotationClassScanner.getClassDescripton(fullTypeStr));
    			returnClsList.add(responseVoInfo);
    			//循环递归添加实体字段中引用的自定义实体
    			AnnotationClassScanner.getReferVo(fullTypeStr, returnClsList);
    		}
    		if(-1==fullTypeStr.indexOf(".")){
    			if(isArray){//如果是数组
    				returnType.append("<").append(fullTypeStr).append("[]>");
    			}else{
    				returnType.append("<").append(fullTypeStr).append(">");
    			}
    		}else{
    			if(isArray){
    				returnType.append("<").append(fullTypeStr.substring(fullTypeStr.lastIndexOf(".")+1)).append("[]>");
    			}else{
    				returnType.append("<").append(fullTypeStr.substring(fullTypeStr.lastIndexOf(".")+1)).append(">");
    			}
    		}
    	}
    }
    
    /**
     * 循环递归添加实体字段中引用的自定义实体
     * @param className
     * @param returnClsList
     */
    public static void getReferVo(String className,List<ResponseVoInfo> returnClsList){
    	try {
			Class cls = Class.forName(className);
			Field[] fields = cls.getDeclaredFields();
			for(Field field:fields){
				if(Modifier.isStatic(field.getModifiers())){
					continue;
				}
				String fieldType = field.getType().getName();
				if(!AnnotationClassScanner.isSimpleParameterType(fieldType)){
					if(fieldType.equals("java.util.List")||fieldType.equals("java.util.Map")){
						Type type = field.getGenericType();
						if(type instanceof ParameterizedType){
							ParameterizedType parameterizedType = (ParameterizedType)type;
							//基本类型
							//Type basicType = parameterizedType.getRawType();  
				            //获取泛型类型
				            Type[] genericTypes = parameterizedType.getActualTypeArguments();  
				            for (Type t:genericTypes) {  
				                //判断泛型,简单类型不作处理,自定义类型处理
				            	String typeStr = t.toString();
				            	if(typeStr.startsWith("class")){
				            		typeStr = typeStr.replace("class", "").trim();
				            	}
				            	if(!AnnotationClassScanner.isSimpleParameterType(typeStr)){
				            		ResponseVoInfo responseVoInfo = new ResponseVoInfo(typeStr);
				            		if(-1!=typeStr.indexOf(".")){
				        				responseVoInfo.setSimpleName(typeStr.substring(typeStr.lastIndexOf(".")+1));
				        			}
									if(null!=ResponseVoScanner.beanFieldsMap.get(typeStr)){
					    				responseVoInfo.setFields(ResponseVoScanner.beanFieldsMap.get(typeStr));
					    			}
									//读取vo类描述信息
					    			responseVoInfo.setDescription(AnnotationClassScanner.getClassDescripton(typeStr));
									returnClsList.add(responseVoInfo);
									AnnotationClassScanner.getReferVo(typeStr, returnClsList);
				            	}
				            }  
						}
					}else if(field.getType().isArray()){
						String componentType = field.getType().getComponentType().toString();
                    	if(componentType.startsWith("class")){
                    		componentType = componentType.replace("class", "").trim();
		            	}
                    	if(componentType.startsWith("interface")){
                    		componentType = componentType.replace("interface", "").trim();
		            	}
                    	if(!AnnotationClassScanner.isSimpleParameterType(componentType)){
                    		ResponseVoInfo responseVoInfo = new ResponseVoInfo(componentType);
                    		if(-1!=componentType.indexOf(".")){
                    			responseVoInfo.setSimpleName(componentType.substring(componentType.lastIndexOf(".")+1));
                    		}
                    		if(null!=ResponseVoScanner.beanFieldsMap.get(componentType)){
                    			responseVoInfo.setFields(ResponseVoScanner.beanFieldsMap.get(componentType));
                    		}
                    		//读取vo类描述信息
			    			responseVoInfo.setDescription(AnnotationClassScanner.getClassDescripton(componentType));
                    		returnClsList.add(responseVoInfo);
                    		AnnotationClassScanner.getReferVo(componentType, returnClsList);
                    	}
					}else{
						ResponseVoInfo responseVoInfo = new ResponseVoInfo(fieldType);
						if(-1!=fieldType.indexOf(".")){
	        				responseVoInfo.setSimpleName(fieldType.substring(fieldType.lastIndexOf(".")+1));
	        			}
						if(null!=ResponseVoScanner.beanFieldsMap.get(fieldType)){
		    				responseVoInfo.setFields(ResponseVoScanner.beanFieldsMap.get(fieldType));
		    			}
						//读取vo类描述信息
		    			responseVoInfo.setDescription(AnnotationClassScanner.getClassDescripton(fieldType));
						returnClsList.add(responseVoInfo);
						AnnotationClassScanner.getReferVo(fieldType, returnClsList);
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 
     * @title: getClassDescripton 
     * @description: 读取类描述信息  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年3月30日 下午2:40:40  
     * @param classType
     * @return 
     * @return String 返回类型 
     * @throws
     */
    private static String getClassDescripton(String classType){
    	String descripton = "";
    	try {
			ApiModelDescription apiModelDescription = Class.forName(classType).getAnnotation(ApiModelDescription.class);
			if(null!=apiModelDescription){
				descripton = apiModelDescription.description();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return descripton;
    }
    
    /**
	 * 验证参数类型是否为简单类型 int,Integer等
	 * @param typeStr
	 * @return
	 */
    public static boolean isSimpleParameterType(String typeStr){
    	String simpleTypes = "java.lang.Object,java.lang.String,java.lang.Integer,int,java.lang.Long,long,java.lang.Float,float,java.lang.Double,double,java.lang.Boolean,boolean,java.lang.Byte,byte";
    	if(simpleTypes.contains(typeStr)){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**
	 * 验证返回类型是否为简单类型 int,Integer等
	 * @param typeStr
	 * @return
	 */
    public static boolean isSimpleReturnType(String typeStr){
    	String simpleTypes = "java.lang.String,java.lang.Integer,int,java.lang.Long,long,java.lang.Float,float,java.lang.Double,double,java.lang.Boolean,boolean,java.lang.Byte,byte,java.util.List,java.util.Map";
    	if(simpleTypes.contains(typeStr)){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**
     * 解析参数相关信息
     * @param annotations
     * @param isPathVar
     * @return
     */
    public static JSONObject getParameterInfo(Annotation[] annotations,boolean isPathVar){
    	JSONObject parameterInfo = new JSONObject();
    	Integer isRequired = 0;
		StringBuilder description = new StringBuilder();
		for(Annotation annotation:annotations){
			if(annotation instanceof NotEmpty){
				isRequired = 1;
				NotEmpty notEmpty = (NotEmpty)annotation;
				if(StringUtils.isNotEmpty(notEmpty.message())){
					description.append(notEmpty.message()).append(";");
				}
			}
			if(annotation instanceof NotNull){
				isRequired = 1;
				NotNull notNull = (NotNull)annotation;
				if(StringUtils.isNotEmpty(notNull.message())){
					description.append(notNull.message()).append(";");
				}
			}
			if(annotation instanceof Min){
				isRequired = 1;
				Min min = (Min)annotation;
				if(StringUtils.isNotEmpty(min.message())){
					description.append(min.message()).append(";");
				}
			}
			if(annotation instanceof ApiParam){
				ApiParam apiParam = (ApiParam)annotation;
				if(apiParam.required()){
					isRequired = 1;
				}
				if(StringUtils.isNotEmpty(apiParam.value())){
					description = new StringBuilder(apiParam.value());
				}
			}
			if(isPathVar){
				if(annotation instanceof PathVariable){
					isRequired = 1;
				}
			}
		}
		parameterInfo.put("isRequired", isRequired);
		parameterInfo.put("description", description.toString());
		return parameterInfo;
    }
    
    /**
     * 清空api数据信息
     */
    public void clearData(){
    	this.packageList.clear();
		this.typeFilters.clear();
		this.classList.clear();
		AnnotationClassScanner.classApiIdIndex = 0;
		AnnotationClassScanner.methodApiIdIndex = 0;
		AnnotationClassScanner.apisMap.clear();
		AnnotationClassScanner.apiClassList.clear();
		AnnotationClassScanner.apiClassMap.clear();
		AnnotationClassScanner.apiMethodMap.clear();
		AnnotationClassScanner.apiGroupsMap.clear();
    }
    
}
