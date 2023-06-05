package com.example.onekids_project.request.kids;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Data
public class UpdateKidsAdminRequest {

    @Valid
    UpdateKidMainInforRequest kidMainInfo;

}
