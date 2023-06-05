package com.example.onekids_project.response.caskinternal;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CashInternalinResponse extends IdResponse {

    private String code;

    private double money;

    private LocalDateTime timePayment;

    private String content;

    private String note;

    private String category;

    private boolean payment;

    private boolean approved;

    private boolean canceled;

    private LocalDate date;

    private String createdBy;

    private Long idPeopleTypeInternal;

    private Long idPeopleTypeOther;

    private LocalDateTime createdDate;

    private boolean checkApprove;

    private boolean locked;

    private double totalMoney;

    private String nameInternal;

    private String nameOther;

    List<PeopleTypeOtherResponse> peopleTypeInternalList;

    List<PeopleTypeOtherResponse> peopleTypeOtherList;
}
