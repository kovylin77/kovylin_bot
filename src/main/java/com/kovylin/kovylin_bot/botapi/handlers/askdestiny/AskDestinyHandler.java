package com.kovylin.kovylin_bot.botapi.handlers.askdestiny;

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
public class AskDestinyHandler implements InputMessageHandler {

    private UserDataCache userDataCache;

    private ReplyMessageService messagesService;

    public AskDestinyHandler(UserDataCache userDataCache,
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
        return BotState.ASK_DESTINY;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId,"reply.askDestiny");
        userDataCache.setCurrentUserBotState(userId, BotState.FILLING_PROFILE);

        return replyToUser;
    }
}
