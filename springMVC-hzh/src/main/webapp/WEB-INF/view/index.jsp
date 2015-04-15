<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/static/includes/include.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>葫芦娃的二进制世界</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

</head>

<jsp:include page="/static/includes/top.jsp" />
<body>
        <!-- The default timeline stylesheet -->
        <link rel="stylesheet" href="static/assets/css/timeline.css" />
        <!-- Our customizations to the theme -->
        <link rel="stylesheet" href="static/assets/css/styles.css" />
        
        <!-- Google Fonts -->
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Dancing+Script|Antic+Slab" />
        
        <!--[if lt IE 9]>
          <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
    </head>
    
    <body>

		<div id="timeline">
			<!-- Timeline.js will genereate the markup here -->
		</div>
        
        <!-- JavaScript includes - jQuery, turn.js and our own script.js -->
		<script src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
		<script src="static/assets/js/timeline-min.js"></script>
		<script src="static/assets/js/script.js"></script>

</body>
<jsp:include page="/static/includes/foot.jsp" />
</html>