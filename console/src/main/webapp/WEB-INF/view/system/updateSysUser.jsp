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
						<a href="<%=request.getContextPath()%>/system/sysUserList">用户管理</a>
						<i class="fa fa-angle-right"></i>
					</li>
					<li>
						<a href="<%=request.getContextPath()%>/system/updateSysUser/${user.id}">用户修改</a>
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
								<span class="caption-subject bold uppercase">用户修改</span>
							</div>
						</div>
						<div class="portlet-body form">
							<form id="form" role="form" class="form-horizontal" action="<%=request.getContextPath()%>/system/updateSysUser" method="POST">
								<input type="hidden" id="id" name="id">
								<div class="form-body">
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="loginName">登录账号<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<input type="text" class="form-control" required id="loginName" name="loginName" placeholder="请输入登录账号">
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="name">用户姓名<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<input type="text" class="form-control" required id="name" name="name" placeholder="请输入用户姓名">
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="loginPassword">用户密码<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<input type="password" class="form-control" id="loginPassword" required name="loginPassword" placeholder="请输入密码">
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="loginPasswordAgain">确认密码<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<input type="password" class="form-control" required id="loginPasswordAgain" name="loginPasswordAgain" placeholder="请确认密码">
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="roleId">用户角色<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<select class="form-control" required id="roleId" name="roleId">
												<c:forEach items="${roles}" var="role">
													<option value="${role.id}">${role.name}</option>
												</c:forEach>
											</select>
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="state">用户状态<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<select class="form-control" required id="state" name="state">
												<option value="QY">启用</option>
												<option value="TY">停用</option>
											</select>
											<div class="form-control-focus">
											</div>
										</div>
									</div>
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
	initMenu("_system_sysUserList");
	loadFrom("form", {
		id:'${user.id}',
		loginName:'${user.loginName}',
		name:'${user.name}',
		customerId:'${customerId}',
		roleId:'${roleId}',
		state:'${user.state}'
	});
});
function onSubmit(){
	if(!validateForm("form")){
		return;
	}
	var lp = $("#loginPassword").val();
	var lpv = $("#loginPasswordAgain").val();
	if(lp != lpv){
		bootbox.alert("密码不一致!", function(){
			$("#loginPassword").val("");
			$("#loginPasswordAgain").val("");
		});
	}
	else{
		submitForm("form", "submit", "", "id");
	}
}
function onReset(){
	resetForm("form");
}
function onBack(){
	window.location.href="<%=request.getContextPath()%>/system/sysUserList";
}
</script>
</html>