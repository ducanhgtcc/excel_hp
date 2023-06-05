package com.example.onekids_project.service.serviceimpl.sms;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.common.SmsConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.system.SysConfig;
import com.example.onekids_project.entity.user.SmsCode;
import com.example.onekids_project.entity.user.SmsReceivers;
import com.example.onekids_project.entity.user.SmsSend;
import com.example.onekids_project.integration.dto.SmsResultDTO;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.smsNotify.SmsNotifyDataRequest;
import com.example.onekids_project.response.phone.PhoneResponse;
import com.example.onekids_project.response.sms.SmsConvertResponse;
import com.example.onekids_project.service.dto.sms.SmsDTO;
import com.example.onekids_project.service.servicecustom.common.PhoneCommonService;
import com.example.onekids_project.service.servicecustom.sms.SmsDataService;
import com.example.onekids_project.service.servicecustom.sms.SmsService;
import com.example.onekids_project.util.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * date 2021-04-03 14:24
 *
 * @author lavanviet
 */
@Service
public class SmsDataServiceImpl implements SmsDataService {

    @Autowired
    PhoneCommonService phoneCommonService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private SmsSendRepository smsSendRepository;

    @Autowired
    private SmsReiceiversRepository smsReiceiversRepository;

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsCodeRepository smsCodeRepository;

    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private SysConfigRepository sysConfigRepository;

