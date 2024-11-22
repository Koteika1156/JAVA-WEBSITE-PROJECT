package com.example.demo.models.response.clinic;

import com.example.demo.models.entity.ClinicEntity;
import com.example.demo.models.interfaces.ModelWithMessage;
import com.example.demo.models.response.doctor.DoctorResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@SuperBuilder
public class ClinicResponse extends ModelWithMessage {
    private String id;

    private String name;
    private String address;
    private String phone;
    private String email;

    private LocalTime openTime;
    private LocalTime closeTime;

    private List<DoctorResponse> doctors;

    public static ClinicResponse toResponse(ClinicEntity clinic) {
        if (clinic == null) {
            return null;
        }

        return ClinicResponse
                .builder()
                .id(clinic.getId())
                .name(clinic.getName())
                .address(clinic.getAddress())
                .phone(clinic.getPhone())
                .email(clinic.getEmail())
                .openTime(clinic.getOpenTime())
                .closeTime(clinic.getCloseTime())
                .doctors(
                        clinic.getDoctors() != null ?
                                clinic.getDoctors().stream()
                                        .map(DoctorResponse::toResponse)
                                        .toList()
                                :
                                List.of()
                )
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClinicResponse that = (ClinicResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(phone, that.phone) && Objects.equals(email, that.email) && Objects.equals(openTime, that.openTime) && Objects.equals(closeTime, that.closeTime) && Objects.equals(doctors, that.doctors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, phone, email, openTime, closeTime, doctors);
    }
}
