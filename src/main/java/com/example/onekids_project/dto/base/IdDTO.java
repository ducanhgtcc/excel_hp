package com.example.onekids_project.dto.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@MappedSuperclass
public abstract class IdDTO {
    private Long id;
}
