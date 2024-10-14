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
	public final static Button flash_exchange_10 = new Button("10U兑换", "10");
	public final static Button flash_exchange_20 = new Button("20U兑换", "20");
	public final static Button flash_exchange_30 = new Button("30U兑换", "30");
	public final static Button flash_exchange_50 = new Button("50U兑换", "50");
	public final static Button flash_exchange_100 = new Button("100U兑换", "100");
	public final static Button flash_exchange_200 = new Button("200U兑换", "200");
	public final static Button flash_exchange_500 = new Button("500U兑换", "500");
	public final static Button flash_exchange_1000 = new Button("100U兑换", "1000");
}
