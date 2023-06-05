package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.finance.feesextend.FnPackageExtend;

import java.util.List;

/**
 * date 2021-10-01 16:04
 *
 * @author lavanviet
 */
public interface FnPackageExtendRepositoryCustom {
    List<FnPackageExtend> searchPackageExtend(Long idSchool, String name);
}
