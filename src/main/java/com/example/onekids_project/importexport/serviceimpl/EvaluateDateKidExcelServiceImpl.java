package com.example.onekids_project.importexport.serviceimpl;

import com.example.onekids_project.dto.KidsDTO;
import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.dto.SchoolDTO;
import com.example.onekids_project.importexport.model.EvaluateDateKidModel;
import com.example.onekids_project.importexport.service.EvaluateDateKidExcelService;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.service.servicecustom.KidsService;
import com.example.onekids_project.service.servicecustom.MaClassService;
import com.example.onekids_project.service.servicecustom.SchoolService;
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
import java.util.Map;

@Service
public class EvaluateDateKidExcelServiceImpl implements EvaluateDateKidExcelService {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private KidsService kidsService;

    @Autowired
    private MaClassService maClassService;

    /**
     * Xuất file excel đánh giá học sinh theo ngày
     *
     * @param evaluateDateKidModels
     * @param idSchool
     * @param idClass
     * @param currentDate
     * @return
     * @throws IOException
     */
    @Override
    public ByteArrayInputStream evaluateDateToExcel(List<EvaluateDateKidModel> evaluateDateKidModels, Long idSchool, Long idClass, LocalDate currentDate) throws IOException {

        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(131, 164, 196));
        XSSFColor yellowTwo = new XSSFColor(new java.awt.Color(217, 210, 144));

