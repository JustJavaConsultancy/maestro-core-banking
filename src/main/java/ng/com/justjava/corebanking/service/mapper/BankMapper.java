package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.Bank;
import ng.com.justjava.corebanking.service.dto.BankDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Bank} and its DTO {@link BankDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BankMapper extends EntityMapper<BankDTO, Bank> {

    default Bank fromId(Long id) {
        if (id == null) {
            return null;
        }
        Bank bank = new Bank();
        bank.setId(id);
        return bank;
    }
}
