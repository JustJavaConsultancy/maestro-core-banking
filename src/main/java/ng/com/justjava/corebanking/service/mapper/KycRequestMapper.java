package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.KycRequest;
import ng.com.justjava.corebanking.service.dto.KycRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link KycRequest} and its DTO {@link KycRequestDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface KycRequestMapper extends EntityMapper<KycRequestDTO, KycRequest> {

    @Mapping(source = "profile.bvn", target = "bvn")
    @Mapping(source = "profile.nin", target = "nin")
    @Mapping(source = "requestDocType", target = "documentType")
    @Mapping(source = "profile.id", target = "profileId")
    @Mapping(source = "profile.phoneNumber", target = "profilePhoneNumber")
    @Mapping(source = "profile.fullName", target = "profileFullName")
    @Mapping(source = "senderProfile.id", target = "senderProfileId")
    @Mapping(source = "senderProfile.phoneNumber", target = "senderProfilePhoneNumber")
    @Mapping(source = "senderProfile.fullName", target = "senderProfileFullName")
    @Mapping(source = "approver.id", target = "approverId")
    @Mapping(source = "approver.phoneNumber", target = "approverPhoneNumber")
    @Mapping(source = "approver.fullName", target = "approverFullName")
    @Mapping(source = "docDateIssued", target = "dateissued")
    KycRequestDTO toDto(KycRequest kycRequest);

    @Mapping(source = "profileId", target = "profile")
    @Mapping(source = "senderProfileId", target = "senderProfile")
    @Mapping(source = "approverId", target = "approver")
    @Mapping(source = "documentType", target = "requestDocType")
    @Mapping(source = "dateissued", target = "docDateIssued")
    KycRequest toEntity(KycRequestDTO kycRequestDTO);

    default KycRequest fromId(Long id) {
        if (id == null) {
            return null;
        }
        KycRequest kycRequest = new KycRequest();
        kycRequest.setId(id);
        return kycRequest;
    }
}
