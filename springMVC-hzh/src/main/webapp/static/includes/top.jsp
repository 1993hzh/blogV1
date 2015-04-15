<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>Top</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<style type="text/css">
.center-block {
	width: 80%;
	margin-left: 20%;
}

.index_type {
	word-break:break-all;
	width:100%;
	overflow: hidden;
	zoom: 1;
}

.my_type {
	font-size:14px;
	margin-right:5px;
	margin-bottom:10px;
	cursor:pointer;
	float:left;
}

.my_type_close {
	float:none;
	margin-left:3px;
}
</style>
<script type="text/javascript">
	function GetQueryString(url) {
		var regex = new RegExp("([a-zA-Z]+://[^/]+).*[/]([^/]+$)");
		var r = url.match(regex);
		//var r = url.replace(/(.*\/){0,}([^\.]+).*/ig, "$2");
		if (r != null)
			return r[r.length - 1].substring(0, r[r.length - 1]
					.lastIndexOf("!"));
		return null;
	}
	
	function popLoginWindow() {
		$("#loginwrap").show();
		$("#loginwrap").dialog({
			title:"登录",classed:"mydialog",onClose:function() {$(this).dialog("close");},
			buttons: [
				{text: "登录",classed: "btn-primary",click:function() {login(this);}},
            	{text: "取消",classed: "btn-default",click:function() {$(this).dialog("close");}}
			]
		});
	}
	
	function login(object) {
		$("#name").next().css("display", "none");
		$("#password").next().css("display", "none");
		$("#login_msg").remove();
		var name=$("#name").val();
		var password=$("#password").val();
		var flag = 0;
		if (name.trim() == "" || name == null) {
			$("#name").next().css("display", "inline-block");
			flag++;
		}
		if (password.trim() == "" || password == null) {
			$("#password").next().css("display", "inline-block");
			flag++;
		}
		if (flag > 0) {
			return false;
		}
		$.ajax({
			url:'user/user!login.action',
			data: 'name='+name+'&password='+password,
			dataType:'text',
			type:'post',
			success:function(msg) {
				if (msg == "true") {
					$(object).dialog("close");
					window.location.href=window.location.href;
				} else {
					$("#name").val("");
					$("#password").val("");
					$(".modal-footer").prepend("<span id=\"login_msg\" style=\"margin-right:20%;color:red;\">"+msg+"</span>");
					//$.messager.alert(msg);
				}
			}
		})
	}
	
	function logout() {
		$.ajax({
			url:'user/user!logout.action',
			success:function(msg) {
				if (msg == "true") {
					window.location.href=window.location.href;
				} else {
					$.messager.alert(msg);
				}
			}
		})
	}
	
	function pop_createMood() {
		$("#moodwrap").show();
		$("#moodwrap").dialog({
			title:"发表心情",classed:"mydialog",onClose:function() {$(this).dialog("close");},
			buttons: [
				{text: "确定",classed: "btn-primary",click:function() {createMood(this);}},
            	{text: "取消",classed: "btn-default",click:function() {$(this).dialog("close");}}
			]
		});
	}
	
	function pop_createType() {
		$("#typewrap").show();
		$("#typewrap").dialog({
			title:"日志分类",classed:"mydialog",onClose:function() {$(this).dialog("close");},
		});
		inializeTypeForm();
	}
	
	function createMood(object) {
		var content=$("#mood_content").val();
		if(!isEmpty(content) || !isCommentLength(content)) {
			return false;
		}
		$.ajax({
			url:'mood/mood!create.action',
			data: 'content='+content,
			dataType:'text',
			type:'post',
			success:function(msg) {
				if (msg == "true") {
					$(object).dialog("close");
					window.location.href=window.location.href;
				} else {
					$.messager.alert(msg);
				}
			}
		})
	}
	
	var inializeTypeForm = function() {
		$.ajax({
			url:'diary/diary!indexType.action',
			dataType:'text',
			type:'post',
			success:function(msg) {
				var type = eval("("+msg+")");
				$("#index_type").html("");
				$("#type_name").val("");
				$("#type_id").val("");
				$.each(type, function(index, value){
					var append="<span class=\"label label-info my_type\"><span class=\"my_type_name\">"+value.name+"</span>";
					append = append + "<button type=\"button\" class=\"close my_type_close\" aria-hidden=\"true\">&times;</button>";
					append = append + "<input type=\"hidden\" class=\"my_type_id\" value=\""+value.id+"\"/></span>";
					$("#index_type").append(append);
				})
			}
		})
	}
	
	function createType(object, id) {
		var name=$("#type_name").val();
		var id=$("#type_id").val();
		var url = 'diary/diary!createType.action';
		if(!isEmpty(name) || !isCommentLength(name)) {
			return false;
		}
		if (id != null && id.trim() != "") {
			url = 'diary/diary!updateType.action?id='+id;
		}
		$.ajax({
			url:url,
			data: 'name='+name,
			dataType:'text',
			type:'post',
			success:function(msg) {
				$("#type_name").val("");
				$("#type_id").val("");
				var msgSubstr = msg.substr(0,4);
				var type_id = (msgSubstr==msg) ? id : msg.substr(4);
				if (msgSubstr=="true") {
					if (msgSubstr==msg) {
						$(".my_type_id").each(function() {
							if ($(this).val() == id) {
								$(this).prev().prev().html(name);
							}
						});
					} else {
						var append="<span class=\"label label-info my_type\"><span class=\"my_type_name\">"+name+"</span>";
						append = append + "<button type=\"button\" class=\"close my_type_close\" aria-hidden=\"true\">&times;</button>";
						append = append + "<input type=\"hidden\" class=\"my_type_id\" value=\""+type_id+"\"/></span>";
						$("#index_type").append(append);
					}
				} else {
					$.messager.alert(msg);
				}
			}
		})
	}
	
	$(document).on('click', '.my_type_close', function() {
		var object = $(this);
		var id=object.next().val();
		if (id == null || id.trim() == "") {
			$.messager.alert("标签没有ID，快去debug骚年");
			return false;
		}
		$.ajax({
			url:'diary/diary!deleteType.action',
			data: 'id='+id,
			dataType:'text',
			type:'post',
			success:function(msg) {
				if (msg=="true") {
					object.parent().remove();
				} else {
					$.messager.alert(msg);
				}
			}
		});
	});
	
	$(document).on('click', '.my_type_name', function() {
		var object = $(this);
		var id = object.next().next().val();
		var name = object.html();
		if (id == null || id.trim() == "") {
			$.messager.alert("标签没有ID，快去debug骚年");
			return false;
		}
		$("#type_name").val(name);
		$("#type_id").val(id);
	});
	
	$(function() {
		var url = window.document.location.href;
		var id = GetQueryString(url);
		if (id == null) {
			return;
		}
		$("li").removeClass("active");
		$("#" + id + "").addClass("active");
	})
