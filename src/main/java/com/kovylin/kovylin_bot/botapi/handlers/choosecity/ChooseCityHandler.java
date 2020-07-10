package com.kovylin.kovylin_bot.botapi.handlers.choosecity;

import com.kovylin.kovylin_bot.botapi.BotState;
import com.kovylin.kovylin_bot.botapi.CallbackMessageHandler;
import com.kovylin.kovylin_bot.botapi.handlers.Handler;
import com.kovylin.kovylin_bot.botapi.handlers.UserDataProfile;
import com.kovylin.kovylin_bot.cache.UserDataCache;
import com.kovylin.kovylin_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class ChooseCityHandler extends Handler implements CallbackMessageHandler {

    private UserDataCache userDataCache;

    private ReplyMessageService messagesService;

    public ChooseCityHandler(UserDataCache userDataCache,
                             ReplyMessageService messagesService) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(CallbackQuery callbackQuery) {
        return processUsersInput(callbackQuery);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.CHOOSE_CITY;
    }

    private SendMessage processUsersInput(CallbackQuery callbackQuery) {
        int userId = callbackQuery.getFrom().getId();
        long chatId = callbackQuery.getMessage().getChatId();

        UserDataProfile profile = userDataCache.getUserDataProfile(userId);
        profile.setCity(callbackQuery.getData());
        userDataCache.saveUserDataProfile(userId, profile);
        userDataCache.setCurrentUserBotState(userId, BotState.START);

        return new SendMessage(chatId, formatMessage(profile));
    }

    private String formatMessage(UserDataProfile profile) {
        String message = "Вы выбрали:";
        if (profile.getBrand() != null && !profile.getBrand().isEmpty()) {
            message = message.concat("\nМарка: " + profile.getBrand());
        }
        if (profile.getModel() != null && !profile.getModel().isEmpty()) {
            message = message.concat("\nМодель: " + profile.getModel());
        }
        if (profile.getCity() != null && !profile.getCity().isEmpty()) {
            message = message.concat("\nОбласть: " + profile.getCity());
        }
        return message.concat("\n\nДля перезапуска бота введите /start");
    }
}
