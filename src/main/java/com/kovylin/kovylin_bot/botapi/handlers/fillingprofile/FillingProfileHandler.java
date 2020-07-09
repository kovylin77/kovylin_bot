package com.kovylin.kovylin_bot.botapi.handlers.fillingprofile;

import com.kovylin.kovylin_bot.botapi.BotState;
import com.kovylin.kovylin_bot.botapi.InputMessageHandler;
import com.kovylin.kovylin_bot.cache.UserDataCache;
import com.kovylin.kovylin_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {

    private UserDataCache userDataCache;

    private ReplyMessageService messagesService;

    public FillingProfileHandler(UserDataCache userDataCache, ReplyMessageService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getCurrentUserBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setCurrentUserBotState(message.getFrom().getId(), BotState.ASK_NAME);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserDataProfile profileData = userDataCache.getUserDataProfile(userId);
        BotState botState = userDataCache.getCurrentUserBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_NAME)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setCurrentUserBotState(userId, BotState.ASK_AGE);
        }

        if (botState.equals(BotState.ASK_AGE)) {
            profileData.setName(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askAge");
            userDataCache.setCurrentUserBotState(userId, BotState.ASK_GENDER);
        }

        if (botState.equals(BotState.ASK_GENDER)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askGender");
            profileData.setAge(Integer.parseInt(usersAnswer));
            userDataCache.setCurrentUserBotState(userId, BotState.ASK_NUMBER);
        }

        if (botState.equals(BotState.ASK_NUMBER)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askNumber");
            profileData.setGender(usersAnswer);
            userDataCache.setCurrentUserBotState(userId, BotState.ASK_COLOR);
        }

        if (botState.equals(BotState.ASK_COLOR)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askColor");
            profileData.setNumber(Integer.parseInt(usersAnswer));
            userDataCache.setCurrentUserBotState(userId, BotState.ASK_MOVIE);
        }

        if (botState.equals(BotState.ASK_MOVIE)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askMovie");
            profileData.setColor(usersAnswer);
            userDataCache.setCurrentUserBotState(userId, BotState.ASK_SONG);
        }

        if (botState.equals(BotState.ASK_SONG)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askSong");
            profileData.setMovie(usersAnswer);
            userDataCache.setCurrentUserBotState(userId, BotState.PROFILE_FILLED);
        }

        if (botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setSong(usersAnswer);
            userDataCache.setCurrentUserBotState(userId, BotState.ASK_DESTINY);
            replyToUser = new SendMessage(chatId, String.format("%s %s", "Данные по вашей анкете", profileData));
        }

        userDataCache.saveUserDataProfile(userId, profileData);

        return replyToUser;
    }
}
