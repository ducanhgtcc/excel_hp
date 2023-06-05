package com.example.onekids_project.service.serviceimpl.classmenuipml;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.common.UrlFileConstant;
import com.example.onekids_project.entity.classes.ClassMenu;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.classes.ManuFile;
import com.example.onekids_project.entity.classes.UrlMenuFile;
import com.example.onekids_project.importexport.model.ClassMenuModel;
import com.example.onekids_project.repository.ClassMenuFileRepository;
import com.example.onekids_project.repository.ClassMenuRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.UrlMenuFileRepository;
import com.example.onekids_project.request.classmenu.*;
import com.example.onekids_project.request.schedule.ApproveStatus;
import com.example.onekids_project.response.classmenu.*;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.service.servicecustom.classmenu.ClassMenuService;
import com.example.onekids_project.util.ExportExcelUtils;
import com.example.onekids_project.util.HandleFileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
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
public class ClassMenuServiceImpl implements ClassMenuService {

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private ClassMenuRepository classMenuRepository;

    @Autowired
    private ClassMenuFileRepository classMenuFileRepository;

    @Autowired
    private UrlMenuFileRepository urlMenuFileRepository;

    @Autowired
    private SchoolService schoolService;

    @Override
    public List<TabAllClassMenuInWeekResponse> getAllClassMenuMultiClassInWeek(UserPrincipal principal, Long idSchool, SearchAllClassMenuRequest searchAllClassMenuRequest) {
        List<TabAllClassMenuInWeekResponse> tabAllClassMenuInWeekResponseList = new ArrayList<>();
        String scheduleDate = searchAllClassMenuRequest.getTimeClassMenu();
        LocalDate scheduleDateParse = LocalDate.parse(scheduleDate);
        List<MaClass> maClassList = maClassRepository.searchMaClassByIdGrade(idSchool, searchAllClassMenuRequest.getIdGrade(), searchAllClassMenuRequest.getIdClass());
        if (CollectionUtils.isEmpty(maClassList)) {
            return null;
        }
        /**
         * Vòng lặp lấy từ Class
         */
        maClassList.forEach(maClass -> {
            TabAllClassMenuInWeekResponse tabAllClassMenuInWeekResponse = new TabAllClassMenuInWeekResponse();
            tabAllClassMenuInWeekResponse.setIdClass(maClass.getId());
            tabAllClassMenuInWeekResponse.setClassName(maClass.getClassName());
            tabAllClassMenuInWeekResponse.setIdSchool(idSchool);
            tabAllClassMenuInWeekResponse.setMorningSaturday(maClass.isMorningSaturday());
            tabAllClassMenuInWeekResponse.setAfternoonSaturday(maClass.isAfternoonSaturday());
            tabAllClassMenuInWeekResponse.setEveningSaturday(maClass.isEveningSaturday());
            tabAllClassMenuInWeekResponse.setSunday(maClass.isSunday());
            List<TabClassMenuResponse> tabClassMenuResponseList = new ArrayList<>();
            /**
             * Nếu Class đó có thực đơn
             */
            if (!CollectionUtils.isEmpty(maClass.getClassMenuList())) {
                /**
                 * Lọc những thực đơn có ngày từ thứ 2 đến  chủ nhật (giá trị ngày thứ 2 lấy từ request đưa vào)
                 */
                List<ClassMenu> classMenuList = maClass.getClassMenuList().stream()
                        .filter(x -> x.getMenuDate().isAfter(scheduleDateParse.plusDays(-1)) && x.getMenuDate().isBefore(scheduleDateParse.plusDays(7)))
                        .collect(Collectors.toList());
                /**
                 * Nếu không tồn tại những thực đơn nằm trong khoảng thời gian đó
                 */
                if (CollectionUtils.isEmpty(classMenuList)) {

                    tabAllClassMenuInWeekResponse.setApprove(maClass.getClassMenuList().get(0).isApproved());
                    /**
                     * Sáng
                     */
                    TabClassMenuResponse tabClassMenuBreakfastResponse = new TabClassMenuResponse();
                    tabClassMenuBreakfastResponse.setSessionDay("Sáng");
                    tabClassMenuBreakfastResponse.setIdClass(maClass.getId());
                    /**
                     * Phụ sáng
                     */
                    TabClassMenuResponse tabClassMenuSecondBreakfastResponse = new TabClassMenuResponse();
                    tabClassMenuSecondBreakfastResponse.setSessionDay("Phụ Sáng");
                    tabClassMenuSecondBreakfastResponse.setIdClass(maClass.getId());
                    /**
                     * Trưa
                     */
                    TabClassMenuResponse tabClassMenuLunchResponse = new TabClassMenuResponse();
                    tabClassMenuLunchResponse.setSessionDay("Trưa");
                    tabClassMenuLunchResponse.setIdClass(maClass.getId());
                    /**
                     * Chiều
                     */
                    TabClassMenuResponse tabClassMenuAfternoonResponse = new TabClassMenuResponse();
                    tabClassMenuAfternoonResponse.setSessionDay("Chiều");
                    tabClassMenuAfternoonResponse.setIdClass(maClass.getId());
                    /**
                     * Phụ chiều
                     */
                    TabClassMenuResponse tabClassMenuSecondAfternoonResponse = new TabClassMenuResponse();
                    tabClassMenuSecondAfternoonResponse.setSessionDay("Phụ chiều");
                    tabClassMenuSecondAfternoonResponse.setIdClass(maClass.getId());
                    /**
                     * Tối
                     */
                    TabClassMenuResponse tabClassMenuDinnerResponse = new TabClassMenuResponse();
                    tabClassMenuDinnerResponse.setSessionDay("Tối");
                    tabClassMenuDinnerResponse.setIdClass(maClass.getId());

                    tabClassMenuResponseList.add(tabClassMenuBreakfastResponse);
                    tabClassMenuResponseList.add(tabClassMenuSecondBreakfastResponse);
                    tabClassMenuResponseList.add(tabClassMenuLunchResponse);
                    tabClassMenuResponseList.add(tabClassMenuAfternoonResponse);
                    tabClassMenuResponseList.add(tabClassMenuSecondAfternoonResponse);
                    tabClassMenuResponseList.add(tabClassMenuDinnerResponse);

                    tabAllClassMenuInWeekResponse.setTabClassMenuList(tabClassMenuResponseList);
                    tabAllClassMenuInWeekResponseList.add(tabAllClassMenuInWeekResponse);
                }
                /**
                 * Nếu tồn tại thực đơn nằm trong khoảng thời gian đó
                 */
                else {

                    /**
                     * Thực đơn Sáng
                     */
                    TabClassMenuResponse tabClassMenuBreakfastResponse = new TabClassMenuResponse();
                    tabClassMenuBreakfastResponse.setSessionDay("Sáng");
                    tabClassMenuBreakfastResponse.setIdClass(maClass.getId());
                    /**
                     * Thực đơn Phụ Sáng
                     */
                    TabClassMenuResponse tabClassMenuSecondBreakfastResponse = new TabClassMenuResponse();
                    tabClassMenuSecondBreakfastResponse.setSessionDay("Phụ sáng");
                    tabClassMenuSecondBreakfastResponse.setIdClass(maClass.getId());
                    /**
                     * Thực đơn Trưa
                     */
                    TabClassMenuResponse tabClassMenuLunchResponse = new TabClassMenuResponse();
                    tabClassMenuLunchResponse.setSessionDay("Trưa");
                    tabClassMenuLunchResponse.setIdClass(maClass.getId());
                    /**
                     * Thực đơn Chiều
                     */
                    TabClassMenuResponse tabClassMenuAfternoonResponse = new TabClassMenuResponse();
                    tabClassMenuAfternoonResponse.setSessionDay("Chiều");
                    tabClassMenuAfternoonResponse.setIdClass(maClass.getId());
                    /**
                     * Thực đơn Phụ chiều
                     */
                    TabClassMenuResponse tabClassMenuSecondAfternoonResponse = new TabClassMenuResponse();
                    tabClassMenuSecondAfternoonResponse.setSessionDay("Phụ chiều");
                    tabClassMenuSecondAfternoonResponse.setIdClass(maClass.getId());
                    /**
                     * Thực đơn Tối
                     */
                    TabClassMenuResponse tabClassMenuDinnerResponse = new TabClassMenuResponse();
                    tabClassMenuDinnerResponse.setSessionDay("Tối");
                    tabClassMenuDinnerResponse.setIdClass(maClass.getId());

                    classMenuList.forEach(classMenu -> {
                        // SÁNG
                        if (StringUtils.isNotBlank(classMenu.getBreakfastTime()) || StringUtils.isNotBlank(classMenu.getBreakfastContentList())) {
                            DayOfWeek a = classMenu.getMenuDate().getDayOfWeek();
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("*" + classMenu.getBreakfastTime() + "\n" + classMenu.getBreakfastContentList() + "\n");
                            if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.MONDAY) {
                                tabClassMenuBreakfastResponse.setMonday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.TUESDAY) {
                                tabClassMenuBreakfastResponse.setTuesday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.WEDNESDAY) {
                                tabClassMenuBreakfastResponse.setWednesday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.THURSDAY) {
                                tabClassMenuBreakfastResponse.setThursday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.FRIDAY) {
                                tabClassMenuBreakfastResponse.setFriday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                tabClassMenuBreakfastResponse.setSaturday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                tabClassMenuBreakfastResponse.setSunday(stringBuilder.toString());
                            }
                        }
                        if (StringUtils.isNotBlank(classMenu.getSecondBreakfastTime()) || StringUtils.isNotBlank(classMenu.getSecondBreakfastContentList())) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("*" + classMenu.getSecondBreakfastTime() + "\n" + classMenu.getSecondBreakfastContentList() + "\n");
                            if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.MONDAY) {
                                tabClassMenuSecondBreakfastResponse.setMonday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.TUESDAY) {
                                tabClassMenuSecondBreakfastResponse.setTuesday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.WEDNESDAY) {
                                tabClassMenuSecondBreakfastResponse.setWednesday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.THURSDAY) {
                                tabClassMenuSecondBreakfastResponse.setThursday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.FRIDAY) {
                                tabClassMenuSecondBreakfastResponse.setFriday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                tabClassMenuSecondBreakfastResponse.setSaturday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                tabClassMenuSecondBreakfastResponse.setSunday(stringBuilder.toString());
                            }
                        }
                        if (StringUtils.isNotBlank(classMenu.getLunchTime()) || StringUtils.isNotBlank(classMenu.getLunchContentList())) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("*" + classMenu.getLunchTime() + "\n" + classMenu.getLunchContentList() + "\n");
                            if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.MONDAY) {
                                tabClassMenuLunchResponse.setMonday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.TUESDAY) {
                                tabClassMenuLunchResponse.setTuesday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.WEDNESDAY) {
                                tabClassMenuLunchResponse.setWednesday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.THURSDAY) {
                                tabClassMenuLunchResponse.setThursday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.FRIDAY) {
                                tabClassMenuLunchResponse.setFriday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                tabClassMenuLunchResponse.setSaturday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                tabClassMenuLunchResponse.setSunday(stringBuilder.toString());
                            }
                        }
                        if (StringUtils.isNotBlank(classMenu.getAfternoonTime()) || StringUtils.isNotBlank(classMenu.getAfternoonContentList())) {

                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("*" + classMenu.getAfternoonTime() + "\n" + classMenu.getAfternoonContentList() + "\n");
                            if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.MONDAY) {
                                tabClassMenuAfternoonResponse.setMonday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.TUESDAY) {
                                tabClassMenuAfternoonResponse.setTuesday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.WEDNESDAY) {
                                tabClassMenuAfternoonResponse.setWednesday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.THURSDAY) {
                                tabClassMenuAfternoonResponse.setThursday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.FRIDAY) {
                                tabClassMenuAfternoonResponse.setFriday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                tabClassMenuAfternoonResponse.setSaturday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                tabClassMenuAfternoonResponse.setSunday(stringBuilder.toString());
                            }
                        }
                        if (StringUtils.isNotBlank(classMenu.getSecondAfternoonTime()) || StringUtils.isNotBlank(classMenu.getSecondAfternoonContentList())) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("*" + classMenu.getSecondAfternoonTime() + "\n" + classMenu.getSecondAfternoonContentList() + "\n");
                            if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.MONDAY) {
                                tabClassMenuSecondAfternoonResponse.setMonday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.TUESDAY) {
                                tabClassMenuSecondAfternoonResponse.setTuesday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.WEDNESDAY) {
                                tabClassMenuSecondAfternoonResponse.setWednesday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.THURSDAY) {
                                tabClassMenuSecondAfternoonResponse.setThursday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.FRIDAY) {
                                tabClassMenuSecondAfternoonResponse.setFriday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                tabClassMenuSecondAfternoonResponse.setSaturday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                tabClassMenuSecondAfternoonResponse.setSunday(stringBuilder.toString());
                            }
                        }
                        if (StringUtils.isNotBlank(classMenu.getDinnerTime()) || StringUtils.isNotBlank(classMenu.getDinnerContentList())) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("*" + classMenu.getDinnerTime() + "\n" + classMenu.getDinnerContentList() + "\n");
                            if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.MONDAY) {
                                tabClassMenuDinnerResponse.setMonday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.TUESDAY) {
                                tabClassMenuDinnerResponse.setTuesday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.WEDNESDAY) {
                                tabClassMenuDinnerResponse.setWednesday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.THURSDAY) {
                                tabClassMenuDinnerResponse.setThursday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.FRIDAY) {
                                tabClassMenuDinnerResponse.setFriday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                tabClassMenuDinnerResponse.setSaturday(stringBuilder.toString());
                            } else if (classMenu.getMenuDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                tabClassMenuDinnerResponse.setSunday(stringBuilder.toString());
                            }
                        }

                    });
                    tabClassMenuResponseList.add(tabClassMenuBreakfastResponse);
                    tabClassMenuResponseList.add(tabClassMenuSecondBreakfastResponse);
                    tabClassMenuResponseList.add(tabClassMenuLunchResponse);
                    tabClassMenuResponseList.add(tabClassMenuAfternoonResponse);
                    tabClassMenuResponseList.add(tabClassMenuSecondAfternoonResponse);
                    tabClassMenuResponseList.add(tabClassMenuDinnerResponse);
                    tabAllClassMenuInWeekResponse.setTabClassMenuList(tabClassMenuResponseList);

                    tabAllClassMenuInWeekResponseList.add(tabAllClassMenuInWeekResponse);

                }

            }
            /**
             * Nếu class đó không có thực đơn
             */
            else {
                /**
                 * Sáng
                 */
                TabClassMenuResponse tabClassMenuBreakfastResponse = new TabClassMenuResponse();
                tabClassMenuBreakfastResponse.setSessionDay("Sáng");
                tabClassMenuBreakfastResponse.setIdClass(maClass.getId());
                /**
                 * Phụ sáng
                 */
                TabClassMenuResponse tabClassMenuSecondBreakfastResponse = new TabClassMenuResponse();
                tabClassMenuSecondBreakfastResponse.setSessionDay("Phụ Sáng");
                tabClassMenuSecondBreakfastResponse.setIdClass(maClass.getId());
                /**
                 * Trưa
                 */
                TabClassMenuResponse tabClassMenuLunchResponse = new TabClassMenuResponse();
                tabClassMenuLunchResponse.setSessionDay("Trưa");
                tabClassMenuLunchResponse.setIdClass(maClass.getId());
                /**
                 * Chiều
                 */
                TabClassMenuResponse tabClassMenuAfternoonResponse = new TabClassMenuResponse();
                tabClassMenuAfternoonResponse.setSessionDay("Chiều");
                tabClassMenuAfternoonResponse.setIdClass(maClass.getId());
                /**
                 * Phụ chiều
                 */
                TabClassMenuResponse tabClassMenuSecondAfternoonResponse = new TabClassMenuResponse();
                tabClassMenuSecondAfternoonResponse.setSessionDay("Phụ chiều");
                tabClassMenuSecondAfternoonResponse.setIdClass(maClass.getId());
                /**
                 * Tối
                 */
                TabClassMenuResponse tabClassMenuDinnerResponse = new TabClassMenuResponse();
                tabClassMenuDinnerResponse.setSessionDay("Tối");
                tabClassMenuDinnerResponse.setIdClass(maClass.getId());

                tabClassMenuResponseList.add(tabClassMenuBreakfastResponse);
                tabClassMenuResponseList.add(tabClassMenuSecondBreakfastResponse);
                tabClassMenuResponseList.add(tabClassMenuLunchResponse);
                tabClassMenuResponseList.add(tabClassMenuAfternoonResponse);
                tabClassMenuResponseList.add(tabClassMenuSecondAfternoonResponse);
                tabClassMenuResponseList.add(tabClassMenuDinnerResponse);

                tabAllClassMenuInWeekResponse.setTabClassMenuList(tabClassMenuResponseList);
                tabAllClassMenuInWeekResponseList.add(tabAllClassMenuInWeekResponse);
            }
        });
        return tabAllClassMenuInWeekResponseList;
    }

    @Override
    public List<TabClassMenuByIdClassInWeek> getClassMenuByIdClassInWeek(UserPrincipal principal, Long idSchool, Long idClass, SearchAllClassMenuRequest searchAllClassMenuRequest) {
        String mondayString = searchAllClassMenuRequest.getTimeClassMenu();
        LocalDate monday = LocalDate.parse(mondayString);
        LocalDate tuesday = monday.plusDays(1);
        LocalDate wednesday = monday.plusDays(2);
        LocalDate thursday = monday.plusDays(3);
        LocalDate friday = monday.plusDays(4);
        LocalDate saturday = monday.plusDays(5);
        LocalDate sunday = monday.plusDays(6);

        List<TabClassMenuByIdClassInWeek> tabClassMenuByIdClassInWeekList = new ArrayList<>();


        List<ClassMenu> classMenuList = classMenuRepository.findByMaClassIdAndMaClassDelActiveTrueAndMenuDateBetween(idClass, monday, monday.plusDays(6));


        if (CollectionUtils.isEmpty(classMenuList)) {
            List<TabClassMenuDayWeekResponse> tabClassMenuDayWeekResponseList = new ArrayList<>();
            /**
             * Add các buổi(bữa) vào 1 thứ
             */
            for (int i = 0; i <= 5; i++) {
                TabClassMenuDayWeekResponse tabClassMenuDayWeekResponse = new TabClassMenuDayWeekResponse();
                if (i == 0) {
                    tabClassMenuDayWeekResponse.setSessionDay("Sáng");
                } else if (i == 1) {
                    tabClassMenuDayWeekResponse.setSessionDay("Phụ Sáng");
                } else if (i == 2) {
                    tabClassMenuDayWeekResponse.setSessionDay("Trưa");
                } else if (i == 3) {
                    tabClassMenuDayWeekResponse.setSessionDay("Chiều");
                } else if (i == 4) {
                    tabClassMenuDayWeekResponse.setSessionDay("Phụ Chiều");
                } else if (i == 5) {
                    tabClassMenuDayWeekResponse.setSessionDay("Tối");
                }
                tabClassMenuDayWeekResponseList.add(tabClassMenuDayWeekResponse);
            }

            /**
             * Add Các thứ vào Tuần
             */
            for (int i = 0; i <= 6; i++) {
                TabClassMenuByIdClassInWeek tabClassMenuByIdClassInWeek = new TabClassMenuByIdClassInWeek();
                tabClassMenuByIdClassInWeek.setIdClass(idClass);
                tabClassMenuByIdClassInWeek.setTabClassMenuDayClassList(tabClassMenuDayWeekResponseList);
                LocalDate mondayParse = LocalDate.parse(searchAllClassMenuRequest.getTimeClassMenu());
                tabClassMenuByIdClassInWeek.setTimeClassMenu(mondayParse.plusDays(i).toString());
                tabClassMenuByIdClassInWeekList.add(tabClassMenuByIdClassInWeek);
            }

        } else {
            /**
             * Chạy vòng lặp thực đơn của từng thứ
             */
            classMenuList.forEach(classMenu -> {
                TabClassMenuByIdClassInWeek tabClassMenuByIdClassInWeek = new TabClassMenuByIdClassInWeek();
                tabClassMenuByIdClassInWeek.setIdClass(classMenu.getMaClass().getId());
                tabClassMenuByIdClassInWeek.setTimeClassMenu(classMenu.getMenuDate().toString());
                tabClassMenuByIdClassInWeek.setIdClassMenu(classMenu.getId());
                List<TabClassMenuDayWeekResponse> tabClassMenuDayWeekResponseList = new ArrayList<>();

                /**
                 * Add các buổi(bữa) vào thứ
                 */
                for (int i = 0; i <= 5; i++) {
                    TabClassMenuDayWeekResponse tabClassMenuDayWeekResponse = new TabClassMenuDayWeekResponse();
                    if (i == 0) {
                        tabClassMenuDayWeekResponse.setSessionDay("Sáng");
                        tabClassMenuDayWeekResponse.setTimeContent(classMenu.getBreakfastTime());
                        tabClassMenuDayWeekResponse.setContentClassMenu(classMenu.getBreakfastContentList());
                    } else if (i == 1) {
                        tabClassMenuDayWeekResponse.setSessionDay("Phụ Sáng");
                        tabClassMenuDayWeekResponse.setTimeContent(classMenu.getSecondBreakfastTime());
                        tabClassMenuDayWeekResponse.setContentClassMenu(classMenu.getSecondBreakfastContentList());
                    } else if (i == 2) {
                        tabClassMenuDayWeekResponse.setSessionDay("Trưa");
                        tabClassMenuDayWeekResponse.setTimeContent(classMenu.getLunchTime());
                        tabClassMenuDayWeekResponse.setContentClassMenu(classMenu.getLunchContentList());
                    } else if (i == 3) {
                        tabClassMenuDayWeekResponse.setSessionDay("Chiều");
                        tabClassMenuDayWeekResponse.setTimeContent(classMenu.getAfternoonTime());
                        tabClassMenuDayWeekResponse.setContentClassMenu(classMenu.getAfternoonContentList());
                    } else if (i == 4) {
                        tabClassMenuDayWeekResponse.setSessionDay("Phụ chiều");
                        tabClassMenuDayWeekResponse.setTimeContent(classMenu.getSecondAfternoonTime());
                        tabClassMenuDayWeekResponse.setContentClassMenu(classMenu.getSecondAfternoonContentList());
                    } else if (i == 5) {
                        tabClassMenuDayWeekResponse.setSessionDay("Tối");
                        tabClassMenuDayWeekResponse.setTimeContent(classMenu.getDinnerTime());
                        tabClassMenuDayWeekResponse.setContentClassMenu(classMenu.getDinnerContentList());
                    }
                    tabClassMenuDayWeekResponseList.add(tabClassMenuDayWeekResponse);
                }
                tabClassMenuByIdClassInWeek.setTabClassMenuDayClassList(tabClassMenuDayWeekResponseList);
                tabClassMenuByIdClassInWeekList.add(tabClassMenuByIdClassInWeek);
            });
        }

        return tabClassMenuByIdClassInWeekList;
    }

    @Override
    public List<ExcelResponse> getClassMenuByIdClassInWeekNew(Long idSchool, SearchAllClassMenuRequest searchAllClassMenuRequest) {

        MaClass maClass = maClassRepository.findByIdAndIdSchoolAndDelActiveTrue(searchAllClassMenuRequest.getIdClass(), idSchool).orElseThrow();
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        List<ExcelData> bodyList = new ArrayList<>();
        SchoolResponse schoolResponse = schoolService.findByIdSchool(idSchool).stream().findFirst().orElseThrow();
        assert schoolResponse != null;
        //set endDate từ currentDate + 7
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate currentDate = LocalDate.parse(searchAllClassMenuRequest.getTimeClassMenu());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate endDate = currentDate.plusWeeks(1).minusDays(1);
        // get số tuần từ thư viện
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = currentDate.get(weekFields.weekOfWeekBasedYear());
        // fomat kiểu date
        String dateToStr = df.format(currentDate);
        String dateToStrSheet = df.format(endDate);

        List<String> headerStringList = Arrays.asList("THỰC ĐƠN/MENU", AppConstant.EXCEL_SCHOOL.concat(schoolResponse.getSchoolName()), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_TIME.concat("Tuần " + weekNumber + " (" + dateToStr + "-" + dateToStrSheet + ")"));
        List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
        response.setSheetName("Tuần_ " + weekNumber);
        response.setHeaderList(headerList);
        List<TabClassMenuByIdClassInWeek> tabClassMenuByIdClassInWeekList = this.setTabClassMenuByIdClassInWeek(searchAllClassMenuRequest, searchAllClassMenuRequest.getIdClass());
        List<ClassMenuModel> data = listClassMenuVM(tabClassMenuByIdClassInWeekList);
        String str = "";
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) str = "Sáng";
            if (i == 1) str = "Phụ sáng";
            if (i == 2) str = "Trưa";
            if (i == 3) str = "Chiều";
            if (i == 4) str = "Phụ chiều";
            if (i == 5) str = "Tối";
            List<String> bodyStringList = Arrays.asList(str, data.get(i).getContentMonday(), data.get(i).getContentTuesday(), data.get(i).getContentWednesday(), data.get(i).getContentThursday(),
                    data.get(i).getContentFriday(), data.get(i).getContentSaturday(), data.get(i).getContentFriday());
            ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(modelData);
        }
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    // cusstomexcel export
    private List<ClassMenuModel> listClassMenuVM(List<TabClassMenuByIdClassInWeek> tabClassMenuByIdClassInWeekList) {


        List<ClassMenuModel> listClassMenuModel = new ArrayList<>();

        List<String> listMondays = new ArrayList<>();
        List<String> listTuesdays = new ArrayList<>();
        List<String> listWednesdays = new ArrayList<>();
        List<String> listThursdays = new ArrayList<>();
        List<String> listFridays = new ArrayList<>();
        List<String> listSaturdays = new ArrayList<>();
        List<String> listSundays = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (TabClassMenuByIdClassInWeek tabClassMenuByIdClassInWeek : tabClassMenuByIdClassInWeekList) {
            //            String time = !StringUtils.isEmpty(tabClassMenuByIdClassInWeek.getTimeClassMenu()) ? tabClassMenuByIdClassInWeek.getTimeClassMenu():"";\
            LocalDate date = LocalDate.parse(tabClassMenuByIdClassInWeek.getTimeClassMenu());

            String day = String.valueOf(date.getDayOfWeek());
            if (day.equalsIgnoreCase("MONDAY")) {
                for (TabClassMenuDayWeekResponse tabClassMenuDayWeekResponse : tabClassMenuByIdClassInWeek.getTabClassMenuDayClassList()) {

                    if (tabClassMenuDayWeekResponse.getTimeContent() != null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listMondays.add("*" + tabClassMenuDayWeekResponse.getTimeContent() + " \n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else if (tabClassMenuDayWeekResponse.getTimeContent() == null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listMondays.add("\n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else {
                        listMondays.add("");
                    }

                }
            } else if (day.equalsIgnoreCase("TUESDAY")) {

                for (TabClassMenuDayWeekResponse tabClassMenuDayWeekResponse : tabClassMenuByIdClassInWeek.getTabClassMenuDayClassList()) {

                    if (tabClassMenuDayWeekResponse.getTimeContent() != null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listTuesdays.add("*" + tabClassMenuDayWeekResponse.getTimeContent() + " \n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else if (tabClassMenuDayWeekResponse.getTimeContent() == null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listTuesdays.add("\n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else {
                        listTuesdays.add("");
                    }
                }
            } else if (day.equalsIgnoreCase("WEDNESDAY")) {
                for (TabClassMenuDayWeekResponse tabClassMenuDayWeekResponse : tabClassMenuByIdClassInWeek.getTabClassMenuDayClassList()) {
                    if (tabClassMenuDayWeekResponse.getTimeContent() != null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listWednesdays.add("*" + tabClassMenuDayWeekResponse.getTimeContent() + " \n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else if (tabClassMenuDayWeekResponse.getTimeContent() == null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listWednesdays.add("\n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else {
                        listWednesdays.add("");
                    }
                }
            } else if (day.equalsIgnoreCase("THURSDAY")) {
                for (TabClassMenuDayWeekResponse tabClassMenuDayWeekResponse : tabClassMenuByIdClassInWeek.getTabClassMenuDayClassList()) {
                    if (tabClassMenuDayWeekResponse.getTimeContent() != null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listThursdays.add("*" + tabClassMenuDayWeekResponse.getTimeContent() + " \n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else if (tabClassMenuDayWeekResponse.getTimeContent() == null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listThursdays.add("\n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else {
                        listThursdays.add("");
                    }
                }
            } else if (day.equalsIgnoreCase("FRIDAY")) {
                for (TabClassMenuDayWeekResponse tabClassMenuDayWeekResponse : tabClassMenuByIdClassInWeek.getTabClassMenuDayClassList()) {
                    if (tabClassMenuDayWeekResponse.getTimeContent() != null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listFridays.add("*" + tabClassMenuDayWeekResponse.getTimeContent() + " \n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else if (tabClassMenuDayWeekResponse.getTimeContent() == null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listFridays.add("\n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else {
                        listFridays.add("");
                    }
                }
            } else if (day.equalsIgnoreCase("SATURDAY")) {
                for (TabClassMenuDayWeekResponse tabClassMenuDayWeekResponse : tabClassMenuByIdClassInWeek.getTabClassMenuDayClassList()) {
                    if (tabClassMenuDayWeekResponse.getTimeContent() != null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listSaturdays.add("*" + tabClassMenuDayWeekResponse.getTimeContent() + " \n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else if (tabClassMenuDayWeekResponse.getTimeContent() == null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listSaturdays.add("\n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else {
                        listSaturdays.add("");
                    }
                }
            } else if (day.equalsIgnoreCase("SUNDAY")) {
                for (TabClassMenuDayWeekResponse tabClassMenuDayWeekResponse : tabClassMenuByIdClassInWeek.getTabClassMenuDayClassList()) {
                    if (tabClassMenuDayWeekResponse.getTimeContent() != null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listSundays.add("*" + tabClassMenuDayWeekResponse.getTimeContent() + " \n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else if (tabClassMenuDayWeekResponse.getTimeContent() == null && tabClassMenuDayWeekResponse.getContentClassMenu() != null) {
                        listSundays.add("\n" + tabClassMenuDayWeekResponse.getContentClassMenu());
                    } else {
                        listSundays.add("");
                    }
                }
            }


        }
        String[] addArr = {"", "", "", "", "", ""};
        if (CollectionUtils.isEmpty(listMondays)) {
            for (int i = 0; i <= addArr.length; i++) {
                listMondays.add(addArr[i]);
            }
        }
        if (CollectionUtils.isEmpty(listTuesdays)) {
            for (int i = 0; i <= addArr.length; i++) {
                listTuesdays.add(addArr[i]);
            }
        }
        if (CollectionUtils.isEmpty(listWednesdays)) {
            for (int i = 0; i <= addArr.length; i++) {
                listWednesdays.add(addArr[i]);
            }
        }
        if (CollectionUtils.isEmpty(listThursdays)) {
            for (int i = 0; i <= addArr.length; i++) {
                listThursdays.add(addArr[i]);
            }
        }
        if (CollectionUtils.isEmpty(listFridays)) {
            for (int i = 0; i <= addArr.length; i++) {
                listFridays.add(addArr[i]);
            }
        }
        if (CollectionUtils.isEmpty(listSaturdays)) {
            for (int i = 0; i <= addArr.length; i++) {
                listSaturdays.add(addArr[i]);
            }
        }

        if (CollectionUtils.isEmpty(listSundays)) {
            for (int i = 0; i <= addArr.length; i++) {
                listSundays.add(addArr[i]);
            }
        }
        for (int k = 0; k < tabClassMenuByIdClassInWeekList.get(0).getTabClassMenuDayClassList().size(); k++) {

            ClassMenuModel classMenuModel = new ClassMenuModel();

            classMenuModel.setContentMonday(listMondays.get(k));
            classMenuModel.setContentTuesday(listTuesdays.get(k));
            classMenuModel.setContentWednesday(listWednesdays.get(k));
            classMenuModel.setContentThursday(listThursdays.get(k));
            classMenuModel.setContentFriday(listFridays.get(k));
            classMenuModel.setContentSaturday(listSaturdays.get(k));
            classMenuModel.setContentSunday(listSundays.get(k));
            listClassMenuModel.add(classMenuModel);


        }

        return listClassMenuModel;
    }

    private List<TabClassMenuByIdClassInWeek> setTabClassMenuByIdClassInWeek(SearchAllClassMenuRequest searchAllClassMenuRequest, Long idClass) {
        String mondayString = searchAllClassMenuRequest.getTimeClassMenu();
        LocalDate monday = LocalDate.parse(mondayString);
        LocalDate tuesday = monday.plusDays(1);
        LocalDate wednesday = monday.plusDays(2);
        LocalDate thursday = monday.plusDays(3);
        LocalDate friday = monday.plusDays(4);
        LocalDate saturday = monday.plusDays(5);
        LocalDate sunday = monday.plusDays(6);

        List<TabClassMenuByIdClassInWeek> tabClassMenuByIdClassInWeekList = new ArrayList<>();


        List<ClassMenu> classMenuList = classMenuRepository.findByMaClassIdAndMaClassDelActiveTrueAndMenuDateBetween(idClass, monday, monday.plusDays(6));


        if (CollectionUtils.isEmpty(classMenuList)) {
            List<TabClassMenuDayWeekResponse> tabClassMenuDayWeekResponseList = new ArrayList<>();
            /**
             * Add các buổi(bữa) vào 1 thứ
             */
            for (int i = 0; i <= 5; i++) {
                TabClassMenuDayWeekResponse tabClassMenuDayWeekResponse = new TabClassMenuDayWeekResponse();
                if (i == 0) {
                    tabClassMenuDayWeekResponse.setSessionDay("Sáng");
                } else if (i == 1) {
                    tabClassMenuDayWeekResponse.setSessionDay("Phụ Sáng");
                } else if (i == 2) {
                    tabClassMenuDayWeekResponse.setSessionDay("Trưa");
                } else if (i == 3) {
                    tabClassMenuDayWeekResponse.setSessionDay("Chiều");
                } else if (i == 4) {
                    tabClassMenuDayWeekResponse.setSessionDay("Phụ Chiều");
                } else if (i == 5) {
                    tabClassMenuDayWeekResponse.setSessionDay("Tối");
                }
                tabClassMenuDayWeekResponseList.add(tabClassMenuDayWeekResponse);
            }

            /**
             * Add Các thứ vào Tuần
             */
            for (int i = 0; i <= 6; i++) {
                TabClassMenuByIdClassInWeek tabClassMenuByIdClassInWeek = new TabClassMenuByIdClassInWeek();
                tabClassMenuByIdClassInWeek.setIdClass(idClass);
                tabClassMenuByIdClassInWeek.setTabClassMenuDayClassList(tabClassMenuDayWeekResponseList);
                LocalDate mondayParse = LocalDate.parse(searchAllClassMenuRequest.getTimeClassMenu());
                tabClassMenuByIdClassInWeek.setTimeClassMenu(mondayParse.plusDays(i).toString());
                tabClassMenuByIdClassInWeekList.add(tabClassMenuByIdClassInWeek);
            }

        } else {
            /**
             * Chạy vòng lặp thực đơn của từng thứ
             */
            classMenuList.forEach(classMenu -> {
                TabClassMenuByIdClassInWeek tabClassMenuByIdClassInWeek = new TabClassMenuByIdClassInWeek();
                tabClassMenuByIdClassInWeek.setIdClass(classMenu.getMaClass().getId());
                tabClassMenuByIdClassInWeek.setTimeClassMenu(classMenu.getMenuDate().toString());
                tabClassMenuByIdClassInWeek.setIdClassMenu(classMenu.getId());
                List<TabClassMenuDayWeekResponse> tabClassMenuDayWeekResponseList = new ArrayList<>();

                /**
                 * Add các buổi(bữa) vào thứ
                 */
                for (int i = 0; i <= 5; i++) {
                    TabClassMenuDayWeekResponse tabClassMenuDayWeekResponse = new TabClassMenuDayWeekResponse();
                    if (i == 0) {
                        tabClassMenuDayWeekResponse.setSessionDay("Sáng");
                        tabClassMenuDayWeekResponse.setTimeContent(classMenu.getBreakfastTime());
                        tabClassMenuDayWeekResponse.setContentClassMenu(classMenu.getBreakfastContentList());
                    } else if (i == 1) {
                        tabClassMenuDayWeekResponse.setSessionDay("Phụ Sáng");
                        tabClassMenuDayWeekResponse.setTimeContent(classMenu.getSecondBreakfastTime());
                        tabClassMenuDayWeekResponse.setContentClassMenu(classMenu.getSecondBreakfastContentList());
                    } else if (i == 2) {
                        tabClassMenuDayWeekResponse.setSessionDay("Trưa");
                        tabClassMenuDayWeekResponse.setTimeContent(classMenu.getLunchTime());
                        tabClassMenuDayWeekResponse.setContentClassMenu(classMenu.getLunchContentList());
                    } else if (i == 3) {
                        tabClassMenuDayWeekResponse.setSessionDay("Chiều");
                        tabClassMenuDayWeekResponse.setTimeContent(classMenu.getAfternoonTime());
                        tabClassMenuDayWeekResponse.setContentClassMenu(classMenu.getAfternoonContentList());
                    } else if (i == 4) {
                        tabClassMenuDayWeekResponse.setSessionDay("Phụ chiều");
                        tabClassMenuDayWeekResponse.setTimeContent(classMenu.getSecondAfternoonTime());
                        tabClassMenuDayWeekResponse.setContentClassMenu(classMenu.getSecondAfternoonContentList());
                    } else if (i == 5) {
                        tabClassMenuDayWeekResponse.setSessionDay("Tối");
                        tabClassMenuDayWeekResponse.setTimeContent(classMenu.getDinnerTime());
                        tabClassMenuDayWeekResponse.setContentClassMenu(classMenu.getDinnerContentList());
                    }
                    tabClassMenuDayWeekResponseList.add(tabClassMenuDayWeekResponse);
                }
                tabClassMenuByIdClassInWeek.setTabClassMenuDayClassList(tabClassMenuDayWeekResponseList);
                tabClassMenuByIdClassInWeekList.add(tabClassMenuByIdClassInWeek);
            });
        }

        return tabClassMenuByIdClassInWeekList;
    }

    @Override
    @Transactional
    public boolean createClassMenuInClassInWeek(Long idSchool, UserPrincipal principal, List<TabClassMenuByIdClassInWeekRequest> tabClassMenuByIdClassInWeekList) {
        tabClassMenuByIdClassInWeekList.forEach(tabClassMenuByIdClassInWeek -> {
            Optional<MaClass> maClassOptional = maClassRepository.findByIdAndIdSchoolAndDelActiveTrue(tabClassMenuByIdClassInWeek.getIdClass(), idSchool);
            if (maClassOptional.isEmpty()) {
                return;
            }
            ClassMenu classMenu = new ClassMenu();
            if (tabClassMenuByIdClassInWeek.getIdClassMenu() != null) {
                Optional<ClassMenu> classMenuOptional = classMenuRepository.findByIdAndDelActiveTrue(tabClassMenuByIdClassInWeek.getIdClassMenu());

                if (classMenuOptional.isEmpty()) {
                    return;
                }
                ClassMenu classMenuOld = classMenuOptional.get();
                classMenu.setIdCreated(classMenuOld.getIdCreated());
                classMenu.setCreatedDate(classMenuOld.getCreatedDate());
                classMenu.setId(tabClassMenuByIdClassInWeek.getIdClassMenu());
            }
            classMenu.setApproved(!principal.getSchoolConfig().isApprovedMenu());
            classMenu.setMaClass(maClassOptional.get());
            classMenu.setId(tabClassMenuByIdClassInWeek.getIdClassMenu());
            classMenu.setIdSchool(idSchool);
            String menuDate = tabClassMenuByIdClassInWeek.getTimeClassMenu();
            classMenu.setMenuDate(LocalDate.parse(menuDate));

            if (!CollectionUtils.isEmpty(tabClassMenuByIdClassInWeek.getTabClassMenuDayClassList())) {
                tabClassMenuByIdClassInWeek.getTabClassMenuDayClassList().forEach(tabClassMenuDayClass -> {
                    if (tabClassMenuDayClass.getSessionDay().equalsIgnoreCase("Sáng")) {
                        if (tabClassMenuDayClass.getTimeContent() == null) {
                            classMenu.setBreakfastTime("");
                        } else {
                            classMenu.setBreakfastTime(tabClassMenuDayClass.getTimeContent());
                        }
                        if (tabClassMenuDayClass.getContentClassMenu() == null) {
                            classMenu.setBreakfastContentList("");
                        } else {
                            classMenu.setBreakfastContentList(tabClassMenuDayClass.getContentClassMenu());
                        }
                    } else if (tabClassMenuDayClass.getSessionDay().equalsIgnoreCase("Phụ Sáng")) {
                        if (tabClassMenuDayClass.getTimeContent() == null) {
                            classMenu.setSecondBreakfastTime("");
                        } else {
                            classMenu.setSecondBreakfastTime(tabClassMenuDayClass.getTimeContent());
                        }
                        if (tabClassMenuDayClass.getContentClassMenu() == null) {
                            classMenu.setSecondBreakfastContentList("");
                        } else {
                            classMenu.setSecondBreakfastContentList(tabClassMenuDayClass.getContentClassMenu());
                        }

                    } else if (tabClassMenuDayClass.getSessionDay().equalsIgnoreCase("Trưa")) {
                        if (tabClassMenuDayClass.getTimeContent() == null) {
                            classMenu.setLunchTime("");
                        } else {
                            classMenu.setLunchTime(tabClassMenuDayClass.getTimeContent());
                        }
                        if (tabClassMenuDayClass.getContentClassMenu() == null) {
                            classMenu.setLunchContentList("");
                        } else {
                            classMenu.setLunchContentList(tabClassMenuDayClass.getContentClassMenu());
                        }

                    } else if (tabClassMenuDayClass.getSessionDay().equalsIgnoreCase("Chiều")) {
                        if (tabClassMenuDayClass.getTimeContent() == null) {
                            classMenu.setAfternoonTime("");
                        } else {
                            classMenu.setAfternoonTime(tabClassMenuDayClass.getTimeContent());
                        }
                        if (tabClassMenuDayClass.getContentClassMenu() == null) {
                            classMenu.setAfternoonContentList("");
                        } else {
                            classMenu.setAfternoonContentList(tabClassMenuDayClass.getContentClassMenu());
                        }

                    } else if (tabClassMenuDayClass.getSessionDay().equalsIgnoreCase("Phụ Chiều")) {
                        if (tabClassMenuDayClass.getTimeContent() == null) {
                            classMenu.setSecondAfternoonTime("");
                        } else {
                            classMenu.setSecondAfternoonTime(tabClassMenuDayClass.getTimeContent());
                        }
                        if (tabClassMenuDayClass.getContentClassMenu() == null) {
                            classMenu.setSecondAfternoonContentList("");
                        } else {
                            classMenu.setSecondAfternoonContentList(tabClassMenuDayClass.getContentClassMenu());
                        }

                    } else if (tabClassMenuDayClass.getSessionDay().equalsIgnoreCase("Tối")) {
                        if (tabClassMenuDayClass.getTimeContent() == null) {
                            classMenu.setDinnerTime("");
                        } else if (tabClassMenuDayClass.getContentClassMenu() == null) {
                            classMenu.setDinnerContentList("");
                        } else {
                            classMenu.setDinnerTime(tabClassMenuDayClass.getTimeContent());
                            classMenu.setDinnerContentList(tabClassMenuDayClass.getContentClassMenu());
                        }

                    }
                });

            }
            classMenuRepository.save(classMenu);
        });

        return true;
    }

    @Override
    @Transactional
    public boolean saveClassMenuMultiClassMultiWeek(Long idSchool, UserPrincipal principal, CreateMultiClassMenu createMultiClassMenu) {
        List<String> classMenuDateListRequest = createMultiClassMenu.getWeekClassMenu();
        List<LocalDate> classMenuDateListRequestParse = classMenuDateListRequest.stream().map(item -> LocalDate.parse(item)).collect(Collectors.toList());
        List<LocalDate> listDateDayInWeek = new ArrayList<>();

        classMenuDateListRequestParse.forEach(item -> {
            for (int i = 0; i <= 6; i++) {
                LocalDate dateOfWeek = item.plusDays(i);
                listDateDayInWeek.add(dateOfWeek);
            }
        });

        createMultiClassMenu.getListIdClass().forEach(idClass -> {
            /**
             * Nếu tồn tại maClass đó và có menu_date trong bảng Class_Menu nằm trong  thời gian List scheduleDate đưa vào thì tiến hành update
             */
            if (classMenuRepository.existsByMaClassIdAndMaClassDelActiveTrueAndMenuDateIn(idClass, listDateDayInWeek)) {
                List<ClassMenu> classMenuList = classMenuRepository.findByMaClassIdAndMaClassDelActiveTrueAndMenuDateIn(idClass, listDateDayInWeek);

                /**
                 * Xóa các id_class có scheduleDate trong bảng Class_Schedule nằm trong ListScheduleDate đưa vào
                 */
                classMenuRepository.deleteByMaClassIdAndMaClassDelActiveTrueAndMenuDateIn(idClass, listDateDayInWeek);

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
                for (String menuDateRequest : createMultiClassMenu.getWeekClassMenu()) {
                    /**
                     * Từ thứ 2 chạy vòng lặp +1 để suy ra từng thứ
                     */
                    LocalDate monday = LocalDate.parse(menuDateRequest);
                    for (int i = 0; i <= 6; i++) {
                        ClassMenu classMenu = new ClassMenu();
                        classMenu.setIdSchool(idSchool);
                        classMenu.setMaClass(maClass);
                        classMenu.setApproved(!principal.getSchoolConfig().isApprovedMenu());
                        /**
                         * i=0:Thứ 2,i=1:Thứ 3,i=2:Thứ 4,...
                         */
                        LocalDate afterMonday = monday.plusDays(i);
                        classMenu.setMenuDate(afterMonday);
                        classMenu = classMenuRepository.save(classMenu);

                        /**
                         * Lấy nội dung ngày thứ 2 từ Request
                         */
                        CreateTabAllClassMenu createTabAllClassMenu = createMultiClassMenu.getCreateTabAllClassMenu().get(i);
                        for (CreateTabDayInWeek createTabDayInWeek : createTabAllClassMenu.getCreateTabDayInWeek()) {
                            if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Sáng") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentClassMenu()))) {
                                classMenu.setBreakfastTime(createTabDayInWeek.getTimeContent());
                                classMenu.setBreakfastContentList(createTabDayInWeek.getContentClassMenu());
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Phụ Sáng") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentClassMenu()))) {
                                classMenu.setSecondBreakfastTime(createTabDayInWeek.getTimeContent());
                                classMenu.setSecondBreakfastContentList(createTabDayInWeek.getContentClassMenu());
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Trưa") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentClassMenu()))) {
                                classMenu.setLunchTime(createTabDayInWeek.getTimeContent());
                                classMenu.setLunchContentList(createTabDayInWeek.getContentClassMenu());
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Chiều") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentClassMenu()))) {
                                classMenu.setAfternoonTime(createTabDayInWeek.getTimeContent());
                                classMenu.setAfternoonContentList(createTabDayInWeek.getContentClassMenu());
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Phụ Chiều") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentClassMenu()))) {
                                classMenu.setSecondAfternoonTime(createTabDayInWeek.getTimeContent());
                                classMenu.setSecondAfternoonContentList(createTabDayInWeek.getContentClassMenu());
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Tối") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentClassMenu()))) {
                                classMenu.setDinnerTime(createTabDayInWeek.getTimeContent());
                                classMenu.setDinnerContentList(createTabDayInWeek.getContentClassMenu());
                            }
                        }
                        classMenu = classMenuRepository.save(classMenu);
                    }

                }
            }
            /**
             * Nếu không tồn tại maClass đó và có menu_date trong bảng Class_Menu nằm trong  thời gian List scheduleDate đưa vào thì tiến hành insert
             */
            else {
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
                for (String menuDateRequest : createMultiClassMenu.getWeekClassMenu()) {
                    /**
                     * Từ thứ 2 chạy vòng lặp +1 để suy ra từng thứ
                     */
                    LocalDate monday = LocalDate.parse(menuDateRequest);
                    for (int i = 0; i <= 6; i++) {
                        ClassMenu classMenu = new ClassMenu();
                        classMenu.setIdSchool(idSchool);
                        classMenu.setMaClass(maClass);
                        classMenu.setApproved(!principal.getSchoolConfig().isApprovedMenu());
                        /**
                         * i=0:Thứ 2,i=1:Thứ 3,i=2:Thứ 4,...
                         */
                        LocalDate afterMonday = monday.plusDays(i);
                        classMenu.setMenuDate(afterMonday);


                        /**
                         * Lấy nội dung ngày thứ 2 từ Request
                         */
                        CreateTabAllClassMenu createTabAllClassMenu = createMultiClassMenu.getCreateTabAllClassMenu().get(i);
                        for (CreateTabDayInWeek createTabDayInWeek : createTabAllClassMenu.getCreateTabDayInWeek()) {
                            if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Sáng") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentClassMenu()))) {
                                classMenu.setBreakfastTime(createTabDayInWeek.getTimeContent());
                                classMenu.setBreakfastContentList(createTabDayInWeek.getContentClassMenu());
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Phụ Sáng") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentClassMenu()))) {
                                classMenu.setSecondBreakfastTime(createTabDayInWeek.getTimeContent());
                                classMenu.setSecondBreakfastContentList(createTabDayInWeek.getContentClassMenu());
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Trưa") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentClassMenu()))) {
                                classMenu.setLunchTime(createTabDayInWeek.getTimeContent());
                                classMenu.setLunchContentList(createTabDayInWeek.getContentClassMenu());
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Chiều") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentClassMenu()))) {
                                classMenu.setAfternoonTime(createTabDayInWeek.getTimeContent());
                                classMenu.setAfternoonContentList(createTabDayInWeek.getContentClassMenu());
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Phụ Chiều") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentClassMenu()))) {
                                classMenu.setSecondAfternoonTime(createTabDayInWeek.getTimeContent());
                                classMenu.setSecondAfternoonContentList(createTabDayInWeek.getContentClassMenu());
                            } else if (createTabDayInWeek.getSessionDay().equalsIgnoreCase("Tối") && (StringUtils.isNotBlank(createTabDayInWeek.getTimeContent()) || StringUtils.isNotBlank(createTabDayInWeek.getContentClassMenu()))) {
                                classMenu.setDinnerTime(createTabDayInWeek.getTimeContent());
                                classMenu.setDinnerContentList(createTabDayInWeek.getContentClassMenu());
                            }
                        }
                        classMenu = classMenuRepository.save(classMenu);
                    }
                }
            }
        });
        return true;
    }

    @Override
    public List<TabDetailClassMenuAllClassResponse> findAllClassMenuTabDetail(Long idSchool, SearchAllClassMenuRequest searchAllClassMenuRequest) {
        /**
         *Tìm kiếm các lớp theo ngày,khối(nếu có),lớp(nếu có)
         */
        List<MaClass> maClassList = maClassRepository.searchMaClassByIdGrade(idSchool, searchAllClassMenuRequest.getIdGrade(), searchAllClassMenuRequest.getIdClass());
        if (CollectionUtils.isEmpty(maClassList)) {
            return null;
        }
        String timeClassMenu = searchAllClassMenuRequest.getTimeClassMenu();
        LocalDate monday = LocalDate.parse(timeClassMenu);
        List<TabDetailClassMenuAllClassResponse> tabDetailClassMenuAllClassResponseList = new ArrayList<>();

        for (MaClass maClass : maClassList) {
            TabDetailClassMenuAllClassResponse tabDetailClassMenuAllClassResponse = new TabDetailClassMenuAllClassResponse();
            tabDetailClassMenuAllClassResponse.setGradeName(maClass.getGrade().getGradeName());
            tabDetailClassMenuAllClassResponse.setClassName(maClass.getClassName());
            tabDetailClassMenuAllClassResponse.setIdClass(maClass.getId());
            tabDetailClassMenuAllClassResponse.setIsMonday(timeClassMenu);
            if (!CollectionUtils.isEmpty(maClass.getClassMenuList())) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i <= 6; i++) {
                    LocalDate monday1 = monday.plusDays(i);
                    List<ClassMenu> classMenuList = maClass.getClassMenuList().stream().filter(item -> item.getMenuDate().isEqual(monday1)).collect(Collectors.toList());
                    classMenuList.forEach(classMenu -> {
                        for (int j = 0; j <= 6; j++) {
                            if (classMenu.getMenuDate().isEqual(monday.plusDays(j))) {
                                if (StringUtils.isNotBlank(classMenu.getBreakfastTime()) || StringUtils.isNotBlank(classMenu.getBreakfastContentList())) {
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

                                    tabDetailClassMenuAllClassResponse.setListCheckContentDay(stringBuilder.toString());

                                    break;

                                }
                                if (StringUtils.isNotBlank(classMenu.getSecondBreakfastTime()) || StringUtils.isNotBlank(classMenu.getSecondBreakfastContentList())) {
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
                                    tabDetailClassMenuAllClassResponse.setListCheckContentDay(stringBuilder.toString());
                                    break;
                                }
                                if (StringUtils.isNotBlank(classMenu.getLunchTime()) || StringUtils.isNotBlank(classMenu.getLunchContentList())) {
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
                                    tabDetailClassMenuAllClassResponse.setListCheckContentDay(stringBuilder.toString());
                                    break;
                                }
                                if (StringUtils.isNotBlank(classMenu.getAfternoonTime()) || StringUtils.isNotBlank(classMenu.getAfternoonContentList())) {
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
                                    tabDetailClassMenuAllClassResponse.setListCheckContentDay(stringBuilder.toString());
                                    break;
                                }
                                if (StringUtils.isNotBlank(classMenu.getSecondAfternoonTime()) || StringUtils.isNotBlank(classMenu.getSecondAfternoonContentList())) {
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
                                    tabDetailClassMenuAllClassResponse.setListCheckContentDay(stringBuilder.toString());
                                    break;
                                }
                                if (StringUtils.isNotBlank(classMenu.getDinnerTime()) || StringUtils.isNotBlank(classMenu.getDinnerContentList())) {
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
                                    tabDetailClassMenuAllClassResponse.setListCheckContentDay(stringBuilder.toString());
                                    break;
                                }
                            }
                        }
                        tabDetailClassMenuAllClassResponse.setApprove(classMenu.isApproved());
                    });
                }


            }
            /**
             * Hiển thị trên tab thời khóa biểu dạng file ảnh
             */
            if (!CollectionUtils.isEmpty(maClass.getManuFileList())) {
                List<FileAndPictureMenuResponse> fileAndPictureMenuResponseList = new ArrayList<>();
                maClass.getManuFileList().forEach(manuFile -> {
                    if (manuFile.getFromFileTime().isEqual(monday) && manuFile.getToFileTime().isEqual(monday.plusDays(6))) {
                        StringBuilder strFileList = new StringBuilder();
                        manuFile.getUrlMenuFileList().forEach(
                                urlMenuFile -> {
                                    FileAndPictureMenuResponse fileAndPictureMenuResponse = new FileAndPictureMenuResponse();
                                    if (StringUtils.isNotBlank(urlMenuFile.getNameFile())) {
                                        fileAndPictureMenuResponse.setName(urlMenuFile.getNameFile());
                                        fileAndPictureMenuResponse.setUrl(urlMenuFile.getUrlFile());
                                        fileAndPictureMenuResponse.setIdMenuFile(urlMenuFile.getId());
                                        fileAndPictureMenuResponse.setIdUrlMenuFile(urlMenuFile.getId());
                                    } else if (StringUtils.isNotBlank(urlMenuFile.getNamePicture())) {
                                        fileAndPictureMenuResponse.setName(urlMenuFile.getNamePicture());
                                        fileAndPictureMenuResponse.setUrl(urlMenuFile.getUrlPicture());
                                        fileAndPictureMenuResponse.setIdMenuFile(urlMenuFile.getId());
                                        fileAndPictureMenuResponse.setIdUrlMenuFile(urlMenuFile.getId());
                                    }
                                    fileAndPictureMenuResponseList.add(fileAndPictureMenuResponse);
                                }
                        );
                        tabDetailClassMenuAllClassResponse.setFileList(fileAndPictureMenuResponseList);
                        if (!CollectionUtils.isEmpty(fileAndPictureMenuResponseList)) {
                            tabDetailClassMenuAllClassResponse.setApprove(manuFile.isApproved());
                        }
                    }
                });
            }

            tabDetailClassMenuAllClassResponseList.add(tabDetailClassMenuAllClassResponse);
        }


        return tabDetailClassMenuAllClassResponseList;
    }

    @Override
    public List<TabClassMenuViewDetail> findClassMenuDetailByClass(Long idSchool, Long idClass) {
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
        List<TabClassMenuViewDetail> tabClassMenuViewDetailList = new ArrayList<>();
        List<ClassMenu> classMenuList = classMenuRepository
                .findByMaClassIdAndMaClassDelActiveTrueAndMenuDateBetween(idClass, firstDayOfYear, lastDayOfYear)
                .stream().filter(x -> x.getMenuDate().getDayOfWeek() == DayOfWeek.MONDAY).collect(Collectors.toList());
        mondays.forEach(mondayFirst -> {
            TabClassMenuViewDetail tabClassMenuViewDetail = new TabClassMenuViewDetail();
            String timeApplyWeek = DateTimeFormatter.ofPattern("(dd/LL/yyyy").format(mondayFirst) + "-" + DateTimeFormatter.ofPattern("dd/LL/yyyy)").format(mondayFirst.plusDays(6));
            tabClassMenuViewDetail.setTimeApplyWeek(timeApplyWeek);
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int weekNumber = 0;
            if (mondayFirst.plusDays(7).getYear() == mondayFirst.getYear() + 1) {
                weekNumber = 53;
            } else {
                weekNumber = mondayFirst.get(weekFields.weekOfWeekBasedYear());
            }
            tabClassMenuViewDetail.setWeeknumber(String.valueOf(weekNumber));
            tabClassMenuViewDetail.setIdClass(idClass);
            tabClassMenuViewDetail.setIsMonday(mondayFirst.toString());
            classMenuList.forEach(classMenu -> {
                if (mondayFirst.isEqual(classMenu.getMenuDate())) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int j = 0; j <= 6; j++) {
                        List<ClassMenu> classMenuOptional = classMenuRepository.findDistinctByMaClassIdAndMaClassDelActiveTrueAndMenuDate(idClass, mondayFirst.plusDays(j));
                        if (classMenuOptional.isEmpty()) {
                            continue;
                        }
                        ClassMenu classMenu1 = classMenuOptional.get(0);
                        if (StringUtils.isNotBlank(classMenu1.getBreakfastTime()) || StringUtils.isNotBlank(classMenu1.getBreakfastContentList())) {
                            if (classMenu1.getMenuDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                if (classMenu1.getMaClass().isSunday()) {
                                    stringBuilder.append("CN" + " | ");
                                }
                            } else if (classMenu1.getMenuDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                if (classMenu1.getMaClass().isMorningSaturday() || classMenu1.getMaClass().isAfternoonSaturday() || classMenu1.getMaClass().isEveningSaturday()) {
                                    stringBuilder.append("T" + (j + 2) + " | ");
                                }
                            } else {
                                stringBuilder.append("T" + (j + 2) + " | ");
                            }

                            tabClassMenuViewDetail.setListCheckContentday(stringBuilder.toString());

                            continue;
                        } else if (StringUtils.isNotBlank(classMenu1.getSecondBreakfastTime()) || StringUtils.isNotBlank(classMenu1.getSecondBreakfastContentList())) {
                            if (classMenu1.getMenuDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                if (classMenu1.getMaClass().isSunday()) {
                                    stringBuilder.append("CN" + " | ");
                                }
                            } else if (classMenu1.getMenuDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                if (classMenu1.getMaClass().isMorningSaturday() || classMenu1.getMaClass().isAfternoonSaturday() || classMenu1.getMaClass().isEveningSaturday()) {
                                    stringBuilder.append("T" + (j + 2) + " | ");
                                }
                            } else {
                                stringBuilder.append("T" + (j + 2) + " | ");
                            }

                            tabClassMenuViewDetail.setListCheckContentday(stringBuilder.toString());

                            continue;
                        } else if (StringUtils.isNotBlank(classMenu1.getLunchTime()) || StringUtils.isNotBlank(classMenu1.getLunchContentList())) {
                            if (classMenu1.getMenuDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                if (classMenu1.getMaClass().isSunday()) {
                                    stringBuilder.append("CN" + " | ");
                                }
                            } else if (classMenu1.getMenuDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                if (classMenu1.getMaClass().isMorningSaturday() || classMenu1.getMaClass().isAfternoonSaturday() || classMenu1.getMaClass().isEveningSaturday()) {
                                    stringBuilder.append("T" + (j + 2) + " | ");
                                }
                            } else {
                                stringBuilder.append("T" + (j + 2) + " | ");
                            }

                            tabClassMenuViewDetail.setListCheckContentday(stringBuilder.toString());

                            continue;
                        } else if (StringUtils.isNotBlank(classMenu1.getAfternoonTime()) || StringUtils.isNotBlank(classMenu1.getAfternoonContentList())) {
                            if (classMenu1.getMenuDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                if (classMenu1.getMaClass().isSunday()) {
                                    stringBuilder.append("CN" + " | ");
                                }
                            } else if (classMenu1.getMenuDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                if (classMenu1.getMaClass().isMorningSaturday() || classMenu1.getMaClass().isAfternoonSaturday() || classMenu1.getMaClass().isEveningSaturday()) {
                                    stringBuilder.append("T" + (j + 2) + " | ");
                                }
                            } else {
                                stringBuilder.append("T" + (j + 2) + " | ");
                            }

                            tabClassMenuViewDetail.setListCheckContentday(stringBuilder.toString());

                            continue;
                        } else if (StringUtils.isNotBlank(classMenu1.getSecondAfternoonTime()) || StringUtils.isNotBlank(classMenu1.getSecondAfternoonContentList())) {
                            if (classMenu1.getMenuDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                if (classMenu1.getMaClass().isSunday()) {
                                    stringBuilder.append("CN" + " | ");
                                }
                            } else if (classMenu1.getMenuDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                if (classMenu1.getMaClass().isMorningSaturday() || classMenu1.getMaClass().isAfternoonSaturday() || classMenu1.getMaClass().isEveningSaturday()) {
                                    stringBuilder.append("T" + (j + 2) + " | ");
                                }
                            } else {
                                stringBuilder.append("T" + (j + 2) + " | ");
                            }

                            tabClassMenuViewDetail.setListCheckContentday(stringBuilder.toString());

                            continue;
                        } else if (StringUtils.isNotBlank(classMenu1.getDinnerTime()) || StringUtils.isNotBlank(classMenu1.getDinnerContentList())) {
                            if (classMenu1.getMenuDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                                if (classMenu1.getMaClass().isSunday()) {
                                    stringBuilder.append("CN" + " | ");
                                }
                            } else if (classMenu1.getMenuDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                                if (classMenu1.getMaClass().isMorningSaturday() || classMenu1.getMaClass().isAfternoonSaturday() || classMenu1.getMaClass().isEveningSaturday()) {
                                    stringBuilder.append("T" + (j + 2) + " | ");
                                }
                            } else {
                                stringBuilder.append("T" + (j + 2) + " | ");
                            }

                            tabClassMenuViewDetail.setListCheckContentday(stringBuilder.toString());

                            continue;
                        }
                    }
                    tabClassMenuViewDetail.setApprove(classMenu.isApproved());
                }
            });

            /**
             * Hiển thị file hoặc ảnh
             */
            List<ManuFile> menuFieList = classMenuFileRepository.findByMaClassIdAndFromFileTimeAndToFileTime(idClass, mondayFirst, mondayFirst.plusDays(6));
            if (!CollectionUtils.isEmpty(menuFieList)) {
                List<FileAndPictureMenuResponse> filePictures = new ArrayList<>();
                menuFieList.forEach(menuFile -> {
                    menuFile.getUrlMenuFileList().forEach(
                            urlMenuFile -> {
                                FileAndPictureMenuResponse fileAndPictureMenuResponse = new FileAndPictureMenuResponse();
                                if (StringUtils.isNotBlank(urlMenuFile.getNameFile())) {
                                    fileAndPictureMenuResponse.setIdUrlMenuFile(urlMenuFile.getId());

                                    fileAndPictureMenuResponse.setName(urlMenuFile.getNameFile());
                                    fileAndPictureMenuResponse.setUrl(urlMenuFile.getUrlFile());
                                } else if (StringUtils.isNotBlank(urlMenuFile.getNamePicture())) {
                                    fileAndPictureMenuResponse.setIdUrlMenuFile(urlMenuFile.getId());

                                    fileAndPictureMenuResponse.setName(urlMenuFile.getNamePicture());
                                    fileAndPictureMenuResponse.setUrl(urlMenuFile.getUrlPicture());
                                }
                                filePictures.add(fileAndPictureMenuResponse);
                            }
                    );
                });
                tabClassMenuViewDetail.setFileList(filePictures);
            }

            tabClassMenuViewDetailList.add(tabClassMenuViewDetail);


        });

        return tabClassMenuViewDetailList;
    }

    @Override
    public boolean updateApprove(Long idSchool, ApproveStatus approveStatus) {
        Optional<MaClass> maClassOptional = maClassRepository.findByIdAndIdSchoolAndDelActiveTrue(approveStatus.getIdClass(), idSchool);
        if (maClassOptional.isEmpty()) {
            return false;
        }

        LocalDate monday = approveStatus.getIsMonday();
//        LocalDate mondayParse = LocalDate.parse(monday);
        MaClass maClass = maClassOptional.get();
        List<ManuFile> manuFiles = classMenuFileRepository.findManuFile(idSchool, approveStatus.getIdClass(), monday);
        if (manuFiles.size() > 0) {
            manuFiles.get(0).setApproved(approveStatus.isApprove());
            classMenuFileRepository.save(manuFiles.get(0));
        }
        for (int i = 0; i <= 6; i++) {
            LocalDate mondayParse1 = monday.plusDays(i);
            List<ClassMenu> classMenuList = maClass.getClassMenuList().stream().filter(x -> x.getMenuDate().isEqual(mondayParse1)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(classMenuList)) {
                ClassMenu classMenu = classMenuList.get(0);
                classMenu.setApproved(approveStatus.isApprove());
                classMenuRepository.save(classMenu);
            }
        }
        return true;
    }

    @Override
    public boolean updateMultiApprove(Long idSchool, List<ApproveStatus> approveStatusList) {
        return false;
    }

    @Override
    public boolean saveMenuFile(Long idSchool, UserPrincipal principal, CreateFileAndPictureMenuRequest createFileAndPictureMenuRequest) throws IOException {
        int monthCurrent = LocalDate.now().getMonthValue();
        int yearCurrent = LocalDate.now().getYear();
        LocalDate datePlus = createFileAndPictureMenuRequest.getFromFileTime().plusDays(6);
        ManuFile manuFile = classMenuFileRepository.findMenuFile(idSchool, createFileAndPictureMenuRequest.getIdClass(), createFileAndPictureMenuRequest.getFromFileTime());

        List<ClassMenu> classMenu = classMenuRepository.findByMaClassIdAndMaClassDelActiveTrueAndMenuDateBetween(principal.getIdClassLogin(), createFileAndPictureMenuRequest.getFromFileTime(), datePlus);


        if (manuFile == null) {
            ManuFile manuFileNew = new ManuFile();
            manuFileNew.setIdSchool(idSchool);
            if (!CollectionUtils.isEmpty(classMenu)) {
                manuFileNew.setApproved(classMenu.get(0).isApproved());
            } else {
                manuFileNew.setApproved(!principal.getSchoolConfig().isApprovedMenu());
            }
            manuFileNew.setFromFileTime(createFileAndPictureMenuRequest.getFromFileTime());
            manuFileNew.setToFileTime(createFileAndPictureMenuRequest.getFromFileTime().plusDays(6));
            manuFileNew.setMaClass(maClassRepository.findByIdMaClass(idSchool, createFileAndPictureMenuRequest.getIdClass()).get());

            Set<UrlMenuFile> urlMenuFiles = new HashSet<>();
            List<MultipartFile> multipartFileList = createFileAndPictureMenuRequest.getMultipartFileList();
            multipartFileList.forEach(multipartFilex -> {

                if (createFileAndPictureMenuRequest.getMultipartFileList() != null) {
                    String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.THUC_DON);
                    String fileName = HandleFileUtils.removeSpace(System.currentTimeMillis() + "_" + idSchool + "_" + multipartFilex.getOriginalFilename());
                    try {
                        HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFilex, fileName, UploadDownloadConstant.WIDTH_OTHER);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String urlFileMenu = AppConstant.URL_DEFAULT + idSchool + "/" + yearCurrent + "/T" + monthCurrent + "/" + "thucdon/";
                    String uploadFileMenu = AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + yearCurrent + "\\T" + monthCurrent + "\\" + "thucdon\\";
                    String directoryFileMenu = AppConstant.DIRECTORY_DEFAULT + idSchool + "\\\\" + yearCurrent + "\\\\T" + monthCurrent + "\\\\" + "thucdon\\\\";
                    Path pathFileMenu = Paths.get(uploadFileMenu, fileName);
                    try {
                        Files.write(pathFileMenu, multipartFilex.getBytes());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                    String extension = FilenameUtils.getExtension(multipartFilex.getOriginalFilename());

                    Set<ManuFile> urlMenuFileSet = new HashSet<>();
                    if (!extension.equals("jpg") && !extension.equals("png") && !extension.equals("jpeg")) {
                        UrlMenuFile urlMenuFileObject = new UrlMenuFile();
                        urlMenuFileObject.setNameFile(multipartFilex.getOriginalFilename());
                        urlMenuFileObject.setUrlFile(urlFileMenu + fileName);
                        urlMenuFileObject.setUrlLocalFile(directoryFileMenu + fileName);

                        urlMenuFileSet.add(manuFileNew);
                        urlMenuFileRepository.save(urlMenuFileObject);
                        urlMenuFiles.add(urlMenuFileObject);
                    } else {
                        UrlMenuFile urlMenuFileObject = new UrlMenuFile();
                        urlMenuFileObject.setNamePicture(multipartFilex.getOriginalFilename());
                        urlMenuFileObject.setUrlPicture(urlFileMenu + fileName);
                        urlMenuFileObject.setUrlLocalPicture(directoryFileMenu + fileName);

                        urlMenuFileSet.add(manuFileNew);
                        urlMenuFileRepository.save(urlMenuFileObject);
                        urlMenuFiles.add(urlMenuFileObject);
                    }
                }


            });
            manuFileNew.setUrlMenuFileList(urlMenuFiles);
            classMenuFileRepository.save(manuFileNew);
        } else {
            ManuFile manuFileOld = manuFile;
            Set<UrlMenuFile> urlMenuFilesOld = manuFileOld.getUrlMenuFileList();
            List<MultipartFile> multipartFileList = createFileAndPictureMenuRequest.getMultipartFileList();

            multipartFileList.forEach(multipartFilex -> {

                if (createFileAndPictureMenuRequest.getMultipartFileList() != null) {
                    String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.THUC_DON);
                    String fileName = HandleFileUtils.removeSpace(System.currentTimeMillis() + "_" + idSchool + "_" + multipartFilex.getOriginalFilename());
                    try {
                        HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFilex, fileName, UploadDownloadConstant.WIDTH_OTHER);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String urlFileMenu = AppConstant.URL_DEFAULT + idSchool + "/" + yearCurrent + "/T" + monthCurrent + "/" + "thucdon/";
                    String uploadFileMenu = AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + yearCurrent + "\\T" + monthCurrent + "\\" + "thucdon\\";
                    String directoryFileMenu = AppConstant.DIRECTORY_DEFAULT + idSchool + "\\\\" + yearCurrent + "\\\\T" + monthCurrent + "\\\\" + "thucdon\\\\";
                    Path pathFileMenu = Paths.get(uploadFileMenu, fileName);
                    try {
                        Files.write(pathFileMenu, multipartFilex.getBytes());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                    String extension = FilenameUtils.getExtension(multipartFilex.getOriginalFilename());

                    Set<ManuFile> urlMenuFileSet = new HashSet<>();
                    if (!extension.equals("jpg") && !extension.equals("png") && !extension.equals("jpeg")) {
                        UrlMenuFile urlMenuFileObject = new UrlMenuFile();
                        urlMenuFileObject.setNameFile(multipartFilex.getOriginalFilename());
                        urlMenuFileObject.setUrlFile(urlFileMenu + fileName);
                        urlMenuFileObject.setUrlLocalFile(directoryFileMenu + fileName);

                        urlMenuFileSet.add(manuFileOld);
                        urlMenuFileRepository.save(urlMenuFileObject);
                        urlMenuFilesOld.add(urlMenuFileObject);
                    } else {
                        UrlMenuFile urlMenuFileObject = new UrlMenuFile();
                        urlMenuFileObject.setNamePicture(multipartFilex.getOriginalFilename());
                        urlMenuFileObject.setUrlPicture(urlFileMenu + fileName);
                        urlMenuFileObject.setUrlLocalPicture(directoryFileMenu + fileName);

                        urlMenuFileSet.add(manuFileOld);
                        urlMenuFileRepository.save(urlMenuFileObject);
                        urlMenuFilesOld.add(urlMenuFileObject);
                    }
                }


            });
            manuFileOld.setUrlMenuFileList(urlMenuFilesOld);
            classMenuFileRepository.save(manuFileOld);

        }

        return true;
    }

    @Transactional
    @Override
    public boolean deletMenuFileById(Long idSchool, Long idUrlMenuFile) {
//        ScheduleFile scheduleFile = scheduleFileRepository.findById(idScheduleFile).get();
//        scheduleFileRepository.deleteById(idScheduleFile);
        deleteEx(idUrlMenuFile);
        UrlMenuFile urlMenuFile = urlMenuFileRepository.findById(idUrlMenuFile).get();
        String urlLocal = "";
        String urlLocalThumbnail = "";
        if (StringUtils.isNotBlank(urlMenuFile.getUrlLocalFile())) {
            urlLocal = HandleFileUtils.removeSpace(urlMenuFile.getUrlLocalFile());
            urlLocalThumbnail = StringUtils.replace(urlLocal, UploadDownloadConstant.HOC_TAP + "\\\\", UploadDownloadConstant.HOC_TAP + "\\\\thumbnail_");
        } else if (StringUtils.isNotBlank(urlMenuFile.getUrlLocalPicture())) {
            urlLocal = HandleFileUtils.removeSpace(urlMenuFile.getUrlLocalPicture());
            urlLocalThumbnail = StringUtils.replace(urlLocal, UploadDownloadConstant.HOC_TAP + "\\\\", UploadDownloadConstant.HOC_TAP + "\\\\thumbnail_");
        }

        HandleFileUtils.deleteFilePictureInDirectory(urlLocal, urlLocalThumbnail);


        deleteUrl(idUrlMenuFile);
        return true;
    }

    public void deleteEx(Long idUrlMenuFile) {
        classMenuFileRepository.deleteExUrlFile(idUrlMenuFile);
    }

    public void deleteUrl(Long idUrlMenuFile) {
        urlMenuFileRepository.deleteUrlFile(idUrlMenuFile);
    }

    @Override
    public boolean deleteContentMenu(List<SearchAllClassMenuRequest> searchAllClassMenuRequests) {
        searchAllClassMenuRequests.forEach(searchMenuInClassRequest -> {
            List<ClassMenu> classMenuList = classMenuRepository.findByMaClassIdAndMaClassDelActiveTrueAndMenuDateBetween(searchMenuInClassRequest.getIdClass(), LocalDate.parse(searchMenuInClassRequest.getIsMonday()), LocalDate.parse(searchMenuInClassRequest.getIsMonday()).plusDays(6));
            if (CollectionUtils.isEmpty(classMenuList)) {
                return;
            }
            classMenuList.forEach(classMenu -> {
                if (classMenu != null) {
                    classMenuRepository.deleteById(classMenu.getId());

                }
            });
        });

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
                ManuFile manuFile = classMenuFileRepository.findMenuFile(idSchool, idClass, monday);

                List<ClassMenu> classMenu = classMenuRepository.findByMaClassIdAndMaClassDelActiveTrueAndMenuDateBetween(idClass, monday, datePlus);
                List<MultipartFile> multipartFileList = fileAndPictureMenuMultiClassRequest.getMultipartFile();
                if (manuFile != null) {
                    Set<UrlMenuFile> urlMenuFiles = manuFile.getUrlMenuFileList();
                    urlMenuFiles.forEach(x -> {
                        if (!Strings.isEmpty(x.getUrlLocalFile())) {
                            HandleFileUtils.deleteFileOrPictureInFolder(x.getUrlLocalFile());
                        }
                        if (!Strings.isEmpty(x.getUrlLocalPicture())) {
                            HandleFileUtils.deleteFileOrPictureInFolder(x.getUrlLocalPicture());
                        }
                        urlMenuFileRepository.delete(x);
                    });
                    classMenuFileRepository.delete(manuFile);
                }
                // tuần đó chưa có file hoạc ảnh -> tạo mới
                ManuFile manuFileNew = new ManuFile();
                manuFileNew.setIdSchool(idSchool);
                if (!CollectionUtils.isEmpty(classMenu)) {
                    manuFileNew.setApproved(classMenu.get(0).isApproved());
                } else {
                    manuFileNew.setApproved(!principal.getSchoolConfig().isApprovedMenu());
                }
                manuFileNew.setFromFileTime(monday);
                manuFileNew.setToFileTime(datePlus);
                manuFileNew.setMaClass(maClassRepository.findByIdMaClass(idSchool, idClass).get());
                classMenuFileRepository.save(manuFileNew);
                Set<UrlMenuFile> urlMenuFiles = new HashSet<>();
                Set<ManuFile> urlMenuFileSet = new HashSet<>();
                for (MultipartFile multipartFilex : multipartFileList) {
//                    String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_FOLDER, UploadDownloadConstant.THUC_DON);
                    String extension = FilenameUtils.getExtension(multipartFilex.getOriginalFilename());

                    if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpeg")) {
                        HandleFileResponse handleFileResponse = HandleFileUtils.getUrlFieldOrPictureSaved(multipartFilex, idSchool, UploadDownloadConstant.THUC_DON);
                        UrlMenuFile urlMenuFileObject = new UrlMenuFile();

                        urlMenuFileObject.setNamePicture(handleFileResponse.getName());
                        urlMenuFileObject.setUrlPicture(handleFileResponse.getUrlWeb());
                        urlMenuFileObject.setUrlLocalPicture(handleFileResponse.getUrlLocal());

                        urlMenuFileSet.add(manuFileNew);
                        urlMenuFileRepository.save(urlMenuFileObject);
                        urlMenuFiles.add(urlMenuFileObject);
                    } else {
                        UrlMenuFile urlMenuFileObject = new UrlMenuFile();
                        HandleFileResponse handleFileResponse = HandleFileUtils.getUrlFieldOrPictureSaved(multipartFilex, idSchool, UploadDownloadConstant.THUC_DON);

                        urlMenuFileObject.setNameFile(handleFileResponse.getName());
                        urlMenuFileObject.setUrlFile(handleFileResponse.getUrlWeb());
                        urlMenuFileObject.setUrlLocalFile(handleFileResponse.getUrlLocal());

                        urlMenuFileSet.add(manuFileNew);
                        urlMenuFileRepository.save(urlMenuFileObject);
                        urlMenuFiles.add(urlMenuFileObject);
                    }
                }
                manuFileNew.setUrlMenuFileList(urlMenuFiles);
                classMenuFileRepository.save(manuFileNew);

            }
        }
        return true;
    }


}
