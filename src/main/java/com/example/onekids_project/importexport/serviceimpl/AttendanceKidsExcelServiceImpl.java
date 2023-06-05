package com.example.onekids_project.importexport.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.AttendanceArriveKids;
import com.example.onekids_project.entity.kids.AttendanceKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.importexport.model.AttendanceKidsModel;
import com.example.onekids_project.importexport.service.AttendanceKidsExcelService;
import com.example.onekids_project.repository.AttendanceKidsRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.attendancekids.AttendanceKidsSearchCustomRequest;
import com.example.onekids_project.request.attendancekids.AttendanceKidsSearchDetailRequest;
import com.example.onekids_project.response.attendancekids.AttendanceKidsStatisticalResponse;
import com.example.onekids_project.response.excel.ExcelDataNew;
import com.example.onekids_project.response.excel.ExcelDynamicResponse;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.MaClassService;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.service.servicecustom.attendancekids.AttendanceKidsStatisticalService;
import com.example.onekids_project.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AttendanceKidsExcelServiceImpl implements AttendanceKidsExcelService {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private MaClassService maClassService;

    @Autowired
    private AttendanceKidsStatisticalService attendanceKidsStatisticalService;

    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;


    @Override
    public ByteArrayInputStream attendanceToExcel(List<AttendanceKidsModel> attendanceKidsModelList, Long idSchool, Long idClass, LocalDate currentDate) throws IOException {

        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(131, 164, 196));
        XSSFColor greenOne = new XSSFColor(new java.awt.Color(149, 203, 74));
        XSSFColor yellowTwo = new XSSFColor(new java.awt.Color(217, 210, 144));
        XSSFColor blueTwo = new XSSFColor(new java.awt.Color(51, 153, 255));
        XSSFColor yellowThree = new XSSFColor(new java.awt.Color(224, 191, 28));

        int[] widths = {5, 30, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 10, 10, 20};
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");

//        LocalDate date = currentDate.plusMonths(1);

//        String endDate = df.format(date);

        String dateToStr = df.format(currentDate);
        String dateToStrSheet = df1.format(currentDate);
        String[] columns = {"STT", "Họ và tên", "Nghỉ có phép", "Nghỉ không phép", "Đi học", "Có phép", "Không phép", "Đi học", "Có phép", "Không phép", "Đi học", "Có phép", "Không phép", "Đi học", "Sáng", "Phụ sáng", "Trưa", "Chiều", "Phụ Chiều", "Tối", "Đến", "Về", "Phút"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();
            SchoolResponse schoolResponse = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
            String schoolName = "";
            if (schoolResponse != null) {
                schoolName = schoolResponse.getSchoolName();
            }

            MaClassDTO classDTO = maClassService.findByIdMaClass(idSchool, idClass).stream().findFirst().orElse(null);
            String className = "";
            if (classDTO != null) {
                className = classDTO.getClassName();
            }
            Sheet sheet = workbook.createSheet(dateToStrSheet);

            sheet.setDisplayGridlines(false);
            for (int i = 0; i < 4; i++) {
                Row headerRow = sheet.createRow(i);
                for (int col = 0; col < columns.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    if (col == 0 && i == 0) {
                        cell.setCellValue("BẢNG KÊ ĐIỂM DANH NGÀY");
                        CellStyle cellStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 18);
                        cellFont.setBold(true);
                        cellFont.setColor(IndexedColors.RED.getIndex());

                        cellStyle.setFont(cellFont);
                        cell.setCellStyle(cellStyle);
                    } else if (col == 0 && i == 1) {
                        cell.setCellValue("Trường: " + schoolName);
                        CellStyle twoStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setBold(true);
                        twoStyle.setFont(cellFont);
                        cell.setCellStyle(twoStyle);
                    } else if (col == 0 && i == 2) {
                        cell.setCellValue("Lớp: " + className);
                        CellStyle threeStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 11);
                        cellFont.setBold(true);
                        threeStyle.setFont(cellFont);
                        cell.setCellStyle(threeStyle);
                    } else if (col == 0 && i == 3) {
                        cell.setCellValue("Ngày: " + dateToStr);
                        CellStyle threeStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
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
            Font headerFont = workbook.createFont();
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
            Font contentFont = workbook.createFont();
            contentFont.setColor(IndexedColors.BLACK.getIndex());
            contentCellStyle.setFont(contentFont);
            contentCellStyle.setWrapText(true);
            contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentCellStyle.setAlignment(HorizontalAlignment.CENTER);
            contentCellStyle.setBorderBottom(BorderStyle.THIN);
            contentCellStyle.setBorderTop(BorderStyle.THIN);
            contentCellStyle.setBorderRight(BorderStyle.THIN);
            contentCellStyle.setBorderLeft(BorderStyle.THIN);
            //Style content  status
            CellStyle contentStatusStyle = workbook.createCellStyle();
            Font contentStatusFont = workbook.createFont();
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
            Font contentLessonFont = workbook.createFont();
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
            //Style content  Meal
            CellStyle contentMealStyle = workbook.createCellStyle();
            Font contentMealFont = workbook.createFont();
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
            //Style content  shuttle
            CellStyle contentShuttleStyle = workbook.createCellStyle();
            Font contentShuttleFont = workbook.createFont();
            contentShuttleFont.setColor(IndexedColors.BLACK.getIndex());
            contentShuttleStyle.setFont(contentShuttleFont);
            contentShuttleStyle.setWrapText(true);
            ((XSSFCellStyle) contentShuttleStyle).setFillForegroundColor(blueTwo);
            contentShuttleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            contentShuttleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentShuttleStyle.setAlignment(HorizontalAlignment.CENTER);
            contentShuttleStyle.setBorderBottom(BorderStyle.THIN);
            contentShuttleStyle.setBorderTop(BorderStyle.THIN);
            contentShuttleStyle.setBorderRight(BorderStyle.THIN);
            contentShuttleStyle.setBorderLeft(BorderStyle.THIN);
            //Style content  status
            CellStyle contentLateStyle = workbook.createCellStyle();
            Font contentLateFont = workbook.createFont();
            contentLateFont.setColor(IndexedColors.BLACK.getIndex());
            contentLateStyle.setFont(contentLateFont);
            contentLateStyle.setWrapText(true);
            ((XSSFCellStyle) contentLateStyle).setFillForegroundColor(yellowThree);
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
            for (int col = 0; col < 23; col++) {
                Cell cell = headerRow4.createCell(col);
                CellStyle cellHeaderRow4 = workbook.createCellStyle();
                cellHeaderRow4.setBorderBottom(BorderStyle.THIN);
                cellHeaderRow4.setBorderTop(BorderStyle.THIN);
                cellHeaderRow4.setBorderRight(BorderStyle.THIN);
                cellHeaderRow4.setBorderLeft(BorderStyle.THIN);
                cell.setCellStyle(cellHeaderRow4);
            }


//            // Header
//            sheet.addMergedRegion();
//
//
//

            Cell cellAttendance = headerRow4.createCell(0);
            cellAttendance.setCellValue("ĐIỂM DANH");
            CellStyle cellAttendanceStyle = workbook.createCellStyle();
            Font headerAttendance = workbook.createFont();
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
            cellStatus.setCellValue("TRẠNG THÁI ĐI HỌC");
            CellStyle cellStatusStyle = workbook.createCellStyle();
            Font headerStatus = workbook.createFont();
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
            cellLesson.setCellValue("BUỔI HỌC");


            CellStyle cellLessonStyle = workbook.createCellStyle();
            Font headerLesson = workbook.createFont();
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


            Cell cellMeal = headerRow4.createCell(14);
            cellMeal.setCellValue("BỮA ĂN");
            CellStyle cellMealStyle = workbook.createCellStyle();
            Font headerMeal = workbook.createFont();
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
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 14, 19));

            Cell cellShuttle = headerRow4.createCell(20);
            cellShuttle.setCellValue("GIỜ ĐƯA ĐÓN");
            CellStyle cellShuttleStyle = workbook.createCellStyle();
            Font headerShuttle = workbook.createFont();
            headerShuttle.setBold(true);
            cellShuttleStyle.setFont(headerShuttle);
            ((XSSFCellStyle) cellShuttleStyle).setFillForegroundColor(blueTwo);
            cellShuttleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellShuttleStyle.setAlignment(HorizontalAlignment.CENTER);
            cellShuttleStyle.setBorderBottom(BorderStyle.THIN);
            cellShuttleStyle.setBorderTop(BorderStyle.THIN);
            cellShuttleStyle.setBorderRight(BorderStyle.THIN);
            cellShuttleStyle.setBorderLeft(BorderStyle.THIN);
            cellShuttle.setCellStyle(cellShuttleStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 20, 21));

            Cell cellLate = headerRow4.createCell(22);
            cellLate.setCellValue("ĐÓN MUỘN");
            CellStyle cellLateStyle = workbook.createCellStyle();
            Font headerLate = workbook.createFont();
            headerLate.setBold(true);
            cellLateStyle.setFont(headerLate);
            ((XSSFCellStyle) cellLateStyle).setFillForegroundColor(yellowThree);
            cellLateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellLateStyle.setAlignment(HorizontalAlignment.CENTER);
            cellLateStyle.setBorderBottom(BorderStyle.THIN);
            cellLateStyle.setBorderTop(BorderStyle.THIN);
            cellLateStyle.setBorderRight(BorderStyle.THIN);
            cellLateStyle.setBorderLeft(BorderStyle.THIN);
            cellLate.setCellStyle(cellLateStyle);


            int rowParent = 5;
            Row headerRow5 = sheet.createRow(rowParent);
            // Header 4
            for (int col = 0; col < 23; col++) {
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
            cellLateStyle1.setAlignment(HorizontalAlignment.CENTER);
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
            cellLesson4.setCellValue("Đi học");
            cellLesson4.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle4 = workbook.createCellStyle();
            cellLateStyle4.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 4, 4));

            Cell cellLesson6 = headerRow5.createCell(5);
            cellLesson6.setCellValue("Sáng");
            cellLesson6.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle5 = workbook.createCellStyle();
            cellLateStyle5.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowParent, 5, 7));

            Cell cellLesson7 = headerRow5.createCell(8);
            cellLesson7.setCellValue("Chiều");
            cellLesson7.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle6 = workbook.createCellStyle();
            cellLateStyle6.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowParent, 8, 10));

            Cell cellLesson8 = headerRow5.createCell(11);
            cellLesson8.setCellValue("Tối");
            cellLesson8.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle7 = workbook.createCellStyle();
            cellLateStyle7.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowParent, 11, 13));

            Cell cellLesson9 = headerRow5.createCell(14);
            cellLesson9.setCellValue("Sáng");
            cellLesson9.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle8 = workbook.createCellStyle();
            cellLateStyle8.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 14, 14));

            Cell cellLesson10 = headerRow5.createCell(15);
            cellLesson10.setCellValue("Phụ Sáng");
            cellLesson10.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle9 = workbook.createCellStyle();
            cellLateStyle9.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 15, 15));

            Cell cellLesson11 = headerRow5.createCell(16);
            cellLesson11.setCellValue("Trưa");
            cellLesson11.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle10 = workbook.createCellStyle();
            cellLateStyle10.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 16, 16));

            Cell cellLesson12 = headerRow5.createCell(17);
            cellLesson12.setCellValue("Chiều");
            cellLesson12.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle11 = workbook.createCellStyle();
            cellLateStyle11.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 17, 17));

            Cell cellLesson13 = headerRow5.createCell(18);
            cellLesson13.setCellValue("Phụ Chiều");
            cellLesson13.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle12 = workbook.createCellStyle();
            cellLateStyle12.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 18, 18));

            Cell cellLesson14 = headerRow5.createCell(19);
            cellLesson14.setCellValue("Tối");
            cellLesson14.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle13 = workbook.createCellStyle();
            cellLateStyle13.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 19, 19));

            Cell cellLesson15 = headerRow5.createCell(20);
            cellLesson15.setCellValue("Đến");
            cellLesson15.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle14 = workbook.createCellStyle();
            cellLateStyle14.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 20, 20));

            Cell cellLesson16 = headerRow5.createCell(21);
            cellLesson16.setCellValue("Về");
            cellLesson16.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle15 = workbook.createCellStyle();
            cellLateStyle15.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 21, 21));

            Cell cellLesson17 = headerRow5.createCell(22);
            cellLesson17.setCellValue("Phút");
            cellLesson17.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle16 = workbook.createCellStyle();
            cellLateStyle16.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 22, 22));


            Row headerRow6 = sheet.createRow(rowChild);

