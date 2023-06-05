package com.example.onekids_project.service.serviceimpl.sms;

import com.example.onekids_project.common.SmsConstant;
import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.entity.agent.Supplier;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.integration.SmsInte;
import com.example.onekids_project.integration.dto.SmsResultDTO;
import com.example.onekids_project.integration.exception.IntegrationException;
import com.example.onekids_project.integration.util.SmsUtil;
import com.example.onekids_project.repository.BrandRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.service.dto.sms.SmsDTO;
import com.example.onekids_project.service.servicecustom.sms.SmsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SmsServiceImpl implements SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
    @Autowired
    private SmsInte smsInte;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private BrandRepository brandRepository;


    @Override
    @Async
    public Future<SmsResultDTO> sendSms(long shoolId, String phone, String content) {
        SmsResultDTO smsResultDTO = new SmsResultDTO();
        smsResultDTO.setPhone(phone);
        String smsContent = SmsUtil.convertVietnamese(content);
        String errorChk = checkSmsBuget(shoolId, content, 1);
        if (StringUtils.isNotEmpty(errorChk)) {
            smsResultDTO.setErrCode("-101");
            smsResultDTO.setErrMsg(errorChk);
            smsResultDTO.setSuccess(false);
            return CompletableFuture.completedFuture(smsResultDTO);
        }
        SmsDTO smsDTO = getSmsDTOByShoolId(shoolId);
        try {
            smsResultDTO = smsInte.sendSms(smsDTO.getSupplierCode(), smsDTO.getServiceUrl(), smsDTO.getUserName(), smsDTO.getPassowrd(), phone, smsContent,
                    smsDTO.getBrandType(), smsDTO.getBrandName());
        } catch (IntegrationException e) {
            logger.error("SmsServiceImpl:", e);
            smsResultDTO.setErrMsg(e.getMessage());
            smsResultDTO.setErrCode("-102");
            smsResultDTO.setSuccess(false);
        }

        if ("0".equals(smsResultDTO.getErrCode())) {
            long totalSms = Long.valueOf(SmsUtil.getPartCount(content));
            schoolRepository.updateUsed(totalSms, shoolId);
        }
        return CompletableFuture.completedFuture(smsResultDTO);
    }

    @Override
    @Async
    public Future<SmsResultDTO> sendSmsOne(long shoolId, String phone, String content) {
        SmsResultDTO smsResultDTO = new SmsResultDTO();
        smsResultDTO.setPhone(phone);
        String smsContent = SmsUtil.convertVietnamese(content);
        SmsDTO smsDTO = getSmsDTOByShoolId(shoolId);
        try {
            smsResultDTO = smsInte.sendSms(smsDTO.getSupplierCode(), smsDTO.getServiceUrl(), smsDTO.getUserName(), smsDTO.getPassowrd(), phone, smsContent,
                    smsDTO.getBrandType(), smsDTO.getBrandName());
        } catch (IntegrationException e) {
            logger.error("SmsServiceImpl:", e);
            smsResultDTO.setErrMsg(e.getMessage());
            smsResultDTO.setErrCode("-102");
            smsResultDTO.setSuccess(false);
        }

        if ("0".equals(smsResultDTO.getErrCode())) {
            long totalSms = Long.valueOf(SmsUtil.getPartCount(content));
            schoolRepository.updateUsed(totalSms, shoolId);
        }
        return CompletableFuture.completedFuture(smsResultDTO);
    }

    @Override
    @Async
    public Future<List<SmsResultDTO>> sendSms(long shoolId, List<String> phones, String content) {
        List<SmsResultDTO> smsResultDTOList = new ArrayList<>();
        String smsContent = SmsUtil.convertVietnamese(content);
        String errorChk = checkSmsBuget(shoolId, content, phones.size());
        if (StringUtils.isNotEmpty(errorChk)) {

            phones.forEach(phone -> {
                SmsResultDTO smsResultDTO = new SmsResultDTO();
                smsResultDTO.setErrCode("-101");
                smsResultDTO.setErrMsg(errorChk);
                smsResultDTO.setPhone(phone);
                smsResultDTO.setSuccess(false);
            });

            return CompletableFuture.completedFuture(smsResultDTOList);
        }
        SmsDTO smsDTO = getSmsDTOByShoolId(shoolId);
        AtomicReference<String> error = new AtomicReference<>("");
        String status = "0";
        String supplierCode = smsDTO.getSupplierCode();
        switch (supplierCode) {
            case "NEO":
                for (String s : phones) {
                    Future<SmsResultDTO> smsResultDTO = sendSms(shoolId, s, smsContent);
                    try {
                        smsResultDTOList.add(smsResultDTO.get());
                    } catch (InterruptedException e) {
                        logger.error("SmsServiceImpl:", e);
                    } catch (ExecutionException e) {
                        logger.error("SmsServiceImpl:", e);
                    }
                }
                break;
            case "VNPT":
                StringBuffer listPhone = new StringBuffer();
                phones.forEach(phone -> {
                    listPhone.append(";").append(phone);
                });
                listPhone.deleteCharAt(0);
                smsResultDTOList = sendMutilSms(smsDTO, shoolId, listPhone.toString(), smsContent);
                long count = smsResultDTOList.stream().filter(SmsResultDTO::isSuccess).count();
                long totalSms = Long.valueOf(SmsUtil.getPartCount(content)) * count;
                schoolRepository.updateUsed(totalSms, shoolId);
                break;
            default:
                throw new NotFoundException(SmsConstant.NO_SUPPLIER);
        }
        return CompletableFuture.completedFuture(smsResultDTOList);
    }

    @Override
    @Async
    public Future<List<SmsResultDTO>> sendSmsMulti(long shoolId, List<String> phones, String content) {
        List<SmsResultDTO> smsResultDTOList = new ArrayList<>();
        String smsContent = SmsUtil.convertVietnamese(content);
        SmsDTO smsDTO = getSmsDTOByShoolId(shoolId);
        String supplierCode = smsDTO.getSupplierCode();
        switch (supplierCode) {
            case SmsConstant.SUP_NEO:
                for (String s : phones) {
                    Future<SmsResultDTO> smsResultDTO = sendSms(shoolId, s, smsContent);
                    try {
                        smsResultDTOList.add(smsResultDTO.get());
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error("SmsServiceImpl:", e);
                    }
                }
                break;
            case SmsConstant.SUP_VNPT:
                StringBuffer listPhone = new StringBuffer();
                phones.forEach(phone -> {
                    listPhone.append(";").append(phone);
                });
                listPhone.deleteCharAt(0);
                smsResultDTOList = sendMutilSms(smsDTO, shoolId, listPhone.toString(), smsContent);
                long count = smsResultDTOList.stream().filter(SmsResultDTO::isSuccess).count();
                long totalSms = Long.valueOf(SmsUtil.getPartCount(content)) * count;
                schoolRepository.updateUsed(totalSms, shoolId);
                break;
            default:
                throw new NotFoundException(SmsConstant.NO_SUPPLIER);
        }
        return CompletableFuture.completedFuture(smsResultDTOList);
    }
