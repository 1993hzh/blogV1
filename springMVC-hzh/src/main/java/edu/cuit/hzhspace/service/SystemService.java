package edu.cuit.hzhspace.service;

import javax.persistence.Query;

import edu.cuit.hzhspace.model.System;
import org.springframework.stereotype.Service;

@Service
public class SystemService extends AbstractService<System> {

	public SystemService(Class<System> entity) {
		super(entity);
	}

	public SystemService() {
		this(System.class);
	}

	/**
	 * @return
	 */
	public Long getTotalNum() {
		Query query = emRead.createNativeQuery("select totalNum from t_system where id = 1");
		return (query.getResultList().size() == 0) ? 0 : Long.valueOf(query.getSingleResult().toString());
	}

	public void setTotalNum(Long totalNum) {
		emWrite.createNativeQuery("update t_system set totalNum = '" + totalNum + "' where id = 1").executeUpdate();
	}

}
