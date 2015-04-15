package edu.cuit.hzhspace.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * 留言板
 * @author Zhonghua Hu
 *
 */
@Entity
@Table(name = "t_message")
public class Message extends AbstractEntity {

	private static final long serialVersionUID = -6135564194172989450L;

	private String ip;
	private String email;
	private String content;
	private Date date;
	@OrderBy(value = "date asc")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "message", fetch = FetchType.EAGER)
	private Set<Comment> comments;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getEmailDTO() {
		try {
			String[] mails = email.split("@");
			if (mails[0].length() <= 3) {
				return "***@" + mails[1];
			}
			return mails[0].substring(0, mails[0].length() - 4).concat("***@" + mails[1]);
		} catch (Exception e) {
			return email;
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
}
