package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.agent.Agent;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.usermaster.AgentMaster;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.master.request.agent.AgentMasterSearchRequest;
import com.example.onekids_project.master.request.agent.CreateAccountAgentRequest;
import com.example.onekids_project.master.response.agent.AgentMasterForAgentResponse;
import com.example.onekids_project.master.response.agent.AgentMasterResponse;
import com.example.onekids_project.master.response.agent.AgentMasterUpdateRequest;
import com.example.onekids_project.master.service.AccountAgentService;
import com.example.onekids_project.repository.AgentMasterRepository;
import com.example.onekids_project.repository.AgentRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.security.payload.AccountCreateData;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class AccountAgentServiceImpl implements AccountAgentService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private AgentMasterRepository agentMasterRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private MaUserService maUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MaUserRepository maUserRepository;


    @Transactional
    @Override
    public boolean createAccountAgent(CreateAccountAgentRequest createAccountAgentRequest) {
        AccountCreateData accountCreateData = new AccountCreateData();
        this.setPropertiesSignUp(accountCreateData, createAccountAgentRequest);
        MaUser maUser = maUserService.createAccountOther(accountCreateData);
        Agent agent = agentRepository.findByIdAndDelActiveTrue(createAccountAgentRequest.getIdAgent()).orElseThrow(() -> new NotFoundException("not found agent by id"));
        AgentMaster agentMaster = modelMapper.map(createAccountAgentRequest, AgentMaster.class);
        agentMaster.setMaUser(maUser);
        agentMaster.setAgent(agent);
        agentMasterRepository.save(agentMaster);
        return true;
    }

    @Override
    public List<AgentMasterResponse> getAllAgentMaster(AgentMasterSearchRequest agentMasterSearchRequest) {
        List<AgentMaster> agentMasterList = agentMasterRepository.findAllAgentMaster(agentMasterSearchRequest);
        List<AgentMasterResponse> agentMasterResponseList = listMapper.mapList(agentMasterList, AgentMasterResponse.class);
        return agentMasterResponseList;
    }

    @Override
    public List<AgentMasterForAgentResponse> getAccountAgentByIdAgent(Long idAgent) {
        List<AgentMaster> agentMasterList = agentMasterRepository.findByAgentIdAndDelActiveTrue(idAgent);
        List<AgentMasterForAgentResponse> agentMasterForAgentResponseList = listMapper.mapList(agentMasterList, AgentMasterForAgentResponse.class);
        return agentMasterForAgentResponseList;
    }

    @Transactional
    @Override
    public AgentMasterResponse updateAccountAgent(AgentMasterUpdateRequest agentMasterUpdateRequest) {
        AgentMaster agentMaster = agentMasterRepository.findByIdAndDelActiveTrue(agentMasterUpdateRequest.getId()).orElseThrow(() -> new NotFoundException("not found agentmaster by id"));
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(agentMasterUpdateRequest.getMaUser().getId()).orElseThrow(() -> new NotFoundException("not found mauser by id"));
        if (!maUser.getPasswordShow().equals(agentMasterUpdateRequest.getMaUser().getPasswordShow())) {
            agentMaster.getMaUser().setPasswordHash(passwordEncoder.encode(agentMasterUpdateRequest.getMaUser().getPasswordShow()));
        }
        /**
         * check username exist
         */
        if (!maUser.getUsername().equals(agentMasterUpdateRequest.getMaUser().getUsername())) {
            maUserService.checkExistUsernameAndAppType(agentMasterUpdateRequest.getMaUser().getUsername(), AppTypeConstant.AGENT);
        }
        agentMaster.getMaUser().setGender(agentMasterUpdateRequest.getGender());
        modelMapper.map(agentMasterUpdateRequest, agentMaster);
        AgentMaster agentMasterSaved = agentMasterRepository.save(agentMaster);
        AgentMasterResponse agentMasterResponse = modelMapper.map(agentMasterSaved, AgentMasterResponse.class);
        return agentMasterResponse;
    }

    @Override
    public boolean deleteAgentMaster(Long id) {
        AgentMaster agentMaster = agentMasterRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found agentmaster by id"));
        agentMaster.getMaUser().setDelActive(AppConstant.APP_FALSE);
        agentMaster.setDelActive(AppConstant.APP_FALSE);
        agentMasterRepository.save(agentMaster);
        return true;
    }


    private void setPropertiesSignUp(AccountCreateData accountCreateData, CreateAccountAgentRequest createAccountAgentRequest) {
        accountCreateData.setFullName(createAccountAgentRequest.getFullName());
        accountCreateData.setPhone(createAccountAgentRequest.getPhone());
        accountCreateData.setAppType(AppTypeConstant.AGENT);
        accountCreateData.setUsername(createAccountAgentRequest.getUsername());
        accountCreateData.setPassword(createAccountAgentRequest.getPassword());
        accountCreateData.setGender(createAccountAgentRequest.getGender());
    }

}
