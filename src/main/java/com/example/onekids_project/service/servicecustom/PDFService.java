package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.employeeSalary.SearchAttendanceSalaryRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import com.itextpdf.text.DocumentException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface PDFService {

    ByteArrayInputStream createKidPDF(UserPrincipal principal, Long idOrder, List<Long> idList) throws DocumentException, IOException;


    ByteArrayInputStream createSalaryPDF(UserPrincipal principal, Long idOrder, List<Long> idList) throws IOException, DocumentException;

    ByteArrayInputStream exportKid(UserPrincipal principal) throws IOException;

    ByteArrayInputStream exportAttendanceSalary(UserPrincipal principal, SearchAttendanceSalaryRequest request) throws IOException;
}