// Header
            for (int col = 0; col < 23; col++) {

                if (col < 5) {
                    sheet.setColumnWidth(col, widths[col] * 256);
                    Cell cell = headerRow6.createCell(col);
                    cell.setCellStyle(headerKidsCellStyle);
//                    CellStyle cellHeaderRow6 = workbook.createCellStyle();
//                    cellHeaderRow6.setBorderBottom(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderTop(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderRight(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderLeft(BorderStyle.THIN);
//                    cell.setCellStyle(cellHeaderRow6);
                }
                if (col > 4 && col < 14) {
                    sheet.setColumnWidth(col, widths[col] * 256);
                    Cell cell = headerRow6.createCell(col);
                    cell.setCellStyle(headerKidsCellStyle);
                    cell.setCellValue(columns[col]);
//                    CellStyle cellHeaderRow6 = workbook.createCellStyle();
//                    cellHeaderRow6.setBorderBottom(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderTop(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderRight(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderLeft(BorderStyle.THIN);
//                    cell.setCellStyle(cellHeaderRow6);
                }


                if (col > 13) {
                    sheet.setColumnWidth(col, widths[col] * 256);
                    Cell cell = headerRow6.createCell(col);
                    cell.setCellStyle(headerKidsCellStyle);
//                    CellStyle cellHeaderRow6 = workbook.createCellStyle();
//                    cellHeaderRow6.setBorderBottom(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderTop(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderRight(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderLeft(BorderStyle.THIN);
//                    cell.setCellStyle(cellHeaderRow6);
                }

            }


