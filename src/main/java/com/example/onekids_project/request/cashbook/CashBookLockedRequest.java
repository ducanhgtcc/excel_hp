package com.example.onekids_project.request.cashbook;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * date 2021-03-09 14:48
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class CashBookLockedRequest extends IdRequest {

    @NotNull
    private Boolean locked;

    @Override
    public String toString() {
        return "CashBookLockedRequest{" +
                "locked=" + locked +
                "} " + super.toString();
    }
}