        int[] widths = {5, 23, 15, 23, 23, 23, 23, 23, 23};
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String dateToStr = df.format(currentDate);
        String dateToStrSheet = df1.format(currentDate);
        String[] columns = {"STT", "Họ và tên", "Duyệt", "Học tập", "Ăn uống", "Ngủ nghỉ", "Vệ sinh", "Sức khỏe", "Nhận xét chung"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            SchoolResponse school = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
            String schoolName = "";
            if (school != null) {
                schoolName = school.getSchoolName();
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
                        cell.setCellValue("BẢNG THỐNG KÊ NHẬN XÉT NGÀY");
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
            sheet.createFreezePane(2, 5);

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

            //Style content  content
            CellStyle contentCStyle = workbook.createCellStyle();
            Font contentMealFont = workbook.createFont();
            contentMealFont.setColor(IndexedColors.BLACK.getIndex());
            contentCStyle.setFont(contentMealFont);
            contentCStyle.setWrapText(true);
            ((XSSFCellStyle) contentCStyle).setFillForegroundColor(yellowTwo);
            contentCStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            contentCStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentCStyle.setAlignment(HorizontalAlignment.CENTER);
            contentCStyle.setBorderBottom(BorderStyle.THIN);
            contentCStyle.setBorderTop(BorderStyle.THIN);
            contentCStyle.setBorderRight(BorderStyle.THIN);
            contentCStyle.setBorderLeft(BorderStyle.THIN);

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
            Cell cellEvualate = headerRow4.createCell(0);
            cellEvualate.setCellValue("NHẬN XÉT");
            CellStyle cellEvualateStyle = workbook.createCellStyle();
            Font headerEvualate = workbook.createFont();
            headerEvualate.setBold(true);
            headerEvualate.setColor(IndexedColors.RED.getIndex());
            cellEvualateStyle.setFont(headerEvualate);
            ((XSSFCellStyle) cellEvualateStyle).setFillForegroundColor(greyOne);
            cellEvualateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellEvualateStyle.setAlignment(HorizontalAlignment.CENTER);
            cellEvualateStyle.setBorderBottom(BorderStyle.THIN);
            cellEvualateStyle.setBorderTop(BorderStyle.THIN);
            cellEvualateStyle.setBorderRight(BorderStyle.THIN);
            cellEvualateStyle.setBorderLeft(BorderStyle.THIN);
            cellEvualate.setCellStyle(cellEvualateStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 0, 1));


            Cell cellStatus = headerRow4.createCell(2);
            cellStatus.setCellValue("");
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

            Cell cellContent = headerRow4.createCell(3);
            cellContent.setCellValue("NỘI DUNG");
            CellStyle cellContentStyle = workbook.createCellStyle();
            Font headerContent = workbook.createFont();
            headerContent.setBold(true);
            cellContentStyle.setFont(headerContent);
            ((XSSFCellStyle) cellContentStyle).setFillForegroundColor(yellowTwo);
            cellContentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellContentStyle.setAlignment(HorizontalAlignment.CENTER);
            cellContentStyle.setBorderBottom(BorderStyle.THIN);
            cellContentStyle.setBorderTop(BorderStyle.THIN);
            cellContentStyle.setBorderRight(BorderStyle.THIN);
            cellContentStyle.setBorderLeft(BorderStyle.THIN);
            cellContent.setCellStyle(cellContentStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 3, 8));

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
            for (EvaluateDateKidModel evaluateDateKidModel : evaluateDateKidModels) {

                Row row = sheet.createRow(rowIdx++);

                Cell cellId = row.createCell(0);
                cellId.setCellValue(evaluateDateKidModel.getId());
                cellId.setCellStyle(contentCellStyle);

                Cell cellKidName = row.createCell(1);
                cellKidName.setCellValue(evaluateDateKidModel.getKidName());
                cellKidName.setCellStyle(contentCellStyle);


                Cell cellEvaluateDate = row.createCell(2);
                cellEvaluateDate.setCellValue(evaluateDateKidModel.getIsApproved());
                cellEvaluateDate.setCellStyle(contentStatusStyle);


                Cell cellContentLearn = row.createCell(3);
                cellContentLearn.setCellValue(evaluateDateKidModel.getContentLearn());
                cellContentLearn.setCellStyle(contentCStyle);

                Cell cellContentEat = row.createCell(4);
                cellContentEat.setCellValue(evaluateDateKidModel.getContentEat());
                cellContentEat.setCellStyle(contentCStyle);

                Cell cellContentSleep = row.createCell(5);
                cellContentSleep.setCellValue(evaluateDateKidModel.getContentSleep());
                cellContentSleep.setCellStyle(contentCStyle);

                Cell cellContentSanitary = row.createCell(6);
                cellContentSanitary.setCellValue(evaluateDateKidModel.getContentSanitary());
                cellContentSanitary.setCellStyle(contentCStyle);

                Cell cellContentHealt = row.createCell(7);
                cellContentHealt.setCellValue(evaluateDateKidModel.getContentHealt());
                cellContentHealt.setCellStyle(contentCStyle);

                Cell cellContentCommon = row.createCell(8);
                cellContentCommon.setCellValue(evaluateDateKidModel.getContentCommon());
                cellContentCommon.setCellStyle(contentCStyle);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


    /**
     * Xuất file excel đánh giá theo tháng
     *
     * @param map
     * @param idSchool
     * @param idClass
     * @param date
     * @return
     * @throws IOException
     */
    @Override
    public ByteArrayInputStream evaluateDateToExcelMonth(Map<Long, List<EvaluateDateKidModel>> map, Long idSchool, Long idClass, LocalDate date) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(131, 164, 196));
        XSSFColor yellowTwo = new XSSFColor(new java.awt.Color(217, 210, 144));

