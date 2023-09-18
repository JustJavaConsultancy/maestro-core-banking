package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.AccessItem;
import ng.com.justjava.corebanking.service.dto.AccessItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link AccessItem} and its DTO {@link AccessItemDTO}.
 */
@Mapper(componentModel = "spring", uses ={})
public interface AccessItemMapper extends EntityMapper<AccessItemDTO, AccessItem> {


    @Mapping(source = "right.code", target = "rightCode")
    @Mapping(source = "accessRight.name", target = "accessRightName")
    AccessItemDTO toDto(AccessItem accessItem);


    default AccessItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        AccessItem accessItem = new AccessItem();
        accessItem.setId(id);
        return accessItem;
    }
}
