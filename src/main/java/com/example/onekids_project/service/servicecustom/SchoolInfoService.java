package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.schoolconfig.UpdateBankInfoRequest;
import com.example.onekids_project.response.caskinternal.SchoolInfoBankResponses;
import com.example.onekids_project.response.school.SchoolInfoResponse;
import com.example.onekids_project.response.supperplus.SchoolInfoConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;

/**
 * date 2021-03-05 15:25
 *
 * @author lavanviet
 */
public interface SchoolInfoService {
    SchoolInfoResponse findSchoolInfo(UserPrincipal principal);

    boolean updateBankInfo(UserPrincipal principal, UpdateBankInfoRequest request);

    SchoolInfoBankResponses findDetailSchoolInfoBank(UserPrincipal principal);

    SchoolInfoConfigResponse findSchoolInfoConfig();
    void updateSchoolInfoConfig(SchoolInfoConfigResponse request);
}
