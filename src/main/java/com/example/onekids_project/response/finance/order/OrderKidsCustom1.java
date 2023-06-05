package com.example.onekids_project.response.finance.order;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-02-23 15:32
 *
 * @author lavanviet
 */
@Getter
@Setter
public class OrderKidsCustom1 extends IdResponse {
    private int year;

    private int month;

    private String code;

    private boolean locked;

    private boolean view;

    private boolean parentRead;

    //tổng số tiền phải thu
    private long moneyTotalIn;

    //tổng số tiền đã thu
    private long moneyPaidIn;

    //tổng số tiền phải chi
    private long moneyTotalOut;

    //tổng số tiền đã chi
    private long moneyPaidOut;

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
