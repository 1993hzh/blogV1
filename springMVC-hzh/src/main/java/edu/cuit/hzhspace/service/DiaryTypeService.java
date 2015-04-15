package edu.cuit.hzhspace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.cuit.hzhspace.model.DiaryType;

@Service
public class DiaryTypeService extends AbstractService<DiaryType> {

	public DiaryTypeService(Class<DiaryType> entity) {
		super(entity);
	}
	
	public DiaryTypeService() {
		this(DiaryType.class);
	}
	
	public List<DiaryType> getDiaryTypes() {
		return this.query("select dt from DiaryType dt order by name");
	}
}
