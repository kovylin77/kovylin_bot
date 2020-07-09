package com.kovylin.kovylin_bot.botapi.handlers.choosemodel;

import com.kovylin.kovylin_bot.botapi.BotState;
import com.kovylin.kovylin_bot.botapi.InputMessageHandler;
import com.kovylin.kovylin_bot.botapi.handlers.UserDataProfile;
import com.kovylin.kovylin_bot.cache.UserDataCache;
import com.kovylin.kovylin_bot.data.Brands;
import com.kovylin.kovylin_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class ChooseModelHandler implements InputMessageHandler {

    private UserDataCache userDataCache;

    private ReplyMessageService messagesService;

    public ChooseModelHandler(UserDataCache userDataCache,
                              ReplyMessageService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.CHOOSE_MODEL;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserDataProfile profile = userDataCache.getUserDataProfile(userId);
        profile.setModel(inputMsg.getText());
        userDataCache.saveUserDataProfile(userId, profile);
        userDataCache.setCurrentUserBotState(userId, BotState.CHOOSE_CITY);
        return messagesService.getReplyMessage(chatId,"reply.chooseCity");
    }
}