package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.master.service.HeightSampleService;
import com.example.onekids_project.master.service.VaccinateService;
import com.example.onekids_project.master.service.WeightSampleService;
import com.example.onekids_project.mobile.service.servicecustom.AppVersionService;
import com.example.onekids_project.repository.SysInforRepository;
import com.example.onekids_project.request.system.SysInforRequest;
import com.example.onekids_project.response.system.SysInforResponse;
import com.example.onekids_project.service.servicecustom.BirthdaySampleService;
import com.example.onekids_project.service.servicecustom.SysInforService;
import com.example.onekids_project.service.servicecustom.WebSystemTitleService;
import com.example.onekids_project.service.servicecustom.sms.SmsSampleService;
import com.example.onekids_project.service.servicecustom.system.ApiService;
import com.example.onekids_project.util.GenerateCode;
import com.example.onekids_project.validate.RequestValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysInforServiceImpl implements SysInforService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SysInforRepository sysInforRepository;

    @Autowired
    private AppVersionService AppVersionService;

    @Autowired
    private WebSystemTitleService webSystemTitleService;

    @Autowired
    private BirthdaySampleService birthdaySampleService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private VaccinateService vaccinateService;

    @Autowired
    private WeightSampleService weightSampleService;

    @Autowired
    private HeightSampleService heightSampleService;

    @Autowired
    private SmsSampleService smsSampleService;

    @Override
    public SysInforResponse getFirstSupportOnekids() {
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        SysInforResponse sysInforResponse = modelMapper.map(sysInfor, SysInforResponse.class);
        return sysInforResponse;
    }

    @Override
    public SysInforResponse updateSystemInfor(SysInforRequest sysInforRequest) {
        RequestValidate.checkVerificationCodeAdmin(sysInforRequest.getVerificationCode());
        SysInfor sysInfor = sysInforRepository.findByIdAndDelActiveTrue(sysInforRequest.getId()).orElseThrow();
        modelMapper.map(sysInforRequest, sysInfor);
        SysInfor sysInforSaved = sysInforRepository.save(sysInfor);
        SysInforResponse sysInforResponse = modelMapper.map(sysInforSaved, SysInforResponse.class);
        return sysInforResponse;
    }

    @Transactional
    @Override
    public boolean createCommonALl() {
        this.createSysinfor();
        AppVersionService.createAppVersion();
        birthdaySampleService.createBirthSampleForSystem();
        vaccinateService.createVaccineDefault();
        weightSampleService.createWeightDefault();
        heightSampleService.createHeightDefault();
        smsSampleService.createSmsCodeDefault();
        return true;
    }

    private void createSysinfor() {
        List<SysInfor> sysInforList = sysInforRepository.findAll();
        if (CollectionUtils.isEmpty(sysInforList)) {
            SysInfor sysInfor = new SysInfor();
            sysInfor.setIdCreated(SystemConstant.ID_SYSTEM);
            sysInfor.setCreatedBy(SystemConstant.SYSTEM_NAME);
            sysInfor.setCreatedDate(LocalDateTime.now());
            sysInfor.setVerificationCode(GenerateCode.getLetterUpperCase());
            sysInforRepository.save(sysInfor);
        }
    }
}
