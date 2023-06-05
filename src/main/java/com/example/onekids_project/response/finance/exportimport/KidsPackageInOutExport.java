package com.example.onekids_project.response.finance.exportimport;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-03-20 13:18
 *
 * @author lavanviet
 */
@Getter
@Setter
public class KidsPackageInOutExport {
    private String fullName;

    private LocalDate birthDay;

    private List<String> codeList;

    private double moneyTotal;

    private double moneyPaidTotal;

    private double moneyRemainInTotal;

    private double moneyRemainOutTotal;

}
