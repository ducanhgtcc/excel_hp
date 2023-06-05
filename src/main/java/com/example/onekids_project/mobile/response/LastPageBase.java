package com.example.onekids_project.mobile.response;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class LastPageBase {
    private boolean lastPage;
}
