package com.example.onekids_project.request.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@Data
public class BaseRequest {

    private String pageNumber;

    private String maxPageItem;

    private String sort;
}
