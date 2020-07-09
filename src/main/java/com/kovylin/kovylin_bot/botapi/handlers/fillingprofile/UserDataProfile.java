package com.kovylin.kovylin_bot.botapi.handlers.fillingprofile;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDataProfile {

    String name;

    String gender;

    String color;

    String movie;

    String song;

    int age;

    int number;
}
