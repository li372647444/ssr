<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<!--[if IE 8]><html lang="zh-CN" class="ie8"><![endif]-->
<!--[if IE 9]><html lang="zh-CN" class="ie9"><![endif]-->
<!--[if !IE]><!-->
<html lang="zh-CN">
<!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@ include file="/WEB-INF/view/base/resources.jsp"%>
    <link href="<%=request.getContextPath()%>/resources/css/error/error.css" rel="stylesheet" type="text/css"/>
</head>
<body class="page-md page-500-full-page">
<div class="row">
	<div class="col-md-12 page-500">
		<div class=" number">
			 401
		</div>
		<div class=" details">
			<h3>无访问权限!</h3>
			<p>
				无访问权限,请联系管理员!<br/><br/>
			</p>
		</div>
	</div>
</div>
</body>
</html>