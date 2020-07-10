package com.kovylin.kovylin_bot.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotStateContext {

    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    private Map<BotState, CallbackMessageHandler> callbackHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers, List<CallbackMessageHandler> callbackMessageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
        callbackMessageHandlers.forEach(handler -> this.callbackHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    public SendMessage processCallbackQuery(BotState currentState, CallbackQuery callbackQuery) {
        CallbackMessageHandler currentMessageHandler = findCallbackHandler(currentState);
        return currentMessageHandler.handle(callbackQuery);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        return messageHandlers.get(currentState);
    }

    private CallbackMessageHandler findCallbackHandler(BotState currentState) {
        return callbackHandlers.get(currentState);
    }
}
