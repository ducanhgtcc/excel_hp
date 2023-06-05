package com.example.onekids_project.mobile.request;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-06-07 14:55
 *
 * @author lavanviet
 */
@Getter
@Setter
@ToString
public class IdAndDateRequest {
    @NotNull
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
