package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.VehicleInsuranceRequest;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.justjava.corebanking.service.dto.VehicleInsuranceRequestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link VehicleInsuranceRequest} and its DTO {@link VehicleInsuranceRequestDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class, InsuranceTypeMapper.class})
public interface VehicleInsuranceRequestMapper extends EntityMapper<VehicleInsuranceRequestDTO, VehicleInsuranceRequest> {

    @Mapping(source = "profile.id", target = "profileId")
    @Mapping(source = "insuranceType.id", target = "insuranceTypeId")
    VehicleInsuranceRequestDTO toDto(VehicleInsuranceRequest vehicleInsuranceRequest);

    @Mapping(source = "profileId", target = "profile")
    @Mapping(source = "insuranceTypeId", target = "insuranceType")
    VehicleInsuranceRequest toEntity(VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO);

    default VehicleInsuranceRequest fromId(Long id) {
        if (id == null) {
            return null;
        }
        VehicleInsuranceRequest vehicleInsuranceRequest = new VehicleInsuranceRequest();
        vehicleInsuranceRequest.setId(id);
        return vehicleInsuranceRequest;
    }
}
