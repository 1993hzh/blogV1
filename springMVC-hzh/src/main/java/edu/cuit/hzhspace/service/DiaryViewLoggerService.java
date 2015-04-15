package edu.cuit.hzhspace.service;

import org.springframework.stereotype.Service;

import edu.cuit.hzhspace.model.DiaryViewLogger;

@Service
public class DiaryViewLoggerService extends AbstractService<DiaryViewLogger> {

	public DiaryViewLoggerService(Class<DiaryViewLogger> entity) {
		super(entity);
	}
	
	public DiaryViewLoggerService() {
		this(DiaryViewLogger.class);
	}

}
