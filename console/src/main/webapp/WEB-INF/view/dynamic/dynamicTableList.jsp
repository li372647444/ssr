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
						<a href="<%=request.getContextPath()%>/system/dynamicTableList">动态表管理</a>
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
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="tableName">表名</label>
										<div class="col-md-10">
											<input type="text" class="form-control" id="tableName" name="tableName" placeholder="请输入表名">
											<div class="form-control-focus">
											</div>
										</div>
									</div>
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
								<i class="fa fa-cogs"></i>动态表列表
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
$(function() {
	Metronic.init();
	Layout.init();
	QuickSidebar.init();
	initMenu("_dynamic_dynamicTableList");
	var dataSource = new girdDataSource({
		columns: [
			{
				property: 'tableName',
				label: '表名',
				align: 'center',
				sortable: false
			},
			{
				property: 'remark',
				label: '描述',
				align: 'center',
				sortable: false
			},
			{
				property: 'createTime',
				label: '创建时间',
				align: 'center',
				sortable: false
			},
			{
				property: 'opt',
				label: '操作',
				align: 'center',
				sortable: false,
				render: function(val,row,index){
					var html = "<a href='<%=request.getContextPath()%>/dynamic/updateDynamicTable/"+row.id+"'>动态表修改</a>";
					html += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#' onclick='deleteRow("+row.id+")'>动态表删除</a>";
					return html;
				}
			}
		],
		loadFromServer: true,
		data: "<%=request.getContextPath()%>/dynamic/dynamicTableList"
	});
	$("#grid").datagrid({dataSource: dataSource});
});
function onSubmit(){
	var tableName = $('#tableName').val();
	$('#grid').datagrid('reload',{
		tableName: tableName
    });
}
function onReset(){
	resetForm("form");
}
function onNew(){
	window.location.href="<%=request.getContextPath()%>/dynamic/addDynamicTable";
}
function deleteRow(id){
	bootbox.confirm("确定删除信息吗？", function(result) {
		if (result) {  
			 $.ajax({
		        type	: 'post',
		        url		: "<%=request.getContextPath()%>/dynamic/deleteDynamicTable/"+id+"",
		        success : function(data) {
		        	if(data.success == false || data.message){
	                	bootbox.alert(data.errorMessage);
		            } else {
		            	bootbox.alert("提交成功");
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