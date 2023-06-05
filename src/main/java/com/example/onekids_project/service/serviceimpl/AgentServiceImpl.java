package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.AgentDTO;
import com.example.onekids_project.entity.agent.Agent;
import com.example.onekids_project.entity.agent.AgentSms;
import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.master.request.AgentSmsRequest;
import com.example.onekids_project.master.response.SmsAgentResponse;
import com.example.onekids_project.master.response.agent.BrandforAgentResponse;
import com.example.onekids_project.repository.AgentRepository;
import com.example.onekids_project.repository.AgentSmsRepository;
import com.example.onekids_project.repository.BrandRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.request.agent.CreateAgentRequest;
import com.example.onekids_project.request.agent.SearchAgentRequest;
import com.example.onekids_project.request.agent.UpdateAgentRequest;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.brand.FindAgentBrandRequest;
import com.example.onekids_project.response.agent.AgentOtherResponse;
import com.example.onekids_project.response.agent.AgentResponse;
import com.example.onekids_project.response.agent.AgentUniqueResponse;
import com.example.onekids_project.response.agent.ListAgentResponse;
import com.example.onekids_project.response.brandManagement.FindAgentBrandResponse;
import com.example.onekids_project.response.brandManagement.ListFindAgentBrandResponse;
import com.example.onekids_project.service.servicecustom.AgentService;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AgentSmsRepository agentSmsRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<AgentOtherResponse> findAllAgentOther() {
        List<Agent> agentList = agentRepository.findByDelActiveTrueOrderByAgentName();
        List<AgentOtherResponse> agentOtherResponseList = listMapper.mapList(agentList, AgentOtherResponse.class);
        return agentOtherResponseList;
    }

    @Override
    public List<AgentUniqueResponse> findAllAgentUnique() {
        List<Agent> agentList = agentRepository.findByDelActiveTrue();
        List<AgentUniqueResponse> agentUniqueResponseList = listMapper.mapList(agentList, AgentUniqueResponse.class);
        return agentUniqueResponseList;
    }

    @Override
    public ListAgentResponse findAllAgent(Pageable pageable) {
        List<Agent> agentList = agentRepository.findAllAgent(pageable);
        List<AgentResponse> agentDTOList = listMapper.mapList(agentList, AgentResponse.class);
        ListAgentResponse listAgentResponse = new ListAgentResponse();
        listAgentResponse.setAgentList(agentDTOList);
        return listAgentResponse;
    }

    @Override
    public Optional<AgentDTO> findByIdAgent(Long id) {
        Optional<Agent> agentOptional = agentRepository.findByIdAndDelActive(id, AppConstant.APP_TRUE);
        Optional<AgentDTO> agentDTOOptional = Optional.ofNullable(modelMapper.map(agentOptional.get(), AgentDTO.class));
        return agentDTOOptional;
    }

    @Override
    public ListAgentResponse searchAgent(Pageable pageable, SearchAgentRequest searchAgentRequest) {
        List<Agent> gradeList = agentRepository.searchAgent(pageable, searchAgentRequest);
        List<AgentResponse> agentDTOList = listMapper.mapList(gradeList, AgentResponse.class);
        ListAgentResponse listAgentResponse = new ListAgentResponse();
        listAgentResponse.setAgentList(agentDTOList);
        return listAgentResponse;
    }

    @Override
    @Transactional
    public AgentResponse createAgent(CreateAgentRequest createAgentRequest) {
        Agent newAgent = modelMapper.map(createAgentRequest, Agent.class);

        newAgent.setAgentCode(RandomStringUtils.random(4, true, true));//todo
        newAgent.setSmsBudgetDate(LocalDateTime.now());
        if (newAgent.isAgentActive()) {
            newAgent.setDateActive(LocalDate.now());
        } else {
            newAgent.setDateUnactive(LocalDate.now());
        }
        Agent savedAgent = agentRepository.save(newAgent);
        AgentResponse agentResponse = modelMapper.map(savedAgent, AgentResponse.class);
        return agentResponse;
    }

    @Override
    public AgentResponse updateAgent(UpdateAgentRequest updateAgentRequest) {
        Optional<Agent> gradeOptional = agentRepository.findByIdAndDelActive(updateAgentRequest.getId(), AppConstant.APP_TRUE);
        Agent odlAgent = gradeOptional.get();
        modelMapper.map(updateAgentRequest, odlAgent);
        if (odlAgent.isAgentActive()) {
            odlAgent.setDateActive(LocalDate.now());
        } else {
            odlAgent.setDateUnactive(LocalDate.now());
        }
        Agent newAgent = agentRepository.save(odlAgent);
        AgentResponse gradeEditResponse = modelMapper.map(newAgent, AgentResponse.class);

        return gradeEditResponse;
    }

    @Override
    public boolean deleteAgent(Long id) {
        Optional<Agent> gradeOptional = agentRepository.findByIdAndDelActive(id, true);

        Agent deleteAgent = gradeOptional.get();
        long sizeSchool = deleteAgent.getSchoolList().stream().filter(item -> item.isDelActive()).count();
        if (sizeSchool == 0) {
            deleteAgent.setDelActive(false);
            agentRepository.save(deleteAgent);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    @Override
    public boolean saveAgentSms(AgentSmsRequest agentSmsRequest) {
        if (agentSmsRequest.getSmsAdd() != 0) {
            List<AgentSms> agentSmsList = new ArrayList<>();
            agentSmsRequest.getIdAgentList().forEach(idAgent -> {
                AgentSms agentSms = new AgentSms();
                agentSms.setContent(agentSmsRequest.getContent());
                agentSms.setSmsAdd(agentSmsRequest.getSmsAdd());
                agentSms.setSmsDate(LocalDateTime.now());
                Agent agent = agentRepository.findByIdAndDelActive(idAgent, AppConstant.APP_TRUE).get();
                agent.setSmsTotal(agent.getSmsTotal() + agentSms.getSmsAdd());
                agentSms.setAgent(agent);
                agentSmsRepository.save(agentSms);
                agentRepository.save(agent);
            });
            return true;
        }
        return false;
    }

    @Override
    public List<SmsAgentResponse> findAgentSmsByIdAgent(Long idAgent) {
        List<AgentSms> agentSmsList = agentSmsRepository.findByAgentId(idAgent, Sort.by("id").descending());
        List<SmsAgentResponse> agentResponseList = new ArrayList<>();
        agentSmsList.stream().forEach(agentSms -> {
            SmsAgentResponse smsAgentResponse = new SmsAgentResponse();
            smsAgentResponse.setContent(agentSms.getContent());
            smsAgentResponse.setSmsDate(agentSms.getSmsDate().toString());
            smsAgentResponse.setNumberSms(agentSms.getSmsAdd());
            smsAgentResponse.setCreateBy(maUserRepository.findByIdAndDelActiveTrue(agentSms.getIdCreated()).get().getFullName());
            agentResponseList.add(smsAgentResponse);
        });
        return agentResponseList;
    }

    @Override
    public boolean updateMultiActiveAgent(List<Long> ids, boolean activeOrUnActive) {
        if (ids != null) {
            for (Long id : ids) {
                Agent agent = agentRepository.findById(id).get();
                agent.setAgentActive(activeOrUnActive);
                agentRepository.save(agent);
            }
        }
        return true;
    }

    @Override
    public ListFindAgentBrandResponse findAllAgentMaster(FindAgentBrandRequest request) {
        ListFindAgentBrandResponse response = new ListFindAgentBrandResponse();
        List<Agent> agentList = agentRepository.findAgent(request);
        long total = agentRepository.countTotalAccount(request);
        List<FindAgentBrandResponse> dataList = listMapper.mapList(agentList, FindAgentBrandResponse.class);
        response.setDataList(dataList);
        response.setTotal(total);
        return response;
    }

    @Override
    public List<BrandforAgentResponse> findBrandforAgent(Long idAgent) {
        agentRepository.findByIdAndDelActiveTrue(idAgent);
        List<Brand> brandList = brandRepository.findAll();
        List<Long> brandForAgentList = brandRepository.findByAgentList_Id(idAgent).stream().map(x -> x.getId()).collect(Collectors.toList());
        List<BrandforAgentResponse> brandforAgentResponseList = listMapper.mapList(brandList, BrandforAgentResponse.class);
        brandforAgentResponseList.forEach(y -> {
            brandForAgentList.forEach(z -> {
                if (y.getId().equals(z)) {
                    y.setUsed(AppConstant.APP_TRUE);
                }
            });
        });
        return brandforAgentResponseList;
    }

    @Transactional
    @Override
    public boolean updateBrandForAgent(Long idAgent, List<IdObjectRequest> idObjectRequestList) {
        agentRepository.findByIdAndDelActiveTrue(idAgent).orElseThrow(() -> new NotFoundException("not found agent by id"));
        agentRepository.deleteAgentBrand(idAgent);
        idObjectRequestList.forEach(x -> agentRepository.insertAgentBrand(idAgent, x.getId()));
        return true;
    }
}
