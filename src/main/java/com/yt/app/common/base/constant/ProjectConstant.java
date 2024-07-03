package com.yt.app.common.base.constant;

/**
 * <p>
 * 全局常用变量 - 工程使用
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/7/20 18:16
 */
public interface ProjectConstant extends BaseConstant {

	// ===============================================================================
	// ============================ ↓↓↓↓↓↓ 文件系列 ↓↓↓↓↓↓ ============================
	// ===============================================================================

	/**
	 * linux系统分隔符
	 */
	String SEPARATOR_SPRIT = "/";
	/**
	 * win系统分隔符
	 */
	String SEPARATOR_BACKSLASH = "\\\\";

	/**
	 * 分隔符 - 逗号
	 */
	String SEPARATOR_COMMA = ",";
	/**
	 * 分隔符 - 点
	 */
	String SEPARATOR_SPOT = ".";

	/**
	 * 临时文件相关
	 */
	String DEFAULT_FOLDER_TMP = PROJECT_ROOT_DIRECTORY + "/tmp";
	String DEFAULT_FOLDER_TMP_GENERATE = PROJECT_ROOT_DIRECTORY + "/tmp-generate";

}
