package com.example.onekids_project.service.serviceimpl.chart;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.Album;
import com.example.onekids_project.entity.classes.ListPicture;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.EmployeeStatusTimeline;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.KidsStatusTimeline;
import com.example.onekids_project.entity.school.Grade;
import com.example.onekids_project.model.chart.EmployeeStatusTimelineModel;
import com.example.onekids_project.model.chart.KidsStatusTimelineModel;
import com.example.onekids_project.model.finance.OrderNumberModel;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.chart.*;
import com.example.onekids_project.response.changeQuery.chart.AttendanceKidsQueryResponse;
import com.example.onekids_project.response.changeQuery.chart.EvaluateKidsDateQueryResponse;
import com.example.onekids_project.response.changeQuery.chart.EvaluateKidsWeekMonthQueryResponse;
import com.example.onekids_project.response.chart.*;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.chart.ChartKidsService;
import com.example.onekids_project.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

/**
 * date 2021-09-07 2:06 PM
 *
 * @author nguyễn văn thụ
 */
@Service
public class ChartKidsServiceImpl implements ChartKidsService {

    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private EvaluateDateRepository evaluateDateRepository;
    @Autowired
    private EvaluateWeekRepository evaluateWeekRepository;
    @Autowired
    private EvaluateMonthRepository evaluateMonthRepository;
    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;
    @Autowired
    private AttendanceArriveKidsRepository attendanceArriveKidsRepository;
    @Autowired
    private FnOrderKidsRepository fnOrderKidsRepository;
    @Autowired
    private FnKidsPackageRepository fnKidsPackageRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ListPictureRepository listPictureRepository;
    @Autowired
    private KidsStatusTimelineRepository kidsStatusTimelineRepository;
    @Autowired
    private EmployeeStatusTimelineRepository employeeStatusTimelineRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Long> findAllStatusKids(Long idSchool) {
        List<Long> longList = new ArrayList<>();
        List<Kids> kidsList = kidsRepository.findByIdSchoolAndDelActiveTrue(idSchool);
        long studying = kidsList.stream().filter(x -> AppConstant.STUDYING.equals(x.getKidStatus())).count();
        long studyWait = kidsList.stream().filter(x -> AppConstant.STUDY_WAIT.equals(x.getKidStatus())).count();
        long leaveSchool = kidsList.stream().filter(x -> AppConstant.LEAVE_SCHOOL.equals(x.getKidStatus())).count();
        long reserve = kidsList.stream().filter(x -> AppConstant.RESERVE.equals(x.getKidStatus())).count();
        longList.add(0, studying);
        longList.add(1, studyWait);
        longList.add(2, reserve);
        longList.add(3, leaveSchool);
        return longList;
    }

