package edu.cuit.hzhspace.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import edu.cuit.hzhspace.FlexGridJsonData;
import edu.cuit.hzhspace.PageContext;

/**
 * 封装简单的CRUD和基于jpql语句的分页查询功能，复杂逻辑可覆盖此类的CRUD方法。默认使用uuc数据库，
 * 使用不同数据库时覆写getEntityManager方法<br>
 * 
 */
public abstract class AbstractService<T> {
	protected Logger logger = Logger.getLogger(AbstractService.class);
	String[] formats = new String[] { "yyyy-MM-dd_HH:mm:ss", "yyyy-MM-dd" };

	@PersistenceContext(unitName = "hzhRead")
	protected EntityManager emRead;
	
	@PersistenceContext(unitName = "hzhWrite")
	protected EntityManager emWrite;

	/**
	 * 这样写的目的是可能对数据库进行切分，即多个persistence-unit
	 * 
	 * @return
	 */
	protected EntityManager getReadEntityManager() {
		return emRead;
	}
	
	protected EntityManager getWriteEntityManager() {
		return emWrite;
	}

	protected Class<T> entity;

	public AbstractService(Class<T> entity) {
		this.entity = entity;
	}

	/**
	 * 创建实体
	 * 
	 * @param entity
	 */
	public void create(T entity) {
		getWriteEntityManager().persist(entity);
	}

	/**
	 * 更新实体
	 * 
	 * @param entity
	 */
	public void edit(T entity) {
		getWriteEntityManager().merge(entity);
	}

	/**
	 * 删除实体
	 * 
	 * @param ids
	 */
	public void remove(String id) {
		getWriteEntityManager().remove(getWriteEntityManager().merge(id));
	}

	/**
	 * 通过id的逗号表达式批量删除实体
	 * @param entity
	 * @return 
	 */
	public String removeByIds(String idList) {
		StringBuffer removed = new StringBuffer();
		if (null != idList && idList.trim().length() > 0) {
			String[] strIds = idList.split(",");
			Object[] ids = new Object[strIds.length];
			for (int i = 0; i < strIds.length; i++) {
				try {
					ids[i] = Integer.valueOf(strIds[i]);
				} catch (NumberFormatException ignore) {
					ids[i] = strIds[i];// 主建不为int
				}
			}
			for (int i = 0; i < strIds.length; i++) {
				try {
					getWriteEntityManager().remove(getWriteEntityManager().getReference(entity, ids[i]));
					removed.append(ids[i]).append(",1,");
				} catch (Exception e) {
					removed.append(ids[i]).append(",0,").append(e.getMessage());
				}
				removed.append(";");
			}
		}
		return removed.toString();
	}

	/**
	 * 根据id查找单个实体
	 * 
	 * @param entityId
	 *            实体ID或者包含实体ID的离线实体（后者必须是AbstractEntity的子类）
	 * @return
	 */
	public T find(Object entityId) {
		return getReadEntityManager().find(entity, entityId);
	}

	/**
	 * 查询所有实体
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAll() {
		CriteriaQuery cq = getReadEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entity));
		return getReadEntityManager().createQuery(cq).getResultList();
	}

	/**
	 * 查找范围内实体
	 * 
	 * @param range
	 *            int[0]开始位置 int[1]结束位置
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findRange(int[] range) {
		CriteriaQuery cq = getReadEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entity));
		Query q = getReadEntityManager().createQuery(cq);
		q.setMaxResults(range[1] - range[0]);
		q.setFirstResult(range[0]);
		return q.getResultList();
	}

	/**
	 * 查找范围内实体
	 * 
	 * @param range
	 *            int[0]开始位置 int[1]结束位置
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public List<T> searchRange(String jpql, int[] range) {
		Query query = getReadEntityManager().createQuery(jpql);
		query.setFirstResult(range[0]);
		query.setMaxResults(range[1]);
		return query.getResultList();
	}

	/**
	 * 查找范围内实体
	 * 
	 * @param range
	 *            int[0]开始位置 int[1]结束位置
	 * @return
	 */
	public List<T> searchRange(StringBuffer jpql, int[] range) {
		return searchRange(jpql.toString(), range);
	}

	/**
	 * glexgrid分页查询<br>
	 * 
	 * @param jpql
	 *            查询语句
	 * @param offset
	 *            记录开始位置
	 * @param pagesize
	 *            页面大小
	 * @return
	 */
	public FlexGridJsonData searchFlexGridPaginated(StringBuffer jpql) {
		return searchFlexGridPaginated(jpql.toString());
	}

	/**
	 * glexgrid分页查询<br>
	 * 
	 * @param jpql
	 *            查询语句
	 * @param offset
	 *            记录开始位置
	 * @param pagesize
	 *            页面大小
	 * @return
	 */
	public FlexGridJsonData searchFlexGridPaginated(String jpql) {
		return searchFlexGridPaginated(jpql, null, PageContext.getOffset(), PageContext.getPagesize());
	}

	/**
	 * glexgrid分页查询<br>
	 * 
	 * @param jpql
	 *            查询语句
	 * @param offset
	 *            记录开始位置
	 * @param pagesize
	 *            页面大小
	 * @return
	 */
	public FlexGridJsonData searchFlexGridPaginated(String jpql, int offset, int pagesize) {
		return searchFlexGridPaginated(jpql, null, offset, pagesize);
	}

