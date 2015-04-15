package edu.cuit.hzhspace;

/**
 * 分页参数保存类
 * @author huitang
 *
 */
public class PageContext {
	private static ThreadLocal<Integer> offset = new ThreadLocal<Integer>();
	private static ThreadLocal<Integer> pagesize = new ThreadLocal<Integer>();

	public static void setOffset(int _offset) {
		offset.set(_offset);
	}

	public static int getOffset() {
		Integer os = offset.get();
		if (os == null) {
			return 0;
		}
		return os;
	}

	public static void removeOffset() {
		offset.remove();
	}

	public static void setPagesize(int _pagesize) {
		pagesize.set(_pagesize);
	}

	public static int getPagesize() {
		Integer ps = pagesize.get();
		if (ps == null) {
			return Integer.MAX_VALUE;
		}
		return ps;
	}

	public static void removePagesize() {
		pagesize.remove();
	}
}
