package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-05-28 4:03 PM
 *
 * @author nguyễn văn thụ
 */
public interface ClassMenuOrdinalParentService {
    List<String> classMenuOrdinalParent(UserPrincipal principal, LocalDate localDate);
}
