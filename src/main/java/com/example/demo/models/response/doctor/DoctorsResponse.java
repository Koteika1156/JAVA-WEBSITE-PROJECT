package com.example.demo.models.response.doctor;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class DoctorsResponse {
    private List<DoctorResponse> doctors;
}
