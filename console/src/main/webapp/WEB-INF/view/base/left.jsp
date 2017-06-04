<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- BEGIN SIDEBAR -->
<div class="page-sidebar-wrapper">
    <!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
    <!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
    <div class="page-sidebar navbar-collapse collapse">
        <!-- BEGIN SIDEBAR MENU -->
        <ul class="page-sidebar-menu" data-auto-scroll="true" data-slide-speed="200">
            <!-- DOC: To remove the sidebar toggler from the sidebar you just need to completely remove the below "sidebar-toggler-wrapper" LI element -->
            <li class="sidebar-toggler-wrapper">
                <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
                <div class="sidebar-toggler">
                </div>
                <!-- END SIDEBAR TOGGLER BUTTON -->
            </li>
            <li id="m_home">
                <a href="<%=request.getContextPath()%>/">
                <i class="icon-home"></i>
                <span class="title">首页</span>
                <span id="m_home_s"></span>
                </a>
            </li>
            
            <c:forEach items="${sessionScope.session_menu}" var="module">
			<li>
				<a href="javascript:;">
				<c:if test="${module.value.id=='SSR_C_SYS'}">
				<i class="icon-user"></i>
				</c:if>
				<c:if test="${module.value.id=='SSR_C_DYNAMIC'}">
				<i class="icon-settings"></i>
				</c:if>
				<c:if test="${module.value.id=='SSR_B_DYNAMIC'}">
				<i class="icon-settings"></i>
				</c:if>
                <span class="title">${module.value.name}</span>
                <span class="arrow"></span>
                <span></span>
                </a>
				<ul class="sub-menu">
					<c:forEach items="${module.value.funList}" var="function">
					<c:if test="${function.isMenu==true}">
					<li id="${fn:replace(function.url, '/', '_')}">
						<a href="<%=request.getContextPath()%>${function.url}">${function.name}</a>
					</li>
					</c:if>
					</c:forEach>
				</ul>
			</li>
			</c:forEach>
        </ul>
        <!-- END SIDEBAR MENU -->
    </div>
</div>
<!-- END SIDEBAR -->