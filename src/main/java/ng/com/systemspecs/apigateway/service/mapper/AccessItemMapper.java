package ng.com.systemspecs.apigateway.service.mapper;


import ng.com.systemspecs.apigateway.domain.AccessItem;
import ng.com.systemspecs.apigateway.domain.Address;
import ng.com.systemspecs.apigateway.domain.Right;
import ng.com.systemspecs.apigateway.domain.WalletAccount;
import ng.com.systemspecs.apigateway.service.dto.AccessItemDTO;
import ng.com.systemspecs.apigateway.service.dto.AddressDTO;
import ng.com.systemspecs.apigateway.service.dto.WalletAccountDTO;
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
