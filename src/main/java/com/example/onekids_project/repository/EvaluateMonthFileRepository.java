package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.EvaluateMonthFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EvaluateMonthFileRepository extends CrudRepository<EvaluateMonthFile, Long> {

    //có id trong DB thì xóa, không có thì vẫn chạy bình thường
    @Modifying
    @Query(value = "delete from evaluate_month_file where id=:id", nativeQuery = true)
    void deleteByIdCustom(Long id);

}
