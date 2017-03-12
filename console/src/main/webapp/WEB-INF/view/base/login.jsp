<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<!--[if IE 8]><html lang="zh-CN" class="ie8"><![endif]-->
<!--[if IE 9]><html lang="zh-CN" class="ie9"><![endif]-->
<!--[if !IE]><!-->
<html lang="zh-CN">
<!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/login/login.css" type="text/css"></link>
    <script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/resources/js/common.js" type="text/javascript"></script>
    <title>SSR-后台管理</title>
</head>
<body>
	<div class="top_div"></div>
	<div class="div1">
		<div class="div2">
			<div class="tou"></div>
			<div class="initial_left_hand" id="left_hand"></div>
			<div class="initial_right_hand" id="right_hand"></div>
		</div>
		<div>
			<form id="loginForm" action="<%=request.getContextPath()%>/login" method="post">
				<p class="p1">
					<span class="u_logo"></span> <input class="ipt" id="loginName" name="loginName" type="text" placeholder="请输入用户" value="">
				</p>
				<p class="p2">
					<span class="p_logo"></span> <input class="ipt" id="loginPassword" name="loginPassword" type="password" placeholder="请输入密码" value="">
				</p>
				<div class="float1">
					<input class="ipt2" id="validateCode" name="validateCode" type="text" placeholder="请输入验证码" value="">
				</div>
				<div class="float2">
					<img class="validateCodeImg" id="validateCodeImg" onclick="reloadImg()" src="<%=request.getContextPath()%>/validateCode?type=loginValidateCode&length=4"/>
				</div>
				<p>
					<span id="msg" class="msg"></span>
				</p>
				<div class="div3">
					<input type="button" class="loginbtn" id="login" name="login" onclick="loginVilidate();" value="登录"/>
				</div>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		if(window != top){
			top.location.href = window.location;
		}
		$("#loginName")[0].focus();
	});

	function loginVilidate() {
		var name = $("#loginName").val().trim();
		if (name.length == 0) {
			$("#msg").html("登陆名不能为空!");
			$("#loginName")[0].focus();
			return;
		}
		var loginPassword = $("#loginPassword").val().trim();
		if (loginPassword.length == 0) {
			$("#msg").html("密码不能为空!");
			$("#loginPassword")[0].focus();
			return;
		}
		var validateCode = $("#validateCode").val().trim();
		if (validateCode.length == 0) {
			$("#msg").html("验证码不能为空!");
			$("#validateCode")[0].focus();
			return;
		}
		submitForm("loginForm","login","subSuccess","");
	}

	function subSuccess(data){
		if(data.errorMessage){
			$("#loginPassword").val("");
			$("#validateCode").val(""); 
			var t = new Date().getTime();
			$("#validateCodeImg").attr("src","<%=request.getContextPath()%>/validateCode?type=loginValidateCode&length=4&t="+t);
			$("#msg").html(data.errorMessage);
		}
		else{
			window.location.href = "<%=request.getContextPath()%>/index";
		}
	}
	
	function reloadImg() {
		var t = new Date().getTime();
		$("#validateCodeImg").attr("src","<%=request.getContextPath()%>/validateCode?type=loginValidateCode&length=4&t=" + t);
	}
	
	document.onkeydown = function(event_e) {
		if (window.event){
			event_e = window.event;
		}
		var int_keycode = event_e.charCode || event_e.keyCode;
		if (int_keycode == 13) {
			loginVilidate();
		}
	}
</script>
</html>
