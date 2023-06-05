package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-03 3:01 CH
 *
 * @author ADMIN
 */

@Getter
@Setter
public class SalaryForPaymentResponse extends IdRequest {

    private boolean locked;

    private double paid;

    //thành tiền
    //(số lượng sử dụng*đơn giá)/số lượng mỗi đơn giá
    private double money;

    //dùng để check tính toán ra số tiền trên giao diện
    private boolean checkMoney;

    //dùng để tích chọn thanh toán trên giao diện
    private boolean checked;

    private String name;

    private String category;

}