	/**
	 * @param jpql
	 * @param params
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public FlexGridJsonData searchFlexGridPaginatedWithDate(String jpql) {
		return searchFlexGridPaginatedWithDate(jpql, null, PageContext.getOffset(), PageContext.getPagesize());
	}

	/**
	 * @param jpql
	 * @param params
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public FlexGridJsonData searchFlexGridPaginatedWithDate(String jpql, String[] params) {
		return searchFlexGridPaginatedWithDate(jpql, params, PageContext.getOffset(), PageContext.getPagesize());
	}

	/**
	 * "from HandsetPositionTrajectory  refreshTime between ? and ? order by refreshtime"
	 * 按照时间优先的方式查询
	 * @param jpql
	 * @param params
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public FlexGridJsonData searchFlexGridPaginatedWithDate(String jpql, String[] params, int offset, int pagesize) {
		Object[] parseParams = new Object[params.length];
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				boolean isDate = false;
				for (String format : formats) {
					try {
						parseParams[i] = new SimpleDateFormat(format).parse(params[i]);
						isDate = true;
						break;
					} catch (ParseException ingore) {
					}
				}
				if (!isDate) {
					parseParams[i] = new String(params[i]);// 不是日期
				}
			}
		}
		return searchFlexGridPaginated(jpql, parseParams, offset, pagesize);
	}

	/**
	 * glexgrid分页查询<br>
	 * 
	 * @param jpql
	 *            查询语句
	 * @param offset
	 *            记录开始位置
	 * @param pagesize
	 *            页面大小
	 * @return
	 */
	public FlexGridJsonData searchFlexGridPaginated(String jpql, Object[] params, int offset, int pagesize) {
		// 获取结果集
		Query query = getReadEntityManager().createQuery(jpql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
		query.setFirstResult(offset);
		query.setMaxResults(pagesize);
		List<?> datas = query.getResultList();

		// 返回FlexGridJsonData
		return new FlexGridJsonData(offset / pagesize + 1, datas, getCount(jpql, params), pagesize);
	}

	/**
	 * 得到记录数据
	 */
	private int getCount(String jpql, Object[] params) {
		String formJpql = jpql;
		formJpql = "from " + StringUtils.substringAfter(jpql, "from");
		formJpql = StringUtils.substringBefore(formJpql, "order by");
		jpql = "select count(*) " + formJpql;

		// 获取记录总数
		Query query = getReadEntityManager().createQuery(jpql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
		return ((Long) query.getSingleResult()).intValue();
	}

	/**
	 * 获得记录条数
	 * @param jpql
	 * @return
	 */
	public int getCount(String jpql) {
		Query query = getReadEntityManager().createQuery(jpql);
		return ((Long) query.getSingleResult()).intValue();
	}

	/**
	 * 通过jpql语言查询
	 * 
	 * @param jpql
	 * @return
	 */
	public List<T> query(String jpql) {
		return query(jpql, null);
	}

	/**
	 * 通过jpql语言查询
	 * 
	 * @param jpql
	 * @return
	 */
	public List<T> query(String jpql, Object param) {
		return query(jpql, new Object[] { param });
	}

	/**
	 * 通过jpql语言查询
	 * 
	 * @param jpql
	 * @param params
	 *            查询参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> query(String jpql, Object[] params) {
		Query query = getReadEntityManager().createQuery(jpql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
		return query.getResultList();
	}

	/**
	 * 通过jpql语言查询唯一一条记录,如果没空返回null
	 * 
	 * @param jpql
	 * @return
	 */
	public T querySingle(String jpql) {
		return querySingle(jpql, new Object[] {});
	}

	/**
	 * 通过jpql语言查询
	 * 
	 * @param jpql
	 * @return
	 */
	public T querySingle(String jpql, Object param) {
		return querySingle(jpql, new Object[] { param });
	}

	/**
	 * 通过jpql语言查询,没有或多于一个都返回空
	 * 
	 * @param jpql
	 * @param params
	 *            查询参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T querySingle(String jpql, Object[] params) {

		Query query = getReadEntityManager().createQuery(jpql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
		T entity = null;
		try {
			entity = (T) query.getSingleResult();
		} catch (NoResultException e) {
			//e.printStackTrace();
		} catch (NonUniqueResultException e2) {//bug调试使用
			//e2.printStackTrace();
		}
		return entity;
	}

	/**
	 * 初始化ID
	 * @param ids 待处理ID列表
	 * @param isInterger 是否是整数
	 * @return
	 */
	public static Object[] initIds(String ids, boolean isInterger) {
		Object[] objIds = null;
		if (null != ids && ids.trim().length() > 0) {
			String[] strIds = ids.split(",");
			if (isInterger) {
				objIds = new Object[strIds.length];
				for (int i = 0; i < strIds.length; i++) {
					try {
						objIds[i] = Integer.valueOf(strIds[i]);
					} catch (NumberFormatException ignore) {
						objIds[i] = strIds[i];//主建不为int
					}
				}
			} else {
				objIds = strIds;
			}
		} else {
			objIds = new Object[0];
		}
		return objIds;
	}

	/**
	 * 初始化ID
	 * @param ids 待处理ID列表
	 * @param isInterger 是否是整数
	 * @return
	 */
	public static Object[] initIdsStr(String ids) {
		return initIds(ids, false);
	}

	public List<?> queryNativeQuery(String sql) {
		return emRead.createNativeQuery(sql).getResultList();
	}

	public List<?> queryNativeQuery(String sql, Object... params) {
		Query query = emRead.createNativeQuery(sql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
		return query.getResultList();
	}
}
