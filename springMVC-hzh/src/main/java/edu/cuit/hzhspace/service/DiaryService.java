package edu.cuit.hzhspace.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.cuit.hzhspace.model.Diary;
import edu.cuit.hzhspace.model.DiaryViewLogger;
import edu.cuit.hzhspace.util.IPSeekerFactory;

@Service
public class DiaryService extends AbstractService<Diary> {

	@Autowired
	private DiaryViewLoggerService diaryViewLoggerService;

	public DiaryService(Class<Diary> entity) {
		super(entity);
	}

	public DiaryService() {
		this(Diary.class);
	}

	public void addViewLog(String ip, String diaryId) {
		try {
			List<DiaryViewLogger> dvls = diaryViewLoggerService
					.query("select dvl from DiaryViewLogger dvl where dvl.diaryId='" + diaryId + "' and dvl.ip = '"
							+ ip + "' order by date desc");
			if (dvls != null && dvls.size() > 0) {//如果存在多条重复记录则删除
				for (int i = 1; i < dvls.size(); i++) {
					diaryViewLoggerService.removeByIds(dvls.get(i).getId());
				}
				DiaryViewLogger dvl = dvls.get(0);
				dvl.setDate(new Date());
				diaryViewLoggerService.edit(dvl);
			} else {
				String result = IPSeekerFactory.getResource().getArea(ip);
				DiaryViewLogger logger = new DiaryViewLogger();
				logger.setDate(new Date());
				logger.setDiaryId(diaryId);
				logger.setIp(ip);
				logger.setPlace(result);
				diaryViewLoggerService.create(logger);
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<DiaryViewLogger> getViewLoggers(String diaryId) {
		return (List<DiaryViewLogger>) emRead
				.createQuery("select dvl from DiaryViewLogger dvl where diaryId = '" + diaryId + "' order by date desc")
				.setMaxResults(5).getResultList();
	}

	/**
	 * 根据传入的日志ID获取上一篇与下一篇的id
	 * @param diaryId
	 * @return
	 */
	public Map<String, String> getPrevAndNext(String diaryId) throws Exception {
		Query prevQuery = emRead
				.createNativeQuery("select id from t_diary where date < (select date from t_diary where id='" + diaryId
						+ "') order by date desc limit 1");
		Query nextQuery = emRead
				.createNativeQuery("select id from t_diary where date > (select date from t_diary where id='" + diaryId
						+ "') order by date asc limit 1");
		Map<String, String> prevAndNext = new HashMap<String, String>();
		prevAndNext.put("prev", prevQuery.getResultList().size() == 0 ? null : prevQuery.getSingleResult().toString());
		prevAndNext.put("next", nextQuery.getResultList().size() == 0 ? null : nextQuery.getSingleResult().toString());
		return prevAndNext;
	}
}
