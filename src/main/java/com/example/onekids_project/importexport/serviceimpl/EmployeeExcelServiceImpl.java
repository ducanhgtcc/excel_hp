package com.example.onekids_project.importexport.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.AccountType;
import com.example.onekids_project.entity.school.Department;
import com.example.onekids_project.importexport.model.*;
import com.example.onekids_project.importexport.service.EmployeeExcelService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.AccountTypeRepository;
import com.example.onekids_project.repository.DepartmentRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.request.department.TabDepartmentRequest;
import com.example.onekids_project.request.department.TabProfessionalRequest;
import com.example.onekids_project.request.employee.CreateEmployeeExcelRequest;
import com.example.onekids_project.request.employee.CreateEmployeeMainInfoRequest;
import com.example.onekids_project.request.employee.CreateEmployeeRequest;
import com.example.onekids_project.request.kids.AppIconParentRequest;
import com.example.onekids_project.request.kids.AppIconTeacherRequest;
import com.example.onekids_project.response.excel.ExcelDataNew;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.school.ListAppIconTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppIconTeacherAddSerivce;
import com.example.onekids_project.service.servicecustom.AppIconTeacherService;
import com.example.onekids_project.service.servicecustom.EmployeeService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.ExportExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeExcelServiceImpl implements EmployeeExcelService {

    @Autowired
    private ListMapper listMapper;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private AccountTypeRepository accountTypeRepository;
    @Autowired
    private AppIconTeacherService appIconTeacherService;

    @Autowired
    private AppIconTeacherAddSerivce appIconTeacherAddSerivce;
    @Autowired
    private EmployeeService employeeService;


    @Override
    public ByteArrayInputStream customersToExcel(List<EmployeeModel> employeeModelList, String nameSchool) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(255, 255, 255));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(255, 255, 0));
        int[] widths = {5, 10, 20, 17, 12, 20, 16, 16, 17, 23, 16, 18, 23, 23, 11, 11, 11, 13, 15, 15, 15};

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        String dateToStr = df.format(currentDate);

        String[] COLUMNs = {"STT", "Tình trạng", "Họ và tên", "Ngày sinh", "Giới tính", "TK/SĐT", "Ngày vào", "Ngày ký HĐ", "Ngày hết hạn HĐ", "Email", "Số CMND", "Ngày cấp", "Địa chỉ thường chú", "Chỗ ở hiện tại", "Hôn nhân", "Số con", "Trình độ", "Chuyên môn", "Phòng ban", "Đối tượng", "Ghi chú"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("NHAN_SU");

            sheet.setDisplayGridlines(false);
            for (int i = 0; i < 4; i++) {
                Row headerRow = sheet.createRow(i);
                for (int col = 0; col < COLUMNs.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    if (col == 0 && i == 0) {
                        cell.setCellValue("DANH SÁCH NHÂN SỰ");
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
            sheet.createFreezePane(7, 5);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();

            ((XSSFCellStyle) headerCellStyle).setFillForegroundColor(blueOne);
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            headerCellStyle.setFont(headerFont);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);


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
                cell.setCellStyle(headerCellStyle);
            }


            int rowIdx = 5;
            for (EmployeeModel employeeModel : employeeModelList) {
                Row row = sheet.createRow(rowIdx++);

                Cell cellStt = row.createCell(0);
                cellStt.setCellValue(employeeModel.getStt());
                cellStt.setCellStyle(contentCellStyle);

                Cell cellStatus = row.createCell(1);
                cellStatus.setCellValue(employeeModel.getEmployeeStatus());
                cellStatus.setCellStyle(contentCellStyle);

                Cell cellFullName = row.createCell(2);
                cellFullName.setCellValue(employeeModel.getFullName());
                cellFullName.setCellStyle(contentCellStyle);

                Cell cellBirthday = row.createCell(3);
                cellBirthday.setCellValue(employeeModel.getBirthday());
                cellBirthday.setCellStyle(contentCellStyle);

                Cell cellGender = row.createCell(4);
                cellGender.setCellValue(employeeModel.getGender());
                cellGender.setCellStyle(contentCellStyle);


                Cell cellPhone = row.createCell(5);
                cellPhone.setCellValue(employeeModel.getPhone());
                cellPhone.setCellStyle(contentCellStyle);


                Cell cellStartDate = row.createCell(6);
                cellStartDate.setCellValue(employeeModel.getStartDate());
                cellStartDate.setCellStyle(contentCellStyle);


                Cell cellContractDate = row.createCell(7);
                cellContractDate.setCellValue(employeeModel.getContractDate());
                cellContractDate.setCellStyle(contentCellStyle);


                Cell cellEndDate = row.createCell(8);
                cellEndDate.setCellValue(employeeModel.getEndDate());
                cellEndDate.setCellStyle(contentCellStyle);

                Cell cellEmail = row.createCell(9);
                cellEmail.setCellValue(employeeModel.getEmail());
                cellEmail.setCellStyle(contentCellStyle);


                Cell cellCmnd = row.createCell(10);
                cellCmnd.setCellValue(employeeModel.getCmnd());
                cellCmnd.setCellStyle(contentCellStyle);

                Cell cellCmndDate = row.createCell(11);
                cellCmndDate.setCellValue(employeeModel.getCmndDate());
                cellCmndDate.setCellStyle(contentCellStyle);

                Cell cellPermanentAddress = row.createCell(12);
                cellPermanentAddress.setCellValue(employeeModel.getPermanentAddress());
                cellPermanentAddress.setCellStyle(contentCellStyle);

                Cell cellAddress = row.createCell(13);
                cellAddress.setCellValue(employeeModel.getAddress());
                cellAddress.setCellStyle(contentCellStyle);

                Cell cellmarriedStatus = row.createCell(14);
                cellmarriedStatus.setCellValue(employeeModel.getMarriedStatus());
                cellmarriedStatus.setCellStyle(contentCellStyle);
                if (employeeModel.getNumberChildren() != null) {
                    Cell cellNumberChildren = row.createCell(15);
                    cellNumberChildren.setCellValue(employeeModel.getNumberChildren());
                    cellNumberChildren.setCellStyle(contentCellStyle);
                } else {
                    Cell cellNumberChildren = row.createCell(15);
                    cellNumberChildren.setCellValue(0);
                    cellNumberChildren.setCellStyle(contentCellStyle);
                }

                Cell cellEducationLevel = row.createCell(16);
                cellEducationLevel.setCellValue(employeeModel.getEducationLevel());
                cellEducationLevel.setCellStyle(contentCellStyle);

                Cell cellProfessional = row.createCell(17);
                cellProfessional.setCellValue(employeeModel.getProfessional());
                cellProfessional.setCellStyle(contentCellStyle);

                Cell cellNameDepartment = row.createCell(18);
                cellNameDepartment.setCellValue(employeeModel.getNameDepartment());
                cellNameDepartment.setCellStyle(contentCellStyle);

                Cell cellObjectEmployee = row.createCell(19);
                cellObjectEmployee.setCellValue(employeeModel.getObjectEmployee().replace("[", "").replace("]", ""));
                cellObjectEmployee.setCellStyle(contentCellStyle);

                Cell cellNote = row.createCell(20);
                cellNote.setCellValue(employeeModel.getNote());
                cellNote.setCellStyle(contentCellStyle);

            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public ListEmployeeModelImport importExcelEmployee(UserPrincipal principal, MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ListEmployeeModelImport listEmployeeModelImport = new ListEmployeeModelImport();
        List<EmployeeModelImport> employeeModels = new ArrayList<>();
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = firstSheet.iterator();
        rowIterator.next();
        while (rowIterator.hasNext()) { // check row
            Row nextRow = rowIterator.next(); // next row
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            if (nextRow.getRowNum() > 4) {
                EmployeeModelImport employeeModel = new EmployeeModelImport();
                while (cellIterator.hasNext()) {
                    try {
                        Cell nextCell = cellIterator.next();
                        int columnIndex1 = nextCell.getColumnIndex();
                        switch (columnIndex1) {
                            case 0:
                                employeeModel.setStt((long) nextCell.getNumericCellValue());
                                continue;
                            case 1:
                                employeeModel.setEmployeeStatus(nextCell.getStringCellValue());
                                continue;
                            case 2:
                                employeeModel.setFullName(nextCell.getStringCellValue());
                                continue;
                            case 3:
//                                employeeModel.setBirthday(LocalDate.parse(sdf.format(nextCell.getDateCellValue())));
                                employeeModel.setBirthday(nextCell.getStringCellValue());
                                break;
                            case 4:
                                if (nextCell.getStringCellValue().equalsIgnoreCase(AppConstant.MALE)) {
                                    employeeModel.setGender(AppConstant.MALE);
                                } else if (nextCell.getStringCellValue().equalsIgnoreCase(AppConstant.FEMALE)) {
                                    employeeModel.setGender(AppConstant.FEMALE);
                                }
                                break;
                            case 5:
                                employeeModel.setPhone(nextCell.getStringCellValue());
                                break;
                            case 6:
                                employeeModel.setStartDate(nextCell.getStringCellValue());
                                break;
                            case 7:
                                employeeModel.setContractDate(nextCell.getStringCellValue());
                                break;
                            case 8:
                                employeeModel.setEndDate(nextCell.getStringCellValue());
                                break;
                            case 9:
                                employeeModel.setEmail(nextCell.getStringCellValue());
                                break;
                            case 10:
                                employeeModel.setCmnd(nextCell.getStringCellValue());
                                break;
                            case 11:
                                employeeModel.setCmndDate(nextCell.getStringCellValue());
                                break;
                            case 12:
                                employeeModel.setPermanentAddress(nextCell.getStringCellValue());
                                break;
                            case 13:
                                employeeModel.setAddress(nextCell.getStringCellValue());
                                break;
                            case 14:
                                employeeModel.setMarriedStatus(nextCell.getStringCellValue());
                                break;
                            case 15:
                                employeeModel.setNumberChildren((int) nextCell.getNumericCellValue());
                                break;
                            case 16:
                                employeeModel.setEducationLevel(nextCell.getStringCellValue());
                                break;
                            case 17:
                                employeeModel.setProfessional(nextCell.getStringCellValue());
                                break;
                            case 18:
                                employeeModel.setNameDepartment(nextCell.getStringCellValue());
                                break;
                            case 19:
                                employeeModel.setObjectEmployee(nextCell.getStringCellValue());
                                break;
                            case 20:
                                employeeModel.setNote(nextCell.getStringCellValue());
                                break;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
                employeeModels.add(employeeModel);
            }
        }
        List<EmployeeModelImport> employeeModelsFail = employeeModels.stream().filter(x -> x.getFullName().isEmpty() || x.getBirthday().isEmpty() || x.getGender().isEmpty() || x.getPhone().isEmpty() || x.getStartDate().isEmpty()).collect(Collectors.toList());
        List<EmployeeModelImportFail> employeeModelImportFail = listMapper.mapList(employeeModelsFail, EmployeeModelImportFail.class);
        employeeModels = employeeModels.stream().filter(x -> !x.getFullName().isEmpty() && !x.getBirthday().isEmpty() && !x.getGender().isEmpty() && !x.getPhone().isEmpty() && !x.getStartDate().isEmpty()).collect(Collectors.toList());

        listEmployeeModelImport.setEmployeeModelImportFailList(employeeModelImportFail);
        listEmployeeModelImport.setEmployeeModelImportList(employeeModels);
        return listEmployeeModelImport;
    }

    @Override
    public ByteArrayInputStream customEmployeeImportFailExcel(List<EmployeeModelImportFail> employeeModelImportFailList, String nameSchool) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(255, 255, 255));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(255, 255, 0));
        XSSFColor redOne = new XSSFColor(new java.awt.Color(255, 64, 0));

        int[] widths = {5, 10, 20, 17, 12, 20, 16, 16, 17, 23, 16, 18, 23, 23, 11, 11, 11, 13, 15, 15, 15};

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        String dateToStr = df.format(currentDate);

        String[] COLUMNs = {"STT", "Tình trạng", "Họ và tên", "Ngày sinh", "Giới tính", "TK/SĐT", "Ngày vào", "Ngày ký HĐ", "Ngày hết hạn HĐ", "Email", "Số CMND", "Ngày cấp", "Địa chỉ thường chú", "Chỗ ở hiện tại", "Hôn nhân", "Số con", "Trình độ", "Chuyên môn", "Phòng ban", "Đối tượng", "Ghi chú"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("NHAN_SU");

            sheet.setDisplayGridlines(false);
            for (int i = 0; i < 4; i++) {
                Row headerRow = sheet.createRow(i);
                for (int col = 0; col < COLUMNs.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    if (col == 0 && i == 0) {
                        cell.setCellValue("DANH SÁCH NHÂN SỰ LỖI DATA");
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
                        cell.setCellValue("Lưu ý: + Bạn không được để trống các ô màu đỏ \n" + "+ Fomat dạng TEXT dữ liệu ngày theo dạng 01/11/1111 \n");
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
            sheet.createFreezePane(7, 5);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            Font headerFontWhite = workbook.createFont();
            headerFontWhite.setBold(true);
            headerFontWhite.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();

            ((XSSFCellStyle) headerCellStyle).setFillForegroundColor(blueOne);
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            headerCellStyle.setFont(headerFont);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle headerCellStyleRed = workbook.createCellStyle();

            ((XSSFCellStyle) headerCellStyleRed).setFillForegroundColor(redOne);
            headerCellStyleRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            headerCellStyleRed.setFont(headerFontWhite);
            headerCellStyleRed.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyleRed.setBorderBottom(BorderStyle.THIN);
            headerCellStyleRed.setBorderTop(BorderStyle.THIN);
            headerCellStyleRed.setBorderRight(BorderStyle.THIN);
            headerCellStyleRed.setBorderLeft(BorderStyle.THIN);


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
                if (col < 7) {
                    cell.setCellStyle(headerCellStyleRed);
                } else {
                    cell.setCellStyle(headerCellStyle);
                }
            }


            int rowIdx = 5;
            for (EmployeeModelImportFail employeeModel : employeeModelImportFailList) {
                Row row = sheet.createRow(rowIdx++);

                Cell cellStt = row.createCell(0);
                cellStt.setCellValue(employeeModel.getStt());
                cellStt.setCellStyle(contentCellStyle);

                Cell cellStatus = row.createCell(1);
                cellStatus.setCellValue(employeeModel.getEmployeeStatus());
                cellStatus.setCellStyle(contentCellStyle);

                Cell cellFullName = row.createCell(2);
                cellFullName.setCellValue(employeeModel.getFullName());
                cellFullName.setCellStyle(contentCellStyle);

                Cell cellBirthday = row.createCell(3);
                String date = employeeModel.getBirthday() != null ? employeeModel.getBirthday() : "";
                cellBirthday.setCellValue(date);
                cellBirthday.setCellStyle(contentCellStyle);

                Cell cellGender = row.createCell(4);
                cellGender.setCellValue(employeeModel.getGender());
                cellGender.setCellStyle(contentCellStyle);


                Cell cellPhone = row.createCell(5);
                cellPhone.setCellValue(employeeModel.getPhone());
                cellPhone.setCellStyle(contentCellStyle);


                Cell cellStartDate = row.createCell(6);
                String dateStart = employeeModel.getStartDate() != null ? employeeModel.getStartDate() : "";
                cellStartDate.setCellValue(dateStart);
                cellStartDate.setCellStyle(contentCellStyle);


                Cell cellContractDate = row.createCell(7);
                String dateContract = employeeModel.getContractDate() != null ? employeeModel.getContractDate() : "";

                cellContractDate.setCellValue(dateContract);
                cellContractDate.setCellStyle(contentCellStyle);


                Cell cellEndDate = row.createCell(8);
                String dateEnd = employeeModel.getEndDate() != null ? employeeModel.getEndDate() : "";
                cellEndDate.setCellValue(dateEnd);
                cellEndDate.setCellStyle(contentCellStyle);

                Cell cellEmail = row.createCell(9);
                cellEmail.setCellValue(employeeModel.getEmail());
                cellEmail.setCellStyle(contentCellStyle);


                Cell cellCmnd = row.createCell(10);
                cellCmnd.setCellValue(employeeModel.getCmnd());
                cellCmnd.setCellStyle(contentCellStyle);

                Cell cellCmndDate = row.createCell(11);
                String dateCmnd = employeeModel.getCmndDate() != null ? employeeModel.getCmndDate() : "";
                cellCmndDate.setCellValue(dateCmnd);
                cellCmndDate.setCellStyle(contentCellStyle);

                Cell cellPermanentAddress = row.createCell(12);
                cellPermanentAddress.setCellValue(employeeModel.getPermanentAddress());
                cellPermanentAddress.setCellStyle(contentCellStyle);

                Cell cellAddress = row.createCell(13);
                cellAddress.setCellValue(employeeModel.getAddress());
                cellAddress.setCellStyle(contentCellStyle);

                Cell cellmarriedStatus = row.createCell(14);
                cellmarriedStatus.setCellValue(employeeModel.getMarriedStatus());
                cellmarriedStatus.setCellStyle(contentCellStyle);
                if (employeeModel.getNumberChildren() != null) {
                    Cell cellNumberChildren = row.createCell(15);
                    cellNumberChildren.setCellValue(employeeModel.getNumberChildren());
                    cellNumberChildren.setCellStyle(contentCellStyle);
                } else {
                    Cell cellNumberChildren = row.createCell(15);
                    cellNumberChildren.setCellValue(0);
                    cellNumberChildren.setCellStyle(contentCellStyle);
                }

                Cell cellEducationLevel = row.createCell(16);
                cellEducationLevel.setCellValue(employeeModel.getEducationLevel());
                cellEducationLevel.setCellStyle(contentCellStyle);

                Cell cellProfessional = row.createCell(17);
                cellProfessional.setCellValue(employeeModel.getProfessional());
                cellProfessional.setCellStyle(contentCellStyle);

                Cell cellNameDepartment = row.createCell(18);
                cellNameDepartment.setCellValue(employeeModel.getNameDepartment());
                cellNameDepartment.setCellStyle(contentCellStyle);

                Cell cellObjectEmployee = row.createCell(19);
                cellObjectEmployee.setCellValue(employeeModel.getObjectEmployee().replace("[", "").replace("]", ""));
                cellObjectEmployee.setCellStyle(contentCellStyle);

                Cell cellNote = row.createCell(20);
                cellNote.setCellValue(employeeModel.getNote());
                cellNote.setCellStyle(contentCellStyle);

            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public ByteArrayInputStream exportAttendanceEmployeeDate(UserPrincipal principal, List<AttendanceEmployeeDate> modelList, LocalDate date) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(131, 164, 196));
        XSSFColor greenOne = new XSSFColor(new java.awt.Color(149, 203, 74));
        XSSFColor yellowTwo = new XSSFColor(new java.awt.Color(217, 210, 144));
        XSSFColor blueTwo = new XSSFColor(new java.awt.Color(51, 153, 255));
        XSSFColor yellowThree = new XSSFColor(new java.awt.Color(224, 191, 28));

        int[] widths = {5, 30, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 10, 10, 10, 10};
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateToStrSheet = df1.format(date);
        String schoolName = principal.getSchool().getSchoolName();

        String[] columns = {"STT", "Họ và tên", "Nghỉ có phép", "Nghỉ không phép", "Đi làm", "Có phép", "Không phép", "Đi làm", "Có phép", "Không phép", "Đi làm", "Có phép", "Không phép", "Đi làm", "Sáng", "Trưa", "Tối", "Đến", "Về", "Phút", "Phút"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet(dateToStrSheet);

            sheet.setDisplayGridlines(false);
            for (int i = 0; i < 3; i++) {
                Row headerRow = sheet.createRow(i);
                for (int col = 0; col < columns.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    if (col == 0 && i == 0) {
                        cell.setCellValue("BẢNG KÊ CHẤM CÔNG NGÀY");
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
                        cell.setCellValue("Ngày: " + df.format(date));
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
            for (int col = 0; col < 21; col++) {
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
            cellAttendance.setCellValue("CHẤM CÔNG NHÂN VIÊN");
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
            cellStatus.setCellValue("TRẠNG THÁI ĐI LÀM");
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
            cellLesson.setCellValue("BUỔI LÀM");


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
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 14, 16));

            Cell cellShuttle = headerRow4.createCell(17);
            cellShuttle.setCellValue("GIỜ ĐẾN - GIỜ VỀ");
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
            sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 17, 18));

            Cell cellLate = headerRow4.createCell(19);
            cellLate.setCellValue("ĐI MUỘN");
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

            Cell cellSoon = headerRow4.createCell(20);
            cellSoon.setCellValue("VỀ SỚM");
            Font headerSoon = workbook.createFont();
            headerSoon.setBold(true);
            cellSoon.setCellStyle(cellLateStyle);


            int rowParent = 5;
            Row headerRow5 = sheet.createRow(rowParent);
            // Header 4
            for (int col = 0; col < 21; col++) {
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
            cellLesson4.setCellValue("Đi làm");
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

            Cell cellLesson11 = headerRow5.createCell(15);
            cellLesson11.setCellValue("Trưa");
            cellLesson11.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle10 = workbook.createCellStyle();
            cellLateStyle10.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 15, 15));

            Cell cellLesson14 = headerRow5.createCell(16);
            cellLesson14.setCellValue("Tối");
            cellLesson14.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle13 = workbook.createCellStyle();
            cellLateStyle13.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 16, 16));

            Cell cellLesson15 = headerRow5.createCell(17);
            cellLesson15.setCellValue("Đến");
            cellLesson15.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle14 = workbook.createCellStyle();
            cellLateStyle14.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 17, 17));

            Cell cellLesson16 = headerRow5.createCell(18);
            cellLesson16.setCellValue("Về");
            cellLesson16.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle15 = workbook.createCellStyle();
            cellLateStyle15.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 18, 18));

            Cell cellLesson17 = headerRow5.createCell(19);
            cellLesson17.setCellValue("Phút");
            cellLesson17.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle16 = workbook.createCellStyle();
            cellLateStyle16.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 19, 19));

            Cell cellLesson18 = headerRow5.createCell(20);
            cellLesson18.setCellValue("Phút");
            cellLesson18.setCellStyle(headerKidsCellStyle);
            CellStyle cellLateStyle17 = workbook.createCellStyle();
            cellLateStyle17.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 20, 20));


            Row headerRow6 = sheet.createRow(rowChild);

