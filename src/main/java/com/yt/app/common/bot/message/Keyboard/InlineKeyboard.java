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
		inlinekeyboardbutton.setText(ButtonResource.flash_exchange_201.getName());
		inlinekeyboardbutton.setCallbackData(ButtonResource.flash_exchange_201.getCallBackData());
		inlinekeyboardbuttons.add(inlinekeyboardbutton);

		InlineKeyboardButton inlinekeyboardbutton2 = new InlineKeyboardButton();
		inlinekeyboardbutton2.setText(ButtonResource.flash_exchange_202.getName());
		inlinekeyboardbutton2.setCallbackData(ButtonResource.flash_exchange_202.getCallBackData());
		inlinekeyboardbuttons.add(inlinekeyboardbutton2);

		listinlinekeyboardbuttons.add(inlinekeyboardbuttons);

		InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
		markupKeyboard.setKeyboard(listinlinekeyboardbuttons);
		return markupKeyboard;
	}
}
