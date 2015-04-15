package edu.cuit.hzhspace.service;


import org.springframework.stereotype.Service;
import edu.cuit.hzhspace.model.Message;

@Service
public class MessageService extends AbstractService<Message> {

	public MessageService(Class<Message> entity) {
		super(entity);
	}

	public MessageService() {
		this(Message.class);
	}

}
