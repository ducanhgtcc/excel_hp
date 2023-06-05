package com.example.onekids_project.util;

import com.example.onekids_project.security.payload.LoginRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class LoginRequestBean {
    LoginRequest loginRequest;
}
