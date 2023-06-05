package com.example.onekids_project.request.finance;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-02-18 16:35
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class LockedRequest extends IdRequest {
    private boolean locked;
}
