<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<%@include file="/static/includes/include.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>葫芦娃的留言版</title>
<style type="text/css">
.message_li {
	margin-top: 10px;
}

.my_reply {
	display:none;
}

</style>
<script type="text/javascript">
	var page_href = "message/message!index.action?page=";
	$(function() {
		$("#submit_btn").click(
			function() {
				var email = $("#email").val();
				var content = $("#content").val();
				if(!isEmail(email) || !isEmpty(content) || !isCommentLength(content)) {
					return false;
				}
				$.ajax({
					url : "message/message!create.action",
					data : $("#message_form").serialize(),
					dataType : "text",
					type : "post",
					success : function(msg) {
						if (msg == "true") {
							$.messager.alert("葫芦娃已经收到了您的留言！");
							window.location.href = window.location.href;
						} else {
							$.messager.alert(msg);
						}
					}
				})
			});
	})

	function deleteMessage(id) {
		$.ajax({
			url : "message/message!delete.action",
			data : "id=" + id,
			dataType : "text",
			type : "post",
			success : function(msg) {
				if (msg == "true") {
					$("li[id='" + id + "']").remove();
				} else {
					$.messager.alert(msg);
				}
			}
		})
	}

	function reply(object, id) {
		$(object).next().find(".comment_content").val("");
		$(object).next().toggle();
		$(object).next().find(".my_reply").hide();
		$(object).next().find(".my_comment").show();
		$(object).next().find(".comment_content").attr("placeholder", "");
		//loadComment(object, id);
	}

	function comment(object, id) {
		var content = $(object).parent().prev().val();
		if (!isEmpty(content) || !isCommentLength(content)) {
			return false;
		}
		$.ajax({
			url : 'message/message!comment.action',
			data : {id:id,content:content},
			dataType : 'text',
			type : 'post',
			success : function(msg) {
				if (msg == "true") {
					$(object).parent().parent().parent().toggle();
				} else {
					$.messager.alert(msg);
				}
			}
		})
	}
	
	function replyComment(object, id,comment_id,principle_id,type) {
		var my_replyer = $(object).parent().prev().text();
		$("#"+id).find(".my_reply").show();
		$("#"+id).find(".my_comment").hide();
		$("#"+id).find(".reply_comment_id").val(comment_id);
		$("#"+id).find(".reply_principle_id").val(principle_id);
		$("#"+id).find(".reply_type").val(type);
		$("#"+id).find(".comment_content").attr("placeholder", "回复  "+my_replyer+"：");
		$("#"+id).find(".comment_content").focus();
	}
	
	function replyCommentDo(object) {
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
					$(object).parent().parent().parent().toggle();
				} else {
					$.messager.alert(msg);
				}
			}
		})
	}
</script>
</head>
<jsp:include page="/static/includes/top.jsp" />
<body>
	<div style="width: 80%; margin: 0 auto;height:100%;">
		<div
			style="width: 60%; padding-left: 2%;padding-right: 30px; min-height: 420px;float: left; background: url(static/images/r_line.jpg) repeat-y right;">
			<ul>
				<c:forEach var="message" items="${data.rows}" varStatus="index">
					<li id="${message.id}" class="message_li">
						<div class="panel panel-default">
							<div class="panel-heading" style="height:40px;">
								<span class="badge" style="float:left;">第${data.total - data.pageSize * (data.page-1) - index.index}楼</span>
								<c:if test="${sessionScope.loginUser != null}">
									<button type="button" onclick="deleteMessage('${message.id}')" class="close pull-right" aria-hidden="true">&times;</button>
								</c:if>
								<span style="float:right;"><fmt:formatDate value="${message.date}" type="both" />&nbsp;&nbsp;</span>
							</div>
							<div class="panel-body customPre">
								<pre>${message.content}</pre>
							</div>
							<div class="panel-footer">
								From：${message.emailDTO} <a href="javascript:void(0)"
									class="pull-right" onclick="reply(this,'${message.id}')">回复（${fn:length(message.comments)}）</a>
								<div style="display:none;">
									<hr class="message_separate" />
									<ul class="list-group mycomment">
										<c:forEach var="comments" items="${message.comments}" varStatus="index">
											<li class="list-group-item">
												<blockquote>
													<c:if test="${sessionScope.loginUser != null}">
														<button type="button" class="close" aria-hidden="true" 
															onclick="removeComment(this,'${comments.id}')">&times;</button>
													</c:if>
													<pre>${comments.content}</pre>
													<small class="pull-right"><span class="my_replyer">${comments.replyer}</span>&nbsp;&nbsp;
														<cite title="Source Title"><fmt:formatDate value="${comments.date}" pattern="yyyy年MM月dd日 HH:mm" />
															&nbsp;&nbsp;<a href="javascript:void(0)" class="pull-right"
																 onclick="replyComment(this,'${message.id}','${comments.id}','${comments.id}','comment')">回复</a>
														</cite>
													</small>
												</blockquote>
												<c:forEach var="replies" items="${comments.commentReplies}" varStatus="index">
													<blockquote class="reply_comment">
														<c:if test="${sessionScope.loginUser != null}">
															<button type="button" class="close" 
																aria-hidden="true" onclick="removeReply(this,'${replies.id}')">&times;</button>
														</c:if>
														<pre><span class="reply_span">回复&nbsp;&nbsp;${replies.replyTo}：</span>${replies.content}</pre>
														<small class="pull-right"><span class="my_replyer">${replies.replyer}</span>&nbsp;&nbsp;
															<cite title="Source Title"><fmt:formatDate value="${replies.date}" pattern="yyyy年MM月dd日 HH:mm" />
																&nbsp;&nbsp;<a href="javascript:void(0)" class="pull-right"
																	 onclick="replyComment(this,'${message.id}','${comments.id}','${replies.id}','commentreply')">
																	 回复</a>
															</cite>
														</small>
													</blockquote>
												</c:forEach>
											</li>
										</c:forEach>
									</ul>
									<div class="input-group">
										<input type="text" class="form-control comment_content" name="content">
										<span class="input-group-btn">
											<button class="btn btn-default my_comment" type="button" onclick="comment(this,'${message.id}')">发布</button>
											<button class="btn btn-default my_reply" type="button" onclick="replyCommentDo(this)">回复</button>
											<input class="reply_comment_id" type="hidden" value=""/>
											<input class="reply_principle_id" type="hidden" value=""/>
											<input class="reply_type" type="hidden" value=""/>
										</span>
									</div>
									<!-- /input-group -->
								</div>
							</div>
						</div></li>
				</c:forEach>
			</ul>
			<div class="text-center"><jsp:include
					page="/static/includes/page.jsp" /></div>
		</div>
		<div
			style="padding-top:30px;padding-left: 30px; width: 40%; height: 420px;text-align: left; float: right;">
			<form id="message_form" isExtendsValidate="true">
				<div class="controls">
					<label class="control-label" for="input01"><font
						color="red">*</font>&nbsp;E-Mail&nbsp;&nbsp;</label> <input type="text"
						placeholder="请输入您的邮箱地址方便我联系您" style="width:80%;" name="email"
						class="input-xlarge" id="email" />
				</div>
				<div style="margin-top:15px;margin-bottom:5px;height: 280px;">
					<label>留言内容&nbsp;&nbsp;</label>
					<textarea id="content" name="content"
						style="margin: 0px; width: 80%;height: 80%;resize: none;" required></textarea>
					<div class="controls" style="float:right;margin:10px;">
						<input id="submit_btn" type="button" class="btn btn-success"
							value="提交" />
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
<jsp:include page="/static/includes/foot.jsp" />
</html>