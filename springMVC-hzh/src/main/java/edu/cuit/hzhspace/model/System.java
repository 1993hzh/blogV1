package edu.cuit.hzhspace.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_system")
public class System extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column
	private Long totalNum = 0L;

	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}
}
