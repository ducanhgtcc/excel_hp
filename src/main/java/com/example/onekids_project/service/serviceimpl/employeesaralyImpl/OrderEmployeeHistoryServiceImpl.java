package com.example.onekids_project.service.serviceimpl.employeesaralyImpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.employeesalary.ExOrderHistoryEmployeeSalary;
import com.example.onekids_project.entity.finance.employeesalary.FnOrderEmployee;
import com.example.onekids_project.entity.finance.employeesalary.OrderEmployeeHistory;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.ExOrderHistoryEmployeeSalaryRepository;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.OrderEmployeeHistoryRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.employeeSalary.SearchAttendanceSalaryRequest;
import com.example.onekids_project.response.attendanceemployee.AttendanceEmployeesStatisticalResponse;
import com.example.onekids_project.response.employeesalary.OrderSalaryHistoryDetailResponse;
import com.example.onekids_project.response.employeesalary.OrderSalaryHistoryResponse;
import com.example.onekids_project.response.employeesalary.SalaryPackageHistoryPaymentResponse;
import com.example.onekids_project.response.excel.ExcelDataNew;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.employeesaraly.AttendanceEmployeeService;
import com.example.onekids_project.service.servicecustom.employeesaraly.OrderEmployeeHistoryService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.ExportExcelUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * date 2021-03-03 4:46 CH
 *
 * @author ADMIN
 */
@Service
public class OrderEmployeeHistoryServiceImpl implements OrderEmployeeHistoryService {

    @Autowired
    private OrderEmployeeHistoryRepository orderEmployeeHistoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private AttendanceEmployeeService attendanceEmployeeService;

    @Autowired
    private ExOrderHistoryEmployeeSalaryRepository exOrderHistoryEmployeeSalaryRepository;


    @Override
    public OrderEmployeeHistory saveOrderEmployeeHistory(String category, String name, LocalDate date, double moneyInput, String description, CashBookHistory cashBookHistory, FnOrderEmployee fnOrderEmployee) {
        OrderEmployeeHistory orderEmployeeHistory = new OrderEmployeeHistory();
        orderEmployeeHistory.setCategory(category);
        orderEmployeeHistory.setName(name);
        orderEmployeeHistory.setDate(date);
        orderEmployeeHistory.setMoneyInput(moneyInput);
        orderEmployeeHistory.setDescription(description);
        orderEmployeeHistory.setCashBookHistory(cashBookHistory);
        orderEmployeeHistory.setFnOrderEmployee(fnOrderEmployee);
        return orderEmployeeHistoryRepository.save(orderEmployeeHistory);
    }

