package edu.cuit.hzhspace.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.cuit.hzhspace.model.Comment;
import edu.cuit.hzhspace.model.Diary;
import edu.cuit.hzhspace.model.DiaryType;
import edu.cuit.hzhspace.model.User;
import edu.cuit.hzhspace.service.CommentService;
import edu.cuit.hzhspace.service.DiaryService;
import edu.cuit.hzhspace.service.DiaryTypeService;
import edu.cuit.hzhspace.util.RealIpGet;

@Controller
@Scope("prototype")
public class DiaryController extends CustomMultiActionController {

	@Resource
	private CommentService commentService;
	@Resource
	private DiaryService diaryService;
	@Resource
	private DiaryTypeService diaryTypeService;

	@SuppressWarnings("unused")
	private List<DiaryType> types = new ArrayList<DiaryType>();
	private Diary entity = new Diary();

	@RequestMapping(value = "/diary/diary!index.action")
	public ModelAndView index(String page, String typeId, String themeQuery) {
		StringBuffer sb = new StringBuffer("select m from Diary m ");
		ModelAndView mv = new ModelAndView("diary/diary");
		if (null != typeId && typeId.trim().length() > 0) {
			sb.append("where m.type.id = '" + typeId + "'");
		}
		if (null != themeQuery && themeQuery.trim().length() > 0) {
			sb.append("where m.theme like '%" + themeQuery + "%'");
		}
		sb.append(" order by date desc");
		data = queryPara(diaryService, sb.toString(), page);
		mv.addObject("data", data);
		mv.addObject("types", diaryTypeService.getDiaryTypes());
		mv.addObject("typeId", typeId);
		return mv;
	}

	@RequestMapping(value = "/diary/diary!create.action")
	@ResponseBody
	public String create(String theme, StringBuffer content, String typeId, String state) {
		try {
			if (null == getLoginUser() || getLoginUser().getUserType().equals(User.USER_TYPE_GUEST)) {
				return "权限不足";
			}
			entity.setTheme(theme);
			if (state.equals("draft")) {
				entity.setState(Diary.STATE_DRAFT);
			} else if (state.equals("common")) {
				entity.setState(Diary.STATE_COMMON);
			} else {
				return "不要逗我玩好吗";
			}
			entity.setType(diaryTypeService.find(typeId));
			entity.setContent(content.toString());
			entity.setCreator(getLoginUser());
			entity.setDate(new Date());
			diaryService.create(entity);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
		return "true" + entity.getId();
	}

	@RequestMapping(value = "/diary/diary!update.action")
	@ResponseBody
	public String update(String id, String theme, StringBuffer content, String typeId, String state) {
		try {
			entity = diaryService.find(id);
			if (state.equals("draft")) {
				entity.setState(Diary.STATE_DRAFT);
			} else if (state.equals("common")) {
				entity.setState(Diary.STATE_COMMON);
			} else {
				return "不要逗我玩好吗";
			}
			entity.setType(diaryTypeService.find(typeId));
			entity.setContent(content.toString());
			entity.setTheme(theme);
			diaryService.edit(entity);
			return "true" + entity.getId();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/diary/diary!detail.action")
	public ModelAndView diaryDetail(String id) {
		try {
			ModelAndView mv = new ModelAndView("diary/diaryDetail");
			entity = diaryService.find(id);
			Map<String, String> singlePage = diaryService.getPrevAndNext(id);
			entity.setViewNum(entity.getViewNum() + 1);
			diaryService.edit(entity);
			diaryService.addViewLog(RealIpGet.getIpAddr(request), entity.getId());
			mv.addObject("diary", entity);
			mv.addObject("singlePage", singlePage);
			mv.addObject("viewers", diaryService.getViewLoggers(entity.getId()));
			return mv;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return new ModelAndView("error/exception");
		}
	}

	@RequestMapping(value = "/diary/diary!operation.action")
	public ModelAndView diaryOperation(String id) {
		try {
			ModelAndView mv = new ModelAndView("diary/diaryOperation");
			if (null == getLoginUser() || getLoginUser().getUserType().equals(User.USER_TYPE_GUEST)) {
				return new ModelAndView("error/403");
			}
			mv.addObject("types", diaryTypeService.getDiaryTypes());
			if (id == null) {
				return mv;
			}
			entity = diaryService.find(id);
			mv.addObject("diary", entity);
			return mv;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return new ModelAndView("error/exception");
		}
	}

	@RequestMapping(value = "/diary/diary!delete.action")
	@ResponseBody
	public String delete(String id) {
		try {
			//拥有者可以删除
			if (null != getLoginUser() && getLoginUser().getUserType().equals(User.USER_TYPE_OWNER)) {
				diaryService.removeByIds(id);
				return "true";
			} else {
				return "权限不足";
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/diary/diary!comment.action")
	@ResponseBody
	public String comment(String id, String content) {
		try {
			if (content == null || content.trim().length() <= 0) {
				return "请不要回复空内容！";
			}
			entity = diaryService.find(id);
			commentService.createComment(Comment.COMMENT_TYPE_DIARY, content, entity, getLoginUser(),
					RealIpGet.getIpAddr(request));
			return "true";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	public Diary getEntity() {
		return entity;
	}

	public void setEntity(Diary entity) {
		this.entity = entity;
	}

}
