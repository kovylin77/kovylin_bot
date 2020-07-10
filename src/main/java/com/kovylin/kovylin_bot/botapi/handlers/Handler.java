package com.kovylin.kovylin_bot.botapi.handlers;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Handler {

    protected InlineKeyboardMarkup getInlineMessageButtons(List<String> options) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (String option : options) {
            InlineKeyboardButton button = new InlineKeyboardButton().setText(option);
            button.setCallbackData(option);
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
            buttonsRow.add(button);
            rowList.add(buttonsRow);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
