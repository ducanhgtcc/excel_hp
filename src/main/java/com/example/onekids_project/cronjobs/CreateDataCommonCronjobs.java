package com.example.onekids_project.cronjobs;

import com.example.onekids_project.bean.PortBean;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lavanviet
 */
@PropertySource(value = "cronjob.properties")
@Component
public class CreateDataCommonCronjobs {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PortBean portBean;
    @Autowired
    private SchoolRepository schoolRepository;

    @Scheduled(cron = "${create.dataCommon.folder}")
    public void createFolderForAllSchool(){
        logger.info("---start create folder for all school auto---");
        portBean.checkPortForCronjob();
        List<School> schoolList = schoolRepository.findAllByDelActiveTrue();
        schoolList.forEach(x-> CommonUtil.createFolderSchool(x.getId()));
        logger.info("---end create folder for all school auto---");
    }
}