</script>
</head>

<body>
	<nav class="navbar navbar-inverse" role="navigation">
	<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1"
		style="margin-left:10%;">
		<ul class="nav navbar-nav">
			<li id="home" class="active"><a href="home/home!index.action"><span
					class="glyphicon glyphicon-home"></span>&nbsp;&nbsp;主页</a></li>
			<li id="diary"><a href="diary/diary!index.action?page=1"><span
					class="glyphicon glyphicon-pencil"></span>&nbsp;&nbsp;日志</a></li>
			<li id="mood"><a href="mood/mood!index.action?page=1"><span
					class="glyphicon glyphicon-heart-empty"></span>&nbsp;&nbsp;心情</a></li>
			<li id="message"><a href="message/message!index.action?page=1"><span
					class="glyphicon glyphicon-comment"></span>&nbsp;&nbsp;留言</a></li>
			<li id="connect"><a href="connect/connect!index.action"><span
					class="glyphicon glyphicon-send"></span>&nbsp;&nbsp;关于</a></li>
		</ul>
		<!-- <form class="navbar-form navbar-right" role="search">
			<div class="form-group">
				<input type="text" class="form-control" placeholder="Search">
			</div>
		</form> -->
		<ul class="nav navbar-nav navbar-right">
			<li class="dropdown">
				<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"
				<c:if test="${sessionScope.loginUser eq null}">onclick="popLoginWindow()"</c:if>>
					<c:if test="${sessionScope.loginUser eq null}">&nbsp;&nbsp;登录&nbsp;&nbsp;</c:if>
					<c:if test="${sessionScope.loginUser != null}">${loginUser.name}<b
							class="caret"></b>
					</c:if> </a> <c:if test="${sessionScope.loginUser != null}">
					<ul class="dropdown-menu">
						<li><a href="diary/diary!operation.action">发表日志</a>
						</li>
						<li class="divider"></li>
						<li><a href="javascript:void(0);" onclick="pop_createMood()">发表心情</a>
						</li>
						<li class="divider"></li>
						<li><a href="javascript:void(0);" onclick="pop_createType()">日志分类</a>
						</li>
						<li class="divider"></li>
						<li><a href="javascript:void(0);" onclick="logout()">退出</a></li>
						<!-- <li><a href="#">Separated link</a></li> -->
					</ul>
				</c:if>
			</li>
		</ul>
	</div>
	</nav>
	<!-- login form -->
	<div id="loginwrap" style="display:none;">
		<form id="loginform" role="form" action="user/user!login.action"
			type="post">
			<div>
				<div class="form-group center-block">
					<label for="username">用户名：</label> <input type="username"
						style="width:60%;display:inline-block;" class="form-control"
						name="name" id="name" required>
					<div style="display:none;width:20%;">
						<span style="color:red;">&nbsp;&nbsp;不能为空哦</span>
					</div>
				</div>
			</div>
			<div>
				<div class="form-group center-block">
					<label for="password">密&nbsp;&nbsp;&nbsp;码：</label> <input
						type="password" style="width:60%;display:inline-block;"
						class="form-control" name="password" id="password" required>
					<div style="display:none;width:20%;">
						<span style="color:red;">&nbsp;&nbsp;不能为空哦</span>
					</div>
				</div>
			</div>
		</form>
	</div>
	<!-- create mood -->
	<div id="moodwrap" style="display:none;">
		<form id="moodwrap" role="form" action="mood/mood!create.action"
			type="post">
			<textarea id="mood_content" style="resize: none;" class="form-control" rows="4" name="mood.content"></textarea>
		</form>
	</div>
	<!-- create type -->
	<div id="typewrap" style="display:none;">
		<div class="index_type" id="index_type">
			
		</div>
		<hr class="message_separate" />
		<div class="create_type">
			<form id="type_detail">
				<input id="type_id" type="hidden"/>
				<input id="type_name" type="text" placeholder="输入分类名" class="input-xlarge"/>
				<div class="glyphicon glyphicon-plus-sign" style="cursor:pointer;" onclick="createType(this)"></div>
			</form>
		</div>
	</div>
	<%-- <div
		style="background-color: black; width: 100%; padding: 5% 5% 0 5%; text-align: center;">
		<p style="font-size: 45px;">Hello World!</p>
		<ul class="nav nav-tabs navbar-left">
			<li id="home" class="active"><a href="home/home!index.action"><span
					class="glyphicon glyphicon-home"></span>&nbsp;&nbsp;主页</a>
			</li>
			<li id="diary"><a href="diary/diary!index.action?page=1"><span
					class="glyphicon glyphicon-pencil"></span>&nbsp;&nbsp;日志</a>
			</li>
			<li id="mood"><a href="mood/mood!index.action?page=1"><span
					class="glyphicon glyphicon-heart-empty"></span>&nbsp;&nbsp;心情</a>
			</li>
			<li id="message"><a href="message/message!index.action?page=1"><span
					class="glyphicon glyphicon-comment"></span>&nbsp;&nbsp;留言</a>
			</li>
			<li id="connect"><a href="connect/connect!index.action"><span
					class="glyphicon glyphicon-send"></span>&nbsp;&nbsp;关于</a>
			</li>
			<form class="navbar-form navbar-left" role="search">
				<div class="form-group">
					<input type="text" class="form-control" placeholder="Search">
				</div>
			</form>
			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown">XXXXXX <b class="caret"></b> </a>
				<ul class="dropdown-menu">
					<li><a href="#">Action</a></li>
					<li><a href="#">Another action</a></li>
					<li><a href="#">Something else here</a></li>
					<li class="divider"></li>
					<li><a href="#">Separated link</a></li>
				</ul></li>
		</ul>
	</div>--%>
</body>
</html>