    @Override
    public List<OrderSalaryHistoryResponse> findOrderKidsHistory(UserPrincipal principal, Long idOrder) {
        CommonValidate.checkDataPlus(principal);
        List<OrderEmployeeHistory> orderEmployeeHistoryList = orderEmployeeHistoryRepository.findByFnOrderEmployeeIdOrderByIdDesc(idOrder);
        List<OrderSalaryHistoryResponse> responseList = new ArrayList<>();
        orderEmployeeHistoryList.forEach(x -> {
            OrderSalaryHistoryResponse model = modelMapper.map(x, OrderSalaryHistoryResponse.class);
            List<ExOrderHistoryEmployeeSalary> exOrderHistoryEmployeeSalaryList = x.getExOrderHistoryEmployeeSalaryList();
            double moneyPaymentTotal = exOrderHistoryEmployeeSalaryList.stream().mapToDouble(ExOrderHistoryEmployeeSalary::getMoney).sum();
            model.setMoneyPayment(moneyPaymentTotal);
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<OrderSalaryHistoryDetailResponse> findOrderKidsHistoryDetail(UserPrincipal principal, Long idOrderHistory) {
        List<ExOrderHistoryEmployeeSalary> exOrderHistoryEmployeeSalaryList = exOrderHistoryEmployeeSalaryRepository.findByOrderEmployeeHistoryId(idOrderHistory);
        List<OrderSalaryHistoryDetailResponse> responseList = new ArrayList<>();
        exOrderHistoryEmployeeSalaryList.forEach(x -> {
            OrderSalaryHistoryDetailResponse model = new OrderSalaryHistoryDetailResponse();
            model.setId(x.getId());
            model.setMoney(x.getMoney());
            model.setName(x.getFnEmployeeSalary().getName());
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<SalaryPackageHistoryPaymentResponse> findSalaryPackagePaymentDetail(UserPrincipal principal, Long idSalaryPackage) {
        List<ExOrderHistoryEmployeeSalary> exOrderHistoryEmployeeSalaryList = exOrderHistoryEmployeeSalaryRepository.findByFnEmployeeSalaryIdOrderByIdDesc(idSalaryPackage);
        return listMapper.mapList(exOrderHistoryEmployeeSalaryList, SalaryPackageHistoryPaymentResponse.class);
    }

    @Override
    public ByteArrayInputStream exportAttendanceSalary(UserPrincipal principal, SearchAttendanceSalaryRequest request) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(131, 164, 196));
        XSSFColor greenOne = new XSSFColor(new java.awt.Color(149, 203, 74));
        XSSFColor yellowTwo = new XSSFColor(new java.awt.Color(217, 210, 144));
        XSSFColor blueTwo = new XSSFColor(new java.awt.Color(51, 153, 255));
        XSSFColor yellowThree = new XSSFColor(new java.awt.Color(224, 191, 28));
        XSSFColor yellowThreeDay = new XSSFColor(new java.awt.Color(180, 198, 231));

        int[] widths = {5, 30, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 14, 7, 7, 7, 14, 14};
        LocalDate dateStart = request.getDateStartEnd().get(0);
        LocalDate dateEnd = request.getDateStartEnd().get(1);
        Long idSchool = principal.getIdSchoolLogin();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateToStr = df.format(dateStart);
        String dateToStrSheet = df.format(dateEnd);
        String[] columns = {"STT", "Họ và tên", "Nghỉ có phép", "Nghỉ không phép", "Đi làm", "Có phép", "Không phép", "Đi làm", "Có phép", "Không phép", "Đi làm", "Có phép", "Không phép", "Đi làm", "Buổi", "Sáng", "Trưa", "Chiều", "Phút"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
            String schoolName = school.getSchoolName();
            // xuất học sinh theo trường
            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findBySchoolIdAndEmployeeStatusAndAppTypeAndDelActiveTrue(idSchool, request.getStatus(), AppTypeConstant.TEACHER);
            Sheet sheet = workbook.createSheet("cham_cong");
            for (int i = 0; i < 4; i++) {
                Row headerRow = sheet.createRow(i);
                for (int col = 0; col < columns.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    if (col == 0 && i == 0) {
                        cell.setCellValue("BẢNG TỔNG HỢP CHẤM CÔNG NHÂN SỰ");
                        CellStyle cellStyle = workbook.createCellStyle();
                        org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 18);
                        cellFont.setBold(true);
                        cellFont.setColor(IndexedColors.RED.getIndex());
                        cellStyle.setFont(cellFont);
                        cell.setCellStyle(cellStyle);
                    } else if (col == 0 && i == 1) {
                        cell.setCellValue("Trường: " + schoolName);
                        CellStyle twoStyle = workbook.createCellStyle();
                        org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
                        cellFont.setBold(true);
                        twoStyle.setFont(cellFont);
                        cell.setCellStyle(twoStyle);
                    } else if (col == 0 && i == 2) {
                        cell.setCellValue("Toàn trường: ");
                        CellStyle threeStyle = workbook.createCellStyle();
                        org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 11);
                        cellFont.setBold(true);
                        threeStyle.setFont(cellFont);
                        cell.setCellStyle(threeStyle);
                    } else if (col == 0 && i == 3) {
                        cell.setCellValue("Ngày: " + dateToStr + " - " + dateToStrSheet);
                        CellStyle threeStyle = workbook.createCellStyle();
                        org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 11);
                        cellFont.setBold(true);
                        threeStyle.setFont(cellFont);
                        cell.setCellStyle(threeStyle);
                    } else {
                        CellStyle cellStyle = workbook.createCellStyle();
                        cell.setCellStyle(cellStyle);
                    }
                }
            }
            sheet.createFreezePane(2, 7);
            // header row 5
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 10);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            CellStyle headerKidsCellStyle = workbook.createCellStyle();
            ((XSSFCellStyle) headerKidsCellStyle).setFillForegroundColor(yellowOne);
            headerKidsCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerKidsCellStyle.setFont(headerFont);
            headerKidsCellStyle.setWrapText(true);
            headerKidsCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerKidsCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerKidsCellStyle.setBorderBottom(BorderStyle.THIN);
            headerKidsCellStyle.setBorderTop(BorderStyle.THIN);
            headerKidsCellStyle.setBorderRight(BorderStyle.THIN);
            headerKidsCellStyle.setBorderLeft(BorderStyle.THIN);

            //style content
            CellStyle contentCellStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font contentFont = workbook.createFont();
            contentFont.setColor(IndexedColors.BLACK.getIndex());
            contentCellStyle.setFont(contentFont);
            contentCellStyle.setWrapText(true);
            contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentCellStyle.setAlignment(HorizontalAlignment.CENTER);
            contentCellStyle.setBorderBottom(BorderStyle.THIN);
            contentCellStyle.setBorderTop(BorderStyle.THIN);
            contentCellStyle.setBorderRight(BorderStyle.THIN);
            contentCellStyle.setBorderLeft(BorderStyle.THIN);

            //style content
            CellStyle contentCellStylenew = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font contentFontnew = workbook.createFont();
            contentFontnew.setColor(IndexedColors.BLACK.getIndex());
            contentCellStylenew.setFont(contentFont);
            contentCellStylenew.setWrapText(true);
            contentCellStylenew.setVerticalAlignment(VerticalAlignment.CENTER);
            contentCellStylenew.setAlignment(HorizontalAlignment.LEFT);
            contentCellStylenew.setBorderBottom(BorderStyle.THIN);
            contentCellStylenew.setBorderTop(BorderStyle.THIN);
            contentCellStylenew.setBorderRight(BorderStyle.THIN);
            contentCellStylenew.setBorderLeft(BorderStyle.THIN);

            //Style content  status
            CellStyle contentStatusStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font contentStatusFont = workbook.createFont();
            contentStatusFont.setColor(IndexedColors.BLACK.getIndex());
            contentStatusStyle.setFont(contentStatusFont);
            contentStatusStyle.setWrapText(true);
            ((XSSFCellStyle) contentStatusStyle).setFillForegroundColor(blueOne);
            contentStatusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            contentStatusStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentStatusStyle.setAlignment(HorizontalAlignment.CENTER);
            contentStatusStyle.setBorderBottom(BorderStyle.THIN);
            contentStatusStyle.setBorderTop(BorderStyle.THIN);
            contentStatusStyle.setBorderRight(BorderStyle.THIN);
            contentStatusStyle.setBorderLeft(BorderStyle.THIN);

            //Style content  lesson
            CellStyle contentLessonStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font contentLessonFont = workbook.createFont();
            contentLessonFont.setColor(IndexedColors.BLACK.getIndex());
            contentLessonStyle.setFont(contentLessonFont);
            contentLessonStyle.setWrapText(true);
            ((XSSFCellStyle) contentLessonStyle).setFillForegroundColor(greenOne);
            contentLessonStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            contentLessonStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentLessonStyle.setAlignment(HorizontalAlignment.CENTER);
            contentLessonStyle.setBorderBottom(BorderStyle.THIN);
            contentLessonStyle.setBorderTop(BorderStyle.THIN);
            contentLessonStyle.setBorderRight(BorderStyle.THIN);
            contentLessonStyle.setBorderLeft(BorderStyle.THIN);

            // số buổi
            CellStyle contentLateStyleDay = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font contentLateFontDay = workbook.createFont();
            contentLateFontDay.setColor(IndexedColors.BLACK.getIndex());
            contentLateStyleDay.setFont(contentLateFontDay);
            contentLateStyleDay.setWrapText(true);
            ((XSSFCellStyle) contentLateStyleDay).setFillForegroundColor(yellowThree);
            contentLateStyleDay.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            contentLateStyleDay.setVerticalAlignment(VerticalAlignment.CENTER);
            contentLateStyleDay.setAlignment(HorizontalAlignment.CENTER);
            contentLateStyleDay.setBorderBottom(BorderStyle.THIN);
            contentLateStyleDay.setBorderTop(BorderStyle.THIN);
            contentLateStyleDay.setBorderRight(BorderStyle.THIN);
            contentLateStyleDay.setBorderLeft(BorderStyle.THIN);

            //Style content  Meal
            CellStyle contentMealStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font contentMealFont = workbook.createFont();
            contentMealFont.setColor(IndexedColors.BLACK.getIndex());
            contentMealStyle.setFont(contentMealFont);
            contentMealStyle.setWrapText(true);
            ((XSSFCellStyle) contentMealStyle).setFillForegroundColor(yellowTwo);
            contentMealStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            contentMealStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentMealStyle.setAlignment(HorizontalAlignment.CENTER);
            contentMealStyle.setBorderBottom(BorderStyle.THIN);
            contentMealStyle.setBorderTop(BorderStyle.THIN);
            contentMealStyle.setBorderRight(BorderStyle.THIN);
            contentMealStyle.setBorderLeft(BorderStyle.THIN);

            //Style content  status
            CellStyle contentLateStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font contentLateFont = workbook.createFont();
            contentLateFont.setColor(IndexedColors.BLACK.getIndex());
            contentLateStyle.setFont(contentLateFont);
            contentLateStyle.setWrapText(true);
            ((XSSFCellStyle) contentLateStyle).setFillForegroundColor(yellowThreeDay);
            contentLateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            contentLateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentLateStyle.setAlignment(HorizontalAlignment.CENTER);
            contentLateStyle.setBorderBottom(BorderStyle.THIN);
            contentLateStyle.setBorderTop(BorderStyle.THIN);
            contentLateStyle.setBorderRight(BorderStyle.THIN);
            contentLateStyle.setBorderLeft(BorderStyle.THIN);

            // Row for Header4
            int rowHeader = 4;
            Row headerRow4 = sheet.createRow(rowHeader);
            // Header 4
            for (int col = 0; col < 20; col++) {
                Cell cell = headerRow4.createCell(col);
                CellStyle cellHeaderRow4 = workbook.createCellStyle();
                cellHeaderRow4.setBorderBottom(BorderStyle.THIN);
                cellHeaderRow4.setBorderTop(BorderStyle.THIN);
                cellHeaderRow4.setBorderRight(BorderStyle.THIN);
                cellHeaderRow4.setBorderLeft(BorderStyle.THIN);
                cell.setCellStyle(cellHeaderRow4);
            }

            Cell cellAttendance = headerRow4.createCell(0);
            cellAttendance.setCellValue("CHẤM CÔNG NHÂN VIÊN");
            CellStyle cellAttendanceStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerAttendance = workbook.createFont();
            headerAttendance.setBold(true);
            headerAttendance.setColor(IndexedColors.RED.getIndex());
            cellAttendanceStyle.setFont(headerAttendance);
            ((XSSFCellStyle) cellAttendanceStyle).setFillForegroundColor(greyOne);
            cellAttendanceStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellAttendanceStyle.setAlignment(HorizontalAlignment.CENTER);
            cellAttendanceStyle.setBorderBottom(BorderStyle.THIN);
            cellAttendanceStyle.setBorderTop(BorderStyle.THIN);
            cellAttendanceStyle.setBorderRight(BorderStyle.THIN);
            cellAttendanceStyle.setBorderLeft(BorderStyle.THIN);
            cellAttendance.setCellStyle(cellAttendanceStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 0, 1));

            Cell cellStatus = headerRow4.createCell(2);
            cellStatus.setCellValue("LÀM CẢ NGÀY");
            CellStyle cellStatusStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerStatus = workbook.createFont();
            headerStatus.setBold(true);
            cellStatusStyle.setFont(headerStatus);
            ((XSSFCellStyle) cellStatusStyle).setFillForegroundColor(blueOne);
            cellStatusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStatusStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStatusStyle.setBorderBottom(BorderStyle.THIN);
            cellStatusStyle.setBorderTop(BorderStyle.THIN);
            cellStatusStyle.setBorderRight(BorderStyle.THIN);
            cellStatusStyle.setBorderLeft(BorderStyle.THIN);
            cellStatus.setCellStyle(cellStatusStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 2, 4));

