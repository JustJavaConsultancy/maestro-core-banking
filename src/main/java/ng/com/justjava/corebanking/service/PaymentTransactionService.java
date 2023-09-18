package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.PaymentTransactionDTO;
import ng.com.justjava.corebanking.domain.PaymentTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link PaymentTransaction}.
 */
public interface PaymentTransactionService {

    /**
     * Save a paymentTransaction.
     *
     * @param paymentTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentTransactionDTO save(PaymentTransactionDTO paymentTransactionDTO);
    /**
     * Save a paymentTransaction.
     *
     * @param paymentTransaction the entity to save.
     * @return the persisted entity.
     */
    PaymentTransaction save(PaymentTransaction paymentTransaction);
    /**
     * Get all the paymentTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentTransactionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" paymentTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentTransactionDTO> findOne(Long id);
    Optional<PaymentTransactionDTO> findOneByTransactionRef(String transactionRef);
    List<PaymentTransactionDTO> findBySourceAccount(String sourceAccount);
	List<PaymentTransactionDTO> findByDestinationAccount(String destinationAccount);
	List<PaymentTransactionDTO> findBySourceAccountName(String sourceAccountName);
    /**
     * Delete the "id" paymentTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