//            Cell cellChild5 = headerRow6.createCell(5);
//            cellChild5.setCellValue("Có phép");
//            cellChild5.setCellStyle(headerKidsCellStyle);
//
//            Cell cellChild6 = headerRow6.createCell(6);
//            cellChild6.setCellValue("Không phép");
//            cellChild6.setCellStyle(headerKidsCellStyle);
//
//            Cell cellChild7 = headerRow6.createCell(7);
//            cellChild7.setCellValue("Đi học");
//            cellChild7.setCellStyle(headerKidsCellStyle);
//
//
//            Cell cellChild8 = headerRow6.createCell(8);
//            cellChild8.setCellValue("Có phép");
//            cellChild8.setCellStyle(headerKidsCellStyle);
//
//
//            Cell cellChild9 = headerRow6.createCell(9);
//            cellChild9.setCellValue("Không phép");
//            cellChild9.setCellStyle(headerKidsCellStyle);
//
//
//            Cell cellChild10 = headerRow6.createCell(10);
//            cellChild10.setCellValue("Đi học");
//            cellChild10.setCellStyle(headerKidsCellStyle);
//
//
//            Cell cellChild11 = headerRow6.createCell(11);
//            cellChild11.setCellValue("Có phép");
//            cellChild11.setCellStyle(headerKidsCellStyle);
//
//
//            Cell cellChild12 = headerRow6.createCell(12);
//            cellChild12.setCellValue("Không phép");
//            cellChild12.setCellStyle(headerKidsCellStyle);
//
//
//            Cell cellChild13 = headerRow6.createCell(13);
//            cellChild13.setCellValue("Đi học");
//            cellChild13.setCellStyle(headerKidsCellStyle);


            int rowIdx = 7;
            for (AttendanceKidsModel attendanceKidsModel : attendanceKidsModelList) {

                Row row = sheet.createRow(rowIdx++);

                Cell cellId = row.createCell(0);
                cellId.setCellValue(attendanceKidsModel.getId());
                cellId.setCellStyle(contentCellStyle);

                Cell cellKidName = row.createCell(1);
                cellKidName.setCellValue(attendanceKidsModel.getKidName());
                cellKidName.setCellStyle(contentCellStyle);

                Cell cellAbsentLetterYes = row.createCell(2);
                cellAbsentLetterYes.setCellValue(attendanceKidsModel.getAbsentLetterYes());
                cellAbsentLetterYes.setCellStyle(contentStatusStyle);

                Cell cellAbsentLetterNo = row.createCell(3);
                cellAbsentLetterNo.setCellValue(attendanceKidsModel.getAbsentLetterNo());
                cellAbsentLetterNo.setCellStyle(contentStatusStyle);

                Cell cellAbsentStatus = row.createCell(4);
                cellAbsentStatus.setCellValue(attendanceKidsModel.getAbsentStatus());
                cellAbsentStatus.setCellStyle(contentStatusStyle);

                Cell cellMorningYes = row.createCell(5);
                cellMorningYes.setCellValue(attendanceKidsModel.getMorningYes());
                cellMorningYes.setCellStyle(contentLessonStyle);

                Cell cellMorningNo = row.createCell(6);
                cellMorningNo.setCellValue(attendanceKidsModel.getMorningNo());
                cellMorningNo.setCellStyle(contentLessonStyle);

                Cell cellMorning = row.createCell(7);
                cellMorning.setCellValue(attendanceKidsModel.getMorning());
                cellMorning.setCellStyle(contentLessonStyle);

                Cell cellAfternoonYes = row.createCell(8);
                cellAfternoonYes.setCellValue(attendanceKidsModel.getAfternoonYes());
                cellAfternoonYes.setCellStyle(contentLessonStyle);


                Cell cellAfternoonNo = row.createCell(9);
                cellAfternoonNo.setCellValue(attendanceKidsModel.getAfternoonNo());
                cellAfternoonNo.setCellStyle(contentLessonStyle);

                Cell cellAfternoon = row.createCell(10);
                cellAfternoon.setCellValue(attendanceKidsModel.getAfternoon());
                cellAfternoon.setCellStyle(contentLessonStyle);

                Cell cellEveningYes = row.createCell(11);
                cellEveningYes.setCellValue(attendanceKidsModel.getEveningYes());
                cellEveningYes.setCellStyle(contentLessonStyle);

                Cell cellEveningNo = row.createCell(12);
                cellEveningNo.setCellValue(attendanceKidsModel.getEveningNo());
                cellEveningNo.setCellStyle(contentLessonStyle);

                Cell cellEvening = row.createCell(13);
                cellEvening.setCellValue(attendanceKidsModel.getEvening());
                cellEvening.setCellStyle(contentLessonStyle);

                Cell cellEatBreakfast = row.createCell(14);
                cellEatBreakfast.setCellValue(attendanceKidsModel.getEatBreakfast());
                cellEatBreakfast.setCellStyle(contentMealStyle);

                Cell cellEatSecondBreakfast = row.createCell(15);
                cellEatSecondBreakfast.setCellValue(attendanceKidsModel.getEatSecondBreakfast());
                cellEatSecondBreakfast.setCellStyle(contentMealStyle);

                Cell celleatLunch = row.createCell(16);
                celleatLunch.setCellValue(attendanceKidsModel.getEatLunch());
                celleatLunch.setCellStyle(contentMealStyle);

                Cell celleatAfternoon = row.createCell(17);
                celleatAfternoon.setCellValue(attendanceKidsModel.getEatAfternoon());
                celleatAfternoon.setCellStyle(contentMealStyle);

                Cell celleatSecondAfternoon = row.createCell(18);
                celleatSecondAfternoon.setCellValue(attendanceKidsModel.getEatSecondAfternoon());
                celleatSecondAfternoon.setCellStyle(contentMealStyle);

                Cell celleatDinner = row.createCell(19);
                celleatDinner.setCellValue(attendanceKidsModel.getEatDinner());
                celleatDinner.setCellStyle(contentMealStyle);

                Cell celltimeArriveKid = row.createCell(20);
                celltimeArriveKid.setCellValue(attendanceKidsModel.getTimeArriveKid());
                celltimeArriveKid.setCellStyle(contentShuttleStyle);

                Cell celltimeLeaveKid = row.createCell(21);
                celltimeLeaveKid.setCellValue(attendanceKidsModel.getTimeLeaveKid());
                celltimeLeaveKid.setCellStyle(contentShuttleStyle);

                Cell cellminutePickupLate = row.createCell(22);
                cellminutePickupLate.setCellValue(attendanceKidsModel.getMinutePickupLate());
                cellminutePickupLate.setCellStyle(contentLateStyle);

            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public ByteArrayInputStream attendanceToExcelMonth(Map<Long, List<AttendanceKidsModel>> map, Long idSchool, Long idClass, LocalDate date) throws IOException {

        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(131, 164, 196));
        XSSFColor greenOne = new XSSFColor(new java.awt.Color(149, 203, 74));
        XSSFColor yellowTwo = new XSSFColor(new java.awt.Color(217, 210, 144));
        XSSFColor blueTwo = new XSSFColor(new java.awt.Color(51, 153, 255));
        XSSFColor yellowThree = new XSSFColor(new java.awt.Color(224, 191, 28));

        int[] widths = {5, 30, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 10, 10, 20};
        int month = date.getMonthValue();
        int year = date.getYear();
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1).minusDays(1);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        String dateToStr = df.format(dateStart);
        String dateToStrSheet = df.format(dateEnd);
        String[] columns = {"STT", "Họ và tên", "Nghỉ có phép", "Nghỉ không phép", "Đi học", "Có phép", "Không phép", "Đi học", "Có phép", "Không phép", "Đi học", "Có phép", "Không phép", "Đi học", "Sáng", "Phụ sáng", "Trưa", "Chiều", "Phụ Chiều", "Tối", "Đến", "Về", "Phút"};


        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {

            for (Map.Entry<Long, List<AttendanceKidsModel>> entry : map.entrySet()) {
                List<AttendanceKidsModel> kidVMList = entry.getValue();
                Long idKid = entry.getKey();
////                List<Kids> kidsDTO = kidsRepository.findByIdKidtest(idSchool, idKid);
//
//                KidsDTO kidsDTO = kidsService.findByIdKidEx(idSchool, idKid).stream().findFirst().orElse(null);
//                String kidsName = "";
//
//                if (kidsDTO != null) {
//                    kidsName = kidsDTO.getFullName();
//                }
                Optional<Kids> kids = kidsRepository.findById(idKid);
                if (kids.isEmpty()) {
                    continue;
                }
                String kidsName = kids.get().getFullName();
                SchoolResponse schoolResponse = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
                String schoolName = "";
                if (schoolResponse != null) {
                    schoolName = schoolResponse.getSchoolName();
                }

                MaClassDTO classDTO = maClassService.findByIdMaClass(idSchool, idClass).stream().findFirst().orElse(null);
                String className = "";
                if (classDTO != null) {
                    className = classDTO.getClassName();
                }
                Sheet sheet = workbook.createSheet(kidsName.concat(AppConstant.SPACE_EXPORT_ID.concat(kids.get().getId().toString())));

//                sheet.setDisplayGridlines(false);
                for (int i = 0; i < 4; i++) {
                    Row headerRow = sheet.createRow(i);
                    for (int col = 0; col < columns.length; col++) {
                        Cell cell = headerRow.createCell(col);
                        if (col == 0 && i == 0) {
                            cell.setCellValue("BẢNG KÊ ĐIỂM DANH THÁNG");
                            CellStyle cellStyle = workbook.createCellStyle();
                            Font cellFont = workbook.createFont();
                            cellFont.setFontHeightInPoints((short) 18);
                            cellFont.setBold(true);
                            cellFont.setColor(IndexedColors.RED.getIndex());

                            cellStyle.setFont(cellFont);
                            cell.setCellStyle(cellStyle);
                        } else if (col == 0 && i == 1) {
                            cell.setCellValue("Trường: " + schoolName);
                            CellStyle twoStyle = workbook.createCellStyle();
                            Font cellFont = workbook.createFont();
                            cellFont.setBold(true);
                            twoStyle.setFont(cellFont);
                            cell.setCellStyle(twoStyle);
                        } else if (col == 0 && i == 2) {
                            cell.setCellValue("Lớp: " + className);
                            CellStyle threeStyle = workbook.createCellStyle();
                            Font cellFont = workbook.createFont();
                            cellFont.setFontHeightInPoints((short) 11);
                            cellFont.setBold(true);
                            threeStyle.setFont(cellFont);
                            cell.setCellStyle(threeStyle);
                        } else if (col == 0 && i == 3) {
                            cell.setCellValue("Ngày: " + dateToStr + " - " + dateToStrSheet);
                            CellStyle threeStyle = workbook.createCellStyle();
                            Font cellFont = workbook.createFont();
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
                Font headerFont = workbook.createFont();
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
                Font contentFont = workbook.createFont();
                contentFont.setColor(IndexedColors.BLACK.getIndex());
                contentCellStyle.setFont(contentFont);
                contentCellStyle.setWrapText(true);
                contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentCellStyle.setAlignment(HorizontalAlignment.CENTER);
                contentCellStyle.setBorderBottom(BorderStyle.THIN);
                contentCellStyle.setBorderTop(BorderStyle.THIN);
                contentCellStyle.setBorderRight(BorderStyle.THIN);
                contentCellStyle.setBorderLeft(BorderStyle.THIN);
                //Style content  status
                CellStyle contentStatusStyle = workbook.createCellStyle();
                Font contentStatusFont = workbook.createFont();
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
                Font contentLessonFont = workbook.createFont();
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
                //Style content  Meal
                CellStyle contentMealStyle = workbook.createCellStyle();
                Font contentMealFont = workbook.createFont();
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
                //Style content  shuttle
                CellStyle contentShuttleStyle = workbook.createCellStyle();
                Font contentShuttleFont = workbook.createFont();
                contentShuttleFont.setColor(IndexedColors.BLACK.getIndex());
                contentShuttleStyle.setFont(contentShuttleFont);
                contentShuttleStyle.setWrapText(true);
                ((XSSFCellStyle) contentShuttleStyle).setFillForegroundColor(blueTwo);
                contentShuttleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentShuttleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentShuttleStyle.setAlignment(HorizontalAlignment.CENTER);
                contentShuttleStyle.setBorderBottom(BorderStyle.THIN);
                contentShuttleStyle.setBorderTop(BorderStyle.THIN);
                contentShuttleStyle.setBorderRight(BorderStyle.THIN);
                contentShuttleStyle.setBorderLeft(BorderStyle.THIN);
                //Style content  status
                CellStyle contentLateStyle = workbook.createCellStyle();
                Font contentLateFont = workbook.createFont();
                contentLateFont.setColor(IndexedColors.BLACK.getIndex());
                contentLateStyle.setFont(contentLateFont);
                contentLateStyle.setWrapText(true);
                ((XSSFCellStyle) contentLateStyle).setFillForegroundColor(yellowThree);
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
                for (int col = 0; col < 23; col++) {
                    Cell cell = headerRow4.createCell(col);
                    CellStyle cellHeaderRow4 = workbook.createCellStyle();
                    cellHeaderRow4.setBorderBottom(BorderStyle.THIN);
                    cellHeaderRow4.setBorderTop(BorderStyle.THIN);
                    cellHeaderRow4.setBorderRight(BorderStyle.THIN);
                    cellHeaderRow4.setBorderLeft(BorderStyle.THIN);
                    cell.setCellStyle(cellHeaderRow4);
                }


//            // Header
//            sheet.addMergedRegion();
//
//
//

                Cell cellAttendance = headerRow4.createCell(0);
                cellAttendance.setCellValue("ĐIỂM DANH");
                CellStyle cellAttendanceStyle = workbook.createCellStyle();
                Font headerAttendance = workbook.createFont();
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
                cellStatus.setCellValue("TRẠNG THÁI ĐI HỌC");
                CellStyle cellStatusStyle = workbook.createCellStyle();
                Font headerStatus = workbook.createFont();
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
                cellLesson.setCellValue("BUỔI HỌC");


                CellStyle cellLessonStyle = workbook.createCellStyle();
                Font headerLesson = workbook.createFont();
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


                Cell cellMeal = headerRow4.createCell(14);
                cellMeal.setCellValue("BỮA ĂN");
                CellStyle cellMealStyle = workbook.createCellStyle();
                Font headerMeal = workbook.createFont();
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
                sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 14, 19));

                Cell cellShuttle = headerRow4.createCell(20);
                cellShuttle.setCellValue("GIỜ ĐƯA ĐÓN");
                CellStyle cellShuttleStyle = workbook.createCellStyle();
                Font headerShuttle = workbook.createFont();
                headerShuttle.setBold(true);
                cellShuttleStyle.setFont(headerShuttle);
                ((XSSFCellStyle) cellShuttleStyle).setFillForegroundColor(blueTwo);
                cellShuttleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellShuttleStyle.setAlignment(HorizontalAlignment.CENTER);
                cellShuttleStyle.setBorderBottom(BorderStyle.THIN);
                cellShuttleStyle.setBorderTop(BorderStyle.THIN);
                cellShuttleStyle.setBorderRight(BorderStyle.THIN);
                cellShuttleStyle.setBorderLeft(BorderStyle.THIN);
                cellShuttle.setCellStyle(cellShuttleStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 20, 21));

                Cell cellLate = headerRow4.createCell(22);
                cellLate.setCellValue("ĐÓN MUỘN");
                CellStyle cellLateStyle = workbook.createCellStyle();
                Font headerLate = workbook.createFont();
                headerLate.setBold(true);
                cellLateStyle.setFont(headerLate);
                ((XSSFCellStyle) cellLateStyle).setFillForegroundColor(yellowThree);
                cellLateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellLateStyle.setAlignment(HorizontalAlignment.CENTER);
                cellLateStyle.setBorderBottom(BorderStyle.THIN);
                cellLateStyle.setBorderTop(BorderStyle.THIN);
                cellLateStyle.setBorderRight(BorderStyle.THIN);
                cellLateStyle.setBorderLeft(BorderStyle.THIN);
                cellLate.setCellStyle(cellLateStyle);


                int rowParent = 5;
                Row headerRow5 = sheet.createRow(rowParent);
                // Header 4
                for (int col = 0; col < 23; col++) {
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
                cellLesson1.setCellValue("Thời gian");
                cellLesson1.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle1 = workbook.createCellStyle();
                cellLateStyle1.setAlignment(HorizontalAlignment.CENTER);
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
                cellLesson4.setCellValue("Đi học");
                cellLesson4.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle4 = workbook.createCellStyle();
                cellLateStyle4.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 4, 4));

                Cell cellLesson6 = headerRow5.createCell(5);
                cellLesson6.setCellValue("Sáng");
                cellLesson6.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle5 = workbook.createCellStyle();
                cellLateStyle5.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowParent, 5, 7));

                Cell cellLesson7 = headerRow5.createCell(8);
                cellLesson7.setCellValue("Chiều");
                cellLesson7.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle6 = workbook.createCellStyle();
                cellLateStyle6.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowParent, 8, 10));

                Cell cellLesson8 = headerRow5.createCell(11);
                cellLesson8.setCellValue("Tối");
                cellLesson8.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle7 = workbook.createCellStyle();
                cellLateStyle7.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowParent, 11, 13));

                Cell cellLesson9 = headerRow5.createCell(14);
                cellLesson9.setCellValue("Sáng");
                cellLesson9.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle8 = workbook.createCellStyle();
                cellLateStyle8.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 14, 14));

                Cell cellLesson10 = headerRow5.createCell(15);
                cellLesson10.setCellValue("Phụ Sáng");
                cellLesson10.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle9 = workbook.createCellStyle();
                cellLateStyle9.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 15, 15));

                Cell cellLesson11 = headerRow5.createCell(16);
                cellLesson11.setCellValue("Trưa");
                cellLesson11.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle10 = workbook.createCellStyle();
                cellLateStyle10.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 16, 16));

                Cell cellLesson12 = headerRow5.createCell(17);
                cellLesson12.setCellValue("Chiều");
                cellLesson12.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle11 = workbook.createCellStyle();
                cellLateStyle11.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 17, 17));

                Cell cellLesson13 = headerRow5.createCell(18);
                cellLesson13.setCellValue("Phụ Chiều");
                cellLesson13.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle12 = workbook.createCellStyle();
                cellLateStyle12.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 18, 18));

                Cell cellLesson14 = headerRow5.createCell(19);
                cellLesson14.setCellValue("Tối");
                cellLesson14.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle13 = workbook.createCellStyle();
                cellLateStyle13.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 19, 19));

                Cell cellLesson15 = headerRow5.createCell(20);
                cellLesson15.setCellValue("Đến");
                cellLesson15.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle14 = workbook.createCellStyle();
                cellLateStyle14.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 20, 20));

                Cell cellLesson16 = headerRow5.createCell(21);
                cellLesson16.setCellValue("Về");
                cellLesson16.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle15 = workbook.createCellStyle();
                cellLateStyle15.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 21, 21));

                Cell cellLesson17 = headerRow5.createCell(22);
                cellLesson17.setCellValue("Phút");
                cellLesson17.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle16 = workbook.createCellStyle();
                cellLateStyle16.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 22, 22));


                Row headerRow6 = sheet.createRow(rowChild);

