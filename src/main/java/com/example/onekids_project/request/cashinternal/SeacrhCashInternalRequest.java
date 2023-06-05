package com.example.onekids_project.request.cashinternal;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class SeacrhCashInternalRequest extends PageNumberWebRequest {

    private String nameOrPhone;

    private String type;

    @Override
    public String toString() {
        return "SeacrhCashInternalRequest{" +
                "nameOrPhone='" + nameOrPhone + '\'' +
                ", type=" + type +
                "} " + super.toString();
    }
}
