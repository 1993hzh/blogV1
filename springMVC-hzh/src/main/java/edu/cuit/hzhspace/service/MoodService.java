package edu.cuit.hzhspace.service;

import org.springframework.stereotype.Service;

import edu.cuit.hzhspace.model.Mood;

@Service
public class MoodService extends AbstractService<Mood>{

	public MoodService(Class<Mood> entity) {
		super(entity);
	}

	public MoodService() {
		this(Mood.class);
	}
}