            Cell cellLesson = headerRow4.createCell(5);
            cellLesson.setCellValue("BUỔI LÀM");
            CellStyle cellLessonStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerLesson = workbook.createFont();
            headerLesson.setBold(true);
            cellLessonStyle.setFont(headerLesson);
            ((XSSFCellStyle) cellLessonStyle).setFillForegroundColor(greenOne);
            cellLessonStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellLessonStyle.setAlignment(HorizontalAlignment.CENTER);
            cellLessonStyle.setBorderBottom(BorderStyle.THIN);
            cellLessonStyle.setBorderTop(BorderStyle.THIN);
            cellLessonStyle.setBorderRight(BorderStyle.THIN);
            cellLessonStyle.setBorderLeft(BorderStyle.THIN);
            cellLesson.setCellStyle(cellLessonStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 5, 13));

            // số buổi ăn
            Cell cellLateEat = headerRow4.createCell(14);
            cellLateEat.setCellValue("ĂN CẢ NGÀY");
            CellStyle cellLateStyleDay = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerLateDay = workbook.createFont();
            headerLateDay.setBold(true);
            cellLateStyleDay.setFont(headerLateDay);
            ((XSSFCellStyle) cellLateStyleDay).setFillForegroundColor(yellowThreeDay);
            cellLateStyleDay.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellLateStyleDay.setAlignment(HorizontalAlignment.CENTER);
            cellLateStyleDay.setBorderBottom(BorderStyle.THIN);
            cellLateStyleDay.setBorderTop(BorderStyle.THIN);
            cellLateStyleDay.setBorderRight(BorderStyle.THIN);
            cellLateStyleDay.setBorderLeft(BorderStyle.THIN);
            cellLateEat.setCellStyle(cellLateStyleDay);

            Cell cellMeal = headerRow4.createCell(15);
            cellMeal.setCellValue("BỮA ĂN");
            CellStyle cellMealStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerMeal = workbook.createFont();
            headerMeal.setBold(true);
            cellMealStyle.setFont(headerMeal);
            ((XSSFCellStyle) cellMealStyle).setFillForegroundColor(yellowTwo);
            cellMealStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellMealStyle.setAlignment(HorizontalAlignment.CENTER);
            cellMealStyle.setBorderBottom(BorderStyle.THIN);
            cellMealStyle.setBorderTop(BorderStyle.THIN);
            cellMealStyle.setBorderRight(BorderStyle.THIN);
            cellMealStyle.setBorderLeft(BorderStyle.THIN);
            cellMeal.setCellStyle(cellMealStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 15, 17));

            // đón muộn
            Cell cellLateEatate = headerRow4.createCell(18);
            cellLateEatate.setCellValue("ĐI MUỘN");
            CellStyle cellLateStyleDaylate = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerLateDaylate = workbook.createFont();
            headerLateDaylate.setBold(true);
            cellLateStyleDaylate.setFont(headerLateDaylate);
            ((XSSFCellStyle) cellLateStyleDaylate).setFillForegroundColor(yellowThreeDay);
            cellLateStyleDaylate.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellLateStyleDaylate.setAlignment(HorizontalAlignment.CENTER);
            cellLateStyleDaylate.setBorderBottom(BorderStyle.THIN);
            cellLateStyleDaylate.setBorderTop(BorderStyle.THIN);
            cellLateStyleDaylate.setBorderRight(BorderStyle.THIN);
            cellLateStyleDaylate.setBorderLeft(BorderStyle.THIN);
            cellLateEatate.setCellStyle(cellLateStyleDaylate);

            // đón muộn
            Cell cellLateEatateend = headerRow4.createCell(19);
            cellLateEatateend.setCellValue("VỀ SỚM");
            CellStyle cellLateStyleDaylateend = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerLateDaylateend = workbook.createFont();
            headerLateDaylateend.setBold(true);
            cellLateStyleDaylateend.setFont(headerLateDaylateend);
            ((XSSFCellStyle) cellLateStyleDaylateend).setFillForegroundColor(yellowThreeDay);
            cellLateStyleDaylateend.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellLateStyleDaylateend.setAlignment(HorizontalAlignment.CENTER);
            cellLateStyleDaylateend.setBorderBottom(BorderStyle.THIN);
            cellLateStyleDaylateend.setBorderTop(BorderStyle.THIN);
            cellLateStyleDaylateend.setBorderRight(BorderStyle.THIN);
            cellLateStyleDaylateend.setBorderLeft(BorderStyle.THIN);
            cellLateEatateend.setCellStyle(cellLateStyleDaylateend);

            int rowParent = 5;
            Row headerRow5 = sheet.createRow(rowParent);
            // Header 4
            for (int col = 0; col < 20; col++) {
                Cell cell = headerRow5.createCell(col);
                CellStyle cellHeaderRow5 = workbook.createCellStyle();
                cellHeaderRow5.setBorderBottom(BorderStyle.THIN);
                cellHeaderRow5.setBorderTop(BorderStyle.THIN);
                cellHeaderRow5.setBorderRight(BorderStyle.THIN);
                cellHeaderRow5.setBorderLeft(BorderStyle.THIN);
                cell.setCellStyle(cellHeaderRow5);
            }

