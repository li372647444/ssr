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
						<i class="fa fa-angle-right"></i>
					</li>
					<li>
						<a href="<%=request.getContextPath()%>/dynamic/dynamicManage/${table.tableName}/add">${table.remark}新增</a>
					</li>
				</ul>
			</div>
			<!-- BEGIN FROM -->
			<div class="row">
				<div class="col-md-12">
					<!-- BEGIN SAMPLE FORM PORTLET-->
					<div class="portlet light bordered">
						<div class="portlet-title">
							<div class="caption font-green-haze">
								<i class="icon-user font-green-haze"></i>
								<span class="caption-subject bold uppercase">${table.remark}新增</span>
							</div>
						</div>
						<div class="portlet-body form">
							<form id="form" role="form" class="form-horizontal" action="<%=request.getContextPath()%>/dynamic/dynamicManage/${table.tableName}/add" method="POST">
								<input type="hidden" id="id" name="id">
								<div class="form-body">
									<c:forEach items="${columns}" var="column">
										<c:if test="${column.isSystemField!=true}">
											<c:if test="${column.typeForMysql=='integer' || column.typeForMysql=='double' || column.typeForMysql=='decimal'}">
												<div class="form-group form-md-line-input">
													<label class="col-md-2 control-label" for="${column.columnName}">${column.remark}
														<c:if test="${column.nullable!=true}">
															<span style="color:red;">*</span>
														</c:if>
													</label>
													<div class="col-md-10">
														<input type="number" class="form-control" <c:if test='${column.nullable}!=true'>required</c:if> id="${column.columnName}" name="${column.columnName}" placeholder="请输入${column.remark}">
														<div class="form-control-focus">
														</div>
													</div>
												</div>
											</c:if>
											<c:if test="${column.typeForMysql=='datetime' || column.typeForMysql=='date'}">
												<div class="form-group form-md-line-input">
													<label class="col-md-2 control-label" for="${column.columnName}">${column.remark}
														<c:if test="${column.nullable!=true}">
															<span style="color:red;">*</span>
														</c:if>
													</label>
													<div class="col-md-10">
														<input type="text" class="form-control ${column.typeForMysql}" <c:if test='${column.nullable}!=true'>required</c:if> id="${column.columnName}" name="${column.columnName}" placeholder="请输入${column.remark}">
														<div class="form-control-focus">
														</div>
													</div>
												</div>
											</c:if>
											<c:if test="${column.typeForMysql=='varchar' || column.typeForMysql=='text' 
												|| column.typeForMysql=='blob'}">
												<div class="form-group form-md-line-input">
													<label class="col-md-2 control-label" for="${column.columnName}">${column.remark}
														<c:if test="${column.nullable!=true}">
															<span style="color:red;">*</span>
														</c:if>
													</label>
													<div class="col-md-10">
														<input type="text" class="form-control" <c:if test='${column.nullable}!=true'>required</c:if> id="${column.columnName}" name="${column.columnName}" placeholder="请输入${column.remark}">
														<div class="form-control-focus">
														</div>
													</div>
												</div>
											</c:if>
											<c:if test="${column.typeForMysql=='enum'}">
												<div class="form-group form-md-line-input">
													<label class="col-md-2 control-label" for="${column.columnName}">${column.remark}
														<c:if test="${column.nullable!=true}">
															<span style="color:red;">*</span>
														</c:if>
													</label>
													<div class="col-md-10">
														<select class="form-control" id="${column.columnName}" name="${column.columnName}" <c:if test='${column.nullable}!=true'>required</c:if>>
															<option value="">请选择</option>
															<c:forEach items="${column.enumValue}" var="enum_value">
																<option value="${enum_value.split('-')[0]}">${enum_value.split('-')[1]}</option>
															</c:forEach>
														</select>
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
											<button type="button" id="submit" class="btn green" onclick="onSubmit()">提交</button>
											<button type="button" class="btn default" onclick="onReset()">重置</button>
											<button type="button" class="btn default" onclick="onBack()">返回</button>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
					<!-- END SAMPLE FORM PORTLET-->
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
	initMenu("_dynamic_dynamicManage_"+'${table.tableName}'+"_list");
	$('.datetime').datepicker({
		format: 'yyyy-mm-dd',
		autoclose: true,
		language: 'zh-CN'
    });
	$('.date').datepicker({
		format: 'yyyy-mm-dd',
		autoclose: true,
		language: 'zh-CN'
    });
});
function onSubmit(){
	if(!validateForm("form")){
		return;
	}
	submitForm("form", "submit", "", "id");
}
function onReset(){
	resetForm("form");
}
function onBack(){
	window.location.href="<%=request.getContextPath()%>/dynamic/dynamicManage/${table.tableName}/list";
}
</script>
</html>