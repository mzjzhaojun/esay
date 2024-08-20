package com.yt.app.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p>
 * 基类查询参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/9/13 0013 1:57
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long currentUserId;

	@JsonIgnore
	private String currentUsername;

}
