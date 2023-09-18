package ng.com.justjava.corebanking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ng.com.justjava.corebanking.service.*;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.kafka.producer.TransProducer;
import ng.com.justjava.corebanking.domain.Loan;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.LoanStatus;
import ng.com.justjava.corebanking.domain.enumeration.SpecificChannel;
import ng.com.justjava.corebanking.service.*;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.util.LibertyAssuredUtils;
import ng.com.justjava.corebanking.util.Utility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.System.out;

@Service
@Transactional
public class LibertyAssuredServiceImpl implements LibertyAssuredService {

    private final TransProducer producer;
    private final WalletAccountService walletAccountService;
    private final ProfileService profileService;
    private final LoanService loanService;
    private final Utility utility;
    private final LibertyAssuredUtils libertyAssuredUtils;
    private final LenderService lenderService;
    private final Environment environment;


    //    charge_percentage
    @Value("${app.liberty-assured.charge_percentage}")
    private double libertyChargePercentage;

    @Value("${app.liberty-assured.charge_flat_rate}")
    private double libertyChargeFlatRate;

    @Value("${app.cashconnect.register-webhook-url}")
    private String registerWebHookURL;
    @Value("${app.liberty-assured.mock-json.eligibility-response}")
    private String eligibilityResponse;
    @Value("${app.liberty-assured.mock-json.approval-response}")
    private String approvalResponse;
    @Value("${app.liberty-assured.mock-json.loan-disbursement-response}")
    private String disbursementResponse;
    @Value("${app.liberty-assured.mock-json.request-offer-response}")
    private String requestLoanOfferResponse;
    @Value("${app.liberty-assured.mock-json.calculate-repayment-response}")
    private String calculateRepaymentResponse;

    public LibertyAssuredServiceImpl(TransProducer producer, WalletAccountService walletAccountService, ProfileService profileService, @Lazy LoanService loanService, Utility utility, LibertyAssuredUtils libertyAssuredUtils, LenderService lenderService, Environment environment) {
        this.producer = producer;
        this.walletAccountService = walletAccountService;
        this.profileService = profileService;
        this.loanService = loanService;
        this.utility = utility;
        this.libertyAssuredUtils = libertyAssuredUtils;
        this.lenderService = lenderService;
        this.environment = environment;
    }

