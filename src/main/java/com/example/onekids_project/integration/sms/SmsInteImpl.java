package com.example.onekids_project.integration.sms;

import com.example.onekids_project.common.SmsConstant;
import com.example.onekids_project.entity.user.SmsCode;
import com.example.onekids_project.integration.SmsInte;
import com.example.onekids_project.integration.dto.SmsResultDTO;
import com.example.onekids_project.integration.exception.IntegrationException;
import com.example.onekids_project.integration.sms.neo.SendMTSoap11BindingStub;
import com.example.onekids_project.integration.sms.vnpt.BrandNameWSPortBindingStub;
import com.example.onekids_project.integration.sms.vnpt.UploadMultySmsResponse;
import com.example.onekids_project.integration.util.SmsUtil;
import com.example.onekids_project.repository.SmsCodeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SmsInteImpl implements SmsInte {
    @Autowired
    private SmsCodeRepository smsCodeRepository;

    @Override
    public SmsResultDTO sendSms(String supplierCode, String serviceUrl, String username, String password, String phone, String content, int brandType, String brandname) throws IntegrationException {
        SmsResultDTO smsResultDTO = new SmsResultDTO();
        smsResultDTO.setPhone(phone);
        if (supplierCode != null) {

            switch (supplierCode) {
                case "NEO":
                    String rs = sendSmsNeo(serviceUrl, username, password, phone, content, getNeoBrandType(brandType), brandname);
                    smsResultDTO.setErrCode(rs.split("|")[0]);
                    smsResultDTO.setErrMsg(rs.split("|")[1]);
                    if (StringUtils.isNotEmpty(rs) && rs.startsWith("0")) {
                        smsResultDTO.setSuccess(true);
                    } else {
                        smsResultDTO.setSuccess(false);
                    }
                    break;
                case "VNPT":
                    long result = sendSmsVnpt(serviceUrl, username, password, phone, content, getVnptBrandType(brandType), brandname);
                    smsResultDTO.setErrCode(Long.toString(result));
                    if (result == 0) {
                        smsResultDTO.setSuccess(true);
                    } else {
                        smsResultDTO.setSuccess(false);
                    }
                    break;
                default:
                    throw new NotFoundException(SmsConstant.NO_SUPPLIER);
            }
        } else {
            smsResultDTO.setErrCode("-100");
            smsResultDTO.setSuccess(false);
        }
        Optional<SmsCode> smsCode = smsCodeRepository.findByCodeErrorAndServiceProvider(smsResultDTO.getErrCode(), supplierCode.equals("NEO") ? 3 : 1);
        if (smsCode.isPresent()) {
            smsResultDTO.setErrCodeId(smsCode.get().getId());
        }
        return smsResultDTO;
    }

    @Override
    public List<SmsResultDTO> sendMutilSms(String supplierCode, String serviceUrl, String username, String password, String phones, String content, int brandType, String brandname) throws IntegrationException {
        List<SmsResultDTO> smsResultDTOList = new ArrayList<>();
        UploadMultySmsResponse uploadMultySmsResponse = sendMutilSmsVnpt(serviceUrl, username, password, phones, content, getVnptBrandType(brandType), brandname);

        String errorCode = Integer.toString(uploadMultySmsResponse.getErrorCode());
        String errDesc = uploadMultySmsResponse.getDescription();
        String success = uploadMultySmsResponse.getListSuccess();
        String fail = uploadMultySmsResponse.getListFail();
        if (StringUtils.isBlank(success) == false) {
            List<String> listSuccess = Stream.of(success.split(";")).collect(Collectors.toList());
            listSuccess.forEach(phone -> {
                SmsResultDTO smsResultDTO = new SmsResultDTO();
                smsResultDTO.setErrCode(errorCode);
                smsResultDTO.setErrMsg(errDesc);
                smsResultDTO.setPhone(phone);
                smsResultDTO.setSuccess(true);
                smsResultDTOList.add(smsResultDTO);
            });
        }
        if (StringUtils.isBlank(fail) == false) {
            List<String> listFail = Stream.of(fail.split(";")).collect(Collectors.toList());
            listFail.forEach(phone -> {
                SmsResultDTO smsResultDTO = new SmsResultDTO();
                smsResultDTO.setErrCode(errorCode);
                smsResultDTO.setErrMsg(errDesc);
                smsResultDTO.setPhone(phone);
                smsResultDTO.setSuccess(false);
                smsResultDTOList.add(smsResultDTO);
            });
        }
        Optional<SmsCode> smsCode = smsCodeRepository.findByCodeErrorAndServiceProvider(smsResultDTOList.get(0).getErrCode(), supplierCode.equals("NEO") ? 3 : 1);
        if (smsCode.isPresent()) {
            smsResultDTOList.forEach(smsResultDTO -> {
                smsResultDTO.setErrCodeId(smsCode.get().getId());
            });
        }
        return smsResultDTOList;
    }

    private String sendSmsNeo(String serviceUrl, String username, String password, String receiver, String content, int brandType, String brandname) throws IntegrationException {
        try {
            SendMTSoap11BindingStub st = new SendMTSoap11BindingStub(new URL(serviceUrl), null);
            String rs = st.insertSMS(username, password, receiver, content, brandType, brandname, 0,
                    SmsUtil.createID());
            return rs;
        } catch (Throwable e) {
            throw new IntegrationException(e);
        }
    }

    private int getNeoBrandType(int brandType) {
        int result = 2;
        return result;
    }

    private String getVnptBrandType(int brandType) {
        String result = "2";
        switch (brandType) {
            case 0:
                result = "1";
                break;
            case 1:
                result = "2";
                break;
        }
        return result;
    }

    private long sendSmsVnpt(String serviceUrl, String username, String password, String receiver, String content, String serviceKind, String brandname) throws IntegrationException {
        try {
            BrandNameWSPortBindingStub brandNameWSPortBindingStub = new BrandNameWSPortBindingStub(new URL(serviceUrl), null);
            long result = brandNameWSPortBindingStub.uploadSMS(username, password, brandname, receiver, "0", serviceKind, content, 0);
            result = result > 0 ? 0 : result;
            return result;
        } catch (Throwable e) {
            throw new IntegrationException(e);
        }
    }

    private UploadMultySmsResponse sendMutilSmsVnpt(String serviceUrl, String username, String password, String receivers, String content, String serviceKind, String brandname) throws IntegrationException {
        try {
            BrandNameWSPortBindingStub brandNameWSPortBindingStub = new BrandNameWSPortBindingStub(new URL(serviceUrl), null);
            UploadMultySmsResponse uploadMultySmsResponse = brandNameWSPortBindingStub.uploadMultiSMS(username, password, brandname, receivers, "0", serviceKind, content, 0);
            return uploadMultySmsResponse;
        } catch (Throwable e) {
            throw new IntegrationException(e);
        }
    }


}
