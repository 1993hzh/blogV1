<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<%@include file="/static/includes/include.jsp"%>
<title><c:if test="${diary.theme == null}">发表新日志</c:if> <c:if
		test="${diary.theme != null}">编辑${diary.theme}</c:if>
</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="static/plugins/ckeditor/ckeditor.js"></script>
<style>
.theme {
	overflow: hidden;
	zoom: 1;
}

.content {
	overflow: hidden;
	zoom: 1;
}

.diary_button {
	overflow: hidden;
	zoom: 1;
}
</style>
<script type="text/javascript">
	$(function(){
		<c:if test="${sessionScope.loginUser == null}">
			window.location.href = "diary/diary!index.action";
		</c:if>
		var myeditor = CKEDITOR.replace('content');
	});
     
	<c:if test="${diary.theme == null}">
		var url = "diary/diary!create.action";
	</c:if>
	<c:if test="${diary.theme != null}">
		var url = "diary/diary!update.action?id=${diary.id}";
	</c:if>
	
	var operation = function(state) {
		var theme = $("#theme").val();
		var content = CKEDITOR.instances.content.getData();
		var typeId = $("#selector").val();
		if (!isEmpty(content) || !isEmpty(theme)) {
			return false;
		}
		$.ajax({
			url:url,
			data:{theme:theme, content:content, state:state, typeId:typeId},
			dataType:"text",
			type:"post",
			success:function(msg) {
				var msgSubstr = msg.substr(0,4);
				if (msgSubstr=="true") {
					window.location.href="diary/diary!detail.action?id="+msg.substr(4);
				} else {
					$.messager.alert(msg);
				}
			}
		})
	}
</script>
</head>

<body>
	<jsp:include page="/static/includes/top.jsp" />
	<form id="diaryForm">
		<div style="width: 80%; margin: 0 auto;">
			<div class="detail_container">
				<div class="theme" style="width:100%;margin-bottom:10px;">
					<label style="width:5%;float:left;" class="control-label" for="input01"><font color="red">*</font>&nbsp;标题&nbsp;&nbsp;</label>
					<select id="selector" style="float:right;margin-left:1%;width:9%;">
						<c:forEach var="type" items="${types}">
							<option value="${type.id}" <c:if test="${type.id eq diary.type.id}">selected</c:if>>${type.name}</option>
						</c:forEach>
					</select>
					<input style="width:85%;float:right;" type="text" id="theme" name="theme" class="input-xlarge" value="${diary.theme}" />
				</div>
				<div class="content" style="width:100%;margin-bottom:10px;">
					<label style="width:5%;float:left;" class="control-label" for="input01"><font color="red">*</font>&nbsp;内容&nbsp;&nbsp;</label>
					<div style="width:95%;float:right;"><textarea id="content" name="content">&lt;p&gt;${diary.content}&lt;/p&gt;</textarea></div>
				</div>
				<div class="controls diary_button">
					<input onclick="operation('draft')" type="button" class="btn btn-default pull-right" value="保存" style="margin-left:20px;"/>
					<input onclick="operation('common')" type="button" class="btn btn-success pull-right" value="发表" />
				</div>
			</div>
		</div>
	</form>
	<jsp:include page="/static/includes/foot.jsp" />
</body>
</html>
