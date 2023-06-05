package com.example.onekids_project.util;

import com.example.onekids_project.common.SmsConstant;
import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.entity.agent.Supplier;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.integration.util.SmsUtil;
import com.example.onekids_project.request.smsNotify.SmsNotifyDataRequest;
import com.example.onekids_project.response.phone.AccountLoginResponse;
import com.example.onekids_project.response.sms.SmsConvertResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.dto.sms.SmsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SmsUtils {

    /**
     * conver phone 84
     *
     * @param
     * @return true gửi firebase, false là ko gửi firebase
     */
    public static String convertPhone(String phone) {
        String cutPhone = phone.substring(0, 1);
        if (cutPhone.equalsIgnoreCase(SmsConstant.PHONE_HEAD)) {
            phone = "0".concat(phone.substring(2));
        }
        return phone;
    }


    public static Optional<SmsDTO> getSmsDTOByShoolId(School school) {
        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setCurrLocalDateTime(LocalDateTime.now());
        if (school != null) {
            Brand brand = school.getBrand();
            if (brand != null) {
                smsDTO.setBrandName(brand.getBrandName());
                if (brand.isBrandTypeAds()) {
                    smsDTO.setBrandType(SmsConstant.BRAND_ADS);
                } else {
                    smsDTO.setBrandType(SmsConstant.BRAND_ADS_NO);
                }
                Supplier supplier = brand.getSupplier();
                if (supplier != null) {
                    smsDTO.setSupplierCode(supplier.getSupplierName());
                    smsDTO.setServiceUrl(supplier.getSupplierLink());
                    smsDTO.setUserName(supplier.getUsernameLink());
                    smsDTO.setPassowrd(supplier.getPasswordLink());
                }
            }
        }
        return Optional.of(smsDTO);
    }

    public static Kids getParentPhone(String phone, List<Kids> kidsList) {
        List<Kids> kids = kidsList.stream().filter(x -> x.getParent().getMaUser().getPhone().equalsIgnoreCase(phone)).collect(Collectors.toList());
        return kids.get(0);
    }

    private List<InfoEmployeeSchool> getEmployeeByPhone(String phone, List<InfoEmployeeSchool> infoEmployeeSchoolList) {
        return infoEmployeeSchoolList.stream().filter(x -> x.getEmployee().getMaUser().getPhone().equalsIgnoreCase(phone)).collect(Collectors.toList());
    }

    public static InfoEmployeeSchool getTeacherPhone(String phone, List<InfoEmployeeSchool> infoEmployeeSchoolList) {
        List<InfoEmployeeSchool> reponseList = infoEmployeeSchoolList.stream().filter(x -> x.getEmployee().getMaUser().getPhone().equalsIgnoreCase(phone)).collect(Collectors.toList());
        return reponseList.get(0);
    }

    public static boolean checkSmsBuget(School school, String content, int totalParent) {
        long budget = school.getSmsBudget();
        long used = school.getSmsUsed();
        long total = school.getSmsTotal();
        long totalSms = totalParent * ((long) SmsUtil.getPartCount(content));
        if (school.isSmsActiveMore()) {
            return totalSms <= total - used;
        } else {
            if (totalSms > budget && totalSms > budget - used) {
                return false;
            }
            return totalSms >= budget || totalSms <= total - used;
        }
    }

    public static String convertContentSendAccount(AccountLoginResponse x, WebSystemTitle webSystemTitle) {
        String contentSms = webSystemTitle.getTitle().concat(webSystemTitle.getContent());
        String[] parts = x.getUsername().split(SmsConstant.USERNAME_CUT);
        contentSms = contentSms.replace(SmsConstant.USER_NAME, parts[0]);
        contentSms = contentSms.replace(SmsConstant.PASSWORD_NAME, x.getPasswordShow());
        return contentSms;
    }


    public static SmsConvertResponse convertSms(String contentSms) {
        SmsConvertResponse smsConvertResponse = new SmsConvertResponse();
        String dataSms = SmsUtil.convertVietnamese(contentSms);
        List<String> dataSmsList = SmsUtil.getSmsParts(dataSms);
        smsConvertResponse.setSmsConvert(dataSmsList);
        return smsConvertResponse;
    }


    public static String converContentWithTitle(UserPrincipal principal, String content) {
        if (principal.getSysConfig().isShowTitleSms()) {
            if (principal.getSysConfig().getTitleContentSms() != null) {
                return principal.getSysConfig().getTitleContentSms().concat(content);
            } else {
                return content;
            }
        }
        return content;
    }

    public static void checkSendSms(InfoEmployeeSchool infoEmployeeSchool) {
        if (!infoEmployeeSchool.isSmsSend()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn cần được cấp quyền gửi Sms");
        }
    }

}

