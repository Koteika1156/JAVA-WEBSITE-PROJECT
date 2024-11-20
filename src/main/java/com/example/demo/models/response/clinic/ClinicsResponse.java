package com.example.demo.models.response.clinic;

import com.example.demo.models.interfaces.ModelWithMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class ClinicsResponse extends ModelWithMessage {
    private List<ClinicResponse> clinics;
}
