package com.example.onekids_project.request.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-10-18 16:02
 *
 * @author lavanviet
 */
@Getter
@Setter
public class KidsSearchExtendRequest {
    @StringInList(values = {AppConstant.KID_SEARCH_START_DATE, AppConstant.KID_SEARCH_BIRTHDAY})
    private String type;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateList;
}
