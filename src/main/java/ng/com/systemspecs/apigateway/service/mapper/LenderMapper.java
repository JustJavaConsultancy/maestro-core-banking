package ng.com.systemspecs.apigateway.service.mapper;


import ng.com.systemspecs.apigateway.domain.Lender;
import ng.com.systemspecs.apigateway.service.dto.LenderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Lender} and its DTO {@link LenderDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface LenderMapper extends EntityMapper<LenderDTO, Lender> {

    @Mapping(source = "profile.id", target = "profileId")
    @Mapping(source = "profile.phoneNumber", target = "profilePhoneNumber")
    @Mapping(source = "profile.fullName", target = "name")
    LenderDTO toDto(Lender lender);

    @Mapping(source = "profileId", target = "profile")
    Lender toEntity(LenderDTO lenderDTO);

    default Lender fromId(Long id) {
        if (id == null) {
            return null;
        }
        Lender lender = new Lender();
        lender.setId(id);
        return lender;
    }
}
