package com.example.onekids_project.request.schedule;


import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Data
public class CreateTitleClassRequest {

    @NonNull
    private Long idClass;

    @NonNull
    private  String scheduleDate;

    private  String scheduleTitle;

}
