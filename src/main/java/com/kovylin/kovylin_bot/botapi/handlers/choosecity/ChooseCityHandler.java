package com.kovylin.kovylin_bot.botapi.handlers.choosecity;

import com.kovylin.kovylin_bot.botapi.BotState;
import com.kovylin.kovylin_bot.botapi.InputMessageHandler;
import com.kovylin.kovylin_bot.botapi.handlers.UserDataProfile;
import com.kovylin.kovylin_bot.cache.UserDataCache;
import com.kovylin.kovylin_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class ChooseCityHandler implements InputMessageHandler {

    private UserDataCache userDataCache;

    public ChooseCityHandler(UserDataCache userDataCache,
                             ReplyMessageService messagesService) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.CHOOSE_CITY;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserDataProfile profile = userDataCache.getUserDataProfile(userId);
        profile.setCity(inputMsg.getText());
        userDataCache.saveUserDataProfile(userId, profile);
        userDataCache.setCurrentUserBotState(userId, BotState.START);

        return new SendMessage(chatId,
                "Вы выбрали " + profile.getBrand() + " " + profile.getModel() + " в области(ях) " + profile.getCity() +
                "\nДля перезапуска бота введите /start");
    }
}
