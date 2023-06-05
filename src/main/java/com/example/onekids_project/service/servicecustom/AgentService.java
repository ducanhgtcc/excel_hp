package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.AgentDTO;
import com.example.onekids_project.master.request.AgentSmsRequest;
import com.example.onekids_project.master.response.SmsAgentResponse;
import com.example.onekids_project.master.response.agent.BrandforAgentResponse;
import com.example.onekids_project.request.agent.CreateAgentRequest;
import com.example.onekids_project.request.agent.SearchAgentRequest;
import com.example.onekids_project.request.agent.UpdateAgentRequest;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.brand.FindAgentBrandRequest;
import com.example.onekids_project.response.agent.AgentOtherResponse;
import com.example.onekids_project.response.agent.AgentResponse;
import com.example.onekids_project.response.agent.AgentUniqueResponse;
import com.example.onekids_project.response.agent.ListAgentResponse;
import com.example.onekids_project.response.brandManagement.ListFindAgentBrandResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AgentService {


    /**
     * find all agent
     *
     * @return
     */
    List<AgentOtherResponse> findAllAgentOther();

    /**
     * find all agent
     *
     * @return
     */
    List<AgentUniqueResponse> findAllAgentUnique();

    /**
     * tìm kiếm tất cả đại lý
     *
     * @param pageable
     * @return
     */
    ListAgentResponse findAllAgent(Pageable pageable);

    /**
     * tìm kiếm đại lý theo id
     *
     * @param id
     * @return
     */
    Optional<AgentDTO> findByIdAgent(Long id);

    /**
     * tìm kiếm đai lý theo tùy chọn
     *
     * @param pageable
     * @param searchAgentRequest
     * @return
     */
    ListAgentResponse searchAgent(Pageable pageable, SearchAgentRequest searchAgentRequest);

    /**
     * thêm mới đại lý
     *
     * @param createAgentRequest
     * @return
     */
    AgentResponse createAgent(CreateAgentRequest createAgentRequest);

    /**
     * cập nhật đại lý
     *
     * @param updateAgentRequest
     * @return
     */
    AgentResponse updateAgent(UpdateAgentRequest updateAgentRequest);

    /**
     * xóa đại lý
     *
     * @param id
     * @return
     */
    boolean deleteAgent(Long id);


    boolean saveAgentSms(AgentSmsRequest agentSmsRequest);

    List<SmsAgentResponse> findAgentSmsByIdAgent(Long idAgent);

    boolean updateMultiActiveAgent(List<Long> ids, boolean activeOrUnActive);

    ListFindAgentBrandResponse findAllAgentMaster(FindAgentBrandRequest request);

    List<BrandforAgentResponse> findBrandforAgent(Long idAgent);

    boolean updateBrandForAgent(Long idAgent, List<IdObjectRequest> idObjectRequestList);
}
