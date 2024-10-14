package com.yt.app.common.enums;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
public enum YtPageBeanEnum {

	PAGENO("pageNum"), PAGESIZE("pageSize"), TOTALCOUNT("totalcount"), PAGESTART("pageStart"), PAGEEND("pageEnd"), PAGECOUNT("pageCount"), ORDERBY("orderBy"), DIR("dir"), ASC("asc"), DESC("desc"), TOKEN("token");

	private String name;

	YtPageBeanEnum(String name) {
		this.name = name;
	}

	public String getValue() {
		return name;
	}
}
