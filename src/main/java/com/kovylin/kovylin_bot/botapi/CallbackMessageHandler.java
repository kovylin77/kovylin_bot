package com.kovylin.kovylin_bot.botapi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackMessageHandler {

    SendMessage handle(CallbackQuery callbackQuery);

    BotState getHandlerName();
}
