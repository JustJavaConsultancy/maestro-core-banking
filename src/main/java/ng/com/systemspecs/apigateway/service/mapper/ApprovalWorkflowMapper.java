package ng.com.systemspecs.apigateway.service.mapper;


import ng.com.systemspecs.apigateway.domain.*;
import ng.com.systemspecs.apigateway.service.dto.ApprovalWorkflowDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApprovalWorkflow} and its DTO {@link ApprovalWorkflowDTO}.
 */
@Mapper(componentModel = "spring", uses = {ApprovalGroupMapper.class})
public interface ApprovalWorkflowMapper extends EntityMapper<ApprovalWorkflowDTO, ApprovalWorkflow> {

    @Mapping(source = "initiator.id", target = "initiatorId")
    @Mapping(source = "initiator.name", target = "initiatorName")
    @Mapping(source = "approver.id", target = "approverId")
    @Mapping(source = "approver.name", target = "approverName")
    @Mapping(source = "transactionType.name", target = "transactionTypeName")
    @Mapping(source = "transactionType.id", target = "transactionTypeId")
    @Mapping(source = "transactionType.code", target = "transactionTypeCode")
    ApprovalWorkflowDTO toDto(ApprovalWorkflow approvalWorkflow);

    @Mapping(source = "initiatorId", target = "initiator")
    @Mapping(source = "approverId", target = "approver")
    ApprovalWorkflow toEntity(ApprovalWorkflowDTO approvalWorkflowDTO);

    default ApprovalWorkflow fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApprovalWorkflow approvalWorkflow = new ApprovalWorkflow();
        approvalWorkflow.setId(id);
        return approvalWorkflow;
    }
}
