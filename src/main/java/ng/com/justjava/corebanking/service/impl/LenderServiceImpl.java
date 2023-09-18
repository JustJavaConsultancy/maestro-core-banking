package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.LenderRepository;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.mapper.LenderMapper;
import ng.com.justjava.corebanking.client.ExternalEmailRESTClient;
import ng.com.justjava.corebanking.domain.Lender;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.enumeration.AddressType;
import ng.com.justjava.corebanking.service.LenderService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.util.Utility;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Lender}.
 */
@Service
@Transactional
public class LenderServiceImpl implements LenderService {

    private final Logger log = LoggerFactory.getLogger(LenderServiceImpl.class);

    private final LenderRepository lenderRepository;
    private final WalletAccountService walletAccountService;
    private final ExternalEmailRESTClient externalEmailRESTClient;
    private final Utility utility;
    private final ProfileService profileService;

    private final LenderMapper lenderMapper;

    public LenderServiceImpl(LenderRepository lenderRepository, WalletAccountService walletAccountService, ExternalEmailRESTClient externalEmailRESTClient, Utility utility, ProfileService profileService, LenderMapper lenderMapper) {
        this.lenderRepository = lenderRepository;
        this.walletAccountService = walletAccountService;
        this.externalEmailRESTClient = externalEmailRESTClient;
        this.utility = utility;
        this.profileService = profileService;
        this.lenderMapper = lenderMapper;
    }

    @Override
    public LenderDTO save(LenderDTO lenderDTO) {
        log.debug("Request to save Lender : {}", lenderDTO);
        Lender lender = lenderMapper.toEntity(lenderDTO);
        lender = lenderRepository.save(lender);
        return lenderMapper.toDto(lender);
    }

    @Override
    public LenderDTO save(LenderDTO lenderDTO, Profile profile) {
        Lender lender = lenderMapper.toEntity(lenderDTO);
        lender.setProfile(profile);

        Lender save = lenderRepository.save(lender);
        log.debug("Saved Lender ==> " + save);
        return lenderMapper.toDto(save);
    }

    @Override
    public Lender save(Lender lender) {
        return lenderRepository.save(lender);
    }


    @Override
    @Transactional(readOnly = true)
    public List<LenderDTO> findAll() {
        log.debug("Request to get all Lenders");
        return lenderRepository.findAll().stream()
            .map(lenderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<LenderDTO> findOne(Long id) {
        log.debug("Request to get Lender : {}", id);
        return lenderRepository.findById(id)
            .map(lenderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lender : {}", id);
        lenderRepository.deleteById(id);
    }

    public Optional<Lender> findByProfilePhoneNumber(String profile_phoneNumber) {
        return lenderRepository.findByProfilePhoneNumber(profile_phoneNumber);
    }

    @Override
    public GenericResponseDTO createLender(@Valid CreateLenderDTO lenderDTO, HttpSession session) {

        if (!StringUtils.isEmpty(lenderDTO.getNin())) {
            NINRequestDTO ninRequestDTO = new NINRequestDTO();
            NINExamplePayload ninExamplePayload = new NINExamplePayload();
            ninExamplePayload.setValue(lenderDTO.getNin());
            ArrayList<NINExamplePayload> customFields = new ArrayList<>();
            customFields.add(ninExamplePayload);
            ninRequestDTO.setCustomFields(customFields);

            try {
                NINDataResponseDTO ninDataResponseDTO = profileService.getNINDetails(ninRequestDTO);
                if (ninDataResponseDTO != null) {
                    NINResponseDTO ninResponseDTO = ninDataResponseDTO.getData();

                    if (ninResponseDTO != null) {
                        String phoneNumber = ninResponseDTO.getMobileNumber();
                        lenderDTO.setPhoneNumber(utility.formatPhoneNumber(phoneNumber));

                    } else {
                        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null);
                    }
                } else {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Nin validation failed", null);
                }
            } catch (Exception e) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), null);
            }
        } else {
            lenderDTO.setPhoneNumber(utility.formatPhoneNumber(lenderDTO.getPhoneNumber()));
        }


        Optional<Lender> allByProfilePhoneNumber = findByProfilePhoneNumber(lenderDTO.getPhoneNumber());
        if (allByProfilePhoneNumber.isPresent()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Lender already exists!", null);
        }

        Profile profileServiceByPhoneNumber = profileService.findByPhoneNumber(lenderDTO.getPhoneNumber());
        if (profileServiceByPhoneNumber != null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User profile already exists!", null);
        }

        if (StringUtils.isEmpty(lenderDTO.getPin())) {
            lenderDTO.setPin("1234");
        }
        if (StringUtils.isEmpty(lenderDTO.getAccountName())) {
            lenderDTO.setAccountName(lenderDTO.getFirstName());
        }

        if (StringUtils.isEmpty(String.valueOf(lenderDTO.getOpeningBalance()))) {
            lenderDTO.setOpeningBalance(0.0);
        }

