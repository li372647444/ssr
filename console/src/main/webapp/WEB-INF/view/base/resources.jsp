<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page trimDirectiveWhitespaces="true" %>
<title>SSR-后台管理</title>
<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-COMPATIBLE" content="IE-Edge"/>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/css/fonts.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css"/>
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL PLUGIN STYLES -->
<link href="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/gritter/css/jquery.gritter.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/bootstrap-daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/datagrid/datagrid.css" rel="stylesheet" type="text/css"/>
<!-- END PAGE LEVEL PLUGIN STYLES -->
<!-- BEGIN THEME STYLES -->
<link href="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/css/components.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/css/plugins.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/resources/metronic/theme/assets/admin/layout/css/layout.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath()%>/resources/metronic/theme/assets/admin/layout/css/themes/darkblue.css" rel="stylesheet" type="text/css" id="style_color"/>
<!-- END THEME STYLES -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/respond.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/excanvas.min.js"></script> 
<![endif]-->
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/jquery.cokie.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/datagrid/datagrid.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN CORE FLOT -->
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/flot/jquery.flot.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/flot/jquery.flot.resize.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/flot/jquery.flot.pie.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/flot/jquery.flot.stack.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/flot/jquery.flot.crosshair.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/plugins/flot/jquery.flot.categories.min.js"></script>
<!-- END CORE FLOT -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/global/scripts/metronic.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/admin/layout/scripts/layout.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/resources/metronic/theme/assets/admin/layout/scripts/quick-sidebar.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/common.js"></script>
<!-- END PAGE LEVEL SCRIPTS -->