package edu.cuit.hzhspace.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("prototype")
public class ConnectController extends CustomMultiActionController {

	@RequestMapping(value="connect/connect!index.action")
	public String index() {
		return "connect/connect";
	}
}
