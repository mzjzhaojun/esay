package com.yt.app.api.v1.vo;

import java.util.List;

import com.yt.app.api.v1.dbo.base.BaseVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;




@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysOxxVo extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer code;
	SellData data;

	@Getter
	@Setter
	public class SellData {
		List<Object> sell;

		public SellData() {
		};

		public SellData(List<Object> sell) {
			super();
			this.sell = sell;
		}
	}
}