// Header
                for (int col = 0; col < 23; col++) {

                    if (col < 5) {
                        sheet.setColumnWidth(col, widths[col] * 256);
                        Cell cell = headerRow6.createCell(col);
                        cell.setCellStyle(headerKidsCellStyle);
//                    CellStyle cellHeaderRow6 = workbook.createCellStyle();
//                    cellHeaderRow6.setBorderBottom(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderTop(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderRight(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderLeft(BorderStyle.THIN);
//                    cell.setCellStyle(cellHeaderRow6);
                    }
                    if (col > 4 && col < 14) {
                        sheet.setColumnWidth(col, widths[col] * 256);
                        Cell cell = headerRow6.createCell(col);
                        cell.setCellStyle(headerKidsCellStyle);
                        cell.setCellValue(columns[col]);
//                    CellStyle cellHeaderRow6 = workbook.createCellStyle();
//                    cellHeaderRow6.setBorderBottom(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderTop(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderRight(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderLeft(BorderStyle.THIN);
//                    cell.setCellStyle(cellHeaderRow6);
                    }


                    if (col > 13) {
                        sheet.setColumnWidth(col, widths[col] * 256);
                        Cell cell = headerRow6.createCell(col);
                        cell.setCellStyle(headerKidsCellStyle);
//                    CellStyle cellHeaderRow6 = workbook.createCellStyle();
//                    cellHeaderRow6.setBorderBottom(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderTop(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderRight(BorderStyle.THIN);
//                    cellHeaderRow6.setBorderLeft(BorderStyle.THIN);
//                    cell.setCellStyle(cellHeaderRow6);
                    }

                }


