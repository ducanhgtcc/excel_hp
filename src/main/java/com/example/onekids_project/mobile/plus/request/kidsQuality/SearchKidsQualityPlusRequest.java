package com.example.onekids_project.mobile.plus.request.kidsQuality;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class SearchKidsQualityPlusRequest extends PageNumberRequest {

    private String kidName;

    private Long idClass;

    @Override
    public String toString() {
        return "SearchKidsQualityPlusRequest{" +
                "kidName='" + kidName + '\'' +
                ", idClass=" + idClass +
                "} " + super.toString();
    }
}

