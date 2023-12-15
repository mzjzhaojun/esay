package com.yt.app.common.util;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class VideoUtil {
	public static String getVideoFirstImg(String path, String root_path, Long id) throws Exception {
		Frame frame = null;
		// 构造器支持InputStream，可以直接传MultipartFile.getInputStream()
		FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(path);
		// 开始播放
		fFmpegFrameGrabber.start();
		// 获取视频总帧数
//		int ftp = fFmpegFrameGrabber.getLengthInFrames();
		// 指定第几帧
		fFmpegFrameGrabber.setFrameNumber(5);
		// 获取指定第几帧的图片
		frame = fFmpegFrameGrabber.grabImage();
		// 文件绝对路径+名字
		String fileName = root_path + "/" + id + ".jpg";
		File outPut = new File(fileName);
		ImageIO.write(FrameToBufferedImage(frame), "jpg", outPut);
		fFmpegFrameGrabber.close();
		return fileName;
	}

	public static String getVideoLastImg(String path, String root_path, Long id) throws Exception {
		Frame frame = null;
		FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(path);
		// 开始播放
		fFmpegFrameGrabber.start();
		// 获取视频总帧数
		int ftp = fFmpegFrameGrabber.getLengthInFrames();
		// 指定第几帧
		fFmpegFrameGrabber.setFrameNumber(ftp - 1);
		// 获取指定第几帧的图片
		frame = fFmpegFrameGrabber.grabImage();
		String fileName = root_path + "/" + id + ".jpg";
		File outPut = new File(fileName);
		ImageIO.write(FrameToBufferedImage(frame), "jpg", outPut);
		fFmpegFrameGrabber.close();
		return fileName;
	}

	public static BufferedImage FrameToBufferedImage(Frame frame) {
		Java2DFrameConverter converter = new Java2DFrameConverter();
		BufferedImage bufferedImage = converter.getBufferedImage(frame);
		return bufferedImage;
	}

	public static void main(String[] args) throws Exception {
//		String videoFileName = "D:/11.mp4";
	}

}
