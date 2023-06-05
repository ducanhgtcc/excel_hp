package com.example.onekids_project.request.finance;

import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

/**
 * date 2021-02-19 11:06
 *
 * @author lavanviet
 */
@Getter
@Setter
public class KidsRequestCustom1 {
    @Valid
    private List<IdObjectRequest> fnKidsPackageList;

    @Override
    public String toString() {
        return "KidsRequestCustom1{" +
                "fnKidsPackageList=" + fnKidsPackageList +
                "} " + super.toString();
    }
}
