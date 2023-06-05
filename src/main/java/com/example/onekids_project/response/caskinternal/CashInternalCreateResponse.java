package com.example.onekids_project.response.caskinternal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CashInternalCreateResponse {

    List<PeopleTypeOtherResponse> peopleTypeInternalList;

    List<PeopleTypeOtherResponse> peopleTypeOtherList;

    private boolean paymentNote;

    private boolean receiptNote;
}
