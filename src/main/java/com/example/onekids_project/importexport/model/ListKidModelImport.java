package com.example.onekids_project.importexport.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListKidModelImport {
    List<KidModelImportFail> kidModelImportFailList;
    List<KidModelImport> kidModelImportList;
}
