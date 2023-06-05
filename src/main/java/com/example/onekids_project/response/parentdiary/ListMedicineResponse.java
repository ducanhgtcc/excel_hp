package com.example.onekids_project.response.parentdiary;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ListMedicineResponse extends TotalResponse {
    List<MedicineResponse> medicineResponses;
}
