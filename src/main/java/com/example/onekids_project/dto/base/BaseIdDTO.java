package com.example.onekids_project.dto.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseIdDTO<T> {

    private Long id;

    private Long idCreate;

    private T createdBy;

    private LocalDateTime createdDate;

    private boolean delActive;

    private int pageNumber;

    private int maxPageItem;

    private String sort;
}