        WalletExternalDTO walletExternalDTO = convertWalletAccountDTO(lenderDTO);
        ResponseEntity<GenericResponseDTO> walletExternalResult = walletAccountService.createWalletExternal(walletExternalDTO, session);
        if (HttpStatus.OK.equals(walletExternalResult.getStatusCode())) {
            GenericResponseDTO body = walletExternalResult.getBody();
            if (body != null) {
                List<WalletAccountDTO> walletAccountDTOList = null;
                try {
                    if (body.getData() != null) {
                        walletAccountDTOList = (List<WalletAccountDTO>) body.getData();
                    } else {
                        return body;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return body;
                }

                String jwt = (String) session.getAttribute("jwt");

                if (walletAccountDTOList != null && walletAccountDTOList.get(0) != null) {
                    WalletAccountDTO walletAccountDTO = walletAccountDTOList.get(0);
                    String agentPhoneNumber = lenderDTO.getPhoneNumber();

                    Profile profile = profileService.findByPhoneNumber(agentPhoneNumber);

                    Lender lender = new Lender();
                    lender.setPreferredTenure(lenderDTO.getPreferredTenure());
                    lender.setRate(lenderDTO.getRate());
                    lender.setProfile(profile);
//                    lender.setLenderType(lenderDTO.getLenderType());


                    log.info("LENDER ABOUT TO BE SAVED ===> " + lender);

                    Lender save = save(lender);

                    log.info("SAVED LENDER ===> " + save);
                    User user = profile.getUser();

                    CreateLenderResponseDTO createLenderResponseDTO = new CreateLenderResponseDTO();
                    createLenderResponseDTO.setAccountName(walletAccountDTO.getAccountName());
                    createLenderResponseDTO.setAccountNumber(walletAccountDTO.getAccountNumber());
                    createLenderResponseDTO.setPhoneNumber(profile.getPhoneNumber());
                    createLenderResponseDTO.setRate(save.getRate());
                    createLenderResponseDTO.setLenderId(save.getId());
                    createLenderResponseDTO.setUser(user);
                    createLenderResponseDTO.setPreferredTenure(save.getPreferredTenure().toString());
                    createLenderResponseDTO.setFullName(profile.getFullName());
                    createLenderResponseDTO.setToken(jwt);

                    return new GenericResponseDTO("00", HttpStatus.CREATED, "success", createLenderResponseDTO);
                }
            }
        }

        return walletExternalResult.getBody();
    }

    @Override
    public LenderDTO updateLender(LenderDTO lenderDTO) {
        lenderDTO.setProfilePhoneNumber(utility.formatPhoneNumber(lenderDTO.getProfilePhoneNumber()));
        String profilePhoneNumber = lenderDTO.getProfilePhoneNumber();

        Profile profile = profileService.findByPhoneNumber(profilePhoneNumber);

        LenderDTO save = save(lenderDTO, profile);

        log.debug("Updated lender " + save);
        return save;


    }

    @Override
    public Optional<LenderDTO> getLenderByPhoneNumber(String phoneNumber) {
        return findByProfilePhoneNumber(utility.formatPhoneNumber(phoneNumber)).map(lenderMapper::toDto);
    }

    @Override
    public Optional<LenderDTO> getLenderById(Long lenderId) {

        return lenderRepository.findById(lenderId).map(lenderMapper::toDto);
    }

    public WalletExternalDTO convertWalletAccountDTO(CreateLenderDTO createAgentDTO) {
        WalletExternalDTO walletExternalDTO = new WalletExternalDTO();

        walletExternalDTO.setPin(createAgentDTO.getPin());
        walletExternalDTO.setPhoneNumber(createAgentDTO.getPhoneNumber());
        walletExternalDTO.setPassword(createAgentDTO.getPassword());
        walletExternalDTO.setFirstName(createAgentDTO.getFirstName());
        walletExternalDTO.setGender(createAgentDTO.getGender());
        walletExternalDTO.setDateOfBirth(createAgentDTO.getDateOfBirth());
        walletExternalDTO.setAccountName(createAgentDTO.getAccountName());
        walletExternalDTO.setLastName(createAgentDTO.getLastName());
        walletExternalDTO.setOpeningBalance(createAgentDTO.getOpeningBalance());
        walletExternalDTO.setDeviceNotificationToken(createAgentDTO.getDeviceNotificationToken());
        walletExternalDTO.setAddress(createAgentDTO.getAddress());
        walletExternalDTO.setEmail(createAgentDTO.getEmail());
        walletExternalDTO.setLatitude(createAgentDTO.getLatitude());
        walletExternalDTO.setLongitude(createAgentDTO.getLongitude());
        walletExternalDTO.setLocalGovt(createAgentDTO.getLocalGovt());
        walletExternalDTO.setPhoto(createAgentDTO.getPhoto());
        walletExternalDTO.setScheme(createAgentDTO.getScheme());
        walletExternalDTO.setState(createAgentDTO.getState());
        walletExternalDTO.setNin(createAgentDTO.getNin());
        walletExternalDTO.setCity(createAgentDTO.getCity());
        walletExternalDTO.setAddressType(AddressType.BUSINESS.toString());
        walletExternalDTO.setWalletAccountTypeId(1L);

        return walletExternalDTO;
    }


}
