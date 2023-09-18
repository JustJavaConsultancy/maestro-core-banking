package ng.com.justjava.corebanking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ng.com.justjava.corebanking.repository.LoanRepository;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.mapper.LoanMapper;
import ng.com.justjava.corebanking.service.mapper.LoansCreatedMapper;
import ng.com.justjava.corebanking.domain.Loan;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.LoanStatus;
import ng.com.justjava.corebanking.domain.enumeration.LoanType;
import ng.com.justjava.corebanking.service.LenderService;
import ng.com.justjava.corebanking.service.LibertyAssuredService;
import ng.com.justjava.corebanking.service.LoanService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.justjava.corebanking.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Loan}.
 */
@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    private final Logger log = LoggerFactory.getLogger(LoanServiceImpl.class);

    private final LoanRepository loanRepository;
    private final LibertyAssuredService libertyAssuredService;

    private final LoanMapper loanMapper;
    private final LoansCreatedMapper loanCreatedMapper;

    private final ProfileService profileService;
    private final LenderService lenderService;
    private final WalletAccountService walletAccountService;
    private final Utility utility;
    private final Environment environment;

    //    charge_percentage
    @Value("${app.liberty-assured.charge_percentage}")
    private double libertyChargePercentage;

    @Value("${app.liberty-assured.charge_flat_rate}")
    private double libertyChargeFlatRate;

    @Value("${app.percentage.vat}")
    private double vatFeePercentage;


    public LoanServiceImpl(LoanRepository loanRepository, LibertyAssuredService libertyAssuredService, LoansCreatedMapper loanCreatedMapper,
                           LoanMapper loanMapper, ProfileService profileService, LenderService lenderService, WalletAccountService walletAccountService, Utility utility,
                           Environment environment) {
        this.loanRepository = loanRepository;
        this.libertyAssuredService = libertyAssuredService;
        this.loanMapper = loanMapper;
        this.profileService = profileService;
        this.lenderService = lenderService;
        this.walletAccountService = walletAccountService;
        this.utility = utility;
        this.loanCreatedMapper = loanCreatedMapper;
        this.environment = environment;
    }

    @Override
    public LoanDTO save(LoanDTO loanDTO) {
        Optional<ProfileDTO> theUser = profileService.findByUserIsCurrentUser();
        if (theUser.isPresent()) {
            Long profileId = theUser.get().getId();
            log.debug("Request to save Loan : {}", loanDTO);
            loanDTO.setProfileId(profileId);
            Loan loan = loanMapper.toEntity(loanDTO);
            loan = loanRepository.save(loan);
            return loanMapper.toDto(loan);
        }
        return null;
    }

    @Override
    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanDTO> findAll() {
        log.debug("Request to get all Loans");
        return loanRepository.findAll().stream()
            .map(loanMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponseDTO findAllByProfileId() {
        Optional<ProfileDTO> theUser = profileService.findByUserIsCurrentUser();
        if (theUser.isPresent()) {
            log.debug(theUser.get().getId() + "===========> the current user id");
            Long profileId = theUser.get().getId();
            log.debug(profileId + "===============>Request to get all Loans");
            Collection<LoanDTO> loans = loanRepository.findAllByProfileIdOrderByIdDesc(profileId)
                .stream()
                .map(loanCreatedMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
            return new GenericResponseDTO("00", HttpStatus.OK, "success", loans);
        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Current login user is null", null);

    }

    @Override
    public List<LoanDTO> findAllByProfile(Profile profile) {
        return loanRepository.findAllByProfileOrderByIdDesc(profile).stream().map(loanMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoanDTO> findOne(Long id) {
        log.debug("Request to get Loan : {}", id);
        return loanRepository.findById(id)
            .map(loanMapper::toDto);
    }

    @Override
    public Optional<LoanDTO> findByLoanId(String loanId) {
        return loanRepository.findByLoanId(loanId).map(loanMapper::toDto);
    }

    @Override
    public Optional<Loan> findLoanByLoanId(String loanId) {
        return loanRepository.findByLoanId(loanId);
    }

    @Override
    public List<Loan> findLoanByCustomerIdAndStatus(String customerId, LoanStatus status) {

        return loanRepository.findLoanByCustomerIdAndStatusOrderByCreatedDateDesc(customerId, status);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Loan : {}", id);
        loanRepository.deleteById(id);
    }

    @Override
    public GenericResponseDTO createLoan(LoanDTO loanDTO, HttpSession session) {

        if (loanDTO.getAmount() == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Loan amount not set");
        }

        Optional<ProfileDTO> profileDTOOptional = profileService.findByUserIsCurrentUser();

        if (profileDTOOptional.isPresent()) {
            ProfileDTO profileDTO = profileDTOOptional.get();

            Optional<LenderDTO> one = lenderService.findOne(loanDTO.getLenderId());

            if (!one.isPresent()) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Lender");
            }

            String lenderPhoneNumber = one.get().getProfilePhoneNumber();
            List<WalletAccount> sourceWallets = walletAccountService.findByAccountOwnerPhoneNumber(lenderPhoneNumber);

            double amount = loanDTO.getAmount();
            /*double vatCharge = amount * 0.07;
            double amountToBeDeducted = amount - vatCharge - 450;*/
            double commissionPercentage = libertyChargePercentage * amount;
            Double charges = commissionPercentage + libertyChargeFlatRate;
            Double vat = (commissionPercentage * vatFeePercentage) + (libertyChargeFlatRate * vatFeePercentage);

            GenericResponseDTO sufficientFunds = utility.checkSufficientFunds(amount, 0.0, sourceWallets.get(1).getAccountNumber(), charges + vat);
            log.info("Sufficient fund ==> {}", sufficientFunds);
            try {
                if (HttpStatus.OK.equals(sufficientFunds.getStatus())) {
                    double lenderBalance = Double.parseDouble((String) sufficientFunds.getData());
                    if (!Double.isNaN(lenderBalance)) {
                        if (lenderBalance <= 500000.00) {
                            Profile profileServiceByPhoneNumber = profileService.findByPhoneNumber(lenderPhoneNumber);
                            if (profileServiceByPhoneNumber != null) {
                                User user = profileServiceByPhoneNumber.getUser();
                                if (user != null) {
                                    String email = user.getEmail();
                                    if (email != null) {
                                        String subject = "LOW BALANCE NOTIFICATION";
                                        String msgContent = "Dear " + user.getFirstName() + "," +
                                            "<br/>" +
                                            "<br/>" +
                                            "<br/>" +
                                            "Here is to notify you that your Remita Wallet balance has reached the minimum limit of ₦500,000.00." +
                                            "<br/>" +
                                            "<br/>" +
                                            "Kindly fund your wallet as soon as possible. <br/><br/><br/>Thank you";
                                        utility.sendToNotificationConsumer("", "", "", subject, msgContent, "", email, lenderPhoneNumber);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (sufficientFunds.getStatus().isError()) {
                return sufficientFunds;
            }

//            CreateBorrowerDTO createBorrowerDTO = new CreateBorrowerDTO();
//            ProfileDTO profileDTO = profileDTOOptional.get();
//            loanDTO.setProfileId(profileDTO.getId());
//            List<Address> addresses = profileDTO.getAddresses();
//
//            User user = profileDTO.getUser();
//            createBorrowerDTO.setBorrowerFirstname(user.getFirstName());
//            createBorrowerDTO.setBorrowerLastname(user.getLastName());
//
//            if (!addresses.isEmpty()) {
//                createBorrowerDTO.setBorrowerAddress(addresses.get(0).getAddress());
//                createBorrowerDTO.setBorrowerCity(addresses.get(0).getCity());
//                createBorrowerDTO.setBorrowerProvince(addresses.get(0).getState());
//                createBorrowerDTO.setBorrowerCountry("NG");
//            } else {
//                createBorrowerDTO.setBorrowerAddress(profileDTO.getFullAddress());
//            }
//            createBorrowerDTO.setBorrowerBusinessName(profileDTO.getFullName());
//            if (profileDTO.getGender().equalsIgnoreCase("Male")) {
//                createBorrowerDTO.setBorrowerTitle(1);
//            } else {
//                createBorrowerDTO.setBorrowerTitle(2);
//            }
//
//            createBorrowerDTO.setBorrowerWorkingStatus("Employee");
//            if ("Male".equalsIgnoreCase(profileDTO.getGender())) {
//                createBorrowerDTO.setBorrowerGender("Male");
//            } else {
//                createBorrowerDTO.setBorrowerGender("Female");
//            }
//            createBorrowerDTO.setBorrowerMobile(utility.returnPhoneNumberFormat(loanDTO.getPhoneNumber()));
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//            LocalDate dateOfBirth = profileDTO.getDateOfBirth();
//            String format = dateOfBirth.format(formatter);
//
//            createBorrowerDTO.setBorrowerDob(format);
//            createBorrowerDTO.setBorrowerDescription(profileDTO.getFullName());
//            createBorrowerDTO.setBorrowerEmail(user.getEmail());
//
//            createBorrowerDTO.setCustomField5037(profileDTO.getBvn());
//            createBorrowerDTO.setCustomField5035(profileDTO.getNin());
//            createBorrowerDTO.setCustomField5040("Married");
//
//            ResponseEntity<GenericResponseDTO> responseEntity = libertyAssuredService.createBorrower(createBorrowerDTO);
//
//
//            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
//
//                GenericResponseDTO body = responseEntity.getBody();
//
//                if (responseEntity.hasBody()) {
//                    String data = (String) body.getData();
//
//                    try {
//                        JSONObject response = new JSONObject(data);
//                        String borrower_id = response.getString("borrower_id");
//
//                        out.println("Borrower Id ===> " + borrower_id);
            try {
                EligibilityRequestDTO eligibilityRequestDTO = new EligibilityRequestDTO();
                eligibilityRequestDTO.setPhoneNumber(loanDTO.getPhoneNumber());

                EligibilityResponse eligibilityResponse = new EligibilityResponse();

                ResponseEntity<GenericResponseDTO> eligibility = libertyAssuredService.eligibility(eligibilityRequestDTO);
                GenericResponseDTO genericResponseDTO = eligibility.getBody();
                if (genericResponseDTO != null) {

                    if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                        eligibilityResponse = (EligibilityResponse) genericResponseDTO.getData();

                        log.debug("Eligibility ===> {}", eligibilityResponse);

//                            return new GenericResponseDTO(genericResponseDTO.getCode(), genericResponseDTO.getStatus(), genericResponseDTO.getMessage(), eligibilityResponse);
                    } else {
                        return new GenericResponseDTO(genericResponseDTO.getCode(), HttpStatus.BAD_REQUEST, genericResponseDTO.getMessage(), eligibilityResponse);
                    }
                }
                RequestLoanOfferRequestDTO requestLoanOfferRequestDTO = new RequestLoanOfferRequestDTO();
                requestLoanOfferRequestDTO.setRequestedAmount(loanDTO.getAmount());
                requestLoanOfferRequestDTO.setDuration(Double.parseDouble(loanDTO.getDuration()));
                requestLoanOfferRequestDTO.setPhoneNumber(loanDTO.getPhoneNumber());

                ResponseEntity<GenericResponseDTO> loanOffer = libertyAssuredService.requestLoanOffer(requestLoanOfferRequestDTO);
                genericResponseDTO = loanOffer.getBody();

                if (genericResponseDTO != null) {

                    if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {

                        CalculateRepaymentResponseDTO calculateRepaymentResponseDTO = (CalculateRepaymentResponseDTO) genericResponseDTO.getData();
                        ApproveLoanRequestDTO approveLoanRequestDTO = new ApproveLoanRequestDTO();
                        String numberofrepayments;
                        double collectionamount;
                        double totalcollectionamount;

                        if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                            approveLoanRequestDTO.setBorrowerPhone(loanDTO.getPhoneNumber());
                            approveLoanRequestDTO.setDuration(6);
                            approveLoanRequestDTO.setInterestRate(18);
                            approveLoanRequestDTO.setRequestedAmount(3000.0);

                            numberofrepayments = "6";
                            collectionamount = 5700;
                            totalcollectionamount = 1141;

                        } else {
                            approveLoanRequestDTO.setBorrowerPhone(loanDTO.getPhoneNumber());
                            approveLoanRequestDTO.setDuration(calculateRepaymentResponseDTO.getDuration());
                            approveLoanRequestDTO.setInterestRate(calculateRepaymentResponseDTO.getInterestRate());
                            approveLoanRequestDTO.setRequestedAmount(calculateRepaymentResponseDTO.getRequestedAmount());

                            numberofrepayments = Integer.toString(calculateRepaymentResponseDTO.getDuration());
                            collectionamount = calculateRepaymentResponseDTO.getMonthlyRepaymentValue();
                            totalcollectionamount = calculateRepaymentResponseDTO.getTotalRepaymentValue();
                        }

                        String ministry = eligibilityResponse.getMinistry();
                        String company = eligibilityResponse.getCompany();

                        loanDTO.setCustomerBankCode(eligibilityResponse.getCustomerBankCode());
                        loanDTO.setCustomerAccountNo(eligibilityResponse.getCustomerAccountNo());
                        loanDTO.setCustomerId(eligibilityResponse.getCustomerId());
                        loanDTO.setEligibilityAmount(eligibilityResponse.getEligibleAmount());
                        loanDTO.setBorrowerId(eligibilityResponse.getCustomerId());
                        loanDTO.setMinistry(ministry);
                        loanDTO.setStatus(LoanStatus.NEW);
                        loanDTO.setNumberOfRepayments(numberofrepayments);
                        loanDTO.setCollectionAmount(String.valueOf(collectionamount));
                        loanDTO.setTotalCollectionAmount(String.valueOf(totalcollectionamount));
                        loanDTO.setTenure(String.valueOf(calculateRepaymentResponseDTO.getDuration()));
                        loanDTO.setInterestRate(String.valueOf(calculateRepaymentResponseDTO.getInterestRate()));
                        loanDTO.setProfileId(profileDTO.getId());
                        loanDTO.setLoanType(LoanType.CASH_LOAN);
                        loanDTO.setLenderId(loanDTO.getLenderId());
                        LoanDTO save = save(loanDTO);

                        log.debug("Saved loan dto ===> " + save);

                        ResponseEntity<GenericResponseDTO> approval = libertyAssuredService.approveLoan(approveLoanRequestDTO);
                        genericResponseDTO = approval.getBody();

                        if (genericResponseDTO != null) {
                            if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {

                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");
                                ZonedDateTime dateTime = ZonedDateTime.now();
                                String formattedDateTime = dateTime.format(formatter);
                                Optional<LoanDTO> updatedLoanOptional = findOne(save.getId());
                                if (!updatedLoanOptional.isPresent()) {
                                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Could not retrieve " +
                                        "loan", null);
                                }
                                LoanDTO updatedLoanDTO = updatedLoanOptional.get();

                                LoanDisbursementRequestDTO loanDisbursementRequestDTO = new LoanDisbursementRequestDTO();
                                loanDisbursementRequestDTO.setLenderId(Long.toString(loanDTO.getLenderId()));
                                loanDisbursementRequestDTO.setBorrowerPhone(loanDTO.getPhoneNumber());
                                loanDisbursementRequestDTO.setCurrency("NGN");
                                loanDisbursementRequestDTO.setNumberOfRepayments(numberofrepayments);
                                loanDisbursementRequestDTO.setDateOfCollection(formattedDateTime);
                                loanDisbursementRequestDTO.setDateOfDisbursement(formattedDateTime);
                                loanDisbursementRequestDTO.setLoanAmount(updatedLoanDTO.getAmount());
                                loanDisbursementRequestDTO.setAccountNumber(updatedLoanDTO.getCustomerAccountNo()); //todo use
                                // persisted accountNumber
                                loanDisbursementRequestDTO.setCollectionAmount(collectionamount);
                                loanDisbursementRequestDTO.setTotalCollectionAmount(totalcollectionamount);
                                loanDisbursementRequestDTO.setApprovalRequest(Integer.parseInt(updatedLoanDTO.getApprovalId())); //todo use the value of approval id
//                                loanDisbursementRequestDTO.setApprovalRequest(Integer.parseInt(updatedLoanDTO.getApprovalId()));
// todo use the value of approval id to make request

                                DisbursementResponse disbursementResponse;
                                DisbursementResponseData disbursementResponseData;

                                ResponseEntity<GenericResponseDTO> disburse = libertyAssuredService.disburseLoan(loanDisbursementRequestDTO);
                                genericResponseDTO = disburse.getBody();

                                if (genericResponseDTO != null) {
                                    if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {

                                        if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                                            disbursementResponseData = (DisbursementResponseData) genericResponseDTO.getData();
                                            return new GenericResponseDTO(genericResponseDTO.getCode(), genericResponseDTO.getStatus(), genericResponseDTO.getMessage(), disbursementResponseData);

                                        } else {
//                                            disbursementResponse = (DisbursementResponse) genericResponseDTO.getData();
                                            disbursementResponse = new ObjectMapper().readValue((String) genericResponseDTO.getData(), DisbursementResponse.class);
                                            return new GenericResponseDTO(genericResponseDTO.getCode(), genericResponseDTO.getStatus(), genericResponseDTO.getMessage(), disbursementResponse);

                                        }
                                    } else {
                                        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Disbursement " +
                                            "failed", null);
                                    }
                                }
                            }
                        }
                    }
                }

                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Operation failed", null);
            } catch (Exception e) {
                e.printStackTrace();
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred, please try again later", null);
            }
        }
//        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Failed to retrieve borrower Id data");

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Failed to retrieve user profile");

    }

    @Override
    public Optional<Loan> findByStatusAndBorrowerIdAndCustomerId(LoanStatus status, String borrowerId, String customerId) {
        return loanRepository.findByStatusAndBorrowerIdAndCustomerId(LoanStatus.APPROVED, borrowerId, customerId);
    }
}
