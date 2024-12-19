package com.example.demo.models.request.clinic;

import com.example.demo.models.Prototype;
import com.example.demo.models.entity.ClinicEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClinicAddRequest implements Prototype<ClinicAddRequest> {
    private String name;
    private String address;
    private String phone;
    private String email;

    private LocalTime openTime;
    private LocalTime closeTime;

    public ClinicAddRequest(ClinicAddRequest clinicAddRequest) {
        this.name = clinicAddRequest.getName();
        this.address = clinicAddRequest.getAddress();
        this.phone = clinicAddRequest.getPhone();
        this.email = clinicAddRequest.getEmail();
        this.openTime = clinicAddRequest.getOpenTime();
        this.closeTime = clinicAddRequest.getCloseTime();
    }

    public static ClinicEntity toEntity(ClinicAddRequest response) {
        if (response == null) {
            return null;
        }

        ClinicEntity clinic = new ClinicEntity();

        clinic.setName(response.getName());
        clinic.setAddress(response.getAddress());
        clinic.setPhone(response.getPhone());
        clinic.setEmail(response.getEmail());
        clinic.setOpenTime(response.getOpenTime());
        clinic.setCloseTime(response.getCloseTime());

        return clinic;
    }

    @Override
    public ClinicAddRequest clone() {
        return new ClinicAddRequest(this);
    }
}
