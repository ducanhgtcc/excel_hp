package com.example.onekids_project.response.subject;

import com.example.onekids_project.dto.GradeDTO;
import com.example.onekids_project.dto.SubjectDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListSubjectResponse {
    List<SubjectDTO> subjectList;
}
