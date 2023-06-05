package com.example.onekids_project.importexport.serviceimpl;


import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.school.Grade;
import com.example.onekids_project.importexport.model.*;
import com.example.onekids_project.importexport.service.KidsExcelService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.GradeRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.request.kids.*;
import com.example.onekids_project.response.excel.ExcelDataNew;
import com.example.onekids_project.response.school.AppIconResponse;
import com.example.onekids_project.response.school.ListAppIconResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.*;
import com.example.onekids_project.util.ConvertData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class KidsExcelServiceImpl implements KidsExcelService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private MaClassService maClassService;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private AppIconParentService appIconParentService;
    @Autowired
    private AppIconParentAddSerivce appIconParentAddSerivce;

    @Autowired
    private KidsService kidsService;

    @Override
    public ByteArrayInputStream customKidsToExcel(List<KidModel> kidModelList, String nameSchool) throws IOException {

        XSSFColor greyOne = new XSSFColor(new java.awt.Color(255, 255, 255));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(255, 255, 0));
        XSSFColor pinkOne = new XSSFColor(new java.awt.Color(255, 178, 189));
        XSSFColor greenOne = new XSSFColor(new java.awt.Color(120, 243, 136));
        int[] widths = {5, 10, 15, 15, 17, 15, 15, 15, 20, 15, 15, 15, 15, 18, 15, 15, 15, 15, 18, 15, 15, 15, 15, 20, 20};

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        String dateToStr = df.format(currentDate);

        String[] COLUMNs = {"STT", "Tình trạng", "Khối học", "Lớp học", "Tên nhóm", "Họ và tên", "Ngày sinh", "Giới tính", "Biệt danh", "Địa chỉ", "Người đại diện", "Ngày nhập học", "Ngày bảo lưu", "Ngày nghỉ học", "Họ tên mẹ", "Ngày sinh", "Số điện thoại", "Email", "Nghề nghiệp", "Họ tên bố", "Ngày sinh", "Số điện thoại", "Email", "Nghề nghiệp", "Ghi chú"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
//            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("Danh_sach_hoc_sinh");

            sheet.setDisplayGridlines(false);
            for (int i = 0; i < 4; i++) {
                Row headerRow = sheet.createRow(i);
                for (int col = 0; col < COLUMNs.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    if (col == 0 && i == 0) {
                        cell.setCellValue("DANH SÁCH HỌC SINH");
                        CellStyle cellStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 18);
                        cellFont.setBold(true);
                        cellFont.setColor(IndexedColors.RED.getIndex());

                        ((XSSFCellStyle) cellStyle).setFillForegroundColor(greyOne);
                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        cellStyle.setFont(cellFont);
                        cell.setCellStyle(cellStyle);
                    } else if (col == 0 && i == 1) {
                        cell.setCellValue("Trường: " + nameSchool);
                        CellStyle twoStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setBold(true);
                        ((XSSFCellStyle) twoStyle).setFillForegroundColor(greyOne);
                        twoStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        twoStyle.setFont(cellFont);
                        cell.setCellStyle(twoStyle);
                    } else if (col == 0 && i == 2) {
                        cell.setCellValue("Ngày: " + dateToStr);
                        CellStyle threeStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 11);
                        cellFont.setBold(true);
                        ((XSSFCellStyle) threeStyle).setFillForegroundColor(greyOne);
                        threeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        threeStyle.setFont(cellFont);
                        cell.setCellStyle(threeStyle);
                    } else {
                        CellStyle cellStyle = workbook.createCellStyle();
                        ((XSSFCellStyle) cellStyle).setFillForegroundColor(greyOne);

                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        cell.setCellStyle(cellStyle);
                    }


                }

            }
            sheet.createFreezePane(6, 5);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerKidsCellStyle = workbook.createCellStyle();
            ((XSSFCellStyle) headerKidsCellStyle).setFillForegroundColor(blueOne);
            headerKidsCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerKidsCellStyle.setFont(headerFont);
            headerKidsCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerKidsCellStyle.setBorderBottom(BorderStyle.THIN);
            headerKidsCellStyle.setBorderTop(BorderStyle.THIN);
            headerKidsCellStyle.setBorderRight(BorderStyle.THIN);
            headerKidsCellStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle headerMotherCellStyle = workbook.createCellStyle();
            ((XSSFCellStyle) headerMotherCellStyle).setFillForegroundColor(pinkOne);
            headerMotherCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerMotherCellStyle.setFont(headerFont);
            headerMotherCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerMotherCellStyle.setBorderBottom(BorderStyle.THIN);
            headerMotherCellStyle.setBorderTop(BorderStyle.THIN);
            headerMotherCellStyle.setBorderRight(BorderStyle.THIN);
            headerMotherCellStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle headerFatherCellStyle = workbook.createCellStyle();
            ((XSSFCellStyle) headerFatherCellStyle).setFillForegroundColor(greenOne);
            headerFatherCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerFatherCellStyle.setFont(headerFont);
            headerFatherCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerFatherCellStyle.setBorderBottom(BorderStyle.THIN);
            headerFatherCellStyle.setBorderTop(BorderStyle.THIN);
            headerFatherCellStyle.setBorderRight(BorderStyle.THIN);
            headerFatherCellStyle.setBorderLeft(BorderStyle.THIN);


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

            // Row for Header
            Row headerRow = sheet.createRow(4);

            // Header
            for (int col = 0; col < COLUMNs.length; col++) {
                sheet.setColumnWidth(col, widths[col] * 256);
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
                if (col < 13) {
                    cell.setCellStyle(headerKidsCellStyle);
                } else if (col > 12 && col < 18) {
                    cell.setCellStyle(headerMotherCellStyle);
                } else if (col > 17 && col < 23) {
                    cell.setCellStyle(headerFatherCellStyle);
                } else {
                    cell.setCellStyle(headerKidsCellStyle);
                }

            }


            int rowIdx = 5;
            for (KidModel kidModel : kidModelList) {

                Row row = sheet.createRow(rowIdx++);

                Cell cellStt = row.createCell(0);
                cellStt.setCellValue(kidModel.getStt());
                cellStt.setCellStyle(contentCellStyle);

                Cell cellStatus = row.createCell(1);
                cellStatus.setCellValue(kidModel.getKidStatus());
                cellStatus.setCellStyle(contentCellStyle);

                Cell cellGrade = row.createCell(2);
                cellGrade.setCellValue(kidModel.getGrade());
                cellGrade.setCellStyle(contentCellStyle);

                Cell cellClass = row.createCell(3);
                cellClass.setCellValue(kidModel.getClassName());
                cellClass.setCellStyle(contentCellStyle);

                Cell cellGroup = row.createCell(4);
                cellGroup.setCellValue(kidModel.getGroupName());
                cellGroup.setCellStyle(contentCellStyle);

                Cell cellFullName = row.createCell(5);
                cellFullName.setCellValue(kidModel.getFullName());
                cellFullName.setCellStyle(contentCellStyle);


                Cell cellBirthday = row.createCell(6);
                cellBirthday.setCellValue(kidModel.getBirthDay());
                cellBirthday.setCellStyle(contentCellStyle);


                Cell cellGender = row.createCell(7);
                cellGender.setCellValue(kidModel.getGender());
                cellGender.setCellStyle(contentCellStyle);


                Cell cellUserName = row.createCell(8);
                cellUserName.setCellValue(kidModel.getNickName());
                cellUserName.setCellStyle(contentCellStyle);


                Cell cellAddress = row.createCell(9);
                cellAddress.setCellValue(kidModel.getAddress());
                cellAddress.setCellStyle(contentCellStyle);

                Cell cellRepresentation = row.createCell(10);
                cellRepresentation.setCellValue(kidModel.getRepresentation());
                cellRepresentation.setCellStyle(contentCellStyle);


                Cell cellDateStart = row.createCell(11);
                cellDateStart.setCellValue(kidModel.getDateStart());
                cellDateStart.setCellStyle(contentCellStyle);

                Cell cellDateRetain = row.createCell(12);
                cellDateRetain.setCellValue(kidModel.getDateRetain());
                cellDateRetain.setCellStyle(contentCellStyle);

                Cell cellDateLeave = row.createCell(13);
                cellDateLeave.setCellValue(kidModel.getDateLeave());
                cellDateLeave.setCellStyle(contentCellStyle);

                Cell cellMotherName = row.createCell(14);
                cellMotherName.setCellValue(kidModel.getMotherName());
                cellMotherName.setCellStyle(contentCellStyle);

                Cell cellMotherBirthday = row.createCell(15);
                cellMotherBirthday.setCellValue(kidModel.getMotherBirthday());
                cellMotherBirthday.setCellStyle(contentCellStyle);

                Cell cellMotherPhone = row.createCell(16);
                cellMotherPhone.setCellValue(kidModel.getMotherPhone());
                cellMotherPhone.setCellStyle(contentCellStyle);


                Cell cellMotherEmail = row.createCell(17);
                cellMotherEmail.setCellValue(kidModel.getMotherEmail());
                cellMotherEmail.setCellStyle(contentCellStyle);

                Cell cellMotherJob = row.createCell(18);
                cellMotherJob.setCellValue(kidModel.getMotherJob());
                cellMotherJob.setCellStyle(contentCellStyle);

                Cell cellFatherName = row.createCell(19);
                cellFatherName.setCellValue(kidModel.getFatherName());
                cellFatherName.setCellStyle(contentCellStyle);

                Cell cellFatherBirthday = row.createCell(20);
                cellFatherBirthday.setCellValue(kidModel.getFatherBirthday());
                cellFatherBirthday.setCellStyle(contentCellStyle);

                Cell cellFatherPhone = row.createCell(21);
                cellFatherPhone.setCellValue(kidModel.getFatherPhone());
                cellFatherPhone.setCellStyle(contentCellStyle);

                Cell cellFatherEmail = row.createCell(22);
                cellFatherEmail.setCellValue(kidModel.getFatherEmail());
                cellFatherEmail.setCellStyle(contentCellStyle);

                Cell cellFatherJob = row.createCell(23);
                cellFatherJob.setCellValue(kidModel.getFatherJob());
                cellFatherJob.setCellStyle(contentCellStyle);

                Cell cellNote = row.createCell(24);
                cellNote.setCellValue(kidModel.getNote());
                cellNote.setCellStyle(contentCellStyle);


            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public ByteArrayInputStream customKidsToExcelHeightWeight(List<HeightWeightModel> heightWeightModels, String nameSchool, String className) throws IOException {

        XSSFColor greyOne = new XSSFColor(new java.awt.Color(255, 255, 255));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(255, 255, 0));
        XSSFColor pinkOne = new XSSFColor(new java.awt.Color(255, 178, 189));
        XSSFColor greenOne = new XSSFColor(new java.awt.Color(120, 243, 136));
        XSSFColor yellowTwo = new XSSFColor(new java.awt.Color(217, 210, 144));
        int[] widths = {5, 20, 15, 15, 17, 15, 15, 15, 20, 15, 15, 15, 15, 18, 15, 15, 15, 15, 18, 15, 15, 15, 15, 20};

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat tf = new SimpleDateFormat("dd.MM.yyyy");
        Date currentDate = new Date();
        String dateToStr = df.format(currentDate);
        String dateToStr2 = tf.format(currentDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String[] COLUMNs = {"STT", "Họ và tên", "Cân nặng (Kg)", "Ngày cân", "Chiều cao (Cm)", "Ngày đo"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet(dateToStr2);

            sheet.setDisplayGridlines(false);
            for (int i = 0; i < 5; i++) {
                Row headerRow = sheet.createRow(i);
                for (int col = 0; col < COLUMNs.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    if (col == 0 && i == 0) {
                        cell.setCellValue("BẢNG KÊ SỐ ĐO CHIỀU CAO - CÂN NẶNG MỚI NHẤT");
                        CellStyle cellStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 18);
                        cellFont.setBold(true);
                        cellFont.setColor(IndexedColors.RED.getIndex());

                        ((XSSFCellStyle) cellStyle).setFillForegroundColor(greyOne);
                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        cellStyle.setFont(cellFont);
                        cell.setCellStyle(cellStyle);
                    } else if (col == 0 && i == 1) {
                        cell.setCellValue("Trường: " + nameSchool);
                        CellStyle twoStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setBold(true);
                        ((XSSFCellStyle) twoStyle).setFillForegroundColor(greyOne);
                        twoStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        twoStyle.setFont(cellFont);
                        cell.setCellStyle(twoStyle);
                    } else if (col == 0 && i == 2) {
                        cell.setCellValue("Ngày: " + dateToStr);
                        CellStyle threeStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 11);
                        cellFont.setBold(true);
                        ((XSSFCellStyle) threeStyle).setFillForegroundColor(greyOne);
                        threeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        threeStyle.setFont(cellFont);
                        cell.setCellStyle(threeStyle);
                    } else if (col == 0 && i == 3) {
//                        +Class
                        cell.setCellValue("Lớp: " + className);
                        CellStyle threeStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 11);
                        cellFont.setBold(true);
                        ((XSSFCellStyle) threeStyle).setFillForegroundColor(greyOne);
                        threeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        threeStyle.setFont(cellFont);
                        cell.setCellStyle(threeStyle);
                    } else {
                        CellStyle cellStyle = workbook.createCellStyle();
                        ((XSSFCellStyle) cellStyle).setFillForegroundColor(greyOne);

                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        cell.setCellStyle(cellStyle);
                    }
                }

            }
            sheet.createFreezePane(6, 6);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            headerFont.setFontHeightInPoints((short) 13);
            CellStyle headerKidsCellStyle = workbook.createCellStyle();
            ((XSSFCellStyle) headerKidsCellStyle).setFillForegroundColor(blueOne);
            headerKidsCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerKidsCellStyle.setFont(headerFont);
            headerKidsCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerKidsCellStyle.setBorderBottom(BorderStyle.THIN);
            headerKidsCellStyle.setBorderTop(BorderStyle.THIN);
            headerKidsCellStyle.setBorderRight(BorderStyle.THIN);
            headerKidsCellStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle headerMotherCellStyle = workbook.createCellStyle();
            ((XSSFCellStyle) headerMotherCellStyle).setFillForegroundColor(pinkOne);
            headerMotherCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerMotherCellStyle.setFont(headerFont);
            headerMotherCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerMotherCellStyle.setBorderBottom(BorderStyle.THIN);
            headerMotherCellStyle.setBorderTop(BorderStyle.THIN);
            headerMotherCellStyle.setBorderRight(BorderStyle.THIN);
            headerMotherCellStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle headerFatherCellStyle = workbook.createCellStyle();
            ((XSSFCellStyle) headerFatherCellStyle).setFillForegroundColor(greenOne);
            headerFatherCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerFatherCellStyle.setFont(headerFont);
            headerFatherCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerFatherCellStyle.setBorderBottom(BorderStyle.THIN);
            headerFatherCellStyle.setBorderTop(BorderStyle.THIN);
            headerFatherCellStyle.setBorderRight(BorderStyle.THIN);
            headerFatherCellStyle.setBorderLeft(BorderStyle.THIN);


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

            // Row for Header4
            int rowHeader = 4;
            Row headerRow4 = sheet.createRow(rowHeader);
            // Header 4
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow4.createCell(col);
                CellStyle cellHeaderRow4 = workbook.createCellStyle();
                cellHeaderRow4.setBorderBottom(BorderStyle.THIN);
                cellHeaderRow4.setBorderTop(BorderStyle.THIN);
                cellHeaderRow4.setBorderRight(BorderStyle.THIN);
                cellHeaderRow4.setBorderLeft(BorderStyle.THIN);
                cell.setCellStyle(cellHeaderRow4);
            }
            // Header
            Cell cellEvualate = headerRow4.createCell(0);
            cellEvualate.setCellValue("CÂN ĐO");
            CellStyle cellEvualateStyle = workbook.createCellStyle();
            Font headerEvualate = workbook.createFont();
            headerEvualate.setBold(true);
            headerEvualate.setColor(IndexedColors.RED.getIndex());
            cellEvualateStyle.setFont(headerEvualate);
            ((XSSFCellStyle) cellEvualateStyle).setFillForegroundColor(yellowTwo);
            cellEvualateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellEvualateStyle.setAlignment(HorizontalAlignment.CENTER);
            cellEvualateStyle.setBorderBottom(BorderStyle.THIN);
            cellEvualateStyle.setBorderTop(BorderStyle.THIN);
            cellEvualateStyle.setBorderRight(BorderStyle.THIN);
            cellEvualateStyle.setBorderLeft(BorderStyle.THIN);
            cellEvualate.setCellStyle(cellEvualateStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 0, 1));

            Cell cellContent = headerRow4.createCell(2);
            cellContent.setCellValue("SỐ ĐO CHIỀU CAO - CÂN NẶNG");
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
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 2, 5));

            // Row for Header
            Row headerRow = sheet.createRow(5);

            // Header
            for (int col = 0; col < COLUMNs.length; col++) {
                sheet.setColumnWidth(col, widths[col] * 256);
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
                if (col < 13) {
                    cell.setCellStyle(headerKidsCellStyle);
                } else if (col > 12 && col < 18) {
                    cell.setCellStyle(headerMotherCellStyle);
                } else if (col > 17 && col < 23) {
                    cell.setCellStyle(headerFatherCellStyle);
                } else {
                    cell.setCellStyle(headerKidsCellStyle);
                }

            }


            int rowIdx = 6;
            for (HeightWeightModel heightWeightModel : heightWeightModels) {

                Row row = sheet.createRow(rowIdx++);

                Cell cellStt = row.createCell(0);
                cellStt.setCellValue(heightWeightModel.getStt());
                cellStt.setCellStyle(contentCellStyle);

                Cell cellHoten = row.createCell(1);
                if (heightWeightModel.getKidName() != null) {
                    cellHoten.setCellValue(heightWeightModel.getKidName());
                } else {
                    cellHoten.setCellValue("");
                }
                cellHoten.setCellStyle(contentCellStyle);

                Cell cellCannang = row.createCell(2);
                if (heightWeightModel.getWeight() != null) {
                    cellCannang.setCellValue(heightWeightModel.getWeight());
                } else {
                    cellCannang.setCellValue("");
                }
                cellCannang.setCellStyle(contentCellStyle);

                Cell cellNgaycan = row.createCell(3);
                if (heightWeightModel.getTimeWeight() != null) {
                    cellNgaycan.setCellValue(heightWeightModel.getTimeWeight().toString());
                } else {
                    cellNgaycan.setCellValue("");
                }
                cellNgaycan.setCellStyle(contentCellStyle);

                Cell cellChieucao = row.createCell(4);
                if (heightWeightModel.getHeight() != null) {
                    cellChieucao.setCellValue(heightWeightModel.getHeight());
                } else {
                    cellChieucao.setCellValue("");
                }
                cellChieucao.setCellStyle(contentCellStyle);

                Cell cellNgaydo = row.createCell(5);
                if (heightWeightModel.getTimeHeight() != null) {
                    cellNgaydo.setCellValue(heightWeightModel.getTimeHeight().toString());
                } else {
                    cellNgaydo.setCellValue("");
                }
                cellNgaydo.setCellStyle(contentCellStyle);

            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    /**
     * Xuất all chieu cao can nang of học sinh
     */
    @Override
    public ByteArrayInputStream customKidsToExcelAllHeightWeight(List<HeightWeightModel> modelList, Long idSchool, Long idClass) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(131, 164, 196));
        XSSFColor yellowTwo = new XSSFColor(new java.awt.Color(217, 210, 144));
        XSSFColor yellowTwo1 = new XSSFColor(new java.awt.Color(255, 255, 116));
        int[] widths = {5, 23, 23, 23, 23};
        String[] columns = {"STT", "Cân nặng (Kg)", "Ngày cân", "Chiều cao (Cm)", "Ngày đo"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {

            for (HeightWeightModel model : modelList) {

                MaClassDTO classDTO = maClassService.findByIdMaClass(idSchool, idClass).stream().findFirst().orElse(null);

                String className = "";
                if (classDTO != null) {
                    className = classDTO.getClassName();
                }
                SchoolResponse school = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
                String schoolName = "";
                if (school != null) {
                    schoolName = school.getSchoolName();
                }
                Sheet sheet = workbook.createSheet(model.getKidName().concat(AppConstant.SPACE_EXPORT_ID).concat(model.getId().toString()));
                sheet.setDisplayGridlines(false);
                for (int i = 0; i < 4; i++) {
                    Row headerRow = sheet.createRow(i);
                    for (int col = 0; col < columns.length; col++) {
                        Cell cell = headerRow.createCell(col);
                        if (col == 0 && i == 0) {
                            cell.setCellValue("BẢNG KÊ SỐ ĐO CHIỀU CAO - CÂN NẶNG");
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
                            cell.setCellValue("Học sinh: " + model.getKidName());
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
                sheet.createFreezePane(5, 6);

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
                cellEvualate.setCellValue("SỐ ĐO CHIỀU CAO - CÂN NẶNG");
                CellStyle cellEvualateStyle = workbook.createCellStyle();
                Font headerEvualate = workbook.createFont();
                headerEvualate.setBold(true);
                headerEvualate.setColor(IndexedColors.BLACK.getIndex());
                cellEvualateStyle.setFont(headerEvualate);
                ((XSSFCellStyle) cellEvualateStyle).setFillForegroundColor(yellowTwo);
                cellEvualateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellEvualateStyle.setAlignment(HorizontalAlignment.CENTER);
                cellEvualateStyle.setBorderBottom(BorderStyle.THIN);
                cellEvualateStyle.setBorderTop(BorderStyle.THIN);
                cellEvualateStyle.setBorderRight(BorderStyle.THIN);
                cellEvualateStyle.setBorderLeft(BorderStyle.THIN);
                cellEvualate.setCellStyle(cellEvualateStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 0, 4));


                // Row for Header
                Row headerRow = sheet.createRow(5);

                // header row 5
                Font headerFont = workbook.createFont();
                headerFont.setFontHeightInPoints((short) 13);
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
                ((XSSFCellStyle) contentStatusStyle).setFillForegroundColor(yellowTwo1);
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

                // Header
                for (int col = 0; col < columns.length; col++) {
                    sheet.setColumnWidth(col, widths[col] * 256);
                    Cell cell = headerRow.createCell(col);
                    cell.setCellValue(columns[col]);
                    cell.setCellStyle(headerKidsCellStyle);
                }

                int rowIdx = 6;
                int i = 1;
                for (KidsHeightModel heightModel : model.getKidsHeightModelList()) {
                    Row row = sheet.createRow(rowIdx++);
                    Cell cellSTT = row.createCell(0);
                    cellSTT.setCellValue(i++);
                    cellSTT.setCellStyle(contentCellStyle);

                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue("");
                    cell1.setCellStyle(contentStatusStyle);

                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue("");
                    cell2.setCellStyle(contentStatusStyle);

                    Cell cellChieuCao = row.createCell(3);
                    if (heightModel.getHeight() != null) {
                        cellChieuCao.setCellValue(heightModel.getHeight());
                    } else {
                        cellChieuCao.setCellValue("");
                    }
                    cellChieuCao.setCellStyle(contentStatusStyle);

                    Cell cellNgayDo = row.createCell(4);
                    if (heightModel.getTimeHeight() != null) {
                        cellNgayDo.setCellValue(heightModel.getTimeHeight().toString());
                    } else {
                        cellNgayDo.setCellValue("");
                    }
                    cellNgayDo.setCellStyle(contentStatusStyle);
                }
                for (KidsWeightModel weightModel : model.getKidsWeightModelList()) {
                    Row row = sheet.createRow(rowIdx++);
                    Cell cellSTT = row.createCell(0);
                    cellSTT.setCellValue(i++);
                    cellSTT.setCellStyle(contentCellStyle);

                    Cell cellCanNang = row.createCell(1);
                    if (weightModel.getWeight() != null) {
                        cellCanNang.setCellValue(weightModel.getWeight());
                    } else {
                        cellCanNang.setCellValue("");
                    }
                    cellCanNang.setCellStyle(contentStatusStyle);


                    Cell cellNgayCan = row.createCell(2);
                    if (weightModel.getTimeWeight() != null) {
                        cellNgayCan.setCellValue(weightModel.getTimeWeight().toString());
                    } else {
                        cellNgayCan.setCellValue("");
                    }
                    cellNgayCan.setCellStyle(contentStatusStyle);

                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue("");
                    cell3.setCellStyle(contentStatusStyle);

                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue("");
                    cell4.setCellStyle(contentStatusStyle);
                }

            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public ListKidModelImport importExcelKids(UserPrincipal principal, MultipartFile multipartFile) throws IOException {
        ListKidModelImport listKidModelImport = new ListKidModelImport();
        InputStream inputStream = multipartFile.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<KidModelImport> kidModels = new ArrayList<>();
        List<KidModelImport> kidModelsFail = new ArrayList<>();
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = firstSheet.iterator();
        rowIterator.next();
        while (rowIterator.hasNext()) { // check row
            Row nextRow = rowIterator.next(); // next row
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            if (nextRow.getRowNum() > 4) {
                KidModelImport kidModel = new KidModelImport();
                while (cellIterator.hasNext()) {
                    try {
                        Cell nextCell = cellIterator.next();
                        int columnIndex1 = nextCell.getColumnIndex();
                        switch (columnIndex1) {
                            case 0:
                                kidModel.setStt((long) nextCell.getNumericCellValue());
                                break;
                            case 1:
                                if (nextCell.getStringCellValue().equalsIgnoreCase(KidsStatusConstant.STUDYING_NAME)) {
                                    kidModel.setKidStatus(KidsStatusConstant.STUDYING);
                                } else if (nextCell.getStringCellValue().equalsIgnoreCase(KidsStatusConstant.STUDY_WAIT_NAME)) {
                                    kidModel.setKidStatus(KidsStatusConstant.STUDY_WAIT);
                                } else kidModel.setKidStatus("");
                                break;
                            case 2:
                                kidModel.setGrade(nextCell.getStringCellValue());
                                break;
                            case 3:
                                kidModel.setClassName(nextCell.getStringCellValue());
                                break;
                            case 4:
                                kidModel.setFullName(nextCell.getStringCellValue());
                                break;
                            case 5:
//                                kidModel.setBirthDay(LocalDate.parse(sdf.format(nextCell.getDateCellValue())));
                                kidModel.setBirthDay(nextCell.getStringCellValue());
                                break;
                            case 6:
                                if (nextCell.getStringCellValue().equalsIgnoreCase(AppConstant.MALE)) {
                                    kidModel.setGender(AppConstant.MALE);
                                } else if (nextCell.getStringCellValue().equalsIgnoreCase(AppConstant.FEMALE)) {
                                    kidModel.setGender(AppConstant.FEMALE);
                                } else kidModel.setGender(AppConstant.MALE);
                                break;
                            case 7:
                                kidModel.setNickName(nextCell.getStringCellValue());
                                break;
                            case 8:
                                kidModel.setAddress(nextCell.getStringCellValue());
                                break;
                            case 9:
                                kidModel.setRepresentation(nextCell.getStringCellValue());
                                break;
                            case 10:
                                kidModel.setDateStart(nextCell.getStringCellValue());
                                break;
                            case 11:
                                kidModel.setDateRetain(nextCell.getStringCellValue());
                                break;
                            case 12:
                                kidModel.setDateLeave(nextCell.getStringCellValue());
                                break;
                            case 13:
                                kidModel.setMotherName(nextCell.getStringCellValue());
                                break;
                            case 14:
                                kidModel.setMotherBirthday(nextCell.getStringCellValue());
                                break;
                            case 15:
                                String phoneMother = nextCell.getStringCellValue();
                                if (phoneMother.matches("[0-9]+") && phoneMother.length() == 10) {
                                    kidModel.setMotherPhone(phoneMother);
                                } else kidModel.setMotherPhone("");
//                                kidModel.setMotherPhone(Long.toString(Math.round(nextCell.getNumericCellValue())));
                                break;
                            case 16:
                                kidModel.setMotherEmail(nextCell.getStringCellValue());
                                break;
                            case 17:
                                kidModel.setMotherJob(nextCell.getStringCellValue());
                                break;
                            case 18:
                                kidModel.setFatherName(nextCell.getStringCellValue());
                                break;
                            case 19:
                                kidModel.setFatherBirthday(nextCell.getStringCellValue());
                                break;
                            case 20:
                                String phoneDad = nextCell.getStringCellValue();
                                if (phoneDad.matches("[0-9]+") && phoneDad.length() == 10) {
                                    kidModel.setFatherPhone(phoneDad);
                                } else kidModel.setFatherPhone("");
//                                kidModel.setFatherPhone(Long.toString(Math.round(nextCell.getNumericCellValue())));
                                break;
                            case 21:
                                kidModel.setFatherEmail(nextCell.getStringCellValue());
                                break;
                            case 22:
                                kidModel.setFatherJob(nextCell.getStringCellValue());
                                break;
                            case 23:
                                kidModel.setNote(nextCell.getStringCellValue());
                                break;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
                kidModels.add(kidModel);
            }
        }
        kidModelsFail = kidModels.stream().filter(x -> StringUtils.isEmpty(x.getGrade()) || StringUtils.isEmpty(x.getKidStatus()) || StringUtils.isEmpty(x.getClassName()) || StringUtils.isEmpty(x.getFullName()) || StringUtils.isEmpty(x.getRepresentation()) || StringUtils.isEmpty(x.getBirthDay()) || StringUtils.isEmpty(x.getGender()) || StringUtils.isEmpty(x.getDateStart())).collect(Collectors.toList());
        List<KidModelImportFail> kidModelImportFailList = listMapper.mapList(kidModelsFail, KidModelImportFail.class);
        kidModels = kidModels.stream().filter(x -> !StringUtils.isEmpty(x.getGrade()) && !StringUtils.isEmpty(x.getKidStatus()) && !StringUtils.isEmpty(x.getClassName()) && !StringUtils.isEmpty(x.getFullName()) && !StringUtils.isEmpty(x.getRepresentation()) && !StringUtils.isEmpty(x.getBirthDay()) && !StringUtils.isEmpty(x.getGender()) && !StringUtils.isEmpty(x.getDateStart())).collect(Collectors.toList());
        listKidModelImport.setKidModelImportList(kidModels);
        listKidModelImport.setKidModelImportFailList(kidModelImportFailList);
        return listKidModelImport;
    }

    @Override
    public ByteArrayInputStream customKidsImportFailExcel(List<KidModelImportFail> kidModelList, String nameSchool) throws IOException {

        XSSFColor greyOne = new XSSFColor(new java.awt.Color(255, 255, 255));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(255, 255, 0));
        XSSFColor pinkOne = new XSSFColor(new java.awt.Color(255, 178, 189));
        XSSFColor greenOne = new XSSFColor(new java.awt.Color(120, 243, 136));
        XSSFColor redOne = new XSSFColor(new java.awt.Color(255, 64, 0));
        int[] widths = {5, 10, 15, 15, 17, 15, 15, 15, 20, 15, 15, 15, 15, 18, 15, 15, 15, 15, 18, 15, 15, 15, 15, 20, 20};

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        String dateToStr = df.format(currentDate);

        String[] COLUMNs = {"STT", "Tình trạng", "Khối học", "Lớp học", "Họ và tên", "Ngày sinh", "Giới tính", "Biệt danh", "Địa chỉ", "Người đại diện", "Ngày nhập học", "Ngày bảo lưu", "Ngày nghỉ học", "Họ tên mẹ", "Ngày sinh", "Số điện thoại", "Email", "Nghề nghiệp", "Họ tên bố", "Ngày sinh", "Số điện thoại", "Email", "Nghề nghiệp", "Ghi chú"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
//            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("Danh_sach_hoc_sinh_loi_data");

            sheet.setDisplayGridlines(false);
            for (int i = 0; i < 4; i++) {
                Row headerRow = sheet.createRow(i);
                for (int col = 0; col < COLUMNs.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    if (col == 0 && i == 0) {
                        cell.setCellValue("DANH SÁCH HỌC SINH IMPORT LỖI");
                        CellStyle cellStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 18);
                        cellFont.setBold(true);
                        cellFont.setColor(IndexedColors.RED.getIndex());

                        ((XSSFCellStyle) cellStyle).setFillForegroundColor(greyOne);
                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        cellStyle.setFont(cellFont);
                        cell.setCellStyle(cellStyle);
                    } else if (col == 0 && i == 1) {
                        cell.setCellValue("Trường: " + nameSchool);
                        CellStyle twoStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setBold(true);
                        ((XSSFCellStyle) twoStyle).setFillForegroundColor(greyOne);
                        twoStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        twoStyle.setFont(cellFont);
                        cell.setCellStyle(twoStyle);
                    } else if (col == 0 && i == 2) {
                        cell.setCellValue("Ngày: " + dateToStr);
                        CellStyle threeStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 11);
                        cellFont.setBold(true);
                        ((XSSFCellStyle) threeStyle).setFillForegroundColor(greyOne);
                        threeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        threeStyle.setFont(cellFont);
                        cell.setCellStyle(threeStyle);
                    } else if (col == 0 && i == 3) {
                        cell.setCellValue("Lưu ý: + Bạn không được để trống các ô màu đỏ \n" + "+ Fomat dạng TEXT dữ liệu ngày theo dạng 01/11/1111 \n" +
                                "+ Thông tin người đại diện cần đầy đủ tên và số điện thoại \n");
                        CellStyle threeStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 11);
                        cellFont.setBold(true);
                        cellFont.setColor(IndexedColors.RED.getIndex());
                        ((XSSFCellStyle) threeStyle).setFillForegroundColor(greyOne);
                        threeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        threeStyle.setFont(cellFont);
                        cell.setCellStyle(threeStyle);
                    } else {
                        CellStyle cellStyle = workbook.createCellStyle();
                        ((XSSFCellStyle) cellStyle).setFillForegroundColor(greyOne);

                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        cell.setCellStyle(cellStyle);
                    }


                }

            }
            sheet.createFreezePane(6, 5);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            Font headerFontWhite = workbook.createFont();
            headerFontWhite.setBold(true);
            headerFontWhite.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerKidsCellStyle = workbook.createCellStyle();
            ((XSSFCellStyle) headerKidsCellStyle).setFillForegroundColor(blueOne);
            headerKidsCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerKidsCellStyle.setFont(headerFont);
            headerKidsCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerKidsCellStyle.setBorderBottom(BorderStyle.THIN);
            headerKidsCellStyle.setBorderTop(BorderStyle.THIN);
            headerKidsCellStyle.setBorderRight(BorderStyle.THIN);
            headerKidsCellStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle headerMotherCellStyle = workbook.createCellStyle();
            ((XSSFCellStyle) headerMotherCellStyle).setFillForegroundColor(pinkOne);
            headerMotherCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerMotherCellStyle.setFont(headerFont);
            headerMotherCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerMotherCellStyle.setBorderBottom(BorderStyle.THIN);
            headerMotherCellStyle.setBorderTop(BorderStyle.THIN);
            headerMotherCellStyle.setBorderRight(BorderStyle.THIN);
            headerMotherCellStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle headerCellStyleRed = workbook.createCellStyle();

            ((XSSFCellStyle) headerCellStyleRed).setFillForegroundColor(redOne);
            headerCellStyleRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            headerCellStyleRed.setFont(headerFontWhite);
            headerCellStyleRed.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyleRed.setBorderBottom(BorderStyle.THIN);
            headerCellStyleRed.setBorderTop(BorderStyle.THIN);
            headerCellStyleRed.setBorderRight(BorderStyle.THIN);
            headerCellStyleRed.setBorderLeft(BorderStyle.THIN);

            CellStyle headerFatherCellStyle = workbook.createCellStyle();
            ((XSSFCellStyle) headerFatherCellStyle).setFillForegroundColor(greenOne);
            headerFatherCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerFatherCellStyle.setFont(headerFont);
            headerFatherCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerFatherCellStyle.setBorderBottom(BorderStyle.THIN);
            headerFatherCellStyle.setBorderTop(BorderStyle.THIN);
            headerFatherCellStyle.setBorderRight(BorderStyle.THIN);
            headerFatherCellStyle.setBorderLeft(BorderStyle.THIN);


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

            // Row for Header
            Row headerRow = sheet.createRow(4);

            // Header
            for (int col = 0; col < COLUMNs.length; col++) {
                sheet.setColumnWidth(col, widths[col] * 256);
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);

                if (col < 12) {
                    cell.setCellStyle(headerKidsCellStyle);
                } else if (col > 11 && col < 17) {
                    cell.setCellStyle(headerMotherCellStyle);
                } else if (col > 16 && col < 22) {
                    cell.setCellStyle(headerFatherCellStyle);
                } else {
                    cell.setCellStyle(headerKidsCellStyle);
                }
                if (col < 7) {
                    cell.setCellStyle(headerCellStyleRed);
                } else if (col == 9 || col == 10) {
                    cell.setCellStyle(headerCellStyleRed);
                }

            }


            int rowIdx = 5;
            for (KidModelImportFail kidModel : kidModelList) {

                Row row = sheet.createRow(rowIdx++);

                Cell cellStt = row.createCell(0);
                cellStt.setCellValue(kidModel.getStt());
                cellStt.setCellStyle(contentCellStyle);

                Cell cellStatus = row.createCell(1);
                if (kidModel.getKidStatus().equalsIgnoreCase(AppConstant.STUDYING)) {
                    String status = KidsStatusConstant.STUDYING_NAME;
                    cellStatus.setCellValue(status);

                } else if (kidModel.getKidStatus().equalsIgnoreCase(AppConstant.STUDY_WAIT)) {
                    String status = KidsStatusConstant.STUDY_WAIT_NAME;
                    cellStatus.setCellValue(status);
                } else {
                    String status = "";
                    cellStatus.setCellValue(status);
                }
                cellStatus.setCellStyle(contentCellStyle);


                Cell cellGrade = row.createCell(2);
                cellGrade.setCellValue(kidModel.getGrade());
                cellGrade.setCellStyle(contentCellStyle);

                Cell cellClass = row.createCell(3);
                cellClass.setCellValue(kidModel.getClassName());
                cellClass.setCellStyle(contentCellStyle);

                Cell cellFullName = row.createCell(4);
                cellFullName.setCellValue(kidModel.getFullName());
                cellFullName.setCellStyle(contentCellStyle);


                Cell cellBirthday = row.createCell(5);
                cellBirthday.setCellValue(kidModel.getBirthDay());
                cellBirthday.setCellStyle(contentCellStyle);


                Cell cellGender = row.createCell(6);
                cellGender.setCellValue(kidModel.getGender());
                cellGender.setCellStyle(contentCellStyle);


                Cell cellUserName = row.createCell(7);
                cellUserName.setCellValue(kidModel.getNickName());
                cellUserName.setCellStyle(contentCellStyle);


                Cell cellAddress = row.createCell(8);
                cellAddress.setCellValue(kidModel.getAddress());
                cellAddress.setCellStyle(contentCellStyle);

                Cell cellRepresentation = row.createCell(9);
                cellRepresentation.setCellValue(kidModel.getRepresentation());
                cellRepresentation.setCellStyle(contentCellStyle);


                Cell cellDateStart = row.createCell(10);
                cellDateStart.setCellValue(kidModel.getDateStart());
                cellDateStart.setCellStyle(contentCellStyle);

                Cell cellDateRetain = row.createCell(11);
                cellDateRetain.setCellValue(kidModel.getDateRetain());
                cellDateRetain.setCellStyle(contentCellStyle);

                Cell cellDateLeave = row.createCell(12);
                cellDateLeave.setCellValue(kidModel.getDateLeave());
                cellDateLeave.setCellStyle(contentCellStyle);

                Cell cellMotherName = row.createCell(13);
                cellMotherName.setCellValue(kidModel.getMotherName());
                cellMotherName.setCellStyle(contentCellStyle);

                Cell cellMotherBirthday = row.createCell(14);
                cellMotherBirthday.setCellValue(kidModel.getMotherBirthday());
                cellMotherBirthday.setCellStyle(contentCellStyle);

                Cell cellMotherPhone = row.createCell(15);
                cellMotherPhone.setCellValue(kidModel.getMotherPhone());
                cellMotherPhone.setCellStyle(contentCellStyle);


                Cell cellMotherEmail = row.createCell(16);
                cellMotherEmail.setCellValue(kidModel.getMotherEmail());
                cellMotherEmail.setCellStyle(contentCellStyle);

                Cell cellMotherJob = row.createCell(17);
                cellMotherJob.setCellValue(kidModel.getMotherJob());
                cellMotherJob.setCellStyle(contentCellStyle);

                Cell cellFatherName = row.createCell(18);
                cellFatherName.setCellValue(kidModel.getFatherName());
                cellFatherName.setCellStyle(contentCellStyle);

                Cell cellFatherBirthday = row.createCell(19);
                cellFatherBirthday.setCellValue(kidModel.getFatherBirthday());
                cellFatherBirthday.setCellStyle(contentCellStyle);

                Cell cellFatherPhone = row.createCell(20);
                cellFatherPhone.setCellValue(kidModel.getFatherPhone());
                cellFatherPhone.setCellStyle(contentCellStyle);

                Cell cellFatherEmail = row.createCell(21);
                cellFatherEmail.setCellValue(kidModel.getFatherEmail());
                cellFatherEmail.setCellStyle(contentCellStyle);

                Cell cellFatherJob = row.createCell(22);
                cellFatherJob.setCellValue(kidModel.getFatherJob());
                cellFatherJob.setCellStyle(contentCellStyle);

                Cell cellNote = row.createCell(23);
                cellNote.setCellValue(kidModel.getNote());
                cellNote.setCellStyle(contentCellStyle);


            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Transactional
    @Override
    public void importExcelNewKids(UserPrincipal principal, CreateKidsExcelRequest request) {
        for (ExcelDataNew model : request.getBodyList()) {
            Optional<Grade> grade = gradeRepository.findByGradeNameAndDelActiveTrueAndSchool_Id(model.getPro2().trim(), principal.getIdSchoolLogin());
            Optional<MaClass> maClass = maClassRepository.findByClassNameAndDelActiveTrueAndIdSchool(model.getPro3().trim(), principal.getIdSchoolLogin());
            if (grade.isPresent() && maClass.isPresent()) {
                long check = grade.get().getMaClassList().stream().filter(x -> x.getId().equals(maClass.get().getId())).count();
                if (check == 1) {
                    CreateKidsRequest createKidsRequest = new CreateKidsRequest();
                    CreateKidMainInforRequest kidMainInforRequest = new CreateKidMainInforRequest();
                    this.setInfoKids(kidMainInforRequest, model);
                    kidMainInforRequest.setIdGrade(grade.get().getId());
                    kidMainInforRequest.setIdClass(maClass.get().getId());
                    KidsExtraInfoRequest kidsExtraInfo = new KidsExtraInfoRequest();
                    createKidsRequest.setKidMainInfo(kidMainInforRequest);
                    createKidsRequest.setKidsExtraInfo(kidsExtraInfo);
//                    List<AppIconResponse> appIconResponseList = new ArrayList<>();
                    List<AppIconParentRequest> parentIconApp;
                    ListAppIconResponse listAppIconResponse = appIconParentAddSerivce.findAppIconParentAddCreate(principal.getIdSchoolLogin());
                    parentIconApp = listMapper.mapList(listAppIconResponse.getAppIconResponseList(), AppIconParentRequest.class);
//                    if (listAppIconResponse.getAppIconResponseList().size() > 0) {
//                        parentIconApp = listMapper.mapList(listAppIconResponse.getAppIconResponseList(), AppIconParentRequest.class);
//                    } else {
//                        appIconResponseList.addAll(listAppIconResponse.getAppIconResponseList1());
//                        appIconResponseList.addAll(listAppIconResponse.getAppIconResponseList2());
//                        parentIconApp = listMapper.mapList(appIconResponseList, AppIconParentRequest.class);
//                    }
                    createKidsRequest.setParentIconApp(parentIconApp);
                    kidsService.createKids(principal, createKidsRequest);
                }
            }
        }
    }

    private void setInfoKids(CreateKidMainInforRequest kidMainInforRequest, ExcelDataNew model){
        kidMainInforRequest.setKidStatus(KidsStatusConstant.STUDYING);
        kidMainInforRequest.setFullName(model.getPro4());
        kidMainInforRequest.setBirthDay(ConvertData.convertStringToDate(model.getPro5()));
        kidMainInforRequest.setGender(model.getPro6());
        kidMainInforRequest.setNickName(model.getPro7());
        kidMainInforRequest.setAddress(model.getPro8());
        kidMainInforRequest.setPermanentAddress(model.getPro9());
        kidMainInforRequest.setEthnic(model.getPro10());
        kidMainInforRequest.setRepresentation(model.getPro11());
        kidMainInforRequest.setDateStart(ConvertData.convertStringToDate(model.getPro12()));
        kidMainInforRequest.setMotherName(model.getPro13());
        kidMainInforRequest.setMotherBirthday(ConvertData.convertStringToDate(model.getPro14()));
        kidMainInforRequest.setMotherPhone(model.getPro15());
        kidMainInforRequest.setMotherEmail(model.getPro16());
        kidMainInforRequest.setMotherJob(model.getPro17());
        kidMainInforRequest.setFatherName(model.getPro18());
        kidMainInforRequest.setFatherBirthday(ConvertData.convertStringToDate(model.getPro19()));
        kidMainInforRequest.setFatherPhone(model.getPro20());
        kidMainInforRequest.setFatherEmail(model.getPro21());
        kidMainInforRequest.setFatherJob(model.getPro22());
        kidMainInforRequest.setNote(model.getPro23());
        kidMainInforRequest.setIdentificationNumber(model.getPro24());
    }
}
