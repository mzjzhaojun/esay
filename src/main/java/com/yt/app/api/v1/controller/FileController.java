package com.yt.app.api.v1.controller;

import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.yt.app.api.v1.entity.YtFile;
import com.yt.app.api.v1.service.FileService;
import com.yt.app.common.base.impl.YtBaseControllerImpl;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtRequestEntity;
import com.yt.app.common.common.yt.YtResponseEntity;
import com.yt.app.common.util.FileUtil;

/**
 * @author zj default test
 * 
 * @version 1.1
 */

@RestController
@RequestMapping("/rest/v1/file")
public class FileController extends YtBaseControllerImpl<YtFile, Long> {

	

	@Autowired
	private FileService service;

	@Autowired
	private Gson gson;

	/**
	 * 普通上传
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "上传", response = YtFile.class)
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> add(MultipartHttpServletRequest request) {
		MultipartFile file = request.getFile("file");

		String url = request.getParameter("url");
		String watermark = request.getParameter("watermark"); // 水印内容
		YtFile furl = null;
		try {
			furl = service.addFile(file, url, watermark);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new YtResponseEntity<Object>(new YtBody(furl));
	}

	/**
	 * 普通上传
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "文本编辑上传", response = YtFile.class)
	@RequestMapping(value = "/upload/imgFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> addImgFile(MultipartHttpServletRequest request) {
		MultipartFile file = null;
		MultipartFile getfile = request.getFile("file");
		MultipartFile imgFile = request.getFile("imgFile");
		if (getfile != null) {
			file = getfile;
		} else if (imgFile != null) {
			file = imgFile;
		}
		String url = request.getParameter("url");
		String watermark = request.getParameter("watermark"); // 水印内容
		YtFile furl = null;
		try {
			furl = service.addFile(file, url, watermark);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new YtResponseEntity<Object>(furl);
	}

	@ApiOperation(value = "上传", response = YtFile.class)
	@RequestMapping(value = "/video/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> updload(MultipartHttpServletRequest request) {
		MultipartFile file = request.getFile("file");
		YtFile furl = null;
		try {
			furl = service.addFileVideo(file, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new YtResponseEntity<Object>(new YtBody(furl));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@ApiOperation(value = "删除")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> delete(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Integer i = service.remove(id);
		return new YtResponseEntity<Object>(new YtBody(i));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@ApiOperation(value = "删除")
	@RequestMapping(value = "/delete/array", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> deleteArray(YtRequestEntity<List<String>> requestEntity, HttpServletRequest request,
			HttpServletResponse response) {
		Integer i = service.removeArray(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(i));
	}

	/**
	 * 下载
	 * 
	 * @param id
	 * @param response
	 */
	@ApiOperation(value = "下载", response = YtFile.class)
	@RequestMapping(value = "/dt/{ids}", method = RequestMethod.GET)
	public void dowloadall(@PathVariable String ids, HttpServletResponse response) {
		if (ids.indexOf(",") != -1) {
			String[] arrayid = ids.split(",");
			List<YtFile> listfiles = new ArrayList<YtFile>();
			for (String string : arrayid) {
				YtFile fl = service.get(Long.valueOf(string));
				listfiles.add(fl);
			}
			FileUtil.dowloadfiles(listfiles, response);
		} else {
			YtFile fl = service.get(Long.valueOf(ids));
			FileUtil.dowloadfile(fl, response);
		}
	}

