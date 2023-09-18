package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.JournalLineRepository;
import ng.com.justjava.corebanking.service.mapper.SchemeMapper;
import ng.com.justjava.corebanking.domain.Journal;
import ng.com.justjava.corebanking.domain.JournalLine;
import ng.com.justjava.corebanking.domain.Scheme;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.AccountStatus;
import ng.com.justjava.corebanking.domain.enumeration.PaymentType;
import ng.com.justjava.corebanking.domain.enumeration.TransactionStatus;
import ng.com.justjava.corebanking.service.AgentService;
import ng.com.justjava.corebanking.service.JournalLineService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.SchemeService;
import ng.com.justjava.corebanking.service.TellerService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.ProfileDTO;
import ng.com.justjava.corebanking.service.dto.SchemeDTO;
import ng.com.justjava.corebanking.service.dto.SchemeSumOfCreditsDTO;
import ng.com.justjava.corebanking.service.dto.WalletSumOfCreditsDTO;
import ng.com.justjava.corebanking.util.Utility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class JournalLineServiceImpl implements JournalLineService {
  @Value("${app.constants.dfs.transit-account}")
  private String TRANSIT_ACCOUNT;

  private final JournalLineRepository journalLineRepository;
  private final WalletAccountService walletAccountService;
  private final TellerService tellerService;
  private final SchemeService schemeService;
  private final SchemeMapper schemeMapper;
  private final Utility utility;
  private final ProfileService profileService;
  private final AgentService agentService;

  public JournalLineServiceImpl(
    JournalLineRepository journalLineRepository,
    @Lazy WalletAccountService walletAccountService,
    @Lazy TellerService tellerService,
    @Lazy SchemeService schemeService,
    SchemeMapper schemeMapper,
    @Lazy Utility utility,
    @Lazy ProfileService profileService,
    @Lazy AgentService agentService
  ) {
    this.journalLineRepository = journalLineRepository;
    this.walletAccountService = walletAccountService;
    this.tellerService = tellerService;
    this.schemeService = schemeService;
    this.schemeMapper = schemeMapper;
    this.utility = utility;
    this.profileService = profileService;
    this.agentService = agentService;
  }

  @Override
  public JournalLine save(JournalLine journalLine, Journal journal, WalletAccount walletAccount) {
    journalLine.setJounal(journal);
    journalLine.setWalletAccount(walletAccount);
    journalLineRepository.save(journalLine);
    return journalLine;
  }

  @Override
  public JournalLine save(JournalLine journalLine) {
    return journalLineRepository.save(journalLine);
  }

  @Override
  public List<JournalLine> findAll() {
    return journalLineRepository.findAll();
  }

  @Override
  public Page<JournalLine> findAll(Pageable pageable) {
    return journalLineRepository.findAll(pageable);
  }

  @Override
  public Optional<JournalLine> findOne(Long id) {
    return journalLineRepository.findById(id);
  }

  @Override
  public List<JournalLine> findByWalletAccount_AccountNumberWithDateRange(
    String accountNumber,
    LocalDateTime fromDate,
    LocalDateTime toDate
  ) {
    return journalLineRepository.findByWalletAccount_AccountNumberAndJounalTransDateBetween(accountNumber, fromDate, toDate);
  }

  @Override
  public List<JournalLine> findByWalletAccount_AccountNumber(String accountNumber) {
    return journalLineRepository.findByWalletAccount_AccountNumberOrderByJounal_TransDateDesc(accountNumber);
  }

  @Override
  public void delete(Long id) {
    journalLineRepository.deleteById(id);
  }

  @Override
  public List<SchemeSumOfCreditsDTO> getSumOfSchemeCredits() {
    return journalLineRepository.getSumOfSchemeCredits();
  }

  @Override
  public List<WalletSumOfCreditsDTO> getSumOfWalletCredits() {
    return journalLineRepository.getSumOfWalletCredits();
  }

  @Override
  public List<WalletSumOfCreditsDTO> getSumOfWalletDebits() {
    return journalLineRepository.getSumOfWalletDebits();
  }

  public List<SchemeSumOfCreditsDTO> getSumOfSchemeDebits() {
    return journalLineRepository.getSumOfSchemeDebits();
  }

  @Override
  public List<JournalLine> findByWalletAccountAccountNumberAndDebitGreaterThan(String accountNumber, Double debit) {
    // TODO Auto-generated method stub
    return journalLineRepository.findByWalletAccountAccountNumberAndDebitGreaterThanAndWalletAccountStatusNot(
      accountNumber,
      debit,
      AccountStatus.INACTIVE
    );
  }

  @Override
  public List<JournalLine> findByWalletAccountAccountNumberAndCreditGreaterThan(String accountNumber, Double credit) {
    return journalLineRepository.findByWalletAccountAccountNumberAndCreditGreaterThanAndWalletAccountStatusNot(
      accountNumber,
      credit,
      AccountStatus.INACTIVE
    );
  }

  @Override
  public List<JournalLine> findAccountDailyTransaction(String accountNumber) {
    //        return journalLineRepository.findByWalletAccountAccountNumberAndJounalTransDateAndWalletAccountStatusNot(accountNumber, LocalDate.now(), AccountStatus.INACTIVE);
    return journalLineRepository.findByWalletAccountAccountNumberAndJounalTransDateBetweenAndWalletAccountStatusNot(
      accountNumber,
      LocalDate.now().atStartOfDay(),
      LocalDate.now().atTime(LocalTime.MAX),
      AccountStatus.INACTIVE
    );
  }

  @Override
  public Double getAccountDailyTransactionAmount(String accountNumber) {
    List<JournalLine> dailyTrans = findAccountDailyTransaction(accountNumber);
    /*Double amount = 0.00;
		for (JournalLine journalLine : dailyTrans) {
			amount += journalLine.getDebit();
		}*/

    return dailyTrans.stream().map(JournalLine::getDebit).reduce(0.0, Double::sum);
  }

  @Override
  public List<JournalLine> findCustomerInvoice() {
    String phoneNumber = "";
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
      phoneNumber = ((UserDetails) principal).getUsername();
    } else phoneNumber = principal.toString();

    //        return journalLineRepository.findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeOrderByJounal_TransDateDesc(phoneNumber, PaymentType.INVOICE);
    return journalLineRepository.findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeNotOrderByJounal_TransDateDesc(
      phoneNumber,
      PaymentType.PAYMENT
    );
  }

  @Override
  public List<JournalLine> findByWalletAccountAccountOwnerPhoneNumber(String phoneNumber, HttpSession session) {
    Scheme schemeFromSession = utility.getSchemeFromSession(session);
    phoneNumber = utility.formatPhoneNumber(phoneNumber);
    return journalLineRepository.findByWalletAccountAccountOwnerPhoneNumberAndWalletAccountSchemeAndWalletAccountStatusNot(
      phoneNumber,
      schemeFromSession,
      AccountStatus.INACTIVE
    );
  }

  @Override
  public List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberOrderByDate(String phoneNumber, HttpSession session) {
    Scheme schemeFromSession = utility.getSchemeFromSession(session);
    phoneNumber = utility.formatPhoneNumber(phoneNumber);
    return journalLineRepository.findByWalletAccountAccountOwnerPhoneNumberAndWalletAccountSchemeAndWalletAccountStatusNotOrderByJounalTransDateDesc(
      phoneNumber,
      schemeFromSession,
      AccountStatus.INACTIVE
    );
  }

  @Override
  public List<JournalLine> findByWalletAccountAccountOwnerPhoneNumberWithDateRange(
    String phoneNumber,
    LocalDateTime startDate,
    LocalDateTime fromDate,
    HttpSession session
  ) {
    Scheme schemeFromSession = utility.getSchemeFromSession(session);
    phoneNumber = utility.formatPhoneNumber(phoneNumber);
    return journalLineRepository.findByWalletAccountAccountOwnerPhoneNumberAndWalletAccountSchemeAndWalletAccountStatusNotAndJounalTransDateBetween(
      phoneNumber,
      schemeFromSession,
      AccountStatus.INACTIVE,
      startDate,
      fromDate
    );
  }

  @Override
  public List<JournalLine> findByUserIsCurrentUser(HttpSession session) {
    return journalLineRepository.findByUserIsCurrentUserAndScheme(utility.getSchemeFromSession(session));
  }

  @Override
  public List<JournalLine> findByUserIsCurrentUserOrderByTransactionDate(HttpSession session) {
    return journalLineRepository.findByUserIsCurrentUserOrderByTransactionDateAndWalletAccountScheme(utility.getSchemeFromSession(session));
  }

  @Override
  public List<JournalLine> findByUserIsCurrentUserOrderByTransactionDateDateRange(
    LocalDateTime fromDate,
    LocalDateTime toDate,
    HttpSession session
  ) {
    return journalLineRepository.findByUserIsCurrentUserOrderByTransactionDateAndWalletAccountScheme(
      utility.getSchemeFromSession(session),
      fromDate,
      toDate
    );
  }

  @Override
  public List<JournalLine> findByUserIsCurrentUserOrderByTransactionDateAirtimeData(HttpSession session) {
    return journalLineRepository.findByUserIsCurrentUserAirtimeDataOrderByTransactionDateAndWalletAccountScheme(
      utility.getSchemeFromSession(session)
    );
  }

  @Override
  public List<JournalLine> findCustomerReceipt(HttpSession session) {
    String phoneNumber = null;
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
      phoneNumber = ((UserDetails) principal).getUsername();
    } else phoneNumber = principal.toString();

    Scheme schemeFromSession = utility.getSchemeFromSession(session);
    //        return journalLineRepository.findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeAndDebitGreaterThanAndWalletAccountSchemeAndWalletAccountStatusNot(phoneNumber, PaymentType.PAYMENT, 0.00, schemeFromSession, AccountStatus.INACTIVE);
    return journalLineRepository.findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeNotAndDebitGreaterThanAndWalletAccountSchemeAndWalletAccountStatusNotAndJounalTransactionStatusOrderByJounal_TransDateDesc(
      phoneNumber,
      PaymentType.INVOICE,
      0.00,
      schemeFromSession,
      AccountStatus.INACTIVE,
      TransactionStatus.COMPLETED
    );
  }

  @Override
  public LinkedHashSet<Journal> findCustomerReceipts(HttpSession session) {
    String phoneNumber = null;
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
      phoneNumber = ((UserDetails) principal).getUsername();
    } else phoneNumber = principal.toString();

    Scheme schemeFromSession = utility.getSchemeFromSession(session);

    List<JournalLine> journalLines = journalLineRepository.findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeNotAndDebitGreaterThanAndWalletAccountSchemeAndWalletAccountStatusNotAndJounalTransactionStatusOrderByJounal_TransDateDesc(
      phoneNumber,
      PaymentType.INVOICE,
      0.00,
      schemeFromSession,
      AccountStatus.INACTIVE,
      TransactionStatus.COMPLETED
    );

    LinkedHashSet<Journal> journals = new LinkedHashSet<>();

    for (JournalLine j : journalLines) {
      journals.add(j.getJounal());
    }

    return journals;
  }

  @Override
  public List<JournalLine> findByJounalReference(String reference) {
    // TODO Auto-generated method stub
    return journalLineRepository.findByJounalReference(reference);
  }

  @Override
  public List<JournalLine> findByJounalReferenceNotTransitAccount(String reference) {
    // TODO Auto-generated method stub
    return journalLineRepository.findByJounalReferenceAndWalletAccountAccountNumberNot(reference, TRANSIT_ACCOUNT);
  }

  @Override
  public List<JournalLine> findByJounalReferenceAndWalletAccount_AccountNumber(String reference, String accountNumber) {
    return journalLineRepository.findByJounalReferenceAndWalletAccount_AccountNumber(reference, accountNumber);
  }

  @Override
  public List<JournalLine> findAllByWalletAccountAccountNumberAndJounalTransDateBetween(
    String accountNumber,
    LocalDateTime transDate,
    LocalDateTime endDate
  ) {
    return journalLineRepository.findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndJounalPaymentTypeOrderByJounal_TransDateAsc(
      accountNumber,
      transDate,
      endDate,
      PaymentType.PAYMENT
    );
  }

  @Override
  public List<JournalLine> findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndScheme(
    String accountNumber,
    LocalDateTime startDate,
    LocalDateTime endDate,
    String schemeId
  ) {
    Scheme scheme = schemeService.findBySchemeID(schemeId);
    List<Scheme> schemeList = getAdminSchemes();

    if (schemeList.contains(scheme)) {
      List<JournalLine> journalLines = journalLineRepository.findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndJounalPaymentTypeAndWalletAccountSchemeOrderByJounal_TransDateDesc(
        accountNumber,
        startDate,
        endDate,
        PaymentType.PAYMENT,
        scheme
      );

      List<JournalLine> journalLineList = new ArrayList<>();
      for (JournalLine journalLine : journalLines) {
        if (journalLine.getWalletAccount().getScheme().equals(scheme)) {
          journalLineList.add(journalLine);
        }
      }

      return journalLineList;
    }
    return null;
  }

  @Override
  public List<JournalLine> findAllByWalletAccountAccountNumberAndJounalTransDateBetweenSearchByKey(
    String accountNumber,
    LocalDateTime transDate,
    LocalDateTime endDate,
    String key
  ) {
    //        return journalLineRepository.findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndJounalPaymentTypeOrJounalReferenceContainingIgnoreCase(accountNumber, transDate, endDate, PaymentType.PAYMENT, key);
    //        return journalLineRepository.searchByAccountNumberAndJounalPaymentTypeWithDateRange(accountNumber, transDate, endDate, PaymentType.PAYMENT, key);
    return journalLineRepository.searchByAccountNumberAndJounalPaymentTypeNotWithDateRange(
      accountNumber,
      transDate,
      endDate,
      PaymentType.INVOICE,
      key
    );
  }

  @Override
  public Page<JournalLine> findAllByJounalTransDateBetween(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate) {
    //        return journalLineRepository.findAllByJounalTransDateBetweenAndJounalPaymentTypeOrderByJounal_TransDateDesc(startDate, endDate, PaymentType.PAYMENT, pageable);
    return journalLineRepository.findAllByJounalTransDateBetweenAndJounalPaymentTypeNotOrderByJounal_TransDateDesc(
      startDate,
      endDate,
      PaymentType.INVOICE,
      pageable
    );
  }

  @Override
  public List<JournalLine> findAllByJounalTransDateBetweenAndScheme(LocalDateTime startDate, LocalDateTime endDate, String schemeId) {
    List<Scheme> adminSchemes = getAdminSchemes();
    Scheme scheme = schemeService.findBySchemeID(schemeId);
    if (adminSchemes.contains(scheme)) {
      List<JournalLine> journalLines = journalLineRepository.findAllByJounalTransDateBetweenAndJounalPaymentTypeNotAndWalletAccountSchemeOrderByJounal_TransDateDesc(
        startDate,
        endDate,
        PaymentType.INVOICE,
        scheme
      );
      List<JournalLine> journalLineList = new ArrayList<>();

      for (JournalLine journalLine : journalLines) {
        if (journalLine.getWalletAccount().getScheme().equals(scheme)) {
          journalLineList.add(journalLine);
        }
      }

      return journalLineList;
    }
    return null;
  }

  @Override
  public Page<JournalLine> findAllJournaline(Pageable pageable) {
    return journalLineRepository.findAll(pageable);
  }

  @Override
  public Page<JournalLine> findAllByWalletAccount_AccountOwnerIsNotNull(Pageable pageable) {
    return journalLineRepository.findAllByJounalPaymentTypeNotAndWalletAccount_AccountOwnerIsNotNullOrderByJounal_TransDateDesc(
      PaymentType.INVOICE,
      pageable
    );
  }

  @Override
  public Page<JournalLine> findAllByWalletAccount_AccountOwnerIsNotNullSearch(String key, Pageable pageable) {
    return journalLineRepository.searchByPaymentTypeNotAccountOwnerAndSearchKey(PaymentType.INVOICE, key, pageable);
  }

  @Override
  public Page<JournalLine> findAllByWalletAccount_AccountOwnerIsNotNullAndWalletAccountNumberSearch(
    String accountNumber,
    String key,
    Pageable pageable
  ) {
    return journalLineRepository.searchByAccountNumberPaymentTypeNotAccountOwnerAndSearchKey(
      PaymentType.INVOICE,
      accountNumber,
      key,
      pageable
    );
  }

  @Override
  public Page<JournalLine> findAllByWalletAccount_AccountOwnerIsNotNullSearchAndScheme(String key, String schemeId, Pageable pageable) {
    Scheme scheme = schemeService.findBySchemeID(schemeId);

    List<WalletAccount> walletAccountList = walletAccountService.findAllByAccountOwnerIsNotNullAndScheme(schemeId);
    //        return journalLineRepository.findAllByJounalPaymentTypeAndWalletAccount_AccountOwnerIsNotNullOrJounalReferenceContainingIgnoreCaseAndWalletAccountScheme_SchemeID(PaymentType.PAYMENT, key, scheme, pageable);

    List<JournalLine> journalLines = journalLineRepository.searchByPaymentTypeNotAccountOwnerAndSchemeAndSearchKey(
      PaymentType.INVOICE,
      key,
      walletAccountList
    );

    List<JournalLine> results = new ArrayList<>();

    for (JournalLine journalLine : journalLines) {
      if (journalLine.getWalletAccount().getScheme().equals(scheme)) {
        results.add(journalLine);
      }
    }

    int start = (int) pageable.getOffset();
    int totalSize = results.size();

    int end = Math.min((start + pageable.getPageSize()), totalSize);
    if (start > totalSize) {
      return new PageImpl<>(new ArrayList<>(), pageable, totalSize);
    }

    return new PageImpl<>(results.subList(start, end), pageable, totalSize);
  }

  @Override
  public Double sumOfSuspendedTransactionsAmount(String accountNumber) {
    Double aDouble = journalLineRepository.sumSuspendedTransactionsAmount(accountNumber);
    return aDouble == null ? 0.0 : aDouble;
  }

  @Override
  public Page<JournalLine> getAllTransitAccountDebitTransactions(Pageable pageable) {
    return journalLineRepository.findAllByWalletAccount_AccountNumberAndDebitGreaterThanOrderByJounal_TransDateDesc(
      TRANSIT_ACCOUNT,
      0.00,
      pageable
    );
  }

  @Override
  public Page<JournalLine> getAllTransitAccountCreditTransactions(Pageable pageable) {
    return journalLineRepository.findAllByWalletAccount_AccountNumberAndCreditGreaterThanOrderByJounal_TransDateDesc(
      TRANSIT_ACCOUNT,
      0.00,
      pageable
    );
  }

  @Override
  public Page<JournalLine> getAllTransitAccountTransactions(Pageable pageable) {
    return journalLineRepository.findAllByWalletAccount_AccountNumberOrderByJounal_TransDateDesc(TRANSIT_ACCOUNT, pageable);
  }

  @Override
  public Page<JournalLine> getAllJournalLinesByStatusAndAccountNumberDateRange(
    Pageable pageable,
    TransactionStatus status,
    String accountNumber,
    LocalDateTime startDate,
    LocalDateTime endDate
  ) {
    return journalLineRepository.findAllByWalletAccountAccountNumberAndJounalTransactionStatusAndJounalTransDateBetweenOrderByJounal_TransDateDesc(
      accountNumber,
      status,
      startDate,
      endDate,
      pageable
    );
  }

  @Override
  public Long count() {
    return journalLineRepository.count();
  }

  @Override
  public double sumOfAllCreditsToAllCustomerAccounts(LocalDateTime startDate, LocalDateTime endDate) {
    return journalLineRepository.sumOfAllCreditsToAllCustomerAccounts(startDate, endDate);
  }

  @Override
  public double sumOfAllDebitsToAllCustomerAccounts(LocalDateTime startDate, LocalDateTime endDate) {
    return journalLineRepository.sumOfAllDebitsToAllCustomerAccounts(startDate, endDate);
  }

  @Override
  public double sumOfAllCreditsToAnAccount(String accountNumber, LocalDateTime startDate, LocalDateTime endDate) {
    return journalLineRepository.sumOfAllCreditsToAnAccount(accountNumber, startDate, endDate);
  }

  @Override
  public double sumOfAllDebitsToAnAccount(String accountNumber, LocalDateTime startDate, LocalDateTime endDate) {
    return journalLineRepository.sumOfAllDebitsToAnAccount(accountNumber, startDate, endDate);
  }

  @Override
  public List<JournalLine> findAllSuperAgentJounalTransDateBetween(
    Pageable pageable,
    String superAgentPhoneNumber,
    LocalDateTime startDate,
    LocalDateTime endDate
  ) {
    superAgentPhoneNumber = utility.formatPhoneNumber(superAgentPhoneNumber);
    List<WalletAccount> walletAccountList = walletAccountService.findByAccountOwnerPhoneNumber(superAgentPhoneNumber);
    if (walletAccountList.isEmpty()) {
      return null;
    }

    WalletAccount walletAccount = walletAccountList.get(0);
    Set<WalletAccount> subWallets = walletAccount.getSubWallets();
    return journalLineRepository.findAllByWalletAccountAndWalletAccountSubWalletsInAndJounalTransDateBetweenOrderByJounal_TransDateDesc(
      walletAccount,
      subWallets,
      startDate,
      endDate
    );
  }

  @Override
  public List<JournalLine> findCustomerInvoiceByPhoneNumber(String phoneNumber) {
    phoneNumber = utility.formatPhoneNumber(phoneNumber);
    return journalLineRepository.findByWalletAccountAccountOwnerPhoneNumberAndJounalPaymentTypeNotAndDebitGreaterThan(
      phoneNumber,
      PaymentType.INVOICE,
      0.0
    );
  }

  /*


    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {

        Object principal = auth.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        GenericResponseDTO adminScheme = schemeService.getAdminScheme(username);
        List<SchemeDTO> data = (List<SchemeDTO>) adminScheme.getData();

        List<Scheme> schemeList = data.stream().map(schemeMapper::toEntity).collect(Collectors.toList());

        return journalLineRepository.findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndJounalPaymentTypeOrJounalReferenceContainingIgnoreCaseAndWalletAccountSchemeIn(accountNumber, transDate, endDate, PaymentType.PAYMENT, key, schemeList);
    }

*/

  /*
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {

        Object principal = auth.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        GenericResponseDTO adminScheme = schemeService.getAdminScheme(username);
        List<SchemeDTO> data = (List<SchemeDTO>) adminScheme.getData();

        List<Scheme> schemeList = data.stream().map(schemeMapper::toEntity).collect(Collectors.toList());

        return journalLineRepository.findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndJounalPaymentTypeAndWalletAccountSchemeIn(accountNumber, transDate, endDate, PaymentType.PAYMENT, schemeList);
    }

        */

  @Override
  public List<Scheme> getAdminSchemes() {
    List<Scheme> schemeList = new ArrayList<>();

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    Object principal = auth.getPrincipal();
    String username;
    if (principal instanceof UserDetails) {
      username = ((UserDetails) principal).getUsername();
    } else {
      username = principal.toString();
    }
    GenericResponseDTO adminScheme = schemeService.getAdminScheme(username);
    List<SchemeDTO> data = (List<SchemeDTO>) adminScheme.getData();

    if (data != null) {
      schemeList = data.stream().map(schemeMapper::toEntity).collect(Collectors.toList());
    }

    return schemeList;
  }

  @Override
  public List<JournalLine> getCustomerBonusPoints(String phoneNumber) {
    Optional<WalletAccount> bonusPointAccountOptional = walletAccountService.findOneByAccountOwnerAndBonusPointAccountType(phoneNumber);
    if (bonusPointAccountOptional.isPresent()) {
      WalletAccount walletAccount = bonusPointAccountOptional.get();

      String accountNumber = walletAccount.getAccountNumber();

      return journalLineRepository.findByWalletAccount_AccountNumberOrderByJounal_TransDateDesc(accountNumber);
    }
    return new ArrayList<>();
  }

  @Override
  public List<JournalLine> getTransactionStatements(String transRef) {
    return journalLineRepository.findByJounalReference(transRef);
  }

  @Override
  public String getUniqueTransRef() {
    return utility.getUniqueTransRef();
  }

  @Override
  public List<JournalLine> findByWalletAccountIn(List<WalletAccount> walletAccounts) {
    return journalLineRepository.findByWalletAccountIsIn(walletAccounts);
  }

  @Override
  public GenericResponseDTO getSuperAgentAgentsTransactions(Pageable pageable) {
    Optional<ProfileDTO> currentUser = profileService.findByUserIsCurrentUser();
    if (currentUser.isPresent()) {
      ProfileDTO profileDTO = currentUser.get();
      String phoneNumber = profileDTO.getPhoneNumber();
      List<WalletAccount> subAgentWallets = walletAccountService.findSubAgentWalletsBySuperAgentPhoneNumber(phoneNumber, 2L);
      System.out.println("Sub Agents Wallet ==> " + subAgentWallets);
      if (subAgentWallets != null) {
        List<JournalLine> journalLines = journalLineRepository.findByWalletAccountIsIn(subAgentWallets);
        System.out.println("Sub Agents Lines ==> " + journalLines);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("totalNumberOfRecords", journalLines.size());

        return new GenericResponseDTO("00", HttpStatus.OK, "success", journalLines, metaMap);
      }

      return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No Sub agents found", null);
    }

    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid login profile", null);
  }

  @Override
  public GenericResponseDTO getSuperAgentTellersTransactions(Pageable pageable) {
    Optional<ProfileDTO> currentUser = profileService.findByUserIsCurrentUser();
    if (currentUser.isPresent()) {
      ProfileDTO profileDTO = currentUser.get();
      String phoneNumber = profileDTO.getPhoneNumber();
      List<WalletAccount> subAgentWallets = walletAccountService.findSubAgentWalletsBySuperAgentPhoneNumber(phoneNumber, 5L);
      System.out.println("Tellers Wallet ==> " + subAgentWallets);

      if (subAgentWallets != null) {
        List<JournalLine> journalLines = journalLineRepository.findByWalletAccountIsIn(subAgentWallets);
        System.out.println("Tellers lines ==> " + journalLines);
        /*
                HttpHeaders headers = PaginationUtil
                    .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), journalLines);

                Map<String, Object> metaMap = new LinkedHashMap<>();
                metaMap.put("size", journalLines.getSize());
                metaMap.put("totalNumberOfRecords", journalLines.getTotalElements());*/

          return new GenericResponseDTO("00", HttpStatus.OK, "success", journalLines);
      }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No sub agents found", null);
    }
      return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid login profile", null);
  }

//    @Override
//    public GenericResponseDTO getAllWalletsSummary(String schemeId, LocalDateTime fromDate, LocalDateTime toDate) {
//        journalLineRepository.getAllWalletsSummary(schemeId, fromDate, toDate);
//        return new GenericResponseDTO("00", HttpStatus.OK, "success", null);
//    }
}