//            Cell cellChild5 = headerRow6.createCell(5);
//            cellChild5.setCellValue("Có phép");
//            cellChild5.setCellStyle(headerKidsCellStyle);
//
//            Cell cellChild6 = headerRow6.createCell(6);
//            cellChild6.setCellValue("Không phép");
//            cellChild6.setCellStyle(headerKidsCellStyle);
//
//            Cell cellChild7 = headerRow6.createCell(7);
//            cellChild7.setCellValue("Đi học");
//            cellChild7.setCellStyle(headerKidsCellStyle);
//
//
//            Cell cellChild8 = headerRow6.createCell(8);
//            cellChild8.setCellValue("Có phép");
//            cellChild8.setCellStyle(headerKidsCellStyle);
//
//
//            Cell cellChild9 = headerRow6.createCell(9);
//            cellChild9.setCellValue("Không phép");
//            cellChild9.setCellStyle(headerKidsCellStyle);
//
//
//            Cell cellChild10 = headerRow6.createCell(10);
//            cellChild10.setCellValue("Đi học");
//            cellChild10.setCellStyle(headerKidsCellStyle);
//
//
//            Cell cellChild11 = headerRow6.createCell(11);
//            cellChild11.setCellValue("Có phép");
//            cellChild11.setCellStyle(headerKidsCellStyle);
//
//
//            Cell cellChild12 = headerRow6.createCell(12);
//            cellChild12.setCellValue("Không phép");
//            cellChild12.setCellStyle(headerKidsCellStyle);
//
//
//            Cell cellChild13 = headerRow6.createCell(13);
//            cellChild13.setCellValue("Đi học");
//            cellChild13.setCellStyle(headerKidsCellStyle);

                int rowIdx = 7;

                for (AttendanceKidsModel attendanceKidsModel : kidVMList) {

                    Row row = sheet.createRow(rowIdx++);

                    Cell cellId = row.createCell(0);
                    cellId.setCellValue(attendanceKidsModel.getId());
                    cellId.setCellStyle(contentCellStyle);

                    Cell cellKidName = row.createCell(1);
                    cellKidName.setCellValue(attendanceKidsModel.getAttendanceDate());
                    cellKidName.setCellStyle(contentCellStyle);

                    Cell cellAbsentLetterYes = row.createCell(2);
                    cellAbsentLetterYes.setCellValue(attendanceKidsModel.getAbsentLetterYes());
                    cellAbsentLetterYes.setCellStyle(contentStatusStyle);

                    Cell cellAbsentLetterNo = row.createCell(3);
                    cellAbsentLetterNo.setCellValue(attendanceKidsModel.getAbsentLetterNo());
                    cellAbsentLetterNo.setCellStyle(contentStatusStyle);

                    Cell cellAbsentStatus = row.createCell(4);
                    cellAbsentStatus.setCellValue(attendanceKidsModel.getAbsentStatus());
                    cellAbsentStatus.setCellStyle(contentStatusStyle);

                    Cell cellMorningYes = row.createCell(5);
                    cellMorningYes.setCellValue(attendanceKidsModel.getMorningYes());
                    cellMorningYes.setCellStyle(contentLessonStyle);

                    Cell cellMorningNo = row.createCell(6);
                    cellMorningNo.setCellValue(attendanceKidsModel.getMorningNo());
                    cellMorningNo.setCellStyle(contentLessonStyle);

                    Cell cellMorning = row.createCell(7);
                    cellMorning.setCellValue(attendanceKidsModel.getMorning());
                    cellMorning.setCellStyle(contentLessonStyle);

                    Cell cellAfternoonYes = row.createCell(8);
                    cellAfternoonYes.setCellValue(attendanceKidsModel.getAfternoonYes());
                    cellAfternoonYes.setCellStyle(contentLessonStyle);


                    Cell cellAfternoonNo = row.createCell(9);
                    cellAfternoonNo.setCellValue(attendanceKidsModel.getAfternoonNo());
                    cellAfternoonNo.setCellStyle(contentLessonStyle);

                    Cell cellAfternoon = row.createCell(10);
                    cellAfternoon.setCellValue(attendanceKidsModel.getAfternoon());
                    cellAfternoon.setCellStyle(contentLessonStyle);

                    Cell cellEveningYes = row.createCell(11);
                    cellEveningYes.setCellValue(attendanceKidsModel.getEveningYes());
                    cellEveningYes.setCellStyle(contentLessonStyle);

                    Cell cellEveningNo = row.createCell(12);
                    cellEveningNo.setCellValue(attendanceKidsModel.getEveningNo());
                    cellEveningNo.setCellStyle(contentLessonStyle);

                    Cell cellEvening = row.createCell(13);
                    cellEvening.setCellValue(attendanceKidsModel.getEvening());
                    cellEvening.setCellStyle(contentLessonStyle);

                    Cell cellEatBreakfast = row.createCell(14);
                    cellEatBreakfast.setCellValue(attendanceKidsModel.getEatBreakfast());
                    cellEatBreakfast.setCellStyle(contentMealStyle);

                    Cell cellEatSecondBreakfast = row.createCell(15);
                    cellEatSecondBreakfast.setCellValue(attendanceKidsModel.getEatSecondBreakfast());
                    cellEatSecondBreakfast.setCellStyle(contentMealStyle);

                    Cell celleatLunch = row.createCell(16);
                    celleatLunch.setCellValue(attendanceKidsModel.getEatLunch());
                    celleatLunch.setCellStyle(contentMealStyle);

                    Cell celleatAfternoon = row.createCell(17);
                    celleatAfternoon.setCellValue(attendanceKidsModel.getEatAfternoon());
                    celleatAfternoon.setCellStyle(contentMealStyle);

                    Cell celleatSecondAfternoon = row.createCell(18);
                    celleatSecondAfternoon.setCellValue(attendanceKidsModel.getEatSecondAfternoon());
                    celleatSecondAfternoon.setCellStyle(contentMealStyle);

                    Cell celleatDinner = row.createCell(19);
                    celleatDinner.setCellValue(attendanceKidsModel.getEatDinner());
                    celleatDinner.setCellStyle(contentMealStyle);

                    Cell celltimeArriveKid = row.createCell(20);
                    celltimeArriveKid.setCellValue(attendanceKidsModel.getTimeArriveKid());
                    celltimeArriveKid.setCellStyle(contentShuttleStyle);

                    Cell celltimeLeaveKid = row.createCell(21);
                    celltimeLeaveKid.setCellValue(attendanceKidsModel.getTimeLeaveKid());
                    celltimeLeaveKid.setCellStyle(contentShuttleStyle);

                    Cell cellminutePickupLate = row.createCell(22);
                    cellminutePickupLate.setCellValue(attendanceKidsModel.getMinutePickupLate());
                    cellminutePickupLate.setCellStyle(contentLateStyle);

                }


            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }

    }

    @Override
    public ByteArrayInputStream exportAttendaceKidCustom(UserPrincipal principal, AttendanceKidsSearchCustomRequest request) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(131, 164, 196));
        XSSFColor greenOne = new XSSFColor(new java.awt.Color(149, 203, 74));
        XSSFColor yellowTwo = new XSSFColor(new java.awt.Color(217, 210, 144));
        XSSFColor blueTwo = new XSSFColor(new java.awt.Color(51, 153, 255));
        XSSFColor yellowThree = new XSSFColor(new java.awt.Color(224, 191, 28));
        XSSFColor yellowThreeDay = new XSSFColor(new java.awt.Color(180, 198, 231));

        int[] widths = {5, 30, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 14, 7, 7, 7, 7, 7, 7, 20};
        LocalDate dateStart = request.getDateStartEnd().get(0);
        LocalDate dateEnd = request.getDateStartEnd().get(1);

        Long idSchool = principal.getIdSchoolLogin();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dateToStr = df.format(dateStart);
        String dateToStrSheet = df.format(dateEnd);
        String[] columns = {"STT", "Họ và tên", "Nghỉ có phép", "Nghỉ không phép", "Đi học", "Có phép", "Không phép", "Đi học", "Có phép", "Không phép", "Đi học", "Có phép", "Không phép", "Đi học", "Buổi", "Sáng", "Phụ sáng", "Trưa", "Chiều", "Phụ Chiều", "Tối", "Phút"};

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
            String schoolName = school.getSchoolName();
            // xuất học sinh theo trường
            if (request.getIdClass() == null) {

                List<Long> idClassList = maClassRepository.findIdClassInSchool(idSchool);
                idClassList.forEach(a -> {
                    MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(a).orElseThrow();
                    List<Kids> kidsList = kidsRepository.findByDelActiveTrueAndMaClass_IdAndKidStatus(a, request.getStatus());
                    Sheet sheet = workbook.createSheet(maClass.getClassName());
                    for (int i = 0; i < 4; i++) {
                        Row headerRow = sheet.createRow(i);
                        for (int col = 0; col < columns.length; col++) {
                            Cell cell = headerRow.createCell(col);
                            if (col == 0 && i == 0) {
                                cell.setCellValue("BẢNG KÊ ĐIỂM DANH");
                                CellStyle cellStyle = workbook.createCellStyle();
                                Font cellFont = workbook.createFont();
                                cellFont.setFontHeightInPoints((short) 18);
                                cellFont.setBold(true);
                                cellFont.setColor(IndexedColors.RED.getIndex());

                                cellStyle.setFont(cellFont);
                                cell.setCellStyle(cellStyle);
                            } else if (col == 0 && i == 1) {
                                cell.setCellValue("Trường: " + schoolName);
                                CellStyle twoStyle = workbook.createCellStyle();
                                Font cellFont = workbook.createFont();
                                cellFont.setBold(true);
                                twoStyle.setFont(cellFont);
                                cell.setCellStyle(twoStyle);
                            } else if (col == 0 && i == 2) {
                                cell.setCellValue("Toàn trường: ");
                                CellStyle threeStyle = workbook.createCellStyle();
                                Font cellFont = workbook.createFont();
                                cellFont.setFontHeightInPoints((short) 11);
                                cellFont.setBold(true);
                                threeStyle.setFont(cellFont);
                                cell.setCellStyle(threeStyle);
                            } else if (col == 0 && i == 3) {
                                cell.setCellValue("Ngày: " + dateToStr + " - " + dateToStrSheet);
                                CellStyle threeStyle = workbook.createCellStyle();
                                Font cellFont = workbook.createFont();
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
                    Font headerFont = workbook.createFont();
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
                    Font contentFont = workbook.createFont();
                    contentFont.setColor(IndexedColors.BLACK.getIndex());
                    contentCellStyle.setFont(contentFont);
                    contentCellStyle.setWrapText(true);
                    contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    contentCellStyle.setAlignment(HorizontalAlignment.CENTER);
                    contentCellStyle.setBorderBottom(BorderStyle.THIN);
                    contentCellStyle.setBorderTop(BorderStyle.THIN);
                    contentCellStyle.setBorderRight(BorderStyle.THIN);
                    contentCellStyle.setBorderLeft(BorderStyle.THIN);
                    //Style content  status
                    CellStyle contentStatusStyle = workbook.createCellStyle();
                    Font contentStatusFont = workbook.createFont();
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
                    Font contentLessonFont = workbook.createFont();
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
                    Font contentLateFontDay = workbook.createFont();
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
                    Font contentMealFont = workbook.createFont();
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
                    Font contentLateFont = workbook.createFont();
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
                    for (int col = 0; col < 22; col++) {
                        Cell cell = headerRow4.createCell(col);
                        CellStyle cellHeaderRow4 = workbook.createCellStyle();
                        cellHeaderRow4.setBorderBottom(BorderStyle.THIN);
                        cellHeaderRow4.setBorderTop(BorderStyle.THIN);
                        cellHeaderRow4.setBorderRight(BorderStyle.THIN);
                        cellHeaderRow4.setBorderLeft(BorderStyle.THIN);
                        cell.setCellStyle(cellHeaderRow4);
                    }


                    Cell cellAttendance = headerRow4.createCell(0);
                    cellAttendance.setCellValue("ĐIỂM DANH");
                    CellStyle cellAttendanceStyle = workbook.createCellStyle();
                    Font headerAttendance = workbook.createFont();
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
                    cellStatus.setCellValue("HỌC CẢ NGÀY");
                    CellStyle cellStatusStyle = workbook.createCellStyle();
                    Font headerStatus = workbook.createFont();
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
                    cellLesson.setCellValue("BUỔI HỌC");


                    CellStyle cellLessonStyle = workbook.createCellStyle();
                    Font headerLesson = workbook.createFont();
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
                    Font headerLateDay = workbook.createFont();
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
                    Font headerMeal = workbook.createFont();
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
                    sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 15, 20));


                    Cell cellLate = headerRow4.createCell(21);
                    cellLate.setCellValue("ĐÓN MUỘN");
                    CellStyle cellLateStyle = workbook.createCellStyle();
                    Font headerLate = workbook.createFont();
                    headerLate.setBold(true);
                    cellLateStyle.setFont(headerLate);
                    ((XSSFCellStyle) cellLateStyle).setFillForegroundColor(yellowThree);
                    cellLateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cellLateStyle.setAlignment(HorizontalAlignment.CENTER);
                    cellLateStyle.setBorderBottom(BorderStyle.THIN);
                    cellLateStyle.setBorderTop(BorderStyle.THIN);
                    cellLateStyle.setBorderRight(BorderStyle.THIN);
                    cellLateStyle.setBorderLeft(BorderStyle.THIN);
                    cellLate.setCellStyle(cellLateStyle);

                    int rowParent = 5;
                    Row headerRow5 = sheet.createRow(rowParent);
                    // Header 4
                    for (int col = 0; col < 22; col++) {
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
                    cellLateStyle1.setAlignment(HorizontalAlignment.CENTER);
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
                    cellLesson4.setCellValue("Đi học");
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

                    Cell cellLesson10 = headerRow5.createCell(16);
                    cellLesson10.setCellValue("Chỉ ăn phụ sáng");
                    cellLesson10.setCellStyle(headerKidsCellStyle);
                    CellStyle cellLateStyle9 = workbook.createCellStyle();
                    cellLateStyle9.setAlignment(HorizontalAlignment.CENTER);
                    sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 16, 16));

                    Cell cellLesson11 = headerRow5.createCell(17);
                    cellLesson11.setCellValue("Chỉ ăn trưa");
                    cellLesson11.setCellStyle(headerKidsCellStyle);
                    CellStyle cellLateStyle10 = workbook.createCellStyle();
                    cellLateStyle10.setAlignment(HorizontalAlignment.CENTER);
                    sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 17, 17));

                    Cell cellLesson12 = headerRow5.createCell(18);
                    cellLesson12.setCellValue("Chỉ ăn chiều ");
                    cellLesson12.setCellStyle(headerKidsCellStyle);
                    CellStyle cellLateStyle11 = workbook.createCellStyle();
                    cellLateStyle11.setAlignment(HorizontalAlignment.CENTER);
                    sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 18, 18));

                    Cell cellLesson13 = headerRow5.createCell(19);
                    cellLesson13.setCellValue("Chỉ ăn phụ chiều");
                    cellLesson13.setCellStyle(headerKidsCellStyle);
                    CellStyle cellLateStyle12 = workbook.createCellStyle();
                    cellLateStyle12.setAlignment(HorizontalAlignment.CENTER);
                    sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 19, 19));

                    Cell cellLesson14 = headerRow5.createCell(20);
                    cellLesson14.setCellValue("Chỉ ăn tối");
                    cellLesson14.setCellStyle(headerKidsCellStyle);
                    CellStyle cellLateStyle13 = workbook.createCellStyle();
                    cellLateStyle13.setAlignment(HorizontalAlignment.CENTER);
                    sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 20, 20));

                    Cell cellLesson17 = headerRow5.createCell(21);
                    cellLesson17.setCellValue("Phút");
                    cellLesson17.setCellStyle(headerKidsCellStyle);
                    CellStyle cellLateStyle16 = workbook.createCellStyle();
                    cellLateStyle16.setAlignment(HorizontalAlignment.CENTER);
                    sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 21, 21));

                    Row headerRow6 = sheet.createRow(rowChild);

                    for (int col = 0; col < 22; col++) {
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
                    int getEatMorningSecond = 0;

                    int getEatNoon = 0;
                    int getEatAfternoon = 0;
                    int getEatAfternoonSecond = 0;
                    int geteatEvening = 0;
                    int getMinutesLate = 0;

                    for (Kids x : kidsList) {
                        AttendanceKidsStatisticalResponse fillData = attendanceKidsStatisticalService.attendanceKidsStatistical(x, dateStart, dateEnd);
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
                        getEatMorningSecond = fillData.getEatMorningSecond() + getEatMorningSecond;
                        getEatNoon = fillData.getEatNoon() + getEatNoon;
                        getEatAfternoon = fillData.getEatAfternoon() + getEatAfternoon;
                        getEatAfternoonSecond = fillData.getEatAfternoonSecond() + getEatAfternoonSecond;
                        geteatEvening = fillData.getEatEvening() + geteatEvening;
                        getMinutesLate = fillData.getMinutesLate() + getMinutesLate;


                        Row row = sheet.createRow(rowIdx++);

                        Cell cellId = row.createCell(0);
                        cellId.setCellValue(i);
                        cellId.setCellStyle(contentCellStyle);

                        Cell cellKidName = row.createCell(1);
                        cellKidName.setCellValue(x.getFullName());
                        cellKidName.setCellStyle(contentCellStyle);

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
                        cellEatSecondBreakfast.setCellValue(fillData.getEatMorningSecond());
                        cellEatSecondBreakfast.setCellStyle(contentMealStyle);

                        Cell celleatLunch = row.createCell(17);
                        celleatLunch.setCellValue(fillData.getEatNoon());
                        celleatLunch.setCellStyle(contentMealStyle);

                        Cell celleatAfternoon = row.createCell(18);
                        celleatAfternoon.setCellValue(fillData.getEatAfternoon());
                        celleatAfternoon.setCellStyle(contentMealStyle);

                        Cell celleatSecondAfternoon = row.createCell(19);
                        celleatSecondAfternoon.setCellValue(fillData.getEatAfternoonSecond());
                        celleatSecondAfternoon.setCellStyle(contentMealStyle);

                        Cell celleatDinner = row.createCell(20);
                        celleatDinner.setCellValue(fillData.getEatEvening());
                        celleatDinner.setCellStyle(contentMealStyle);

                        Cell cellminutePickupLate = row.createCell(21);
                        cellminutePickupLate.setCellValue(fillData.getMinutesLate());
                        cellminutePickupLate.setCellStyle(contentLateStyle);


                    }

                    int rowEnd = 7 + kidsList.size();
                    Row headerRowEnd = sheet.createRow(rowEnd);

                    Cell cellAttendanceTotal = headerRowEnd.createCell(0);
                    cellAttendanceTotal.setCellValue("Tổng cộng");
                    CellStyle cellAttendanceStyleTotal = workbook.createCellStyle();
                    Font headerAttendanceTotal = workbook.createFont();
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
                    cell16.setCellValue(getEatMorningSecond);

                    Cell cell17 = headerRowEnd.createCell(17);
                    cell17.setCellStyle(cellAttendanceStyleTotal);
                    cell17.setCellValue(getEatNoon);

                    Cell cell18 = headerRowEnd.createCell(18);
                    cell18.setCellStyle(cellAttendanceStyleTotal);
                    cell18.setCellValue(getEatAfternoon);

                    Cell cell19 = headerRowEnd.createCell(19);
                    cell19.setCellStyle(cellAttendanceStyleTotal);
                    cell19.setCellValue(getEatAfternoonSecond);

                    Cell cell20 = headerRowEnd.createCell(20);
                    cell20.setCellStyle(cellAttendanceStyleTotal);
                    cell20.setCellValue(geteatEvening);

                    Cell cell21 = headerRowEnd.createCell(21);
                    cell21.setCellStyle(cellAttendanceStyleTotal);
                    cell21.setCellValue(getMinutesLate);

                });

            } else {
                Long idClass = request.getIdClass();
                List<Kids> kidsList = kidsRepository.findByDelActiveTrueAndMaClass_IdAndKidStatus(idClass, request.getStatus());
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow();
                String className = maClass.getClassName();
                Sheet sheet = workbook.createSheet(className);

                for (int i = 0; i < 4; i++) {
                    Row headerRow = sheet.createRow(i);
                    for (int col = 0; col < columns.length; col++) {
                        Cell cell = headerRow.createCell(col);
                        if (col == 0 && i == 0) {
                            cell.setCellValue("BẢNG KÊ ĐIỂM DANH LỚP");
                            CellStyle cellStyle = workbook.createCellStyle();
                            Font cellFont = workbook.createFont();
                            cellFont.setFontHeightInPoints((short) 18);
                            cellFont.setBold(true);
                            cellFont.setColor(IndexedColors.RED.getIndex());

                            cellStyle.setFont(cellFont);
                            cell.setCellStyle(cellStyle);
                        } else if (col == 0 && i == 1) {
                            cell.setCellValue("Trường: " + schoolName);
                            CellStyle twoStyle = workbook.createCellStyle();
                            Font cellFont = workbook.createFont();
                            cellFont.setBold(true);
                            twoStyle.setFont(cellFont);
                            cell.setCellStyle(twoStyle);
                        } else if (col == 0 && i == 2) {
                            cell.setCellValue("Lớp: " + className);
                            CellStyle threeStyle = workbook.createCellStyle();
                            Font cellFont = workbook.createFont();
                            cellFont.setFontHeightInPoints((short) 11);
                            cellFont.setBold(true);
                            threeStyle.setFont(cellFont);
                            cell.setCellStyle(threeStyle);
                        } else if (col == 0 && i == 3) {
                            cell.setCellValue("Ngày: " + dateToStr + " - " + dateToStrSheet);
                            CellStyle threeStyle = workbook.createCellStyle();
                            Font cellFont = workbook.createFont();
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
                Font headerFont = workbook.createFont();
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
                Font contentFont = workbook.createFont();
                contentFont.setColor(IndexedColors.BLACK.getIndex());
                contentCellStyle.setFont(contentFont);
                contentCellStyle.setWrapText(true);
                contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentCellStyle.setAlignment(HorizontalAlignment.CENTER);
                contentCellStyle.setBorderBottom(BorderStyle.THIN);
                contentCellStyle.setBorderTop(BorderStyle.THIN);
                contentCellStyle.setBorderRight(BorderStyle.THIN);
                contentCellStyle.setBorderLeft(BorderStyle.THIN);
                //Style content  status
                CellStyle contentStatusStyle = workbook.createCellStyle();
                Font contentStatusFont = workbook.createFont();
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
                Font contentLessonFont = workbook.createFont();
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
                Font contentLateFontDay = workbook.createFont();
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
                Font contentMealFont = workbook.createFont();
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
                Font contentLateFont = workbook.createFont();
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
                for (int col = 0; col < 22; col++) {
                    Cell cell = headerRow4.createCell(col);
                    CellStyle cellHeaderRow4 = workbook.createCellStyle();
                    cellHeaderRow4.setBorderBottom(BorderStyle.THIN);
                    cellHeaderRow4.setBorderTop(BorderStyle.THIN);
                    cellHeaderRow4.setBorderRight(BorderStyle.THIN);
                    cellHeaderRow4.setBorderLeft(BorderStyle.THIN);
                    cell.setCellStyle(cellHeaderRow4);
                }


                Cell cellAttendance = headerRow4.createCell(0);
                cellAttendance.setCellValue("ĐIỂM DANH");
                CellStyle cellAttendanceStyle = workbook.createCellStyle();
                Font headerAttendance = workbook.createFont();
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
                cellStatus.setCellValue("HỌC CẢ NGÀY");
                CellStyle cellStatusStyle = workbook.createCellStyle();
                Font headerStatus = workbook.createFont();
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
                cellLesson.setCellValue("BUỔI HỌC");


                CellStyle cellLessonStyle = workbook.createCellStyle();
                Font headerLesson = workbook.createFont();
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
                Font headerLateDay = workbook.createFont();
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
                Font headerMeal = workbook.createFont();
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
                sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 15, 20));


                Cell cellLate = headerRow4.createCell(21);
                cellLate.setCellValue("ĐÓN MUỘN");
                CellStyle cellLateStyle = workbook.createCellStyle();
                Font headerLate = workbook.createFont();
                headerLate.setBold(true);
                cellLateStyle.setFont(headerLate);
                ((XSSFCellStyle) cellLateStyle).setFillForegroundColor(yellowThree);
                cellLateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellLateStyle.setAlignment(HorizontalAlignment.CENTER);
                cellLateStyle.setBorderBottom(BorderStyle.THIN);
                cellLateStyle.setBorderTop(BorderStyle.THIN);
                cellLateStyle.setBorderRight(BorderStyle.THIN);
                cellLateStyle.setBorderLeft(BorderStyle.THIN);
                cellLate.setCellStyle(cellLateStyle);

                int rowParent = 5;
                Row headerRow5 = sheet.createRow(rowParent);
                // Header 4
                for (int col = 0; col < 22; col++) {
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
                cellLateStyle1.setAlignment(HorizontalAlignment.CENTER);
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
                cellLesson4.setCellValue("Đi học");
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

                Cell cellLesson10 = headerRow5.createCell(16);
                cellLesson10.setCellValue("Chỉ ăn phụ sáng");
                cellLesson10.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle9 = workbook.createCellStyle();
                cellLateStyle9.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 16, 16));

                Cell cellLesson11 = headerRow5.createCell(17);
                cellLesson11.setCellValue("Chỉ ăn trưa");
                cellLesson11.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle10 = workbook.createCellStyle();
                cellLateStyle10.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 17, 17));

                Cell cellLesson12 = headerRow5.createCell(18);
                cellLesson12.setCellValue("Chỉ ăn chiều ");
                cellLesson12.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle11 = workbook.createCellStyle();
                cellLateStyle11.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 18, 18));

                Cell cellLesson13 = headerRow5.createCell(19);
                cellLesson13.setCellValue("Chỉ ăn phụ chiều");
                cellLesson13.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle12 = workbook.createCellStyle();
                cellLateStyle12.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 19, 19));

                Cell cellLesson14 = headerRow5.createCell(20);
                cellLesson14.setCellValue("Chỉ ăn tối");
                cellLesson14.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle13 = workbook.createCellStyle();
                cellLateStyle13.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 20, 20));

                Cell cellLesson17 = headerRow5.createCell(21);
                cellLesson17.setCellValue("Phút");
                cellLesson17.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle16 = workbook.createCellStyle();
                cellLateStyle16.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 21, 21));

                Row headerRow6 = sheet.createRow(rowChild);

                for (int col = 0; col < 22; col++) {
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
                int getEatMorningSecond = 0;

                int getEatNoon = 0;
                int getEatAfternoon = 0;
                int getEatAfternoonSecond = 0;
                int geteatEvening = 0;
                int getMinutesLate = 0;

                if (kidsList.size() == 0) {
                    throw new NotFoundException("Lớp không có học sinh nào.");
                } else {
                    for (Kids x : kidsList) {
                        AttendanceKidsStatisticalResponse fillData = attendanceKidsStatisticalService.attendanceKidsStatistical(x, dateStart, dateEnd);
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
                        getEatMorningSecond = fillData.getEatMorningSecond() + getEatMorningSecond;
                        getEatNoon = fillData.getEatNoon() + getEatNoon;
                        getEatAfternoon = fillData.getEatAfternoon() + getEatAfternoon;
                        getEatAfternoonSecond = fillData.getEatAfternoonSecond() + getEatAfternoonSecond;
                        geteatEvening = fillData.getEatEvening() + geteatEvening;
                        getMinutesLate = fillData.getMinutesLate() + getMinutesLate;


                        Row row = sheet.createRow(rowIdx++);

                        Cell cellId = row.createCell(0);
                        cellId.setCellValue(i);
                        cellId.setCellStyle(contentCellStyle);

                        Cell cellKidName = row.createCell(1);
                        cellKidName.setCellValue(x.getFullName());
                        cellKidName.setCellStyle(contentCellStyle);

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
                        cellEatSecondBreakfast.setCellValue(fillData.getEatMorningSecond());
                        cellEatSecondBreakfast.setCellStyle(contentMealStyle);

                        Cell celleatLunch = row.createCell(17);
                        celleatLunch.setCellValue(fillData.getEatNoon());
                        celleatLunch.setCellStyle(contentMealStyle);

                        Cell celleatAfternoon = row.createCell(18);
                        celleatAfternoon.setCellValue(fillData.getEatAfternoon());
                        celleatAfternoon.setCellStyle(contentMealStyle);

                        Cell celleatSecondAfternoon = row.createCell(19);
                        celleatSecondAfternoon.setCellValue(fillData.getEatAfternoonSecond());
                        celleatSecondAfternoon.setCellStyle(contentMealStyle);

                        Cell celleatDinner = row.createCell(20);
                        celleatDinner.setCellValue(fillData.getEatEvening());
                        celleatDinner.setCellStyle(contentMealStyle);

                        Cell cellminutePickupLate = row.createCell(21);
                        cellminutePickupLate.setCellValue(fillData.getMinutesLate());
                        cellminutePickupLate.setCellStyle(contentLateStyle);


                    }

                    int rowEnd = 7 + kidsList.size();
                    Row headerRowEnd = sheet.createRow(rowEnd);

                    Cell cellAttendanceTotal = headerRowEnd.createCell(0);
                    cellAttendanceTotal.setCellValue("Tổng cộng");
                    CellStyle cellAttendanceStyleTotal = workbook.createCellStyle();
                    Font headerAttendanceTotal = workbook.createFont();
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
                    cell16.setCellValue(getEatMorningSecond);

                    Cell cell17 = headerRowEnd.createCell(17);
                    cell17.setCellStyle(cellAttendanceStyleTotal);
                    cell17.setCellValue(getEatNoon);

                    Cell cell18 = headerRowEnd.createCell(18);
                    cell18.setCellStyle(cellAttendanceStyleTotal);
                    cell18.setCellValue(getEatAfternoon);

                    Cell cell19 = headerRowEnd.createCell(19);
                    cell19.setCellStyle(cellAttendanceStyleTotal);
                    cell19.setCellValue(getEatAfternoonSecond);

                    Cell cell20 = headerRowEnd.createCell(20);
                    cell20.setCellStyle(cellAttendanceStyleTotal);
                    cell20.setCellValue(geteatEvening);

                    Cell cell21 = headerRowEnd.createCell(21);
                    cell21.setCellStyle(cellAttendanceStyleTotal);
                    cell21.setCellValue(getMinutesLate);

                }

            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }

    }

    @Override
    public List<ExcelNewResponse> exportAttendanceKidCustomNew(UserPrincipal principal, AttendanceKidsSearchCustomRequest request) {
        School school = schoolRepository.findByIdAndDelActiveTrue(principal.getIdSchoolLogin()).orElseThrow();
        String schoolName = school.getSchoolName();
        // xuất học sinh theo trường
        List<ExcelNewResponse> responseList = new ArrayList<>();
        LocalDate dateStart = request.getDateStartEnd().get(0);
        LocalDate dateEnd = request.getDateStartEnd().get(1);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateToStr = df.format(dateStart);
        String dateToStrSheet = df.format(dateEnd);

        if (request.getIdClass() == null) {
            List<Long> idClassList = maClassRepository.findIdClassInSchool(principal.getIdSchoolLogin());
            idClassList.forEach(x -> {
                List<ExcelDataNew> bodyList = new ArrayList<>();
                ExcelNewResponse response = new ExcelNewResponse();
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(x).orElseThrow();
                List<String> headerStringList = Arrays.asList("BẢNG KÊ ĐIỂM DANH", AppConstant.EXCEL_SCHOOL.concat(schoolName), "Toàn trường", AppConstant.EXCEL_DATE.concat(dateToStr.concat(" - ").concat(dateToStrSheet)));
                List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
                ExcelDataNew headerMulti = this.setHeaderMulti();
                ExcelDataNew headerMulti1 = this.setHeaderMulti1();
                headerList.add(headerMulti);
                headerList.add(headerMulti1);
                response.setHeaderList(headerList);
                response.setSheetName(maClass.getClassName());
                this.setDataExcel(bodyList, x, dateStart, dateEnd, request.getStatus());
                response.setBodyList(bodyList);
                responseList.add(response);
            });
            return responseList;
        } else {
            List<ExcelDataNew> bodyList = new ArrayList<>();
            Long idClass = request.getIdClass();
            ExcelNewResponse response = new ExcelNewResponse();
            MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow();
            List<String> headerStringList = Arrays.asList("BẢNG KÊ ĐIỂM DANH LỚP", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_DATE.concat(dateToStr.concat(" - ").concat(dateToStrSheet)));
            List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
            response.setSheetName(maClass.getClassName());
            ExcelDataNew headerMulti = this.setHeaderMulti();
            ExcelDataNew headerMulti1 = this.setHeaderMulti1();
            headerList.add(headerMulti);
            headerList.add(headerMulti1);
            response.setHeaderList(headerList);
            this.setDataExcel(bodyList, idClass, dateStart, dateEnd, request.getStatus());
            response.setBodyList(bodyList);
            responseList.add(response);
            return responseList;
        }
    }

    @Override
    public ExcelDynamicResponse excelExportAttendanceKidsDetailService(AttendanceKidsSearchDetailRequest request) {
        ExcelDynamicResponse response = new ExcelDynamicResponse();
        List<ExcelNewResponse> dataList = new ArrayList<>();
        String kidStatus = request.getStatus();
//        LocalDate date = request.getDate();
        List<LocalDate> dateList = request.getDateStartEnd();
        LocalDate startDate = dateList.get(0);
        LocalDate endDate = dateList.get(1);
        int dayNumberOfMonth = ConvertData.convertDateToDay(startDate, endDate);
        if (dayNumberOfMonth > 31) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khoảng cách giữa 2 ngày không được lớn hơn 31 ngày");
        }
