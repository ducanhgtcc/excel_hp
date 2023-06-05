package com.example.onekids_project.request.birthdaymanagement;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReiceiversRequest extends IdRequest {

    private boolean isApproved;

    private boolean sendDel;

    @Override
    public String toString() {
        return "UpdateReiceiversRequest{" +
                "isApproved=" + isApproved +
                ", sendDel=" + sendDel +
                "} " + super.toString();
    }
}
