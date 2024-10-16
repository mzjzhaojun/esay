package com.yt.app.common.bot.message.Keyboard;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class InlineKeyboard {
	public static InlineKeyboardMarkup getInlineKeyboardMarkup() {
		List<List<InlineKeyboardButton>> listinlinekeyboardbuttons = new ArrayList<>();
		List<InlineKeyboardButton> inlinekeyboardbuttons = new ArrayList<>();

		InlineKeyboardButton inlinekeyboardbutton = new InlineKeyboardButton();
		inlinekeyboardbutton.setText(ButtonResource.FLASH_EXCHANGE_10.getName());
		inlinekeyboardbutton.setCallbackData(ButtonResource.FLASH_EXCHANGE_10.getCallBackData());
		inlinekeyboardbuttons.add(inlinekeyboardbutton);

		InlineKeyboardButton inlinekeyboardbutton2 = new InlineKeyboardButton();
		inlinekeyboardbutton2.setText(ButtonResource.FLASH_EXCHANGE_20.getName());
		inlinekeyboardbutton2.setCallbackData(ButtonResource.FLASH_EXCHANGE_20.getCallBackData());
		inlinekeyboardbuttons.add(inlinekeyboardbutton2);

		List<InlineKeyboardButton> inlinekeyboardbuttons3 = new ArrayList<>();
		InlineKeyboardButton inlinekeyboardbutton3 = new InlineKeyboardButton();
		inlinekeyboardbutton3.setText(ButtonResource.FLASH_EXCHANGE_30.getName());
		inlinekeyboardbutton3.setCallbackData(ButtonResource.FLASH_EXCHANGE_30.getCallBackData());
		inlinekeyboardbuttons3.add(inlinekeyboardbutton3);

		InlineKeyboardButton inlinekeyboardbutton5 = new InlineKeyboardButton();
		inlinekeyboardbutton5.setText(ButtonResource.FLASH_EXCHANGE_50.getName());
		inlinekeyboardbutton5.setCallbackData(ButtonResource.FLASH_EXCHANGE_50.getCallBackData());
		inlinekeyboardbuttons3.add(inlinekeyboardbutton5);

		List<InlineKeyboardButton> inlinekeyboardbuttons5 = new ArrayList<>();
		InlineKeyboardButton inlinekeyboardbutton100 = new InlineKeyboardButton();
		inlinekeyboardbutton100.setText(ButtonResource.FLASH_EXCHANGE_100.getName());
		inlinekeyboardbutton100.setCallbackData(ButtonResource.FLASH_EXCHANGE_100.getCallBackData());
		inlinekeyboardbuttons5.add(inlinekeyboardbutton100);

		InlineKeyboardButton inlinekeyboardbutton200 = new InlineKeyboardButton();
		inlinekeyboardbutton200.setText(ButtonResource.FLASH_EXCHANGE_200.getName());
		inlinekeyboardbutton200.setCallbackData(ButtonResource.FLASH_EXCHANGE_200.getCallBackData());
		inlinekeyboardbuttons5.add(inlinekeyboardbutton200);

		List<InlineKeyboardButton> inlinekeyboardbuttons7 = new ArrayList<>();
		InlineKeyboardButton inlinekeyboardbutton500 = new InlineKeyboardButton();
		inlinekeyboardbutton500.setText(ButtonResource.FLASH_EXCHANGE_500.getName());
		inlinekeyboardbutton500.setCallbackData(ButtonResource.FLASH_EXCHANGE_500.getCallBackData());
		inlinekeyboardbuttons7.add(inlinekeyboardbutton500);

		InlineKeyboardButton inlinekeyboardbutton1000 = new InlineKeyboardButton();
		inlinekeyboardbutton1000.setText(ButtonResource.FLASH_EXCHANGE_1000.getName());
		inlinekeyboardbutton1000.setCallbackData(ButtonResource.FLASH_EXCHANGE_1000.getCallBackData());
		inlinekeyboardbuttons7.add(inlinekeyboardbutton1000);

		listinlinekeyboardbuttons.add(inlinekeyboardbuttons);
		listinlinekeyboardbuttons.add(inlinekeyboardbuttons3);
		listinlinekeyboardbuttons.add(inlinekeyboardbuttons5);
		listinlinekeyboardbuttons.add(inlinekeyboardbuttons7);

		InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
		markupKeyboard.setKeyboard(listinlinekeyboardbuttons);
		return markupKeyboard;
	}

	public static InlineKeyboardMarkup getUpInlineKeyboardMarkup() {
		List<List<InlineKeyboardButton>> listinlinekeyboardbuttons = new ArrayList<>();
		List<InlineKeyboardButton> inlinekeyboardbuttons = new ArrayList<>();

		InlineKeyboardButton inlinekeyboardbutton = new InlineKeyboardButton();
		inlinekeyboardbutton.setText(ButtonResource.FLASH_EXCHANGE_BACK.getName());
		inlinekeyboardbutton.setCallbackData(ButtonResource.FLASH_EXCHANGE_BACK.getCallBackData());
		inlinekeyboardbuttons.add(inlinekeyboardbutton);

		listinlinekeyboardbuttons.add(inlinekeyboardbuttons);

		InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
		markupKeyboard.setKeyboard(listinlinekeyboardbuttons);
		return markupKeyboard;
	}
}
