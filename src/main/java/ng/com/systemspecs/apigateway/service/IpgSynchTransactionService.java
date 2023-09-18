package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.IpgSynchTransaction;
import ng.com.systemspecs.apigateway.domain.enumeration.NavsaTransactionStatus;
import ng.com.systemspecs.apigateway.service.dto.stp.IPGSynchTransactionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.IpgSynchTransaction}.
 */
public interface IpgSynchTransactionService {

    /**
     * Save a ipgSynchTransaction.
     *
     * @param ipgSynchTransactionDTO the entity to save.
     * @return the persisted entity.
     */

    IPGSynchTransactionDTO save(IPGSynchTransactionDTO ipgSynchTransactionDTO);

    /**
     * Get all the ipgSynchTransactions.
     *
     * @return the list of entities.
     */
    List<IpgSynchTransaction> findAll();


    /**
     * Get the "id" ipgSynchTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IpgSynchTransaction> findOne(Long id);

    /**
     * Delete the "id" ipgSynchTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<IpgSynchTransaction> findByTransactionRef(String transactionRef);

    List<IpgSynchTransaction> findByStatus(String status);


    List<IpgSynchTransaction> changeStatus(String transactionRef, NavsaTransactionStatus status);
}
