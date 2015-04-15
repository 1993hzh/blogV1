package edu.cuit.hzhspace.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "t_diarytype")
public class DiaryType extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Column(unique = true)
	private String name;

	@OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
	private List<Diary> diaries;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Diary> getDiaries() {
		return diaries;
	}

	public void setDiaries(List<Diary> diaries) {
		this.diaries = diaries;
	}
}