//             Row for Header
            int rowChild = 6;
            Cell cellLesson0 = headerRow5.createCell(0);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 0, 0));
            cellLesson0.setCellValue("STT");
            cellLesson0.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle0 = workbook.createCellStyle();
            cellLateStyle0.setAlignment(HorizontalAlignment.CENTER);


            Cell cellLesson1 = headerRow5.createCell(1);
            cellLesson1.setCellValue("Họ và tên");
            cellLesson1.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle1 = workbook.createCellStyle();
            cellLateStyle1.setAlignment(HorizontalAlignment.LEFT);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 1, 1));

            Cell cellLesson2 = headerRow5.createCell(2);
            cellLesson2.setCellValue("Nghỉ có phép");
            cellLesson2.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle2 = workbook.createCellStyle();
            cellLateStyle2.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 2, 2));

            Cell cellLesson3 = headerRow5.createCell(3);
            cellLesson3.setCellValue("Nghỉ không phép");
            cellLesson3.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle3 = workbook.createCellStyle();
            cellLateStyle3.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 3, 3));

            Cell cellLesson4 = headerRow5.createCell(4);
            cellLesson4.setCellValue("Đi làm");
            cellLesson4.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle4 = workbook.createCellStyle();
            cellLateStyle4.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 4, 4));

            Cell cellLesson6 = headerRow5.createCell(5);
            cellLesson6.setCellValue("Chỉ buổi sáng");
            cellLesson6.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle5 = workbook.createCellStyle();
            cellLateStyle5.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowParent, 5, 7));

            Cell cellLesson7 = headerRow5.createCell(8);
            cellLesson7.setCellValue("Chỉ buổi chiều");
            cellLesson7.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle6 = workbook.createCellStyle();
            cellLateStyle6.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowParent, 8, 10));

            Cell cellLesson8 = headerRow5.createCell(11);
            cellLesson8.setCellValue("Chỉ buổi tối");
            cellLesson8.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle7 = workbook.createCellStyle();
            cellLateStyle7.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowParent, 11, 13));

            Cell cellLesson171 = headerRow5.createCell(14);
            cellLesson171.setCellValue("Buổi");
            cellLesson171.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle161 = workbook.createCellStyle();
            cellLateStyle161.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 14, 14));


            Cell cellLesson9 = headerRow5.createCell(15);
            cellLesson9.setCellValue("Chỉ ăn sáng");
            cellLesson9.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle8 = workbook.createCellStyle();
            cellLateStyle8.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 15, 15));

            Cell cellLesson11 = headerRow5.createCell(16);
            cellLesson11.setCellValue("Chỉ ăn trưa");
            cellLesson11.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle10 = workbook.createCellStyle();
            cellLateStyle10.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 16, 16));

            Cell cellLesson14 = headerRow5.createCell(17);
            cellLesson14.setCellValue("Chỉ ăn tối");
            cellLesson14.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle13 = workbook.createCellStyle();
            cellLateStyle13.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 17, 17));

            Cell cellLesson17 = headerRow5.createCell(18);
            cellLesson17.setCellValue("Phút");
            cellLesson17.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle16 = workbook.createCellStyle();
            cellLateStyle16.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 18, 18));

            Cell cellLesson18 = headerRow5.createCell(19);
            cellLesson18.setCellValue("Phút");
            cellLesson18.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle18 = workbook.createCellStyle();
            cellLateStyle18.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 19, 19));

            Row headerRow6 = sheet.createRow(rowChild);

            for (int col = 0; col < 20; col++) {
                if (col < 5) {
                    sheet.setColumnWidth(col, widths[col] * 256);
                    Cell cell = headerRow6.createCell(col);
                    cell.setCellStyle(headerKidsCellStyle);
                }
                if (col > 4 && col < 14) {
                    sheet.setColumnWidth(col, widths[col] * 256);
                    Cell cell = headerRow6.createCell(col);
                    cell.setCellStyle(headerKidsCellStyle);
                    cell.setCellValue(columns[col]);
                }

                if (col > 13) {
                    sheet.setColumnWidth(col, widths[col] * 256);
                    Cell cell = headerRow6.createCell(col);
                    cell.setCellStyle(headerKidsCellStyle);
                }
            }
            int rowIdx = 7;
            int i = 0;

            int getAllDayYesTotal = 0;
            int getAllDay = 0;
            int getAllDayNo = 0;

            int getMorningYes = 0;
            int getMorningNo = 0;
            int getMorning = 0;

            int getAfternoonYes = 0;
            int getAfternoonNo = 0;
            int getAfternoon = 0;

            int getEveningYes = 0;
            int getEveningNo = 0;
            int getEvening = 0;

            int getEatAllDay = 0;

            int getEatMorning = 0;
            int getEatAfternoon = 0;
            int minusSoon = 0;
            int minusLate = 0;

            for (InfoEmployeeSchool x : infoEmployeeSchoolList) {

                AttendanceEmployeesStatisticalResponse fillData = attendanceEmployeeService.attendanceEmployeesStatistical(x, dateStart, dateEnd);
                i++;
                getAllDayYesTotal = fillData.getAllDayYes() + getAllDayYesTotal;
                getAllDay = fillData.getAllDay() + getAllDay;
                getAllDayNo = fillData.getAllDayNo() + getAllDayNo;

                getMorningYes = fillData.getMorningYes() + getMorningYes;
                getMorningNo = fillData.getMorningNo() + getMorningNo;
                getMorning = fillData.getMorning() + getMorning;

                getAfternoonYes = fillData.getAfternoonYes() + getAfternoonYes;
                getAfternoonNo = fillData.getAfternoonNo() + getAfternoonNo;
                getAfternoon = fillData.getAfternoon() + getAfternoon;

                getEveningYes = fillData.getEveningYes() + getEveningYes;
                getEveningNo = fillData.getEveningNo() + getEveningNo;
                getEvening = fillData.getEvening() + getEvening;

                getEatAllDay = fillData.getEatAllDay() + getEatAllDay;
                getEatMorning = fillData.getEatMorning() + getEatMorning;
                getEatAfternoon = fillData.getEatAfternoon() + getEatAfternoon;

                minusLate = fillData.getMinutesArriveLate();
                minusSoon = fillData.getMinutesLeaveSoon();

                Row row = sheet.createRow(rowIdx++);

                Cell cellId = row.createCell(0);
                cellId.setCellValue(i);
                cellId.setCellStyle(contentCellStyle);

                Cell cellKidName = row.createCell(1);
                cellKidName.setCellValue(x.getFullName());
                cellKidName.setCellStyle(contentCellStylenew);

                Cell cellAbsentLetterYes = row.createCell(2);
                cellAbsentLetterYes.setCellValue(fillData.getAllDayYes());
                cellAbsentLetterYes.setCellStyle(contentStatusStyle);

                Cell cellAbsentLetterNo = row.createCell(3);
                cellAbsentLetterNo.setCellValue(fillData.getAllDayNo());
                cellAbsentLetterNo.setCellStyle(contentStatusStyle);

                Cell cellAbsentStatus = row.createCell(4);
                cellAbsentStatus.setCellValue(fillData.getAllDay());
                cellAbsentStatus.setCellStyle(contentStatusStyle);

                Cell cellMorningYes = row.createCell(5);
                cellMorningYes.setCellValue(fillData.getMorningYes());
                cellMorningYes.setCellStyle(contentLessonStyle);

                Cell cellMorningNo = row.createCell(6);
                cellMorningNo.setCellValue(fillData.getMorningNo());
                cellMorningNo.setCellStyle(contentLessonStyle);

                Cell cellMorning = row.createCell(7);
                cellMorning.setCellValue(fillData.getMorning());
                cellMorning.setCellStyle(contentLessonStyle);

                Cell cellAfternoonYes = row.createCell(8);
                cellAfternoonYes.setCellValue(fillData.getAfternoonYes());
                cellAfternoonYes.setCellStyle(contentLessonStyle);

                Cell cellAfternoonNo = row.createCell(9);
                cellAfternoonNo.setCellValue(fillData.getAfternoonNo());
                cellAfternoonNo.setCellStyle(contentLessonStyle);

                Cell cellAfternoon = row.createCell(10);
                cellAfternoon.setCellValue(fillData.getAfternoon());
                cellAfternoon.setCellStyle(contentLessonStyle);

                Cell cellEveningYes = row.createCell(11);
                cellEveningYes.setCellValue(fillData.getEveningYes());
                cellEveningYes.setCellStyle(contentLessonStyle);

                Cell cellEveningNo = row.createCell(12);
                cellEveningNo.setCellValue(fillData.getEveningNo());
                cellEveningNo.setCellStyle(contentLessonStyle);

                Cell cellEvening = row.createCell(13);
                cellEvening.setCellValue(fillData.getEvening());
                cellEvening.setCellStyle(contentLessonStyle);

                Cell cellminutePickupLateDay = row.createCell(14);
                cellminutePickupLateDay.setCellValue(fillData.getEatAllDay());
                cellminutePickupLateDay.setCellStyle(contentLateStyle);

                Cell cellEatBreakfast = row.createCell(15);
                cellEatBreakfast.setCellValue(fillData.getEatMorning());
                cellEatBreakfast.setCellStyle(contentMealStyle);

                Cell cellEatSecondBreakfast = row.createCell(16);
                cellEatSecondBreakfast.setCellValue(fillData.getEatAfternoon());
                cellEatSecondBreakfast.setCellStyle(contentMealStyle);

                Cell celleatLunch = row.createCell(17);
                celleatLunch.setCellValue(fillData.getEatEvening());
                celleatLunch.setCellStyle(contentMealStyle);

                Cell celleatAfternoon = row.createCell(18);
                celleatAfternoon.setCellValue(fillData.getMinutesArriveLate());
                celleatAfternoon.setCellStyle(contentMealStyle);

                Cell celleatSecondAfternoon = row.createCell(19);
                celleatSecondAfternoon.setCellValue(fillData.getMinutesLeaveSoon());
                celleatSecondAfternoon.setCellStyle(contentMealStyle);

            }

            int rowEnd = 7 + infoEmployeeSchoolList.size();
            Row headerRowEnd = sheet.createRow(rowEnd);

            Cell cellAttendanceTotal = headerRowEnd.createCell(0);
            cellAttendanceTotal.setCellValue("Tổng cộng");
            CellStyle cellAttendanceStyleTotal = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerAttendanceTotal = workbook.createFont();
            headerAttendanceTotal.setBold(true);
            headerAttendanceTotal.setColor(IndexedColors.BLACK.getIndex());
            cellAttendanceStyleTotal.setFont(headerAttendanceTotal);
            ((XSSFCellStyle) cellAttendanceStyleTotal).setFillForegroundColor(blueTwo);
            cellAttendanceStyleTotal.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellAttendanceStyleTotal.setAlignment(HorizontalAlignment.CENTER);
            cellAttendanceStyleTotal.setBorderBottom(BorderStyle.THIN);
            cellAttendanceStyleTotal.setBorderTop(BorderStyle.THIN);
            cellAttendanceStyleTotal.setBorderRight(BorderStyle.THIN);
            cellAttendanceStyleTotal.setBorderLeft(BorderStyle.THIN);
            cellAttendanceTotal.setCellStyle(cellAttendanceStyleTotal);
            sheet.addMergedRegion(new CellRangeAddress(rowEnd, rowEnd, 0, 1));

            Cell cell2 = headerRowEnd.createCell(2);
            cell2.setCellStyle(cellAttendanceStyleTotal);
            cell2.setCellValue(getAllDayYesTotal);

            Cell cell3 = headerRowEnd.createCell(3);
            cell3.setCellStyle(cellAttendanceStyleTotal);
            cell3.setCellValue(getAllDayNo);

            Cell cell4 = headerRowEnd.createCell(4);
            cell4.setCellStyle(cellAttendanceStyleTotal);
            cell4.setCellValue(getAllDay);

            Cell cell5 = headerRowEnd.createCell(5);
            cell5.setCellStyle(cellAttendanceStyleTotal);
            cell5.setCellValue(getMorningYes);

            Cell cell6 = headerRowEnd.createCell(6);
            cell6.setCellStyle(cellAttendanceStyleTotal);
            cell6.setCellValue(getMorningNo);

            Cell cell7 = headerRowEnd.createCell(7);
            cell7.setCellStyle(cellAttendanceStyleTotal);
            cell7.setCellValue(getMorning);

            Cell cell8 = headerRowEnd.createCell(8);
            cell8.setCellStyle(cellAttendanceStyleTotal);
            cell8.setCellValue(getAfternoonYes);

            Cell cell9 = headerRowEnd.createCell(9);
            cell9.setCellStyle(cellAttendanceStyleTotal);
            cell9.setCellValue(getAfternoonNo);

            Cell cell10 = headerRowEnd.createCell(10);
            cell10.setCellStyle(cellAttendanceStyleTotal);
            cell10.setCellValue(getAfternoon);

            Cell cell11 = headerRowEnd.createCell(11);
            cell11.setCellStyle(cellAttendanceStyleTotal);
            cell11.setCellValue(getEveningYes);

            Cell cell12 = headerRowEnd.createCell(12);
            cell12.setCellStyle(cellAttendanceStyleTotal);
            cell12.setCellValue(getEveningNo);

            Cell cell13 = headerRowEnd.createCell(13);
            cell13.setCellStyle(cellAttendanceStyleTotal);
            cell13.setCellValue(getEvening);

            Cell cell14 = headerRowEnd.createCell(14);
            cell14.setCellStyle(cellAttendanceStyleTotal);
            cell14.setCellValue(getEatAllDay);

            Cell cell15 = headerRowEnd.createCell(15);
            cell15.setCellStyle(cellAttendanceStyleTotal);
            cell15.setCellValue(getEatMorning);

            Cell cell16 = headerRowEnd.createCell(16);
            cell16.setCellStyle(cellAttendanceStyleTotal);
            cell16.setCellValue(getEatAfternoon);

            Cell cell17 = headerRowEnd.createCell(17);
            cell17.setCellStyle(cellAttendanceStyleTotal);
            cell17.setCellValue(getEatAfternoon);

            Cell cell18 = headerRowEnd.createCell(18);
            cell18.setCellStyle(cellAttendanceStyleTotal);
            cell18.setCellValue(minusLate);

            Cell cell19 = headerRowEnd.createCell(19);
            cell19.setCellStyle(cellAttendanceStyleTotal);
            cell19.setCellValue(minusSoon);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }

    }

    @Override
    public List<ExcelNewResponse> exportAttendanceSalaryNew(UserPrincipal principal, SearchAttendanceSalaryRequest request) {
        List<ExcelNewResponse> responseList = new ArrayList<>();
        ExcelNewResponse response = new ExcelNewResponse();
        List<ExcelDataNew> bodyList = new ArrayList<>();

        LocalDate dateStart = request.getDateStartEnd().get(0);
        LocalDate dateEnd = request.getDateStartEnd().get(1);
        Long idSchool = principal.getIdSchoolLogin();
        String dateToStr = ConvertData.convertLocalDateToString(dateStart);
        String dateToStrSheet = ConvertData.convertLocalDateToString(dateEnd);
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
        String schoolName = school.getSchoolName();
        List<String> headerStringList = Arrays.asList("BẢNG TỔNG HỢP CHẤM CÔNG NHÂN SỰ", AppConstant.EXCEL_SCHOOL.concat(schoolName), "Toàn trường", AppConstant.EXCEL_DATE.concat(dateToStr).concat(AppConstant.SPACE_EXPORT_ID).concat(dateToStrSheet));
        List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
        ExcelDataNew headerMulti = this.setHeaderMulti();
        ExcelDataNew headerMulti1 = this.setHeaderMulti1();
        headerList.add(headerMulti);
        headerList.add(headerMulti1);
        response.setSheetName("chấm công");
        response.setHeaderList(headerList);
        // xuất học sinh theo trường
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findBySchoolIdAndEmployeeStatusAndAppTypeAndDelActiveTrue(idSchool, request.getStatus(), AppTypeConstant.TEACHER);
        int getAllDayYesTotal = 0;
        int getAllDay = 0;
        int getAllDayNo = 0;

        int getMorningYes = 0;
        int getMorningNo = 0;
        int getMorning = 0;

        int getAfternoonYes = 0;
        int getAfternoonNo = 0;
        int getAfternoon = 0;

        int getEveningYes = 0;
        int getEveningNo = 0;
        int getEvening = 0;

        int getEatAllDay = 0;

        int getEatMorning = 0;
        int getEatAfternoon = 0;
        int getEatEvening = 0;
        int minusSoon = 0;
        int minusLate = 0;
        long i = 1;
        for (InfoEmployeeSchool infoEmployeeSchool : infoEmployeeSchoolList) {
            AttendanceEmployeesStatisticalResponse x = attendanceEmployeeService.attendanceEmployeesStatistical(infoEmployeeSchool, dateStart, dateEnd);
            getAllDayYesTotal += x.getAllDayYes();
            getAllDay += x.getAllDay();
            getAllDayNo += x.getAllDayNo();

            getMorningYes += x.getMorningYes();
            getMorningNo += x.getMorningNo();
            getMorning += x.getMorning();

            getAfternoonYes += x.getAfternoonYes();
            getAfternoonNo += x.getAfternoonNo();
            getAfternoon += x.getAfternoon();

            getEveningYes += x.getEveningYes();
            getEveningNo += x.getEveningNo();
            getEvening += x.getEvening();

            getEatAllDay += x.getEatAllDay();
            getEatMorning += x.getEatMorning();
            getEatAfternoon += x.getEatAfternoon();
            getEatEvening += x.getEatEvening();

            minusLate += x.getMinutesArriveLate();
            minusSoon += x.getMinutesLeaveSoon();
            List<String> bodyStringList = Arrays.asList(String.valueOf(i++), infoEmployeeSchool.getFullName(), String.valueOf(x.getAllDayYes()), String.valueOf(x.getAllDayNo()), String.valueOf(x.getAllDay()), String.valueOf(x.getMorningYes()), String.valueOf(x.getMorningNo()), String.valueOf(x.getMorning()),
                    String.valueOf(x.getAfternoonYes()), String.valueOf(x.getAfternoonNo()), String.valueOf(x.getAfternoon()), String.valueOf(x.getEveningYes()), String.valueOf(x.getEveningNo()), String.valueOf(x.getEvening()), String.valueOf(x.getEatAllDay()), String.valueOf(x.getEatMorning()), String.valueOf(x.getEatAfternoon()),
                    String.valueOf(x.getEatEvening()), String.valueOf(x.getMinutesArriveLate()), String.valueOf(x.getMinutesLeaveSoon()));
            ExcelDataNew modelData = ExportExcelUtils.setBodyExcelNew(bodyStringList);
            bodyList.add(modelData);
        }
        List<String> bodyStringList = Arrays.asList("Tổng cộng", "", String.valueOf(getAllDayYesTotal), String.valueOf(getAllDayNo), String.valueOf(getAllDay), String.valueOf(getMorningYes), String.valueOf(getMorningNo), String.valueOf(getMorning),
                String.valueOf(getAfternoonYes), String.valueOf(getAfternoonNo), String.valueOf(getAfternoon), String.valueOf(getEveningYes), String.valueOf(getEveningNo), String.valueOf(getEvening), String.valueOf(getEatAllDay), String.valueOf(getEatMorning), String.valueOf(getEatAfternoon), String.valueOf(getEatEvening),
                String.valueOf(minusLate), String.valueOf(minusSoon));
        ExcelDataNew modelData = ExportExcelUtils.setBodyExcelNew(bodyStringList);
        bodyList.add(modelData);
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    private ExcelDataNew setHeaderMulti() {
        ExcelDataNew headerMulti = new ExcelDataNew();
        headerMulti.setPro1("CHẤM CÔNG NHÂN VIÊN");
        headerMulti.setPro2("");
        headerMulti.setPro3("LÀM CẢ NGÀY");
        headerMulti.setPro4("");
        headerMulti.setPro5("");
        headerMulti.setPro6("BUỔI LÀM");
        headerMulti.setPro7("");
        headerMulti.setPro8("");
        headerMulti.setPro9("");
        headerMulti.setPro10("");
        headerMulti.setPro11("");
        headerMulti.setPro12("");
        headerMulti.setPro13("");
        headerMulti.setPro14("");
        headerMulti.setPro15("ĂN CẢ NGÀY");
        headerMulti.setPro16("BỮA ĂN");
        headerMulti.setPro17("");
        headerMulti.setPro18("");
        headerMulti.setPro19("ĐI MUỘN");
        headerMulti.setPro20("VỀ SỚM");
        return headerMulti;
    }

    private ExcelDataNew setHeaderMulti1() {
        ExcelDataNew headerMulti = new ExcelDataNew();
        headerMulti.setPro1("STT");
        headerMulti.setPro2("Họ và tên");
        headerMulti.setPro3("Nghỉ có phép");
        headerMulti.setPro4("Nghỉ không phép");
        headerMulti.setPro5("Đi làm");
        headerMulti.setPro6("Chỉ buổi sáng");
        headerMulti.setPro7("");
        headerMulti.setPro8("");
        headerMulti.setPro9("Chỉ buổi chiều");
        headerMulti.setPro10("");
        headerMulti.setPro11("");
        headerMulti.setPro12("Chỉ buổi tối");
        headerMulti.setPro13("");
        headerMulti.setPro14("");
        headerMulti.setPro15("Buổi");
        headerMulti.setPro16("Chỉ ăn sáng");
        headerMulti.setPro17("Chỉ ăn trưa");
        headerMulti.setPro18("Chỉ ăn tối");
        headerMulti.setPro19("Phút");
        headerMulti.setPro20("Phút");
        return headerMulti;
    }
}
