package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.EvaluateAttachFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EvaluateAttachFileRepository extends CrudRepository<EvaluateAttachFile, Long> {
    //có id trong DB thì xóa, không có thì vẫn chạy bình thường
    @Modifying
    @Query(value = "delete from evaluate_attach_file where id=:id", nativeQuery = true)
    void deleteByIdCustom(Long id);
}
