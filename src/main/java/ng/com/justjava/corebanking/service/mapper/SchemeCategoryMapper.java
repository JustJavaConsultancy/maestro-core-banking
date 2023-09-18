package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.SchemeCategory;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.justjava.corebanking.service.dto.SchemeCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchemeCategory} and its DTO {@link SchemeCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SchemeCategoryMapper extends EntityMapper<SchemeCategoryDTO, SchemeCategory> {



    default SchemeCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        SchemeCategory schemeCategory = new SchemeCategory();
        schemeCategory.setId(id);
        return schemeCategory;
    }
}
