package com.example.onekids_project.request.kids;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class KidsGroupOutRequest {

    //idGroupOut
    @NotNull
    private Long idGroupOut;

    @NotEmpty
    private List<Long> idList;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOut;

}
