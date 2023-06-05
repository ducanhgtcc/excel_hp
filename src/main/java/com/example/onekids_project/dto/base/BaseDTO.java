package com.example.onekids_project.dto.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class BaseDTO<T> {

    private Long id;

    private Long idCreated;

    private T createdBy;

    private LocalDateTime createdDate;

    private Long idModified;

    private T lastModifieBy;

    private LocalDateTime lastModifieDate;

//    private boolean delActive;
//
//    private int pageNumber;
//
//    private int maxPageItem;
}
