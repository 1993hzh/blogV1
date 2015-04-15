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
<base href="<%=basePath%>">
<%@include file="/static/includes/include.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>葫芦娃的心情历程</title>
<link rel="stylesheet" href="static/css/MyTimeLine.css">
<style type="text/css">
.mood_href {
	color: white;
	margin-bottom: 10px;
}
</style>
<script type="text/javascript">
	var page_href="mood/mood!index.action?page=";
	$(function() {
		$(".timeline_li").each(function() {
			resize_li($(this));
		});
	})

	function deleteMood(id) {
		$.ajax({
			url : "mood/mood!delete.action",
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
		resize_li($("li[id='" + id + "']"));
		$(object).next().find(".my_reply").hide();
		$(object).next().find(".my_comment").show();
		$(object).next().find(".comment_content").attr("placeholder", "");
	}

	function comment(object, id) {
		var content = $(object).parent().prev().val();
		if (!isEmpty(content) || !isCommentLength(content)) {
			return false;
		}
		$.ajax({
			url : 'mood/mood!comment.action',
			data : {id:id, content:content},
			dataType : 'text',
			type : 'post',
			success : function(msg) {
				if (msg == "true") {
					$(object).parent().parent().parent().toggle();
					resize_li($("li[id='" + id + "']"));
				} else {
					$.messager.alert(msg);
				}
			}
		})
	}

	var resize_li = function(object) {
		var content_height = $(object).find(".content").css("height");
		$(object).css("height", content_height);
	}

	function removeComment(object, id) {
		$.ajax({
			url : 'comment/comment!remove.action',
			data : 'id=' + id,
			dataType : 'text',
			type : 'post',
			success : function(msg) {
				if (msg == "true") {
					$(object).parent().parent().remove();
				} else {
					$.messager.alert(msg);
				}
			}
		})
	}

	function favor(object, id) {
		var value = parseInt($(object).text());
		$.ajax({
			url : 'mood/mood!favor.action',
			data : 'id=' + id + '&type=upper',
			dataType : 'text',
			type : 'post',
			success : function(msg) {
				if (msg == "true") {
					$(object).text(parseInt(value + 1));
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
					$(object).parent().parent().parent().toggle();
					resize_li($("li[id='" + id + "']"));
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
		<ul class="timeline">
			<c:forEach var="mood" items="${data.rows}" varStatus="index">
				<li id="${mood.id}" class="timeline_li">
					<div class="time">
						<fmt:formatDate value="${mood.date}" pattern="yyyy-MM-dd" />
					</div>
					<div class="version">
						<fmt:formatDate value="${mood.date}" pattern="HH:mm" />
					</div>
					<div class="number" style="cursor:pointer;"
						onclick="favor(this,'${mood.id}')">${mood.upper}</div>
					<div class="content">
						<div class="detail">
							<c:if test="${sessionScope.loginUser != null}">
								<button type="button" onclick="deleteMood('${mood.id}')" class="close" aria-hidden="true">&times;</button>
							</c:if>
							<div class="mood_content customPre">
								<pre>${mood.content}</pre>
							</div>
							<a href="javascript:void(0)" class="pull-right mood_href"
								onclick="reply(this,'${mood.id}')">回复（${fn:length(mood.comments)}）</a>
							<div style="display:none;" class="mood_comment">
								<hr class="message_separate" />
								<ul class="list-group">
									<c:forEach var="comments" items="${mood.comments}" varStatus="index">
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
															 onclick="replyComment(this,'${mood.id}','${comments.id}','${comments.id}','comment')">回复</a>
													</cite>
												</small>
											</blockquote>
											<c:forEach var="replies" items="${comments.commentReplies}" varStatus="index">
												<blockquote class="reply_comment">
													<c:if test="${sessionScope.loginUser != null}">
														<button type="button" class="close" aria-hidden="true"
															onclick="removeReply(this,'${replies.id}')">&times;</button>
													</c:if>
												<pre><span class="reply_span">回复&nbsp;&nbsp;${replies.replyTo}：</span>${replies.content}</pre>
												<small class="pull-right"><span class="my_replyer">${replies.replyer}</span>&nbsp;&nbsp;
													<cite title="Source Title"><fmt:formatDate value="${replies.date}" pattern="yyyy年MM月dd日 HH:mm" />
														&nbsp;&nbsp;<a href="javascript:void(0)" class="pull-right"
															 onclick="replyComment(this,'${mood.id}','${comments.id}','${replies.id}','commentreply')">回复</a>
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
										<button class="btn btn-default my_comment" type="button" onclick="comment(this,'${mood.id}')">发布</button>
										<button class="btn btn-default my_reply" type="button" onclick="replyCommentDo(this,'${mood.id}')">回复</button>
										<input class="reply_comment_id" type="hidden" value=""/>
										<input class="reply_principle_id" type="hidden" value=""/>
										<input class="reply_type" type="hidden" value=""/>
									</span>
								</div>
								<!-- /input-group -->
							</div>
						</div>
					</div>
				</li>
			</c:forEach>
		</ul>
		<div class="text-center"><jsp:include page="/static/includes/page.jsp" /></div>
	</div>
</body>
<jsp:include page="/static/includes/foot.jsp" />
</html>