package com.example.onekids_project.cronjobs;

import com.example.onekids_project.bean.PortBean;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.kids.EvaluateDate;
import com.example.onekids_project.entity.kids.EvaluateMonth;
import com.example.onekids_project.entity.kids.EvaluateWeek;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.school.SchoolConfig;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.util.DateCommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * tạo đánh giá học sinh
 */
@PropertySource(value = "cronjob.properties")
@Component
public class EvaluateKidsCronjobs {
    private static final Logger logger = LoggerFactory.getLogger(EvaluateKidsCronjobs.class);

    @Autowired
    private EvaluateDateRepository evaluateDateRepository;

    @Autowired
    private EvaluateWeekRepository evaluateWeekRepository;

    @Autowired
    private EvaluateMonthRepository evaluateMonthRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private SchoolConfigRepository schoolConfigRepository;

    @Autowired
    private PortBean portBean;

    /**
     * tự động tạo đánh giá cho một ngày trong bảng ma_evaluate_kids, tạo lúc 0:10 sáng
     */
    //second, minute, hour, day of month, month, day(s) of week
    @Transactional
    @Scheduled(cron = "${evaluate.date}")
    public void generateEvaluateDateKids() {
        logger.info("---start auto create evaluate date---");
        portBean.checkPortForCronjob();
        LocalDate nowDate = LocalDate.now();
        this.generateEvaluateFromDate(nowDate, null);
        logger.info("---end auto create evaluate date---");
    }

    public void generateEvaluateDateManual(LocalDate date, Long idSchool) {
        logger.info("---start auto create evaluate date manual---");
        this.generateEvaluateFromDate(date, idSchool);
        logger.info("---end auto create evaluate date manual---");
    }


    private void generateEvaluateFromDate(LocalDate nowDate, Long idSchoolInput) {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<School> schoolList = new ArrayList<>();
        List<EvaluateDate> totalList = new ArrayList<>();
        if (idSchoolInput == null) {
            schoolList = schoolRepository.findAllBySchoolActiveTrueAndDelActiveTrue();
        } else {
            School school = schoolRepository.findById(idSchoolInput).orElseThrow();
            schoolList.add(school);
        }
        List<EvaluateDate> existEvaluateList = evaluateDateRepository.findByDate(nowDate);
        List<Long> idKidExistEvaluateList = existEvaluateList.stream().map(x -> x.getKids().getId()).collect(Collectors.toList());
        List<Kids> allKidList = existEvaluateList.size() == 0 ? kidsRepository.findByKidStatusAndDelActiveTrue(KidsStatusConstant.STUDYING) : kidsRepository.findByIdNotInAndKidStatusAndDelActiveTrue(idKidExistEvaluateList, KidsStatusConstant.STUDYING);
        schoolList.forEach(x -> {
            Long idSchool = x.getId();
            List<Kids> kidsList = allKidList.stream().filter(a -> a.getIdSchool().equals(idSchool)).collect(Collectors.toList());
            kidsList.forEach(y -> {
                EvaluateDate evaluateDate = new EvaluateDate();
                evaluateDate.setIdCreated(SystemConstant.ID_SYSTEM);
                evaluateDate.setAutoCreateDate(localDateTime);
                evaluateDate.setDate(nowDate);
                evaluateDate.setApproved(x.getSchoolConfig().isEvaluate());
                evaluateDate.setKids(y);
                evaluateDate.setIdSchool(idSchool);
                evaluateDate.setIdGrade(y.getIdGrade());
                evaluateDate.setIdClass(y.getMaClass().getId());
                totalList.add(evaluateDate);
            });
        });
        evaluateDateRepository.saveAll(totalList);
    }


