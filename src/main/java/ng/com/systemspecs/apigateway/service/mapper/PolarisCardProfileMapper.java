package ng.com.systemspecs.apigateway.service.mapper;

import ng.com.systemspecs.apigateway.domain.PolarisCardProfile;
import ng.com.systemspecs.apigateway.service.dto.PolarisCardProfileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface PolarisCardProfileMapper extends EntityMapper<PolarisCardProfileDTO, PolarisCardProfile> {

}
