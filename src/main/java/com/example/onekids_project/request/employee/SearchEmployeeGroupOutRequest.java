package com.example.onekids_project.request.employee;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-07-14 5:11 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@ToString
public class SearchEmployeeGroupOutRequest extends PageNumberWebRequest {

    @NotNull
    private Long idGroupOut;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate yearOut;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateInList;
}
