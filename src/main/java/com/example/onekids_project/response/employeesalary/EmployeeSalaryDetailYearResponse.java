package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Data;

/**
 * date 2021-02-23 3:40 CH
 *
 * @author ADMIN
 */
@Data
public class EmployeeSalaryDetailYearResponse extends IdResponse {

    private int year;

    private int month;

    private String code;

    private boolean locked;

    private boolean view;

    private double moneyTotalIn;

    //tổng đã trả
    private double moneyTotalPaidIn;

    //tổng còn lại trả
    private double moneyTotalRemainIn;

    //tổng chi
    private double moneyTotalOut;

    //tổng đã chi
    private double moneyTotalPaidOut;

    //tổng còn lại chi
    private double moneyTotalRemainOut;

    //tổng số khoản thu
    private int totalNumber;

    //số khoản thu đóng đủ
    private int enoughNumber;

    //số khoản thu thiếu
    private int partNumber;

    //số khoản chưa thu
    private int emptyNumber;

}
