package com.example.demo.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String doctorId;
    private String userId;
    private String hospitalId;

    @Temporal(TemporalType.TIMESTAMP)
    private String startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private String endTime;
}
