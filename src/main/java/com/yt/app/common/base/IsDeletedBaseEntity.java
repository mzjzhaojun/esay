package com.yt.app.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@AllArgsConstructor
@NoArgsConstructor
public abstract class IsDeletedBaseEntity<T extends Model<T>> extends Model<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableField(value = "is_deleted", fill = FieldFill.INSERT)
	private Boolean isDeleted;

}
