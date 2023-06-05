package com.example.onekids_project.repository;

import com.example.onekids_project.entity.agent.Agent;
import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.repository.repositorycustom.AgentBrandRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentBrandRepository extends JpaRepository<Brand, Long>, AgentBrandRepositoryCustom {

//    có thể code theo 2 kiểu này
//    @Query(value = "select * from ma_agent where id=:id and del_active=:delActive", nativeQuery = true)
//    Optional<Agent> findByIdAndDelActive(@Param("id") Long id, @Param("delActive") boolean delActive);

    /**
     * tìm kiếm đại lý theo id
     * @param id
     * @param delActive
     * @return
     */
    Optional<Agent> findByIdAndDelActive(Long id, boolean delActive);
}
