package com.example.onekids_project.response.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public class DataResponse {

    private static HttpStatus statusValue = null;

    public static ResponseEntity<Object> getData(Object body, HttpStatus httpStatus) {
        statusValue = httpStatus;
        return new ResponseEntity(new Data(body), httpStatus);
    }

    @Getter
    @Setter
    private static class Data {
        private String companyName = "One Group";
        private String appName = "OneKids";
        private LocalDate nowDate = LocalDate.now();
        private int status = statusValue.value();
        private String version = "1.0";
        private Object data;

        private Data(Object data) {
            this.data = data;
        }
    }
}
