package com.example.onekids_project.request.celebrate;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CelebrateSampleActiveRequest extends IdRequest {
    private boolean smsSend;

    private boolean appSend;

    private boolean active;

}
