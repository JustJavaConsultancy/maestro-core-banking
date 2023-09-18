package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.WalletAccountTypeDTO;
import ng.com.justjava.corebanking.domain.WalletAccountType;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link WalletAccountType}.
 */
public interface WalletAccountTypeService {

    /**
     * Save a walletAccountType.
     *
     * @param walletAccountTypeDTO the entity to save.
     * @return the persisted entity.
     */
    WalletAccountTypeDTO save(WalletAccountTypeDTO walletAccountTypeDTO);

    /**
     * Get all the walletAccountTypes.
     *
     * @return the list of entities.
     */
    List<WalletAccountTypeDTO> findAll();


    /**
     * Get the "id" walletAccountType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WalletAccountTypeDTO> findOne(Long id);

    /**
     * Get the "id" walletAccountType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WalletAccountType> findOneById(Long id);


    /**
     * Get the "accountTypeId" walletAccountType.
     *
     * @param accountTypeId the id of the entity.
     * @return the entity.
     */
    Optional<WalletAccountTypeDTO> findByAccountTypeId(Long accountTypeId);


    /**
     * Delete the "id" walletAccountType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
