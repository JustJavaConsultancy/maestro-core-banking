package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.service.kafka.producer.TransProducer;
import ng.com.justjava.corebanking.service.mapper.WalletAccountMapper;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.Scheme;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.SpecificChannel;
import ng.com.justjava.corebanking.service.InsuranceService;
import ng.com.justjava.corebanking.service.LenderService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.FundDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.InsuranceDTO;
import ng.com.justjava.corebanking.service.dto.LenderDTO;
import ng.com.justjava.corebanking.service.dto.ValidTransactionResponse;
import ng.com.justjava.corebanking.service.dto.WalletAccountDTO;
import ng.com.justjava.corebanking.service.dto.WalletExternalDTO;
import ng.com.justjava.corebanking.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class InsuranceServiceImpl implements InsuranceService {

    private final Logger log = LoggerFactory.getLogger(InsuranceServiceImpl.class);

    private final LenderService lenderService;
    private final WalletAccountService walletAccountService;
    private final Utility utility;
    private final TransProducer transProducer;
    private final ProfileService profileService;
    private final WalletAccountMapper walletAccountMapper;

    public InsuranceServiceImpl(LenderService lenderService, WalletAccountService walletAccountService, Utility utility, TransProducer transProducer, ProfileService profileService, WalletAccountMapper walletAccountMapper) {
        this.lenderService = lenderService;
        this.walletAccountService = walletAccountService;
        this.utility = utility;
        this.transProducer = transProducer;
        this.profileService = profileService;
        this.walletAccountMapper = walletAccountMapper;
    }


    @Override
    public GenericResponseDTO processInsurance(InsuranceDTO insuranceDTO, HttpSession session) {
        WalletAccount customerAccount = walletAccountService.findOneByAccountNumber(insuranceDTO.getAccountNumber());
        if (customerAccount == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Customer wallet Account", null);
        }
        Profile accountOwner = customerAccount.getAccountOwner();
        if (accountOwner == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Account Owner", null);

        }
        Optional<LenderDTO> lenderDTOOptional = lenderService.getLenderById(insuranceDTO.getLenderId());

        if (lenderDTOOptional.isPresent()) {
            LenderDTO lenderDTO = lenderDTOOptional.get();
            String profilePhoneNumber = lenderDTO.getProfilePhoneNumber();
            List<WalletAccount> walletAccounts = walletAccountService.findByAccountOwnerPhoneNumber(profilePhoneNumber);
            WalletAccount walletAccount = null;
            if (walletAccounts.isEmpty()) {
                walletAccount = createNewWalletForLender(profilePhoneNumber, session);
            } else {
                walletAccount = walletAccounts.get(0);
            }
            if (walletAccount == null) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Could not generate wallet for Lender", insuranceDTO.getLenderId());
            }

            insuranceDTO.setInsuranceAccount(walletAccount.getAccountNumber());

            FundDTO fundDTO = utility.buildFundDTO(insuranceDTO.getAccountNumber(), insuranceDTO.getInsuranceAccount(), insuranceDTO.getAmount(),
                accountOwner.getFullName(), lenderDTO.getName(), profilePhoneNumber, SpecificChannel.INSURANCE.getName(), "WalletToWallet");
            fundDTO.setNarration("Insurance Policy");
            fundDTO.setCharges(insuranceDTO.getCharges());

            ValidTransactionResponse validTransaction = utility.isValidTransaction(fundDTO.getChannel(), fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber(), fundDTO.getAmount(), fundDTO.getBonusAmount(), false);
            if (validTransaction.isValid()) {
                transProducer.send(fundDTO);

                return new GenericResponseDTO("00", HttpStatus.OK, "Insurance loan processed successfully", null);

            } else {

                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, validTransaction.getMessage(), null);
            }
        }


        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid lender", null);
    }

    private WalletAccount createNewWalletForLender(String profilePhoneNumber, HttpSession session) {
        Profile profile = profileService.findByPhoneNumber(profilePhoneNumber);
        if (profile != null) {
            User user = profile.getUser();
            if (user != null) {

                WalletExternalDTO walletExternalDTO = new WalletExternalDTO();
                walletExternalDTO.setWalletLimit(0.0);
                walletExternalDTO.setWalletAccountTypeId(1L);
                walletExternalDTO.setPhoneNumber(profilePhoneNumber);
                walletExternalDTO.setAccountName(user.getFirstName());
                walletExternalDTO.setOpeningBalance(0.0);
                Scheme scheme = utility.getSchemeFromSession(session);

                ResponseEntity<GenericResponseDTO> responseEntity = walletAccountService.createWalletForExternal(walletExternalDTO, new GenericResponseDTO(), scheme, profile, true);

                if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                    WalletAccountDTO walletAccountDTO = null;
                    GenericResponseDTO body = responseEntity.getBody();
                    if (body != null) {
                        List<WalletAccountDTO> data = (List<WalletAccountDTO>) body.getData();
                        if (data != null && !data.isEmpty()) {
                            walletAccountDTO = data.get(0);
                        }

                        if (walletAccountDTO == null) {
                            return walletAccountMapper.toEntity(walletAccountDTO);
                        }

                    }

                }

            }

        }
        return null;
    }
}
