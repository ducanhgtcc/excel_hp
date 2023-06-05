package com.example.onekids_project.service.serviceimpl.scheduleimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.common.UrlFileConstant;
import com.example.onekids_project.entity.classes.*;
import com.example.onekids_project.importexport.model.ScheduleModel;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.classmenu.CreateFileAndPictureMenuMultiClassRequest;
import com.example.onekids_project.request.schedule.*;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.schedule.*;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.service.servicecustom.schedule.ScheduleService;
import com.example.onekids_project.util.DataUtils;
import com.example.onekids_project.util.ExportExcelUtils;
import com.example.onekids_project.util.HandleFileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;


@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ScheduleMorningRepository scheduleMorningRepository;
    @Autowired
    private ScheduleAfternoonRepository scheduleAfternoonRepository;
    @Autowired
    private ScheduleEveningRepository scheduleEveningRepository;

    @Autowired
    private ScheduleFileRepository scheduleFileRepository;
    @Autowired
    private UrlScheduleFileRepository urlScheduleFileRepository;

    @Autowired
    private SchoolService schoolService;


    @Override
    public List<ScheduleResponse> findAllScheduleInWeek(Long idSchool, SearchScheduleRequest searchScheduleRequest) {

        /**
         *Tìm kiếm các lớp theo ngày,khối(nếu có),lớp(nếu có)
         */
        List<MaClass> maClassList = maClassRepository.searchMaClassByIdGrade(idSchool, searchScheduleRequest.getIdGrade(), searchScheduleRequest.getIdClass());
        if (CollectionUtils.isEmpty(maClassList)) {
            return null;
        }
        /**
         *Tạo List chứa các thời biểu các lớp
         */
        List<ScheduleResponse> scheduleResponseList = new ArrayList<>();
        /**
         *Thời gian từ Client đưa vào của thứ 2 dạng String
         */
        String timeSchedule = searchScheduleRequest.getTimeSchedule();

        /**
         *Parse sang LocalDate
         */
        LocalDate monday = LocalDate.parse(timeSchedule);
        LocalDate tuesday = monday.plusDays(1);
        LocalDate wednesday = tuesday.plusDays(1);
        LocalDate thursday = wednesday.plusDays(1);
        LocalDate friday = thursday.plusDays(1);
        LocalDate saturday = friday.plusDays(1);
        LocalDate sunday = saturday.plusDays(1);
        String titleSchedule = null;

        if (CollectionUtils.isEmpty(maClassList)) {
            return null;
        }

        /**
         *Chạy vòng lặp các Class đã tìm thấy
         */
        for (MaClass maClass : maClassList) {
            /**
             * Kiểm tra lớp đó có thời khóa biểu hay không,nếu lớp có
             */
            if (!CollectionUtils.isEmpty(maClass.getClassScheduleList())) {
                /**
                 *Chạy vòng lặp lấy các thời khóa biểu cho từng class
                 */
                /**
                 * Tạo thời khóa biểu 1 lớp
                 */
                ScheduleResponse scheduleResponse = new ScheduleResponse();
                /**
                 * Tạo thời khóa biểu các buổi sáng chiều tối
                 */
                ScheduleDayRespone scheduleMorningDayRespone = new ScheduleDayRespone();
                ScheduleDayRespone scheduleAfternoonDayRespone = new ScheduleDayRespone();
                ScheduleDayRespone scheduleEveningDayRespone = new ScheduleDayRespone();

                for (ClassSchedule classSchedule : maClass.getClassScheduleList()) {

                    //List<ClassSchedule> classScheduleList = classScheduleRepository.findByScheduleDateGreaterThanEqualAndScheduleDateLessThanEqualAndDelActiveTrue(monday, sunday);
                    /**
                     * Nếu lớp đó có ngày ScheduleDate nhỏ hơn ngày của thứ 2 và lớn hơn ngày của chủ nhật
                     */
                    if (classSchedule.getScheduleDate().isBefore(monday) && classSchedule.getScheduleDate().isAfter(sunday)) {
//                    if (CollectionUtils.isEmpty(classScheduleList)) {
                        /**
                         * Tạo thời khóa biểu 1 lớp
                         */
                        scheduleResponse = new ScheduleResponse();
                        /**
                         *Tạo thời khóa biểu của buổi sáng
                         */
                        scheduleMorningDayRespone = new ScheduleDayRespone();
                        scheduleMorningDayRespone.setSessionDay("Sáng");
                        scheduleMorningDayRespone.setIdClass(maClass.getId());
                        scheduleMorningDayRespone.setClassName(maClass.getClassName());

                        /**
                         * Tạo thời khóa buổi chiều
                         */
                        scheduleAfternoonDayRespone = new ScheduleDayRespone();
                        scheduleAfternoonDayRespone.setSessionDay("Chiều");
                        scheduleAfternoonDayRespone.setIdClass(maClass.getId());
                        scheduleAfternoonDayRespone.setClassName(maClass.getClassName());
                        /**
                         * Tạo thời khóa biểu buổi tối
                         */
                        scheduleEveningDayRespone = new ScheduleDayRespone();
                        scheduleEveningDayRespone.setSessionDay("Tối");
                        scheduleEveningDayRespone.setIdClass(maClass.getId());
                        scheduleEveningDayRespone.setClassName(maClass.getClassName());

                        /**
                         *  Add Thời khóa biểu sáng,chiều , tối
                         */
                        List<ScheduleDayRespone> scheduleDayResponseList = new ArrayList<>();
                        scheduleDayResponseList.add(scheduleMorningDayRespone);
                        scheduleDayResponseList.add(scheduleAfternoonDayRespone);
                        scheduleDayResponseList.add(scheduleEveningDayRespone);
                        /**
                         *set cho đối tượng scheduleResponse
                         */
                        titleSchedule = classSchedule.getScheduleTitle();

                        scheduleResponse.setScheduleDayResponeList(scheduleDayResponseList);
                        scheduleResponse.setIdClass(maClass.getId());
                        scheduleResponse.setClassName(maClass.getClassName());
                        scheduleResponse.setMorningSaturday(maClass.isMorningSaturday());
                        scheduleResponse.setAfternoonSaturday(maClass.isAfternoonSaturday());
                        scheduleResponse.setEveningSaturday(maClass.isEveningSaturday());
                        scheduleResponse.setSunday(maClass.isSunday());
                        scheduleResponse.setScheduleTitle(titleSchedule);
                        scheduleResponseList.add(scheduleResponse);
                    } else {
                        /**
                         * Nếu thời khóa biểu có ngày trùng ngày thứ 2
                         */
                        if (classSchedule.getScheduleDate().isEqual(monday)) {
                            scheduleMorningDayRespone.setSessionDay("Sáng");
                            scheduleMorningDayRespone.setIdClass(maClass.getId());
                            scheduleMorningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleMorningList())) {
                                StringBuilder stringMorningMonday = new StringBuilder();
                                for (ScheduleMorning scheduleMorning : classSchedule.getScheduleMorningList()) {
                                    stringMorningMonday.append("*" + scheduleMorning.getTime() + "\n");
                                    stringMorningMonday.append(scheduleMorning.getContent() + "\n \n");

                                }
                                scheduleMorningDayRespone.setMonday(stringMorningMonday.toString());
                            }
                            scheduleAfternoonDayRespone.setSessionDay("Chiều");
                            scheduleAfternoonDayRespone.setIdClass(maClass.getId());
                            scheduleAfternoonDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleAfternoonList())) {
                                StringBuilder stringAfternoonMonday = new StringBuilder();
                                for (ScheduleAfternoon scheduleAfternoon : classSchedule.getScheduleAfternoonList()) {
                                    stringAfternoonMonday.append("*" + scheduleAfternoon.getTime() + "\n");
                                    stringAfternoonMonday.append(scheduleAfternoon.getContent() + "\n \n");
                                }
                                scheduleAfternoonDayRespone.setMonday(stringAfternoonMonday.toString());
                            }
                            scheduleEveningDayRespone.setSessionDay("Tối");
                            scheduleEveningDayRespone.setIdClass(maClass.getId());
                            scheduleEveningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleEveningList())) {
                                StringBuilder stringEveningMonday = new StringBuilder();
                                for (ScheduleEvening scheduleEvening : classSchedule.getScheduleEveningList()) {
                                    stringEveningMonday.append("*" + scheduleEvening.getTime() + "\n");
                                    stringEveningMonday.append(scheduleEvening.getContent() + "\n \n");
                                }
                                scheduleEveningDayRespone.setMonday(stringEveningMonday.toString());
                            }
                            titleSchedule = classSchedule.getScheduleTitle();
                        }
                        /**
                         * Nếu thời khóa biểu có ngày trùng ngày thứ 3
                         */
                        else if (classSchedule.getScheduleDate().isEqual(tuesday)) {
                            scheduleMorningDayRespone.setSessionDay("Sáng");
                            scheduleMorningDayRespone.setIdClass(maClass.getId());
                            scheduleMorningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleMorningList())) {
                                StringBuilder stringMorningTuesday = new StringBuilder();
                                for (ScheduleMorning scheduleMorning : classSchedule.getScheduleMorningList()) {
                                    stringMorningTuesday.append("*" + scheduleMorning.getTime() + "\n");
                                    stringMorningTuesday.append(scheduleMorning.getContent() + "\n \n");
                                }
                                scheduleMorningDayRespone.setTuesday(stringMorningTuesday.toString());
                            }
                            scheduleAfternoonDayRespone.setSessionDay("Chiều");
                            scheduleAfternoonDayRespone.setIdClass(maClass.getId());
                            scheduleAfternoonDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleAfternoonList())) {
                                StringBuilder stringAfternoonTuesday = new StringBuilder();
                                for (ScheduleAfternoon scheduleAfternoon : classSchedule.getScheduleAfternoonList()) {
                                    stringAfternoonTuesday.append("*" + scheduleAfternoon.getTime() + "\n");
                                    stringAfternoonTuesday.append(scheduleAfternoon.getContent() + "\n \n");
                                    /*scheduleMorningDayRespone.setTimeTuesday(scheduleAfternoon.getTime());
                                    scheduleMorningDayRespone.setContentTuesday(scheduleAfternoon.getContent());*/
                                }
                                scheduleAfternoonDayRespone.setTuesday(stringAfternoonTuesday.toString());
                            }
                            scheduleEveningDayRespone.setSessionDay("Tối");
                            scheduleEveningDayRespone.setIdClass(maClass.getId());
                            scheduleEveningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleEveningList())) {
                                StringBuilder stringEveningTuesday = new StringBuilder();
                                for (ScheduleEvening scheduleEvening : classSchedule.getScheduleEveningList()) {
                                    stringEveningTuesday.append("*" + scheduleEvening.getTime() + "\n");
                                    stringEveningTuesday.append(scheduleEvening.getContent() + "\n \n");
                                    /*scheduleMorningDayRespone.setTimeTuesday(scheduleEvening.getTime());
                                    scheduleMorningDayRespone.setContentTuesday(scheduleEvening.getContent());*/
                                }
                                scheduleEveningDayRespone.setTuesday(stringEveningTuesday.toString());
                            }
                            titleSchedule = classSchedule.getScheduleTitle();
                        }
                        /**
                         * Nếu thời khóa biểu có ngày trùng ngày thứ 4
                         */
                        else if (classSchedule.getScheduleDate().isEqual(wednesday)) {
                            scheduleMorningDayRespone.setSessionDay("Sáng");
                            scheduleMorningDayRespone.setIdClass(maClass.getId());
                            scheduleMorningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleMorningList())) {
                                StringBuilder stringMorningWednesday = new StringBuilder();
                                for (ScheduleMorning scheduleMorning : classSchedule.getScheduleMorningList()) {
                                    stringMorningWednesday.append("*" + scheduleMorning.getTime() + "\n");
                                    stringMorningWednesday.append(scheduleMorning.getContent() + "\n \n");
                                }
                                scheduleMorningDayRespone.setWednesday(stringMorningWednesday.toString());
                            }

                            scheduleAfternoonDayRespone.setSessionDay("Chiều");
                            scheduleAfternoonDayRespone.setIdClass(maClass.getId());
                            scheduleAfternoonDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleAfternoonList())) {
                                StringBuilder stringAfternoonWednesday = new StringBuilder();
                                for (ScheduleAfternoon scheduleAfternoon : classSchedule.getScheduleAfternoonList()) {
                                    stringAfternoonWednesday.append("*" + scheduleAfternoon.getTime() + "\n");
                                    stringAfternoonWednesday.append(scheduleAfternoon.getContent() + "\n \n");
                                }
                                scheduleAfternoonDayRespone.setWednesday(stringAfternoonWednesday.toString());
                            }
                            scheduleEveningDayRespone.setSessionDay("Tối");
                            scheduleEveningDayRespone.setIdClass(maClass.getId());
                            scheduleEveningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleEveningList())) {
                                StringBuilder stringEveningWednesday = new StringBuilder();
                                for (ScheduleEvening scheduleEvening : classSchedule.getScheduleEveningList()) {
                                    stringEveningWednesday.append("*" + scheduleEvening.getTime() + "\n");
                                    stringEveningWednesday.append(scheduleEvening.getContent() + "\n \n");
                                }
                                scheduleEveningDayRespone.setWednesday(stringEveningWednesday.toString());
                            }
                            titleSchedule = classSchedule.getScheduleTitle();

                        }
                        /**
                         * Nếu thời khóa biểu có ngày trùng ngày thứ 5
                         */
                        else if (classSchedule.getScheduleDate().isEqual(thursday)) {
                            scheduleMorningDayRespone.setSessionDay("Sáng");
                            scheduleMorningDayRespone.setIdClass(maClass.getId());
                            scheduleMorningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleMorningList())) {
                                StringBuilder stringMorningThursday = new StringBuilder();
                                for (ScheduleMorning scheduleMorning : classSchedule.getScheduleMorningList()) {
                                    stringMorningThursday.append("*" + scheduleMorning.getTime() + "\n");
                                    stringMorningThursday.append(scheduleMorning.getContent() + "\n \n");
                                }
                                scheduleMorningDayRespone.setThursday(stringMorningThursday.toString());
                            }
                            scheduleAfternoonDayRespone.setSessionDay("Chiều");
                            scheduleAfternoonDayRespone.setIdClass(maClass.getId());
                            scheduleAfternoonDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleAfternoonList())) {
                                StringBuilder stringAfternoonThursday = new StringBuilder();
                                for (ScheduleAfternoon scheduleAfternoon : classSchedule.getScheduleAfternoonList()) {
                                    stringAfternoonThursday.append("*" + scheduleAfternoon.getTime() + "\n");
                                    stringAfternoonThursday.append(scheduleAfternoon.getContent() + "\n \n");
                                }
                                scheduleAfternoonDayRespone.setThursday(stringAfternoonThursday.toString());
                            }
                            scheduleEveningDayRespone.setSessionDay("Tối");
                            scheduleEveningDayRespone.setIdClass(maClass.getId());
                            scheduleEveningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleEveningList())) {
                                StringBuilder stringEveningThursday = new StringBuilder();
                                for (ScheduleEvening scheduleEvening : classSchedule.getScheduleEveningList()) {
                                    stringEveningThursday.append("*" + scheduleEvening.getTime() + "\n");
                                    stringEveningThursday.append(scheduleEvening.getContent() + "\n \n");
                                }
                                scheduleEveningDayRespone.setThursday(stringEveningThursday.toString());

                            }
                            titleSchedule = classSchedule.getScheduleTitle();
                        }
                        /**
                         * Nếu thời khóa biểu có ngày trùng ngày thứ 6
                         */
                        else if (classSchedule.getScheduleDate().isEqual(friday)) {
                            scheduleMorningDayRespone.setSessionDay("Sáng");
                            scheduleMorningDayRespone.setIdClass(maClass.getId());
                            scheduleMorningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleMorningList())) {
                                StringBuilder stringMorningFriday = new StringBuilder();
                                for (ScheduleMorning scheduleMorning : classSchedule.getScheduleMorningList()) {
                                    stringMorningFriday.append("*" + scheduleMorning.getTime() + "\n");
                                    stringMorningFriday.append(scheduleMorning.getContent() + "\n \n");
                                }
                                scheduleMorningDayRespone.setFriday(stringMorningFriday.toString());
                            }
                            scheduleAfternoonDayRespone.setSessionDay("Chiều");
                            scheduleAfternoonDayRespone.setIdClass(maClass.getId());
                            scheduleAfternoonDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleAfternoonList())) {
                                StringBuilder stringAfternoonFriday = new StringBuilder();
                                for (ScheduleAfternoon scheduleAfternoon : classSchedule.getScheduleAfternoonList()) {
                                    stringAfternoonFriday.append("*" + scheduleAfternoon.getTime() + "\n");
                                    stringAfternoonFriday.append(scheduleAfternoon.getContent() + "\n \n");
                                }
                                scheduleAfternoonDayRespone.setFriday(stringAfternoonFriday.toString());
                            }
                            scheduleEveningDayRespone.setSessionDay("Tối");
                            scheduleEveningDayRespone.setIdClass(maClass.getId());
                            scheduleEveningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleEveningList())) {
                                StringBuilder stringEveningFriday = new StringBuilder();
                                for (ScheduleEvening scheduleEvening : classSchedule.getScheduleEveningList()) {
                                    stringEveningFriday.append(scheduleEvening.getTime() + "\n");
                                    stringEveningFriday.append(scheduleEvening.getContent() + "\n \n");
                                }
                                scheduleEveningDayRespone.setFriday(stringEveningFriday.toString());
                            }
                            titleSchedule = classSchedule.getScheduleTitle();
                        }
                        /**
                         * Nếu thời khóa biểu có ngày trùng ngày thứ 7
                         */
                        else if (classSchedule.getScheduleDate().isEqual(saturday)) {
                            scheduleMorningDayRespone.setSessionDay("Sáng");
                            scheduleMorningDayRespone.setIdClass(maClass.getId());
                            scheduleMorningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleMorningList())) {
                                StringBuilder stringMorningSaturday = new StringBuilder();
                                for (ScheduleMorning scheduleMorning : classSchedule.getScheduleMorningList()) {
                                    stringMorningSaturday.append("*" + scheduleMorning.getTime() + "\n");
                                    stringMorningSaturday.append(scheduleMorning.getContent() + "\n \n");
                                    /*scheduleMorningDayRespone.setTimeSaturday(scheduleMorning.getTime());
                                    scheduleMorningDayRespone.setContentSaturday(scheduleMorning.getContent());*/
                                }
                                scheduleMorningDayRespone.setSaturday(stringMorningSaturday.toString());
                            }
                            scheduleAfternoonDayRespone.setSessionDay("Chiều");
                            scheduleAfternoonDayRespone.setIdClass(maClass.getId());
                            scheduleAfternoonDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleAfternoonList())) {
                                StringBuilder stringAfternoonSaturday = new StringBuilder();
                                for (ScheduleAfternoon scheduleAfternoon : classSchedule.getScheduleAfternoonList()) {
                                    stringAfternoonSaturday.append("*" + scheduleAfternoon.getTime() + "\n");
                                    stringAfternoonSaturday.append(scheduleAfternoon.getContent() + "\n \n");
                                }
                                scheduleAfternoonDayRespone.setSaturday(stringAfternoonSaturday.toString());
                            }
                            scheduleEveningDayRespone.setSessionDay("Tối");
                            scheduleEveningDayRespone.setIdClass(maClass.getId());
                            scheduleEveningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleEveningList())) {
                                StringBuilder stringEveningSaturday = new StringBuilder();
                                for (ScheduleEvening scheduleEvening : classSchedule.getScheduleEveningList()) {
                                    stringEveningSaturday.append("*" + scheduleEvening.getTime() + "\n");
                                    stringEveningSaturday.append(scheduleEvening.getContent() + "\n \n");
                                }
                                scheduleEveningDayRespone.setSaturday(stringEveningSaturday.toString());
                            }
                            titleSchedule = classSchedule.getScheduleTitle();
                        }
                        /**
                         * Nếu thời khóa biểu có ngày trùng ngày CN
                         */
                        else if (classSchedule.getScheduleDate().isEqual(sunday)) {
                            scheduleMorningDayRespone.setSessionDay("Sáng");
                            scheduleMorningDayRespone.setIdClass(maClass.getId());
                            scheduleMorningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleMorningList())) {
                                StringBuilder stringMorningSunday = new StringBuilder();
                                for (ScheduleMorning scheduleMorning : classSchedule.getScheduleMorningList()) {
                                    stringMorningSunday.append("*" + scheduleMorning.getTime() + "\n");
                                    stringMorningSunday.append(scheduleMorning.getContent() + "\n \n");
                                }
                                scheduleMorningDayRespone.setSunday(stringMorningSunday.toString());
                            }
                            scheduleAfternoonDayRespone.setSessionDay("Chiều");
                            scheduleAfternoonDayRespone.setIdClass(maClass.getId());
                            scheduleAfternoonDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleAfternoonList())) {
                                StringBuilder stringAfternoonSunday = new StringBuilder();
                                for (ScheduleAfternoon scheduleAfternoon : classSchedule.getScheduleAfternoonList()) {
                                    stringAfternoonSunday.append("*" + scheduleAfternoon.getTime() + "\n");
                                    stringAfternoonSunday.append(scheduleAfternoon.getContent() + "\n \n");
                                }
                                scheduleAfternoonDayRespone.setSunday(stringAfternoonSunday.toString());
                            }
                            scheduleEveningDayRespone.setSessionDay("Tối");
                            scheduleEveningDayRespone.setIdClass(maClass.getId());
                            scheduleEveningDayRespone.setClassName(maClass.getClassName());
                            if (!CollectionUtils.isEmpty(classSchedule.getScheduleEveningList())) {
                                StringBuilder stringEveningSunday = new StringBuilder();
                                for (ScheduleEvening scheduleEvening : classSchedule.getScheduleEveningList()) {
                                    stringEveningSunday.append("*" + scheduleEvening.getTime() + "\n");
                                    stringEveningSunday.append(scheduleEvening.getContent() + "\n \n");
                                }
                                scheduleEveningDayRespone.setSunday(stringEveningSunday.toString());
                            }
                            titleSchedule = classSchedule.getScheduleTitle();
                        }
                    }
                }
                List<ScheduleDayRespone> scheduleDayResponseList = new ArrayList<>();
                scheduleMorningDayRespone.setSessionDay("Sáng");
                scheduleMorningDayRespone.setIdClass(maClass.getId());
                scheduleMorningDayRespone.setClassName(maClass.getClassName());

                scheduleDayResponseList.add(scheduleMorningDayRespone);
                scheduleAfternoonDayRespone.setSessionDay("Chiều");
                scheduleAfternoonDayRespone.setIdClass(maClass.getId());
                scheduleAfternoonDayRespone.setClassName(maClass.getClassName());

                scheduleDayResponseList.add(scheduleAfternoonDayRespone);
                scheduleEveningDayRespone.setSessionDay("Tối");
                scheduleEveningDayRespone.setIdClass(maClass.getId());
                scheduleEveningDayRespone.setClassName(maClass.getClassName());

                scheduleDayResponseList.add(scheduleEveningDayRespone);
                scheduleResponse.setScheduleDayResponeList(scheduleDayResponseList);
                scheduleResponse.setIdClass(maClass.getId());
                scheduleResponse.setClassName(maClass.getClassName());
                scheduleResponse.setMorningSaturday(maClass.isMorningSaturday());
                scheduleResponse.setAfternoonSaturday(maClass.isAfternoonSaturday());
                scheduleResponse.setEveningSaturday(maClass.isEveningSaturday());
                scheduleResponse.setSunday(maClass.isSunday());
                scheduleResponse.setScheduleTitle(titleSchedule);
                scheduleResponseList.add(scheduleResponse);
            } else {

                ScheduleResponse scheduleResponse = new ScheduleResponse();
                ScheduleDayRespone scheduleMorningDayRespone = new ScheduleDayRespone();
                scheduleMorningDayRespone.setSessionDay("Sáng");
                scheduleMorningDayRespone.setIdClass(maClass.getId());
                scheduleMorningDayRespone.setClassName(maClass.getClassName());

                ScheduleDayRespone scheduleAfternoonDayRespone = new ScheduleDayRespone();
                scheduleAfternoonDayRespone.setSessionDay("Chiều");
                scheduleAfternoonDayRespone.setIdClass(maClass.getId());
                scheduleAfternoonDayRespone.setClassName(maClass.getClassName());

                ScheduleDayRespone scheduleEveningDayRespone = new ScheduleDayRespone();
                scheduleEveningDayRespone.setSessionDay("Tối");
                scheduleEveningDayRespone.setIdClass(maClass.getId());
                scheduleEveningDayRespone.setClassName(maClass.getClassName());

                List<ScheduleDayRespone> scheduleDayResponseList = new ArrayList<>();
                scheduleDayResponseList.add(scheduleMorningDayRespone);
                scheduleDayResponseList.add(scheduleAfternoonDayRespone);
                scheduleDayResponseList.add(scheduleEveningDayRespone);
                scheduleResponse.setScheduleDayResponeList(scheduleDayResponseList);
                scheduleResponse.setIdClass(maClass.getId());
                scheduleResponse.setMorningSaturday(maClass.isMorningSaturday());
                scheduleResponse.setAfternoonSaturday(maClass.isAfternoonSaturday());
                scheduleResponse.setEveningSaturday(maClass.isEveningSaturday());
                scheduleResponse.setSunday(maClass.isSunday());
                scheduleResponse.setClassName(maClass.getClassName());
//                scheduleResponse.setScheduleTitle(titleSchedule);
                scheduleResponseList.add(scheduleResponse);
            }
        }
        return scheduleResponseList;
    }

    @Override
    public List<ScheduleInClassWeekResponse> findAllScheduleInClassWeekByTimeSchedule(Long idSchool, SearchScheduleInClassRequest searchScheduleInClassRequest) {
        /**
         * Tìm lớp theo id Class
         */
        Optional<MaClass> maClassOptional = maClassRepository.findByIdAndIdSchoolAndDelActiveTrue(searchScheduleInClassRequest.getIdClass(), idSchool);
        if (maClassOptional.isEmpty()) {
            return null;
        }

        /**
         * Tạo list các thời khóa biểu tuần trong 1 lớp
         */
        List<ScheduleInClassWeekResponse> scheduleInClassWeekResponseList = new ArrayList<>();

        /**
         * Khai báo thời khóa biểu của từng thứ trong 1 lớp
         */
        List<ScheduleInClassResponse> scheduleInClassMondayResponseList = new ArrayList<>();//Thứ 2
        List<ScheduleInClassResponse> scheduleInClassTuesdayResponseList = new ArrayList<>();//Thứ 3
        List<ScheduleInClassResponse> scheduleInClassWednesdayResponseList = new ArrayList<>();//Thứ 4
        List<ScheduleInClassResponse> scheduleInClassThursdayResponseList = new ArrayList<>();//Thứ 5
        List<ScheduleInClassResponse> scheduleInClassFridayResponseList = new ArrayList<>();//Thứ 6
        List<ScheduleInClassResponse> scheduleInClassSaturdayResponseList = new ArrayList<>();//Thứ 7
        List<ScheduleInClassResponse> scheduleInClassSundayResponseList = new ArrayList<>();//CN

        LocalDate monday = null;
        LocalDate tuesday = null;
        LocalDate wednesday = null;
        LocalDate thursday = null;
        LocalDate friday = null;
        LocalDate saturday = null;
        LocalDate sunday = null;

        /**
         * Truyền vào giá trị thời gian ngày đầu tuần parse sang Localdate
         */
        if (StringUtils.isNotBlank(searchScheduleInClassRequest.getTimeSchedule())) {
            monday = LocalDate.parse(searchScheduleInClassRequest.getTimeSchedule());
            tuesday = monday.plusDays(1);
            wednesday = tuesday.plusDays(1);
            thursday = wednesday.plusDays(1);
            friday = thursday.plusDays(1);
            saturday = friday.plusDays(1);
            sunday = saturday.plusDays(1);
        }
        MaClass maClass = maClassOptional.get();
        /**
         * Nếu lớp đó có thời khóa biểu
         */
        if (!CollectionUtils.isEmpty(maClass.getClassScheduleList())) {
            /**
             * Chạy vòng lặp các thời khóa biểu trong lớp
             */
            LocalDate finalMonday = monday;
            LocalDate finalTuesday = tuesday;
            LocalDate finalWednesday = wednesday;
            LocalDate finalThursday = thursday;
            LocalDate finalFriday = friday;
            LocalDate finalSaturday = saturday;
            LocalDate finalSunday = sunday;
            /**
             * Lọc ra thời khóa biểu thứ 2 của lớp đó
             */
            List<ClassSchedule> classScheduleMondayList = maClass.getClassScheduleList().stream().filter(x -> finalMonday.isEqual(x.getScheduleDate())).collect(Collectors.toList());

            /**
             * Lọc ra thời khóa biểu thứ 3 của lớp đó
             */
            List<ClassSchedule> classScheduleTuesdayList = maClass.getClassScheduleList().stream().filter(x -> finalTuesday.isEqual(x.getScheduleDate())).collect(Collectors.toList());

            /**
             * Lọc ra thời khóa biểu thứ 4 của lớp đó
             */
            List<ClassSchedule> classScheduleWednesdayList = maClass.getClassScheduleList().stream().filter(x -> finalWednesday.isEqual(x.getScheduleDate())).collect(Collectors.toList());

            /**
             * Lọc ra thời khóa biểu thứ 5 của lớp đó
             */
            List<ClassSchedule> classScheduleThursdayList = maClass.getClassScheduleList().stream().filter(x -> finalThursday.isEqual(x.getScheduleDate())).collect(Collectors.toList());

            /**
             * Lọc ra thời khóa biểu thứ 6 của lớp đó
             */
            List<ClassSchedule> classScheduleFridayList = maClass.getClassScheduleList().stream().filter(x -> finalFriday.isEqual(x.getScheduleDate())).collect(Collectors.toList());

            /**
             * Lọc ra thời khóa biểu thứ 7 của lớp đó
             */
            List<ClassSchedule> classScheduleSaturdayList = maClass.getClassScheduleList().stream().filter(x -> finalSaturday.isEqual(x.getScheduleDate())).collect(Collectors.toList());

            /**
             * Lọc ra thời khóa biểu CN của lớp đó
             */
            List<ClassSchedule> classScheduleSundayList = maClass.getClassScheduleList().stream().filter(x -> finalSunday.isEqual(x.getScheduleDate())).collect(Collectors.toList());


            /**
             * Nếu thời khóa biểu thứ 2 không có
             */
            if (CollectionUtils.isEmpty(classScheduleMondayList)) {
                /**
                 * Tạo các buổi cho thứ 2
                 */
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ 2
                 */
                scheduleInClassMondayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassMondayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassMondayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassMondayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassMondayWeekResponse.setScheduleInClassResponseList(scheduleInClassMondayResponseList);
                scheduleInClassMondayWeekResponse.setScheduleDate(monday);
                scheduleInClassMondayWeekResponse.setIdClass(maClass.getId());

                //scheduleInClassMondayWeekResponse.setIdSchedule();
                /**
                 * Add thứ 2 vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassMondayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ 2 có
             */
            else {
                /**
                 * Thời khóa biểu thứ 2
                 */
                ClassSchedule scheduleMonday = classScheduleMondayList.get(0);
                /**
                 * Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 2(lấy từ class_schedule)
                 */
                List<ScheduleMorning> scheduleMorningList = scheduleMonday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleMonday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleMonday.getScheduleEveningList();


                /**
                 * Nếu thứ 2 đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                    scheduleInClassMorningResponse.setTimeSchedule(scheduleMonday.getScheduleDate().toString());
                    scheduleInClassMondayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleMonday.getScheduleMorningList()) {
                        /**
                         * Thời khóa biểu buổi sáng,chiều,tối
                         */
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleMonday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassMondayResponseList.add(scheduleInClassMorningResponse);
                    }
                }

                /**
                 * Nếu thứ 2 đó không có thời khóa biểu buổi chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleMonday.getScheduleDate().toString());
                    scheduleInClassMondayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleMonday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleMonday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassMondayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }

                /**
                 * Nếu thứ 2 đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleMonday.getScheduleDate().toString());
                    scheduleInClassMondayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleMonday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleMonday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassMondayResponseList.add(scheduleInClassEveningResponse);
                    }
                }

                ScheduleInClassWeekResponse scheduleInClassMondayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassMondayWeekResponse.setScheduleInClassResponseList(scheduleInClassMondayResponseList);
                scheduleInClassMondayWeekResponse.setIdSchedule(scheduleMonday.getId());
                scheduleInClassMondayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassMondayWeekResponse.setScheduleTitle(scheduleMonday.getScheduleTitle());

                scheduleInClassMondayWeekResponse.setScheduleDate(monday);

                scheduleInClassWeekResponseList.add(scheduleInClassMondayWeekResponse);
            }


            /**
             * Nếu thời khóa biểu thứ 3 không có
             */
            if (CollectionUtils.isEmpty(classScheduleTuesdayList)) {
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                /**
                 * Tạo các buổi cho thứ 3
                 */
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ 3
                 */
                scheduleInClassTuesdayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassTuesdayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassTuesdayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassTuesdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassTuesdayWeekResponse.setScheduleInClassResponseList(scheduleInClassTuesdayResponseList);
                scheduleInClassTuesdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassTuesdayWeekResponse.setScheduleDate(tuesday);
                /**
                 * Add thứ 3 vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassTuesdayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ 3 có
             */
            else {
                /**
                 * Thời khóa biểu thứ 3
                 */
                ClassSchedule scheduleTuesday = classScheduleTuesdayList.get(0);
                /**
                 * Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 2(lấy từ class_schedule)
                 */
                List<ScheduleMorning> scheduleMorningList = scheduleTuesday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleTuesday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleTuesday.getScheduleEveningList();

                /**
                 * Nếu thứ 3 đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                    scheduleInClassMorningResponse.setTimeSchedule(scheduleTuesday.getScheduleDate().toString());
                    scheduleInClassTuesdayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleTuesday.getScheduleMorningList()) {
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleTuesday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassTuesdayResponseList.add(scheduleInClassMorningResponse);
                    }
                }

                /**
                 * Nếu thứ 3 đó không có thời khóa biểu buổi chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleTuesday.getScheduleDate().toString());
                    scheduleInClassTuesdayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleTuesday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleTuesday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassTuesdayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }

                /**
                 * Nếu thứ 3 đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleTuesday.getScheduleDate().toString());
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassTuesdayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleTuesday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleTuesday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassTuesdayResponseList.add(scheduleInClassEveningResponse);
                    }
                }

                ScheduleInClassWeekResponse scheduleInClassTuesdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassTuesdayWeekResponse.setScheduleInClassResponseList(scheduleInClassTuesdayResponseList);
                scheduleInClassTuesdayWeekResponse.setIdSchedule(scheduleTuesday.getId());
                scheduleInClassTuesdayWeekResponse.setScheduleDate(tuesday);
                scheduleInClassTuesdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassTuesdayWeekResponse.setScheduleTitle(scheduleTuesday.getScheduleTitle());

                scheduleInClassWeekResponseList.add(scheduleInClassTuesdayWeekResponse);
            }

            /**
             * Nếu thời khóa biểu thứ 4 không có
             */
            if (CollectionUtils.isEmpty(classScheduleWednesdayList)) {
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                /**
                 * Tạo các buổi cho thứ 4
                 */
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());

                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());


                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ 4
                 */
                scheduleInClassWednesdayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassWednesdayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassWednesdayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassWednesdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassWednesdayWeekResponse.setScheduleInClassResponseList(scheduleInClassWednesdayResponseList);
                scheduleInClassWednesdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassWednesdayWeekResponse.setScheduleDate(wednesday);
                /**
                 * Add thứ 4 vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassWednesdayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ 4 có
             */
            else {
                /**
                 *Thời khóa biểu thứ 4
                 */
                ClassSchedule scheduleWenesday = classScheduleWednesdayList.get(0);
                /**
                 * Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 4(lấy từ class_schedule)
                 */
                List<ScheduleMorning> scheduleMorningList = scheduleWenesday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleWenesday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleWenesday.getScheduleEveningList();

                /**
                 * Nếu thứ 4 đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());

                    scheduleInClassMorningResponse.setTimeSchedule(scheduleWenesday.getScheduleDate().toString());
                    scheduleInClassWednesdayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleWenesday.getScheduleMorningList()) {
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleWenesday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassWednesdayResponseList.add(scheduleInClassMorningResponse);
                    }
                }

                /**
                 * Nếu thứ 4 đó không có thời khóa biểu buổi chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleWenesday.getScheduleDate().toString());
                    scheduleInClassWednesdayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleWenesday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleWenesday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassWednesdayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }

                /**
                 * Nếu thứ 4 đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleWenesday.getScheduleDate().toString());
                    scheduleInClassWednesdayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleWenesday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleWenesday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassWednesdayResponseList.add(scheduleInClassEveningResponse);
                    }
                }

                ScheduleInClassWeekResponse scheduleInClassWednesdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassWednesdayWeekResponse.setScheduleInClassResponseList(scheduleInClassWednesdayResponseList);
                scheduleInClassWednesdayWeekResponse.setIdSchedule(scheduleWenesday.getId());
                scheduleInClassWednesdayWeekResponse.setScheduleDate(wednesday);
                scheduleInClassWednesdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassWednesdayWeekResponse.setScheduleTitle(scheduleWenesday.getScheduleTitle());


                scheduleInClassWeekResponseList.add(scheduleInClassWednesdayWeekResponse);
            }

            /**
             * Nếu thời khóa biểu thứ 5 không có
             */
            if (CollectionUtils.isEmpty(classScheduleThursdayList)) {
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                /**
                 * Tạo các buổi cho thứ 5
                 */
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ 5
                 */
                scheduleInClassThursdayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassThursdayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassThursdayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassThursdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassThursdayWeekResponse.setScheduleInClassResponseList(scheduleInClassThursdayResponseList);
                scheduleInClassThursdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassThursdayWeekResponse.setScheduleDate(thursday);
                /**
                 * Add thứ 5 vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassThursdayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ 5 có
             */
            else {
                //Thời khóa biểu thứ 5
                ClassSchedule scheduleThursday = classScheduleThursdayList.get(0);
                //Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 2(lấy từ class_schedule)
                List<ScheduleMorning> scheduleMorningList = scheduleThursday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleThursday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleThursday.getScheduleEveningList();

                /**
                 * Nếu thứ 5 đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                    scheduleInClassMorningResponse.setTimeSchedule(scheduleThursday.getScheduleDate().toString());
                    scheduleInClassThursdayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleThursday.getScheduleMorningList()) {
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleThursday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassThursdayResponseList.add(scheduleInClassMorningResponse);
                    }
                }

                /**
                 * Nếu thứ 5 đó không có thời khóa biểu buổi chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleThursday.getScheduleDate().toString());
                    scheduleInClassThursdayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleThursday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleThursday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassThursdayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }
                /**
                 * Nếu thứ 5 đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleThursday.getScheduleDate().toString());
                    scheduleInClassThursdayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleThursday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleThursday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassThursdayResponseList.add(scheduleInClassEveningResponse);
                    }
                }
                ScheduleInClassWeekResponse scheduleInClassThursdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassThursdayWeekResponse.setScheduleInClassResponseList(scheduleInClassThursdayResponseList);
                scheduleInClassThursdayWeekResponse.setScheduleDate(thursday);
                scheduleInClassThursdayWeekResponse.setIdSchedule(scheduleThursday.getId());
                scheduleInClassThursdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassThursdayWeekResponse.setScheduleTitle(scheduleThursday.getScheduleTitle());


                scheduleInClassWeekResponseList.add(scheduleInClassThursdayWeekResponse);
            }

            /**
             * Nếu thời khóa biểu thứ 6 không có
             */
            if (CollectionUtils.isEmpty(classScheduleFridayList)) {
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                /**
                 * Tạo các buổi cho thứ 6
                 */
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ 6
                 */
                scheduleInClassFridayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassFridayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassFridayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassFridayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassFridayWeekResponse.setScheduleInClassResponseList(scheduleInClassFridayResponseList);
                scheduleInClassFridayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassFridayWeekResponse.setScheduleDate(friday);
                /**
                 * Add thứ 6 vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassFridayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ 6 có
             */
            else {
                /**
                 * Thời khóa biểu thứ 6
                 */
                ClassSchedule scheduleFriday = classScheduleFridayList.get(0);
                //Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 2(lấy từ class_schedule)
                List<ScheduleMorning> scheduleMorningList = scheduleFriday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleFriday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleFriday.getScheduleEveningList();

                /**
                 * Nếu thứ 6 đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                    scheduleInClassMorningResponse.setTimeSchedule(scheduleFriday.getScheduleDate().toString());
                    scheduleInClassFridayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleFriday.getScheduleMorningList()) {
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleFriday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassFridayResponseList.add(scheduleInClassMorningResponse);
                    }
                }
                /**
                 * Nếu thứ 6 đó không có thời khóa biểu buổi chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleFriday.getScheduleDate().toString());
                    scheduleInClassFridayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleFriday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleFriday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassFridayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }

                /**
                 * Nếu thứ 6 đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleFriday.getScheduleDate().toString());
                    scheduleInClassFridayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleFriday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleFriday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassFridayResponseList.add(scheduleInClassEveningResponse);
                    }
                }

                ScheduleInClassWeekResponse scheduleInClassFridayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassFridayWeekResponse.setScheduleInClassResponseList(scheduleInClassFridayResponseList);
                scheduleInClassFridayWeekResponse.setIdSchedule(scheduleFriday.getId());
                scheduleInClassFridayWeekResponse.setScheduleDate(friday);
                scheduleInClassFridayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassFridayWeekResponse.setScheduleTitle(scheduleFriday.getScheduleTitle());

                scheduleInClassWeekResponseList.add(scheduleInClassFridayWeekResponse);
            }

            /**
             * Nếu thời khóa biểu thứ 7 không có
             */
            if (CollectionUtils.isEmpty(classScheduleSaturdayList)) {
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                /**
                 * Tạo các buổi cho thứ 7
                 */
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ 7
                 */
                scheduleInClassSaturdayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassSaturdayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassSaturdayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassSaturdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassSaturdayWeekResponse.setScheduleInClassResponseList(scheduleInClassSaturdayResponseList);
                scheduleInClassSaturdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassSaturdayWeekResponse.setScheduleDate(saturday);
                /**
                 * Add thứ 7 vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassSaturdayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ 7 có
             */
            else {
                /**
                 * Thời khóa biểu thứ 7
                 */
                ClassSchedule scheduleSaturday = classScheduleSaturdayList.get(0);
                //Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 7(lấy từ class_schedule)
                List<ScheduleMorning> scheduleMorningList = scheduleSaturday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleSaturday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleSaturday.getScheduleEveningList();


                /**
                 * Nếu thứ 7 đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                    scheduleInClassMorningResponse.setTimeSchedule(scheduleSaturday.getScheduleDate().toString());
                    scheduleInClassSaturdayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleSaturday.getScheduleMorningList()) {
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleSaturday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassSaturdayResponseList.add(scheduleInClassMorningResponse);
                    }
                }
                /**
                 * Nếu thứ 7 đó không có thời khóa biểu buổi Chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleSaturday.getScheduleDate().toString());
                    scheduleInClassSaturdayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleSaturday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleSaturday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassSaturdayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }

                /**
                 * Nếu thứ 7 đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleSaturday.getScheduleDate().toString());
                    scheduleInClassSaturdayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleSaturday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleSaturday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassSaturdayResponseList.add(scheduleInClassEveningResponse);
                    }
                }

                ScheduleInClassWeekResponse scheduleInClassSaturdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassSaturdayWeekResponse.setScheduleInClassResponseList(scheduleInClassSaturdayResponseList);
                scheduleInClassSaturdayWeekResponse.setScheduleDate(saturday);
                scheduleInClassSaturdayWeekResponse.setIdSchedule(scheduleSaturday.getId());
                scheduleInClassSaturdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassSaturdayWeekResponse.setScheduleTitle(scheduleSaturday.getScheduleTitle());

                scheduleInClassWeekResponseList.add(scheduleInClassSaturdayWeekResponse);
            }

            /**
             * Nếu thời khóa biểu thứ CN không có
             */
            if (CollectionUtils.isEmpty(classScheduleSundayList)) {
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                /**
                 * Tạo các buổi cho thứ CN
                 */
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ CN
                 */
                scheduleInClassSundayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassSundayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassSundayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassSundayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassSundayWeekResponse.setScheduleInClassResponseList(scheduleInClassSundayResponseList);
                scheduleInClassSundayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassSundayWeekResponse.setScheduleDate(sunday);
                /**
                 * Add thứ CN vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassSundayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ CN có
             */
            else {
                /**
                 * Thời khóa biểu thứ CN
                 */
                ClassSchedule scheduleSunday = classScheduleSundayList.get(0);
                //Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 7(lấy từ class_schedule)
                List<ScheduleMorning> scheduleMorningList = scheduleSunday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleSunday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleSunday.getScheduleEveningList();

                /**
                 * Nếu thứ CN đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                    scheduleInClassMorningResponse.setTimeSchedule(scheduleSunday.getScheduleDate().toString());
                    scheduleInClassSundayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleSunday.getScheduleMorningList()) {
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleSunday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassSundayResponseList.add(scheduleInClassMorningResponse);
                    }
                }

                /**
                 * Nếu thứ CN đó không có thời khóa biểu buổi chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleSunday.getScheduleDate().toString());
                    scheduleInClassSundayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleSunday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleSunday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassSundayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }

                /**
                 * Nếu thứ CN đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleSunday.getScheduleDate().toString());
                    scheduleInClassSundayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleSunday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleSunday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassSundayResponseList.add(scheduleInClassEveningResponse);
                    }
                }

                ScheduleInClassWeekResponse scheduleInClassSundayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassSundayWeekResponse.setScheduleInClassResponseList(scheduleInClassSundayResponseList);
                scheduleInClassSundayWeekResponse.setScheduleDate(sunday);
                scheduleInClassSundayWeekResponse.setIdSchedule(scheduleSunday.getId());
                scheduleInClassSundayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassSundayWeekResponse.setScheduleTitle(scheduleSunday.getScheduleTitle());
                scheduleInClassWeekResponseList.add(scheduleInClassSundayWeekResponse);
            }
        }
        /**
         * Nếu lớp đó không có thời khóa biểu
         */
        else {
            /**
             * Tạo các buổi cho thứ 2
             */
            ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());

            /**
             * Add Các buổi vào thứ 2
             */
            scheduleInClassMondayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassMondayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassMondayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassMondayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassMondayWeekResponse.setScheduleInClassResponseList(scheduleInClassMondayResponseList);
            scheduleInClassMondayWeekResponse.setScheduleDate(monday);
            scheduleInClassMondayWeekResponse.setIdClass(maClass.getId());
            /**
             * Add thứ 2 vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassMondayWeekResponse);


            /**
             * Tạo các buổi cho thứ 3
             */
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());
            /**
             * Add Các buổi vào thứ 3
             */
            scheduleInClassTuesdayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassTuesdayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassTuesdayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassTuesdayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassTuesdayWeekResponse.setScheduleInClassResponseList(scheduleInClassTuesdayResponseList);
            scheduleInClassTuesdayWeekResponse.setScheduleDate(tuesday);
            scheduleInClassTuesdayWeekResponse.setIdClass(maClass.getId());
            /**
             * Add thứ 3 vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassTuesdayWeekResponse);

            /**
             * Tạo các buổi cho thứ 4
             */
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());
            /**
             * Add Các buổi vào thứ 4
             */
            scheduleInClassWednesdayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassWednesdayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassWednesdayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassWednesdayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassWednesdayWeekResponse.setScheduleInClassResponseList(scheduleInClassWednesdayResponseList);
            scheduleInClassWednesdayWeekResponse.setScheduleDate(wednesday);
            scheduleInClassWednesdayWeekResponse.setIdClass(maClass.getId());
            /**
             * Add thứ 4 vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassWednesdayWeekResponse);

            /**
             * Tạo các buổi cho thứ 5
             */
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());

            /**
             * Add Các buổi vào thứ 5
             */
            scheduleInClassThursdayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassThursdayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassThursdayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassThursdayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassThursdayWeekResponse.setScheduleInClassResponseList(scheduleInClassThursdayResponseList);
            scheduleInClassThursdayWeekResponse.setScheduleDate(thursday);
            scheduleInClassThursdayWeekResponse.setIdClass(maClass.getId());
            /**
             * Add thứ 5 vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassThursdayWeekResponse);

            /**
             * Tạo các buổi cho thứ 6
             */
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());
            /**
             * Add Các buổi vào thứ 6
             */
            scheduleInClassFridayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassFridayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassFridayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassFridayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassFridayWeekResponse.setScheduleInClassResponseList(scheduleInClassFridayResponseList);
            scheduleInClassFridayWeekResponse.setScheduleDate(friday);
            scheduleInClassFridayWeekResponse.setIdClass(maClass.getId());
            /**
             * Add thứ 6 vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassFridayWeekResponse);

            /**
             * Tạo các buổi cho thứ 7
             */
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());
            /**
             * Add Các buổi vào thứ 7
             */
            scheduleInClassSaturdayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassSaturdayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassSaturdayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassSaturdayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassSaturdayWeekResponse.setScheduleInClassResponseList(scheduleInClassSaturdayResponseList);
            scheduleInClassSaturdayWeekResponse.setScheduleDate(saturday);
            scheduleInClassSaturdayWeekResponse.setIdClass(maClass.getId());
            /**
             * Add thứ 7 vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassSaturdayWeekResponse);

            /**
             * Tạo các buổi cho thứ CN
             */
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());
            /**
             * Add Các buổi vào thứ CN
             */
            scheduleInClassSundayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassSundayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassSundayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassSundayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassSundayWeekResponse.setScheduleInClassResponseList(scheduleInClassSundayResponseList);
            scheduleInClassSundayWeekResponse.setScheduleDate(sunday);
            scheduleInClassSundayWeekResponse.setIdClass(maClass.getId());
//            scheduleInClassSundayWeekResponse.setScheduleTitle(maClass.getS);

            /**
             * Add thứ CN vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassSundayWeekResponse);
        }

        return scheduleInClassWeekResponseList;
    }

    @Override
    public List<ExcelResponse> findAllScheduleInClassWeekByTimeScheduleNew(Long idSchool, SearchScheduleInClassRequest searchScheduleInClassRequest) {
        /**
         * Tìm lớp theo id Class
         */
        Optional<MaClass> maClassOptional = maClassRepository.findByIdAndIdSchoolAndDelActiveTrue(searchScheduleInClassRequest.getIdClass(), idSchool);
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        List<ExcelData> bodyList = new ArrayList<>();
        SchoolResponse schoolResponse = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
        assert schoolResponse != null;

        //set endDate từ currentDate + 7
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.parse(searchScheduleInClassRequest.getTimeSchedule(), formatter);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate endDate = currentDate.plusWeeks(1).minusDays(1);
        // get số tuần từ thư viện
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = currentDate.get(weekFields.weekOfWeekBasedYear());
        // fomat kiểu date
        String dateToStr = df.format(currentDate);
        String dateToStrSheet = df.format(endDate);

        List<ScheduleInClassWeekResponse> scheduleInClassWeekResponseList = this.setScheduleInClassWeekResponse(searchScheduleInClassRequest, maClassOptional);
        // GET CHỦ ĐỀ
        String titleSchedule = scheduleInClassWeekResponseList.get(0).getScheduleTitle();

        List<String> headerStringList = Arrays.asList("KẾ HOẠCH GIẢNG DẠY/ WEEKLY PLAN", AppConstant.EXCEL_SCHOOL.concat(schoolResponse.getSchoolName()), AppConstant.EXCEL_CLASS.concat(maClassOptional.get().getClassName()), AppConstant.EXCEL_TIME.concat("Tuần " + weekNumber + " (" + dateToStr + "-" + dateToStrSheet + ")"), "Chủ đề: "+titleSchedule);
        List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
        response.setSheetName("Tuần_ " + weekNumber);
        response.setHeaderList(headerList);

        // Tách dữ liệu, lấy số row cho các buổi sáng, chiều, tối, trả về morningList, afternoonList....
        Map<String, Integer> map = this.getRowMax(scheduleInClassWeekResponseList);
        int rowMorning = 0;
        int rowAfternoon = 0;
        int rowEvening = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("Sáng")) {
                rowMorning = entry.getValue();
            } else if (entry.getKey().equalsIgnoreCase("Chiều")) {
                rowAfternoon = entry.getValue();
            } else if (entry.getKey().equalsIgnoreCase("Tối")) {
                rowEvening = entry.getValue();
            }
        }
        List<ScheduleModel> listScheduleModel = this.getDays(scheduleInClassWeekResponseList, rowMorning, rowAfternoon, rowEvening);

        for (int i = 0; i < rowMorning; i++) {
            List<String> bodyStringList = Arrays.asList((i == 0) ? "Buổi sáng/Morning" : "", listScheduleModel.get(i).getContentMonday(), listScheduleModel.get(i).getContentTuesday(), listScheduleModel.get(i).getContentWednesday(), listScheduleModel.get(i).getContentThursday(),
                    listScheduleModel.get(i).getContentFriday(), listScheduleModel.get(i).getContentSaturday(), listScheduleModel.get(i).getContentFriday());
            ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(modelData);
        }
        for (int i = 0; i < rowAfternoon; i++) {
            int i1 = i + rowMorning;
            List<String> bodyStringList = Arrays.asList((i == 0) ? "Buổi chiều/Afternoon" : "", listScheduleModel.get(i1).getContentMonday(), listScheduleModel.get(i1).getContentTuesday(), listScheduleModel.get(i1).getContentWednesday(), listScheduleModel.get(i1).getContentThursday(),
                    listScheduleModel.get(i1).getContentFriday(), listScheduleModel.get(i1).getContentSaturday(), listScheduleModel.get(i1).getContentFriday());
            ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(modelData);
        }
        for (int i = 0; i < rowEvening; i++) {
            int i1 = i + rowMorning + rowAfternoon;
            List<String> bodyStringList = Arrays.asList((i == 0) ? "Buổi tối/Evening" : "", listScheduleModel.get(i1).getContentMonday(), listScheduleModel.get(i1).getContentTuesday(), listScheduleModel.get(i1).getContentWednesday(), listScheduleModel.get(i1).getContentThursday(),
                    listScheduleModel.get(i1).getContentFriday(), listScheduleModel.get(i1).getContentSaturday(), listScheduleModel.get(i1).getContentFriday());
            ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(modelData);
        }
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    @Override
    @Transactional
    public boolean saveScheduleClassWeek(Long idSchool, UserPrincipal principal, List<ScheduleInClassWeekRequest> scheduleInClassWeekRequestList) {
        if (CollectionUtils.isEmpty(scheduleInClassWeekRequestList)) {
            return false;
        }
        for (ScheduleInClassWeekRequest scheduleInClassWeekRequest : scheduleInClassWeekRequestList) {
            /**
             * Nếu thời khóa biểu của thứ đó đưa vào không có id,ta thực insert vào bảng class_schedule
             */
            if (scheduleInClassWeekRequest.getIdSchedule() == null) {
                ClassSchedule classSchedule = modelMapper.map(scheduleInClassWeekRequest, ClassSchedule.class);
                classSchedule.setMaClass(maClassRepository.findByIdMaClass(idSchool, scheduleInClassWeekRequest.getIdClass()).get());

                classSchedule.setIdSchool(idSchool);
                classSchedule.setApproved(!principal.getSchoolConfig().isApprovedSchedule());
//                classSchedule.setScheduleTitle(scheduleInClassWeekRequest.getScheduleTitle());

                classSchedule = classScheduleRepository.save(classSchedule);
                List<ScheduleMorning> scheduleMorningList = new ArrayList<>();
                List<ScheduleAfternoon> scheduleAfternoonList = new ArrayList<>();
                List<ScheduleEvening> scheduleEveningList = new ArrayList<>();
                for (ScheduleInClassRequest scheduleInClassRequest : scheduleInClassWeekRequest.getScheduleInClassResponseList()) {
                    if (scheduleInClassRequest.getSessionDay().equalsIgnoreCase("Sáng")) {
                        if (StringUtils.isNotBlank(scheduleInClassRequest.getTimeContent()) || StringUtils.isNotBlank(scheduleInClassRequest.getContentSchedule())) {
                            ScheduleMorning scheduleMorning = new ScheduleMorning();
                            scheduleMorning.setTime(scheduleInClassRequest.getTimeContent());
                            scheduleMorning.setContent(scheduleInClassRequest.getContentSchedule());
                            if (StringUtils.isNotBlank(scheduleMorning.getContent()) && StringUtils.isNotBlank(scheduleMorning.getTime())) {
                                classSchedule.setApproved(!principal.getSchoolConfig().isApprovedSchedule());
                            }
                            scheduleMorning.setClassSchedule(classSchedule);
                            scheduleMorningList.add(scheduleMorning);
                        }
                    } else if (scheduleInClassRequest.getSessionDay().equalsIgnoreCase("Chiều")) {
                        if (StringUtils.isNotBlank(scheduleInClassRequest.getTimeContent()) || StringUtils.isNotBlank(scheduleInClassRequest.getContentSchedule())) {
                            ScheduleAfternoon scheduleAfternoon = new ScheduleAfternoon();
                            scheduleAfternoon.setTime(scheduleInClassRequest.getTimeContent());
                            scheduleAfternoon.setContent(scheduleInClassRequest.getContentSchedule());
                            if (StringUtils.isNotBlank(scheduleAfternoon.getContent()) && StringUtils.isNotBlank(scheduleAfternoon.getTime())) {
                                classSchedule.setApproved(!principal.getSchoolConfig().isApprovedSchedule());
                            }
                            scheduleAfternoon.setClassSchedule(classSchedule);
                            scheduleAfternoonList.add(scheduleAfternoon);
                        }

                    } else if (scheduleInClassRequest.getSessionDay().equalsIgnoreCase("Tối")) {
                        if (StringUtils.isNotBlank(scheduleInClassRequest.getTimeContent()) || StringUtils.isNotBlank(scheduleInClassRequest.getContentSchedule())) {
                            ScheduleEvening scheduleEvening = new ScheduleEvening();
                            scheduleEvening.setTime(scheduleInClassRequest.getTimeContent());
                            scheduleEvening.setContent(scheduleInClassRequest.getContentSchedule());
                            if (StringUtils.isNotBlank(scheduleEvening.getContent()) && StringUtils.isNotBlank(scheduleEvening.getTime())) {
                                classSchedule.setApproved(!principal.getSchoolConfig().isApprovedSchedule());
                            }
                            scheduleEvening.setClassSchedule(classSchedule);
                            scheduleEveningList.add(scheduleEvening);
                        }

                    }
                }

                if (scheduleMorningList.size() > 0) {
                    classSchedule.setScheduleMorningList(scheduleMorningList);
                }

                if (scheduleAfternoonList.size() > 0) {
                    classSchedule.setScheduleAfternoonList(scheduleAfternoonList);
                }
                if (scheduleEveningList.size() > 0) {
                    classSchedule.setScheduleEveningList(scheduleEveningList);
                }

            }
            /**
             * Nếu thời khóa biểu của thứ đó đưa vào có id,ta thực update vào bảng class_schedule
             */
            else {
                /**
                 * Tìm kiếm thời khóa biểu của lớp đó
                 */
                Optional<ClassSchedule> oldClassSchedule = classScheduleRepository.findByIdAndDelActiveTrue(scheduleInClassWeekRequest.getIdSchedule());
                /**
                 * Xóa tất cả các bản ghi tại 3 bảng sáng chiều tối
                 */
                scheduleMorningRepository.deleteByClassSchedule(oldClassSchedule.get());
                scheduleAfternoonRepository.deleteByClassScheduleId(oldClassSchedule.get().getId());
                scheduleEveningRepository.deleteByClassScheduleId(oldClassSchedule.get().getId());
                //modelMapper.map(scheduleInClassWeekRequest, oldClassSchedule.get());
                List<ScheduleMorning> scheduleMorningList = new ArrayList<>();
                List<ScheduleAfternoon> scheduleAfternoonList = new ArrayList<>();
                List<ScheduleEvening> scheduleEveningList = new ArrayList<>();
                /**
                 * Chạy vòng lớp để lấy từng buổi trong ngày
                 */
                for (ScheduleInClassRequest scheduleInClassRequest : scheduleInClassWeekRequest.getScheduleInClassResponseList()) {
                    /**
                     * Nếu đó là buổi sáng
                     */
                    if (scheduleInClassRequest.getSessionDay().equalsIgnoreCase("Sáng")) {

                        if (StringUtils.isNotBlank(scheduleInClassRequest.getTimeContent()) || StringUtils.isNotBlank(scheduleInClassRequest.getContentSchedule())) {
                            ScheduleMorning scheduleMorning = new ScheduleMorning();
                            scheduleMorning.setTime(scheduleInClassRequest.getTimeContent());
                            scheduleMorning.setContent(scheduleInClassRequest.getContentSchedule());
                            scheduleMorning.setClassSchedule(oldClassSchedule.get());
                            scheduleMorningList.add(scheduleMorning);
                        }

                    }
                    /**
                     * Nếu đó là buổi chiều
                     */
                    else if (scheduleInClassRequest.getSessionDay().equalsIgnoreCase("Chiều")) {
                        if (StringUtils.isNotBlank(scheduleInClassRequest.getTimeContent()) || StringUtils.isNotBlank(scheduleInClassRequest.getContentSchedule())) {
                            ScheduleAfternoon scheduleAfternoon = new ScheduleAfternoon();
                            scheduleAfternoon.setTime(scheduleInClassRequest.getTimeContent());
                            scheduleAfternoon.setContent(scheduleInClassRequest.getContentSchedule());
                            scheduleAfternoon.setClassSchedule(oldClassSchedule.get());
                            scheduleAfternoonList.add(scheduleAfternoon);
                        }
                    }
                    /**
                     * Nếu đó là buổi tối
                     */
                    else if (scheduleInClassRequest.getSessionDay().equalsIgnoreCase("Tối")) {
                        if (StringUtils.isNotBlank(scheduleInClassRequest.getTimeContent()) || StringUtils.isNotBlank(scheduleInClassRequest.getContentSchedule())) {
                            ScheduleEvening scheduleEvening = new ScheduleEvening();
                            scheduleEvening.setTime(scheduleInClassRequest.getTimeContent());
                            scheduleEvening.setContent(scheduleInClassRequest.getContentSchedule());
                            scheduleEvening.setClassSchedule(oldClassSchedule.get());
                            scheduleEveningList.add(scheduleEvening);
                        }
                    }
                }
                if (scheduleMorningList.size() > 0) {
                    oldClassSchedule.get().setScheduleMorningList(scheduleMorningList);
                }
                if (scheduleAfternoonList.size() > 0) {
                    oldClassSchedule.get().setScheduleAfternoonList(scheduleAfternoonList);
                }
                if (scheduleEveningList.size() > 0) {
                    oldClassSchedule.get().setScheduleEveningList(scheduleEveningList);
                }
                oldClassSchedule.get().setScheduleDate(scheduleInClassWeekRequest.getScheduleDate());
//                oldClassSchedule.get().setScheduleTitle(scheduleInClassWeekRequest.getScheduleTitle());
//                oldClassSchedule.get().setApproved(!principal.getSchoolConfig().isApprovedSchedule());
                ClassSchedule newClassSchedule = classScheduleRepository.save(oldClassSchedule.get());
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean saveMultiSchedule(Long idSchool, UserPrincipal principal, CreateMultiSchedule createMultiSchedule) {
        List<String> scheduleDateListRequest = createMultiSchedule.getWeekSchedule();
        /**
         * Parse sang localDate
         */
        List<LocalDate> scheduleDateListRequestParse = scheduleDateListRequest.stream().map(item -> LocalDate.parse(item)).collect(Collectors.toList());
        List<LocalDate> listDateDayInWeek = new ArrayList<>();
        scheduleDateListRequestParse.forEach(item -> {
            for (int i = 0; i <= 6; i++) {
                LocalDate dateOfWeek = item.plusDays(i);
                listDateDayInWeek.add(dateOfWeek);
            }
        });
        /**
         * Chạy vòng lặp các id_class từ request
         */
        for (Long idClass : createMultiSchedule.getListIdClass()) {

            /**
             * Nếu tồn tại maClass đó và có schedule_Date trong bảng Class_Schedule nằm trong  thời gian List scheduleDate đưa vào thì tiến hành update
             */
            if (classScheduleRepository.existsByMaClassIdAndMaClassDelActiveTrueAndScheduleDateIn(idClass, listDateDayInWeek)) {
                List<ClassSchedule> classScheduleList = classScheduleRepository.findByMaClassIdAndMaClassDelActiveTrueAndScheduleDateIn(idClass, listDateDayInWeek);
                /**
                 * Xóa các bản ghi có idSchedule có id_class có scheduleDate trong bảng Sáng chiều tối nằm trong ListMenuDate đưa vào có id trong bảng
                 */
                classScheduleList.forEach(item -> {
                    scheduleMorningRepository.deleteByClassSchedule(item);
                    scheduleAfternoonRepository.deleteByClassScheduleId(item.getId());
                    scheduleEveningRepository.deleteByClassScheduleId(item.getId());
                });

                /**
                 * Xóa các id_class có menuDate trong bảng Class_Menu nằm trong ListMenuDate đưa vào
                 */
                classScheduleRepository.deleteByMaClassIdAndMaClassDelActiveTrueAndScheduleDateIn(idClass, listDateDayInWeek);

                /**
                 * Insert
                 */
                /**
                 * Tìm kiếm maClass theo id
                 */
                MaClass maClass = maClassRepository.findByIdMaClass(idSchool, idClass).get();
                /**
                 * Chạy vòng lặp để lấy các ngày thứ 2 của các tuần
                 */
                for (String scheduleDateRequest : createMultiSchedule.getWeekSchedule()) {
                    LocalDate monday = LocalDate.parse(scheduleDateRequest);
                    /**
                     * Từ thứ 2 chạy vòng lặp +1 để suy ra từng thứ
                     */
                    for (int i = 0; i <= 6; i++) {
                        ClassSchedule classSchedule = new ClassSchedule();
                        classSchedule.setIdSchool(idSchool);
                        classSchedule.setMaClass(maClass);
                        classSchedule.setApproved(!principal.getSchoolConfig().isApprovedSchedule());
                        classSchedule.setScheduleTitle(createMultiSchedule.getScheduleTitle());
                        /**
                         * i=0:Thứ 2,i=1:Thứ 3,i=2:Thứ 4,...
                         */
                        LocalDate afterMonday = monday.plusDays(i);
                        classSchedule.setScheduleDate(afterMonday);
                        classSchedule = classScheduleRepository.save(classSchedule);
                        /**
                         * Lấy nội dung ngày thứ 2 từ Request
                         */
                        CreateTabAllSchedule createTabAllSchedule = createMultiSchedule.getCreateTabAllSchedule().get(i);
                        /**
                         * Chạy vòng for lấy từ buổi Sáng,Chiều,Tối của thứ 2
                         */
                        for (CreateTabDayInWeek createTabDayInWeek : createTabAllSchedule.getCreateTabDayInWeek()) {
                            if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Sáng") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentSchedule()))) {
                                ScheduleMorning scheduleMorning = new ScheduleMorning();
                                scheduleMorning.setContent(createTabDayInWeek.getContentSchedule());
                                scheduleMorning.setTime(createTabDayInWeek.getTimeContent());
                                scheduleMorning.setClassSchedule(classSchedule);
                                scheduleMorningRepository.save(scheduleMorning);
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Chiều") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentSchedule()))) {
                                ScheduleAfternoon scheduleAfternoon = new ScheduleAfternoon();
                                scheduleAfternoon.setContent(createTabDayInWeek.getContentSchedule());
                                scheduleAfternoon.setTime(createTabDayInWeek.getTimeContent());
                                scheduleAfternoon.setClassSchedule(classSchedule);
                                scheduleAfternoonRepository.save(scheduleAfternoon);
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Tối") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentSchedule()))) {
                                ScheduleEvening scheduleEvening = new ScheduleEvening();
                                scheduleEvening.setContent(createTabDayInWeek.getContentSchedule());
                                scheduleEvening.setTime(createTabDayInWeek.getTimeContent());
                                scheduleEvening.setClassSchedule(classSchedule);
                                scheduleEveningRepository.save(scheduleEvening);
                            }
                        }
                    }
                }
            }
            /**
             * Nếu không tồn tại maClass đó trong bảng Class_Schedule thì tiến hành insert
             */
            else {
                /**
                 * Tìm kiếm maClass theo id
                 */
                MaClass maClass = maClassRepository.findByIdMaClass(idSchool, idClass).get();
                ClassSchedule classSchedule1 = new ClassSchedule();
                classSchedule1.setScheduleTitle(createMultiSchedule.getScheduleTitle());
                /**
                 * Chạy vòng lặp để lấy các ngày thứ 2 của các tuần
                 */
                for (String scheduleDateRequest : createMultiSchedule.getWeekSchedule()) {
                    LocalDate monday = LocalDate.parse(scheduleDateRequest);
                    /**
                     * Từ thứ 2 chạy vòng lặp +1 để suy ra từng thứ
                     */
                    for (int i = 0; i <= 6; i++) {
                        ClassSchedule classSchedule = new ClassSchedule();
                        classSchedule.setIdSchool(idSchool);
                        classSchedule.setMaClass(maClass);
                        classSchedule.setScheduleTitle(createMultiSchedule.getScheduleTitle());
                        /**
                         * i=0:Thứ 2,i=1:Thứ 3,i=2:Thứ 4,...
                         */
                        LocalDate afterMonday = monday.plusDays(i);
                        classSchedule.setScheduleDate(afterMonday);
                        classSchedule.setApproved(!principal.getSchoolConfig().isApprovedSchedule());
                        classSchedule = classScheduleRepository.save(classSchedule);
                        /**
                         * Lấy nội dung ngày thứ i từ Request
                         */
                        CreateTabAllSchedule createTabAllSchedule = createMultiSchedule.getCreateTabAllSchedule().get(i);
                        /**
                         * Chạy vòng for lấy từ buổi Sáng,Chiều,Tối của thứ 2
                         */
                        for (CreateTabDayInWeek createTabDayInWeek : createTabAllSchedule.getCreateTabDayInWeek()) {
                            if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Sáng") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentSchedule()))) {
                                ScheduleMorning scheduleMorning = new ScheduleMorning();
                                scheduleMorning.setContent(createTabDayInWeek.getContentSchedule());
                                scheduleMorning.setTime(createTabDayInWeek.getTimeContent());
                                scheduleMorning.setClassSchedule(classSchedule);
                                scheduleMorningRepository.save(scheduleMorning);
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Chiều") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentSchedule()))) {
                                ScheduleAfternoon scheduleAfternoon = new ScheduleAfternoon();
                                scheduleAfternoon.setContent(createTabDayInWeek.getContentSchedule());
                                scheduleAfternoon.setTime(createTabDayInWeek.getTimeContent());
                                scheduleAfternoon.setClassSchedule(classSchedule);
                                scheduleAfternoonRepository.save(scheduleAfternoon);
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Tối") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentSchedule()))) {
                                ScheduleEvening scheduleEvening = new ScheduleEvening();
                                scheduleEvening.setContent(createTabDayInWeek.getContentSchedule());
                                scheduleEvening.setTime(createTabDayInWeek.getTimeContent());
                                scheduleEvening.setClassSchedule(classSchedule);
                                scheduleEveningRepository.save(scheduleEvening);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<TabDetailScheduleAllClassResponse> findAllScheduleTabDetail(Long idSchool, SearchScheduleRequest searchScheduleRequest) {
        /**
         *Tìm kiếm các lớp theo ngày,khối(nếu có),lớp(nếu có)
         */
        List<MaClass> maClassList = maClassRepository.searchMaClassByIdGrade(idSchool, searchScheduleRequest.getIdGrade(), searchScheduleRequest.getIdClass());
        if (CollectionUtils.isEmpty(maClassList)) {
            return null;
        }
        String timeSchedule = searchScheduleRequest.getTimeSchedule();
        LocalDate monday = LocalDate.parse(timeSchedule);
        List<TabDetailScheduleAllClassResponse> tabDetailScheduleAllClassResponseList = new ArrayList<>();
        for (MaClass maClass : maClassList) {
            TabDetailScheduleAllClassResponse tabDetailScheduleAllClassResponse = new TabDetailScheduleAllClassResponse();
            tabDetailScheduleAllClassResponse.setGradeName(maClass.getGrade().getGradeName());
            tabDetailScheduleAllClassResponse.setClassName(maClass.getClassName());
            tabDetailScheduleAllClassResponse.setIdClass(maClass.getId());
            tabDetailScheduleAllClassResponse.setIsMonday(timeSchedule);
            if (!CollectionUtils.isEmpty(maClass.getClassScheduleList())) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i <= 6; i++) {
                    LocalDate monday1 = monday.plusDays(i);
                    List<ClassSchedule> classScheduleList = maClass.getClassScheduleList().stream().filter(item -> item.getScheduleDate().isEqual(monday1)).collect(Collectors.toList());
                    classScheduleList.forEach(classSchedule -> {
                        for (int j = 0; j <= 6; j++) {
                            if (classSchedule.getScheduleDate().isEqual(monday.plusDays(j))) {
                                if (!CollectionUtils.isEmpty(classSchedule.getScheduleMorningList())) {
                                    if (monday.plusDays(j).isEqual(monday.plusDays(6))) {
                                        if (maClass.isSunday()) {
                                            stringBuilder.append("CN" + " | ");
                                        }
                                    } else if (monday.plusDays(j).isEqual(monday.plusDays(5))) {
                                        if (maClass.isMorningSaturday() || maClass.isAfternoonSaturday() || maClass.isEveningSaturday()) {
                                            stringBuilder.append("T" + (j + 2) + " | ");
                                        }
                                    } else {
                                        stringBuilder.append("T" + (j + 2) + " | ");
                                    }

                                    tabDetailScheduleAllClassResponse.setListCheckContentDay(stringBuilder.toString());

                                    break;

                                } else if (!CollectionUtils.isEmpty(classSchedule.getScheduleAfternoonList())) {
                                    if (monday.plusDays(j).isEqual(monday.plusDays(6))) {
                                        if (maClass.isSunday()) {
                                            stringBuilder.append("CN" + " | ");
                                        }
                                    } else if (monday.plusDays(j).isEqual(monday.plusDays(5))) {
                                        if (maClass.isMorningSaturday() || maClass.isAfternoonSaturday() || maClass.isEveningSaturday()) {
                                            stringBuilder.append("T" + (j + 2) + " | ");
                                        }
                                    } else {
                                        stringBuilder.append("T" + (j + 2) + " | ");
                                    }
                                    tabDetailScheduleAllClassResponse.setListCheckContentDay(stringBuilder.toString());
                                    break;
                                } else if (!CollectionUtils.isEmpty(classSchedule.getScheduleEveningList())) {
                                    if (monday.plusDays(j).isEqual(monday.plusDays(6))) {
                                        if (maClass.isSunday()) {
                                            stringBuilder.append("CN" + " | ");
                                        }
                                    } else if (monday.plusDays(j).isEqual(monday.plusDays(5))) {
                                        if (maClass.isMorningSaturday() || maClass.isAfternoonSaturday() || maClass.isEveningSaturday()) {
                                            stringBuilder.append("T" + (j + 2) + " | ");
                                        }
                                    } else {
                                        stringBuilder.append("T" + (j + 2) + " | ");
                                    }
                                    tabDetailScheduleAllClassResponse.setListCheckContentDay(stringBuilder.toString());
                                    break;
                                }
                            }
                        }
                        tabDetailScheduleAllClassResponse.setApprove(classSchedule.isApproved());
                    });
                }


            }
            /**
             * Hiển thị trên tab thời khóa biểu dạng file ảnh
             */
            if (!CollectionUtils.isEmpty(maClass.getScheduleFileList())) {
                List<FileAndPictureResponse> filePictures = new ArrayList<>();
                maClass.getScheduleFileList().forEach(scheduleFile -> {
                    if (scheduleFile.getFromFileTsime().isEqual(monday) && scheduleFile.getToFileTsime().isEqual(monday.plusDays(6))) {
                        StringBuilder strFileList = new StringBuilder();
                        scheduleFile.getUrlScheuldeFileList().forEach(
                                urlScheuldeFile -> {
                                    FileAndPictureResponse fileAndPictureResponse = new FileAndPictureResponse();
                                    if (StringUtils.isNotBlank(urlScheuldeFile.getNameFile())) {
                                        fileAndPictureResponse.setName(urlScheuldeFile.getNameFile());
                                        fileAndPictureResponse.setUrl(urlScheuldeFile.getUrlFile());
                                        fileAndPictureResponse.setIdScheduleFile(scheduleFile.getId());
                                        fileAndPictureResponse.setIdUrlScheduleFile(urlScheuldeFile.getId());
                                    } else if (StringUtils.isNotBlank(urlScheuldeFile.getNamePicture())) {
                                        fileAndPictureResponse.setName(urlScheuldeFile.getNamePicture());
                                        fileAndPictureResponse.setUrl(urlScheuldeFile.getUrlPicture());
                                        fileAndPictureResponse.setIdScheduleFile(scheduleFile.getId());
                                        fileAndPictureResponse.setIdUrlScheduleFile(urlScheuldeFile.getId());
                                    }
                                    filePictures.add(fileAndPictureResponse);
                                }
                        );
                        tabDetailScheduleAllClassResponse.setFileList(filePictures);
                        if (!CollectionUtils.isEmpty(filePictures)) {
                            tabDetailScheduleAllClassResponse.setApprove(scheduleFile.isApproved());
                        }
                    }
                });
            }
            tabDetailScheduleAllClassResponseList.add(tabDetailScheduleAllClassResponse);
        }
        return tabDetailScheduleAllClassResponseList;
    }

    @Override
    @Transactional
    public boolean updateApprove(Long idSchool, ApproveStatus approveStatus) {
        Optional<MaClass> maClassOptional = maClassRepository.findByIdAndIdSchoolAndDelActiveTrue(approveStatus.getIdClass(), idSchool);
        if (maClassOptional.isEmpty()) {
            return false;
        }
        LocalDate monday = approveStatus.getIsMonday();
//        LocalDate mondayParse = LocalDate.parse(monday);
        MaClass maClass = maClassOptional.get();
        List<ScheduleFile> scheduleFile = scheduleFileRepository.findScheduleFile(idSchool, approveStatus.getIdClass(), monday);
        if (scheduleFile.size() > 0) {
            scheduleFile.get(0).setApproved(approveStatus.isApprove());
            scheduleFileRepository.save(scheduleFile.get(0));
        }

        for (int i = 0; i <= 6; i++) {
            LocalDate mondayParse1 = monday.plusDays(i);
            List<ClassSchedule> classScheduleList = maClass.getClassScheduleList().stream().filter(x -> x.getScheduleDate().isEqual(mondayParse1)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(classScheduleList)) {
                classScheduleList.forEach(item -> {
                    item.setApproved(approveStatus.isApprove());
                    classScheduleRepository.save(item);
                });
            }


        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateMultiApprove(Long idSchool, List<ApproveStatus> approveStatusList) {
        approveStatusList.forEach(approveStatus -> {
            Optional<MaClass> maClassOptional = maClassRepository.findByIdAndIdSchoolAndDelActiveTrue(approveStatus.getIdClass(), idSchool);
            if (maClassOptional.isEmpty()) {
                return;
            }
            LocalDate monday = approveStatus.getIsMonday();
//            LocalDate mondayParse = LocalDate.parse(monday);
            MaClass maClass = maClassOptional.get();
            for (int i = 0; i <= 6; i++) {
                LocalDate mondayParse1 = monday.plusDays(i);
                List<ClassSchedule> classScheduleList = maClass.getClassScheduleList().stream().filter(x -> x.getScheduleDate().isEqual(mondayParse1)).collect(Collectors.toList());
                ClassSchedule classSchedule = classScheduleList.get(0);
                classSchedule.setApproved(approveStatus.isApprove());
                classScheduleRepository.save(classSchedule);
            }
        });
        return true;
    }

    @Override
    public List<TabScheduleViewDetail> findScheduleDetailByClass(Long idSchool, Long idClass) {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfYear = now.with(firstDayOfYear());
        LocalDate lastDayOfYear = now.with(lastDayOfYear());
        List<LocalDate> mondays = new ArrayList<>();
        LocalDate monday = firstDayOfYear.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        while (monday.isBefore(lastDayOfYear)) {
            mondays.add(monday);
            // Set up the next loop.
            monday = monday.plusDays(7);
        }
        Collections.sort(mondays, new Comparator<LocalDate>() {
            @Override
            public int compare(LocalDate o1, LocalDate o2) {
                return o2.compareTo(o1);
            }
        });
        List<TabScheduleViewDetail> tabScheduleViewDetailList = new ArrayList<>();
        List<ClassSchedule> classScheduleList = classScheduleRepository
                .findByMaClassIdAndMaClassDelActiveTrueAndScheduleDateBetween(idClass, firstDayOfYear, lastDayOfYear)
                .stream().filter(x -> x.getScheduleDate().getDayOfWeek() == DayOfWeek.MONDAY).collect(Collectors.toList());

        mondays.forEach(mondayFirst -> {
            TabScheduleViewDetail tabScheduleViewDetail = new TabScheduleViewDetail();
            String timeApplyWeek = DateTimeFormatter.ofPattern("(dd/LL/yyyy").format(mondayFirst) + "-" + DateTimeFormatter.ofPattern("dd/LL/yyyy)").format(mondayFirst.plusDays(6));
            tabScheduleViewDetail.setTimeApplyWeek(timeApplyWeek);
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int weekNumber = 0;
            if (mondayFirst.plusDays(6).getYear() == mondayFirst.getYear() + 1) {
                weekNumber = mondays.size();
            } else {
                int week = mondayFirst.get(WeekFields.of(Locale.getDefault()).weekOfYear());
                weekNumber = DataUtils.convertWeek(mondayFirst, week);
            }
            tabScheduleViewDetail.setWeeknumber(String.valueOf(weekNumber));
            tabScheduleViewDetail.setIdClass(idClass);
            tabScheduleViewDetail.setIsMonday(mondayFirst.toString());
            classScheduleList.forEach(classSchedule -> {

                if (mondayFirst.isEqual(classSchedule.getScheduleDate())) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int j = 0; j <= 6; j++) {
                        List<ClassSchedule> classScheduleOptional = classScheduleRepository.findDistinctByMaClassIdAndMaClassDelActiveTrueAndScheduleDate(idClass, mondayFirst.plusDays(j));
                        if (classScheduleOptional.isEmpty()) {
                            continue;
                        }
                        ClassSchedule classSchedule1 = classScheduleOptional.get(0);
                        if (!CollectionUtils.isEmpty(classSchedule1.getScheduleMorningList())) {
                            if (classSchedule1.getScheduleDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                if (classSchedule1.getMaClass().isSunday()) {
                                    stringBuilder.append("CN" + " | ");
                                }
                            } else if (classSchedule1.getScheduleDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                if (classSchedule1.getMaClass().isMorningSaturday() || classSchedule1.getMaClass().isAfternoonSaturday() || classSchedule1.getMaClass().isEveningSaturday()) {
                                    stringBuilder.append("T" + (j + 2) + " | ");
                                }
                            } else {
                                stringBuilder.append("T" + (j + 2) + " | ");
                            }

                            tabScheduleViewDetail.setListCheckContentday(stringBuilder.toString());

                            continue;

                        } else if (!CollectionUtils.isEmpty(classSchedule1.getScheduleAfternoonList())) {
                            if (classSchedule1.getScheduleDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                if (classSchedule1.getMaClass().isSunday()) {
                                    stringBuilder.append("CN" + " | ");
                                }
                            } else if (classSchedule1.getScheduleDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                if (classSchedule1.getMaClass().isMorningSaturday() || classSchedule1.getMaClass().isAfternoonSaturday() || classSchedule1.getMaClass().isEveningSaturday()) {
                                    stringBuilder.append("T" + (j + 2) + " | ");
                                }
                            } else {
                                stringBuilder.append("T" + (j + 2) + " | ");
                            }
                            tabScheduleViewDetail.setListCheckContentday(stringBuilder.toString());
                            continue;
                        } else if (!CollectionUtils.isEmpty(classSchedule1.getScheduleEveningList())) {
                            if (classSchedule1.getScheduleDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                if (classSchedule1.getMaClass().isSunday()) {
                                    stringBuilder.append("CN" + " | ");
                                }
                            } else if (classSchedule1.getScheduleDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                if (classSchedule1.getMaClass().isMorningSaturday() || classSchedule1.getMaClass().isAfternoonSaturday() || classSchedule1.getMaClass().isEveningSaturday()) {
                                    stringBuilder.append("T" + (j + 2) + " | ");
                                }
                            } else {
                                stringBuilder.append("T" + (j + 2) + " | ");
                            }
                            tabScheduleViewDetail.setListCheckContentday(stringBuilder.toString());
                            continue;
                        }
                    }
                    tabScheduleViewDetail.setApprove(classSchedule.isApproved());
                }

            });

            /**
             * Hiển thị file hoặc ảnh
             */
            List<ScheduleFile> scheduleFileList = scheduleFileRepository.findByMaClassIdAndFromFileTsimeAndToFileTsime(idClass, mondayFirst, mondayFirst.plusDays(6));
            if (!CollectionUtils.isEmpty(scheduleFileList)) {
                List<FileAndPictureResponse> filePictures = new ArrayList<>();
                scheduleFileList.forEach(scheduleFile -> {
                    scheduleFile.getUrlScheuldeFileList().forEach(
                            urlScheuldeFile -> {
                                FileAndPictureResponse fileAndPictureResponse = new FileAndPictureResponse();
                                if (StringUtils.isNotBlank(urlScheuldeFile.getNameFile())) {
                                    fileAndPictureResponse.setName(urlScheuldeFile.getNameFile());
                                    fileAndPictureResponse.setUrl(urlScheuldeFile.getUrlFile());
                                    fileAndPictureResponse.setIdUrlScheduleFile(urlScheuldeFile.getId());
                                } else if (StringUtils.isNotBlank(urlScheuldeFile.getNamePicture())) {
                                    fileAndPictureResponse.setName(urlScheuldeFile.getNamePicture());
                                    fileAndPictureResponse.setUrl(urlScheuldeFile.getUrlPicture());
                                    fileAndPictureResponse.setIdUrlScheduleFile(urlScheuldeFile.getId());
                                }
                                filePictures.add(fileAndPictureResponse);
                            }
                    );
                    if (!CollectionUtils.isEmpty(filePictures)) {
                        tabScheduleViewDetail.setApprove(scheduleFile.isApproved());
                    }
                });
                tabScheduleViewDetail.setFileList(filePictures);
            }
            tabScheduleViewDetailList.add(tabScheduleViewDetail);
        });

        return tabScheduleViewDetailList;
    }

    @Override
    @Transactional
    public boolean deleteContentSchedule(List<SearchScheduleInClassRequest> searchScheduleInClassRequestList) {
        searchScheduleInClassRequestList.forEach(searchScheduleInClassRequest -> {
            List<ClassSchedule> classScheduleList = classScheduleRepository.findByMaClassIdAndMaClassDelActiveTrueAndScheduleDateBetween(searchScheduleInClassRequest.getIdClass(), LocalDate.parse(searchScheduleInClassRequest.getIsMonday()), LocalDate.parse(searchScheduleInClassRequest.getIsMonday()).plusDays(6));
            if (CollectionUtils.isEmpty(classScheduleList)) {
                return;
            }
            classScheduleList.forEach(classSchedule -> {
                if (!CollectionUtils.isEmpty(classSchedule.getScheduleMorningList())) {
                    List<ScheduleMorning> scheduleMorningList = classSchedule.getScheduleMorningList();
                    scheduleMorningRepository.deleteByIdClass(classSchedule.getId());

                }
                if (!CollectionUtils.isEmpty(classSchedule.getScheduleAfternoonList())) {
                    List<ScheduleAfternoon> scheduleAfternoonList = classSchedule.getScheduleAfternoonList();
                    scheduleAfternoonRepository.deleteByIdClass(classSchedule.getId());

                }
                if (!CollectionUtils.isEmpty(classSchedule.getScheduleEveningList())) {
                    List<ScheduleEvening> scheduleEveningList = classSchedule.getScheduleEveningList();
                    scheduleEveningRepository.deleteByIdClass(classSchedule.getId());

                }
            });
        });

        return true;
    }

    @Override
    @Transactional
    public boolean saveScheduleFile(Long idSchool, UserPrincipal principal, CreateFileAndPictureRequest createFileAndPictureRequest) throws IOException {
        int monthCurrent = LocalDate.now().getMonthValue();
        int yearCurrent = LocalDate.now().getYear();
        LocalDate datePlus = createFileAndPictureRequest.getFromFileTsime().plusDays(6);


//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
//        String monday = date.format(formatter);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        String monday = date.format(formatter);

        List<ClassSchedule> classSchedule = classScheduleRepository.findByMaClassIdAndMaClassDelActiveTrueAndScheduleDateBetween(principal.getIdClassLogin(), createFileAndPictureRequest.getFromFileTsime(), datePlus);

        List<ScheduleFile> scheduleFile = scheduleFileRepository.findScheduleFile(idSchool, createFileAndPictureRequest.getIdClass(), createFileAndPictureRequest.getFromFileTsime());

        if (scheduleFile.size() == 0) {

            ScheduleFile scheduleFileNew = new ScheduleFile();


            scheduleFileNew.setIdSchool(idSchool);
            if (!CollectionUtils.isEmpty(classSchedule)) {
                scheduleFileNew.setApproved(classSchedule.get(0).isApproved());
            } else {
                scheduleFileNew.setApproved(!principal.getSchoolConfig().isApprovedSchedule());

            }
            scheduleFileNew.setFromFileTsime(createFileAndPictureRequest.getFromFileTsime());
            scheduleFileNew.setToFileTsime(createFileAndPictureRequest.getFromFileTsime().plusDays(6));
            scheduleFileNew.setMaClass(maClassRepository.findByIdMaClass(idSchool, createFileAndPictureRequest.getIdClass()).get());

            Set<UrlScheuldeFile> urlScheuldeFileSet1 = new HashSet<>();


            List<MultipartFile> multipartFileList = createFileAndPictureRequest.getMultipartFileList();

            multipartFileList.forEach(multipartFilex -> {

                if (createFileAndPictureRequest.getMultipartFileList() != null) {

                    String fileName = HandleFileUtils.removeSpace(System.currentTimeMillis() + "_" + idSchool + "_" + multipartFilex.getOriginalFilename());

                    String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.HOC_TAP);
                    try {
                        HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFilex, fileName, UploadDownloadConstant.WIDTH_OTHER);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String urlFileSchedule = (AppConstant.URL_DEFAULT + idSchool + "/" + yearCurrent + "/T" + monthCurrent + "/" + "hoctap/");
                    String uploadFileSchedule = (AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + yearCurrent + "\\T" + monthCurrent + "\\" + "hoctap\\");
                    String directoryFileSchedule = AppConstant.DIRECTORY_DEFAULT + idSchool + "\\\\" + yearCurrent + "\\\\T" + monthCurrent + "\\\\" + "hoctap\\\\";
                    Path pathFileSchedule = Paths.get(uploadFileSchedule, fileName);
                    try {
                        Files.write(pathFileSchedule, multipartFilex.getBytes());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                    String extension = FilenameUtils.getExtension(multipartFilex.getOriginalFilename());

                    Set<ScheduleFile> scheduleFilesSet = new HashSet<>();

                    if (!extension.equals("jpg") && !extension.equals("png") && !extension.equals("jpeg")) {
                        UrlScheuldeFile urlScheuldeFileObject = new UrlScheuldeFile();
                        urlScheuldeFileObject.setNameFile(multipartFilex.getOriginalFilename());
                        urlScheuldeFileObject.setUrlFile(urlFileSchedule + fileName);
                        urlScheuldeFileObject.setUrlLocalFile(directoryFileSchedule + fileName);
                        scheduleFilesSet.add(scheduleFileNew);
                        urlScheduleFileRepository.save(urlScheuldeFileObject);
                        urlScheuldeFileSet1.add(urlScheuldeFileObject);
                    } else {
                        UrlScheuldeFile urlScheuldeFileObject = new UrlScheuldeFile();
                        urlScheuldeFileObject.setNamePicture(multipartFilex.getOriginalFilename());
                        urlScheuldeFileObject.setUrlPicture(urlFileSchedule + fileName);
                        urlScheuldeFileObject.setUrlLocalPicture(directoryFileSchedule + fileName);
                        scheduleFilesSet.add(scheduleFileNew);
                        urlScheduleFileRepository.save(urlScheuldeFileObject);
                        urlScheuldeFileSet1.add(urlScheuldeFileObject);
                    }
                }
            });

            scheduleFileNew.setUrlScheuldeFileList(urlScheuldeFileSet1);

            scheduleFileRepository.save(scheduleFileNew);


        }
        if (scheduleFile.size() == 1) {
            ScheduleFile scheduleFileOld = scheduleFile.get(0);
            Long idFileList = scheduleFileOld.getId();

//            List<UrlScheuldeFile> urlScheuldeFileObject = (List<UrlScheuldeFile>) scheduleFileOld.getUrlScheuldeFileList();

            Set<UrlScheuldeFile> urlScheuldeFileObjectOld = scheduleFileOld.getUrlScheuldeFileList();

            List<MultipartFile> multipartFileList = createFileAndPictureRequest.getMultipartFileList();

            multipartFileList.forEach(multipartFile -> {

                if (createFileAndPictureRequest.getMultipartFileList() != null) {
                    String fileName = HandleFileUtils.removeSpace(System.currentTimeMillis() + "_" + idSchool + "_" + multipartFile.getOriginalFilename());

                    String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.HOC_TAP);
                    try {
                        HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String urlFileSchedule = (AppConstant.URL_DEFAULT + idSchool + "/" + yearCurrent + "/T" + monthCurrent + "/" + "hoctap/");
                    String uploadFileSchedule = (AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + yearCurrent + "\\T" + monthCurrent + "\\" + "hoctap\\");
                    String directoryFileSchedule = AppConstant.DIRECTORY_DEFAULT + idSchool + "\\\\" + yearCurrent + "\\\\T" + monthCurrent + "\\\\" + "hoctap\\\\";
                    Path pathFileSchedule = Paths.get(uploadFileSchedule, fileName);
                    try {
                        Files.write(pathFileSchedule, multipartFile.getBytes());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                    String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

//                    Set<UrlScheuldeFile> urlScheuldeFileSet = new HashSet<>();
                    Set<ScheduleFile> scheduleFilesSet = new HashSet<>();
                    if (!extension.equals("jpg") && !extension.equals("png") && !extension.equals("jpeg")) {
                        UrlScheuldeFile urlScheuldeFileObject = new UrlScheuldeFile();
                        urlScheuldeFileObject.setNameFile(multipartFile.getOriginalFilename());
                        urlScheuldeFileObject.setUrlFile(urlFileSchedule + fileName);
                        urlScheuldeFileObject.setUrlLocalFile(directoryFileSchedule + fileName);
                        scheduleFilesSet.add(scheduleFileOld);
                        urlScheduleFileRepository.save(urlScheuldeFileObject);
                        urlScheuldeFileObjectOld.add(urlScheuldeFileObject);
                    } else {
                        UrlScheuldeFile urlScheuldeFileObject = new UrlScheuldeFile();
                        urlScheuldeFileObject.setNamePicture(multipartFile.getOriginalFilename());
                        urlScheuldeFileObject.setUrlPicture(urlFileSchedule + fileName);
                        urlScheuldeFileObject.setUrlLocalPicture(directoryFileSchedule + fileName);
                        scheduleFilesSet.add(scheduleFileOld);
                        urlScheduleFileRepository.save(urlScheuldeFileObject);
                        urlScheuldeFileObjectOld.add(urlScheuldeFileObject);
                    }
                }
            });

            scheduleFileOld.setUrlScheuldeFileList(urlScheuldeFileObjectOld);
            scheduleFileRepository.save(scheduleFileOld);
        }

        return true;
    }

    @Transactional
    @Override
    public boolean deleteScheduleFileById(Long idSchool, Long idUrlScheduleFile) {
//        ScheduleFile scheduleFile = scheduleFileRepository.findById(idScheduleFile).get();
//        scheduleFileRepository.deleteById(idScheduleFile);
        deleteEx(idUrlScheduleFile);
        UrlScheuldeFile urlScheuldeFile = urlScheduleFileRepository.findById(idUrlScheduleFile).get();
        String urlLocal = "";
        String urlLocalThumbnail = "";
        if (StringUtils.isNotBlank(urlScheuldeFile.getUrlLocalFile())) {
            urlLocal = HandleFileUtils.removeSpace(urlScheuldeFile.getUrlLocalFile());
            urlLocalThumbnail = StringUtils.replace(urlLocal, UploadDownloadConstant.HOC_TAP + "\\\\", UploadDownloadConstant.HOC_TAP + "\\\\thumbnail_");
        } else if (StringUtils.isNotBlank(urlScheuldeFile.getUrlLocalPicture())) {
            urlLocal = HandleFileUtils.removeSpace(urlScheuldeFile.getUrlLocalPicture());
            urlLocalThumbnail = StringUtils.replace(urlLocal, UploadDownloadConstant.HOC_TAP + "\\\\", UploadDownloadConstant.HOC_TAP + "\\\\thumbnail_");
        }
        HandleFileUtils.deleteFilePictureInDirectory(urlLocal, urlLocalThumbnail);

        deleteUrl(idUrlScheduleFile);

//        scheduleFileRepository.deleteExUrlFile(idUrlScheduleFile);
//        urlScheduleFileRepository.deleteUrlFile(idUrlScheduleFile);
        return true;
    }

    public void deleteEx(Long idUrlScheduleFile) {
        scheduleFileRepository.deleteExUrlFile(idUrlScheduleFile);
    }

    public void deleteUrl(Long idUrlScheduleFile) {
        urlScheduleFileRepository.deleteUrlFile(idUrlScheduleFile);
    }

    @Override
    public boolean saveScheduleTitleClass(Long idSchoolLogin, CreateTitleClassRequest createTitleClassRequest) {


        Optional<MaClass> maClassOptional = maClassRepository.findByIdAndIdSchoolAndDelActiveTrue(createTitleClassRequest.getIdClass(), idSchoolLogin);
        if (maClassOptional.isEmpty()) {
            return false;
        }
        LocalDate mondayParse = LocalDate.parse(createTitleClassRequest.getScheduleDate());
        MaClass maClass = maClassOptional.get();
        for (int i = 0; i <= 6; i++) {
            LocalDate mondayParse1 = mondayParse.plusDays(i);
            List<ClassSchedule> classScheduleList = maClass.getClassScheduleList().stream().filter(x -> x.getScheduleDate().isEqual(mondayParse1)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(classScheduleList)) {
                classScheduleList.forEach(item -> {
                    item.setScheduleTitle(createTitleClassRequest.getScheduleTitle());
                    classScheduleRepository.save(item);
                });
            }


        }
        return true;

    }

    @Override
    public boolean createFileAndPictureMultiClass(UserPrincipal principal, CreateFileAndPictureMenuMultiClassRequest fileAndPictureMenuMultiClassRequest) throws IOException {

        List<String> classMenuDateListRequest = fileAndPictureMenuMultiClassRequest.getWeekClassMenu();
        List<LocalDate> dateMondayList = classMenuDateListRequest.stream().map(item -> LocalDate.parse(item)).collect(Collectors.toList());
        Long idSchool = principal.getIdSchoolLogin();
        for (Long idClass : fileAndPictureMenuMultiClassRequest.getListIdClass()) {
            for (LocalDate monday : dateMondayList) {
                LocalDate datePlus = monday.plusDays(6);
                List<ScheduleFile> scheduleFileList = scheduleFileRepository.findScheduleFile(idSchool, idClass, monday);
                ClassSchedule classSchedules = classScheduleRepository.findByMaClassIdAndDelActiveTrueAndScheduleDate(idClass, monday);
                List<MultipartFile> multipartFileList = fileAndPictureMenuMultiClassRequest.getMultipartFile();
                if (!CollectionUtils.isEmpty(scheduleFileList)) {
                    Set<UrlScheuldeFile> urlScheuldeFileList = scheduleFileList.get(0).getUrlScheuldeFileList();
                    urlScheuldeFileList.forEach(x -> {
                        if (!Strings.isEmpty(x.getUrlLocalFile())) {
                            HandleFileUtils.deleteFileOrPictureInFolder(x.getUrlLocalFile());
                        }
                        if (!Strings.isEmpty(x.getUrlLocalPicture())) {
                            HandleFileUtils.deleteFileOrPictureInFolder(x.getUrlLocalPicture());
                        }
                        urlScheduleFileRepository.delete(x);
                    });
                    scheduleFileRepository.delete(scheduleFileList.get(0));
                }
                // tuần đó chưa có file hoạc ảnh -> tạo mới
                ScheduleFile scheduleFile = new ScheduleFile();
                scheduleFile.setIdSchool(idSchool);
                if (classSchedules != null) {
                    scheduleFile.setApproved(classSchedules.isApproved());
                } else {
                    scheduleFile.setApproved(!principal.getSchoolConfig().isApprovedMenu());
                }
                scheduleFile.setFromFileTsime(monday);
                scheduleFile.setToFileTsime(datePlus);
                scheduleFile.setMaClass(maClassRepository.findByIdMaClass(idSchool, idClass).get());
                scheduleFileRepository.save(scheduleFile);
                Set<UrlScheuldeFile> urlScheuldeFileHashSet = new HashSet<>();
                Set<ScheduleFile> scheduleFileHashSet = new HashSet<>();
                for (MultipartFile multipartFilex : multipartFileList) {
//                    String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_FOLDER, UploadDownloadConstant.THUC_DON);


                    String extension = FilenameUtils.getExtension(multipartFilex.getOriginalFilename());
                    if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpeg")) {
                        HandleFileResponse handleFileResponse = HandleFileUtils.getUrlFieldOrPictureSaved(multipartFilex, idSchool, UploadDownloadConstant.HOC_TAP);

                        UrlScheuldeFile urlScheuldeFile = new UrlScheuldeFile();
                        urlScheuldeFile.setNamePicture(handleFileResponse.getName());
                        urlScheuldeFile.setUrlPicture(handleFileResponse.getUrlWeb());
                        urlScheuldeFile.setUrlLocalPicture(handleFileResponse.getUrlLocal());
                        scheduleFileHashSet.add(scheduleFile);
                        urlScheduleFileRepository.save(urlScheuldeFile);
                        urlScheuldeFileHashSet.add(urlScheuldeFile);
                    } else {
                        HandleFileResponse handleFileResponse = HandleFileUtils.getUrlFieldOrPictureSaved(multipartFilex, idSchool, UploadDownloadConstant.HOC_TAP);

                        UrlScheuldeFile urlScheuldeFile = new UrlScheuldeFile();
                        urlScheuldeFile.setNameFile(handleFileResponse.getName());
                        urlScheuldeFile.setUrlFile(handleFileResponse.getUrlWeb());
                        urlScheuldeFile.setUrlLocalFile(handleFileResponse.getUrlLocal());
                        scheduleFileHashSet.add(scheduleFile);
                        urlScheduleFileRepository.save(urlScheuldeFile);
                        urlScheuldeFileHashSet.add(urlScheuldeFile);
                    }
                }
                scheduleFile.setUrlScheuldeFileList(urlScheuldeFileHashSet);
                scheduleFileRepository.save(scheduleFile);

            }
        }
        return true;
    }

    private List<ScheduleInClassWeekResponse> setScheduleInClassWeekResponse(SearchScheduleInClassRequest searchScheduleInClassRequest, Optional<MaClass> maClassOptional) {

        if (maClassOptional.isEmpty()) {
            return null;
        }
        /**
         * Tạo list các thời khóa biểu tuần trong 1 lớp
         */
        List<ScheduleInClassWeekResponse> scheduleInClassWeekResponseList = new ArrayList<>();

        /**
         * Khai báo thời khóa biểu của từng thứ trong 1 lớp
         */
        List<ScheduleInClassResponse> scheduleInClassMondayResponseList = new ArrayList<>();//Thứ 2
        List<ScheduleInClassResponse> scheduleInClassTuesdayResponseList = new ArrayList<>();//Thứ 3
        List<ScheduleInClassResponse> scheduleInClassWednesdayResponseList = new ArrayList<>();//Thứ 4
        List<ScheduleInClassResponse> scheduleInClassThursdayResponseList = new ArrayList<>();//Thứ 5
        List<ScheduleInClassResponse> scheduleInClassFridayResponseList = new ArrayList<>();//Thứ 6
        List<ScheduleInClassResponse> scheduleInClassSaturdayResponseList = new ArrayList<>();//Thứ 7
        List<ScheduleInClassResponse> scheduleInClassSundayResponseList = new ArrayList<>();//CN

        LocalDate monday = null;
        LocalDate tuesday = null;
        LocalDate wednesday = null;
        LocalDate thursday = null;
        LocalDate friday = null;
        LocalDate saturday = null;
        LocalDate sunday = null;

        /**
         * Truyền vào giá trị thời gian ngày đầu tuần parse sang Localdate
         */
        if (StringUtils.isNotBlank(searchScheduleInClassRequest.getTimeSchedule())) {
            monday = LocalDate.parse(searchScheduleInClassRequest.getTimeSchedule());
            tuesday = monday.plusDays(1);
            wednesday = tuesday.plusDays(1);
            thursday = wednesday.plusDays(1);
            friday = thursday.plusDays(1);
            saturday = friday.plusDays(1);
            sunday = saturday.plusDays(1);
        }
        MaClass maClass = maClassOptional.get();
        /**
         * Nếu lớp đó có thời khóa biểu
         */
        if (!CollectionUtils.isEmpty(maClass.getClassScheduleList())) {
            /**
             * Chạy vòng lặp các thời khóa biểu trong lớp
             */
            LocalDate finalMonday = monday;
            LocalDate finalTuesday = tuesday;
            LocalDate finalWednesday = wednesday;
            LocalDate finalThursday = thursday;
            LocalDate finalFriday = friday;
            LocalDate finalSaturday = saturday;
            LocalDate finalSunday = sunday;
            /**
             * Lọc ra thời khóa biểu thứ 2 của lớp đó
             */
            List<ClassSchedule> classScheduleMondayList = maClass.getClassScheduleList().stream().filter(x -> finalMonday.isEqual(x.getScheduleDate())).collect(Collectors.toList());

            /**
             * Lọc ra thời khóa biểu thứ 3 của lớp đó
             */
            List<ClassSchedule> classScheduleTuesdayList = maClass.getClassScheduleList().stream().filter(x -> finalTuesday.isEqual(x.getScheduleDate())).collect(Collectors.toList());

            /**
             * Lọc ra thời khóa biểu thứ 4 của lớp đó
             */
            List<ClassSchedule> classScheduleWednesdayList = maClass.getClassScheduleList().stream().filter(x -> finalWednesday.isEqual(x.getScheduleDate())).collect(Collectors.toList());

            /**
             * Lọc ra thời khóa biểu thứ 5 của lớp đó
             */
            List<ClassSchedule> classScheduleThursdayList = maClass.getClassScheduleList().stream().filter(x -> finalThursday.isEqual(x.getScheduleDate())).collect(Collectors.toList());

            /**
             * Lọc ra thời khóa biểu thứ 6 của lớp đó
             */
            List<ClassSchedule> classScheduleFridayList = maClass.getClassScheduleList().stream().filter(x -> finalFriday.isEqual(x.getScheduleDate())).collect(Collectors.toList());

            /**
             * Lọc ra thời khóa biểu thứ 7 của lớp đó
             */
            List<ClassSchedule> classScheduleSaturdayList = maClass.getClassScheduleList().stream().filter(x -> finalSaturday.isEqual(x.getScheduleDate())).collect(Collectors.toList());

            /**
             * Lọc ra thời khóa biểu CN của lớp đó
             */
            List<ClassSchedule> classScheduleSundayList = maClass.getClassScheduleList().stream().filter(x -> finalSunday.isEqual(x.getScheduleDate())).collect(Collectors.toList());


            /**
             * Nếu thời khóa biểu thứ 2 không có
             */
            if (CollectionUtils.isEmpty(classScheduleMondayList)) {
                /**
                 * Tạo các buổi cho thứ 2
                 */
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ 2
                 */
                scheduleInClassMondayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassMondayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassMondayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassMondayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassMondayWeekResponse.setScheduleInClassResponseList(scheduleInClassMondayResponseList);
                scheduleInClassMondayWeekResponse.setScheduleDate(monday);
                scheduleInClassMondayWeekResponse.setIdClass(maClass.getId());

                //scheduleInClassMondayWeekResponse.setIdSchedule();
                /**
                 * Add thứ 2 vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassMondayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ 2 có
             */
            else {
                /**
                 * Thời khóa biểu thứ 2
                 */
                ClassSchedule scheduleMonday = classScheduleMondayList.get(0);
                /**
                 * Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 2(lấy từ class_schedule)
                 */
                List<ScheduleMorning> scheduleMorningList = scheduleMonday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleMonday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleMonday.getScheduleEveningList();


                /**
                 * Nếu thứ 2 đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                    scheduleInClassMorningResponse.setTimeSchedule(scheduleMonday.getScheduleDate().toString());
                    scheduleInClassMondayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleMonday.getScheduleMorningList()) {
                        /**
                         * Thời khóa biểu buổi sáng,chiều,tối
                         */
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleMonday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassMondayResponseList.add(scheduleInClassMorningResponse);
                    }
                }

                /**
                 * Nếu thứ 2 đó không có thời khóa biểu buổi chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleMonday.getScheduleDate().toString());
                    scheduleInClassMondayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleMonday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleMonday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassMondayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }

                /**
                 * Nếu thứ 2 đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleMonday.getScheduleDate().toString());
                    scheduleInClassMondayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleMonday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleMonday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassMondayResponseList.add(scheduleInClassEveningResponse);
                    }
                }

                ScheduleInClassWeekResponse scheduleInClassMondayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassMondayWeekResponse.setScheduleInClassResponseList(scheduleInClassMondayResponseList);
                scheduleInClassMondayWeekResponse.setIdSchedule(scheduleMonday.getId());
                scheduleInClassMondayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassMondayWeekResponse.setScheduleTitle(scheduleMonday.getScheduleTitle());

                scheduleInClassMondayWeekResponse.setScheduleDate(monday);

                scheduleInClassWeekResponseList.add(scheduleInClassMondayWeekResponse);
            }


            /**
             * Nếu thời khóa biểu thứ 3 không có
             */
            if (CollectionUtils.isEmpty(classScheduleTuesdayList)) {
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                /**
                 * Tạo các buổi cho thứ 3
                 */
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ 3
                 */
                scheduleInClassTuesdayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassTuesdayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassTuesdayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassTuesdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassTuesdayWeekResponse.setScheduleInClassResponseList(scheduleInClassTuesdayResponseList);
                scheduleInClassTuesdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassTuesdayWeekResponse.setScheduleDate(tuesday);
                /**
                 * Add thứ 3 vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassTuesdayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ 3 có
             */
            else {
                /**
                 * Thời khóa biểu thứ 3
                 */
                ClassSchedule scheduleTuesday = classScheduleTuesdayList.get(0);
                /**
                 * Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 2(lấy từ class_schedule)
                 */
                List<ScheduleMorning> scheduleMorningList = scheduleTuesday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleTuesday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleTuesday.getScheduleEveningList();

                /**
                 * Nếu thứ 3 đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                    scheduleInClassMorningResponse.setTimeSchedule(scheduleTuesday.getScheduleDate().toString());
                    scheduleInClassTuesdayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleTuesday.getScheduleMorningList()) {
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleTuesday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassTuesdayResponseList.add(scheduleInClassMorningResponse);
                    }
                }

                /**
                 * Nếu thứ 3 đó không có thời khóa biểu buổi chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleTuesday.getScheduleDate().toString());
                    scheduleInClassTuesdayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleTuesday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleTuesday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassTuesdayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }

                /**
                 * Nếu thứ 3 đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleTuesday.getScheduleDate().toString());
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassTuesdayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleTuesday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleTuesday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassTuesdayResponseList.add(scheduleInClassEveningResponse);
                    }
                }

                ScheduleInClassWeekResponse scheduleInClassTuesdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassTuesdayWeekResponse.setScheduleInClassResponseList(scheduleInClassTuesdayResponseList);
                scheduleInClassTuesdayWeekResponse.setIdSchedule(scheduleTuesday.getId());
                scheduleInClassTuesdayWeekResponse.setScheduleDate(tuesday);
                scheduleInClassTuesdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassTuesdayWeekResponse.setScheduleTitle(scheduleTuesday.getScheduleTitle());

                scheduleInClassWeekResponseList.add(scheduleInClassTuesdayWeekResponse);
            }

            /**
             * Nếu thời khóa biểu thứ 4 không có
             */
            if (CollectionUtils.isEmpty(classScheduleWednesdayList)) {
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                /**
                 * Tạo các buổi cho thứ 4
                 */
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());

                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());


                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ 4
                 */
                scheduleInClassWednesdayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassWednesdayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassWednesdayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassWednesdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassWednesdayWeekResponse.setScheduleInClassResponseList(scheduleInClassWednesdayResponseList);
                scheduleInClassWednesdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassWednesdayWeekResponse.setScheduleDate(wednesday);
                /**
                 * Add thứ 4 vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassWednesdayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ 4 có
             */
            else {
                /**
                 *Thời khóa biểu thứ 4
                 */
                ClassSchedule scheduleWenesday = classScheduleWednesdayList.get(0);
                /**
                 * Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 4(lấy từ class_schedule)
                 */
                List<ScheduleMorning> scheduleMorningList = scheduleWenesday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleWenesday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleWenesday.getScheduleEveningList();

                /**
                 * Nếu thứ 4 đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());

                    scheduleInClassMorningResponse.setTimeSchedule(scheduleWenesday.getScheduleDate().toString());
                    scheduleInClassWednesdayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleWenesday.getScheduleMorningList()) {
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleWenesday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassWednesdayResponseList.add(scheduleInClassMorningResponse);
                    }
                }

                /**
                 * Nếu thứ 4 đó không có thời khóa biểu buổi chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleWenesday.getScheduleDate().toString());
                    scheduleInClassWednesdayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleWenesday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleWenesday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassWednesdayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }

                /**
                 * Nếu thứ 4 đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    /**
                     * Thời khóa biểu buổi sáng,chiều,tối
                     */
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleWenesday.getScheduleDate().toString());
                    scheduleInClassWednesdayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleWenesday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleWenesday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassWednesdayResponseList.add(scheduleInClassEveningResponse);
                    }
                }

                ScheduleInClassWeekResponse scheduleInClassWednesdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassWednesdayWeekResponse.setScheduleInClassResponseList(scheduleInClassWednesdayResponseList);
                scheduleInClassWednesdayWeekResponse.setIdSchedule(scheduleWenesday.getId());
                scheduleInClassWednesdayWeekResponse.setScheduleDate(wednesday);
                scheduleInClassWednesdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassWednesdayWeekResponse.setScheduleTitle(scheduleWenesday.getScheduleTitle());


                scheduleInClassWeekResponseList.add(scheduleInClassWednesdayWeekResponse);
            }

            /**
             * Nếu thời khóa biểu thứ 5 không có
             */
            if (CollectionUtils.isEmpty(classScheduleThursdayList)) {
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                /**
                 * Tạo các buổi cho thứ 5
                 */
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ 5
                 */
                scheduleInClassThursdayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassThursdayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassThursdayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassThursdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassThursdayWeekResponse.setScheduleInClassResponseList(scheduleInClassThursdayResponseList);
                scheduleInClassThursdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassThursdayWeekResponse.setScheduleDate(thursday);
                /**
                 * Add thứ 5 vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassThursdayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ 5 có
             */
            else {
                //Thời khóa biểu thứ 5
                ClassSchedule scheduleThursday = classScheduleThursdayList.get(0);
                //Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 2(lấy từ class_schedule)
                List<ScheduleMorning> scheduleMorningList = scheduleThursday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleThursday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleThursday.getScheduleEveningList();

                /**
                 * Nếu thứ 5 đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                    scheduleInClassMorningResponse.setTimeSchedule(scheduleThursday.getScheduleDate().toString());
                    scheduleInClassThursdayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleThursday.getScheduleMorningList()) {
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleThursday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassThursdayResponseList.add(scheduleInClassMorningResponse);
                    }
                }

                /**
                 * Nếu thứ 5 đó không có thời khóa biểu buổi chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleThursday.getScheduleDate().toString());
                    scheduleInClassThursdayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleThursday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleThursday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassThursdayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }
                /**
                 * Nếu thứ 5 đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleThursday.getScheduleDate().toString());
                    scheduleInClassThursdayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleThursday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleThursday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassThursdayResponseList.add(scheduleInClassEveningResponse);
                    }
                }
                ScheduleInClassWeekResponse scheduleInClassThursdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassThursdayWeekResponse.setScheduleInClassResponseList(scheduleInClassThursdayResponseList);
                scheduleInClassThursdayWeekResponse.setScheduleDate(thursday);
                scheduleInClassThursdayWeekResponse.setIdSchedule(scheduleThursday.getId());
                scheduleInClassThursdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassThursdayWeekResponse.setScheduleTitle(scheduleThursday.getScheduleTitle());


                scheduleInClassWeekResponseList.add(scheduleInClassThursdayWeekResponse);
            }

            /**
             * Nếu thời khóa biểu thứ 6 không có
             */
            if (CollectionUtils.isEmpty(classScheduleFridayList)) {
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                /**
                 * Tạo các buổi cho thứ 6
                 */
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ 6
                 */
                scheduleInClassFridayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassFridayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassFridayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassFridayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassFridayWeekResponse.setScheduleInClassResponseList(scheduleInClassFridayResponseList);
                scheduleInClassFridayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassFridayWeekResponse.setScheduleDate(friday);
                /**
                 * Add thứ 6 vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassFridayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ 6 có
             */
            else {
                /**
                 * Thời khóa biểu thứ 6
                 */
                ClassSchedule scheduleFriday = classScheduleFridayList.get(0);
                //Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 2(lấy từ class_schedule)
                List<ScheduleMorning> scheduleMorningList = scheduleFriday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleFriday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleFriday.getScheduleEveningList();

                /**
                 * Nếu thứ 6 đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                    scheduleInClassMorningResponse.setTimeSchedule(scheduleFriday.getScheduleDate().toString());
                    scheduleInClassFridayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleFriday.getScheduleMorningList()) {
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleFriday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassFridayResponseList.add(scheduleInClassMorningResponse);
                    }
                }
                /**
                 * Nếu thứ 6 đó không có thời khóa biểu buổi chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleFriday.getScheduleDate().toString());
                    scheduleInClassFridayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleFriday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleFriday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassFridayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }

                /**
                 * Nếu thứ 6 đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleFriday.getScheduleDate().toString());
                    scheduleInClassFridayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleFriday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleFriday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassFridayResponseList.add(scheduleInClassEveningResponse);
                    }
                }

                ScheduleInClassWeekResponse scheduleInClassFridayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassFridayWeekResponse.setScheduleInClassResponseList(scheduleInClassFridayResponseList);
                scheduleInClassFridayWeekResponse.setIdSchedule(scheduleFriday.getId());
                scheduleInClassFridayWeekResponse.setScheduleDate(friday);
                scheduleInClassFridayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassFridayWeekResponse.setScheduleTitle(scheduleFriday.getScheduleTitle());

                scheduleInClassWeekResponseList.add(scheduleInClassFridayWeekResponse);
            }

            /**
             * Nếu thời khóa biểu thứ 7 không có
             */
            if (CollectionUtils.isEmpty(classScheduleSaturdayList)) {
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                /**
                 * Tạo các buổi cho thứ 7
                 */
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ 7
                 */
                scheduleInClassSaturdayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassSaturdayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassSaturdayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassSaturdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassSaturdayWeekResponse.setScheduleInClassResponseList(scheduleInClassSaturdayResponseList);
                scheduleInClassSaturdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassSaturdayWeekResponse.setScheduleDate(saturday);
                /**
                 * Add thứ 7 vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassSaturdayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ 7 có
             */
            else {
                /**
                 * Thời khóa biểu thứ 7
                 */
                ClassSchedule scheduleSaturday = classScheduleSaturdayList.get(0);
                //Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 7(lấy từ class_schedule)
                List<ScheduleMorning> scheduleMorningList = scheduleSaturday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleSaturday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleSaturday.getScheduleEveningList();


                /**
                 * Nếu thứ 7 đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                    scheduleInClassMorningResponse.setTimeSchedule(scheduleSaturday.getScheduleDate().toString());
                    scheduleInClassSaturdayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleSaturday.getScheduleMorningList()) {
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleSaturday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassSaturdayResponseList.add(scheduleInClassMorningResponse);
                    }
                }
                /**
                 * Nếu thứ 7 đó không có thời khóa biểu buổi Chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleSaturday.getScheduleDate().toString());
                    scheduleInClassSaturdayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleSaturday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleSaturday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassSaturdayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }

                /**
                 * Nếu thứ 7 đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleSaturday.getScheduleDate().toString());
                    scheduleInClassSaturdayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleSaturday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleSaturday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassSaturdayResponseList.add(scheduleInClassEveningResponse);
                    }
                }

                ScheduleInClassWeekResponse scheduleInClassSaturdayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassSaturdayWeekResponse.setScheduleInClassResponseList(scheduleInClassSaturdayResponseList);
                scheduleInClassSaturdayWeekResponse.setScheduleDate(saturday);
                scheduleInClassSaturdayWeekResponse.setIdSchedule(scheduleSaturday.getId());
                scheduleInClassSaturdayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassSaturdayWeekResponse.setScheduleTitle(scheduleSaturday.getScheduleTitle());

                scheduleInClassWeekResponseList.add(scheduleInClassSaturdayWeekResponse);
            }

            /**
             * Nếu thời khóa biểu thứ CN không có
             */
            if (CollectionUtils.isEmpty(classScheduleSundayList)) {
                ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                /**
                 * Tạo các buổi cho thứ CN
                 */
                scheduleInClassMorningResponse.setSessionDay("Sáng");
                scheduleInClassMorningResponse.setIdClass(maClass.getId());
                scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                scheduleInClassEveningResponse.setSessionDay("Tối");
                scheduleInClassEveningResponse.setIdClass(maClass.getId());
                scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                /**
                 * Add Các buổi vào thứ CN
                 */
                scheduleInClassSundayResponseList.add(scheduleInClassMorningResponse);
                scheduleInClassSundayResponseList.add(scheduleInClassAfternoonResponse);
                scheduleInClassSundayResponseList.add(scheduleInClassEveningResponse);

                ScheduleInClassWeekResponse scheduleInClassSundayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassSundayWeekResponse.setScheduleInClassResponseList(scheduleInClassSundayResponseList);
                scheduleInClassSundayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassSundayWeekResponse.setScheduleDate(sunday);
                /**
                 * Add thứ CN vào tuần đó
                 */
                scheduleInClassWeekResponseList.add(scheduleInClassSundayWeekResponse);

            }
            /**
             * Nếu thời khóa biểu thứ CN có
             */
            else {
                /**
                 * Thời khóa biểu thứ CN
                 */
                ClassSchedule scheduleSunday = classScheduleSundayList.get(0);
                //Mảng các thời khóa biểu buổi sáng,chiều,tối thứ 7(lấy từ class_schedule)
                List<ScheduleMorning> scheduleMorningList = scheduleSunday.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = scheduleSunday.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = scheduleSunday.getScheduleEveningList();

                /**
                 * Nếu thứ CN đó không có thời khóa biểu buổi sáng
                 */
                if (CollectionUtils.isEmpty(scheduleMorningList)) {
                    ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                    scheduleInClassMorningResponse.setSessionDay("Sáng");
                    scheduleInClassMorningResponse.setIdClass(maClass.getId());
                    scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                    scheduleInClassMorningResponse.setTimeSchedule(scheduleSunday.getScheduleDate().toString());
                    scheduleInClassSundayResponseList.add(scheduleInClassMorningResponse);
                } else {
                    for (ScheduleMorning scheduleMorning : scheduleSunday.getScheduleMorningList()) {
                        ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
                        scheduleInClassMorningResponse.setSessionDay("Sáng");
                        scheduleInClassMorningResponse.setIdClass(maClass.getId());
                        scheduleInClassMorningResponse.setClassName(maClass.getClassName());
                        scheduleInClassMorningResponse.setTimeSchedule(scheduleSunday.getScheduleDate().toString());
                        scheduleInClassMorningResponse.setTimeContent(scheduleMorning.getTime());
                        scheduleInClassMorningResponse.setContentSchedule(scheduleMorning.getContent());
                        scheduleInClassSundayResponseList.add(scheduleInClassMorningResponse);
                    }
                }

                /**
                 * Nếu thứ CN đó không có thời khóa biểu buổi chiều
                 */
                if (CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                    scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                    scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                    scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                    scheduleInClassAfternoonResponse.setTimeSchedule(scheduleSunday.getScheduleDate().toString());
                    scheduleInClassSundayResponseList.add(scheduleInClassAfternoonResponse);
                } else {
                    for (ScheduleAfternoon scheduleAfternoon : scheduleSunday.getScheduleAfternoonList()) {
                        ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
                        scheduleInClassAfternoonResponse.setSessionDay("Chiều");
                        scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
                        scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
                        scheduleInClassAfternoonResponse.setTimeSchedule(scheduleSunday.getScheduleDate().toString());
                        scheduleInClassAfternoonResponse.setTimeContent(scheduleAfternoon.getTime());
                        scheduleInClassAfternoonResponse.setContentSchedule(scheduleAfternoon.getContent());
                        scheduleInClassSundayResponseList.add(scheduleInClassAfternoonResponse);
                    }
                }

                /**
                 * Nếu thứ CN đó không có thời khóa biểu buổi tối
                 */
                if (CollectionUtils.isEmpty(scheduleEveningList)) {
                    ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                    scheduleInClassEveningResponse.setSessionDay("Tối");
                    scheduleInClassEveningResponse.setIdClass(maClass.getId());
                    scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                    scheduleInClassEveningResponse.setTimeSchedule(scheduleSunday.getScheduleDate().toString());
                    scheduleInClassSundayResponseList.add(scheduleInClassEveningResponse);
                } else {
                    for (ScheduleEvening scheduleEvening : scheduleSunday.getScheduleEveningList()) {
                        ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
                        scheduleInClassEveningResponse.setSessionDay("Tối");
                        scheduleInClassEveningResponse.setIdClass(maClass.getId());
                        scheduleInClassEveningResponse.setClassName(maClass.getClassName());
                        scheduleInClassEveningResponse.setTimeSchedule(scheduleSunday.getScheduleDate().toString());
                        scheduleInClassEveningResponse.setTimeContent(scheduleEvening.getTime());
                        scheduleInClassEveningResponse.setContentSchedule(scheduleEvening.getContent());
                        scheduleInClassSundayResponseList.add(scheduleInClassEveningResponse);
                    }
                }

                ScheduleInClassWeekResponse scheduleInClassSundayWeekResponse = new ScheduleInClassWeekResponse();
                scheduleInClassSundayWeekResponse.setScheduleInClassResponseList(scheduleInClassSundayResponseList);
                scheduleInClassSundayWeekResponse.setScheduleDate(sunday);
                scheduleInClassSundayWeekResponse.setIdSchedule(scheduleSunday.getId());
                scheduleInClassSundayWeekResponse.setIdClass(maClass.getId());
                scheduleInClassSundayWeekResponse.setScheduleTitle(scheduleSunday.getScheduleTitle());
                scheduleInClassWeekResponseList.add(scheduleInClassSundayWeekResponse);
            }
        }
        /**
         * Nếu lớp đó không có thời khóa biểu
         */
        else {
            /**
             * Tạo các buổi cho thứ 2
             */
            ScheduleInClassResponse scheduleInClassMorningResponse = new ScheduleInClassResponse();
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            ScheduleInClassResponse scheduleInClassAfternoonResponse = new ScheduleInClassResponse();
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            ScheduleInClassResponse scheduleInClassEveningResponse = new ScheduleInClassResponse();
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());

            /**
             * Add Các buổi vào thứ 2
             */
            scheduleInClassMondayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassMondayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassMondayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassMondayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassMondayWeekResponse.setScheduleInClassResponseList(scheduleInClassMondayResponseList);
            scheduleInClassMondayWeekResponse.setScheduleDate(monday);
            scheduleInClassMondayWeekResponse.setIdClass(maClass.getId());
            /**
             * Add thứ 2 vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassMondayWeekResponse);


            /**
             * Tạo các buổi cho thứ 3
             */
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());
            /**
             * Add Các buổi vào thứ 3
             */
            scheduleInClassTuesdayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassTuesdayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassTuesdayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassTuesdayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassTuesdayWeekResponse.setScheduleInClassResponseList(scheduleInClassTuesdayResponseList);
            scheduleInClassTuesdayWeekResponse.setScheduleDate(tuesday);
            scheduleInClassTuesdayWeekResponse.setIdClass(maClass.getId());
            /**
             * Add thứ 3 vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassTuesdayWeekResponse);

            /**
             * Tạo các buổi cho thứ 4
             */
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());
            /**
             * Add Các buổi vào thứ 4
             */
            scheduleInClassWednesdayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassWednesdayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassWednesdayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassWednesdayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassWednesdayWeekResponse.setScheduleInClassResponseList(scheduleInClassWednesdayResponseList);
            scheduleInClassWednesdayWeekResponse.setScheduleDate(wednesday);
            scheduleInClassWednesdayWeekResponse.setIdClass(maClass.getId());
            /**
             * Add thứ 4 vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassWednesdayWeekResponse);

            /**
             * Tạo các buổi cho thứ 5
             */
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());

            /**
             * Add Các buổi vào thứ 5
             */
            scheduleInClassThursdayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassThursdayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassThursdayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassThursdayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassThursdayWeekResponse.setScheduleInClassResponseList(scheduleInClassThursdayResponseList);
            scheduleInClassThursdayWeekResponse.setScheduleDate(thursday);
            scheduleInClassThursdayWeekResponse.setIdClass(maClass.getId());
            /**
             * Add thứ 5 vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassThursdayWeekResponse);

            /**
             * Tạo các buổi cho thứ 6
             */
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());
            /**
             * Add Các buổi vào thứ 6
             */
            scheduleInClassFridayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassFridayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassFridayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassFridayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassFridayWeekResponse.setScheduleInClassResponseList(scheduleInClassFridayResponseList);
            scheduleInClassFridayWeekResponse.setScheduleDate(friday);
            scheduleInClassFridayWeekResponse.setIdClass(maClass.getId());
            /**
             * Add thứ 6 vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassFridayWeekResponse);

            /**
             * Tạo các buổi cho thứ 7
             */
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());
            /**
             * Add Các buổi vào thứ 7
             */
            scheduleInClassSaturdayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassSaturdayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassSaturdayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassSaturdayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassSaturdayWeekResponse.setScheduleInClassResponseList(scheduleInClassSaturdayResponseList);
            scheduleInClassSaturdayWeekResponse.setScheduleDate(saturday);
            scheduleInClassSaturdayWeekResponse.setIdClass(maClass.getId());
            /**
             * Add thứ 7 vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassSaturdayWeekResponse);

            /**
             * Tạo các buổi cho thứ CN
             */
            scheduleInClassMorningResponse.setSessionDay("Sáng");
            scheduleInClassMorningResponse.setIdClass(maClass.getId());
            scheduleInClassMorningResponse.setClassName(maClass.getClassName());
            scheduleInClassAfternoonResponse.setSessionDay("Chiều");
            scheduleInClassAfternoonResponse.setIdClass(maClass.getId());
            scheduleInClassAfternoonResponse.setClassName(maClass.getClassName());
            scheduleInClassEveningResponse.setSessionDay("Tối");
            scheduleInClassEveningResponse.setIdClass(maClass.getId());
            scheduleInClassEveningResponse.setClassName(maClass.getClassName());
            /**
             * Add Các buổi vào thứ CN
             */
            scheduleInClassSundayResponseList.add(scheduleInClassMorningResponse);
            scheduleInClassSundayResponseList.add(scheduleInClassAfternoonResponse);
            scheduleInClassSundayResponseList.add(scheduleInClassEveningResponse);

            ScheduleInClassWeekResponse scheduleInClassSundayWeekResponse = new ScheduleInClassWeekResponse();
            scheduleInClassSundayWeekResponse.setScheduleInClassResponseList(scheduleInClassSundayResponseList);
            scheduleInClassSundayWeekResponse.setScheduleDate(sunday);
            scheduleInClassSundayWeekResponse.setIdClass(maClass.getId());
//            scheduleInClassSundayWeekResponse.setScheduleTitle(maClass.getS);

            /**
             * Add thứ CN vào tuần đó
             */
            scheduleInClassWeekResponseList.add(scheduleInClassSundayWeekResponse);
        }

        return scheduleInClassWeekResponseList;
    }

    private Map<String, Integer> getRowMax(List<ScheduleInClassWeekResponse> scheduleInClassWeekResponses) {
        List<Integer> MorningList = new ArrayList();
        List<Integer> AfternoonList = new ArrayList();
        List<Integer> EveningList = new ArrayList();

        for (ScheduleInClassWeekResponse scheduleInClassWeekResponse : scheduleInClassWeekResponses) {
            Integer numberMorning = 0;
            Integer numberAfternoon = 0;
            Integer numberEvening = 0;

            // check sessionday là sáng thì tăng row buổi sáng
            for (ScheduleInClassResponse scheduleInClassResponse : scheduleInClassWeekResponse.getScheduleInClassResponseList()) {
                if (scheduleInClassResponse.getSessionDay().equalsIgnoreCase("Sáng") && scheduleInClassResponse.getContentSchedule() != null) {
                    numberMorning++;
                } else if (scheduleInClassResponse.getSessionDay().equalsIgnoreCase("Chiều") && scheduleInClassResponse.getContentSchedule() != null) {
                    numberAfternoon++;
                } else if (scheduleInClassResponse.getSessionDay().equalsIgnoreCase("Tối") && scheduleInClassResponse.getContentSchedule() != null) {
                    numberEvening++;
                }

            }
            MorningList.add(numberMorning);
            AfternoonList.add(numberAfternoon);
            EveningList.add(numberEvening);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("Sáng", Collections.max(MorningList));
        map.put("Chiều", Collections.max(AfternoonList));
        map.put("Tối", Collections.max(EveningList));

        return map;
    }

    private List<ScheduleModel> getDays(List<ScheduleInClassWeekResponse> scheduleInClassWeekResponses, int maxMorning, int maxAfternoon, int maxEvening) {

        List<ScheduleModel> listSchedule = new ArrayList<>();
        List<String> listMondays = new ArrayList<>();
        List<String> listTuesdays = new ArrayList<>();
        List<String> listWednesdays = new ArrayList<>();
        List<String> listThursdays = new ArrayList<>();
        List<String> listFridays = new ArrayList<>();
        List<String> listSaturdays = new ArrayList<>();
        List<String> listSundays = new ArrayList<>();
        int maxMorningTemp = maxMorning;
        int maxAfternoonTemp = maxAfternoon;
        int maxEveningTemp = maxEvening;

        for (int i = 0; i < scheduleInClassWeekResponses.size(); i++) {

            maxMorning = maxMorningTemp;
            maxAfternoon = maxAfternoonTemp;
            maxEvening = maxEveningTemp;

            ScheduleInClassWeekResponse scheduleInClassWeekResponse = scheduleInClassWeekResponses.get(i);
            List<String> list = new ArrayList<>();
            int col = 1;
            int m = 0;
            for (int j = 0; j <= maxMorning + maxAfternoon + maxEvening; j++) {

                List<ScheduleInClassResponse> scheduleInClassResponses = scheduleInClassWeekResponse.getScheduleInClassResponseList();

                if (col <= maxMorning || j == 0) {
                    if (maxMorningTemp == 0) {
                        col = maxMorning;
                        maxAfternoon++;
                    } else if (maxMorningTemp != 0 && maxAfternoonTemp == 0 && col == 1) {
                        maxAfternoon++;
                    }
                    if (scheduleInClassResponses.get(m).getSessionDay().equalsIgnoreCase("Sáng") && scheduleInClassResponses.get(m).getContentSchedule() != null) {
                        if (scheduleInClassResponses.get(m).getTimeContent() != null) {
                            list.add("*" + scheduleInClassResponses.get(m).getTimeContent() + "\n" + scheduleInClassResponses.get(m).getContentSchedule());

                        } else {
                            list.add(" \n" + scheduleInClassResponses.get(j).getContentSchedule());

                        }

                    } else {
                        for (int k = col; k <= maxMorning; k++) {
                            list.add("");
                            if (m >= 1) {
                                m--;
                            }

                            if (k < maxMorning) {
                                col++;
                            }
                        }

                    }

                } else if ((col > maxMorning && col <= maxMorning + maxAfternoon)) {
                    if (maxMorningTemp != 0 && maxAfternoonTemp == 0 && col == maxMorningTemp + 1) {
                        maxEvening++;
                    } else if (maxMorningTemp == 0 && maxAfternoonTemp == 0 && col == maxMorningTemp + 1) {
                        maxMorning++;
                        col = maxMorning + maxAfternoon;
                        maxEvening++;

                    } else if (maxMorningTemp == 0 && col == maxMorningTemp + 1) {
                        maxMorning++;
                        col++;
                        maxAfternoon--;

                    }
                    if (scheduleInClassResponses.get(m).getSessionDay().equalsIgnoreCase("Chiều") && scheduleInClassResponses.get(m).getContentSchedule() != null) {
                        if (scheduleInClassResponses.get(m).getTimeContent() != null) {
                            list.add("*" + scheduleInClassResponses.get(m).getTimeContent() + "\n" + scheduleInClassResponses.get(m).getContentSchedule());
                        } else {
                            list.add(" \n" + scheduleInClassResponses.get(m).getContentSchedule());
                        }

                    } else {
                        for (int k = col; k <= maxMorning + maxAfternoon; k++) {
                            list.add("");
                            if (m >= 0) {
                                if (maxMorningTemp != 0 && maxAfternoonTemp == 0 || maxMorningTemp == 0 && maxAfternoonTemp != 0 || maxMorning == maxMorningTemp && maxAfternoon == maxAfternoonTemp || maxMorning == 1 && maxAfternoon == 1) {
                                    m = 1;
                                } else {
                                    m--;
                                }
                            }
                            if (k < maxMorning + maxAfternoon) {
                                col++;
                            }


                        }

                    }

                } else if (col > maxMorning + maxAfternoon && col <= maxMorning + maxAfternoon + maxEvening) {
                    if (maxMorningTemp != 0 && maxAfternoonTemp == 0 && col == maxMorning + maxAfternoon + 1) {
                        maxEvening--;
                    } else if (maxMorningTemp == 0 && maxAfternoonTemp == 0 && col == maxMorning + maxAfternoon + 1) {
                        col = maxMorning + maxAfternoon + 1;
                        maxEvening--;
                    } else if (maxAfternoonTemp == 0 && col == maxMorning + maxAfternoon + 1) {
                        maxAfternoon++;
                        col++;
                        maxEvening--;
                    }
                    if (scheduleInClassResponses.size() > m) {
                        if (scheduleInClassResponses.get(m).getSessionDay().equalsIgnoreCase("Tối") && scheduleInClassResponses.get(m).getContentSchedule() != null) {
                            if (scheduleInClassResponses.get(m).getTimeContent() != null) {
                                list.add("*" + scheduleInClassResponses.get(m).getTimeContent() + "\n" + scheduleInClassResponses.get(m).getContentSchedule());
                            } else {
                                list.add(" \n" + scheduleInClassResponses.get(m).getContentSchedule());
                            }

                        } else {
                            for (int k = col; k <= maxMorning + maxAfternoon + maxEvening; k++) {
                                list.add("");
                                if (m >= 0) {
                                    m--;
                                }
                                col++;
                            }

                        }
                    } else {
                        for (int k = col; k <= maxMorning + maxAfternoon + maxEvening; k++) {
                            list.add("");
                            if (m >= 0) {
                                m--;
                            }
                            col++;
                        }
                    }
                }

                m++;
                col++;
            }
            switch (i) {
                case 0:
                    list.forEach(s -> listMondays.add(s));
                    list.clear();
                    break;
                case 1:
                    list.forEach(s -> listTuesdays.add(s));
                    list.clear();
                    break;
                case 2:
                    list.forEach(s -> listWednesdays.add(s));
                    list.clear();
                    break;
                case 3:
                    list.forEach(s -> listThursdays.add(s));
                    list.clear();
                    break;
                case 4:
                    list.forEach(s -> listFridays.add(s));
                    list.clear();
                    break;
                case 5:
                    list.forEach(s -> listSaturdays.add(s));
                    list.clear();
                    break;
                case 6:
                    list.forEach(s -> listSundays.add(s));
                    list.clear();
                    break;
                default:
                    list.clear();
                    break;
            }

        }

        maxMorning = maxMorningTemp;
        maxAfternoon = maxAfternoonTemp;
        maxEvening = maxEveningTemp;
        if (maxMorningTemp == 0) maxMorning = 1;
        if (maxAfternoonTemp == 0) maxAfternoon = 1;

        for (int k = 0; k < maxMorning + maxEvening + maxAfternoon; k++) {

            ScheduleModel scheduleModel = new ScheduleModel();

            scheduleModel.setContentMonday(listMondays.get(k));
            scheduleModel.setContentTuesday(listTuesdays.get(k));
            scheduleModel.setContentWednesday(listWednesdays.get(k));
            scheduleModel.setContentThursday(listThursdays.get(k));
            scheduleModel.setContentFriday(listFridays.get(k));
            scheduleModel.setContentSaturday(listSaturdays.get(k));
            scheduleModel.setContentSunday(listSundays.get(k));
            listSchedule.add(scheduleModel);


        }
        return listSchedule;
    }
}
