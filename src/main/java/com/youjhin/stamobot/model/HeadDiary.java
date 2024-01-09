package com.youjhin.stamobot.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity(name = "headDiaryDataTable")
public class HeadDiary {

    @Id
    private long chatId;

    private Timestamp dateOfFilling;

    private String howMuchHurtScale;

    private String natureHeadache;

    private String areaHurt;

    private String werePain;

    private String symptoms;

    private String relieveAttack;

    private String preventionHeadache;

}
