package com.example.onekids_project.request.onecam;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;

/**
 * @author lavanviet
 */
@Data
public class OneCamConfigRequest extends IdRequest {
    private boolean plusStatus;

    private int plusNumber;

    private boolean teacherStatus;

    private int teacherNumber;

    private boolean parentStatus;

    private int parentNumber;
}
