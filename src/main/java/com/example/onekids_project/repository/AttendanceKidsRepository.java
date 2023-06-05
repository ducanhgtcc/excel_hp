package com.example.onekids_project.repository;

import com.example.onekids_project.dto.ListIdKidDTO;
import com.example.onekids_project.entity.kids.AttendanceKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.repository.repositorycustom.AttendanceKidsRepositoryCustom;
import com.example.onekids_project.response.changeQuery.chart.AttendanceKidsQueryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceKidsRepository extends JpaRepository<AttendanceKids, Long>, AttendanceKidsRepositoryCustom {
    @Modifying
    @Query(value = "delete from ma_attendance_kids where id=:id", nativeQuery = true)
    void deleteByIdCustom(Long id);

    @Query("SELECT new com.example.onekids_project.response.changeQuery.chart.AttendanceKidsQueryResponse(ak.attendanceDate, ak.kids.id, ak.attendanceArriveKids.morning, ak.attendanceArriveKids.morningYes, ak.attendanceArriveKids.morningNo, " +
            "ak.attendanceArriveKids.afternoon, ak.attendanceArriveKids.afternoonYes, ak.attendanceArriveKids.afternoonNo, " +
            "ak.attendanceArriveKids.evening, ak.attendanceArriveKids.eveningYes, ak.attendanceArriveKids.eveningNo) from AttendanceKids ak " +
            "where ak.idSchool = :idSchool " +
            "and (:idGrade is null or ak.idGrade = :idGrade) " +
            "and (:idClass is null or ak.maClass.id = :idClass) " +
            "and ak.attendanceDate in :dates " +
            "and ak.delActive = true")
    List<AttendanceKidsQueryResponse> searchAttendanceKidsChart(@Param("idSchool") Long idSchool,
                                                                @Param("idGrade") Long idGrade,
                                                                @Param("idClass") Long idClass,
                                                                @Param("dates") List<LocalDate> dates);

    /**
     * tổng số học sinh trong một tháng của 1 lớp
     *
     * @param idSchool
     * @param idClass
     * @param dateStart
     * @param dateEnd
     * @return
     */


    @Query("select new com.example.onekids_project.dto.ListIdKidDTO(ak.kids.id) from AttendanceKids ak " +
            "left join ak.attendanceArriveKids aak " +
            "left join ak.attendanceLeaveKids alk " +
            "left join ak.attendanceEatKids aek " +
            "where ak.idSchool = :idSchool and ak.maClass.id = :idClass and ak.attendanceDate >= :dateStart and ak.attendanceDate < :dateEnd " +
            "group by ak.kids.id")
    List<ListIdKidDTO> totalAttendanceKidsDetailOfMonth(@Param("idSchool") Long idSchool,
                                                        @Param("idClass") Long idClass,
                                                        @Param("dateStart") LocalDate dateStart,
                                                        @Param("dateEnd") LocalDate dateEnd);

    Optional<AttendanceKids> findByAttendanceDateAndKidsId(LocalDate date, Long idKid);

    Optional<AttendanceKids> findByIdAndDelActiveTrue(Long idAttendance);

    List<AttendanceKids> findByIdSchoolAndAttendanceDateAndAttendanceArrive(Long idSchool, LocalDate date, boolean status);

    List<AttendanceKids> findByIdSchoolAndAttendanceDate(Long idSchool, LocalDate date);

    List<AttendanceKids> findByKidsIdAndAttendanceDate(Long idKid, LocalDate date);

    AttendanceKids findByDelActiveTrueAndAttendanceDateAndKids_IdAndMaClass_Id(LocalDate date, Long idKid, Long idClass);

    AttendanceKids findByDelActiveTrueAndAttendanceDateAndKids_Id(LocalDate date, Long idKid);

    List<AttendanceKids> findByKidsInAndAttendanceDate(List<Kids> kids, LocalDate date);

    List<AttendanceKids> findByKidsInAndAttendanceDateBetween(List<Kids> kidsList, LocalDate startDate, LocalDate endDate);

    List<AttendanceKids> findByDelActiveTrueAndMaClass_IdAndAttendanceDate(Long idClass, LocalDate date);

    List<AttendanceKids> findByKidsIdInAndAttendanceDate(List<Long> idKidList, LocalDate date);

    List<AttendanceKids> findByKidsIdAndAttendanceDateGreaterThanEqualAndAttendanceDateLessThanEqual(Long idKid, LocalDate startDate, LocalDate endDate);

    List<AttendanceKids> findByIdSchoolAndAttendanceDateAndKidsKidStatusAndKidsDelActiveTrue(Long idSchool, LocalDate date, String kidStatus);
    List<AttendanceKids> findByAttendanceDateAndDelActiveTrue(LocalDate date);
    long countByIdSchoolAndAttendanceDateAndDelActiveTrue(Long idSchool, LocalDate date);
    List<AttendanceKids> findByAttendanceDate(LocalDate date);
}
