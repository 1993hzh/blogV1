package edu.cuit.hzhspace.controller;

import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.cuit.hzhspace.model.User;
import edu.cuit.hzhspace.service.UserService;

@Controller
@Scope("prototype")
public class UserController extends CustomMultiActionController {
	@Resource
	private UserService userService;

	private User entity = new User();

	@RequestMapping(value = "/user/user!login.action")
	@ResponseBody
	public String login(String name, String password) {
		entity = userService.login(name, password);
		if (entity != null) {
			request.getSession().setAttribute("loginUser", entity);
			return "true";
		} else {
			return "用户不存在或密码错误";
		}
	}

	@RequestMapping(value = "/user/user!logout.action")
	@ResponseBody
	public String logout() {
		try {
			request.getSession().invalidate();
			return "true";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	public User getEntity() {
		return entity;
	}

	public void setEntity(User entity) {
		this.entity = entity;
	}

}
