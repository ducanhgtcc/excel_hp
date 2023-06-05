package com.example.onekids_project.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TestDTO {
    @NotBlank
    private int id;
    @NotBlank
    private String name;
    private String address;
    private double salary;
    private double salary1;
    private double salary2;
}
