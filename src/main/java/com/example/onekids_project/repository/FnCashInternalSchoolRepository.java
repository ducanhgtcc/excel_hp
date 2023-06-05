package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.CashInternal.FnCashInternal;
import com.example.onekids_project.repository.repositorycustom.FnCashInternalSchoolRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FnCashInternalSchoolRepository extends JpaRepository<FnCashInternal, Long>, FnCashInternalSchoolRepositoryCustom {
    Optional<FnCashInternal> findByIdAndDelActiveTrue(Long id);

    Optional<FnCashInternal> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    Optional<FnCashInternal> findByIdAndPaymentFalseAndDelActiveTrue(Long id);

    Optional<FnCashInternal> findByIdAndIdSchoolAndPaymentFalseAndDelActiveTrue(Long id, Long idSchool);
    Optional<FnCashInternal> findByIdAndIdSchoolAndCanceledFalseAndPaymentFalseAndDelActiveTrue(Long id, Long idSchool);

    int countByIdSchoolAndCategoryAndApprovedFalseAndCanceledFalseAndDelActiveTrue(Long idSchool, String category);
}
