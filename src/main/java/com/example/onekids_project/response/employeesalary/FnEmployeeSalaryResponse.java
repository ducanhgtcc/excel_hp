package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FnEmployeeSalaryResponse extends IdResponse {
    //tổng số tiền của hóa đơn
    private double moneyTotal;

    //tổng số tiền đã trả
    private double moneyTotalPaid;

    //thời gian chỉnh sửa gần nhất của orderKids
    private LocalDateTime dateTime;

    private String description;

    private List<SalaryForPaymentResponse> dataList;

}
