<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<!--[if IE 8]><html lang="zh-CN" class="ie8"><![endif]-->
<!--[if IE 9]><html lang="zh-CN" class="ie9"><![endif]-->
<!--[if !IE]><!-->
<html lang="zh-CN">
<!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@ include file="/WEB-INF/view/base/resources.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/view/base/header.jsp"%>
	<%@ include file="/WEB-INF/view/base/left.jsp"%>
	<!-- BEGIN CONTENT -->
	<div class="page-content-wrapper">
		<div class="page-content">
			<div class="page-bar" style="margin-top: 40px;">
				<ul class="page-breadcrumb">
					<li>
						<i class="fa fa-home"></i>
						<a href="<%=request.getContextPath()%>/index">首页</a>
						<i class="fa fa-angle-right"></i>
					</li>
					<li>
						<a href="<%=request.getContextPath()%>/dynamic/dynamicManage/${table.tableName}/list">${table.remark}管理</a>
					</li>
				</ul>
			</div>
			<!-- BEGIN FROM -->
			<div class="row">
				<div class="col-md-12">
					<div class="portlet light bordered">
						<div class="portlet-title">
							<div class="caption font-green-haze">
								<i class="icon-user font-green-haze"></i>
								<span class="caption-subject bold uppercase">查询条件</span>
							</div>
						</div>
						<div class="portlet-body form">
							<form id="form" class="form-horizontal">
								<div class="form-body">
									<c:forEach items="${columns}" var="column">
										<c:if test="${column.isQueryCondition==true}">
											<c:if test="${column.typeForMysql=='varchar' || column.typeForMysql=='text'}">
												<div class="form-group form-md-line-input">
													<label class="col-md-2 control-label" for="${column.columnName}">${column.remark}
													</label>
													<div class="col-md-10">
														<input type="text" class="form-control" id="${column.columnName}" name="${column.columnName}" placeholder="请输入${column.remark}">
														<div class="form-control-focus">
														</div>
													</div>
												</div>
											</c:if>
										</c:if>
									</c:forEach>
								</div>
								<div class="form-actions">
									<div class="row">
										<div class="col-md-offset-2 col-md-10">
											<button type="button" id="submit" class="btn green" onclick="onSubmit()">查询</button>
											<button type="button" class="btn default" onclick="onReset()">重置</button>
											<button type="button" class="btn default" onclick="onNew()">新增</button>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
					<!-- BEGIN SAMPLE TABLE PORTLET-->
					<div class="portlet box green">
						<div class="portlet-title">
							<div class="caption">
								<i class="fa fa-cogs"></i>列表
							</div>
							<div class="tools">
							</div>
						</div>
						<div class="portlet-body">
							<div class="table-scrollable">
								<table id="grid" class="table table-striped table-bordered table-hover datagrid">
								</table>
							</div>
						</div>
					</div>
					<!-- END SAMPLE TABLE PORTLET-->
				</div>
			</div>
			<!-- END FROM -->
		</div>
	</div>
	<!-- END CONTENT -->
	<%@ include file="/WEB-INF/view/base/footer.jsp"%>
</body>
<script>
var listDisplaycolumns = [];
var queryColumns = [];
$(function() {
	Metronic.init();
	Layout.init();
	QuickSidebar.init();
	initMenu("_dynamic_dynamicManage_"+'${table.tableName}'+"_list");
	<c:forEach items="${columns}" var="column">
		if("${column.isListDisplay}"=='true'){
			if("${column.typeForMysql}"=='enum'){
				listDisplaycolumns.push({"property":"${column.columnName}","label":"${column.remark}","align":'center',"sortable":false,"render": function(val,row,index){
						<c:forEach items="${column.enumValue}" var="enumValue">
							if("${enumValue.split('-')[0]}"==val){
								return "${enumValue.split('-')[1]}";
							}
						</c:forEach>
						return "";
					}
				});
			} else {
				listDisplaycolumns.push({"property":"${column.columnName}","label":"${column.remark}","align":'center',"sortable":false});
			}
		}
		if("${column.isQueryCondition}"){
			queryColumns.push({"columnName":"${column.columnName}"});
		}
	</c:forEach>
	
	var optFunction = function(val,row,index){
		var html = "<a href='<%=request.getContextPath()%>/dynamic/dynamicManage/${table.tableName}/update/"+row.id+"'>修改</a>";
		html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#' onclick='deleteRow("+row.id+")'>删除</a>";
		return html;
	}
	
	listDisplaycolumns.push({"property":"opt","label":"操作","align":'center',"sortable":false,"render":optFunction});
	var dataSource = new girdDataSource({
		columns: listDisplaycolumns,
		loadFromServer: true,
		data: "<%=request.getContextPath()%>/dynamic/dynamicManage/${table.tableName}/list/"
	});
	$("#grid").datagrid({dataSource: dataSource});
});
function onSubmit(){
	var data = new Object();
	for(var index=0;index<queryColumns.length;index++){
		var columnName = queryColumns[index].columnName;
		data[columnName] = $("#"+columnName).val();
	}
	console.log(data);
	$('#grid').datagrid('reload',data);
}
function onReset(){
	resetForm("form");
}
function onNew(){
	window.location.href="<%=request.getContextPath()%>/dynamic/dynamicManage/${table.tableName}/add/"
}
function deleteRow(id){
	bootbox.confirm("确定删除信息吗？", function(result) {
		if (result) {  
			 $.ajax({
		        type	: 'post',
		        url		: "<%=request.getContextPath()%>/dynamic/dynamicManage/${table.tableName}/delete/"+id,
		        success : function(data) {
		        	if(data.success == false || data.message){
	                	bootbox.alert(data.errorMessage);
		            } else {
		            	bootbox.alert("删除成功");
		            	onSubmit();
		            }
		  		}, 
		  		error : function(XMLHttpRequest, textStatus, errorThrown) {
		            bootbox.alert(XMLHttpRequest.responseText);
		  		}
	        });
        }
	});
}

</script>
</html>