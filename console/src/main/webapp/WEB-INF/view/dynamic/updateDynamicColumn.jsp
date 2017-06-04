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
						<a href="<%=request.getContextPath()%>/dynamic/dynamicTableList">动态表管理</a>
						<i class="fa fa-angle-right"></i>
					</li>
					<li>
						<a href="<%=request.getContextPath()%>/dynamic/dynamicColumnList/${dynamicTableManage.id}">${dynamicTableManage.tableName}表的列管理</a>
						<i class="fa fa-angle-right"></i>
					</li>
					<li>
						<a href="<%=request.getContextPath()%>/dynamic/updateDynamicColumn/${dynamicColumnManage.id}">${dynamicColumnManage.columnName}列修改</a>
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
								<span class="caption-subject bold uppercase">列修改</span>
							</div>
						</div>
						<div class="portlet-body form">
							<form id="form" role="form" class="form-horizontal" action="<%=request.getContextPath()%>/dynamic/updateDynamicColumn" method="POST">
								<input type="hidden" id="id" name="id">
								<input type="hidden" id="tableId" name="tableId">
								<div class="form-body">
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="columnName">列名<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<input type="text" class="form-control" required id="columnName" name="columnName" placeholder="请输入列名">
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="typeForMysql">Mysql数据库列类型<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<select class="form-control" required id="typeForMysql" name="typeForMysql">
												<c:forEach items="${typesForMysql}" var="type">
													<option value="${type.index}">${type.name}</option>
												</c:forEach>
											</select>
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="length">长度<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<input type="text" class="form-control" required id="length" name="length" placeholder="请输入长度">
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="decimalPoint">小数点数<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<input type="text" class="form-control" required id="decimalPoint" name="decimalPoint" placeholder="请输入小数点数">
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="enumValue">枚举值<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<input type="text" class="form-control" required id="enumValue" name="enumValue" placeholder="请输入枚举值,如（0-否,1-是）">
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="nullable">是否可为空<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<select class="form-control" required id="nullable" name="nullable">
												<option value="true" selected="selected">是</option>
												<option value="false">否</option>
											</select>
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="insertDefaultValue">新增时默认值</label>
										<div class="col-md-10">
											<input type="text" class="form-control" id="insertDefaultValue" name="insertDefaultValue" placeholder="请输入新增时默认值">
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="isQueryCondition">是否作为查询条件<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<select class="form-control" required id="isQueryCondition" name="isQueryCondition">
												<option value="true" selected="selected">是</option>
												<option value="false">否</option>
											</select>
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="queryConditionSymbol">查询条件符号<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<select class="form-control" required id="queryConditionSymbol" name="queryConditionSymbol">
												<c:forEach items="${queryConditionSymbols}" var="symbol">
													<option value="${symbol.index}">${symbol.desc}</option>
												</c:forEach>
											</select>
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="queryDefaultValue">查询时默认值</label>
										<div class="col-md-10">
											<input type="text" class="form-control" id="queryDefaultValue" name="queryDefaultValue" placeholder="请输入查询时默认值">
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="isAllowUpdate">是否允许修改<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<select class="form-control" required id="isAllowUpdate" name="isAllowUpdate">
												<option value="true" selected="selected">是</option>
												<option value="false">否</option>
											</select>
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="isListDisplay">列表时是否显示<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<select class="form-control" required id="isListDisplay" name="isListDisplay">
												<option value="true" selected="selected">是</option>
												<option value="false">否</option>
											</select>
											<div class="form-control-focus">
											</div>
										</div>
									</div>
									<div class="form-group form-md-line-input">
										<label class="col-md-2 control-label" for="remark">备注<span style="color:red;">*</span></label>
										<div class="col-md-10">
											<input type="text" class="form-control" required id="remark" name="remark" placeholder="请输入备注">
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
	initMenu("_dynamic_dynamicTableList");
	$("#typeForMysql").change(function(){
		changeTypeForMysql($(this).val());
	});
	changeTypeForMysql('${dynamicColumnManage.typeForMysql}');
	loadFrom("form", {
		id:'${dynamicColumnManage.id}',
		tableId:'${dynamicColumnManage.tableId}',
		columnName:'${dynamicColumnManage.columnName}',
		typeForMysql:'${dynamicColumnManage.typeForMysql}',
		length:'${dynamicColumnManage.length}',
		decimalPoint:'${dynamicColumnManage.decimalPoint}',
		enumValue:'${dynamicColumnManage.enumValue}',
		nullable:'${dynamicColumnManage.nullable}',
		insertDefaultValue:'${dynamicColumnManage.insertDefaultValue}',
		isQueryCondition:'${dynamicColumnManage.isQueryCondition}',
		queryConditionSymbol:'${dynamicColumnManage.queryConditionSymbol}',
		queryDefaultValue:'${dynamicColumnManage.queryDefaultValue}',
		isAllowUpdate:'${dynamicColumnManage.isAllowUpdate}',
		isListDisplay:'${dynamicColumnManage.isListDisplay}',
		remark:'${dynamicColumnManage.remark}'
	});
	$("#isQueryCondition").change(function(){
		if("false"==$(this).val()){
			disableAndRequiredControl("queryConditionSymbol",true,false);
			disableAndRequiredControl("queryDefaultValue",true,false);
		} else {
			disableAndRequiredControl("queryConditionSymbol",false,true);
			disableAndRequiredControl("queryDefaultValue",false,false);
		}
	});
});

