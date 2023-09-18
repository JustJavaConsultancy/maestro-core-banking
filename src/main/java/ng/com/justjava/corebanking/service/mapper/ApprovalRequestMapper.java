package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.ApprovalRequest;
import ng.com.justjava.corebanking.service.dto.ApprovalRequestDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ApprovalRequest} and its DTO {@link ApprovalRequestDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface ApprovalRequestMapper extends EntityMapper<ApprovalRequestDTO, ApprovalRequest> {

    ApprovalRequestDTO toDto(ApprovalRequest approvalRequest);

    default ApprovalRequest fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setId(id);
        return approvalRequest;
    }
}
