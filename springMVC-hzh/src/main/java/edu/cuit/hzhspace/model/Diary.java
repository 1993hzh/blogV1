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
 * 日志
 * @author Zhonghua Hu
 *
 */
@Entity
@Table(name = "t_diary")
public class Diary extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	public static final Integer STATE_COMMON = 0;
	public static final Integer STATE_DRAFT = 1;

	@ManyToOne
	private DiaryType type;//日志所属分类

	private String theme;//主题
	private String content;//内容
	private Date date;//发表时间

	private int state;//状态,0为正常,1为草稿

	@ManyToOne
	private User creator;//作者
	private int viewNum;//查看次数

	@OrderBy(value = "date asc")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "diary", fetch = FetchType.EAGER)
	private Set<Comment> comments;

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
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

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public int getViewNum() {
		return viewNum;
	}

	public void setViewNum(int viewNum) {
		this.viewNum = viewNum;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public DiaryType getType() {
		return type;
	}

	public void setType(DiaryType type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
