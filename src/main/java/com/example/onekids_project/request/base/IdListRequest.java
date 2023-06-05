package com.example.onekids_project.request.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * date 2021-02-18 3:45 CH
 *
 * @author ADMIN
 */
@Data
public class IdListRequest {

    @NotEmpty
    private List<Long> idList;
}
