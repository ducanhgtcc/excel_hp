package com.example.onekids_project.request.common;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class StatusRequest extends IdRequest {
    @NotNull
    private Boolean status;

    @Override
    public String toString() {
        return "StatusRequest{" +
                "status=" + status +
                "} " + super.toString();
    }
}
