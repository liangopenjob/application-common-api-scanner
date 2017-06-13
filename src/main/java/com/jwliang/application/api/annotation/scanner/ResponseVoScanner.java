package com.jwliang.application.api.annotation.scanner;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import com.jwliang.application.api.annotation.definition.ApiModelProperty;
import com.jwliang.application.api.annotation.definition.mocker.BooleanArrayMock;
import com.jwliang.application.api.annotation.definition.mocker.BooleanMock;
import com.jwliang.application.api.annotation.definition.mocker.ByteArrayMock;
import com.jwliang.application.api.annotation.definition.mocker.ByteMock;
import com.jwliang.application.api.annotation.definition.mocker.DoubleArrayMock;
import com.jwliang.application.api.annotation.definition.mocker.DoubleMock;
import com.jwliang.application.api.annotation.definition.mocker.FloatArrayMock;
import com.jwliang.application.api.annotation.definition.mocker.FloatMock;
import com.jwliang.application.api.annotation.definition.mocker.IntArrayMock;
import com.jwliang.application.api.annotation.definition.mocker.IntMock;
import com.jwliang.application.api.annotation.definition.mocker.LongArrayMock;
import com.jwliang.application.api.annotation.definition.mocker.LongMock;
import com.jwliang.application.api.annotation.definition.mocker.OtherArrayMock;
import com.jwliang.application.api.annotation.definition.mocker.OtherMock;
import com.jwliang.application.api.annotation.definition.mocker.StringArrayMock;
import com.jwliang.application.api.annotation.definition.mocker.StringMock;
import com.jwliang.application.api.annotation.scanner.bean.ResponseVoField;
import com.jwliang.application.api.annotation.scanner.bean.mock.BooleanArrayMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.BooleanMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.ByteArrayMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.ByteMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.DoubleArrayMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.DoubleMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.FloatArrayMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.FloatMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.IntArrayMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.IntMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.LongArrayMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.LongMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.OtherArrayMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.OtherMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.StringArrayMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.StringMockType;
import com.jwliang.application.api.annotation.scanner.bean.mock.VoFieldMockType;
import com.jwliang.application.api.annotation.scanner.common.KeyConstant;
import com.jwliang.application.api.annotation.scanner.common.ResponseVo;
import com.jwliang.application.api.annotation.scanner.utils.CommonUtil;
/**
 * 
 * @ClassName: ResponseVoScanner  
 * @Description: 扫描vo主体类
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ResponseVoScanner {
	private static final String RESOURCE_PATTERN = "/**/*.class";

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private List<String> packageList = new LinkedList<String>();

	private List<TypeFilter> typeFilters = new LinkedList<TypeFilter>();

	private List<Class<?>> classList = new ArrayList<Class<?>>();

	public static final Map<String, List<ResponseVoField>> beanFieldsMap = new HashMap<String, List<ResponseVoField>>();

	/** 
	 * 构造函数 
	 * @param packagesToScan 指定哪些包需要被扫描,支持多个包"package.a,package.b"并对每个包都会递归搜索 
	 * @param filters 指定扫描包中含有特定注解标记的bean,支持多个注解 
	 */
	public ResponseVoScanner(String[] packagesToScan,
			Class<? extends Annotation>... filters) {
		this.clearData();
		if (packagesToScan != null) {
			for (String packagePath : packagesToScan) {
				this.packageList.add(packagePath);
			}
		}
		if (filters != null) {
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
	public List<Class<?>> getClassList() throws IOException,
			ClassNotFoundException {
		this.classList.clear();
		if (!this.packageList.isEmpty()) {
			for (String pkg : this.packageList) {
				String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
						+ ClassUtils.convertClassNameToResourcePath(pkg)
						+ RESOURCE_PATTERN;
				Resource[] resources = this.resourcePatternResolver
						.getResources(pattern);
				MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(
						this.resourcePatternResolver);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						MetadataReader reader = readerFactory
								.getMetadataReader(resource);
						String className = reader.getClassMetadata()
								.getClassName();
						if (this.matchesEntityTypeFilter(reader, readerFactory)) {
							Class cls = Class.forName(className);
							this.classList.add(cls);
							//解析实体bean字段
							List<ResponseVoField> fieldList = new ArrayList<ResponseVoField>();
							Field[] fields = cls.getDeclaredFields();
							//如果参数继承其它Class获取父级Field
							if(!cls.getSuperclass().equals(Object.class)){
								List<Field> fList = new ArrayList<Field>();
								CommonUtil.setHierarchyFields(cls, fList);
								fields = fList.toArray(new Field[fList.size()]);
							}
							for (Field field : fields) {
								if (Modifier.isStatic(field.getModifiers())) {
									continue;
								}
								ResponseVoField responseVoField = new ResponseVoField(
										field.getName());
								String fieldType;
								String simpleFieldType;
								boolean isArray = field.getType().isArray();
								if (isArray) {
									String componentType = field.getType()
											.getComponentType().toString();
									if (componentType.startsWith("class")) {
										componentType = componentType.replace(
												"class", "").trim();
									}
									if (componentType.startsWith("interface")) {
										componentType = componentType.replace(
												"interface", "").trim();
									}
									fieldType = componentType + "[]";
									if (-1 == componentType.indexOf(".")) {
										simpleFieldType = fieldType;
									} else {
										simpleFieldType = componentType
												.substring(componentType
														.lastIndexOf(".") + 1)
												+ "[]";
									}
								} else {
									fieldType = field.getGenericType()
											.toString();
									if (fieldType.startsWith("class")) {
										fieldType = fieldType.replace("class",
												"").trim();
									}
									if (fieldType.startsWith("interface")) {
										fieldType = fieldType.replace(
												"interface", "").trim();
									}
									simpleFieldType = fieldType;
									if (-1 != fieldType.indexOf("<")) {
										String preType = fieldType.substring(0,
												fieldType.indexOf("<"));
										simpleFieldType = fieldType
												.substring(preType
														.lastIndexOf(".") + 1);
									} else {
										if (-1 != fieldType.indexOf(".")) {
											simpleFieldType = fieldType
													.substring(fieldType
															.lastIndexOf(".") + 1);
										}
									}
								}
								responseVoField.setType(fieldType);
								responseVoField.setSimpleType(simpleFieldType);
								if (field
										.isAnnotationPresent(ApiModelProperty.class)) {
									ApiModelProperty apiModelProperty = field
											.getAnnotation(ApiModelProperty.class);
									responseVoField
											.setDescription(apiModelProperty
													.value());
									responseVoField
											.setIsAllowEmpty(apiModelProperty
													.required() ? 1 : 0);
								}
								//设置field注解信息
								responseVoField.setVoFieldMockType(this.getVoFieldMockType(field));
								fieldList.add(responseVoField);
							}
							ResponseVoScanner.beanFieldsMap.put(className,
									fieldList);
						}
					}
				}
			}
		}
		this.addResponseVoField();
		return this.classList;
	}

	/**
	 * 添加公共结构ResponseVo字段信息
	 */
	public void addResponseVoField() {
		List<ResponseVoField> fieldList = new ArrayList<ResponseVoField>();
		Field[] fields = ResponseVo.class.getDeclaredFields();
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			ResponseVoField responseVoField = new ResponseVoField(
					field.getName());
			String fieldType = field.getType().getName();
			String simpleFieldType = fieldType;
			if (-1 != fieldType.indexOf(".")) {
				simpleFieldType = fieldType.substring(fieldType
						.lastIndexOf(".") + 1);
			}
			responseVoField.setType(fieldType);
			responseVoField.setSimpleType(simpleFieldType);
			if (field.isAnnotationPresent(ApiModelProperty.class)) {
				ApiModelProperty apiModelProperty = field
						.getAnnotation(ApiModelProperty.class);
				responseVoField.setDescription(apiModelProperty.value());
				responseVoField.setIsAllowEmpty(apiModelProperty.required() ? 1
						: 0);
			}
			if(KeyConstant.KEY_RESULT_STATUS.equals(field.getName())){
				responseVoField.setDescription("状态编码");
				responseVoField.setIsAllowEmpty(0);
			}else if(KeyConstant.KEY_RESULT_MESSAGE.equals(field.getName())){
				responseVoField.setDescription("提示信息");
				responseVoField.setIsAllowEmpty(0);
			}else if(KeyConstant.KEY_RESULT_DATA.equals(field.getName())){
				responseVoField.setDescription("结果数据");
				responseVoField.setIsAllowEmpty(1);
			}
			fieldList.add(responseVoField);
		}
		ResponseVoScanner.beanFieldsMap.put(ResponseVo.class.getName(),
				fieldList);
	}

	/**
	 * 清空Vo数据信息
	 */
	public void clearData() {
		this.packageList.clear();
		this.typeFilters.clear();
		this.classList.clear();
		ResponseVoScanner.beanFieldsMap.clear();
	}

	/** 
	 * 检查当前扫描到的Bean含有任何一个指定的注解标记 
	 * @param reader 
	 * @param readerFactory 
	 * @return 
	 * @throws IOException 
	 */
	private boolean matchesEntityTypeFilter(MetadataReader reader,
			MetadataReaderFactory readerFactory) throws IOException {
		if (!this.typeFilters.isEmpty()) {
			for (TypeFilter filter : this.typeFilters) {
				if (filter.match(reader, readerFactory)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @title: getVoFieldMockType 
	 * @description: 获取字段mock注解信息,只读取配置一个mock注解的信息,一个字段配置多个mock注解将失效 
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com  
	 * @time: 2017年2月27日 下午2:10:05  
	 * @param field
	 * @return 
	 * @return VoFieldMockType 返回类型 
	 * @throws
	 */
	private VoFieldMockType getVoFieldMockType(Field field){
		Annotation[] annotations = field.getAnnotations();
		if(null==annotations||0==annotations.length){
			return null;
		}
		List<VoFieldMockType> mockTypes = new ArrayList<VoFieldMockType>();
		for(Annotation an:annotations){
			if(an instanceof BooleanMock){
				BooleanMock booleanMock = (BooleanMock)an;
				BooleanMockType booleanMockType = new BooleanMockType();
				booleanMockType.setBooleanStr(booleanMock.booleanStr());
				booleanMockType.setDescription(booleanMock.description());
				mockTypes.add(booleanMockType);
			}else if(an instanceof BooleanArrayMock){
				BooleanArrayMock booleanArrayMock = (BooleanArrayMock)an;
				BooleanArrayMockType booleanArrayMockType = new BooleanArrayMockType();
				booleanArrayMockType.setBooleanStr(booleanArrayMock.booleanStr());
				booleanArrayMockType.setArrayLength(booleanArrayMock.arrayLength());
				booleanArrayMockType.setDescription(booleanArrayMock.description());
				mockTypes.add(booleanArrayMockType);
			}else if(an instanceof ByteMock){
				ByteMock byteMock = (ByteMock)an;
				ByteMockType byteMockType = new ByteMockType();
				byteMockType.setByteStr(byteMock.byteStr());
				byteMockType.setDescription(byteMock.description());
				mockTypes.add(byteMockType);
			}else if(an instanceof ByteArrayMock){
				ByteArrayMock byteArrayMock = (ByteArrayMock)an;
				ByteArrayMockType byteArrayMockType = new ByteArrayMockType();
				byteArrayMockType.setByteStr(byteArrayMock.byteStr());
				byteArrayMockType.setArrayLength(byteArrayMock.arrayLength());
				byteArrayMockType.setDescription(byteArrayMock.description());
				mockTypes.add(byteArrayMockType);
			}else if(an instanceof IntMock){
				IntMock intMock = (IntMock)an;
				IntMockType intMockType = new IntMockType();
				intMockType.setIntStr(intMock.intStr());
				intMockType.setDescription(intMock.description());
				mockTypes.add(intMockType);
			}else if(an instanceof IntArrayMock){
				IntArrayMock intArrayMock = (IntArrayMock)an;
				IntArrayMockType intArrayMockType = new IntArrayMockType();
				intArrayMockType.setIntStr(intArrayMock.intStr());
				intArrayMockType.setArrayLength(intArrayMock.arrayLength());
				intArrayMockType.setDescription(intArrayMock.description());
				mockTypes.add(intArrayMockType);
			}else if(an instanceof LongMock){
				LongMock longMock = (LongMock)an;
				LongMockType longMockType = new LongMockType();
				longMockType.setLongStr(longMock.longStr());
				longMockType.setDescription(longMock.description());
				mockTypes.add(longMockType);
			}else if(an instanceof LongArrayMock){
				LongArrayMock longArrayMock = (LongArrayMock)an;
				LongArrayMockType longArrayMockType = new LongArrayMockType();
				longArrayMockType.setLongStr(longArrayMock.longStr());
				longArrayMockType.setArrayLength(longArrayMock.arrayLength());
				longArrayMockType.setDescription(longArrayMock.description());
				mockTypes.add(longArrayMockType);
			}else if(an instanceof FloatMock){
				FloatMock floatMock = (FloatMock)an;
				FloatMockType floatMockType = new FloatMockType();
				floatMockType.setFloatStr(floatMock.floatStr());
				floatMockType.setStart(floatMock.start());
				floatMockType.setEnd(floatMock.end());
				floatMockType.setDecimal(floatMock.decimal());
				floatMockType.setDescription(floatMock.description());
				mockTypes.add(floatMockType);
			}else if(an instanceof FloatArrayMock){
				FloatArrayMock floatArrayMock = (FloatArrayMock)an;
				FloatArrayMockType floatArrayMockType = new FloatArrayMockType();
				floatArrayMockType.setFloatStr(floatArrayMock.floatStr());
				floatArrayMockType.setStart(floatArrayMock.start());
				floatArrayMockType.setEnd(floatArrayMock.end());
				floatArrayMockType.setDecimal(floatArrayMock.decimal());
				floatArrayMockType.setArrayLength(floatArrayMock.arrayLength());
				floatArrayMockType.setDescription(floatArrayMock.description());
				mockTypes.add(floatArrayMockType);
			}else if(an instanceof DoubleMock){
				DoubleMock doubleMock = (DoubleMock)an;
				DoubleMockType doubleMockType = new DoubleMockType();
				doubleMockType.setDoubleStr(doubleMock.doubleStr());
				doubleMockType.setStart(doubleMock.start());
				doubleMockType.setEnd(doubleMock.end());
				doubleMockType.setDecimal(doubleMock.decimal());
				doubleMockType.setDescription(doubleMock.description());
				mockTypes.add(doubleMockType);
			}else if(an instanceof DoubleArrayMock){
				DoubleArrayMock doubleArrayMock = (DoubleArrayMock)an;
				DoubleArrayMockType doubleArrayMockType = new DoubleArrayMockType();
				doubleArrayMockType.setDoubleStr(doubleArrayMock.doubleStr());
				doubleArrayMockType.setStart(doubleArrayMock.start());
				doubleArrayMockType.setEnd(doubleArrayMock.end());
				doubleArrayMockType.setDecimal(doubleArrayMock.decimal());
				doubleArrayMockType.setArrayLength(doubleArrayMock.arrayLength());
				doubleArrayMockType.setDescription(doubleArrayMock.description());
				mockTypes.add(doubleArrayMockType);
			}else if(an instanceof StringMock){
				StringMock stringMock = (StringMock)an;
				StringMockType stringMockType = new StringMockType();
				stringMockType.setMockType(stringMock.mockType().getValue());
				stringMockType.setStringStr(stringMock.stringStr());
				stringMockType.setLength(stringMock.length());
				stringMockType.setDescription(stringMock.description());
				mockTypes.add(stringMockType);
			}else if(an instanceof StringArrayMock){
				StringArrayMock stringArrayMock = (StringArrayMock)an;
				StringArrayMockType stringArrayMockType = new StringArrayMockType();
				stringArrayMockType.setMockType(stringArrayMock.mockType().getValue());
				stringArrayMockType.setStringStr(stringArrayMock.stringStr());
				stringArrayMockType.setLength(stringArrayMock.length());
				stringArrayMockType.setArrayLength(stringArrayMock.arrayLength());
				stringArrayMockType.setDescription(stringArrayMock.description());
				mockTypes.add(stringArrayMockType);
			}else if(an instanceof OtherMock){
				OtherMock otherMock = (OtherMock)an;
				OtherMockType otherMockType = new OtherMockType();
				otherMockType.setMockType(otherMock.mockType().getValue());
				otherMockType.setOtherStr(otherMock.otherStr());
				otherMockType.setDescription(otherMock.description());
				mockTypes.add(otherMockType);
			}else if(an instanceof OtherArrayMock){
				OtherArrayMock otherArrayMock = (OtherArrayMock)an;
				OtherArrayMockType otherArrayMockType = new OtherArrayMockType();
				otherArrayMockType.setMockType(otherArrayMock.mockType().getValue());
				otherArrayMockType.setOtherStr(otherArrayMock.otherStr());
				otherArrayMockType.setArrayLength(otherArrayMock.arrayLength());
				otherArrayMockType.setDescription(otherArrayMock.description());
				mockTypes.add(otherArrayMockType);
			}
		}
		if(1!=mockTypes.size()){
			return null;
		}else{
			return mockTypes.get(0);
		}
	}
	
}