	/**
	 * 文件上传
	 * 
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public String uploadFile(HttpServletResponse response, MultipartHttpServletRequest request) {
		if (!ServletFileUpload.isMultipartContent(request)) {
			return null;
		}
		List<MultipartFile> files = request.getFiles("imgFile");
		String watermark = request.getParameter("watermark"); // 水印内容
		YtFile furl = null;
		try {
			furl = service.addFile(files.get(0), null, watermark);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gson.toJson(furl);
	}

	/**
	 * 下载视频
	 * 
	 * @param id
	 * @param response
	 */
	@ApiOperation(value = "下载", response = YtFile.class)
	@RequestMapping(value = "/{id}/{size}", method = RequestMethod.GET)
	public void dowload(@PathVariable Long id, @PathVariable Long size, HttpServletRequest request,
			HttpServletResponse response) {
		YtFile fv = service.get(id);
		if (fv == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		try {
			StringBuffer sb = new StringBuffer();
			String filepath = sb.append(fv.getRoot_path()).append(fv.getRelative_path()).append(java.io.File.separator)
					.append(fv.getFile_name()).toString();
			java.io.File file = new java.io.File(filepath);
			if (file.exists()) {
				if (FileUtil.isAudio(file))
					response.setContentType("audio/" + fv.getSuffix());
				else if (FileUtil.isVideo(file)) {
					sendVideo(request, response, file, file.getName());
				} else if (FileUtil.isImage(file))
					response.setContentType("image/" + fv.getSuffix());
				else if (FileUtil.isDoc(file))
					response.setContentType("application/" + fv.getSuffix());
				else {
					response.setContentType("application/octet-stream");
					response.setHeader("Accept-Ranges", "bytes");
					response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fv.getFile_name()));
				}
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			FileInputStream inputStream = new FileInputStream(file);
			OutputStream stream = response.getOutputStream();
			byte[] buffer = new byte[1024];
			int i = -1;
			while ((i = inputStream.read(buffer)) != -1) {
				stream.write(buffer, 0, i);
			}
			stream.flush();
			stream.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendVideo(HttpServletRequest request, HttpServletResponse response, File file, String fileName)
			throws FileNotFoundException, IOException {
		RandomAccessFile randomFile = new RandomAccessFile(file, "r");
		long contentLength = randomFile.length();
		String range = request.getHeader("Range");
		int start = 0, end = 0;
		if (range != null && range.startsWith("bytes=")) {
			String[] values = range.split("=")[1].split("-");
			start = Integer.parseInt(values[0]);
			if (values.length > 1) {
				end = Integer.parseInt(values[1]);
			}
		}
		int requestSize = 0;
		if (end != 0 && end > start) {
			requestSize = end - start + 1;
		} else {
			requestSize = Integer.MAX_VALUE;
		}
		byte[] buffer = new byte[4096];
		response.setContentType("video/mp4");
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("ETag", fileName);
		response.setHeader("Last-Modified", new Date().toString());
		if (range == null) {
			response.setHeader("Content-length", contentLength + "");
		} else {
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);// 206
			long requestStart = 0, requestEnd = 0;
			String[] ranges = range.split("=");
			if (ranges.length > 1) {
				String[] rangeDatas = ranges[1].split("-");
				requestStart = Integer.parseInt(rangeDatas[0]);
				if (rangeDatas.length > 1) {
					requestEnd = Integer.parseInt(rangeDatas[1]);
				}
			}
			long length = 0;
			if (requestEnd > 0) {
				length = requestEnd - requestStart + 1;
				response.setHeader("Content-length", "" + length);
				response.setHeader("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + contentLength);
			} else {
				length = contentLength - requestStart;
				response.setHeader("Content-length", "" + length);
				response.setHeader("Content-Range",
						"bytes " + requestStart + "-" + (contentLength - 1) + "/" + contentLength);
			}
		}
		ServletOutputStream out = response.getOutputStream();
		int needSize = requestSize;
		randomFile.seek(start);
		while (needSize > 0) {
			int len = randomFile.read(buffer);
			if (needSize < buffer.length) {
				out.write(buffer, 0, needSize);
			} else {
				out.write(buffer, 0, len);
				if (len < buffer.length) {
					break;
				}
			}
			needSize -= buffer.length;
		}
		randomFile.close();
		out.close();
	}

	/**
	 * 普通上传
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "上传", response = YtFile.class)
	@RequestMapping(value = "/txt/dowload/{id}", method = RequestMethod.GET)
	public void dowloadtxt(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
	}

	/**
	 * 普通上传
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "上传", response = YtFile.class)
	@RequestMapping(value = "/customer/dowload/{id}", method = RequestMethod.GET)
	public void dowloadcustomer(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
	}

	/**
	 * 普通上传
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "上传", response = YtFile.class)
	@RequestMapping(value = "/txt/dowloadmobile/{id}", method = RequestMethod.GET)
	public void dowloadmobile(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
	}

}
