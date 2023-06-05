package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.KidsTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author lavanviet
 */
public interface KidsTransferRepository extends JpaRepository<KidsTransfer, Long> {
    Optional<KidsTransfer> findByIdAndDelActiveTrue(Long id);
    List<KidsTransfer> findByKidsIdAndDelActiveTrueOrderByIdDesc(Long idKid);
    List<KidsTransfer> findByKidsIdInAndDelActiveTrue(List<Long> idKidList);
    List<KidsTransfer> findByKidsIdAndInStatusTrueAndDelActiveTrue(Long idKid);
    List<KidsTransfer> findByKidsIdAndOutStatusTrueAndDelActiveTrue(Long idKid);

    List<KidsTransfer> findByKidsIdAndDelActiveTrueOrderByInStatusDescOutStatusDescIdDesc(Long idKid);
    List<KidsTransfer> findByKidsIdAndDelActiveTrueOrderByOutStatusDescInStatusDescIdDesc(Long idKid);
}
