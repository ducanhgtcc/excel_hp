package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.master.request.menu.MenuSupportRequest;
import com.example.onekids_project.master.request.menu.MenuUpdateRequest;
import com.example.onekids_project.master.response.MenuSupportResponse;

import java.util.List;

/**
 * date 2021-10-15 14:45
 *
 * @author lavanviet
 */
public interface MenuSupportService {
    List<MenuSupportResponse> getSupport();
    void createSupport(MenuSupportRequest request);
    void updateSupport(MenuUpdateRequest request);
    void deleteSupportById(Long id);

    List<MenuSupportResponse> showMenuSupport();
}
