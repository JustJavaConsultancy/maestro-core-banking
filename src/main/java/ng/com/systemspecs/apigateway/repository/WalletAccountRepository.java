package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.Scheme;
import ng.com.systemspecs.apigateway.domain.WalletAccount;
import ng.com.systemspecs.apigateway.domain.enumeration.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the WalletAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalletAccountRepository extends JpaRepository<WalletAccount, Long> {

    default List<WalletAccount> findAllBySchemeId(Long scheme_id) {
        return findAllBySchemeIdAndWalletAccountType_WalletAccountTypeNot(scheme_id, "BonusPoint");
    }

    List<WalletAccount> findAllBySchemeIdAndWalletAccountType_WalletAccountTypeNot(Long scheme_id, String walletAccountType);

    default Page<WalletAccount> findAllBySchemeSchemeID(String schemeID, Pageable pageable) {
        return findAllBySchemeSchemeIDAndWalletAccountType_WalletAccountTypeNot(schemeID, pageable, "BonusPoint");
    }

    Page<WalletAccount> findAllBySchemeSchemeIDAndWalletAccountType_WalletAccountTypeNot(String schemeId, Pageable pageable, String walletAccountType);


    default List<WalletAccount> findAllByAccountNumberAndAccountNameAndSchemeId(String accountNumber, String accountName, Long scheme_id) {
        return findAllByAccountNumberAndAccountNameAndSchemeIdAndWalletAccountType_WalletAccountTypeNot(accountNumber, accountName, scheme_id, "BonusPoint");
    }

    List<WalletAccount> findAllByAccountNumberAndAccountNameAndSchemeIdAndWalletAccountType_WalletAccountTypeNot(String accountNumber, String accountName, Long scheme_id, String walletAccountType);

    @Query("select walletAccount from WalletAccount walletAccount where walletAccount.status not in ('INACTIVE') and walletAccount.walletAccountType.walletAccountType <> 'BonusPoint' and walletAccount.accountOwner.user.login = ?#{principal.username} order by walletAccount.id")
    List<WalletAccount> findByUserIsCurrentUser();

    @Query("select walletAccount from WalletAccount walletAccount where walletAccount.scheme = :scheme and walletAccount.walletAccountType.walletAccountType <> 'BonusPoint' and walletAccount.status not in ('INACTIVE') and walletAccount.accountOwner.user.login = ?#{principal.username} order by walletAccount.id")
    List<WalletAccount> findByUserIsCurrentUserScheme(@Param("scheme") Scheme scheme);

    default List<WalletAccount> findAllByAccountOwnerPhoneNumberAndSchemeAndStatusNot(String phoneNumber, Scheme scheme, AccountStatus status) {
        return findAllByAccountOwnerPhoneNumberAndSchemeAndStatusNotAndWalletAccountTypeAccountypeIDNot(phoneNumber, scheme, status, 4L);
    }

    default List<WalletAccount> findAllByAccountOwnerPhoneNumberAndSchemeSchemeID(String phoneNumber, String schemeId) {
        return findAllByAccountOwnerPhoneNumberAndSchemeSchemeIDAndStatus(phoneNumber, schemeId, AccountStatus.ACTIVE);
    }

    List<WalletAccount> findAllByAccountOwnerPhoneNumberAndSchemeSchemeIDAndStatus(String phoneNumber,
                                                                                   String schemeId, AccountStatus status);

    List<WalletAccount> findAllByAccountOwnerPhoneNumberAndSchemeAndStatusNotAndWalletAccountTypeAccountypeIDNot(String phoneNumber, Scheme scheme, AccountStatus status, Long accountTypeId);

    WalletAccount findByAccountNumber(String accountNumber);

    default WalletAccount findOneByAccountNumber(String accountNumber) {
        return findOneByAccountNumberAndStatusNot(accountNumber, AccountStatus.INACTIVE);
    }

    @Query("select w from WalletAccount w where w.accountNumber = ?1 and w.status <> ?2")
    WalletAccount findOneByAccountNumberAndStatusNot(String accountNumber, AccountStatus status);

    @Query("select (count(w) > 0) from WalletAccount w where w.status = ?1 and w.accountNumber in ?2")
    boolean existsByStatusAndAccountNumberIn(AccountStatus status, List<String> accountNumbers);

    boolean existsByAccountNumberIn(List<String> accountNumbers);

    boolean existsByAccountNumber(String accountNumber);

    Optional<WalletAccount> findByAccountNameAndStatusNot(String accountName, AccountStatus status);

    @EntityGraph(attributePaths = {"subWallets"})
    List<WalletAccount> findByAccountOwnerPhoneNumberAndStatusNotAndWalletAccountType_AccountypeID(String phoneNumber, AccountStatus status, Long walletAccountType_accountypeID);

    default List<WalletAccount> findByAccountOwnerPhoneNumber(String phoneNumber) {
        return findByAccountOwnerPhoneNumberAndStatusNotAndWalletAccountType_WalletAccountTypeNot(phoneNumber, AccountStatus.INACTIVE, "BonusPoint");
    }

    List<WalletAccount> findByAccountOwnerPhoneNumberAndStatusNotAndWalletAccountType_WalletAccountTypeNot(String phoneNumber, AccountStatus status, String walletAccountType);

    List<WalletAccount> findByAccountOwnerPhoneNumberAndSchemeAndStatusNot(String phoneNumber, Scheme scheme, AccountStatus status);

    default List<WalletAccount> findByAccountOwnerPhoneNumberAndAccountNameAndScheme_SchemeID(String phoneNumber, String accountName, String schemeID) {
        return findByAccountOwnerPhoneNumberAndAccountNameAndScheme_SchemeIDAndWalletAccountType_WalletAccountTypeNot(phoneNumber, accountName, schemeID, "BonusPoint");
    }

    default List<WalletAccount> findByAccountOwnerPhoneNumberAndScheme_SchemeID(String phoneNumber, String schemeID) {
        return findByAccountOwnerPhoneNumberAndScheme_SchemeIDAndWalletAccountType_WalletAccountTypeNot(phoneNumber, schemeID, "BonusPoint");
    }

    List<WalletAccount> findByAccountOwnerPhoneNumberAndScheme_SchemeIDAndWalletAccountType_WalletAccountTypeNot(String phoneNumber, String schemeID, String walletAccountType);

    @Query("select w from WalletAccount w " +
        "where w.accountOwner.phoneNumber = ?1 and w.accountName = ?2 and w.scheme.schemeID = ?3 and w.walletAccountType.walletAccountType <> ?4")
    List<WalletAccount> findByAccountOwnerPhoneNumberAndAccountNameAndScheme_SchemeIDAndWalletAccountType_WalletAccountTypeNot(String phoneNumber, String accountName, String schemeID, String walletAccountType);

    default Optional<WalletAccount> findByAccountOwnerPhoneNumberAndAccountName(String phoneNumber, String accountName) {
        return findByAccountOwnerPhoneNumberAndAccountNameAndStatusNotAndWalletAccountType_WalletAccountTypeNot(phoneNumber, accountName, AccountStatus.INACTIVE, "BonusPoint");
    }

    default Optional<WalletAccount> findByAccountOwnerPhoneNumberAndAccountNameAndScheme_SchemeID(String phoneNumber, String accountName) {
        return findByAccountOwnerPhoneNumberAndAccountNameAndStatusNotAndWalletAccountType_WalletAccountTypeNot(phoneNumber, accountName, AccountStatus.INACTIVE, "BonusPoint");
    }

    Optional<WalletAccount> findByAccountOwnerPhoneNumberAndAccountNameAndStatusNotAndWalletAccountType_WalletAccountTypeNot(String phoneNumber, String accountName, AccountStatus status, String walletAccountType);

    @Query("select walletAccount from WalletAccount walletAccount where walletAccount.status not in ('INACTIVE') and walletAccount.scheme = :scheme and walletAccount.walletAccountType.walletAccountType <> 'BonusPoint' and walletAccount.accountOwner.user.login = ?#{principal.username} order by walletAccount.dateOpened")
    List<WalletAccount> findByUserIsCurrentUserOrderByDateOpenedScheme(@Param("scheme") Scheme scheme);

    @Query(
        value = "select  SUM(current_balance) as balance from wallet_account;", nativeQuery = true)
    double totalBalance();

    default Page<WalletAccount> findAllByAccountOwnerIsNotNull(Pageable pageable) {
        return findAllByAccountOwnerIsNotNullAndStatusNotAndWalletAccountType_WalletAccountTypeNotOrderByDateOpened(AccountStatus.INACTIVE, pageable, "BonusPoint");
    }

    Page<WalletAccount> findAllByAccountOwnerIsNotNullAndStatusNotAndWalletAccountType_WalletAccountTypeNotOrderByDateOpened(AccountStatus status, Pageable pageable, String walletAccountType);

    Page<WalletAccount> findAllByAccountOwnerIsNotNullAndStatusNot(AccountStatus status, Pageable pageable);

    default List<WalletAccount> findAllByAccountOwnerIsNotNullAndSchemeAndStatusNotOrderByDateOpenedDesc(Scheme scheme, AccountStatus status) {
        return findAllByAccountOwnerIsNotNullAndSchemeAndStatusNotAndWalletAccountType_WalletAccountTypeNotOrderByDateOpenedDesc(scheme, status, "BonusPoint");
    }

    List<WalletAccount> findAllByAccountOwnerIsNotNullAndSchemeAndStatusNotAndWalletAccountType_WalletAccountTypeNotOrderByDateOpenedDesc(Scheme scheme, AccountStatus status, String walletAccountType);

    default List<WalletAccount> findAllByAccountOwnerIsNull() {
        return findAllByAccountOwnerIsNullAndStatusNotAndWalletAccountType_WalletAccountTypeNot(AccountStatus.INACTIVE, "BonusPoint");
    }

    List<WalletAccount> findAllByAccountOwnerIsNullAndStatusNotAndWalletAccountType_WalletAccountTypeNot(AccountStatus status, String walletAccountType);

    default List<WalletAccount> findAllBySchemeAndAccountOwnerIsNull(Scheme scheme) {
        return findAllBySchemeAndAccountOwnerIsNullAndStatusNotAndWalletAccountType_WalletAccountTypeNot(scheme, AccountStatus.INACTIVE, "BonusPoint");
    }

    List<WalletAccount> findAllBySchemeAndAccountOwnerIsNullAndStatusNotAndWalletAccountType_WalletAccountTypeNot(Scheme scheme, AccountStatus status, String walletAccountType);

    @Query("select walletAccount from WalletAccount walletAccount where lower(concat(walletAccount.accountNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.user.firstName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.user.lastName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.phoneNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.currentBalance, '')) LIKE lower(concat('%',:keyword, '%'))   and walletAccount.status not in ('INACTIVE') and walletAccount.walletAccountType.walletAccountType <> 'BonusPoint' and walletAccount.accountOwner is not null order by walletAccount.dateOpened DESC ")
    Page<WalletAccount> findAllByAccountOwnerIsNotNullSearchKeyword(Pageable pageable, @Param("keyword") String keyword);


    @Query("select walletAccount from WalletAccount walletAccount where lower(concat(walletAccount.accountNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.user.firstName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.user.lastName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.phoneNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.currentBalance, '')) LIKE lower(concat('%',:keyword, '%'))  and walletAccount.status not in ('INACTIVE') and walletAccount.scheme.schemeID = :scheme and walletAccount.walletAccountType.walletAccountType <> 'BonusPoint' and walletAccount.accountOwner is not null order by walletAccount.dateOpened DESC ")
    List<WalletAccount> findAllByAccountOwnerIsNotNullSearchKeywordAndScheme(@Param("scheme") String scheme, @Param("keyword") String keyword);

    @Query("select walletAccount from WalletAccount walletAccount where lower(concat(walletAccount.accountNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.user.firstName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.user.lastName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.phoneNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.currentBalance, '')) LIKE lower(concat('%',:keyword, '%'))  and walletAccount.status not in ('INACTIVE') and walletAccount.scheme.schemeID = :scheme and walletAccount.walletAccountType.walletAccountType <> 'BonusPoint' and walletAccount.accountOwner is not null and walletAccount.accountName = walletAccount.accountOwner.user.firstName order by walletAccount.dateOpened DESC ")
    List<WalletAccount> findAllPrimaryWalletByAccountOwnerIsNotNullSearchKeywordAndScheme(@Param("scheme") String scheme, @Param("keyword") String keyword);

    @Query("select walletAccount from WalletAccount walletAccount where lower(concat(walletAccount.accountNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.user.firstName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.user.lastName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.phoneNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.currentBalance, '')) LIKE lower(concat('%',:keyword, '%'))  and walletAccount.status not in ('INACTIVE') and walletAccount.walletAccountType.walletAccountType <> 'BonusPoint' and walletAccount.accountOwner is not null and walletAccount.accountName = walletAccount.accountOwner.user.firstName order by walletAccount.dateOpened DESC ")
    List<WalletAccount> findAllPrimaryWalletByAccountOwnerIsNotNullSearchKeyword(@Param("keyword") String keyword);

    Page<WalletAccount> findAllByAccountOwnerIsNotNullAndAccountNumberContainingIgnoreCaseOrAccountNameContainingIgnoreCaseOrAccountOwner_User_FirstNameContainingIgnoreCaseOrAccountOwner_User_LastNameContainingIgnoreCaseOrCurrentBalanceContainingIgnoreCaseOrAccountOwner_PhoneNumberContainingIgnoreCase(Pageable pageable, String s1, String s2, String s3, String s4, String s5, String s6);

    List<WalletAccount> findAllByScheme_SchemeID(String scheme_schemeID);

    default List<WalletAccount> findAllByScheme_SchemeIDAndAccountOwnerIsNotNullOrAccountOwner_FullNameContainingIgnoreCase(String scheme_schemeID, String key) {
        return findAllByScheme_SchemeIDAndAccountOwnerIsNotNullAndAccountOwner_User_FirstNameContainingIgnoreCaseOrAccountOwner_User_LastNameContainingIgnoreCaseOrCurrentBalanceContainingIgnoreCaseOrAccountOwner_PhoneNumberContainingIgnoreCase(scheme_schemeID, key, key, key, key);
    }

    List<WalletAccount> findAllByScheme_SchemeIDAndAccountOwnerIsNotNullAndAccountOwner_User_FirstNameContainingIgnoreCaseOrAccountOwner_User_LastNameContainingIgnoreCaseOrCurrentBalanceContainingIgnoreCaseOrAccountOwner_PhoneNumberContainingIgnoreCase(String scheme_schemeID, @Size(max = 50) String accountOwner_user_firstName, @Size(max = 50) String accountOwner_user_lastName, String currentBalance, String accountOwner_phoneNumber);

    @Query("select walletAccount from WalletAccount walletAccount where lower(concat(walletAccount.accountNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.user.firstName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.user.lastName, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.accountOwner.phoneNumber, '')) LIKE lower(concat('%',:keyword, '%')) or lower(concat(walletAccount.currentBalance, '')) LIKE lower(concat('%',:keyword, '%')) and walletAccount.status not in ('INACTIVE') and walletAccount.scheme.schemeID = :scheme and walletAccount.walletAccountType.walletAccountType <> 'BonusPoint' and walletAccount.accountOwner is not null order by walletAccount.dateOpened DESC")
    List<WalletAccount> findAllByAccountOwnerIsNotNullSearchKeywordAndSchemeWithoutPage(@Param("scheme") String scheme, @Param("keyword") String keyword);


    Optional<WalletAccount> findByAccountOwner_PhoneNumberAndWalletAccountType_AccountypeID(String accountOwner_phoneNumber, Long walletAccountType_accountypeID);

    List<WalletAccount> findAllByTrackingRefNotNull();

    List<WalletAccount> findAllByTrackingRefIsNotNullAndNubanAccountNoIsNull();

    List<WalletAccount> findByNubanAccountNo(String nubanAccountNo);

    List<WalletAccount> findByAccountOwner_PhoneNumberAndWalletAccountTypeAccountypeID(String phoneNumber, Long walletAccountType_accountypeID);

    @EntityGraph(attributePaths = {"subWallets", "parent"})
    List<WalletAccount> findAllByParent(WalletAccount parent);

    @EntityGraph(attributePaths = {"subWallets", "parent"})
    List<WalletAccount> findAllByParentAndWalletAccountType_AccountypeID(WalletAccount parent, Long walletAccountType_accountypeID);

    List<WalletAccount> findAllByTrackingRefIsNull();

    List<WalletAccount> findAllByNubanAccountNoIsNull();

    List<WalletAccount> findAllByNubanAccountNoNotNull();

    @Query("select walletAccount from WalletAccount walletAccount where walletAccount.accountOwner is not null and walletAccount.nubanAccountNo not in ('', ' ', 'null', 'YJIoHUi9jpgngZ+GyiApyQ==') and walletAccount.nubanAccountNo is not null ")
    List<WalletAccount> findWalletAccountsByNubanAccountNoIsNotNull();

    List<WalletAccount> findWalletAccountsByNubanAccountNoNotInAndAccountOwnerNotNullAndNubanAccountNoIsNotNull(List<String> nubanAccountNo);

    @Query("select w.accountOwner from WalletAccount w where w.scheme.schemeID = ?1")
    List<Profile> findAccountOwnerByScheme_SchemeID(String schemeId);


    @Query("select w from WalletAccount w where w.currentBalance <> w.actualBalance")
    List<WalletAccount> findWalletAccountsByCurrentBalanceIsNotActualBalance();

    @Modifying
    @Query("delete from WalletAccount walletAccount where walletAccount.accountNumber = ?1")
    void deleteByAccountNumber(String accountNumber);
}
