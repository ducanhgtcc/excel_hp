package com.example.onekids_project.importexport.model;

import lombok.Data;

import java.util.List;

/**
 * date 2021-03-27 16:48
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class AttendanceEmployeeMonth {
    private Long id;

    private String name;

    List<AttendanceEmployeeDetailMonth> attendanceEmployeeDateList;
}