// Header
            for (int col = 0; col < 21; col++) {

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
            int index = 0;
            for (AttendanceEmployeeDate model : modelList) {
                index++;
                Row row = sheet.createRow(rowIdx++);

                Cell cellId = row.createCell(0);
                cellId.setCellValue(index);
                cellId.setCellStyle(contentCellStyle);

                Cell cellKidName = row.createCell(1);
                cellKidName.setCellValue(model.getName());
                cellKidName.setCellStyle(contentCellStyle);

                Cell cellAbsentLetterYes = row.createCell(2);
                cellAbsentLetterYes.setCellValue(model.isAbsentYes() ? "x" : "");
                cellAbsentLetterYes.setCellStyle(contentStatusStyle);

                Cell cellAbsentLetterNo = row.createCell(3);
                cellAbsentLetterNo.setCellValue(model.isAbsentNo() ? "x" : "");
                cellAbsentLetterNo.setCellStyle(contentStatusStyle);

                Cell cellAbsentStatus = row.createCell(4);
                cellAbsentStatus.setCellValue(model.isGoSchool() ? "x" : "");
                cellAbsentStatus.setCellStyle(contentStatusStyle);

                Cell cellMorningYes = row.createCell(5);
                cellMorningYes.setCellValue(model.isMorningYes() ? "x" : "");
                cellMorningYes.setCellStyle(contentLessonStyle);

                Cell cellMorningNo = row.createCell(6);
                cellMorningNo.setCellValue(model.isMorningNo() ? "x" : "");
                cellMorningNo.setCellStyle(contentLessonStyle);

                Cell cellMorning = row.createCell(7);
                cellMorning.setCellValue(model.isMorning() ? "x" : "");
                cellMorning.setCellStyle(contentLessonStyle);

                Cell cellAfternoonYes = row.createCell(8);
                cellAfternoonYes.setCellValue(model.isAfternoonYes() ? "x" : "");
                cellAfternoonYes.setCellStyle(contentLessonStyle);


                Cell cellAfternoonNo = row.createCell(9);
                cellAfternoonNo.setCellValue(model.isAfternoonNo() ? "x" : "");
                cellAfternoonNo.setCellStyle(contentLessonStyle);

                Cell cellAfternoon = row.createCell(10);
                cellAfternoon.setCellValue(model.isAfternoon() ? "x" : "");
                cellAfternoon.setCellStyle(contentLessonStyle);

                Cell cellEveningYes = row.createCell(11);
                cellEveningYes.setCellValue(model.isEveningYes() ? "x" : "");
                cellEveningYes.setCellStyle(contentLessonStyle);

                Cell cellEveningNo = row.createCell(12);
                cellEveningNo.setCellValue(model.isEveningNo() ? "x" : "");
                cellEveningNo.setCellStyle(contentLessonStyle);

                Cell cellEvening = row.createCell(13);
                cellEvening.setCellValue(model.isEvening() ? "x" : "");
                cellEvening.setCellStyle(contentLessonStyle);

                Cell cellEatBreakfast = row.createCell(14);
                cellEatBreakfast.setCellValue(model.isBreakfast() ? "x" : "");
                cellEatBreakfast.setCellStyle(contentMealStyle);

                Cell celleatLunch = row.createCell(15);
                celleatLunch.setCellValue(model.isLunch() ? "x" : "");
                celleatLunch.setCellStyle(contentMealStyle);

                Cell celleatDinner = row.createCell(16);
                celleatDinner.setCellValue(model.isDinner() ? "x" : "");
                celleatDinner.setCellStyle(contentMealStyle);

                Cell celltimeArriveKid = row.createCell(17);
                celltimeArriveKid.setCellValue(model.getArriveTime() == null ? "" : ConvertData.convertTimeHHMM(model.getArriveTime()));
                celltimeArriveKid.setCellStyle(contentShuttleStyle);

                Cell celltimeLeaveKid = row.createCell(18);
                celltimeLeaveKid.setCellValue(model.getLeaveTime() == null ? "" : ConvertData.convertTimeHHMM(model.getLeaveTime()));
                celltimeLeaveKid.setCellStyle(contentShuttleStyle);

                Cell cellminutePickupLate = row.createCell(19);
                cellminutePickupLate.setCellValue(model.getMinuteArriveLate());
                cellminutePickupLate.setCellStyle(contentLateStyle);

                Cell cellminutePickupSoon = row.createCell(20);
                cellminutePickupSoon.setCellValue(model.getMinuteLeaveSoon());
                cellminutePickupSoon.setCellStyle(contentLateStyle);

            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public List<ExcelNewResponse> exportAttendanceEmployeeDateNew(UserPrincipal principal, List<AttendanceEmployeeDate> modelList, LocalDate date) {
        List<ExcelNewResponse> responseList = new ArrayList<>();
        ExcelNewResponse response = new ExcelNewResponse();
        List<ExcelDataNew> bodyList = new ArrayList<>();

        String schoolName = principal.getSchool().getSchoolName();
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateToStrSheet = df1.format(date);

        List<String> headerStringList = Arrays.asList("BẢNG KÊ CHẤM CÔNG NGÀY", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_DATE.concat(ConvertData.convertLocalDateToString(date)), "");
        List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
        ExcelDataNew headerMulti = this.setHeaderMulti();
        ExcelDataNew headerMulti1 = this.setHeaderMulti1();
        headerList.add(headerMulti);
        headerList.add(headerMulti1);
        response.setSheetName(dateToStrSheet);
        response.setHeaderList(headerList);
        long i = 1;
        for (AttendanceEmployeeDate x : modelList) {
            List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getName(), x.isAbsentYes() ? "x" : "", x.isAbsentNo() ? "x" : "", x.isGoSchool() ? "x" : "", x.isMorningYes() ? "x" : "", x.isMorningNo() ? "x" : "", x.isMorning() ? "x" : "",
                    x.isAfternoonYes() ? "x" : "", x.isAfternoonNo() ? "x" : "", x.isAfternoon() ? "x" : "", x.isEveningYes() ? "x" : "", x.isEveningNo() ? "x" : "", x.isEvening() ? "x" : "", x.isBreakfast() ? "x" : "", x.isLunch() ? "x" : "", x.isDinner() ? "x" : "",
                    x.getArriveTime() == null ? "" : ConvertData.convertTimeHHMM(x.getArriveTime()), x.getLeaveTime() == null ? "" : ConvertData.convertTimeHHMM(x.getLeaveTime()), String.valueOf(x.getMinuteArriveLate()), String.valueOf(x.getMinuteLeaveSoon()));
            ExcelDataNew modelData = ExportExcelUtils.setBodyExcelNew(bodyStringList);
            bodyList.add(modelData);
        }
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    @Override
    public ByteArrayInputStream exportAttendanceEmployeeMonth(UserPrincipal principal, List<AttendanceEmployeeMonth> modelList, LocalDate date) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        XSSFColor blueOne = new XSSFColor(new java.awt.Color(131, 164, 196));
        XSSFColor greenOne = new XSSFColor(new java.awt.Color(149, 203, 74));
        XSSFColor yellowTwo = new XSSFColor(new java.awt.Color(217, 210, 144));
        XSSFColor blueTwo = new XSSFColor(new java.awt.Color(51, 153, 255));
        XSSFColor yellowThree = new XSSFColor(new java.awt.Color(224, 191, 28));

        int[] widths = {5, 30, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 10, 10, 10, 10};
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateToStrSheet = df1.format(date);
        String schoolName = principal.getSchool().getSchoolName();

        String[] columns = {"STT", "Thời gian", "Nghỉ có phép", "Nghỉ không phép", "Đi làm", "Có phép", "Không phép", "Đi làm", "Có phép", "Không phép", "Đi làm", "Có phép", "Không phép", "Đi làm", "Sáng", "Trưa", "Tối", "Đến", "Về", "Phút", "Phút"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            modelList.forEach(x -> {
//            CreationHelper createHelper = workbook.getCreationHelper();
                Sheet sheet = workbook.createSheet(x.getName());

                sheet.setDisplayGridlines(false);
                for (int i = 0; i < 3; i++) {
                    Row headerRow = sheet.createRow(i);
                    for (int col = 0; col < columns.length; col++) {
                        Cell cell = headerRow.createCell(col);
                        if (col == 0 && i == 0) {
                            cell.setCellValue("BẢNG KÊ CHẤM CÔNG THÁNG " + date.getMonthValue());
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
                for (int col = 0; col < 21; col++) {
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
                cellAttendance.setCellValue("CHẤM CÔNG NHÂN VIÊN");
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
                cellStatus.setCellValue("TRẠNG THÁI ĐI LÀM");
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
                cellLesson.setCellValue("BUỔI LÀM");


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
                sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 14, 16));

                Cell cellShuttle = headerRow4.createCell(17);
                cellShuttle.setCellValue("GIỜ ĐẾN - GIỜ VỀ");
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
                sheet.addMergedRegion(new CellRangeAddress(rowHeader, rowHeader, 17, 18));

                Cell cellLate = headerRow4.createCell(19);
                cellLate.setCellValue("ĐI MUỘN");
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

                Cell cellSoon = headerRow4.createCell(20);
                cellSoon.setCellValue("VỀ SỚM");
                Font headerSoon = workbook.createFont();
                headerSoon.setBold(true);
                cellSoon.setCellStyle(cellLateStyle);


                int rowParent = 5;
                Row headerRow5 = sheet.createRow(rowParent);
                // Header 4
                for (int col = 0; col < 21; col++) {
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
                cellLesson4.setCellValue("Đi làm");
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

                Cell cellLesson11 = headerRow5.createCell(15);
                cellLesson11.setCellValue("Trưa");
                cellLesson11.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle10 = workbook.createCellStyle();
                cellLateStyle10.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 15, 15));

                Cell cellLesson14 = headerRow5.createCell(16);
                cellLesson14.setCellValue("Tối");
                cellLesson14.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle13 = workbook.createCellStyle();
                cellLateStyle13.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 16, 16));

                Cell cellLesson15 = headerRow5.createCell(17);
                cellLesson15.setCellValue("Đến");
                cellLesson15.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle14 = workbook.createCellStyle();
                cellLateStyle14.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 17, 17));

                Cell cellLesson16 = headerRow5.createCell(18);
                cellLesson16.setCellValue("Về");
                cellLesson16.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle15 = workbook.createCellStyle();
                cellLateStyle15.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 18, 18));

                Cell cellLesson17 = headerRow5.createCell(19);
                cellLesson17.setCellValue("Phút");
                cellLesson17.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle16 = workbook.createCellStyle();
                cellLateStyle16.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 19, 19));

                Cell cellLesson18 = headerRow5.createCell(20);
                cellLesson18.setCellValue("Phút");
                cellLesson18.setCellStyle(headerKidsCellStyle);
                CellStyle cellLateStyle17 = workbook.createCellStyle();
                cellLateStyle17.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 20, 20));


                Row headerRow6 = sheet.createRow(rowChild);

