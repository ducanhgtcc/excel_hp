package com.example.onekids_project.service.serviceimpl.finance;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.model.finance.OrderNumberModel;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.finance.statistical.FinanceSearchKidsRequest;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.finance.exportimport.KidsPackageExport;
import com.example.onekids_project.response.finance.exportimport.KidsPackageInOutExport;
import com.example.onekids_project.response.finance.exportimport.KidsPackageOrderExport;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.finance.ExportImportFinanceService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.ExportExcelUtils;
import com.example.onekids_project.util.FinanceUltils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-03-20 10:24
 *
 * @author Nguyễn Thành
 */
@Service
public class ExportImportFinanceServiceImpl implements ExportImportFinanceService {

    @Autowired
    private FnOrderKidsRepository fnOrderKidsRepository;

    @Autowired
    private FnKidsPackageRepository fnKidsPackageRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Override
    public List<KidsPackageOrderExport> getKidsPackageInOrOut(List<Kids> kidsList, int year, int startMonth, int endMonth, String category) {
        List<KidsPackageOrderExport> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            List<FnOrderKids> fnOrderKidsList = fnOrderKidsRepository.findByKidsIdAndYearAndMonthGreaterThanEqualAndMonthLessThanEqual(x.getId(), year, startMonth, endMonth);
            KidsPackageOrderExport model = new KidsPackageOrderExport();
            List<String> codeList = new ArrayList<>();
            double money = 0;
            double moneyPaid = 0;
            for (FnOrderKids y : fnOrderKidsList) {
                List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndFnPackageCategoryAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), category, y.getMonth(), y.getYear());
                if (FinanceUltils.getOrderStatus(fnKidsPackageList)) {
                    money += fnKidsPackageList.stream().mapToDouble(FinanceUltils::getMoneyCalculate).sum();
                    moneyPaid += fnKidsPackageList.stream().mapToDouble(FnKidsPackage::getPaid).sum();
                    codeList.add(y.getCode());
                }
            }
            if (CollectionUtils.isNotEmpty(codeList)) {
                model.setFullName(x.getFullName());
                model.setBirthDay(x.getBirthDay());
                model.setCodeList(codeList);
                model.setMoneyTotal(money);
                model.setMoneyPaidTotal(moneyPaid);
                model.setMoneyRemainTotal(model.getMoneyTotal() - model.getMoneyPaidTotal());
                responseList.add(model);
            }
        });
        return responseList;
    }

    @Override
    public List<KidsPackageInOutExport> getKidsPackageInAndOut(List<Kids> kidsList, int year, int startMonth, int endMonth) {
        List<KidsPackageInOutExport> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            KidsPackageInOutExport model = new KidsPackageInOutExport();
            List<FnOrderKids> fnOrderKidsList = fnOrderKidsRepository.findByKidsIdAndYearAndMonthGreaterThanEqualAndMonthLessThanEqual(x.getId(), year, startMonth, endMonth);
            double moneyIn = 0;
            double moneyOut = 0;
            double moneyPaidIn = 0;
            double moneyPaidOut = 0;
            List<String> codeList = new ArrayList<>();
            for (FnOrderKids y : fnOrderKidsList) {
                List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), y.getMonth(), y.getYear());
                if (FinanceUltils.getOrderStatus(fnKidsPackageList)) {
                    List<FnKidsPackage> inList = fnKidsPackageList.stream().filter(a -> a.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
                    List<FnKidsPackage> outList = fnKidsPackageList.stream().filter(a -> a.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
                    moneyIn += inList.stream().mapToDouble(FinanceUltils::getMoneyCalculate).sum();
                    moneyOut += outList.stream().mapToDouble(FinanceUltils::getMoneyCalculate).sum();
                    moneyPaidIn += inList.stream().mapToDouble(FnKidsPackage::getPaid).sum();
                    moneyPaidOut += outList.stream().mapToDouble(FnKidsPackage::getPaid).sum();
                    codeList.add(y.getCode());
                }
            }
            if (CollectionUtils.isNotEmpty(codeList)) {
                model.setFullName(x.getFullName());
                model.setBirthDay(x.getBirthDay());
                model.setMoneyTotal(moneyIn - moneyOut);
                model.setMoneyPaidTotal(moneyPaidIn - moneyPaidOut);
                double totalRemain = model.getMoneyTotal() - model.getMoneyPaidTotal();
                if (totalRemain > 0) {
                    model.setMoneyRemainInTotal(totalRemain);
                } else {
                    model.setMoneyRemainOutTotal(Math.abs(totalRemain));
                }
                model.setCodeList(codeList);
                responseList.add(model);
            }
        });
        return responseList;
    }

    @Override
    public List<KidsPackageInOutExport> getKidsPackageInAndOutTrue(List<Kids> kidsList, int year, int startMonth, int endMonth) {
        List<KidsPackageInOutExport> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            KidsPackageInOutExport model = new KidsPackageInOutExport();
            List<FnOrderKids> fnOrderKidsList = fnOrderKidsRepository.findByKidsIdAndYearAndMonthGreaterThanEqualAndMonthLessThanEqual(x.getId(), year, startMonth, endMonth);
            double moneyIn = 0;
            double moneyOut = 0;
            List<String> codeList = new ArrayList<>();
            for (FnOrderKids y : fnOrderKidsList) {
                List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), y.getMonth(), y.getYear());
                OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderModel(fnKidsPackageList);
                if (orderNumberModel.getTotalNumber() > 0 && orderNumberModel.getTotalNumber() == orderNumberModel.getEnoughNumber()) {
                    List<FnKidsPackage> inList = fnKidsPackageList.stream().filter(a -> a.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
                    List<FnKidsPackage> outList = fnKidsPackageList.stream().filter(a -> a.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
                    moneyIn += inList.stream().mapToDouble(FinanceUltils::getMoneyCalculate).sum();
                    moneyOut += outList.stream().mapToDouble(FinanceUltils::getMoneyCalculate).sum();
                    codeList.add(y.getCode());
                }
            }
            if (CollectionUtils.isNotEmpty(codeList)) {
                model.setFullName(x.getFullName());
                model.setBirthDay(x.getBirthDay());
                model.setMoneyTotal(moneyIn - moneyOut);
                model.setCodeList(codeList);
                responseList.add(model);
            }
        });
        return responseList;
    }

    @Override
    public List<KidsPackageInOutExport> getKidsPackageInAndOutTotal(List<Kids> kidsList, int year, int startMonth, int endMonth) {
        List<KidsPackageInOutExport> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            KidsPackageInOutExport model = new KidsPackageInOutExport();
            List<FnOrderKids> fnOrderKidsList = fnOrderKidsRepository.findByKidsIdAndYearAndMonthGreaterThanEqualAndMonthLessThanEqual(x.getId(), year, startMonth, endMonth);
            double moneyIn = 0;
            double moneyOut = 0;
            double moneyPaidIn = 0;
            double moneyPaidOut = 0;
            List<String> codeList = new ArrayList<>();
            for (FnOrderKids y : fnOrderKidsList) {
                List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), y.getMonth(), y.getYear());
                List<FnKidsPackage> inList = fnKidsPackageList.stream().filter(a -> a.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
                List<FnKidsPackage> outList = fnKidsPackageList.stream().filter(a -> a.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
                moneyIn += inList.stream().mapToDouble(FinanceUltils::getMoneyCalculate).sum();
                moneyOut += outList.stream().mapToDouble(FinanceUltils::getMoneyCalculate).sum();
                moneyPaidIn += inList.stream().mapToDouble(FnKidsPackage::getPaid).sum();
                moneyPaidOut += outList.stream().mapToDouble(FnKidsPackage::getPaid).sum();
                codeList.add(y.getCode());
            }
            if (CollectionUtils.isNotEmpty(codeList)) {
                model.setFullName(x.getFullName());
                model.setBirthDay(x.getBirthDay());
                model.setMoneyTotal(moneyIn - moneyOut);
                model.setMoneyPaidTotal(moneyPaidIn - moneyPaidOut);
                double totalRemain = model.getMoneyTotal() - model.getMoneyPaidTotal();
                if (totalRemain > 0) {
                    model.setMoneyRemainInTotal(totalRemain);
                } else {
                    model.setMoneyRemainOutTotal(Math.abs(totalRemain));
                }
                model.setCodeList(codeList);
                responseList.add(model);
            }
        });
        return responseList;
    }

    @Override
    public List<KidsPackageExport> getKidsPackageOrder(List<Kids> kidsList, int year, int startMonth, int endMonth) {
        List<KidsPackageExport> dataList = new ArrayList<>();
        kidsList.forEach(x -> {
            List<String> codeList = new ArrayList<>();
            List<FnOrderKids> fnOrderKidsList = fnOrderKidsRepository.findByKidsIdAndYearAndMonthGreaterThanEqualAndMonthLessThanEqual(x.getId(), year, startMonth, endMonth);
            fnOrderKidsList.forEach(y -> {
                List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), y.getMonth(), y.getYear());
                if (FinanceUltils.getOrderStatus(fnKidsPackageList)) {
                    codeList.add(y.getCode());
                }
            });
            if (CollectionUtils.isNotEmpty(codeList)) {
                KidsPackageExport model = new KidsPackageExport();
                model.setFullName(x.getFullName());
                model.setBirthDay(x.getBirthDay());
                model.setCodeList(codeList);
                dataList.add(model);
            }
        });
        return dataList;
    }

    @Override
    public ByteArrayInputStream exportFinanceKid(UserPrincipal principal, FinanceSearchKidsRequest request) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(131, 164, 196));
        XSSFColor headerColor = new XSSFColor(new java.awt.Color(0, 137, 203));

        int[] widths = {5, 30, 15, 60, 15, 15, 15};
        Long idSchool = principal.getIdSchoolLogin();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String[] columns = {"STT", "2", "3", "4", "5", "6", "7"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
            String schoolName = school.getSchoolName();
            Long idClass = request.getIdClass();
            String year = request.getYear();
            year.substring(0, 3);
            String newYear = year.substring(0, 4);
            int startMonth = Integer.parseInt(request.getStartMonth());
            int endMonth = Integer.parseInt(request.getEndMonth());
            int yearint = Integer.parseInt(newYear);
            if (idClass == null) {
                List<Long> idClassList = maClassRepository.findIdClassInSchool(idSchool);
                idClassList.forEach(a -> {
                    MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(a).orElseThrow();
                    List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(a, request);
                    Sheet sheet = workbook.createSheet(maClass.getClassName());
                    for (int i = 0; i < 4; i++) {
                        Row headerRow = sheet.createRow(i);
                        for (int col = 0; col < columns.length; col++) {
                            Cell cell = headerRow.createCell(col);
                            if (col == 0 && i == 0) {
                                cell.setCellValue("DANH SÁCH HỌC SINH CHƯA HOÀN THÀNH KHOẢN THU ");
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
                                cell.setCellValue("Lớp: " + maClass.getClassName());
                                CellStyle threeStyle = workbook.createCellStyle();
                                org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
                                cellFont.setFontHeightInPoints((short) 11);
                                cellFont.setBold(true);
                                threeStyle.setFont(cellFont);
                                cell.setCellStyle(threeStyle);
                            } else if (col == 0) {
                                cell.setCellValue("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear);
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
                    sheet.createFreezePane(2, 6);
                    //Style content  status
                    CellStyle contentStatusStyle = workbook.createCellStyle();
                    org.apache.poi.ss.usermodel.Font contentStatusFont = workbook.createFont();
                    contentStatusFont.setColor(IndexedColors.BLACK.getIndex());
                    contentStatusStyle.setFont(contentStatusFont);
                    contentStatusStyle.setWrapText(true);
                    Font headerStatus = workbook.createFont();
                    headerStatus.setBold(true);
                    contentStatusStyle.setFont(headerStatus);
                    ((XSSFCellStyle) contentStatusStyle).setFillForegroundColor(headerColor);
                    contentStatusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    contentStatusStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    contentStatusStyle.setAlignment(HorizontalAlignment.CENTER);
                    contentStatusStyle.setBorderBottom(BorderStyle.THIN);
                    contentStatusStyle.setBorderTop(BorderStyle.THIN);
                    contentStatusStyle.setBorderRight(BorderStyle.THIN);
                    contentStatusStyle.setBorderLeft(BorderStyle.THIN);

                    //Style content  Meal
                    CellStyle contentMealStyle = workbook.createCellStyle();
                    org.apache.poi.ss.usermodel.Font contentMealFont = workbook.createFont();
                    contentMealFont.setColor(IndexedColors.BLACK.getIndex());
                    contentMealStyle.setFont(contentMealFont);
                    contentMealStyle.setWrapText(true);
                    ((XSSFCellStyle) contentMealStyle).setFillForegroundColor(greyOne);
                    contentMealStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    contentMealStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    contentMealStyle.setAlignment(HorizontalAlignment.CENTER);
                    contentMealStyle.setBorderBottom(BorderStyle.THIN);
                    contentMealStyle.setBorderTop(BorderStyle.THIN);
                    contentMealStyle.setBorderRight(BorderStyle.THIN);
                    contentMealStyle.setBorderLeft(BorderStyle.THIN);

                    //Style content  Meal
                    CellStyle contentMealStyleName = workbook.createCellStyle();
                    org.apache.poi.ss.usermodel.Font contentMealFontName = workbook.createFont();
                    contentMealFontName.setColor(IndexedColors.BLACK.getIndex());
                    contentMealStyleName.setFont(contentMealFontName);
                    contentMealStyleName.setWrapText(true);
                    ((XSSFCellStyle) contentMealStyleName).setFillForegroundColor(greyOne);
                    contentMealStyleName.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    contentMealStyleName.setVerticalAlignment(VerticalAlignment.CENTER);
                    contentMealStyleName.setAlignment(HorizontalAlignment.LEFT);
                    contentMealStyleName.setBorderBottom(BorderStyle.THIN);
                    contentMealStyleName.setBorderTop(BorderStyle.THIN);
                    contentMealStyleName.setBorderRight(BorderStyle.THIN);
                    contentMealStyleName.setBorderLeft(BorderStyle.THIN);

                    // header row 5
                    org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                    headerFont.setColor(IndexedColors.BLACK.getIndex());
                    CellStyle headerKidsCellStyle = workbook.createCellStyle();
                    ((XSSFCellStyle) headerKidsCellStyle).setFillForegroundColor(blueOne);

                    int rowParent = 4;
                    Row headerRow5 = sheet.createRow(rowParent);
                    for (int col = 0; col < 6; col++) {
                        Cell cell = headerRow5.createCell(col);
                        CellStyle cellHeaderRow5 = workbook.createCellStyle();
                        cellHeaderRow5.setBorderBottom(BorderStyle.THIN);
                        cellHeaderRow5.setBorderTop(BorderStyle.THIN);
                        cellHeaderRow5.setBorderRight(BorderStyle.THIN);
                        cellHeaderRow5.setBorderLeft(BorderStyle.THIN);
                        cell.setCellStyle(cellHeaderRow5);
                    }

                    int rowChild = 5;
                    Cell cellLesson0 = headerRow5.createCell(0);
                    cellLesson0.setCellValue("STT");
//                    cellLesson0.setBold(true);
                    cellLesson0.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle0 = workbook.createCellStyle();
                    cellLateStyle0.setAlignment(HorizontalAlignment.CENTER);
                    Cell cellLesson1 = headerRow5.createCell(1);
                    cellLesson1.setCellValue("Tên học sinh");
                    cellLesson1.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle1 = workbook.createCellStyle();
                    cellLateStyle1.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                    cellLateStyle1.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson2 = headerRow5.createCell(2);
                    cellLesson2.setCellValue("Ngày sinh");
                    cellLesson2.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle2 = workbook.createCellStyle();
                    cellLateStyle2.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson3 = headerRow5.createCell(3);
                    cellLesson3.setCellValue("Danh sách mã hóa đơn");
                    cellLesson3.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle3 = workbook.createCellStyle();
                    cellLateStyle3.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson4 = headerRow5.createCell(4);
                    cellLesson4.setCellValue("Tổng phải thu");
                    cellLesson4.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle4 = workbook.createCellStyle();
                    cellLateStyle4.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson5 = headerRow5.createCell(5);
                    cellLesson5.setCellValue("Đã thu");
                    cellLesson5.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle5 = workbook.createCellStyle();
                    cellLateStyle5.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson6 = headerRow5.createCell(6);
                    cellLesson6.setCellValue("Còn lại phải thu");
                    cellLesson6.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle6 = workbook.createCellStyle();
                    cellLateStyle6.setAlignment(HorizontalAlignment.CENTER);
                    Row headerRow6 = sheet.createRow(rowChild);
                    for (int col = 0; col < 7; col++) {
                        sheet.setColumnWidth(col, widths[col] * 256);
                        Cell cell = headerRow6.createCell(col);
                    }
                    int rowIdx = 5;
                    int i = 0;
                    List<KidsPackageOrderExport> dataKidList = this.getKidsPackageInOrOut(kidsList, yearint, startMonth, endMonth, FinanceConstant.CATEGORY_IN);
                    for (KidsPackageOrderExport x : dataKidList) {
                        i -= -1;
                        Row row = sheet.createRow(rowIdx++);

                        Cell cellId = row.createCell(0);
                        cellId.setCellValue(i);
                        cellId.setCellStyle(contentMealStyle);

                        Cell cellKidName = row.createCell(1);
                        cellKidName.setCellValue(x.getFullName());
                        cellKidName.setCellStyle(contentMealStyleName);

                        String code = x.getCodeList().toString().replace("[", "");
                        String dataCode = code.replace("]", "");


                        Cell cellAbsentLetterYes = row.createCell(2);
                        cellAbsentLetterYes.setCellValue(ConvertData.convertLocalDateToString(x.getBirthDay()));
                        cellAbsentLetterYes.setCellStyle(contentMealStyle);

                        Cell cellAbsentLetterNo = row.createCell(3);
                        cellAbsentLetterNo.setCellValue(dataCode);
                        cellAbsentLetterNo.setCellStyle(contentMealStyleName);


                        double moneytotal = x.getMoneyTotal();
                        double moneypaidtotal = x.getMoneyPaidTotal();
                        double moneyremaintotal = x.getMoneyRemainTotal();

                        DecimalFormat formatter = new DecimalFormat("###,###,###");
                        String total = formatter.format(moneytotal);
                        String totalFill = total.replace(",", ".");

                        String paidtotal = formatter.format(moneypaidtotal);
                        String paidtotalFill = paidtotal.replace(",", ".");

                        String remainTotal = formatter.format(moneyremaintotal);
                        String remainTotalFill = remainTotal.replace(",", ".");

                        Cell cellAbsentStatus = row.createCell(4);
                        cellAbsentStatus.setCellValue(totalFill);
                        cellAbsentStatus.setCellStyle(contentMealStyle);

                        Cell cellMorningYes = row.createCell(5);
                        cellMorningYes.setCellValue(paidtotalFill);
                        cellMorningYes.setCellStyle(contentMealStyle);

                        Cell cellMorningNo = row.createCell(6);
                        cellMorningNo.setCellValue(remainTotalFill);
                        cellMorningNo.setCellStyle(contentMealStyle);
                    }
                });
            } else {
                Long idClassSelect = request.getIdClass();
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClassSelect).orElseThrow();
                List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(idClassSelect, request);
                Sheet sheet = workbook.createSheet(maClass.getClassName());
                for (int i = 0; i < 4; i++) {
                    Row headerRow = sheet.createRow(i);
                    for (int col = 0; col < columns.length; col++) {
                        Cell cell = headerRow.createCell(col);
                        if (col == 0 && i == 0) {
                            cell.setCellValue("DANH SÁCH HỌC SINH CHƯA HOÀN THÀNH KHOẢN THU ");
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
                            cell.setCellValue("Lớp: " + maClass.getClassName());
                            CellStyle threeStyle = workbook.createCellStyle();
                            org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
                            cellFont.setFontHeightInPoints((short) 11);
                            cellFont.setBold(true);
                            threeStyle.setFont(cellFont);
                            cell.setCellStyle(threeStyle);
                        } else if (col == 0) {
                            cell.setCellValue("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + "năm " + newYear);
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
                sheet.createFreezePane(2, 6);
                //Style content  status
                CellStyle contentStatusStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font contentStatusFont = workbook.createFont();
                contentStatusFont.setColor(IndexedColors.BLACK.getIndex());
                contentStatusStyle.setFont(contentStatusFont);
                contentStatusStyle.setWrapText(true);
                Font headerStatus = workbook.createFont();
                headerStatus.setBold(true);
                contentStatusStyle.setFont(headerStatus);
                ((XSSFCellStyle) contentStatusStyle).setFillForegroundColor(headerColor);
                contentStatusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentStatusStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentStatusStyle.setAlignment(HorizontalAlignment.CENTER);
                contentStatusStyle.setBorderBottom(BorderStyle.THIN);
                contentStatusStyle.setBorderTop(BorderStyle.THIN);
                contentStatusStyle.setBorderRight(BorderStyle.THIN);
                contentStatusStyle.setBorderLeft(BorderStyle.THIN);


                //Style content  Meal
                CellStyle contentMealStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font contentMealFont = workbook.createFont();
                contentMealFont.setColor(IndexedColors.BLACK.getIndex());
                contentMealStyle.setFont(contentMealFont);
                contentMealStyle.setWrapText(true);
                ((XSSFCellStyle) contentMealStyle).setFillForegroundColor(greyOne);
                contentMealStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentMealStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentMealStyle.setAlignment(HorizontalAlignment.CENTER);
                contentMealStyle.setBorderBottom(BorderStyle.THIN);
                contentMealStyle.setBorderTop(BorderStyle.THIN);
                contentMealStyle.setBorderRight(BorderStyle.THIN);
                contentMealStyle.setBorderLeft(BorderStyle.THIN);

                //Style content  Meal
                CellStyle contentMealStyleName = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font contentMealFontName = workbook.createFont();
                contentMealFontName.setColor(IndexedColors.BLACK.getIndex());
                contentMealStyleName.setFont(contentMealFontName);
                contentMealStyleName.setWrapText(true);
                ((XSSFCellStyle) contentMealStyleName).setFillForegroundColor(greyOne);
                contentMealStyleName.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentMealStyleName.setVerticalAlignment(VerticalAlignment.CENTER);
                contentMealStyleName.setAlignment(HorizontalAlignment.LEFT);
                contentMealStyleName.setBorderBottom(BorderStyle.THIN);
                contentMealStyleName.setBorderTop(BorderStyle.THIN);
                contentMealStyleName.setBorderRight(BorderStyle.THIN);
                contentMealStyleName.setBorderLeft(BorderStyle.THIN);

                // header row 5
                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setFontHeightInPoints((short) 10);
                headerFont.setColor(IndexedColors.BLACK.getIndex());
                CellStyle headerKidsCellStyle = workbook.createCellStyle();
                ((XSSFCellStyle) headerKidsCellStyle).setFillForegroundColor(yellowOne);

                int rowParent = 4;
                Row headerRow5 = sheet.createRow(rowParent);
                // Header 4
                for (int col = 0; col < 6; col++) {
                    Cell cell = headerRow5.createCell(col);
                    CellStyle cellHeaderRow5 = workbook.createCellStyle();
                    cellHeaderRow5.setBorderBottom(BorderStyle.THIN);
                    cellHeaderRow5.setBorderTop(BorderStyle.THIN);
                    cellHeaderRow5.setBorderRight(BorderStyle.THIN);
                    cellHeaderRow5.setBorderLeft(BorderStyle.THIN);
                    cell.setCellStyle(cellHeaderRow5);
                }

//             Row for Header
                int rowChild = 5;
                Cell cellLesson0 = headerRow5.createCell(0);
//                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 0, 0));
                cellLesson0.setCellValue("STT");
                cellLesson0.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle0 = workbook.createCellStyle();
                cellLateStyle0.setAlignment(HorizontalAlignment.CENTER);


                Cell cellLesson1 = headerRow5.createCell(1);
                cellLesson1.setCellValue("Tên học sinh");
                cellLesson1.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle1 = workbook.createCellStyle();
                cellLateStyle1.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                cellLateStyle1.setAlignment(HorizontalAlignment.CENTER);
//                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 1, 1));

                Cell cellLesson2 = headerRow5.createCell(2);
                cellLesson2.setCellValue("Ngày sinh");
                cellLesson2.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle2 = workbook.createCellStyle();
                cellLateStyle2.setAlignment(HorizontalAlignment.CENTER);
//                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 2, 2));

                Cell cellLesson3 = headerRow5.createCell(3);
                cellLesson3.setCellValue("Danh sách mã hóa đơn");
                cellLesson3.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle3 = workbook.createCellStyle();
                cellLateStyle3.setAlignment(HorizontalAlignment.CENTER);
//                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 3, 3));

                Cell cellLesson4 = headerRow5.createCell(4);
                cellLesson4.setCellValue("Tổng phải chi");
                cellLesson4.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle4 = workbook.createCellStyle();
                cellLateStyle4.setAlignment(HorizontalAlignment.CENTER);
//                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 4, 4));

                Cell cellLesson5 = headerRow5.createCell(5);
                cellLesson5.setCellValue("Đã chi");
                cellLesson5.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle5 = workbook.createCellStyle();
                cellLateStyle5.setAlignment(HorizontalAlignment.CENTER);
//                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 5, 5));

                Cell cellLesson6 = headerRow5.createCell(6);
                cellLesson6.setCellValue("Còn lại phải chi");
                cellLesson6.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle6 = workbook.createCellStyle();
                cellLateStyle6.setAlignment(HorizontalAlignment.CENTER);
//                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 6, 6));
                Row headerRow6 = sheet.createRow(rowChild);
                for (int col = 0; col < 7; col++) {
                    sheet.setColumnWidth(col, widths[col] * 256);
                    Cell cell = headerRow6.createCell(col);
                }

                int rowIdx = 5;
                int i = 0;

                List<KidsPackageOrderExport> dataKidList = this.getKidsPackageInOrOut(kidsList, yearint, startMonth, endMonth, "in");

                for (KidsPackageOrderExport x : dataKidList) {
                    i -= -1;

                    Row row = sheet.createRow(rowIdx++);

                    Cell cellId = row.createCell(0);
                    cellId.setCellValue(i);
                    cellId.setCellStyle(contentMealStyle);

                    Cell cellKidName = row.createCell(1);
                    cellKidName.setCellValue(x.getFullName());
                    cellKidName.setCellStyle(contentMealStyleName);

                    String code = x.getCodeList().toString().replace("[", "");
                    String dataCode = code.replace("]", "");

                    Cell cellAbsentLetterYes = row.createCell(2);
                    cellAbsentLetterYes.setCellValue(ConvertData.convertLocalDateToString(x.getBirthDay()));
                    cellAbsentLetterYes.setCellStyle(contentMealStyle);

                    Cell cellAbsentLetterNo = row.createCell(3);
                    cellAbsentLetterNo.setCellValue(dataCode);
                    cellAbsentLetterNo.setCellStyle(contentMealStyleName);

                    double moneytotal = x.getMoneyTotal();
                    double moneypaidtotal = x.getMoneyPaidTotal();
                    double moneyremaintotal = x.getMoneyRemainTotal();

                    DecimalFormat formatter = new DecimalFormat("###,###,###");
                    String total = formatter.format(moneytotal);
                    String totalFill = total.replace(",", ".");

                    String paidtotal = formatter.format(moneypaidtotal);
                    String paidtotalFill = paidtotal.replace(",", ".");

                    String remainTotal = formatter.format(moneyremaintotal);
                    String remainTotalFill = remainTotal.replace(",", ".");

                    Cell cellAbsentStatus = row.createCell(4);
                    cellAbsentStatus.setCellValue(totalFill);
                    cellAbsentStatus.setCellStyle(contentMealStyle);

                    Cell cellMorningYes = row.createCell(5);
                    cellMorningYes.setCellValue(paidtotalFill);
                    cellMorningYes.setCellStyle(contentMealStyle);

                    Cell cellMorningNo = row.createCell(6);
                    cellMorningNo.setCellValue(remainTotalFill);
                    cellMorningNo.setCellStyle(contentMealStyle);
                }

            }
            // xuất học sinh theo trường
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public List<ExcelResponse> exportFinanceKidNew(UserPrincipal principal, FinanceSearchKidsRequest request) {
        List<ExcelResponse> responseList = new ArrayList<>();

        Long idSchool = principal.getIdSchoolLogin();
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
        String schoolName = school.getSchoolName();
        Long idClass = request.getIdClass();
        String year = request.getYear();
//        year.substring(0, 3);
        String newYear = year.substring(0, 4);
        int startMonth = Integer.parseInt(request.getStartMonth());
        int endMonth = Integer.parseInt(request.getEndMonth());
        int yearint = Integer.parseInt(newYear);
        if (idClass == null) {
            List<Long> idClassList = maClassRepository.findIdClassInSchool(idSchool);
            idClassList.forEach(a -> {
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(a).orElseThrow();
                List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(a, request);
                List<ExcelData> bodyList = new ArrayList<>();
                ExcelResponse response = new ExcelResponse();
                List<String> headerStringList = Arrays.asList("DANH SÁCH HỌC SINH CHƯA HOÀN THÀNH KHOẢN THU", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_DATE.concat("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear));
                List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
                response.setSheetName(maClass.getClassName());
                response.setHeaderList(headerList);
                List<KidsPackageOrderExport> dataKidList = this.getKidsPackageInOrOut(kidsList, yearint, startMonth, endMonth, FinanceConstant.CATEGORY_IN);
                long i = 0;
                for (KidsPackageOrderExport x : dataKidList) {
                    String code = x.getCodeList().toString().replace("[", "");
                    String dataCode = code.replace("]", "");

                    String moneyTotal = this.setMoneyDouble(x.getMoneyTotal());
                    String moneyPaidTotal = this.setMoneyDouble(x.getMoneyPaidTotal());
                    String moneyRemainTotal = this.setMoneyDouble(x.getMoneyRemainTotal());

                    List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getFullName(), ConvertData.convertLocalDateToString(x.getBirthDay()), dataCode,
                            moneyTotal, moneyPaidTotal, moneyRemainTotal);
                    ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                    bodyList.add(modelData);
                }
                response.setBodyList(bodyList);
                responseList.add(response);
            });
        } else {
            Long idClassSelect = request.getIdClass();
            MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClassSelect).orElseThrow();
            List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(idClassSelect, request);
            List<ExcelData> bodyList = new ArrayList<>();
            ExcelResponse response = new ExcelResponse();
            List<String> headerStringList = Arrays.asList("DANH SÁCH HỌC SINH CHƯA HOÀN THÀNH KHOẢN THU", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_DATE.concat("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear));
            List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
            response.setSheetName(maClass.getClassName());
            response.setHeaderList(headerList);
            List<KidsPackageOrderExport> dataKidList = this.getKidsPackageInOrOut(kidsList, yearint, startMonth, endMonth, FinanceConstant.CATEGORY_IN);
            long i = 0;
            for (KidsPackageOrderExport x : dataKidList) {
                String code = x.getCodeList().toString().replace("[", "");
                String dataCode = code.replace("]", "");
                String moneyTotal = this.setMoneyDouble(x.getMoneyTotal());
                String moneyPaidTotal = this.setMoneyDouble(x.getMoneyPaidTotal());
                String moneyRemainTotal = this.setMoneyDouble(x.getMoneyRemainTotal());

                List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getFullName(), String.valueOf(x.getBirthDay()), dataCode,
                        moneyTotal, moneyPaidTotal, moneyRemainTotal);
                ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                bodyList.add(modelData);
            }
            response.setBodyList(bodyList);
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public ByteArrayInputStream exportFinanceKidOut(UserPrincipal principal, FinanceSearchKidsRequest request) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor headerColor = new XSSFColor(new java.awt.Color(0, 137, 203));

        int[] widths = {5, 30, 15, 60, 15, 15, 15};
        Long idSchool = principal.getIdSchoolLogin();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String[] columns = {"STT", "2", "3", "4", "5", "6", "7"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
            String schoolName = school.getSchoolName();
            Long idClass = request.getIdClass();
            String year = request.getYear();
            year.substring(0, 3);
            String newYear = year.substring(0, 4);
            int startMonth = Integer.parseInt(request.getStartMonth());
            int endMonth = Integer.parseInt(request.getEndMonth());
            int yearint = Integer.parseInt(newYear);
            if (idClass == null) {
                List<Long> idClassList = maClassRepository.findIdClassInSchool(idSchool);
                idClassList.forEach(a -> {
                    MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(a).orElseThrow();
                    List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(a, request);
                    Sheet sheet = workbook.createSheet(maClass.getClassName());
                    for (int i = 0; i < 4; i++) {
                        Row headerRow = sheet.createRow(i);
                        for (int col = 0; col < columns.length; col++) {
                            Cell cell = headerRow.createCell(col);
                            if (col == 0 && i == 0) {
                                cell.setCellValue("DANH SÁCH HỌC SINH CHƯA HOÀN THÀNH KHOẢN CHI ");
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
                                cell.setCellValue("Lớp: " + maClass.getClassName());
                                CellStyle threeStyle = workbook.createCellStyle();
                                org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
                                cellFont.setFontHeightInPoints((short) 11);
                                cellFont.setBold(true);
                                threeStyle.setFont(cellFont);
                                cell.setCellStyle(threeStyle);
                            } else if (col == 0) {
                                cell.setCellValue("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear);
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
                    sheet.createFreezePane(2, 6);
                    //Style content  status
                    CellStyle contentStatusStyle = workbook.createCellStyle();
                    org.apache.poi.ss.usermodel.Font contentStatusFont = workbook.createFont();
                    contentStatusFont.setColor(IndexedColors.BLACK.getIndex());
                    contentStatusStyle.setFont(contentStatusFont);
                    contentStatusStyle.setWrapText(true);
                    Font headerStatus = workbook.createFont();
                    headerStatus.setBold(true);
                    contentStatusStyle.setFont(headerStatus);
                    ((XSSFCellStyle) contentStatusStyle).setFillForegroundColor(headerColor);
                    contentStatusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    contentStatusStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    contentStatusStyle.setAlignment(HorizontalAlignment.CENTER);
                    contentStatusStyle.setBorderBottom(BorderStyle.THIN);
                    contentStatusStyle.setBorderTop(BorderStyle.THIN);
                    contentStatusStyle.setBorderRight(BorderStyle.THIN);
                    contentStatusStyle.setBorderLeft(BorderStyle.THIN);


                    //Style content  Meal
                    CellStyle contentMealStyle = workbook.createCellStyle();
                    org.apache.poi.ss.usermodel.Font contentMealFont = workbook.createFont();
                    contentMealFont.setColor(IndexedColors.BLACK.getIndex());
                    contentMealStyle.setFont(contentMealFont);
                    contentMealStyle.setWrapText(true);
                    ((XSSFCellStyle) contentMealStyle).setFillForegroundColor(greyOne);
                    contentMealStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    contentMealStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    contentMealStyle.setAlignment(HorizontalAlignment.CENTER);
                    contentMealStyle.setBorderBottom(BorderStyle.THIN);
                    contentMealStyle.setBorderTop(BorderStyle.THIN);
                    contentMealStyle.setBorderRight(BorderStyle.THIN);
                    contentMealStyle.setBorderLeft(BorderStyle.THIN);

                    //Style content  Meal
                    CellStyle contentMealStyleName = workbook.createCellStyle();
                    org.apache.poi.ss.usermodel.Font contentMealFontName = workbook.createFont();
                    contentMealFontName.setColor(IndexedColors.BLACK.getIndex());
                    contentMealStyleName.setFont(contentMealFontName);
                    contentMealStyleName.setWrapText(true);
                    ((XSSFCellStyle) contentMealStyleName).setFillForegroundColor(greyOne);
                    contentMealStyleName.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    contentMealStyleName.setVerticalAlignment(VerticalAlignment.CENTER);
                    contentMealStyleName.setAlignment(HorizontalAlignment.LEFT);
                    contentMealStyleName.setBorderBottom(BorderStyle.THIN);
                    contentMealStyleName.setBorderTop(BorderStyle.THIN);
                    contentMealStyleName.setBorderRight(BorderStyle.THIN);
                    contentMealStyleName.setBorderLeft(BorderStyle.THIN);

                    // header row 5
                    org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                    headerFont.setFontHeightInPoints((short) 10);
                    headerFont.setColor(IndexedColors.BLACK.getIndex());
                    CellStyle headerKidsCellStyle = workbook.createCellStyle();
                    ((XSSFCellStyle) headerKidsCellStyle).setFillForegroundColor(yellowOne);

                    int rowParent = 4;
                    Row headerRow5 = sheet.createRow(rowParent);
                    // Header 4
                    for (int col = 0; col < 6; col++) {
                        Cell cell = headerRow5.createCell(col);
                        CellStyle cellHeaderRow5 = workbook.createCellStyle();
                        cellHeaderRow5.setBorderBottom(BorderStyle.THIN);
                        cellHeaderRow5.setBorderTop(BorderStyle.THIN);
                        cellHeaderRow5.setBorderRight(BorderStyle.THIN);
                        cellHeaderRow5.setBorderLeft(BorderStyle.THIN);
                        cell.setCellStyle(cellHeaderRow5);
                    }

//             Row for Header
                    int rowChild = 5;
                    Cell cellLesson0 = headerRow5.createCell(0);
//                    sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 0, 0));
                    cellLesson0.setCellValue("STT");
                    cellLesson0.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle0 = workbook.createCellStyle();
                    cellLateStyle0.setAlignment(HorizontalAlignment.CENTER);


                    Cell cellLesson1 = headerRow5.createCell(1);
                    cellLesson1.setCellValue("Tên học sinh");
                    cellLesson1.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle1 = workbook.createCellStyle();
                    cellLateStyle1.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                    cellLateStyle1.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson2 = headerRow5.createCell(2);
                    cellLesson2.setCellValue("Ngày sinh");
                    cellLesson2.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle2 = workbook.createCellStyle();
                    cellLateStyle2.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson3 = headerRow5.createCell(3);
                    cellLesson3.setCellValue("Danh sách mã hóa đơn");
                    cellLesson3.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle3 = workbook.createCellStyle();
                    cellLateStyle3.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson4 = headerRow5.createCell(4);
                    cellLesson4.setCellValue("Tổng phải chi");
                    cellLesson4.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle4 = workbook.createCellStyle();
                    cellLateStyle4.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson5 = headerRow5.createCell(5);
                    cellLesson5.setCellValue("Đã chi");
                    cellLesson5.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle5 = workbook.createCellStyle();
                    cellLateStyle5.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson6 = headerRow5.createCell(6);
                    cellLesson6.setCellValue("Còn lại phải chi");
                    cellLesson6.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle6 = workbook.createCellStyle();
                    cellLateStyle6.setAlignment(HorizontalAlignment.CENTER);
                    Row headerRow6 = sheet.createRow(rowChild);
                    for (int col = 0; col < 7; col++) {
                        sheet.setColumnWidth(col, widths[col] * 256);
                        Cell cell = headerRow6.createCell(col);
                    }

                    int rowIdx = 5;
                    int i = 0;

                    List<KidsPackageOrderExport> dataKidList = this.getKidsPackageInOrOut(kidsList, yearint, startMonth, endMonth, FinanceConstant.CATEGORY_OUT);

                    for (KidsPackageOrderExport x : dataKidList) {
                        i -= -1;

                        Row row = sheet.createRow(rowIdx++);

                        Cell cellId = row.createCell(0);
                        cellId.setCellValue(i);
                        cellId.setCellStyle(contentMealStyle);

                        Cell cellKidName = row.createCell(1);
                        cellKidName.setCellValue(x.getFullName());
                        cellKidName.setCellStyle(contentMealStyleName);

                        String code = x.getCodeList().toString().replace("[", "");
                        String dataCode = code.replace("]", "");

                        Cell cellAbsentLetterYes = row.createCell(2);
                        cellAbsentLetterYes.setCellValue(ConvertData.convertLocalDateToString(x.getBirthDay()));
                        cellAbsentLetterYes.setCellStyle(contentMealStyle);

                        Cell cellAbsentLetterNo = row.createCell(3);
                        cellAbsentLetterNo.setCellValue(dataCode);
                        cellAbsentLetterNo.setCellStyle(contentMealStyleName);


                        double moneytotal = x.getMoneyTotal();
                        double moneypaidtotal = x.getMoneyPaidTotal();
                        double moneyremaintotal = x.getMoneyRemainTotal();

                        DecimalFormat formatter = new DecimalFormat("###,###,###");
                        String total = formatter.format(moneytotal);
                        String totalFill = total.replace(",", ".");

                        String paidtotal = formatter.format(moneypaidtotal);
                        String paidtotalFill = paidtotal.replace(",", ".");

                        String remainTotal = formatter.format(moneyremaintotal);
                        String remainTotalFill = remainTotal.replace(",", ".");

                        Cell cellAbsentStatus = row.createCell(4);
                        cellAbsentStatus.setCellValue(totalFill);
                        cellAbsentStatus.setCellStyle(contentMealStyle);

                        Cell cellMorningYes = row.createCell(5);
                        cellMorningYes.setCellValue(paidtotalFill);
                        cellMorningYes.setCellStyle(contentMealStyle);

                        Cell cellMorningNo = row.createCell(6);
                        cellMorningNo.setCellValue(remainTotalFill);
                        cellMorningNo.setCellStyle(contentMealStyle);
                    }
                });
            } else {
                Long idClassSelect = request.getIdClass();
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClassSelect).orElseThrow();
                List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(idClassSelect, request);
                Sheet sheet = workbook.createSheet(maClass.getClassName());
                for (int i = 0; i < 4; i++) {
                    Row headerRow = sheet.createRow(i);
                    for (int col = 0; col < columns.length; col++) {
                        Cell cell = headerRow.createCell(col);
                        if (col == 0 && i == 0) {
                            cell.setCellValue("DANH SÁCH HỌC SINH CHƯA HOÀN THÀNH KHOẢN THU ");
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
                            cell.setCellValue("Lớp: " + maClass.getClassName());
                            CellStyle threeStyle = workbook.createCellStyle();
                            org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
                            cellFont.setFontHeightInPoints((short) 11);
                            cellFont.setBold(true);
                            threeStyle.setFont(cellFont);
                            cell.setCellStyle(threeStyle);
                        } else if (col == 0) {
                            cell.setCellValue("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + "năm " + newYear);
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
                sheet.createFreezePane(2, 6);
                //Style content  status
                CellStyle contentStatusStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font contentStatusFont = workbook.createFont();
                contentStatusFont.setColor(IndexedColors.BLACK.getIndex());
                contentStatusStyle.setFont(contentStatusFont);
                contentStatusStyle.setWrapText(true);
                Font headerStatus = workbook.createFont();
                headerStatus.setBold(true);
                contentStatusStyle.setFont(headerStatus);
                ((XSSFCellStyle) contentStatusStyle).setFillForegroundColor(headerColor);
                contentStatusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentStatusStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentStatusStyle.setAlignment(HorizontalAlignment.CENTER);
                contentStatusStyle.setBorderBottom(BorderStyle.THIN);
                contentStatusStyle.setBorderTop(BorderStyle.THIN);
                contentStatusStyle.setBorderRight(BorderStyle.THIN);
                contentStatusStyle.setBorderLeft(BorderStyle.THIN);


                //Style content  Meal
                CellStyle contentMealStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font contentMealFont = workbook.createFont();
                contentMealFont.setColor(IndexedColors.BLACK.getIndex());
                contentMealStyle.setFont(contentMealFont);
                contentMealStyle.setWrapText(true);
                ((XSSFCellStyle) contentMealStyle).setFillForegroundColor(greyOne);
                contentMealStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentMealStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentMealStyle.setAlignment(HorizontalAlignment.CENTER);
                contentMealStyle.setBorderBottom(BorderStyle.THIN);
                contentMealStyle.setBorderTop(BorderStyle.THIN);
                contentMealStyle.setBorderRight(BorderStyle.THIN);
                contentMealStyle.setBorderLeft(BorderStyle.THIN);

                //Style content  Meal
                CellStyle contentMealStyleName = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font contentMealFontName = workbook.createFont();
                contentMealFontName.setColor(IndexedColors.BLACK.getIndex());
                contentMealStyleName.setFont(contentMealFontName);
                contentMealStyleName.setWrapText(true);
                ((XSSFCellStyle) contentMealStyleName).setFillForegroundColor(greyOne);
                contentMealStyleName.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentMealStyleName.setVerticalAlignment(VerticalAlignment.CENTER);
                contentMealStyleName.setAlignment(HorizontalAlignment.LEFT);
                contentMealStyleName.setBorderBottom(BorderStyle.THIN);
                contentMealStyleName.setBorderTop(BorderStyle.THIN);
                contentMealStyleName.setBorderRight(BorderStyle.THIN);
                contentMealStyleName.setBorderLeft(BorderStyle.THIN);

                // header row 5
                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setFontHeightInPoints((short) 10);
                headerFont.setColor(IndexedColors.BLACK.getIndex());
                CellStyle headerKidsCellStyle = workbook.createCellStyle();
                ((XSSFCellStyle) headerKidsCellStyle).setFillForegroundColor(yellowOne);

                int rowParent = 4;
                Row headerRow5 = sheet.createRow(rowParent);
                // Header 4
                for (int col = 0; col < 6; col++) {
                    Cell cell = headerRow5.createCell(col);
                    CellStyle cellHeaderRow5 = workbook.createCellStyle();
                    cellHeaderRow5.setBorderBottom(BorderStyle.THIN);
                    cellHeaderRow5.setBorderTop(BorderStyle.THIN);
                    cellHeaderRow5.setBorderRight(BorderStyle.THIN);
                    cellHeaderRow5.setBorderLeft(BorderStyle.THIN);
                    cell.setCellStyle(cellHeaderRow5);
                }

//             Row for Header
                int rowChild = 5;
                Cell cellLesson0 = headerRow5.createCell(0);
                cellLesson0.setCellValue("STT");
                cellLesson0.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle0 = workbook.createCellStyle();
                cellLateStyle0.setAlignment(HorizontalAlignment.CENTER);


                Cell cellLesson1 = headerRow5.createCell(1);
                cellLesson1.setCellValue("Tên học sinh");
                cellLesson1.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle1 = workbook.createCellStyle();
                cellLateStyle1.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                cellLateStyle1.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson2 = headerRow5.createCell(2);
                cellLesson2.setCellValue("Ngày sinh");
                cellLesson2.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle2 = workbook.createCellStyle();
                cellLateStyle2.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson3 = headerRow5.createCell(3);
                cellLesson3.setCellValue("Danh sách mã hóa đơn");
                cellLesson3.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle3 = workbook.createCellStyle();
                cellLateStyle3.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson4 = headerRow5.createCell(4);
                cellLesson4.setCellValue("Tổng phải chi");
                cellLesson4.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle4 = workbook.createCellStyle();
                cellLateStyle4.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson5 = headerRow5.createCell(5);
                cellLesson5.setCellValue("Đã chi");
                cellLesson5.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle5 = workbook.createCellStyle();
                cellLateStyle5.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson6 = headerRow5.createCell(6);
                cellLesson6.setCellValue("Còn lại phải chi");
                cellLesson6.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle6 = workbook.createCellStyle();
                cellLateStyle6.setAlignment(HorizontalAlignment.CENTER);
                Row headerRow6 = sheet.createRow(rowChild);
                for (int col = 0; col < 7; col++) {
                    sheet.setColumnWidth(col, widths[col] * 256);
                    Cell cell = headerRow6.createCell(col);
                }

                int rowIdx = 5;
                int i = 0;

                List<KidsPackageOrderExport> dataKidList = this.getKidsPackageInOrOut(kidsList, yearint, startMonth, endMonth, "in");

                for (KidsPackageOrderExport x : dataKidList) {
                    i -= -1;

                    Row row = sheet.createRow(rowIdx++);

                    Cell cellId = row.createCell(0);
                    cellId.setCellValue(i);
                    cellId.setCellStyle(contentMealStyle);

                    Cell cellKidName = row.createCell(1);
                    cellKidName.setCellValue(x.getFullName());
                    cellKidName.setCellStyle(contentMealStyleName);

                    String code = x.getCodeList().toString().replace("[", "");
                    String dataCode = code.replace("]", "");

                    Cell cellAbsentLetterYes = row.createCell(2);
                    cellAbsentLetterYes.setCellValue(ConvertData.convertLocalDateToString(x.getBirthDay()));
                    cellAbsentLetterYes.setCellStyle(contentMealStyle);

                    Cell cellAbsentLetterNo = row.createCell(3);
                    cellAbsentLetterNo.setCellValue(dataCode);
                    cellAbsentLetterNo.setCellStyle(contentMealStyleName);


                    double moneytotal = x.getMoneyTotal();
                    double moneypaidtotal = x.getMoneyPaidTotal();
                    double moneyremaintotal = x.getMoneyRemainTotal();

                    DecimalFormat formatter = new DecimalFormat("###,###,###");
                    String total = formatter.format(moneytotal);
                    String totalFill = total.replace(",", ".");

                    String paidtotal = formatter.format(moneypaidtotal);
                    String paidtotalFill = paidtotal.replace(",", ".");

                    String remainTotal = formatter.format(moneyremaintotal);
                    String remainTotalFill = remainTotal.replace(",", ".");

                    Cell cellAbsentStatus = row.createCell(4);
                    cellAbsentStatus.setCellValue(totalFill);
                    cellAbsentStatus.setCellStyle(contentMealStyle);

                    Cell cellMorningYes = row.createCell(5);
                    cellMorningYes.setCellValue(paidtotalFill);
                    cellMorningYes.setCellStyle(contentMealStyle);

                    Cell cellMorningNo = row.createCell(6);
                    cellMorningNo.setCellValue(remainTotalFill);
                    cellMorningNo.setCellStyle(contentMealStyle);
                }

            }
            // xuất học sinh theo trường
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public List<ExcelResponse> exportFinanceKidOutNew(UserPrincipal principal, FinanceSearchKidsRequest request) {
        List<ExcelResponse> responseList = new ArrayList<>();

        Long idSchool = principal.getIdSchoolLogin();
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
        String schoolName = school.getSchoolName();
        Long idClass = request.getIdClass();
        String year = request.getYear();
        String newYear = year.substring(0, 4);
        int startMonth = Integer.parseInt(request.getStartMonth());
        int endMonth = Integer.parseInt(request.getEndMonth());
        int yearint = Integer.parseInt(newYear);
        if (idClass == null) {
            List<Long> idClassList = maClassRepository.findIdClassInSchool(idSchool);
            idClassList.forEach(a -> {
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(a).orElseThrow();
                List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(a, request);
                List<ExcelData> bodyList = new ArrayList<>();
                ExcelResponse response = new ExcelResponse();
                List<String> headerStringList = Arrays.asList("DANH SÁCH HỌC SINH CHƯA HOÀN THÀNH KHOẢN CHI", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_DATE.concat("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear));
                List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
                response.setSheetName(maClass.getClassName());
                response.setHeaderList(headerList);
                List<KidsPackageOrderExport> dataKidList = this.getKidsPackageInOrOut(kidsList, yearint, startMonth, endMonth, FinanceConstant.CATEGORY_OUT);
                long i = 0;
                for (KidsPackageOrderExport x : dataKidList) {
                    String code = x.getCodeList().toString().replace("[", "");
                    String dataCode = code.replace("]", "");

                    String moneyTotal = this.setMoneyDouble(x.getMoneyTotal());
                    String moneyPaidTotal = this.setMoneyDouble(x.getMoneyPaidTotal());
                    String moneyRemainTotal = this.setMoneyDouble(x.getMoneyRemainTotal());

                    List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getFullName(), ConvertData.convertLocalDateToString(x.getBirthDay()), dataCode,
                            moneyTotal, moneyPaidTotal, moneyRemainTotal);
                    ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                    bodyList.add(modelData);
                }
                response.setBodyList(bodyList);
                responseList.add(response);
            });
        } else {
            Long idClassSelect = request.getIdClass();
            MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClassSelect).orElseThrow();
            List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(idClassSelect, request);
            List<ExcelData> bodyList = new ArrayList<>();
            ExcelResponse response = new ExcelResponse();
            List<String> headerStringList = Arrays.asList("DANH SÁCH HỌC SINH CHƯA HOÀN THÀNH KHOẢN THU", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_DATE.concat("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear));
            List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
            response.setSheetName(maClass.getClassName());
            response.setHeaderList(headerList);
            List<KidsPackageOrderExport> dataKidList = this.getKidsPackageInOrOut(kidsList, yearint, startMonth, endMonth, FinanceConstant.CATEGORY_OUT);
            long i = 0;
            for (KidsPackageOrderExport x : dataKidList) {
                String code = x.getCodeList().toString().replace("[", "");
                String dataCode = code.replace("]", "");
                String moneyTotal = this.setMoneyDouble(x.getMoneyTotal());
                String moneyPaidTotal = this.setMoneyDouble(x.getMoneyPaidTotal());
                String moneyRemainTotal = this.setMoneyDouble(x.getMoneyRemainTotal());

                List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getFullName(), String.valueOf(x.getBirthDay()), dataCode,
                        moneyTotal, moneyPaidTotal, moneyRemainTotal);
                ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                bodyList.add(modelData);
            }
            response.setBodyList(bodyList);
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public ByteArrayInputStream exportFinanceKidInOut(UserPrincipal principal, FinanceSearchKidsRequest request) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor headerColor = new XSSFColor(new java.awt.Color(0, 137, 203));
        int[] widths = {5, 30, 15, 90};
        Long idSchool = principal.getIdSchoolLogin();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String[] columns = {"STT", "1", "2", "3"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
            String schoolName = school.getSchoolName();
            Long idClass = request.getIdClass();
            String year = request.getYear();
            year.substring(0, 3);
            String newYear = year.substring(0, 4);
            int startMonth = Integer.parseInt(request.getStartMonth());
            int endMonth = Integer.parseInt(request.getEndMonth());
            int yearint = Integer.parseInt(newYear);
            if (idClass == null) {
                List<Long> idClassList = maClassRepository.findIdClassInSchool(idSchool);
                idClassList.forEach(a -> {
                    MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(a).orElseThrow();
                    List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(a, request);
                    Sheet sheet = workbook.createSheet(maClass.getClassName());
                    for (int i = 0; i < 4; i++) {
                        Row headerRow = sheet.createRow(i);
                        for (int col = 0; col < columns.length; col++) {
                            Cell cell = headerRow.createCell(col);
                            if (col == 0 && i == 0) {
                                cell.setCellValue("DANH SÁCH HỌC SINH CÓ HÓA ĐƠN CHƯA HOÀN THÀNH ");
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
                                cell.setCellValue("Lớp: " + maClass.getClassName());
                                CellStyle threeStyle = workbook.createCellStyle();
                                org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
                                cellFont.setFontHeightInPoints((short) 11);
                                cellFont.setBold(true);
                                threeStyle.setFont(cellFont);
                                cell.setCellStyle(threeStyle);
                            } else if (col == 0) {
                                cell.setCellValue("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear);
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
                    sheet.createFreezePane(2, 6);
                    //Style content  status
                    CellStyle contentStatusStyle = workbook.createCellStyle();
                    org.apache.poi.ss.usermodel.Font contentStatusFont = workbook.createFont();
                    contentStatusFont.setColor(IndexedColors.BLACK.getIndex());
                    contentStatusStyle.setFont(contentStatusFont);
                    contentStatusStyle.setWrapText(true);
                    Font headerStatus = workbook.createFont();
                    headerStatus.setBold(true);
                    contentStatusStyle.setFont(headerStatus);
                    ((XSSFCellStyle) contentStatusStyle).setFillForegroundColor(headerColor);
                    contentStatusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    contentStatusStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    contentStatusStyle.setAlignment(HorizontalAlignment.CENTER);
                    contentStatusStyle.setBorderBottom(BorderStyle.THIN);
                    contentStatusStyle.setBorderTop(BorderStyle.THIN);
                    contentStatusStyle.setBorderRight(BorderStyle.THIN);
                    contentStatusStyle.setBorderLeft(BorderStyle.THIN);


                    //Style content  Meal
                    CellStyle contentMealStyle = workbook.createCellStyle();
                    org.apache.poi.ss.usermodel.Font contentMealFont = workbook.createFont();
                    contentMealFont.setColor(IndexedColors.BLACK.getIndex());
                    contentMealStyle.setFont(contentMealFont);
                    contentMealStyle.setWrapText(true);
                    ((XSSFCellStyle) contentMealStyle).setFillForegroundColor(greyOne);
                    contentMealStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    contentMealStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    contentMealStyle.setAlignment(HorizontalAlignment.CENTER);
                    contentMealStyle.setBorderBottom(BorderStyle.THIN);
                    contentMealStyle.setBorderTop(BorderStyle.THIN);
                    contentMealStyle.setBorderRight(BorderStyle.THIN);
                    contentMealStyle.setBorderLeft(BorderStyle.THIN);

                    //Style content  Meal
                    CellStyle contentMealStyleName = workbook.createCellStyle();
                    org.apache.poi.ss.usermodel.Font contentMealFontName = workbook.createFont();
                    contentMealFontName.setColor(IndexedColors.BLACK.getIndex());
                    contentMealStyleName.setFont(contentMealFontName);
                    contentMealStyleName.setWrapText(true);
                    ((XSSFCellStyle) contentMealStyleName).setFillForegroundColor(greyOne);
                    contentMealStyleName.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    contentMealStyleName.setVerticalAlignment(VerticalAlignment.CENTER);
                    contentMealStyleName.setAlignment(HorizontalAlignment.LEFT);
                    contentMealStyleName.setBorderBottom(BorderStyle.THIN);
                    contentMealStyleName.setBorderTop(BorderStyle.THIN);
                    contentMealStyleName.setBorderRight(BorderStyle.THIN);
                    contentMealStyleName.setBorderLeft(BorderStyle.THIN);


                    // header row 5
                    org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                    headerFont.setFontHeightInPoints((short) 10);
                    headerFont.setColor(IndexedColors.BLACK.getIndex());
                    CellStyle headerKidsCellStyle = workbook.createCellStyle();
                    ((XSSFCellStyle) headerKidsCellStyle).setFillForegroundColor(yellowOne);

                    int rowParent = 4;
                    Row headerRow5 = sheet.createRow(rowParent);
                    // Header 4
                    for (int col = 0; col < 4; col++) {
                        Cell cell = headerRow5.createCell(col);
                        CellStyle cellHeaderRow5 = workbook.createCellStyle();
                        cellHeaderRow5.setBorderBottom(BorderStyle.THIN);
                        cellHeaderRow5.setBorderTop(BorderStyle.THIN);
                        cellHeaderRow5.setBorderRight(BorderStyle.THIN);
                        cellHeaderRow5.setBorderLeft(BorderStyle.THIN);
                        cell.setCellStyle(cellHeaderRow5);
                    }

//             Row for Header
                    int rowChild = 5;
                    Cell cellLesson0 = headerRow5.createCell(0);
//                    sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 0, 0));
                    cellLesson0.setCellValue("STT");
                    cellLesson0.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle0 = workbook.createCellStyle();
                    cellLateStyle0.setAlignment(HorizontalAlignment.CENTER);


                    Cell cellLesson1 = headerRow5.createCell(1);
                    cellLesson1.setCellValue("Tên học sinh");
                    cellLesson1.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle1 = workbook.createCellStyle();
                    cellLateStyle1.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                    cellLateStyle1.setAlignment(HorizontalAlignment.LEFT);
//                    sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 1, 1));

                    Cell cellLesson2 = headerRow5.createCell(2);
                    cellLesson2.setCellValue("Ngày sinh");
                    cellLesson2.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle2 = workbook.createCellStyle();
                    cellLateStyle2.setAlignment(HorizontalAlignment.CENTER);
//                    sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 2, 2));

                    Cell cellLesson3 = headerRow5.createCell(3);
                    cellLesson3.setCellValue("Danh sách mã hóa đơn");
                    cellLesson3.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle3 = workbook.createCellStyle();
                    cellLateStyle3.setAlignment(HorizontalAlignment.LEFT);
//                    sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 3, 3));

                    for (int col = 0; col < 4; col++) {
                        sheet.setColumnWidth(col, widths[col] * 256);
//                        Cell cell = headerRow6.createCell(col);
                    }

                    int rowIdx = 5;
                    int i = 0;

                    List<KidsPackageExport> dataList = this.getKidsPackageOrder(kidsList, yearint, startMonth, endMonth);

                    for (KidsPackageExport x : dataList) {
                        i -= -1;

                        Row row = sheet.createRow(rowIdx++);

                        Cell cellId = row.createCell(0);
                        cellId.setCellValue(i);
                        cellId.setCellStyle(contentMealStyle);

                        Cell cellKidName = row.createCell(1);
                        cellKidName.setCellValue(x.getFullName());
                        cellKidName.setCellStyle(contentMealStyleName);

                        String code = x.getCodeList().toString().replace("[", "");
                        String dataCode = code.replace("]", "");


                        Cell cellAbsentLetterYes = row.createCell(2);
                        cellAbsentLetterYes.setCellValue(ConvertData.convertLocalDateToString(x.getBirthDay()));
                        cellAbsentLetterYes.setCellStyle(contentMealStyle);

                        Cell cellAbsentLetterNo = row.createCell(3);
                        cellAbsentLetterNo.setCellValue(dataCode);
                        cellAbsentLetterNo.setCellStyle(contentMealStyleName);
                    }
                });
            } else {
                Long idClassSelect = request.getIdClass();
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClassSelect).orElseThrow();
                List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(idClassSelect, request);
                Sheet sheet = workbook.createSheet(maClass.getClassName());
                for (int i = 0; i < 4; i++) {
                    Row headerRow = sheet.createRow(i);
                    for (int col = 0; col < columns.length; col++) {
                        Cell cell = headerRow.createCell(col);
                        if (col == 0 && i == 0) {
                            cell.setCellValue("DANH SÁCH HỌC SINH CÓ HÓA ĐƠN CHƯA HOÀN THÀNH ");
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
                            cell.setCellValue("Lớp: " + maClass.getClassName());
                            CellStyle threeStyle = workbook.createCellStyle();
                            org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
                            cellFont.setFontHeightInPoints((short) 11);
                            cellFont.setBold(true);
                            threeStyle.setFont(cellFont);
                            cell.setCellStyle(threeStyle);
                        } else if (col == 0) {
                            cell.setCellValue("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear);
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
                sheet.createFreezePane(2, 6);
                //Style content  status
                CellStyle contentStatusStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font contentStatusFont = workbook.createFont();
                contentStatusFont.setColor(IndexedColors.BLACK.getIndex());
                contentStatusStyle.setFont(contentStatusFont);
                contentStatusStyle.setWrapText(true);
                Font headerStatus = workbook.createFont();
                headerStatus.setBold(true);
                contentStatusStyle.setFont(headerStatus);
                ((XSSFCellStyle) contentStatusStyle).setFillForegroundColor(headerColor);
                contentStatusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentStatusStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentStatusStyle.setAlignment(HorizontalAlignment.CENTER);
                contentStatusStyle.setBorderBottom(BorderStyle.THIN);
                contentStatusStyle.setBorderTop(BorderStyle.THIN);
                contentStatusStyle.setBorderRight(BorderStyle.THIN);
                contentStatusStyle.setBorderLeft(BorderStyle.THIN);


                //Style content  Meal
                CellStyle contentMealStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font contentMealFont = workbook.createFont();
                contentMealFont.setColor(IndexedColors.BLACK.getIndex());
                contentMealStyle.setFont(contentMealFont);
                contentMealStyle.setWrapText(true);
                ((XSSFCellStyle) contentMealStyle).setFillForegroundColor(greyOne);
                contentMealStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentMealStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentMealStyle.setAlignment(HorizontalAlignment.CENTER);
                contentMealStyle.setBorderBottom(BorderStyle.THIN);
                contentMealStyle.setBorderTop(BorderStyle.THIN);
                contentMealStyle.setBorderRight(BorderStyle.THIN);
                contentMealStyle.setBorderLeft(BorderStyle.THIN);
                //Style content  Meal
                CellStyle contentMealStyleName = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font contentMealFontName = workbook.createFont();
                contentMealFontName.setColor(IndexedColors.BLACK.getIndex());
                contentMealStyleName.setFont(contentMealFontName);
                contentMealStyleName.setWrapText(true);
                ((XSSFCellStyle) contentMealStyleName).setFillForegroundColor(greyOne);
                contentMealStyleName.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentMealStyleName.setVerticalAlignment(VerticalAlignment.CENTER);
                contentMealStyleName.setAlignment(HorizontalAlignment.LEFT);
                contentMealStyleName.setBorderBottom(BorderStyle.THIN);
                contentMealStyleName.setBorderTop(BorderStyle.THIN);
                contentMealStyleName.setBorderRight(BorderStyle.THIN);
                contentMealStyleName.setBorderLeft(BorderStyle.THIN);
                // header row 5
                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setFontHeightInPoints((short) 10);
                headerFont.setColor(IndexedColors.BLACK.getIndex());
                CellStyle headerKidsCellStyle = workbook.createCellStyle();
                ((XSSFCellStyle) headerKidsCellStyle).setFillForegroundColor(yellowOne);

                int rowParent = 4;
                Row headerRow5 = sheet.createRow(rowParent);
                // Header 4
                for (int col = 0; col < 4; col++) {
                    Cell cell = headerRow5.createCell(col);
                    CellStyle cellHeaderRow5 = workbook.createCellStyle();
                    cellHeaderRow5.setBorderBottom(BorderStyle.THIN);
                    cellHeaderRow5.setBorderTop(BorderStyle.THIN);
                    cellHeaderRow5.setBorderRight(BorderStyle.THIN);
                    cellHeaderRow5.setBorderLeft(BorderStyle.THIN);
                    cell.setCellStyle(cellHeaderRow5);
                }

//             Row for Header
                int rowChild = 5;
                Cell cellLesson0 = headerRow5.createCell(0);
                cellLesson0.setCellValue("STT");
                cellLesson0.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle0 = workbook.createCellStyle();
                cellLateStyle0.setAlignment(HorizontalAlignment.CENTER);


                Cell cellLesson1 = headerRow5.createCell(1);
                cellLesson1.setCellValue("Tên học sinh");
                cellLesson1.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle1 = workbook.createCellStyle();
                cellLateStyle1.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                cellLateStyle1.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson2 = headerRow5.createCell(2);
                cellLesson2.setCellValue("Ngày sinh");
                cellLesson2.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle2 = workbook.createCellStyle();
                cellLateStyle2.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson3 = headerRow5.createCell(3);
                cellLesson3.setCellValue("Danh sách mã hóa đơn");
                cellLesson3.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle3 = workbook.createCellStyle();
                cellLateStyle3.setAlignment(HorizontalAlignment.CENTER);

                for (int col = 0; col < 4; col++) {
                    sheet.setColumnWidth(col, widths[col] * 256);
                }

                int rowIdx = 5;
                int i = 0;

                List<KidsPackageExport> dataList = this.getKidsPackageOrder(kidsList, yearint, startMonth, endMonth);

                for (KidsPackageExport x : dataList) {
                    i -= -1;

                    Row row = sheet.createRow(rowIdx++);

                    Cell cellId = row.createCell(0);
                    cellId.setCellValue(i);
                    cellId.setCellStyle(contentMealStyle);

                    Cell cellKidName = row.createCell(1);
                    cellKidName.setCellValue(x.getFullName());
                    cellKidName.setCellStyle(contentMealStyleName);

                    String code = x.getCodeList().toString().replace("[", "");
                    String dataCode = code.replace("]", "");

                    Cell cellAbsentLetterYes = row.createCell(2);
                    cellAbsentLetterYes.setCellValue(ConvertData.convertLocalDateToString(x.getBirthDay()));
                    cellAbsentLetterYes.setCellStyle(contentMealStyle);

                    Cell cellAbsentLetterNo = row.createCell(3);
                    cellAbsentLetterNo.setCellValue(dataCode);
                    cellAbsentLetterNo.setCellStyle(contentMealStyleName);

                }
            }
            // xuất học sinh theo trường
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public List<ExcelResponse> exportFinanceKidInOutNew(UserPrincipal principal, FinanceSearchKidsRequest request) {
        List<ExcelResponse> responseList = new ArrayList<>();

        Long idSchool = principal.getIdSchoolLogin();
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
        String schoolName = school.getSchoolName();
        Long idClass = request.getIdClass();
        String year = request.getYear();
        year.substring(0, 3);
        String newYear = year.substring(0, 4);
        int startMonth = Integer.parseInt(request.getStartMonth());
        int endMonth = Integer.parseInt(request.getEndMonth());
        int yearint = Integer.parseInt(newYear);
        if (idClass == null) {
            List<Long> idClassList = maClassRepository.findIdClassInSchool(idSchool);
            idClassList.forEach(a -> {
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(a).orElseThrow();
                List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(a, request);
                List<ExcelData> bodyList = new ArrayList<>();
                ExcelResponse response = new ExcelResponse();
                List<String> headerStringList = Arrays.asList("DANH SÁCH HỌC SINH CÓ HÓA ĐƠN CHƯA HOÀN THÀNH", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_DATE.concat("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear));
                List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
                response.setSheetName(maClass.getClassName());
                response.setHeaderList(headerList);
                List<KidsPackageExport> dataList = this.getKidsPackageOrder(kidsList, yearint, startMonth, endMonth);
                long i = 0;
                for (KidsPackageExport x : dataList) {
                    String code = x.getCodeList().toString().replace("[", "");
                    String dataCode = code.replace("]", "");
                    List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getFullName(), ConvertData.convertLocalDateToString(x.getBirthDay()), dataCode);
                    ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                    bodyList.add(modelData);
                }
                response.setBodyList(bodyList);
                responseList.add(response);
            });
            return responseList;
        } else {
            Long idClassSelect = request.getIdClass();
            MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClassSelect).orElseThrow();
            List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(idClassSelect, request);
            List<ExcelData> bodyList = new ArrayList<>();
            ExcelResponse response = new ExcelResponse();
            List<String> headerStringList = Arrays.asList("DANH SÁCH HỌC SINH CÓ HÓA ĐƠN CHƯA HOÀN THÀNH", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_DATE.concat("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear));
            List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
            response.setSheetName(maClass.getClassName());
            response.setHeaderList(headerList);
            List<KidsPackageExport> dataList = this.getKidsPackageOrder(kidsList, yearint, startMonth, endMonth);
            long i = 0;
            for (KidsPackageExport x : dataList) {
                String code = x.getCodeList().toString().replace("[", "");
                String dataCode = code.replace("]", "");
                List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getFullName(), String.valueOf(x.getBirthDay()), dataCode);
                ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                bodyList.add(modelData);
            }
            response.setBodyList(bodyList);
            responseList.add(response);
            return responseList;
        }
    }

    @Override
    public ByteArrayInputStream exportFinanceKidInAndOut(UserPrincipal principal, FinanceSearchKidsRequest request) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(131, 164, 196));
        XSSFColor headerColor = new XSSFColor(new java.awt.Color(0, 137, 203));
        int[] widths = {5, 20, 15, 40, 26, 24, 12, 12};
        Long idSchool = principal.getIdSchoolLogin();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String[] columns = {"STT", "2", "3", "4", "5", "6", "7"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
            String schoolName = school.getSchoolName();
            Long idClass = request.getIdClass();
            String year = request.getYear();
            year.substring(0, 3);
            String newYear = year.substring(0, 4);
            int startMonth = Integer.parseInt(request.getStartMonth());
            int endMonth = Integer.parseInt(request.getEndMonth());
            int yearint = Integer.parseInt(newYear);
            if (idClass == null) {
                List<Long> idClassList = maClassRepository.findIdClassInSchool(idSchool);
                idClassList.forEach(a -> {
                    MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(a).orElseThrow();
                    List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(a, request);
                    Sheet sheet = workbook.createSheet(maClass.getClassName());
                    for (int i = 0; i < 4; i++) {
                        Row headerRow = sheet.createRow(i);
                        for (int col = 0; col < columns.length; col++) {
                            Cell cell = headerRow.createCell(col);
                            if (col == 0 && i == 0) {
                                cell.setCellValue("DANH SÁCH TỔNG HỢP THU CHI CỦA HỌC SINH ");
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
                                cell.setCellValue("Lớp: " + maClass.getClassName());
                                CellStyle threeStyle = workbook.createCellStyle();
                                org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
                                cellFont.setFontHeightInPoints((short) 11);
                                cellFont.setBold(true);
                                threeStyle.setFont(cellFont);
                                cell.setCellStyle(threeStyle);
                            } else if (col == 0) {
                                cell.setCellValue("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear);
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
                    sheet.createFreezePane(2, 6);
                    //Style content  status
                    CellStyle contentStatusStyle = workbook.createCellStyle();
                    org.apache.poi.ss.usermodel.Font contentStatusFont = workbook.createFont();
                    contentStatusFont.setColor(IndexedColors.BLACK.getIndex());
                    contentStatusStyle.setFont(contentStatusFont);
                    contentStatusStyle.setWrapText(true);
                    Font headerStatus = workbook.createFont();
                    headerStatus.setBold(true);
                    contentStatusStyle.setFont(headerStatus);
                    ((XSSFCellStyle) contentStatusStyle).setFillForegroundColor(headerColor);
                    contentStatusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    contentStatusStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    contentStatusStyle.setAlignment(HorizontalAlignment.CENTER);
                    contentStatusStyle.setBorderBottom(BorderStyle.THIN);
                    contentStatusStyle.setBorderTop(BorderStyle.THIN);
                    contentStatusStyle.setBorderRight(BorderStyle.THIN);
                    contentStatusStyle.setBorderLeft(BorderStyle.THIN);

                    //Style content  Meal
                    CellStyle contentMealStyle = workbook.createCellStyle();
                    org.apache.poi.ss.usermodel.Font contentMealFont = workbook.createFont();
                    contentMealFont.setColor(IndexedColors.BLACK.getIndex());
                    contentMealStyle.setFont(contentMealFont);
                    contentMealStyle.setWrapText(true);
                    ((XSSFCellStyle) contentMealStyle).setFillForegroundColor(greyOne);
                    contentMealStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    contentMealStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    contentMealStyle.setAlignment(HorizontalAlignment.CENTER);
                    contentMealStyle.setBorderBottom(BorderStyle.THIN);
                    contentMealStyle.setBorderTop(BorderStyle.THIN);
                    contentMealStyle.setBorderRight(BorderStyle.THIN);
                    contentMealStyle.setBorderLeft(BorderStyle.THIN);
                    //Style content  Meal
                    CellStyle contentMealStyleName = workbook.createCellStyle();
                    org.apache.poi.ss.usermodel.Font contentMealFontName = workbook.createFont();
                    contentMealFontName.setColor(IndexedColors.BLACK.getIndex());
                    contentMealStyleName.setFont(contentMealFontName);
                    contentMealStyleName.setWrapText(true);
                    ((XSSFCellStyle) contentMealStyleName).setFillForegroundColor(greyOne);
                    contentMealStyleName.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    contentMealStyleName.setVerticalAlignment(VerticalAlignment.CENTER);
                    contentMealStyleName.setAlignment(HorizontalAlignment.LEFT);
                    contentMealStyleName.setBorderBottom(BorderStyle.THIN);
                    contentMealStyleName.setBorderTop(BorderStyle.THIN);
                    contentMealStyleName.setBorderRight(BorderStyle.THIN);
                    contentMealStyleName.setBorderLeft(BorderStyle.THIN);
                    // header row 5
                    org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                    headerFont.setColor(IndexedColors.BLACK.getIndex());
                    CellStyle headerKidsCellStyle = workbook.createCellStyle();
                    ((XSSFCellStyle) headerKidsCellStyle).setFillForegroundColor(blueOne);

                    int rowParent = 4;
                    Row headerRow5 = sheet.createRow(rowParent);
                    for (int col = 0; col < 8; col++) {
                        Cell cell = headerRow5.createCell(col);
                        CellStyle cellHeaderRow5 = workbook.createCellStyle();
                        cellHeaderRow5.setBorderBottom(BorderStyle.THIN);
                        cellHeaderRow5.setBorderTop(BorderStyle.THIN);
                        cellHeaderRow5.setBorderRight(BorderStyle.THIN);
                        cellHeaderRow5.setBorderLeft(BorderStyle.THIN);
                        cell.setCellStyle(cellHeaderRow5);
                    }

                    int rowChild = 5;
                    Cell cellLesson0 = headerRow5.createCell(0);
                    cellLesson0.setCellValue("STT");
                    cellLesson0.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle0 = workbook.createCellStyle();
                    cellLateStyle0.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson1 = headerRow5.createCell(1);
                    cellLesson1.setCellValue("Tên học sinh");
                    cellLesson1.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle1 = workbook.createCellStyle();
                    cellLateStyle1.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                    cellLateStyle1.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson2 = headerRow5.createCell(2);
                    cellLesson2.setCellValue("Ngày sinh");
                    cellLesson2.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle2 = workbook.createCellStyle();
                    cellLateStyle2.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson3 = headerRow5.createCell(3);
                    cellLesson3.setCellValue("Mã hóa đơn");
                    cellLesson3.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle3 = workbook.createCellStyle();
                    cellLateStyle3.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson4 = headerRow5.createCell(4);
                    cellLesson4.setCellValue("Tổng phải thu - tổng phải chi");
                    cellLesson4.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle4 = workbook.createCellStyle();
                    cellLateStyle4.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson5 = headerRow5.createCell(5);
                    cellLesson5.setCellValue("Tổng đã thu - tổng đã chi");
                    cellLesson5.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle5 = workbook.createCellStyle();
                    cellLateStyle5.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson6 = headerRow5.createCell(6);
                    cellLesson6.setCellValue("Thu thêm");
                    cellLesson6.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle6 = workbook.createCellStyle();
                    cellLateStyle6.setAlignment(HorizontalAlignment.CENTER);

                    Cell cellLesson7 = headerRow5.createCell(7);
                    cellLesson7.setCellValue("Trả lại");
                    cellLesson7.setCellStyle(contentStatusStyle);
                    CellStyle cellLateStyle7 = workbook.createCellStyle();
                    cellLateStyle7.setAlignment(HorizontalAlignment.CENTER);

                    Row headerRow6 = sheet.createRow(rowChild);
                    for (int col = 0; col < 8; col++) {
                        sheet.setColumnWidth(col, widths[col] * 256);
                        Cell cell = headerRow6.createCell(col);
                    }
                    int rowIdx = 5;
                    int i = 0;
                    List<KidsPackageInOutExport> dataKidList = this.getKidsPackageInAndOut(kidsList, yearint, startMonth, endMonth);
                    for (KidsPackageInOutExport x : dataKidList) {
                        i -= -1;
                        Row row = sheet.createRow(rowIdx++);

                        Cell cellId = row.createCell(0);
                        cellId.setCellValue(i);
                        cellId.setCellStyle(contentMealStyle);

                        Cell cellKidName = row.createCell(1);
                        cellKidName.setCellValue(x.getFullName());
                        cellKidName.setCellStyle(contentMealStyleName);

                        String codeList = x.getCodeList().toString();
                        String dataCode = codeList.replace("]", "");
                        String dataCode1 = dataCode.replace("[", "");

                        Cell cellAbsentLetterYes = row.createCell(2);
                        cellAbsentLetterYes.setCellValue(ConvertData.convertLocalDateToString(x.getBirthDay()));
                        cellAbsentLetterYes.setCellStyle(contentMealStyle);

                        Cell cellAbsentLetterNo = row.createCell(3);
                        cellAbsentLetterNo.setCellValue(dataCode1);
                        cellAbsentLetterNo.setCellStyle(contentMealStyleName);


                        double tongthu = x.getMoneyTotal();
                        double tongchi = x.getMoneyPaidTotal();
                        double thuthem = x.getMoneyRemainInTotal();
                        double tralai = x.getMoneyRemainOutTotal();


                        DecimalFormat formatter = new DecimalFormat("###,###,###");
                        String total = formatter.format(tongthu);
                        String totalFill = total.replace(",", ".");

                        String paidtotal = formatter.format(tongchi);
                        String paidtotalFill = paidtotal.replace(",", ".");

                        String remainTotal = formatter.format(thuthem);
                        String remainTotalFill = remainTotal.replace(",", ".");

                        String remainOutTotal = formatter.format(tralai);
                        String remainOutTotalFill = remainOutTotal.replace(",", ".");

                        Cell cellAbsentStatus = row.createCell(4);
                        cellAbsentStatus.setCellValue(totalFill);
                        cellAbsentStatus.setCellStyle(contentMealStyle);

                        Cell cellMorningYes = row.createCell(5);
                        cellMorningYes.setCellValue(paidtotalFill);
                        cellMorningYes.setCellStyle(contentMealStyle);

                        Cell cellMorningNo = row.createCell(6);
                        cellMorningNo.setCellValue(remainTotalFill);
                        cellMorningNo.setCellStyle(contentMealStyle);

                        Cell cellMorningNoNew = row.createCell(7);
                        cellMorningNoNew.setCellValue(remainOutTotalFill);
                        cellMorningNoNew.setCellStyle(contentMealStyle);
                    }
                });
            } else {
                Long IdClassSelect = request.getIdClass();
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(IdClassSelect).orElseThrow();
                List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(IdClassSelect, request);
                Sheet sheet = workbook.createSheet(maClass.getClassName());
                for (int i = 0; i < 4; i++) {
                    Row headerRow = sheet.createRow(i);
                    for (int col = 0; col < columns.length; col++) {
                        Cell cell = headerRow.createCell(col);
                        if (col == 0 && i == 0) {
                            cell.setCellValue("DANH SÁCH TỔNG HỢP THU CHI CỦA HỌC SINH ");
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
                            cell.setCellValue("Lớp: " + maClass.getClassName());
                            CellStyle threeStyle = workbook.createCellStyle();
                            org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
                            cellFont.setFontHeightInPoints((short) 11);
                            cellFont.setBold(true);
                            threeStyle.setFont(cellFont);
                            cell.setCellStyle(threeStyle);
                        } else if (col == 0) {
                            cell.setCellValue("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear);
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
                sheet.createFreezePane(2, 6);
                //Style content  status
                CellStyle contentStatusStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font contentStatusFont = workbook.createFont();
                contentStatusFont.setColor(IndexedColors.BLACK.getIndex());
                contentStatusStyle.setFont(contentStatusFont);
                contentStatusStyle.setWrapText(true);
                Font headerStatus = workbook.createFont();
                headerStatus.setBold(true);
                contentStatusStyle.setFont(headerStatus);
                ((XSSFCellStyle) contentStatusStyle).setFillForegroundColor(headerColor);
                contentStatusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentStatusStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentStatusStyle.setAlignment(HorizontalAlignment.CENTER);
                contentStatusStyle.setBorderBottom(BorderStyle.THIN);
                contentStatusStyle.setBorderTop(BorderStyle.THIN);
                contentStatusStyle.setBorderRight(BorderStyle.THIN);
                contentStatusStyle.setBorderLeft(BorderStyle.THIN);

                //Style content  Meal
                CellStyle contentMealStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font contentMealFont = workbook.createFont();
                contentMealFont.setColor(IndexedColors.BLACK.getIndex());
                contentMealStyle.setFont(contentMealFont);
                contentMealStyle.setWrapText(true);
                ((XSSFCellStyle) contentMealStyle).setFillForegroundColor(greyOne);
                contentMealStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentMealStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentMealStyle.setAlignment(HorizontalAlignment.CENTER);
                contentMealStyle.setBorderBottom(BorderStyle.THIN);
                contentMealStyle.setBorderTop(BorderStyle.THIN);
                contentMealStyle.setBorderRight(BorderStyle.THIN);
                contentMealStyle.setBorderLeft(BorderStyle.THIN);
                //Style content  Meal
                CellStyle contentMealStyleName = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font contentMealFontName = workbook.createFont();
                contentMealFontName.setColor(IndexedColors.BLACK.getIndex());
                contentMealStyleName.setFont(contentMealFontName);
                contentMealStyleName.setWrapText(true);
                ((XSSFCellStyle) contentMealStyleName).setFillForegroundColor(greyOne);
                contentMealStyleName.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                contentMealStyleName.setVerticalAlignment(VerticalAlignment.CENTER);
                contentMealStyleName.setAlignment(HorizontalAlignment.LEFT);
                contentMealStyleName.setBorderBottom(BorderStyle.THIN);
                contentMealStyleName.setBorderTop(BorderStyle.THIN);
                contentMealStyleName.setBorderRight(BorderStyle.THIN);
                contentMealStyleName.setBorderLeft(BorderStyle.THIN);
                // header row 5
                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setColor(IndexedColors.BLACK.getIndex());
                CellStyle headerKidsCellStyle = workbook.createCellStyle();
                ((XSSFCellStyle) headerKidsCellStyle).setFillForegroundColor(blueOne);

                int rowParent = 4;
                Row headerRow5 = sheet.createRow(rowParent);
                for (int col = 0; col < 8; col++) {
                    Cell cell = headerRow5.createCell(col);
                    CellStyle cellHeaderRow5 = workbook.createCellStyle();
                    cellHeaderRow5.setBorderBottom(BorderStyle.THIN);
                    cellHeaderRow5.setBorderTop(BorderStyle.THIN);
                    cellHeaderRow5.setBorderRight(BorderStyle.THIN);
                    cellHeaderRow5.setBorderLeft(BorderStyle.THIN);
                    cell.setCellStyle(cellHeaderRow5);
                }

                int rowChild = 5;
                Cell cellLesson0 = headerRow5.createCell(0);
                cellLesson0.setCellValue("STT");
                cellLesson0.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle0 = workbook.createCellStyle();
                cellLateStyle0.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson1 = headerRow5.createCell(1);
                cellLesson1.setCellValue("Tên học sinh");
                cellLesson1.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle1 = workbook.createCellStyle();
                cellLateStyle1.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                cellLateStyle1.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson2 = headerRow5.createCell(2);
                cellLesson2.setCellValue("Ngày sinh");
                cellLesson2.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle2 = workbook.createCellStyle();
                cellLateStyle2.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson3 = headerRow5.createCell(3);
                cellLesson3.setCellValue("Mã hóa đơn");
                cellLesson3.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle3 = workbook.createCellStyle();
                cellLateStyle3.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson4 = headerRow5.createCell(4);
                cellLesson4.setCellValue("Tổng phải thu - tổng phải chi");
                cellLesson4.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle4 = workbook.createCellStyle();
                cellLateStyle4.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson5 = headerRow5.createCell(5);
                cellLesson5.setCellValue("Tổng đã thu - tổng đã chi");
                cellLesson5.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle5 = workbook.createCellStyle();
                cellLateStyle5.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson6 = headerRow5.createCell(6);
                cellLesson6.setCellValue("Thu thêm");
                cellLesson6.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle6 = workbook.createCellStyle();
                cellLateStyle6.setAlignment(HorizontalAlignment.CENTER);

                Cell cellLesson7 = headerRow5.createCell(7);
                cellLesson7.setCellValue("Trả lại");
                cellLesson7.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle7 = workbook.createCellStyle();
                cellLateStyle7.setAlignment(HorizontalAlignment.CENTER);

                Row headerRow6 = sheet.createRow(rowChild);
                for (int col = 0; col < 8; col++) {
                    sheet.setColumnWidth(col, widths[col] * 256);
                    Cell cell = headerRow6.createCell(col);
                }
                int rowIdx = 5;
                int i = 0;
                List<KidsPackageInOutExport> dataKidList = this.getKidsPackageInAndOut(kidsList, yearint, startMonth, endMonth);
                for (KidsPackageInOutExport x : dataKidList) {
                    i -= -1;
                    Row row = sheet.createRow(rowIdx++);

                    Cell cellId = row.createCell(0);
                    cellId.setCellValue(i);
                    cellId.setCellStyle(contentMealStyle);

                    Cell cellKidName = row.createCell(1);
                    cellKidName.setCellValue(x.getFullName());
                    cellKidName.setCellStyle(contentMealStyleName);

                    String codeList = x.getCodeList().toString();
                    String dataCode = codeList.replace("]", "");
                    String dataCode1 = dataCode.replace("[", "");

                    Cell cellAbsentLetterYes = row.createCell(2);
                    cellAbsentLetterYes.setCellValue(ConvertData.convertLocalDateToString(x.getBirthDay()));
                    cellAbsentLetterYes.setCellStyle(contentMealStyle);

                    Cell cellAbsentLetterNo = row.createCell(3);
                    cellAbsentLetterNo.setCellValue(dataCode1);
                    cellAbsentLetterNo.setCellStyle(contentMealStyleName);


                    double tongthu = x.getMoneyTotal();
                    double tongchi = x.getMoneyPaidTotal();
                    double thuthem = x.getMoneyRemainInTotal();
                    double tralai = x.getMoneyRemainOutTotal();


                    DecimalFormat formatter = new DecimalFormat("###,###,###");
                    String total = formatter.format(tongthu);
                    String totalFill = total.replace(",", ".");

                    String paidtotal = formatter.format(tongchi);
                    String paidtotalFill = paidtotal.replace(",", ".");

                    String remainTotal = formatter.format(thuthem);
                    String remainTotalFill = remainTotal.replace(",", ".");

                    String remainOutTotal = formatter.format(tralai);
                    String remainOutTotalFill = remainOutTotal.replace(",", ".");

                    Cell cellAbsentStatus = row.createCell(4);
                    cellAbsentStatus.setCellValue(totalFill);
                    cellAbsentStatus.setCellStyle(contentMealStyle);

                    Cell cellMorningYes = row.createCell(5);
                    cellMorningYes.setCellValue(paidtotalFill);
                    cellMorningYes.setCellStyle(contentMealStyle);

                    Cell cellMorningNo = row.createCell(6);
                    cellMorningNo.setCellValue(remainTotalFill);
                    cellMorningNo.setCellStyle(contentMealStyle);

                    Cell cellMorningNoNew = row.createCell(7);
                    cellMorningNoNew.setCellValue(remainOutTotalFill);
                    cellMorningNoNew.setCellStyle(contentMealStyle);
                }
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public List<ExcelResponse> exportFinanceKidInAndOutNew(UserPrincipal principal, FinanceSearchKidsRequest request) {
        List<ExcelResponse> responseList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
        String schoolName = school.getSchoolName();
        Long idClass = request.getIdClass();
        String year = request.getYear();
        String newYear = year.substring(0, 4);
        int startMonth = Integer.parseInt(request.getStartMonth());
        int endMonth = Integer.parseInt(request.getEndMonth());
        int yearint = Integer.parseInt(newYear);
        if (idClass == null) {
            List<Long> idClassList = maClassRepository.findIdClassInSchool(idSchool);
            idClassList.forEach(a -> {
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(a).orElseThrow();
                List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(a, request);
                List<ExcelData> bodyList = new ArrayList<>();
                ExcelResponse response = new ExcelResponse();
                List<String> headerStringList = Arrays.asList("DANH SÁCH TỔNG HỢP THU CHI CỦA HỌC SINH CHƯA HOÀN THÀNH", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_DATE.concat("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear));
                List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
                response.setSheetName(maClass.getClassName());
                response.setHeaderList(headerList);
                List<KidsPackageInOutExport> dataKidList = this.getKidsPackageInAndOut(kidsList, yearint, startMonth, endMonth);
                long i = 0;
                for (KidsPackageInOutExport x : dataKidList) {
                    String code = x.getCodeList().toString().replace("[", "");
                    String dataCode = code.replace("]", "");
                    String moneyTotal = this.setMoneyDouble(x.getMoneyTotal());
                    String moneyPaidTotal = this.setMoneyDouble(x.getMoneyPaidTotal());
                    String moneyRemainInTotal = this.setMoneyDouble(x.getMoneyRemainInTotal());
                    String moneyRemainOutTotal = this.setMoneyDouble(x.getMoneyRemainOutTotal());

                    List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getFullName(), ConvertData.convertLocalDateToString(x.getBirthDay()), dataCode,
                            moneyTotal, moneyPaidTotal, moneyRemainInTotal, moneyRemainOutTotal);
                    ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                    bodyList.add(modelData);
                }
                response.setBodyList(bodyList);
                responseList.add(response);
            });
        } else {
            Long idClassSelect = request.getIdClass();
            MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClassSelect).orElseThrow();
            List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(idClassSelect, request);
            List<ExcelData> bodyList = new ArrayList<>();
            ExcelResponse response = new ExcelResponse();
            List<String> headerStringList = Arrays.asList("DANH SÁCH TỔNG HỢP THU CHI CỦA HỌC SINH CHƯA HOÀN THÀNH", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_DATE.concat("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear));
            List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
            response.setSheetName(maClass.getClassName());
            response.setHeaderList(headerList);
            List<KidsPackageInOutExport> dataKidList = this.getKidsPackageInAndOut(kidsList, yearint, startMonth, endMonth);
            long i = 0;
            for (KidsPackageInOutExport x : dataKidList) {
                String code = x.getCodeList().toString().replace("[", "");
                String dataCode = code.replace("]", "");
                String moneyTotal = this.setMoneyDouble(x.getMoneyTotal());
                String moneyPaidTotal = this.setMoneyDouble(x.getMoneyPaidTotal());
                String moneyRemainInTotal = this.setMoneyDouble(x.getMoneyRemainInTotal());
                String moneyRemainOutTotal = this.setMoneyDouble(x.getMoneyRemainOutTotal());

                List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getFullName(), String.valueOf(x.getBirthDay()), dataCode,
                        moneyTotal, moneyPaidTotal, moneyRemainInTotal, moneyRemainOutTotal);
                ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                bodyList.add(modelData);
            }
            response.setBodyList(bodyList);
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public List<ExcelResponse> exportFinanceKidInAndOutTrueNew(UserPrincipal principal, FinanceSearchKidsRequest request) {
        List<ExcelResponse> responseList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
        String schoolName = school.getSchoolName();
        Long idClass = request.getIdClass();
        String year = request.getYear();
        String newYear = year.substring(0, 4);
        int startMonth = Integer.parseInt(request.getStartMonth());
        int endMonth = Integer.parseInt(request.getEndMonth());
        int yearint = Integer.parseInt(newYear);
        if (idClass == null) {
            List<Long> idClassList = maClassRepository.findIdClassInSchool(idSchool);
            idClassList.forEach(a -> {
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(a).orElseThrow();
                List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(a, request);
                List<ExcelData> bodyList = new ArrayList<>();
                ExcelResponse response = new ExcelResponse();
                List<String> headerStringList = Arrays.asList("DANH SÁCH TỔNG HỢP THU CHI CỦA HỌC SINH ĐÃ HOÀN THÀNH", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_DATE.concat("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear));
                List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
                response.setSheetName(maClass.getClassName());
                response.setHeaderList(headerList);
                List<KidsPackageInOutExport> dataKidList = this.getKidsPackageInAndOutTrue(kidsList, yearint, startMonth, endMonth);
                long i = 0;
                for (KidsPackageInOutExport x : dataKidList) {
                    String code = x.getCodeList().toString().replace("[", "");
                    String dataCode = code.replace("]", "");
                    String moneyTotal = this.setMoneyDouble(x.getMoneyTotal());
                    List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getFullName(), ConvertData.convertLocalDateToString(x.getBirthDay()), dataCode,
                            moneyTotal);
                    ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                    bodyList.add(modelData);
                }
                response.setBodyList(bodyList);
                responseList.add(response);
            });
        } else {
            Long idClassSelect = request.getIdClass();
            MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClassSelect).orElseThrow();
            List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(idClassSelect, request);
            List<ExcelData> bodyList = new ArrayList<>();
            ExcelResponse response = new ExcelResponse();
            List<String> headerStringList = Arrays.asList("DANH SÁCH TỔNG HỢP THU CHI CỦA HỌC SINH ĐÃ HOÀN THÀNH", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_DATE.concat("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear));
            List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
            response.setSheetName(maClass.getClassName());
            response.setHeaderList(headerList);
            List<KidsPackageInOutExport> dataKidList = this.getKidsPackageInAndOutTrue(kidsList, yearint, startMonth, endMonth);
            long i = 0;
            for (KidsPackageInOutExport x : dataKidList) {
                String code = x.getCodeList().toString().replace("[", "");
                String dataCode = code.replace("]", "");
                String moneyTotal = this.setMoneyDouble(x.getMoneyTotal());
                List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getFullName(), String.valueOf(x.getBirthDay()), dataCode,
                        moneyTotal);
                ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                bodyList.add(modelData);
            }
            response.setBodyList(bodyList);
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public List<ExcelResponse> exportFinanceKidInAndOutTotalNew(UserPrincipal principal, FinanceSearchKidsRequest request) {
        List<ExcelResponse> responseList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
        String schoolName = school.getSchoolName();
        Long idClass = request.getIdClass();
        String year = request.getYear();
        String newYear = year.substring(0, 4);
        int startMonth = Integer.parseInt(request.getStartMonth());
        int endMonth = Integer.parseInt(request.getEndMonth());
        int yearint = Integer.parseInt(newYear);
        if (idClass == null) {
            List<Long> idClassList = maClassRepository.findIdClassInSchool(idSchool);
            idClassList.forEach(a -> {
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(a).orElseThrow();
                List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(a, request);
                List<ExcelData> bodyList = new ArrayList<>();
                ExcelResponse response = new ExcelResponse();
                List<String> headerStringList = Arrays.asList("DANH SÁCH TỔNG HỢP THU CHI CỦA HỌC SINH", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_DATE.concat("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear));
                List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
                response.setSheetName(maClass.getClassName());
                response.setHeaderList(headerList);
                List<KidsPackageInOutExport> dataKidList = this.getKidsPackageInAndOutTotal(kidsList, yearint, startMonth, endMonth);
                long i = 0;
                for (KidsPackageInOutExport x : dataKidList) {
                    String code = x.getCodeList().toString().replace("[", "");
                    String dataCode = code.replace("]", "");
                    String moneyTotal = this.setMoneyDouble(x.getMoneyTotal());
                    String moneyPaidTotal = this.setMoneyDouble(x.getMoneyPaidTotal());
                    String moneyRemainInTotal = this.setMoneyDouble(x.getMoneyRemainInTotal());
                    String moneyRemainOutTotal = this.setMoneyDouble(x.getMoneyRemainOutTotal());

                    List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getFullName(), String.valueOf(x.getBirthDay()), dataCode,
                            moneyTotal, moneyPaidTotal, moneyRemainInTotal, moneyRemainOutTotal);
                    ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                    bodyList.add(modelData);
                }
                response.setBodyList(bodyList);
                responseList.add(response);
            });
        } else {
            Long idClassSelect = request.getIdClass();
            MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClassSelect).orElseThrow();
            List<Kids> kidsList = kidsRepository.searchByStatusOrderByName(idClassSelect, request);
            List<ExcelData> bodyList = new ArrayList<>();
            ExcelResponse response = new ExcelResponse();
            List<String> headerStringList = Arrays.asList("DANH SÁCH TỔNG HỢP THU CHI CỦA HỌC SINH", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(maClass.getClassName()), AppConstant.EXCEL_DATE.concat("Thời gian: " + "tháng " + request.getStartMonth() + " tới " + "tháng " + request.getEndMonth() + " năm " + newYear));
            List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
            response.setSheetName(maClass.getClassName());
            response.setHeaderList(headerList);
            List<KidsPackageInOutExport> dataKidList = this.getKidsPackageInAndOutTotal(kidsList, yearint, startMonth, endMonth);
            long i = 0;
            for (KidsPackageInOutExport x : dataKidList) {
                String code = x.getCodeList().toString().replace("[", "");
                String dataCode = code.replace("]", "");
                String moneyTotal = this.setMoneyDouble(x.getMoneyTotal());
                String moneyPaidTotal = this.setMoneyDouble(x.getMoneyPaidTotal());
                String moneyRemainInTotal = this.setMoneyDouble(x.getMoneyRemainInTotal());
                String moneyRemainOutTotal = this.setMoneyDouble(x.getMoneyRemainOutTotal());

                List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getFullName(), String.valueOf(x.getBirthDay()), dataCode,
                        moneyTotal, moneyPaidTotal, moneyRemainInTotal, moneyRemainOutTotal);
                ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
                bodyList.add(modelData);
            }
            response.setBodyList(bodyList);
            responseList.add(response);
        }
        return responseList;
    }

    private String setMoneyDouble(double money){
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String total = formatter.format(money);
        return total.replace(",", ".");
    }
}
