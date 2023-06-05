package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.Api;
import com.example.onekids_project.repository.repositorycustom.ApiRepositoryCustom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiRepository extends CrudRepository<Api, Long>, ApiRepositoryCustom {
    /**
     * tìm kiếm api theo id
     * @param id
     * @return
     */
    Optional<Api> findByIdAndDelActiveTrue(Long id);

    /**
     * tìm kiếm tất cả api
     * @return
     */
    List<Api> findByDelActiveTrue();
    /**
     * tìm kiếm tất cả api
     * @return
     */
    List<Api> findByTypeAndDelActiveTrueOrderByRanks(String type);

    List<Api> findByIdInAndDelActiveTrue(List<Long> idList);

    List<Api> findByIdGreaterThanEqualAndIdLessThanEqual(long minId, long maxId);

}
