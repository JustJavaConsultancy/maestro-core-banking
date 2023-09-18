package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.Beneficiary;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.service.dto.BeneficiaryDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.Address}.
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
