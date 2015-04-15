package edu.cuit.hzhspace.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_commentreply")
public class CommentReply extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String REPLY_TYPE_COMMENT = "comment";//对评论的回复
	public static final String REPLY_TYPE_COMMENTREPLY = "commentreply";//对评论回复的回复

	private String content;
	
	private String replyer;//ip用户为“网友[***.***.***.***]”

	private String replyTo;//回复的对象名，可能是comment的replyer，也可能是commentReply的replyer

	private String ip;
	private Date date;

	@ManyToOne
	private Comment comment;//这条回复是在哪条评论下

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReplyer() {
		return replyer;
	}

	public void setReplyer(String replyer) {
		this.replyer = replyer;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

}
