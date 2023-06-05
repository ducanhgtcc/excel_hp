package com.example.onekids_project.entity.agent;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.usermaster.AgentMaster;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_agent")
public class Agent extends BaseEntity<String> {

    @Column(nullable = false, unique = true)
    private String agentCode;

    @Column(nullable = false)
    private String agentName;

    @Column(length = 50)
    private String agentEmail;

    @Column(length = 15)
    private String agentPhone;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean agentActive = AppConstant.APP_TRUE;

    private String agentAddress;

    private String agentWebsite;

    @Column(length = 8000)
    private String agentDescription;

    private String contactName;

    @Column(length = 50)
    private String contactEmail;

    @Column(length = 15)
    private String contactPhone;

    private long smsBudget;

    private LocalDateTime smsBudgetDate;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean smsActiveMore;

    private long smsUsed;

    private long smsTotal;

    private LocalDate demoStart;

    private LocalDate demoEnd;

    private LocalDate dateContractStart;

    private LocalDate dateContractEnd;

    private LocalDate dateStart;

    private LocalDate dateEnd;

    private LocalDate dateActive;

    private LocalDate dateUnactive;

    private String website;

    @JsonManagedReference
    @OneToMany(mappedBy = "agent", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AgentSms> agentSmsList;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "ex_agent_brand",
            joinColumns = @JoinColumn(name = "id_agent"),
            inverseJoinColumns = @JoinColumn(name = "id_brand")
    )
    @JsonManagedReference
    private List<Brand> brandList;

    @JsonManagedReference
    @OneToMany(mappedBy = "agent", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<School> schoolList;

    @OneToMany(mappedBy = "agent", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AgentMaster> agentMasterList;
}
