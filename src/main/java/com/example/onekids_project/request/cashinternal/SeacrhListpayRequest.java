package com.example.onekids_project.request.cashinternal;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SeacrhListpayRequest extends PageNumberWebRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateStartEnd;

    private String status;

    private String code;

    @Override
    public String toString() {
        return "SeacrhListpayRequest{" +
                "dateStartEnd=" + dateStartEnd +
                ", status=" + status +
                ", code='" + code + '\'' +
                "} " + super.toString();
    }
}
