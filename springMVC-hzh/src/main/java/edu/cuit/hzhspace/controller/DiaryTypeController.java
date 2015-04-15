package edu.cuit.hzhspace.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import edu.cuit.hzhspace.model.DiaryType;
import edu.cuit.hzhspace.model.User;
import edu.cuit.hzhspace.service.DiaryTypeService;

@Controller
@Scope("prototype")
public class DiaryTypeController extends CustomMultiActionController {

	@Resource
	private DiaryTypeService diaryTypeService;

	private DiaryType entity = new DiaryType();

	@RequestMapping(value = "/diary/diary!indexType.action")
	@ResponseBody
	public String index() {
		List<DiaryType> types = diaryTypeService.query("select dt from DiaryType dt order by name");
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		if (null != types) {
			for (DiaryType dt : types) {
				sb.append("{\"id\":\"" + dt.getId() + "\", \"name\":\"" + dt.getName() + "\"},");
			}
			if (types.size() > 0) {
				sb.deleteCharAt(sb.lastIndexOf(","));
			}
		}
		sb.append("]");
		return sb.toString();
	}

	@RequestMapping(value = "/diary/diary!createType.action")
	@ResponseBody
	public String create(String name) {
		try {
			if (null == getLoginUser() || getLoginUser().getUserType().equals(User.USER_TYPE_GUEST)) {
				return "权限不足";
			}
			entity.setName(name);
			diaryTypeService.create(entity);
		} catch (JpaSystemException jse) {
			logger.error(jse.getLocalizedMessage());
			return "分类名重复";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
		return "true" + entity.getId();
	}

	@RequestMapping(value = "/diary/diary!updateType.action")
	@ResponseBody
	public String update(String id, String name) {
		try {
			entity = diaryTypeService.find(id);
			entity.setName(name);
			diaryTypeService.edit(entity);
			return "true";
		} catch (JpaSystemException jse) {
			logger.error(jse.getLocalizedMessage());
			return "分类名重复";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/diary/diary!deleteType.action")
	@ResponseBody
	public String delete(String id) {
		try {
			//拥有者可以删除
			if (null != getLoginUser() && getLoginUser().getUserType().equals(User.USER_TYPE_OWNER)) {
				diaryTypeService.removeByIds(id);
				return "true";
			} else {
				return "权限不足";
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return e.getMessage();
		}
	}

	public DiaryType getEntity() {
		return entity;
	}

	public void setEntity(DiaryType entity) {
		this.entity = entity;
	}

}
