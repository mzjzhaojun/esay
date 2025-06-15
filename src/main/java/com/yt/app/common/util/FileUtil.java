/**
 * 
 */
package com.yt.app.common.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.yt.app.api.v1.entity.YtFile;
import com.yt.app.common.config.YtConfig;

import sun.misc.BASE64Encoder;

/**
 * 文件处理
 * 
 * @author zj
 *
 */
public class FileUtil {

	public static final String DOT = ".";
	public static final String SLASH_ONE = "/";
	public static final String SLASH_TWO = "\\";

	public static void writeFile(File file, byte[] b) {
		ByteArrayInputStream bis = null;
		FileOutputStream out = null;
		try {
			bis = new ByteArrayInputStream(b);
			out = new FileOutputStream(file);
			byte[] by = new byte[1024];
			int len;
			while ((len = bis.read(b)) != -1) {
				out.write(by, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public static File getFile(String path, String filename) {
		path = checkDirectory(path);
		if (StringUtils.isNotEmpty(path)) {
			return new File(path + filename);
		}
		return null;
	}

	public static String checkDirectory(String path) {
		if (StringUtils.isBlank(path)) {
			return "";
		} else {
			path = path.replaceAll("\\s", "");
			if (!(path.endsWith("/") || path.endsWith("\\"))) {
				path += File.separator;
			}
			return path;
		}
	}

	public static void writeFile(String file, byte[] b) {
		writeFile(new File(file), b);
	}

	public static byte[] readFile(File file) {
		ByteArrayOutputStream ois = null;
		FileInputStream fis = null;
		try {
			ois = new ByteArrayOutputStream();
			fis = new FileInputStream(file);
			byte[] b = new byte[1024];
			int len;
			while ((len = fis.read(b)) != -1) {
				ois.write(b, 0, len);
			}
			return ois.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.flush();
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {

					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return null;

	}

	public static void writeFile(InputStream is, File file) {
		byte[] b = new byte[1024];
		FileOutputStream fos = null;
		file.length();
		try {
			fos = new FileOutputStream(file);
			int len;
			while ((len = is.read(b)) != -1) {
				fos.write(b, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public static boolean isImage(File file) {
		if (file.isDirectory()) {
			return false;
		}
		String fileName = file.getName();
		int len = fileName.indexOf(".");
		String imagesPattern = fileName.substring(len + 1, fileName.length()).toLowerCase();
		if ("jpg".equals(imagesPattern)) {
			return true;
		} else if ("bmp".equals(imagesPattern)) {
			return true;
		} else if ("gif".equals(imagesPattern)) {
			return true;
		} else if ("psd".equals(imagesPattern)) {
			return true;
		} else if ("pcx".equals(imagesPattern)) {
			return true;
		} else if ("png".equals(imagesPattern)) {
			return true;
		} else if ("dxf".equals(imagesPattern)) {
			return true;
		} else if ("cdr".equals(imagesPattern)) {
			return true;
		} else if ("ico".equals(imagesPattern)) {
			return true;
		} else if ("bmp".equals(imagesPattern)) {
			return true;
		} else if ("jpeg".equals(imagesPattern)) {
			return true;
		} else if ("svg".equals(imagesPattern)) {
			return true;
		} else if ("wmf".equals(imagesPattern)) {
			return true;
		} else if ("emf".equals(imagesPattern)) {
			return true;
		} else if ("eps".equals(imagesPattern)) {
			return true;
		} else if ("tga".equals(imagesPattern)) {
			return true;
		} else if ("nef".equals(imagesPattern)) {
			return true;
		} else if ("tif".equals(imagesPattern)) {
			return true;
		} else if ("tiff".equals(imagesPattern)) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean isImage(String fileName) {
		int len = fileName.indexOf(".");
		String imagesPattern = fileName.substring(len + 1, fileName.length()).toLowerCase();
		if ("jpg".equals(imagesPattern))
			return true;
		// if ("bmp".equals(imagesPattern))
		// return true;
		// if ("gif".equals(imagesPattern))
		// return true;
		// if ("psd".equals(imagesPattern))
		// return true;
		// if ("pcx".equals(imagesPattern))
		// return true;
		// if ("png".equals(imagesPattern))
		// return true;
		// if ("dxf".equals(imagesPattern))
		// return true;
		// if ("cdr".equals(imagesPattern))
		// return true;
		// if ("ico".equals(imagesPattern))
		// return true;
		// if ("bmp".equals(imagesPattern))
		// return true;
		// if ("jpeg".equals(imagesPattern))
		// return true;
		// if ("svg".equals(imagesPattern))
		// return true;
		// if ("wmf".equals(imagesPattern))
		// return true;
		// if ("emf".equals(imagesPattern))
		// return true;
		// if ("eps".equals(imagesPattern))
		// return true;
		// if ("tga".equals(imagesPattern))
		// return true;
		// if ("nef".equals(imagesPattern))
		// return true;
		// if ("tif".equals(imagesPattern))
		// return true;
		// if ("tiff".equals(imagesPattern)) {
		// return true;
		// }
		return false;
	}

	public static boolean isDoc(java.io.File file) {
		if (file.isDirectory()) {
			return false;
		}
		String fileName = file.getName();
		int len = fileName.indexOf(".");
		String imagesPattern = fileName.substring(len + 1, fileName.length()).toLowerCase();
		if ("pdf".equals(imagesPattern))
			return true;
		if ("xlsx".equals(imagesPattern))
			return true;
		if ("docx".equals(imagesPattern)) {
			return true;
		}
		return false;
	}

	public static boolean isAudio(File file) {
		if (file.isDirectory()) {
			return false;
		}
		String fileName = file.getName();
		int len = fileName.indexOf(".");
		String audioPattern = fileName.substring(len + 1, fileName.length()).toLowerCase();
		if ("au".equals(audioPattern)) {
			return true;
		} else if ("aiff".equals(audioPattern)) {
			return true;
		} else if ("wav".equals(audioPattern)) {
			return true;
		} else if ("midi".equals(audioPattern)) {
			return true;
		} else if ("rfm".equals(audioPattern)) {
			return true;
		} else if ("amr".equals(audioPattern)) {
			return true;
		} else if ("aac".equals(audioPattern)) {
			return true;
		} else if ("mp3".equals(audioPattern)) {
			return true;
		} else if ("ogg".equals(audioPattern)) {
			return true;
		} else if ("pcm".equals(audioPattern)) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean isVideo(File file) {
		if (file.isDirectory()) {
			return false;
		}
		String fileName = file.getName();
		int len = fileName.indexOf(".");
		String audioPattern = fileName.substring(len + 1, fileName.length()).toLowerCase();
		if ("mp4".equals(audioPattern)) {
			return true;
		} else if ("avi".equals(audioPattern)) {
			return true;
		} else if ("rmvb".equals(audioPattern)) {
			return true;
		} else if ("3gp".equals(audioPattern)) {
			return true;
		} else if ("mkv".equals(audioPattern)) {
			return true;
		} else if ("flv".equals(audioPattern)) {
			return true;
		} else if ("wmv".equals(audioPattern)) {
			return true;
		} else if ("dvd".equals(audioPattern)) {
			return true;
		} else if ("vcd".equals(audioPattern)) {
			return true;
		} else if ("mpg".equals(audioPattern)) {
			return true;
		} else {
			return false;
		}

	}

	public static String getWithoutExtension(String fileName) {
		String ext = StringUtils.substring(fileName, 0, StringUtils.lastIndexOf(fileName, DOT));
		return StringUtils.trimToEmpty(ext);
	}

	public static String getExtension(String fileName) {
		if (StringUtils.INDEX_NOT_FOUND == StringUtils.indexOf(fileName, DOT))
			return StringUtils.EMPTY;
		String ext = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, DOT));
		return StringUtils.trimToEmpty(ext);
	}

	public static boolean isExtension(String fileName, String ext) {
		return StringUtils.equalsIgnoreCase(getExtension(fileName), ext);
	}

	public static boolean hasExtension(String fileName) {
		return !isExtension(fileName, StringUtils.EMPTY);
	}

	public static String trimExtension(String ext) {
		return getExtension(DOT + ext);
	}

	public static String fillExtension(String fileName, String ext) {
		fileName = replacePath(fileName + DOT);
		ext = trimExtension(ext);
		if (!hasExtension(fileName)) {
			return fileName + getExtension(ext);
		}
		if (!isExtension(fileName, ext)) {
			return getWithoutExtension(fileName) + getExtension(ext);
		}
		return fileName;
	}

	public static boolean isFile(String fileName) {
		return hasExtension(fileName);
	}

	public static boolean isFolder(String fileName) {
		return !hasExtension(fileName);
	}

	public static String replacePath(String path) {
		return StringUtils.replace(StringUtils.trimToEmpty(path), SLASH_ONE, SLASH_TWO);
	}

	public static String trimLeftPath(String path) {
		if (isFile(path))
			return path;
		path = replacePath(path);
		String top = StringUtils.left(path, 1);
		if (StringUtils.equalsIgnoreCase(SLASH_TWO, top))
			return StringUtils.substring(path, 1);
		return path;
	}

	public static String trimRightPath(String path) {
		if (isFile(path))
			return path;
		path = replacePath(path);
		String bottom = StringUtils.right(path, 1);
		if (StringUtils.equalsIgnoreCase(SLASH_TWO, bottom))
			return StringUtils.substring(path, 0, path.length() - 2);
		return path + SLASH_TWO;
	}

	public static String trimPath(String path) {
		path = StringUtils.replace(StringUtils.trimToEmpty(path), SLASH_ONE, SLASH_TWO);
		path = trimLeftPath(path);
		path = trimRightPath(path);
		return path;
	}

	public static String bulidFullPath(String... paths) {
		StringBuffer sb = new StringBuffer();
		for (String path : paths) {
			sb.append(trimPath(path));
		}
		return sb.toString();
	}

	public static void dowloadfile(YtFile fl, HttpServletResponse response) {
		if (fl == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		StringBuffer sb = new StringBuffer();

		fl.setFile_name(fl.getId() + "." + fl.getSuffix());

		String filepath = sb.append(fl.getRoot_path()).append(fl.getRelative_path()).append(java.io.File.separator).append(fl.getFile_name()).toString();
		java.io.File file = new java.io.File(filepath);
		if (file.exists()) {
			if (FileUtil.isAudio(file))
				response.setContentType("audio/" + fl.getSuffix());
			else if (FileUtil.isVideo(file))
				response.setContentType("video/" + fl.getSuffix());
			else if (FileUtil.isImage(file))
				response.setContentType("image/" + fl.getSuffix());
			else if (FileUtil.isDoc(file))
				response.setContentType("application/" + fl.getSuffix());
			else {
				response.setContentType("application/octet-stream");
				response.setHeader("Accept-Ranges", "bytes");
				response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fl.getFile_name()));
			}
		}
		try {
			FileInputStream inputStream = new FileInputStream(file);
			OutputStream stream = response.getOutputStream();
			byte[] buffer = new byte[1024 * 1024 * 4];
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

	public static void dowloadfile(String filepath, Long userid, HttpServletResponse response) {
		java.io.File file = new java.io.File(filepath);
		if (file.exists()) {
			response.setContentType("text/csv;charset=UTF-8");
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Content-Disposition", "attachment;fileName=" + DateTimeUtil.getDate() + "-" + userid + ".csv");
		}
		try {
			FileInputStream inputStream = new FileInputStream(file);
			OutputStream stream = response.getOutputStream();
			byte[] buffer = new byte[1024 * 1024 * 4];
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

	public static void dowloadfiles(List<YtFile> listfiles, HttpServletResponse response) {
		if (listfiles.size() == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		try {
			String rootpath = "";
			List<File> files = new ArrayList<File>();
			for (YtFile fl : listfiles) {
				StringBuffer sb = new StringBuffer();
				String filepath = sb.append(fl.getRoot_path()).append(fl.getRelative_path()).append(java.io.File.separator).append(fl.getId() + "." + fl.getSuffix()).toString();
				File file = new File(filepath);
				rootpath = fl.getRoot_path();
				files.add(file);
			}
			String outFilePath = rootpath + "\\all.zip";
			File file = new File(outFilePath);
			FileOutputStream outStream = new FileOutputStream(file);
			ZipOutputStream toClient = new ZipOutputStream(outStream);
			zipFile(files, toClient);
			toClient.close();
			outStream.close();
			downloadZip(file, response);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 压缩文件列表中的文件
	 * 
	 * @param files
	 * @param outputStream
	 * @throws IOException
	 */
	public static void zipFile(List<File> files, ZipOutputStream outputStream) {
		try {
			int size = files.size();
			// 压缩列表中的文件
			for (int i = 0; i < size; i++) {
				File file = (File) files.get(i);
				zipFile(file, outputStream);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 将文件写入到zip文件中
	 * 
	 * @param inputFile
	 * @param outputstream
	 * @throws Exception
	 */
	public static void zipFile(File inputFile, ZipOutputStream outputstream) {
		try {
			if (inputFile.exists()) {
				if (inputFile.isFile()) {
					FileInputStream inStream = new FileInputStream(inputFile);
					BufferedInputStream bInStream = new BufferedInputStream(inStream);
					ZipEntry entry = new ZipEntry(inputFile.getName());
					outputstream.putNextEntry(entry);
					final int MAX_BYTE = 10 * 1024 * 1024; // 最大的流为10M
					long streamTotal = 0;
					int streamNum = 0;
					int leaveByte = 0;
					byte[] inOutbyte;
					streamTotal = bInStream.available();
					streamNum = (int) Math.floor(streamTotal / MAX_BYTE);
					leaveByte = (int) streamTotal % MAX_BYTE;
					if (streamNum > 0) {
						for (int j = 0; j < streamNum; ++j) {
							inOutbyte = new byte[MAX_BYTE];
							bInStream.read(inOutbyte, 0, MAX_BYTE);
							outputstream.write(inOutbyte, 0, MAX_BYTE);
						}
					}
					inOutbyte = new byte[leaveByte];
					bInStream.read(inOutbyte, 0, leaveByte);
					outputstream.write(inOutbyte);
					outputstream.closeEntry();
					bInStream.close();
					inStream.close();
				}
			} else {
				throw new ServletException("not find！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载打包的文件
	 * 
	 * @param file
	 * @param response
	 */
	public static void downloadZip(File file, HttpServletResponse response) {
		try {
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			response.reset();
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			file.delete();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static String createfilepath(Date date, YtConfig appConfig) {
		StringBuffer sb = new StringBuffer();
		String yearfolder = DateTimeUtil.getDateTime(date, DateTimeUtil.DEFAULT_DATE_FORMAT);
		File filePath = new File(appConfig.getFilePath());
		if (!filePath.exists()) {
			try {
				filePath.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String ypath = sb.append(java.io.File.separator).append(yearfolder).toString();
		java.io.File yfile = new java.io.File(filePath + ypath);
		if (!yfile.exists()) {
			try {
				yfile.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String hfolder = DateTimeUtil.getDateTime(date, DateTimeUtil.DEFAULT_HOUR_FORMAT);
		String hpath = sb.append(java.io.File.separator).append(hfolder).toString();
		java.io.File hfile = new java.io.File(filePath + hpath);
		if (!hfile.exists()) {
			try {
				hfile.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hpath;
	}

	public static String bufferedImageToBase64(BufferedImage bufferedImage) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "PNG", os);
		byte[] bytes = os.toByteArray();// 转换成字节
		BASE64Encoder encoder = new BASE64Encoder();
		String base64 = encoder.encodeBuffer(bytes).trim().replaceAll("\n", "").replaceAll("\r", "");// 转换成base64串//删除 \r\n
		return base64;
	}
}
