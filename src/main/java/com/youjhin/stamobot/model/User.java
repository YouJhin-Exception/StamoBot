package com.youjhin.stamobot.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


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

    private int age;

//
//    public long getChatId() {
//        return chatId;
//    }
//
//    public void setChatId(long chatId) {
//        this.chatId = chatId;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public Timestamp getRegisteredAt() {
//        return registeredAt;
//    }
//
//    public void setRegisteredAt(Timestamp registeredAt) {
//        this.registeredAt = registeredAt;
//    }


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
