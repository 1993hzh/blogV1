package edu.cuit.hzhspace.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * 评论
 * @author Zhonghua Hu
 *
 */
@Entity
@Table(name = "t_comment")
public class Comment extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	public static final String COMMENT_TYPE_DIARY = "diary";
	public static final String COMMENT_TYPE_MESSAGE = "message";
	public static final String COMMENT_TYPE_MOOD = "mood";
	public static final String COMMENT_TYPE_COMMENT = "comment";//回复某个回复

	private String principalId;//绑定的主体id，即是对XX的评论
	private String type;//对哪一类型的回复
	private String content;

	private String ip;
	private String replyer;//回复者

	private Date date;

	@OrderBy(value = "date asc")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "comment", fetch = FetchType.EAGER)
	private Set<CommentReply> commentReplies;

	@ManyToOne
	private Mood mood;
	@ManyToOne
	private Message message;
	@ManyToOne
	private Diary diary;

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Diary getDiary() {
		return diary;
	}

	public void setDiary(Diary diary) {
		this.diary = diary;
	}

	public Mood getMood() {
		return mood;
	}

	public void setMood(Mood mood) {
		this.mood = mood;
	}

	public String getReplyer() {
		return replyer;
	}

	public void setReplyer(String replyer) {
		this.replyer = replyer;
	}

	public Set<CommentReply> getCommentReplies() {
		return commentReplies;
	}

	public void setCommentReplies(Set<CommentReply> commentReplies) {
		this.commentReplies = commentReplies;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
