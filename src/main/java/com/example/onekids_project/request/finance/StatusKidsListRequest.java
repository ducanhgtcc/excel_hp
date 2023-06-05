package com.example.onekids_project.request.finance;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * date 2021-02-19 10:40
 *
 * @author lavanviet
 */
@Getter
@Setter
public class StatusKidsListRequest {
    private boolean status;

    @NotEmpty
    @Valid
    private List<KidsRequestCustom1> kidsList;

    @Override
    public String toString() {
        return "StatusKidsListRequest{" +
                "status=" + status +
                ", kidsList=" + kidsList +
                '}';
    }
}