    /**
     * tự động tạo đánh giá cho một tuần vào ngày thứ 2 trong bảng ma_evaluate_week, tạo lúc 0:15 sáng
     */
    //second, minute, hour, day of month, month, day(s) of week
    @Transactional
    @Scheduled(cron = "${evaluate.week}")
    public void generateEvaluateWeekKids() {
        logger.info("---start auto create evaluate week---");
        portBean.checkPortForCronjob();
        LocalDate nowDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        List<EvaluateWeek> totalList = new ArrayList<>();
        List<School> schoolList = schoolRepository.findAllBySchoolActiveTrueAndDelActiveTrue();
        List<EvaluateWeek> existEvaluateList = evaluateWeekRepository.findByDate(nowDate);
        List<Long> idKidExistEvaluateList = existEvaluateList.stream().map(x -> x.getKids().getId()).collect(Collectors.toList());
        List<Kids> allKidList = existEvaluateList.size() == 0 ? kidsRepository.findByKidStatusAndDelActiveTrue(KidsStatusConstant.STUDYING) : kidsRepository.findByIdNotInAndKidStatusAndDelActiveTrue(idKidExistEvaluateList, KidsStatusConstant.STUDYING);
        for (School x : schoolList) {
            Long idSchool = x.getId();
            List<Kids> kidsList = allKidList.stream().filter(a -> a.getIdSchool().equals(idSchool)).collect(Collectors.toList());
            kidsList.forEach(y -> {
                EvaluateWeek evaluateWeek = new EvaluateWeek();
                int week = nowDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
                evaluateWeek.setIdCreated(SystemConstant.ID_SYSTEM);
                evaluateWeek.setAutoCreateDate(localDateTime);
                evaluateWeek.setWeek(week);
                evaluateWeek.setDate(nowDate);
                evaluateWeek.setApproved(x.getSchoolConfig().isEvaluate());
                evaluateWeek.setKids(y);
                evaluateWeek.setIdSchool(idSchool);
                evaluateWeek.setIdGrade(y.getIdGrade());
                evaluateWeek.setIdClass(y.getMaClass().getId());
                totalList.add(evaluateWeek);
            });
        }
        evaluateWeekRepository.saveAll(totalList);
        logger.info("---end auto create evaluate week---");
    }

    /**
     * tự động tạo đánh giá cho một tháng vào ngày mùng 1 của tháng trong bảng ma_evaluate_month, tạo lúc 0:20 sáng
     */
    //second, minute, hour, day of month, month, day(s) of week
    @Transactional
    @Scheduled(cron = "${evaluate.month}")
    public void generateEvaluateMonthKids() {
        logger.info("---start auto create evaluate month---");
        portBean.checkPortForCronjob();
        LocalDate nowDate = LocalDate.now();
        int month = nowDate.getMonthValue();
        int year = nowDate.getYear();
        LocalDateTime localDateTime = LocalDateTime.now();
        List<EvaluateMonth> totalList = new ArrayList<>();
        List<School> schoolList = schoolRepository.findAllBySchoolActiveTrueAndDelActiveTrue();
        List<EvaluateMonth> existEvaluateList = evaluateMonthRepository.findByMonthAndYear(month, year);
        List<Long> idKidExistEvaluateList = existEvaluateList.stream().map(x -> x.getKids().getId()).collect(Collectors.toList());
        List<Kids> allKidList = existEvaluateList.size() == 0 ? kidsRepository.findByKidStatusAndDelActiveTrue(KidsStatusConstant.STUDYING) : kidsRepository.findByIdNotInAndKidStatusAndDelActiveTrue(idKidExistEvaluateList, KidsStatusConstant.STUDYING);
        for (School x : schoolList) {
            Long idSchool = x.getId();
            List<Kids> kidsList = allKidList.stream().filter(a -> a.getIdSchool().equals(idSchool)).collect(Collectors.toList());
            kidsList.forEach(y -> {
                EvaluateMonth evaluateMonth = new EvaluateMonth();
                evaluateMonth.setIdCreated(SystemConstant.ID_SYSTEM);
                evaluateMonth.setAutoCreateDate(localDateTime);
                evaluateMonth.setMonth(month);
                evaluateMonth.setYear(year);
                evaluateMonth.setApproved(x.getSchoolConfig().isEvaluate());
                evaluateMonth.setKids(y);
                evaluateMonth.setIdSchool(idSchool);
                evaluateMonth.setIdGrade(y.getIdGrade());
                evaluateMonth.setIdClass(y.getMaClass().getId());
                totalList.add(evaluateMonth);
            });
        }
        evaluateMonthRepository.saveAll(totalList);
        logger.info("---end auto create evaluate month---");
    }


//    ===================tạo cho từng trường=======================

