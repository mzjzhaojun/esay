package com.yt.app.common.common.yt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yt.app.common.enums.YtPageBeanEnum;
import com.yt.app.common.util.StringUtil;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
public class YtPageBean<T> implements YtIPage<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private static int defaultPageSize = 10;

	private Object values;

	private int pageSize = defaultPageSize;

	private int pageNo;

	private int totalCount = 0;

	public YtPageBean(Object elements) {
		this.values = elements;
	}

	public YtPageBean(Map<String, Object> param, List<T> elements, int totalCount) {
		this.pageNo = param.get(YtPageBeanEnum.PAGENO.getValue()) == null ? 0 : Integer.parseInt(param.get(YtPageBeanEnum.PAGENO.getValue()).toString());
		this.pageSize = param.get(YtPageBeanEnum.PAGESIZE.getValue()) == null ? 0 : Integer.parseInt(param.get(YtPageBeanEnum.PAGESIZE.getValue()).toString());
		if (elements == null)
			elements = new ArrayList<>();
		this.values = elements;
		this.totalCount = totalCount;
	}

	public YtPageBean(List<T> elements) {
		if (elements == null)
			elements = new ArrayList<>();
		this.values = elements;
	}

	public int getNextPageNo() {
		return getCurrent() + 1;
	}

	public int getPreviousPageNo() {
		return getCurrent() - 1;
	}

	public int getPageCount() {
		if (totalCount == 0)
			return totalCount;
		return (totalCount + pageSize - 1) / pageSize;
	}

	public static int getStartOfPage(int pageNo, int pageSize) {
		int startIndex = (pageNo - 1) * pageSize + 1;
		if (startIndex < 1)
			startIndex = 1;
		return startIndex;
	}

	public static int getDefaultPageSize() {
		return defaultPageSize;
	}

	public static boolean isPaging(Map<String, Object> param) {
		if (param != null && param.get(YtPageBeanEnum.PAGENO.getValue()) != null && StringUtil.checkNotEmpty(param.get(YtPageBeanEnum.PAGENO.getValue()).toString()))
			return true;
		else
			return false;
	}

	@Override
	public Object getRecords() {
		return this.values;
	}

	@Override
	public int getTotal() {
		return totalCount;
	}

	@Override
	public int getSize() {
		return pageSize;
	}

	@Override
	public int getCurrent() {
		return pageNo;
	}

	@Override
	public int getPages() {
		if (totalCount == 0)
			return totalCount;
		return (totalCount + pageSize - 1) / pageSize;
	}
}
