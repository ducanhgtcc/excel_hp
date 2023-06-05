package com.example.onekids_project.cronjobs;

import com.example.onekids_project.bean.PortBean;
import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.entity.kids.*;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * tạo điểm danh cho học sinh
 */
@PropertySource(value = "cronjob.properties")
@Component
public class AttendanceKidsCronjobs {
    private static final Logger logger = LoggerFactory.getLogger(AttendanceKidsCronjobs.class);

    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Autowired
    private AttendanceArriveKidsRepository attendanceArriveKidsRepository;

    @Autowired
    private AttendanceLeaveKidsRepository attendanceLeaveKidsRespository;

    @Autowired
    private AttendanceEatKidsRepository attendanceEatKidsRepository;

    @Autowired
    private AttendanceConfigRepository attendanceConfigRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private PortBean portBean;

    /**
     * tự động tạo điểm danh đến, về và ăn, trong một ngày trong bảng ma_attendance_kids, tạo 0:5p sáng
     */
    //second, minute, hour, day of month, month, day(s) of week
    @Transactional
    @Scheduled(cron = "${attendancekids.generate}")
    public void generateAttendanceKids() {
        logger.info("------start create attendance auto------");
        portBean.checkPortForCronjob();
        LocalDate nowDate = LocalDate.now();
        this.generateAttendanceForDate(nowDate, null);
        logger.info("------end create attendance auto------");
    }

    public void generateAttendanceManual(LocalDate date, Long idSchool) {
        logger.info("------start create attendance manual------");
        this.generateAttendanceForDate(date, idSchool);
        logger.info("------end create attendance manual------");
    }

    private void generateAttendanceForDate(LocalDate nowDate, Long idSchoolInput) {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<School> schoolList = new ArrayList<>();
        if (idSchoolInput == null) {
            schoolList = schoolRepository.findAllBySchoolActiveTrueAndDelActiveTrue();
        } else {
            School school = schoolRepository.findById(idSchoolInput).orElseThrow();
            schoolList.add(school);
        }
        List<AttendanceKids> existEvaluateList = attendanceKidsRepository.findByAttendanceDate(nowDate);
        List<Long> idKidExistEvaluateList = existEvaluateList.stream().map(x -> x.getKids().getId()).collect(Collectors.toList());
        List<Kids> allKidList = existEvaluateList.size() == 0 ? kidsRepository.findByKidStatusAndDelActiveTrue(KidsStatusConstant.STUDYING) : kidsRepository.findByIdNotInAndKidStatusAndDelActiveTrue(idKidExistEvaluateList, KidsStatusConstant.STUDYING);
        schoolList.forEach(x -> {
            Long idSchool = x.getId();
            AttendanceConfig attendanceConfig = attendanceConfigRepository.findAttendanceConfigFinal(idSchool).orElseThrow(() -> new NotFoundException("not foud attendanceDateConfig by id in cronjob 1"));
            List<Kids> kidsList = allKidList.stream().filter(a -> a.getIdSchool().equals(idSchool)).collect(Collectors.toList());
            kidsList.forEach(kid -> {
                AttendanceKids attendanceKids = new AttendanceKids();
                AttendanceArriveKids attendanceArriveKids = new AttendanceArriveKids();
                AttendanceLeaveKids attendanceLeaveKids = new AttendanceLeaveKids();
                AttendanceEatKids attendanceEatKids = new AttendanceEatKids();
                /**
                 * tạo điểm danh trong bảng ma_attendance_kids
                 */
                attendanceKids.setAutoCreateDate(localDateTime);
                attendanceKids.setAttendanceDate(nowDate);
                attendanceKids.setKids(kid);
                attendanceKids.setIdSchool(idSchool);
                attendanceKids.setIdGrade(kid.getIdGrade());
                attendanceKids.setMaClass(kid.getMaClass());
                attendanceKids.setAttendanceConfig(attendanceConfig);
                AttendanceKids attendanceKidsNowDate = attendanceKidsRepository.save(attendanceKids);

                /**
                 * tạo điểm danh đến
                 */
                attendanceArriveKids.setAutoCreateDate(localDateTime);
                attendanceArriveKids.setAttendanceKids(attendanceKidsNowDate);
                attendanceArriveKidsRepository.save(attendanceArriveKids);

                /**
                 * tạo điểm danh về
                 */
                attendanceLeaveKids.setAutoCreateDate(localDateTime);
                attendanceLeaveKids.setAttendanceKids(attendanceKidsNowDate);
                attendanceLeaveKidsRespository.save(attendanceLeaveKids);

                /**
                 * tạo điểm danh ăn
                 */
                attendanceEatKids.setAutoCreateDate(localDateTime);
                attendanceEatKids.setAttendanceKids(attendanceKidsNowDate);
                attendanceEatKidsRepository.save(attendanceEatKids);
            });
        });
    }

