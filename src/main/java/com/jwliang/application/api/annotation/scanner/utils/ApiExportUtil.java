package com.jwliang.application.api.annotation.scanner.utils;

import java.awt.Color;
import java.io.OutputStream;
import java.util.Date;
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
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @ClassName: ApiExportUtil  
 * @Description: api导出pdf工具
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年3月23日
 *
 */

public class ApiExportUtil {
	
    private static BaseFont bfChinese;
	
	static{
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 
	 * @title: exportPdfDocs 
	 * @description: Api Docs生成pdf文件入口  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月23日 下午2:33:11  
	 * @param outputStream 
	 * @return void 返回类型 
	 * @throws
	 */
	public void exportPdfDocs(OutputStream outputStream){
		Document pdfDocument = null;
        try {
        	//页面大小  
        	Rectangle rectangle = new Rectangle(PageSize.A4);  
        	//页面背景色  
        	rectangle.setBackgroundColor(new Color(246,246,246)); 
        	pdfDocument = new Document(rectangle);
        	//构建一个PDF文档输出流程
        	PdfWriter pdfWriter = PdfWriter.getInstance(pdfDocument,outputStream);
        	//设置PDF页面事件
        	pdfWriter.setPageEvent(new TextWaterMarkPdfPageEvent("飞凡科技"));
        	pdfWriter.setPageEvent(new HeadFootInfoPdfPageEvent());
            //打开PDF文件流
        	pdfDocument.open();
        	//添加文档标题
        	Paragraph docTitle = new Paragraph(ApiScannerService.docTitle, new Font(bfChinese, 16, Font.BOLD));
        	docTitle.setSpacingBefore(10);
        	docTitle.setSpacingAfter(10);
        	docTitle.setAlignment(Element.ALIGN_CENTER);
        	pdfDocument.add(docTitle);
        	//获取Api Docs元数据
        	List<ApiClassAnnotationInfo> apiClassList = AnnotationClassScanner.apiClassList;
        	//循环添加Api Docs目录
        	this.addDocsItems(pdfDocument, apiClassList);
        	//循环添加Api Docs信息
        	int index = 1;
			for(ApiClassAnnotationInfo apiClass:apiClassList){
				List<ApiMethodAnnotationInfo> apiList = apiClass.getApiList();
				for(int i=0;i<apiList.size();i++){
					this.addApiDocs(index, pdfDocument, apiList.get(i));
					index++;
				}
			}
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
        	if(pdfDocument!=null){
        		pdfDocument.close();
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
			Font f12 = new Font(bfChinese, 12, Font.NORMAL);
			Font f14 = new Font(bfChinese, 14, Font.BOLD);
			this.addParagraph(document, "目录", 20, f14);
			int index = 1;
			for(ApiClassAnnotationInfo apiClass:apiClassList){
				List<ApiMethodAnnotationInfo> apiList = apiClass.getApiList();
				for(int i=0;i<apiList.size();i++){
					String item = index + "、" + apiList.get(i).getTitle();
					if(StringUtils.isNotEmpty(apiList.get(i).getAuthor())){
						item = item + "(" + apiList.get(i).getAuthor() + ")"; 
					}
					if(apiList.get(i).isDeprecated()){
						this.addParagraph(document, item, 10, new Font(bfChinese, 12, Font.ITALIC | Font.STRIKETHRU));
					}else{
						this.addParagraph(document, item, 10, f12);
					}
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
     * @description: Api信息生产pdf  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年3月23日 下午2:31:16  
     * @param index
     * @param document
     * @param api 
     * @return void 返回类型 
     * @throws
     */
	private void addApiDocs(int index, Document document, ApiMethodAnnotationInfo api){
		try {
			//设置中文字体和字体样式
			Font f10 = new Font(bfChinese, 10, Font.NORMAL);
			Font f12 = new Font(bfChinese, 12, Font.BOLD);
			Font f14 = new Font(bfChinese, 14, Font.BOLD);
			//接口描述  接口作者
//			document.add(new Paragraph(index + "、接口描述", f12)); 
//			document.add(new Paragraph(api.getTitle(), f10));
//			if(StringUtils.isNotEmpty(api.getAuthor())){
//				document.add(new Paragraph(api.getAuthor(), f10)); 
//			}
			PdfPTable apiTable = new PdfPTable(1);
			apiTable.setWidthPercentage(100);//设置表格占PDF文档100%宽度
			apiTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);//水平方向表格控件左对齐
			//处理header
			PdfPCell apiHeaderCell = new PdfPCell();//创建一个表格的表头单元格
//			apiHeaderCell.disableBorderSide(PdfPCell.NO_BORDER);
			apiHeaderCell.setBackgroundColor(new Color(65, 160, 214));
			apiHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			apiHeaderCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			apiHeaderCell.setBorderColor(new Color(65, 160, 214));
			apiHeaderCell.setFixedHeight(30);
			String headerStr = index + "、" + api.getTitle();
			if(StringUtils.isNotEmpty(api.getAuthor())){
				headerStr = headerStr + "(" + api.getAuthor() + ")"; 
			}
			apiHeaderCell.setPhrase(new Paragraph(headerStr, f14));
			apiTable.addCell(apiHeaderCell);
			document.add(apiTable);
			//请求链接
			this.addParagraph(document, "(1)请求链接", 5, f12);
			if(api.isDeprecated()){
				this.addParagraph(document, api.getClassUri() + api.getUri(), 5, new Font(bfChinese, 10, Font.ITALIC | Font.STRIKETHRU));
			}else{
				this.addParagraph(document, api.getClassUri() + api.getUri(), 5, f10);
			}
			//请求方式
			this.addParagraph(document, "(2)请求方式", 5, f12);
			this.addParagraph(document, api.getMethod(), 5, f10);
			//请求参数
			this.addParameters(document, api, f10, f12);
			//响应模型
			this.addResModel(document, api, f10, f12);
			//响应示例
			this.addResExample(document, api, f10, f12);
			//变更记录
			this.addChangeLogs(document, api, f10, f12);
			//备注说明
			this.addApiMarks(document, api, f10, f12);
			//状态码信息
			this.addStatuCodes(document, api, f10, f12);
			//添加间隔
			this.addParagraph(document, " ", 15, f12);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * 
	 * @title: addParameters 
	 * @description: 参数信息生成pdf  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月23日 下午2:31:39  
	 * @param document
	 * @param api
	 * @param f10
	 * @param f12 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addParameters(Document document, ApiMethodAnnotationInfo api, Font f10, Font f12){
		try {
			//请求参数
			this.addParagraph(document, "(3)请求参数", 5, f12);
			String[] parameterHeaders = {"序号", "名称", "类型", "必选", "描述"};
			PdfPTable parameterTable = new PdfPTable(parameterHeaders.length);
			parameterTable.setWidthPercentage(100);//设置表格占PDF文档100%宽度
			parameterTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);//水平方向表格控件左对齐
			//处理header
			PdfPCell parameterHeaderCell = new PdfPCell();//创建一个表格的表头单元格
			parameterHeaderCell.setBackgroundColor(new Color(49, 176, 213));
			parameterHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			parameterHeaderCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			for(String headerStr : parameterHeaders){
				parameterHeaderCell.setPhrase(new Paragraph(headerStr, f10));
				parameterTable.addCell(parameterHeaderCell);
			}
			//处理正文
			PdfPCell parameterContentCell = new PdfPCell();//创建一个表格的正文内容单元格
			parameterContentCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			parameterContentCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			List<ApiMethodParameter> parameters = api.getParameters();
			for(int i=0;i<parameters.size();i++){
				//序号
				parameterContentCell.setPhrase(new Paragraph(String.valueOf(i+1), f10));
				parameterContentCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				parameterTable.addCell(parameterContentCell);
				parameterContentCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				//名称
				parameterContentCell.setPhrase(new Paragraph(parameters.get(i).getName(), f10));
				parameterTable.addCell(parameterContentCell);
				//类型
				parameterContentCell.setPhrase(new Paragraph(parameters.get(i).getSimpleType(), f10));
				parameterTable.addCell(parameterContentCell);
				//必选
				parameterContentCell.setPhrase(new Paragraph(parameters.get(i).getIsRequired()==1?"是":"否", f10));
				parameterTable.addCell(parameterContentCell);
				//描述
				parameterContentCell.setPhrase(new Paragraph(parameters.get(i).getDescription(), f10));
				parameterTable.addCell(parameterContentCell);
			}
			document.add(parameterTable);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @title: addResModel 
	 * @description: 响应模型生成pdf  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月23日 下午2:31:53  
	 * @param document
	 * @param api
	 * @param f10
	 * @param f12 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addResModel(Document document, ApiMethodAnnotationInfo api, Font f10, Font f12){
		try {
			//响应模型
			this.addParagraph(document, "(4)响应模型", 5, f12);
			//数据模型
			String tType = api.getReturnType().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
			String returnType = "数据模型:" + tType;
			this.addParagraph(document, returnType, 5, f10);
			//循环处理模型
			List<ResponseVoInfo> returnClsList = api.getReturnClsList();
			for(int i=0;i<returnClsList.size();i++){
				ResponseVoInfo voInfo = returnClsList.get(i);
				this.addParagraph(document, voInfo.getSimpleName(), 10, f12);
				String[] voHeaders = {"名称", "类型", "必选", "描述"};
				PdfPTable voTable = new PdfPTable(voHeaders.length);
				voTable.setWidthPercentage(100);//设置表格占PDF文档100%宽度
				voTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);//水平方向表格控件左对齐
				//处理header
				PdfPCell voHeaderCell = new PdfPCell();//创建一个表格的表头单元格
				voHeaderCell.setBackgroundColor(new Color(49, 176, 213));
				voHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				voHeaderCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				for(String headerStr : voHeaders){
					voHeaderCell.setPhrase(new Paragraph(headerStr, f10));
					voTable.addCell(voHeaderCell);
				}
				//处理正文
				PdfPCell voContentCell = new PdfPCell();//创建一个表格的正文内容单元格
				voContentCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				voContentCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				List<ResponseVoField> fields = voInfo.getFields();
				for(int k=0;k<fields.size();k++){
					ResponseVoField field = fields.get(k);
					String showDataType = field.getShowType().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
					if(i==0&&k==2){
						showDataType = api.getReturnDataType().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
					}
					//名称
					voContentCell.setPhrase(new Paragraph(field.getName(), f10));
					voTable.addCell(voContentCell);
					//类型
					voContentCell.setPhrase(new Paragraph(showDataType, f10));
					voTable.addCell(voContentCell);
					//必选
					voContentCell.setPhrase(new Paragraph(field.getIsAllowEmpty()==1?"是":"否", f10));
					voTable.addCell(voContentCell);
					//描述
					voContentCell.setPhrase(new Paragraph(field.getDescription(), f10));
					voTable.addCell(voContentCell);
				}
				document.add(voTable);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @title: addResExample 
	 * @description: 样例数据生成pdf  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月23日 下午2:32:07  
	 * @param document
	 * @param api
	 * @param f10
	 * @param f12 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addResExample(Document document, ApiMethodAnnotationInfo api, Font f10, Font f12){
		//响应示例
		this.addParagraph(document, "(5)响应示例", 5, f12);
		//样例数据
		String exampleStr = ApiScannerService.getCommonExampleJson(api).toJSONString();
		this.addParagraph(document, exampleStr, 5, f10);
	}
	
	/**
	 * 
	 * @title: addChangeLogs 
	 * @description: 变更记录生成pdf  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月23日 下午2:32:24  
	 * @param document
	 * @param api
	 * @param f10
	 * @param f12 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addChangeLogs(Document document, ApiMethodAnnotationInfo api, Font f10, Font f12){
		try {
			//变更记录
			this.addParagraph(document, "(6)变更记录", 5, f12);
			if(0!=api.getApiLogs().size()){
				String[] logHeaders = {"序号", "变更内容", "变更日期", "变更人"};
				PdfPTable logTable = new PdfPTable(logHeaders.length);
				logTable.setWidthPercentage(100);//设置表格占PDF文档100%宽度
				logTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);//水平方向表格控件左对齐
				//处理header
				PdfPCell logHeaderCell = new PdfPCell();//创建一个表格的表头单元格
				logHeaderCell.setBackgroundColor(new Color(49, 176, 213));
				logHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				logHeaderCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				for(String headerStr : logHeaders){
					logHeaderCell.setPhrase(new Paragraph(headerStr, f10));
					logTable.addCell(logHeaderCell);
				}
				//处理正文
				PdfPCell logContentCell = new PdfPCell();//创建一个表格的正文内容单元格
				logContentCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				logContentCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				List<ApiLogInfo> apiLogs = api.getApiLogs();
				for(int i=0;i<apiLogs.size();i++){
					//序号
					logContentCell.setPhrase(new Paragraph(String.valueOf(i+1), f10));
					logTable.addCell(logContentCell);
					//变更内容
					logContentCell.setPhrase(new Paragraph(apiLogs.get(i).getLogMsg(), f10));
					logTable.addCell(logContentCell);
					//变更日期
					logContentCell.setPhrase(new Paragraph(apiLogs.get(i).getLogDate(), f10));
					logTable.addCell(logContentCell);
					//变更人
					logContentCell.setPhrase(new Paragraph(apiLogs.get(i).getLogPerson(), f10));
					logTable.addCell(logContentCell);
				}
				document.add(logTable);
			}else{
				document.add(new Paragraph("暂无", f10));
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @title: addApiMarks 
	 * @description: 备注信息生成pdf  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月23日 下午2:32:41  
	 * @param document
	 * @param api
	 * @param f10
	 * @param f12 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addApiMarks(Document document, ApiMethodAnnotationInfo api, Font f10, Font f12){
		//备注说明
		this.addParagraph(document, "(7)备注说明", 5, f12);
		if(StringUtils.isNotEmpty(api.getRemark())){
			//Api备注信息
			String[] apiMarks = api.getRemark().split("</br>");
			for(String apiMark:apiMarks){
				this.addParagraph(document, apiMark, 5, f10);
			}
		}else{
			this.addParagraph(document, "暂无", 5, f10);
		}
	}
	
	/**
	 * 
	 * @title: addStatuCodes 
	 * @description: 状态码信息生成pdf  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月23日 下午2:32:54  
	 * @param document
	 * @param api
	 * @param f10
	 * @param f12 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addStatuCodes(Document document, ApiMethodAnnotationInfo api, Font f10, Font f12){
		try {
			//状态码信息
			this.addParagraph(document, "(8)状态码信息", 5, f12);
			if(0!=api.getApiLogs().size()){
				String[] statusHeaders = {"状态编码", "提示信息"};
				PdfPTable statusTable = new PdfPTable(statusHeaders.length);
				statusTable.setWidthPercentage(100);//设置表格占PDF文档100%宽度
				statusTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);//水平方向表格控件左对齐
				//处理header
				PdfPCell statusHeaderCell = new PdfPCell();//创建一个表格的表头单元格
				statusHeaderCell.setBackgroundColor(new Color(49, 176, 213));
				statusHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				statusHeaderCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				for(String headerStr : statusHeaders){
					statusHeaderCell.setPhrase(new Paragraph(headerStr, f10));
					statusTable.addCell(statusHeaderCell);
				}
				//处理正文
				PdfPCell statusContentCell = new PdfPCell();//创建一个表格的正文内容单元格
				statusContentCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				statusContentCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				List<ApiStatusInfo> apiStatuses = api.getApiStatuses();
				for(int i=0;i<apiStatuses.size();i++){
					//状态编码
					statusContentCell.setPhrase(new Paragraph(String.valueOf(apiStatuses.get(i).getCode()), f10));
					statusTable.addCell(statusContentCell);
					//提示信息
					statusContentCell.setPhrase(new Paragraph(apiStatuses.get(i).getMessage(), f10));
					statusTable.addCell(statusContentCell);
				}
				document.add(statusTable);
			}else{
				this.addParagraph(document, "暂无", 5, f10);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @title: addParagraph 
	 * @description: 添加段落文本  
	 * @author: liangjunwei
	 * @email: liangjwjob_2014@sina.com   
	 * @time: 2017年3月23日 下午7:10:33  
	 * @param document
	 * @param text
	 * @param spacing
	 * @param f 
	 * @return void 返回类型 
	 * @throws
	 */
	private void addParagraph(Document document, String text, float spacing, Font f){
		try {
			Paragraph paragraph = new Paragraph(text, f);
			paragraph.setSpacingAfter(spacing);
			document.add(paragraph);
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
	}
	
	//添加文本水印事件类
	class TextWaterMarkPdfPageEvent extends PdfPageEventHelper{
		private String waterMarkText;
		public TextWaterMarkPdfPageEvent(String waterMarkText){
			this.waterMarkText = waterMarkText;
		}
		public void onEndPage(PdfWriter writer, Document document) {
	        try{
		        float pageWidth = document.right() + document.left();//获取pdf内容正文页面宽度
		        float pageHeight = document.top() + document.bottom();//获取pdf内容正文页面高度
		        //设置水印字体格式
		        Font waterMarkFont = new Font(bfChinese, 20, Font.BOLD, Color.blue);
		        PdfContentByte waterMarkPdfContent = writer.getDirectContentUnder();
		        Phrase phrase = new Phrase(waterMarkText, waterMarkFont);
		        ColumnText.showTextAligned(waterMarkPdfContent,Element.ALIGN_CENTER,phrase,  
	                    pageWidth*0.25f,pageHeight*0.2f,45);
		        ColumnText.showTextAligned(waterMarkPdfContent,Element.ALIGN_CENTER,phrase,  
	                    pageWidth*0.25f,pageHeight*0.5f,45);
		        ColumnText.showTextAligned(waterMarkPdfContent,Element.ALIGN_CENTER,phrase,  
	                    pageWidth*0.25f,pageHeight*0.8f,45);
		        ColumnText.showTextAligned(waterMarkPdfContent,Element.ALIGN_CENTER,phrase,  
	                    pageWidth*0.65f,pageHeight*0.2f,45);
		        ColumnText.showTextAligned(waterMarkPdfContent,Element.ALIGN_CENTER,phrase,  
	                    pageWidth*0.65f,pageHeight*0.5f,45);
		        ColumnText.showTextAligned(waterMarkPdfContent,Element.ALIGN_CENTER,phrase,  
	                    pageWidth*0.65f,pageHeight*0.8f,45);
	        }catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	//添加文档头事件类
    class HeadFootInfoPdfPageEvent extends PdfPageEventHelper{
		public HeadFootInfoPdfPageEvent(){
			
		}
		public void onEndPage(PdfWriter writer, Document document) {
	        try{
	        	PdfContentByte headAndFootPdfContent = writer.getDirectContent();
	        	headAndFootPdfContent.saveState();
	        	headAndFootPdfContent.beginText();
    	        headAndFootPdfContent.setFontAndSize(bfChinese, 10);
    	        //文档页头信息设置
    	        float x = document.top(-20);
    	        //页头信息左面
    	        headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_LEFT, "飞凡信息科技有限公司", document.left(), x, 0);
    	        //页头信息中间
    	        headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_CENTER, "", (document.right() + document.left())/2, x, 0);
    	        //页头信息右面
    	        headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_RIGHT, DateUtil.getYyMdStr(new Date()), document.right(), x, 0);
    	        //文档页脚信息设置
    	        float y = document.bottom(-20);
    	        //页脚信息左面
    	        headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_LEFT,
    	                           "技术产品研发部",
    	                           document.left(), y, 0);
    	        //页脚信息中间
    	        headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_CENTER,
    	        		          "第"+writer.getPageNumber()+ "页",
    	                           (document.right() + document.left())/2,
    	                           y, 0);
    	        //页脚信息右面
    	        headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_RIGHT,
    	                           "飞凡信息科技有限公司",
    	                           document.right(), y, 0);
    	        headAndFootPdfContent.endText();
    	        headAndFootPdfContent.restoreState();
	        }catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
}
