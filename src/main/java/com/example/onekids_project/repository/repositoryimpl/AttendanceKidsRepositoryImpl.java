package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AttendanceConstant;
import com.example.onekids_project.entity.kids.AttendanceKids;
import com.example.onekids_project.repository.repositorycustom.AttendanceKidsRepositoryCustom;
import com.example.onekids_project.request.attendancekids.AttendanceKidsSearchRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AttendanceKidsRepositoryImpl extends BaseRepositoryimpl<AttendanceKids> implements AttendanceKidsRepositoryCustom {
    @Override
    public Optional<AttendanceKids> findByIdAttendance(Long idSchool, Long id) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (id != null) {
            queryStr.append("and id=:id");
            mapParams.put("id", id);
        }
        List<AttendanceKids> attendanceKidsList = findAllNoPaging(queryStr.toString(), mapParams);
        if (!CollectionUtils.isEmpty(attendanceKidsList)) {
            return Optional.ofNullable(attendanceKidsList.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<AttendanceKids> searchAllAttendanceKidsDate(Long idSchool, String type, AttendanceKidsSearchRequest attendanceKidsSearchRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and attendance_date=:attendanceDate ");
        mapParams.put("attendanceDate", attendanceKidsSearchRequest.getDate());
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", attendanceKidsSearchRequest.getIdClass());
        if (attendanceKidsSearchRequest.getAbsentStatus() != null) {
            if (type.equals(AttendanceConstant.ATTENDANCE_ARRIVE)) {
                queryStr.append("and attendance_arrive=:absentStatus ");
            } else if (type.equals(AttendanceConstant.ATTENDANCE_LEAVE)) {
                queryStr.append("and attendance_leave=:absentStatus ");
            } else if (type.equals(AttendanceConstant.ATTENDANCE_EAT)) {
                queryStr.append("and attendance_eat=:absentStatus ");
            }
            mapParams.put("absentStatus", attendanceKidsSearchRequest.getAbsentStatus());
        }
        queryStr.append("order by (Select first_name from ma_kids model1 where model1.id=model.id_kid) collate utf8_vietnamese_ci ");
        List<AttendanceKids> attendanceKidsList = findAllNoPaging(queryStr.toString(), mapParams);
        return attendanceKidsList;
    }

    @Override
    public List<AttendanceKids> findAttendanceKidsDetailOfMonth(Long idSchool, Long idKid, LocalDate dateStart, LocalDate dateEnd) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kid=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (dateStart != null && dateEnd != null) {
            queryStr.append("and attendance_date>=:dateStart and attendance_date<:dateEnd ");
            mapParams.put("dateStart", dateStart);
            mapParams.put("dateEnd", dateEnd);
        }
        queryStr.append("order by attendance_date desc");
        List<AttendanceKids> attendanceKidsList = findAllNoPaging(queryStr.toString(), mapParams);
        return attendanceKidsList;
    }

    /**
     * tìm kiếm điểm danh tất cả học sinh 1 lớp trong một tháng
     *
     * @param idSchool
     * @param idClass
     * @return
     */
    @Override
    public List<AttendanceKids> findAttendanceKidsClassOfMonth(Long idSchool, Long idClass, LocalDate dateStart, LocalDate dateEnd) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);

        queryStr.append("and attendance_date>=:dateStart and attendance_date<:dateEnd ");
        mapParams.put("dateStart", dateStart);
        mapParams.put("dateEnd", dateEnd);

        queryStr.append("order by attendance_date asc");
        List<AttendanceKids> attendanceKidsList = findAllNoPaging(queryStr.toString(), mapParams);
        return attendanceKidsList;
    }

    @Override
    public List<AttendanceKids> findAttendanceKidsForMobile(Long idSchool, Long idKid, LocalDate localDate, Integer pageNumber) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kid=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (localDate != null) {
            queryStr.append("and attendance_date=:localDate ");
            mapParams.put("localDate", localDate);
        }
        queryStr.append("and attendance_date <=:nowDate ");
        mapParams.put("nowDate", LocalDate.now());
        queryStr.append("order by attendance_date desc ");
        List<AttendanceKids> attendanceKidsList = findAllMobilePaging(queryStr.toString(), mapParams, pageNumber);
        return attendanceKidsList;
    }

    @Override
    public long getCountAttendance(Long idSchool, Long idKid, LocalDate localDate, Integer pageNumber) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kid=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (localDate != null) {
            queryStr.append("and attendance_date=:localDate ");
            mapParams.put("localDate", localDate);
        }
        queryStr.append("and attendance_date <=:nowDate ");
        mapParams.put("nowDate", LocalDate.now());
        List<AttendanceKids> attendanceKidsList = findAllMobilePaging(queryStr.toString(), mapParams, pageNumber + 1);
        return attendanceKidsList.size();
    }

    @Override
    public List<AttendanceKids> findAttendanceMonth(Long idSchool, Long idKid, LocalDate dateStart, LocalDate dateEnd) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kid=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (dateStart != null && dateEnd != null) {
            queryStr.append("and attendance_date>=:dateStart and attendance_date<:dateEnd ");
            mapParams.put("dateStart", dateStart);
            mapParams.put("dateEnd", dateEnd);
        }
        queryStr.append("order by attendance_date desc ");
        List<AttendanceKids> attendanceKidsList = findAllNoPaging(queryStr.toString(), mapParams);
        return attendanceKidsList;
    }

    @Override
    public List<AttendanceKids> findAttendanceEatMonth(Long idSchool, Long idKid, LocalDate dateStart, LocalDate dateEnd) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kid=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (dateStart != null && dateEnd != null) {
            queryStr.append("and attendance_date>=:dateStart and attendance_date<:dateEnd ");
            mapParams.put("dateStart", dateStart);
            mapParams.put("dateEnd", dateEnd);
        }
        queryStr.append("and attendance_date <=:nowDate ");
        mapParams.put("nowDate", LocalDate.now());
        queryStr.append("order by attendance_date desc ");
        List<AttendanceKids> attendanceKidsList = findAllNoPaging(queryStr.toString(), mapParams);
        return attendanceKidsList;
    }

    @Override
    public List<AttendanceKids> findAllMonthAttendanceKidsForMobile(Long idSchool, Long idKid, LocalDate localDate, LocalDate dateStart, LocalDate dateEnd) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKid != null) {
            queryStr.append("and id_kid=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (dateStart != null && dateEnd != null) {
            queryStr.append("and attendance_date>=:dateStart and attendance_date<:dateEnd ");
            mapParams.put("dateStart", dateStart);
            mapParams.put("dateEnd", dateEnd);
        }
        queryStr.append("order by attendance_date desc ");
        List<AttendanceKids> attendanceKidsList = findAllNoPaging(queryStr.toString(), mapParams);
        return attendanceKidsList;
    }

