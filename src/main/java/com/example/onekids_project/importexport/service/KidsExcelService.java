package com.example.onekids_project.importexport.service;

        import com.example.onekids_project.importexport.model.*;
        import com.example.onekids_project.request.employee.CreateEmployeeExcelRequest;
        import com.example.onekids_project.request.kids.CreateKidsExcelRequest;
        import com.example.onekids_project.security.model.CurrentUser;
        import com.example.onekids_project.security.model.UserPrincipal;
        import org.springframework.web.bind.annotation.RequestBody;
        import org.springframework.web.multipart.MultipartFile;

        import javax.validation.Valid;
        import java.io.ByteArrayInputStream;
        import java.io.IOException;
        import java.util.List;
        import java.util.Map;

public interface KidsExcelService {
    /**
     * Xuất file excel học sinh
     * @param kidModelList
     * @param nameSchool
     * @return
     * @throws IOException
     */
    ByteArrayInputStream customKidsToExcel(List<KidModel> kidModelList, String nameSchool) throws IOException;
    /**
     * Xuất file excel học sinh chiều cao cân nặng
     * @param heightWeightModelList
     * @param nameSchool
     * @param className
     * @return
     * @throws IOException
     */
    ByteArrayInputStream customKidsToExcelHeightWeight(List<HeightWeightModel> heightWeightModelList, String nameSchool, String className) throws IOException;
    /**
     * Xuất file excel học sinh chiều cao cân nặng
     * @param modelList
     * @param idSchool
     * @param idClass
     * @return
     * @throws IOException
     */
    ByteArrayInputStream customKidsToExcelAllHeightWeight(List<HeightWeightModel> modelList, Long idSchool, Long idClass) throws IOException;
    /**
     * Import file excel học sinh
     * @param principal
     * @param multipartFile
     * @return
     * @throws IOException
     */
    ListKidModelImport importExcelKids(UserPrincipal principal, MultipartFile multipartFile) throws IOException;

    /**
     * Xuất file excel import học sinh lỗi
     * @param
     * @param
     * @return
     * @throws IOException
     */
    ByteArrayInputStream customKidsImportFailExcel(List<KidModelImportFail> kidModelList, String nameSchool) throws IOException;

    void importExcelNewKids(UserPrincipal principal, CreateKidsExcelRequest request);
}
