package com.yt.app.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtRequestEntity;
import com.yt.app.common.enums.YtPageBeanEnum;

import cn.hutool.core.bean.BeanUtil;

public class RequestUtil {

	@SuppressWarnings("unchecked")
	public static Map<String, Object> requestEntityToParamMap(YtRequestEntity<Object> requestEntity) {
		Map<String, Object> map = new HashMap<String, Object>();
		Object obj = requestEntity.getBody();
		if (obj instanceof Map) {
			map = requestEntity.getBody() == null ? new HashMap<String, Object>() : (Map<String, Object>) requestEntity.getBody();
		} else {
			try {
				map = BeanUtil.beanToMap(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String pageNo = requestEntity.getHeaders().getFirst(YtPageBeanEnum.PAGENO.getValue());
		String orderby = requestEntity.getHeaders().getFirst(YtPageBeanEnum.ORDERBY.getValue());
		Integer start;
		Integer calculatePageSizes;
		if (orderby != null && StringUtil.checkNotEmpty(orderby)) {
			map.put(YtPageBeanEnum.ORDERBY.getValue(), orderby);
			String dir = requestEntity.getHeaders().getFirst(YtPageBeanEnum.DIR.getValue());
			if (dir == null || !StringUtil.checkNotEmpty(dir))
				map.put(YtPageBeanEnum.DIR.getValue(), YtPageBeanEnum.ASC.getValue());
			else
				map.put(YtPageBeanEnum.DIR.getValue(), dir);
		}
		if (pageNo == null || !StringUtil.checkNotEmpty(pageNo)) {
			return map;
		}
		String pageSize = requestEntity.getHeaders().getFirst(YtPageBeanEnum.PAGESIZE.getValue());
		Integer pageNos = Integer.parseInt(pageNo);
		if (pageSize == null || !StringUtil.checkNotEmpty(pageSize)) {
			calculatePageSizes = YtPageBean.getDefaultPageSize();
			start = YtPageBean.getStartOfPage(pageNos, calculatePageSizes) - 1;
		} else {
			calculatePageSizes = Integer.parseInt(pageSize);
			start = YtPageBean.getStartOfPage(pageNos, calculatePageSizes) - 1;
		}
		map.put(YtPageBeanEnum.PAGENO.getValue(), pageNo);
		map.put(YtPageBeanEnum.PAGESIZE.getValue(), calculatePageSizes);
		map.put(YtPageBeanEnum.PAGESTART.getValue(), start);
		map.put(YtPageBeanEnum.PAGEEND.getValue(), calculatePageSizes);
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> requestDecryptEntityToParamMap(YtRequestDecryptEntity<Object> requestDecryptEntity) {
		Map<String, Object> map = requestDecryptEntity.getBody() == null ? new HashMap<String, Object>() : (Map<String, Object>) requestDecryptEntity.getBody();
		String pageNo = requestDecryptEntity.getHeaders().getFirst(YtPageBeanEnum.PAGENO.getValue());
		String orderby = requestDecryptEntity.getHeaders().getFirst(YtPageBeanEnum.ORDERBY.getValue());
		Integer start;
		Integer calculatePageSizes;
		if (orderby != null && StringUtil.checkNotEmpty(orderby)) {
			map.put(YtPageBeanEnum.ORDERBY.getValue(), orderby);
			String dir = requestDecryptEntity.getHeaders().getFirst(YtPageBeanEnum.DIR.getValue());
			if (dir == null || !StringUtil.checkNotEmpty(dir))
				map.put(YtPageBeanEnum.DIR.getValue(), YtPageBeanEnum.ASC.getValue());
			else
				map.put(YtPageBeanEnum.DIR.getValue(), dir);
		}
		if (pageNo == null || !StringUtil.checkNotEmpty(pageNo)) {
			return map;
		}
		String pageSize = requestDecryptEntity.getHeaders().getFirst(YtPageBeanEnum.PAGESIZE.getValue());
		Integer pageNos = Integer.parseInt(pageNo);
		if (pageSize == null || !StringUtil.checkNotEmpty(pageSize)) {
			calculatePageSizes = YtPageBean.getDefaultPageSize();
			start = YtPageBean.getStartOfPage(pageNos, calculatePageSizes) - 1;
		} else {
			calculatePageSizes = Integer.parseInt(pageSize);
			start = YtPageBean.getStartOfPage(pageNos, calculatePageSizes) - 1;
		}
		map.put(YtPageBeanEnum.PAGENO.getValue(), pageNo);
		map.put(YtPageBeanEnum.PAGESIZE.getValue(), calculatePageSizes);
		map.put(YtPageBeanEnum.PAGESTART.getValue(), start);
		map.put(YtPageBeanEnum.PAGEEND.getValue(), calculatePageSizes);
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> requestToParamMap(HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Iterator<?> entries = request.getParameterMap().entrySet().iterator();
		Map.Entry<String, Object> entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry<String, Object>) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

	public static void requestSetAttribute(HttpServletRequest request, String key, Object v) {
		request.getSession().setAttribute(key, v);
	}

	public static void requestGetAttribute(HttpServletRequest request, String key) {
		request.getSession().getAttribute(key);
	}

}
