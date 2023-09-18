package ng.com.justjava.corebanking.service.mapper;

import ng.com.justjava.corebanking.domain.PolarisCardProfile;
import ng.com.justjava.corebanking.service.dto.PolarisCardProfileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface PolarisCardProfileMapper extends EntityMapper<PolarisCardProfileDTO, PolarisCardProfile> {

}
