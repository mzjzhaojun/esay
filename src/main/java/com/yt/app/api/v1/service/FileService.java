package com.yt.app.api.v1.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.yt.app.api.v1.entity.YtFile;

import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version 1.1
 */

public interface FileService extends YtIBaseService<YtFile, Long> {

	YtIPage<YtFile> page(Map<String, Object> param);

	/**
	 * 保存文件
	 * 
	 * @param file
	 * @return
	 */
	YtFile addFile(MultipartFile file) throws IOException;

	/**
	 * 保存文件
	 * 
	 * @param file
	 * @return
	 */
	YtFile addFile(MultipartFile file, String url, String watermark) throws IOException;

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	Integer remove(long id);

	/**
	 * 根据数组id删除
	 * 
	 * @param list
	 * @return
	 */
	Integer removeArray(List<String> list);

	/**
	 * 保存文件
	 * 
	 * @param file
	 * @return
	 */
	YtFile addImg(String file, String url) throws IOException;

	/**
	 * 保存视频文件
	 * 
	 * @param file
	 * @return
	 */
	YtFile addFileVideo(MultipartFile file, Integer size) throws Exception;

}