    @Transactional
    @Override
    public void sendSmsKid(List<Kids> kidList, Long idSchool, SmsNotifyDataRequest request) throws ExecutionException, InterruptedException {
        kidList = kidList.stream().filter(x -> x.getParent() != null).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(kidList)) {
            return;
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.EMPTY_OBJECT);
        }
        this.checkDataBeforeSend(idSchool, request);
        //set title sms
        SysConfig schoolConfig = sysConfigRepository.findBySchoolIdAndDelActive(idSchool, AppConstant.APP_TRUE).orElseThrow();
        if (StringUtils.isNotBlank(schoolConfig.getTitleContentSms())) {
            request.setSendTitle(schoolConfig.getTitleContentSms());
        }
        List<PhoneResponse> phoneResponseList = phoneCommonService.findPhoneParent(kidList);
        List<String> phoneSmsList = new ArrayList<>();
        phoneResponseList.forEach(phone -> phoneSmsList.add(phone.getPhone()));
        List<String> phoneSms = List.copyOf(Set.copyOf(phoneSmsList));
        SmsDTO smsDTO = SmsUtils.getSmsDTOByShoolId(schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không có brand name"));
        SmsSend smsSend = this.saveSmsSend(request, phoneSmsList, idSchool, SmsConstant.TYPE_SMS, AppTypeConstant.PARENT, smsDTO);
        String contentSms = StringUtils.isNotBlank(request.getSendTitle()) ? request.getSendTitle() + " " + request.getSendContent() : request.getSendContent();
        if (request.isTimer() && request.getDateTime() != null) {
            smsSend.setTimeAlarm(request.getDateTime());
            smsSend.setSent(AppConstant.APP_FALSE);
            smsSend = smsSendRepository.save(smsSend);
            SmsSend finalSmsSend = smsSend;
            kidList.forEach(kid -> {
                this.saveSmsReceivers(kid, finalSmsSend);
            });
        } else {
            Future<List<SmsResultDTO>> rs = smsService.sendSms(idSchool, phoneSms, contentSms);
            smsSend.setSent(AppConstant.APP_TRUE);
            smsSend = smsSendRepository.save(smsSend);
            List<SmsResultDTO> smsResultDTOS = rs.get();
            SmsSend finalSmsSend = smsSend;
            for (SmsResultDTO result : smsResultDTOS) {
                String phoneCv = this.getPhoneResult(result.getPhone());
                SmsCode smsCode = smsCodeRepository.findById(result.getErrCodeId()).orElseThrow(() -> new NoSuchElementException("not found errorCodeId in database: " + result.getErrCodeId()));
                Kids kid = SmsUtils.getParentPhone(phoneCv, kidList);
                Parent parent = kid.getParent();
                this.saveSmsReceiversSend(parent, idSchool, smsCode, kid.getId(), finalSmsSend);
            }
        }
    }

    @Transactional
    @Override
    public void sendSmsEmployee(List<InfoEmployeeSchool> infoList, Long idSchool, SmsNotifyDataRequest request, String appType) throws ExecutionException, InterruptedException {
        this.checkDataBeforeSend(idSchool, request);
        infoList = infoList.stream().filter(x -> x.getEmployee() != null).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(infoList)) {
            return;
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.EMPTY_OBJECT);
        }
        SysConfig schoolConfig = sysConfigRepository.findBySchoolIdAndDelActive(idSchool, AppConstant.APP_TRUE).orElseThrow();
        if (StringUtils.isNotBlank(schoolConfig.getTitleContentSms())) {
            request.setSendTitle(schoolConfig.getTitleContentSms());
        }
        List<PhoneResponse> phoneResponseList = phoneCommonService.findPhoneWithInfo(infoList);
        List<String> phoneSmsList = phoneResponseList.stream().map(PhoneResponse::getPhone).distinct().collect(Collectors.toList());
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
        SmsDTO smsDTO = SmsUtils.getSmsDTOByShoolId(school).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không có brand name"));
        SmsSend smsSend = this.saveSmsSend(request, phoneSmsList, idSchool, SmsConstant.TYPE_SMS, appType, smsDTO);
        String contentSms = StringUtils.isNotBlank(request.getSendTitle()) ? request.getSendTitle() + " " + request.getSendContent() : request.getSendContent();
        if (request.isTimer() && request.getDateTime() != null) {
            smsSend.setTimeAlarm(request.getDateTime());
            smsSend.setSent(AppConstant.APP_FALSE);
            smsSend = smsSendRepository.save(smsSend);
            SmsSend finalSmsSend = smsSend;
            infoList.forEach(info -> this.saveSmsReceiversInfo(info, finalSmsSend));
        } else {
            Future<List<SmsResultDTO>> rs = smsService.sendSms(idSchool, phoneSmsList, contentSms);
            smsSend.setSent(AppConstant.APP_TRUE);
            smsSend = smsSendRepository.save(smsSend);
            List<SmsResultDTO> smsResultDTOS = rs.get();
            SmsSend finalSmsSend = smsSend;
            for (SmsResultDTO result : smsResultDTOS) {
                String phoneCv = this.getPhoneResult(result.getPhone());
                SmsCode smsCode = smsCodeRepository.findById(result.getErrCodeId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found smsCode in infoEmployee"));
                InfoEmployeeSchool infoEmployeeSchool = SmsUtils.getTeacherPhone(phoneCv, infoList);
                this.saveSmsReceiversInfoSend(infoEmployeeSchool, idSchool, smsCode, finalSmsSend);
            }
        }
    }


    private SmsSend saveSmsSend(SmsNotifyDataRequest request, List<String> phoneSmsList, Long idSchool, String type, String appType, SmsDTO smsDTO) {
        SmsSend smsSend = modelMapper.map(request, SmsSend.class);
        int smsNumber = phoneSmsList.size();
        if (!CollectionUtils.isEmpty(phoneSmsList)) {
            smsSend.setSmsNumber(phoneSmsList.size());
        }
        smsSend.setTitleContent(request.getSendTitle());
        smsSend.setId_school(idSchool);
        smsSend.setSendType(type);
        smsSend.setAppType(appType);
        smsSend.setServiceProvider(smsDTO.getSupplierCode());
        String contentSms = StringUtils.isNotBlank(request.getSendTitle()) ? request.getSendTitle() + " " + request.getSendContent() : request.getSendContent();
        SmsConvertResponse smsConvertResponse = SmsUtils.convertSms(contentSms);
        int totalSend = smsConvertResponse.getSmsConvert().size() * smsNumber;
        smsSend.setSmsTotal(totalSend);
        return smsSend;
    }

    private void saveSmsReceivers(Kids kid, SmsSend smsSend) {
        SmsReceivers smsReceivers = new SmsReceivers();
        smsReceivers.setIdUserReceiver(kid.getParent().getMaUser().getId());
        smsReceivers.setIdKid(kid.getId());
        smsReceivers.setPhone(kid.getParent().getMaUser().getPhone());
        smsReceivers.setIdSchool(kid.getIdSchool());
        smsReceivers.setSmsSend(smsSend);
        smsReiceiversRepository.save(smsReceivers);
    }

    private void saveSmsReceiversInfo(InfoEmployeeSchool infoEmployeeSchool, SmsSend smsSend) {
        SmsReceivers smsReceivers = new SmsReceivers();
        smsReceivers.setIdUserReceiver(infoEmployeeSchool.getId());
        smsReceivers.setPhone(infoEmployeeSchool.getPhone());
        smsReceivers.setIdSchool(infoEmployeeSchool.getEmployee().getIdSchoolLogin());
        smsReceivers.setSmsSend(smsSend);
        smsReiceiversRepository.save(smsReceivers);
    }

    private String getPhoneResult(String phone) {
        String cutPhone = phone.substring(0, 2);
        if (cutPhone.equalsIgnoreCase("84")) {
            phone = "0".concat(phone.substring(2));
        }
        return phone;
    }

    private void saveSmsReceiversSend(Parent parent, Long idSchool, SmsCode smsCode, Long idKid, SmsSend smsSend) {
        SmsReceivers smsReceivers = new SmsReceivers();
        smsReceivers.setIdUserReceiver(parent.getMaUser().getId());
        smsReceivers.setPhone(parent.getMaUser().getPhone());
        smsReceivers.setIdSchool(idSchool);
        smsReceivers.setSmsCode(smsCode);
        smsReceivers.setIdKid(idKid);
        smsReceivers.setSmsSend(smsSend);
        smsReiceiversRepository.save(smsReceivers);
    }

    private void saveSmsReceiversInfoSend(InfoEmployeeSchool info, Long idSchool, SmsCode smsCode, SmsSend smsSend) {
        SmsReceivers smsReceivers = new SmsReceivers();
        smsReceivers.setIdUserReceiver(info.getEmployee().getMaUser().getId());
        smsReceivers.setPhone(info.getPhone());
        smsReceivers.setIdSchool(idSchool);
        smsReceivers.setSmsCode(smsCode);
        smsReceivers.setSmsSend(smsSend);
        smsReiceiversRepository.save(smsReceivers);
    }

    private void checkDataBeforeSend(Long idSchool, SmsNotifyDataRequest request) {
        if (idSchool == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.SCHOOL_NOT_FOUND);
        }
        if (StringUtils.isBlank(request.getSendContent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CONTENT_BLANK);
        }
        if (request.isTimer() && request.getDateTime() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chưa chọn thời gian gửi");
        }
    }


}
