package edu.cuit.hzhspace.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.cuit.hzhspace.service.SystemService;

/**
 * 在线人数监听和历史总访问量
 * @author Zhonghua Hu
 *
 */
public class OnlineListener implements HttpSessionListener {

	private static Logger logger = Logger.getLogger(HttpSessionListener.class);

	private static final int SESSION_CHECK_TIME = 30 * 60 * 1000;//有效性检测时间
	private static Long totalNumAmongCheckTime = 0L;
	private static Long lastCheckTime = System.currentTimeMillis();
	private Long savedNum;
	private SystemService systemService;
	
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		ServletContext ctx = event.getSession().getServletContext();
		systemService = (SystemService) WebApplicationContextUtils.getRequiredWebApplicationContext(ctx).getBean(
				"systemService");

		Integer onlineNum = (Integer) ctx.getAttribute("onlineNum");
		if (onlineNum == null) {
			onlineNum = new Integer(1);
		} else {
			int count = onlineNum.intValue();
			onlineNum = new Integer(count + 1);
		}
		ctx.setAttribute("onlineNum", onlineNum);
		/*
		 * 上面是计算在线人数
		 * 
		 * 下面是计算总访问量
		 */
		++totalNumAmongCheckTime;
		ctx.setAttribute("totalNum", threadRun() + totalNumAmongCheckTime);
		logger.error("在线人数：" + onlineNum);
		logger.error("总人数：" + totalNumAmongCheckTime);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		ServletContext ctx = event.getSession().getServletContext();
		Integer onlineNum = (Integer) ctx.getAttribute("onlineNum");
		if (onlineNum == null) {
			onlineNum = new Integer(0);
		} else {
			int count = onlineNum.intValue();
			onlineNum = new Integer(count - 1);
		}
		ctx.setAttribute("onlineNum", onlineNum);
	}

	private Long threadRun() {
		final long currentTime = System.currentTimeMillis();
		if (currentTime - lastCheckTime > SESSION_CHECK_TIME) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					setSavedNum(totalNumAmongCheckTime + getSavedNum());
					//时隔半小时刷新一次，保存到数据库，同时servletContext清零
					lastCheckTime = currentTime;//时间重置
					totalNumAmongCheckTime = 0L;
				}
			}).start();
		}
		return getSavedNum();
	}

	public Long getSavedNum() {
		savedNum = systemService.getTotalNum();
		return savedNum;
	}

	public void setSavedNum(Long savedNum) {
		systemService.setTotalNum(savedNum);
		this.savedNum = savedNum;
	}

}