//    private void updateSmsHistory(String content, String phone, long shoolId, String sendBy, LocalDateTime sendTime, String status, String error) {
//        SmsHistory smsHistory = new SmsHistory();
//        smsHistory.setContent(content);
//        smsHistory.setPhoneNumber(phone);
//        smsHistory.setSchoolId(shoolId);
//        smsHistory.setSendBy(sendBy);
//        smsHistory.setSendTime(sendTime);
//        smsHistory.setStatus(status);
//        smsHistory.setStatusCode(error);
//        smsHistoryRepository.save(smsHistory);
//    }

    private String checkSmsBuget(long shoolId, String content, int totalParent) {
        String error = "";
        Optional<School> school = schoolRepository.findById(shoolId);
        if (school.isPresent()) {
            long budget = school.get().getSmsBudget();
            long used = school.get().getSmsUsed();
            long total = school.get().getSmsTotal();
            Long totalSms = 0L;
            totalSms = totalParent * (Long.valueOf(SmsUtil.getPartCount(content)));
            if (school.get().isSmsActiveMore()) {
                if (totalSms > total - used) {
                    error = "Not enough SMS Total";
                }
            } else {
                if (totalSms > budget && totalSms > budget - used) {
                    error = "Not enough SMS Budget";
                }
                if (totalSms < budget && totalSms > total - used) {
                    error = "Not enough SMS Budget";
                }
            }
        }
        return error;
    }

//    private void postUpdate(long shoolId, String content, int used) {
//        long totalSms = Long.valueOf(SmsUtil.getPartCount(content));
//        schoolRepository.updateUsed(totalSms, shoolId);
//    }

    private List<SmsResultDTO> sendMutilSms(SmsDTO smsDTO, long shoolId, String phones, String content) {
        List<SmsResultDTO> smsResultDTOList = new ArrayList<>();
        String error = "";
        String status = "0";
        try {
            smsResultDTOList = smsInte.sendMutilSms(smsDTO.getSupplierCode(), smsDTO.getServiceUrl(), smsDTO.getUserName(), smsDTO.getPassowrd(), phones, content, smsDTO.getBrandType(), smsDTO.getBrandName());
        } catch (IntegrationException e) {
            logger.error("SmsServiceImpl:", e);
            List<String> phoneList = Stream.of(phones.split(";")).collect(Collectors.toList());
            phoneList.forEach(phone -> {
                SmsResultDTO smsResultDTO = new SmsResultDTO();
                smsResultDTO.setErrCode("-101");
                smsResultDTO.setErrMsg(e.getMessage());
                smsResultDTO.setPhone(phone);
                smsResultDTO.setSuccess(false);
            });
        }
        return smsResultDTOList;
    }

    @Override
    public SmsDTO getSmsDTOByShoolId(long shoolId) {
        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setCurrLocalDateTime(LocalDateTime.now());
        Optional<School> school = schoolRepository.findById(shoolId);
        if (school.isPresent()) {
            Brand brand = school.get().getBrand();
            if (brand != null) {
                smsDTO.setBrandName(brand.getBrandName());
                if (brand.isBrandTypeAds()) {
                    smsDTO.setBrandType(0);
                } else {
                    smsDTO.setBrandType(1);
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
        return smsDTO;
    }
}
