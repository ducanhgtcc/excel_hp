package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.sample.AttendanceSample;
import com.example.onekids_project.repository.repositorycustom.AttendanceSampleRepositoryCustom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceSampleRepositoryImpl extends BaseRepositoryimpl<AttendanceSample> implements AttendanceSampleRepositoryCustom {
    @Override
    public List<AttendanceSample> findAllAttendanceSample(Long idSchool, Long idSystem) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idSystem != null) {
            queryStr.append("or id_school=:idSystem ");
            mapParams.put("idSystem", idSystem);
        }
        queryStr.append("order by id_school desc");
        List<AttendanceSample> attendanceSampleList = findAllNoPaging(queryStr.toString(), mapParams);
        return attendanceSampleList;
    }

    @Override
    public List<AttendanceSample> findAllAttendanceSampleWithType(Long idSchool, Long idSystem, String type) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and attendance_type=:type ");
        mapParams.put("type", type);

        if (idSchool != null && idSystem != null) {
            queryStr.append("and (id_school=:idSchool or id_school=:idSystem) ");
            mapParams.put("idSchool", idSchool);
            mapParams.put("idSystem", idSystem);
        } else {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }


        queryStr.append("order by id_school desc");
        List<AttendanceSample> attendanceSampleList = findAllNoPaging(queryStr.toString(), mapParams);
        return attendanceSampleList;
    }
}
