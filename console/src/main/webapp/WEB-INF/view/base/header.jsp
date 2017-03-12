<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- BEGIN HEADER -->
<div class="page-header navbar navbar-fixed-top">
    <!-- BEGIN HEADER INNER -->
    <div class="page-header-inner">
        <!-- BEGIN LOGO -->
        <div class="page-logo">
            <a href="<%=request.getContextPath()%>/">
            <img width="120px" height="20px" src="<%=request.getContextPath()%>/resources/images/logo.jpg" alt="logo" class="logo-default"/>
            </a>
            <div class="menu-toggler sidebar-toggler hide">
            </div>
        </div>
        <!-- END LOGO -->
        <!-- BEGIN RESPONSIVE MENU TOGGLER -->
        <a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse">
        </a>
        <!-- END RESPONSIVE MENU TOGGLER -->
        <!-- BEGIN TOP NAVIGATION MENU -->
        <div class="top-menu">
            <ul class="nav navbar-nav pull-right">
            	<c:if test="${not empty sessionScope.auth_userName}">
            	<li>
                	<a href="${sessionScope.auth_url}">
                    <i class="icon-cloud-upload"></i>返回主页</a>
                </li>
                </c:if>
                <!-- BEGIN USER LOGIN DROPDOWN -->
                <li class="dropdown dropdown-user">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
                    <c:if test="${empty sessionScope.auth_userName}">
                    <span class="username">${sessionScope.session_user.name}</span>
                    </c:if>
                    <c:if test="${not empty sessionScope.auth_userName}">
                    <span class="username">${sessionScope.auth_userName}</span>
                    </c:if>
                    <i class="fa fa-angle-down"></i>
                    </a>
                    <ul class="dropdown-menu">
                    	<c:if test="${empty sessionScope.auth_userName}">
                        <li>
                            <a href="javascript:;" onclick="openChangePasswordModal()">
                            <i class="icon-lock"></i>修改密码</a>
                        </li>
                        </c:if>
                        <li>
                            <a href="<%=request.getContextPath()%>/exit">
                            <i class="icon-key"></i>退出登录</a>
                        </li>
                    </ul>
                </li>
                <!-- END USER LOGIN DROPDOWN -->
            </ul>
        </div>
        <!-- END TOP NAVIGATION MENU -->
    </div>
    <!-- END HEADER INNER -->
</div>
<!-- END HEADER -->
<!-- BEGIN PASSWORD -->
<div class="modal fade" id="passwordModal" tabindex="-1" role="dialog" style="display: none">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-body">
        <form class="form-horizontal" id="passwordChangeForm" action="<%=request.getContextPath()%>/updatePassword" method="POST">
          <div class="form-body">
	         <div class="form-group form-md-line-input">
	           <label class="col-md-2 control-label" for="oldPwd">原密码<span style="color:red;">*</span></label>
	           <div class="col-md-10">
	           	 <input type="password" class="form-control" required id="oldPwd" name="oldPwd" placeholder="请输入原密码">
	           	 <div class="form-control-focus">
	           	 </div>
	           </div>
	         </div>
	         <div class="form-group form-md-line-input">
	           <label class="col-md-2 control-label" for="newPwd">新密码<span style="color:red;">*</span></label>
	           <div class="col-md-10">
	           	 <input type="password" class="form-control" required id="newPwd" name="newPwd" placeholder="请输入新密码">
	           	 <div class="form-control-focus">
	           	 </div>
	           </div>
	         </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" id="passwordChangeSubmit" onclick="onChangePassword()" class="btn btn-primary">提交</button>
      </div>
    </div>
  </div>
</div>
<!-- END PASSWORD -->
<script>
function openChangePasswordModal(){
	$("#oldPwd").val("");
	$("#newPwd").val("");
	$("#passwordModal").modal("show"); 
}
function onChangePassword(){
	if(!validateForm("passwordChangeForm")){
		return;
	}
	submitForm("passwordChangeForm", "passwordChangeSubmit", "changePasswordSuc", "");
}
function changePasswordSuc(data){
	if(data.errorMessage){
		bootbox.alert(data.errorMessage);
	}
	else{
		bootbox.alert("密码修改成功!", function(){
			window.location.href="<%=request.getContextPath()%>/exit";
		});
	}
}
</script>