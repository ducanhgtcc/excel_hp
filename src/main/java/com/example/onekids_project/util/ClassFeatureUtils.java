package com.example.onekids_project.util;

import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.mobile.response.FeatureClassResponse;
import com.example.onekids_project.mobile.response.TeacherClassResponse;

import java.util.ArrayList;
import java.util.List;

public class ClassFeatureUtils {

    /**
     * set đặc trưng của lớp
     *
     * @param infoEmployeeSchoolList
     * @param maClass
     * @return
     */
    public static FeatureClassResponse setFeatureClass(List<InfoEmployeeSchool> infoEmployeeSchoolList, MaClass maClass) {
        FeatureClassResponse response = new FeatureClassResponse();
        List<TeacherClassResponse> dataList = new ArrayList<>();
        infoEmployeeSchoolList.forEach(x -> {
            TeacherClassResponse data = new TeacherClassResponse();
            data.setPhone(x.getPhone());
            data.setTeacherName(x.getFullName());
            dataList.add(data);
        });

        response.setNumberKid((int) maClass.getKidsList().stream().filter(y -> y.getKidStatus().equalsIgnoreCase(KidsStatusConstant.STUDYING)).count());
        response.setClassName(maClass.getClassName());
        response.setTeacherClassList(dataList);
        response.setId(maClass.getId());
        return response;
    }
}
