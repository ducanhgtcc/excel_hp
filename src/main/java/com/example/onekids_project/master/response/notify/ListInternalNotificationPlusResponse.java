package com.example.onekids_project.master.response.notify;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-08-12 2:24 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ListInternalNotificationPlusResponse extends TotalResponse {

    List<InternalNotificationPlusResponse> dataList;
}
