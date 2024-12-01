package com.example.demo.models.response.clinic;

import com.example.demo.models.interfaces.ModelWithMessage;
import com.example.demo.util.strategy.SortStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class ClinicsResponse extends ModelWithMessage {
    @JsonIgnore
    private final SortStrategy<ClinicResponse> strategy;
    private List<ClinicResponse> clinics;

    public ClinicsResponse sortClinics() {
        if (strategy != null) {
            strategy.sort(clinics);
        }
        return this;
    }
}
