package com.example.onekids_project.controller.pdf;

/**
 * date 2021-03-01 11:38
 *
 * @author Nguyễn Thành
 */

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.finance.CashInternal.FnCashInternal;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.WalletParent;
import com.example.onekids_project.entity.school.FnBank;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.model.pdf.KidsOrderPdf;
import com.example.onekids_project.model.pdf.KidsPackagePdf;
import com.example.onekids_project.model.pdf.OrderPdf;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.FinanceUltils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/web/pdfn")
public class CreatePDF {

    @Autowired
    private FnBankRepository fnBankRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private FnOrderKidsRepository fnOrderKidsRepository;

    @Autowired
    private FnKidsPackageRepository fnKidsPackageRepository;
    @Autowired
    private SchoolRepository repository;

    @Autowired
    private FnCashInternalSchoolRepository fnCashInternalSchoolRepository;

    private static String numberToString(BigDecimal number) {
        String sNumber = number.toString();
        // Tao mot bien tra ve
        String sReturn = "";
        // Tim chieu dai cua chuoi
        int iLen = sNumber.length();
        // Lat nguoc chuoi nay lai
        String sNumber1 = "";
        for (int i = iLen - 1; i >= 0; i--) {
            sNumber1 += sNumber.charAt(i);
        }
        // Tao mot vong lap de doc so
        // Tao mot bien nho vi tri cua sNumber
        int iRe = 0;
        do {
            // Tao mot bien cat tam
            String sCut = "";
            if (iLen > 3) {
                sCut = sNumber1.substring((iRe * 3), (iRe * 3) + 3);
                sReturn = Read(sCut, iRe) + sReturn;
                iRe++;
                iLen -= 3;
            } else {
                sCut = sNumber1.substring((iRe * 3), (iRe * 3) + iLen);
                sReturn = Read(sCut, iRe) + sReturn;
                break;
            }
        } while (true);
        if (sReturn.length() > 1) {
            sReturn = sReturn.substring(0, 1).toUpperCase() + sReturn.substring(1);
        }
        sReturn = sReturn + "đồng";

        // xu ly lan cuoi voi 220 000 tỷ 000 000 000 000 000 HUTTV ADDED 3 OCT
        if (sNumber.length() > 12) {
            // tren gia tri can xu ly, kiem tra xem don vi TY co = 000 khong
            int begin = sNumber.length() - 9;
            String donviTy = sNumber.substring(begin - 3, (begin - 3) + 3);
            if (donviTy.equals("000")) {
                sReturn = sReturn.replaceFirst("ngàn", "ngàn tỷ");
            }
        }

        return sReturn;
    }

    // Khoi tao ham Read
    private static String Read(String sNumber, int iPo) {
        // Tao mot bien tra ve
        String sReturn = "";
        // Tao mot bien so
        String[] sPo = {"", "ngàn" + " ",
                "triệu" + " ", "tỷ" + " ", "ngàn" + " "};
        String[] sSo = {"không" + " ", "một" + " ",
                "hai" + " ", "ba" + " ",
                "bốn" + " ", "năm" + " ",
                "sáu" + " ", "bảy" + " ",
                "tám" + " ", "chín" + " "};
        String sDonvi[] = {"", "mươi" + " ",
                "trăm" + " "};
        // Tim chieu dai cua chuoi
        int iLen = sNumber.length();
        // Tao mot bien nho vi tri doc
        int iRe = 0;
        // Tao mot vong lap de doc so
        for (int i = 0; i < iLen; i++) {
            String sTemp = "" + sNumber.charAt(i);
            int iTemp = Integer.parseInt(sTemp);
            // Tao mot bien ket qua
            String sRead = "";
            // Kiem tra xem so nhan vao co = 0 hay ko
            if (iTemp == 0) {
                switch (iRe) {
                    case 0:
                        break;// Khong lam gi ca
                    case 1: {
                        if (Integer.parseInt("" + sNumber.charAt(0)) != 0) {
                            sRead = "lẻ" + " ";
                        }
                        break;
                    }
                    case 2: {
                        if (Integer.parseInt("" + sNumber.charAt(0)) != 0
                                && Integer.parseInt("" + sNumber.charAt(1)) != 0) {
                            sRead = "không trăm" + " ";
                        }
                        break;
                    }
                }
            } else if (iTemp == 1) {
                if (iRe == 1) {
                    sRead = "mười" + " ";
                } else {
                    sRead = "một" + " " + sDonvi[iRe];
                }
            } else if (iTemp == 5) {
                if (iRe == 0) {
                    if (sNumber.length() <= 1) {
                        sRead = "năm" + " ";
                    } else if (Integer.parseInt("" + sNumber.charAt(1)) != 0) {
                        sRead = "lăm" + " ";
                    } else
                        sRead = "năm" + " ";
                } else {
                    sRead = sSo[iTemp] + sDonvi[iRe];
                }
            } else {
                sRead = sSo[iTemp] + sDonvi[iRe];
            }

            sReturn = sRead + sReturn;
            iRe++;
        }
        if (sReturn.length() > 0) {
            sReturn += sPo[iPo];
        }
        // xu ly lan cuoi voi 220 000 tỷ 000 000 000 000 000
        if (sNumber.length() > 12) {
            // tren gia tri can xu ly, kiem tra xem don vi TY co = 000 khong
            System.out.println(sNumber.substring(11, 8));
        }
        return sReturn;
    }

