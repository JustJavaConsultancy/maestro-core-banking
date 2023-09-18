package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.ApprovalGroup;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.justjava.corebanking.service.dto.ApprovalGroupDTO;

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
