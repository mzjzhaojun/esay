package com.yt.app.common.common.yt;

import java.io.Serializable;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
public interface YtIPage<T> extends Serializable {

	int getTotal();

	int getNextPageNo();

	int getPreviousPageNo();

	int getSize();

	int getCurrent();

	int getPages();

	Object getRecords();

}
