package com.example.onekids_project.request.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
public abstract class IdRequest {
    @NotNull
    private Long id;

}
