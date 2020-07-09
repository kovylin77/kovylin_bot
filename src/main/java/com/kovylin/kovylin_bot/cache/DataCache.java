package com.kovylin.kovylin_bot.cache;

import com.kovylin.kovylin_bot.botapi.BotState;
import com.kovylin.kovylin_bot.botapi.handlers.fillingprofile.UserDataProfile;

public interface DataCache {

    void setCurrentUserBotState(int userId, BotState botState);

    BotState getCurrentUserBotState(int userId);

    UserDataProfile getUserDataProfile(int userId);

    void saveUserDataProfile(int userId, UserDataProfile userDataProfile);
}
