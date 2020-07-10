package com.kovylin.kovylin_bot.botapi.handlers.choosemodel;

import com.kovylin.kovylin_bot.botapi.BotState;
import com.kovylin.kovylin_bot.botapi.CallbackMessageHandler;
import com.kovylin.kovylin_bot.botapi.handlers.Handler;
import com.kovylin.kovylin_bot.botapi.handlers.UserDataProfile;
import com.kovylin.kovylin_bot.cache.UserDataCache;
import com.kovylin.kovylin_bot.data.Cities;
import com.kovylin.kovylin_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ChooseModelHandler extends Handler implements CallbackMessageHandler {

    private UserDataCache userDataCache;

    private ReplyMessageService messagesService;

    public ChooseModelHandler(UserDataCache userDataCache,
                              ReplyMessageService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(CallbackQuery callbackQuery) {
        return processUsersInput(callbackQuery);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.CHOOSE_MODEL;
    }

    private SendMessage processUsersInput(CallbackQuery callbackQuery) {
        int userId = callbackQuery.getFrom().getId();
        long chatId = callbackQuery.getMessage().getChatId();

        UserDataProfile profile = userDataCache.getUserDataProfile(userId);
        System.out.println("DATA IS " + callbackQuery.getData());
        profile.setModel(callbackQuery.getData());
        userDataCache.saveUserDataProfile(userId, profile);
        SendMessage sendMessage;
        sendMessage = messagesService.getReplyMessage(chatId,"reply.chooseCity");
        userDataCache.setCurrentUserBotState(userId, BotState.CHOOSE_CITY);
        List<String> options = new ArrayList<>();
        options.add(Cities.KHARKIV);
        options.add(Cities.ALL);
        sendMessage.setReplyMarkup(getInlineMessageButtons(options));
        return sendMessage;
    }
}
