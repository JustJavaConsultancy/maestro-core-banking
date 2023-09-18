package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.ProfileType;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.justjava.corebanking.service.dto.ProfileTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfileType} and its DTO {@link ProfileTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProfileTypeMapper extends EntityMapper<ProfileTypeDTO, ProfileType> {



    default ProfileType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProfileType profileType = new ProfileType();
        profileType.setId(id);
        return profileType;
    }
}
