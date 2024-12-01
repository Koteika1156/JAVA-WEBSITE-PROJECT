package com.example.demo.models.response.doctor;

import com.example.demo.util.strategy.SortStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class DoctorsResponse {

    @JsonIgnore
    private final SortStrategy<DoctorResponse> strategy;

    private List<DoctorResponse> doctors;

    public DoctorsResponse sortDoctors() {
        if (strategy != null) {
            strategy.sort(doctors);
        }
        return this;
    }
}
