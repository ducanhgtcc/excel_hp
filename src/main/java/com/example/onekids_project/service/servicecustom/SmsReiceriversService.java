package com.example.onekids_project.service.servicecustom;

//import org.springframework.data.domain.Pageable;

import com.example.onekids_project.dto.SmsReiceiveDTO;

public interface SmsReiceriversService {

    boolean deleteSmsReiceivers(Long id);

    SmsReiceiveDTO findByIdSmsReiceiver(Long idSchoolLogin, Long id);

    boolean deleteMultiSms(Long idSchoolLogin, Long[] ids);

}
