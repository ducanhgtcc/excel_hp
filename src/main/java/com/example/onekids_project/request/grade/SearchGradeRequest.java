package com.example.onekids_project.request.grade;

import com.example.onekids_project.request.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchGradeRequest extends BaseRequest {
    private String gradeName;

    @Override
    public String toString() {
        return "SearchGradeRequest{" +
                "gradeName='" + gradeName + '\'' +
                "} " + super.toString();
    }
}
