package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.finance.fees.FnKidsPackageDefault;

import java.util.List;

public interface FnKidsPackageDefaultRepositoryCustom {
    List<FnKidsPackageDefault> getPackageDefaultKids(Long idKid);
}
