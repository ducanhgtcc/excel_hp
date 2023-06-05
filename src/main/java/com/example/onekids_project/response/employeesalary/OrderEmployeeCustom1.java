package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-22 15:44
 *
 * @author Phạm Ngọc Thắng
 */
@Getter
@Setter
public class OrderEmployeeCustom1 extends IdResponse {
    private int year;

    private int month;

    private String code;

    private boolean view;

    private boolean locked;

    //tổng thu
    private double moneyTotalIn;

    //tổng đã thu
    private double moneyTotalPaidIn;

    //tổng còn lại thu(tổng thu-tổng đã thu)
    private double moneyTotalRemainIn;

    //tổng chi
    private double moneyTotalOut;

    //tổng đã chi
    private double moneyTotalPaidOut;

    //tổng còn lại chi(tổng chi-tổng đã chi)
    private double moneyTotalRemainOut;

    //còn lại phải thu: bằng hiệu còn lại thu-còn lại chi>0
    private double totalMoneyRemainIn;

    //còn lại phải chi: bằng hiệu còn lại thu-còn lại chi<0
    private double totalMoneyRemainOut;

    //tổng số khoản thu
    private int totalNumber;

    //số khoản thu đóng đủ
    private int enoughNumber;

    //số khoản thu thiếu
    private int partNumber;

    //số khoản chưa thu
    private int emptyNumber;

}

