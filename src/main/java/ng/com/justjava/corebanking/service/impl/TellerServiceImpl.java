package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.TellerRepository;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.mapper.TellerMapper;
import ng.com.justjava.corebanking.domain.Agent;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.Teller;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.service.AgentService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.TellerService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.util.Utility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class TellerServiceImpl implements TellerService {

    private final TellerRepository tellerRepository;
    private final TellerMapper tellerMapper;
    private final ProfileService profileService;
    private final Utility utility;
    private final AgentService agentService;
    private final WalletAccountService walletAccountService;

    public TellerServiceImpl(TellerRepository tellerRepository, TellerMapper tellerMapper, ProfileService profileService, Utility utility, AgentService agentService, WalletAccountService walletAccountService) {
        this.tellerRepository = tellerRepository;
        this.tellerMapper = tellerMapper;
        this.profileService = profileService;
        this.utility = utility;
        this.agentService = agentService;
        this.walletAccountService = walletAccountService;
    }

    @Override
    public TellerDTO save(TellerDTO tellerDTO) {

        String phoneNumber = tellerDTO.getPhoneNumber();
        phoneNumber= utility.formatPhoneNumber(phoneNumber);

        Teller teller = tellerMapper.toEntity(tellerDTO);
        teller.setProfile(profileService.findByPhoneNumber(phoneNumber));

        if (tellerDTO.getAgentPhoneNumber() != null){
            String agentPhoneNumber = utility.formatPhoneNumber(tellerDTO.getAgentPhoneNumber());
            Optional<Agent> agentOptional = agentService.findAllByProfilePhoneNumber(agentPhoneNumber);
            agentOptional.ifPresent(teller::setAgent);
            if (agentOptional.isPresent()){
                teller.setAgent(agentOptional.get());
            }else {
                return null;
            }
        }

        teller =  tellerRepository.save(teller);
        return tellerMapper.toDto(teller);
    }

    @Override
    public List<TellerDTO> findAll() {
        return tellerRepository.findAll().stream().map(tellerMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<TellerDTO> findOne(Long id) {
        return tellerRepository.findById(id).map(tellerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        tellerRepository.deleteById(id);
    }

    @Override
    public List<TellerDTO> findAllByAgentProfilePhoneNumber(String phoneNumber) {
        return tellerRepository.findAllByAgentProfilePhoneNumber(phoneNumber).stream().map(tellerMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TellerDTO> findByLocationLike(String location) {
        return tellerRepository.findByLocationLike(location).stream().map(tellerMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<TellerDTO> findByProfilePhoneNumber(String phoneNumber) {
        return tellerRepository.findByProfilePhoneNumber(phoneNumber).map(tellerMapper::toDto);
    }

    @Override
    public GenericResponseDTO createTeller(CreateAgentDTO createAgentDTO, HttpSession session) {

        String superAgentPhoneNumber = utility.formatPhoneNumber(createAgentDTO.getSuperAgentPhoneNumber());

        String s = utility.formatPhoneNumber(createAgentDTO.getPhoneNumber());
        Optional<TellerDTO> byProfilePhoneNumber = findByProfilePhoneNumber(s);
        if (byProfilePhoneNumber.isPresent()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "PhoneNumber already exists!", null);
        }

        createAgentDTO.setPhoneNumber(utility.formatPhoneNumber(createAgentDTO.getPhoneNumber()));

        if (!StringUtils.isEmpty(createAgentDTO.getNin())) {
            NINRequestDTO ninRequestDTO = new NINRequestDTO();
            NINExamplePayload ninExamplePayload = new NINExamplePayload();
            ninExamplePayload.setValue(createAgentDTO.getNin());
            ArrayList<NINExamplePayload> customFields = new ArrayList<>();
            customFields.add(ninExamplePayload);
            ninRequestDTO.setCustomFields(customFields);

            try {
                NINDataResponseDTO ninDataResponseDTO = profileService.getNINDetails(ninRequestDTO);
                if (ninDataResponseDTO != null) {
                    NINResponseDTO ninResponseDTO = ninDataResponseDTO.getData();

                    if (ninResponseDTO != null) {
                        String phoneNumber = ninResponseDTO.getMobileNumber();
                        createAgentDTO.setPhoneNumber(utility.formatPhoneNumber(phoneNumber));

                    } else {
                        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "fail", null);
                    }
                } else {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "validation failed", null);
                }
            } catch (Exception e) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), null);
            }
        }

        if (StringUtils.isEmpty(createAgentDTO.getPin())) {
            createAgentDTO.setPin("1234");
        }
        if (StringUtils.isEmpty(createAgentDTO.getAccountName())) {
            createAgentDTO.setAccountName(createAgentDTO.getFirstName());
        }

        if (StringUtils.isEmpty(String.valueOf(createAgentDTO.getOpeningBalance()))) {
            createAgentDTO.setOpeningBalance(0.0);
        }

        Agent superAgent = null;

        if (!StringUtils.isEmpty(superAgentPhoneNumber)) {
            Optional<Agent> superAgentOptional = agentService.findAllByProfilePhoneNumber(superAgentPhoneNumber);
            if (!superAgentOptional.isPresent()) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid super agent phoneNumber", null);
            }
            superAgent = superAgentOptional.get();
        }


        WalletExternalDTO walletExternalDTO = agentService.convertWalletAccountDTO(createAgentDTO, 5L, 2);
        ResponseEntity<GenericResponseDTO> walletExternalResult = walletAccountService.createWalletExternal(walletExternalDTO, session);
        if (HttpStatus.OK.equals(walletExternalResult.getStatusCode())) {
            GenericResponseDTO body = walletExternalResult.getBody();
            if (body != null) {
                List<WalletAccountDTO> walletAccountDTOList = null;
                try {
                    walletAccountDTOList = (List<WalletAccountDTO>) body.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (walletAccountDTOList != null && walletAccountDTOList.get(0) != null) {
                    WalletAccountDTO walletAccountDTO = walletAccountDTOList.get(0);
                    String accountOwnerPhoneNumber = createAgentDTO.getPhoneNumber();
                    Profile profile = profileService.findByPhoneNumber(accountOwnerPhoneNumber);
                    Teller teller = new Teller();
                    teller.setLocation(createAgentDTO.getLocation());
                    teller.setProfile(profile);
                    teller.setLatitude(createAgentDTO.getLatitude());
                    teller.setLongitude(createAgentDTO.getLongitude());
                    teller.setAgent(superAgent);
                    teller = tellerRepository.save(teller);

                    if (superAgent != null) {
                        GenericResponseDTO genericResponseDTO = agentService.addTellerToAgent(superAgent.getProfile().getPhoneNumber(), teller.getProfile().getPhoneNumber());
                        if (!HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                            return genericResponseDTO;
                        }
                    }

                    CreateAgentResponseDTO createAgentResponseDTO = new CreateAgentResponseDTO();
                    createAgentResponseDTO.setAccountName(walletAccountDTO.getAccountName());
                    createAgentResponseDTO.setAccountNumber(walletAccountDTO.getAccountNumber());
                    createAgentResponseDTO.setPhoneNumber(profile.getPhoneNumber());
                    createAgentResponseDTO.setLocation(teller.getLocation());
                    createAgentResponseDTO.setAgentId(teller.getId());
                    createAgentResponseDTO.setFullName(profile.getFullName());
                    return new GenericResponseDTO("00", HttpStatus.CREATED, "success", createAgentResponseDTO);
                }
            }
        }
        return walletExternalResult.getBody();
    }

    @Override
    public List<TellerDTO> getAllASuperAgentTellers(String phoneNumber) {
        return tellerRepository.findAllByAgentProfilePhoneNumber(phoneNumber).stream().map(tellerMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public TellerDTO upgradeToTeller(String phoneNumber, String location, double latitude, double longitude) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        Optional<TellerDTO> tellerOptional = findByProfilePhoneNumber(phoneNumber);
        if (tellerOptional.isPresent()) {
            return tellerOptional.get();
        }

        Profile tellerProfile = profileService.findByPhoneNumber(phoneNumber);
        Teller teller = new Teller();
        teller.setProfile(tellerProfile);
        teller.setLocation(location);
        teller.setLatitude(latitude);
        teller.setLongitude(longitude);
        Teller save = tellerRepository.save(teller);
        return tellerMapper.toDto(save);
    }

    @Override
    public GenericResponseDTO setTellerLimit(String accountNumber, Double amount) {
        if (amount > 0) {
            WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
            if (walletAccount != null) {
                walletAccount.setWalletLimit(String.valueOf(amount));
                WalletAccount save = walletAccountService.save(walletAccount);
                System.out.println("Updated Wallet Account Limit {} === " + save);
                return new GenericResponseDTO("00", HttpStatus.OK, "success", save);
            }
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid account Number", null);
        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Amount must be greater than zero", null);
    }
}
