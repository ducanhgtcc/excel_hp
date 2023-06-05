package com.example.onekids_project.request.finance.kidspackage;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-02-19 15:40
 *
 * @author lavanviet
 */
@Getter
@Setter
public class UserNumberRequest extends IdRequest {
    private int usedNumber;

    @Override
    public String toString() {
        return "UserNumberRequest{" +
                "usedNumber=" + usedNumber +
                "} " + super.toString();
    }
}
