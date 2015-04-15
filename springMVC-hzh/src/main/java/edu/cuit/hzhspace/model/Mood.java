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
 * 心情
 * @author Zhonghua Hu
 *
 */
@Entity
@Table(name="t_mood")
public class Mood extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	
	private String content;
	private Date date;
	@ManyToOne
	private User creator;
	private int upper;
	private int lower;
	@OrderBy(value = "date asc")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "mood", fetch = FetchType.EAGER)
	private Set<Comment> comments;


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

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public int getUpper() {
		return upper;
	}

	public void setUpper(int upper) {
		this.upper = upper;
	}

	public int getLower() {
		return lower;
	}

	public void setLower(int lower) {
		this.lower = lower;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
}
