package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.WalletAccountType;
import ng.com.justjava.corebanking.service.dto.WalletAccountTypeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link WalletAccountType} and its DTO {@link WalletAccountTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WalletAccountTypeMapper extends EntityMapper<WalletAccountTypeDTO, WalletAccountType> {

    default WalletAccountType fromId(Long id) {
        if (id == null) {
            return null;
        }
        WalletAccountType walletAccountType = new WalletAccountType();
        walletAccountType.setId(id);
        return walletAccountType;
    }
}
