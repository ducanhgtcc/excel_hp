package com.example.onekids_project.model.finance;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-05-18 11:56
 *
 * @author lavanviet
 */
@Getter
@Setter
public class OrderNumberModel {
    //số khoản đóng đủ
    private int enoughNumber;

    //số khoản đóng thiếu
    private int partNumber;

    //số khoản chưa đóng
    private int emptyNumber;

    //tổng số khoản
    private int totalNumber;

}
