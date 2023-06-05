package com.example.onekids_project.request.parentdiary;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @author lavanviet
 */
@Getter
@Setter
public class ExportMedicineRequest {
    @NotEmpty
    private List<Long> idList;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotNull
    private Long idClass;
}
