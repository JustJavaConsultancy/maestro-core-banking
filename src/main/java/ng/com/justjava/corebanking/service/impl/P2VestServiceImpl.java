package ng.com.justjava.corebanking.service.impl;

import com.google.gson.Gson;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.enumeration.LoanStatus;
import ng.com.justjava.corebanking.domain.enumeration.LoanType;
import ng.com.justjava.corebanking.domain.enumeration.Tenure;
import ng.com.justjava.corebanking.service.LoanService;
import ng.com.justjava.corebanking.service.P2VestService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.util.P2VestUtil;
import ng.com.justjava.corebanking.util.Utility;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Transactional
public class P2VestServiceImpl implements P2VestService {

    private final P2VestUtil p2VestUtil;
    private final ProfileService profileService;
    private final Utility utility;
    private final LoanService loanService;

    public P2VestServiceImpl(P2VestUtil p2VestUtil, ProfileService profileService, Utility utility, LoanService loanService) {

        this.p2VestUtil = p2VestUtil;
        this.profileService = profileService;
        this.utility = utility;
        this.loanService = loanService;
    }

    @Override
    public GenericResponseDTO validateOffer(P2VestValidateOfferRequestDTO p2VestValidateOfferRequestDTO) {

        if (p2VestValidateOfferRequestDTO == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Payload not found", null);
        }

        try{

            GenericResponseDTO response = p2VestUtil.requestWithPayload("/v1/p2p/loan/validate-offer-request", p2VestValidateOfferRequestDTO, HttpMethod.POST);

            if (response == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response", null);
            }

            String genericResponseDTOData = (String) response.getData();

            P2VestValidateOfferResponseDTO p2VestValidateOfferResponseDTOS = new Gson().fromJson(genericResponseDTOData, P2VestValidateOfferResponseDTO.class);

            return new GenericResponseDTO("00", HttpStatus.OK, "Successful", p2VestValidateOfferResponseDTOS);
        }
        catch (Exception e){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
        }

    }

    @Override
    public GenericResponseDTO offer(P2VestOfferRequestDTO p2VestOfferRequestDTO) {

        if (p2VestOfferRequestDTO == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Payload not found", null);
        }

        try{

            GenericResponseDTO response = p2VestUtil.requestWithPayload("/v1/p2p/loan/offer", p2VestOfferRequestDTO, HttpMethod.POST);

            if (response == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response", null);
            }

            String genericResponseDTOData = (String) response.getData();

            P2VestOfferResponseDTO p2VestOfferResponseDTOS = new Gson().fromJson(genericResponseDTOData, P2VestOfferResponseDTO.class);

            return new GenericResponseDTO("00", HttpStatus.OK, "Successful", p2VestOfferResponseDTOS);
        }
        catch (Exception e){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
        }

    }

    @Override
    public GenericResponseDTO createBorrower(P2VestCreateBorrowerRequestDTO p2VestCreateBorrowerRequestDTO) {

        if (p2VestCreateBorrowerRequestDTO == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Payload not found", null);
        }

        try{

            GenericResponseDTO response = p2VestUtil.requestWithPayload("/v1/p2p/loan/borrower", p2VestCreateBorrowerRequestDTO, HttpMethod.POST);

            if (response == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response", null);
            }

            String genericResponseDTOData = (String) response.getData();

            P2VestCreateBorrowerResponseDTO p2VestCreateBorrowerResponseDTO = new Gson().fromJson(genericResponseDTOData, P2VestCreateBorrowerResponseDTO.class);

            return new GenericResponseDTO("00", HttpStatus.OK, "Successful", p2VestCreateBorrowerResponseDTO);
        }
        catch (Exception e){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
        }

    }

