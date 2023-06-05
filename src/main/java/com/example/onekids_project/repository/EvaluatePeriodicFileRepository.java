package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.EvaluatePeriodicFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EvaluatePeriodicFileRepository extends CrudRepository<EvaluatePeriodicFile, Long> {

    //có id trong DB thì xóa, không có thì vẫn chạy bình thường
    @Modifying
    @Query(value = "delete from evaluate_periodic_file where id=:id", nativeQuery = true)
    void deleteByIdCustom(Long id);
}
