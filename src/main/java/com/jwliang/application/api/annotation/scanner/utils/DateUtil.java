package com.jwliang.application.api.annotation.scanner.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
	
	public static String getYyMdHmsStr(Date date){
    	SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
    	return sf.format(date);
    }
	
    public static String getYyMdStr(Date date){
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    	return sf.format(date);
    }
    
    public static String getChTimeStr(Date date){
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    	return sf.format(date);
    }
	
}
