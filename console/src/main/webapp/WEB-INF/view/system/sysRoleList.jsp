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
						<a href="<%=request.getContextPath()%>/system/sysRoleList">角色管理</a>
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
										<label class="col-md-2 control-label" for="name">角色名称</label>
										<div class="col-md-10">
											<input type="text" class="form-control" id="name" name="name" placeholder="请输入角色名称">
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
								<i class="fa fa-cogs"></i>角色列表
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
	initMenu("_system_sysRoleList");
	var dataSource = new girdDataSource({
		columns: [
			{
				property: 'name',
				label: '角色名称',
				align: 'center',
				sortable: false
			},
			{
				property: 'state',
				label: '角色状态',
				align: 'center',
				sortable: false,
				render: function(val,row,index){
					if(val == "QY"){
			    		return "<span style='color: blue'>启用</span>";
			    	}
			    	else{
			    		return "<span style='color: red'>停用</span>";
			    	}
				}
			},
			{
				property: 'description',
				label: '角色描述',
				align: 'center',
				sortable: false
			},
			{
				property: 'opt',
				label: '操作',
				align: 'center',
				sortable: false,
				render: function(val,row,index){
					var html = "<a style='margin-right: 10px;' href='<%=request.getContextPath()%>/system/setRoleMenu/"+row.id+"'>菜单设定</a>";
			    	html += "<a style='margin-right: 10px;' href='<%=request.getContextPath()%>/system/setRoleFunction/"+row.id+"'>功能设定</a>";
			    	html += "<a href='<%=request.getContextPath()%>/system/updateSysRole/"+row.id+"'>角色修改</a>";
			    	return html;
				}
			}
		],
		loadFromServer: true,
		data: "<%=request.getContextPath()%>/system/sysRoleList"
	});
	$("#grid").datagrid({dataSource: dataSource});
});
function onSubmit(){
	var name = $('#name').val();
	$('#grid').datagrid('reload',{
        name: name
    });
}
function onReset(){
	resetForm("form");
}
function onNew(){
	window.location.href="<%=request.getContextPath()%>/system/addSysRole";
}
</script>
</html>