package com.example.onekids_project.mobile.teacher.request.medicine;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class SearchMedicineTeacherRequest extends PageNumberRequest {

    private Boolean confirmStatus;

    private String keyWord;

    private String dateDetail;

    private String dateSick;

    @Override
    public String toString() {
        return "SearchMedicineTeacherRequest{" +
                "confirmStatus=" + confirmStatus +
                ", keyWord='" + keyWord + '\'' +
                ", dateDetail='" + dateDetail + '\'' +
                ", dateSick='" + dateSick + '\'' +
                "} " + super.toString();
    }
}
