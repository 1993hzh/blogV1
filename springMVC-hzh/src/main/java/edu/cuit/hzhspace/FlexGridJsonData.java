package edu.cuit.hzhspace;

import java.util.List;


/**
 * flexgrid数据封装
 * @author huitang
 *
 */
public class FlexGridJsonData {
	private int page;//当前页
	private List<?> rows;//数据记录
	private int total;//数据总数
	private int pageSize = 10;//每页大小

	public FlexGridJsonData() {
	}

	/**
	 * @param page当前页
	 * @param datas数据
	 * @param total总计录数
	 */
	public FlexGridJsonData(int page, List<?> datas, int total) {
		this.page = page;
		this.rows = datas;
		this.total = total;
	}

	/**
	 * @param page当前页
	 * @param datas数据
	 * @param total总计录数
	 * @param pageSize页面大小
	 */
	public FlexGridJsonData(int page, List<?> datas, int total, int pageSize) {
		this.page = page;
		this.rows = datas;
		this.total = total;
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 取得第一页
	 * 
	 * @return
	 */
	public int getTopPage() {
		return 1;
	}

	/**
	 * 取得上一页
	 * 
	 * @return
	 */
	public int getPreviousPage() {
		if (page <= 1) {
			return 1;
		}
		return page - 1;
	}

	/**
	 * 取得下一页
	 * 
	 * @return
	 */
	public int getNextPage() {
		if (page >= getTotalPages()) {
			return getTotalPages() == 0 ? 1 : getTotalPages();
		}
		return page + 1;
	}

	/**
	 * 取得最后一页
	 * 
	 * @return
	 */
	public int getBottomPage() {
		return getTotalPages() == 0 ? 1 : getTotalPages();
	}

	/**
	 * 总页数
	 * @return
	 */
	public int getTotalPages() {
		return (total + pageSize - 1) / pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}