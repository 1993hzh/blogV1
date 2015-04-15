package edu.cuit.hzhspace.controller;

import java.util.Date;

import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.cuit.hzhspace.model.Comment;
import edu.cuit.hzhspace.model.Message;
import edu.cuit.hzhspace.model.User;
import edu.cuit.hzhspace.service.CommentService;
import edu.cuit.hzhspace.service.MessageService;
import edu.cuit.hzhspace.util.RealIpGet;

@Controller
@Scope("prototype")
public class MessageController extends CustomMultiActionController {

	@Resource
	private MessageService messageService;
	@Resource
	private CommentService commentService;

	private Message entity = new Message();

	@RequestMapping(value = "/message/message!index.action")
	public ModelAndView index(String page) {
		data = queryPara(messageService, "select m from Message m order by date desc", page);
		return new ModelAndView("message/message", "data", data);
	}

	@RequestMapping(value = "/message/message!create.action")
	@ResponseBody
	public String create(String email, String content) {
		try {
			entity.setIp(RealIpGet.getRemortIP(request));
			entity.setContent(content);
			entity.setEmail(email);
			entity.setDate(new Date());
			messageService.create(entity);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
		return "true";
	}

	public String update() {
		return null;
	}

	@RequestMapping(value = "/message/message!delete.action")
	@ResponseBody
	public String delete(String id) {
		try {
			//拥有者可以删除
			if (null != getLoginUser() && getLoginUser().getUserType().equals(User.USER_TYPE_OWNER)) {
				messageService.removeByIds(id);
				return "true";
			} else {
				return "权限不足";
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/message/message!comment.action")
	@ResponseBody
	public String comment(String id, String content) {
		try {
			entity = messageService.find(id);
			commentService.createComment(Comment.COMMENT_TYPE_MESSAGE, content, entity, getLoginUser(),
					RealIpGet.getIpAddr(request));
			return "true";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	public Message getEntity() {
		return entity;
	}

	public void setEntity(Message entity) {
		this.entity = entity;
	}
}
