package edu.cuit.hzhspace;

import java.io.IOException;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class CustomMessageConventer extends AbstractHttpMessageConverter<Object> {

	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	public CustomMessageConventer() {
		super(MediaType.ALL);
	}

	@Override
	protected Long getContentLength(Object object, MediaType contentType) {
		return (long) object.toString().length();
	}

	@Override
	protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		System.out.println("dsa");
		return inputMessage.getBody();
	}

	@Override
	protected void writeInternal(Object t, HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		System.out.println("dsa1");
		outputMessage.getBody().flush();
	}

}
