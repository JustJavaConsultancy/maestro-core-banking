package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.Address;
import ng.com.justjava.corebanking.domain.Beneficiary;
import ng.com.justjava.corebanking.service.dto.AddressDTO;
import ng.com.justjava.corebanking.service.dto.BeneficiaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface BeneficiaryMapper extends EntityMapper<BeneficiaryDTO, Beneficiary> {

    @Mapping(source = "accountOwner.id", target = "accountOwnerId")
    BeneficiaryDTO toDto(Beneficiary beneficiary);

    @Mapping(source = "accountOwnerId", target = "accountOwner")
    Beneficiary toEntity(BeneficiaryDTO beneficiaryDTO);

    default Beneficiary fromId(Long id) {
        if (id == null) {
            return null;
        }
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(id);
        return beneficiary;
    }
}
