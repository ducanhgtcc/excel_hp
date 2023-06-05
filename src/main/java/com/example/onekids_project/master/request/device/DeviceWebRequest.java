package com.example.onekids_project.master.request.device;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
public class DeviceWebRequest {
    @NotBlank
    private String idDevice;

}
