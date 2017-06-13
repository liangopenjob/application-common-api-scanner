//搜索Api
function doSearch(){
	var searchText = $("#searchTextId").val();
	if(searchText==""){
	   showToast("请输入搜索信息!", false);
	}else{
	   $.post(searchApisUrl,{searchText:searchText},function(searchApis){
		   if(searchApis.length==0){
			   showToast("没有搜索到数据!", false);
		   }else{
			  showSearchApis(searchApis);
		   }
	   });
	}
}
//渲染搜索Api结果数据
function showSearchApis(methodsData){
	//清空已有数据
	$("#searchContentId").html("");
	if(methodsData.length==0){
	    //渲染html
		$("#searchContentId").html("<div style='text-align:center;line-height:400px;color:#CDB399;font-size:20px;font-family:fantasy;letter-spacing:3px;'>没有搜索到数据!</div>");
	    //展示模态框
		$("#myModal").modal("show");
	    return;
	}
    var methodStr = "";
	for(var k=0;k<methodsData.length;k++){
		methodStr = methodStr + "<div class='panel panel-default'>";
		methodStr = methodStr + "<div class='panel-heading' data-toggle='collapse' data-parent='#searchContentId' href='#s-method"+methodsData[k].id+"'>";
		/*****title*****/
		var methodTitle = "<div class='div-float-left'>";
		methodTitle = methodTitle + "<button id='s-methodId"+methodsData[k].id+"' type='button' class='btn ";
		if(methodsData[k].method=="POST"){
			methodTitle = methodTitle + "btn-success";
		}else{
			methodTitle = methodTitle + "btn-info";
		}
		methodTitle = methodTitle + "' style='cursor:auto;width:63px;'>"+methodsData[k].method+"</button>";
		methodTitle = methodTitle + "&nbsp;&nbsp;请求连接："
		if(methodsData[k].deprecated){
			methodTitle = methodTitle + "<span id='s-uriId"+methodsData[k].id+"' class='text-line-through'>"+methodsData[k].classUri+methodsData[k].uri+"</span>";
		}else{
			methodTitle = methodTitle + "<span id='s-uriId"+methodsData[k].id+"'>"+methodsData[k].classUri+methodsData[k].uri+"</span>";
		}
		if(methodsData[k].author!=""){
			methodTitle = methodTitle + "<span class='margin-left-50 text-info-color'>Author："+methodsData[k].author+"</span>";
		}
		methodTitle = methodTitle + "</div>";
		methodTitle = methodTitle + "<div class='div-float-right' style='height:30px;line-height:30px;'>";
		methodTitle = methodTitle + "<span class='margin-right-10 line-height-30' style='display:inline-block;vertical-align:middle;text-align:center;'>"+methodsData[k].title+"</span>"+"<span id='s-sw"+methodsData[k].id+"' class='switch-off' style='display:inline-block;vertical-align:middle;text-align:center;' title='Mock 开关' attrKey='"+methodsData[k].classUri+"#"+methodsData[k].uri+"' onclick='setApiMock_s(this);'></span>";
		methodTitle = methodTitle + "</div>";
		methodTitle = methodTitle + "<div class='div-clear'></div>";
		/*****title*****/
		methodStr = methodStr + methodTitle;
		methodStr = methodStr + "</div>";
		methodStr = methodStr + "<div id='s-method"+methodsData[k].id+"' class='panel-collapse collapse'>";
		/*****detail*****/
		var methodTabStr = "";
		methodTabStr = methodTabStr + "<ul class='nav nav-tabs'><li class='active'><a href='#s-modelId"+methodsData[k].id+"' data-toggle='tab'>响应模型</a></li><li><a href='#s-exampleId"+methodsData[k].id+"' data-toggle='tab'>响应示例</a></li><li><a href='#s-logsId"+methodsData[k].id+"' data-toggle='tab'>变更记录</a></li><li><a href='#s-remarkId"+methodsData[k].id+"' data-toggle='tab'>备注说明</a></li></ul>";
		methodTabStr = methodTabStr + "<div class='tab-content'>";
		//处理响应模型
		methodTabStr = methodTabStr + "<div class='tab-pane fade in active' id='s-modelId"+methodsData[k].id+"'>";
		var respModelStr = "<div class='panel-body'>";
		respModelStr = respModelStr + "<h3><span class='label label-info'>数据模型</span>&nbsp;&nbsp;"+methodsData[k].returnType+"</h3>";
		respModelStr = respModelStr + "<div class='panel-group margin-top-10' id='s-accordion"+methodsData[k].id+"'>";
		var returnClsData = methodsData[k].returnClsList;
		var apiStatusesData = methodsData[k].apiStatuses;
		var returnClsStr = "";
		for(var rIdx=0;rIdx<returnClsData.length;rIdx++){
			returnClsStr = returnClsStr + "<div class='panel panel-default'>";
			returnClsStr = returnClsStr + "<div class='panel-heading'><h4><a data-toggle='collapse' data-parent='#s-accordion"+methodsData[k].id+"' href='#s-collapse"+methodsData[k].id+rIdx+"'>&nbsp;"+returnClsData[rIdx].simpleName+"</a></h4></div>";
			returnClsStr = returnClsStr + "<div id='s-collapse"+methodsData[k].id+rIdx+"' class='panel-collapse collapse'>";
			var fieldTableStr = "<table class='table table-condensed'>";
			fieldTableStr = fieldTableStr + "<tr><th>&nbsp;名称</th><th>类型</th><th>必选</th><th>描述</th></tr>";
			var fieldsData = returnClsData[rIdx].fields;
			for(var fIdx=0;fIdx<fieldsData.length;fIdx++){
				var isAllowEmptyCn = fieldsData[fIdx].isAllowEmpty==1?"是":"否";
				var showDataType = fieldsData[fIdx].showType;
				if(rIdx==0&&fIdx==2){
					showDataType = methodsData[k].returnDataType;
				}
				if(rIdx==0&&fIdx==0&&apiStatusesData.length>0){
					fieldTableStr = fieldTableStr + "<tr data-toggle='collapse' href='#s-apiStatusId"+methodsData[k].id+"' style='background-color:#D9EDF7;cursor:pointer;' class='info'><td width='15%'>"+fieldsData[fIdx].name+"</td><td width='45%'>"+showDataType+"</td><td width='10%'>"+isAllowEmptyCn+"</td><td width='30%'>"+fieldsData[fIdx].description+"</td></tr>";
					var statusStr = "<tr><td colspan='4'>";
					statusStr = statusStr + "<div id='s-apiStatusId"+methodsData[k].id+"' class='panel-collapse collapse'><div class='panel-body'>";
					statusStr = statusStr + "<table class='table table-condensed'>";
					statusStr = statusStr + "<thead style='color:#2A6496;font-weight:bold;font-family:cursive;'><tr class='warning'><td>状态编码</td><td>提示信息</td></tr></thead>";
					for(var sIdx=0;sIdx<apiStatusesData.length;sIdx++){
						statusStr = statusStr + "<tr><td>&nbsp;"+apiStatusesData[sIdx].code+"</td><td>"+apiStatusesData[sIdx].message+"</td></tr>";
					}
					statusStr = statusStr + "</table>";	
				    statusStr = statusStr + "</div></div>";
					statusStr = statusStr + "</td><tr/>";
					fieldTableStr = fieldTableStr + statusStr;
				}else{
					fieldTableStr = fieldTableStr + "<tr><td width='15%'>&nbsp;"+fieldsData[fIdx].name+"</td><td width='45%'>"+showDataType+"</td><td width='10%'>"+isAllowEmptyCn+"</td><td width='30%'>"+fieldsData[fIdx].description+"</td></tr>";
				}
			}
			fieldTableStr = fieldTableStr + "</table>"
			returnClsStr = returnClsStr + fieldTableStr;
			returnClsStr = returnClsStr + "</div>";
			returnClsStr = returnClsStr + "</div>";
		}
		respModelStr = respModelStr + returnClsStr;
		respModelStr = respModelStr + "</div>";
		respModelStr = respModelStr + "</div>";
		methodTabStr = methodTabStr + respModelStr; //methodTabStr = methodTabStr + "1";
		methodTabStr = methodTabStr + "</div>";
		//处理响应示例
		methodTabStr = methodTabStr + "<div class='tab-pane fade' id='s-exampleId"+methodsData[k].id+"'>";
		methodTabStr = methodTabStr + "";
		methodTabStr = methodTabStr + "</div>";
		//处理变更记录
		methodTabStr = methodTabStr + "<div class='tab-pane fade' id='s-logsId"+methodsData[k].id+"'>";
		var changeLogs = "<table class='table table-condensed font-size-15'>";
		changeLogs = changeLogs + "<tr><th width='5%'>序号</th><th width='75%'>变更内容</th><th class='text-align-center' width='10%'>变更日期</th><th class='text-align-center' width='10%'>变更人</th></tr>";
		var apiLogs = methodsData[k].apiLogs;
		if(apiLogs.length>0){
			for(var logIdx=0;logIdx<apiLogs.length;logIdx++){
		       changeLogs = changeLogs + "<tr><td class='text-align-center'>"+(logIdx+1)+"</td><td>"+apiLogs[logIdx].logMsg+"</td><td class='text-align-center'>"+apiLogs[logIdx].logDate+"</td><td class='text-align-center'>"+apiLogs[logIdx].logPerson+"</td></tr>";
			}
		}else{
			changeLogs = changeLogs + "<tr><td colspan='4' class='text-align-center'>暂无</td></tr>";
		}
		changeLogs = changeLogs + "</table>";
		methodTabStr = methodTabStr + changeLogs;
		//处理变更记录
		methodTabStr = methodTabStr + "</div>";
		//处理备注说明
		methodTabStr = methodTabStr + "<div class='tab-pane fade' style='padding:15px;line-height:25px;' id='s-remarkId"+methodsData[k].id+"'>";
		if(methodsData[k].remark==""){
			methodTabStr = methodTabStr + "<div style='text-align:center;'>暂无</div>";
		}else{
			methodTabStr = methodTabStr + methodsData[k].remark;
		}
		methodTabStr = methodTabStr + "</div>";
		methodTabStr = methodTabStr + "</div>";
		/*****detail*****/
		methodStr = methodStr + methodTabStr;
		/*****请求参数*****/
		var reqHeadStr = "<div class='panel-width panel panel-info'><div class='panel-heading'><h3 class='panel-title'>请求参数</h3></div><div class='panel-body'>";
		methodStr = methodStr + reqHeadStr;
		var reqParamsStr = "<table class='table table-condensed'>";
		reqParamsStr = reqParamsStr + "<tr><th>序号</th><th>名称</th><th>值</th><th>类型</th><th>必选</th><th>描述</th></tr>";
		var parameters = methodsData[k].parameters;
		for(var paramIdx=0;paramIdx<parameters.length;paramIdx++){
			var isRequiredCls = parameters[paramIdx].isRequired==1?"required":"";
			var isRequiredCn = parameters[paramIdx].isRequired==1?"是":"否";
			reqParamsStr = reqParamsStr + "<tr><td width='10%'>&nbsp;&nbsp;"+(paramIdx+1)+"</td><td width='15%'>"+parameters[paramIdx].name+"</td><td width='20%'><input type='text' name='"+parameters[paramIdx].name+"' class='form-control normal-input-width "+isRequiredCls+"' placeholder='"+isRequiredCls+"' /></td><td width='20%'>"+parameters[paramIdx].simpleType+"</td><td width='15%'>"+isRequiredCn+"</td><td width='20%'>"+parameters[paramIdx].description+"</td></tr>";
		}
		reqParamsStr = reqParamsStr + "</table>";
		methodStr = methodStr + reqParamsStr;
		var treReqStr = "<button type='button' class='btn btn-info' onclick='tryRequest_s(this,"+methodsData[k].id+");'>试一下!</button>";
		treReqStr = treReqStr + "<div class='panel panel-info margin-top-10 div-display-none' id='s-responseDivId"+methodsData[k].id+"'>";
		treReqStr = treReqStr + "<div class='panel-heading'><h3 class='panel-title'>响应值</h3></div>";
		treReqStr = treReqStr + "<div class='panel-body' id='s-responseBodyId"+methodsData[k].id+"'></div>";
		treReqStr = treReqStr + "</div>";
		methodStr = methodStr + treReqStr; 
		methodStr = methodStr + "</div></div>";
		/*****请求参数*****/
		methodStr = methodStr + "</div>";
		methodStr = methodStr + "</div>";
	}
	//渲染html
	$("#searchContentId").html(methodStr);
	//初始化mock开关
	honeySwitch.init("#searchContentId");
	//设置当前tab下的switch开关状态
	setSwitchState_s();
	//获取api返回样例信息
	setExamplesData_s();
	//展示模态框
	$("#myModal").modal("show");
}
//设置当前tab下的switch开关状态
function setSwitchState_s(){
    $.get(apiMockMapUrl,function(apiMockMapData){
		 $("#searchContentId").find("span[id^='s-sw']").each(function(n){
			 if($(this).attr("class")=="switch-on"||$(this).attr("class")=="switch-off"){
				 var mockKey = $(this).attr("attrKey");
				 if(apiMockMapData[mockKey]){
					 honeySwitch.showOn("#"+$(this).attr("id"));
				 }else{
					 honeySwitch.showOff("#"+$(this).attr("id"));
				 }
				 //判断是否在白名单内
				 if(!isAllow){
		        	 $(this).addClass("switch-disabled");
		         }
			 }
		 });
    });
}
//设置样例数据
function setExamplesData_s(){
    $.get(apiExamplesUrl,function(apiExamplesData){
	   apiExamples = apiExamplesData;
	   //格式化样例数据
	   $("div[id^='s-exampleId']").each(function(n){
		    var idStr = $(this).attr("id");
	        var options = {
		        dom : "#"+idStr
		    };
		    var jf = new JsonFormater(options);
		    jf.doFormat(apiExamples[idStr.substring(2)]);
	   });
    });
}
//设置指定api是否启用mocker
function setApiMock_s(obj){
	 //判断是否在白名单内
 	 var hasCls = $(obj).hasClass("switch-disabled");
 	 if(!hasCls){
 		 var sId = $(obj).attr("id");
 		 var mockType;
 		 if($(obj).attr("class")=="switch-on"){
 			 mockType = 0;
 		 }
 		 if($(obj).attr("class")=="switch-off"){
 			 mockType = 1;
 		 }
 		 var apiId = sId.substring(4);
 		 //设置api mock信息
 		 $.get(apiMockReqUrl+"/"+apiId+"/"+mockType,function(apiMockData){
 			 if(apiMockData.status==200){
 				  if(apiMockData.isAllMockSet==0){
    				 honeySwitch.showOff("#allSw");
    			  }else{
    				 honeySwitch.showOn("#allSw");
    			  }
 			  }else{
 				  //开关状态复原
 				  if(mockType==0){//开启
 					 honeySwitch.showOn("#"+sId);
 				  }else{//关闭
 					 honeySwitch.showOff("#"+sId);
 				  }
 			  }
 			  showToast(apiMockData.message, true);
 		 });	  
 	 }
	 //取消事件冒泡
     var e = arguments.callee.caller.arguments[0]||window.event;
     if(e&&e.stopPropagation) {
        e.stopPropagation();
     }else if (window.event) {
        window.event.cancelBubble = true;
     }
}
//尝试请求
function tryRequest_s(obj,methodId){
	var params = {};
	var validFlag = true;
	$(obj).prev().find("input").each(function(index){
		params[$(this).attr("name")] = $(this).val();
		if($(this).hasClass("required")&&$(this).val()==""){
			$(this).addClass("input-required-border");
			validFlag = false;
		}else{
			$(this).removeClass("input-required-border");
		}
	});
	if(validFlag){
		var reqType = "GET";
		if(-1==$("#s-methodId"+methodId).html().indexOf("GET")){
			reqType = "POST";
		}
		var paramsStr = JSON.stringify(params);
		var uriStr = $("#s-uriId"+methodId).html();
		$.post(simulateReqUrl,{paramsStr:paramsStr,reqType:reqType,uriStr:uriStr},function(retData){
        $("#s-responseDivId"+methodId).removeClass("div-display-none");
        $("#s-responseDivId"+methodId).addClass("div-display-block");
			var options = {
			   dom : "#s-responseBodyId"+methodId
		    };
		    var jf = new JsonFormater(options);
		    jf.doFormat(JSON.stringify(retData));
		});
	}
}