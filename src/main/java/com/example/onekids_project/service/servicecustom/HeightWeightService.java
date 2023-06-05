package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.KidsHeightWeightDTO;
import com.example.onekids_project.importexport.model.HeightWeightModel;
import com.example.onekids_project.request.kidsheightweight.CreateKidsHeightWeightRequest;
import com.example.onekids_project.request.kidsheightweight.SearchKidsHeightWeightRequest;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.kidsheightweight.HeightWeightSampleRespone;
import com.example.onekids_project.response.kidsheightweight.KidsHeightWeightResponse;
import com.example.onekids_project.response.kidsheightweight.ListKidsHeightWeightResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;
import java.util.Optional;

public interface HeightWeightService {


    KidsHeightWeightResponse createKidsWeightHeight(Long idSchool, CreateKidsHeightWeightRequest createKidsHeightWeightRequest);

    ListKidsHeightWeightResponse searchAddHeightWeight(UserPrincipal principal, SearchKidsHeightWeightRequest request);

    Optional<KidsHeightWeightDTO> findByIdHeightWeight(Long idSchoolLogin, Long id);

    /* List cân nặng chiều cao mới nhất
     *
     */

    ListKidsHeightWeightResponse searchMenuHeightWeight(Long idSchool, SearchKidsHeightWeightRequest searchStudentRequest);
    /*
        ID cân nặng chiều cao + ids kids
     */

    List<KidsHeightWeightResponse> findKidsHeightWeightToExcel(Long idSchool, SearchKidsHeightWeightRequest searchKidsHeightWeightRequest);

    List<HeightWeightModel> detachedListKidsHeightWeightAll(List<KidsHeightWeightResponse> kidsHeightWeightResponses, List<Long> kidDTOList);

    List<ExcelResponse> detachedListKidsHeightWeightAllNew(List<KidsHeightWeightResponse> kidsHeightWeightResponses, List<Long> kidDTOList, Long idSchool, Long idClass);


    /**
     * Chuyển đổi đối tượng KidsExportResponse thành KidVM để đổ dữ liệu lên excel
     *
     * @param listKidsHeightWeightResponse
     * @return
     */
    List<HeightWeightModel> getFileAllKidByHeightWeight(ListKidsHeightWeightResponse listKidsHeightWeightResponse, String nameSchool);

    List<ExcelResponse> getFileAllKidByHeightWeightNew(ListKidsHeightWeightResponse listKidsHeightWeightResponse, Long idSchool, Long idClass);

    List<HeightWeightSampleRespone> findHeightWeightSample(Long idKid);

}
