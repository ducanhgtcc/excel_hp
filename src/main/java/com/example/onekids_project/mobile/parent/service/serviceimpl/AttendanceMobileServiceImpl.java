package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AttendanceConstant;
import com.example.onekids_project.entity.kids.*;
import com.example.onekids_project.mobile.parent.importexport.model.AttendanceKidsExModel;
import com.example.onekids_project.mobile.parent.response.attendance.*;
import com.example.onekids_project.mobile.parent.response.kids.KidsParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.AttendanceMobileService;
import com.example.onekids_project.mobile.parent.service.servicecustom.KidsParentService;
import com.example.onekids_project.repository.AbsentLetterRepository;
import com.example.onekids_project.repository.AttendanceKidsRepository;
import com.example.onekids_project.response.attendancekids.AttendanceConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class AttendanceMobileServiceImpl implements AttendanceMobileService {


    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Autowired
    private KidsParentService kidsParentService;

    @Autowired
    private AbsentLetterRepository absentLetterRepository;

    @Override
    public ListAttendanceMobileResponse findAttendace(UserPrincipal principal, Integer pageNumber, LocalDate localDate) {
        //chọn ngày tương lai thì return
        ListAttendanceMobileResponse response = new ListAttendanceMobileResponse();
        if (localDate != null && localDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chọn ngày nhỏ hơn hoặc bằng ngày hiện tại");
        }
        Long idSchool = principal.getIdSchoolLogin();
        Long idKid = principal.getIdKidLogin();
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findAttendanceKidsForMobile(idSchool, idKid, localDate, pageNumber);
        List<AttendanceMobileResponse> dataList = new ArrayList<>();
        attendanceKidsList.forEach(x -> {
            AttendanceConfig attendanceConfig = x.getAttendanceConfig();
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceConfig, x.getAttendanceDate());
            //có đi học ít nhất 1 buổi trong ngày
            if (attendanceConfigResponse.isMorningAttendanceArrive() || attendanceConfigResponse.isAfternoonAttendanceArrive() || attendanceConfigResponse.isEveningAttendanceArrive()) {
                AttendanceMobileResponse model = new AttendanceMobileResponse();
                AttendanceArriveKids arrive = x.getAttendanceArriveKids();
                AttendanceLeaveKids leave = x.getAttendanceLeaveKids();
                AttendanceEatKids eat = x.getAttendanceEatKids();
                model.setId(x.getId());
                model.setDate(x.getAttendanceDate());
                if (arrive.isMorning()) {
                    Map<String, String> stringStringMap = new HashMap<>();
                    stringStringMap.put(AttendanceConstant.MOR, AttendanceConstant.TYPE_GO_SCHOOL);
                    model.setStatusList(stringStringMap);
                }
                Map<String, String> stringStringMap = this.setModelStatusList(arrive);
                model.setStatusList(stringStringMap);
                model.setTimeArriveKid(arrive.getTimeArriveKid());
                String contentArrive = "";
                if (StringUtils.isBlank(arrive.getArriveContent())) {
                    if (arrive.getTimeArriveKid() != null) {
                        contentArrive = AttendanceConstant.CONTENT_ARRIVE;
                    }
                } else {
                    contentArrive = arrive.getArriveContent();
                }
                model.setArriveContent(contentArrive);
                model.setArriveLink(StringUtils.isNotBlank(arrive.getArriveUrlPicture()) ? arrive.getArriveUrlPicture() : "");
                model.setTimeLeaveKid(leave.getTimeLeaveKid());
                String contentLeave = "";
                if (StringUtils.isBlank(leave.getLeaveContent())) {
                    if (leave.getTimeLeaveKid() != null) {
                        contentLeave = AttendanceConstant.CONTENT_LEAVE;
                    }
                } else {
                    contentLeave = leave.getLeaveContent();
                }
                model.setLeaveContent(contentLeave);
                model.setLeaveLink(StringUtils.isNotBlank(leave.getLeaveUrlPicture()) ? leave.getLeaveUrlPicture() : "");
                model.setEatList(Arrays.asList(eat.isBreakfast(), eat.isSecondBreakfast(), eat.isLunch(), eat.isAfternoon(), eat.isSecondAfternoon(), eat.isDinner()));
                if (model.getEatList().stream().filter(b -> b).count() == 0) {
                    List<String> stringList = model.getStatusList().values().stream().collect(Collectors.toList());
                    long count = stringList.stream().filter(d -> d.equals(AttendanceConstant.TYPE_NO_ATTENDANCE)).count();
                    if (count == 3) {
                        model.setStatus(AppConstant.APP_TRUE);
                    }
                }
                dataList.add(model);
            }
        });
        long count = attendanceKidsRepository.getCountAttendance(idSchool, idKid, localDate, pageNumber);
        boolean lastPage = count == 0;
        response.setDataList(dataList);
        response.setLastPage(lastPage);
        return response;
    }

    private Map<String, String> setModelStatusList(AttendanceArriveKids arrive) {
        Map<String, String> stringStringMap = new HashMap<>();
        //set buổi sáng
        if (!arrive.isMorning() && !arrive.isMorningYes() && !arrive.isMorningNo()) {
            stringStringMap.put(AttendanceConstant.MOR, AttendanceConstant.TYPE_NO_ATTENDANCE);
        } else {
            if (arrive.isMorning()) {
                stringStringMap.put(AttendanceConstant.MOR, AttendanceConstant.TYPE_GO_SCHOOL);
            } else if (arrive.isMorningYes()) {
                stringStringMap.put(AttendanceConstant.MOR, AttendanceConstant.TYPE_ABSENT_YES);
            } else if (arrive.isMorningNo()) {
                stringStringMap.put(AttendanceConstant.MOR, AttendanceConstant.TYPE_ABSENT_NO);
            }
        }
        //set buổi chiều
        if (!arrive.isAfternoon() && !arrive.isAfternoonYes() && !arrive.isAfternoonNo()) {
            stringStringMap.put(AttendanceConstant.AFT, AttendanceConstant.TYPE_NO_ATTENDANCE);
        } else {
            if (arrive.isAfternoon()) {
                stringStringMap.put(AttendanceConstant.AFT, AttendanceConstant.TYPE_GO_SCHOOL);
            } else if (arrive.isAfternoonYes()) {
                stringStringMap.put(AttendanceConstant.AFT, AttendanceConstant.TYPE_ABSENT_YES);
            } else if (arrive.isAfternoonNo()) {
                stringStringMap.put(AttendanceConstant.AFT, AttendanceConstant.TYPE_ABSENT_NO);
            }
        }
        //set buổi tối
        if (!arrive.isEvening() && !arrive.isEveningYes() && !arrive.isEveningNo()) {
            stringStringMap.put(AttendanceConstant.EVN, AttendanceConstant.TYPE_NO_ATTENDANCE);
        } else {
            if (arrive.isEvening()) {
                stringStringMap.put(AttendanceConstant.EVN, AttendanceConstant.TYPE_GO_SCHOOL);
            } else if (arrive.isEveningYes()) {
                stringStringMap.put(AttendanceConstant.EVN, AttendanceConstant.TYPE_ABSENT_YES);
            } else if (arrive.isEveningNo()) {
                stringStringMap.put(AttendanceConstant.EVN, AttendanceConstant.TYPE_ABSENT_NO);
            }
        }
        return stringStringMap;
    }

    private Map<String, String> setDataStatusList(String key, String value) {
        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put(key, value);
        return stringStringMap;
    }

    @Override
    public ListAttendanceMonthResponse findAttendanceMonth(UserPrincipal principal, LocalDate localDate) {
        ListAttendanceMonthResponse response = new ListAttendanceMonthResponse();
        LocalDate dateStart = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findAttendanceMonth(principal.getIdSchoolLogin(), principal.getIdKidLogin(), dateStart, dateEnd);
        List<Integer> noAttendanceList = new ArrayList<>();
        List<AttendanceMonthResponse> allList = new ArrayList<>();
        List<AttendanceMonthResponse> morningList = new ArrayList<>();
        List<AttendanceMonthResponse> afternoonList = new ArrayList<>();
        List<AttendanceMonthResponse> eveningList = new ArrayList<>();
        attendanceKidsList.forEach(x -> {
            AttendanceConfig attendanceConfig = x.getAttendanceConfig();
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceConfig, x.getAttendanceDate());
            //có đi học ít nhất 1 buổi trong ngày
            if (attendanceConfigResponse.isMorningAttendanceArrive() || attendanceConfigResponse.isAfternoonAttendanceArrive() || attendanceConfigResponse.isEveningAttendanceArrive()) {
                LocalDate date = x.getAttendanceDate();
                int dateNumber = x.getAttendanceDate().getDayOfMonth();
                AttendanceConfigResponse config = ConvertData.convertAttendanceConfig(attendanceConfig, date);
                AttendanceArriveKids arrive = x.getAttendanceArriveKids();
                if (!x.isAttendanceArrive()) {
                    //ngày chưa điểm danh
                    noAttendanceList.add(dateNumber);
                } else if (config.isMorningAttendanceArrive() == arrive.isMorningYes() && config.isAfternoonAttendanceArrive() == arrive.isAfternoonYes() && config.isEveningAttendanceArrive() == arrive.isEveningYes()) {
                    //nghỉ cả ngày có phép
                    AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_TRUE);
                    allList.add(model);
                } else if (config.isMorningAttendanceArrive() == arrive.isMorningNo() && config.isAfternoonAttendanceArrive() == arrive.isAfternoonNo() && config.isEveningAttendanceArrive() == arrive.isEveningNo()) {
                    //nghỉ cả ngày không phép
                    AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_FALSE);
                    allList.add(model);
                } else {
                    //trường học sáng
                    if (config.isMorningAttendanceArrive()) {
                        //nghỉ sáng có phép
                        if (arrive.isMorningYes()) {
                            AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_TRUE);
                            morningList.add(model);
                        }
                        //nghỉ sáng không phép
                        if (arrive.isMorningNo()) {
                            AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_FALSE);
                            morningList.add(model);
                        }
                    }
                    //trường học chiều
                    if (config.isAfternoonAttendanceArrive()) {
                        //nghỉ chiều có phép
                        if (arrive.isAfternoonYes()) {
                            AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_TRUE);
                            afternoonList.add(model);
                        }
                        //nghỉ chiều không phép
                        if (arrive.isAfternoonNo()) {
                            AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_FALSE);
                            afternoonList.add(model);
                        }
                    }
                    //trường học tối
                    if (config.isEveningAttendanceArrive()) {
                        //nghỉ tối có phép
                        if (arrive.isEveningYes()) {
                            AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_TRUE);
                            eveningList.add(model);
                        }
                        //nghỉ tối không phép
                        if (arrive.isEveningNo()) {
                            AttendanceMonthResponse model = this.getDataModel(dateNumber, AppConstant.APP_FALSE);
                            eveningList.add(model);
                        }
                    }
                }
            }
        });
        response.setNoAttendanceList(noAttendanceList);
        response.setAllList(allList);
        response.setMorningList(morningList);
        response.setAfternoonList(afternoonList);
        response.setEveningList(eveningList);
        response.setAll(allList.size());
        response.setMorning(morningList.size());
        response.setAfternoon(afternoonList.size());
        response.setEvening(eveningList.size());
        return response;
    }

    @Override
    public AttendanceEatResponse findAttendanceEatMonth(UserPrincipal principal, LocalDate localDate) {
        LocalDate dateStart = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findAttendanceEatMonth(principal.getIdSchoolLogin(), principal.getIdKidLogin(), dateStart, dateEnd);
        AttendanceEatResponse attendanceEatResponse = new AttendanceEatResponse();
        List<EatDateResponse> dataList = new ArrayList<>();
        int allNumber = 0;
        int breakfastNumber = 0;
        int secondBreakfastNumber = 0;
        int lunchNumber = 0;
        int afternoonNumber = 0;
        int secondAfternoonNumber = 0;
        int dinnerNumber = 0;
        for (AttendanceKids x : attendanceKidsList) {
            LocalDate date = x.getAttendanceDate();
            AttendanceConfig attendanceConfig = x.getAttendanceConfig();
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceConfig, x.getAttendanceDate());
            //trường có đi học ít nhất 1 buổi trong ngày
            if (attendanceConfigResponse.isMorningAttendanceArrive() || attendanceConfigResponse.isAfternoonAttendanceArrive() || attendanceConfigResponse.isEveningAttendanceArrive()) {
                AttendanceEatKids eat = x.getAttendanceEatKids();
                boolean breakfastStatus = eat.isBreakfast();
                boolean secondBreakfastStatus = eat.isSecondBreakfast();
                boolean lunchStatus = eat.isLunch();
                boolean afternoonStatus = eat.isAfternoon();
                boolean secondAfternoonStatus = eat.isSecondAfternoon();
                boolean dinnerStatus = eat.isDinner();
                //có ít nhất 1 buổi ăn theo config nhà trường trong 1 ngày
                if (attendanceConfig.isMorningEat() || attendanceConfig.isSecondMorningEat() || attendanceConfig.isLunchEat() || attendanceConfig.isAfternoonEat() || attendanceConfig.isSecondAfternoonEat() || attendanceConfig.isEveningEat()) {
                    List<String> stringList = new ArrayList<>();
                    //không ăn cả ngày
                    if (!breakfastStatus && !secondBreakfastStatus && !lunchStatus && !afternoonStatus && !secondAfternoonStatus && !dinnerStatus) {
                        stringList.add(AttendanceConstant.ALL);
                        allNumber++;
                    } else {
                        EatDateResponse model;
                        //trường có ăn sáng, học sinh không ăn sáng
                        if (attendanceConfig.isMorningEat() && !breakfastStatus) {
                            stringList.add(AttendanceConstant.MOR);
                            breakfastNumber++;
                        }
                        //trường có ăn phụ sáng, học sinh không ăn phụ sáng
                        if (attendanceConfig.isSecondMorningEat() && !secondBreakfastStatus) {
                            stringList.add(AttendanceConstant.SMOR);
                            secondBreakfastNumber++;
                        }
                        //trường có ăn trưa, học sinh không ăn trưa
                        if (attendanceConfig.isLunchEat() && !lunchStatus) {
                            stringList.add(AttendanceConstant.LUN);
                            lunchNumber++;
                        }
                        //trường có ăn chiều, học sinh không ăn chiều
                        if (attendanceConfig.isAfternoonEat() && !afternoonStatus) {
                            stringList.add(AttendanceConstant.AFT);
                            afternoonNumber++;
                        }
                        //trường có ăn phụ chiều, học sinh không ăn phụ chiều
                        if (attendanceConfig.isSecondAfternoonEat() && !secondAfternoonStatus) {
                            stringList.add(AttendanceConstant.SAFT);
                            secondAfternoonNumber++;
                        }
                        //trường có ăn tối, học sinh không ăn tối
                        if (attendanceConfig.isEveningEat() && !dinnerStatus) {
                            stringList.add(AttendanceConstant.EVN);
                            dinnerNumber++;
                        }
                    }
                    if (!CollectionUtils.isEmpty(stringList)) {
                        EatDateResponse model = new EatDateResponse();
                        model.setDate(date);
                        model.setDataList(stringList);
                        dataList.add(model);
                    }
                }
            }
        }
        attendanceEatResponse.setAll(allNumber);
        attendanceEatResponse.setMorning(breakfastNumber);
        attendanceEatResponse.setSecondMorning(secondBreakfastNumber);
        attendanceEatResponse.setLunch(lunchNumber);
        attendanceEatResponse.setAfternoon(afternoonNumber);
        attendanceEatResponse.setSecondAfternoon(secondAfternoonNumber);
        attendanceEatResponse.setEvening(dinnerNumber);
        attendanceEatResponse.setEatDateList(dataList);
        return attendanceEatResponse;
    }


    @Override
    public List<AttendanceMobileExcelResponse> findExportMonthAttendace(UserPrincipal principal, LocalDate datePage, LocalDate localDate) {

        Long idSchool = principal.getIdSchoolLogin();
        Long idKid = principal.getIdKidLogin();
        if (idSchool == null || idKid == null) {
            throw new NotFoundException("idSchool or idKids is null");
        }
        if (localDate == null) {
            return null;
        }
        int month = localDate.getMonthValue();
        int year = localDate.getYear();

        LocalDate dateStart = LocalDate.of(year, month, 1);

        LocalDate localDateNow = LocalDate.now();
        int monthNow = localDateNow.getMonthValue();
        int yearNow = localDateNow.getYear();
        LocalDate dateEnd;
        if (year == yearNow && month == monthNow) {
            dateEnd = LocalDate.now().plusDays(1);
        } else {
            dateEnd = dateStart.plusMonths(1);
        }

        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findAllMonthAttendanceKidsForMobile(idSchool, idKid, datePage, dateStart, dateEnd);
        if (CollectionUtils.isEmpty(attendanceKidsList)) {
            return null;
        }
//        attendanceKidsList = attendanceKidsList.stream().filter(x -> x.getAttendanceEatKids().isBreakfast() || x.getAttendanceEatKids().isSecondBreakfast() || x.getAttendanceEatKids().isLunch() || x.getAttendanceEatKids().isAfternoon() || x.getAttendanceEatKids().isSecondAfternoon() || x.getAttendanceEatKids().isDinner()).collect(Collectors.toList());
        List<AttendanceMobileExcelResponse> attendanceMobileResponseList = new ArrayList<>();
        attendanceKidsList.forEach(x -> {

            AttendanceMobileExcelResponse model = new AttendanceMobileExcelResponse();

            AttendanceArriveKids arrive = x.getAttendanceArriveKids();
            AttendanceLeaveKids leave = x.getAttendanceLeaveKids();
            AttendanceEatKids eat = x.getAttendanceEatKids();

            model.setMinutePickupLate(leave.getMinutePickupLate());
            model.setId(x.getId());
            model.setDate(x.getAttendanceDate());
            model.setMorningList(Arrays.asList(arrive.isMorningYes(), arrive.isMorningNo(), arrive.isMorning()));
            model.setAfternoonList(Arrays.asList(arrive.isAfternoonYes(), arrive.isAfternoonNo(), arrive.isAfternoon()));
            model.setEveningList(Arrays.asList(arrive.isEveningYes(), arrive.isEveningNo(), arrive.isEvening()));
            model.setTimeArriveKid(arrive.getTimeArriveKid());
//            String contentArrive = "";
//            if (StringUtils.isBlank(arrive.getArriveContent())) {
//                if (arrive.getTimeArriveKid() != null) {
//                    contentArrive = AttendanceConstant.CONTENT_ARRIVE;
//                }
//            } else {
//                contentArrive = arrive.getArriveContent();
//            }
//            model.setArriveContent(contentArrive);
//            model.setArriveLink(StringUtils.isNotBlank(arrive.getArriveUrlPicture()) ? arrive.getArriveUrlPicture() : "");
            model.setTimeLeaveKid(leave.getTimeLeaveKid());
//            String contentLeave = "";
//            if (StringUtils.isBlank(leave.getLeaveContent())) {
//                if (leave.getTimeLeaveKid() != null) {
//                    contentLeave = AttendanceConstant.CONTENT_LEAVE;
//                }
//            } else {
//                contentLeave = leave.getLeaveContent();
//            }
//            model.setLeaveContent(contentLeave);
//            model.setLeaveLink(StringUtils.isNotBlank(leave.getLeaveUrlPicture()) ? leave.getLeaveUrlPicture() : "");

            model.setEatList(Arrays.asList(eat.isBreakfast(), eat.isSecondBreakfast(), eat.isLunch(), eat.isAfternoon(), eat.isSecondAfternoon(), eat.isDinner()));
            attendanceMobileResponseList.add(model);
        });
        return attendanceMobileResponseList;
    }

    @Override
    public List<AttendanceKidsExModel> detachedAttendanceKidsOfMonth(UserPrincipal principal, List<AttendanceMobileExcelResponse> listAttendanceKids) {
        if (listAttendanceKids == null) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        List<AttendanceKidsExModel> attendanceKidsExModels = new ArrayList<>();
        long i = 1;
        for (AttendanceMobileExcelResponse attendanceMobileResponse : listAttendanceKids) {
            AttendanceKidsExModel attendanceKidsExModel = new AttendanceKidsExModel();
            attendanceKidsExModel.setId(i++);

            KidsParentResponse infoKid = kidsParentService.findKidById(principal);
            String nameKid = infoKid.getFullName();
            if (nameKid != null) {
                attendanceKidsExModel.setKidName(nameKid);
            }

            if (attendanceMobileResponse.getDate() != null) {
                String date = df.format(attendanceMobileResponse.getDate());
                attendanceKidsExModel.setAttendanceDate(date);
            } else {
                attendanceKidsExModel.setAttendanceDate("");
            }

            if (attendanceMobileResponse.getEveningList().size() > 0) {
                if (attendanceMobileResponse.getMorningList().get(2) && attendanceMobileResponse.getAfternoonList().get(2) && attendanceMobileResponse.getAfternoonList().get(2)) {
                    attendanceKidsExModel.setAbsentStatus("x");
                } else {
                    attendanceKidsExModel.setAbsentStatus("");
                }


                if (attendanceMobileResponse.getMorningList().get(0) && attendanceMobileResponse.getAfternoonList().get(0) && attendanceMobileResponse.getAfternoonList().get(0)) {
                    attendanceKidsExModel.setAbsentLetterYes("x");
                } else {
                    attendanceKidsExModel.setAbsentLetterYes("");
                }

                if (attendanceMobileResponse.getMorningList().get(1) && attendanceMobileResponse.getAfternoonList().get(1) && attendanceMobileResponse.getAfternoonList().get(1)) {
                    attendanceKidsExModel.setAbsentLetterNo("x");
                } else {
                    attendanceKidsExModel.setAbsentLetterNo("");
                }
            } else {

                if (attendanceMobileResponse.getMorningList().get(2) && attendanceMobileResponse.getAfternoonList().get(2)) {
                    attendanceKidsExModel.setAbsentStatus("x");
                } else {
                    attendanceKidsExModel.setAbsentStatus("");
                }


                if (attendanceMobileResponse.getMorningList().get(0) && attendanceMobileResponse.getAfternoonList().get(0)) {
                    attendanceKidsExModel.setAbsentLetterYes("x");
                } else {
                    attendanceKidsExModel.setAbsentLetterYes("");
                }

                if (attendanceMobileResponse.getMorningList().get(1) && attendanceMobileResponse.getAfternoonList().get(1)) {
                    attendanceKidsExModel.setAbsentLetterNo("x");
                } else {
                    attendanceKidsExModel.setAbsentLetterNo("");
                }


            }


            if (attendanceMobileResponse.getMorningList().get(0)) {
                attendanceKidsExModel.setMorningYes("x");
            } else {
                attendanceKidsExModel.setMorningYes("");
            }
            if (attendanceMobileResponse.getMorningList().get(1)) {
                attendanceKidsExModel.setMorningNo("x");
            } else {
                attendanceKidsExModel.setMorningNo("");
            }
            if (attendanceMobileResponse.getMorningList().get(2)) {
                attendanceKidsExModel.setMorning("x");
            } else {
                attendanceKidsExModel.setMorning("");
            }

            if (attendanceMobileResponse.getAfternoonList().get(0)) {
                attendanceKidsExModel.setAfternoonYes("x");
            } else {
                attendanceKidsExModel.setAfternoonYes("");
            }
            if (attendanceMobileResponse.getAfternoonList().get(1)) {
                attendanceKidsExModel.setAfternoonNo("x");
            } else {
                attendanceKidsExModel.setAfternoonNo("");
            }
            if (attendanceMobileResponse.getAfternoonList().get(2)) {
                attendanceKidsExModel.setAfternoon("x");
            } else {
                attendanceKidsExModel.setAfternoon("");
            }
            if (attendanceMobileResponse.getEveningList().size() > 0) {
                if (attendanceMobileResponse.getEveningList().get(0)) {
                    attendanceKidsExModel.setEveningYes("x");
                } else {
                    attendanceKidsExModel.setEveningYes("");
                }
                if (attendanceMobileResponse.getEveningList().get(1)) {
                    attendanceKidsExModel.setEveningNo("x");
                } else {
                    attendanceKidsExModel.setEveningNo("");
                }
                if (attendanceMobileResponse.getEveningList().get(2)) {
                    attendanceKidsExModel.setEvening("x");
                } else {
                    attendanceKidsExModel.setEvening("");
                }
            }

            if (attendanceMobileResponse.getEatList().get(0)) {
                attendanceKidsExModel.setEatBreakfast("x");
            } else {
                attendanceKidsExModel.setEatBreakfast("");
            }
            if (attendanceMobileResponse.getEatList().get(1)) {
                attendanceKidsExModel.setEatSecondBreakfast("x");
            } else {
                attendanceKidsExModel.setEatSecondBreakfast("");
            }
            if (attendanceMobileResponse.getEatList().get(2)) {
                attendanceKidsExModel.setEatLunch("x");
            } else {
                attendanceKidsExModel.setEatLunch("");
            }
            if (attendanceMobileResponse.getEatList().get(3)) {
                attendanceKidsExModel.setEatAfternoon("x");
            } else {
                attendanceKidsExModel.setEatAfternoon("");
            }
            if (attendanceMobileResponse.getEatList().get(4)) {
                attendanceKidsExModel.setEatSecondAfternoon("x");
            } else {
                attendanceKidsExModel.setEatSecondAfternoon("");
            }
            if (attendanceMobileResponse.getEatList().get(5)) {
                attendanceKidsExModel.setEatDinner("x");
            } else {
                attendanceKidsExModel.setEatDinner("");
            }
            if (attendanceMobileResponse.getTimeArriveKid() != null) {
                String timeArriveKid = attendanceMobileResponse.getTimeArriveKid().format(dtf);
                attendanceKidsExModel.setTimeArriveKid(timeArriveKid);
            } else {
                attendanceKidsExModel.setTimeArriveKid("");
            }
            if (attendanceMobileResponse.getTimeLeaveKid() != null) {
                String timeLeaveKid = attendanceMobileResponse.getTimeLeaveKid().format(dtf);
                attendanceKidsExModel.setTimeLeaveKid(timeLeaveKid);
            } else {
                attendanceKidsExModel.setTimeLeaveKid("");
            }
            if (attendanceMobileResponse.getMinutePickupLate() != 0) {
                String mutePickupLate = String.valueOf(attendanceMobileResponse.getMinutePickupLate());
                attendanceKidsExModel.setMinutePickupLate(mutePickupLate);
            } else {
                attendanceKidsExModel.setMinutePickupLate("");
            }
            attendanceKidsExModels.add(attendanceKidsExModel);

        }

        return attendanceKidsExModels;
    }


    /**
     * true trường có ăn và học sinh không ăn
     * false là trường không ăn hoặc trường có ăn và học sinh có ăn
     *
     * @param eatConfig
     * @param eatStatus
     * @return
     */
    private boolean checkNotEat(boolean eatConfig, boolean eatStatus) {
        boolean notEatStatus = false;
        //trường có ăn
        if (eatConfig) {
            //nó không ăn
            notEatStatus = eatStatus ? AppConstant.APP_FALSE : AppConstant.APP_TRUE;
        }
        return notEatStatus;
    }

    /**
     * set data for model
     *
     * @param dateNumber
     * @param status
     * @return
     */
    private AttendanceMonthResponse getDataModel(int dateNumber, boolean status) {
        AttendanceMonthResponse model = new AttendanceMonthResponse();
        model.setDate(dateNumber);
        model.setStatus(status);
        return model;
    }

}
