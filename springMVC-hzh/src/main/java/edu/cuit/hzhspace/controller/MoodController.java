package edu.cuit.hzhspace.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.cuit.hzhspace.model.Comment;
import edu.cuit.hzhspace.model.Mood;
import edu.cuit.hzhspace.model.User;
import edu.cuit.hzhspace.service.CommentService;
import edu.cuit.hzhspace.service.MoodService;
import edu.cuit.hzhspace.util.RealIpGet;

@Controller
@Scope("prototype")
public class MoodController extends CustomMultiActionController {

	@Resource
	private MoodService moodService;
	@Resource
	private CommentService commentService;

	private Mood entity = new Mood();

	public static Map<String, List<String>> favorIpAndMoodIdMap = new ConcurrentHashMap<String, List<String>>();

	@RequestMapping(value = "/mood/mood!index.action")
	public ModelAndView index(String page) {
		data.setPageSize(5);
		data = queryPara(moodService, "select m from Mood m order by date desc", page);
		return new ModelAndView("mood/mood", "data", data);
	}

	@RequestMapping(value = "/mood/mood!create.action")
	@ResponseBody
	public String create(String content) {
		try {
			if (null == getLoginUser() || getLoginUser().getUserType().equals(User.USER_TYPE_GUEST)) {
				return "权限不足";
			}
			entity.setContent(content);
			entity.setCreator(getLoginUser());
			entity.setDate(new Date());
			moodService.create(entity);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
		return "true";
	}

	@RequestMapping(value = "/mood/mood!update.action")
	@ResponseBody
	public String update(String id) {
		try {
			entity = moodService.find(id);
			return mapper.writeValueAsString(entity);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/mood/mood!delete.action")
	@ResponseBody
	public String delete(String id) {
		try {
			//拥有者可以删除
			if (null != getLoginUser() && getLoginUser().getUserType().equals(User.USER_TYPE_OWNER)) {
				moodService.removeByIds(id);
				return "true";
			} else {
				return "权限不足";
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	/**
	 * 对心情进行点赞
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/mood/mood!favor.action")
	@ResponseBody
	public String favor(String id, String type) {
		try {
			entity = moodService.find(id);
			String ip = RealIpGet.getIpAddr(request);
			if (null != getLoginUser() && entity.getCreator().getId().equals(getLoginUser().getId())) {
				return "不要给自己点赞好伐！";
			} else {
				if (!sessionCheck(id, ip)) {
					return "请不要重复点赞，这样很可耻啊喂!";
				}
				if (type.equals("upper")) {
					entity.setUpper(entity.getUpper() + 1);
				} else if (type.equals("lower")) {
					entity.setLower(entity.getLower() + 1);
				}
				moodService.edit(entity);
				sessionGenerator(id, ip);//数据库提交事务之后再给session赋值
				return "true";
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	/**
	 * session中保存点赞事件及ip
	 */
	private void sessionGenerator(String id, String ip) {
		List<String> ipTemp = favorIpAndMoodIdMap.get(id);//null即该心情还没人点赞
		List<String> ips = ipTemp == null ? new LinkedList<String>() : favorIpAndMoodIdMap.get(id);
		favorIpAndMoodIdMap.put(id, ips);
	}

	/**
	 * 用一个静态变量来保存id与ip、id的键值对
	 */
	private boolean sessionCheck(String id, String ip) {
		return favorIpAndMoodIdMap.get(id) == null ? true : favorIpAndMoodIdMap.get(id).contains(ip);
	}

	@RequestMapping(value = "/mood/mood!comment.action")
	@ResponseBody
	public String comment(String id, String content) {
		try {
			if (content == null || content.trim().length() <= 0) {
				return "请不要回复空内容！";
			}
			entity = moodService.find(id);
			commentService.createComment(Comment.COMMENT_TYPE_MOOD, content, entity, getLoginUser(),
					RealIpGet.getIpAddr(request));
			return "true";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	public Mood getEntity() {
		return entity;
	}

	public void setEntity(Mood entity) {
		this.entity = entity;
	}

}
