package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.Bank;
import ng.com.systemspecs.apigateway.service.dto.BankDTO;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnquiryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.Bank}.
 */
public interface BankService {

    /**
     * Save a bank.
     *
     * @param bankDTO the entity to save.
     * @return the persisted entity.
     */

    BankDTO save(BankDTO bankDTO);


    /**
     * Save a bank.
     *
     * @param bank the entity to save.
     * @return the persisted entity.
     */

    Bank save(Bank bank);


    /**
     * Get all the banks.
     *
     * @return the list of entities.
     */
    List<BankDTO> findAll();


    /**
     * Get the "id" bank.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BankDTO> findOne(Long id);

    /**
     * Delete the "id" bank.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<Bank> findByBankCode(String bankCode);

    Page<BankDTO> findAll(Pageable pageable);

    Page<BankDTO> findAllRegularBanks(Pageable pageable);

    List<BankDTO> findCommercialBanks();

    List<BankDTO> findMicrofinanceBanks();

    Page<BankDTO> getAllActiveBanks(Pageable pageable);

    List<BankDTO> getAllActiveBanks();

    List<BankDTO> getValidBanks(List<BankDTO> bankLIst, String accountNumber);

    AccountEnquiryResponse verifyBankAccount(String bankCode, String accountNumber);

    Page<Bank> findAllByShortCodeIsNotNull(Pageable pageable);

}
