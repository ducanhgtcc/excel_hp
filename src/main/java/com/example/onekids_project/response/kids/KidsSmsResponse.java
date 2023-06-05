package com.example.onekids_project.response.kids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidsSmsResponse extends IdResponse {

    private boolean smsSend;

    private boolean isActivated;

    private boolean smsReceive;

}
