package com.example.onekids_project.master.service;

import java.util.List;

/**
 * @author lavanviet
 */
public interface CommonMasterService {
    void updatePasswordManyKids(List<Long> idList, String newPassword);
    void updatePasswordManyEmployee(List<Long> idList, String newPassword);
}
