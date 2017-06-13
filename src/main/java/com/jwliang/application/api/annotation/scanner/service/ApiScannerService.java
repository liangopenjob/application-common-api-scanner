package com.jwliang.application.api.annotation.scanner.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jwliang.application.api.annotation.definition.Api;
import com.jwliang.application.api.annotation.definition.mocker.OtherMockerEnum;
import com.jwliang.application.api.annotation.definition.mocker.StringMockerEnum;
import com.jwliang.application.api.annotation.scanner.AnnotationClassScanner;
import com.jwliang.application.api.annotation.scanner.ResponseVoScanner;
import com.jwliang.application.api.annotation.scanner.bean.ApiMethodAnnotationInfo;
import com.jwliang.application.api.annotation.scanner.bean.ResponseVoField;
import com.jwliang.application.api.annotation.scanner.bean.ResponseVoInfo;
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
import com.jwliang.application.api.annotation.scanner.utils.RandomDataUtil;
/**
 * 
 * @ClassName: ApiScannerService  
 * @Description: Api扫描服务类定义
 * @author: liangjunwei5
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年1月22日
 *
 */

@SuppressWarnings("unchecked")
public class ApiScannerService implements InitializingBean {
	
	private final static Logger logger = LoggerFactory.getLogger(ApiScannerService.class);
	
	public static String docTitle = "智慧开放Rest Api Docs";//标题不能超过18个字符
	
	public static boolean isLocked;
	
	public static List<String> whiteIpList = new ArrayList<String>();//白名单ip列表
	
	public static String cpWhiteIpList;//拷贝白名单列表
	
	private String[] voPackages;
	
