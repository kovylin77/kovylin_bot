package com.kovylin.kovylin_bot.cache;

import com.kovylin.kovylin_bot.botapi.BotState;
import com.kovylin.kovylin_bot.botapi.handlers.UserDataProfile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {

    private Map<Integer, BotState> botUserStates = new HashMap<>();

    private Map<Integer, UserDataProfile> usersDataProfile = new HashMap<>();


    @Override
    public void setCurrentUserBotState(int userId, BotState botState) {
        botUserStates.put(userId, botState);
    }

    @Override
    public BotState getCurrentUserBotState(int userId) {
        BotState botState = botUserStates.get(userId);
        if (botState == null) {
            botState = BotState.CHOOSE_BRAND;
        }
        return botState;
    }

    @Override
    public UserDataProfile getUserDataProfile(int userId) {
        UserDataProfile profile = usersDataProfile.get(userId);
        if (profile == null) {
            profile = new UserDataProfile();
        }
        return profile;
    }

    @Override
    public void saveUserDataProfile(int userId, UserDataProfile userDataProfile) {
        usersDataProfile.put(userId, userDataProfile);
    }

    @Override
    public void clearUserDataProfile(int userId) {
        usersDataProfile.remove(userId);
    }
}
