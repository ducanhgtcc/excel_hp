package com.example.onekids_project.request.grade;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateGradeRequest extends IdRequest {
    @NotBlank
    private String gradeName;

    private String gradeDescription;

    @Override
    public String toString() {
        return "UpdateGradeRequest{" +
                "gradeName='" + gradeName + '\'' +
                ", gradeDescription='" + gradeDescription + '\'' +
                "} " + super.toString();
    }
}
