package com.example.onekids_project.service.serviceimpl.cashinternal;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.common.FirebaseConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.CashInternal.FnCashInternal;
import com.example.onekids_project.entity.finance.CashInternal.FnPeopleType;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.FnCashInternalSchoolRepository;
import com.example.onekids_project.repository.PeopleTypeRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.repository.WebSystemTitleRepository;
import com.example.onekids_project.request.cashinternal.*;
import com.example.onekids_project.request.common.StatusListRequest;
import com.example.onekids_project.response.caskinternal.CashInternalinResponse;
import com.example.onekids_project.response.caskinternal.ListCashInternalinResponse;
import com.example.onekids_project.response.caskinternal.PeopleTypeOtherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.cashbook.CashBookHistoryService;
import com.example.onekids_project.service.servicecustom.cashinternal.CashInternalService;
import com.example.onekids_project.util.SchoolUtils;
import com.example.onekids_project.util.UserInforUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CashInternalServiceImpl implements CashInternalService {
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PeopleTypeRepository peopleTypeRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private FnCashInternalSchoolRepository fnCashInternalSchoolRepository;
    @Autowired
    private CashBookHistoryService cashBookHistoryService;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Override
    public ListCashInternalinResponse searchListCashOut(UserPrincipal principal, SeacrhListpayRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListCashInternalinResponse response = new ListCashInternalinResponse();
        List<FnCashInternal> fnCashInternalList = fnCashInternalSchoolRepository.searchInternalPay(idSchool, request);
        long total = fnCashInternalSchoolRepository.countSearchInternalPay(idSchool, request);
        List<CashInternalinResponse> responseList = listMapper.mapList(fnCashInternalList, CashInternalinResponse.class);
        responseList.forEach(x -> x.setLocked(x.isApproved() && x.isPayment() ? AppConstant.APP_TRUE : AppConstant.APP_FALSE));
        double a = responseList.stream().mapToDouble(CashInternalinResponse::getMoney).sum();
        response.setTotalMoney(a);
        response.setTotal(total);
        response.setResponseList(responseList);
        return response;
    }

    @Override
    public double getCashOutTotal(UserPrincipal principal, SearchPayDateMonth request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<FnCashInternal> fnCashInternalList = fnCashInternalSchoolRepository.getInternalPayTotal(idSchool, request);
        return fnCashInternalList.stream().filter(x -> x.getCategory().equals(request.getCategory())).mapToDouble(FnCashInternal::getMoney).sum();
    }

    @Transactional
    @Override
    public boolean createCashInternal(UserPrincipal principal, CreateCashInternalRequest request) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        FnCashInternal newData = modelMapper.map(request, FnCashInternal.class);
        List<FnCashInternal> cashInternalList = fnCashInternalSchoolRepository.searchByIdSchoolandCategory(idSchool);
        newData.setCode(this.getCode(AppConstant.CASHINTERNALOUT, idSchool, cashInternalList));
        newData.setCreatedBy(principal.getFullName());
        if (request.isPayment()) {
            newData.setIdPayment(principal.getId());
            newData.setTimePayment(LocalDateTime.now());
            newData.setPayment(AppConstant.APP_TRUE);
            newData.setIdApproved(principal.getId());
            newData.setTimeApproved(LocalDateTime.now());
            newData.setApproved(AppConstant.APP_TRUE);
            CashBookHistory cashBookHistory = cashBookHistoryService.saveCashBookHistory(idSchool, FinanceConstant.CATEGORY_OUT, FinanceConstant.CASH_BOOK_SCH, request.getDate(), request.getMoney(), FinanceConstant.CASH_BOOK_ORIGIN_SCHOOL_OUT, null);
            newData.setCashBookHistory(cashBookHistory);
        }
        newData.setIdSchool(idSchool);
        newData.setCategory(FinanceConstant.CATEGORY_OUT);
        FnPeopleType fnPeopleType = peopleTypeRepository.findByIdAndDelActiveTrue(request.getIdPeopleTypeInternal()).orElseThrow();
        newData.setFnPeopleTypeInternal(fnPeopleType);
        FnPeopleType fnPeopleTypeOther = peopleTypeRepository.findByIdAndDelActiveTrue(request.getIdPeopleTypeOther()).orElseThrow();
        newData.setFnPeopleTypeOther(fnPeopleTypeOther);
        fnCashInternalSchoolRepository.save(newData);
        //send firebase
        this.sendFirebaseForPlus(82L, request.getContent());
        return true;
    }

    @Override
    public boolean updateManyApproved(UserPrincipal principal, StatusListRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idUser = principal.getId();
        Long idSchool = principal.getIdSchoolLogin();
        request.getIdList().forEach(x -> {
            FnCashInternal fnCashInternal = fnCashInternalSchoolRepository.findByIdAndIdSchoolAndDelActiveTrue(x, idSchool).orElseThrow();
            if (!fnCashInternal.isCanceled() && !fnCashInternal.isPayment() && fnCashInternal.isApproved() != request.getStatus()) {
                this.approveCashOne(fnCashInternal, request.getStatus(), idUser);
            }
        });
        return true;
    }

    @Override
    public boolean cancelMany(UserPrincipal principal, StatusListRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        request.getIdList().forEach(x -> {
            FnCashInternal fnCashInternal = fnCashInternalSchoolRepository.findByIdAndIdSchoolAndDelActiveTrue(x, idSchool).orElseThrow();
            if (!fnCashInternal.isCanceled() && !fnCashInternal.isApproved() && !fnCashInternal.isPayment()) {
                fnCashInternal.setCanceled(AppConstant.APP_TRUE);
                fnCashInternalSchoolRepository.save(fnCashInternal);
            }
        });
        return true;
    }

    @Override
    public int updateManyUnApproved(UserPrincipal principal, List<UpdateApproveCashRequest> request, Long id) {
        CommonValidate.checkDataPlus(principal);
        AtomicInteger coutSuccess = new AtomicInteger();
        request.forEach(x -> {
            Optional<FnCashInternal> fnCashInternalOptional = fnCashInternalSchoolRepository.findByIdAndDelActiveTrue(x.getId());
            if (fnCashInternalOptional.isPresent() && !fnCashInternalOptional.get().isCanceled() && !fnCashInternalOptional.get().isPayment()) {
                FnCashInternal fnCashInternal = fnCashInternalOptional.get();
                if (!fnCashInternal.isPayment()) {
                    fnCashInternal.setApproved(AppConstant.APP_FALSE);
                }
                fnCashInternalSchoolRepository.save(fnCashInternal);
                coutSuccess.getAndIncrement();
            }
        });
        return coutSuccess.get();
    }


    @Override
    public int updateManyCancel(UserPrincipal principal, List<UpdateActiveCashRequest> request, Long id) {
        CommonValidate.checkDataPlus(principal);
        AtomicInteger coutSuccess = new AtomicInteger();
        request.forEach(x -> {
            Optional<FnCashInternal> fnCashInternalOptional = fnCashInternalSchoolRepository.findByIdAndDelActiveTrue(x.getId());
            if (fnCashInternalOptional.isPresent()) {
                FnCashInternal fnCashInternal = fnCashInternalOptional.get();
                if (!fnCashInternal.isPayment() && !fnCashInternal.isCanceled()) {
                    fnCashInternal.setCanceled(AppConstant.APP_TRUE);
                    coutSuccess.getAndIncrement();
                }
                fnCashInternalSchoolRepository.save(fnCashInternal);

            }
        });
        return coutSuccess.get();

    }

    @Override
    public boolean approveCash(UserPrincipal principal, Long id, boolean status) {
        CommonValidate.checkDataPlus(principal);
        FnCashInternal fnCashInternal = fnCashInternalSchoolRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        this.approveCashOne(fnCashInternal, status, principal.getId());
        return true;
    }

    private void approveCashOne(FnCashInternal fnCashInternal, boolean status, Long idUser) {
        fnCashInternal.setApproved(status);
        fnCashInternal.setIdApproved(idUser);
        fnCashInternal.setTimeApproved(LocalDateTime.now());
        fnCashInternalSchoolRepository.save(fnCashInternal);
    }

    @Override
    public CashInternalinResponse findDeTailCashInternal(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        FnCashInternal fnCashInternal = fnCashInternalSchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        CashInternalinResponse response = new CashInternalinResponse();
        List<FnPeopleType> peopleTypeList = peopleTypeRepository.searchPeopleTypeOther(idSchool);
        List<PeopleTypeOtherResponse> listAllPeopleType = listMapper.mapList(peopleTypeList, PeopleTypeOtherResponse.class);
        List<PeopleTypeOtherResponse> internalList = listAllPeopleType.stream().filter(a -> a.getType().equals(FinanceConstant.INTERNAL)).collect(Collectors.toList());
        FnPeopleType fnPeopleType = peopleTypeRepository.findByIdAndDelActiveTrue(fnCashInternal.getFnPeopleTypeInternal().getId()).orElseThrow();
        response.setNameInternal(fnPeopleType.getName());
        FnPeopleType fnPeopleType1 = peopleTypeRepository.findByIdAndDelActiveTrue(fnCashInternal.getFnPeopleTypeOther().getId()).orElseThrow();
        response.setNameOther(fnPeopleType1.getName());
        response.setPeopleTypeInternalList(internalList);
        response.setCode(fnCashInternal.getCode());
        response.setPeopleTypeOtherList(listAllPeopleType);
        response.setMoney(fnCashInternal.getMoney());
        response.setDate(fnCashInternal.getDate());
        response.setContent(fnCashInternal.getContent());
        response.setId(fnCashInternal.getId());
        response.setIdPeopleTypeInternal(fnCashInternal.getFnPeopleTypeInternal().getId());
        response.setIdPeopleTypeOther(fnCashInternal.getFnPeopleTypeOther().getId());
        response.setPayment(fnCashInternal.isPayment());
        response.setApproved(fnCashInternal.isApproved());
        response.setCategory(fnCashInternal.getCategory());
        return response;
    }

    @Override
    public boolean updateCashInternal(UserPrincipal principal, UpdateCashinternalInRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnCashInternal fnCashInternal = fnCashInternalSchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        double moneyOld = fnCashInternal.getMoney();
        modelMapper.map(request, fnCashInternal);
        fnCashInternal.setMoney(fnCashInternal.isApproved() ? moneyOld : request.getMoney());
        fnCashInternal.setCode(fnCashInternal.getCode());
        FnPeopleType fnPeopleType = peopleTypeRepository.findByIdAndDelActiveTrue(request.getIdPeopleTypeInternal()).orElseThrow();
        fnCashInternal.setFnPeopleTypeInternal(fnPeopleType);
        FnPeopleType fnPeopleTypeOther = peopleTypeRepository.findByIdAndDelActiveTrue(request.getIdPeopleTypeOther()).orElseThrow();
        fnCashInternal.setFnPeopleTypeOther(fnPeopleTypeOther);
        fnCashInternalSchoolRepository.save(fnCashInternal);
        return true;
    }

    @Override
    public ListCashInternalinResponse searchListCashIn(UserPrincipal principal, SeacrhListpayRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListCashInternalinResponse response = new ListCashInternalinResponse();
        List<FnCashInternal> fnCashInternalList = fnCashInternalSchoolRepository.searchcollectCash(idSchool, request);
        long total = fnCashInternalSchoolRepository.countSearchCollectCash(idSchool, request);
        List<CashInternalinResponse> responseList = listMapper.mapList(fnCashInternalList, CashInternalinResponse.class);
        responseList.forEach(x -> x.setLocked(x.isApproved() && x.isPayment() ? AppConstant.APP_TRUE : AppConstant.APP_FALSE));
        response.setTotal(total);
        response.setResponseList(responseList);
        return response;
    }

    // phieu thu
    @Transactional
    @Override
    public boolean createCashCollect(UserPrincipal principal, CreateCashInternalRequest request) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        FnCashInternal newData = modelMapper.map(request, FnCashInternal.class);
        List<FnCashInternal> cashInternalList = fnCashInternalSchoolRepository.searchByIdSchoolandCategoryin(idSchool);
        newData.setCreatedBy(principal.getFullName());
        if (request.isPayment()) {
            newData.setIdPayment(principal.getId());
            newData.setTimePayment(LocalDateTime.now());
            newData.setPayment(AppConstant.APP_TRUE);
            newData.setIdApproved(principal.getId());
            newData.setTimeApproved(LocalDateTime.now());
            newData.setApproved(AppConstant.APP_TRUE);
            CashBookHistory cashBookHistory = cashBookHistoryService.saveCashBookHistory(idSchool, FinanceConstant.CATEGORY_IN, FinanceConstant.CASH_BOOK_SCH, request.getDate(), request.getMoney(), FinanceConstant.CASH_BOOK_ORIGIN_SCHOOL_IN, null);
            newData.setCashBookHistory(cashBookHistory);
        }
        newData.setCode(this.getCode(AppConstant.CASHINTERNALIN, idSchool, cashInternalList));
        newData.setIdSchool(principal.getIdSchoolLogin());
        newData.setCategory(FinanceConstant.CATEGORY_IN);
        FnPeopleType fnPeopleType = peopleTypeRepository.findByIdAndDelActiveTrue(request.getIdPeopleTypeInternal()).orElseThrow();
        newData.setFnPeopleTypeInternal(fnPeopleType);
        FnPeopleType fnPeopleTypeOther = peopleTypeRepository.findByIdAndDelActiveTrue(request.getIdPeopleTypeOther()).orElseThrow();
        newData.setFnPeopleTypeOther(fnPeopleTypeOther);
        fnCashInternalSchoolRepository.save(newData);
        //send firebase
        this.sendFirebaseForPlus(81L, request.getContent());
        return true;
    }

    @Transactional
    @Override
    public boolean paymentCash(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnCashInternal fnCashInternal = fnCashInternalSchoolRepository.findByIdAndPaymentFalseAndDelActiveTrue(id).orElseThrow();
        if (fnCashInternal.isApproved()) {
            this.paymentCashOne(principal, fnCashInternal);
        }
        return true;
    }


    private boolean paymentCashOne(UserPrincipal principal, FnCashInternal fnCashInternal) {
        Long idSchool = principal.getIdSchoolLogin();
        fnCashInternal.setPayment(AppConstant.APP_TRUE);
        fnCashInternal.setTimePayment(LocalDateTime.now());
        fnCashInternal.setIdPayment(principal.getId());
        CashBookHistory cashBookHistory = cashBookHistoryService.saveCashBookHistory(idSchool, fnCashInternal.getCategory(), FinanceConstant.CASH_BOOK_SCH, fnCashInternal.getDate(), fnCashInternal.getMoney(), FinanceConstant.CASH_BOOK_ORIGIN_SCHOOL_PAYMENT, null);
        fnCashInternal.setCashBookHistory(cashBookHistory);
        fnCashInternalSchoolRepository.save(fnCashInternal);
        return true;

    }

    @Override
    public ResponseEntity<Resource> printpdf(UserPrincipal principal, Long id) throws IOException, DocumentException {
        Long idSchool = principal.getIdSchoolLogin();
        FnCashInternal fnCashInternal = fnCashInternalSchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        BaseFont CROACIA = BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font phieuthu = new Font(CROACIA, 13, Font.BOLD);
        Font nguoinop = new Font(CROACIA, 11, Font.BOLD);
        Font ngay = new Font(CROACIA, 11, Font.ITALIC);
        Font ky = new Font(CROACIA, 10, Font.ITALIC);
        Font ft = new Font(BaseFont.createFont(AppConstant.URL_FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 13);
        Document document = new Document(PageSize.A5.rotate(), 10, 10, 10, 10);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, os);

        document.open();
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();

        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p = new Paragraph("Tên trường: " + school.getSchoolName(), ft);
        p.add(new Chunk(glue));
        p.add("Quyển số:......");
        document.add(p);

        Paragraph p1 = new Paragraph("ĐC: " + school.getSchoolAddress(), ft);
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


        Paragraph p4 = new Paragraph("PHIẾU THU", phieuthu);
        p4.setAlignment(Paragraph.ALIGN_CENTER);
        p4.setLeading(10f);
        p4.setMultipliedLeading(1);
        document.add(p4);

        Paragraph p5 = new Paragraph("Ngày.." + LocalDate.now().getDayOfMonth() + "..Tháng.." + LocalDate.now().getMonthValue() + "..Năm.." + LocalDate.now().getYear(), ngay);
        p5.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p5);
        p5.setMultipliedLeading(2);

        FnPeopleType fnPeopleType = peopleTypeRepository.findByIdAndDelActiveTrue(fnCashInternal.getFnPeopleTypeOther().getId()).orElseThrow();
        Paragraph p6 = new Paragraph("Họ và tên người nộp:       " + fnPeopleType.getName(), ft);
        p6.setMultipliedLeading(2);
        document.add(p6);

        Paragraph p7 = new Paragraph("Địa chỉ: " + fnPeopleType.getAddress(), ft);
        document.add(p7);

        Paragraph p8 = new Paragraph("Số điện thoại: " + fnPeopleType.getPhone(), ft);
        document.add(p8);

        Paragraph p9 = new Paragraph("Nội dung nộp tiền: " + fnCashInternal.getContent(), ft);
        document.add(p9);


        Paragraph p11 = new Paragraph("Số tiền:  " + fnCashInternal.getMoney(), ft);
        document.add(p11);


        Paragraph p12 = new Paragraph("Bằng chữ:  " + "Một trăm năm mươi triệu đồng", ft);
        document.add(p12);

        Paragraph p13 = new Paragraph("Kèm theo:" + "........................................................................................................................." + "chứng từ gốc", ft);
        document.add(p13);

