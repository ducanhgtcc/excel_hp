package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AttendanceConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.classes.*;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.AttendanceKids;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.schedule.ScheduleDatePlusRequest;
import com.example.onekids_project.mobile.plus.response.schedule.ScheduleClassResponse;
import com.example.onekids_project.mobile.plus.response.schedule.ScheduleClassWeekResponse;
import com.example.onekids_project.mobile.plus.response.schedule.ScheduleDatePlusResponse;
import com.example.onekids_project.mobile.plus.response.schedule.ScheduleWeekPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.SchedulePlusService;
import com.example.onekids_project.mobile.request.ScheduleFileRequest;
import com.example.onekids_project.mobile.response.*;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.attendancekids.AttendanceConfigResponse;
import com.example.onekids_project.response.common.FileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AttendanceKidsUtil;
import com.example.onekids_project.util.BeanDataUtils;
import com.example.onekids_project.util.ClassFeatureUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SchedulePlusServiceImpl implements SchedulePlusService {

    @Autowired
    private ClassScheduleRepository scheduleRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private AttendanceConfigRepository attendanceConfigRepository;

    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Autowired
    private ScheduleFileRepository scheduleFileRepository;

    @Override
    public List<ScheduleClassResponse> searchScheduleClass(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataPlus(principal);
        List<ScheduleClassResponse> responseList = new ArrayList<>();
        List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(principal.getIdSchoolLogin());
        maClassList.forEach(x -> {
            ScheduleClassResponse response = new ScheduleClassResponse();
            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByExEmployeeClassList_MaClass_IdAndDelActiveTrue(x.getId());
            response.setFeatureClassResponse(ClassFeatureUtils.setFeatureClass(infoEmployeeSchoolList, x));
            List<ClassSchedule> scheduleList = scheduleRepository.findByIsApprovedTrueAndMaClass_IdAndScheduleDateAndDelActiveTrue(x.getId(), localDate);
            response.setSchedule(AppConstant.APP_FALSE);
            if (!CollectionUtils.isEmpty(scheduleList)) {
                if (!CollectionUtils.isEmpty(scheduleList.get(0).getScheduleMorningList()) || !CollectionUtils.isEmpty(scheduleList.get(0).getScheduleAfternoonList()) || !CollectionUtils.isEmpty(scheduleList.get(0).getScheduleEveningList())) {
                    response.setSchedule(AppConstant.APP_TRUE);
                }
            }
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public ScheduleDatePlusResponse searchScheduleDate(UserPrincipal principal, ScheduleDatePlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        ScheduleDatePlusResponse response = new ScheduleDatePlusResponse();
        List<ScheduleDateResponse> morningList = new ArrayList<>();
        List<ScheduleDateResponse> afternoonList = new ArrayList<>();
        List<ScheduleDateResponse> eveningList = new ArrayList<>();
        ClassSchedule classSchedule = scheduleRepository.findScheduleDate(principal.getIdSchoolLogin(), request.getIdClass(), request.getDate());     // get ClassSchedule follow ScheduleRepository
        if (classSchedule != null){
            List<ScheduleMorning> scheduleMorningList = classSchedule.getScheduleMorningList();
            List<ScheduleAfternoon> scheduleAfternoonList = classSchedule.getScheduleAfternoonList();
            List<ScheduleEvening> scheduleEveningList = classSchedule.getScheduleEveningList();
            if (!CollectionUtils.isEmpty(scheduleMorningList)) {
                morningList = listMapper.mapList(classSchedule.getScheduleMorningList(), ScheduleDateResponse.class);
            }
            if (!CollectionUtils.isEmpty(scheduleAfternoonList)) {
                afternoonList = listMapper.mapList(classSchedule.getScheduleAfternoonList(), ScheduleDateResponse.class);
            }
            if (!CollectionUtils.isEmpty(scheduleEveningList)) {
                eveningList = listMapper.mapList(classSchedule.getScheduleEveningList(), ScheduleDateResponse.class);
            }
            response.setTitle(StringUtils.isNotBlank(classSchedule.getScheduleTitle()) ? classSchedule.getScheduleTitle() : "");
        }else {
            response.setTitle("");
        }
        response.setMorningList(morningList);
        response.setAfternoonList(afternoonList);
        response.setEveningList(eveningList);
        return response;
    }

    @Override
    public List<Integer> searchScheduleMonth(UserPrincipal principal, ScheduleDatePlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<Integer> responseList = new ArrayList<>();
        List<ClassSchedule> classScheduleList = scheduleRepository.findClassScheduleMonthList(principal.getIdSchoolLogin(), request.getIdClass(), request.getDate().getMonthValue(), request.getDate().getYear());
        classScheduleList.forEach(x -> {
            List<ScheduleMorning> scheduleMorningList = x.getScheduleMorningList();
            List<ScheduleAfternoon> scheduleAfternoonList = x.getScheduleAfternoonList();
            List<ScheduleEvening> scheduleEveningList = x.getScheduleEveningList();
            if (!CollectionUtils.isEmpty(scheduleMorningList) || !CollectionUtils.isEmpty(scheduleAfternoonList) || !CollectionUtils.isEmpty(scheduleEveningList)) {
                responseList.add(x.getScheduleDate().getDayOfMonth());
            }
        });
        return responseList;
    }

    @Override
    public List<ScheduleClassWeekResponse> searchScheduleClassWeek(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataPlus(principal);
        List<ScheduleClassWeekResponse> responseList = new ArrayList<>();
        LocalDate monday = ConvertData.getMondayOfWeek(localDate);
        LocalDate dateEnd = monday.plusDays(6);
        List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(principal.getIdSchoolLogin());
        maClassList.forEach(y -> {
            List<ScheduleStatusDay> dayList = new ArrayList<>();
            ScheduleClassWeekResponse response = new ScheduleClassWeekResponse();
            List<ClassSchedule> scheduleList = scheduleRepository.findByDelActiveTrueAndMaClass_IdAndScheduleDateBetween(y.getId(), monday, dateEnd);
            response.setSchedule(AppConstant.APP_FALSE);
            scheduleList.forEach(x -> {
                ScheduleStatusDay statusDay = new ScheduleStatusDay();
                if (!AttendanceKidsUtil.checkStudyInSchool(BeanDataUtils.getAttendanceConfigDate(principal.getIdSchoolLogin(), x.getScheduleDate()))) {
                    statusDay.setKeyDay(ConvertData.convetDayString(x.getScheduleDate().getDayOfWeek().toString()));
                    statusDay.setStatus(AttendanceConstant.TYPE_ABSENT);
                } else if (!CollectionUtils.isEmpty(x.getScheduleMorningList()) || !CollectionUtils.isEmpty(x.getScheduleAfternoonList()) || !CollectionUtils.isEmpty(x.getScheduleEveningList())) {
                    statusDay.setKeyDay(ConvertData.convetDayString(x.getScheduleDate().getDayOfWeek().toString()));
                    statusDay.setStatus(AppConstant.YES);
                } else {
                    statusDay.setKeyDay(ConvertData.convetDayString(x.getScheduleDate().getDayOfWeek().toString()));
                    statusDay.setStatus(AppConstant.NO);
                }
                dayList.add(statusDay);
                response.setSchedule(AppConstant.APP_TRUE);
            });
            if (CollectionUtils.isEmpty(scheduleList)) {
                this.setStatusDay(monday, principal, dayList);
            }
            response.setId(y.getId());
            response.setNameClass(y.getClassName());
            response.setStatusDayList(dayList);
            responseList.add(response);
        });
        return responseList;
    }

    private List<ScheduleStatusDay> setStatusDay(LocalDate monday, UserPrincipal principal, List<ScheduleStatusDay> dayList) {
        for (int i = 0; i < 7; i++) {
            ScheduleStatusDay statusDay = new ScheduleStatusDay();
            LocalDate date = monday.plusDays(i);
            if (!AttendanceKidsUtil.checkStudyInSchool(BeanDataUtils.getAttendanceConfigDate(principal.getIdSchoolLogin(), date))) {
                statusDay.setKeyDay(ConvertData.convetDayString(date.getDayOfWeek().toString()));
                statusDay.setStatus(AttendanceConstant.TYPE_ABSENT);
            } else {
                statusDay.setKeyDay(ConvertData.convetDayString(date.getDayOfWeek().toString()));
                statusDay.setStatus(AppConstant.NO);
            }
            dayList.add(statusDay);
        }
        return dayList;
    }

    @Override
    public List<ScheduleWeekPlusResponse> searchScheduleWeek(UserPrincipal principal, ScheduleDatePlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = request.getIdClass();
        LocalDate monday = ConvertData.getMondayOfWeek(request.getDate());
        List<ScheduleWeekPlusResponse> responseList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ClassSchedule classSchedule = scheduleRepository.findScheduleDate(idSchool, idClass, monday.plusDays(i));
            if(classSchedule != null){
                if (classSchedule.isApproved()) {
                    if (!CollectionUtils.isEmpty(classSchedule.getScheduleMorningList()) || !CollectionUtils.isEmpty(classSchedule.getScheduleAfternoonList()) || !CollectionUtils.isEmpty(classSchedule.getScheduleEveningList())) {
                        responseList.add(this.setPropertiesSchedule(classSchedule));
                    }
                }
            }
        }
        return responseList;
    }

    @Override
    public List<FeatureClassResponse> searchScheduleFileClass(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        List<FeatureClassResponse> responseList = new ArrayList<>();
        List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(principal.getIdSchoolLogin());
        maClassList.forEach(x -> {
            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByExEmployeeClassList_MaClass_IdAndDelActiveTrue(x.getId());
            FeatureClassResponse response = ClassFeatureUtils.setFeatureClass(infoEmployeeSchoolList, x);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public ImageWeekResponse searchScheduleImage(UserPrincipal principal, ScheduleDatePlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        LocalDate monday = ConvertData.getMondayOfWeek(request.getDate());
        ScheduleFile scheduleFile = scheduleFileRepository.searchScheduleImageWeek(principal.getIdSchoolLogin(), request.getIdClass(), monday);
        return this.setPropertiesImage(scheduleFile);
    }

    @Override
    public ListFileWeekResponse searchScheduleFile(UserPrincipal principal, ScheduleFileRequest request) {
        CommonValidate.checkDataPlus(principal);
        ListFileWeekResponse response = new ListFileWeekResponse();
        List<ScheduleFile> scheduleFileList = scheduleFileRepository.searchScheduleFilePageNumber(principal.getIdSchoolLogin(), request.getId(), request.getPageNumber());
        List<FileWeekResponse> dataList = this.setPropertiesFile(scheduleFileList);
        long count = dataList.size();
        boolean lastPage = count < MobileConstant.MAX_PAGE_ITEM;
        response.setDataList(dataList);
        response.setLastPage(lastPage);
        return response;
    }

    private List<FileWeekResponse> setPropertiesFile(List<ScheduleFile> scheduleFileList) {
        List<FileWeekResponse> dataList = new ArrayList<>();
        scheduleFileList.forEach(x -> {
            List<UrlScheuldeFile> urlScheuldeFileList = List.copyOf(x.getUrlScheuldeFileList());
            FileWeekResponse model = new FileWeekResponse();
            List<FileResponse> fileList = new ArrayList<>();
            List<String> pictureList = new ArrayList<>();
            urlScheuldeFileList.forEach(y -> {
                if (StringUtils.isNotBlank(y.getNameFile())) {
                    FileResponse file = new FileResponse();
                    file.setName(y.getNameFile());
                    file.setUrl(y.getUrlFile());
                    fileList.add(file);
                }
                if (StringUtils.isNotBlank(y.getNamePicture())) {
                    pictureList.add(y.getNamePicture());
                }
            });
            model.setDate(x.getFromFileTsime());
            model.setWeekName(ConvertData.convertDateToWeekname(x.getFromFileTsime()));
            model.setFileList(fileList);
            if (CollectionUtils.isEmpty(fileList) && CollectionUtils.isEmpty(pictureList)) {
                model.setDate(x.getFromFileTsime());
                model.setWeekName("");
                model.setFileList(new ArrayList<>());
            }
            if (CollectionUtils.isEmpty(fileList) && !CollectionUtils.isEmpty(pictureList)) {
                model.setDate(x.getFromFileTsime());
                model.setWeekName(ConvertData.convertDateToWeekname(x.getFromFileTsime()));
                model.setFileList(new ArrayList<>());
            }
            if (!Strings.isBlank(model.getWeekName())) {
                dataList.add(model);
            }
        });
        return dataList;
    }

    private ImageWeekResponse setPropertiesImage(ScheduleFile scheduleFile) {

        ImageWeekResponse response = new ImageWeekResponse();
        List<String> pictureList = new ArrayList<>();
        List<String> fileList = new ArrayList<>();
        Set<UrlScheuldeFile> urls = scheduleFile.getUrlScheuldeFileList();
        if (CollectionUtils.isEmpty(urls)) {
            response.setPictureList(new ArrayList<>());
            response.setWeekName("");
            return response;
        }
        urls.forEach(x -> {
            if (StringUtils.isNotBlank(x.getNamePicture())) {
                pictureList.add(x.getUrlPicture());
            } else if (StringUtils.isNotBlank(x.getUrlFile())) {
                fileList.add(x.getUrlFile());
            }
        });

        if (CollectionUtils.isEmpty(pictureList)) {
            response.setPictureList(new ArrayList<>());
            response.setWeekName("");

            if (!CollectionUtils.isEmpty(fileList)) {
                pictureList.add(MobileConstant.NO_IMAGE);
                response.setPictureList(pictureList);
            }

        } else {
            response.setWeekName(ConvertData.convertDateToWeekname(scheduleFile.getFromFileTsime()));
            response.setPictureList(pictureList);
        }
        return response;
    }

    /**
     * set thuộc tính chi tiết thời khóa biểu tuần
     *
     * @param classSchedule
     * @return
     */
    private ScheduleWeekPlusResponse setPropertiesSchedule(ClassSchedule classSchedule) {
        ScheduleWeekPlusResponse response = new ScheduleWeekPlusResponse();
        response.setDate(classSchedule.getScheduleDate().toString());
        response.setTitle(Strings.isEmpty(classSchedule.getScheduleTitle()) ? "" : classSchedule.getScheduleTitle());
        // List Schedule Morning, Afternoon, Evening
        List<ScheduleMorning> scheduleMorningList = new ArrayList<>();
        List<ScheduleAfternoon> scheduleAfternoonList = new ArrayList<>();
        List<ScheduleEvening> scheduleEveningList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(classSchedule.getScheduleMorningList())) {
            scheduleMorningList = classSchedule.getScheduleMorningList();
        }
        if (!CollectionUtils.isEmpty(classSchedule.getScheduleAfternoonList())) {
            scheduleAfternoonList = classSchedule.getScheduleAfternoonList();
        }
        if (!CollectionUtils.isEmpty(classSchedule.getScheduleEveningList())) {
            scheduleEveningList = classSchedule.getScheduleEveningList();
        }
        List<ScheduleDateResponse> morningList = listMapper.mapList(scheduleMorningList, ScheduleDateResponse.class);
        response.setMorningList(morningList);
        List<ScheduleDateResponse> afternoonList = listMapper.mapList(scheduleAfternoonList, ScheduleDateResponse.class);
        response.setAfternoonList(afternoonList);
        List<ScheduleDateResponse> eveningList = listMapper.mapList(scheduleEveningList, ScheduleDateResponse.class);
        response.setEveningList(eveningList);
        return response;

    }

}
