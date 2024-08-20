package com.yt.app.api.v1.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zj
 * 
 * @version 1.1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}