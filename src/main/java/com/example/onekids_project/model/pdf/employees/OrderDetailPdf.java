package com.example.onekids_project.model.pdf.employees;

import lombok.Data;

import java.util.List;

/**
 * date 2021-03-20 11:09
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class OrderDetailPdf {

    private List<EmployeePackagePdf> dataList;

    //tổng tiền phải thanh toán
    private double moneyTotal;

    //tổng tiền đã trả
    private double moneyPaidTotal;

    //tổng tiền còn thiếu
    private double moneyRemain;
}
