package com.example.onekids_project.controller;

import com.example.onekids_project.request.employeeSalary.SearchAttendanceSalaryRequest;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.PDFService;
import com.example.onekids_project.util.RequestUtils;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * date 2021-03-05 14:56
 *
 * @author Nguyễn Thành
 */
@RestController
@RequestMapping("/web/pdf")
public class PDFController {

    @Autowired
    private PDFService pdfService;

    /**
     * tạo hóa đơn thu học phí cho học sinh
     *
     * @param principal
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    @GetMapping("/kids/order/{idOrder}")
    public ResponseEntity printOrderKidsPDF(@CurrentUser UserPrincipal principal, @PathVariable Long idOrder, @RequestParam List<Long> idList) throws IOException, DocumentException {
        RequestUtils.getFirstRequestPlus(principal, idOrder, idList, "/kids/order/{idOrder}");
        ByteArrayInputStream in = pdfService.createKidPDF(principal, idOrder, idList);
        return ResponseEntity.ok().body(new InputStreamResource(in));
    }

    @GetMapping("/salary/order/{idOrder}")
    public ResponseEntity printSalaryPDF(@CurrentUser UserPrincipal principal, @PathVariable Long idOrder, @RequestParam List<Long> idList) throws IOException, DocumentException {
        RequestUtils.getFirstRequestPlus(principal, idOrder, idList, "/salary/order/{idOrder}");
        ByteArrayInputStream in = pdfService.createSalaryPDF(principal, idOrder, idList);
        return ResponseEntity.ok().body(new InputStreamResource(in));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/export-kid")
    public ResponseEntity getAllAttendanceKidCustom(@CurrentUser UserPrincipal principal) throws IOException {
        RequestUtils.getFirstRequestPlus(principal, "/export-kid");
        ByteArrayInputStream in = pdfService.exportKid(principal);
        return ResponseEntity.ok().body(new InputStreamResource(in));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/export-employ-attedance")
    public ResponseEntity getAllAttendanceKidCustom(@CurrentUser UserPrincipal principal, SearchAttendanceSalaryRequest request) throws IOException {
        RequestUtils.getFirstRequestPlus(principal, "/export-employ-attedance");
        ByteArrayInputStream in = pdfService.exportAttendanceSalary(principal, request);
        return ResponseEntity.ok().body(new InputStreamResource(in));
    }
}
