package com.example.onekids_project.request.common;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * date 2021-05-20 09:36
 *
 * @author lavanviet
 */
@Getter
@Setter
public class YearIdRequest extends IdRequest {
    @NotNull
    private Integer year;

    @Override
    public String toString() {
        return "YearIdRequest{" +
                "year=" + year +
                "} " + super.toString();
    }
}
