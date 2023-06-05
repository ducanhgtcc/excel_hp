package com.example.onekids_project.cronjobs;

import com.example.onekids_project.bean.PortBean;
import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@PropertySource(value = "cronjob.properties")
@Component
public class FileCronjob {
    private static final Logger logger = LoggerFactory.getLogger(FileCronjob.class);
    @Autowired
    private PortBean portBean;

    @Scheduled(cron = "${file.tempData}")
    public void deleteTempData() throws IOException {
        logger.info("---start auto delete temp data---");
        portBean.checkPortForCronjob();
        File dirsavefile = new File(AppConstant.DIRECTORY_DEFAULT + UploadDownloadConstant.TEMP_DATA);
        FileUtils.cleanDirectory(dirsavefile);
        logger.info("---end auto delete temp data---");
    }
}
