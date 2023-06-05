package com.example.onekids_project.request.kids;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class KidsChangeClassRequest {
    @NotNull
    private Long idClass;

    @NotNull
    private List<KidsActionRequest> kidList;
}
