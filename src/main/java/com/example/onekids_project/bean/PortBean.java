package com.example.onekids_project.bean;

import com.example.onekids_project.common.PortConstant;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * date 2021-09-22 11:09
 *
 * @author lavanviet
 */
@Component
public class PortBean {
    private static final Logger logger = LoggerFactory.getLogger(PortBean.class);
    @Autowired
    private Environment environment;

    public void checkPortForCronjob() {
        final String port = environment.getProperty("local.server.port");
        logger.info("------start show for port: {}-------" + port);
        if (!PortConstant.PORT_0.equals(port)) {
            throw new ResponseStatusException(HttpStatus.CREATED, "port invalid for generate auto");
        }
    }

    public boolean checkPortReturn() {
        final String port = environment.getProperty("local.server.port");
        logger.info("------start show for port: {}-----" + port);
        return PortConstant.PORT_0.equals(port);
    }
}
