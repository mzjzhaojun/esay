package com.yt.app.common.common;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Component
public class YtProgressListener implements ProgressListener {

	private HttpSession aa;

	public void setSession(HttpSession paramHttpSession) {
		this.aa = paramHttpSession;
		YtProgressListenerVO localProgress = new YtProgressListenerVO();
		paramHttpSession.setAttribute("status", localProgress);
	}

	@Override
	public void update(long paramLong1, long paramLong2, int paramInt) {
		YtProgressListenerVO localProgress = (YtProgressListenerVO) this.aa.getAttribute("status");
		localProgress.setBytesRead(paramLong1);
		localProgress.setContentLength(paramLong2);
		localProgress.setItems(paramInt);
	}
}