package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.Address;
import ng.com.justjava.corebanking.service.dto.AddressDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {

    AddressDTO toDto(Address address);

    default Address fromId(Long id) {
        if (id == null) {
            return null;
        }
        Address address = new Address();
        address.setId(id);
        return address;
    }
}
