package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.JournalLine;
import ng.com.justjava.corebanking.domain.Scheme;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.AccountStatus;
import ng.com.justjava.corebanking.domain.enumeration.PaymentType;
import ng.com.justjava.corebanking.domain.enumeration.TransactionStatus;
import ng.com.justjava.corebanking.service.dto.SchemeSumOfCreditsDTO;
import ng.com.justjava.corebanking.service.dto.WalletSumOfCreditsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface JournalLineRepository extends JpaRepository<JournalLine, Long> {

    default List<JournalLine> findByWalletAccount_AccountNumberOrderByJounal_TransDateDesc(String walletAccount_accountNumber) {
        ArrayList<PaymentType> paymentTypes = new ArrayList<>();
        paymentTypes.add(PaymentType.INVOICE);
        paymentTypes.add(PaymentType.REJECTED);
        return findByWalletAccount_AccountNumberAndJounal_PaymentTypeNotInOrderByJounal_TransDateDesc(walletAccount_accountNumber, paymentTypes);
    }

    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccount_AccountNumberAndJounal_PaymentTypeNotOrderByJounal_TransDateDesc(String walletAccount_accountNumber, PaymentType jounal_paymentType);

    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccount_AccountNumberAndJounal_PaymentTypeNotInOrderByJounal_TransDateDesc(String walletAccount_accountNumber, Collection<PaymentType> jounal_paymentType);

    default List<JournalLine> findByWalletAccount_AccountNumberAndJounalTransDateBetween(String walletAccount_accountNumber, LocalDateTime fromDate, LocalDateTime toDate) {
        return findByWalletAccount_AccountNumberAndJounal_PaymentTypeNotAndJounalTransDateBetween(walletAccount_accountNumber, PaymentType.INVOICE, fromDate, toDate);
    }

    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccount_AccountNumberAndJounal_PaymentTypeNotAndJounalTransDateBetween(String walletAccount_accountNumber, PaymentType paymentType, LocalDateTime fromDate, LocalDateTime toDate);

    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccount_AccountNumberAndWalletAccountSchemeAndWalletAccountStatusNot(String accountNumber, Scheme scheme, AccountStatus status);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByJounalReference(String reference);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByJounalReferenceAndWalletAccountAccountNumberNot(String reference, String accountNumber);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByJounalReferenceAndWalletAccount_AccountNumber(String jounal_reference, String accountNumber);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumber(String phoneNumber);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndWalletAccountSchemeAndWalletAccountStatusNot(String phoneNumber, Scheme scheme, AccountStatus status);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndWalletAccountSchemeAndWalletAccountStatusNotAndJounalTransDateBetween(String walletAccount_accountOwner_phoneNumber, Scheme walletAccount_scheme, AccountStatus walletAccount_status, LocalDateTime jounal_transDate, LocalDateTime jounal_transDate2);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndWalletAccountSchemeAndWalletAccountStatusNotOrderByJounalTransDateDesc(String phoneNumber, Scheme scheme, AccountStatus status);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndDebitGreaterThan(String phoneNumber, Double debit);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeAndDebitGreaterThan(String phoneNumber, PaymentType paymentType, Double debit);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeNotAndDebitGreaterThan(String phoneNumber, PaymentType paymentType, Double debit);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeAndDebitGreaterThanAndWalletAccountSchemeAndWalletAccountStatusNot(String phoneNumber, PaymentType paymentType, Double debit, Scheme scheme, AccountStatus status);

    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeNotAndDebitGreaterThanAndWalletAccountSchemeAndWalletAccountStatusNot(String phoneNumber, PaymentType paymentType, Double debit, Scheme scheme, AccountStatus status);

    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeAndDebitGreaterThanAndWalletAccountSchemeAndWalletAccountStatusNotAndJounalTransactionStatusOrderByJounal_TransDateDesc(String phoneNumber, PaymentType paymentType, Double debit, Scheme scheme, AccountStatus status, TransactionStatus transactionStatus);

    @EntityGraph(attributePaths = {"jounal", "jounal.journalLines", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeNotAndDebitGreaterThanAndWalletAccountSchemeAndWalletAccountStatusNotAndJounalTransactionStatusOrderByJounal_TransDateDesc(String phoneNumber, PaymentType paymentType, Double debit, Scheme scheme, AccountStatus status, TransactionStatus transactionStatus);

    @EntityGraph(attributePaths = {"jounal", "jounal.journalLines", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeNotInAndDebitGreaterThanAndWalletAccountSchemeAndWalletAccountStatusNotAndJounalTransactionStatusOrderByJounal_TransDateDesc(String phoneNumber, List<PaymentType> paymentTypes, Double debit, Scheme scheme, AccountStatus status, TransactionStatus transactionStatus);

    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    @Query("select journalLine from JournalLine journalLine where journalLine.walletAccount.accountOwner.phoneNumber = ?#{principal.username}")
    List<JournalLine> findByUserIsCurrentUser();

    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    @Query("select journalLine from JournalLine journalLine where journalLine.walletAccount.scheme = :scheme and journalLine.walletAccount.accountOwner.phoneNumber = ?#{principal.username} and walletAccount.status not in ('INACTIVE')")
    List<JournalLine> findByUserIsCurrentUserAndScheme(@Param("scheme") Scheme scheme);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    @Query("select journalLine from JournalLine journalLine where journalLine.walletAccount.accountOwner.phoneNumber = ?#{principal.username} order by journalLine.jounal.transDate")
    List<JournalLine> findByUserIsCurrentUserOrderByTransactionDate();


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    @Query("select journalLine from JournalLine journalLine where journalLine.walletAccount.scheme = :scheme and journalLine.jounal.paymentType not in ('INVOICE') and journalLine.walletAccount.accountOwner.phoneNumber = ?#{principal.username} and journalLine.walletAccount.status not in ('INACTIVE') order by journalLine.jounal.transDate")
    List<JournalLine> findByUserIsCurrentUserOrderByTransactionDateAndWalletAccountScheme(@Param("scheme") Scheme scheme);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    @Query("select journalLine from JournalLine journalLine where journalLine.walletAccount.scheme = :scheme and journalLine.jounal.transDate between :fromDate and :toDate and journalLine.jounal.paymentType not in ('INVOICE') and  journalLine.walletAccount.accountOwner.phoneNumber = ?#{principal.username} and journalLine.walletAccount.status not in ('INACTIVE') order by journalLine.jounal.transDate")
    List<JournalLine> findByUserIsCurrentUserOrderByTransactionDateAndWalletAccountScheme(@Param("scheme") Scheme scheme, @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    @Query("select journalLine from JournalLine journalLine where journalLine.walletAccount.accountOwner.phoneNumber = ?#{principal.username} and journalLine.jounal.narration like '%airtime%' or journalLine.jounal.narration like '%data%' order by journalLine.jounal.transDate")
    List<JournalLine> findByUserIsCurrentUserAirtimeDataOrderByTransactionDate();


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    @Query("select journalLine from JournalLine journalLine where journalLine.walletAccount.scheme = :s and journalLine.walletAccount.accountOwner.phoneNumber = ?#{principal.username} and journalLine.jounal.narration like '%airtime%' or journalLine.jounal.narration like '%data%' and journalLine.walletAccount.status not in ('INACTIVE') order by journalLine.jounal.transDate")
    List<JournalLine> findByUserIsCurrentUserAirtimeDataOrderByTransactionDateAndWalletAccountScheme(@Param("s") Scheme s);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountNumberAndDebitGreaterThan(String accountNumber, Double debit);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountNumberAndDebitGreaterThanAndWalletAccountStatusNot(String accountNumber, Double debit, AccountStatus status);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountNumberAndCreditGreaterThan(String accountNumber, Double credit);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountNumberAndCreditGreaterThanAndWalletAccountStatusNot(String accountNumber, Double debit, AccountStatus status);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountWalletAccountTypeAndCreditGreaterThan(String walletAccountType, Double credit);


    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountNumberAndJounalTransDate(String accountNumber, LocalDate transDate);


    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountNumberAndJounalTransDateAndWalletAccountStatusNot(String accountNumber, LocalDate transDate, AccountStatus status);


    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountNumberAndJounalTransDateBetweenAndWalletAccountStatusNot(String accountNumber, LocalDateTime from, LocalDateTime to, AccountStatus status);


    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeOrderByJounal_TransDateDesc(String phoneNumber, PaymentType paymentType);


    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeNotOrderByJounal_TransDateDesc(String phoneNumber, PaymentType paymentType);


    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeAndWalletAccountSchemeAndWalletAccountStatusNot(String phoneNumber, PaymentType paymentType, Scheme scheme, AccountStatus status);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndJounalPaymentTypeOrderByJounal_TransDateAsc(String walletAccount_accountNumber, LocalDateTime jounal_transDate, LocalDateTime jounal_transDate2, PaymentType jounal_paymentType);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndJounalPaymentTypeNotOrderByJounal_TransDateAsc(String walletAccount_accountNumber, LocalDateTime jounal_transDate, LocalDateTime jounal_transDate2, PaymentType jounal_paymentType);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndJounalPaymentTypeAndWalletAccountSchemeOrderByJounal_TransDateDesc(String walletAccount_accountNumber, LocalDateTime jounal_transDate, LocalDateTime jounal_transDate2, PaymentType jounal_paymentType, Scheme scheme);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndJounalPaymentTypeNotAndWalletAccountSchemeOrderByJounal_TransDateDesc(String walletAccount_accountNumber, LocalDateTime jounal_transDate, LocalDateTime jounal_transDate2, PaymentType jounal_paymentType, Scheme scheme);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    @Query("select j from JournalLine j where lower(concat(j.jounal.reference, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.narration, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.walletAccount.accountNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.externalRef, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transactionStatus, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transDate, '')) LIKE lower(concat('%',:keyword, '%')) and j.walletAccount.accountNumber = :accountNumber and j.jounal.paymentType = :paymentType and j.walletAccount.accountOwner is not null and j.jounal.transDate between :startDate and :endDate order by j.jounal.transDate desc")
    List<JournalLine> searchByAccountNumberAndJounalPaymentTypeWithDateRange(@Param("accountNumber") String accountNumber, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("paymentType") PaymentType paymentType, @Param("keyword") String keyword);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    @Query("select j from JournalLine j where lower(concat(j.jounal.reference, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.narration, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.walletAccount.accountNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.externalRef, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transactionStatus, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transDate, '')) LIKE lower(concat('%',:keyword, '%')) and j.walletAccount.accountNumber = :accountNumber and j.jounal.paymentType <> :paymentType and j.walletAccount.accountOwner is not null and j.jounal.transDate between :startDate and :endDate order by j.jounal.transDate desc")
    List<JournalLine> searchByAccountNumberAndJounalPaymentTypeNotWithDateRange(@Param("accountNumber") String accountNumber, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("paymentType") PaymentType paymentType, @Param("keyword") String keyword);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndJounalPaymentTypeAndWalletAccountSchemeIn(String walletAccount_accountNumber, LocalDateTime jounal_transDate, LocalDateTime jounal_transDate2, PaymentType jounal_paymentType, List<Scheme> walletAccount_scheme);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndJounalPaymentTypeNotAndWalletAccountSchemeIn(String walletAccount_accountNumber, LocalDateTime jounal_transDate, LocalDateTime jounal_transDate2, PaymentType jounal_paymentType, List<Scheme> walletAccount_scheme);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    Page<JournalLine> findAllByJounalTransDateBetweenAndJounalPaymentTypeOrderByJounal_TransDateDesc(LocalDateTime jounal_transDate, LocalDateTime jounal_transDate2, PaymentType paymentType, Pageable pageable);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    Page<JournalLine> findAllByJounalTransDateBetweenAndJounalPaymentTypeNotOrderByJounal_TransDateDesc(LocalDateTime jounal_transDate, LocalDateTime jounal_transDate2, PaymentType paymentType, Pageable pageable);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findAllByJounalTransDateBetweenAndJounalPaymentTypeAndWalletAccountSchemeOrderByJounal_TransDateDesc(LocalDateTime jounal_transDate, LocalDateTime jounal_transDate2, PaymentType paymentType, Scheme scheme);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    List<JournalLine> findAllByJounalTransDateBetweenAndJounalPaymentTypeNotAndWalletAccountSchemeOrderByJounal_TransDateDesc(LocalDateTime jounal_transDate, LocalDateTime jounal_transDate2, PaymentType paymentType, Scheme scheme);



    @Query(
        value = "SELECT wallet_account_id as accountId, wallet_account.account_number as accountNumber, wallet_account.account_name as accountName, SUM(jounal_line.credit) as sum FROM jounal_line LEFT JOIN wallet_account ON jounal_line.wallet_account_id = wallet_account.id GROUP BY wallet_account_id, wallet_account.account_number, wallet_account.account_name",
        nativeQuery = true)
    List<WalletSumOfCreditsDTO> getSumOfWalletCredits();


    @Query(
        value = "SELECT wallet_account_id as accountId, wallet_account.account_number as accountNumber, wallet_account.account_name as accountName, SUM(jounal_line.debit) as sum FROM jounal_line LEFT JOIN wallet_account ON jounal_line.wallet_account_id = wallet_account.id GROUP BY wallet_account_id, wallet_account.account_number, wallet_account.account_name",
        nativeQuery = true)
    List<WalletSumOfCreditsDTO> getSumOfWalletDebits();

    @Query(
        value = "SELECT wallet_account.scheme_id as schemeId, scheme.scheme as scheme, SUM(jounal_line.credit) as sum FROM jounal_line LEFT JOIN wallet_account ON jounal_line.wallet_account_id = wallet_account.id LEFT JOIN scheme ON wallet_account.scheme_id = wallet_account.scheme_id GROUP BY wallet_account.scheme_id, scheme.scheme",
        nativeQuery = true)
    List<SchemeSumOfCreditsDTO> getSumOfSchemeCredits();

    @Query(
        value = "SELECT wallet_account.scheme_id as schemeId, scheme.scheme as scheme, SUM(jounal_line.debit) as sum FROM jounal_line LEFT JOIN wallet_account ON jounal_line.wallet_account_id = wallet_account.id LEFT JOIN scheme ON wallet_account.scheme_id = wallet_account.scheme_id GROUP BY wallet_account.scheme_id, scheme.scheme",
        nativeQuery = true)
    List<SchemeSumOfCreditsDTO> getSumOfSchemeDebits();

    @Query(
        value = "select  SUM(credit) as credit from jounal_line", nativeQuery = true)
    double totalDeposits();

    @Query(
        value = "select  SUM(debit) as debit from jounal_line", nativeQuery = true)
    double totalWithdrawal();

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    Page<JournalLine> findAllByJounalPaymentTypeAndWalletAccount_AccountOwnerIsNotNullOrderByJounal_TransDateDesc(PaymentType paymentType, Pageable pageable);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    Page<JournalLine> findAllByJounalPaymentTypeNotAndWalletAccount_AccountOwnerIsNotNullOrderByJounal_TransDateDesc(PaymentType paymentType, Pageable pageable);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    @Query("select j from JournalLine j where lower(concat(j.jounal.reference, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.narration, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.walletAccount.accountNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.externalRef, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transactionStatus, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transDate, '')) LIKE lower(concat('%',:keyword, '%')) and j.jounal.paymentType = :paymentType and j.walletAccount.accountOwner is not null and j.walletAccount in :walletAccounts order by j.jounal.transDate desc")
    List<JournalLine> searchByPaymentTypeAccountOwnerAndSchemeAndSearchKey(@Param("paymentType") PaymentType paymentType, @Param("keyword") String keyword, @Param("walletAccounts") List<WalletAccount> walletAccounts);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    @Query("select j from JournalLine j where lower(concat(j.jounal.reference, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.narration, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.walletAccount.accountNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.externalRef, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transactionStatus, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transDate, '')) LIKE lower(concat('%',:keyword, '%')) and j.jounal.paymentType <> :paymentType and j.walletAccount.accountOwner is not null and j.walletAccount in :walletAccounts order by j.jounal.transDate desc")
    List<JournalLine> searchByPaymentTypeNotAccountOwnerAndSchemeAndSearchKey(@Param("paymentType") PaymentType paymentType, @Param("keyword") String keyword, @Param("walletAccounts") List<WalletAccount> walletAccounts);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    @Query("select j from JournalLine j where lower(concat(j.jounal.reference, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.narration, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.walletAccount.accountNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.externalRef, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transactionStatus, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transDate, '')) LIKE lower(concat('%',:keyword, '%')) and j.jounal.paymentType = :paymentType and j.walletAccount.accountNumber =:accountNumber and j.walletAccount.accountOwner is not null order by j.jounal.transDate desc")
    Page<JournalLine> searchByAccountNumberPaymentTypeAccountOwnerAndSearchKey(@Param("paymentType") PaymentType paymentType, @Param("accountNumber") String accountNumber, @Param("keyword") String keyword, Pageable pageable);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    @Query("select j from JournalLine j where lower(concat(j.jounal.reference, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.narration, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.walletAccount.accountNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.externalRef, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transactionStatus, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transDate, '')) LIKE lower(concat('%',:keyword, '%')) and j.jounal.paymentType <> :paymentType and j.walletAccount.accountNumber =:accountNumber and j.walletAccount.accountOwner is not null order by j.jounal.transDate desc")
    Page<JournalLine> searchByAccountNumberPaymentTypeNotAccountOwnerAndSearchKey(@Param("paymentType") PaymentType paymentType, @Param("accountNumber") String accountNumber, @Param("keyword") String keyword, Pageable pageable);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    @Query("select j from JournalLine j where lower(concat(j.jounal.reference, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.narration, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.walletAccount.accountNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.externalRef, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transactionStatus, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transDate, '')) LIKE lower(concat('%',:keyword, '%')) and j.jounal.paymentType = :paymentType and j.walletAccount.accountOwner is not null order by j.jounal.transDate desc")
    Page<JournalLine> searchByPaymentTypeAccountOwnerAndSearchKey(@Param("paymentType") PaymentType paymentType, @Param("keyword") String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    @Query("select j from JournalLine j where lower(concat(j.jounal.reference, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.narration, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.walletAccount.accountNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.externalRef, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transactionStatus, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(j.jounal.transDate, '')) LIKE lower(concat('%',:keyword, '%')) and j.jounal.paymentType<> :paymentType and j.walletAccount.accountOwner is not null order by j.jounal.transDate desc")
    Page<JournalLine> searchByPaymentTypeNotAccountOwnerAndSearchKey(@Param("paymentType") PaymentType paymentType, @Param("keyword") String keyword, Pageable pageable);

    @Query("select SUM (j.credit + j.debit) as amount from JournalLine j where j.jounal.transactionStatus = 'SUSPENDED' and j.walletAccount.accountNumber = ?1 ")
    Double sumSuspendedTransactionsAmount(String accountNumber);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    Page<JournalLine> findAllByWalletAccount_AccountNumberAndDebitGreaterThanOrderByJounal_TransDateDesc(String walletAccount_accountNumber, Double debit, Pageable pageable);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    Page<JournalLine> findAllByWalletAccount_AccountNumberAndCreditGreaterThanOrderByJounal_TransDateDesc(String walletAccount_accountNumber, Double debit, Pageable pageable);

    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    Page<JournalLine> findAllByWalletAccount_AccountNumberOrderByJounal_TransDateDesc(String walletAccount_accountNumber, Pageable pageable);


    @EntityGraph(attributePaths = {"jounal","walletAccount","walletAccount.accountOwner","walletAccount.accountOwner.addresses"})
    Page<JournalLine> findAllByWalletAccountAccountNumberAndJounalTransactionStatusAndJounalTransDateBetweenOrderByJounal_TransDateDesc(String accountNumber, TransactionStatus status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);


    @Query("select SUM (credit) as credit from JournalLine j where j.walletAccount.accountOwner is not null and j.jounal.transDate between :startDate and :endDate")
    double sumOfAllCreditsToAllCustomerAccounts( @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("select SUM (j.debit) as credit from JournalLine j where j.walletAccount.accountOwner is not null and j.jounal.transDate between :startDate and :endDate")
    double sumOfAllDebitsToAllCustomerAccounts(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("select SUM(j.credit) as credit from  JournalLine j where j.walletAccount.accountNumber = :accountNumber and j.jounal.transDate between :startDate and :endDate")
    double sumOfAllCreditsToAnAccount(@Param("accountNumber") String accountNumber, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("select SUM(j.debit) as credit from  JournalLine j where j.walletAccount.accountNumber = :accountNumber and j.jounal.transDate between :startDate and :endDate")
    double sumOfAllDebitsToAnAccount(@Param("accountNumber") String accountNumber, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findAllByWalletAccountAndWalletAccountSubWalletsInAndJounalTransDateBetweenOrderByJounal_TransDateDesc(WalletAccount walletAccount, Set<WalletAccount> subWallets, LocalDateTime startDate, LocalDateTime endDate);

    @EntityGraph(attributePaths = {"jounal", "walletAccount", "walletAccount.accountOwner", "walletAccount.accountOwner.addresses"})
    List<JournalLine> findByWalletAccountIsIn(List<WalletAccount> walletAccounts);


    //    @Query("select ")
//    List<JournalLine> getAllWalletsSummary(String schemeId, LocalDateTime fromDate, LocalDateTime toDate);

//    List
}
