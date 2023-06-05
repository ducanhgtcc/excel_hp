package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;

/**
 * date 2021-02-25 10:14 SA
 *
 * @author ADMIN
 */
@Data
public class BillSampleResponse extends IdRequest {

    private String code;

    // tổng thu
    private double totalIn;

    // tổng chi
    private double totalOut;

    //đã thu
    private double paidIn;

    //đã chi
    private double paidOut;

    private String numberApproved;

    private String numberLocked;

    // thanh toán đủ
    private int yesPaid;

    // chưa thanh toán
    private int noPaid;

    // thanh toán thiếu
    private int lackPaid;

    private boolean locked;

    private boolean show;

    private boolean approved;
}