    /**
     * tạo điểm danh khi tạo học sinh
     *
     * @param kids
     */
    public void createAttendanceForKid(Kids kids) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate nowDate = LocalDate.now();
        AttendanceConfig attendanceConfig = attendanceConfigRepository.findAttendanceConfigFinal(kids.getIdSchool()).orElseThrow(() -> new NotFoundException("not foud attendanceDateConfig by id in cronjob 2"));
        this.setProperties(localDateTime, nowDate, kids, kids.getIdSchool(), attendanceConfig);
        logger.info("Tạo điểm danh cho học sinh có id=" + kids.getId());
    }

    /**
     * tạo điểm danh khi tạo đơn xin nghỉ
     *
     * @param absentLetter
     * @param idUser
     */
    public void createAttendanceByAbsent(AbsentLetter absentLetter, Long idUser) {
        //đơn không quá hạn mới cho phép tạo điểm danh tự động
        if (!absentLetter.isExpired()) {
            MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(idUser).orElseThrow();
            this.setPropertiesForAbsent(absentLetter.getAbsentDateList(), absentLetter.getKids(), maUser);
            logger.info("Tạo điểm danh xin nghỉ thành công cho cho học sinh " + absentLetter.getKids().getFullName());
        }
    }

    /**
     * tạo thông tin các bảng
     * 1, attendanceKids
     * 2, attendanceArriveKids
     * 3, attendanceLeaveKids
     * 4, attendanceEatKids
     *
     * @param date
     * @param kid
     */
    private void setProperties(LocalDateTime localDateTime, LocalDate date, Kids kid, Long idSchool, AttendanceConfig attendanceConfig) {
        Optional<AttendanceKids> attendanceKidsOptional = attendanceKidsRepository.findByAttendanceDateAndKidsId(date, kid.getId());
        if (attendanceKidsOptional.isEmpty()) {
            AttendanceKids attendanceKids = new AttendanceKids();
            AttendanceArriveKids attendanceArriveKids = new AttendanceArriveKids();
            AttendanceLeaveKids attendanceLeaveKids = new AttendanceLeaveKids();
            AttendanceEatKids attendanceEatKids = new AttendanceEatKids();
            /**
             * tạo điểm danh trong bảng ma_attendance_kids
             */
            attendanceKids.setAutoCreateDate(localDateTime);
            attendanceKids.setAttendanceDate(date);
            attendanceKids.setKids(kid);
            attendanceKids.setIdSchool(idSchool);
            attendanceKids.setIdGrade(kid.getIdGrade());
            attendanceKids.setMaClass(kid.getMaClass());
            attendanceKids.setAttendanceConfig(attendanceConfig);
            AttendanceKids attendanceKidsNowDate = attendanceKidsRepository.save(attendanceKids);

            /**
             * tạo điểm danh đến
             */
            attendanceArriveKids.setAutoCreateDate(localDateTime);
            attendanceArriveKids.setAttendanceKids(attendanceKidsNowDate);
            attendanceArriveKidsRepository.save(attendanceArriveKids);

            /**
             * tạo điểm danh về
             */
            attendanceLeaveKids.setAutoCreateDate(localDateTime);
            attendanceLeaveKids.setAttendanceKids(attendanceKidsNowDate);
            attendanceLeaveKidsRespository.save(attendanceLeaveKids);

            /**
             * tạo điểm danh ăn
             */
            attendanceEatKids.setAutoCreateDate(localDateTime);
            attendanceEatKids.setAttendanceKids(attendanceKidsNowDate);
            attendanceEatKidsRepository.save(attendanceEatKids);
        }
    }

    /**
     * create attendance cho trường hợp tạo đơn xin nghỉ
     *
     * @param absentDateList
     * @param kid
     * @param maUser
     */
    private void setPropertiesForAbsent(List<AbsentDate> absentDateList, Kids kid, MaUser maUser) {
        Long idSchool = kid.getIdSchool();
        LocalDateTime localDateTime = LocalDateTime.now();
        absentDateList.forEach(x -> {
            AttendanceConfig attendanceConfig = attendanceConfigRepository.findAttendanceConfigFinal(idSchool).orElseThrow(() -> new NotFoundException("not foud attendanceDateConfig by id in cronjob 3"));
            AttendanceKids attendanceKids = new AttendanceKids();
            AttendanceLeaveKids attendanceLeaveKids = new AttendanceLeaveKids();
            AttendanceEatKids attendanceEatKids = new AttendanceEatKids();
            AttendanceArriveKids attendanceArriveKids = new AttendanceArriveKids();

            LocalDate dateAbsent = x.getAbsentDate();
            boolean morningAbsent = x.isAbsentMorning();
            boolean afternoonAbsent = x.isAbsentAfternoon();
            boolean eveningAbsent = x.isAbsentEvening();
            //lấy điểm danh theo ngày
            Optional<AttendanceKids> attendanceKidsOptional = attendanceKidsRepository.findByAttendanceDateAndKidsId(dateAbsent, kid.getId());
            //nếu đã tạo điểm danh thì cập nhật lại điểm danh từ đơn xin nghỉ
            if (attendanceKidsOptional.isPresent()) {
                //tạo điểm danh đến, nhà trường có ít nhất 1 buổi học cho ngày đó
                if (morningAbsent || afternoonAbsent || eveningAbsent) {
                    attendanceKids = attendanceKidsOptional.get();
                    attendanceKids.setAbsentStatus(AppConstant.APP_TRUE);
                    attendanceKids.setIdModified(maUser.getId());
                    attendanceKids.setLastModifieDate(localDateTime);
                    attendanceKids.setLastModifieBy(maUser.getFullName());
                    attendanceKids.setAttendanceArrive(AppConstant.APP_TRUE);
                    attendanceKidsRepository.save(attendanceKids);

                    attendanceArriveKids = attendanceKids.getAttendanceArriveKids();
                    attendanceLeaveKids = attendanceKids.getAttendanceLeaveKids();
                    if (morningAbsent) {
                        attendanceArriveKids.setMorningYes(AppConstant.APP_TRUE);
                        attendanceArriveKids.setMorning(AppConstant.APP_FALSE);
                        attendanceArriveKids.setMorningNo(AppConstant.APP_FALSE);
                    }
                    if (afternoonAbsent) {
                        attendanceArriveKids.setAfternoonYes(AppConstant.APP_TRUE);
                        attendanceArriveKids.setAfternoon(AppConstant.APP_FALSE);
                        attendanceArriveKids.setAfternoonNo(AppConstant.APP_FALSE);
                    }
                    if (eveningAbsent) {
                        attendanceArriveKids.setEveningYes(AppConstant.APP_TRUE);
                        attendanceArriveKids.setEvening(AppConstant.APP_FALSE);
                        attendanceArriveKids.setEveningNo(AppConstant.APP_FALSE);
                    }
                    if (!attendanceArriveKids.isMorning() && !attendanceArriveKids.isAfternoon() && !attendanceArriveKids.isEvening()) {
                        attendanceArriveKids.setTimeArriveKid(null);

                        //reset lại điểm danh về
                        attendanceLeaveKids.setTimeLeaveKid(null);
                        attendanceKids.setAttendanceLeave(AppConstant.APP_FALSE);
                        attendanceLeaveKids.setStatusLeave(AppConstant.APP_FALSE);
                        attendanceLeaveKids.setMinutePickupLate(0);
                    }
                    attendanceArriveKids.setIdModified(maUser.getId());
                    attendanceArriveKids.setLastModifieDate(localDateTime);
                    attendanceArriveKids.setLastModifieBy(maUser.getFullName());
                    attendanceArriveKidsRepository.save(attendanceArriveKids);
                }
            } else {
                //tạo điểm danh cho các ngày theo đơn xin nghỉ khi chưa tồn tại điểm danh
                attendanceKids.setAutoCreateDate(localDateTime);
                attendanceKids.setAttendanceDate(dateAbsent);
                attendanceKids.setKids(kid);
                attendanceKids.setIdSchool(idSchool);
                attendanceKids.setIdGrade(kid.getIdGrade());
                attendanceKids.setMaClass(kid.getMaClass());
                attendanceKids.setAttendanceConfig(attendanceConfig);

                /**
                 * tạo điểm danh đến
                 */
                //nếu có một ngày nghỉ thì tạo có điểm danh đến
                if (morningAbsent || afternoonAbsent || eveningAbsent) {
                    attendanceKids.setAbsentStatus(AppConstant.APP_TRUE);
                    attendanceKids.setIdCreated(maUser.getId());
                    attendanceKids.setCreatedDate(localDateTime);
                    attendanceKids.setCreatedBy(maUser.getFullName());
                    attendanceKids.setIdModified(maUser.getId());
                    attendanceKids.setLastModifieDate(localDateTime);
                    attendanceKids.setLastModifieBy(maUser.getFullName());


                    attendanceKids.setAttendanceArrive(AppConstant.APP_TRUE);
                    attendanceArriveKids.setMorningYes(morningAbsent);
                    attendanceArriveKids.setAfternoonYes(afternoonAbsent);
                    attendanceArriveKids.setEveningYes(eveningAbsent);
                    attendanceArriveKids.setIdCreated(maUser.getId());
                    attendanceArriveKids.setCreatedDate(localDateTime);
                    attendanceArriveKids.setIdModified(maUser.getId());
                    attendanceArriveKids.setLastModifieDate(localDateTime);
                    attendanceArriveKids.setCreatedBy(maUser.getFullName());
                    attendanceArriveKids.setLastModifieBy(maUser.getFullName());
                }
                AttendanceKids attendanceKidsNowDate = attendanceKidsRepository.save(attendanceKids);
                attendanceArriveKids.setAutoCreateDate(localDateTime);
                attendanceArriveKids.setAttendanceKids(attendanceKidsNowDate);
                attendanceArriveKidsRepository.save(attendanceArriveKids);

                /**
                 * tạo điểm danh về
                 */
                attendanceLeaveKids.setAutoCreateDate(localDateTime);
                attendanceLeaveKids.setAttendanceKids(attendanceKidsNowDate);
                attendanceLeaveKidsRespository.save(attendanceLeaveKids);

                /**
                 * tạo điểm danh ăn
                 */
                attendanceEatKids.setAutoCreateDate(localDateTime);
                attendanceEatKids.setAttendanceKids(attendanceKidsNowDate);
                attendanceEatKidsRepository.save(attendanceEatKids);
            }
        });
    }
}
