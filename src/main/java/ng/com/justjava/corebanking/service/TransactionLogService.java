package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.TransactionLog;
import ng.com.justjava.corebanking.service.dto.FundDTO;
import ng.com.justjava.corebanking.service.dto.TransactionLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link TransactionLog}.
 */
public interface TransactionLogService {

    /**
     * Save a transaction log.
     *
     * @param fundDTO the entity to save.
     * @return the persisted entity.
     */

    FundDTO save(FundDTO fundDTO);

    FundDTO saveBulkTrans(FundDTO fundDTO);

    /**
     * Get all the transaction logs.
     *
     * @return the list of entities.
     */
    List<FundDTO> findAll();

    /**
     * Get all the transaction logs.
     *
     * @return the list of entities.
     */
    Page<FundDTO> findAll(Pageable pageable);


    /**
     * Get the "id" transaction log.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FundDTO> findOne(Long id);

    /**
     * Delete the "id" transaction log.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    FundDTO findByTransRef(String transRef);

    List<FundDTO> findByAccountNumber(String accountNumber);

    List<FundDTO> findByAccountNumberAndCreatedDateBetween(String accountNumber, Instant fromDate, Instant toDate);

    FundDTO findByRrr(String rrr);

    boolean existsByRrr(String rrr);

    Page<FundDTO> findAllByKeyword(Pageable pageable, String key);

    Page<FundDTO> findAllByCreatedDateBetween(Instant fromDate, Instant toDate, Pageable pageable);

    List<FundDTO> findAllByCreatedDateBetween(Instant fromDate, Instant toDate);

    Page<FundDTO> findAllBySchemeAndCreatedDateBetween(String scheme, Instant fromInstant, Instant toInstant, Pageable pageable);

    List<FundDTO>  findAllByNarrationLike(String pId);

    List<FundDTO> findAllByNarrationContains(String pId);

    List<FundDTO> findBySourceAccountNumberAndCreatedDateBetween(String accountNumber, Instant fromInstant, Instant toInstant);

    Optional<FundDTO> updateTransactionLogStatus(TransactionLogDto logDto);
}