    @Override
    public GenericResponseDTO createLoan(P2VestCreateLoanDTO p2VestCreateLoanDTO) {

        if (p2VestCreateLoanDTO == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Payload not found", null);
        }


        try{

            P2VestValidateOfferRequestDTO p2VestValidateOfferRequestDTO = new P2VestValidateOfferRequestDTO();
            p2VestValidateOfferRequestDTO.setPhone_number(p2VestCreateLoanDTO.getPhoneNumber());
            p2VestValidateOfferRequestDTO.setNet_pay(Integer.parseInt(p2VestCreateLoanDTO.getNetPay()));

            GenericResponseDTO validateOfferResponse = validateOffer(p2VestValidateOfferRequestDTO);

            if (validateOfferResponse.getStatus() != HttpStatus.OK){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Create loan failed - Could not validate offer", null);
            }

            P2VestValidateOfferResponseDTO p2VestValidateOfferResponseDTO = (P2VestValidateOfferResponseDTO) validateOfferResponse.getData();
            P2VestValidateOfferResponseDataDTO p2VestValidateOfferResponseDataDTO = p2VestValidateOfferResponseDTO.getData();

            P2VestOfferRequestDTO p2VestOfferRequestDTO = new P2VestOfferRequestDTO();
            p2VestOfferRequestDTO.setPhone_number(p2VestCreateLoanDTO.getPhoneNumber());
            p2VestOfferRequestDTO.setLoan_amount(Integer.parseInt(p2VestCreateLoanDTO.getAmount()));
            p2VestOfferRequestDTO.setTenor(Integer.parseInt(p2VestCreateLoanDTO.getDuration()));

            GenericResponseDTO offerResponse = offer(p2VestOfferRequestDTO);

            if (offerResponse.getStatus() != HttpStatus.OK){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Create loan failed - Could not find offer", null);
            }

            P2VestOfferResponseDTO p2VestOfferResponseDTO = (P2VestOfferResponseDTO) offerResponse.getData();
            P2VestOfferResponseDataDTO p2VestOfferResponseDataDTO = p2VestOfferResponseDTO.getData();

            String phone = utility.formatPhoneNumber(p2VestCreateLoanDTO.getPhoneNumber());

            Profile profile = profileService.findByPhoneNumber(phone);

            if (profile == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User Profile does not exist", null);
            }

            String bvn = profile.getBvn();
            LocalDate dob = profile.getDateOfBirth();
            String dateOfBirth = dob.format(DateTimeFormatter.ofPattern("yyyy-MMM-dd"));

            if (bvn == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "BVN not found for this user", null);
            }

            P2VestCreateBorrowerRequestDTO p2VestCreateBorrowerRequestDTO = new P2VestCreateBorrowerRequestDTO();
            p2VestCreateBorrowerRequestDTO.setPhone_number(p2VestCreateLoanDTO.getPhoneNumber());
            p2VestCreateBorrowerRequestDTO.setBvn(bvn);
            p2VestCreateBorrowerRequestDTO.setDob(dateOfBirth);

            GenericResponseDTO createBorrowerResponse = createBorrower(p2VestCreateBorrowerRequestDTO);

            if (offerResponse.getStatus() != HttpStatus.OK){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Create loan failed - Could not create borrower", null);
            }

            P2VestCreateBorrowerResponseDTO p2VestCreateBorrowerResponseDTO = (P2VestCreateBorrowerResponseDTO) createBorrowerResponse.getData();

            if (!p2VestCreateBorrowerResponseDTO.getData()){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Create loan failed - Borrower creation failed", null);
            }

            String loanid = utility.getUniqueLoanId();

            LoanDTO loanDTO = new LoanDTO();
            loanDTO.setAmount(Double.parseDouble(p2VestCreateLoanDTO.getAmount()));
            loanDTO.setTenure(Tenure.MONTHLY.toString());
            loanDTO.setEligibilityAmount(p2VestValidateOfferResponseDataDTO.getAmount_eligible());
            loanDTO.setStatus(LoanStatus.NEW);
            loanDTO.setCustomerId(p2VestCreateLoanDTO.getPhoneNumber());
            loanDTO.setLoanType(LoanType.CASH_LOAN);
            loanDTO.setDuration(p2VestCreateLoanDTO.getDuration());
            loanDTO.setInterestRate(Double.toString(p2VestOfferResponseDataDTO.getInterest_rate()));
            loanDTO.setCollectionAmount(Double.toString(p2VestOfferResponseDataDTO.getRepayment_amount()));
            loanDTO.setTotalCollectionAmount(Double.toString(p2VestOfferResponseDataDTO.getRepayment_amount()));
            loanDTO.setNumberOfRepayments(p2VestCreateLoanDTO.getDuration());
            loanDTO.setLenderId(Long.parseLong(p2VestCreateLoanDTO.getLenderId()));
            loanDTO.setLoanId(loanid);

            LoanDTO save = loanService.save(loanDTO);

            System.out.println("Saved Loan ==> " + save);

            P2VestCreateLoanRequestDTO p2VestCreateLoanRequestDTO = new P2VestCreateLoanRequestDTO();
            p2VestCreateLoanRequestDTO.setPhone_number(p2VestCreateLoanDTO.getPhoneNumber());
            p2VestCreateLoanRequestDTO.setBvn(bvn);
            p2VestCreateLoanRequestDTO.setDob(dateOfBirth);

            GenericResponseDTO response = p2VestUtil.requestWithPayload("/v1/p2p/loan", p2VestCreateLoanRequestDTO, HttpMethod.POST);

            if (response == null){
                Optional<LoanDTO> updatedLoanOptional = loanService.findOne(save.getId());
                LoanDTO saveNew = updatedLoanOptional.get();
                saveNew.setStatus(LoanStatus.UNSUCCESSFUL);
                loanService.save(saveNew);
                System.out.println("Unsuccessful Saved Loan ==> " + saveNew);
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response", null);
            }

            String genericResponseDTOData = (String) response.getData();

            P2VestCreateLoanResponseDTO p2VestCreateLoanResponseDTO = new Gson().fromJson(genericResponseDTOData, P2VestCreateLoanResponseDTO.class);

            if (!p2VestCreateLoanResponseDTO.getData()){
                Optional<LoanDTO> updatedLoanOptional = loanService.findOne(save.getId());
                LoanDTO saveNew = updatedLoanOptional.get();
                saveNew.setStatus(LoanStatus.UNSUCCESSFUL);
                loanService.save(saveNew);
                System.out.println("Unsuccessful Saved Loan ==> " + saveNew);
            }

            Optional<LoanDTO> updatedLoanOptional = loanService.findOne(save.getId());
            LoanDTO saveNew = updatedLoanOptional.get();
            saveNew.setStatus(LoanStatus.APPROVED);
            loanService.save(saveNew);
            System.out.println("Approved Saved Loan ==> " + saveNew);

            return new GenericResponseDTO("00", HttpStatus.OK, "Successful", p2VestCreateLoanResponseDTO);
        }
        catch (Exception e){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
        }

    }
}