//        PdfPTable table = new PdfPTable(5);
//        table.getHeader();
//        table.getHeader().getRole();
//        table.getBody();
//        table.getFooter();
//        table.setSpacingBefore(25);
//        table.setSpacingAfter(25);
//        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
//
//        PdfPCell c1 = new PdfPCell(new Phrase("Id"));
//        table.addCell(c1);
//        c1.getMaxHeight();
//        c1.setBorder(Rectangle.NO_BORDER);
//        PdfPCell c2 = new PdfPCell(new Phrase("Tên"));
//        table.addCell(c2);
//        c2.setBorder(Rectangle.NO_BORDER);
//        PdfPCell c3 = new PdfPCell(new Phrase("Đia chỉ"));
//        table.addCell(c3);
//        c3.setBorder(Rectangle.NO_BORDER);
//
//
//        PdfPCell c4 = new PdfPCell(new Phrase("So dien thoai"));
//        table.addCell(c4);
//        c4.setBorder(Rectangle.NO_BORDER);
//
//        PdfPCell c5 = new PdfPCell(new Phrase("Mo ta"));
//        table.addCell(c5);
//        c5.setBorder(Rectangle.NO_BORDER);
//
//        for (School product : products) {
//            table.addCell(String.valueOf(product.getId()));
//            table.addCell(product.getSchoolName());
//            table.addCell(String.valueOf(product.getSchoolAddress()));
//            table.addCell(String.valueOf(product.getSchoolPhone()));
//            table.addCell(String.valueOf(product.getSchoolDescription()));
//        }
//        document.add(table);
        Paragraph p14 = new Paragraph("Người nộp tiền", nguoinop);
        p14.setMultipliedLeading(2);
        p14.setTabSettings(new TabSettings(120f));
        // cach trai - margin-left
        p14.setIndentationLeft(10f);
        p14.add(Chunk.TABBING);

        p14.add(new Chunk("Người lập phiếu", nguoinop));
        p14.setTabSettings(new TabSettings(120f));
        p14.add(Chunk.TABBING);

        p14.add(new Chunk("Thủ quỹ", nguoinop));
        p14.setTabSettings(new TabSettings(120f));
        p14.add(Chunk.TABBING);

        p14.add(new Chunk("   Kế toán", nguoinop));
        p14.setTabSettings(new TabSettings(120f));
        p14.add(Chunk.TABBING);

        p14.add(new Chunk("   Thủ trưởng", nguoinop));
        p14.setMultipliedLeading(2);
        document.add(p14);


        Paragraph p15 = new Paragraph();
        p15.add(new Chunk("(ký,họ tên)", ky));
        p15.setTabSettings(new TabSettings(120f));
        p15.setIndentationLeft(15f);
        p15.add(Chunk.TABBING);
        p15.add(new Chunk("   (ký, họ tên)", ky));
        p15.setTabSettings(new TabSettings(120f));
        p15.add(Chunk.TABBING);
        p15.add(new Chunk("(ký, họ tên)", ky));
        p15.setTabSettings(new TabSettings(120f));
        p15.add(Chunk.TABBING);
        p15.add(new Chunk("(ký, họ tên)", ky));
        p15.setTabSettings(new TabSettings(125f));
        p15.add(Chunk.TABBING);
        p15.add(new Chunk("(ký, họ tên)", ky));
        document.add(p15);
        p15.setMultipliedLeading(3);

        Paragraph p16 = new Paragraph();
        p16.setTabSettings(new TabSettings(120f));
        p16.add(new Chunk(fnPeopleType.getName(), nguoinop));

        p16.setMultipliedLeading(5);
        document.add(p16);

        document.close();
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ProductPdfReport.pdf");
        return new ResponseEntity<Resource>(new InputStreamResource(is), headers, HttpStatus.OK);
    }


    private String getCode(String nameCash, Long idSchool, List<FnCashInternal> cashInternalList) {
        String firstname = nameCash + idSchool + "-";
        if (cashInternalList.size() == 0) {
            return firstname + "1";
        } else {
            String idcode = cashInternalList.get(0).getCode();
            String[] idcode1 = idcode.split("-");
            String idcodeShow = idcode1[1];
            int idShow = Integer.parseInt(idcodeShow);
            int idFinal = idShow + 1;
            return firstname + idFinal;
        }
    }

    private void sendFirebaseForPlus(Long idSystemTitle, String content) throws FirebaseMessagingException {
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findById(idSystemTitle).orElseThrow();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = UserInforUtils.getPlusInSchoolHasAccount();
        firebaseFunctionService.sendPlusCommon(infoEmployeeSchoolList, webSystemTitle.getTitle(), content, SchoolUtils.getIdSchool(), FirebaseConstant.CATEGORY_CASH_INTERNAL);
    }

}
