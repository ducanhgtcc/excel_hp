package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {
    Optional<SmsCode> findByCodeErrorAndServiceProvider(String codeError, double serviceProvider);

    Optional<SmsCode> findByCodeError(String codeError);
}
