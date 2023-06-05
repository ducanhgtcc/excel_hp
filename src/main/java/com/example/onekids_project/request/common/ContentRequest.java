package com.example.onekids_project.request.common;

import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ContentRequest extends IdRequest {

    @NotBlank
    private String content;

    @Override
    public String toString() {
        return "UpdateAbsentLetterRequest{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}