//        int month = date.getMonthValue();
//        int year = date.getYear();
        Long idSchool = SchoolUtils.getIdSchool();
        School school = schoolRepository.findById(idSchool).orElseThrow();
        String monthYearString = ConvertData.convertTwoDate(startDate, endDate);
        response.setFileName("DIEM_DANH_CHI_TIET_" + monthYearString);
        List<MaClass> maClassList = new ArrayList<>();
        if (Objects.nonNull(request.getIdClass())) {
            MaClass maClass = maClassRepository.findById(request.getIdClass()).orElseThrow();
            maClassList.add(maClass);
        } else {
            List<MaClass> maClasses = maClassRepository.findByIdSchoolAndDelActiveTrue(idSchool);
            maClassList.addAll(maClasses);
        }
        List<String> titleHeaderList = new ArrayList<>();
        List<Integer> sizeColumn = new ArrayList<>();
        titleHeaderList.add("STT");
        titleHeaderList.add("Họ và tên");
        titleHeaderList.add("Ngày sinh");
        titleHeaderList.add("Trạng thái");
        sizeColumn.add(5);
        sizeColumn.add(20);
        sizeColumn.add(12);
        sizeColumn.add(12);
        LocalDate dateFor = dateList.get(0);
        for (int i = 0; i <= dayNumberOfMonth; i++) {
//            LocalDate dateCal = LocalDate.of(dateFor.getYear(), dateFor.getMonthValue(), i+1);
            DayOfWeek dayOfWeek = dateFor.getDayOfWeek();
            String dayOfWeekString;
            String dateMonth = ConvertData.convertDateToStringOne(dateFor);
            if (dayOfWeek == DayOfWeek.MONDAY) {
                dayOfWeekString = "T2";
            } else if (dayOfWeek == DayOfWeek.TUESDAY) {
                dayOfWeekString = "T3";
            } else if (dayOfWeek == DayOfWeek.WEDNESDAY) {
                dayOfWeekString = "T4";
            } else if (dayOfWeek == DayOfWeek.THURSDAY) {
                dayOfWeekString = "T5";
            } else if (dayOfWeek == DayOfWeek.FRIDAY) {
                dayOfWeekString = "T6";
            } else if (dayOfWeek == DayOfWeek.SATURDAY) {
                dayOfWeekString = "T7";
            } else {
                dayOfWeekString = "CN";
            }
            titleHeaderList.add(dateMonth + " (" + dayOfWeekString + ")");
            sizeColumn.add(10);
            dateFor = dateFor.plusDays(1);
        }
        titleHeaderList.add("Đi học (T2-T6)");
        titleHeaderList.add("Đi học (T7)");
        titleHeaderList.add("Đi học (CN)");
        titleHeaderList.add("Tổng đi học");
        titleHeaderList.add("Nghỉ học (T2-T6)");
        titleHeaderList.add("Nghỉ học (T7)");
        titleHeaderList.add("Nghỉ học (CN)");
        titleHeaderList.add("Tổng nghỉ học");
        sizeColumn.add(15);
        sizeColumn.add(15);
        sizeColumn.add(15);
        sizeColumn.add(15);
        sizeColumn.add(15);
        sizeColumn.add(15);
        sizeColumn.add(15);
        sizeColumn.add(15);

        response.setTitleHeaderList(titleHeaderList);
        List<String> proList = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(1);
        titleHeaderList.forEach(x -> {
            proList.add("pro" + count.getAndIncrement());
        });
        response.setProList(proList);
        response.setSizeColumnList(sizeColumn);
        int sizeHeader = titleHeaderList.size() - 4;

        for (MaClass maClass : maClassList) {
            List<ExcelDataNew> bodyList = new ArrayList<>();
            ExcelNewResponse data = new ExcelNewResponse();
            data.setSheetName(maClass.getClassName());
            List<String> headerStringList = Arrays.asList("BẢNG KÊ ĐIỂM DANH LỚP", "Trường: " + school.getSchoolName(), "Lớp: " + maClass.getClassName(), "Tháng: " + monthYearString);
            List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
            List<Kids> kidsList = kidsRepository.findByKidsClassWithStatusNameExcel(maClass.getId(), kidStatus);
            List<Long> longList = new ArrayList<>();
            for (int i = 0; i < sizeHeader; i++) {
                longList.add(0L);
            }
            int i = 1;
            for (Kids x : kidsList) {
                int learn26 = 0;
                int learn7 = 0;
                int learn8 = 0;
                int absent26 = 0;
                int absent7 = 0;
                int absent8 = 0;
                List<String> bodyStringList = new ArrayList<>();
                bodyStringList.add(String.valueOf(i));
                bodyStringList.add(x.getFullName());
                bodyStringList.add(ConvertData.convertLocalDateToString(x.getBirthDay()));
                bodyStringList.add(StudentUtil.getConvertKidStatus(x.getKidStatus()));
                LocalDate dateForIn = dateList.get(0);
                for (int j = 0; j <= dayNumberOfMonth; j++) {
//                    LocalDate dateCal = LocalDate.of(year, month, j);
                    DayOfWeek dayOfWeek = dateForIn.getDayOfWeek();
                    LocalDate finalDateForIn = dateForIn;
                    List<AttendanceKids> attendanceKids = x.getAttendanceKidsList().stream().filter(a -> a.getAttendanceDate().isEqual(finalDateForIn)).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(attendanceKids)) {
                        AttendanceArriveKids arriveKids = attendanceKids.get(0).getAttendanceArriveKids();
                        long resultNumber = 0;
                        String resultString;
                        if (AttendanceKidsUtil.checkArrive(arriveKids)) {
                            resultNumber = 1;
                            resultString = "1";
                            if (dayOfWeek == DayOfWeek.SATURDAY) {
                                learn7++;
                            } else if (dayOfWeek == DayOfWeek.SUNDAY) {
                                learn8++;
                            } else {
                                learn26++;
                            }
                        } else if (AttendanceKidsUtil.checkArriveYes(arriveKids) || AttendanceKidsUtil.checkArriveNo(arriveKids)) {
                            resultString = "0";
                            if (dayOfWeek == DayOfWeek.SATURDAY) {
                                absent7++;
                            } else if (dayOfWeek == DayOfWeek.SUNDAY) {
                                absent8++;
                            } else {
                                absent26++;
                            }
                        } else {
                            resultString = "Chưa ĐD";
                        }
                        bodyStringList.add(resultString);
                        longList.set(j, longList.get(j) + resultNumber);
                    } else {
                        //ko có dữ liệu điểm danh
                        bodyStringList.add("");
                    }
                    dateForIn = dateForIn.plusDays(1);
                }
                int learnTotal = learn26 + learn7 + learn8;
                int absentTotal = absent26 + absent7 + absent8;
                bodyStringList.add(String.valueOf(learn26));
                bodyStringList.add(String.valueOf(learn7));
                bodyStringList.add(String.valueOf(learn8));
                bodyStringList.add(String.valueOf(learnTotal));
                bodyStringList.add(String.valueOf(absent26));
                bodyStringList.add(String.valueOf(absent7));
                bodyStringList.add(String.valueOf(absent8));
                bodyStringList.add(String.valueOf(absentTotal));

                int countLong = dayNumberOfMonth;
                longList.set(++countLong, longList.get(countLong) + learn26);
                longList.set(++countLong, longList.get(countLong) + learn7);
                longList.set(++countLong, longList.get(countLong) + learn8);
                longList.set(++countLong, longList.get(countLong) + learnTotal);
                longList.set(++countLong, longList.get(countLong) + absent26);
                longList.set(++countLong, longList.get(countLong) + absent7);
                longList.set(++countLong, longList.get(countLong) + absent8);
                longList.set(++countLong, longList.get(countLong) + absentTotal);
                ExcelDataNew bodyData = ExportExcelUtils.setBodyExcelNew(bodyStringList);
                bodyList.add(bodyData);
                i++;
            }

            List<String> bodyStringFinalList = new ArrayList<>();
            bodyStringFinalList.add("Tổng");
            bodyStringFinalList.add("");
            bodyStringFinalList.add("");
            bodyStringFinalList.add("");
            longList.forEach(y -> bodyStringFinalList.add(FinanceUltils.formatMoney(y)));
            ExcelDataNew bodyData = ExportExcelUtils.setBodyExcelNew(bodyStringFinalList);
            bodyList.add(bodyData);

            data.setHeaderList(headerList);
            data.setBodyList(bodyList);
            dataList.add(data);
            response.setDataList(dataList);
        }
        return response;
    }

    private void setDataExcel(List<ExcelDataNew> bodyList, Long idClass, LocalDate dateStart, LocalDate dateEnd, String status) {
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
        int getEatMorningSecond = 0;

        int getEatNoon = 0;
        int getEatAfternoon = 0;
        int getEatAfternoonSecond = 0;
        int getEatEvening = 0;
        int getMinutesLate = 0;
        List<Kids> kidsList = kidsRepository.findByKidsClassWithStatusNameExcel(idClass, status);
        for (Kids x : kidsList) {

            AttendanceKidsStatisticalResponse fillData = attendanceKidsStatisticalService.attendanceKidsStatistical(x, dateStart, dateEnd);
            i++;

            getAllDayYesTotal += fillData.getAllDayYes();
            getAllDay += fillData.getAllDay();
            getAllDayNo += fillData.getAllDayNo();

            getMorningYes += fillData.getMorningYes();
            getMorningNo += fillData.getMorningNo();
            getMorning += fillData.getMorning();
            getAfternoonYes += fillData.getAfternoonYes();
            getAfternoonNo += fillData.getAfternoonNo();
            getAfternoon += fillData.getAfternoon();
            getEveningYes += fillData.getEveningYes();
            getEveningNo += fillData.getEveningNo();
            getEvening += fillData.getEvening();
            getEatAllDay += fillData.getEatAllDay();
            getEatMorning += fillData.getEatMorning();
            getEatMorningSecond += fillData.getEatMorningSecond();
            getEatNoon += fillData.getEatNoon();
            getEatAfternoon += fillData.getEatAfternoon();
            getEatAfternoonSecond += fillData.getEatAfternoonSecond();
            getEatEvening += fillData.getEatEvening();
            getMinutesLate += fillData.getMinutesLate();
            List<String> bodyStringList = Arrays.asList(String.valueOf(i), x.getFullName(), String.valueOf(fillData.getAllDayYes()), String.valueOf(fillData.getAllDayNo()), String.valueOf(fillData.getAllDay()), String.valueOf(fillData.getMorningYes()), String.valueOf(fillData.getMorningNo()), String.valueOf(fillData.getMorning()),
                    String.valueOf(fillData.getAfternoonYes()), String.valueOf(fillData.getAfternoonNo()), String.valueOf(fillData.getAfternoon()), String.valueOf(fillData.getEveningYes()), String.valueOf(fillData.getEveningNo()), String.valueOf(fillData.getEvening()), String.valueOf(fillData.getEatAllDay()), String.valueOf(fillData.getEatMorning()), String.valueOf(fillData.getEatMorningSecond()),
                    String.valueOf(fillData.getEatNoon()), String.valueOf(fillData.getEatAfternoon()), String.valueOf(fillData.getEatAfternoonSecond()), String.valueOf(fillData.getEatEvening()), String.valueOf(fillData.getMinutesLate()));
            ExcelDataNew modelData = ExportExcelUtils.setBodyExcelNew(bodyStringList);

            bodyList.add(modelData);
        }
        List<String> bodyStringList = Arrays.asList("Tổng cộng", "", String.valueOf(getAllDayYesTotal), String.valueOf(getAllDayNo), String.valueOf(getAllDay), String.valueOf(getMorningYes), String.valueOf(getMorningNo), String.valueOf(getMorning),
                String.valueOf(getAfternoonYes), String.valueOf(getAfternoonNo), String.valueOf(getAfternoon), String.valueOf(getEveningYes), String.valueOf(getEveningNo), String.valueOf(getEvening), String.valueOf(getEatAllDay), String.valueOf(getEatMorning), String.valueOf(getEatMorningSecond),
                String.valueOf(getEatNoon), String.valueOf(getEatAfternoon), String.valueOf(getEatAfternoonSecond), String.valueOf(getEatEvening), String.valueOf(getMinutesLate));
        ExcelDataNew bodyTotal = ExportExcelUtils.setBodyExcelNew(bodyStringList);
        bodyList.add(bodyTotal);
    }

    private ExcelDataNew setHeaderMulti() {
        ExcelDataNew headerMulti = new ExcelDataNew();
        headerMulti.setPro1("ĐIỂM DANH");
        headerMulti.setPro2("");
        headerMulti.setPro3("HỌC CẢ NGÀY");
        headerMulti.setPro4("");
        headerMulti.setPro5("");
        headerMulti.setPro6("BUỔI HỌC");
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
        headerMulti.setPro19("");
        headerMulti.setPro20("");
        headerMulti.setPro21("");
        headerMulti.setPro22("ĐÓN MUỘN");
        return headerMulti;
    }

    private ExcelDataNew setHeaderMulti1() {
        ExcelDataNew headerMulti = new ExcelDataNew();
        headerMulti.setPro1("STT");
        headerMulti.setPro2("Họ và tên");
        headerMulti.setPro3("Nghỉ có phép");
        headerMulti.setPro4("Nghỉ không phép");
        headerMulti.setPro5("Đi học");
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
        headerMulti.setPro17("Chỉ ăn phụ sáng");
        headerMulti.setPro18("Chỉ ăn trưa");
        headerMulti.setPro19("Chỉ ăn chiều");
        headerMulti.setPro20("Chỉ ăn phụ chiều");
        headerMulti.setPro21("Chỉ ăn tối");
        headerMulti.setPro22("Phút");
        return headerMulti;
    }
}
