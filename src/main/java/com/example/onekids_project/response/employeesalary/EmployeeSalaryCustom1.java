package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-22 14:56
 *
 * @author Phạm Ngọc Thắng
 */
@Getter
@Setter
public class EmployeeSalaryCustom1 extends IdResponse {

    private String description;

    private int number;

    private double price;

    private boolean discount;

    private float userNumber;

    private double discountNumber;

    private double discountPrice;

    private boolean approved;

    private boolean locked;

    private double paid;

    private double money;

    private String name;

    private String category;

    private String unit;

    private boolean attendance;

    //= số sử dụng: lấy ra với mục đích ẩn hiện nút lưu số
    private float showNumber;

    //thuộc tính tự bổ sung
    //số lượng tính toán: khi áp dụng điểm danh
    private float calculateNumber;

    //tiền dự trên số lượng tính toán
    private double moneyTemp;
}
