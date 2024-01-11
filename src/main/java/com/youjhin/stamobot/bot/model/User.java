package com.youjhin.stamobot.bot.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Сущность, представляющая данные пользователя в системе дневника головной боли.
 */
@Getter
@Setter
@Entity(name = "usersDataTable")
public class User {

    @Id
    private long chatId;

    private String firstName;

    private String lastName;

    private String userName;

    private Timestamp registeredAt;

    private String lifeLastName;

    private String lifeFirstName;

    private String age;

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", registeredAt=" + registeredAt +
                ", lifeLastName='" + lifeLastName + '\'' +
                ", lifeFirstName='" + lifeFirstName + '\'' +
                ", age=" + age +
                '}';
    }
}
