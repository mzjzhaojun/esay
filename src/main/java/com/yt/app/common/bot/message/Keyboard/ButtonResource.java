package com.yt.app.common.bot.message.Keyboard;

public class ButtonResource {

	public static class Button {
		private String name;
		private String callBackData;

		public Button(String name, String callBackData) {
			this.name = name;
			this.callBackData = callBackData;
		}

		public String getName() {
			return name;
		}

		public String getCallBackData() {
			return callBackData;
		}
	}

	public final static Button flash_exchange_201 = new Button("兑换给自己", "201");
	public final static Button flash_exchange_202 = new Button("兑换给朋友", "202");
	public final static Button flash_exchange_203 = new Button("查询兑换次数", "203");
}