    /**
     * tạo đánh giá cho một ngày khi tạo học sinh
     */
    public void generateEvaluateDateKidsSchool(Kids y) {
        LocalDate nowDate = LocalDate.now();
        int count = evaluateDateRepository.countByKidsIdAndDate(y.getId(), nowDate);
        if (count == 0) {
            SchoolConfig schoolConfig = schoolConfigRepository.findBySchoolIdAndDelActiveTrue(y.getIdSchool()).orElseThrow(() -> new NotFoundException("EvaluateKidsCronjobs: not found schoolConfig by id"));
            LocalDateTime localDateTime = LocalDateTime.now();
            EvaluateDate evaluateDate = new EvaluateDate();
            evaluateDate.setAutoCreateDate(localDateTime);
            evaluateDate.setDate(nowDate);
            evaluateDate.setKids(y);
            evaluateDate.setApproved(schoolConfig.isEvaluate());
            evaluateDate.setIdSchool(y.getIdSchool());
            evaluateDate.setIdGrade(y.getIdGrade());
            evaluateDate.setIdClass(y.getMaClass().getId());
            evaluateDateRepository.save(evaluateDate);
            logger.info("Tạo đánh giá học sinh theo ngày với idKid=" + y.getId());
        }
    }

    /**
     * tạo đánh giá cho một tuần khi tạo học sinh, chuyển lớp học sinh
     */
    public void generateEvaluateWeekKidsSchool(Kids y) {
        LocalDate monday = DateCommonUtils.getMonday(LocalDate.now());
        EvaluateWeek evaluateWeek = new EvaluateWeek();
        //chưa có nhận xét cho học sinh tại lớp đó thì mới tạo
        List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.findByKidsIdAndIdClassAndDate(y.getId(), y.getMaClass().getId(), monday);
        if (CollectionUtils.isEmpty(evaluateWeekList)) {
            SchoolConfig schoolConfig = schoolConfigRepository.findBySchoolIdAndDelActiveTrue(y.getIdSchool()).orElseThrow(() -> new NotFoundException("EvaluateKidsCronjobs: not found schoolConfig by id"));
            LocalDateTime localDateTime = LocalDateTime.now();
            int week = monday.get(WeekFields.of(Locale.getDefault()).weekOfYear());
            evaluateWeek.setAutoCreateDate(localDateTime);
            evaluateWeek.setDate(monday);
            evaluateWeek.setWeek(week);
            evaluateWeek.setApproved(schoolConfig.isEvaluate());
            evaluateWeek.setKids(y);
            evaluateWeek.setIdSchool(y.getIdSchool());
            evaluateWeek.setIdGrade(y.getIdGrade());
            evaluateWeek.setIdClass(y.getMaClass().getId());
            evaluateWeekRepository.save(evaluateWeek);
            logger.info("Tạo đánh giá học sinh theo tuần với idKid=" + y.getId());
        }
    }

    /**
     * tạo đánh giá cho một tháng khi tạo học sinh, chuyển lớp học sinh
     */
    public void generateEvaluateMonthKidsSchool(Kids y) {
        LocalDate nowDate = LocalDate.now();
        int month = nowDate.getMonthValue();
        int year = nowDate.getYear();
        //chưa có nhận xét cho học sinh tại lớp đó thì mới tạo
        List<EvaluateMonth> evaluateMonthList = evaluateMonthRepository.findByKidsIdAndIdClassAndYearAndMonth(y.getId(), y.getMaClass().getId(), year, month);
        if (CollectionUtils.isEmpty(evaluateMonthList)) {
            SchoolConfig schoolConfig = schoolConfigRepository.findBySchoolIdAndDelActiveTrue(y.getIdSchool()).orElseThrow(() -> new NotFoundException("EvaluateKidsCronjobs: not found schoolConfig by id"));
            LocalDateTime localDateTime = LocalDateTime.now();
            EvaluateMonth evaluateMonth = new EvaluateMonth();
            evaluateMonth.setAutoCreateDate(localDateTime);
            evaluateMonth.setMonth(month);
            evaluateMonth.setYear(year);
            evaluateMonth.setKids(y);
            evaluateMonth.setApproved(schoolConfig.isEvaluate());
            evaluateMonth.setIdSchool(y.getIdSchool());
            evaluateMonth.setIdGrade(y.getIdGrade());
            evaluateMonth.setIdClass(y.getMaClass().getId());
            evaluateMonthRepository.save(evaluateMonth);
            logger.info("Tạo đánh giá học sinh theo tháng với idKid=" + y.getId());
        }
    }
}
