package com.youjhin.stamobot.bot.services;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSession {
    private boolean waitAnswer;
    private int regStep;
    private String firstName;
    private String lastName;
    private String age;
}
