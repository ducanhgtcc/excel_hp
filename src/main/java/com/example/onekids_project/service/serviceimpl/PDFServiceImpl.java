package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.AttendanceEmployee;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalary;
import com.example.onekids_project.entity.finance.employeesalary.FnOrderEmployee;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.WalletParent;
import com.example.onekids_project.entity.school.FnBank;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.school.SchoolInfo;
import com.example.onekids_project.model.pdf.KidsOrderPdf;
import com.example.onekids_project.model.pdf.KidsPackagePdf;
import com.example.onekids_project.model.pdf.OrderPdf;
import com.example.onekids_project.model.pdf.employees.EmployeePackagePdf;
import com.example.onekids_project.model.pdf.employees.EmployeesOrderPdf;
import com.example.onekids_project.model.pdf.employees.OrderDetailPdf;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.employeeSalary.SearchAttendanceSalaryRequest;
import com.example.onekids_project.response.attendanceemployee.AttendanceEmployeesStatisticalResponse;
import com.example.onekids_project.response.attendancekids.AttendanceKidsStatisticalResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.PDFService;
import com.example.onekids_project.service.servicecustom.employeesaraly.AttendanceEmployeeService;
import com.example.onekids_project.util.FinanceUltils;
import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class PDFServiceImpl implements PDFService {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private FnBankRepository fnBankRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private FnOrderKidsRepository fnOrderKidsRepository;

    @Autowired
    private FnKidsPackageRepository fnKidsPackageRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private FnOrderEmployeeRepository fnOrderEmployeeRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private FnEmployeeSalaryRepository fnEmployeeSalaryRepository;

    @Autowired
    private SchoolInfoRepository schoolInfoRepository;

    @Autowired
    private AttendanceEmployeeService attendanceEmployeeService;

    @Override
    public ByteArrayInputStream createKidPDF(@CurrentUser UserPrincipal principal, Long idOrder, List<Long> idList) throws DocumentException, IOException {
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font phieuthu = new Font(CROACIA, 11, Font.BOLD);
        Font stt = new Font(CROACIA, 11, Font.BOLD);
        Font abc = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9);
        Font nguoinop = new Font(CROACIA, 10, Font.BOLD);
        Font nguoilappphieu = new Font(CROACIA, 9, Font.BOLD);
        Font ky = new Font(CROACIA, 9, Font.ITALIC);
        Font tentre1 = new Font(CROACIA, 11, Font.NORMAL, BaseColor.BLACK);
        Font ft = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10);
        // tạo trang giấy kichcs thước a5
        Document document = new Document(PageSize.A5);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, os);
        // căn lề
        document.setMargins(15f, 15f, 10, 0);
        // mở
        document.open();

        Long idSchool = principal.getIdSchoolLogin();
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();

        // in cả 2 phiếu thu và chi
        if (this.getOrderKids(idSchool, idOrder, idList).getInOrder().getDataList().size() != 0 &&
                this.getOrderKids(idSchool, idOrder, idList).getOutOrder().getDataList().size() != 0) {
            // phiếu thu học phí
            Paragraph p4 = new Paragraph(school.getSchoolName(), phieuthu);
            p4.setAlignment(Paragraph.ALIGN_CENTER);
            p4.setSpacingAfter(3f);
            document.add(p4);

            if (school.getSchoolAddress() != null) {
                // địa chỉ
                Paragraph p5 = new Paragraph("Địa chỉ: " + school.getSchoolAddress(), ft);
                p5.setAlignment(Paragraph.ALIGN_CENTER);
                p5.setSpacingAfter(3f);
                document.add(p5);
            } else {
                Paragraph p5 = new Paragraph("Địa chỉ: ", ft);
                p5.setAlignment(Paragraph.ALIGN_CENTER);
                p5.setSpacingAfter(3f);
                document.add(p5);
            }

            // ten phieu thu
            Paragraph p6 = new Paragraph("PHIẾU THU PHÍ THÁNG " + this.getOrderKids(idSchool, idOrder, idList).getMonth() + "/" + this.getOrderKids(idSchool, idOrder, idList).getYear(), phieuthu);
            p6.setAlignment(Paragraph.ALIGN_CENTER);
            p6.setSpacingAfter(3f);
            document.add(p6);

            // tên trẻ
            Chunk glue = new Chunk(new VerticalPositionMark());
            Paragraph p7 = new Paragraph("Trẻ: " + this.getOrderKids(idSchool, idOrder, idList).getKidName(), tentre1);
            p7.add(new Chunk(glue));
            p7.add("Mã:" + "..........");
            document.add(p7);

            // lớp
            Paragraph p8 = new Paragraph("Lớp: " + this.getOrderKids(idSchool, idOrder, idList).getClassName(), tentre1);
            p8.add(new Chunk(glue));
            p8.add("Quyển số:" + "..........");
            document.add(p8);

            //đt
            if (school.getSchoolPhone() != null) {
                Paragraph p9 = new Paragraph("ĐT: " + school.getSchoolPhone(), tentre1);
                p9.add(new Chunk(glue));
                p9.add("Số:" + "..........");
                document.add(p9);
            } else {
                Paragraph p9 = new Paragraph("ĐT: ", tentre1);
                p9.add(new Chunk(glue));
                p9.add("Số:" + "..........");
                document.add(p9);
            }
            // ngày
            Paragraph p10 = new Paragraph("" + "", ky);
            p10.add(new Chunk(glue));
            p10.add("Ngày " + LocalDate.now().getDayOfMonth() + " tháng " + LocalDate.now().getMonthValue() + " năm " + LocalDate.now().getYear());
            document.add(p10);

            //tạo bảng
            PdfPTable pdfPTable = new PdfPTable(7);
            pdfPTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfPTable.setWidths(new int[]{3, 20, 3, 9, 9, 9, 9});
            pdfPTable.setWidthPercentage(100);

            // cách tren margin-top
            pdfPTable.setSpacingBefore(10f);
            PdfPCell c2 = new PdfPCell(new Phrase("TT", stt));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_CENTER);
            c2.setBackgroundColor(new BaseColor(182, 184, 186));
            pdfPTable.addCell(c2);

            PdfPCell c3 = new PdfPCell(new Phrase("Tên loại phí", stt));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c3.setBackgroundColor(new BaseColor(182, 184, 186));
            pdfPTable.addCell(c3);

            PdfPCell c4 = new PdfPCell(new Phrase("SL", stt));
            c4.setBackgroundColor(new BaseColor(182, 184, 186));
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c4);

            PdfPCell c5 = new PdfPCell(new Phrase("Đơn giá", stt));
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c5.setBackgroundColor(new BaseColor(182, 184, 186));
            pdfPTable.addCell(c5);

            PdfPCell c6 = new PdfPCell(new Phrase("Thành tiền", stt));
            c6.setBackgroundColor(new BaseColor(182, 184, 186));
            c6.setHorizontalAlignment(Element.ALIGN_CENTER);
            c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c6);

            PdfPCell c7 = new PdfPCell(new Phrase("Đã thu", stt));
            c7.setBackgroundColor(new BaseColor(182, 184, 186));
            c7.setHorizontalAlignment(Element.ALIGN_CENTER);
            c7.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c7);

            PdfPCell c8 = new PdfPCell(new Phrase("Thiếu", stt));
            c8.setBackgroundColor(new BaseColor(182, 184, 186));
            c8.setHorizontalAlignment(Element.ALIGN_CENTER);
            c8.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c8);
            c8.setBorder(Rectangle.NO_BORDER);

            AtomicInteger index = new AtomicInteger();
            this.getOrderKids(idSchool, idOrder, idList).getInOrder().getDataList().forEach(x -> {
                // stt
                index.getAndIncrement();
                PdfPCell id = new PdfPCell(new Phrase(String.valueOf(index.get()), abc));
                id.setHorizontalAlignment(Element.ALIGN_CENTER);
                id.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(id);

                // ten loại phí
                PdfPCell addCell = new PdfPCell(new Phrase(x.getName(), abc));
                pdfPTable.addCell(addCell);

                //SL
                PdfPCell idth = new PdfPCell(new Phrase(String.valueOf(x.getNumber()), abc));
                idth.setHorizontalAlignment(Element.ALIGN_CENTER);
                idth.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(idth);

                double d = x.getPrice();
                double thanhtien = x.getMoney();
                double dathu = x.getPaid();
                double thieu = x.getRemain();
                DecimalFormat formatter = new DecimalFormat("###,###,###");
                String dongia = formatter.format(d).replace(",", ".");
                String thanhtien1 = formatter.format(thanhtien).replace(",", ".");
                String dathu1 = formatter.format(dathu).replace(",", ".");
                String thieu1 = formatter.format(thieu).replace(",", ".");


                // đơn giá
                PdfPCell thanhtient1 = new PdfPCell(new Phrase(dongia, abc));
                thanhtient1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thanhtient1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(thanhtient1);

                // thành tiền
                PdfPCell thanhtient = new PdfPCell(new Phrase(thanhtien1, abc));
                thanhtient.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thanhtient.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(thanhtient);

                // đã thu
                PdfPCell dathut = new PdfPCell(new Phrase(dathu1, abc));
                dathut.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dathut.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(dathut);

                // thiếu
                PdfPCell thieut = new PdfPCell(new Phrase(thieu1, abc));
                thieut.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thieut.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(thieut);
            });
            double tongcong = this.getOrderKids(idSchool, idOrder, idList).getInOrder().getMoneyTotal();
            double dathu = this.getOrderKids(idSchool, idOrder, idList).getInOrder().getMoneyPaidTotal();
            double thieu = this.getOrderKids(idSchool, idOrder, idList).getInOrder().getMoneyRemain();
            double sodu = this.getOrderKids(idSchool, idOrder, idList).getInOrder().getMoneyWallet();
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            String tongcongt = formatter.format(tongcong).replace(",", ".");
            String dathut = formatter.format(dathu).replace(",", ".");
            String thieut = formatter.format(thieu).replace(",", ".");
            String sodut = formatter.format(sodu).replace(",", ".");

            pdfPTable.addCell(createCellBackGroundGray("Tổng cộng", 1, 4, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCellBackGroundGray(tongcongt, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCellBackGroundGray(dathut, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCellBackGroundGray(thieut, 1, 1, Element.ALIGN_RIGHT));

            pdfPTable.addCell(createCell("Số dư trong ví", 1, 4, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell2(sodut, 1, 3, Element.ALIGN_RIGHT));

//            pdfPTable.addCell(createCell3("Phụ huynh phải thanh toán", 1, 4, Element.ALIGN_RIGHT));
//            pdfPTable.addCell(createCell3(thieut, 1, 3, Element.ALIGN_RIGHT));

            pdfPTable.addCell(createCell("Số tiền đã trả", 1, 4, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell(dathut, 1, 3, Element.ALIGN_RIGHT));

            pdfPTable.addCell(createCell3("Số tiền còn thiếu", 1, 4, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell3(thieut, 1, 3, Element.ALIGN_RIGHT));

            document.add(pdfPTable);


            /// footer
            if (school.getSchoolInfo().getExpired() != null) {
                Paragraph p11 = new Paragraph(school.getSchoolInfo().getExpired(), ky);
                p11.setAlignment(Paragraph.ALIGN_LEFT);
                p11.setSpacingAfter(3f);
                document.add(p11);
            }

            // thông tin ngân hàng chính
            Optional<FnBank> fnBankOptional = fnBankRepository.findBySchoolIdAndCheckedTrueAndDelActiveTrue(idSchool);

            if (fnBankOptional.isPresent()) {
                FnBank fnBank = fnBankOptional.get();
                // Tên ngân hàng
                Paragraph p12 = new Paragraph("Ngân hàng: " + fnBank.getBankName(), nguoinop);
                p12.setAlignment(Paragraph.ALIGN_LEFT);
                p12.setSpacingAfter(3f);
                document.add(p12);
                // so tai khoan
                Paragraph p13 = new Paragraph("Số tài khoản: " + fnBank.getAccountNumber(), nguoinop);
                p13.setAlignment(Paragraph.ALIGN_LEFT);
                p13.setSpacingAfter(3f);
                document.add(p13);

                Paragraph p14 = new Paragraph("Chủ tài khoản: " + fnBank.getFullName(), nguoinop);
                p14.setAlignment(Paragraph.ALIGN_LEFT);
                p14.setSpacingAfter(3f);
                document.add(p14);
            }

            Paragraph p15 = new Paragraph("Người nộp tiền", nguoilappphieu);
            p15.setMultipliedLeading(2);
            p15.setTabSettings(new TabSettings(240f));
            p15.setIndentationLeft(30f);
            p15.add(Chunk.TABBING);
            p15.add(new Chunk("Người lập phiếu", nguoinop));
            p15.setTabSettings(new TabSettings(230f));
            p15.add(Chunk.TABBING);
            document.add(p15);

            Paragraph p16 = new Paragraph(principal.getFullName(), nguoinop);
            p16.setTabSettings(new TabSettings(270f));
            p16.setIndentationLeft(270f);
            p16.setMultipliedLeading(3);
            document.add(p16);

            // tạo phiếu chi
            document.newPage();

            Paragraph pc1 = new Paragraph(school.getSchoolName(), phieuthu);
            pc1.setAlignment(Paragraph.ALIGN_CENTER);
            pc1.setSpacingAfter(3f);
            document.add(pc1);

            // địa chỉ
            Paragraph pc2 = new Paragraph("Địa chỉ: " + school.getSchoolAddress(), ft);
            pc2.setAlignment(Paragraph.ALIGN_CENTER);
            pc2.setSpacingAfter(3f);
            document.add(pc2);

            // ten phieu thu
            Paragraph pc3 = new Paragraph("PHIẾU CHI PHHS THÁNG " + this.getOrderKids(idSchool, idOrder, idList).getMonth() + "/" + this.getOrderKids(idSchool, idOrder, idList).getYear(), phieuthu);
            pc3.setAlignment(Paragraph.ALIGN_CENTER);
            pc3.setSpacingAfter(3f);
            document.add(pc3);
            // tên trẻ
            Chunk glue1 = new Chunk(new VerticalPositionMark());
            Paragraph pc4 = new Paragraph("Trẻ: " + this.getOrderKids(idSchool, idOrder, idList).getKidName(), tentre1);
            pc4.add(new Chunk(glue1));
            pc4.add("Mã:" + "..........");
            document.add(pc4);

            // lớp
            Paragraph pc5 = new Paragraph("Lớp: " + this.getOrderKids(idSchool, idOrder, idList).getClassName(), tentre1);
            pc5.add(new Chunk(glue1));
            pc5.add("Quyển số:" + "..........");
            document.add(pc5);

            //đt
            Paragraph pc6 = new Paragraph("ĐT: " + school.getSchoolPhone(), tentre1);
            pc6.add(new Chunk(glue1));
            pc6.add("Số:" + "..........");
            document.add(pc6);

            // ngày
            Paragraph pc7 = new Paragraph("" + "", ky);
            pc7.add(new Chunk(glue1));
            pc7.add("Ngày " + LocalDate.now().getDayOfMonth() + " tháng " + LocalDate.now().getMonthValue() + " năm " + LocalDate.now().getYear());
            document.add(pc7);

            //tạo bảng
            PdfPTable pdfPTablepc = new PdfPTable(7);
            pdfPTablepc.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfPTablepc.setWidths(new int[]{3, 20, 3, 9, 9, 9, 9});
            pdfPTablepc.setWidthPercentage(100);
            // cách tren margin-top
            pdfPTablepc.setSpacingBefore(10f);
            PdfPCell c1pc = new PdfPCell(new Phrase("TT", stt));
            c1pc.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1pc.setVerticalAlignment(Element.ALIGN_CENTER);
            c1pc.setBackgroundColor(new BaseColor(182, 184, 186));
            pdfPTablepc.addCell(c1pc);

            PdfPCell c2pc = new PdfPCell(new Phrase("Tên loại phí", stt));
            c2pc.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2pc.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c2pc.setBackgroundColor(new BaseColor(182, 184, 186));
            pdfPTablepc.addCell(c2pc);

            PdfPCell c3pc = new PdfPCell(new Phrase("SL", stt));
            c3pc.setBackgroundColor(new BaseColor(182, 184, 186));
            c3pc.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3pc.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTablepc.addCell(c3pc);

            PdfPCell c4pc = new PdfPCell(new Phrase("Đơn giá", stt));
            c4pc.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4pc.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c4pc.setBackgroundColor(new BaseColor(182, 184, 186));
            pdfPTablepc.addCell(c4pc);

            PdfPCell c5pc = new PdfPCell(new Phrase("Thành tiền", stt));
            c5pc.setBackgroundColor(new BaseColor(182, 184, 186));
            c5pc.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5pc.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTablepc.addCell(c5pc);

            PdfPCell c6pc = new PdfPCell(new Phrase("Đã chi", stt));
            c6pc.setBackgroundColor(new BaseColor(182, 184, 186));
            c6pc.setHorizontalAlignment(Element.ALIGN_CENTER);
            c6pc.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTablepc.addCell(c6pc);

            PdfPCell c7pc = new PdfPCell(new Phrase("Thiếu", stt));
            c7pc.setBackgroundColor(new BaseColor(182, 184, 186));
            c7pc.setHorizontalAlignment(Element.ALIGN_CENTER);
            c7pc.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTablepc.addCell(c7pc);
            c7pc.setBorder(Rectangle.NO_BORDER);

            AtomicInteger indexpc = new AtomicInteger();
            this.getOrderKids(idSchool, idOrder, idList).getOutOrder().getDataList().forEach(x -> {
                // stt
                indexpc.getAndIncrement();
                PdfPCell id = new PdfPCell(new Phrase(String.valueOf(indexpc.get()), abc));
                id.setHorizontalAlignment(Element.ALIGN_CENTER);
                id.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTablepc.addCell(id);

                // ten loại phí
                PdfPCell addCell = new PdfPCell(new Phrase(x.getName(), abc));
                pdfPTablepc.addCell(addCell);

                //SL
                PdfPCell idth = new PdfPCell(new Phrase(String.valueOf(x.getNumber()), abc));
                idth.setHorizontalAlignment(Element.ALIGN_CENTER);
                idth.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTablepc.addCell(idth);

                double d = x.getPrice();
                double thanhtien = x.getMoney();
                double pcdathu = x.getPaid();
                double pcthieu = x.getRemain();
                String dongia = formatter.format(d).replace(",", ".");
                String thanhtien1 = formatter.format(thanhtien).replace(",", ".");
                String dathu1 = formatter.format(pcdathu).replace(",", ".");
                String thieu1 = formatter.format(pcthieu).replace(",", ".");

                // đơn giá
                PdfPCell thanhtient1 = new PdfPCell(new Phrase(dongia, abc));
                thanhtient1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thanhtient1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTablepc.addCell(thanhtient1);

                // thành tiền
                PdfPCell thanhtient = new PdfPCell(new Phrase(thanhtien1, abc));
                thanhtient.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thanhtient.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTablepc.addCell(thanhtient);

                // đã thu
                PdfPCell dathupc = new PdfPCell(new Phrase(dathu1, abc));
                dathupc.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dathupc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTablepc.addCell(dathupc);

                // thiếu
                PdfPCell thieupc = new PdfPCell(new Phrase(thieu1, abc));
                thieupc.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thieupc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTablepc.addCell(thieupc);
            });
            double tongcongc = this.getOrderKids(idSchool, idOrder, idList).getOutOrder().getMoneyTotal();
            double dathuc = this.getOrderKids(idSchool, idOrder, idList).getOutOrder().getMoneyPaidTotal();
            double thieuc = this.getOrderKids(idSchool, idOrder, idList).getOutOrder().getMoneyRemain();
            double soduc = this.getOrderKids(idSchool, idOrder, idList).getOutOrder().getMoneyWallet();
            String tongcongtc = formatter.format(tongcongc).replace(",", ".");
            String dathutc = formatter.format(dathuc).replace(",", ".");
            String thieutc = formatter.format(thieuc).replace(",", ".");
            String sodutc = formatter.format(soduc).replace(",", ".");


            pdfPTablepc.addCell(createCellBackGroundGray("Tổng cộng", 1, 4, Element.ALIGN_RIGHT));
            pdfPTablepc.addCell(createCellBackGroundGray(tongcongtc, 1, 1, Element.ALIGN_RIGHT));
            pdfPTablepc.addCell(createCellBackGroundGray(dathutc, 1, 1, Element.ALIGN_RIGHT));
            pdfPTablepc.addCell(createCellBackGroundGray(thieutc, 1, 1, Element.ALIGN_RIGHT));

            pdfPTablepc.addCell(createCell("Số dư trong ví", 1, 4, Element.ALIGN_RIGHT));
            pdfPTablepc.addCell(createCell2(sodutc, 1, 3, Element.ALIGN_RIGHT));

//            pdfPTablepc.addCell(createCell3("Phụ huynh đã nhận", 1, 4, Element.ALIGN_RIGHT));
//            pdfPTablepc.addCell(createCell3(thieutc, 1, 3, Element.ALIGN_RIGHT));

            pdfPTablepc.addCell(createCell("Số tiền đã chi", 1, 4, Element.ALIGN_RIGHT));
            pdfPTablepc.addCell(createCell(dathutc, 1, 3, Element.ALIGN_RIGHT));

            pdfPTablepc.addCell(createCell3("Số tiền còn thiếu", 1, 4, Element.ALIGN_RIGHT));
            pdfPTablepc.addCell(createCell3(thieutc, 1, 3, Element.ALIGN_RIGHT));

            document.add(pdfPTablepc);

            Paragraph pc15 = new Paragraph("Người nhận tiền", nguoinop);
            pc15.setMultipliedLeading(2);
            pc15.setTabSettings(new TabSettings(240f));
            pc15.setIndentationLeft(30f);
            pc15.add(Chunk.TABBING);
            pc15.add(new Chunk("Người lập phiếu", nguoinop));
            pc15.setTabSettings(new TabSettings(230f));
            pc15.add(Chunk.TABBING);
            document.add(pc15);

            p16.setTabSettings(new TabSettings(270f));
            p16.setIndentationLeft(270f);
            p16.setMultipliedLeading(3);
            document.add(p16);

        } else if (this.getOrderKids(idSchool, idOrder, idList).getInOrder().getDataList().size() != 0) {
            // phiếu thu học phí
            Paragraph p4 = new Paragraph(school.getSchoolName(), phieuthu);
            p4.setAlignment(Paragraph.ALIGN_CENTER);
            p4.setSpacingAfter(3f);
            document.add(p4);

            // địa chỉ
            if (school.getSchoolAddress() != null) {
                Paragraph p5 = new Paragraph("Địa chỉ: " + school.getSchoolAddress(), ft);
                p5.setAlignment(Paragraph.ALIGN_CENTER);
                p5.setSpacingAfter(3f);
                document.add(p5);
            } else {
                Paragraph p5 = new Paragraph("Địa chỉ: ", ft);
                p5.setAlignment(Paragraph.ALIGN_CENTER);
                p5.setSpacingAfter(3f);
                document.add(p5);
            }


            // ten phieu thu
            Paragraph p6 = new Paragraph("PHIẾU THU PHÍ THÁNG " + this.getOrderKids(idSchool, idOrder, idList).getMonth() + "/" + this.getOrderKids(idSchool, idOrder, idList).getYear(), phieuthu);
            p6.setAlignment(Paragraph.ALIGN_CENTER);
            p6.setSpacingAfter(3f);
            document.add(p6);
            // tên trẻ
            Chunk glue = new Chunk(new VerticalPositionMark());
            Paragraph p7 = new Paragraph("Trẻ: " + this.getOrderKids(idSchool, idOrder, idList).getKidName(), tentre1);
            p7.add(new Chunk(glue));
            p7.add("Mã:" + "..........");
            document.add(p7);

            // lớp
            Paragraph p8 = new Paragraph("Lớp: " + this.getOrderKids(idSchool, idOrder, idList).getClassName(), tentre1);
            p8.add(new Chunk(glue));
            p8.add("Quyển số:" + "..........");
            document.add(p8);

            //đt\
            if (school.getSchoolPhone() != null) {
                Paragraph p9 = new Paragraph("ĐT: " + school.getSchoolPhone(), tentre1);
                p9.add(new Chunk(glue));
                p9.add("Số:" + "..........");
                document.add(p9);
            } else {
                Paragraph p9 = new Paragraph("ĐT: ", tentre1);
                p9.add(new Chunk(glue));
                p9.add("Số:" + "..........");
                document.add(p9);
            }


            // ngày
            Paragraph p10 = new Paragraph("" + "", ky);
            p10.add(new Chunk(glue));
            p10.add("Ngày " + LocalDate.now().getDayOfMonth() + " tháng " + LocalDate.now().getMonthValue() + " năm " + LocalDate.now().getYear());
            document.add(p10);

            //tạo bảng
            PdfPTable pdfPTable = new PdfPTable(7);
            pdfPTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfPTable.setWidths(new int[]{3, 20, 3, 9, 9, 9, 9});
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setSpacingBefore(10f);
            PdfPCell c2 = new PdfPCell(new Phrase("TT", stt));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_CENTER);
            c2.setBackgroundColor(new BaseColor(182, 184, 186));
            pdfPTable.addCell(c2);

            PdfPCell c3 = new PdfPCell(new Phrase("Tên loại phí", stt));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c3.setBackgroundColor(new BaseColor(182, 184, 186));
            pdfPTable.addCell(c3);

            PdfPCell c4 = new PdfPCell(new Phrase("SL", stt));
            c4.setBackgroundColor(new BaseColor(182, 184, 186));
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c4);

            PdfPCell c5 = new PdfPCell(new Phrase("Đơn giá", stt));
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c5.setBackgroundColor(new BaseColor(182, 184, 186));
            pdfPTable.addCell(c5);

            PdfPCell c6 = new PdfPCell(new Phrase("Thành tiền", stt));
            c6.setBackgroundColor(new BaseColor(182, 184, 186));
            c6.setHorizontalAlignment(Element.ALIGN_CENTER);
            c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c6);

            PdfPCell c7 = new PdfPCell(new Phrase("Đã thu", stt));
            c7.setBackgroundColor(new BaseColor(182, 184, 186));
            c7.setHorizontalAlignment(Element.ALIGN_CENTER);
            c7.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c7);

            PdfPCell c8 = new PdfPCell(new Phrase("Thiếu", stt));
            c8.setBackgroundColor(new BaseColor(182, 184, 186));
            c8.setHorizontalAlignment(Element.ALIGN_CENTER);
            c8.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c8);
            c8.setBorder(Rectangle.NO_BORDER);

            AtomicInteger index = new AtomicInteger();
            this.getOrderKids(idSchool, idOrder, idList).getInOrder().getDataList().forEach(x -> {
                // stt
                index.getAndIncrement();
                PdfPCell id = new PdfPCell(new Phrase(String.valueOf(index.get()), abc));
                id.setHorizontalAlignment(Element.ALIGN_CENTER);
                id.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(id);

                // ten loại phí
                PdfPCell addCell = new PdfPCell(new Phrase(x.getName(), abc));
                pdfPTable.addCell(addCell);

                //SL
                PdfPCell idth = new PdfPCell(new Phrase(String.valueOf(x.getNumber()), abc));
                idth.setHorizontalAlignment(Element.ALIGN_CENTER);
                idth.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(idth);

                double d = x.getPrice();
                double thanhtien = x.getMoney();
                double dathu = x.getPaid();
                double thieu = x.getRemain();
                DecimalFormat formatter = new DecimalFormat("###,###,###");
                String dongia = formatter.format(d).replace(",", ".");
                String thanhtien1 = formatter.format(thanhtien).replace(",", ".");
                String dathu1 = formatter.format(dathu).replace(",", ".");
                String thieu1 = formatter.format(thieu).replace(",", ".");

                // đơn giá
                PdfPCell thanhtient1 = new PdfPCell(new Phrase(dongia, abc));
                thanhtient1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thanhtient1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(thanhtient1);

                // thành tiền
                PdfPCell thanhtient = new PdfPCell(new Phrase(thanhtien1, abc));
                thanhtient.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thanhtient.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(thanhtient);

                // đã thu
                PdfPCell dathut = new PdfPCell(new Phrase(dathu1, abc));
                dathut.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dathut.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(dathut);

                // thiếu
                PdfPCell thieut = new PdfPCell(new Phrase(thieu1, abc));
                thieut.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thieut.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(thieut);
            });
            double tongcong = this.getOrderKids(idSchool, idOrder, idList).getInOrder().getMoneyTotal();
            double dathu = this.getOrderKids(idSchool, idOrder, idList).getInOrder().getMoneyPaidTotal();
            double thieu = this.getOrderKids(idSchool, idOrder, idList).getInOrder().getMoneyRemain();
            double sodu = this.getOrderKids(idSchool, idOrder, idList).getInOrder().getMoneyWallet();
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            String tongcongt = formatter.format(tongcong).replace(",", ".");
            String dathut = formatter.format(dathu).replace(",", ".");
            String thieut = formatter.format(thieu).replace(",", ".");
            String sodut = formatter.format(sodu).replace(",", ".");
            pdfPTable.addCell(createCellBackGroundGray("Tổng cộng", 1, 4, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCellBackGroundGray(tongcongt, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCellBackGroundGray(dathut, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCellBackGroundGray(thieut, 1, 1, Element.ALIGN_RIGHT));

            pdfPTable.addCell(createCell("Số dư trong ví", 1, 4, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell2(sodut, 1, 3, Element.ALIGN_RIGHT));

//            pdfPTable.addCell(createCell3("Phụ huynh phải thanh toán", 1, 4, Element.ALIGN_RIGHT));
//            pdfPTable.addCell(createCell3(thieut, 1, 3, Element.ALIGN_RIGHT));

            pdfPTable.addCell(createCell("Số tiền đã trả", 1, 4, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell(dathut, 1, 3, Element.ALIGN_RIGHT));

            pdfPTable.addCell(createCell3("Số tiền còn thiếu", 1, 4, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell3(thieut, 1, 3, Element.ALIGN_RIGHT));

            document.add(pdfPTable);

            /// footer
            if (school.getSchoolInfo().getExpired() != null) {
                Paragraph p11 = new Paragraph(school.getSchoolInfo().getExpired(), ky);
                p11.setAlignment(Paragraph.ALIGN_LEFT);
                p11.setSpacingAfter(3f);
                document.add(p11);
            }
            // thông tin ngân hàng chính
            Optional<FnBank> fnBankOptional = fnBankRepository.findBySchoolIdAndCheckedTrueAndDelActiveTrue(idSchool);

            if (fnBankOptional.isPresent()) {
                FnBank fnBank = fnBankOptional.get();
                // Tên ngân hàng
                Paragraph p12 = new Paragraph("Ngân hàng: " + fnBank.getBankName(), nguoinop);
                p12.setAlignment(Paragraph.ALIGN_LEFT);
                p12.setSpacingAfter(3f);
                document.add(p12);
                // so tai khoan
                Paragraph p13 = new Paragraph("Số tài khoản: " + fnBank.getAccountNumber(), nguoinop);
                p13.setAlignment(Paragraph.ALIGN_LEFT);
                p13.setSpacingAfter(3f);
                document.add(p13);

                Paragraph p14 = new Paragraph("Chủ tài khoản: " + fnBank.getFullName(), nguoinop);
                p14.setAlignment(Paragraph.ALIGN_LEFT);
                p14.setSpacingAfter(3f);
                document.add(p14);
            }

            Paragraph p15 = new Paragraph("Người nộp tiền", nguoilappphieu);
            p15.setMultipliedLeading(2);
            p15.setTabSettings(new TabSettings(240f));
            p15.setIndentationLeft(30f);
            p15.add(Chunk.TABBING);
            p15.add(new Chunk("Người lập phiếu", nguoilappphieu));
            p15.setTabSettings(new TabSettings(230f));
            p15.add(Chunk.TABBING);
            document.add(p15);

            Paragraph p16 = new Paragraph(principal.getFullName(), nguoinop);
            p16.setTabSettings(new TabSettings(270f));
            p16.setIndentationLeft(270f);
            p16.setMultipliedLeading(3);
            document.add(p16);
        } else if (this.getOrderKids(idSchool, idOrder, idList).getOutOrder().getDataList().size() != 0) {
            // phiếu CHI HỌC PHÍ
            // thông tin trường
            Paragraph p4 = new Paragraph(school.getSchoolName(), phieuthu);
            p4.setAlignment(Paragraph.ALIGN_CENTER);
            p4.setSpacingAfter(3f);
            document.add(p4);

            // địa chỉ
            Paragraph p5 = new Paragraph("Địa chỉ: " + school.getSchoolAddress(), ft);
            p5.setAlignment(Paragraph.ALIGN_CENTER);
            p5.setSpacingAfter(3f);
            document.add(p5);

            // ten phieu thu
            Paragraph p6 = new Paragraph("PHIẾU CHI PHHS THÁNG " + this.getOrderKids(idSchool, idOrder, idList).getMonth() + "/" + this.getOrderKids(idSchool, idOrder, idList).getYear(), phieuthu);
            p6.setAlignment(Paragraph.ALIGN_CENTER);
            p6.setSpacingAfter(3f);
            document.add(p6);
            // tên trẻ
            Chunk glue = new Chunk(new VerticalPositionMark());
            Paragraph p7 = new Paragraph("Trẻ: " + this.getOrderKids(idSchool, idOrder, idList).getKidName(), tentre1);
            p7.add(new Chunk(glue));
            p7.add("Mã:" + "..........");
            document.add(p7);

            // lớp
            Paragraph p8 = new Paragraph("Lớp: " + this.getOrderKids(idSchool, idOrder, idList).getClassName(), tentre1);
            p8.add(new Chunk(glue));
            p8.add("Quyển số:" + "..........");
            document.add(p8);

            //đt
            Paragraph p9 = new Paragraph("ĐT: " + school.getSchoolPhone(), tentre1);
            p9.add(new Chunk(glue));
            p9.add("Số:" + "..........");
            document.add(p9);

            // ngày
            Paragraph p10 = new Paragraph("" + "", ky);
            p10.add(new Chunk(glue));
            p10.add("Ngày " + LocalDate.now().getDayOfMonth() + " tháng " + LocalDate.now().getMonthValue() + " năm " + LocalDate.now().getYear());
            document.add(p10);

            //tạo bảng
            PdfPTable pdfPTable = new PdfPTable(7);
            pdfPTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfPTable.setWidths(new int[]{3, 20, 3, 9, 9, 9, 9});
            pdfPTable.setWidthPercentage(100);
            // cách tren margin-top
            pdfPTable.setSpacingBefore(10f);
            PdfPCell c2 = new PdfPCell(new Phrase("TT", stt));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_CENTER);
            c2.setBackgroundColor(new BaseColor(182, 184, 186));
            pdfPTable.addCell(c2);

            PdfPCell c3 = new PdfPCell(new Phrase("Tên loại phí", stt));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c3.setBackgroundColor(new BaseColor(182, 184, 186));
            pdfPTable.addCell(c3);

            PdfPCell c4 = new PdfPCell(new Phrase("SL", stt));
            c4.setBackgroundColor(new BaseColor(182, 184, 186));
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c4);

            PdfPCell c5 = new PdfPCell(new Phrase("Đơn giá", stt));
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c5.setBackgroundColor(new BaseColor(182, 184, 186));
            pdfPTable.addCell(c5);

            PdfPCell c6 = new PdfPCell(new Phrase("Thành tiền", stt));
            c6.setBackgroundColor(new BaseColor(182, 184, 186));
            c6.setHorizontalAlignment(Element.ALIGN_CENTER);
            c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c6);

            PdfPCell c7 = new PdfPCell(new Phrase("Đã chi", stt));
            c7.setBackgroundColor(new BaseColor(182, 184, 186));
            c7.setHorizontalAlignment(Element.ALIGN_CENTER);
            c7.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c7);

            PdfPCell c8 = new PdfPCell(new Phrase("Thiếu", stt));
            c8.setBackgroundColor(new BaseColor(182, 184, 186));
            c8.setHorizontalAlignment(Element.ALIGN_CENTER);
            c8.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c8);
            c8.setBorder(Rectangle.NO_BORDER);

            AtomicInteger index = new AtomicInteger();
            this.getOrderKids(idSchool, idOrder, idList).getOutOrder().getDataList().forEach(x -> {
                // stt
                index.getAndIncrement();
                PdfPCell id = new PdfPCell(new Phrase(String.valueOf(index.get()), abc));
                id.setHorizontalAlignment(Element.ALIGN_CENTER);
                id.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(id);

                // ten loại phí
                PdfPCell addCell = new PdfPCell(new Phrase(x.getName(), abc));
                pdfPTable.addCell(addCell);

                //SL
                PdfPCell idth = new PdfPCell(new Phrase(String.valueOf(x.getNumber()), abc));
                idth.setHorizontalAlignment(Element.ALIGN_CENTER);
                idth.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(idth);

                double d = x.getPrice();
                double thanhtien = x.getMoney();
                double dathu = x.getPaid();
                double thieu = x.getRemain();
                DecimalFormat formatter = new DecimalFormat("###,###,###");
                String dongia = formatter.format(d).replace(",", ".");
                String thanhtien1 = formatter.format(thanhtien).replace(",", ".");
                String dathu1 = formatter.format(dathu).replace(",", ".");
                String thieu1 = formatter.format(thieu).replace(",", ".");

                // đơn giá
                PdfPCell thanhtient1 = new PdfPCell(new Phrase(dongia, abc));
                thanhtient1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thanhtient1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(thanhtient1);

                // thành tiền
                PdfPCell thanhtient = new PdfPCell(new Phrase(thanhtien1, abc));
                thanhtient.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thanhtient.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(thanhtient);

                // đã thu
                PdfPCell dathut = new PdfPCell(new Phrase(dathu1, abc));
                dathut.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dathut.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(dathut);

                // thiếu
                PdfPCell thieut = new PdfPCell(new Phrase(thieu1, abc));
                thieut.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thieut.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(thieut);
            });
            double tongcong = this.getOrderKids(idSchool, idOrder, idList).getOutOrder().getMoneyTotal();
            double dathu = this.getOrderKids(idSchool, idOrder, idList).getOutOrder().getMoneyPaidTotal();
            double thieu = this.getOrderKids(idSchool, idOrder, idList).getOutOrder().getMoneyPaidTotal();
            double sodu = this.getOrderKids(idSchool, idOrder, idList).getOutOrder().getMoneyWallet();
            double conthieu = this.getOrderKids(idSchool, idOrder, idList).getOutOrder().getMoneyRemain();
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            String tongcongt = formatter.format(tongcong).replace(",", ".");
            String dathut = formatter.format(dathu).replace(",", ".");
            String thieut = formatter.format(thieu).replace(",", ".");
            String sodut = formatter.format(sodu).replace(",", ".");
            String conthieut = formatter.format(conthieu).replace(",", ".");
            pdfPTable.addCell(createCellBackGroundGray("Tổng cộng", 1, 4, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCellBackGroundGray(tongcongt, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCellBackGroundGray(dathut, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCellBackGroundGray(conthieut, 1, 1, Element.ALIGN_RIGHT));

            pdfPTable.addCell(createCell("Số dư trong ví", 1, 4, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell2(sodut, 1, 3, Element.ALIGN_RIGHT));

//            pdfPTable.addCell(createCell3("Phụ huynh đã nhận", 1, 4, Element.ALIGN_RIGHT));
//            pdfPTable.addCell(createCell3(thieut, 1, 3, Element.ALIGN_RIGHT));

            pdfPTable.addCell(createCell("Số tiền đã chi", 1, 4, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell(dathut, 1, 3, Element.ALIGN_RIGHT));

            pdfPTable.addCell(createCell3("Số tiền còn thiếu", 1, 4, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell3(conthieut, 1, 3, Element.ALIGN_RIGHT));

            document.add(pdfPTable);

            Paragraph p15 = new Paragraph("Người nhận tiền", nguoinop);
            p15.setMultipliedLeading(2);
            p15.setTabSettings(new TabSettings(240f));
            p15.setIndentationLeft(30f);
            p15.add(Chunk.TABBING);
            p15.add(new Chunk("Người lập phiếu", nguoinop));
            p15.setTabSettings(new TabSettings(230f));
            p15.add(Chunk.TABBING);
            document.add(p15);

            Paragraph p16 = new Paragraph(principal.getFullName(), nguoinop);
            p16.setTabSettings(new TabSettings(270f));
            p16.setIndentationLeft(270f);
            p16.setMultipliedLeading(3);
            document.add(p16);
        }

        document.close();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductPdfReport.pdf");
        return is;
    }

    @Override
    public ByteArrayInputStream createSalaryPDF(UserPrincipal principal, Long idOrder, List<Long> idList) throws IOException, DocumentException {
        Long idSchool = principal.getIdSchoolLogin();
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font phieuthu = new Font(CROACIA, 11, Font.BOLD);
        Font stt = new Font(CROACIA, 11, Font.BOLD);
        Font abc = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9);
        Font nguoinop = new Font(CROACIA, 10, Font.BOLD);
        Font ky = new Font(CROACIA, 9, Font.ITALIC);
        Font tentre1 = new Font(CROACIA, 11, Font.NORMAL, BaseColor.BLACK);
        Font ft = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9);
        //Create Document instance.
        float[] columnWidths = {1, 5, 5};
        Document document = new Document(PageSize.A5);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, os);
        document.setMargins(15f, 15f, 10, 0);
        //Open the document.
        document.open();

        if (this.getOrderEmployees(idSchool, idOrder, idList).getInOrder().getDataList().size() != 0 &&
                this.getOrderEmployees(idSchool, idOrder, idList).getOutOrder().getDataList().size() != 0) {
            // thông tin trường
            School schooltest = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
            Paragraph p4 = new Paragraph(schooltest.getSchoolName(), phieuthu);
            p4.setAlignment(Paragraph.ALIGN_CENTER);
            p4.setSpacingAfter(3f);
            document.add(p4);

            // địa chỉ
            Paragraph p5 = new Paragraph("Địa chỉ: " + schooltest.getSchoolAddress(), ft);
            p5.setAlignment(Paragraph.ALIGN_CENTER);
            p5.setSpacingAfter(3f);
            document.add(p5);

            // ten phieu thu
            Paragraph p6 = new Paragraph("PHIẾU CHI LƯƠNG THÁNG " + this.getOrderEmployees(idSchool, idOrder, idList).getMonth() + "/" + this.getOrderEmployees(idSchool, idOrder, idList).getYear(), phieuthu);
            p6.setAlignment(Paragraph.ALIGN_CENTER);
            p6.setSpacingAfter(3f);
            document.add(p6);
            // tên trẻ
            Chunk glue = new Chunk(new VerticalPositionMark());
            Paragraph p7 = new Paragraph("NV: " + this.getOrderEmployees(idSchool, idOrder, idList).getEmployeeName(), tentre1);
            p7.add(new Chunk(glue));
            p7.add("Mã:" + "..........");
            document.add(p7);

            // lớp
            Paragraph p8 = new Paragraph("ĐT: " + this.getOrderEmployees(idSchool, idOrder, idList).getPhone(), tentre1);
            p8.add(new Chunk(glue));
            p8.add("Quyển số:" + "..........");
            document.add(p8);

            //đt
            Paragraph p9 = new Paragraph(" ", tentre1);
            p9.add(new Chunk(glue));
            p9.add("Số:" + "..........");
            document.add(p9);

            // ngày
            Paragraph p10 = new Paragraph("" + "", ky);
            p10.add(new Chunk(glue));
            p10.add("Ngày " + LocalDate.now().getDayOfMonth() + " tháng " + LocalDate.now().getMonthValue() + " năm " + LocalDate.now().getYear());
            document.add(p10);

            //Ctạo bảng
            PdfPTable pdfPTable = new PdfPTable(8);
            pdfPTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfPTable.setWidths(new int[]{3, 16, 5, 3, 9, 9, 9, 9});
            pdfPTable.setWidthPercentage(100);
            // cách tren
            pdfPTable.setSpacingBefore(10f);
            PdfPCell c2 = new PdfPCell(new Phrase("TT", stt));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_CENTER);
            c2.setBackgroundColor(BaseColor.GRAY);
            c2.setFixedHeight(15f);
            pdfPTable.addCell(c2);

            PdfPCell c3 = new PdfPCell(new Phrase("Tên loại phí", stt));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c3.setBackgroundColor(BaseColor.GRAY);
            pdfPTable.addCell(c3);

            PdfPCell c31 = new PdfPCell(new Phrase("Đơn vị", stt));
            c31.setBackgroundColor(BaseColor.GRAY);
            c31.setHorizontalAlignment(Element.ALIGN_CENTER);
            c31.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c31);

            PdfPCell c4 = new PdfPCell(new Phrase("SL", stt));
            c4.setBackgroundColor(BaseColor.GRAY);
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c4);

            PdfPCell c5 = new PdfPCell(new Phrase("Đơn giá", stt));
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c5.setBackgroundColor(BaseColor.GRAY);
            pdfPTable.addCell(c5);

            PdfPCell c6 = new PdfPCell(new Phrase("Thành tiền", stt));
            c6.setBackgroundColor(BaseColor.GRAY);
            c6.setHorizontalAlignment(Element.ALIGN_CENTER);
            c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c6);

            PdfPCell c7 = new PdfPCell(new Phrase("Đã thanh toán", stt));
            c7.setBackgroundColor(BaseColor.GRAY);
            c7.setHorizontalAlignment(Element.ALIGN_CENTER);
            c7.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c7);

            PdfPCell c8 = new PdfPCell(new Phrase("Thiếu", stt));
            c8.setBackgroundColor(BaseColor.GRAY);
            c8.setHorizontalAlignment(Element.ALIGN_CENTER);
            c8.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c8);
            c8.setBorder(Rectangle.NO_BORDER);

            AtomicInteger index = new AtomicInteger();

            this.getOrderEmployees(idSchool, idOrder, idList).getOutOrder().getDataList().forEach(x -> {
                index.getAndIncrement();
                PdfPCell id = new PdfPCell(new Phrase(String.valueOf(index.get()), abc));
                id.setHorizontalAlignment(Element.ALIGN_CENTER);
                id.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(id);

                PdfPCell addCell = new PdfPCell(new Phrase(x.getName(), abc));
                pdfPTable.addCell(addCell);

                // đơn vị
                PdfPCell dv = new PdfPCell(new Phrase(x.getUnit(), abc));
                dv.setHorizontalAlignment(Element.ALIGN_CENTER);
                dv.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(dv);

                double d = x.getPrice();
                double thanhtien = x.getMoney();
                double dathu = x.getPaid();
                double thieu = x.getRemain();
                DecimalFormat formatter = new DecimalFormat("###,###,###");
                String dongia = formatter.format(d).replace(",", ".");
                String thanhtien1 = formatter.format(thanhtien).replace(",", ".");
                String dathu1 = formatter.format(dathu).replace(",", ".");
                String thieu1 = formatter.format(thieu).replace(",", ".");


                // số lượng
                PdfPCell idth = new PdfPCell(new Phrase(String.valueOf(x.getNumber()), abc));
                idth.setHorizontalAlignment(Element.ALIGN_CENTER);
                idth.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(idth);

                //đơn giá
                PdfPCell dongiafn = new PdfPCell(new Phrase(dongia, abc));
                dongiafn.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dongiafn.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // thành tiền
                PdfPCell thanhtienfn = new PdfPCell(new Phrase(thanhtien1, abc));
                thanhtienfn.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thanhtienfn.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // đã thanh toán
                PdfPCell dathanhtoan = new PdfPCell(new Phrase(dathu1, abc));
                dathanhtoan.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dathanhtoan.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // thiếu
                PdfPCell thiefnu = new PdfPCell(new Phrase(thieu1, abc));
                thiefnu.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thiefnu.setVerticalAlignment(Element.ALIGN_MIDDLE);

                pdfPTable.addCell(dongiafn);
                pdfPTable.addCell(thanhtienfn);
                pdfPTable.addCell(dathanhtoan);
                pdfPTable.addCell(thiefnu);
            });

            double tongcong = this.getOrderEmployees(idSchool, idOrder, idList).getOutOrder().getMoneyTotal();
            double dathu = this.getOrderEmployees(idSchool, idOrder, idList).getOutOrder().getMoneyPaidTotal();
            double thieu = this.getOrderEmployees(idSchool, idOrder, idList).getOutOrder().getMoneyRemain();
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            String tongcongt = formatter.format(tongcong).replace(",", ".");
            String dathut = formatter.format(dathu).replace(",", ".");
            String thieut = formatter.format(thieu).replace(",", ".");

            pdfPTable.addCell(createCell("Tổng cộng", 1, 5, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell(tongcongt, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell(dathut, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell(thieut, 1, 1, Element.ALIGN_RIGHT));

            pdfPTable.addCell(createCell("Nhà trường đã thanh toán", 1, 5, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell2(dathut, 1, 3, Element.ALIGN_RIGHT));

            pdfPTable.addCell(createCell3("Số tiền còn thiếu", 1, 5, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell3(thieut, 1, 3, Element.ALIGN_RIGHT));

            //Add content to the document using Table objects.
            document.add(pdfPTable);

            /// footer
            Optional<SchoolInfo> schoolInfoOptional = schoolInfoRepository.findBySchoolId(idSchool);
            Paragraph p11 = new Paragraph(schoolInfoOptional.get().getNote(), ky);
            p11.setAlignment(Paragraph.ALIGN_LEFT);
            p11.setSpacingAfter(3f);
            document.add(p11);


            Paragraph p15 = new Paragraph("Người lĩnh", nguoinop);
            p15.setMultipliedLeading(2);
            p15.setIndentationLeft(30f);
            p15.add(Chunk.TABBING);
            p15.add(new Chunk("Người lập phiếu", nguoinop));
            p15.setTabSettings(new TabSettings(230f));
            p15.add(Chunk.TABBING);
            document.add(p15);


            Paragraph p16 = new Paragraph();
            p16.setTabSettings(new TabSettings(275f));
            p16.add(new Chunk(principal.getFullName(), nguoinop));
            p16.setIndentationLeft(270f);
            p16.setMultipliedLeading(4);
            document.add(p16);


            // tạo phiếu chi
            document.newPage();

            Paragraph p41 = new Paragraph(schooltest.getSchoolName(), phieuthu);
            p41.setAlignment(Paragraph.ALIGN_CENTER);
            p41.setSpacingAfter(3f);
            document.add(p41);

            // địa chỉ
            Paragraph p51 = new Paragraph("Địa chỉ: " + schooltest.getSchoolAddress(), ft);
            p51.setAlignment(Paragraph.ALIGN_CENTER);
            p51.setSpacingAfter(3f);
            document.add(p51);

            // ten phieu thu
            Paragraph p61 = new Paragraph("PHIẾU THU TIỀN NHÂN VIÊN THÁNG " + this.getOrderEmployees(idSchool, idOrder, idList).getMonth() + "/" + this.getOrderEmployees(idSchool, idOrder, idList).getYear(), phieuthu);
            p61.setAlignment(Paragraph.ALIGN_CENTER);
            p61.setSpacingAfter(3f);
            document.add(p61);
            // tên trẻ
            Chunk glue1 = new Chunk(new VerticalPositionMark());
            Paragraph p71 = new Paragraph("NV: " + this.getOrderEmployees(idSchool, idOrder, idList).getEmployeeName(), tentre1);
            p71.add(new Chunk(glue1));
            p71.add("Mã:" + "..........");
            document.add(p71);

            // lớp
            Paragraph p81 = new Paragraph("ĐT: " + this.getOrderEmployees(idSchool, idOrder, idList).getPhone(), tentre1);
            p81.add(new Chunk(glue1));
            p81.add("Quyển số:" + "..........");
            document.add(p81);

            //đt
            Paragraph p91 = new Paragraph(" ", tentre1);
            p91.add(new Chunk(glue1));
            p91.add("Số:" + "..........");
            document.add(p91);

            // ngày
            Paragraph p101 = new Paragraph("" + "", ky);
            p101.add(new Chunk(glue1));
            p101.add("Ngày " + LocalDate.now().getDayOfMonth() + " tháng " + LocalDate.now().getMonthValue() + " năm " + LocalDate.now().getYear());
            document.add(p101);

            //Ctạo bảng
            PdfPTable pdfPTable1 = new PdfPTable(8);
            pdfPTable1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfPTable1.setWidths(new int[]{3, 16, 5, 3, 9, 9, 9, 9});
            pdfPTable1.setWidthPercentage(100);
            // cách tren
            pdfPTable1.setSpacingBefore(10f);
            PdfPCell c21 = new PdfPCell(new Phrase("TT", stt));
            c21.setHorizontalAlignment(Element.ALIGN_CENTER);
            c21.setVerticalAlignment(Element.ALIGN_CENTER);
            c21.setBackgroundColor(BaseColor.GRAY);
            c21.setFixedHeight(15f);
            pdfPTable1.addCell(c21);

            PdfPCell c311 = new PdfPCell(new Phrase("Tên loại phí", stt));
            c311.setHorizontalAlignment(Element.ALIGN_CENTER);
            c311.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c311.setBackgroundColor(BaseColor.GRAY);
            pdfPTable1.addCell(c311);

            PdfPCell c3112 = new PdfPCell(new Phrase("Đơn vị", stt));
            c3112.setBackgroundColor(BaseColor.GRAY);
            c3112.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3112.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable1.addCell(c3112);

            PdfPCell c41 = new PdfPCell(new Phrase("SL", stt));
            c41.setBackgroundColor(BaseColor.GRAY);
            c41.setHorizontalAlignment(Element.ALIGN_CENTER);
            c41.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable1.addCell(c41);

            PdfPCell c51 = new PdfPCell(new Phrase("Đơn giá", stt));
            c51.setHorizontalAlignment(Element.ALIGN_CENTER);
            c51.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c51.setBackgroundColor(BaseColor.GRAY);
            pdfPTable1.addCell(c51);

            PdfPCell c61 = new PdfPCell(new Phrase("Thành tiền", stt));
            c61.setBackgroundColor(BaseColor.GRAY);
            c61.setHorizontalAlignment(Element.ALIGN_CENTER);
            c61.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable1.addCell(c61);

            PdfPCell c71 = new PdfPCell(new Phrase("Đã thanh toán", stt));
            c71.setBackgroundColor(BaseColor.GRAY);
            c71.setHorizontalAlignment(Element.ALIGN_CENTER);
            c71.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable1.addCell(c71);

            PdfPCell c81 = new PdfPCell(new Phrase("Thiếu", stt));
            c81.setBackgroundColor(BaseColor.GRAY);
            c81.setHorizontalAlignment(Element.ALIGN_CENTER);
            c81.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable1.addCell(c81);
            c81.setBorder(Rectangle.NO_BORDER);

            AtomicInteger index1 = new AtomicInteger();
            this.getOrderEmployees(idSchool, idOrder, idList).getInOrder().getDataList().forEach(x -> {
                index1.getAndIncrement();
                PdfPCell id = new PdfPCell(new Phrase(String.valueOf(index1.get()), abc));
                id.setHorizontalAlignment(Element.ALIGN_CENTER);
                id.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable1.addCell(id);

                PdfPCell addCell = new PdfPCell(new Phrase(x.getName(), abc));
                pdfPTable1.addCell(addCell);

                // đơn vị
                PdfPCell dv = new PdfPCell(new Phrase(x.getUnit(), abc));
                dv.setHorizontalAlignment(Element.ALIGN_CENTER);
                dv.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable1.addCell(dv);

                // số lượng
                PdfPCell idth = new PdfPCell(new Phrase(String.valueOf(x.getNumber()), abc));
                idth.setHorizontalAlignment(Element.ALIGN_CENTER);
                idth.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable1.addCell(idth);

                double d = x.getPrice();
                double thanhtien1 = x.getMoney();
                double dathu1 = x.getPaid();
                double thieu1 = x.getRemain();
                String dongiafn = formatter.format(d).replace(",", ".");
                String thanhtienfn = formatter.format(thanhtien1).replace(",", ".");
                String dathufn = formatter.format(dathu1).replace(",", ".");
                String thieufn = formatter.format(thieu1).replace(",", ".");


                //đơn giá
                PdfPCell dongia = new PdfPCell(new Phrase(dongiafn, abc));
                dongia.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dongia.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // thành tiền
                PdfPCell thanhtien = new PdfPCell(new Phrase(thanhtienfn, abc));
                thanhtien.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thanhtien.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // đã thanh toán
                PdfPCell dathanhtoan = new PdfPCell(new Phrase(dathufn, abc));
                dathanhtoan.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dathanhtoan.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // thiếu
                PdfPCell thieufn1 = new PdfPCell(new Phrase(thieufn, abc));
                thieufn1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thieufn1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                pdfPTable1.addCell(dongia);
                pdfPTable1.addCell(thanhtien);
                pdfPTable1.addCell(dathanhtoan);
                pdfPTable1.addCell(thieufn1);
            });

            double tongcong1 = this.getOrderEmployees(idSchool, idOrder, idList).getInOrder().getMoneyTotal();
            double dathu1 = this.getOrderEmployees(idSchool, idOrder, idList).getInOrder().getMoneyPaidTotal();
            double thieu1 = this.getOrderEmployees(idSchool, idOrder, idList).getInOrder().getMoneyRemain();
            DecimalFormat formatter1 = new DecimalFormat("###,###,###");
            String tongcongt1 = formatter.format(tongcong1).replace(",", ".");
            String dathut1 = formatter.format(dathu1).replace(",", ".");
            String thieut1 = formatter.format(thieu1).replace(",", ".");

            pdfPTable1.addCell(createCell("Tổng cộng", 1, 5, Element.ALIGN_RIGHT));
            pdfPTable1.addCell(createCell(tongcongt1, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable1.addCell(createCell(dathut1, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable1.addCell(createCell(thieut1, 1, 1, Element.ALIGN_RIGHT));

            pdfPTable1.addCell(createCell("Nhà trường đã thanh toán", 1, 5, Element.ALIGN_RIGHT));
            pdfPTable1.addCell(createCell2(dathut1, 1, 3, Element.ALIGN_RIGHT));

            pdfPTable1.addCell(createCell3("Số tiền còn thiếu", 1, 5, Element.ALIGN_RIGHT));
            pdfPTable1.addCell(createCell3(thieut1, 1, 3, Element.ALIGN_RIGHT));

            //Add content to the document using Table objects.
            document.add(pdfPTable1);

            /// footer

            Paragraph p111 = new Paragraph(schoolInfoOptional.get().getNote(), ky);
            p111.setAlignment(Paragraph.ALIGN_LEFT);
            p111.setSpacingAfter(3f);
            document.add(p111);


            Paragraph p151 = new Paragraph("Người lĩnh", nguoinop);
            p151.setMultipliedLeading(2);
            p151.setTabSettings(new TabSettings(240f));
            p151.setIndentationLeft(30f);
            p151.add(Chunk.TABBING);
            p151.add(new Chunk("Người lập phiếu", nguoinop));
            p151.setTabSettings(new TabSettings(230f));
            p151.add(Chunk.TABBING);
            document.add(p151);


            Paragraph p161 = new Paragraph();
            p161.setTabSettings(new TabSettings(275f));
            p161.add(new Chunk(principal.getFullName(), nguoinop));
            p161.setIndentationLeft(270f);
            p161.setMultipliedLeading(4);
            document.add(p161);

        } else if (this.getOrderEmployees(idSchool, idOrder, idList).getInOrder().getDataList().size() != 0) {
            School schooltest = schoolRepository.findByIdAndDelActiveTrue(principal.getIdSchoolLogin()).orElseThrow();
            Paragraph p4 = new Paragraph(schooltest.getSchoolName(), phieuthu);
            p4.setAlignment(Paragraph.ALIGN_CENTER);
            p4.setSpacingAfter(3f);
            document.add(p4);

            // địa chỉ
            Paragraph p5 = new Paragraph("Địa chỉ: " + schooltest.getSchoolAddress(), ft);
            p5.setAlignment(Paragraph.ALIGN_CENTER);
            p5.setSpacingAfter(3f);
            document.add(p5);

            // ten phieu thu
            Paragraph p6 = new Paragraph("PHIẾU THU TIỀN NHÂN VIÊN THÁNG " + this.getOrderEmployees(idSchool, idOrder, idList).getMonth() + "/" + this.getOrderEmployees(idSchool, idOrder, idList).getYear(), phieuthu);
            p6.setAlignment(Paragraph.ALIGN_CENTER);
            p6.setSpacingAfter(3f);
            document.add(p6);
            // tên trẻ
            Chunk glue = new Chunk(new VerticalPositionMark());
            Paragraph p7 = new Paragraph("NV: " + this.getOrderEmployees(idSchool, idOrder, idList).getEmployeeName(), tentre1);
            p7.add(new Chunk(glue));
            p7.add("Mã:" + "..........");
            document.add(p7);

            // lớp
            Paragraph p8 = new Paragraph("ĐT: " + this.getOrderEmployees(idSchool, idOrder, idList).getPhone(), tentre1);
            p8.add(new Chunk(glue));
            p8.add("Quyển số:" + "..........");
            document.add(p8);

            //đt
            Paragraph p9 = new Paragraph(" ", tentre1);
            p9.add(new Chunk(glue));
            p9.add("Số:" + "..........");
            document.add(p9);

            // ngày
            Paragraph p10 = new Paragraph("" + "", ky);
            p10.add(new Chunk(glue));
            p10.add("Ngày " + LocalDate.now().getDayOfMonth() + " tháng " + LocalDate.now().getMonthValue() + " năm " + LocalDate.now().getYear());
            document.add(p10);

            //Ctạo bảng
            PdfPTable pdfPTable = new PdfPTable(8);
            pdfPTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfPTable.setWidths(new int[]{3, 16, 5, 3, 9, 9, 9, 9});
            pdfPTable.setWidthPercentage(100);
            // cách tren
            pdfPTable.setSpacingBefore(10f);
            PdfPCell c2 = new PdfPCell(new Phrase("TT", stt));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setVerticalAlignment(Element.ALIGN_CENTER);
            c2.setBackgroundColor(BaseColor.GRAY);
            c2.setFixedHeight(15f);
            pdfPTable.addCell(c2);

            PdfPCell c3 = new PdfPCell(new Phrase("Tên loại phí", stt));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c3.setBackgroundColor(BaseColor.GRAY);
            pdfPTable.addCell(c3);

            PdfPCell c31 = new PdfPCell(new Phrase("Đơn vị", stt));
            c31.setBackgroundColor(BaseColor.GRAY);
            c31.setHorizontalAlignment(Element.ALIGN_CENTER);
            c31.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c31);

            PdfPCell c4 = new PdfPCell(new Phrase("SL", stt));
            c4.setBackgroundColor(BaseColor.GRAY);
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c4);

            PdfPCell c5 = new PdfPCell(new Phrase("Đơn giá", stt));
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c5.setBackgroundColor(BaseColor.GRAY);
            pdfPTable.addCell(c5);

            PdfPCell c6 = new PdfPCell(new Phrase("Thành tiền", stt));
            c6.setBackgroundColor(BaseColor.GRAY);
            c6.setHorizontalAlignment(Element.ALIGN_CENTER);
            c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c6);

            PdfPCell c7 = new PdfPCell(new Phrase("Đã thanh toán", stt));
            c7.setBackgroundColor(BaseColor.GRAY);
            c7.setHorizontalAlignment(Element.ALIGN_CENTER);
            c7.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c7);

            PdfPCell c8 = new PdfPCell(new Phrase("Thiếu", stt));
            c8.setBackgroundColor(BaseColor.GRAY);
            c8.setHorizontalAlignment(Element.ALIGN_CENTER);
            c8.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(c8);
            c8.setBorder(Rectangle.NO_BORDER);

            AtomicInteger index = new AtomicInteger();
            this.getOrderEmployees(idSchool, idOrder, idList).getInOrder().getDataList().forEach(x -> {
                index.getAndIncrement();
                PdfPCell id = new PdfPCell(new Phrase(String.valueOf(index.get()), abc));
                id.setHorizontalAlignment(Element.ALIGN_CENTER);
                id.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(id);

                PdfPCell addCell = new PdfPCell(new Phrase(x.getName(), abc));
                pdfPTable.addCell(addCell);

                // đơn vị
                PdfPCell dv = new PdfPCell(new Phrase(x.getUnit(), abc));
                dv.setHorizontalAlignment(Element.ALIGN_CENTER);
                dv.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(dv);

                double d = x.getPrice();
                double dongia = x.getPrice();
                double thanhtien = x.getMoney();
                double dathanhtoan = x.getPaid();
                double thieu = x.getRemain();
                DecimalFormat formatter = new DecimalFormat("###,###,###");
                String dongia_fn = formatter.format(dongia).replace(",", ".");
                String thanhtien_fn = formatter.format(thanhtien).replace(",", ".");
                String dathanhtoan_fn = formatter.format(dathanhtoan).replace(",", ".");
                String thieu_fn = formatter.format(thieu).replace(",", ".");


                // số lượng
                PdfPCell idth = new PdfPCell(new Phrase(String.valueOf(x.getNumber()), abc));
                idth.setHorizontalAlignment(Element.ALIGN_CENTER);
                idth.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(idth);

                //đơn giá
                PdfPCell dongiafn = new PdfPCell(new Phrase(dongia_fn, abc));
                dongiafn.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dongiafn.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // thành tiền
                PdfPCell thanhtienfn = new PdfPCell(new Phrase(thanhtien_fn, abc));
                thanhtienfn.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thanhtienfn.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // đã thanh toán
                PdfPCell dathanhtoanfn = new PdfPCell(new Phrase(dathanhtoan_fn, abc));
                dathanhtoanfn.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dathanhtoanfn.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // thiếu
                PdfPCell thieufn = new PdfPCell(new Phrase(thieu_fn, abc));
                thieufn.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thieufn.setVerticalAlignment(Element.ALIGN_MIDDLE);

                pdfPTable.addCell(dongiafn);
                pdfPTable.addCell(thanhtienfn);
                pdfPTable.addCell(dathanhtoanfn);
                pdfPTable.addCell(thieufn);
            });

            double tongcong = this.getOrderEmployees(idSchool, idOrder, idList).getInOrder().getMoneyTotal();
            double dathu = this.getOrderEmployees(idSchool, idOrder, idList).getInOrder().getMoneyPaidTotal();
            double thieu = this.getOrderEmployees(idSchool, idOrder, idList).getInOrder().getMoneyRemain();
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            String tongcongt = formatter.format(tongcong).replace(",", ".");
            String dathut = formatter.format(dathu).replace(",", ".");
            String thieut = formatter.format(thieu).replace(",", ".");

            School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
            pdfPTable.addCell(createCell("Tổng cộng", 1, 5, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell(tongcongt, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell(dathut, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell(thieut, 1, 1, Element.ALIGN_RIGHT));

            pdfPTable.addCell(createCell("Nhà trường đã thanh toán", 1, 5, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell2(dathut, 1, 3, Element.ALIGN_RIGHT));

            pdfPTable.addCell(createCell3("Số tiền còn thiếu", 1, 5, Element.ALIGN_RIGHT));
            pdfPTable.addCell(createCell3(thieut, 1, 3, Element.ALIGN_RIGHT));

            //Add content to the document using Table objects.
            document.add(pdfPTable);

            /// footer
            Optional<SchoolInfo> schoolInfoOptional = schoolInfoRepository.findBySchoolId(idSchool);
            Paragraph p11 = new Paragraph(schoolInfoOptional.get().getNote(), ky);
            p11.setAlignment(Paragraph.ALIGN_LEFT);
            p11.setSpacingAfter(3f);
            document.add(p11);


            Paragraph p15 = new Paragraph("Người lĩnh", nguoinop);
            p15.setMultipliedLeading(2);
            p15.setTabSettings(new TabSettings(240f));
            p15.setIndentationLeft(30f);
            p15.add(Chunk.TABBING);
            p15.add(new Chunk("Người lập phiếu", nguoinop));
            p15.setTabSettings(new TabSettings(230f));
            p15.add(Chunk.TABBING);
            document.add(p15);


            Paragraph p16 = new Paragraph();
            p16.setTabSettings(new TabSettings(275f));
            p16.add(new Chunk(principal.getFullName(), nguoinop));
            p16.setIndentationLeft(270f);
            p16.setMultipliedLeading(4);
            document.add(p16);

        } else if (this.getOrderEmployees(idSchool, idOrder, idList).getOutOrder().getDataList().size() != 0) {
            // địa chỉ
            School school = schoolRepository.findByIdAndDelActiveTrue(principal.getIdSchoolLogin()).orElseThrow();

            Paragraph p41 = new Paragraph(school.getSchoolName(), phieuthu);
            p41.setAlignment(Paragraph.ALIGN_CENTER);
            p41.setSpacingAfter(3f);
            document.add(p41);

            Paragraph p51 = new Paragraph("Địa chỉ: " + school.getSchoolAddress(), ft);
            p51.setAlignment(Paragraph.ALIGN_CENTER);
            p51.setSpacingAfter(3f);
            document.add(p51);

            // ten phieu thu
            Paragraph p61 = new Paragraph("PHIẾU CHI LƯƠNG THÁNG " + this.getOrderEmployees(idSchool, idOrder, idList).getMonth() + "/" + this.getOrderEmployees(idSchool, idOrder, idList).getYear(), phieuthu);
            p61.setAlignment(Paragraph.ALIGN_CENTER);
            p61.setSpacingAfter(3f);
            document.add(p61);
            // tên trẻ
            Chunk glue1 = new Chunk(new VerticalPositionMark());
            Paragraph p71 = new Paragraph("NV: " + this.getOrderEmployees(idSchool, idOrder, idList).getEmployeeName(), tentre1);
            p71.add(new Chunk(glue1));
            p71.add("Mã:" + "..........");
            document.add(p71);

            // lớp
            Paragraph p81 = new Paragraph("ĐT: " + this.getOrderEmployees(idSchool, idOrder, idList).getPhone(), tentre1);
            p81.add(new Chunk(glue1));
            p81.add("Quyển số:" + "..........");
            document.add(p81);

            //đt
            Paragraph p91 = new Paragraph(" ", tentre1);
            p91.add(new Chunk(glue1));
            p91.add("Số:" + "..........");
            document.add(p91);

            // ngày
            Paragraph p101 = new Paragraph("" + "", ky);
            p101.add(new Chunk(glue1));
            p101.add("Ngày " + LocalDate.now().getDayOfMonth() + " tháng " + LocalDate.now().getMonthValue() + " năm " + LocalDate.now().getYear());
            document.add(p101);

            //Ctạo bảng
            PdfPTable pdfPTable1 = new PdfPTable(8);
            pdfPTable1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfPTable1.setWidths(new int[]{3, 16, 5, 3, 9, 9, 9, 9});
            pdfPTable1.setWidthPercentage(100);
            // cách tren
            pdfPTable1.setSpacingBefore(10f);
            PdfPCell c21 = new PdfPCell(new Phrase("TT", stt));
            c21.setHorizontalAlignment(Element.ALIGN_CENTER);
            c21.setVerticalAlignment(Element.ALIGN_CENTER);
            c21.setBackgroundColor(BaseColor.GRAY);
            c21.setFixedHeight(15f);
            pdfPTable1.addCell(c21);

            PdfPCell c311 = new PdfPCell(new Phrase("Tên loại phí", stt));
            c311.setHorizontalAlignment(Element.ALIGN_CENTER);
            c311.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c311.setBackgroundColor(BaseColor.GRAY);
            pdfPTable1.addCell(c311);

            PdfPCell c3112 = new PdfPCell(new Phrase("Đơn vị", stt));
            c3112.setBackgroundColor(BaseColor.GRAY);
            c3112.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3112.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable1.addCell(c3112);

            PdfPCell c41 = new PdfPCell(new Phrase("SL", stt));
            c41.setBackgroundColor(BaseColor.GRAY);
            c41.setHorizontalAlignment(Element.ALIGN_CENTER);
            c41.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable1.addCell(c41);

            PdfPCell c51 = new PdfPCell(new Phrase("Đơn giá", stt));
            c51.setHorizontalAlignment(Element.ALIGN_CENTER);
            c51.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c51.setBackgroundColor(BaseColor.GRAY);
            pdfPTable1.addCell(c51);

            PdfPCell c61 = new PdfPCell(new Phrase("Thành tiền", stt));
            c61.setBackgroundColor(BaseColor.GRAY);
            c61.setHorizontalAlignment(Element.ALIGN_CENTER);
            c61.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable1.addCell(c61);

            PdfPCell c71 = new PdfPCell(new Phrase("Đã thanh toán", stt));
            c71.setBackgroundColor(BaseColor.GRAY);
            c71.setHorizontalAlignment(Element.ALIGN_CENTER);
            c71.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable1.addCell(c71);

            PdfPCell c81 = new PdfPCell(new Phrase("Thiếu", stt));
            c81.setBackgroundColor(BaseColor.GRAY);
            c81.setHorizontalAlignment(Element.ALIGN_CENTER);
            c81.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable1.addCell(c81);
            c81.setBorder(Rectangle.NO_BORDER);

            AtomicInteger index1 = new AtomicInteger();
            this.getOrderEmployees(idSchool, idOrder, idList).getOutOrder().getDataList().forEach(x -> {
                index1.getAndIncrement();
                PdfPCell id = new PdfPCell(new Phrase(String.valueOf(index1.get()), abc));
                id.setHorizontalAlignment(Element.ALIGN_CENTER);
                id.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable1.addCell(id);

                PdfPCell addCell = new PdfPCell(new Phrase(x.getName(), abc));
                pdfPTable1.addCell(addCell);

                // đơn vị
                PdfPCell dv = new PdfPCell(new Phrase(x.getUnit(), abc));
                pdfPTable1.addCell(dv);

                // số lượng
                PdfPCell idth = new PdfPCell(new Phrase(String.valueOf(x.getNumber()), abc));
                idth.setHorizontalAlignment(Element.ALIGN_CENTER);
                idth.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable1.addCell(idth);

                double d = x.getPrice();
                double thanhtien1 = x.getMoney();
                double dathu = x.getPaid();
                double thieu = x.getRemain();
                DecimalFormat formatter = new DecimalFormat("###,###,###");
                String dongiafn = formatter.format(d).replace(",", ".");
                String thanhtienfn = formatter.format(thanhtien1).replace(",", ".");
                String dathu1 = formatter.format(dathu).replace(",", ".");
                String thieu1 = formatter.format(thieu).replace(",", ".");


                //đơn giá
                PdfPCell dongia = new PdfPCell(new Phrase(dongiafn, abc));
                dongia.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dongia.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // thành tiền
                PdfPCell thanhtien = new PdfPCell(new Phrase(thanhtienfn, abc));
                thanhtien.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thanhtien.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // đã thanh toán
                PdfPCell dathanhtoan = new PdfPCell(new Phrase(dathu1, abc));
                dathanhtoan.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dathanhtoan.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // thiếu
                PdfPCell thieufn = new PdfPCell(new Phrase(thieu1, abc));
                thieufn.setHorizontalAlignment(Element.ALIGN_RIGHT);
                thieufn.setVerticalAlignment(Element.ALIGN_MIDDLE);

                pdfPTable1.addCell(dongia);
                pdfPTable1.addCell(thanhtien);
                pdfPTable1.addCell(dathanhtoan);
                pdfPTable1.addCell(thieufn);
            });

            double tongcong1 = this.getOrderEmployees(idSchool, idOrder, idList).getOutOrder().getMoneyTotal();
            double dathu1 = this.getOrderEmployees(idSchool, idOrder, idList).getOutOrder().getMoneyPaidTotal();
            double thieu1 = this.getOrderEmployees(idSchool, idOrder, idList).getOutOrder().getMoneyRemain();
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            String tongcongt1 = formatter.format(tongcong1).replace(",", ".");
            String dathut1 = formatter.format(dathu1).replace(",", ".");
            String thieut1 = formatter.format(thieu1).replace(",", ".");

            pdfPTable1.addCell(createCell("Tổng cộng", 1, 5, Element.ALIGN_RIGHT));
            pdfPTable1.addCell(createCell(tongcongt1, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable1.addCell(createCell(dathut1, 1, 1, Element.ALIGN_RIGHT));
            pdfPTable1.addCell(createCell(thieut1, 1, 1, Element.ALIGN_RIGHT));

            pdfPTable1.addCell(createCell("Nhà trường đã thanh toán", 1, 5, Element.ALIGN_RIGHT));
            pdfPTable1.addCell(createCell2(dathut1, 1, 3, Element.ALIGN_RIGHT));

            pdfPTable1.addCell(createCell3("Số tiền còn thiếu", 1, 5, Element.ALIGN_RIGHT));
            pdfPTable1.addCell(createCell3(thieut1, 1, 3, Element.ALIGN_RIGHT));

            //Add content to the document using Table objects.
            document.add(pdfPTable1);

            /// footer
            Optional<SchoolInfo> schoolInfoOptional = schoolInfoRepository.findBySchoolId(idSchool);
            Paragraph p111 = new Paragraph(schoolInfoOptional.get().getNote(), ky);
            p111.setAlignment(Paragraph.ALIGN_LEFT);
            p111.setSpacingAfter(3f);
            document.add(p111);

            Paragraph p151 = new Paragraph("Người lĩnh", nguoinop);
            p151.setMultipliedLeading(2);
            p151.setTabSettings(new TabSettings(240f));
            p151.setIndentationLeft(30f);
            p151.add(Chunk.TABBING);
            p151.add(new Chunk("Người lập phiếu", nguoinop));
            p151.setTabSettings(new TabSettings(230f));
            p151.add(Chunk.TABBING);
            document.add(p151);


            Paragraph p161 = new Paragraph();
            p161.setTabSettings(new TabSettings(275f));
            p161.add(new Chunk(principal.getFullName(), nguoinop));
            p161.setIndentationLeft(270f);
            p161.setMultipliedLeading(4);
            document.add(p161);
        }


        // đóng file
        document.close();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductPdfReport.pdf");
        return is;
    }

    @Override
    public ByteArrayInputStream exportKid(UserPrincipal principal) throws IOException {
        XSSFColor greyOne = new XSSFColor(new java.awt.Color(207, 207, 207));
        XSSFColor yellowOne = new XSSFColor(new java.awt.Color(248, 235, 0));
        int[] widths = {5, 30, 30, 30, 30, 30, 30};
        Long idSchool = principal.getIdSchoolLogin();

        String[] columns = {"STT", "2", "3", "4", "5", "6", "7"};
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
            String schoolName = school.getSchoolName();
            Long idClass = null;
            // xuất học sinh theo trường
            List<Long> idClassList = maClassRepository.findIdClassInSchool(idSchool);
            idClassList.forEach(a -> {
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(a).orElseThrow();
                List<Kids> kidsList = kidsRepository.findByDelActiveTrueAndMaClass_Id(a);
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
                            cell.setCellValue("Toàn trường: ");
                            CellStyle threeStyle = workbook.createCellStyle();
                            org.apache.poi.ss.usermodel.Font cellFont = workbook.createFont();
                            cellFont.setFontHeightInPoints((short) 11);
                            cellFont.setBold(true);
                            threeStyle.setFont(cellFont);
                            cell.setCellStyle(threeStyle);
                        } else if (col == 0) {
                            cell.setCellValue("Ngày: " + "dateToStr" + " - " + "dateToStrSheet");
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
                ((XSSFCellStyle) contentStatusStyle).setFillForegroundColor(yellowOne);
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
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 0, 0));
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
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 1, 1));

                Cell cellLesson2 = headerRow5.createCell(2);
                cellLesson2.setCellValue("Ngày sinh");
                cellLesson2.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle2 = workbook.createCellStyle();
                cellLateStyle2.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 2, 2));

                Cell cellLesson3 = headerRow5.createCell(3);
                cellLesson3.setCellValue("Danh sách mã hóa đơn");
                cellLesson3.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle3 = workbook.createCellStyle();
                cellLateStyle3.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 3, 3));

                Cell cellLesson4 = headerRow5.createCell(4);
                cellLesson4.setCellValue("Tổng phải chi");
                cellLesson4.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle4 = workbook.createCellStyle();
                cellLateStyle4.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 4, 4));

                Cell cellLesson5 = headerRow5.createCell(5);
                cellLesson5.setCellValue("Đã chi");
                cellLesson5.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle5 = workbook.createCellStyle();
                cellLateStyle5.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 5, 5));

                Cell cellLesson6 = headerRow5.createCell(6);
                cellLesson6.setCellValue("Còn lại phải chi");
                cellLesson6.setCellStyle(contentStatusStyle);
                CellStyle cellLateStyle6 = workbook.createCellStyle();
                cellLateStyle6.setAlignment(HorizontalAlignment.CENTER);
                sheet.addMergedRegion(new CellRangeAddress(rowParent, rowChild, 6, 6));
                Row headerRow6 = sheet.createRow(rowChild);
                for (int col = 0; col < 7; col++) {
                    sheet.setColumnWidth(col, widths[col] * 256);
                    Cell cell = headerRow6.createCell(col);
                }

                int rowIdx = 5;
                int i = -1;

                for (Kids x : kidsList) {
                    String fillData = "Thanh";
                    i -= -1;

                    Row row = sheet.createRow(rowIdx++);

                    Cell cellId = row.createCell(0);
                    cellId.setCellValue(i);
                    cellId.setCellStyle(contentMealStyle);

                    Cell cellKidName = row.createCell(1);
                    cellKidName.setCellValue(x.getFullName());
                    cellKidName.setCellStyle(contentMealStyle);

                    Cell cellAbsentLetterYes = row.createCell(2);
                    cellAbsentLetterYes.setCellValue(fillData);
                    cellAbsentLetterYes.setCellStyle(contentMealStyle);

                    Cell cellAbsentLetterNo = row.createCell(3);
                    cellAbsentLetterNo.setCellValue(fillData);
                    cellAbsentLetterNo.setCellStyle(contentMealStyle);

                    Cell cellAbsentStatus = row.createCell(4);
                    cellAbsentStatus.setCellValue(fillData);
                    cellAbsentStatus.setCellStyle(contentMealStyle);

                    Cell cellMorningYes = row.createCell(5);
                    cellMorningYes.setCellValue(fillData);
                    cellMorningYes.setCellStyle(contentMealStyle);

                    Cell cellMorningNo = row.createCell(6);
                    cellMorningNo.setCellValue(fillData);
                    cellMorningNo.setCellStyle(contentMealStyle);
                }
            });
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
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

        int[] widths = {5, 30, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 14, 7, 7, 7, 20, 20};

        LocalDate dateStart = LocalDate.now();
        LocalDate dateEnd = LocalDate.now();
        Long idSchool = principal.getIdSchoolLogin();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dateToStr = df.format(dateStart);
        String dateToStrSheet = df.format(dateEnd);
        String[] columns = {"STT", "Họ và tên", "Nghỉ có phép", "Nghỉ không phép", "Đi học", "Có phép", "Không phép", "Đi học", "Có phép", "Không phép", "Đi học", "Có phép", "Không phép", "Đi học", "Buổi", "Sáng", "Trưa", "Chiều", "Phút"};

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
            String schoolName = school.getSchoolName();
            // xuất học sinh theo trường
            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findAll();
            Sheet sheet = workbook.createSheet("CHAM_CONG");
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

            // sđón muộn
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

            // sđón muộn
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
            int getEatMorningSecond = 0;

            int getEatNoon = 0;
            int getEatAfternoon = 0;
            int getEatAfternoonSecond = 0;
            int geteatEvening = 0;
            int getMinutesLate = 0;

            for (InfoEmployeeSchool x : infoEmployeeSchoolList) {

                AttendanceEmployeesStatisticalResponse fillData = attendanceEmployeeService.attendanceEmployeesStatistical(x, dateStart, dateEnd);
                i++;

                Row row = sheet.createRow(rowIdx++);

                Cell cellId = row.createCell(0);
                cellId.setCellValue(i);
                cellId.setCellStyle(contentCellStyle);

                Cell cellKidName = row.createCell(1);
                cellKidName.setCellValue("Tên nhân viên");
                cellKidName.setCellStyle(contentCellStyle);

                Cell cellAbsentLetterYes = row.createCell(2);
                cellAbsentLetterYes.setCellValue(fillData.getAfternoonYes());
                cellAbsentLetterYes.setCellStyle(contentStatusStyle);

                Cell cellAbsentLetterNo = row.createCell(3);
                cellAbsentLetterNo.setCellValue(fillData.getAfternoonYes());
                cellAbsentLetterNo.setCellStyle(contentStatusStyle);

                Cell cellAbsentStatus = row.createCell(4);
                cellAbsentStatus.setCellValue(fillData.getAfternoonYes());
                cellAbsentStatus.setCellStyle(contentStatusStyle);

                Cell cellMorningYes = row.createCell(5);
                cellMorningYes.setCellValue(fillData.getAfternoonYes());
                cellMorningYes.setCellStyle(contentLessonStyle);

                Cell cellMorningNo = row.createCell(6);
                cellMorningNo.setCellValue(fillData.getAfternoonYes());
                cellMorningNo.setCellStyle(contentLessonStyle);

                Cell cellMorning = row.createCell(7);
                cellMorning.setCellValue(fillData.getAfternoonYes());
                cellMorning.setCellStyle(contentLessonStyle);

                Cell cellAfternoonYes = row.createCell(8);
                cellAfternoonYes.setCellValue(fillData.getAfternoonYes());
                cellAfternoonYes.setCellStyle(contentLessonStyle);


                Cell cellAfternoonNo = row.createCell(9);
                cellAfternoonNo.setCellValue(fillData.getAfternoonYes());
                cellAfternoonNo.setCellStyle(contentLessonStyle);

                Cell cellAfternoon = row.createCell(10);
                cellAfternoon.setCellValue(fillData.getAfternoonYes());
                cellAfternoon.setCellStyle(contentLessonStyle);

                Cell cellEveningYes = row.createCell(11);
                cellEveningYes.setCellValue(fillData.getAfternoonYes());
                cellEveningYes.setCellStyle(contentLessonStyle);

                Cell cellEveningNo = row.createCell(12);
                cellEveningNo.setCellValue(fillData.getAfternoonYes());
                cellEveningNo.setCellStyle(contentLessonStyle);

                Cell cellEvening = row.createCell(13);
                cellEvening.setCellValue(fillData.getAfternoonYes());
                cellEvening.setCellStyle(contentLessonStyle);


                Cell cellminutePickupLateDay = row.createCell(14);
                cellminutePickupLateDay.setCellValue(fillData.getAfternoonYes());
                cellminutePickupLateDay.setCellStyle(contentLateStyle);

                Cell cellEatBreakfast = row.createCell(15);
                cellEatBreakfast.setCellValue(fillData.getAfternoonYes());
                cellEatBreakfast.setCellStyle(contentMealStyle);

                Cell cellEatSecondBreakfast = row.createCell(16);
                cellEatSecondBreakfast.setCellValue(fillData.getAfternoonYes());
                cellEatSecondBreakfast.setCellStyle(contentMealStyle);

                Cell celleatLunch = row.createCell(17);
                celleatLunch.setCellValue(fillData.getAfternoonYes());
                celleatLunch.setCellStyle(contentMealStyle);

                Cell celleatAfternoon = row.createCell(18);
                celleatAfternoon.setCellValue(fillData.getAfternoonYes());
                celleatAfternoon.setCellStyle(contentMealStyle);

                Cell celleatSecondAfternoon = row.createCell(19);
                celleatSecondAfternoon.setCellValue(fillData.getAfternoonYes());
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

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }

    }

    public PdfPCell createCell(String content, float borderWidth, int colspan, int alignment) throws IOException, DocumentException {
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font stt = new Font(CROACIA, 9, Font.BOLD, BaseColor.BLACK);
        PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(content), stt));
        cell.setBorderWidth(borderWidth);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    public PdfPCell createCellBackGroundGray(String content, float borderWidth, int colspan, int alignment) throws IOException, DocumentException {
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font stt = new Font(CROACIA, 9, Font.BOLD, BaseColor.BLACK);
        PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(content), stt));
        cell.setBorderWidth(borderWidth);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        cell.setBackgroundColor(new BaseColor(182, 184, 186));
        return cell;
    }

    public PdfPCell createCell2(String content, float borderWidth, int colspan, int alignment) throws IOException, DocumentException {
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font stt = new Font(CROACIA, 9, Font.NORMAL, BaseColor.BLUE);
        PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(content), stt));
        cell.setBorderWidth(borderWidth);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    public PdfPCell createCell3(String content, float borderWidth, int colspan, int alignment) throws IOException, DocumentException {
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font stt = new Font(CROACIA, 9, Font.NORMAL, BaseColor.RED);
        PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(content), stt));
        cell.setBorderWidth(borderWidth);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    private KidsOrderPdf getOrderKids(Long idSchool, Long idOrder, List<Long> idList) {
        FnOrderKids fnOrderKids = fnOrderKidsRepository.findById(idOrder).orElseThrow(() -> new NoSuchElementException("not found order by id in print"));
        int year = fnOrderKids.getYear();
        int month = fnOrderKids.getMonth();
        Kids kids = fnOrderKids.getKids();
        if (!kids.getIdSchool().equals(idSchool)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Học sinh không thuộc trường này");
        }
        KidsOrderPdf response = new KidsOrderPdf();
        response.setYear(year);
        response.setMonth(month);
        response.setKidName(kids.getFullName());
        response.setClassName(kids.getMaClass().getClassName());
        response.setPhone(kids.getParent() != null ? kids.getParent().getMaUser().getPhone() : null);

        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByIdInAndKidsIdSchoolAndDelActiveTrue(idList, idSchool);
        //check số khoản thu truyền vào có bằng số tìm kiếm được trong DB hay không
        if (fnKidsPackageList.size() < idList.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_ENOUGH_PACKAGE);
        }
        WalletParent walletParent = FinanceUltils.getWalletParentFromKids(kids);
        List<FnKidsPackage> inKidsPackageList = fnKidsPackageList.stream().filter(x -> x.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
        List<FnKidsPackage> outKidsPackageList = fnKidsPackageList.stream().filter(x -> x.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
        OrderPdf inOrder = new OrderPdf();
        OrderPdf outOrder = new OrderPdf();
        inOrder.setMoneyWallet(FinanceUltils.getWalletParentBalance(walletParent));
        this.setDataKidsPackage(inOrder, inKidsPackageList);
        outOrder.setMoneyWallet(FinanceUltils.getWalletParentBalance(walletParent));
        this.setDataKidsPackage(outOrder, outKidsPackageList);
        response.setInOrder(inOrder);
        response.setOutOrder(outOrder);
        return response;
    }

    private void setDataKidsPackage(OrderPdf order, List<FnKidsPackage> kidsPackageList) {
        List<KidsPackagePdf> dataList = new ArrayList<>();
        double moneyTotal = 0;
        double moneyPaidTotal = 0;
        double moneyRemain = 0;
        for (FnKidsPackage x : kidsPackageList) {
            KidsPackagePdf model = new KidsPackagePdf();
            model.setName(x.getFnPackage().getName());
            model.setNumber(x.getUsedNumber());
            model.setPrice(FinanceUltils.getPriceData(x));
            model.setMoney(FinanceUltils.getMoneyCalculate(x));
            model.setPaid(x.getPaid());
            model.setRemain(model.getMoney() - model.getPaid());
            moneyTotal += model.getMoney();
            moneyPaidTotal += model.getPaid();
            moneyRemain += model.getRemain();
            dataList.add(model);
        }
        order.setMoneyTotal(moneyTotal);
        order.setMoneyPaidTotal(moneyPaidTotal);
        order.setMoneyRemain(moneyRemain);
        order.setDataList(dataList);
    }


    private EmployeesOrderPdf getOrderEmployees(Long idSchool, Long idOrder, List<Long> idList) {
        FnOrderEmployee fnOrderEmployee = fnOrderEmployeeRepository.findById(idOrder).orElseThrow(() -> new NoSuchElementException("not found order by id in print"));
        int year = fnOrderEmployee.getYear();
        int month = fnOrderEmployee.getMonth();
        InfoEmployeeSchool infoEmployeeSchool = fnOrderEmployee.getInfoEmployeeSchool();
        if (!infoEmployeeSchool.getSchool().getId().equals(idSchool)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nhân sự không thuộc trường này");
        }
        EmployeesOrderPdf response = new EmployeesOrderPdf();
        response.setYear(year);
        response.setMonth(month);
        response.setEmployeeName(infoEmployeeSchool.getFullName());
        response.setPhone(infoEmployeeSchool.getPhone() != null ? infoEmployeeSchool.getPhone() : null);

        List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByIdInAndInfoEmployeeSchool_SchoolId(idList, idSchool);
        //check số khoản thu truyền vào có bằng số tìm kiếm được trong DB hay không
        if (fnEmployeeSalaryList.size() < idList.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_ENOUGH_PACKAGE);
        }

        List<FnEmployeeSalary> inFnEmployeeSalary = fnEmployeeSalaryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
        List<FnEmployeeSalary> outFnEmployeeSalaryList = fnEmployeeSalaryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
        OrderDetailPdf inOrder = new OrderDetailPdf();
        OrderDetailPdf outOrder = new OrderDetailPdf();
        this.setDataEmployeesPackage(inOrder, inFnEmployeeSalary);
        this.setDataEmployeesPackage(outOrder, outFnEmployeeSalaryList);
        response.setInOrder(inOrder);
        response.setOutOrder(outOrder);
        return response;
    }

    private void setDataEmployeesPackage(OrderDetailPdf order, List<FnEmployeeSalary> fnEmployeeSalaryList) {
        List<EmployeePackagePdf> dataList = new ArrayList<>();
        double moneyTotal = 0;
        double moneyPaidTotal = 0;
        double moneyRemain = 0;
        for (FnEmployeeSalary x : fnEmployeeSalaryList) {
            EmployeePackagePdf model = new EmployeePackagePdf();
            model.setUnit(x.getUnit());
            model.setName(x.getName());
            model.setNumber(x.getUserNumber());
            model.setPrice(FinanceUltils.getPriceDataEmployee(x));
            model.setMoney(FinanceUltils.getMoneySalary(x));
            model.setPaid(x.getPaid());
            model.setRemain(model.getMoney() - model.getPaid());
            moneyTotal += model.getMoney();
            moneyPaidTotal += model.getPaid();
            moneyRemain += model.getRemain();
            dataList.add(model);
        }
        order.setMoneyTotal(moneyTotal);
        order.setMoneyPaidTotal(moneyPaidTotal);
        order.setMoneyRemain(moneyRemain);
        order.setDataList(dataList);
    }
}
