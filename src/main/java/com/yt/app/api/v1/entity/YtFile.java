package com.yt.app.api.v1.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author zj
 * 
 * @version 1.1
 */
@Getter
@Setter
public class YtFile implements Serializable {

	private static final long serialVersionUID = 1L;

	Long id;
	Long tenant_id;
	String root_path;
	String relative_path;
	Integer file_type;
	String suffix;
	Integer file_size;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date createtime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date modifytime;
	String file_name;
	Integer version;
	String url;

	public YtFile() {
	}

	public YtFile(Long id, String root_path, String relative_path, Integer file_type, String suffix, Integer file_size,
			Date createtime, Date modifytime, String file_name, Integer version) {
		this.id = id;
		this.root_path = root_path;
		this.relative_path = relative_path;
		this.file_type = file_type;
		this.suffix = suffix;
		this.file_size = file_size;
		this.createtime = createtime;
		this.modifytime = modifytime;
		this.file_name = file_name;
		this.version = version;
	}
}