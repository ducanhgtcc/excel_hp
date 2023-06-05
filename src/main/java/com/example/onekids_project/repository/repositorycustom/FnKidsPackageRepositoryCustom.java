package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.finance.fees.FnKidsPackage;

import java.time.LocalDate;
import java.util.List;

public interface FnKidsPackageRepositoryCustom {
    List<FnKidsPackage> getKidsPackageForKid(Long idSchool, Long idKid, int month, int year);

    List<FnKidsPackage> searchKidsPackageParent(Long idKid, int year);

    List<FnKidsPackage> searchKidsPackageFlowPackage(Long idPackage, LocalDate date);
}
