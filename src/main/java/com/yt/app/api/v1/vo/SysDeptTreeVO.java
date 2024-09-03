package com.yt.app.api.v1.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

import com.yt.app.common.base.BaseVO;

/**
 * <p>
 * 系统管理-部门-树-响应参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2023/10/09 18:10
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysDeptTreeVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long tenant_id;

	private Long parent_id;

	private String name;

	private Integer sort;

	private Long leader_user_id;

	private String phone;

	private String email;

	private Boolean status;

	private String province_name;

	private String city_name;

	private String area_name;

	private String address;

	private String remark;

	private Date create_time;

	private Integer version;

	private List<SysDeptTreeVO> children;

	public void handleData() {

	}

}
