package com.yt.app.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public abstract class YtBaseEntity<T extends Model<T>> extends Model<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "创建人id")
	@TableField(value = "create_by", fill = FieldFill.INSERT)
	Long create_by;

	@ApiModelProperty(value = "创建时间")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	Date create_time;

	@ApiModelProperty(value = "更新人id")
	@TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
	Long update_by;

	@ApiModelProperty(value = "更新时间")
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	Date update_time;

}
