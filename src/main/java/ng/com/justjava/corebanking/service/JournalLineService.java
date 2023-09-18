package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.SchemeSumOfCreditsDTO;
import ng.com.justjava.corebanking.service.dto.WalletSumOfCreditsDTO;
import ng.com.justjava.corebanking.domain.Journal;
import ng.com.justjava.corebanking.domain.JournalLine;
import ng.com.justjava.corebanking.domain.Scheme;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

public interface JournalLineService {
    JournalLine save(JournalLine journalLine, Journal journal, WalletAccount walletAccount);

    JournalLine save(JournalLine journalLine);


    /**
     * Get all the journaline.
     *
     * @return the list of entities.
     */
    List<JournalLine> findAll();

    /**
     * Get all the journaline.
     *
     * @return the list of entities.
     */
    Page<JournalLine> findAll(Pageable pageable);


    /**
     * Get the "id" address.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JournalLine> findOne(Long id);

    List<JournalLine> findByWalletAccount_AccountNumberWithDateRange(String accountNumber, LocalDateTime fromDate, LocalDateTime toDate);

    List<JournalLine> findByWalletAccount_AccountNumber(String accountNumber);

    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumber(String phoneNumber, HttpSession session);

    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberOrderByDate(String phoneNumber, HttpSession session);

    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberWithDateRange(String phoneNumber, LocalDateTime startDate, LocalDateTime fromDate, HttpSession session);

    List<JournalLine> findByUserIsCurrentUser(HttpSession session);

    List<JournalLine> findByUserIsCurrentUserOrderByTransactionDate(HttpSession session);

    List<JournalLine> findByUserIsCurrentUserOrderByTransactionDateDateRange(LocalDateTime fromDate, LocalDateTime toDate, HttpSession session);

    List<JournalLine> findByUserIsCurrentUserOrderByTransactionDateAirtimeData(HttpSession session);

    List<JournalLine> findByWalletAccountAccountNumberAndDebitGreaterThan(String accountNumber, Double debit);

    List<JournalLine> findByWalletAccountAccountNumberAndCreditGreaterThan(String accountNumber, Double debit);

    List<JournalLine> findAccountDailyTransaction(String accountNumber);

    List<JournalLine> findCustomerReceipt(HttpSession session);

    LinkedHashSet<Journal> findCustomerReceipts(HttpSession session);

    List<JournalLine> findCustomerInvoice();

    List<JournalLine> findCustomerInvoiceByPhoneNumber(String phoneNumber);

    List<JournalLine> findByJounalReference(String reference);

    List<JournalLine> findByJounalReferenceNotTransitAccount(String reference);

    List<JournalLine> findByJounalReferenceAndWalletAccount_AccountNumber(String reference, String accountNumber);

    Double getAccountDailyTransactionAmount(String accountNumber);

    void delete(Long id);

    List<SchemeSumOfCreditsDTO> getSumOfSchemeCredits();

    List<SchemeSumOfCreditsDTO> getSumOfSchemeDebits();

    List<WalletSumOfCreditsDTO> getSumOfWalletCredits();

    List<WalletSumOfCreditsDTO> getSumOfWalletDebits();

    List<JournalLine> findAllByWalletAccountAccountNumberAndJounalTransDateBetween(String accountNumber, LocalDateTime transDate, LocalDateTime endDate);

    List<JournalLine> findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndScheme(String accountNumber, LocalDateTime atStartOfDay, LocalDateTime atTime, String schemeId);

    List<JournalLine> findAllByWalletAccountAccountNumberAndJounalTransDateBetweenSearchByKey(String accountNumber, LocalDateTime atStartOfDay, LocalDateTime atTime, String key);

    Page<JournalLine> findAllByJounalTransDateBetween(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);

    List<JournalLine> findAllByJounalTransDateBetweenAndScheme(LocalDateTime atStartOfDay, LocalDateTime atTime, String schemeId);

    Page<JournalLine> findAllJournaline(Pageable pageable);

    Page<JournalLine> findAllByWalletAccount_AccountOwnerIsNotNull(Pageable pageable);

    Page<JournalLine> findAllByWalletAccount_AccountOwnerIsNotNullSearch(String key, Pageable pageable);

    Page<JournalLine> findAllByWalletAccount_AccountOwnerIsNotNullAndWalletAccountNumberSearch(String accountNumber, String key, Pageable pageable);

    Page<JournalLine> findAllByWalletAccount_AccountOwnerIsNotNullSearchAndScheme(String key, String schemeId, Pageable pageable);

    Double sumOfSuspendedTransactionsAmount(String accountNumber);

    Page<JournalLine> getAllTransitAccountDebitTransactions(Pageable pageable);

    Page<JournalLine> getAllTransitAccountCreditTransactions(Pageable pageable);

    Page<JournalLine> getAllTransitAccountTransactions(Pageable pageable);

    Page<JournalLine> getAllJournalLinesByStatusAndAccountNumberDateRange(Pageable pageable, TransactionStatus status, String accountNumber, LocalDateTime startDate, LocalDateTime endDate);

    Long count();

    double sumOfAllCreditsToAllCustomerAccounts(LocalDateTime startDate, LocalDateTime endDate);

    double sumOfAllDebitsToAllCustomerAccounts(LocalDateTime startDate, LocalDateTime endDate);

    double sumOfAllCreditsToAnAccount(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);

    double sumOfAllDebitsToAnAccount(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);

    List<JournalLine> findAllSuperAgentJounalTransDateBetween(Pageable pageable, String superAgentPhoneNumber, LocalDateTime startDate, LocalDateTime endDate);

    List<Scheme> getAdminSchemes();

    List<JournalLine> getCustomerBonusPoints(String phoneNumber);

    List<JournalLine> getTransactionStatements(String transRef);

    String getUniqueTransRef();

    List<JournalLine> findByWalletAccountIn(List<WalletAccount> walletAccounts);

    GenericResponseDTO getSuperAgentAgentsTransactions(Pageable pageable);

    GenericResponseDTO getSuperAgentTellersTransactions(Pageable pageable);

//    GenericResponseDTO getAllWalletsSummary(String schemeId, LocalDateTime fromDate, LocalDateTime toDate);
}
