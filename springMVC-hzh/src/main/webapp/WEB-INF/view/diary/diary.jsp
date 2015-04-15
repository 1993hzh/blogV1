<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">
<%@include file="/static/includes/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>葫芦娃的日志</title>
<style type="text/css">
pre {
	font-size: 13px;
	overflow: hidden;
	text-overflow: ellipsis;
	width: 100%;
	max-height: 100px;
	border: 0;
	margin-top: 10px;
	word-wrap: break-word;
	white-space: pre;
	white-space: pre-wrap;
	/* 	white-space: pre-line;
	white-space: -pre-wrap;
	white-space: -o-pre-wrap;
	white-space: -moz-pre-wrap;
	white-space: -hp-pre-wrap; */
}

.diary_ul {
	padding-right: 40px;
	background: url(static/images/r_line.jpg) repeat-y right;
	width: 75%;
	float: left;
}

.diary_container {
	overflow: hidden;
	zoom: 1;
}

.diaryTheme {
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	-o-text-overflow: ellipsis;
}
</style>
<script type="text/javascript">
	var page_href = "diary/diary!index.action?page=";
	
	var active = function() {
		$("li[id='${typeId}']").addClass("active");
	}
	
	var deleteDiary = function(id) {
		$.messager.confirm("删除", "确定删除吗!", function() {
			$.ajax({
				url:'diary/diary!delete.action?id='+id,
				dataType:"text",
				type:"post",
				success:function(msg) {
					if (msg=="true") {
						$("#"+id).remove();
					} else {
						$.messager.alert(msg);
					}
				}
			});
		})
	}
	
	$(document).on("click", ".themeQueryBtn", function() {
		var themeQuery = $(this).parent().prev().val();
		if (themeQuery == null || themeQuery.trim() == "") {
			$.messager.alert("不能为空哦");
			return false;
		}
		window.location.href = "diary/diary!index.action?themeQuery="+themeQuery;
	})
</script>
</head>
<jsp:include page="/static/includes/top.jsp" />
<body onload="active()">
	<div style="width: 80%; margin: 0 auto;height:100%;">
		<ul class="diary_ul">
			<c:forEach var="diary" items="${data.rows}" varStatus="index">
			<li id="${diary.id}">
				<c:if test="${sessionScope.loginUser != null}">
					<button onclick="deleteDiary('${diary.id}')" type="button" class="close" aria-hidden="true">&times;</button>
				</c:if>
				<div class="diaryTheme"><h3><a href="diary/diary!detail.action?id=${diary.id}">${diary.theme}</a></h3></div>
				<div class="diary_detail">
					发布时间：<fmt:formatDate value="${diary.date}" pattern="yyyy-MM-dd HH:mm"/>
					&nbsp;&nbsp;阅读(${diary.viewNum})&nbsp;&nbsp;评论(${fn:length(diary.comments)})&nbsp;&nbsp;
					作者：${diary.creator.name}[<a href="diary/diary!index.action?typeId=${diary.type.id}">${diary.type.name}</a>]
				</div>
				<div class="diary_container">
					<div class="diary_content">
						<pre>${diary.content}</pre>
					</div>
					<a href="diary/diary!detail.action?id=${diary.id}" class="btn btn-success pull-right">阅读全文</a>
					<c:if test="${sessionScope.loginUser != null}">
						<a href="diary/diary!operation.action?id=${diary.id}" class="btn btn-default pull-right" style="margin-right:15px;">编辑</a>
					</c:if>
				</div>
				<hr class="message_separate" />
			</li>
			</c:forEach>
			<div class="text-center">
				<jsp:include page="/static/includes/page.jsp" />
			</div>
		</ul>
		<div style="float:right;width:25%; padding-left:15px;">
			<div class="input-group">
				<input type="text" class="form-control" placeholder="根据标题进行查找" name="themeQuery"/>
				<span class="input-group-btn">
					<button class="btn btn-default themeQueryBtn" type="button">搜索</button>
				</span>
			</div>
			<ul class="nav nav-pills nav-stacked" style="margin-top:30px;v-align:middle;text-align:center;">
				<p style="cursor:pointer;" class="well" onclick="window.location.href='diary/diary!index.action'">日志分类</p>
				<c:forEach var="type" items="${types}" varStatus="index">
					<li id="${type.id}" class="<c:if test="${typeId != null && typeId eq type.id}"> active </c:if>">
						<a href="diary/diary!index.action?typeId=${type.id}">${type.name}</a></li>
				</c:forEach>
			</ul>
		</div>
	</div>
</body>
<jsp:include page="/static/includes/foot.jsp" />
</html>