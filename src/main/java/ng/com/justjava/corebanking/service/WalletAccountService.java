package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.Scheme;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.AccountStatus;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link WalletAccount}.
 */
public interface WalletAccountService {

    /**
     * Save a walletAccount.
     *
     * @param walletAccountDTO the entity to save.
     * @return the persisted entity.
     */
    WalletAccountDTO save(WalletAccountDTO walletAccountDTO);

    WalletAccount save(WalletAccount walletAccount);

    List<WalletAccount> saveAll(List<WalletAccount> walletAccounts);

    /**
     * Get all the walletAccounts.
     *
     * @return the list of entities.
     */
    Page<WalletAccountDTO> findAll(Pageable pageable);

    List<WalletAccountDTO> findAll();

    List<WalletAccount> findAllWalletAccounts();

    Page<WalletAccountDTO> findAllBySchemeAndSearchByKey(String schemeId, String searchKey, Pageable pageable);

    List<WalletAccountDTO> findByUserIsCurrentUser();

    List<WalletAccountDTO> findByUserIsCurrentUser(HttpSession session);

    List<WalletAccountDTO> findByUserIsCurrentUserAndScheme(HttpSession session);

    List<WalletAccountDTO> findByUserIsCurrentUserOrderByDateOpen(HttpSession session);

    List<WalletAccountDTO> findAllByAccountOwnerPhoneNumber(String phoneNumber, HttpSession session);

    List<WalletAccountDTO> findAllByAccountOwnerPhoneNumber(String phoneNumber);

    /**
     * Get the "id" walletAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WalletAccountDTO> findOne(Long id);

    /**
     * Delete the "id" walletAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    InlineResponseDatum verifyTransRef(String transRef);

    ResponseEntity<PaymentResponseDTO> fund(FundDTO fundDTO);

    ResponseEntity<GenericResponseDTO> fundWalletStp(FundDTO fundDTO);

    ResponseEntity<PaymentResponseDTO> fundSTP(FundDTO fundDTO);

    ResponseEntity<PaymentResponseDTO> fundWalletExternal(String referenceNo);

    ResponseEntity<PaymentResponseDTO> fundWalletExternalDemo(String transRef);

    PaymentResponseDTO sendMoney(FundDTO sendMoneyDTO);

    PaymentResponseDTO sendMoneyBulkTransaction(FundDTO sendMoneyDTO);

    PaymentResponseDTO sendBulkTransaction(FundDTO sendBulkMoneyDTO);

    PaymentResponseDTO sendMoneyWithCorrespondence(String sourceAccountNumber, String accountNumber, double amount);

    PaymentResponseDTO requestMoney(FundDTO requestMoneyDTO);

    PaymentResponseDTO treatInvoice(InvoiceDTO invoiceDTO);

    List<WalletAccount> findByAccountOwnerPhoneNumber(String phoneNumber);

    List<WalletAccount> findAgentWallet(String phoneNumber, Long accountTypeId);

    List<WalletAccount> findSubAgentWalletsBySuperAgentPhoneNumber(String superAgentPhoneNumber, Long accountTypeId);

    Optional<WalletAccount> findByAccountOwnerPhoneNumberAndAccountName(String phoneNumber, String accountName);

    List<WalletAccount> findByAccountOwnerPhoneNumberAndAccountNameAndScheme_SchemeID(String phoneNumber, String accountName, String schemeID);

    List<WalletAccount> findByAccountOwnerPhoneNumberAndScheme_SchemeID(String phoneNumber, String schemeID);

    WalletAccount findOneByAccountNumber(String accountNumber);

    WalletAccount findByAccountNumber(String accountNumber);

    NewWalletAccountResponse openNewWalletAccountByForeignEndPoint(NewWalletAccountDTO newWalletAccountDTO);

    Optional<WalletAccount> findByAccountName(String accountName);

    boolean verifyTransaction(String transRef);

    Page<WalletAccountDTO> findAllByAccountOwnerIsNotNull(Pageable pageable);

    List<WalletAccount> findAllByAccountOwnerIsNotNull();

    List<WalletAccountDTO> findAllByAccountOwnerIsNotNullAndSchemeAndStatusNot(String schemeId);

    List<WalletAccount> findAllByAccountOwnerIsNotNullAndScheme(String schemeId);

    Page<WalletAccountDTO> findAllByAccountOwnerIsNotNullSearchKeyword(Pageable pageable, String keyword);

    Page<WalletAccountDTO> findAllByAccountOwnerIsNotNullSearchKeywordAndScheme(String keyword, String schemeId, Pageable pageable);

    Page<WalletAccountDTO> findAllPrimaryWalletByAccountOwnerIsNotNullSearchKeywordAndScheme(String keyword, String schemeId, Pageable pageable);

    List<WalletAccountDTO> findAllByAccountOwnerIsNull();

    List<WalletAccountDTO> findAllBySchemeAndAccountOwnerIsNull(Scheme scheme);

    List<Profile> findAllBySchemeId(String schemeId);

    GenericResponseDTO changeAccountStatus(String accountNumber, String status);

    GenericResponseDTO managerUserStatus(String phoneNumber, String status, Optional<String> accountNumber);

    GenericResponseDTO checkAccountStatus(String accountNumber);

    boolean checkAccountIsActive(String accountNumber);

    boolean checkAccountCanBeDebited(String accountNumber, double amount);

    boolean checkAccountCanBeCredited(String accountNumber, double amount, String sourceAccountNumber);

    boolean checkAccountCanBeCredited(List<BulkBeneficiaryDTO> accountNumber, double amount, String sourceAccountNumber);

/*
    boolean checkAccountCanBeCredited(List<String> accountNumber, double amount, String sourceAccountNumber);
*/

