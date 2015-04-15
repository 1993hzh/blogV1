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
<title>葫芦娃的主页</title>
<link rel="stylesheet" href="static/css/homeStyle.css">
<script src="static/script/jquery.timelinr-0.9.54.js"></script>
<script>
	$(function() {
		$().timelinr({
			arrowKeys : 'true'
		})
	});
</script>
</head>
<jsp:include page="/static/includes/top.jsp" />
<body>

	<div id="timeline">
		<ul id="dates">
			<li><a href="javascript:void(0)">1993</a></li>
			<li><a href="javascript:void(0)">1998</a></li>
			<li><a href="javascript:void(0)">2004</a></li>
			<li><a href="javascript:void(0)">2007</a></li>
			<li><a href="javascript:void(0)">2010</a></li>
			<li><a href="javascript:void(0)">2014</a></li>
		</ul>
		<ul id="issues">
				<li id="1993">
					<img src="static/images/1.png" width="256" height="256" />
					<h1>7月26日</h1>
					<p>出生在七月份的尾巴，我是狮子座</p>
				</li>
				<li id="1998">
					<img src="static/images/3.png" width="256" height="256" />
					<h1>9月1日</h1>
					<p>走向十六年漫漫求学之路</p>
				</li>
				<li id="2004">
					<img src="static/images/4.png" width="256" height="256" />
					<h1>8月30日</h1>
					<p>初中</p>
				</li>
				<li id="2007">
					<img src="static/images/5.png" width="256" height="256" />
					<h1>8月22日</h1>
					<p>高中</p>
				</li>
				<li id="2010">
					<img src="static/images/6.png" width="256" height="256" />
					<h1>9月14日</h1>
					<p>大学</p>
				</li>
				<li id="2014">
					<img src="static/images/9.png" width="256" height="256" />
					<h1>7月1日</h1>
					<p>本科毕业</p>
				</li>
		</ul>
		<div id="grad_left"></div>
		<div id="grad_right"></div>
		<a href="javascript:void(0)" id="next">+</a> <a href="javascript:void(0)" id="prev">-</a>
	</div>

</body>
<jsp:include page="/static/includes/foot.jsp" />
</html>