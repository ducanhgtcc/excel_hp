package com.example.onekids_project.service.serviceimpl.common;

import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.integration.util.SmsUtil;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.response.sms.SmsConvertResponse;
import com.example.onekids_project.response.sms.SmsResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.common.FindSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FindSmsServiceImpl implements FindSmsService {

    @Autowired
    InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    MaUserRepository maUserRepository;


    SmsUtil smsUtil;

    @Override
    public SmsResponse findSms(UserPrincipal principal) {
        SmsResponse smsResponse = new SmsResponse();
        Boolean check = principal.getSysConfig().isShowTitleSms();
        Optional<MaUser> maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId());
        Boolean checkActiveSms = maUser.get().getEmployee().getInfoEmployeeSchoolList().get(0).isSmsSend();
        smsResponse.setCheckActiveSms(checkActiveSms);
        smsResponse.setShowTitle(check);
        if (check) {
            if (principal.getSysConfig().getTitleContentSms() != null) {
                smsResponse.setTitle(principal.getSysConfig().getTitleContentSms());
            } else {
                smsResponse.setTitle("");
            }
        }

        Boolean checkActive = principal.getSchool().isSmsActiveMore();
        smsResponse.setSmsMore(checkActive);
        if (checkActive) {
            smsResponse.setSmsRemain(principal.getSchool().getSmsTotal() - principal.getSchool().getSmsUsed());
        } else {
            if (principal.getSchool().getSmsBudget() > principal.getSchool().getSmsTotal()) {
                smsResponse.setSmsRemain(principal.getSchool().getSmsTotal() - principal.getSchool().getSmsUsed());
            } else {
                smsResponse.setSmsRemain(principal.getSchool().getSmsBudget() - principal.getSchool().getSmsUsed());
            }

        }

        return smsResponse;
    }

    @Override
    public SmsConvertResponse convertSms(UserPrincipal principal, String contentSms) {
        SmsConvertResponse smsConvertResponse = new SmsConvertResponse();
        String dataSms = SmsUtil.convertVietnamese(contentSms);
        List<String> dataSmsList = SmsUtil.getSmsParts(dataSms);
        smsConvertResponse.setSmsConvert(dataSmsList);
        return smsConvertResponse;
    }
}