    boolean existsByStatusAndAccountNumberIn(AccountStatus status, List<BulkBeneficiaryDTO> accountNumbers);

//    boolean existsByAccountNumber(String accountNumber);

//    ValidTransactionResponse isValidTransaction(String channel, String sourceAccountNumber, String accountNumber,  Double amount, Double bonusAmount);

    String createWalletExternalForUssd(WalletExternalDTO walletExternalDTO);

    Long count();

    double getActualBalance(String accountNumber);

    String sendUSSDFundWalletLink(String message, Profile profile);

    ResponseEntity<GenericResponseDTO> createWalletExternal(WalletExternalDTO walletExternalDTO, HttpSession session);

    ResponseEntity<GenericResponseDTO> createCorporateWalletExternal(WalletExternalDTO walletExternalDTO, HttpSession session);

    List<WalletAccount> findAllByAccountNumberAndAccountNameAndSchemeId(String accountNumber, String accountName, Long schemeId);

    List<WalletAccount> findAllBySchemeIDAndAccountOwnerIsNotNullAndSearch(String schemeId, String key);

    Optional<WalletAccount> findOneByAccountOwnerAndBonusPointAccountType(String profilePhoneNumber);

    WalletAccount getCustomerBonusAccount(Profile profile);

    ResponseEntity<GenericResponseDTO> addSpecialAccount(String accountName, String accountNumber) throws URISyntaxException;

    ResponseEntity<GenericResponseDTO> addSpecialAccount(String accountName, String accountNumber, String scheme) throws URISyntaxException;

    ResponseEntity<GenericResponseDTO> renameSpecialAccount(String accountName, String accountNumber);

    List<WalletAccount> findAllByTrackingRefNotNull();

    List<WalletAccount> findAllByTrackingRefIsNotNullAndNubanAccountNoIsNull();

    List<WalletAccount> findByNubanAccountNo(String nubanAccountNo);

    List<WalletAccountDTO> findAgentPrimaryAccount(String phoneNumber, Long accountTypeId);

    ResponseEntity<GenericResponseDTO> createWalletForExternal(WalletExternalDTO walletExternalDTO, GenericResponseDTO genericResponseDTO, Scheme bySchemeID, Profile profile, boolean createWallet);

    List<WalletAccount> findAllByTrackingRefIsNull();

    List<WalletAccount> findAllByNubanAccountNoIsNull();

    List<WalletAccount> findAllByNubanAccountNoNotNull();

    List<WalletAccountDTO> findAllByNubanAccountNoNotNullWithBalances();

    ResponseEntity<GenericResponseDTO> retrieveNuban(String phoneNumber);

    ResponseEntity<GenericResponseDTO> retrieveNubanByScheme(String phoneNumber, String schemeId);

    PaymentResponseDTO sendMoneyWithCorrespondence(String sourceAccountNumber, String accountNumber, double amount,
                                                   String narration);

    PaymentResponseDTO sendMoneyToBankWithCorrespondence(FundDTO fundDTO);

    List<WalletAccount> findByPhoneNumberAndScheme(String username, String scheme);

    WalletAccount resetBalances(double resetBalance, String accountNumber);

    WalletAccount syncBalances(String accountNumber);

    List<WalletAccountDTO> findAllCurrentBalanceNotEqualActualBalance();

    void deleteByAccountNumber(String accountNumber);
}
