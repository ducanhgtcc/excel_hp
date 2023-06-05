package com.example.onekids_project.controller.pdf;

/**
 * date 2021-03-01 11:38
 *
 * @author Nguyễn Thành
 */

import com.example.onekids_project.repository.*;
import com.example.onekids_project.service.servicecustom.KidsService;
import com.example.onekids_project.service.servicecustom.MaClassService;
import com.example.onekids_project.service.servicecustom.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/test-excel")
public class TestExcel {

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

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private KidsService kidsService;

    @Autowired
    private MaClassService maClassService;


}