    @Override
    public ResponseEntity<GenericResponseDTO> createBorrower(CreateBorrowerDTO createBorrowerDTO) {
        try {
            return libertyAssuredUtils.callLibertyAssuredAPI("/borrower", createBorrowerDTO, HttpMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getCause()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> editBorrower(CreateBorrowerDTO createBorrowerDTO) {
        try {
            return libertyAssuredUtils.callLibertyAssuredAPI("/borrower", createBorrowerDTO, HttpMethod.PUT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getCause()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> getBorrowerDetails(BorrowerDetailsDTO borrowerDetailsDTO) {
        String borrowerId = borrowerDetailsDTO.getBorrowerId();
        if (StringUtils.isNotEmpty(borrowerId)) {
            try {
                String url = "/borrower/" + borrowerId;
                return libertyAssuredUtils.callLibertyAssuredAPI(url, borrowerDetailsDTO, HttpMethod.GET);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getCause()), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "borrower id cannot be empty", null), HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<GenericResponseDTO> borrowerNotifications(String branchId, String type, String[] records) {

        out.println("branchId  " + branchId);
        out.println("type  " + type);
        out.println("records  " + Arrays.toString(records));

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", "success"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> loanNotifications(String branchId, String type, String[] records) {
        out.println("branchId  " + branchId);
        out.println("type  " + type);
        out.println("records  " + Arrays.toString(records));

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", "success"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponseDTO> getLoanDetails(LoanDetailsDTO loanDetailsDTO) {

        String loanId = loanDetailsDTO.getLoanId();
        if (StringUtils.isNotEmpty(loanId)) {
            try {
                String url = "/loan/" + loanId;
                return libertyAssuredUtils.callLibertyAssuredAPI(url, loanDetailsDTO, HttpMethod.GET);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getCause()), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Loan id cannot be empty", null), HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<GenericResponseDTO> createLoan(CreateLoanDTO createLoanDTO) {
        try {
            return libertyAssuredUtils.callLibertyAssuredAPI("/loan", createLoanDTO, HttpMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getCause()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> editLoan(CreateLoanDTO createLoanDTO) {
        try {
            return libertyAssuredUtils.callLibertyAssuredAPI("/loan", createLoanDTO, HttpMethod.PUT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getCause()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> eligibility(EligibilityRequestDTO eligibilityRequestDTO) {

        eligibilityRequestDTO.setPhoneNumber(utility.returnPhoneNumberFormat(eligibilityRequestDTO.getPhoneNumber()));

        try {
            EligibilityResponseNew eligibilityResponseNew;
            GenericResponseDTO genericResponseDTO;
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                genericResponseDTO = new GenericResponseDTO("00", HttpStatus.OK, "success", eligibilityResponse);
            } else {
                genericResponseDTO = libertyAssuredUtils.callEligibilityAPI("eligible/", eligibilityRequestDTO, HttpMethod.POST);
            }

            if (!HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                GenericResponseDTO genericResponseDTO1 = new GenericResponseDTO("99", HttpStatus.BAD_REQUEST,
                    "Eligibility failed", genericResponseDTO.getData());

                return new ResponseEntity<>(genericResponseDTO1, genericResponseDTO.getStatus());
            }

            eligibilityResponseNew = new ObjectMapper().readValue((String) genericResponseDTO.getData(), EligibilityResponseNew.class);

            EligibilityResponse eligibilityResponse = new EligibilityResponse();
            eligibilityResponse.setCustomerAccountNo(eligibilityResponseNew.getPhoneNumber());
            eligibilityResponse.setCustomerId(eligibilityResponseNew.getPhoneNumber());
            List<EligibilityResponseData> data = eligibilityResponseNew.getData();
            int dataIndex = data.size() - 1;
            eligibilityResponse.setEligibleAmount(Double.valueOf(data.get(dataIndex).getAmount()));
            eligibilityResponse.setInterest(Integer.toString(data.get(dataIndex).getInterest()));
            eligibilityResponse.setDuration(Integer.toString(data.get(dataIndex).getDuration()));

//            String data = new ObjectMapper().writeValueAsString(genericResponseDTO.getData());


//            out.println("Eligibility Data ===> " + data);

//            JSONObject jsonObject = new JSONObject(data);
//            System.out.println("JSONOBJECT ==>" + jsonObject);


//            out.println("Eligibility Response ===> " + eligibilityResponse);
            out.println("Eligibility Response ===> " + eligibilityResponse);


           /* String customerId = eligibilityResponse.getCustomerId();
            String customerAccountNo = eligibilityResponse.getCustomerAccountNo();
            String customerBankCode = eligibilityResponse.getCustomerBankCode();
//            String customerId = jsonObject.getString("customer_id");

            Double eligibleAmount = eligibilityResponse.getEligibleAmount();
//            Double eligibleAmount = jsonObject.getDouble("eligible_amount");*/


            String message = String.valueOf(eligibilityResponseNew.getStatus());
//            String message = jsonObject.getString("message");


            if ("200".equalsIgnoreCase(message)) {
                return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", eligibilityResponse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "fail", null), HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Error getting eligibility", e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> approveLoan(ApproveLoanRequestDTO approveLoanRequestDTO) {
        try {
            GenericResponseDTO genericResponseDTO;
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", new Gson().fromJson(approvalResponse, ApproveLoanResponseDtoData.class)), HttpStatus.OK);
            } else {
                genericResponseDTO = libertyAssuredUtils.callEligibilityAPI("approval/", approveLoanRequestDTO, HttpMethod.POST);
            }

            if (!HttpStatus.OK.equals(genericResponseDTO.getStatus())) {

                GenericResponseDTO genericResponseDTO1 = new GenericResponseDTO("99", "Approval failed", genericResponseDTO);

                return new ResponseEntity<>(genericResponseDTO1, genericResponseDTO.getStatus());
            }

            String borrowerPhone = approveLoanRequestDTO.getBorrowerPhone();
            borrowerPhone = utility.returnPhoneNumberFormat(borrowerPhone);
            List<Loan> loanByCustomerId = loanService.findLoanByCustomerIdAndStatus(borrowerPhone, LoanStatus.NEW);
            if (loanByCustomerId.size() == 0) {
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No loan found for this user", null), HttpStatus.BAD_REQUEST);
            }
            Loan loan = loanByCustomerId.get(0);

            if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {

                ApproveLoanResponseDTO approveLoanResponseDTO = new ObjectMapper().readValue((String) genericResponseDTO.getData(), ApproveLoanResponseDTO.class);

                out.println("ApproveLoan Response ===> " + approveLoanResponseDTO);

                if (approveLoanResponseDTO != null) {

                    ApproveLoanResponseDataDTO data = approveLoanResponseDTO.getData();
                    boolean approval = approveLoanResponseDTO.getApproval();

                    if (approval || approveLoanResponseDTO.getData() != null) {

                        ApproveLoanResponseDataDTORemitaRespDataDTO approveLoanResponseDataDTORemitaRespDataDTO =
                            approveLoanResponseDTO.getData().getRemitaResp().getData().getData();

                        loan.setAmount(data.getLoanAmount());
                        loan.setStatus(LoanStatus.APPROVED);
                        loan.setDuration(Integer.toString(approveLoanRequestDTO.getDuration()));
                        loan.setInterestRate(Integer.toString(approveLoanRequestDTO.getInterestRate()));
                        loan.setMandateReference(data.getMandateReference());

                         loan.setApprovalId(Integer.toString(data.getApprovalId())); //todo set approval id from approval response data
                         loan.setLoanId(approveLoanResponseDataDTORemitaRespDataDTO.getCustomerId()); //todo use customer id from remita_resp key
                      loan.setAccountNumber(approveLoanResponseDataDTORemitaRespDataDTO.getAccountNumber());//todo set the value of accountNumber from remita_resp key
                      loan.setCustomerBankCode(approveLoanResponseDataDTORemitaRespDataDTO.getBankCode());//todo set the value of accountNumber from remita_resp key

                        Loan save = loanService.save(loan);
                        out.println("Updated Loan ===> " + save);
                        return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);

                    } else {

                        loan.setStatus(LoanStatus.UNSUCCESSFUL);
                        Loan save = loanService.save(loan);
                        out.println("Updated cancelled Loan ===> " + save);
                        return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);
                    }
                }
            }
            loan.setStatus(LoanStatus.UNSUCCESSFUL);
            Loan save = loanService.save(loan);
            out.println("Updated cancelled Loan ===> " + save);

            return new ResponseEntity<>(genericResponseDTO, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {

            e.printStackTrace();

            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getCause()), HttpStatus.BAD_REQUEST);

        }

    }

    @Override
    public ResponseEntity<GenericResponseDTO> disburseLoan(LoanDisbursementRequestDTO loanDisbursementRequestDTO) {
        loanDisbursementRequestDTO.setUniqueRef(utility.getUniqueTransRef());
        try {
            GenericResponseDTO genericResponseDTO;
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", new Gson().fromJson(disbursementResponse, DisbursementResponseData.class)), HttpStatus.OK);
            } else {
                genericResponseDTO = libertyAssuredUtils.callEligibilityAPI("disbursement/", loanDisbursementRequestDTO, HttpMethod.POST);
            }

            if (!HttpStatus.OK.equals(genericResponseDTO.getStatus())) {

                GenericResponseDTO genericResponseDTO1 = new GenericResponseDTO("99", "Disbursement failed", genericResponseDTO);

                return new ResponseEntity<>(genericResponseDTO1, genericResponseDTO.getStatus());
            }

            String msg = "Failed";

            if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                DisbursementResponse disbursementResponse;
                try {
                    disbursementResponse = new ObjectMapper().readValue((String) genericResponseDTO.getData(), DisbursementResponse.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getMessage()), HttpStatus.BAD_REQUEST);
                }
                out.println("Disburse loan Response ===> " + disbursementResponse);

                if (disbursementResponse != null && disbursementResponse.getApprovalVerified()) {

                    String customerId = loanDisbursementRequestDTO.getBorrowerPhone();
                    List<Loan> loanList = loanService.findLoanByCustomerIdAndStatus(customerId, LoanStatus.APPROVED);

                    if (loanList.isEmpty()) {
                        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, msg, null), HttpStatus.BAD_REQUEST);
                    }

                    //disburse Loan
                    List<WalletAccountDTO> userWalletAccounts = walletAccountService.findByUserIsCurrentUser();
                    msg = "Could not retrieve destination account";

                    if (!userWalletAccounts.isEmpty()) {
                        WalletAccountDTO destinationWalletAccount = userWalletAccounts.get(0);
                        msg = "Could not retrieve Lender. Invalid lender Id";

                        Long aLong = 0L;

                        try {
                            aLong = Long.valueOf(loanDisbursementRequestDTO.getLenderId());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, msg, msg), HttpStatus.BAD_REQUEST);

                        }
                        Optional<LenderDTO> one = lenderService.findOne(aLong);

                        if (one.isPresent()) {

                            LenderDTO lenderDTO = one.get();

                            List<WalletAccount> sourceWallets = walletAccountService.findByAccountOwnerPhoneNumber(lenderDTO.getProfilePhoneNumber());
                            msg = "Could not retrieve source account";

                            if (!sourceWallets.isEmpty()) {

                                WalletAccount sourceWalletAccount = sourceWallets.get(0);

                                String loanAmount = String.valueOf(loanDisbursementRequestDTO.getLoanAmount());

                                String phoneNumber = loanDisbursementRequestDTO.getBorrowerPhone();

                                Double amount = Double.valueOf(loanAmount);
                                FundDTO fundDTO = utility.buildFundDTO(sourceWalletAccount.getAccountNumber(), destinationWalletAccount.getAccountNumber(), amount, sourceWalletAccount.getAccountFullName(), destinationWalletAccount.getAccountFullName(), phoneNumber, SpecificChannel.LIBERTY.getName(), "LoanDisbursement");

                                double commissionPercentage = libertyChargePercentage * amount;
//                                Double charges = commissionPercentage + libertyChargeFlatRate;
//                                Double vat = (commissionPercentage * vatFeePercentage) + (libertyChargeFlatRate * vatFeePercentage);

//                                Double charges = commissionPercentage + libertyChargeFlatRate;
//
//                                fundDTO.setCharges(charges);
                                fundDTO.setNarration("Loan of " + utility.formatMoney(amount) + sourceWalletAccount.getAccountName());

                                ValidTransactionResponse validTransaction = utility.isValidTransaction("walletToWallet", sourceWalletAccount.getAccountNumber(), destinationWalletAccount.getAccountNumber(), fundDTO.getAmount(), fundDTO.getBonusAmount(), false);

                                msg = "Invalid Transaction";

                                if (validTransaction.isValid()) {

                                    producer.send(fundDTO);

                                    Optional<ProfileDTO> currentUser = profileService.findByUserIsCurrentUser();
                                    if (currentUser.isPresent()) {

                                        ProfileDTO profileDTO = currentUser.get();
                                        User user = profileDTO.getUser();
                                        if (user != null) {
                                            String email = user.getEmail();
                                            LocalDate repaymentDate = LocalDate.now().plusMonths(1);
                                            String walletName, walletNumber;
                                            walletName = destinationWalletAccount.getAccountName();
                                            walletNumber = destinationWalletAccount.getAccountNumber();
                                            if (utility.checkStringIsValid(email)) {
                                                String emailMsg = "Dear " + user.getFirstName() +
                                                    "<br/>" +
                                                    "<br/>" +
                                                    "<br/>" +
                                                    "The loan of ₦" + utility.formatMoney(amount) + " has been paid into your account (" + walletName + " - " + walletNumber + ")" +
                                                    "<br/>" +
                                                    "<br/>" +
                                                    "The due date of your loan is " + repaymentDate +
                                                    "<br/>" +
                                                    "<br/>" +
                                                    "<br/>" +
                                                    "Thank you for choosing us!";

                                                utility.sendEmail(email, "Loan Disbursement Notification", emailMsg);

                                            }
                                        }

                                    }
                                } else {

                                    return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, validTransaction.getMessage(), null), HttpStatus.BAD_REQUEST);

                                }
                            }

                        }

                    }

                    Loan loan = loanList.get(0);


                    //create Loan
                    String tenure = loan.getTenure();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    Double principalAmount = loan.getAmount();


//                    CreateLoanDTO createLoanDTO = new CreateLoanDTO();
//                    createLoanDTO.setBorrowerId(loan.getBorrowerId());
////                    createLoanDTO.setLoanProductId("102838");
//                    createLoanDTO.setLoanProductId("110114");
//                    createLoanDTO.setLoanApplicationId(utility.getUniqueRequestId());
//                    createLoanDTO.setLoanDisbursedById("84628");
//                    createLoanDTO.setLoanPrincipalAmount(principalAmount);
//                    createLoanDTO.setLoanReleasedDate(LocalDate.now().format(formatter));
//                    createLoanDTO.setLoanInterestMethod("flat_rate");
//                    createLoanDTO.setLoanInterestType("percentage");
//                    createLoanDTO.setLoanInterestPeriod("Month");
//                    createLoanDTO.setLoanInterest(27.0);
//                    createLoanDTO.setLoanDurationPeriod("Days");
//                    createLoanDTO.setLoanDuration(30);
////                    createLoanDTO.setLoanPaymentSchemeId("1124");
//                    createLoanDTO.setLoanPaymentSchemeId("3");
//                    createLoanDTO.setLoanNumOfRepayments(1);//Todo Needs confirmation
//                    createLoanDTO.setLoanDecimalPlaces("round_up_to_five");
//                    createLoanDTO.setCustomField4287(String.valueOf(principalAmount));
//                    createLoanDTO.setCustomField5251((String) disbursementResponse.getMandateReference());
//                    createLoanDTO.setCustomField6363(loan.getMinistry());
//
//                    ResponseEntity<GenericResponseDTO> loanResponse = createLoan(createLoanDTO);
//
//                    msg = "Error creating loan";
//
//                    if (HttpStatus.OK.equals(loanResponse.getStatusCode())) {
//
//                        if (loanResponse.hasBody()) {
//                            GenericResponseDTO responseBody = loanResponse.getBody();
//                            String responseData = (String) responseBody.getData();
//
//                            JSONObject jsonObject = new JSONObject(responseData);
//                    String loanId = jsonObject.getString("loan_id");

                    out.println("Reference Id ====>" + loanDisbursementRequestDTO.getUniqueRef());
                    loan.setLoanId(loanDisbursementRequestDTO.getUniqueRef());
                    loan.setMandateReference(disbursementResponse.getMandateReference());
                    loan.setStatus(LoanStatus.DISBURSED);
                    loan.setCollectionAmount(Double.toString(loanDisbursementRequestDTO.getCollectionAmount()));
                    loan.setTotalCollectionAmount(Double.toString(loanDisbursementRequestDTO.getTotalCollectionAmount()));
                    loan.setNumberOfRepayments(loanDisbursementRequestDTO.getNumberOfRepayments());
                    Loan save1 = loanService.save(loan);
                    out.println("Save1 ====>" + save1);

                    out.println("Updated loan after disbursement ===> " + save1);
                    return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());

//                    try {
//                        Loan save = loanService.save(loan);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed"), HttpStatus.BAD_REQUEST);
//                    }
//                }
//            }
                }
            }

            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, msg, null), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getCause()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> loanRepayment(LoanRepaymentDTO loanRepaymentDTO) {
        return null;
    }

    @Override
    public ResponseEntity<GenericResponseDTO> requestLoanOffer(RequestLoanOfferRequestDTO requestLoanOfferRequestDTO) {

        try {
            GenericResponseDTO genericResponseDTO;
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", new Gson().fromJson(calculateRepaymentResponse, CalculateRepaymentResponseMockDTO.class)), HttpStatus.OK);
            } else {
                genericResponseDTO = libertyAssuredUtils.callEligibilityAPI("request_offer/", requestLoanOfferRequestDTO, HttpMethod.POST);
            }

            if (!HttpStatus.OK.equals(genericResponseDTO.getStatus())) {

                GenericResponseDTO genericResponseDTO1 = new GenericResponseDTO("99", "Request offer failed", genericResponseDTO);

                return new ResponseEntity<>(genericResponseDTO1, genericResponseDTO.getStatus());
            }

            RequestLoanOfferResponseDTO requestLoanOfferResponseDTO = new ObjectMapper().readValue((String) genericResponseDTO.getData(), RequestLoanOfferResponseDTO.class);

            if (requestLoanOfferResponseDTO.getOfferOkay()) {

                CalculateRepaymentRequestDTO calculateRepaymentRequestDTO = new CalculateRepaymentRequestDTO();
                calculateRepaymentRequestDTO.setRequestedAmount(requestLoanOfferResponseDTO.getAmount());
                calculateRepaymentRequestDTO.setInterestRate(requestLoanOfferResponseDTO.getInterest());
                calculateRepaymentRequestDTO.setDuration(requestLoanOfferResponseDTO.getDuration());
                ResponseEntity<GenericResponseDTO> response = loanCalculation(calculateRepaymentRequestDTO);
                genericResponseDTO = response.getBody();

                return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Offer not accepted", null), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getCause()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<GenericResponseDTO> loanCalculation(CalculateRepaymentRequestDTO calculateRepaymentRequestDTO) {

        try {
            GenericResponseDTO genericResponseDTO;
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", new Gson().fromJson(calculateRepaymentResponse, CalculateRepaymentResponseMockDTO.class)), HttpStatus.OK);
            } else {
                genericResponseDTO = libertyAssuredUtils.callEligibilityAPI("calculate_repayment/", calculateRepaymentRequestDTO, HttpMethod.POST);
            }

            if (!HttpStatus.OK.equals(genericResponseDTO.getStatus())) {

                GenericResponseDTO genericResponseDTO1 = new GenericResponseDTO("99", "Repayment Calculation failed", genericResponseDTO);

                return new ResponseEntity<>(genericResponseDTO1, genericResponseDTO.getStatus());
            }

            CalculateRepaymentResponseDTO calculateRepaymentResponseDTO;
            calculateRepaymentResponseDTO = new ObjectMapper().readValue((String) genericResponseDTO.getData(), CalculateRepaymentResponseDTO.class);

            return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", calculateRepaymentResponseDTO), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getCause()), HttpStatus.BAD_REQUEST);
        }
    }
}
