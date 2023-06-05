package com.example.onekids_project.mobile.teacher.response.medicine;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListMedicineTeacherResponse extends LastPageBase {

    private List<MedicineTeacherResponse> dataList;

}