//    @Override
//    public List<AttendanceKids> findAttendanceKidStartEndDate(Long idKid, LocalDate startDate, LocalDate endDate) {
//        StringBuilder queryStr = new StringBuilder("");
//        Map<String, Object> mapParams = new HashMap<>();
//        if (idKid != null) {
//            queryStr.append("and id_kid=:idKid ");
//            mapParams.put("idKid", idKid);
//        }
//        if (startDate != null && endDate != null) {
//            queryStr.append("and attendance_date>=:dateStart and attendance_date<=:dateEnd ");
//            mapParams.put("dateStart", startDate);
//            mapParams.put("dateEnd", endDate);
//        }
//        queryStr.append("and status_number=1 ");
//        List<AttendanceKids> attendanceKidsList = findAllNoPaging(queryStr.toString(), mapParams);
//        return attendanceKidsList;
//    }

    @Override
    public List<AttendanceKids> findAttendanceKidDay(UserPrincipal principal, LocalDate localDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        LocalDate dateNow = LocalDate.now();
        if (principal.getSchoolConfig().getAgainAttendance() != null) {
            LocalDate dateCheck = LocalDate.now().minusDays(principal.getSchoolConfig().getAgainAttendance());
            if (localDate != null) {
                queryStr.append("  ma_kids as mk ON mk.id = model.id_kid where model.del_active=1 and model.attendance_date=:localDate and model.attendance_date <=:dateNow and model.attendance_date >=:dateCheck and model.id_class=:class order  by mk.first_name collate utf8_vietnamese_ci ");
                mapParams.put("localDate", localDate);
                mapParams.put("class", principal.getIdClassLogin());
                mapParams.put("dateNow", dateNow);
                mapParams.put("dateCheck", dateCheck);
            }
        } else {
            if (localDate != null) {
                queryStr.append("  ma_kids as mk ON mk.id = model.id_kid where model.del_active=1 and model.attendance_date=:localDate and model.attendance_date <=:dateNow and model.id_class=:class order  by mk.first_name collate utf8_vietnamese_ci ");
                mapParams.put("localDate", localDate);
                mapParams.put("class", principal.getIdClassLogin());
                mapParams.put("dateNow", dateNow);
            }
        }


        List<AttendanceKids> attendanceKidsList = findAllNoPagingandOrderBy(queryStr.toString(), mapParams);
        return attendanceKidsList;
    }

    @Override
    public List<AttendanceKids> findAttendanceKidsAfterDate(Long idKid, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_kid=:idKid ");
        mapParams.put("idKid", idKid);
        queryStr.append("and attendance_date>:date ");
        mapParams.put("date", date);
        List<AttendanceKids> attendanceKidsList = findAllNoPaging(queryStr.toString(), mapParams);
        return attendanceKidsList;
    }

    @Override
    public List<AttendanceKids> findAttendanceKidsDateList(List<Long> idKidList, LocalDate date) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_kid in :idKidList ");
        mapParams.put("idKidList", idKidList);
        queryStr.append("and attendance_date=:date ");
        mapParams.put("date", date);
        queryStr.append("order by (Select first_name from ma_kids model1 where model1.id=model.id_kid) collate utf8_vietnamese_ci ");
        List<AttendanceKids> attendanceKidsList = findAllNoPaging(queryStr.toString(), mapParams);
        return attendanceKidsList;
    }

    @Override
    public List<AttendanceKids> findAttendanceKidsMonthYear(Long idKid, int month, int year) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_kid=:idKid ");
        mapParams.put("idKid", idKid);
        queryStr.append("and month(attendance_date)=:month ");
        mapParams.put("month", month);
        queryStr.append("and year(attendance_date)=:year ");
        mapParams.put("year", year);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

}
