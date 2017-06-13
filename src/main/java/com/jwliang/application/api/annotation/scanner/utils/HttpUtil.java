package com.jwliang.application.api.annotation.scanner.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class HttpUtil {
	
	public static JSONObject doGet(String urlStr) {
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			// get请求不需要DoOutPut
			conn.setDoOutput(false);
			conn.setDoInput(true);
			// 设置连接超时时间和读取超时时间
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// 连接服务器
			conn.connect();
			// 取得输入流,并使用Reader读取
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭输入流
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		JSONObject jsonResult = new JSONObject();
		try {
			jsonResult = JSONObject.parseObject(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}

	@SuppressWarnings("rawtypes")
	public static JSONObject doPost(String urlStr, Map params) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			// 发送post请求必须设置为true
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 设置连接超时时间和读取超时时间
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			out = new OutputStreamWriter(conn.getOutputStream());
			// post请求参数写在正文中
			StringBuilder paramsBuilder = new StringBuilder();
			for (Object key : params.keySet()) {
				if (null != params.get(key)) {
					paramsBuilder.append(key).append("=")
							.append(params.get(key)).append("&");
				}
			}
			if (paramsBuilder.length() > 0) {
				paramsBuilder.deleteCharAt(paramsBuilder.length() - 1);
			}
			out.write(paramsBuilder.toString());
			out.flush();
			out.close();
			// 取得输入流,并使用Reader读取
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {// 关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		JSONObject jsonResult = new JSONObject();
		try {
			jsonResult = JSONObject.parseObject(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
	
}
