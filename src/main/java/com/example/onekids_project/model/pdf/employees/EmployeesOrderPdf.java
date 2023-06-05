package com.example.onekids_project.model.pdf.employees;

import lombok.Data;

/**
 * date 2021-03-20 11:08
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class EmployeesOrderPdf {

    private int year;

    private int month;

    private String employeeName;

    private String phone;

    //khoản thu
    private OrderDetailPdf inOrder;

    //khoản chi
    private OrderDetailPdf outOrder;
}
