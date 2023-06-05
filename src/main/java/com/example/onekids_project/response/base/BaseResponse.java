package com.example.onekids_project.response.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;


@Getter
@Setter
public class BaseResponse {

    private int pageNumber;

    private int maxPageItem;

    private String sort;
}
