package com.youjhin.stamobot.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity(name = "headDiaryDataTable")
public class HeadDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long diaryId;

    private long chatId;

    private Timestamp dateOfFilling;

    private String howMuchHurtScale;

    private String natureHeadache;

    private String areaHurt;

    private String werePain;

    private String symptoms;

    private String relieveAttack;

    private String preventionHeadache;

    private String takeMedication;

}
