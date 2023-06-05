package com.example.onekids_project.importexport.serviceimpl;

import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.importexport.model.ClassMenuModel;
import com.example.onekids_project.importexport.service.MenuExcelService;
import com.example.onekids_project.request.classmenu.CreateFileExcelRequest;
import com.example.onekids_project.request.classmenu.CreateTabAllClassMenu;
import com.example.onekids_project.request.classmenu.CreateMultiClassMenu;
import com.example.onekids_project.request.classmenu.CreateTabDayInWeek;
import com.example.onekids_project.response.classmenu.TabClassMenuByIdClassInWeek;
import com.example.onekids_project.response.classmenu.TabClassMenuDayWeekResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.service.servicecustom.MaClassService;
import com.example.onekids_project.service.servicecustom.SchoolService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
public class MenuExcelServiceImpl implements MenuExcelService {

    @Autowired
    SchoolService schoolService;

    @Autowired
    MaClassService maClassService;

    @Override
    public CreateMultiClassMenu saveMenuFileExcel(Long idSchool, CreateFileExcelRequest createFileExcelRequest) throws IOException {

        // ddoc file
        MultipartFile file = createFileExcelRequest.getMultipartFile();
        InputStream inputStream = file.getInputStream();

        Workbook workbook = new XSSFWorkbook(inputStream);

        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = firstSheet.iterator();

        rowIterator.next(); // skip the header row

        CreateTabAllClassMenu createTabAllClassMenu = new CreateTabAllClassMenu();
        List<String> listMondays = new ArrayList<>();
        List<String> listTuesdays = new ArrayList<>();
        List<String> listWednesdays = new ArrayList<>();
        List<String> lisThusdays = new ArrayList<>();
        List<String> listFridays = new ArrayList<>();
        List<String> listSaturdays = new ArrayList<>();
        List<String> listSundays = new ArrayList<>();
        List<CreateTabDayInWeek> createTabDayInWeeks = new ArrayList<>();
        List<List<CreateTabDayInWeek>> listMondays2 = new ArrayList<>();
        List<List<CreateTabDayInWeek>> listTuesdays3 = new ArrayList<>();
        List<List<CreateTabDayInWeek>> listWednesdays4 = new ArrayList<>();
        List<List<CreateTabDayInWeek>> lisThusdays5 = new ArrayList<>();
        List<List<CreateTabDayInWeek>> listFridays6 = new ArrayList<>();
        List<List<CreateTabDayInWeek>> listSaturdays7 = new ArrayList<>();
        List<List<CreateTabDayInWeek>> listSundays8 = new ArrayList<>();
        List<List<List<CreateTabDayInWeek>>> createTabAll = new ArrayList<>();
        int check = 0;
        while (rowIterator.hasNext()) { // có phần tử dòng tiếp theo?
            Row nextRow = rowIterator.next(); // next dòng
            check = nextRow.getRowNum();
            // get từ dòng 5 đến dòng 10
            if (nextRow.getRowNum() > 4 && nextRow.getRowNum() < 11) {

                // Khởi tạo cell
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                while (cellIterator.hasNext()) { //  ô tiếp theo?

                    Cell nextCell = cellIterator.next(); // next ô
                    int numberSize;

                    int columnIndex = nextCell.getColumnIndex(); // get giá trị ô
                    switch (columnIndex) { // chi lấy từ 1 đến 7, tương ứng thứ 2..chủ nhật
                        case 1:
                            String monday = nextCell.getStringCellValue();
                            listMondays.add(monday);
                            numberSize = listMondays.size();
                            List<String> contenDay = convertStrExcel(monday); // xử lý chuỗi
                            createTabDayInWeeks = convertDay(contenDay, numberSize); // set vào createTabDayInWeeks
                            listMondays2.add(createTabDayInWeeks);
                            break;
                        case 2:
                            String tuesday = nextCell.getStringCellValue();
                            listTuesdays.add(tuesday);
                            numberSize = listMondays.size();
                            List<String> contenDay3 = convertStrExcel(tuesday);
                            createTabDayInWeeks = convertDay(contenDay3, numberSize);
                            listTuesdays3.add(createTabDayInWeeks);
                            break;
                        case 3:
                            String wednesday = nextCell.getStringCellValue();
                            listWednesdays.add(wednesday);
                            numberSize = listWednesdays.size();
                            List<String> contenDay4 = convertStrExcel(wednesday);
                            createTabDayInWeeks = convertDay(contenDay4, numberSize);
                            listWednesdays4.add(createTabDayInWeeks);
                            break;
                        case 4:
                            String thusday = nextCell.getStringCellValue();
                            lisThusdays.add(thusday);
                            numberSize = listMondays.size();
                            List<String> contenDay5 = convertStrExcel(thusday);
                            createTabDayInWeeks = convertDay(contenDay5, numberSize);
                            lisThusdays5.add(createTabDayInWeeks);
                            break;
                        case 5:
                            String friday = nextCell.getStringCellValue();
                            listFridays.add(friday);
                            numberSize = listMondays.size();
                            List<String> contenDay6 = convertStrExcel(friday);
                            createTabDayInWeeks = convertDay(contenDay6, numberSize);
                            listFridays6.add(createTabDayInWeeks);
                            break;
                        case 6:
                            String saturday = nextCell.getStringCellValue();
                            listSaturdays.add(saturday);
                            numberSize = listMondays.size();
                            List<String> contenDay7 = convertStrExcel(saturday);
                            createTabDayInWeeks = convertDay(contenDay7, numberSize);
                            listSaturdays7.add(createTabDayInWeeks);
                            break;
                        case 7:
                            String sunday = nextCell.getStringCellValue();
                            listSundays.add(sunday);
                            numberSize = listMondays.size();
                            List<String> contenDay8 = convertStrExcel(sunday);
                            createTabDayInWeeks = convertDay(contenDay8, numberSize);
                            listSundays8.add(createTabDayInWeeks);
                            break;
                    }
                }
            }
        }
        if (check < 10) {
            return null;
        }
        if (listSundays8.size() == 6) {
            createTabAll.add(listMondays2);
            createTabAll.add(listTuesdays3);
            createTabAll.add(listWednesdays4);
            createTabAll.add(lisThusdays5);
            createTabAll.add(listFridays6);
            createTabAll.add(listSaturdays7);
            createTabAll.add(listSundays8);
        }
        CreateMultiClassMenu createMultiClassMenu = new CreateMultiClassMenu();
        List<CreateTabAllClassMenu> createTabAllClassMenuList = new ArrayList<>();
        // lấy mỗi ngày trong createTabAll
        createTabAll.forEach(menuDayInWeek -> {
            CreateTabAllClassMenu createTabAllClassMenu1 = new CreateTabAllClassMenu();
            List<CreateTabDayInWeek> createTabDayInWeekList = new ArrayList<>();
            // lấy mỗi bữa trong ngày set vào CreateTabDayInWeek
            menuDayInWeek.forEach(tabDayInWeek -> {
                CreateTabDayInWeek createTabDayInWeek = new CreateTabDayInWeek();
                createTabDayInWeek.setSessionDay(tabDayInWeek.get(0).getSessionDay());
                createTabDayInWeek.setTimeContent(tabDayInWeek.get(0).getTimeContent());
                createTabDayInWeek.setContentClassMenu(tabDayInWeek.get(0).getContentClassMenu());
//                        createTabDayInWeekList chứa 5 bữa trong ngày
                createTabDayInWeekList.add(createTabDayInWeek);
            });
            // set createTabDayInWeekList vào createTabAllClassMenu
            createTabAllClassMenu1.setCreateTabDayInWeek(createTabDayInWeekList);
            // createTabAllClassMenuList chứa 7 ngày trong 1 tuần
            createTabAllClassMenuList.add(createTabAllClassMenu1);
        });
//        createMultiClassMenu chứa 3 thuộc tính
        createMultiClassMenu.setCreateTabAllClassMenu(createTabAllClassMenuList);
        createMultiClassMenu.setListIdClass(createFileExcelRequest.getListIdClass());
        createMultiClassMenu.setWeekClassMenu(createFileExcelRequest.getWeekClassMenu());


        return createMultiClassMenu;
    }

