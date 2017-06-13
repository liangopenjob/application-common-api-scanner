  //环境配置
  var env = 0;//0:docs与Api服务集成在一个项目提供服务,1:docs单独作为一个项目提供docs服务,2:docs纯粹作为静态html直接访问
  var localPath = "http://127.0.0.1:8080/boisp-xapi";//指定获取Api Docs元数据服务地址(仅当env赋值1、2时可设置,其它情况下无需配置)
  //url信息
  var apiGroupsUrl = localPath + "/docsApi/apiGroupsMap"; // api信息获取url
  var apiExamplesUrl = localPath + "/docsApi/apiRetExamples"; // api返回样例url
  var simulateReqUrl = localPath + "/docsApi/simulateRequest"; // api模拟请求url
  var apiMockReqUrl = localPath + "/docsApi/apiMock"; // 设置api mock信息url
  var apiMockMapUrl = localPath + "/docsApi/apiMockMap"; // apiMockMap信息获取url
  var isAllMockUrl = localPath + "/docsApi/isAllMock"; // 验证是否全部设置为mock状态url
  var exportMockConfigUrl = localPath + "/docsApi/apiMockConfig/out"; // 导出开启mock配置信息url
  var importMockConfigUrl = localPath + "/docsApi/apiMockConfig/in"; // 导入开启mock配置信息url
  var searchApisUrl = localPath + "/docsApi/apis"; // 搜索Api信息url
  var apiExportPdfUrl = localPath + "/docsApi/apiExport/pdf"; // Api Docs导出pdf
  var apiExportDocUrl = localPath + "/docsApi/apiExport/doc"; // Api Docs导出doc
  var apiDocsInfoUrl = localPath + "/docsApi/docsInfo"; // Api Docs文档说明性信息url
  var apiGenCodeUrl = localPath + "/docsApi/generateVoCode"; // Api 生成Java Code信息url
  var apiLockMockUrl = localPath + "/docsApi/lockMock"; // lockMock锁定解锁url
  var apiCommonStatusUrl = localPath + "/docsApi/commonStatus"; // 获取公共状态码信息url
  var downloadHelpPptUrl = localPath + "/docsApi/downloadHelpPpt"; // 下载使用帮助文档url
  if(env==0){
	  var appPath = getContextPath();//获取应用上下文路径
	  if(appPath.indexOf("http")==0){
		  var strIndex = appPath.indexOf("api-docs");
	      if(strIndex!=-1){
		     appPath = appPath.substring(0,strIndex-1);
		  }
		  apiGroupsUrl = appPath + "/docsApi/apiGroupsMap";
		  apiExamplesUrl = appPath + "/docsApi/apiRetExamples";
		  simulateReqUrl = appPath + "/docsApi/simulateRequest";
		  apiMockReqUrl = appPath + "/docsApi/apiMock";
		  apiMockMapUrl = appPath + "/docsApi/apiMockMap";
		  isAllMockUrl = appPath + "/docsApi/isAllMock";
		  exportMockConfigUrl = appPath + "/docsApi/apiMockConfig/out";
		  importMockConfigUrl = appPath + "/docsApi/apiMockConfig/in";
		  searchApisUrl = appPath + "/docsApi/apis";
		  apiExportPdfUrl = appPath + "/docsApi/apiExport/pdf";
		  apiExportDocUrl = appPath + "/docsApi/apiExport/doc";
		  apiDocsInfoUrl = appPath + "/docsApi/docsInfo";
		  apiGenCodeUrl = appPath + "/docsApi/generateVoCode";
		  apiLockMockUrl = appPath + "/docsApi/lockMock";
		  apiCommonStatusUrl = appPath + "/docsApi/commonStatus";
		  downloadHelpPptUrl = appPath + "/docsApi/downloadHelpPpt";
	  }
  }
  
  //获取当前上下文路径
  function getContextPath(){   
	  var localObj = window.location;
	  var contextPath = localObj.pathname.split("/")[1];
	  var basePath = localObj.protocol+"//"+localObj.host+"/"+contextPath;
      return basePath;
  }