        int[] widths = {5, 23, 15, 23, 23, 23, 23, 23, 23};
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int month = date.getMonthValue();
        int year = date.getYear();
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1).minusDays(1);

        String dateToStr = df.format(dateStart);
        String dateToStrSheet = df.format(dateEnd);

        String[] columns = {"STT", "Ngày", "Duyệt", "Học tập", "Ăn uống", "Ngủ nghỉ", "Vệ sinh", "Sức khỏe", "Nhận xét chung"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {

            for (Map.Entry<Long, List<EvaluateDateKidModel>> entry : map.entrySet()) {
                List<EvaluateDateKidModel> kidVMList = entry.getValue();
                Long idKid = entry.getKey();

                SchoolResponse school = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
                String schoolName = "";
                if (school != null) {
                    schoolName = school.getSchoolName();
                }
                KidsDTO kidsDTO = kidsService.findByIdKid(idSchool, idKid).stream().findFirst().orElse(null);
                String kidsName = "";
                if (kidsName != null) {
                    kidsName = kidsDTO.getFullName();
                }

                MaClassDTO classDTO = maClassService.findByIdMaClass(idSchool, idClass).stream().findFirst().orElse(null);
                String className = "";
                if (classDTO != null) {
                    className = classDTO.getClassName();
                }
                Sheet sheet = workbook.createSheet(kidsName + " - " + idKid);

                sheet.setDisplayGridlines(false);
                for (int i = 0; i < 5; i++) {
                    Row headerRow = sheet.createRow(i);
                    for (int col = 0; col < columns.length; col++) {
                        Cell cell = headerRow.createCell(col);
                        if (col == 0 && i == 0) {
                            cell.setCellValue("BẢNG THỐNG KÊ NHẬN XÉT THÁNG");
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
                            cell.setCellValue("Học sinh: " + kidsName);
                            CellStyle threeStyle = workbook.createCellStyle();
                            Font cellFont = workbook.createFont();
                            cellFont.setFontHeightInPoints((short) 11);
                            cellFont.setBold(true);
                            threeStyle.setFont(cellFont);
                            cell.setCellStyle(threeStyle);
                        }
                        else if (col == 0 && i == 4) {
                            cell.setCellValue("Thời gian: " + dateToStr + " - " + dateToStrSheet);
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

                // header row 6
                Font headerFont = workbook.createFont();
                headerFont.setFontHeightInPoints((short) 11);
                headerFont.setColor(IndexedColors.BLACK.getIndex());
                headerFont.setBold(true);

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

                //Style content  content
                CellStyle contentCStyle = workbook.createCellStyle();
                Font contentMealFont = workbook.createFont();
                contentMealFont.setColor(IndexedColors.BLACK.getIndex());
                contentCStyle.setFont(contentMealFont);
                contentCStyle.setWrapText(true);
                ((XSSFCellStyle) contentCStyle).setFillForegroundColor(yellowTwo);
                contentCStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentCStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentCStyle.setAlignment(HorizontalAlignment.CENTER);
                contentCStyle.setBorderBottom(BorderStyle.THIN);
                contentCStyle.setBorderTop(BorderStyle.THIN);
                contentCStyle.setBorderRight(BorderStyle.THIN);
                contentCStyle.setBorderLeft(BorderStyle.THIN);

                // Row for Header5
                int rowHeader = 5;
                Row headerRow5 = sheet.createRow(rowHeader);
                // Header 5
                for (int col = 0; col < columns.length; col++) {
                    Cell cell = headerRow5.createCell(col);
                    CellStyle cellHeaderRow4 = workbook.createCellStyle();
                    cellHeaderRow4.setBorderBottom(BorderStyle.THIN);
                    cellHeaderRow4.setBorderTop(BorderStyle.THIN);
                    cellHeaderRow4.setBorderRight(BorderStyle.THIN);
                    cellHeaderRow4.setBorderLeft(BorderStyle.THIN);
                    cell.setCellStyle(cellHeaderRow4);
                }

//            // Header
                Cell cellEvualate = headerRow5.createCell(0);
                cellEvualate.setCellValue("NHẬN XÉT");
                CellStyle cellEvualateStyle = workbook.createCellStyle();
                Font headerEvualate = workbook.createFont();
                headerEvualate.setBold(true);
                headerEvualate.setColor(IndexedColors.RED.getIndex());
                cellEvualateStyle.setFont(headerEvualate);
                ((XSSFCellStyle) cellEvualateStyle).setFillForegroundColor(greyOne);
                cellEvualateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellEvualateStyle.setAlignment(HorizontalAlignment.CENTER);
                cellEvualateStyle.setBorderBottom(BorderStyle.THIN);
                cellEvualateStyle.setBorderTop(BorderStyle.THIN);
                cellEvualateStyle.setBorderRight(BorderStyle.THIN);
                cellEvualateStyle.setBorderLeft(BorderStyle.THIN);
                cellEvualate.setCellStyle(cellEvualateStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 0, 1));


                Cell cellStatus = headerRow5.createCell(2);
                cellStatus.setCellValue("");
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

                Cell cellContent = headerRow5.createCell(3);
                cellContent.setCellValue("NỘI DUNG");
                CellStyle cellContentStyle = workbook.createCellStyle();
                Font headerContent = workbook.createFont();
                headerContent.setBold(true);
                cellContentStyle.setFont(headerContent);
                ((XSSFCellStyle) cellContentStyle).setFillForegroundColor(yellowTwo);
                cellContentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellContentStyle.setAlignment(HorizontalAlignment.CENTER);
                cellContentStyle.setBorderBottom(BorderStyle.THIN);
                cellContentStyle.setBorderTop(BorderStyle.THIN);
                cellContentStyle.setBorderRight(BorderStyle.THIN);
                cellContentStyle.setBorderLeft(BorderStyle.THIN);
                cellContent.setCellStyle(cellContentStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 3, 8));

                // Row for Header
                Row headerRow = sheet.createRow(6);

                // Header
                for (int col = 0; col < columns.length; col++) {
                    sheet.setColumnWidth(col, widths[col] * 256);
                    Cell cell = headerRow.createCell(col);
                    cell.setCellValue(columns[col]);
                    cell.setCellStyle(headerKidsCellStyle);
                }
                int rowIdx = 7;
                int i = 0;
                for (EvaluateDateKidModel evaluateDateKidModel : kidVMList) {
                    i++;
                    Row row = sheet.createRow(rowIdx++);

                    Cell cellId = row.createCell(0);
                    cellId.setCellValue(i);
                    cellId.setCellStyle(contentCellStyle);

                    Cell cellEvaluateDate = row.createCell(1);
                    cellEvaluateDate.setCellValue(evaluateDateKidModel.getEvaluateDate());
                    cellEvaluateDate.setCellStyle(contentCellStyle);


                    Cell cellApprove = row.createCell(2);
                    cellApprove.setCellValue(evaluateDateKidModel.getIsApproved());
                    cellApprove.setCellStyle(contentStatusStyle);


                    Cell cellContentLearn = row.createCell(3);
                    cellContentLearn.setCellValue(evaluateDateKidModel.getContentLearn());
                    cellContentLearn.setCellStyle(contentCStyle);

                    Cell cellContentEat = row.createCell(4);
                    cellContentEat.setCellValue(evaluateDateKidModel.getContentEat());
                    cellContentEat.setCellStyle(contentCStyle);

                    Cell cellContentSleep = row.createCell(5);
                    cellContentSleep.setCellValue(evaluateDateKidModel.getContentSleep());
                    cellContentSleep.setCellStyle(contentCStyle);

                    Cell cellContentSanitary = row.createCell(6);
                    cellContentSanitary.setCellValue(evaluateDateKidModel.getContentSanitary());
                    cellContentSanitary.setCellStyle(contentCStyle);

                    Cell cellContentHealt = row.createCell(7);
                    cellContentHealt.setCellValue(evaluateDateKidModel.getContentHealt());
                    cellContentHealt.setCellStyle(contentCStyle);

                    Cell cellContentCommon = row.createCell(8);
                    cellContentCommon.setCellValue(evaluateDateKidModel.getContentCommon());
                    cellContentCommon.setCellStyle(contentCStyle);
                }

            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


}
