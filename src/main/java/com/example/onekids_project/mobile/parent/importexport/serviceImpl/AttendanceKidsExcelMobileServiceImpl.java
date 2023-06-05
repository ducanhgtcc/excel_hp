package com.example.onekids_project.mobile.parent.importexport.serviceImpl;

import com.example.onekids_project.mobile.parent.importexport.model.AttendanceKidsExModel;
import com.example.onekids_project.mobile.parent.importexport.service.AttendanceKidsExcelMobileService;
import com.example.onekids_project.mobile.parent.response.kids.KidsParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.KidsParentService;
import com.example.onekids_project.security.model.UserPrincipal;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AttendanceKidsExcelMobileServiceImpl implements AttendanceKidsExcelMobileService {


    @Autowired
    private KidsParentService kidsParentService;

    @Override
    public ByteArrayInputStream attendanceToExcelMonth(UserPrincipal principal, List<AttendanceKidsExModel> attendanceKidsExModel, LocalDate date) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(131, 164, 196));
        XSSFColor greenOne = new XSSFColor(new java.awt.Color(149, 203, 74));
        XSSFColor yellowTwo = new XSSFColor(new java.awt.Color(217, 210, 144));
        XSSFColor blueTwo = new XSSFColor(new java.awt.Color(51, 153, 255));
        XSSFColor yellowThree = new XSSFColor(new java.awt.Color(224, 191, 28));

        int[] widths = {5, 17, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 10, 10, 15};
        int month = date.getMonthValue();
        int year = date.getYear();
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1).minusDays(1);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dateToStr = df.format(dateStart);
        String dateToStrSheet = df.format(dateEnd);
        KidsParentResponse infoKid = kidsParentService.findKidById(principal);
        String nameKid = infoKid.getFullName();
        String className = infoKid.getClassName();
        String[] columns = {"STT", "Ngày", "Nghỉ có phép", "Nghỉ không phép", "Đi học", "Có phép ", "Không phép", "Đi học", "Có phép ", "Không phép", "Đi học", "Có phép ", "Không phép", "Đi học", "Đến", "Về", "Phút"};


        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {

            Sheet sheet = workbook.createSheet(nameKid);

//                sheet.setDisplayGridlines(false);
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
                        cell.setCellValue("Học sinh: " + nameKid);
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
            sheet.createFreezePane(0, 7);

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
            for (int col = 0; col < 17; col++) {
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

            Cell cellShuttle = headerRow4.createCell(14);
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
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 14, 15));

            Cell cellLate = headerRow4.createCell(16);
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
            for (int col = 0; col < 17; col++) {
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


            Cell cellLesson15 = headerRow5.createCell(14);
            cellLesson15.setCellValue("Đến");
            cellLesson15.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle14 = workbook.createCellStyle();
            cellLateStyle14.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 14, 14));

            Cell cellLesson16 = headerRow5.createCell(15);
            cellLesson16.setCellValue("Về");
            cellLesson16.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle15 = workbook.createCellStyle();
            cellLateStyle15.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 15, 15));

            Cell cellLesson17 = headerRow5.createCell(16);
            cellLesson17.setCellValue("Phút");
            cellLesson17.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle16 = workbook.createCellStyle();
            cellLateStyle16.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 16, 16));


            Row headerRow6 = sheet.createRow(rowChild);