// Header
                for (int col = 0; col < 21; col++) {

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
                int index = 0;
                for (AttendanceEmployeeDetailMonth model : x.getAttendanceEmployeeDateList()) {
                    index++;
                    Row row = sheet.createRow(rowIdx++);

                    Cell cellId = row.createCell(0);
                    cellId.setCellValue(index);
                    cellId.setCellStyle(contentCellStyle);

                    Cell cellKidName = row.createCell(1);
                    cellKidName.setCellValue(df.format(model.getDate()));
                    cellKidName.setCellStyle(contentCellStyle);

                    Cell cellAbsentLetterYes = row.createCell(2);
                    cellAbsentLetterYes.setCellValue(checkAttendanceExcel(model).equals("2") ? "x" : "");
                    cellAbsentLetterYes.setCellStyle(contentStatusStyle);

                    Cell cellAbsentLetterNo = row.createCell(3);
                    cellAbsentLetterNo.setCellValue(checkAttendanceExcel(model).equals("3") ? "x" : "");
                    cellAbsentLetterNo.setCellStyle(contentStatusStyle);

                    Cell cellAbsentStatus = row.createCell(4);
                    cellAbsentStatus.setCellValue(checkAttendanceExcel(model).equals("1") ? "x" : "");
                    cellAbsentStatus.setCellStyle(contentStatusStyle);

                    Cell cellMorningYes = row.createCell(5);
                    cellMorningYes.setCellValue(model.isMorningYes() ? "x" : "");
                    cellMorningYes.setCellStyle(contentLessonStyle);

                    Cell cellMorningNo = row.createCell(6);
                    cellMorningNo.setCellValue(model.isMorningNo() ? "x" : "");
                    cellMorningNo.setCellStyle(contentLessonStyle);

                    Cell cellMorning = row.createCell(7);
                    cellMorning.setCellValue(model.isMorning() ? "x" : "");
                    cellMorning.setCellStyle(contentLessonStyle);

                    Cell cellAfternoonYes = row.createCell(8);
                    cellAfternoonYes.setCellValue(model.isAfternoonYes() ? "x" : "");
                    cellAfternoonYes.setCellStyle(contentLessonStyle);


                    Cell cellAfternoonNo = row.createCell(9);
                    cellAfternoonNo.setCellValue(model.isAfternoonNo() ? "x" : "");
                    cellAfternoonNo.setCellStyle(contentLessonStyle);

                    Cell cellAfternoon = row.createCell(10);
                    cellAfternoon.setCellValue(model.isAfternoon() ? "x" : "");
                    cellAfternoon.setCellStyle(contentLessonStyle);

                    Cell cellEveningYes = row.createCell(11);
                    cellEveningYes.setCellValue(model.isEveningYes() ? "x" : "");
                    cellEveningYes.setCellStyle(contentLessonStyle);

                    Cell cellEveningNo = row.createCell(12);
                    cellEveningNo.setCellValue(model.isEveningNo() ? "x" : "");
                    cellEveningNo.setCellStyle(contentLessonStyle);

                    Cell cellEvening = row.createCell(13);
                    cellEvening.setCellValue(model.isEvening() ? "x" : "");
                    cellEvening.setCellStyle(contentLessonStyle);

                    Cell cellEatBreakfast = row.createCell(14);
                    cellEatBreakfast.setCellValue(model.isBreakfast() ? "x" : "");
                    cellEatBreakfast.setCellStyle(contentMealStyle);

                    Cell celleatLunch = row.createCell(15);
                    celleatLunch.setCellValue(model.isLunch() ? "x" : "");
                    celleatLunch.setCellStyle(contentMealStyle);

                    Cell celleatDinner = row.createCell(16);
                    celleatDinner.setCellValue(model.isDinner() ? "x" : "");
                    celleatDinner.setCellStyle(contentMealStyle);

                    Cell celltimeArriveKid = row.createCell(17);
                    celltimeArriveKid.setCellValue(model.getArriveTime() == null ? "" : ConvertData.convertTimeHHMM(model.getArriveTime()));
                    celltimeArriveKid.setCellStyle(contentShuttleStyle);

                    Cell celltimeLeaveKid = row.createCell(18);
                    celltimeLeaveKid.setCellValue(model.getLeaveTime() == null ? "" : ConvertData.convertTimeHHMM(model.getLeaveTime()));
                    celltimeLeaveKid.setCellStyle(contentShuttleStyle);

                    Cell cellminutePickupLate = row.createCell(19);
                    cellminutePickupLate.setCellValue(model.getMinuteArriveLate());
                    cellminutePickupLate.setCellStyle(contentLateStyle);

                    Cell cellminutePickupSoon = row.createCell(20);
                    cellminutePickupSoon.setCellValue(model.getMinuteLeaveSoon());
                    cellminutePickupSoon.setCellStyle(contentLateStyle);

                }
            });

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public List<ExcelNewResponse> exportAttendanceEmployeeMonthNew(UserPrincipal principal, List<AttendanceEmployeeMonth> modelList, LocalDate date) {
        List<ExcelNewResponse> responseList = new ArrayList<>();
        for (AttendanceEmployeeMonth attendanceEmployeeMonth : modelList) {
            ExcelNewResponse response = new ExcelNewResponse();
            List<ExcelDataNew> bodyList = new ArrayList<>();
            String schoolName = principal.getSchool().getSchoolName();

            List<String> headerStringList = Arrays.asList("BẢNG KÊ CHẤM CÔNG THÁNG ".concat(String.valueOf(date.getMonthValue())), AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_DATE.concat(ConvertData.convertLocalDateToString(date)), "");
            List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
            ExcelDataNew headerMulti = this.setHeaderMulti();
            ExcelDataNew headerMulti2 = this.setHeaderMulti2();
            headerList.add(headerMulti);
            headerList.add(headerMulti2);
            response.setSheetName(attendanceEmployeeMonth.getName().concat(AppConstant.SPACE_EXPORT_ID).concat(String.valueOf(attendanceEmployeeMonth.getId())));
            response.setHeaderList(headerList);
            long i = 1;
            for (AttendanceEmployeeDetailMonth x : attendanceEmployeeMonth.getAttendanceEmployeeDateList()) {
                List<String> bodyStringList = Arrays.asList(String.valueOf(i++), ConvertData.convertLocalDateToString(x.getDate()), x.isAbsentYes() ? "x" : "", x.isAbsentNo() ? "x" : "", x.isGoSchool() ? "x" : "", x.isMorningYes() ? "x" : "", x.isMorningNo() ? "x" : "", x.isMorning() ? "x" : "",
                        x.isAfternoonYes() ? "x" : "", x.isAfternoonNo() ? "x" : "", x.isAfternoon() ? "x" : "", x.isEveningYes() ? "x" : "", x.isEveningNo() ? "x" : "", x.isEvening() ? "x" : "", x.isBreakfast() ? "x" : "", x.isLunch() ? "x" : "", x.isDinner() ? "x" : "",
                        x.getArriveTime() == null ? "" : ConvertData.convertTimeHHMM(x.getArriveTime()), x.getLeaveTime() == null ? "" : ConvertData.convertTimeHHMM(x.getLeaveTime()), String.valueOf(x.getMinuteArriveLate()), String.valueOf(x.getMinuteLeaveSoon()));
                ExcelDataNew modelData = ExportExcelUtils.setBodyExcelNew(bodyStringList);
                bodyList.add(modelData);
            }
            response.setBodyList(bodyList);
            responseList.add(response);
        }
        return responseList;
    }

    @Transactional
    @Override
    public void importExcelNewEmployee(UserPrincipal principal, CreateEmployeeExcelRequest request) {
        for (ExcelDataNew excelDataNew : request.getBodyList()) {
            CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest();
            List<TabDepartmentRequest> tabDepartmentRequestList = new ArrayList<>();
            List<TabProfessionalRequest> tabProfessionalRequests = new ArrayList<>();
            List<AppIconTeacherRequest> appIconTeacherRequests = new ArrayList<>();
            CreateEmployeeMainInfoRequest model = new CreateEmployeeMainInfoRequest();
            model.setIdSchool(principal.getIdSchoolLogin());
            this.setEmployeeInfo(model, excelDataNew);
            //Chuyên môn
            if (!excelDataNew.getPro17().isEmpty()) {
                String classEmployee = excelDataNew.getPro17().replace("\n", ",");
                List<String> myList = new ArrayList<>(Arrays.asList(classEmployee.split(",")));
                myList.forEach(x -> {
                    Optional<MaClass> maClass = maClassRepository.findByClassNameAndDelActiveTrueAndIdSchool(x.trim(), principal.getIdSchoolLogin());
                    if (maClass.isPresent()) {
                        TabProfessionalRequest tabProfessionalRequest = new TabProfessionalRequest();
                        tabProfessionalRequest.setId(maClass.get().getId());
                        tabProfessionalRequests.add(tabProfessionalRequest);
                    }
                });
            }
            //Phòng ban
            if (!excelDataNew.getPro18().isEmpty()) {
                String departmentEmployee = excelDataNew.getPro18().replace("\n", ",");
                List<String> myList = new ArrayList<>(Arrays.asList(departmentEmployee.split(",")));
                myList.forEach(x -> {
                    Optional<Department> department = departmentRepository.findByDepartmentNameAndDelActiveTrueAndSchool_Id(x.trim(), principal.getIdSchoolLogin());
                    if (department.isPresent()) {
                        TabDepartmentRequest tabDepartmentRequest = new TabDepartmentRequest();
                        tabDepartmentRequest.setId(department.get().getId());
                        tabDepartmentRequestList.add(tabDepartmentRequest);
                    }
                });
            }
            //Đối tượng
            if (!excelDataNew.getPro19().isEmpty()) {
                String objEmployee = excelDataNew.getPro19().replace("\n", ",");
                List<String> myList = new ArrayList<>(Arrays.asList(objEmployee.split(",")));
                List<AccountType> accountTypeList = new ArrayList<>();
                myList.forEach(x -> {
                    AccountType accountType = accountTypeRepository.findAllByDelActiveTrueAndNameAndAndIdSchool(x.trim(), principal.getIdSchoolLogin());
                    if (accountType != null) {
                        accountTypeList.add(accountType);
                    }
                });
                List<Long> idType = accountTypeList.stream().map(BaseEntity::getId).collect(Collectors.toList());
                model.setIdAccountTypeList(idType);
            }
            ListAppIconTeacherResponse listAppIconTeacherResponse = appIconTeacherAddSerivce.findAppIconTeacherAddCreate(principal.getIdSchoolLogin());
            appIconTeacherRequests = listMapper.mapList(listAppIconTeacherResponse.getAppIconTeacherResponseList(), AppIconTeacherRequest.class);
            createEmployeeRequest.setAppIconTeacherRequestList(appIconTeacherRequests);
            createEmployeeRequest.setCreateEmployeeMainInfoRequest(model);
            createEmployeeRequest.setTabDepartmentRequestList(tabDepartmentRequestList);
            createEmployeeRequest.setTabProfessionalRequestList(tabProfessionalRequests);
            employeeService.createEmployee(createEmployeeRequest, principal);
        }
    }

    private void setEmployeeInfo(CreateEmployeeMainInfoRequest model, ExcelDataNew excelDataNew) {
        model.setEmployeeStatus(EmployeeConstant.STATUS_WORKING);
        model.setAppType(AppTypeConstant.TEACHER);
        model.setFullName(excelDataNew.getPro2());
        model.setBirthday(ConvertData.convertStringToDate(excelDataNew.getPro3()));
        model.setGender(excelDataNew.getPro4());
        model.setPhone(excelDataNew.getPro5());
        model.setStartDate(ConvertData.convertStringToDate(excelDataNew.getPro6()));
        model.setContractDate(StringUtils.isNotBlank(excelDataNew.getPro7()) ? ConvertData.convertStringToDate(excelDataNew.getPro7()) : null);
        model.setEndDate(StringUtils.isNotBlank(excelDataNew.getPro8()) ? ConvertData.convertStringToDate(excelDataNew.getPro8()) : null);
        model.setEmail(excelDataNew.getPro9());
        model.setCmnd(excelDataNew.getPro10());
        model.setCmndDate(StringUtils.isNotBlank(excelDataNew.getPro11()) ? ConvertData.convertStringToDate(excelDataNew.getPro11()) : null);
        model.setPermanentAddress(excelDataNew.getPro12());
        model.setAddress(excelDataNew.getPro13());
        model.setNote(excelDataNew.getPro20());
        if (AppConstant.EMPLOYEE_STATUS_MARRIED.equalsIgnoreCase(excelDataNew.getPro14()) || AppConstant.EMPLOYEE_STATUS_SINGLE.equalsIgnoreCase(excelDataNew.getPro14())) {
            model.setMarriedStatus(excelDataNew.getPro14());
        }
        model.setNumberChildren(StringUtils.isNotBlank(excelDataNew.getPro15()) ? Integer.valueOf(excelDataNew.getPro15()) : null);
        model.setEducationLevel(excelDataNew.getPro16());
    }

    private String checkAttendanceExcel(AttendanceEmployeeDetailMonth attendanceEmployee) {
        if (attendanceEmployee.isAfternoon() || attendanceEmployee.isMorning() || attendanceEmployee.isEvening()) {
            return "1";
        } else if (attendanceEmployee.isMorningYes() || attendanceEmployee.isAfternoonYes() || attendanceEmployee.isEveningYes()) {
            return "2";
        } else return "3";
    }

    private ExcelDataNew setHeaderMulti() {
        ExcelDataNew headerMulti = new ExcelDataNew();
        headerMulti.setPro1("CHẤM CÔNG NHÂN VIÊN");
        headerMulti.setPro2("");
        headerMulti.setPro3("TRẠNG THÁI ĐI LÀM");
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
        headerMulti.setPro15("BỮA ĂN");
        headerMulti.setPro16("");
        headerMulti.setPro17("");
        headerMulti.setPro18("GIỜ ĐẾN - GIỜ VỀ");
        headerMulti.setPro19("");
        headerMulti.setPro20("ĐI MUỘN");
        headerMulti.setPro21("VỀ SỚM");
        return headerMulti;
    }

    private ExcelDataNew setHeaderMulti1() {
        ExcelDataNew headerMulti = new ExcelDataNew();
        headerMulti.setPro1("STT");
        headerMulti.setPro2("Họ và tên");
        headerMulti.setPro3("Nghỉ có phép");
        headerMulti.setPro4("Nghỉ không phép");
        headerMulti.setPro5("Đi làm");
        headerMulti.setPro6("Sáng");
        headerMulti.setPro7("");
        headerMulti.setPro8("");
        headerMulti.setPro9("Chiều");
        headerMulti.setPro10("");
        headerMulti.setPro11("");
        headerMulti.setPro12("Tối");
        headerMulti.setPro13("");
        headerMulti.setPro14("");
        headerMulti.setPro15("Sáng");
        headerMulti.setPro16("Trưa");
        headerMulti.setPro17("Tối");
        headerMulti.setPro18("Đến");
        headerMulti.setPro19("Về");
        headerMulti.setPro20("Phút");
        headerMulti.setPro21("Phút");
        return headerMulti;
    }

    private ExcelDataNew setHeaderMulti2() {
        ExcelDataNew headerMulti = new ExcelDataNew();
        headerMulti.setPro1("STT");
        headerMulti.setPro2("Thời gian");
        headerMulti.setPro3("Nghỉ có phép");
        headerMulti.setPro4("Nghỉ không phép");
        headerMulti.setPro5("Đi làm");
        headerMulti.setPro6("Sáng");
        headerMulti.setPro7("");
        headerMulti.setPro8("");
        headerMulti.setPro9("Chiều");
        headerMulti.setPro10("");
        headerMulti.setPro11("");
        headerMulti.setPro12("Tối");
        headerMulti.setPro13("");
        headerMulti.setPro14("");
        headerMulti.setPro15("Sáng");
        headerMulti.setPro16("Trưa");
        headerMulti.setPro17("Tối");
        headerMulti.setPro18("Đến");
        headerMulti.setPro19("Về");
        headerMulti.setPro20("Phút");
        headerMulti.setPro21("Phút");
        return headerMulti;
    }
}
