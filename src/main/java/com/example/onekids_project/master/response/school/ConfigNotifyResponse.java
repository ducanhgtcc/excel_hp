package com.example.onekids_project.master.response.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * date 2021-04-10 16:41
 *
 * @author lavanviet
 */
@Getter
@Setter
public class ConfigNotifyResponse extends IdResponse {
    private ConfigNotifyPlusCustom configNotifyPlus;

    private ConfigNotifyTeacherCustom configNotifyTeacher;

    private ConfigNotifyParentCustom configNotifyParent;
}

@Getter
@Setter
class ConfigNotifyPlusCustom extends IdResponse {
    //thông báo
    private boolean notify;

    //lời nhắn
    private boolean message;

    //dặn thuốc
    private boolean medicine;

    //xin nghỉ
    private boolean absent;

    //nhận xét
    private boolean evaluate;

    //góp ý
    private boolean feedback;

    //tin tức
    private boolean news ;

    //thu chi nôi bộ
    private boolean cashInternal;

    //Duyệt album
    private boolean albumApproved;
}

@Getter
@Setter
class ConfigNotifyTeacherCustom extends IdResponse {
    //thông báo
    private boolean notify;

    //lời nhắn
    private boolean message;

    //dặn thuốc
    private boolean medicine;

    //xin nghỉ
    private boolean absent;

    //nhận xét
    private boolean evaluate;

    //góp ý
    private boolean feedback;

    //sinh nhật
    private boolean birthday;

    //danh bạ
    private boolean phonebook;

    //tin tức
    private boolean news;

    //thanh toán hóa đơn
    private boolean orderPayment;

    //thông báo hóa đơn
    private boolean orderNotify;

    //hiện thị hóa đơn
    private boolean orderShow;
}

@Getter
@Setter
class ConfigNotifyParentCustom extends IdResponse {
    //thông báo

    private boolean notify;

    //lời nhắn
    private boolean message;

    //dặn thuốc
    private boolean medicine;

    //xin nghỉ
    private boolean absent;

    //album ảnh
    private boolean album;

    //nhận xét
    private boolean evaluate;

    //điểm danh
    private boolean attendance;

    //góp ý
    private boolean feedback;

    //sinh nhật
    private boolean birthday;

    //danh bạ
    private boolean phonebook;

    //nạp rút ví
    private boolean wallet;

    //hiện thị hóa đơn
    private boolean orderShow;

    //thanh toán hóa đơn
    private boolean orderPayment;

    //thông báo hóa đơn
    private boolean orderNotify;

    //tin tức
    private boolean news ;
}
