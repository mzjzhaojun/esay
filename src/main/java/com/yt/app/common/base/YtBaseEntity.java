package com.yt.app.common.base;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>
 * BaseEntity
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/8/18 1:30
 */
@Getter
@Setter
public abstract class YtBaseEntity<T extends Model<T>> extends Model<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long create_by;

	Date create_time;

	Long update_by;

	Date update_time;

}
