package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.master.request.kids.KidsSearchAdminRequest;
import com.example.onekids_project.mobile.plus.request.birthday.SearchKidsBirthdayPlusRequest;
import com.example.onekids_project.mobile.plus.request.kidsQuality.SearchKidsQualityPlusRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchKidsBirthDayRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchParentBirthDayRequest;
import com.example.onekids_project.request.evaluatekids.EvaluatePeriodicSearchRequest;
import com.example.onekids_project.request.finance.statistical.FinanceSearchKidsRequest;
import com.example.onekids_project.request.kids.*;
import com.example.onekids_project.request.kids.transfer.SearchKidsTransferRequest;
import com.example.onekids_project.request.kidsheightweight.SearchKidsHeightWeightRequest;
import com.example.onekids_project.request.studentgroup.SearchStudentGroupRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface KidsRepositoryCustom {
    /**
     * tìm kiếm học sinh theo id
     *
     * @param idSchool
     * @param id
     * @return
     */
    Optional<Kids> findByIdKid(Long idSchool, Long id);

    /**
     * tìm kiếm tất cả học sinh
     *
     * @param idSchool
     * @param pageable
     * @return
     */
    List<Kids> findAllKids(Long idSchool, Pageable pageable);

    List<Kids> searchKidsHeightWeight(Long idSchool, SearchKidsHeightWeightRequest request);

    List<Kids> searchKids(Long idSchool, SearchKidsRequest request);
    List<Kids> searchKidsTransfer(Long idSchool, SearchKidsTransferRequest request);

    List<Kids> searchKidsGroupOut(Long idSchool, SearchKidsGroupOutRequest request);

    List<Kids> searchKidsGroupOutExcel(Long idSchool, ExcelGroupOutRequest request);

    long countSearchKids(Long idSchool, SearchKidsRequest request);
    long countSearchKidsTransfer(Long idSchool, SearchKidsTransferRequest request);

    long countSearchKidsGroupOut(Long idSchool, SearchKidsGroupOutRequest request);

    long countSearchKidsChart(Long idSchool, Long idGrade, Long idClass);

    List<Kids> searchKidsAdmin(KidsSearchAdminRequest request, List<Long> idSchoolList);

    long countSearchAdminKids(KidsSearchAdminRequest request, List<Long> idSchoolList);

    /**
     * tìm kiếm điểm danh của tất cả học sinh
     *
     * @param idSchool
     * @param attendanceKidsSearchRequest
     * @return
     */
//    List<Kids> searchKidsAttendance(Long idSchool, AttendanceKidsSearchRequest attendanceKidsSearchRequest);

    /**
     * tìm kiếm học sinh theo các fied
     * <p>
     * //     * @param searchKidsExportRequest
     *
     * @return
     */

    List<Kids> searchKidsByGradeClass(Long idSchool, SearchKidsExportRequest searchKidsExportRequest);

    /* List danh sách cân nặng chiều cao tất cả học sinh theo lựa chọn
     *  @Param searchKidsHeightWeightRequest
     */

    List<Kids> searchKidsByEvaluatePeriodic(Long idSchool, EvaluatePeriodicSearchRequest evaluatePeriodicSearchRequest);

    /**
     * tìm kiếm sinh nhật học sinh
     */
    List<Kids> searchKidsBirthday(Long idSchool, SearchKidsBirthDayRequest searchKidsBirthDayRequest);

    /**
     * search kid in class
     *
     * @param idSchool
     * @param searchKidsClassRequest
     * @return
     */
    List<Kids> searchKidsClass(Long idSchool, SearchKidsClassRequest searchKidsClassRequest);

    List<Kids> findAllKidsAlbum(Long idSchool);

    List<Kids> findAllKidsAlbumClass(Long idSchool, Long idClass);

    List<Kids> findAllKidsA(Long idSchool);

    List<Kids> findAlbumClass(Long idSchool, Long idClass);

    /**
     * tìm kiếm sinh nhật học sinh mobile
     */
    List<Kids> searchKidsBirthday(UserPrincipal principal, LocalDateTime localDateTime);

    List<Kids> searchKidsBirthdayNoSchool(LocalDate localDate);

    List<Kids> searchParentBirthdayNoSchool(LocalDate localDate);

    List<Kids> searchKidsBirthdayPlus(UserPrincipal principal, LocalDateTime localDate);

    List<Kids> searchKidsBirthWeek(UserPrincipal principal, LocalDate monday);

    List<Kids> searchKidsBirthMonth(UserPrincipal principal, LocalDateTime localDateTime);


    int getKidsBirthdayInClass(Long idClass);

    /**
     * tìm kiếm sức khỏe học sinh của của lớp theo teacher
     */
    List<Kids> finQualityKidsOfClass(UserPrincipal principal);

    // find Kids by idClass and active
    List<Kids> findKidsOfClass(Long idClass);

    List<Kids> findAllKidGroup(Long idSchool, SearchStudentGroupRequest request);

    List<Kids> searchParentBirthdayNew(Long idSchool, SearchParentBirthDayRequest searchParentBirthDayRequest);

    List<Kids> findAllKidsGrade(UserPrincipal principal, List<Long> dataGradeNotifyList);

    List<Kids> findAllKidsClass(UserPrincipal principal, List<Long> idClassList);

    List<Kids> findAllKidsGroup(UserPrincipal principal, List<Long> idGroupList);

    List<Kids> findAllKids(UserPrincipal principal, List<Long> idKid);

    List<Kids> findKidsWithPhoneRepresentationIndSchool(Long idSchool, String phone);

    List<Kids> searchKidsBirthdayNew(Long idSchool, SearchKidsBirthDayRequest request);

    long countSearchKidsBirthday(Long idSchool, SearchKidsBirthDayRequest request);

    List<Kids> searchParentBirthdayNewa(Long idSchool, SearchParentBirthDayRequest request);

    long countsearchParentBirthdayNewa(Long idSchool, SearchParentBirthDayRequest request);

    List<Kids> findByKidsClassWithStatus(Long idClass, String status);
    List<Kids> findByKidsIdSchoolWithClassWithStatus(Long idSchool, Long idClass, String status);

    List<Kids> findByKidsClassWithStatusName(Long idClass, String status, String fullName, List<Long>... idKidList);

    List<Kids> findByKidsClassWithStatusNameExcel(Long idClass, String status);

    List<Kids> findKidInClassAndStatusWithDate(LocalDate date, List<Long> idClassList);

    List<Kids> findKidOneClassAndStatusWithDate(LocalDate date, Long idClass);

    List<Kids> findKidInSchoolAndStatusWithDate(LocalDate date, Long idSchool);

    List<Kids> finQualityKidsforPlus(Long idSchool, SearchKidsQualityPlusRequest request);

    List<Kids> findKidsByIdList(List<Long> idKidList);

    List<Kids> findKidByIdSchool(Long idSchool, SearchKidsBirthdayPlusRequest request);

    List<Kids> searchKidsBirthWeekforplus(Long idSchool, SearchKidsBirthdayPlusRequest request, LocalDate toLocalDate);

    List<Kids> searchMonthBirthday(Long idSchool, SearchKidsBirthdayPlusRequest request);

    List<Kids> findKidByIdSchoolcout(Long idSchool);

    List<Kids> searchKidsBirthWeekforplusnew(Long idSchool, LocalDate monday);

    List<Kids> searchMonthBirthdaycout(Long idSchool);

    /**
     * lấy danh sách học sinh có ít nhất một ngày trong tháng ở trạng thái đi học hoặc bảo lưu hoặc chờ học
     *
     * @param idClass
     * @return
     */
//    List<Kids> findKidsWithStatus(Long idClass);


    List<Kids> searchByStatusOrderByName(Long idClass, FinanceSearchKidsRequest request);

    List<Kids> getKidsInClassAndStatusAndName(Long idClass, String kidsStatus, String fullName);

    List<Kids> getKidsListForCelebrateAuto(Long idSchool, String gender, String type);

    Optional<Kids> getKidsDelActiveAndActivateCustom(Long id);
}