    @GetMapping("/create/{id}")
    public ResponseEntity<Resource> generateExcelReport(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) throws DocumentException, IOException {
        Long idSchool = principal.getIdSchoolLogin();
        FnCashInternal fnCashInternal = fnCashInternalSchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font phieuthu = new Font(CROACIA, 13, Font.BOLD);
        Font nguoinop = new Font(CROACIA, 10, Font.BOLD);
        Font ngay = new Font(CROACIA, 11, Font.ITALIC);
        Font ky = new Font(CROACIA, 8, Font.ITALIC);
        Font ft = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 11);
        Document document = new Document(PageSize.A5.rotate(), 30f, 20f, 30f, 20f);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, os);

        document.open();
        School school = repository.findByIdAndDelActiveTrue(idSchool).orElseThrow();

        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p = new Paragraph("Tên trường: " + school.getSchoolName(), ft);
        p.add(new Chunk(glue));
        p.add("Quyển số:......");
        document.add(p);

        Paragraph p1 = new Paragraph("Địa chỉ: " + school.getSchoolAddress(), ft);
        p1.add(new Chunk(glue));
        p1.add("Số:.................");
        document.add(p1);

        Paragraph p2 = new Paragraph("Điện thoại: " + school.getSchoolPhone(), ft);
        p2.add(new Chunk(glue));
        p2.add("Nợ:................");
        document.add(p2);

        Paragraph p3 = new Paragraph("Email: " + school.getSchoolEmail(), ft);
        p3.add(new Chunk(glue));
        p3.add("Có:.................");
        document.add(p3);

        if (fnCashInternal.getCategory().equals(FinanceConstant.CATEGORY_IN)) {
            Paragraph p4 = new Paragraph("PHIẾU THU", phieuthu);
            p4.setAlignment(Paragraph.ALIGN_CENTER);
            p4.setLeading(10f);
            p4.setMultipliedLeading(1);
            document.add(p4);
        } else {
            Paragraph p4 = new Paragraph("PHIẾU CHI", phieuthu);
            p4.setAlignment(Paragraph.ALIGN_CENTER);
            p4.setLeading(10f);
            p4.setMultipliedLeading(1);
            document.add(p4);
        }


        Paragraph p5 = new Paragraph("Ngày.." + fnCashInternal.getDate().getDayOfMonth() + "..Tháng.." + fnCashInternal.getDate().getMonthValue() + "..Năm.." + fnCashInternal.getDate().getYear(), ngay);
        p5.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p5);
        p5.setMultipliedLeading(2);

        if (fnCashInternal.getCategory().equals(FinanceConstant.CATEGORY_IN)) {
            Paragraph p6 = new Paragraph("Họ và tên người nộp:       " + fnCashInternal.getFnPeopleTypeOther().getName(), ft);
            p6.setMultipliedLeading(2);
            document.add(p6);
        } else {
            Paragraph p6 = new Paragraph("Họ và tên người nhận:       " + fnCashInternal.getFnPeopleTypeOther().getName(), ft);
            p6.setMultipliedLeading(2);
            document.add(p6);
        }

        if (fnCashInternal.getFnPeopleTypeOther().getAddress() != null) {
            Paragraph p7 = new Paragraph("Địa chỉ: " + fnCashInternal.getFnPeopleTypeOther().getAddress(), ft);
            document.add(p7);
        } else {
            Paragraph p7 = new Paragraph("Địa chỉ: " + "", ft);
            document.add(p7);
        }

        if (fnCashInternal.getFnPeopleTypeOther().getPhone() != null) {
            Paragraph p8 = new Paragraph("Số điện thoại: " + fnCashInternal.getFnPeopleTypeOther().getPhone(), ft);
            document.add(p8);
        } else {
            Paragraph p8 = new Paragraph("Số điện thoại: " + "", ft);
            document.add(p8);
        }

        if (fnCashInternal.getCategory().equals(FinanceConstant.CATEGORY_IN)) {
            if (fnCashInternal.getContent() != null) {
                Paragraph p9 = new Paragraph("Lý do nộp: " + fnCashInternal.getContent(), ft);
                document.add(p9);
            } else {
                Paragraph p9 = new Paragraph("Lý do nộp: " + "", ft);
                document.add(p9);
            }
        } else {
            if (fnCashInternal.getContent() != null) {
                Paragraph p9 = new Paragraph("Lý do chi: " + fnCashInternal.getContent(), ft);
                document.add(p9);
            } else {
                Paragraph p9 = new Paragraph("Lý do chi: " + "", ft);
                document.add(p9);
            }
        }

        double d = fnCashInternal.getMoney();
        BigDecimal numBigDecimal = new BigDecimal(d, MathContext.DECIMAL64);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String b = formatter.format(d);
        String a = b.replace(",", ".");
        Paragraph p11 = new Paragraph("Số tiền:  " + a + "   VND", ft);
        document.add(p11);


        Paragraph p12 = new Paragraph("Bằng chữ:  " + numberToString(numBigDecimal), ft);
        document.add(p12);

        Paragraph p13 = new Paragraph("Kèm theo:" + ".........................................................................................................................................." + "chứng từ gốc", ft);
        document.add(p13);

        if (fnCashInternal.getCategory().equals(FinanceConstant.CATEGORY_IN)) {

            Paragraph p14 = new Paragraph("Người nộp tiền", nguoinop);
            p14.setMultipliedLeading(2);
            p14.setTabSettings(new TabSettings(110f));
            p14.setIndentationLeft(10f);
            p14.add(Chunk.TABBING);
            p14.add(new Chunk("Người lập phiếu", nguoinop));
            p14.setTabSettings(new TabSettings(110f));
            p14.add(Chunk.TABBING);
            p14.add(new Chunk("Thủ quỹ", nguoinop));
            p14.setTabSettings(new TabSettings(110f));
            p14.add(Chunk.TABBING);

            p14.add(new Chunk("Kế toán", nguoinop));
            p14.setTabSettings(new TabSettings(110f));
            p14.add(Chunk.TABBING);

            p14.add(new Chunk("Thủ trưởng", nguoinop));
            p14.setMultipliedLeading(3);
            document.add(p14);

        } else {
            Paragraph p14 = new Paragraph("Người nhận tiền", nguoinop);
            p14.setMultipliedLeading(2);
            p14.setTabSettings(new TabSettings(120f));
            p14.add(Chunk.TABBING);
            p14.add(new Chunk("Người lập phiếu", nguoinop));
            p14.setTabSettings(new TabSettings(120f));
            p14.add(Chunk.TABBING);

            p14.add(new Chunk("Thủ quỹ", nguoinop));
            p14.setTabSettings(new TabSettings(120f));
            p14.add(Chunk.TABBING);

            p14.add(new Chunk("Kế toán", nguoinop));
            p14.setTabSettings(new TabSettings(120f));
            p14.add(Chunk.TABBING);

            p14.add(new Chunk("Thủ trưởng", nguoinop));
            p14.setMultipliedLeading(3);
            document.add(p14);
        }
        Paragraph p15 = new Paragraph();
        p15.add(new Chunk("(ký,họ tên)", ky));
        p15.setTabSettings(new TabSettings(115f));
        p15.setIndentationLeft(13f);
        p15.add(Chunk.TABBING);
        p15.add(new Chunk("(ký, họ tên)", ky));
        p15.setTabSettings(new TabSettings(115f));
        p15.add(Chunk.TABBING);
        p15.add(new Chunk("(ký, họ tên)", ky));
        p15.setTabSettings(new TabSettings(115f));
        p15.add(Chunk.TABBING);
        p15.add(new Chunk("(ký, họ tên)", ky));
        p15.setTabSettings(new TabSettings(115f));
        p15.add(Chunk.TABBING);
        p15.add(new Chunk("(ký, họ tên)", ky));
        document.add(p15);
        p15.setMultipliedLeading(3);

        Paragraph p16 = new Paragraph();
        p16.setTabSettings(new TabSettings(120f));
        p16.add(new Chunk(fnCashInternal.getFnPeopleTypeOther().getName(), nguoinop));

        p16.setMultipliedLeading(5);
        document.add(p16);

        document.close();
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductPdfReport.pdf");
        return new ResponseEntity<>(new InputStreamResource(is), headers, HttpStatus.OK);
    }

    // thu hoc phi hoc sinh
    @GetMapping("/create/table")
    public ResponseEntity<Resource> generateExcelReportTable(@CurrentUser UserPrincipal principal) throws DocumentException, IOException {
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font phieuthu = new Font(CROACIA, 11, Font.BOLD);
        Font stt = new Font(CROACIA, 11, Font.BOLD);
        Font abc = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9);
        Font nguoinop = new Font(CROACIA, 10, Font.BOLD);
        Font ky = new Font(CROACIA, 9, Font.ITALIC);
        Font tentre = new Font(CROACIA, 9, Font.BOLD);
        Font ft = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10);
        // tạo trang giấy kichcs thước a5
        Document document = new Document(PageSize.A5);

        // trang 2
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, os);

        document.setMargins(15f, 15f, 10, 0);
        //Open the document.
        document.open();

        // thông tin trường
        School schooltest = repository.findByIdAndDelActiveTrue(1L).orElseThrow();
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
        Paragraph p6 = new Paragraph("PHIẾU THU HỌC PHÍ THÁNG 3", phieuthu);
        p6.setAlignment(Paragraph.ALIGN_CENTER);
        p6.setSpacingAfter(3f);
        document.add(p6);
        // tên trẻ
        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p7 = new Paragraph("Trẻ: " + "Nguyễn Huyền Phương Linh", tentre);
        p7.add(new Chunk(glue));
        p7.add("Mã:" + "0001");
        document.add(p7);

        // lớp
        Paragraph p8 = new Paragraph("Lớp: " + "Doraemon", tentre);
        p8.add(new Chunk(glue));
        p8.add("Quyển số:" + ".....");
        document.add(p8);

        //đt
        Paragraph p9 = new Paragraph("ĐT: " + schooltest.getSchoolPhone(), tentre);
        p9.add(new Chunk(glue));
        p9.add("Số:" + ".....");
        document.add(p9);

        // ngày
        Paragraph p10 = new Paragraph("" + "", ky);
        p10.add(new Chunk(glue));
        p10.add("Ngày " + LocalDate.now().getDayOfMonth() + " tháng " + LocalDate.now().getMonthValue() + " năm " + LocalDate.now().getYear());
        document.add(p10);

        //Ctạo bảng
        PdfPTable pdfPTable = new PdfPTable(7);
        pdfPTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPTable.setWidths(new int[]{3, 20, 3, 9, 9, 9, 9});
        pdfPTable.setWidthPercentage(100);
        // cách tren
        pdfPTable.setSpacingBefore(10f);
        PdfPCell c2 = new PdfPCell(new Phrase("TT", stt));
        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
        c2.setVerticalAlignment(Element.ALIGN_CENTER);
        c2.setBackgroundColor(BaseColor.GRAY);
        pdfPTable.addCell(c2);

        PdfPCell c3 = new PdfPCell(new Phrase("Tên loại phí", stt));
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c3.setBackgroundColor(BaseColor.GRAY);
        pdfPTable.addCell(c3);

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

        PdfPCell c7 = new PdfPCell(new Phrase("Đã thu", stt));
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

        List<Kids> kidsList = kidsRepository.findAll();

        for (Kids x : kidsList) {
            // stt
            PdfPCell id = new PdfPCell(new Phrase(x.getId().toString(), abc));
            id.setHorizontalAlignment(Element.ALIGN_CENTER);
            id.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(id);

            // ten loại phí
            PdfPCell addCell = new PdfPCell(new Phrase(x.getFullName(), abc));
            pdfPTable.addCell(addCell);

            //SL
            PdfPCell idth = new PdfPCell(new Phrase(x.getId().toString(), abc));
            idth.setHorizontalAlignment(Element.ALIGN_CENTER);
            idth.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(idth);

            // đơn giá
            double d = 1245600;
            BigDecimal numBigDecimal = new BigDecimal(d, MathContext.DECIMAL64);
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            String a = formatter.format(d);
//            Paragraph p11 = new Paragraph("Số tiền:  " + a + "   VND", ft);
//            document.add(p11);

            PdfPCell sotien = new PdfPCell(new Phrase(a, abc));
            sotien.setHorizontalAlignment(Element.ALIGN_RIGHT);
            sotien.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(sotien);

            // thành tiền
            PdfPCell thanhtien = new PdfPCell(new Phrase("15,000,000", abc));
            thanhtien.setHorizontalAlignment(Element.ALIGN_RIGHT);
            thanhtien.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(thanhtien);

            // đã thu
            pdfPTable.addCell(sotien);

            // thiếu
            pdfPTable.addCell(sotien);
        }
        School school = repository.findByIdAndDelActiveTrue(1L).orElseThrow();
        pdfPTable.addCell(createCell("Tổng cộng", 1, 4, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell(school.getSchoolPhone(), 1, 1, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell("1,552,000", 1, 1, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell("1,552,000", 1, 1, Element.ALIGN_RIGHT));

        pdfPTable.addCell(createCell("Số dư trong ví", 1, 4, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell2("1,552,000", 1, 3, Element.ALIGN_RIGHT));

        pdfPTable.addCell(createCell3("Phụ huynh phải thanh toán", 1, 4, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell3("1,552,000", 1, 3, Element.ALIGN_RIGHT));

        pdfPTable.addCell(createCell("Số tiền đã trả", 1, 4, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell("1,552,000", 1, 3, Element.ALIGN_RIGHT));

        pdfPTable.addCell(createCell3("Số tiền còn thiếu", 1, 4, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell3("1,552,000", 1, 3, Element.ALIGN_RIGHT));

        //Add content to the document using Table objects.
        document.add(pdfPTable);

        /// footer
        Paragraph p11 = new Paragraph("(P/h thanh toán các khoản thu từ ngày 01 đến ngày 10 hàng tháng)", ky);
        p11.setAlignment(Paragraph.ALIGN_LEFT);
        p11.setSpacingAfter(3f);
        document.add(p11);

        // Tên ngân hàng
        Paragraph p12 = new Paragraph("Ngân hàng: " + schooltest.getSchoolName(), nguoinop);
        p12.setAlignment(Paragraph.ALIGN_LEFT);
        p12.setSpacingAfter(3f);
        document.add(p12);
        // so tai khoan
        Paragraph p13 = new Paragraph("Số tài khoản: " + "109003630352", nguoinop);
        p13.setAlignment(Paragraph.ALIGN_LEFT);
        p13.setSpacingAfter(3f);
        document.add(p13);

        Paragraph p14 = new Paragraph("Chủ tài khoản: " + "Nguyễn Trung Thành", nguoinop);
        p14.setAlignment(Paragraph.ALIGN_LEFT);
        p14.setSpacingAfter(3f);
        document.add(p14);

        Paragraph p15 = new Paragraph("Người nộp tiền", nguoinop);
        p15.setMultipliedLeading(2);
        p15.setTabSettings(new TabSettings(240f));
        p15.setIndentationLeft(30f);
        p15.add(Chunk.TABBING);
        p15.add(new Chunk("Người lập phiếu", nguoinop));
        p15.setTabSettings(new TabSettings(240f));
        p15.add(Chunk.TABBING);
        document.add(p15);

        // trang thứ 2
        document.newPage();
        document.add(new Paragraph("This is Page 2"));
        Font tentre1 = new Font(CROACIA, 11, Font.NORMAL, BaseColor.BLACK);
        // địa chỉ
        Paragraph p52 = new Paragraph("Địa chỉ: " + schooltest.getSchoolAddress(), ft);
        p52.setAlignment(Paragraph.ALIGN_CENTER);
        p52.setSpacingAfter(3f);
        document.add(p52);

        // ten phieu thu
        Paragraph p62 = new Paragraph("PHIẾU CHI CHO PHHS", phieuthu);
        p62.setAlignment(Paragraph.ALIGN_CENTER);
        p62.setSpacingAfter(3f);
        document.add(p62);
        // tên trẻ
        Paragraph p72 = new Paragraph("Trẻ: " + "Nguyễn Huyền Phương Linh", tentre1);
        p72.add(new Chunk(glue));
        p72.add("Mã:" + "HP99-00000001");
        document.add(p72);

        // lớp
        Paragraph p82 = new Paragraph("Lớp: " + "Doraemon", tentre1);
        p82.add(new Chunk(glue));
        p82.add("Quyển số:" + ".....");
        document.add(p82);

        //đt
        Paragraph p92 = new Paragraph("ĐT: " + schooltest.getSchoolPhone(), tentre1);
        p92.add(new Chunk(glue));
        p92.add("Số:" + ".....");
        document.add(p92);

        // ngày
        Paragraph p102 = new Paragraph("" + "", ky);
        p102.add(new Chunk(glue));
        p102.add("Ngày " + LocalDate.now().getDayOfMonth() + " tháng " + LocalDate.now().getMonthValue() + " năm " + LocalDate.now().getYear());
        document.add(p102);

        document.close();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductPdfReport.pdf");
        return new ResponseEntity<>(new InputStreamResource(is), headers, HttpStatus.OK);

    }

    // chi hoc phi hoc sinh
//    @GetMapping("/create/tableout")
//    public ResponseEntity<Resource> generateReportTable(@CurrentUser UserPrincipal principal) throws DocumentException, IOException {
//        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        Font phieuthu = new Font(CROACIA, 11, Font.BOLD);
//        Font stt = new Font(CROACIA, 11, Font.BOLD);
//        Font abc = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9);
//        Font nguoinop = new Font(CROACIA, 10, Font.BOLD);
//        Font ngay = new Font(CROACIA, 11, Font.ITALIC);
//        Font ky = new Font(CROACIA, 9, Font.ITALIC);
//        Font tentre = new Font(CROACIA, 9, Font.BOLD);
//        Font tentre1 = new Font(CROACIA, 11, Font.NORMAL, BaseColor.BLACK);
//        Font ft = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10);
//        Font aa = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 6);
//        //Create Document instance.
//        float[] columnWidths = {1, 5, 5};
//        Document document = new Document(PageSize.A5);
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        PdfWriter.getInstance(document, os);
//        document.setMargins(15f, 15f, 10, 0);
//        //Open the document.
//        document.open();
//        // thông tin trường
//        School schooltest = repository.findByIdAndDelActiveTrue(1L).orElseThrow();
//        Paragraph p4 = new Paragraph(schooltest.getSchoolName(), phieuthu);
//        p4.setAlignment(Paragraph.ALIGN_CENTER);
//        p4.setSpacingAfter(3f);
//        document.add(p4);
//
//        // địa chỉ
//        Paragraph p5 = new Paragraph("Địa chỉ: " + schooltest.getSchoolAddress(), ft);
//        p5.setAlignment(Paragraph.ALIGN_CENTER);
//        p5.setSpacingAfter(3f);
//        document.add(p5);
//
//        // ten phieu thu
//        Paragraph p6 = new Paragraph("PHIẾU CHI CHO PHHS", phieuthu);
//        p6.setAlignment(Paragraph.ALIGN_CENTER);
//        p6.setSpacingAfter(3f);
//        document.add(p6);
//        // tên trẻ
//        Chunk glue = new Chunk(new VerticalPositionMark());
//        Paragraph p7 = new Paragraph("Trẻ: " + "Nguyễn Huyền Phương Linh", tentre1);
//        p7.add(new Chunk(glue));
//        p7.add("Mã:" + "HP99-00000001");
//        document.add(p7);
//
//        // lớp
//        Paragraph p8 = new Paragraph("Lớp: " + "Doraemon", tentre1);
//        p8.add(new Chunk(glue));
//        p8.add("Quyển số:" + ".....");
//        document.add(p8);
//
//        //đt
//        Paragraph p9 = new Paragraph("ĐT: " + schooltest.getSchoolPhone(), tentre1);
//        p9.add(new Chunk(glue));
//        p9.add("Số:" + ".....");
//        document.add(p9);
//
//        // ngày
//        Paragraph p10 = new Paragraph("" + "", ky);
//        p10.add(new Chunk(glue));
//        p10.add("Ngày " + LocalDate.now().getDayOfMonth() + " tháng " + LocalDate.now().getMonthValue() + " năm " + LocalDate.now().getYear());
//        document.add(p10);
//
//        //Ctạo bảng
//        PdfPTable pdfPTable = new PdfPTable(7);
//        pdfPTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        pdfPTable.setWidths(new int[]{3, 20, 3, 9, 9, 9, 9});
//        pdfPTable.setWidthPercentage(100);
//        // cách tren
//        pdfPTable.setSpacingBefore(10f);
//        PdfPCell c2 = new PdfPCell(new Phrase("TT", stt));
//        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
//        c2.setVerticalAlignment(Element.ALIGN_CENTER);
//        c2.setBackgroundColor(BaseColor.GRAY);
//        c2.setFixedHeight(15f);
//        pdfPTable.addCell(c2);
//
//        PdfPCell c3 = new PdfPCell(new Phrase("Tên loại phí", stt));
//        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
//        c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        c3.setBackgroundColor(BaseColor.GRAY);
//        pdfPTable.addCell(c3);
//
//        PdfPCell c4 = new PdfPCell(new Phrase("SL", stt));
//        c4.setBackgroundColor(BaseColor.GRAY);
//        c4.setHorizontalAlignment(Element.ALIGN_CENTER);
//        c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        pdfPTable.addCell(c4);
//
//        PdfPCell c5 = new PdfPCell(new Phrase("Đơn giá", stt));
//        c5.setHorizontalAlignment(Element.ALIGN_CENTER);
//        c5.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        c5.setBackgroundColor(BaseColor.GRAY);
//        pdfPTable.addCell(c5);
//
//        PdfPCell c6 = new PdfPCell(new Phrase("Thành tiền", stt));
//        c6.setBackgroundColor(BaseColor.GRAY);
//        c6.setHorizontalAlignment(Element.ALIGN_CENTER);
//        c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        pdfPTable.addCell(c6);
//
//        PdfPCell c7 = new PdfPCell(new Phrase("Đã thu", stt));
//        c7.setBackgroundColor(BaseColor.GRAY);
//        c7.setHorizontalAlignment(Element.ALIGN_CENTER);
//        c7.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        pdfPTable.addCell(c7);
//
//        PdfPCell c8 = new PdfPCell(new Phrase("Thiếu", stt));
//        c8.setBackgroundColor(BaseColor.GRAY);
//        c8.setHorizontalAlignment(Element.ALIGN_CENTER);
//        c8.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        pdfPTable.addCell(c8);
//        c8.setBorder(Rectangle.NO_BORDER);
//
//        List<Kids> kidsList = kidsRepository.findAll();
//        int index = 0;
//        for (Kids x : kidsList) {
//            index++;
//            PdfPCell id = new PdfPCell(new Phrase(String.valueOf(index), abc));
//            id.setHorizontalAlignment(Element.ALIGN_CENTER);
//            id.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            pdfPTable.addCell(id);
//
//
//            PdfPCell addCell = new PdfPCell(new Phrase(x.getFullName(), abc));
//            pdfPTable.addCell(addCell);
//
//            PdfPCell idth = new PdfPCell(new Phrase(x.getId().toString(), abc));
//            idth.setHorizontalAlignment(Element.ALIGN_CENTER);
//            idth.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            pdfPTable.addCell(idth);
//
//            //todo
//            PdfPCell sotien = new PdfPCell(new Phrase(x.getKidCode(), abc));
//            sotien.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            sotien.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//            PdfPCell dongia = new PdfPCell(new Phrase(x.getKidCode(), abc));
//            sotien.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            sotien.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//            pdfPTable.addCell(sotien);
//            pdfPTable.addCell(dongia);
//            pdfPTable.addCell(dongia);
//            pdfPTable.addCell(dongia);
//        }
//        School school = repository.findByIdAndDelActiveTrue(1L).orElseThrow();
//        pdfPTable.addCell(createCell("Tổng cộng", 1, 4, Element.ALIGN_RIGHT));
//        pdfPTable.addCell(createCell(school.getSchoolPhone(), 1, 1, Element.ALIGN_RIGHT));
//        pdfPTable.addCell(createCell("1,552,000", 1, 1, Element.ALIGN_RIGHT));
//        pdfPTable.addCell(createCell("1,552,000", 1, 1, Element.ALIGN_RIGHT));
//
//
//        pdfPTable.addCell(createCell("Số dư trong ví", 1, 4, Element.ALIGN_RIGHT));
//        pdfPTable.addCell(createCell2("1,552,000", 1, 3, Element.ALIGN_RIGHT));
//
//
//        pdfPTable.addCell(createCell3("Phụ huynh đã nhận", 1, 4, Element.ALIGN_RIGHT));
//        pdfPTable.addCell(createCell3("1,552,000", 1, 3, Element.ALIGN_RIGHT));
//
//
//        pdfPTable.addCell(createCell("Số tiền đã chi", 1, 4, Element.ALIGN_RIGHT));
//        pdfPTable.addCell(createCell("1,552,000", 1, 3, Element.ALIGN_RIGHT));
//
//
//        pdfPTable.addCell(createCell3("Số tiền còn thiếu", 1, 4, Element.ALIGN_RIGHT));
//        pdfPTable.addCell(createCell3("1,552,000", 1, 3, Element.ALIGN_RIGHT));
//
//        //Add content to the document using Table objects.
//        document.add(pdfPTable);
//
//        Paragraph p15 = new Paragraph("Người nhận tiền", nguoinop);
//        p15.setMultipliedLeading(2);
//        p15.setTabSettings(new TabSettings(240f));
//        p15.setIndentationLeft(30f);
//        p15.add(Chunk.TABBING);
//        p15.add(new Chunk("Người lập phiếu", nguoinop));
//        p15.setTabSettings(new TabSettings(240f));
//        p15.add(Chunk.TABBING);
//        document.add(p15);
//
//
//        Paragraph p16 = new Paragraph();
//        p16.setTabSettings(new TabSettings(275f));
//        p16.add(new Chunk("Nguyễn Trung Thành", nguoinop));
//        p16.setIndentationLeft(270f);
//        p16.setMultipliedLeading(4);
//        document.add(p16);
//
//        // đóng file
//        document.close();
//
//
//        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType("application/pdf"));
//        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductPdfReport.pdf");
//        return new ResponseEntity<>(new InputStreamResource(is), headers, HttpStatus.OK);
//
//    }

    // phiếu thanh toán lương
    @Deprecated
    @GetMapping("/create/salary")
    public ResponseEntity<Resource> createPdfSalary(@CurrentUser UserPrincipal principal) throws DocumentException, IOException {
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font phieuthu = new Font(CROACIA, 11, Font.BOLD);
        Font stt = new Font(CROACIA, 11, Font.BOLD);
        Font abc = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9);
        Font nguoinop = new Font(CROACIA, 10, Font.BOLD);
        Font ngay = new Font(CROACIA, 11, Font.ITALIC);
        Font ky = new Font(CROACIA, 9, Font.ITALIC);
        Font tentre = new Font(CROACIA, 9, Font.BOLD);
        Font tentre1 = new Font(CROACIA, 11, Font.NORMAL, BaseColor.BLACK);
        Font ft = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9);
        Font aa = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 6);
        //Create Document instance.
        float[] columnWidths = {1, 5, 5};
        Document document = new Document(PageSize.A5);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, os);
        document.setMargins(15f, 15f, 10, 0);
        //Open the document.
        document.open();
        // thông tin trường
        School schooltest = repository.findByIdAndDelActiveTrue(1L).orElseThrow();
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
        Paragraph p6 = new Paragraph("PHIẾU THANH TOÁN LƯƠNG - T6", phieuthu);
        p6.setAlignment(Paragraph.ALIGN_CENTER);
        p6.setSpacingAfter(3f);
        document.add(p6);
        // tên trẻ
        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p7 = new Paragraph("NV: " + "Nguyễn Huyền Phương Linh", tentre1);
        p7.add(new Chunk(glue));
        p7.add("Mã:" + "HP99-00000001");
        document.add(p7);

        // lớp
        Paragraph p8 = new Paragraph("ĐT: " + "0982642610", tentre1);
        p8.add(new Chunk(glue));
        p8.add("Quyển số:" + ".....");
        document.add(p8);

        //đt
        Paragraph p9 = new Paragraph(" ", tentre1);
        p9.add(new Chunk(glue));
        p9.add("Số:" + ".....");
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

        PdfPCell c7 = new PdfPCell(new Phrase("Đã thu", stt));
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

        List<Kids> kidsList = kidsRepository.findAll();
        int index = 0;
        for (Kids x : kidsList) {
            index++;
            PdfPCell id = new PdfPCell(new Phrase(String.valueOf(index), abc));
            id.setHorizontalAlignment(Element.ALIGN_CENTER);
            id.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(id);


            PdfPCell addCell = new PdfPCell(new Phrase(x.getFullName(), abc));
            pdfPTable.addCell(addCell);

            // đơn vị
            PdfPCell dv = new PdfPCell(new Phrase("Tháng", abc));
            pdfPTable.addCell(dv);


            PdfPCell idth = new PdfPCell(new Phrase(x.getId().toString(), abc));
            idth.setHorizontalAlignment(Element.ALIGN_CENTER);
            idth.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(idth);

            //todo
            PdfPCell sotien = new PdfPCell(new Phrase(x.getKidCode(), abc));
            sotien.setHorizontalAlignment(Element.ALIGN_RIGHT);
            sotien.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell dongia = new PdfPCell(new Phrase(x.getKidCode(), abc));
            sotien.setHorizontalAlignment(Element.ALIGN_RIGHT);
            sotien.setVerticalAlignment(Element.ALIGN_MIDDLE);

            pdfPTable.addCell(sotien);
            pdfPTable.addCell(dongia);
            pdfPTable.addCell(dongia);
            pdfPTable.addCell(dongia);
        }
        School school = repository.findByIdAndDelActiveTrue(1L).orElseThrow();
        pdfPTable.addCell(createCell("Tổng cộng", 1, 5, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell(school.getSchoolPhone(), 1, 1, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell("1,552,000", 1, 1, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell("1,552,000", 1, 1, Element.ALIGN_RIGHT));


        pdfPTable.addCell(createCell("Nhà trường đã thanh toán", 1, 5, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell2("1,552,000", 1, 3, Element.ALIGN_RIGHT));


        pdfPTable.addCell(createCell3("Số tiền còn thiếu", 1, 5, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell3("1,552,000", 1, 3, Element.ALIGN_RIGHT));

        //Add content to the document using Table objects.
        document.add(pdfPTable);

        /// footer

        Paragraph p11 = new Paragraph("(P/h thanh toán các khoản thu từ ngày 01 đến ngày 10 hàng tháng)", ky);
        p11.setAlignment(Paragraph.ALIGN_LEFT);
        p11.setSpacingAfter(3f);
        document.add(p11);

        // Tên ngân hàng
//        // địa chỉ
//        Paragraph p12 = new Paragraph("Ngân hàng: " + schooltest.getSchoolName(), nguoinop);
//        p12.setAlignment(Paragraph.ALIGN_LEFT);
//        p12.setSpacingAfter(3f);
//        document.add(p12);
//        // so tai khoan
//        // địa chỉ
//        Paragraph p13 = new Paragraph("Số tài khoản: " + "109003630352", nguoinop);
//        p13.setAlignment(Paragraph.ALIGN_LEFT);
//        p13.setSpacingAfter(3f);
//        document.add(p13);
//
//        Paragraph p14 = new Paragraph("Chủ tài khoản: " + "Nguyễn Trung Thành", nguoinop);
//        p14.setAlignment(Paragraph.ALIGN_LEFT);
//        p14.setSpacingAfter(3f);
//        document.add(p14);

        Paragraph p15 = new Paragraph("Người lĩnh", nguoinop);
        p15.setMultipliedLeading(2);
        p15.setTabSettings(new TabSettings(240f));
        p15.setIndentationLeft(30f);
        p15.add(Chunk.TABBING);
        p15.add(new Chunk("Người lập phiếu", nguoinop));
        p15.setTabSettings(new TabSettings(240f));
        p15.add(Chunk.TABBING);
        document.add(p15);


        Paragraph p16 = new Paragraph();
        p16.setTabSettings(new TabSettings(275f));
        p16.add(new Chunk("Nguyễn Trung Thành", nguoinop));
        p16.setIndentationLeft(270f);
        p16.setMultipliedLeading(4);
        document.add(p16);

        // đóng file
        document.close();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductPdfReport.pdf");
        return new ResponseEntity<>(new InputStreamResource(is), headers, HttpStatus.OK);
    }

    // phiếu chi cho nhân viên
    @GetMapping("/create/salaryout")
    public ResponseEntity<Resource> createPdfSalaryout(@CurrentUser UserPrincipal principal) throws DocumentException, IOException {
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font phieuthu = new Font(CROACIA, 11, Font.BOLD);
        Font stt = new Font(CROACIA, 11, Font.BOLD);
        Font abc = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9);
        Font nguoinop = new Font(CROACIA, 10, Font.BOLD);
        Font ngay = new Font(CROACIA, 11, Font.ITALIC);
        Font ky = new Font(CROACIA, 9, Font.ITALIC);
        Font tentre = new Font(CROACIA, 9, Font.BOLD);
        Font tentre1 = new Font(CROACIA, 11, Font.NORMAL, BaseColor.BLACK);
        Font ft = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9);
        Font aa = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 6);
        //Create Document instance.
        float[] columnWidths = {1, 5, 5};
        Document document = new Document(PageSize.A5);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, os);
        document.setMargins(15f, 15f, 10, 0);
        //Open the document.
        document.open();
        // thông tin trường
        School schooltest = repository.findByIdAndDelActiveTrue(1L).orElseThrow();
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
        Paragraph p6 = new Paragraph("PHIẾU THU TIỀN NHÂN VIÊN - T6/2021", phieuthu);
        p6.setAlignment(Paragraph.ALIGN_CENTER);
        p6.setSpacingAfter(3f);
        document.add(p6);
        // tên trẻ
        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p7 = new Paragraph("NV: " + "Nguyễn Huyền Phương Linh", tentre1);
        p7.add(new Chunk(glue));
        p7.add("Mã:" + "HP99-00000001");
        document.add(p7);

        // lớp
        Paragraph p8 = new Paragraph("ĐT: " + "0982642610", tentre1);
        p8.add(new Chunk(glue));
        p8.add("Quyển số:" + ".....");
        document.add(p8);

        //đt
        Paragraph p9 = new Paragraph(" ", tentre1);
        p9.add(new Chunk(glue));
        p9.add("Số:" + ".....");
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

        PdfPCell c7 = new PdfPCell(new Phrase("Đã thu", stt));
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

        List<Kids> kidsList = kidsRepository.findAll();
        int index = 0;
        for (Kids x : kidsList) {
            index++;
            PdfPCell id = new PdfPCell(new Phrase(String.valueOf(index), abc));
            id.setHorizontalAlignment(Element.ALIGN_CENTER);
            id.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(id);


            PdfPCell addCell = new PdfPCell(new Phrase(x.getFullName(), abc));
            pdfPTable.addCell(addCell);

            // đơn vị
            PdfPCell dv = new PdfPCell(new Phrase("Tháng", abc));
            pdfPTable.addCell(dv);


            PdfPCell idth = new PdfPCell(new Phrase(x.getId().toString(), abc));
            idth.setHorizontalAlignment(Element.ALIGN_CENTER);
            idth.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(idth);

            //todo
            PdfPCell sotien = new PdfPCell(new Phrase(x.getKidCode(), abc));
            sotien.setHorizontalAlignment(Element.ALIGN_RIGHT);
            sotien.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell dongia = new PdfPCell(new Phrase(x.getKidCode(), abc));
            sotien.setHorizontalAlignment(Element.ALIGN_RIGHT);
            sotien.setVerticalAlignment(Element.ALIGN_MIDDLE);

            pdfPTable.addCell(sotien);
            pdfPTable.addCell(dongia);
            pdfPTable.addCell(dongia);
            pdfPTable.addCell(dongia);
        }
        School school = repository.findByIdAndDelActiveTrue(1L).orElseThrow();
        pdfPTable.addCell(createCell("Tổng cộng", 1, 5, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell(school.getSchoolPhone(), 1, 1, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell("1,552,000", 1, 1, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell("1,552,000", 1, 1, Element.ALIGN_RIGHT));


        pdfPTable.addCell(createCell("Nhà trường đã thu", 1, 5, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell2("1,552,000", 1, 3, Element.ALIGN_RIGHT));


        pdfPTable.addCell(createCell3("Số tiền còn thiếu", 1, 5, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell3("1,552,000", 1, 3, Element.ALIGN_RIGHT));

        //Add content to the document using Table objects.
        document.add(pdfPTable);

        /// footer

        Paragraph p11 = new Paragraph("Ghi chú: nội dung ghi chú", ky);
        p11.setAlignment(Paragraph.ALIGN_LEFT);
        p11.setSpacingAfter(3f);
        document.add(p11);


        Paragraph p15 = new Paragraph("Người nộp", nguoinop);
        p15.setMultipliedLeading(2);
        p15.setTabSettings(new TabSettings(240f));
        p15.setIndentationLeft(30f);
        p15.add(Chunk.TABBING);
        p15.add(new Chunk("Người lập phiếu", nguoinop));
        p15.setTabSettings(new TabSettings(240f));
        p15.add(Chunk.TABBING);
        document.add(p15);


        Paragraph p16 = new Paragraph();
        p16.setTabSettings(new TabSettings(275f));
        p16.add(new Chunk("Nguyễn Trung Thành", nguoinop));
        p16.setIndentationLeft(270f);
        p16.setMultipliedLeading(4);
        document.add(p16);

        // đóng file
        document.close();


        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductPdfReport.pdf");
        return new ResponseEntity<>(new InputStreamResource(is), headers, HttpStatus.OK);

    }

    public PdfPCell createCell(String content, float borderWidth, int colspan, int alignment) throws IOException, DocumentException {
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font stt = new Font(CROACIA, 9, Font.BOLD, BaseColor.BLACK);
        //Create Document instance.
        PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(content), stt));
        cell.setBorderWidth(borderWidth);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    public PdfPCell createCell2(String content, float borderWidth, int colspan, int alignment) throws IOException, DocumentException {
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font stt = new Font(CROACIA, 9, Font.NORMAL, BaseColor.BLUE);
        //Create Document instance.
        PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(content), stt));
        cell.setBorderWidth(borderWidth);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    public PdfPCell createCell3(String content, float borderWidth, int colspan, int alignment) throws IOException, DocumentException {
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font stt = new Font(CROACIA, 9, Font.NORMAL, BaseColor.RED);
        //Create Document instance.
        PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(content), stt));
        cell.setBorderWidth(borderWidth);
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    @GetMapping("/kids/order111/{idOrder}")
    public ResponseEntity createKidOrder(@CurrentUser UserPrincipal principal, @PathVariable Long idOrder, @RequestParam List<Long> idList) throws DocumentException, IOException {
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font phieuthu = new Font(CROACIA, 11, Font.BOLD);
        Font stt = new Font(CROACIA, 11, Font.BOLD);
        Font abc = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9);
        Font nguoinop = new Font(CROACIA, 10, Font.BOLD);
        Font ky = new Font(CROACIA, 9, Font.ITALIC);
        Font tentre1 = new Font(CROACIA, 11, Font.NORMAL, BaseColor.BLACK);
        Font ft = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10);
        // tạo trang giấy kichcs thước a5
        Document document = new Document(PageSize.A5);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, os);
        document.setMargins(15f, 15f, 10, 0);
        //Open the document.
        document.open();

        // thông tin trường
        Long idSchool = principal.getIdSchoolLogin();
        School school = repository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
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
        Paragraph p6 = new Paragraph("PHIẾU THU HỌC PHÍ THÁNG " + this.getOrderKids(idSchool, idOrder, idList).getMonth(), phieuthu);
        p6.setAlignment(Paragraph.ALIGN_CENTER);
        p6.setSpacingAfter(3f);
        document.add(p6);
        // tên trẻ
        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p7 = new Paragraph("Trẻ: " + this.getOrderKids(idSchool, idOrder, idList).getKidName(), tentre1);
        p7.add(new Chunk(glue));
        p7.add("Mã:" + "0001");
        document.add(p7);

        // lớp
        Paragraph p8 = new Paragraph("Lớp: " + this.getOrderKids(idSchool, idOrder, idList).getClassName(), tentre1);
        p8.add(new Chunk(glue));
        p8.add("Quyển số:" + ".....");
        document.add(p8);

        //đt
        Paragraph p9 = new Paragraph("ĐT: " + school.getSchoolPhone(), tentre1);
        p9.add(new Chunk(glue));
        p9.add("Số:" + ".....");
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
        c2.setBackgroundColor(BaseColor.GRAY);
        pdfPTable.addCell(c2);

        PdfPCell c3 = new PdfPCell(new Phrase("Tên loại phí", stt));
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c3.setBackgroundColor(BaseColor.GRAY);
        pdfPTable.addCell(c3);

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

        PdfPCell c7 = new PdfPCell(new Phrase("Đã thu", stt));
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


            // đơn giá
            double d = x.getPrice();
            double thanhtien = x.getMoney();
            double dathu = x.getPaid();
            double thieu = x.getRemain();
            BigDecimal numBigDecimal = new BigDecimal(d, MathContext.DECIMAL64);
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            String a = formatter.format(d);
            String thanhtien1 = formatter.format(thanhtien);
            String dathu1 = formatter.format(dathu);
            String thieu1 = formatter.format(thieu);


            PdfPCell sotien = new PdfPCell(new Phrase(a, abc));
            sotien.setHorizontalAlignment(Element.ALIGN_RIGHT);
            sotien.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfPTable.addCell(a);

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
        String tongcongt = formatter.format(tongcong);
        String dathut = formatter.format(dathu);
        String thieut = formatter.format(thieu);
        String sodut = formatter.format(sodu);
        pdfPTable.addCell(createCell("Tổng cộng", 1, 4, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell(tongcongt, 1, 1, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell(dathut, 1, 1, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell(thieut, 1, 1, Element.ALIGN_RIGHT));

        pdfPTable.addCell(createCell("Số dư trong ví", 1, 4, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell2(sodut, 1, 3, Element.ALIGN_RIGHT));

        pdfPTable.addCell(createCell3("Phụ huynh phải thanh toán", 1, 4, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell3(thieut, 1, 3, Element.ALIGN_RIGHT));

        pdfPTable.addCell(createCell("Số tiền đã trả", 1, 4, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell(dathut, 1, 3, Element.ALIGN_RIGHT));

        pdfPTable.addCell(createCell3("Số tiền còn thiếu", 1, 4, Element.ALIGN_RIGHT));
        pdfPTable.addCell(createCell3(thieut, 1, 3, Element.ALIGN_RIGHT));

        //Add content to the document using Table objects.
        document.add(pdfPTable);

        /// footer
        Paragraph p11 = new Paragraph("(P/h thanh toán các khoản thu từ ngày 01 đến ngày 10 hàng tháng)", ky);
        p11.setAlignment(Paragraph.ALIGN_LEFT);
        p11.setSpacingAfter(3f);
        document.add(p11);


        // thông tin ngân hàng chính
        FnBank fnBank = fnBankRepository.findBySchoolIdAndCheckedTrueAndDelActiveTrue(idSchool).orElseThrow();
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

        Paragraph p15 = new Paragraph("Người nộp tiền", nguoinop);
        p15.setMultipliedLeading(2);
        p15.setTabSettings(new TabSettings(240f));
        p15.setIndentationLeft(30f);
        p15.add(Chunk.TABBING);
        p15.add(new Chunk("Người lập phiếu", nguoinop));
        p15.setTabSettings(new TabSettings(240f));
        p15.add(Chunk.TABBING);
        document.add(p15);

        Paragraph p16 = new Paragraph();
        p16.setTabSettings(new TabSettings(270f));
        p16.setIndentationLeft(270f);
        p16.setMultipliedLeading(3);
        document.add(p16);

        document.close();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductPdfReport.pdf");
        return new ResponseEntity<>(new InputStreamResource(is), headers, HttpStatus.OK);

//        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType("application/pdf"));
//        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductPdfReport.pdf");
//        return new ResponseEntity<>(new InputStreamResource(is), headers, HttpStatus.OK);
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
}
