package com.yt.app.api.v1.vo;

import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;

import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统管理-部门-树-响应参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/10/09 18:10
 */

@Getter
@Setter
public class SysDeptTreeVO {

	@ApiModelProperty("主键ID")
	private Long id;

	@ApiModelProperty("租户ID")
	private Long tenant_id;

	@ApiModelProperty("父ID")
	private Long parent_id;

	@ApiModelProperty("名称")
	private String name;

	@ApiModelProperty("排序")
	private Integer sort;

	@ApiModelProperty("负责人id")
	private Long leader_user_id;

	@ApiModelProperty("联系电话")
	private String phone;

	@ApiModelProperty("邮箱")
	private String email;

	@ApiModelProperty("状态(0:停用 1:正常)")
	private Boolean status;

	@ApiModelProperty("省名称")
	private String province_name;

	@ApiModelProperty("市名称")
	private String city_name;

	@ApiModelProperty("区名称")
	private String area_name;

	@ApiModelProperty("详细地址")
	private String address;

	@ApiModelProperty("备注")
	private String remark;

	@ApiModelProperty("创建时间")
	private Date create_time;

	@ApiModelProperty("版本")
	private Integer version;

	@ApiModelProperty("子级")
	private List<SysDeptTreeVO> children;

	public void handleData() {

	}

}