	private String[] apiPackages;

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info(" scan api start ");
		//扫描vo信息
		if(null==voPackages||voPackages.length==0){
			throw new RuntimeException("vo packages is empty!");
		}
		new ResponseVoScanner(voPackages).getClassList();
		//扫描api信息
		if(null==apiPackages||apiPackages.length==0){
			throw new RuntimeException("api packages is empty!");
		}
		new AnnotationClassScanner(apiPackages, Api.class).getClassList();
		logger.info(" scan api end ");
	}
	
	public static String getDocTitle() {
		return docTitle;
	}

	public static void setDocTitle(String docTitle) {
		if(null!=docTitle){
			if(docTitle.length()>18){
				docTitle = docTitle.substring(0, 18);
			}
			ApiScannerService.docTitle = docTitle;
		}
	}
	
	public static boolean isLocked() {
		return isLocked;
	}

	public static void setLocked(boolean isLocked) {
		ApiScannerService.isLocked = isLocked;
	}

	public static List<String> getWhiteIpList() {
		return whiteIpList;
	}

	public static void setWhiteIpList(List<String> whiteIpList) {
		ApiScannerService.whiteIpList = whiteIpList;
	}

	public static String getCpWhiteIpList() {
		return cpWhiteIpList;
	}

	public static void setCpWhiteIpList(String cpWhiteIpList) {
		ApiScannerService.cpWhiteIpList = cpWhiteIpList;
	}

	public String[] getVoPackages() {
		return voPackages;
	}

	public void setVoPackages(String[] voPackages) {
		this.voPackages = voPackages;
	}

	public String[] getApiPackages() {
		return apiPackages;
	}

	public void setApiPackages(String[] apiPackages) {
		this.apiPackages = apiPackages;
	}
	
	/**
	 * 获取返回自定义类型的json样例
	 * @param apiMethodAnnotationInfo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String,JSONObject> getExampleJson(ApiMethodAnnotationInfo apiMethodAnnotationInfo) {
		Map<String,JSONObject> returnClsJson = new HashMap<String,JSONObject>();
		List<ResponseVoInfo> voInfos = apiMethodAnnotationInfo.getReturnClsList();
		for(int k=voInfos.size()-1;k>0;k--){
			JSONObject json  = new JSONObject();
			ResponseVoInfo voInfo = voInfos.get(k);
			List<ResponseVoField> voFields = voInfo.getFields();
			for(ResponseVoField voField:voFields){
				VoFieldMockType voFilFieldMockType = voField.getVoFieldMockType();
				if("java.lang.String".equals(voField.getType())){
					json.put(voField.getName(), "string");
				    putStringValue(voFilFieldMockType, json, voField.getName());
				}else if("java.lang.String[]".contains(voField.getType())){
					json.put(voField.getName(), new String[]{"string","string","string"});
					putStringArrayValue(voFilFieldMockType, json, voField.getName());
				}else if("java.lang.Boolean,boolean".contains(voField.getType())){
					json.put(voField.getName(), false);
					putBooleanValue(voFilFieldMockType, json, voField.getName());
				}else if("java.lang.Byte,byte".contains(voField.getType())){
					json.put(voField.getName(), 0);
					putByteValue(voFilFieldMockType, json, voField.getName());
				}else if("java.lang.Integer,int".contains(voField.getType())){
					json.put(voField.getName(), 0);
					putIntValue(voFilFieldMockType, json, voField.getName());
				}else if("java.lang.Long,long".contains(voField.getType())){
					json.put(voField.getName(), 0);
					putLongValue(voFilFieldMockType, json, voField.getName());
				}else if("java.lang.Float,float".contains(voField.getType())){
					json.put(voField.getName(), 0.1F);
					putFloatValue(voFilFieldMockType, json, voField.getName());
				}else if("java.lang.Double,double".contains(voField.getType())){
					json.put(voField.getName(), 0.1F);
					putDoubleValue(voFilFieldMockType, json, voField.getName());
				}else if("java.lang.Integer[],int[]".contains(voField.getType())){
					json.put(voField.getName(), new int[]{0,0,0});
					putIntArrayValue(voFilFieldMockType, json, voField.getName());
				}else if("java.lang.Long[],long[]".contains(voField.getType())){
					json.put(voField.getName(), new long[]{0L,0L,0L});
					putLongArrayValue(voFilFieldMockType, json, voField.getName());
				}else if("java.lang.Float[],float[]".contains(voField.getType())){
					json.put(voField.getName(), new float[]{0.1F,0.1F,0.1F});
					putFloatArrayValue(voFilFieldMockType, json, voField.getName());
				}else if("java.lang.Double[],double[]".contains(voField.getType())){
					json.put(voField.getName(), new double[]{0.1D,0.1D,0.1D});
					putDoubleArrayValue(voFilFieldMockType, json, voField.getName());
				}else if("java.lang.Map[]".contains(voField.getType())){
					json.put(voField.getName(), new Map[]{new HashMap(),new HashMap(),new HashMap()});
				}else{
					JSONObject tempOneJson = returnClsJson.get(voField.getType());
					if(null!=tempOneJson){
						json.put(voField.getName(), tempOneJson);
					}else{
						String type = voField.getType().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
						if(type.startsWith("java.util.List")){
							JSONArray jsonArray = new JSONArray();
							String tempType = type.substring(type.indexOf("<")+1, type.length()-1);
							int flag = 0;
							if("java.lang.String".equals(tempType)){
								jsonArray.add("string");
								jsonArray.add("string");
								jsonArray.add("string");
								flag = 1;
							}else if("java.lang.Boolean,boolean".contains(tempType)){
								jsonArray.add(false);
								jsonArray.add(false);
								jsonArray.add(false);
								flag = 2;
							}else if("java.lang.Byte,byte".contains(tempType)){
								jsonArray.add(0);
								jsonArray.add(0);
								jsonArray.add(0);
								flag = 3;
							}else if("java.lang.Integer,int".contains(tempType)){
								jsonArray.add(0);
								jsonArray.add(0);
								jsonArray.add(0);
								flag = 4;
							}else if("java.lang.Long,long".contains(tempType)){
								jsonArray.add(0);
								jsonArray.add(0);
								jsonArray.add(0);
								flag = 5;
							}else if("java.lang.Float,float".contains(tempType)){
								jsonArray.add(0.1F);
								jsonArray.add(0.1F);
								jsonArray.add(0.1F);
								flag = 6;
							}else if("java.lang.Double,double".contains(tempType)){
								jsonArray.add(0.1F);
								jsonArray.add(0.1F);
								jsonArray.add(0.1F);
								flag = 7;
							}else{
								JSONObject tempTwoJson = returnClsJson.get(tempType);
								if(null!=tempTwoJson){
									jsonArray.add(tempTwoJson);
								}
							}
							json.put(voField.getName(), jsonArray);
							if(flag==1){
								putStringArrayValue(voFilFieldMockType, json, voField.getName());
							}else if(flag==2){
								putBooleanArrayValue(voFilFieldMockType, json, voField.getName());
							}else if(flag==3){
								putByteArrayValue(voFilFieldMockType, json, voField.getName());
							}else if(flag==4){
								putIntArrayValue(voFilFieldMockType, json, voField.getName());
							}else if(flag==5){
								putLongArrayValue(voFilFieldMockType, json, voField.getName());
							}else if(flag==6){
								putFloatArrayValue(voFilFieldMockType, json, voField.getName());
							}else if(flag==7){
								putDoubleArrayValue(voFilFieldMockType, json, voField.getName());
							}
						}else if(type.endsWith("[]")){
							JSONArray jsonArray = new JSONArray();
							String tempType = type.substring(0, type.indexOf("["));
							JSONObject tempTwoJson = returnClsJson.get(tempType);
							if(null!=tempTwoJson){
								jsonArray.add(tempTwoJson);
							}
							json.put(voField.getName(), jsonArray);
						}
					}
				}
			}
			returnClsJson.put(voInfo.getName(), json);
		}
		return returnClsJson;
	}
	
	/**
	 * 根据method获取返回样例信息
	 * @param apiMethodAnnotationInfo
	 * @return
	 */
	public static JSONObject getCommonExampleJson(ApiMethodAnnotationInfo apiMethodAnnotationInfo){
		Map<String,JSONObject> returnClsJson = ApiScannerService.getExampleJson(apiMethodAnnotationInfo);
		JSONObject exampleJson = new JSONObject();
		exampleJson.put("status", 200);
		exampleJson.put("message", "成功");
		//此处可以处理ResponseVo<String>、ResponseVo<Integer>等简单类型
		String returnDataType = apiMethodAnnotationInfo.getReturnDataType();
		if("String".equals(returnDataType)){
			exampleJson.put("data", "string");
		}else if("Boolean".equals(returnDataType)){
			exampleJson.put("data", false);
		}else if("Byte".equals(returnDataType)){
			exampleJson.put("data", 0);
		}else if("Integer".equals(returnDataType)){
			exampleJson.put("data", 0);
		}else if("Long".equals(returnDataType)){
			exampleJson.put("data", 0);
		}else if("Float".equals(returnDataType)){
			exampleJson.put("data", 0.1);
		}else if("Double".equals(returnDataType)){
			exampleJson.put("data", 0.1);
		}else{
			//处理ResponseVo类型
			if(!returnDataType.equals(apiMethodAnnotationInfo.getReturnType())){
				String genericTypeName = apiMethodAnnotationInfo.getReturnClsList().get(1).getName();
				if(-1==apiMethodAnnotationInfo.getReturnType().indexOf("&lt;List&lt;")&&-1==apiMethodAnnotationInfo.getReturnType().indexOf("[]")){
					exampleJson.put("data", returnClsJson.get(genericTypeName));
				}else{
					JSONArray jsonArray = new JSONArray();
					jsonArray.add(returnClsJson.get(genericTypeName));
					exampleJson.put("data", jsonArray);
				}
			}
		}
		return exampleJson;
	}

	private static void putStringValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof StringMockType){
			StringMockType stringMockType = (StringMockType)voFieldMockType;
			if(stringMockType.getMockType()==StringMockerEnum.RANDOM_NUM_STR.getValue()){
				json.put(fieldName, RandomDataUtil.randomNumberStr(stringMockType.getLength()));
			}else if(stringMockType.getMockType()==StringMockerEnum.RANDOM_LETTER_STR.getValue()){
				json.put(fieldName, RandomDataUtil.randomLetterStr(stringMockType.getLength()));
			}else if(stringMockType.getMockType()==StringMockerEnum.RANDOM_LETTER_UPPER_STR.getValue()){
				json.put(fieldName, RandomDataUtil.randomUpperLetterStr(stringMockType.getLength()));
			}else if(stringMockType.getMockType()==StringMockerEnum.RANDOM_LETTER_LOWER_STR.getValue()){
				json.put(fieldName, RandomDataUtil.randomLowerLetterStr(stringMockType.getLength()));
			}else if(stringMockType.getMockType()==StringMockerEnum.RANDOM_ALL_CHAR_STR.getValue()){
				json.put(fieldName, RandomDataUtil.randomAllCharStr(stringMockType.getLength()));
			}else if(stringMockType.getMockType()==StringMockerEnum.RANDOM_CH_STR.getValue()){
				json.put(fieldName, RandomDataUtil.randomChStr(stringMockType.getLength()));
			}
			setRandomString(stringMockType.getStringStr(), json, fieldName);
		}else if(voFieldMockType instanceof OtherMockType){
			OtherMockType otherMockType = (OtherMockType)voFieldMockType;
			if(otherMockType.getMockType()==OtherMockerEnum.RANDOM_ID.getValue()){
				json.put(fieldName, RandomDataUtil.randomIdStr());
			}else if(otherMockType.getMockType()==OtherMockerEnum.RANDOM_PHONE.getValue()){
				json.put(fieldName, RandomDataUtil.randomPhoneStr());
			}else if(otherMockType.getMockType()==OtherMockerEnum.RANDOM_EMAIL.getValue()){
				json.put(fieldName, RandomDataUtil.randomEmailStr());
			}else if(otherMockType.getMockType()==OtherMockerEnum.RANDOM_CH_NAME.getValue()){
				json.put(fieldName, RandomDataUtil.randomChName());
			}else if(otherMockType.getMockType()==OtherMockerEnum.RANDOM_ADDRESS.getValue()){
				json.put(fieldName, RandomDataUtil.randomAddress());
			}
			setRandomString(otherMockType.getOtherStr(), json, fieldName);
		}
	}
	
	private static void putStringArrayValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof StringArrayMockType){
			StringArrayMockType stringArrayMockType = (StringArrayMockType)voFieldMockType;
			String[] stringArray = new String[stringArrayMockType.getArrayLength()];
			for(int i=0;i<stringArray.length;i++){
				if(stringArrayMockType.getMockType()==StringMockerEnum.RANDOM_NUM_STR.getValue()){
					stringArray[i] = RandomDataUtil.randomNumberStr(stringArrayMockType.getLength());
				}else if(stringArrayMockType.getMockType()==StringMockerEnum.RANDOM_LETTER_STR.getValue()){
					stringArray[i] = RandomDataUtil.randomLetterStr(stringArrayMockType.getLength());
				}else if(stringArrayMockType.getMockType()==StringMockerEnum.RANDOM_LETTER_UPPER_STR.getValue()){
					stringArray[i] = RandomDataUtil.randomUpperLetterStr(stringArrayMockType.getLength());
				}else if(stringArrayMockType.getMockType()==StringMockerEnum.RANDOM_LETTER_LOWER_STR.getValue()){
					stringArray[i] = RandomDataUtil.randomLowerLetterStr(stringArrayMockType.getLength());
				}else if(stringArrayMockType.getMockType()==StringMockerEnum.RANDOM_ALL_CHAR_STR.getValue()){
					stringArray[i] = RandomDataUtil.randomAllCharStr(stringArrayMockType.getLength());
				}else if(stringArrayMockType.getMockType()==StringMockerEnum.RANDOM_CH_STR.getValue()){
					stringArray[i] = RandomDataUtil.randomChStr(stringArrayMockType.getLength());
				}
				if(StringUtils.isNotEmpty(stringArrayMockType.getStringStr())){
					stringArray[i] = RandomDataUtil.randomString(stringArrayMockType.getStringStr());
				}
			}
			json.put(fieldName, stringArray);
			
//			if(stringArrayMockType.getMockType()==StringMockerEnum.RANDOM_NUM_STR.getValue()){
//				for(int i=0;i<stringArray.length;i++){
//					stringArray[i] = RandomDataUtil.randomNumberStr(stringArrayMockType.getLength());
//					if(StringUtils.isNotEmpty(stringArrayMockType.getStringStr())){
//						stringArray[i] = RandomDataUtil.randomString(stringArrayMockType.getStringStr());
//					}
//				}
//				json.put(fieldName, stringArray);
//			}else if(stringArrayMockType.getMockType()==StringMockerEnum.RANDOM_LETTER_STR.getValue()){
//				for(int i=0;i<stringArray.length;i++){
//					stringArray[i] = RandomDataUtil.randomLetterStr(stringArrayMockType.getLength());
//					if(StringUtils.isNotEmpty(stringArrayMockType.getStringStr())){
//						stringArray[i] = RandomDataUtil.randomString(stringArrayMockType.getStringStr());
//					}
//				}
//				json.put(fieldName, stringArray);
//			}else if(stringArrayMockType.getMockType()==StringMockerEnum.RANDOM_LETTER_UPPER_STR.getValue()){
//				for(int i=0;i<stringArray.length;i++){
//					stringArray[i] = RandomDataUtil.randomUpperLetterStr(stringArrayMockType.getLength());
//					if(StringUtils.isNotEmpty(stringArrayMockType.getStringStr())){
//						stringArray[i] = RandomDataUtil.randomString(stringArrayMockType.getStringStr());
//					}
//				}
//				json.put(fieldName, stringArray);
//			}else if(stringArrayMockType.getMockType()==StringMockerEnum.RANDOM_LETTER_LOWER_STR.getValue()){
//				for(int i=0;i<stringArray.length;i++){
//					stringArray[i] = RandomDataUtil.randomLowerLetterStr(stringArrayMockType.getLength());
//					if(StringUtils.isNotEmpty(stringArrayMockType.getStringStr())){
//						stringArray[i] = RandomDataUtil.randomString(stringArrayMockType.getStringStr());
//					}
//				}
//				json.put(fieldName, stringArray);
//			}else if(stringArrayMockType.getMockType()==StringMockerEnum.RANDOM_ALL_CHAR_STR.getValue()){
//				for(int i=0;i<stringArray.length;i++){
//					stringArray[i] = RandomDataUtil.randomAllCharStr(stringArrayMockType.getLength());
//					if(StringUtils.isNotEmpty(stringArrayMockType.getStringStr())){
//						stringArray[i] = RandomDataUtil.randomString(stringArrayMockType.getStringStr());
//					}
//				}
//				json.put(fieldName, stringArray);
//			}else if(stringArrayMockType.getMockType()==StringMockerEnum.RANDOM_CH_STR.getValue()){
//				for(int i=0;i<stringArray.length;i++){
//					stringArray[i] = RandomDataUtil.randomChStr(stringArrayMockType.getLength());
//					if(StringUtils.isNotEmpty(stringArrayMockType.getStringStr())){
//						stringArray[i] = RandomDataUtil.randomString(stringArrayMockType.getStringStr());
//					}
//				}
//				json.put(fieldName, stringArray);
//			}
		}else if(voFieldMockType instanceof OtherArrayMockType){
			OtherArrayMockType otherArrayMockType = (OtherArrayMockType)voFieldMockType;
			String[] otherArray = new String[otherArrayMockType.getArrayLength()];
			for(int i=0;i<otherArray.length;i++){
				if(otherArrayMockType.getMockType()==OtherMockerEnum.RANDOM_ID.getValue()){
					otherArray[i] = RandomDataUtil.randomIdStr();
				}else if(otherArrayMockType.getMockType()==OtherMockerEnum.RANDOM_PHONE.getValue()){
					otherArray[i] = RandomDataUtil.randomPhoneStr();
				}else if(otherArrayMockType.getMockType()==OtherMockerEnum.RANDOM_EMAIL.getValue()){
					otherArray[i] = RandomDataUtil.randomEmailStr();
				}else if(otherArrayMockType.getMockType()==OtherMockerEnum.RANDOM_CH_NAME.getValue()){
					otherArray[i] = RandomDataUtil.randomChName();
				}else if(otherArrayMockType.getMockType()==OtherMockerEnum.RANDOM_ADDRESS.getValue()){
					otherArray[i] = RandomDataUtil.randomAddress();
				}
				if(StringUtils.isNotEmpty(otherArrayMockType.getOtherStr())){
					otherArray[i] = RandomDataUtil.randomString(otherArrayMockType.getOtherStr());
				}
			}
			json.put(fieldName, otherArray);
			
//			if(otherArrayMockType.getMockType()==OtherMockerEnum.RANDOM_ID.getValue()){
//				for(int i=0;i<otherArray.length;i++){
//					otherArray[i] = RandomDataUtil.randomIdStr();
//					if(StringUtils.isNotEmpty(otherArrayMockType.getOtherStr())){
//						otherArray[i] = RandomDataUtil.randomString(otherArrayMockType.getOtherStr());
//					}
//				}
//				json.put(fieldName, otherArray);
//			}else if(otherArrayMockType.getMockType()==OtherMockerEnum.RANDOM_PHONE.getValue()){
//				for(int i=0;i<otherArray.length;i++){
//					otherArray[i] = RandomDataUtil.randomPhoneStr();
//					if(StringUtils.isNotEmpty(otherArrayMockType.getOtherStr())){
//						otherArray[i] = RandomDataUtil.randomString(otherArrayMockType.getOtherStr());
//					}
//				}
//				json.put(fieldName, otherArray);
//			}else if(otherArrayMockType.getMockType()==OtherMockerEnum.RANDOM_EMAIL.getValue()){
//				for(int i=0;i<otherArray.length;i++){
//					otherArray[i] = RandomDataUtil.randomEmailStr();
//					if(StringUtils.isNotEmpty(otherArrayMockType.getOtherStr())){
//						otherArray[i] = RandomDataUtil.randomString(otherArrayMockType.getOtherStr());
//					}
//				}
//				json.put(fieldName, otherArray);
//			}else if(otherArrayMockType.getMockType()==OtherMockerEnum.RANDOM_CH_NAME.getValue()){
//				for(int i=0;i<otherArray.length;i++){
//					otherArray[i] = RandomDataUtil.randomChName();
//					if(StringUtils.isNotEmpty(otherArrayMockType.getOtherStr())){
//						otherArray[i] = RandomDataUtil.randomString(otherArrayMockType.getOtherStr());
//					}
//				}
//				json.put(fieldName, otherArray);
//			}else if(otherArrayMockType.getMockType()==OtherMockerEnum.RANDOM_ADDRESS.getValue()){
//				for(int i=0;i<otherArray.length;i++){
//					otherArray[i] = RandomDataUtil.randomAddress();
//					if(StringUtils.isNotEmpty(otherArrayMockType.getOtherStr())){
//						otherArray[i] = RandomDataUtil.randomString(otherArrayMockType.getOtherStr());
//					}
//				}
//				json.put(fieldName, otherArray);
//			}
		}
	}
	
	private static void putBooleanValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof BooleanMockType){
			 BooleanMockType booleanMockType = (BooleanMockType)voFieldMockType;
             if(StringUtils.isEmpty(booleanMockType.getBooleanStr())){
            	 json.put(fieldName, RandomDataUtil.randomBooleanValue());//默认生成0-100整数
             }else{
            	 json.put(fieldName, RandomDataUtil.randomBooleanValue(booleanMockType.getBooleanStr()));
             }
		}
	}
	
	private static void putBooleanArrayValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof BooleanArrayMockType){
			 BooleanArrayMockType booleanArrayMockType = (BooleanArrayMockType)voFieldMockType;
             boolean[] booleanArray = new boolean[booleanArrayMockType.getArrayLength()];
             if(StringUtils.isEmpty(booleanArrayMockType.getBooleanStr())){
            	 for(int i=0;i<booleanArray.length;i++){
            		 booleanArray[i] = RandomDataUtil.randomBooleanValue();
            	 }
             }else{
            	 for(int i=0;i<booleanArray.length;i++){
            		 booleanArray[i] = RandomDataUtil.randomBooleanValue(booleanArrayMockType.getBooleanStr());
            	 }
             }
             json.put(fieldName, booleanArray);
		}
	}
	
	private static void putByteValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof ByteMockType){
			 ByteMockType byteMockType = (ByteMockType)voFieldMockType;
             if(StringUtils.isEmpty(byteMockType.getByteStr())){
            	 json.put(fieldName, RandomDataUtil.randomByteValue(100));//默认生成0-100整数
             }else{
            	 json.put(fieldName, RandomDataUtil.randomByteValue(byteMockType.getByteStr()));
             }
		}
	}
	
	private static void putByteArrayValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof ByteArrayMockType){
             ByteArrayMockType byteArrayMockType = (ByteArrayMockType)voFieldMockType;
             byte[] byteArray = new byte[byteArrayMockType.getArrayLength()];
             if(StringUtils.isEmpty(byteArrayMockType.getByteStr())){
            	 for(int i=0;i<byteArray.length;i++){
            		 byteArray[i] = RandomDataUtil.randomByteValue(100);//默认生成0-100整数
            	 }
             }else{
            	 for(int i=0;i<byteArray.length;i++){
            		 byteArray[i] = RandomDataUtil.randomByteValue(byteArrayMockType.getByteStr());
            	 }
             }
             json.put(fieldName, byteArray);
		}
	}
	
	private static void putIntValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof IntMockType){
             IntMockType intMockType = (IntMockType)voFieldMockType;
             if(StringUtils.isEmpty(intMockType.getIntStr())){
            	 json.put(fieldName, RandomDataUtil.randomIntValue(100));//默认生成0-100整数
             }else{
            	 json.put(fieldName, RandomDataUtil.randomIntValue(intMockType.getIntStr()));
             }
		}
	}
	
	private static void putIntArrayValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof IntArrayMockType){
             IntArrayMockType intArrayMockType = (IntArrayMockType)voFieldMockType;
             int[] intArray = new int[intArrayMockType.getArrayLength()];
             if(StringUtils.isEmpty(intArrayMockType.getIntStr())){
            	 for(int i=0;i<intArray.length;i++){
            		 intArray[i] = RandomDataUtil.randomIntValue(100);//默认生成0-100整数
            	 }
             }else{
            	 for(int i=0;i<intArray.length;i++){
            		 intArray[i] = RandomDataUtil.randomIntValue(intArrayMockType.getIntStr());
            	 }
             }
             json.put(fieldName, intArray);
		}
	}
	
	private static void putLongValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof LongMockType){
             LongMockType longMockType = (LongMockType)voFieldMockType;
             if(StringUtils.isEmpty(longMockType.getLongStr())){
            	 json.put(fieldName, RandomDataUtil.randomLongValue());
             }else{
            	 json.put(fieldName, RandomDataUtil.randomLongValue(longMockType.getLongStr()));
             }
		}
	}
	
	private static void putLongArrayValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof LongArrayMockType){
             LongArrayMockType longArrayMockType = (LongArrayMockType)voFieldMockType;
             long[] longArray = new long[longArrayMockType.getArrayLength()];
             if(StringUtils.isEmpty(longArrayMockType.getLongStr())){
            	 for(int i=0;i<longArray.length;i++){
            		 longArray[i] = RandomDataUtil.randomLongValue();
            	 }
             }else{
            	 for(int i=0;i<longArray.length;i++){
            		 longArray[i] = RandomDataUtil.randomLongValue(longArrayMockType.getLongStr());
            	 }
             }
             json.put(fieldName, longArray);
		}
	}
	
	private static void putFloatValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof FloatMockType){
             FloatMockType floatMockType = (FloatMockType)voFieldMockType;
             if(StringUtils.isEmpty(floatMockType.getFloatStr())){
            	 json.put(fieldName, RandomDataUtil.randomFloatValue(floatMockType.getStart(), floatMockType.getEnd(), floatMockType.getDecimal()));
             }else{
            	 json.put(fieldName, RandomDataUtil.randomFloatValue(floatMockType.getFloatStr(), floatMockType.getDecimal()));
             }
		}
	}
	
	private static void putFloatArrayValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof FloatArrayMockType){
			FloatArrayMockType floatArrayMockType = (FloatArrayMockType)voFieldMockType;
			float[] floatArray = new float[floatArrayMockType.getArrayLength()];
			for(int i=0;i<floatArray.length;i++){
				if(StringUtils.isEmpty(floatArrayMockType.getFloatStr())){
					floatArray[i] = RandomDataUtil.randomFloatValue(floatArrayMockType.getStart(), floatArrayMockType.getEnd(), floatArrayMockType.getDecimal());
				}else{
					floatArray[i] = RandomDataUtil.randomFloatValue(floatArrayMockType.getFloatStr(), floatArrayMockType.getDecimal());
				}
			}
            json.put(fieldName, floatArray);
		}
	}
	
	private static void putDoubleValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof DoubleMockType){
             DoubleMockType doubleMockType = (DoubleMockType)voFieldMockType;
             if(StringUtils.isEmpty(doubleMockType.getDoubleStr())){
            	 json.put(fieldName, RandomDataUtil.randomDoubleValue(doubleMockType.getStart(), doubleMockType.getEnd(), doubleMockType.getDecimal()));
             }else{
            	 json.put(fieldName, RandomDataUtil.randomDoubleValue(doubleMockType.getDoubleStr(), doubleMockType.getDecimal()));
             }
		}
	}
	
	private static void putDoubleArrayValue(VoFieldMockType voFieldMockType,JSONObject json,String fieldName){
		if(null==voFieldMockType){
			return;
		}
		if(voFieldMockType instanceof DoubleArrayMockType){
			DoubleArrayMockType doubleArrayMockType = (DoubleArrayMockType)voFieldMockType;
			double[] doubleArray = new double[doubleArrayMockType.getArrayLength()];
			for(int i=0;i<doubleArray.length;i++){
				if(StringUtils.isEmpty(doubleArrayMockType.getDoubleStr())){
				    doubleArray[i] = RandomDataUtil.randomDoubleValue(doubleArrayMockType.getStart(), doubleArrayMockType.getEnd(), doubleArrayMockType.getDecimal());
				}else{
					doubleArray[i] = RandomDataUtil.randomDoubleValue(doubleArrayMockType.getDoubleStr(), doubleArrayMockType.getDecimal());
				}
			}
            json.put(fieldName, doubleArray);
		}
	}
	
	private static void setRandomString(String str, JSONObject json, String fieldName){
		if(StringUtils.isNotEmpty(str)){
			json.put(fieldName, RandomDataUtil.randomString(str));
		}
	}
	
}
