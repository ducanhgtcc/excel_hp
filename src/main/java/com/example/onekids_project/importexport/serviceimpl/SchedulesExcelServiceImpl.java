package com.example.onekids_project.importexport.serviceimpl;

import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.importexport.model.ScheduleModel;
import com.example.onekids_project.importexport.service.SchedulesExcelService;
import com.example.onekids_project.request.schedule.CreateTabDayInWeek;
import com.example.onekids_project.request.schedule.CreateMultiSchedule;
import com.example.onekids_project.request.schedule.CreateTabAllSchedule;
import com.example.onekids_project.request.schedule.UploadScheduleRequest;
import com.example.onekids_project.response.schedule.ScheduleInClassResponse;
import com.example.onekids_project.response.schedule.ScheduleInClassWeekResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.service.servicecustom.MaClassService;
import com.example.onekids_project.service.servicecustom.SchoolService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
public class SchedulesExcelServiceImpl implements SchedulesExcelService {

    @Autowired
    SchoolService schoolService;
    @Autowired
    MaClassService maClassService;

    @Override
    public ByteArrayInputStream schedulesToExcel(List<ScheduleInClassWeekResponse> scheduleInClassWeekResponses, Long idSchool, Long idClass, LocalDate currentDate) throws IOException {

        // Sset color
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(255, 255, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(177, 217, 212));
        XSSFColor greenOne = new XSSFColor(new java.awt.Color(149, 210, 64));
        XSSFColor orangeOne = new XSSFColor(new java.awt.Color(239, 150, 91));
        XSSFColor orangeTwo = new XSSFColor(new java.awt.Color(239, 209, 57));
        // set kích thước colum
        int[] widths = {21, 21, 21, 21, 21, 21, 21};
        //set endDate từ currentDate + 7
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate endDate = currentDate.plusWeeks(1).minusDays(1);
        // get số tuần từ thư viện
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = currentDate.get(weekFields.weekOfWeekBasedYear());

        // GET CHỦ ĐỀ
        String titleSchedule = scheduleInClassWeekResponses.get(0).getScheduleTitle();

        // fomat kiểu date
        String dateToStr = df.format(currentDate);
        String dateToStrSheet = df.format(endDate);
        //set data menu header
        String[] days = {"Thứ hai/Monday", "Thứ ba/Tuesday", "Thứ tư/Wednesday", "Thứ năm/Thursday", "Thứ sáu/Friday", "Thứ bảy/Sat", "Chủ nhật/Sun"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            // get nameSchool
            SchoolResponse school = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
            String schoolName = "";
            if (school != null) {
                schoolName = school.getSchoolName();
            }
            // get nameClass
            MaClassDTO classDTO = maClassService.findByIdMaClass(idSchool, idClass).stream().findFirst().orElse(null);
            String className = "";
            if (classDTO != null) {
                className = classDTO.getClassName();
            }

            // create sheet excel + nameSheet
            Sheet sheet = workbook.createSheet("Tuần_ " + weekNumber);

            // Với 4 dòng đầu tiên:
            for (int i = 0; i < 5; i++) {
                Row headerRow = sheet.createRow(i);
                for (int col = 0; col < days.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    if (col == 0 && i == 0) {
                        cell.setCellValue("KẾ HOẠCH GIẢNG DẠY/ WEEKLY PLAN");
                        CellStyle cellStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellStyle.setAlignment(HorizontalAlignment.CENTER);
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
                        cell.setCellValue("Thời gian: Tuần " + weekNumber + "(" + dateToStr + "-" + dateToStrSheet + ")");
                        CellStyle threeStyle = workbook.createCellStyle();
                        Font cellFont = workbook.createFont();
                        cellFont.setFontHeightInPoints((short) 11);
                        cellFont.setBold(true);
                        threeStyle.setFont(cellFont);
                        cell.setCellStyle(threeStyle);
                    } else if (col == 0 && i == 4) {
                        cell.setCellValue("Chủ đề: " + titleSchedule);
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
//              addMergedRegion : gộp ô; CellRangeAddress: tạo không gian mới
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, days.length));

            // style header row 4

            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 10);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            CellStyle headerKidsCellStyle = workbook.createCellStyle();
            ((XSSFCellStyle) headerKidsCellStyle).setFillForegroundColor(yellowOne);
            headerKidsCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerKidsCellStyle.setFont(headerFont);
            headerKidsCellStyle.setWrapText(true);
            //            headerKidsCellStyle.setFont();
            headerKidsCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerKidsCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerKidsCellStyle.setBorderBottom(BorderStyle.THIN);
            headerKidsCellStyle.setBorderTop(BorderStyle.THIN);
            headerKidsCellStyle.setBorderRight(BorderStyle.THIN);
            headerKidsCellStyle.setBorderLeft(BorderStyle.THIN);
            //style content morning
            CellStyle contentMorningStyle = workbook.createCellStyle();
            Font morningFont = workbook.createFont();
            morningFont.setBold(true);
            morningFont.setFontHeightInPoints((short) 10);
            morningFont.setColor(IndexedColors.BLACK.getIndex());
            contentMorningStyle.setFont(morningFont);
            contentMorningStyle.setWrapText(true);
            ((XSSFCellStyle) contentMorningStyle).setFillForegroundColor(greenOne);
            contentMorningStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            contentMorningStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentMorningStyle.setAlignment(HorizontalAlignment.CENTER);
            contentMorningStyle.setBorderBottom(BorderStyle.THIN);
            contentMorningStyle.setBorderTop(BorderStyle.THIN);
            contentMorningStyle.setBorderRight(BorderStyle.THIN);
            contentMorningStyle.setBorderLeft(BorderStyle.THIN);
            //Style content  Afternoon
            CellStyle contentAfternoonStyle = workbook.createCellStyle();
            Font afternoonFont = workbook.createFont();
            afternoonFont.setBold(true);
            afternoonFont.setFontHeightInPoints((short) 10);
            afternoonFont.setColor(IndexedColors.BLACK.getIndex());
            contentAfternoonStyle.setFont(afternoonFont);
            contentAfternoonStyle.setWrapText(true);
            ((XSSFCellStyle) contentAfternoonStyle).setFillForegroundColor(orangeOne);
            contentAfternoonStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            contentAfternoonStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentAfternoonStyle.setAlignment(HorizontalAlignment.CENTER);
            contentAfternoonStyle.setBorderBottom(BorderStyle.THIN);
            contentAfternoonStyle.setBorderTop(BorderStyle.THIN);
            contentAfternoonStyle.setBorderRight(BorderStyle.THIN);
            contentAfternoonStyle.setBorderLeft(BorderStyle.THIN);
            //Style content Evening
            CellStyle contentEveningStyle = workbook.createCellStyle();
            Font eveningFont = workbook.createFont();
            eveningFont.setBold(true);
            eveningFont.setFontHeightInPoints((short) 10);
            eveningFont.setColor(IndexedColors.BLACK.getIndex());
            contentEveningStyle.setFont(eveningFont);
            contentEveningStyle.setWrapText(true);
            ((XSSFCellStyle) contentEveningStyle).setFillForegroundColor(orangeTwo);
            contentEveningStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            contentEveningStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentEveningStyle.setAlignment(HorizontalAlignment.CENTER);
            contentEveningStyle.setBorderBottom(BorderStyle.THIN);
            contentEveningStyle.setBorderTop(BorderStyle.THIN);
            contentEveningStyle.setBorderRight(BorderStyle.THIN);
            contentEveningStyle.setBorderLeft(BorderStyle.THIN);
            //Style content  body color
            CellStyle contentBodyColorStyle = workbook.createCellStyle();
            Font bodyColorFont = workbook.createFont();
            bodyColorFont.setFontHeightInPoints((short) 10);
            bodyColorFont.setColor(IndexedColors.BLACK.getIndex());
            contentBodyColorStyle.setFont(bodyColorFont);
            contentBodyColorStyle.setWrapText(true);
            ((XSSFCellStyle) contentBodyColorStyle).setFillForegroundColor(blueOne);
            contentBodyColorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            contentBodyColorStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentBodyColorStyle.setAlignment(HorizontalAlignment.CENTER);
            contentBodyColorStyle.setBorderBottom(BorderStyle.THIN);
            contentBodyColorStyle.setBorderTop(BorderStyle.THIN);
            contentBodyColorStyle.setBorderRight(BorderStyle.THIN);
            contentBodyColorStyle.setBorderLeft(BorderStyle.THIN);
            //Style content body
            CellStyle contentBodyStyle = workbook.createCellStyle();
            contentBodyStyle.setWrapText(true);
            contentBodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentBodyStyle.setAlignment(HorizontalAlignment.LEFT);
            contentBodyStyle.setBorderBottom(BorderStyle.THIN);
            contentBodyStyle.setBorderTop(BorderStyle.THIN);
            contentBodyStyle.setBorderRight(BorderStyle.THIN);
            contentBodyStyle.setBorderLeft(BorderStyle.THIN);

            // Row for Header4
            int rowHeader = 5;
            Row headerRow4 = sheet.createRow(rowHeader);


            // Tạo cell đầu cố định
            Cell cellAttendance = headerRow4.createCell(0);
            cellAttendance.setCellValue("Thời gian");
            CellStyle cellAttendanceStyle = workbook.createCellStyle();
            Font headerAttendance = workbook.createFont();
            headerAttendance.setBold(true);
            headerAttendance.setColor(IndexedColors.RED.getIndex());
            cellAttendanceStyle.setFont(headerAttendance);
            ((XSSFCellStyle) cellAttendanceStyle).setFillForegroundColor(yellowOne);
            cellAttendanceStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellAttendanceStyle.setAlignment(HorizontalAlignment.CENTER);
            cellAttendanceStyle.setBorderBottom(BorderStyle.THIN);
            cellAttendanceStyle.setBorderTop(BorderStyle.THIN);
            cellAttendanceStyle.setBorderRight(BorderStyle.THIN);
            cellAttendanceStyle.setBorderLeft(BorderStyle.THIN);
            cellAttendance.setCellStyle(cellAttendanceStyle);
            sheet.setColumnWidth(0, 15 * 256);


            // Header
            for (int col = 1; col < days.length + 1; col++) {
                sheet.setColumnWidth(col, widths[col - 1] * 256);
                Cell cell = headerRow4.createCell(col);
                cell.setCellValue(days[col - 1]);
                cell.setCellStyle(headerKidsCellStyle);
            }

            // Tách dữ liệu, lấy số row cho các buổi sáng, chiều, tối, trả về morningList, afternoonList....
            Map<String, Integer> map = getRowMax(scheduleInClassWeekResponses);

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

            List<ScheduleModel> listScheduleModel = getDays(scheduleInClassWeekResponses, rowMorning, rowAfternoon, rowEvening);

            int rowIdx = 6;
            int rowIdxFixded = 6;
            for (ScheduleModel scheduleModel : listScheduleModel) {

                Row row = sheet.createRow(rowIdx++);

                if (rowIdx == rowIdxFixded + 1) {
                    if (rowMorning == 0) {
                        rowMorning++;
                    }
                    Cell cellHeadMorning = row.createCell(0);
                    cellHeadMorning.setCellValue("Buổi sáng/\n" + "Morning");
                    cellHeadMorning.setCellStyle(contentMorningStyle);
                } else if (rowIdx == rowIdxFixded + rowMorning + 1) {
                    if (rowAfternoon == 0) {
                        rowAfternoon++;
                    }
                    Cell cellAfternoon = row.createCell(0);
                    cellAfternoon.setCellValue("Buổi chiều/\n" + "Afternoon");
                    cellAfternoon.setCellStyle(contentAfternoonStyle);
                } else if (rowIdx == rowIdxFixded + rowMorning + rowAfternoon + 1) {
                    Cell cellEvening = row.createCell(0);
                    cellEvening.setCellValue("Buổi Tối/\n" + "Evening");
                    cellEvening.setCellStyle(contentEveningStyle);
                } else {
                    Cell cellCommon = row.createCell(0);
                    cellCommon.setCellStyle(contentBodyStyle);
                }
                List<String> listStrMonday = convertString(scheduleModel.getContentMonday());
                XSSFRichTextString rstMonday = contentBody(listStrMonday, workbook);
                Cell cellMonday = row.createCell(1);
                cellMonday.setCellValue(rstMonday);
                cellMonday.setCellStyle(contentBodyStyle);

                List<String> listStrTuesday = convertString(scheduleModel.getContentTuesday());
                XSSFRichTextString rstTuesday = contentBody(listStrTuesday, workbook);
                Cell cellTuesday = row.createCell(2);
                cellTuesday.setCellValue(rstTuesday);
                cellTuesday.setCellStyle(contentBodyStyle);

                List<String> listStrWednesday = convertString(scheduleModel.getContentWednesday());
                XSSFRichTextString rstWednesday = contentBody(listStrWednesday, workbook);
                Cell cellWednesday = row.createCell(3);
                cellWednesday.setCellValue(rstWednesday);
                cellWednesday.setCellStyle(contentBodyStyle);

                List<String> listStrThursday = convertString(scheduleModel.getContentThursday());
                XSSFRichTextString rstThursday = contentBody(listStrThursday, workbook);
                Cell cellThursday = row.createCell(4);
                cellThursday.setCellValue(rstThursday);
                cellThursday.setCellStyle(contentBodyStyle);

                List<String> listStrFriday = convertString(scheduleModel.getContentFriday());
                XSSFRichTextString rstFriday = contentBody(listStrFriday, workbook);
                Cell cellFriday = row.createCell(5);
                cellFriday.setCellValue(rstFriday);
                cellFriday.setCellStyle(contentBodyStyle);

                List<String> listStrSaturday = convertString(scheduleModel.getContentSaturday());
                XSSFRichTextString rstSaturday = contentBody(listStrSaturday, workbook);
                Cell cellSaturday = row.createCell(6);
                cellSaturday.setCellValue(rstSaturday);
                cellSaturday.setCellStyle(contentBodyStyle);

                List<String> listStrSunday = convertString(scheduleModel.getContentSunday());
                XSSFRichTextString rstSunday = contentBody(listStrSunday, workbook);
                Cell cellSunday = row.createCell(7);
                cellSunday.setCellValue(rstSunday);
                cellSunday.setCellStyle(contentBodyStyle);

            }
            if (rowIdxFixded != rowIdxFixded + rowMorning - 1 && rowMorning > 0) {
                sheet.addMergedRegion(new CellRangeAddress(rowIdxFixded, rowIdxFixded + rowMorning - 1, 0, 0));
            }
            if (rowIdxFixded + rowMorning != rowIdxFixded + rowMorning + rowAfternoon - 1 && rowAfternoon > 0) {
                sheet.addMergedRegion(new CellRangeAddress(rowIdxFixded + rowMorning, rowIdxFixded + rowMorning + rowAfternoon - 1, 0, 0));
            }
            if (rowIdxFixded + rowMorning + rowAfternoon != rowIdxFixded + rowMorning + rowAfternoon + rowEvening - 1 && rowEvening > 0) {
                sheet.addMergedRegion(new CellRangeAddress(rowIdxFixded + rowMorning + rowAfternoon, rowIdxFixded + rowMorning + rowAfternoon + rowEvening - 1, 0, 0));
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // importFiletoDB
    @Override
    public CreateMultiSchedule saveScheduleFileExcel(Long idSchool, UploadScheduleRequest uploadScheduleRequest) throws IOException {

        MultipartFile file = uploadScheduleRequest.getMultipartFile();
        InputStream inputStream = file.getInputStream();

        Workbook workbook = new XSSFWorkbook(inputStream);

        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = firstSheet.iterator();

        rowIterator.next(); // skip the header row


        List<List<String>> listMondays = new ArrayList<>();
        List<List<String>> listTuesdays = new ArrayList<>();
        List<List<String>> listWednesdays = new ArrayList<>();
        List<List<String>> lisThusdays = new ArrayList<>();
        List<List<String>> listFridays = new ArrayList<>();
        List<List<String>> listSaturdays = new ArrayList<>();
        List<List<String>> listSundays = new ArrayList<>();
        List<List<String>> listTimeDay = new ArrayList<>();
        List<List<List<String>>> createTabDay = new ArrayList<>();
        String scheduleTitleConvert = null;

        while (rowIterator.hasNext()) { // check row
            Row nextRow = rowIterator.next(); // next row
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            while (cellIterator.hasNext()) {
                Cell nextCell = cellIterator.next();
                int numberSize;
                if (nextRow.getRowNum() == 4 && nextCell.getColumnIndex() == 0) {
                    String scheduleTitleEx = nextCell.getStringCellValue();
                    scheduleTitleConvert = scheduleTitleEx.replace("Chủ đề: ", "");
                }
                if (nextRow.getRowNum() > 5) {
                    int columnIndex1 = nextCell.getColumnIndex(); // get giá trị ô
                    switch (columnIndex1) {
                        case 0:
                            String timeDay = nextCell.getStringCellValue();
                            List<String> contenDay = convertStrExcel(timeDay); // xử lý chuỗi
                            listTimeDay.add(contenDay);
                            break;
                        case 1:
                            String monday = nextCell.getStringCellValue(); // lấy giá trị trong ô 1
                            List<String> contenDay2 = convertStrExcel(monday); // xử lý chuỗi
                            listMondays.add(contenDay2);
                            break;
                        case 2:
                            String tuesday = nextCell.getStringCellValue();
                            List<String> contenDay3 = convertStrExcel(tuesday); // xử lý chuỗi
                            listTuesdays.add(contenDay3);
                            break;
                        case 3:
                            String wednesday = nextCell.getStringCellValue();
                            List<String> contenDay4 = convertStrExcel(wednesday); // xử lý chuỗi
                            listWednesdays.add(contenDay4);
                            break;
                        case 4:
                            String thusday = nextCell.getStringCellValue();
                            List<String> contenDay5 = convertStrExcel(thusday); // xử lý chuỗi
                            lisThusdays.add(contenDay5);

                            break;
                        case 5:
                            String friday = nextCell.getStringCellValue();
                            List<String> contenDay6 = convertStrExcel(friday); // xử lý chuỗi
                            listFridays.add(contenDay6);
                            break;
                        case 6:
                            String saturday = nextCell.getStringCellValue();
                            List<String> contenDay7 = convertStrExcel(saturday); // xử lý chuỗi
                            listSaturdays.add(contenDay7);
                            break;
                        case 7:
                            String sunday = nextCell.getStringCellValue();
                            List<String> contenDay8 = convertStrExcel(sunday); // xử lý chuỗi
                            listSundays.add(contenDay8);
                            break;
                    }

                }

            }
        }

        createTabDay.add(listMondays);
        createTabDay.add(listTuesdays);
        createTabDay.add(listWednesdays);
        createTabDay.add(lisThusdays);
        createTabDay.add(listFridays);
        createTabDay.add(listSaturdays);
        createTabDay.add(listSundays);
        int i = 0;
        int afternoon = 0;
        int evening = 0;
        int endDay = listTimeDay.size();
        for (List<String> x : listTimeDay) {
            if (x.get(0).equalsIgnoreCase("Buổi chiều/")) {
                afternoon = i;
            }
            if (x.get(0).equalsIgnoreCase("Buổi tối/")) {
                evening = i;
            }
            i++;


        }
        if (afternoon < 1 || evening < 1) {
            return null;
        }

        List<List<CreateTabDayInWeek>> listAllWeek = convertDay(createTabDay, afternoon, evening, endDay);
        List<CreateTabAllSchedule> createTabAllScheduleList = new ArrayList<>();
        listAllWeek.forEach(scheduleDayInWeek -> {
            CreateTabAllSchedule createTabAllSchedule = new CreateTabAllSchedule();
            List<CreateTabDayInWeek> createTabDayInWeekList = new ArrayList<>();
            scheduleDayInWeek.forEach(tabDayInWeek -> {
                CreateTabDayInWeek createTabDayInWeek = new CreateTabDayInWeek();
                createTabDayInWeek.setSessionDay(tabDayInWeek.getSessionDay());
                createTabDayInWeek.setTimeContent(tabDayInWeek.getTimeContent());
                createTabDayInWeek.setContentSchedule(tabDayInWeek.getContentSchedule());
//                        createTabDayInWeekList chứa các tiết trong ngày
                createTabDayInWeekList.add(createTabDayInWeek);
            });

            createTabAllSchedule.setCreateTabDayInWeek(createTabDayInWeekList);
            // createTabAllClassMenuList chứa 7 ngày trong 1 tuần
            createTabAllScheduleList.add(createTabAllSchedule);
        });

        CreateMultiSchedule createMultiSchedule = new CreateMultiSchedule();
        createMultiSchedule.setCreateTabAllSchedule(createTabAllScheduleList);
        createMultiSchedule.setListIdClass(uploadScheduleRequest.getListIdClass());
        createMultiSchedule.setWeekSchedule(uploadScheduleRequest.getWeekClassMenu());
        createMultiSchedule.setScheduleTitle(scheduleTitleConvert);

        return createMultiSchedule;
    }

    private List<String> convertStrExcel(String str) {
        List<String> list = new ArrayList<>();
        String cutStr = str.replace("*", "");
        String[] strArray = cutStr.split("\n");
        if (strArray.length > 1) {
            StringBuilder covertList = new StringBuilder();
            for (int i = 0; i < strArray.length; i++) {
                if (i == 0) {
                    list.add(strArray[0]);
                } else {
                    covertList.append(strArray[i]).append('\n');
                }
            }
            list.add(covertList.toString().trim().replaceAll(" +", " "));
        } else if (strArray.length == 1) {
            list.add("");
            list.add(strArray[0].trim().replaceAll(" +", " "));

        } else {
            list.add("");
            list.add("\n");
        }
        return list;
    }

    private List<List<CreateTabDayInWeek>> convertDay(List<List<List<String>>> createTabDay, int afternoon, int evening, int endDay) {
        List<List<CreateTabDayInWeek>> list = new ArrayList<>();
        createTabDay.forEach(dayInWeek -> {
            CreateTabAllSchedule createTabAllSchedule = new CreateTabAllSchedule();
            List<CreateTabAllSchedule> createTabAllClassMenuList = new ArrayList<>();
            List<CreateTabDayInWeek> createTabDayInWeekList = new ArrayList<>();


            for (int i = 0; i < afternoon; i++) {
                CreateTabDayInWeek createTabDayInWeeks = new CreateTabDayInWeek();

                createTabDayInWeeks.setSessionDay("Sáng");
                List<String> listTimeInDay = dayInWeek.get(i);
                if (listTimeInDay.size() != 0) {
                    createTabDayInWeeks.setTimeContent(listTimeInDay.get(0));

                    createTabDayInWeeks.setContentSchedule(listTimeInDay.get(1));
                    createTabDayInWeekList.add(createTabDayInWeeks);
                }
            }
            for (int k = afternoon; k < evening; k++) {
                CreateTabDayInWeek createTabDayInWeeks = new CreateTabDayInWeek();
                createTabDayInWeeks.setSessionDay("Chiều");
                List<String> listTimeInDay = dayInWeek.get(k);
                if (listTimeInDay.size() != 0) {
                    createTabDayInWeeks.setTimeContent(listTimeInDay.get(0));
                    createTabDayInWeeks.setContentSchedule(listTimeInDay.get(1));
                    createTabDayInWeekList.add(createTabDayInWeeks);
                }

            }
            for (int n = evening; n < endDay; n++) {
                CreateTabDayInWeek createTabDayInWeeks = new CreateTabDayInWeek();
                createTabDayInWeeks.setSessionDay("Tối");
                List<String> listTimeInDay = dayInWeek.get(n);
                if (listTimeInDay.size() != 0) {
                    createTabDayInWeeks.setTimeContent(listTimeInDay.get(0));
                    createTabDayInWeeks.setContentSchedule(listTimeInDay.get(1));
                    createTabDayInWeekList.add(createTabDayInWeeks);
                }

            }
            list.add(createTabDayInWeekList);

        });
        return list;
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
                            list.add("*" + scheduleInClassResponses.get(m).getTimeContent() + " \n" + scheduleInClassResponses.get(m).getContentSchedule());

                        } else {
                            list.add("\n" + scheduleInClassResponses.get(j).getContentSchedule());

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
                            list.add("*" + scheduleInClassResponses.get(m).getTimeContent() + " \n" + scheduleInClassResponses.get(m).getContentSchedule());
                        } else {
                            list.add("\n" + scheduleInClassResponses.get(m).getContentSchedule());
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
                                list.add("*" + scheduleInClassResponses.get(m).getTimeContent() + " \n" + scheduleInClassResponses.get(m).getContentSchedule());
                            } else {
                                list.add("\n" + scheduleInClassResponses.get(m).getContentSchedule());
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
        System.out.println(listSchedule);

        return listSchedule;
    }

    private List<String> convertString(String str) {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String[] strArray = str.split("\n");
        for (int i = 0; i < strArray.length; i++) {
//            if (i == 0) {
            list.add(strArray[i]);
//            } else {
//                sb.append(strArray[i]);
//            }
        }
//        String newWord = sb.toString();
//        list.add(newWord);
        return list;
    }

    private XSSFRichTextString contentBody(List<String> list, Workbook workbook) {

        XSSFRichTextString rts = new XSSFRichTextString("");

        Font bodyFont = workbook.createFont();
        bodyFont.setFontHeightInPoints((short) 10);
        bodyFont.setColor(IndexedColors.BLACK.getIndex());
        Font fontBoldOne = workbook.createFont();
        fontBoldOne.setBold(true); //set bold
        fontBoldOne.setColor(IndexedColors.BLACK.getIndex());
        fontBoldOne.setFontHeightInPoints((short) 10); //add font size

        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                rts.append(list.get(0) + "\n", (XSSFFont) fontBoldOne);

            } else {
                rts.append(list.get(i) + "\n", (XSSFFont) bodyFont);
            }


        }
        return rts;
    }
}



