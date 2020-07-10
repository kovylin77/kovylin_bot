package com.kovylin.kovylin_bot.botapi.handlers.choosebrand;

import com.kovylin.kovylin_bot.botapi.BotState;
import com.kovylin.kovylin_bot.botapi.BotStateContext;
import com.kovylin.kovylin_bot.botapi.CallbackMessageHandler;
import com.kovylin.kovylin_bot.botapi.InputMessageHandler;
import com.kovylin.kovylin_bot.botapi.handlers.Handler;
import com.kovylin.kovylin_bot.botapi.handlers.UserDataProfile;
import com.kovylin.kovylin_bot.cache.UserDataCache;
import com.kovylin.kovylin_bot.data.Brands;
import com.kovylin.kovylin_bot.data.Cities;
import com.kovylin.kovylin_bot.data.Models;
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
public class ChooseBrandHandler extends Handler implements CallbackMessageHandler {

    private UserDataCache userDataCache;

    private ReplyMessageService messagesService;

    public ChooseBrandHandler(UserDataCache userDataCache,
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
        return BotState.CHOOSE_BRAND;
    }

    private SendMessage processUsersInput(CallbackQuery callbackQuery) {
        int userId = callbackQuery.getFrom().getId();
        long chatId = callbackQuery.getMessage().getChatId();
        String messageText = callbackQuery.getData();
        SendMessage replyToUser;
        UserDataProfile profile = userDataCache.getUserDataProfile(userId);
        profile.setBrand(messageText);
        userDataCache.saveUserDataProfile(userId, profile);

        if (!messageText.equals(Brands.BMW)) {
            replyToUser = messagesService.getReplyMessage(chatId,"reply.chooseModel");
            List<String> options = new ArrayList<>();
            options.add(Models.ALL);
            if (messageText.equals(Brands.INFINITI)) {
                options.add(Models.FX35);
                options.add(Models.G);
            }
            if (messageText.equals(Brands.LEXUS)) {
                options.add(Models.IS);
                options.add(Models.RX);
                options.add(Models.GS);
            }
            replyToUser.setReplyMarkup(getInlineMessageButtons(options));
            userDataCache.setCurrentUserBotState(userId, BotState.CHOOSE_MODEL);
        } else {
            replyToUser = messagesService.getReplyMessage(chatId,"reply.chooseCity");
            userDataCache.setCurrentUserBotState(userId, BotState.CHOOSE_CITY);
            List<String> options = new ArrayList<>();
            options.add(Cities.KHARKIV);
            options.add(Cities.ALL);
            replyToUser.setReplyMarkup(getInlineMessageButtons(options));
        }
        return replyToUser;
    }
}
