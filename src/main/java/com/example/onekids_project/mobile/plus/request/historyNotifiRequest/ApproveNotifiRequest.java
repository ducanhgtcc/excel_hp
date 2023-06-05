package com.example.onekids_project.mobile.plus.request.historyNotifiRequest;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ApproveNotifiRequest extends IdRequest {
    @NotNull
    private List<Long> idUserReceiverList;

    @Override
    public String toString() {
        return "ApproveNotifiRequest{" +
                "idUserReceiverList=" + idUserReceiverList +
                "} " + super.toString();
    }
}