// Header
            for (int col = 0; col < 17; col++) {

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
            for (AttendanceKidsExModel attendanceKidsModel : attendanceKidsExModel) {

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


                Cell celltimeArriveKid = row.createCell(14);
                celltimeArriveKid.setCellValue(attendanceKidsModel.getTimeArriveKid());
                celltimeArriveKid.setCellStyle(contentShuttleStyle);

                Cell celltimeLeaveKid = row.createCell(15);
                celltimeLeaveKid.setCellValue(attendanceKidsModel.getTimeLeaveKid());
                celltimeLeaveKid.setCellStyle(contentShuttleStyle);

                Cell cellminutePickupLate = row.createCell(16);
                cellminutePickupLate.setCellValue(attendanceKidsModel.getMinutePickupLate());
                cellminutePickupLate.setCellStyle(contentLateStyle);

            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());


        }
    }

    @Override
    public ByteArrayInputStream attendanceEatToExcelMonth(UserPrincipal principal, List<AttendanceKidsExModel> attendanceKidsExModel, LocalDate date) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(131, 164, 196));
        XSSFColor greenOne = new XSSFColor(new java.awt.Color(149, 203, 74));
        XSSFColor yellowTwo = new XSSFColor(new java.awt.Color(217, 210, 144));
        XSSFColor blueTwo = new XSSFColor(new java.awt.Color(51, 153, 255));
        XSSFColor yellowThree = new XSSFColor(new java.awt.Color(224, 191, 28));

        int[] widths = {5, 23, 7, 7, 7, 7, 7, 7};
        int month = date.getMonthValue();
        int year = date.getYear();
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1).minusDays(1);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dateToStr = df.format(dateStart);
        String dateToStrSheet = df.format(dateEnd);
        KidsParentResponse infoKid = kidsParentService.findKidById(principal);
        String nameKid = infoKid.getFullName();
        String className = infoKid.getClassName();
        String[] columns = {"STT", "Ngày", "Sáng", "Phụ sáng", "Trưa", "Chiều", "Phụ Chiều", "Tối"};


        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {

            Sheet sheet = workbook.createSheet(nameKid);

//                sheet.setDisplayGridlines(false);
            for (int i = 0; i < 4; i++) {
                Row headerRow = sheet.createRow(i);
                for (int col = 0; col < columns.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    if (col == 0 && i == 0) {
                        cell.setCellValue("BẢNG KÊ ĐIỂM DANH ĂN NGÀY TRONG THÁNG");
                        CellStyle cellStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 18);
                        cellFont.setBold(true);
                        cellFont.setColor(IndexedColors.RED.getIndex());

                        cellStyle.setFont(cellFont);
                        cell.setCellStyle(cellStyle);
                    } else if (col == 0 && i == 1) {
                        cell.setCellValue("Học sinh: " + nameKid);
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
                        cell.setCellValue("Ngày: " + dateToStr + "-" + dateToStrSheet);
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
//                sheet.createFreezePane(2, 5);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columns.length));

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
            for (int col = 0; col < columns.length; col++) {
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


            Cell cellMeal = headerRow4.createCell(2);
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
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 2, 7));


            // Row for Header
            Row headerRow = sheet.createRow(5);

            // Header
            for (int col = 0; col < columns.length; col++) {
                sheet.setColumnWidth(col, widths[col] * 256);
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(headerKidsCellStyle);
            }
            int rowIdx = 6;
            for (AttendanceKidsExModel model : attendanceKidsExModel) {

                Row row = sheet.createRow(rowIdx++);

                Cell cellId = row.createCell(0);
                cellId.setCellValue(model.getId());
                cellId.setCellStyle(contentCellStyle);

                Cell cellAttendanceDate = row.createCell(1);
                cellAttendanceDate.setCellValue(model.getAttendanceDate());
                cellAttendanceDate.setCellStyle(contentCellStyle);

                Cell cellEatBreakfast = row.createCell(2);
                cellEatBreakfast.setCellValue(model.getEatBreakfast());
                cellEatBreakfast.setCellStyle(contentMealStyle);

                Cell cellEatSecondBreakfast = row.createCell(3);
                cellEatSecondBreakfast.setCellValue(model.getEatSecondBreakfast());
                cellEatSecondBreakfast.setCellStyle(contentMealStyle);

                Cell celleatLunch = row.createCell(4);
                celleatLunch.setCellValue(model.getEatLunch());
                celleatLunch.setCellStyle(contentMealStyle);

                Cell celleatAfternoon = row.createCell(5);
                celleatAfternoon.setCellValue(model.getAfternoon());
                celleatAfternoon.setCellStyle(contentMealStyle);

                Cell celleatSecondAfternoon = row.createCell(6);
                celleatSecondAfternoon.setCellValue(model.getEatSecondAfternoon());
                celleatSecondAfternoon.setCellStyle(contentMealStyle);

                Cell celleatDinner = row.createCell(7);
                celleatDinner.setCellValue(model.getEatDinner());
                celleatDinner.setCellStyle(contentMealStyle);


            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        }
    }
}