    // Tách chuỗi lấy từ excel thành 2 phân tử
    private List<String> convertStrExcel(String str) {
        List<String> list = new ArrayList<>();
        String cutStr = str.replace("*", "");
        String[] strArray = cutStr.split("\n");
        if (strArray.length > 1) {
            StringBuilder covertList = new StringBuilder();
            for (int i = 0; i < strArray.length; i++) {
                if (i == 0) {
                    list.add(strArray[0].trim().replaceAll(" +", " "));
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
            list.add("");
        }
        return list;
    }

    // set createTabDayInWeeks các bữa trong 1 buổi
    private List<CreateTabDayInWeek> convertDay(List<String> contenDay, Integer numberSize) {
        List<CreateTabDayInWeek> list = new ArrayList<>();
        CreateTabDayInWeek createTabDayInWeeks = new CreateTabDayInWeek();
        switch (numberSize) {
            //sang
            case 1:
                createTabDayInWeeks.setSessionDay("Sáng");
                for (int i = 0; i < contenDay.size(); i++) {
                    if (i == 0) {
                        createTabDayInWeeks.setTimeContent(contenDay.get(0));
                    } else {
                        createTabDayInWeeks.setContentClassMenu(contenDay.get(1));
                    }
                }
                break;
            //phu sang
            case 2:
                createTabDayInWeeks.setSessionDay("Phụ Sáng");
                for (int i = 0; i < contenDay.size(); i++) {
                    if (i == 0) {
                        createTabDayInWeeks.setTimeContent(contenDay.get(0));
                    } else {
                        createTabDayInWeeks.setContentClassMenu(contenDay.get(1));
                    }
                }
                break;
            //trua
            case 3:
                createTabDayInWeeks.setSessionDay("Trưa");
                for (int i = 0; i < contenDay.size(); i++) {
                    if (i == 0) {
                        createTabDayInWeeks.setTimeContent(contenDay.get(0));
                    } else {
                        createTabDayInWeeks.setContentClassMenu(contenDay.get(1));
                    }
                }
                break;
            //chieu
            case 4:
                createTabDayInWeeks.setSessionDay("Chiều");
                for (int i = 0; i < contenDay.size(); i++) {
                    if (i == 0) {
                        createTabDayInWeeks.setTimeContent(contenDay.get(0));
                    } else {
                        createTabDayInWeeks.setContentClassMenu(contenDay.get(1));
                    }
                }
                break;
            //phuchieu
            case 5:
                createTabDayInWeeks.setSessionDay("Phụ chiều");
                for (int i = 0; i < contenDay.size(); i++) {
                    if (i == 0) {
                        createTabDayInWeeks.setTimeContent(contenDay.get(0));
                    } else {
                        createTabDayInWeeks.setContentClassMenu(contenDay.get(1));
                    }
                }
                break;
            //toi
            case 6:
                createTabDayInWeeks.setSessionDay("Tối");
                for (int i = 0; i < contenDay.size(); i++) {
                    if (i == 0) {
                        createTabDayInWeeks.setTimeContent(contenDay.get(0));
                    } else {
                        createTabDayInWeeks.setContentClassMenu(contenDay.get(1));
                    }
                }
                break;
        }
        list.add(createTabDayInWeeks);


        return list;
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

    /**
     * xuất file excel thực đơn lớp
     *
     * @param tabClassMenuByIdClassInWeekList
     * @param idSchool
     * @param idClass
     * @param currentDate
     * @return
     * @throws IOException
     */

    @Override
    public ByteArrayInputStream customMenuExcel(List<TabClassMenuByIdClassInWeek> tabClassMenuByIdClassInWeekList, Long idSchool, Long idClass, LocalDate currentDate) throws IOException {

        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor greenOne = new XSSFColor(new java.awt.Color(149, 210, 64));


        int[] widths = {15, 20, 20, 20, 20, 20, 20, 20};
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate endDate = currentDate.plusWeeks(1).minusDays(1);

        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = currentDate.get(weekFields.weekOfWeekBasedYear());

        String dateToStr = df.format(currentDate);
        String dateToStrSheet = df.format(endDate);
        String[] days = {"Bữa ăn", "Thứ hai/Monday", "Thứ ba/Tuesday", "Thứ tư/Wednesday", "Thứ năm/Thursday", "Thứ sáu/Friday", "Thứ bảy/Sat", "Chủ nhật/Sun"};
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
            Sheet sheet = workbook.createSheet("Tuần " + weekNumber);

            for (int i = 0; i < 4; i++) {
                Row headerRow = sheet.createRow(i);
                for (int col = 0; col < days.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    if (col == 0 && i == 0) {
                        cell.setCellValue("THỰC ĐƠN/MENU");
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
                    } else {
                        CellStyle cellStyle = workbook.createCellStyle();
                        cell.setCellStyle(cellStyle);
                    }


                }

            }
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, days.length));
            // header row 4
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


            //Style content  body color
            CellStyle contentBodyColorStyle = workbook.createCellStyle();
            Font bodyColorFont = workbook.createFont();
            bodyColorFont.setFontHeightInPoints((short) 10);
            bodyColorFont.setColor(IndexedColors.WHITE.getIndex());
            contentBodyColorStyle.setFont(bodyColorFont);
            contentBodyColorStyle.setWrapText(true);
            ((XSSFCellStyle) contentBodyColorStyle).setFillForegroundColor(greenOne);
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
            int rowHeader = 4;
            Row headerRow4 = sheet.createRow(rowHeader);
            for (int col = 0; col < days.length; col++) {
                sheet.setColumnWidth(col, widths[col] * 256);
                Cell cell = headerRow4.createCell(col);
                cell.setCellValue(days[col]);
                cell.setCellStyle(headerKidsCellStyle);
            }


            List<ClassMenuModel> listClassMenuModel = listClassMenuVM(tabClassMenuByIdClassInWeekList);

            int rowIdx = 5;
            int i = 0;
            for (ClassMenuModel scheduleVM : listClassMenuModel) {

                Row row = sheet.createRow(rowIdx++);
                String time = "";
                if (tabClassMenuByIdClassInWeekList.get(0).getTabClassMenuDayClassList().get(i).getSessionDay() != null) {
                    time = tabClassMenuByIdClassInWeekList.get(0).getTabClassMenuDayClassList().get(i).getSessionDay();
                }
                Cell cellHeaderRow = row.createCell(0);
                cellHeaderRow.setCellValue(time);
                cellHeaderRow.setCellStyle(contentBodyColorStyle);
                i++;


                List<String> listStrMonday = convertString(scheduleVM.getContentMonday());
                XSSFRichTextString rstMonday = contentBody(listStrMonday, workbook);
                Cell cellMonday = row.createCell(1);
                cellMonday.setCellValue(rstMonday);
                cellMonday.setCellStyle(contentBodyStyle);

                List<String> listStrTuesday = convertString(scheduleVM.getContentTuesday());
                XSSFRichTextString rstTuesday = contentBody(listStrTuesday, workbook);
                Cell cellTuesday = row.createCell(2);
                cellTuesday.setCellValue(rstTuesday);
                cellTuesday.setCellStyle(contentBodyStyle);

                List<String> listStrWednesday = convertString(scheduleVM.getContentWednesday());
                XSSFRichTextString rstWednesday = contentBody(listStrWednesday, workbook);
                Cell cellWednesday = row.createCell(3);
                cellWednesday.setCellValue(rstWednesday);
                cellWednesday.setCellStyle(contentBodyStyle);

                List<String> listStrThursday = convertString(scheduleVM.getContentThursday());
                XSSFRichTextString rstThursday = contentBody(listStrThursday, workbook);
                Cell cellThursday = row.createCell(4);
                cellThursday.setCellValue(rstThursday);
                cellThursday.setCellStyle(contentBodyStyle);

                List<String> listStrFriday = convertString(scheduleVM.getContentFriday());
                XSSFRichTextString rstFriday = contentBody(listStrFriday, workbook);
                Cell cellFriday = row.createCell(5);
                cellFriday.setCellValue(rstFriday);
                cellFriday.setCellStyle(contentBodyStyle);

                List<String> listStrSaturday = convertString(scheduleVM.getContentSaturday());
                XSSFRichTextString rstSaturday = contentBody(listStrSaturday, workbook);
                Cell cellSaturday = row.createCell(6);
                cellSaturday.setCellValue(rstSaturday);
                cellSaturday.setCellStyle(contentBodyStyle);

                List<String> listStrSunday = convertString(scheduleVM.getContentSunday());
                XSSFRichTextString rstSunday = contentBody(listStrSunday, workbook);
                Cell cellSunday = row.createCell(7);
                cellSunday.setCellValue(rstSunday);
                cellSunday.setCellStyle(contentBodyStyle);

            }


            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


}
