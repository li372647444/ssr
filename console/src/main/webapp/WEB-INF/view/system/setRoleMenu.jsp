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
						<i class="fa fa-angle-right"></i>
					</li>
					<li>
						<a href="<%=request.getContextPath()%>/system/setRoleMenu/${role.id}">菜单权限</a>
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
								<span class="caption-subject bold uppercase">菜单权限</span>
							</div>
						</div>
						<div class="portlet-body form">
							<form id="form" role="form" class="form-horizontal" action="<%=request.getContextPath()%>/system/setRoleMenu" method="POST">
								<input type="hidden" id="id" name="id">
								<div class="form-body">
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="name">角色名称<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<input type="text" class="form-control" required id="name" name="name" placeholder="请输入角色名称">
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="description">角色描述<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<input type="text" class="form-control" required id="description" name="description" placeholder="请输入角色描述">
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="state">角色状态<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<select class="form-control" required id="state" name="state">
												<option value="QY">启用</option>
												<option value="TY">停用</option>
											</select>
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									
									<c:set var="rights" value="${rights}"/>
									<c:forEach items="${moduleMap}" var="module">
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label">${module.value.name}<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<div class="md-checkbox-list">
												<c:forEach items="${module.value.funList}" var="fun">
												<c:if test="${fun.isMenu==true}">
												<c:set var="funId" value="${fun.id}"/>
												<div class="md-checkbox">
												<input id="${fun.id}" name="rightIds" type="checkbox" value="${fun.id}" <c:if test="${fn:contains(rights, funId)}">checked="checked"</c:if> class="md-check" />
												<label for="${fun.id}">
													<span class="inc"></span>
													<span class="check"></span>
													<span class="box"></span>
													${fun.name}
												</label>
												</div>
												</c:if>
												</c:forEach>
											</div>
										</div>
									</div>
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
	loadFrom("form", {
		id:'${role.id}',
		name:'${role.name}',
		description:'${role.description}',
		state:'${role.state}'
	});
});
function back(){
	window.location.href="<%=request.getContextPath()%>/system/sysRoleList";
}
function onSubmit(){
	if(!validateForm("form")){
		return;
	}
	var val = $('input[name="rightIds"]:checked').val();
	if (val == null) {
		bootbox.alert('请选择需要设置的菜单!');
		return;
	}
	submitForm("form", "submit", "", "id");
}
function onReset(){
	$('input[name="rightIds"]').each(function(){
		$(this).attr("checked", false);
	});
	resetForm("form");
}
function onBack(){
	window.location.href="<%=request.getContextPath()%>/system/sysRoleList";
}
</script>
</html>