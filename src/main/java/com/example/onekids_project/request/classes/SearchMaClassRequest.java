package com.example.onekids_project.request.classes;

import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchMaClassRequest extends BaseRequest {
    private String className;

    @Override
    public String toString() {
        return "SearchMaClassRequest{" +
                "className='" + className + '\'' +
                "} " + super.toString();
    }
}
