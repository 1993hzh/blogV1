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
<title>${diary.theme}</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<style type="text/css">
pre {
	font-size: 13px;
	width: 100%;
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

.detail_container{
	overflow: hidden;
	zoom: 1;
}

.viewer{
	overflow: hidden;
	zoom: 1;
}

.span_ip {
	float:left;
}

.viewer blockquote p {
	font-size: 14px;
}

.viewer blockquote {
	font-size:14px;
}

.viewer blockquote small {
	font-size:13px;
}

</style>
<script>
	function pop_comment() {
		$("#commentwrap").show();
		$("#commentwrap").dialog({
			title:"发表评论",classed:"mydialog",onClose:function() {$(this).dialog("close");},
			buttons: [
				{text: "确定",classed: "btn-primary",click:function() {createComment(this);}},
            	{text: "取消",classed: "btn-default",click:function() {$(this).dialog("close");}}
			]
		});
	}
	
	function createComment(object) {
		var id = "${diary.id}";
		var content = $("#comment_content").val();
		if (!isEmpty(content) || !isCommentLength(content)) {
			return false;
		}
		$.ajax({
			url:'diary/diary!comment.action',
			data:{id:id, content:content},
			dataType:'text',
			type:'post',
			success:function(msg) {
				if (msg == "true") {
					$(object).dialog("close");
					window.location.href = window.location.href;
				} else {
					$.messager.alert(msg);
				}
			}
		})
	}
	
	function replyComment(object, id,comment_id,principle_id,type) {
		var my_replyer = $(object).parent().prev().text();
		$("#"+id).find(".my_quick_reply").toggle();
		$("#"+id).find(".reply_comment_id").val(comment_id);
		$("#"+id).find(".reply_principle_id").val(principle_id);
		$("#"+id).find(".reply_type").val(type);
		$("#"+id).find(".comment_content").val("");
		$("#"+id).find(".comment_content").attr("placeholder", "回复  "+my_replyer+"：");
		$("#"+id).find(".comment_content").focus();
	}
	
	function replyCommentDo(object,id) {
		var commentId = $(object).parent().find(".reply_comment_id").val();
		var principleId = $(object).parent().find(".reply_principle_id").val();
		var type = $(object).parent().find(".reply_type").val();
		var content = $(object).parent().prev().val();
		if (!isEmpty(content) || !isCommentLength(content)) {
			return false;
		}
		$.ajax({
			url : 'comment/comment!reply.action',
			data : {commentId: commentId, principleId:principleId, content:content, type:type},
			dataType : 'text',
			type : 'post',
			success : function(msg) {
				if (msg == "true") {
					$("#"+id).find(".my_quick_reply").hide();
				} else {
					$.messager.alert(msg);
				}
			}
		})
	}
</script>
</head>

<body>
	<!-- create comment -->
	<div id="commentwrap" style="display:none;">
		<form id="commentwrap" role="form" action="diary/diary!create.action"
			type="post">
			<textarea id="comment_content" style="resize: none;" class="form-control" rows="4" name="comment.content"></textarea>
		</form>
	</div>
	<!-- end create -->
	<jsp:include page="/static/includes/top.jsp" />
	<div style="width: 80%; margin: 0 auto;">
		<div class="detail_container">
			<div class="diary_theme">
				<h2>${diary.theme}</h2>
				<div class="diary_about">
					<p>
						<fmt:formatDate value="${diary.date}" pattern="yyyy-MM-dd HH:mm" />
						&nbsp;&nbsp;阅读(${diary.viewNum})&nbsp;&nbsp;评论(${fn:length(diary.comments)})
					</p>
				</div>
			</div>
			<div class="pag">
				<ul class="pager">
					<li class="previous <c:if test="${singlePage.prev eq null}"> disabled</c:if>">
						<a href="diary/diary!detail.action?id=${singlePage.prev}">&larr;上一篇</a>
					</li>
					<li class="next <c:if test="${singlePage.next eq null}"> disabled</c:if>">
						<a href="diary/diary!detail.action?id=${singlePage.next}">下一篇 &rarr;</a>
					</li>
				</ul>
			</div>
			<div class="diary_content">
				<pre>${diary.content}</pre>
			</div>
			<div class="pag">
				<ul class="pager">
					<li class="previous <c:if test="${singlePage.prev eq null}"> disabled</c:if>">
						<a href="diary/diary!detail.action?id=${singlePage.prev}">&larr;上一篇</a>
					</li>
					<li class="next <c:if test="${singlePage.next eq null}"> disabled</c:if>">
						<a href="diary/diary!detail.action?id=${singlePage.next}">下一篇 &rarr;</a>
					</li>
				</ul>
			</div>
			<hr class="message_separate" />
			<div class="viewer">
				<h4>本文最近访客</h4>
				<c:forEach var="viewer" items="${viewers}">
					<blockquote class="span_ip">网友
						<c:forTokens var="ip" items="${viewer.ip}" delims="." begin="0" end="2">${ip}.</c:forTokens>*[${viewer.place}]<small>
						<fmt:formatDate value="${viewer.date}" pattern="yyyy-MM-dd HH:mm" /></small>
					</blockquote>
				</c:forEach>
			</div>
			<div class="comment">
			<hr class="message_separate" />
				<button type="button" class="btn btn-success" onclick="pop_comment()">发表评论</button>
				<c:forEach var="comments" items="${diary.comments}" varStatus="index">
					<li id="${diary.id}" class="list-group-item" style="margin-top:10px;">
						<blockquote>
							<c:if test="${sessionScope.loginUser != null}">
								<button type="button" class="close" aria-hidden="true" onclick="removeComment(this,'${comments.id}')">&times;</button>
							</c:if>
							<pre>${comments.content}</pre>
							<small class="pull-right"><span class="my_replyer">${comments.replyer}</span>&nbsp;&nbsp;
								<cite title="Source Title"><fmt:formatDate value="${comments.date}" pattern="yyyy年MM月dd日 HH:mm" />&nbsp;&nbsp;
									<a href="javascript:void(0)" class="pull-right"
										 onclick="replyComment(this,'${diary.id}','${comments.id}','${comments.id}','comment')">回复</a>
								</cite>
							</small>
						</blockquote>
						<c:forEach var="replies" items="${comments.commentReplies}" varStatus="index">
							<blockquote class="reply_comment">
								<c:if test="${sessionScope.loginUser != null}">
									<button type="button" class="close" aria-hidden="true" onclick="removeReply(this,'${replies.id}')">&times;</button>
								</c:if>
								<pre><span class="reply_span">回复&nbsp;&nbsp;${replies.replyTo}：</span>${replies.content}</pre>
								<small class="pull-right"><span class="my_replyer">${replies.replyer}</span>&nbsp;&nbsp;
									<cite title="Source Title"><fmt:formatDate value="${replies.date}" pattern="yyyy年MM月dd日 HH:mm" />
										&nbsp;&nbsp;<a href="javascript:void(0)" class="pull-right"
											 onclick="replyComment(this,'${diary.id}','${comments.id}','${replies.id}','commentreply')">回复</a>
									</cite>
								</small>
							</blockquote>
						</c:forEach>
						<div class="input-group my_quick_reply" style="display:none;">
							<input type="text" class="form-control comment_content" name="content">
							<span class="input-group-btn">
								<button class="btn btn-default" type="button" onclick="replyCommentDo(this,'${diary.id}')">回复</button>
								<input class="reply_comment_id" type="hidden" value=""/>
								<input class="reply_principle_id" type="hidden" value=""/>
								<input class="reply_type" type="hidden" value=""/>
							</span>
						</div>	
					</li>
				</c:forEach>
			</div>
		</div>
	</div>
	<jsp:include page="/static/includes/foot.jsp" />
</body>
</html>
