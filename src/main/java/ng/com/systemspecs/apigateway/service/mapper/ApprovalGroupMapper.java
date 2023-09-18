package ng.com.systemspecs.apigateway.service.mapper;


import ng.com.systemspecs.apigateway.domain.*;
import ng.com.systemspecs.apigateway.service.dto.ApprovalGroupDTO;

import ng.com.systemspecs.apigateway.service.dto.ApprovalWorkflowDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApprovalGroup} and its DTO {@link ApprovalGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApprovalGroupMapper extends EntityMapper<ApprovalGroupDTO, ApprovalGroup> {

    @Mapping(source = "profiles", target = "profiles")
    ApprovalGroupDTO toDto(ApprovalGroup approvalGroup);

    ApprovalGroup toEntity(ApprovalGroupDTO approvalGroupDTO);

    default ApprovalGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApprovalGroup approvalGroup = new ApprovalGroup();
        approvalGroup.setId(id);
        return approvalGroup;
    }
}
