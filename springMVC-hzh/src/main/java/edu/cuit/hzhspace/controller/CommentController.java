package edu.cuit.hzhspace.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.cuit.hzhspace.model.Comment;
import edu.cuit.hzhspace.model.CommentReply;
import edu.cuit.hzhspace.model.User;
import edu.cuit.hzhspace.service.CommentReplyService;
import edu.cuit.hzhspace.service.CommentService;
import edu.cuit.hzhspace.util.RealIpGet;

@Controller
@Scope("prototype")
public class CommentController extends CustomMultiActionController {

	@Resource
	private CommentService commentService;
	@Resource
	private CommentReplyService commentReplyService;

	@RequestMapping(value = "/comment/comment!remove.action")
	@ResponseBody
	public String remove(String id) {
		try {
			//拥有者可以删除
			if (null != getLoginUser() && getLoginUser().getUserType().equals(User.USER_TYPE_OWNER)) {
				commentService.removeComment(id);
				return "true";
			} else {
				return "权限不足";
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/comment/comment!reply.action")
	@ResponseBody
	public String reply(String commentId, String principleId, String type, String content) {
		try {
			commentReplyService.createReply(RealIpGet.getIpAddr(request), content, commentService.find(commentId),
					type, principleId, getLoginUser());
			return "true";
		} catch (JpaSystemException be) {
			logger.error(be.getLocalizedMessage());
			return "回复内容太长，不要超过255个字节，你懂得！";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/comment/comment!removeReply.action")
	@ResponseBody
	public String removeReply(String principleId) {
		try {
			//拥有者可以删除
			if (null != getLoginUser() && getLoginUser().getUserType().equals(User.USER_TYPE_OWNER)) {
				commentService.removeReply(principleId);
				return "true";
			} else {
				return "权限不足";
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	/**
	 * @param commentId
	 * @return
	 */
	@RequestMapping(value = "/comment/comment!loadCommentReply.action")
	@ResponseBody
	public String loadCommentReply(String commentId) {
		try {
			StringBuffer sb = new StringBuffer();
			List<CommentReply> crs = commentReplyService.query("select cr from CommentReply cr where cr.comment.id='"
					+ commentId + "'");
			for (CommentReply cr : crs) {
				sb.append("<blockquote class=\"reply_comment\">");
				if (getLoginUser() != null) {
					sb.append("<button type=\"button\" class=\"close\" aria-hidden=\"true\" onclick=\"removeReply(this,'"
							+ cr.getId() + "')\">&times;</button>");
				}
				sb.append("<pre><span class=\"reply_span\">回复&nbsp;&nbsp;" + cr.getReplyTo() + "：</span>"
						+ cr.getContent() + "</pre>");
				sb.append("<small class=\"pull-right\"><span class=\"my_replyer\">" + cr.getReplyer()
						+ "</span>&nbsp;&nbsp;");
				sb.append("<cite title=\"Source Title\">"
						+ format.format(cr.getDate())
						+ "&nbsp;&nbsp;<a href=\"javascript:void(0)\" class=\"pull-right\" onclick=\"replyComment(this,'"
						+ commentService.getPrincipleId(commentId) + "','" + commentId + "','" + cr.getId()
						+ "','commentreply')\">回复</a></cite></small></blockquote>");
			}
			return sb.toString();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	/**
	 * @param principleId
	 * @return
	 */
	@RequestMapping(value = "/comment/comment!loadComment.action")
	@ResponseBody
	public String loadComment(String principleId) {
		try {
			StringBuffer sb = new StringBuffer();
			List<Comment> cs = commentService
					.query("select c from Comment c where c.principalId='" + principleId + "'");
			for (Comment c : cs) {
				sb.append("<li class=\"list-group-item\"><blockquote>");
				if (getLoginUser() != null) {
					sb.append("<button type=\"button\" class=\"close\" aria-hidden=\"true\" onclick=\"removeComment(this,'"
							+ c.getId() + "')\">&times;</button>");
				}
				sb.append("<pre>" + c.getContent() + "</pre><small class=\"pull-right\"><span class=\"my_replyer\">");
				sb.append(c.getReplyer() + "</span>&nbsp;&nbsp;<cite title=\"Source Title\">" + format.format(c.getDate()));
				sb.append("&nbsp;&nbsp;<a href=\"javascript:void(0)\" class=\"pull-right\" ");
				sb.append("onclick=\"replyComment(this,'" + principleId + "','" + c.getId() + "','" + c.getId()
						+ "','comment')\">回复</a>");
				sb.append("</cite></small></blockquote><div class=\"commentReply\">");
				sb.append("<a class=\"text-muted pull-right\" onclick=\"loadCommentReply(this,'" + c.getId()
						+ "')\">加载回复...</a></div></li>");
			}
			return sb.toString();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}
}
