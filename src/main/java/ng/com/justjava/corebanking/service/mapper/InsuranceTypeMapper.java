package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.InsuranceType;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.justjava.corebanking.service.dto.InsuranceTypeDTO;

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
