package com.jwliang.application.api.annotation.scanner.utils;

import java.awt.Color;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jwliang.application.api.annotation.scanner.AnnotationClassScanner;
import com.jwliang.application.api.annotation.scanner.bean.ApiClassAnnotationInfo;
import com.jwliang.application.api.annotation.scanner.bean.ApiLogInfo;
import com.jwliang.application.api.annotation.scanner.bean.ApiMethodAnnotationInfo;
import com.jwliang.application.api.annotation.scanner.bean.ApiMethodParameter;
import com.jwliang.application.api.annotation.scanner.bean.ApiStatusInfo;
import com.jwliang.application.api.annotation.scanner.bean.ResponseVoField;
import com.jwliang.application.api.annotation.scanner.bean.ResponseVoInfo;
import com.jwliang.application.api.annotation.scanner.service.ApiScannerService;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.style.RtfParagraphStyle;

/**
 * 
 * @ClassName: ApiDocPdfUtil  
 * @Description: Api导出doc、pdf工具类
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年3月24日
 *
 */

public class ApiDocPdfUtil {
	
	private static BaseFont bfChinese;
	
	static{
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 
	 * @title: exportDocs 
	 * @description: 导出doc或pdf入口  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com  
	 * @time: 2017年3月24日 下午5:28:54  
	 * @param outputStream
	 * @param fileType 
	 * @return void 返回类型 
	 * @throws
	 */
	public void exportDocs(OutputStream outputStream, String fileType){
		Document document = null;
        try {
        	document = new Document(PageSize.A4);
        	//构建一个文档输出流程
        	if("doc".equals(fileType)){
        		RtfWriter2.getInstance(document, outputStream);
        	}else{
        		PdfWriter.getInstance(document, outputStream);
        	}
            //打开文件流
        	document.open();
        	//添加文档标题
        	Paragraph docTitle = new Paragraph(ApiScannerService.docTitle, new Font(bfChinese, 16, Font.BOLD));
        	docTitle.setSpacingBefore(10);
        	docTitle.setSpacingAfter(10);
        	docTitle.setAlignment(Element.ALIGN_CENTER);
			document.add(docTitle);
        	//获取Api Docs元数据
        	List<ApiClassAnnotationInfo> apiClassList = AnnotationClassScanner.apiClassList;
        	//循环添加Api Docs目录
        	this.addDocsItems(document, apiClassList);
        	//循环添加Api Docs信息
        	int index = 1;
			for(ApiClassAnnotationInfo apiClass:apiClassList){
				List<ApiMethodAnnotationInfo> apiList = apiClass.getApiList();
				for(int i=0;i<apiList.size();i++){
					this.addApiDocs(index, document, apiList.get(i), fileType);
					index++;
				}
			}
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
        	if(document!=null){
        		document.close();
        	}
        }
    }
	
	/**
	 * 
	 * @title: addDocsItems 
	 * @description: 添加目录信息  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月24日 上午10:22:15  
	 * @param document
	 * @param apiClassList 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addDocsItems(Document document, List<ApiClassAnnotationInfo> apiClassList) {
		try {
			Font f10 = new Font(bfChinese, 10, Font.NORMAL);
			Font f12 = new Font(bfChinese, 12, Font.NORMAL);
			Font f14 = new Font(bfChinese, 14, Font.BOLD);
			this.addParagraph(document, "目录", f14);
			this.addParagraph(document, " ", f10);
			int index = 1;
			for(ApiClassAnnotationInfo apiClass:apiClassList){
				List<ApiMethodAnnotationInfo> apiList = apiClass.getApiList();
				for(int i=0;i<apiList.size();i++){
					String item = index + "、" + apiList.get(i).getTitle();
					if(StringUtils.isNotEmpty(apiList.get(i).getAuthor())){
						item = item + "(" + apiList.get(i).getAuthor() + ")"; 
					}
					if(apiList.get(i).isDeprecated()){
						this.addParagraph(document, item, new Font(bfChinese, 12, Font.ITALIC | Font.STRIKETHRU));
					}else{
						this.addParagraph(document, item, f12);
					}
					this.addParagraph(document, " ", f10);
					index++;
				}
			}
			//新起一页
			document.newPage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @title: addApiDocs 
	 * @description: 添加api信息  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月24日 下午5:29:19  
	 * @param index
	 * @param document
	 * @param api 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addApiDocs(int index, Document document, ApiMethodAnnotationInfo api, String fileType){
		try {
			//设置字体样式
			Font f10 = new Font(bfChinese, 10, Font.NORMAL);
			Font f12 = new Font(bfChinese, 12, Font.BOLD);
			//接口标题
			String headerStr = index + "、" + api.getTitle();
			if(StringUtils.isNotEmpty(api.getAuthor())){
				headerStr = headerStr + "(" + api.getAuthor() + ")"; 
			}
			this.addParagraph(document, " ", f12);
			if("doc".equals(fileType)){
				this.addParagraph(document, headerStr, RtfParagraphStyle.STYLE_HEADING_3);
			}else{
				this.addParagraph(document, headerStr, f12);
			}
			//添加表格
			Table table = new Table(5);//创建一个5列的表格
			table.setWidth(100);
	        table.setAlignment(Element.ALIGN_CENTER);//水平居中   
	        table.setAlignment(Element.ALIGN_MIDDLE);//垂直居中   
	        table.setAutoFillEmptyCells(true);//自动填满
	        table.setBorder(0);//设置边框
	        table.setPadding(1);//设置间距
			//请求链接
	        this.addReqLink(table, api, f10);
			//请求方式
	        this.addReqType(table, api, f10);
			//请求参数
			this.addParameters(table, api, f10);
			//响应模型
			this.addResModel(table, api, f10);
			//响应示例
			this.addResExample(table, api, f10);
			//变更记录
			this.addChangeLogs(table, api, f10);
			//备注说明
			this.addApiMarks(table, api, f10);
			//状态码信息
			this.addStatuCodes(table, api, f10);
			document.add(table);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @title: addReqLink 
	 * @description: 添加请求连接  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com  
	 * @time: 2017年3月24日 下午5:29:41  
	 * @param table
	 * @param api
	 * @param f10 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addReqLink(Table table, ApiMethodAnnotationInfo api, Font f10){
		try {
			Cell cell = this.getCommonCell("请求连接", f10);
			cell.setBackgroundColor(new Color(192,194,204));
			table.addCell(cell);
			//---内容---
			String uri = api.getClassUri() + api.getUri();
			Cell valueCell = null;
			if(api.isDeprecated()){
				valueCell = new Cell(new Paragraph(uri,new Font(bfChinese, 10, Font.ITALIC | Font.STRIKETHRU)));
			}else{
				valueCell = new Cell(new Paragraph(uri,f10));
			}
			valueCell.setUseAscender(true);
			valueCell.setUseDescender(true);
			valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			valueCell.setVerticalAlignment(Element.ALIGN_CENTER);
			valueCell.setColspan(4);
			table.addCell(valueCell);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @title: addReqType 
	 * @description: 添加请求类型  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月24日 下午5:29:55  
	 * @param table
	 * @param api
	 * @param f10 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addReqType(Table table, ApiMethodAnnotationInfo api, Font f10){
		Cell cell = this.getCommonCell("请求方式", f10);
		cell.setBackgroundColor(new Color(192,194,204));
        table.addCell(cell);
        //---内容---
        Cell valueCell = this.getCommonCell(api.getMethod(), f10);
        valueCell.setColspan(4);//合并单元格
        valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(valueCell);
	}
	
	/**
	 * 
	 * @title: addParameters 
	 * @description: 添加请求参数  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月24日 下午5:30:06  
	 * @param table
	 * @param api
	 * @param f10 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addParameters(Table table, ApiMethodAnnotationInfo api, Font f10){
		try {
			List<ApiMethodParameter> parameters = api.getParameters();
			//请求参数
			Cell parameterNameCell = this.getCommonCell("请求参数", f10);
			parameterNameCell.setBackgroundColor(new Color(192,194,204));//背景颜色
			parameterNameCell.setRowspan(parameters.size() + 1);//合并单元格 参数格式 + header
			table.addCell(parameterNameCell);
			String[] parameterHeaders = {"名称", "类型", "必选", "描述"};
			for(String headStr:parameterHeaders){
				Cell cell = this.getCommonCell(headStr, f10);
				cell.setBackgroundColor(new Color(192,194,204));//背景颜色
				table.addCell(cell);
			}
			for(int i=0;i<parameters.size();i++){
				//名称
				table.addCell(this.getValueCell(parameters.get(i).getName(), f10));
				//类型
				table.addCell(this.getValueCell(parameters.get(i).getSimpleType(), f10));
				//必选
				table.addCell(this.getValueCell(parameters.get(i).getIsRequired()==1?"是":"否", f10));
				//描述
				table.addCell(this.getValueCell(parameters.get(i).getDescription(), f10));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @title: addResModel 
	 * @description: 添加响应模型  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月24日 下午5:30:23  
	 * @param table
	 * @param api
	 * @param f10 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addResModel(Table table, ApiMethodAnnotationInfo api, Font f10){
		try {
			int rowNum = 1;
			List<ResponseVoInfo> returnClsList = api.getReturnClsList();
			for(ResponseVoInfo voInfo:returnClsList){
			   rowNum = rowNum + 2 + voInfo.getFields().size();
			}
			//响应模型
			Cell resModelNameCell = this.getCommonCell("响应模型", f10);
			resModelNameCell.setBackgroundColor(new Color(192,194,204));//背景颜色
			resModelNameCell.setRowspan(rowNum);
			table.addCell(resModelNameCell);
			//数据模型
			String tType = api.getReturnType().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
			String returnType = "数据模型:" + tType;
			Cell returnModelCell = this.getCommonCell(returnType, f10);
			returnModelCell.setBackgroundColor(new Color(192,194,204));//背景颜色
			returnModelCell.setColspan(4);
			table.addCell(returnModelCell);
			//循环处理模型
			for(int i=0;i<returnClsList.size();i++){
				ResponseVoInfo voInfo = returnClsList.get(i);
				//添加model cell
				Cell modelCell = this.getCommonCell(voInfo.getSimpleName(), f10);
				modelCell.setBackgroundColor(new Color(192,194,204));//背景颜色
				modelCell.setColspan(4);
				table.addCell(modelCell);
				String[] voHeaders = {"名称", "类型", "必选", "描述"};
				for(String voHeader:voHeaders){
					Cell cell = this.getCommonCell(voHeader, f10);
					cell.setBackgroundColor(new Color(192,194,204));//背景颜色
					table.addCell(cell);
				}
				//处理正文
				List<ResponseVoField> fields = voInfo.getFields();
				for(int k=0;k<fields.size();k++){
					ResponseVoField field = fields.get(k);
					String showDataType = field.getShowType().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
					if(i==0&&k==2){
						showDataType = api.getReturnDataType().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
					}
					//名称
					table.addCell(this.getValueCell(field.getName(), f10));
					//类型
					table.addCell(this.getValueCell(showDataType, f10));
					//必选
					table.addCell(this.getValueCell(field.getIsAllowEmpty()==1?"是":"否", f10));
					//描述
					table.addCell(this.getValueCell(field.getDescription(), f10));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @title: addResExample 
	 * @description: 添加响应示例  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月24日 下午5:30:35  
	 * @param table
	 * @param api
	 * @param f10 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addResExample(Table table, ApiMethodAnnotationInfo api, Font f10){
		//响应示例
		Cell cell = this.getCommonCell("响应示例", f10);
		cell.setBackgroundColor(new Color(192,194,204));
        table.addCell(cell);
        //---内容---
      	String exampleStr = ApiScannerService.getCommonExampleJson(api).toJSONString();//样例数据
        Cell value = this.getValueCell(exampleStr, f10);
        value.setColspan(4);//合并单元格
        table.addCell(value);
	}
	
	/**
	 * 
	 * @title: addChangeLogs 
	 * @description: 添加变更记录  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月24日 下午5:30:50  
	 * @param table
	 * @param api
	 * @param f10 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addChangeLogs(Table table, ApiMethodAnnotationInfo api, Font f10){
		try {
			List<ApiLogInfo> apiLogs = api.getApiLogs();
			//变更记录
			int rowNum = 0;
			if(0==apiLogs.size()){
				rowNum = 2;
			}else{
				rowNum = 1 + apiLogs.size();
			}
			Cell changeLogNameCell = this.getCommonCell("变更记录", f10);
			changeLogNameCell.setBackgroundColor(new Color(192,194,204));//背景颜色
			changeLogNameCell.setRowspan(rowNum);
			table.addCell(changeLogNameCell);
			//添加标题
			String[] logHeaders = {"序号", "变更内容", "变更日期", "变更人"};
			for(String logHeader:logHeaders){
				Cell cell = this.getCommonCell(logHeader, f10);
				cell.setBackgroundColor(new Color(192,194,204));//背景颜色
				table.addCell(cell);
			}
			//添加内容
			if(0!=apiLogs.size()){
				for(int i=0;i<apiLogs.size();i++){
					//序号
					table.addCell(this.getValueCell(String.valueOf(i+1), f10));
					//变更内容
					table.addCell(this.getValueCell(apiLogs.get(i).getLogMsg(), f10));
					//变更日期
					table.addCell(this.getValueCell(apiLogs.get(i).getLogDate(), f10));
					//变更人
					table.addCell(this.getValueCell(apiLogs.get(i).getLogPerson(), f10));
				}
			}else{
		        Cell value = this.getValueCell("暂无", f10);
		        value.setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
		        value.setColspan(4);//合并单元格
		        table.addCell(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @title: addApiMarks 
	 * @description: 添加备注说明  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月24日 下午5:31:01  
	 * @param table
	 * @param api
	 * @param f10 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addApiMarks(Table table, ApiMethodAnnotationInfo api, Font f10){
		try {
			//备注说明
			int rowNum = 0;
			if(StringUtils.isEmpty(api.getRemark())){
				rowNum = 1;
			}else{
				rowNum = api.getRemark().split("</br>").length;
			}
			Cell remarkNameCell = this.getCommonCell("备注说明", f10);
			remarkNameCell.setBackgroundColor(new Color(192,194,204));//背景颜色
			remarkNameCell.setRowspan(rowNum);
			table.addCell(remarkNameCell);
			//添加内容
			if(StringUtils.isNotEmpty(api.getRemark())){
				//Api备注信息
				String[] apiMarks = api.getRemark().split("</br>");
				for(String apiMark:apiMarks){
					Cell cell = this.getValueCell(apiMark, f10);
					cell.setColspan(4);
					table.addCell(cell);
				}
			}else{
			    Cell value = this.getValueCell("暂无", f10);
			    value.setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
		        value.setColspan(4);//合并单元格
		        table.addCell(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @title: addStatuCodes 
	 * @description: 添加状态码信息  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月24日 下午5:31:14  
	 * @param table
	 * @param api
	 * @param f10 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addStatuCodes(Table table, ApiMethodAnnotationInfo api, Font f10){
		try {
			List<ApiStatusInfo> apiStatuses = api.getApiStatuses();
			//状态码信息
			int rowNum = 0;
			if(0==apiStatuses.size()){
				rowNum = 2;
			}else{
				rowNum = 1 + apiStatuses.size();
			}
			Cell statusNameCell = this.getCommonCell("状态码信息", f10);
			statusNameCell.setBackgroundColor(new Color(192,194,204));//背景颜色
			statusNameCell.setRowspan(rowNum);
			table.addCell(statusNameCell);
			//添加标题
			String[] statusHeaders = {"状态编码", "提示信息"};
			for(String statusHeader:statusHeaders){
				Cell cell = this.getCommonCell(statusHeader, f10);
				cell.setColspan(2);
				cell.setBackgroundColor(new Color(192,194,204));//背景颜色
				table.addCell(cell);
			}
			//添加内容
			if(0!=apiStatuses.size()){
				for(int i=0;i<apiStatuses.size();i++){
					//状态编码
					Cell codeCell = this.getValueCell(String.valueOf(apiStatuses.get(i).getCode()), f10);
					codeCell.setColspan(2);
					table.addCell(codeCell);
					//提示信息
					Cell msgCell = this.getValueCell(apiStatuses.get(i).getMessage(), f10);
					msgCell.setColspan(2);
					table.addCell(msgCell);
				}
			}else{
		        Cell value = this.getValueCell("暂无", f10);
		        value.setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
		        value.setColspan(4);//合并单元格
		        table.addCell(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @title: addParagraph 
	 * @description: 添加段落信息  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月24日 下午5:31:39  
	 * @param document
	 * @param text
	 * @param spacing
	 * @param f 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addParagraph(Document document, String text, Font f){
		try {
			Paragraph paragraph = new Paragraph(text, f);
			document.add(paragraph);
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 
	 * @title: getCommonCell 
	 * @description: 获取cell对象  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月24日 下午5:31:52  
	 * @param headStr
	 * @param f
	 * @return 
	 * @return Cell 返回类型 
	 * @throws
	 */
	private Cell getCommonCell(String headStr, Font f){
		try {
			//列名
			Cell cell = new Cell(new Paragraph(headStr,f));
			cell.setUseAscender(true);
			cell.setUseDescender(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
			return cell;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @title: getValueCell 
	 * @description: 获取value cell  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月25日 上午8:59:37  
	 * @param valueStr
	 * @param f
	 * @return 
	 * @return Cell 返回类型 
	 * @throws
	 */
	private Cell getValueCell(String valueStr, Font f){
		try {
			//列名
			Cell cell = new Cell(new Paragraph(valueStr,f));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);//水平居中
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
			return cell;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}

