<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">
<%@include file="/static/includes/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>关于葫芦娃</title>
</head>
<jsp:include page="/static/includes/top.jsp" />
<body>
	<div style="width: 80%; margin: 0 auto;">
		<div
			style="width: 70%; padding-left: 2%;padding-right: 30px; float: left; background: url(static/images/r_line.jpg) repeat-y right;">
			<ul>
				<h2 class="text-center text-success">About my space</h2>
				<p class="text-success text-center">开始时间：2014年5月1日</p>
				<p class="text-success text-center">完成时间：2014年5月13日</p>
				<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;完成第一个真正意义上属于自己的地盘真是一段血泪史。</p>
				<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;从1号开始了解了各大云平台的情况，后来选择范围缩小到了SAE和GAE这两个，毕竟GAE是Google的，逼格比上Sina还是要高点的，所以选择了GAE（我当然不会说是因为GAE几乎完全免费）。后来的事实告诉我一开始的选择有多二。</p>
				<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;一直到5号左右，把GAE的Java相关的东西了解了一下，做出了一个Demo测试了一下，然后发现GAE的二级域名早被GFW全墙了，这还不算，毕竟可以跟自己的域名绑定嘛，结果绑定的时候才发现自己的CN域名Google根本不支持啊喂！！！WTF！！！</p>
				<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无奈之下只好支持国产，开始鼓捣SAE，因为SAE跟GAE的web容器都是Jetty，并且SAE暂时不支持EJB，加上个人现在真的对Struts无感，所以综合之下用了SpringMVC+Hibernate的框架（话说当初真的应该选Mybatis的，因为hibernate的HQL实在不敢恭维）。</p>
				<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;真正开发的时间大概有一个星期左右，前端用的是bootstrap毫无疑问，由于个人前端水平以及审美水平有限，前端做的并不友好，不过我不会放弃治疗的！</p>
				<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;最后，请大家多多支持，如果发现安全漏洞可以用邮箱提交给我，非常感谢！</p>
				<p></p>
			</ul>
		</div>
		<div
			style="padding-top:30px;padding-left: 30px; width: 30%; text-align: left; float: right;">
			<p>网名：葫芦娃</p>
			<p>英文名：AccIdent</p>
			<p>现居：四川—成都</p>
			<p>职业：码农</p>
			<p>
				新浪微博：<a href="http://weibo.com/u/2440019140" target="blank">葫芦娃AccIdent</a>
			</p>
			<p>
				E-mail：<a href="mailto:mail@huzhonghua.cn">mail@huzhonghua.cn</a>
			</p>
		</div>
	</div>
</body>
<jsp:include page="/static/includes/foot.jsp" />
<!-- <iframe name="content_frame" scrolling="no" marginwidth=0 marginheight=0
	width=100% height=70 src="../includes/foot.jsp" frameborder=0></iframe> -->
</html>