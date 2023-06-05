package com.example.onekids_project.request.common;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ActiveRequest extends IdRequest {
    @NotNull
    private Boolean active;

    @Override
    public String toString() {
        return "StatusRequest{" +
                "status=" + active +
                "} " + super.toString();
    }
}
