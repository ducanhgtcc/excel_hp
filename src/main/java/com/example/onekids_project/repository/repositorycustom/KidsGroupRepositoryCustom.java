package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.KidsGroup;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface KidsGroupRepositoryCustom {
    /**
     * tìm kiếm
     *
     * @param idSchool
     * @param request
     * @return
     */
    List<KidsGroup> findAllKidsGroup(Long idSchool, PageNumberWebRequest request);

    long countKidsGroup(Long idSchool);

    /**
     * tìm kiếm nhóm học sinh
     *
     * @param idSchool
     * @param id
     * @return
     */
    Optional<KidsGroup> findByIdKidsGroup(Long idSchool, Long id);

//    /**
//     * chuyển đổi giữa các học sinh trong nhóm
//     * @param idGroup
//     * @param idKidsList
//     * @return
//     */
//    boolean transferKidsGroup(Long idGroup, List<Long> idKidsList);
}
