package com.example.onekids_project.response.finance.kidspackage;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-03-11 08:50
 *
 * @author lavanviet
 */
@Getter
@Setter
public class KidsInfoDataResponse extends IdResponse {
    private String fullName;

    private LocalDate birthDay;

    private ClassCustom1 maClass;

}

@Getter
@Setter
class ClassCustom1 {
    private String className;
}

