package com.example.onekids_project.repository;

import com.example.onekids_project.entity.agent.Agent;
import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.repository.repositorycustom.AgentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long>, AgentRepositoryCustom {

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

    /**
     * find by id va chua bi xoa
     * @param id
     * @return
     */
    Optional<Agent> findByIdAndDelActiveTrue(Long id);

    /**
     * find all agent
     * @return
     */
    List<Agent> findByDelActiveTrue();
    /**
     * find all agent
     * @return
     */
    List<Agent> findByDelActiveTrueOrderByAgentName();

    @Modifying
    @Query(value = "delete from ex_agent_brand where id_agent=:idAgent", nativeQuery = true)
    void deleteAgentBrand(Long idAgent);

    @Modifying
    @Query(value = "insert into ex_agent_brand(id_agent, id_brand) value(:idAgent, :idBrand)", nativeQuery = true)
    void insertAgentBrand(Long idAgent, Long idBrand);

    @Query(value = "select id_brand from ex_agent_brand where id_agent=:idAgent", nativeQuery = true)
    List<Long> findBrandByIdAgent(Long idAgent);
}