    @Override
    public List<ChartStatusKidsResponse> findStuddingStatusKids(Long idSchool, StatusKidsChartRequest request) {
        List<ChartStatusKidsResponse> responseList = new ArrayList<>();
        int[] dates = new int[]{1, 10, 20};
        if (request.getDate().getDayOfMonth() < 10) {
            dates = new int[]{1};
        } else if (request.getDate().getDayOfMonth() < 20) {
            dates = new int[]{1, 10};
        }
        List<LocalDate> dateList = this.setListDateCustomDate(dates, request.getDate().getYear());
//        if (request.getDate().isAfter(LocalDate.now())) {
//            dateList =
//        }
//        assert dateList != null;
        dateList.forEach(x -> {
            ChartStatusKidsResponse response = new ChartStatusKidsResponse();
            long countKids = attendanceKidsRepository.countByIdSchoolAndAttendanceDateAndDelActiveTrue(idSchool, x);
            response.setName(" Ngày " + (x.getDayOfMonth() < 10 ? "0" + x.getDayOfMonth() : x.getDayOfMonth()) + "/" + (x.getMonthValue() < 10 ? "0" + x.getMonthValue() : x.getMonthValue()));
            response.setCountStudding(countKids);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<KidsStatusTimelineResponse> findFiveStatusKids(Long idSchool, int year) {
        List<KidsStatusTimelineResponse> responseList = new ArrayList<>();
        List<KidsStatusTimelineModel> modelList = this.getKidsStatusTimeline(idSchool, year);
        modelList.forEach(x -> {
            KidsStatusTimelineResponse response = new KidsStatusTimelineResponse();
            response.setName("Tháng " + x.getMonth());
            modelMapper.map(x, response);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<KidsStatusTimelineResponse> findFirstMonthStatusKids(Long idSchool, int year) {
        List<KidsStatusTimelineResponse> responseList = new ArrayList<>();
        List<KidsStatusTimelineModel> modelList = this.getKidsMonthStatusTimeline(idSchool, year);
        modelList.forEach(x -> {
            KidsStatusTimelineResponse response = new KidsStatusTimelineResponse();
            response.setName("Ngày 1/" + x.getMonth());
            modelMapper.map(x, response);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<EmployeeStatusTimelineResponse> findFourStatusEmployee(Long idSchool, int year) {
        List<EmployeeStatusTimelineResponse> responseList = new ArrayList<>();
        List<EmployeeStatusTimelineModel> modelList = this.getEmployeeStatusTimeline(idSchool, year);
        modelList.forEach(x -> {
            EmployeeStatusTimelineResponse response = new EmployeeStatusTimelineResponse();
            response.setName("Tháng " + x.getMonth());
            modelMapper.map(x, response);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<ChartStatusKidsResponse> findDetailStatusKids(Long idSchool, StatusKidsChartRequest request) {
        List<ChartStatusKidsResponse> responseList = new ArrayList<>();
        if (AppConstant.CHART_GRADE.equals(request.getTypeSchool())) {
            List<Grade> gradeList = gradeRepository.findByDelActiveTrueAndSchool_Id(idSchool);
            gradeList.forEach(y -> {
                List<Kids> kidsList = kidsRepository.findByIdSchoolAndIdGradeAndDelActiveTrue(idSchool, y.getId());
                ChartStatusKidsResponse response = this.setStatusKids(kidsList);
                response.setName(y.getGradeName());
                responseList.add(response);
            });
        } else {
            List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(idSchool);
            maClassList.forEach(y -> {
                List<Kids> kidsList = kidsRepository.findByIdSchoolAndMaClassIdAndDelActiveTrue(idSchool, y.getId());
                ChartStatusKidsResponse response = this.setStatusKids(kidsList);
                response.setName(y.getClassName());
                responseList.add(response);
            });
        }
        return responseList;
    }

    @Override
    public List<ChartEvaluateResponse> getEvaluateChart(Long idSchool, EvaluateKidsChartRequest request) {
        List<ChartEvaluateResponse> responseList = new ArrayList<>();
        List<LocalDate> dateList = null;
        Long idGrade = request.getIdGrade();
        Long idClass = request.getIdClass();
        if (AppConstant.CHART_DATE.equals(request.getType())) {
            dateList = this.setListDate(request.getDateList().get(0), request.getDateList().get(1));
        } else if (AppConstant.CHART_WEEK.equals(request.getType())) {
            if (request.getWeekList().get(0).isBefore(request.getWeekList().get(1))) {
                dateList = this.setListWeek(request.getWeekList().get(0), request.getWeekList().get(1));
            } else {
                dateList = this.setListWeek(request.getWeekList().get(1), request.getWeekList().get(0));
            }
        } else if (AppConstant.CHART_MONTH.equals(request.getType())) {
            LocalDate startDate = request.getMonthList().get(0);
            LocalDate endDate = request.getMonthList().get(1);
            dateList = this.setListMonth(startDate, endDate);
        }
        assert dateList != null;
        dateList.forEach(a -> {
            ChartEvaluateResponse response = new ChartEvaluateResponse();
            if (AppConstant.CHART_DATE.equals(request.getType())) {
                List<EvaluateKidsDateQueryResponse> evaluateDateList = evaluateDateRepository.getEvaluateKidsDateChart(idSchool, idGrade, idClass, a);
                long countEvaluate = evaluateDateList.stream().filter(x -> !x.getLearnContent().equals("") || !x.getEatContent().equals("") || !x.getSleepContent().equals("") ||
                        !x.getSanitaryContent().equals("") || !x.getHealtContent().equals("") || !x.getCommonContent().equals("")).map(EvaluateKidsDateQueryResponse::getKids).filter(BaseEntity::isDelActive).distinct().count();
                response.setName(ConvertData.convertLocalDateToString(a));
                response.setEvaluateYes(countEvaluate);
                response.setEvaluateNo(evaluateDateList.size() - countEvaluate);
            } else if (AppConstant.CHART_WEEK.equals(request.getType())) {
                List<EvaluateKidsWeekMonthQueryResponse> evaluateWeekList = evaluateWeekRepository.getEvaluateKidsWeekChart(idSchool, idGrade, idClass, a);
                long countEvaluate = evaluateWeekList.stream().filter(x -> !x.getContent().equals("")).map(EvaluateKidsWeekMonthQueryResponse::getKids).filter(BaseEntity::isDelActive).distinct().count();
                int weekOfYear = a.get(WeekFields.of(Locale.getDefault()).weekOfYear()) - 1;
                response.setName("Tuần " + weekOfYear + "");
                response.setEvaluateYes(countEvaluate);
                response.setEvaluateNo(evaluateWeekList.size() - countEvaluate);
            } else if (AppConstant.CHART_MONTH.equals(request.getType())) {
                List<EvaluateKidsWeekMonthQueryResponse> evaluateMonthList = evaluateMonthRepository.getEvaluateKidsMonthChart(idSchool, idGrade, idClass, a.getMonthValue(), a.getYear());
                long countEvaluate = evaluateMonthList.stream().filter(x -> !x.getContent().equals("")).map(EvaluateKidsWeekMonthQueryResponse::getKids).filter(BaseEntity::isDelActive).distinct().count();
                response.setName(ConvertData.convertDateToMonth(a.getMonthValue(), a.getYear()));
                response.setEvaluateYes(countEvaluate);
                response.setEvaluateNo(evaluateMonthList.size() - countEvaluate);
            }
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<ChartAlbumResponse> getAlbumChart(Long idSchool, AlbumKidsChartRequest request) {
        List<ChartAlbumResponse> responseList = new ArrayList<>();
        if (AppConstant.CHART_CLASS.equals(request.getTypeSchool())) {
            List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(idSchool);
            maClassList.forEach(x -> {
                ChartAlbumResponse response = new ChartAlbumResponse();
                long albumYes = 0;
                long albumNo = 0;
                List<Album> albumList = albumRepository.findAllAlbumClassChart(x.getId(), AppConstant.TYPE_CLASS, request.getDate());
                for (Album album : albumList) {
                    List<ListPicture> listPictures = listPictureRepository.findByAlbumIdAndIsApprovedTrueAndDelActiveTrue(album.getId());
                    if (CollectionUtils.isNotEmpty(listPictures)) {
                        albumYes++;
                    } else {
                        albumNo++;
                    }
                }
                response.setAlbumYes(albumYes);
                response.setAlbumNo(albumNo);
                response.setName(x.getClassName());
                responseList.add(response);
            });
        } else {
            List<Grade> gradeList = gradeRepository.findByDelActiveTrueAndSchool_Id(idSchool);
            gradeList.forEach(x -> {
                ChartAlbumResponse response = new ChartAlbumResponse();
                long albumYes = 0;
                long albumNo = 0;
                List<Album> albumList = albumRepository.findAllAlbumGradeChart(x.getId(), AppConstant.TYPE_CLASS, request.getDate());
                for (Album album : albumList) {
                    List<ListPicture> listPictures = listPictureRepository.findByAlbumIdAndIsApprovedTrueAndDelActiveTrue(album.getId());
                    if (CollectionUtils.isNotEmpty(listPictures)) {
                        albumYes++;
                    } else {
                        albumNo++;
                    }
                }
                response.setAlbumYes(albumYes);
                response.setAlbumNo(albumNo);
                response.setName(x.getGradeName());
                responseList.add(response);
            });
        }
        return responseList;
    }

    @Override
    public List<ChartAlbumResponse> getAlbumSchoolDateChart(Long idSchool, AlbumKidsChartRequest request) {
        List<ChartAlbumResponse> responseList = new ArrayList<>();
        List<LocalDate> dateList = this.setListDate(request.getDateList().get(0), request.getDateList().get(1));
        List<Album> albumList = albumRepository.findAllAlbumDateInChart(idSchool, request.getIdClass(), request.getIdGrade(), AppConstant.TYPE_SCHOOL, request.getDateList());
        dateList.forEach(date -> {
            ChartAlbumResponse response = new ChartAlbumResponse();
            long albumYes = 0;
            long albumNo = 0;
            List<Album> albumList1 = albumList.stream().filter(x -> date.equals(x.getCreatedDate().toLocalDate())).collect(Collectors.toList());
            for (Album album : albumList1) {
                List<ListPicture> listPictures = listPictureRepository.findByAlbumIdAndIsApprovedTrueAndDelActiveTrue(album.getId());
                if (CollectionUtils.isNotEmpty(listPictures)) {
                    albumYes++;
                } else {
                    albumNo++;
                }
            }
            response.setAlbumYes(albumYes);
            response.setAlbumNo(albumNo);
            response.setName(ConvertData.convertLocalDateToString(date));
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<ChartAlbumResponse> getAlbumDateChart(Long idSchool, AlbumKidsChartRequest request) {
        List<ChartAlbumResponse> responseList = new ArrayList<>();
        List<LocalDate> dateList = this.setListDate(request.getDateList().get(0), request.getDateList().get(1));
        List<Album> albumList = albumRepository.findAllAlbumDateInChart(idSchool, request.getIdClass(), request.getIdGrade(), AppConstant.TYPE_CLASS, request.getDateList());
        dateList.forEach(date -> {
            ChartAlbumResponse response = new ChartAlbumResponse();
            long albumYes = 0;
            long albumNo = 0;
            List<Album> albumList1 = albumList.stream().filter(x -> date.equals(x.getCreatedDate().toLocalDate())).collect(Collectors.toList());
            for (Album album : albumList1) {
                List<ListPicture> listPictures = listPictureRepository.findByAlbumIdAndIsApprovedTrueAndDelActiveTrue(album.getId());
                if (CollectionUtils.isNotEmpty(listPictures)) {
                    albumYes++;
                } else {
                    albumNo++;
                }
            }
            response.setAlbumYes(albumYes);
            response.setAlbumNo(albumNo);
            response.setName(ConvertData.convertLocalDateToString(date));
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<ChartAttendanceResponse> getAttendanceChart(Long idSchool, AttendanceKidsChartRequest request) {
        List<ChartAttendanceResponse> responseList = new ArrayList<>();
        LocalDate startDate = null;
        LocalDate endDate = null;

        if (AppConstant.CHART_DATE.equals(request.getType())) {
            startDate = request.getDateList().get(0);
            endDate = request.getDateList().get(1);
        } else if (AppConstant.CHART_WEEK.equals(request.getType())) {
            startDate = request.getWeek();
            endDate = request.getWeek().plusDays(6);
        } else if (AppConstant.CHART_MONTH.equals(request.getType())) {
            startDate = request.getMonth();
            endDate = request.getMonth().plusMonths(1).plusDays(-1);
        }
        assert startDate != null;
        int[] dates = new int[]{1, 5, 10, 15, 20};
        List<LocalDate> dateList = AppConstant.CHART_YEAR.equals(request.getType()) ? this.setListDateCustomDate(dates, request.getYear().getYear()) : this.setListDate(startDate, endDate);
        List<AttendanceKidsQueryResponse> attendanceKidsList = attendanceKidsRepository.searchAttendanceKidsChart(idSchool, request.getIdGrade(), request.getIdClass(), dateList);
        dateList.forEach(a -> {
            List<AttendanceKidsQueryResponse> attendanceKidsList1 = attendanceKidsList.stream().filter(x -> a.equals(x.getAttendanceDate())).collect(Collectors.toList());
            ChartAttendanceResponse response = new ChartAttendanceResponse();
            List<Long> kidsList = new ArrayList<>();
            List<Long> kidsListYes = new ArrayList<>();
            List<Long> kidsListNo = new ArrayList<>();
            List<Long> kidsListUn = new ArrayList<>();
            attendanceKidsList1.forEach(x -> {
                if (this.checkArrive(x)) {
                    kidsList.add(x.getIdKid());
                } else if (this.checkArriveYes(x) && !this.checkArrive(x)) {
                    kidsListYes.add(x.getIdKid());
                } else if (this.checkArriveNo(x) && !this.checkArriveYes(x) && !this.checkArrive(x)) {
                    kidsListNo.add(x.getIdKid());
                } else if (!this.checkArriveNo(x) && !this.checkArriveYes(x) && !this.checkArrive(x)) {
                    kidsListUn.add(x.getIdKid());
                }
            });
            response.setName(ConvertData.convertLocalDateToString(a));
            response.setAttendance(kidsList.stream().distinct().count());
            response.setAttendanceYes(kidsListYes.stream().distinct().count());
            response.setAttendanceNo(kidsListNo.stream().distinct().count());
            response.setAttendanceUn(kidsListUn.stream().distinct().count());
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<ChartFeesResponse> getFeesKidsChart(Long idSchool, FeesKidsChartRequest request) {
        List<ChartFeesResponse> responseList = new ArrayList<>();
        //Tháng 1 - 12
        UserPrincipal principal = PrincipalUtils.getUserPrincipal();
        Long idKid = null;
        if (StringUtils.equals(principal.getAppType(), AppTypeConstant.PARENT)) {
            idKid = principal.getIdKidLogin();
        }
        for (int i = 1; i <= 12; i++) {
            ChartFeesResponse response = new ChartFeesResponse();
            List<FnOrderKids> fnOrderKidsList;
            if (Objects.isNull(idKid)) {
                fnOrderKidsList = fnOrderKidsRepository.findByYearAndMonthAndKidsIdSchoolAndDelActiveTrue(request.getYear(), i, idSchool);
            } else {
                fnOrderKidsList = fnOrderKidsRepository.findByKidsIdAndYearAndMonthAndKidsIdSchoolAndDelActiveTrue(idKid, request.getYear(), i, idSchool);
            }
            List<Kids> kidsList = fnOrderKidsList.stream().map(FnOrderKids::getKids).filter(a -> AppConstant.STUDYING.equals(a.getKidStatus())).collect(Collectors.toList());
            long numberSuccess = 0;
            long numberNoSuccess = 0;
            long numberUnSuccess = 0;
            long totalPaidIn = 0;
            long totalPaidOut = 0;
            for (Kids x : kidsList) {
                List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), i, request.getYear());
                OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderModel(fnKidsPackageList);
                if (orderNumberModel.getTotalNumber() == 0) {
                    //chưa có khoảng nào
                    numberUnSuccess++;
                } else if (orderNumberModel.getEnoughNumber() == orderNumberModel.getTotalNumber()) {
                    //chưa hoàn thành hóa đơn
                    numberSuccess++;
                } else if (orderNumberModel.getEnoughNumber() < orderNumberModel.getTotalNumber()) {
                    //đã hoàn thành hóa đơn
                    numberNoSuccess++;
                }
                List<FnKidsPackage> inList = fnKidsPackageList.stream().filter(y -> y.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
                List<FnKidsPackage> outList = fnKidsPackageList.stream().filter(y -> y.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
                totalPaidIn += inList.stream().mapToLong(y -> (long) y.getPaid()).sum();
                totalPaidOut += outList.stream().mapToLong(y -> (long) y.getPaid()).sum();

            }
            response.setMoneyIn(totalPaidIn);
            response.setMoneyOut(totalPaidOut);
            response.setName("Tháng " + i);
            response.setFeesUn(numberUnSuccess);
            response.setFeesYes(numberSuccess);
            response.setFeesNo(numberNoSuccess);
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public List<ExcelResponse> getExcelStatusKidsForChart(Long idSchool, int year) {
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        List<ExcelData> bodyList = new ArrayList<>();
        List<ExcelData> headerList = new ArrayList<>();
        List<KidsStatusTimelineModel> modelList = this.getKidsStatusTimeline(idSchool, year);
        response.setSheetName("Học_sinh");
        for (KidsStatusTimelineModel x : modelList) {
            List<String> headerStringList = Arrays.asList("THỐNG KÊ TRẠNG THÁI HOC SINH NĂM " + year, "");
            headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
            List<String> bodyStringList = Arrays.asList(String.valueOf(x.getMonth()), String.valueOf(x.getStudying()), String.valueOf(x.getStudyWait()),
                    String.valueOf(x.getReserve()), String.valueOf(x.getLeaveSchool()), String.valueOf(x.getOutSchool()));
            ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(modelData);
        }
        response.setHeaderList(headerList);
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    @Override
    public List<ExcelResponse> getExcelStatusEmployeeForChart(Long idSchool, int year) {
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        List<ExcelData> bodyList = new ArrayList<>();
        List<ExcelData> headerList = new ArrayList<>();
        List<EmployeeStatusTimelineModel> modelList = this.getEmployeeStatusTimeline(idSchool, year);
        response.setSheetName("Nhân_sự");
        for (EmployeeStatusTimelineModel x : modelList) {
            List<String> headerStringList = Arrays.asList("THỐNG KÊ TRẠNG THÁI NHÂN SỰ NĂM " + year, "");
            headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
            List<String> bodyStringList = Arrays.asList(String.valueOf(x.getMonth()), String.valueOf(x.getWorking()), String.valueOf(x.getRetain()),
                    String.valueOf(x.getLeave()), String.valueOf(x.getOutSchool()));
            ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(modelData);
        }
        response.setHeaderList(headerList);
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    private boolean checkArrive(AttendanceKidsQueryResponse arriveKids) {
        return arriveKids.isMorning() || arriveKids.isAfternoon() || arriveKids.isEvening();
    }

    private boolean checkArriveYes(AttendanceKidsQueryResponse arriveKids) {
        return arriveKids.isMorningYes() || arriveKids.isAfternoonYes() || arriveKids.isEveningYes();
    }

    private boolean checkArriveNo(AttendanceKidsQueryResponse arriveKids) {
        return arriveKids.isMorningNo() || arriveKids.isAfternoonNo() || arriveKids.isEveningNo();
    }

    private List<LocalDate> setListDate(LocalDate start, LocalDate end) {
        List<LocalDate> totalDates = new ArrayList<>();
        while (!start.isAfter(end)) {
            totalDates.add(start);
            start = start.plusDays(1);
        }
        return totalDates;
    }

    private List<LocalDate> setListMonth(LocalDate start, LocalDate end) {
        List<LocalDate> totalMonths = new ArrayList<>();
        while (!start.isAfter(end)) {
            totalMonths.add(start);
            start = start.plusMonths(1);
        }
        return totalMonths;
    }

    private List<LocalDate> setListWeek(LocalDate start, LocalDate end) {
        List<LocalDate> totalWeeks = new ArrayList<>();
        while (!start.isAfter(end)) {
            totalWeeks.add(start);
            start = start.plusWeeks(1);
        }
        return totalWeeks;
    }

    private List<LocalDate> setListDateCustomDate(int[] dates, int year) {
        List<LocalDate> dateList = new ArrayList<>();
        for (int i = 1; i <= (LocalDate.now().getYear() == year ? LocalDate.now().getMonthValue() : 12); i++) {
            for (int k : dates) {
                LocalDate date = LocalDate.of(year, i, k);
                dateList.add(date);
            }
        }
        return dateList;
    }

    private ChartStatusKidsResponse setStatusKids(List<Kids> kidsList) {
        ChartStatusKidsResponse response = new ChartStatusKidsResponse();
        long studying = kidsList.stream().filter(x -> AppConstant.STUDYING.equals(x.getKidStatus())).count();
        long studyWait = kidsList.stream().filter(x -> AppConstant.STUDY_WAIT.equals(x.getKidStatus())).count();
        long leaveSchool = kidsList.stream().filter(x -> AppConstant.LEAVE_SCHOOL.equals(x.getKidStatus())).count();
        long reserve = kidsList.stream().filter(x -> AppConstant.RESERVE.equals(x.getKidStatus())).count();
        response.setStudying(studying);
        response.setStudyWait(studyWait);
        response.setLeaveSchool(leaveSchool);
        response.setReserve(reserve);
        return response;
    }

    /**
     * lấy 5 trạng thái học sinh theo thời gian
     *
     * @param idSchool
     * @param year
     * @return
     */
    private List<KidsStatusTimelineModel> getKidsStatusTimeline(Long idSchool, int year) {
        List<KidsStatusTimelineModel> dataList = new ArrayList<>();
        Integer maxMonth = DateCommonUtils.getMonthMax(year);
        if (maxMonth == null) {
            return dataList;
        }
        List<Kids> kidsList = kidsRepository.findByIdSchool(idSchool);
        for (int i = 1; i <= maxMonth; i++) {
            KidsStatusTimelineModel model = new KidsStatusTimelineModel();
            LocalDate date = LocalDate.of(year, i, 1);
            //lấy những học sinh được tạo phải nhỏ hơn hoặc bằng tháng hiện tại
            List<Kids> list = kidsList.stream().filter(x -> x.getCreatedDate().toLocalDate().isBefore(date.plusMonths(1))).collect(Collectors.toList());
            //số học sinh ra trường trong tháng hiện tại
            List<Kids> outSchoolList = list.stream().filter(x -> x.getOutDate() != null && x.getGroupOutKids() != null && (x.getOutDate().isAfter(date) || x.getOutDate().isEqual(date)) && x.getOutDate().isBefore(date.plusMonths(1))).collect(Collectors.toList());
            List<Kids> inSchoolList = list.stream().filter(x -> outSchoolList.stream().noneMatch(x::equals)).collect(Collectors.toList());
            int studying = 0;
            int studyWait = 0;
            int reserve = 0;
            int leaveSchool = 0;
            int outSchool = outSchoolList.size();
            for (Kids x : inSchoolList) {
                //đã bị xóa hoặc ra trường trước tháng hiện tại
                if (x.getOutDate() != null && x.getOutDate().isBefore(date)) {
                    //không làm gì cả
                } else {
                    Optional<KidsStatusTimeline> kidsStatusTimelineOptional = kidsStatusTimelineRepository.findFirstByKidsIdAndStartDateIsBeforeOrderByIdDesc(x.getId(), date.plusMonths(1));
                    if (kidsStatusTimelineOptional.isPresent()) {
                        KidsStatusTimeline kidsStatusTimeline = kidsStatusTimelineOptional.get();
                        if (KidsStatusConstant.STUDYING.equals(kidsStatusTimeline.getStatus())) {
                            studying++;
                        } else if (KidsStatusConstant.STUDY_WAIT.equals(kidsStatusTimeline.getStatus())) {
                            studyWait++;
                        } else if (KidsStatusConstant.RESERVE.equals(kidsStatusTimeline.getStatus())) {
                            reserve++;
                        } else if (KidsStatusConstant.LEAVE_SCHOOL.equals(kidsStatusTimeline.getStatus())) {
                            leaveSchool++;
                        }
                    }
                }
            }
            model.setMonth(i);
            model.setStudying(studying);
            model.setStudyWait(studyWait);
            model.setReserve(reserve);
            model.setLeaveSchool(leaveSchool);
            model.setOutSchool(outSchool);
            dataList.add(model);
        }
        return dataList;
    }

    private List<KidsStatusTimelineModel> getKidsMonthStatusTimeline(Long idSchool, int year) {
        List<KidsStatusTimelineModel> dataList = new ArrayList<>();
        Integer maxMonth = DateCommonUtils.getMonthMax(year);
        if (maxMonth == null) {
            return dataList;
        }
//        List<Kids> kidsList = kidsRepository.findByIdSchool(idSchool);
        for (int i = 1; i <= maxMonth; i++) {
            KidsStatusTimelineModel model = new KidsStatusTimelineModel();
            LocalDate date = LocalDate.of(year, i, 1);
//            //lấy những học sinh được tạo phải nhỏ hơn hoặc bằng tháng hiện tại
//            List<Kids> list = kidsList.stream().filter(x -> x.getCreatedDate().toLocalDate().isBefore(date.plusMonths(1))).collect(Collectors.toList());
//            //số học sinh ra trường trong tháng hiện tại
//            List<Kids> outSchoolList = list.stream().filter(x -> x.getOutDate() != null && x.getGroupOutKids() != null && (x.getOutDate().isAfter(date) || x.getOutDate().isEqual(date)) && x.getOutDate().isBefore(date.plusMonths(1))).collect(Collectors.toList());
//            List<Kids> inSchoolList = list.stream().filter(x -> outSchoolList.stream().noneMatch(x::equals)).collect(Collectors.toList());
//            int studying = 0;
//            int studyWait = 0;
//            int reserve = 0;
//            int leaveSchool = 0;
//            int outSchool = outSchoolList.size();
//            for (Kids x : inSchoolList) {
//                //đã bị xóa hoặc ra trường trước tháng hiện tại
//                if (x.getOutDate() != null && x.getOutDate().isBefore(date)) {
//                    //không làm gì cả
//                } else {
//                    Optional<KidsStatusTimeline> kidsStatusTimelineOptional = kidsStatusTimelineRepository.findFirstByKidsIdAndStartDateIsBeforeOrderByIdDesc(x.getId(), date.plusMonths(1));
//                    if (kidsStatusTimelineOptional.isPresent()) {
//                        KidsStatusTimeline kidsStatusTimeline = kidsStatusTimelineOptional.get();
//                        if (KidsStatusConstant.STUDYING.equals(kidsStatusTimeline.getStatus())) {
//                            studying++;
//                        } else if (KidsStatusConstant.STUDY_WAIT.equals(kidsStatusTimeline.getStatus())) {
//                            studyWait++;
//                        } else if (KidsStatusConstant.RESERVE.equals(kidsStatusTimeline.getStatus())) {
//                            reserve++;
//                        } else if (KidsStatusConstant.LEAVE_SCHOOL.equals(kidsStatusTimeline.getStatus())) {
//                            leaveSchool++;
//                        }
//                    }
//                }
//            }
            List<Kids> list = kidsRepository.findKidInSchoolAndStatusWithDate(date, idSchool);
            model.setMonth(i);
            model.setStudying(list.size());
            model.setStudyWait(0);
            model.setReserve(0);
            model.setLeaveSchool(0);
            model.setOutSchool(0);
            dataList.add(model);
        }
        return dataList;
    }

    private List<EmployeeStatusTimelineModel> getEmployeeStatusTimeline(Long idSchool, int year) {
        List<EmployeeStatusTimelineModel> dataList = new ArrayList<>();
        Integer maxMonth = DateCommonUtils.getMonthMax(year);
        if (maxMonth == null) {
            return dataList;
        }
        List<InfoEmployeeSchool> kidsList = infoEmployeeSchoolRepository.findBySchoolId(idSchool);
        for (int i = 1; i <= maxMonth; i++) {
            EmployeeStatusTimelineModel model = new EmployeeStatusTimelineModel();
            LocalDate date = LocalDate.of(year, i, 1);
            //lấy những nhân sự được tạo phải nhỏ hơn hoặc bằng tháng hiện tại
            List<InfoEmployeeSchool> list = kidsList.stream().filter(x -> x.getCreatedDate().toLocalDate().isBefore(date.plusMonths(1))).collect(Collectors.toList());
            //số học sinh ra trường trong tháng hiện tại
            List<InfoEmployeeSchool> outSchoolList = list.stream().filter(x -> x.getOutDate() != null && x.getGroupOutEmployee() != null && (x.getOutDate().isAfter(date) || x.getOutDate().isEqual(date)) && x.getOutDate().isBefore(date.plusMonths(1))).collect(Collectors.toList());
            List<InfoEmployeeSchool> inSchoolList = list.stream().filter(x -> outSchoolList.stream().noneMatch(x::equals)).collect(Collectors.toList());
            int working = 0;
            int retain = 0;
            int leave = 0;
            int outSchool = outSchoolList.size();
            for (InfoEmployeeSchool x : inSchoolList) {
                //đã bị xóa hoặc ra trường trước tháng hiện tại
                if (x.getOutDate() != null && x.getOutDate().isBefore(date)) {
                    //không làm gì cả
                } else {
                    Optional<EmployeeStatusTimeline> employeeStatusTimelineOptional = employeeStatusTimelineRepository.findFirstByInfoEmployeeSchoolIdAndStartDateIsBeforeOrderByIdDesc(x.getId(), date.plusMonths(1));
                    if (employeeStatusTimelineOptional.isPresent()) {
                        EmployeeStatusTimeline employeeStatusTimeline = employeeStatusTimelineOptional.get();
                        if (EmployeeConstant.STATUS_WORKING.equals(employeeStatusTimeline.getStatus())) {
                            working++;
                        } else if (EmployeeConstant.STATUS_RETAIN.equals(employeeStatusTimeline.getStatus())) {
                            retain++;
                        } else if (EmployeeConstant.STATUS_LEAVE.equals(employeeStatusTimeline.getStatus())) {
                            leave++;
                        }
                    }
                }
            }
            model.setMonth(i);
            model.setWorking(working);
            model.setRetain(retain);
            model.setLeave(leave);
            model.setOutSchool(outSchool);
            dataList.add(model);
        }
        return dataList;
    }
}
