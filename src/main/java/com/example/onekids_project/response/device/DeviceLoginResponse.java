package com.example.onekids_project.response.device;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class DeviceLoginResponse extends IdResponse {
    private String idDevice;

    private String type;

    private LocalDateTime dateLogin;

}
