package com.kovylin.kovylin_bot.botapi.handlers;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDataProfile {

    String brand;

    String model;

    String city;
}
