package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.Customersubscription;
import ng.com.justjava.corebanking.service.dto.CustomersubscriptionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Customersubscription} and its DTO {@link CustomersubscriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class, BillerMapper.class})
public interface CustomersubscriptionMapper extends EntityMapper<CustomersubscriptionDTO, Customersubscription> {

    @Mapping(source = "phoneNumber.id", target = "phoneNumberId")
    @Mapping(source = "biller.id", target = "billerId")
    CustomersubscriptionDTO toDto(Customersubscription customersubscription);

    @Mapping(source = "phoneNumberId", target = "phoneNumber")
    @Mapping(source = "billerId", target = "biller")
    Customersubscription toEntity(CustomersubscriptionDTO customersubscriptionDTO);

    default Customersubscription fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customersubscription customersubscription = new Customersubscription();
        customersubscription.setId(id);
        return customersubscription;
    }
}
