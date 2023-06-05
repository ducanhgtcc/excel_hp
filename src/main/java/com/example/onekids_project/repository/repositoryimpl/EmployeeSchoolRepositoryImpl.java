package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.repository.repositorycustom.EmployeeSchoolRepositoryCustom;
import com.example.onekids_project.request.birthdaymanagement.SearchTeacherBirthDayRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeSchoolRepositoryImpl extends BaseRepositoryimpl<InfoEmployeeSchool> implements EmployeeSchoolRepositoryCustom {

//    @Override
//    public List<MaUser> searchTeacherBirthday(Long idSchoolLogin, SearchTeacherBirthDayRequest searchTeacherBirthDayRequest) {
//        StringBuilder queryStr = new StringBuilder("");
//        Map<String, Object> mapParams = new HashMap<>();
//        if (searchTeacherBirthDayRequest != null) {
//            if (searchTeacherBirthDayRequest.getDate() != null) {
//                queryStr.append("and birthday=:birthday ");
//                mapParams.put("birthday", searchTeacherBirthDayRequest.getDate());
//            }
//            if (searchTeacherBirthDayRequest.getWeek() != null) {
//                queryStr.append("and birthday >=:monday ");
//                mapParams.put("monday", searchTeacherBirthDayRequest.getWeek().toString());
//                queryStr.append(" and birthday <=:sunday ");
//                mapParams.put("sunday", searchTeacherBirthDayRequest.getWeek().plusDays(6).toString());
//            }
//            if (searchTeacherBirthDayRequest.getMonth() != null) {
//                queryStr.append("and month(birthday) =:month ");
//                mapParams.put("month", searchTeacherBirthDayRequest.getMonth().getMonthValue());
//            }
//            if (StringUtils.isNotBlank(searchTeacherBirthDayRequest.getName())) {
//                queryStr.append("and full_name like :fullName ");
//                mapParams.put("fullName", "%" + searchTeacherBirthDayRequest.getName() + "%");
//            }
//
//        }
//        return findAllNoPaging(queryStr.toString(), mapParams);
//    }
}
