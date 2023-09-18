package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.Address;
import ng.com.justjava.corebanking.service.dto.BeneficiaryDTO;
import ng.com.justjava.corebanking.domain.Beneficiary;
import ng.com.justjava.corebanking.domain.Profile;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Address}.
 */
public interface BeneficiaryService {

    /**
     * Save a address.
     *
     * @param beneficiaryDTO the entity to save.
     * @return the persisted entity.
     */

    BeneficiaryDTO save(BeneficiaryDTO beneficiaryDTO, Profile addressOwner);

    Beneficiary save(BeneficiaryDTO beneficiaryDTO);

    Beneficiary save(Beneficiary beneficiary);

    /**
     * Get all the addresses.
     *
     * @return the list of entities.
     */
    List<BeneficiaryDTO> findAll();


    /**
     * Get the "id" address.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BeneficiaryDTO> findOne(Long id);

    /**
     * Delete the "id" address.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<BeneficiaryDTO> findCustomerBeneficiaries(String phoneNumber);

    Optional<Beneficiary> findByAccountOwnerAndBankCode(Profile accountOwner, String accountNumber, String bankCode);

    Optional<Beneficiary> findByAccountOwnerAndAccountNumber(Profile accountOwner, String accountNumber);

}
