package com.yt.app.common.bot.message.Keyboard;

public class ButtonResource {

	public final static String EXCHANGE = "白资兑换";

	public final static String UPSTEP = "撤销重选";

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

	public final static Button FLASH_EXCHANGE_10 = new Button("兑换10U", "10兑换");
	public final static Button FLASH_EXCHANGE_20 = new Button("兑换20U", "20兑换");
	public final static Button FLASH_EXCHANGE_30 = new Button("兑换30U", "30兑换");
	public final static Button FLASH_EXCHANGE_50 = new Button("兑换50U", "50兑换");
	public final static Button FLASH_EXCHANGE_100 = new Button("兑换100U", "100兑换");
	public final static Button FLASH_EXCHANGE_200 = new Button("兑换200U", "200兑换");
	public final static Button FLASH_EXCHANGE_500 = new Button("兑换500U", "500兑换");
	public final static Button FLASH_EXCHANGE_1000 = new Button("兑换1000U", "1000兑换");

	public final static Button FLASH_EXCHANGE_EXCHANGE = new Button(EXCHANGE, EXCHANGE);
	public final static Button FLASH_EXCHANGE_BACK = new Button(UPSTEP, UPSTEP);
}
