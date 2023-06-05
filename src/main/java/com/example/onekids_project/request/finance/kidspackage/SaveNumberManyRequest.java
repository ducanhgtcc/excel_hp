package com.example.onekids_project.request.finance.kidspackage;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-15 11:11
 *
 * @author lavanviet
 */
@Getter
@Setter
public class SaveNumberManyRequest extends IdRequest {
    private List<UserNumberRequest> fnKidsPackageList;

    @Override
    public String toString() {
        return "TransferNumberManyRequest{" +
                "fnKidsPackageList=" + fnKidsPackageList +
                "} " + super.toString();
    }
}
