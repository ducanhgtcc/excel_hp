package com.example.onekids_project.mobile.plus.request.historyNotifiRequest;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class DeleteNotifiRequest extends IdRequest {
    @NotNull
    private Long idUserReceiver;

    @Override
    public String toString() {
        return "ApproveNotifiRequest{" +
                "idUserReceiver=" + idUserReceiver +
                "} " + super.toString();
    }
}

