package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.ApprovalWorkflowRepository;
import ng.com.justjava.corebanking.service.mapper.ApprovalWorkflowMapper;
import ng.com.justjava.corebanking.domain.ApprovalGroup;
import ng.com.justjava.corebanking.domain.ApprovalWorkflow;
import ng.com.justjava.corebanking.domain.Right;
import ng.com.justjava.corebanking.service.ApprovalWorkflowService;
import ng.com.justjava.corebanking.service.RightService;
import ng.com.justjava.corebanking.service.dto.ApprovalWorkflowDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ApprovalWorkflow}.
 */
@Service
@Transactional
public class ApprovalWorkflowServiceImpl implements ApprovalWorkflowService {

    private final Logger log = LoggerFactory.getLogger(ApprovalWorkflowServiceImpl.class);

    private final ApprovalWorkflowRepository approvalWorkflowRepository;

    private final ApprovalWorkflowMapper approvalWorkflowMapper;
    private final RightService rightService;

    public ApprovalWorkflowServiceImpl(ApprovalWorkflowRepository approvalWorkflowRepository, ApprovalWorkflowMapper approvalWorkflowMapper, RightService rightService) {
        this.approvalWorkflowRepository = approvalWorkflowRepository;
        this.approvalWorkflowMapper = approvalWorkflowMapper;
        this.rightService = rightService;
    }

    @Override
    public ApprovalWorkflowDTO save(ApprovalWorkflowDTO approvalWorkflowDTO) {
        log.debug("Request to save ApprovalWorkflow : {}", approvalWorkflowDTO);

        if (approvalWorkflowDTO.getId() == null) {

            ApprovalWorkflow byName = approvalWorkflowRepository.findByName(approvalWorkflowDTO.getName());
            ApprovalWorkflow workflow = approvalWorkflowRepository.findByTransactionType_code(approvalWorkflowDTO.getTransactionTypeCode());
            if (byName != null || workflow != null) {
                return null;
            }

        }
        ApprovalWorkflow approvalWorkflow = approvalWorkflowMapper.toEntity(approvalWorkflowDTO);
        String transactionTypeCode = approvalWorkflowDTO.getTransactionTypeCode();
        Optional<Right> oneByCode = rightService.findOneByCode(transactionTypeCode);
        if (oneByCode.isPresent()) {
            approvalWorkflow.setTransactionType(oneByCode.get());
            approvalWorkflow = approvalWorkflowRepository.save(approvalWorkflow);
            return approvalWorkflowMapper.toDto(approvalWorkflow);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalWorkflowDTO> findAll() {
        log.debug("Request to get all ApprovalWorkflows");
        return approvalWorkflowRepository.findAll().stream()
            .map(approvalWorkflowMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ApprovalWorkflowDTO> findOne(Long id) {
        log.debug("Request to get ApprovalWorkflow : {}", id);
        return approvalWorkflowRepository.findById(id)
            .map(approvalWorkflowMapper::toDto);
    }

    @Override
    public String delete(Long id) {
        log.debug("Request to delete ApprovalWorkflow : {}", id);
        Optional<ApprovalWorkflow> byId = approvalWorkflowRepository.findById(id);
        if (byId.isPresent()) {
            approvalWorkflowRepository.deleteById(id);
            return "success";
        }
        return "Invalid ID";
    }

    @Override
    public ApprovalWorkflow findByTransactionType_code(String requestTypeCode) {
        return approvalWorkflowRepository.findByTransactionType_code(requestTypeCode.trim());
    }

    @Override
    public List<ApprovalWorkflow> findByApprover(@NotNull ApprovalGroup approver) {
        return approvalWorkflowRepository.findByApprover(approver);
    }

    @Override
    public List<ApprovalWorkflow> findByInitiator(@NotNull ApprovalGroup initiator) {
        return approvalWorkflowRepository.findByInitiator(initiator);
    }
}
