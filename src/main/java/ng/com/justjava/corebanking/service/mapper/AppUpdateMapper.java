package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.AppUpdate;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.justjava.corebanking.service.dto.AppUpdateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUpdate} and its DTO {@link AppUpdateDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AppUpdateMapper extends EntityMapper<AppUpdateDTO, AppUpdate> {



    default AppUpdate fromId(Long id) {
        if (id == null) {
            return null;
        }
        AppUpdate appUpdate = new AppUpdate();
        appUpdate.setId(id);
        return appUpdate;
    }
}
