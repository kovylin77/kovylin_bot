package com.kovylin.kovylin_bot.botapi.handlers.choosebrand;

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
public class ChooseBrandHandler implements InputMessageHandler {

    private UserDataCache userDataCache;

    private ReplyMessageService messagesService;

    public ChooseBrandHandler(UserDataCache userDataCache,
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
        return BotState.CHOOSE_BRAND;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser;
        UserDataProfile profile = new UserDataProfile();
        profile.setBrand(inputMsg.getText());
        userDataCache.saveUserDataProfile(userId, profile);
        if (!inputMsg.getText().equals(Brands.BMW)) {
            replyToUser = messagesService.getReplyMessage(chatId,"reply.chooseModel");
            userDataCache.setCurrentUserBotState(userId, BotState.CHOOSE_MODEL);
        } else {
            replyToUser = messagesService.getReplyMessage(chatId,"reply.chooseCity");
            userDataCache.setCurrentUserBotState(userId, BotState.CHOOSE_CITY);
        }
        return replyToUser;
    }
}
