package ng.com.systemspecs.apigateway.service.mapper;


import ng.com.systemspecs.apigateway.domain.*;
import ng.com.systemspecs.apigateway.service.dto.InsuranceTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link InsuranceType} and its DTO {@link InsuranceTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InsuranceTypeMapper extends EntityMapper<InsuranceTypeDTO, InsuranceType> {

    default InsuranceType fromId(Long id) {
        if (id == null) {
            return null;
        }
        InsuranceType insuranceType = new InsuranceType();
        insuranceType.setId(id);
        return insuranceType;
    }
}
