package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.kids.AttendanceConfig;
import com.example.onekids_project.repository.repositorycustom.AttendanceConfigCustom;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AttendanceConfigRepositoryImpl extends BaseRepositoryimpl<AttendanceConfig> implements AttendanceConfigCustom {
    @Override
    public Optional<AttendanceConfig> findAttendanceConfigFinal(Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        queryStr.append("ORDER BY created_date DESC LIMIT 1 ");
        List<AttendanceConfig> attendanceConfigList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(attendanceConfigList)) {
            return Optional.empty();
        }
        return Optional.ofNullable(attendanceConfigList.get(0));
    }

    @Override
    public Optional<AttendanceConfig> findAttendanceConfigDate(Long idSchool, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and date(created_date)<=:date ");
        mapParams.put("date", date);

        queryStr.append("ORDER BY created_date DESC LIMIT 1 ");
        List<AttendanceConfig> attendanceConfigList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(attendanceConfigList)) {
            return Optional.empty();
        }
        return Optional.ofNullable(attendanceConfigList.get(0));
    }

    @Override
    public Optional<AttendanceConfig> findAttendanceConfigInDate(Long idSchool, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and date(created_date)=:date ");
        mapParams.put("date", date);

        queryStr.append("ORDER BY created_date DESC LIMIT 1 ");
        List<AttendanceConfig> attendanceConfigList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(attendanceConfigList)) {
            return Optional.empty();
        }
        return Optional.ofNullable(attendanceConfigList.get(0));
    }
}