function changeTypeForMysql(value){
	if("integer"==value || "varchar"==value){//长度启用，小数点数、枚举值禁用
		disableAndRequiredControl("length",false,true);
		disableAndRequiredControl("decimalPoint",true,false);
		disableAndRequiredControl("enumValue",true,false);
	} else if("datetime"==value || "date"==value || "text"==value || "blob"==value){//长度和小数点数、枚举值都禁用
		disableAndRequiredControl("length",true,false);
		disableAndRequiredControl("decimalPoint",true,false);
		disableAndRequiredControl("enumValue",true,false);
	} else if("double"==value || "decimal"==value){//长度和小数点数都启用，枚举值禁用
		disableAndRequiredControl("length",false,true);
		disableAndRequiredControl("decimalPoint",false,true);
		disableAndRequiredControl("enumValue",true,false);
	} else if("bit"==value){//验证长度，禁用长度、小数点数、枚举值
		disableAndRequiredControl("length",true,true);
		disableAndRequiredControl("decimalPoint",true,false);
		disableAndRequiredControl("enumValue",true,false);
		$("#length").val("1");
	} else if("enum"==value){//验证长度与枚举值，禁用小数点数
		disableAndRequiredControl("length",true,false);
		disableAndRequiredControl("decimalPoint",true,false);
		disableAndRequiredControl("enumValue",false,true);
	}
	//BLOB, TEXT, GEOMETRY or JSON column 'content' can't have a default value
	if("text"==value || "blob"==value){
		disableAndRequiredControl("insertDefaultValue",true,false);
		$("#insertDefaultValue").parent().parent().hide();
	} else{
		disableAndRequiredControl("insertDefaultValue",false,false);
		$("#insertDefaultValue").parent().parent().show();
	}
}

function disableAndRequiredControl(buttonId, disable,required) {
	$("#"+buttonId).val("");
    if(disable){
        $("#" + buttonId).attr("disabled", true);
    } else {
        $("#" + buttonId).attr("disabled", false);
    }
    if(required){
        $("#" + buttonId).attr("required", true);
    } else {
        $("#" + buttonId).attr("required", false);
    }
}
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
	window.location.href="<%=request.getContextPath()%>/dynamic/dynamicColumnList/${dynamicColumnManage.tableId}";
}
</script>
</html>