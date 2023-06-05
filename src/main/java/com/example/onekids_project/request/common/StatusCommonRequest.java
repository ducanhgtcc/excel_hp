package com.example.onekids_project.request.common;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-03-24 11:28
 *
 * @author lavanviet
 */
@Getter
@Setter
public class StatusCommonRequest extends IdRequest {
    @NotNull
    private LocalDate date;
}
