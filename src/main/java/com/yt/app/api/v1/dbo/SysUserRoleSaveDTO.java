package com.yt.app.api.v1.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 保存用户角色参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/9/10 14:19
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRoleSaveDTO {

	@NotNull(message = "用户id不能为空！")
	private Long userId;

	private List<Long> roleIdList;

}
