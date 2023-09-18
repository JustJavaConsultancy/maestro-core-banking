package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.ApprovalGroupRepository;
import ng.com.justjava.corebanking.service.mapper.ApprovalGroupMapper;
import ng.com.justjava.corebanking.service.mapper.ApprovalWorkflowMapper;
import ng.com.justjava.corebanking.domain.ApprovalGroup;
import ng.com.justjava.corebanking.domain.ApprovalWorkflow;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.Right;
import ng.com.justjava.corebanking.service.ApprovalGroupService;
import ng.com.justjava.corebanking.service.ApprovalWorkflowService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.dto.ApprovalGroupDTO;
import ng.com.justjava.corebanking.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ApprovalGroup}.
 */
@Service
@Transactional
public class ApprovalGroupServiceImpl implements ApprovalGroupService {

    private final Logger log = LoggerFactory.getLogger(ApprovalGroupServiceImpl.class);

    private final ApprovalGroupRepository approvalGroupRepository;
    private final ApprovalWorkflowService approvalWorkflowService;
    private final ApprovalWorkflowMapper approvalWorkflowMapper;
    private final ProfileService profileService;
    private final Utility utility;

    private final ApprovalGroupMapper approvalGroupMapper;

    public ApprovalGroupServiceImpl(ApprovalGroupRepository approvalGroupRepository, ApprovalWorkflowService approvalWorkflowService, ApprovalWorkflowMapper approvalWorkflowMapper, ProfileService profileService, Utility utility, ApprovalGroupMapper approvalGroupMapper) {
        this.approvalGroupRepository = approvalGroupRepository;
        this.approvalWorkflowService = approvalWorkflowService;
        this.approvalWorkflowMapper = approvalWorkflowMapper;
        this.profileService = profileService;
        this.utility = utility;
        this.approvalGroupMapper = approvalGroupMapper;
    }

    @Override
    public ApprovalGroupDTO save(ApprovalGroupDTO approvalGroupDTO) {
        log.debug("Request to save ApprovalGroup : {}", approvalGroupDTO);
        if (approvalGroupDTO.getId() == null) {
            ApprovalGroup byName = findByName(approvalGroupDTO.getName());
            if (byName != null) {
                return null;
            }
        }

        ApprovalGroup approvalGroup = approvalGroupMapper.toEntity(approvalGroupDTO);

        approvalGroup = approvalGroupRepository.save(approvalGroup);

        List<String> phoneNumbers = approvalGroupDTO.getPhoneNumbers();
        for (String phone : phoneNumbers) {
            Profile profile = profileService.findByPhoneNumber(utility.formatPhoneNumber(phone));
            if (profile != null) {
                approvalGroup.addProfile(profile);
            } else {
                return null;
            }
        }
        return approvalGroupMapper.toDto(approvalGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalGroupDTO> findAll() {
        log.debug("Request to get all ApprovalGroups");
        return approvalGroupRepository.findAll().stream()
            .map(approvalGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ApprovalGroupDTO> findOne(Long id) {
        log.debug("Request to get ApprovalGroup : {}", id);
        return approvalGroupRepository.findById(id)
            .map(approvalGroupMapper::toDto);
    }

    @Override
    public String delete(Long id) {
        log.debug("Request to delete ApprovalGroup : {}", id);

        Optional<ApprovalGroup> approvalGroupOptional = approvalGroupRepository.findById(id);
        if (approvalGroupOptional.isPresent()) {

            ApprovalGroup approvalGroup = approvalGroupOptional.get();
            List<ApprovalWorkflow> byApprover = approvalWorkflowService.findByApprover(approvalGroup);

            List<ApprovalWorkflow> byInitiator = approvalWorkflowService.findByInitiator(approvalGroup);

            if (!byApprover.isEmpty()) {
                return "Approval group already assigned to a workflow as approval";
            }

            if (!byInitiator.isEmpty()) {
                return "Approval group already assigned to a workflow as initiator";
            }

            approvalGroup = removeApprovalGroupProfiles(approvalGroup);

            approvalGroupRepository.delete(approvalGroup);

            return "success";
        }
        return "Inavlid id";
    }

    public ApprovalGroup removeApprovalGroupProfiles(ApprovalGroup approvalGroup) {
        Set<Profile> profiles = approvalGroup.getProfiles();

        log.info("Profiles ===> " + profiles);

        List<Profile> profileArrayList = new ArrayList<>(profiles);

        for (Profile profile : profileArrayList) {
            log.info("Profile ===> " + profile);
            approvalGroup = approvalGroup.removeProfile(profile);
            log.info("approvalGroup ==> " + approvalGroup);
        }
        return approvalGroupRepository.save(approvalGroup);

    }

    @Override
    public ApprovalGroup findByName(String name) {
        return approvalGroupRepository.findByName(name);
    }

    @Override
    public List<Right> getRights(String phoneNumber) {
        Profile profile = profileService.findByPhoneNumber(utility.formatPhoneNumber(phoneNumber));
        ApprovalGroup approvalGroup = profile.getApprovalGroup();
        List<ApprovalWorkflow> workflowList = approvalWorkflowService.findByInitiator(approvalGroup);

        List<Right> rights = new ArrayList<>();
        workflowList.forEach(approvalWorkflow -> {
            rights.add(approvalWorkflow.getTransactionType());
        });

        return rights;

    }
}
