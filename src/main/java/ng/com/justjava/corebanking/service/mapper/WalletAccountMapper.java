package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.service.dto.WalletAccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link WalletAccount} and its DTO {@link WalletAccountDTO}.
 */
@Mapper(componentModel = "spring", uses = {SchemeMapper.class, WalletAccountTypeMapper.class, ProfileMapper.class})
public interface WalletAccountMapper extends EntityMapper<WalletAccountDTO, WalletAccount> {

    @Mapping(source = "scheme.id", target = "schemeId")
    @Mapping(source = "scheme.scheme", target = "schemeName")
    @Mapping(source = "walletAccountType.id", target = "walletAccountTypeId")
    @Mapping(source = "accountOwner.id", target = "accountOwnerId")
    @Mapping(source = "accountOwner.fullName", target = "accountOwnerName")
    @Mapping(source = "accountOwner.phoneNumber", target = "accountOwnerPhoneNumber")
    WalletAccountDTO toDto(WalletAccount walletAccount);

    @Mapping(source = "schemeId", target = "scheme")
    @Mapping(source = "walletAccountTypeId", target = "walletAccountType")
    @Mapping(source = "accountOwnerId", target = "accountOwner")
    WalletAccount toEntity(WalletAccountDTO walletAccountDTO);

    default WalletAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        WalletAccount walletAccount = new WalletAccount();
        walletAccount.setId(id);
        return walletAccount;
    }
}
