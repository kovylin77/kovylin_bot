package com.kovylin.kovylin_bot.botapi.handlers.start;

import com.kovylin.kovylin_bot.botapi.BotState;
import com.kovylin.kovylin_bot.botapi.InputMessageHandler;
import com.kovylin.kovylin_bot.botapi.handlers.Handler;
import com.kovylin.kovylin_bot.cache.UserDataCache;
import com.kovylin.kovylin_bot.data.Brands;
import com.kovylin.kovylin_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class StartHandler extends Handler implements InputMessageHandler {

    private UserDataCache userDataCache;

    private ReplyMessageService messagesService;

    public StartHandler(UserDataCache userDataCache,
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
        return BotState.START;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        userDataCache.clearUserDataProfile(userId);
        SendMessage replyToUser = messagesService.getReplyMessage(chatId,"reply.chooseBrand");

        List<String> buttons = new ArrayList<>();
        buttons.add(Brands.BMW);
        buttons.add(Brands.INFINITI);
        buttons.add(Brands.LEXUS);
        replyToUser.setReplyMarkup(getInlineMessageButtons(buttons));
        userDataCache.setCurrentUserBotState(userId, BotState.CHOOSE_BRAND);

        return replyToUser;
    }
}
