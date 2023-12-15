package com.yt.app.api.v1.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysOxxVo {
	Integer code;
	SellData data;

	public SysOxxVo() {
	};

	public SysOxxVo(Integer code, SellData data) {
		super();
		this.code = code;
		this.data = data;
	}

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
