package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.TransactionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the TransactionLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {

    Optional<TransactionLog> findByTransRefIgnoreCase(String transRef);

    List<TransactionLog> findByRrr(String rrr);

    @Query("select transactionlog from TransactionLog transactionlog where lower(transactionlog.specificChannel) LIKE lower(concat('%',:keyword, '%')) or lower(transactionlog.channel) LIKE lower(concat('%',:keyword, '%')) or lower(transactionlog.sourceAccountNumber) LIKE lower(concat('%',:keyword, '%')) or lower(transactionlog.accountNumber) LIKE lower(concat('%',:keyword, '%')) or lower(transactionlog.beneficiaryName) LIKE lower(concat('%',:keyword, '%')) or lower(transactionlog.narration) LIKE lower(concat('%',:keyword, '%')) order by transactionlog.createdDate DESC ")
    Page<TransactionLog> findAllByKeywordSearch(Pageable pageable, @Param("keyword") String keyword);

    Page<TransactionLog> findAllByCreatedDateBetweenOrderByCreatedDateDesc(Instant fromDate, Instant toDate, Pageable pageable);

    @Query("select t from TransactionLog t where t.createdDate between ?1 and ?2 order by t.createdDate DESC")
    List<TransactionLog> findAllByCreatedDateBetweenOrderByCreatedDateDesc(Instant fromDate, Instant toDate);


    @Query("select t from TransactionLog t where t.accountNumber = ?1")
    List<TransactionLog> findByAccountNumber(String accountNumber);

    @Query("select t from TransactionLog t where t.sourceAccountNumber = ?1")
    List<TransactionLog> findBySourceAccountNumber(String sourceAccountNumber);


    @Query("select t from TransactionLog t " +
        "where t.accountNumber = ?1 and t.createdDate between ?2 and ?3 " +
        "order by t.createdDate DESC")
    List<TransactionLog> findByAccountNumberAndCreatedDateBetweenOrderByCreatedDateDesc(String accountNumber, Instant fromDate, Instant toDate);

    @Query("select t from TransactionLog t " +
        "where t.sourceAccountNumber = ?1 and t.createdDate between ?2 and ?3 " +
        "order by t.createdDate DESC")
    List<TransactionLog> findBySourceAccountNumberAndCreatedDateBetweenOrderByCreatedDateDesc(String sourceAccountNumber, Instant fromDate, Instant toDate);

    @Query("select (count(t) > 0) from TransactionLog t where t.rrr = ?1")
    boolean existsByRrr(String rrr);

    List<TransactionLog> findAllByNarrationLike(String pId);
    @Query("select t from TransactionLog t where t.narration like concat('%', ?1, '%')")
    List<TransactionLog> findAllByNarrationContains(String pId);
}
