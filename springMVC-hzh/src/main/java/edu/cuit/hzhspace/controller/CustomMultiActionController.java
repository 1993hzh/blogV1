package edu.cuit.hzhspace.controller;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import edu.cuit.hzhspace.FlexGridJsonData;
import edu.cuit.hzhspace.StringEditor;
import edu.cuit.hzhspace.model.User;
import edu.cuit.hzhspace.service.AbstractService;

/**
 * 由各controller继承
 * @author Zhonghua Hu
 *
 */
@Controller
public class CustomMultiActionController extends MultiActionController {

	protected static Logger logger = Logger.getLogger(MultiActionController.class);
	
	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpSession session;

	protected ObjectMapper mapper = new ObjectMapper();//实体对象转换json
	protected FlexGridJsonData data = new FlexGridJsonData();

	protected SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	
	/**
	 * 分页查询方法
	 * @param service
	 * @param hql
	 * @param page
	 * @return
	 */
	public <T> FlexGridJsonData queryPara(AbstractService<T> service, String hql, String page) {
		Integer ipage = 1;
		if (StringUtils.isNumeric(page) && Integer.valueOf(page) > 0) {
			ipage = Integer.valueOf(page);
		} else {
			ipage = 1;
		}
		return service.searchFlexGridPaginated(hql, (ipage - 1) * data.getPageSize(), data.getPageSize());
	}

	/**
	 * 对特殊字符进行处理，防注入
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringEditor());
	}

	protected User getLoginUser() {
		if (request.getSession().getAttribute("loginUser") == null) {
			return null;
		}
		return (User) request.getSession().getAttribute("loginUser");
	}
}
