package com.example.onekids_project.request.kidsheightweight;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class CreateKidsHeightWeightRequest  extends IdRequest {

    private Double weight;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate timeWeight;

    private Double height;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate timeHeight;

    private int age;

    @Override
    public String toString() {
        return "CreateKidsHeightWeightRequest{" +
                "weight=" + weight +
                ", timeWeight=" + timeWeight +
                ", height=" + height +
                ", timeHeight=" + timeHeight +
                ", age=" + age +
                "} " + super.toString();
    }
}
