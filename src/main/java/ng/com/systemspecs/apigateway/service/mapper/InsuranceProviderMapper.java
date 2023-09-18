package ng.com.systemspecs.apigateway.service.mapper;


import ng.com.systemspecs.apigateway.domain.InsuranceProvider;
import ng.com.systemspecs.apigateway.service.dto.InsuranceProviderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link InsuranceProvider} and its DTO {@link InsuranceProviderDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface InsuranceProviderMapper extends EntityMapper<InsuranceProviderDTO, InsuranceProvider> {

    @Mapping(source = "profile.id", target = "profileId")
    @Mapping(source = "profile.phoneNumber", target = "profilePhoneNumber")
    InsuranceProviderDTO toDto(InsuranceProvider insuranceProvider);

    @Mapping(source = "profileId", target = "profile")
    InsuranceProvider toEntity(InsuranceProviderDTO insuranceProviderDTO);

    default InsuranceProvider fromId(Long id) {
        if (id == null) {
            return null;
        }
        InsuranceProvider insuranceProvider = new InsuranceProvider();
        insuranceProvider.setId(id);
        return insuranceProvider;
    }
}
