package com.example.onekids_project.request.classes;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassSearchNewRequest extends PageNumberWebRequest {
    private String className;

    @Override
    public String toString() {
        return "ClassSearchNewRequest{" +
                "className='" + className + '\'' +
                "} " + super.toString();
    }
}
