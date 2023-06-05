package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.KidsClassDate;
import com.example.onekids_project.repository.repositorycustom.KidsClassDateRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface KidsClassDateRepository extends JpaRepository<KidsClassDate, Long>, KidsClassDateRepositoryCustom {
    List<KidsClassDate> findByKidsIdAndMaClassIdOrderByCreatedDateDesc(Long idKid, Long idClass);

    @Query(value = "SELECT id_class FROM kids_class_date where id_kid=:idKid and id<=(select id from kids_class_date where id_kid=:idKid and id_class=:idClass order by id desc limit 1)", nativeQuery = true)
    List<Long> findidClassList(Long idKid, Long idClass);

    @Query(value = "SELECT distinct id_kid from kids_class_date where id_class=:idClass and from_date <=:date and case when to_date is not null then to_date >=:date  else true end", nativeQuery = true)
    List<Long> findByDateAndClass(LocalDate date, Long idClass);
}
