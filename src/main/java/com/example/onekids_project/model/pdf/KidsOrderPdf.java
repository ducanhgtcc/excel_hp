package com.example.onekids_project.model.pdf;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-08 15:09
 *
 * @author lavanviet
 */
@Getter
@Setter
public class KidsOrderPdf {

    private int year;

    private int month;

    private String kidName;

    private String className;

    //sdt tài khoản phụ huynh
    private String phone;

    //khoản thu
    private OrderPdf inOrder;

    //khoản chi
    private OrderPdf outOrder;
}
