package com.yt.app.common.common;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Setter
@Getter
public class YtProgressListenerVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private long bytesRead;
	private long contentLength;
	private long items;
}
