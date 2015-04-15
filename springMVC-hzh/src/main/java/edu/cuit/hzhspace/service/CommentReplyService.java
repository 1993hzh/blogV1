package edu.cuit.hzhspace.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.cuit.hzhspace.model.Comment;
import edu.cuit.hzhspace.model.CommentReply;
import edu.cuit.hzhspace.model.User;

@Service
public class CommentReplyService extends AbstractService<CommentReply> {

	@Autowired
	private CommentService commentService;

	public CommentReplyService(Class<CommentReply> entity) {
		super(entity);
	}

	public CommentReplyService() {
		this(CommentReply.class);
	}

	/**
	 * @param ip
	 * @param content
	 * @param comment
	 * @param type
	 * @param id
	 * @throws Exception
	 */
	public void createReply(String ip, String content, Comment comment, String type, String id, User user)
			throws Exception {
		CommentReply cr = new CommentReply();
		cr.setDate(new Date());
		cr.setIp(ip);
		if (user == null) {//非注册用户评论
			cr.setReplyer(commentService.getReplierInfo(ip));
		} else {
			cr.setReplyer(user.getName());
		}
		//根据回复对象主体不同获取不同主体的replyer
		if (type.equals(CommentReply.REPLY_TYPE_COMMENT)) {
			cr.setReplyTo(commentService.find(id).getReplyer());
		} else if (type.equals(CommentReply.REPLY_TYPE_COMMENTREPLY)) {
			cr.setReplyTo(this.find(id).getReplyer());
		}
		cr.setComment(comment);
		cr.setContent(content);
		this.create(cr);
	}

}
