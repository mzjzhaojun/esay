package com.yt.app.api.v1.service.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.yt.app.api.v1.entity.YtFile;
import com.yt.app.api.v1.mapper.FileMapper;
import com.yt.app.api.v1.service.FileService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.config.YtConfig;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.util.FileUtil;
import com.yt.app.common.util.RedisUtil;

import cn.hutool.core.lang.Snowflake;
import sun.misc.BASE64Decoder;

/**
 * @author zj default
 * 
 * @version 1.1
 */

@Service
public class FileServiceImpl extends YtBaseServiceImpl<YtFile, Long> implements FileService {
	@Autowired
	private FileMapper mapper;

	@Autowired
	YtConfig appConfig;

	@Autowired
	Snowflake idworker;

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public YtIPage<YtFile> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<YtFile>(Collections.emptyList());
			}
		}
		return new YtPageBean<YtFile>(param, mapper.list(param), count);
	}

	@Override
	@Transactional
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public YtFile addFile(MultipartFile file) throws IOException {
		String filename = file.getOriginalFilename();
		String type = (filename.substring(filename.lastIndexOf(".") + 1));
		if (FileUtil.isImage(filename))
			type = "jpeg";
		Date uploaddate = new Date();
		String filepath = FileUtil.createfilepath(uploaddate, appConfig);
		InputStream is = file.getInputStream();
		YtFile fl = null;
		StringBuffer sb = new StringBuffer();
		String name = "";
		String path = "";
		fl = new YtFile();
		fl.setRoot_path(appConfig.getFilePath());
		fl.setRelative_path(filepath);
		fl.setCreatetime(new Date());
		fl.setFile_size((int) file.getSize());
		fl.setSuffix(type);
		mapper.post(fl);
		name = sb.append(fl.getId()).append(".").append(type).toString();
		sb = new StringBuffer();
		String apath = sb.append(filepath).append(java.io.File.separator).append(name).toString();
		sb = new StringBuffer();
		path = sb.append(appConfig.getFilePath()).append(apath).toString();
		java.io.File uploadfile = new java.io.File(path);
		FileUtil.writeFile(is, uploadfile);
		YtFile f = mapper.get(fl.getId());
		if (!name.equals(""))
			f.setFile_name(name);
		f.setModifytime(new Date());
		mapper.put(f);
		f.setUrl(RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain")+RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "fileuploadurl").replace("{id}", fl.getId() + ""));
		return f;
	}

	@Override
	@Transactional
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public String addInputStream(InputStream is) {
		Date uploaddate = new Date();
		String filepath = FileUtil.createfilepath(uploaddate, appConfig);
		YtFile fl = null;
		StringBuffer sb = new StringBuffer();
		String name = "";
		String path = "";
		fl = new YtFile();
		fl.setRoot_path(appConfig.getFilePath());
		fl.setRelative_path(filepath);
		fl.setCreatetime(new Date());
		fl.setFile_size(1);
		fl.setSuffix("jpeg");
		mapper.post(fl);
		name = sb.append(fl.getId()).append(".").append("jpeg").toString();
		sb = new StringBuffer();
		String apath = sb.append(filepath).append(java.io.File.separator).append(name).toString();
		sb = new StringBuffer();
		path = sb.append(appConfig.getFilePath()).append(apath).toString();
		java.io.File uploadfile = new java.io.File(path);
		FileUtil.writeFile(is, uploadfile);
		YtFile f = mapper.get(fl.getId());
		if (!name.equals(""))
			f.setFile_name(name);
		f.setModifytime(new Date());
		mapper.put(f);
		f.setUrl(RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain")+RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "fileuploadurl").replace("{id}", fl.getId() + ""));
		return f.getUrl();
	}
	
	
	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public String addBase64String(String base64) {
		Date uploaddate = new Date();
		String filepath = FileUtil.createfilepath(uploaddate, appConfig);
		YtFile fl = null;
		StringBuffer sb = new StringBuffer();
		String name = "";
		String path = "";
		fl = new YtFile();
		fl.setRoot_path(appConfig.getFilePath());
		fl.setRelative_path(filepath);
		fl.setCreatetime(new Date());
		fl.setFile_size(1);
		fl.setSuffix("jpeg");
		mapper.post(fl);
		name = sb.append(fl.getId()).append(".").append("jpeg").toString();
		sb = new StringBuffer();
		String apath = sb.append(filepath).append(java.io.File.separator).append(name).toString();
		sb = new StringBuffer();
		path = sb.append(appConfig.getFilePath()).append(apath).toString();
		java.io.File uploadfile = new java.io.File(path);
		BufferedOutputStream bos = null;
        FileOutputStream fos = null;
		BASE64Decoder decoder = new BASE64Decoder();
        try {
			byte[] bfile = decoder.decodeBuffer(base64);
			fos = new FileOutputStream(uploadfile);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		YtFile f = mapper.get(fl.getId());
		if (!name.equals(""))
			f.setFile_name(name);
		f.setModifytime(new Date());
		
		mapper.put(f);
		f.setUrl(RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain")+RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "fileuploadurl").replace("{id}", fl.getId() + ""));
		return f.getUrl();
	}
	
	

	@Override
	@Transactional
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public YtFile addFile(MultipartFile file, String url, String watermark) throws IOException {
		String fileurl = null;
		if (watermark != null) {// 需要添加水印
			fileurl = addWorkMarkToMutipartFile(file, watermark);
			if (fileurl != null) {
				java.io.File f = new java.io.File(fileurl);
				FileItem fileItem;
				try {
					fileItem = new DiskFileItem("mainFile", Files.probeContentType(f.toPath()), false, f.getName(), (int) f.length(), f.getParentFile());
					try (InputStream input = new FileInputStream(f); OutputStream os = fileItem.getOutputStream();) {
						IOUtils.copy(input, os);
						file = new CommonsMultipartFile(fileItem);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		String filename = file.getOriginalFilename();
		String type = (filename.substring(filename.lastIndexOf(".") + 1));
		if (FileUtil.isImage(filename))
			type = "jpeg";
		Date uploaddate = new Date();
		String filepath = FileUtil.createfilepath(uploaddate, appConfig);
		InputStream is = file.getInputStream();
		YtFile fl = new YtFile();
		StringBuffer sb = new StringBuffer();
		String name = "";
		String path = "";
		if (url == null)
			url = "";
		String id = this.findId(url);
		boolean mat = id.matches("\\d+");
		if (mat) {
			fl = mapper.get(Long.valueOf(id));
			if (fl != null) {
				path = sb.append(fl.getRoot_path()).append(fl.getRelative_path()).append(java.io.File.separator).append(fl.getFile_name()).toString();
				java.io.File deletefile = new java.io.File(path);
				deletefile.delete();
				fl.setFile_size((int) file.getSize());
				fl.setSuffix(type);
				mapper.put(fl);
				sb = new StringBuffer();
				name = sb.append(fl.getId()).append(".").append(type).toString();
				sb = new StringBuffer();
				String apath = sb.append(fl.getRelative_path()).append(java.io.File.separator).append(name).toString();
				sb = new StringBuffer();
				path = sb.append(fl.getRoot_path()).append(apath).toString();
			} else {
				fl = new YtFile();
				fl.setId(Long.valueOf(id));
				fl.setRoot_path(appConfig.getFilePath());
				fl.setRelative_path(filepath);
				fl.setCreatetime(new Date());
				fl.setFile_size((int) file.getSize());
				fl.setSuffix(type);
				mapper.post(fl);
				// name = sb.append(fl.getId()).append(".").append(type).toString();
				sb = new StringBuffer();
				String apath = sb.append(filepath).append(java.io.File.separator).append(name).toString();
				sb = new StringBuffer();
				path = sb.append(appConfig.getFilePath()).append(apath).toString();
			}
		} else {
			fl.setRoot_path(appConfig.getFilePath());
			fl.setRelative_path(filepath);
			fl.setCreatetime(new Date());
			fl.setFile_size((int) file.getSize());
			fl.setSuffix(type);
			mapper.post(fl);
			name = sb.append(fl.getId()).append(".").append(type).toString();
			sb = new StringBuffer();
			String apath = sb.append(filepath).append(java.io.File.separator).append(name).toString();
			sb = new StringBuffer();
			path = sb.append(appConfig.getFilePath()).append(apath).toString();
		}
		java.io.File uploadfile = new java.io.File(path);
		FileUtil.writeFile(is, uploadfile);
		YtFile f = mapper.get(fl.getId());

		// 判断是否是视频格式
		if (type.equalsIgnoreCase("mp4") || type.equalsIgnoreCase("avi")) {
			// 取一帧图片
			try {
				YtFile fJPG = new YtFile();
				fJPG.setRoot_path(appConfig.getFilePath());
				fJPG.setRelative_path(filepath);
				fJPG.setCreatetime(new Date());
				fJPG.setSuffix("jpg");
				mapper.post(fJPG);

				YtFile getFile = mapper.get(fJPG.getId());
				getFile.setFile_name(fJPG.getId() + ".jpg");
				mapper.put(getFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		f.setFile_name(filename);
		f.setModifytime(new Date());
		String curl = RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain")+RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "fileuploadurl").replace("{file}", "file");
		f.setUrl(curl.replace("{id}", fl.getId() + ""));
		mapper.put(f);
		return f;
	}

	public String findId(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}

	@Override
	@Transactional
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public Integer remove(long id) {
		Integer i = 0;
		YtFile fl = mapper.get(id);
		if (fl != null) {
			StringBuffer sb = new StringBuffer();
			sb.append(appConfig.getFilePath()).append(fl.getRelative_path()).append(java.io.File.separator).append(fl.getFile_name()).toString();
			java.io.File deletefile = new java.io.File(sb.toString());
			deletefile.delete();
			i = mapper.delete(id);
		}
		return i;
	}

	@Override
	@Transactional
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public Integer removeArray(List<String> list) {
		Integer i = 0;
		for (String id : list) {
			YtFile fl = mapper.get(Long.parseLong(id));
			if (fl != null) {
				StringBuffer sb = new StringBuffer();
				sb.append(appConfig.getFilePath()).append(fl.getRelative_path()).append(java.io.File.separator).append(fl.getFile_name()).toString();
				java.io.File deletefile = new java.io.File(sb.toString());
				deletefile.delete();
				i = mapper.delete(Long.parseLong(id));
			}
		}
		return i;
	}

	@Override
	@Transactional
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public YtFile addImg(String imgStr, String url) throws IOException {
		String type = "jpeg";
		Date uploaddate = new Date();
		String filepath = FileUtil.createfilepath(uploaddate, appConfig);
		YtFile fl = new YtFile();
		StringBuffer sb = new StringBuffer();
		String name = "";
		String path = "";
		if (url == null)
			url = "";
		String id = this.findId(url);
		boolean mat = id.matches("\\d+");
		if (mat) {
			fl = mapper.get(Long.valueOf(id));
			if (fl != null) {
				path = sb.append(fl.getRoot_path()).append(fl.getRelative_path()).append(java.io.File.separator).append(fl.getFile_name()).toString();
				java.io.File deletefile = new java.io.File(path);
				deletefile.delete();
				fl.setFile_size(100);
				fl.setSuffix(type);
				mapper.put(fl);
				sb = new StringBuffer();
				name = sb.append(fl.getId()).append(".").append(type).toString();
				sb = new StringBuffer();
				String apath = sb.append(fl.getRelative_path()).append(java.io.File.separator).append(name).toString();
				sb = new StringBuffer();
				path = sb.append(fl.getRoot_path()).append(apath).toString();
			} else {
				fl = new YtFile();
				fl.setId(Long.valueOf(id));
				fl.setRoot_path(appConfig.getFilePath());
				fl.setRelative_path(filepath);
				fl.setCreatetime(new Date());
				fl.setFile_size(100);
				fl.setSuffix(type);
				mapper.post(fl);
				name = sb.append(fl.getId()).append(".").append(type).toString();
				sb = new StringBuffer();
				String apath = sb.append(filepath).append(java.io.File.separator).append(name).toString();
				sb = new StringBuffer();
				path = sb.append(appConfig.getFilePath()).append(apath).toString();
			}
		} else {
			fl.setRoot_path(appConfig.getFilePath());
			fl.setRelative_path(filepath);
			fl.setCreatetime(new Date());
			fl.setFile_size(100);
			fl.setSuffix(type);
			mapper.post(fl);
			name = sb.append(fl.getId()).append(".").append(type).toString();
			sb = new StringBuffer();
			String apath = sb.append(filepath).append(java.io.File.separator).append(name).toString();
			sb = new StringBuffer();
			path = sb.append(appConfig.getFilePath()).append(apath).toString();
		}
		java.io.File uploadfile = new java.io.File(path);
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(uploadfile);
			out.write(b);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		YtFile f = mapper.get(fl.getId());
		if (!name.equals(""))
			f.setFile_name(name);
		f.setModifytime(new Date());
		mapper.put(f);
		String curl = RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain")+RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "fileuploadurl").replace("{file}", "file");
		f.setUrl(curl.replace("{id}", fl.getId() + ""));
		return f;
	}

	@Override
	@Transactional
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public YtFile addFileVideo(MultipartFile file, Integer size) throws Exception {
		String filename = file.getOriginalFilename();
		String type = (filename.substring(filename.lastIndexOf(".") + 1));
		Date uploaddate = new Date();
		String filepath = FileUtil.createfilepath(uploaddate, appConfig);
		InputStream is = file.getInputStream();
		YtFile fl = null;
		StringBuffer sb = new StringBuffer();
		String name = "";
		String path = "";
		fl = new YtFile();
		fl.setRoot_path(appConfig.getFilePath());
		fl.setRelative_path(filepath);
		fl.setCreatetime(new Date());
		fl.setFile_size((int) file.getSize());
		fl.setSuffix(type);
		if (size != null) {
			fl.setFile_size(Integer.valueOf(size));
		}
		mapper.post(fl);
		name = sb.append(fl.getId()).append("_").append("voice").append(".").append(type).toString();
		sb = new StringBuffer();
		String apath = sb.append(filepath).append(java.io.File.separator).append(name).toString();
		sb = new StringBuffer();
		path = sb.append(appConfig.getFilePath()).append(apath).toString();
		java.io.File uploadfile = new java.io.File(path);
		FileUtil.writeFile(is, uploadfile);
		YtFile f = mapper.get(fl.getId());
		if (!name.equals(""))
			f.setFile_name(name);
		f.setModifytime(new Date());
		mapper.put(f);
		f.setUrl(RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain")+RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "fileuploadurl").replace("{id}", fl.getId() + "/" + size));
		return f;
	}

	/**
	 * 直接给multipartFile加上文字水印再进行保存图片的操作方便省事
	 *
	 * @param multipartFile 文件上传的对象
	 * @param markImg       水印文件的路径 如果是相对路径请使用相对路径new Image的方法,此处用的是url
	 * @return
	 * @throws IOException
	 */
	public String addWorkMarkToMutipartFile(MultipartFile multipartFile, String watermark) throws IOException {
//		InputStream is = null;
		// 获取图片文件名 xxx.png xxx
		String originFileName = multipartFile.getOriginalFilename();
		// 获取原图片后缀 png
		String fileType = checkSuffix(originFileName);
		// 获取图片原始信息
		String dContentType = multipartFile.getContentType();
		String suffix = fileType.substring(1, fileType.length());
		// 是图片且不是gif才加水印
		if (originFileName.contains(".unknown") || (!fileType.equalsIgnoreCase(".gif") && dContentType.contains("image"))) {
			// 获取水印图片
			InputStream inputImg = multipartFile.getInputStream();
			Image img = ImageIO.read(inputImg);
			// 加图片水印
			int imgWidth = img.getWidth(null);
			int imgHeight = img.getHeight(null);

			BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
			// 设置字体
//			Font font = new Font("微软雅黑", Font.BOLD, 40);
			// 调用画文字水印的方法
//			String date = DateTimeUtil.getDateTime(DateTimeUtil.DEFAULT_DATE_HOUR_MINUTE_FORMAT);
//			FileUtil.markWord(bufImg, img, "时间：" + date, watermark, font, Color.WHITE, "left-bottom");
//			ByteArrayOutputStream bs = new ByteArrayOutputStream();
//			ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
			// ImageIO.write(bufImg, suffix, imOut);
			// is = new ByteArrayInputStream(bs.toByteArray());

			String path = "c:/file/";
			String newpath = path + idworker.nextId() + ".jpg";
			// 不写入本地
			java.io.File dest = new java.io.File(path);
			if (!dest.getParentFile().exists()) { // 判断文件父目录是否存在
				dest.getParentFile().mkdir();
			}
			ImageIO.write(bufImg, suffix, new java.io.File(newpath));
			return newpath;
		}
		// 返回加了水印的上传对象
		return null;
	}

	/**
	 * 判断后缀类型
	 * 
	 * @param fileType
	 */
	public static String checkSuffix(String originalFilename) {
		String fileType = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
		if (".unknown".equals(fileType.toLowerCase())) {
			fileType = ".jpeg";
		}
		return fileType;
	}
}