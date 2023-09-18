package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.AgentRepository;
import ng.com.justjava.corebanking.repository.TellerRepository;
import ng.com.justjava.corebanking.repository.UserRepository;
import ng.com.justjava.corebanking.service.mapper.AgentMapper;
import ng.com.justjava.corebanking.client.ExternalOTPRESTClient;
import ng.com.justjava.corebanking.domain.Agent;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.Scheme;
import ng.com.justjava.corebanking.domain.SuperAgent;
import ng.com.justjava.corebanking.domain.Teller;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.AddressType;
import ng.com.justjava.corebanking.domain.enumeration.DocumentType;
import ng.com.justjava.corebanking.domain.enumeration.SpecificChannel;
import ng.com.justjava.corebanking.domain.enumeration.TransactionStatus;
import ng.com.justjava.corebanking.service.AgentService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.SuperAgentService;
import ng.com.justjava.corebanking.service.UserService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.WalletAccountTypeService;
import ng.com.justjava.corebanking.service.dto.AgentDTO;
import ng.com.justjava.corebanking.service.dto.AgentInviteDTO;
import ng.com.justjava.corebanking.service.dto.CreateAgentDTO;
import ng.com.justjava.corebanking.service.dto.CreateAgentResponseDTO;
import ng.com.justjava.corebanking.service.dto.FundDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.InviteeDTO;
import ng.com.justjava.corebanking.service.dto.NINDataResponseDTO;
import ng.com.justjava.corebanking.service.dto.NINExamplePayload;
import ng.com.justjava.corebanking.service.dto.NINRequestDTO;
import ng.com.justjava.corebanking.service.dto.NINResponseDTO;
import ng.com.justjava.corebanking.service.dto.PaymentResponseDTO;
import ng.com.justjava.corebanking.service.dto.ProfileDTO;
import ng.com.justjava.corebanking.service.dto.SendSMSDTO;
import ng.com.justjava.corebanking.service.dto.SuperAgentDataDTO;
import ng.com.justjava.corebanking.service.dto.SuperAgentDebitCreditDTO;
import ng.com.justjava.corebanking.service.dto.SuperAgentMetricsDTO;
import ng.com.justjava.corebanking.service.dto.WalletAccountDTO;
import ng.com.justjava.corebanking.service.dto.WalletAccountTypeDTO;
import ng.com.justjava.corebanking.service.dto.WalletExternalDTO;
import ng.com.justjava.corebanking.service.mapper.WalletAccountMapperImpl;
import ng.com.justjava.corebanking.util.Utility;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Transactional
@Service
public class AgentServiceImpl implements AgentService {

    private final Logger log = LoggerFactory.getLogger(AgentServiceImpl.class);

    private static final long Lower_Bond = 100000L;

    @Value("${app.email.subjects.agent-invite}")
    private String agentInviteSubject;

    @Value("${app.email.contents.agent-invite}")
    private String agentInviteSystemMessage;

    @Value("${app.email.contents.agent-invite-link}")
    private String agentInviteLink;

    @Value("${app.document-url}")
    private String documentUrl;

    private final AgentRepository agentRepository;
    private final UserService userService;
    private final AgentMapper agentMapper;
    private final ProfileService profileService;
    private final SuperAgentService superAgentService;
    private final UserRepository userRepository;
    private final TellerRepository tellerRepository;
    private final Utility utility;
    private final WalletAccountService walletAccountService;
    private final WalletAccountTypeService walletAccountTypeService;
    private final PasswordEncoder passwordEncoder;
    private final WalletAccountMapperImpl walletAccountMapperImpl;
    private static final long Upper_Bond = 900000L;
    private User theUser;
    private final ExternalOTPRESTClient externalOTPRESTClient;
    @Value("${app.sms.message.otp}")
    private String smsMessage;

    public AgentServiceImpl(AgentRepository agentRepository, UserService userService, AgentMapper agentMapper, ProfileService profileService, @Lazy SuperAgentService superAgentService, UserRepository userRepository, TellerRepository tellerRepository, Utility utility, WalletAccountService walletAccountService, WalletAccountTypeService walletAccountTypeService, PasswordEncoder passwordEncoder, ExternalOTPRESTClient externalOTPRESTClient, WalletAccountMapperImpl walletAccountMapperImpl) {
        this.agentRepository = agentRepository;
        this.userService = userService;
        this.agentMapper = agentMapper;
        this.profileService = profileService;
        this.superAgentService = superAgentService;
        this.userRepository = userRepository;
        this.tellerRepository = tellerRepository;
        this.utility = utility;
        this.walletAccountService = walletAccountService;
        this.walletAccountTypeService = walletAccountTypeService;
        this.passwordEncoder = passwordEncoder;
        this.externalOTPRESTClient = externalOTPRESTClient;
        this.walletAccountMapperImpl = walletAccountMapperImpl;
    }

    @Override
    public AgentDTO save(AgentDTO agentDTO) {
        log.debug("Request to save Agent : {}", agentDTO);

        String phoneNumber = agentDTO.getPhoneNumber();
        phoneNumber= utility.formatPhoneNumber(phoneNumber);
        Agent agent = agentMapper.toEntity(agentDTO);

        agent.setProfile(profileService.findByPhoneNumber(phoneNumber));
        agent = agentRepository.save(agent);
        return agentMapper.toDto(agent);
    }

    @Override
    public List<AgentDTO> findAll() {
        log.debug("Request to get all Agents");
        return agentRepository.findAll().stream().map(agentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<AgentDTO> findOne(Long id) {
        log.debug("Request to get Agent : {}", id);
        return agentRepository.findById(id).map(agentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Agent : {}", id);
        agentRepository.deleteById(id);
    }

    @Override
    public List<Agent> findAllBySuperAgent_Id(Long superAgentId) {
        log.debug("Request to get all Agents by SuperAgent : {}", superAgentId);
        return agentRepository.findAllBySuperAgent_Id(superAgentId);
    }

    @Override
    public List<Agent> findByLocationLike(String location) {
        log.debug("Request to get all Agents by Location : {}", location);

        return agentRepository.findByLocationLike(location);
    }

    @Override
    public Optional<Agent> findAllByProfilePhoneNumber(String agentPhoneNumber) {
        return agentRepository.findByProfilePhoneNumber(utility.formatPhoneNumber(agentPhoneNumber));
    }

    @Override
    public AgentDTO upgradeToAnAgent(String phoneNumber, String location) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        Optional<Agent> agentOptional = findAllByProfilePhoneNumber(phoneNumber);
        if (agentOptional.isPresent()){
            return agentMapper.toDto(agentOptional.get());
        }
        Profile agentProfile = profileService.findByPhoneNumber(phoneNumber);
        Agent agent = new Agent();
        agent.setProfile(agentProfile);
        agent.setLocation(location);
        Agent save = agentRepository.save(agent);
        return agentMapper.toDto(save);
    }

    @Override
    public GenericResponseDTO createAgent(CreateAgentDTO createAgentDTO, HttpSession session) {

        if (agentRepository.existsByProfilePhoneNumber(utility.formatPhoneNumber(createAgentDTO.getPhoneNumber()))) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Agent already exist");
        }

        String superAgentPhoneNumber = utility.formatPhoneNumber(createAgentDTO.getSuperAgentPhoneNumber());

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
                        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null);
                    }
                } else {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Nin validation failed", null);
                }
            } catch (Exception e) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), null);
            }
        } else {
            createAgentDTO.setPhoneNumber(utility.formatPhoneNumber(createAgentDTO.getPhoneNumber()));
        }

        Optional<Agent> allByProfilePhoneNumber = findAllByProfilePhoneNumber(createAgentDTO.getPhoneNumber());

        if (allByProfilePhoneNumber.isPresent()) {
            Agent agent = allByProfilePhoneNumber.get();
            Agent superAgent = agent.getSuperAgent();
            Optional<SuperAgent> byAgent = superAgentService.findByAgent(superAgent);
            if (byAgent.isPresent()) {
                SuperAgent superAgent1 = byAgent.get();
                Scheme scheme = superAgent1.getScheme();
                Scheme schemeFromSession = utility.getSchemeFromSession(session);
                if (schemeFromSession.equals(scheme)) {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "You are already an agent", null);
                }
            }
        }

        Agent superAgent = null;

        if (!StringUtils.isEmpty(superAgentPhoneNumber)) {
            Optional<Agent> superAgentOptional = findAllByProfilePhoneNumber(superAgentPhoneNumber);
            Optional<SuperAgent> byAgentProfilePhoneNumber = superAgentService.findByAgentProfilePhoneNumber(superAgentPhoneNumber);
            if (!superAgentOptional.isPresent() || !byAgentProfilePhoneNumber.isPresent()) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid super agent phoneNumber", null);
            }
            superAgent = superAgentOptional.get();
            SuperAgent superAgentEntity = byAgentProfilePhoneNumber.get();
            Scheme scheme = superAgentEntity.getScheme();
            if (scheme == null) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "invalid super agent scheme", null);
            }

            createAgentDTO.setScheme(scheme.getSchemeID());
        }

        /*Profile profileServiceByPhoneNumber = profileService.findByPhoneNumber(createAgentDTO.getPhoneNumber());

        if (profileServiceByPhoneNumber != null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User profile already exists!", null);
        }*/

        if (StringUtils.isEmpty(createAgentDTO.getPin())) {
            createAgentDTO.setPin("1234");
        }
        if (StringUtils.isEmpty(createAgentDTO.getAccountName())) {
            createAgentDTO.setAccountName(createAgentDTO.getFirstName());
        }

        if (StringUtils.isEmpty(String.valueOf(createAgentDTO.getOpeningBalance()))) {
            createAgentDTO.setOpeningBalance(0.0);
        }

        String cACDocument = createAgentDTO.getcACDoc();
        String documentName = "agent-cac-" + utility.returnPhoneNumberFormat(createAgentDTO.getPhoneNumber());
        String cACDocFormat = createAgentDTO.getcACDocFormat();

        GenericResponseDTO genericResponseDTO = new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Document type", null);
        if (utility.checkStringIsValid(cACDocFormat)) {
            if (DocumentType.JPG.name().equalsIgnoreCase(cACDocFormat)) {
                documentName = documentName + ".jpg";
            } else if (DocumentType.PDF.name().equalsIgnoreCase(cACDocFormat)) {
                documentName = documentName + ".pdf";
            } else {
                return genericResponseDTO;
            }
        }

        String cacFileName = documentUrl + documentName;

        String c01Doc = createAgentDTO.getC01Doc();
        String c01DocumentName = "agent-c01-" + utility.returnPhoneNumberFormat(createAgentDTO.getPhoneNumber());
        String c01DocFormat = createAgentDTO.getC01DocFormat();

        if (utility.checkStringIsValid(c01DocFormat)) {
            if (DocumentType.JPG.name().equalsIgnoreCase(c01DocFormat)) {
                c01DocumentName = c01DocumentName + ".jpg";
            } else if (DocumentType.PDF.name().equalsIgnoreCase(c01DocFormat)) {
                c01DocumentName = c01DocumentName + ".pdf";
            } else {
                return genericResponseDTO;
            }
        }
        String c01FileName = documentUrl + c01DocumentName;

        String c07Doc = createAgentDTO.getC07Doc();
        String c07DocumentName = "agent-c01-" + utility.returnPhoneNumberFormat(createAgentDTO.getPhoneNumber());
        String c07DocFormat = createAgentDTO.getC07DocFormat();

        if (utility.checkStringIsValid(c07DocFormat)) {
            if (DocumentType.JPG.name().equalsIgnoreCase(c07DocFormat)) {
                c07DocumentName = c07DocumentName + ".jpg";
            } else if (DocumentType.PDF.name().equalsIgnoreCase(c07DocFormat)) {
                c07DocumentName = c07DocumentName + ".pdf";
            } else {
                return genericResponseDTO;
            }
        }
        String c07FileName = documentUrl + c07DocumentName;

        String c02Doc = createAgentDTO.getC02Doc();
        String c02DocumentName = "agent-c02-" + utility.returnPhoneNumberFormat(createAgentDTO.getPhoneNumber());
        String c02DocFormat = createAgentDTO.getC02DocFormat();

        if (utility.checkStringIsValid(c02DocFormat)) {
            if (DocumentType.JPG.name().equalsIgnoreCase(c02DocFormat)) {
                c02DocumentName = c02DocumentName + ".jpg";
            } else if (DocumentType.PDF.name().equalsIgnoreCase(c02DocFormat)) {
                c02DocumentName = c02DocumentName + ".pdf";
            } else {
                return genericResponseDTO;
            }
        }

        String c02FileName = documentUrl + c02DocumentName;

        if (utility.checkStringIsValid(cACDocument)) {
            utility.saveImage(cACDocument, cacFileName);
        }
        if (utility.checkStringIsValid(c01Doc)) {
            utility.saveImage(c01Doc, c01FileName);
        }
        if (utility.checkStringIsValid(c07Doc)) {
            utility.saveImage(c07Doc, c07FileName);
        }
        if (utility.checkStringIsValid(c02Doc)) {
            utility.saveImage(c02Doc, c02FileName);
        }
        String generatedPassword = null;
        if (utility.checkStringIsNotValid(createAgentDTO.getPassword())) {
            //generate Password
            generatedPassword = Utility.generateSecurePassword().trim();
            //set the generated password
            createAgentDTO.setPassword(generatedPassword);
        }

        int kyc = 2;
        if (createAgentDTO.isSuperAgent() && utility.checkStringIsValid(createAgentDTO.getBvn())) {
            kyc = 3;
        }

        WalletExternalDTO walletExternalDTO = convertWalletAccountDTO(createAgentDTO, 2L, kyc);
        ResponseEntity<GenericResponseDTO> walletExternalResult = walletAccountService.createWalletExternal(walletExternalDTO, session);
        if (HttpStatus.OK.equals(walletExternalResult.getStatusCode())) {
            GenericResponseDTO body = walletExternalResult.getBody();

            if (body != null) {
                List<WalletAccountDTO> walletAccountDTOList = null;
                try {
                    if (body.getData() != null) {
                        List<WalletAccount> walletAccountList = (List<WalletAccount>) body.getData();
                        walletAccountDTOList = walletAccountMapperImpl.toDto(walletAccountList);
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
                    String agentPhoneNumber = createAgentDTO.getPhoneNumber();

                    Profile profile = profileService.findByPhoneNumber(agentPhoneNumber);

                    log.info("Company reg no ===> " + createAgentDTO.getCompanyRegNo());
                    log.info("Company reg type ===> " + createAgentDTO.getCompanyRegType());

                    Agent agent = new Agent();
                    agent.setLocation(createAgentDTO.getAddress());
                    agent.setLatitude(createAgentDTO.getLatitude());
                    agent.setLongitude(createAgentDTO.getLongitude());
                    agent.setBusinessName(createAgentDTO.getBusinessName());
                    agent.setBusinessType(createAgentDTO.getBusinessType());
                    agent.setCompanyRegNumber(createAgentDTO.getCompanyRegNo());
                    agent.setCompanyRegType(createAgentDTO.getCompanyRegType());
                    agent.setOccupation(createAgentDTO.getOccupation());
                    agent.setProfile(profile);
                    agent.setSuperAgent(superAgent);

                    log.info("AGENT ABOUT TO BE SAVED ===> " + agent);

                    agent = agentRepository.save(agent);

                    log.info("SAVED AGENT ===> " + agent);
                    User user = profile.getUser();

                    if (StringUtils.isEmpty(superAgentPhoneNumber)) {

                        long otp = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);
                        SendSMSDTO sendSMSDTO = new SendSMSDTO();
                        sendSMSDTO.setMobileNumber(agentPhoneNumber);
                        sendSMSDTO.setSmsMessage(smsMessage + " " + otp);

                        try {
                            externalOTPRESTClient.sendSMS(sendSMSDTO);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        session.setAttribute("otp", String.valueOf(otp));
                        if (user != null) {
                            user.setKey(String.valueOf(otp));
                            userRepository.save(user);

                            String email = user.getEmail();
                            if (utility.checkStringIsValid(email)) {
                                utility.sendEmail(email, "OTP Received", smsMessage + " " + otp);
                            }
                        }

//                        userService.sendOTP(agentPhoneNumber);

                    } else {
                        if (superAgent != null) {
                            agent = setAgentToSuperAgent(superAgentPhoneNumber, agentPhoneNumber, superAgent, agent);
                        }
                    }

                    CreateAgentResponseDTO createAgentResponseDTO = new CreateAgentResponseDTO();
                    createAgentResponseDTO.setAccountName(walletAccountDTO.getAccountName());
                    createAgentResponseDTO.setAccountNumber(walletAccountDTO.getAccountNumber());
                    createAgentResponseDTO.setPhoneNumber(profile.getPhoneNumber());
                    createAgentResponseDTO.setLocation(agent.getLocation());
                    createAgentResponseDTO.setAgentId(agent.getId());
                    createAgentResponseDTO.setUser(user);
                    createAgentResponseDTO.setToken(jwt);
                    createAgentResponseDTO.setFullName(profile.getFullName());

                    if (utility.checkStringIsValid(generatedPassword)) {
                        String emailContent = String.format("Your Wallet temporary password is %s . Please login to change the password", generatedPassword);

                        utility.sendToNotificationConsumer("", "", "", "Your Password",
                            emailContent, emailContent, createAgentDTO.getEmail(), profile.getPhoneNumber());
                    }
                    return new GenericResponseDTO("00", HttpStatus.CREATED, "success", createAgentResponseDTO);
                }
            }
        }
        return walletExternalResult.getBody();
    }

    @Override
    public GenericResponseDTO addAgentToSuperAgent(String superAgentPhoneNo, String agentPhoneNo) {
        superAgentPhoneNo = utility.formatPhoneNumber(superAgentPhoneNo);
        agentPhoneNo = utility.formatPhoneNumber(agentPhoneNo);

        Optional<Agent> superAgentOptional = findAllByProfilePhoneNumber(superAgentPhoneNo);
        if (!superAgentOptional.isPresent()){
            return new GenericResponseDTO("99",HttpStatus.BAD_REQUEST, "Invalid super agent phoneNumber", null);
        }

        Optional<Agent> agentOptional = findAllByProfilePhoneNumber(agentPhoneNo);
        if (!agentOptional.isPresent()){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid agent phoneNumber", null);
        }

        Agent superAgent = superAgentOptional.get();
        Agent agent = agentOptional.get();
        agent = setAgentToSuperAgent(superAgentPhoneNo, agentPhoneNo, superAgent, agent);

        return new GenericResponseDTO("00", HttpStatus.OK, "success", agentMapper.toDto(agent));
    }

    private Agent setAgentToSuperAgent(String superAgentPhoneNo, String agentPhoneNo, Agent superAgent, Agent agent) {
        Set<Agent> subAgents = superAgent.getSubAgents();
        subAgents.add(agent);
        superAgent.setSubAgents(subAgents);
        superAgent = agentRepository.save(superAgent);

        System.out.println("Updated super agent ==> " + superAgent);

        agent.setSuperAgent(superAgent);
        agent = agentRepository.save(agent);
        System.out.println("Updated agent ==> " + agent);

        List<WalletAccount> walletAccounts = walletAccountService.findAgentWallet(agentPhoneNo, 2L);
        List<WalletAccount> superAgentWalletAccounts = walletAccountService.findAgentWallet(superAgentPhoneNo, 2L);
        WalletAccount superAgentWalletAccount = superAgentWalletAccounts.get(0);
        walletAccounts.forEach(walletAccount -> {
            walletAccount.setParent(superAgentWalletAccount);
            WalletAccount save = walletAccountService.save(walletAccount);
            log.info("Updated Agent Wallet Account ==> " + save);
        });

        HashSet<WalletAccount> walletAccountSets = new HashSet<>(walletAccounts);
        superAgentWalletAccount.setSubWallets(walletAccountSets);
        WalletAccount save = walletAccountService.save(superAgentWalletAccount);
        log.info("Updated Super Agent Wallet Account ==> " + save);
        return agent;
    }

    @Override
    public GenericResponseDTO addTellerToAgent(String agentPhoneNo, String tellerPhoneNo) {
        agentPhoneNo = utility.formatPhoneNumber(agentPhoneNo);
        tellerPhoneNo = utility.formatPhoneNumber(tellerPhoneNo);
        Optional<Agent> agentOptional = findAllByProfilePhoneNumber(agentPhoneNo);
        if (!agentOptional.isPresent()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid agent phoneNumber", null);
        }

        Optional<Teller> tellerOptional = tellerRepository.findByProfilePhoneNumber(tellerPhoneNo);
        if (!tellerOptional.isPresent()){
            return new GenericResponseDTO("99",HttpStatus.BAD_REQUEST, "Invalid teller phoneNumber", null);
        }

        Agent agent = agentOptional.get();
        Teller teller = tellerOptional.get();
        Set<Teller> tellers = agent.getTellers();
        tellers.add(teller);

        agent.setTellers(tellers);
        agent = agentRepository.save(agent);

        System.out.println("Updated agent ==> " + agent);

        teller.setAgent(agent);
        teller = tellerRepository.save(teller);
        System.out.println("Updated teller ==> " + teller);

        List<WalletAccount> walletAccounts = walletAccountService.findAgentWallet(tellerPhoneNo, 5L);
        List<WalletAccount> agentWalletAccounts = walletAccountService.findAgentWallet(agentPhoneNo, 2L);
        if (!agentWalletAccounts.isEmpty()) {
            WalletAccount agentWalletAccount = agentWalletAccounts.get(0);
            walletAccounts.forEach(walletAccount -> {
                walletAccount.setParent(agentWalletAccount);
                WalletAccount save = walletAccountService.save(walletAccount);
                log.info("Updated teller Wallet Account ==> " + save);
            });

            HashSet<WalletAccount> walletAccountSets = new HashSet<>(walletAccounts);
            agentWalletAccount.setSubWallets(walletAccountSets);
            WalletAccount save = walletAccountService.save(agentWalletAccount);
            log.info("Updated Agent Wallet Account ==> " + save);

            return new GenericResponseDTO("00", HttpStatus.OK, "success", agentMapper.toDto(agent));
        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Agent wallet not found");
    }

    public WalletExternalDTO convertWalletAccountDTO(CreateAgentDTO createAgentDTO, Long accountTypeId, int kyc) {

        Optional<WalletAccountTypeDTO> walletAccountTypeDTOOptional = walletAccountTypeService.findByAccountTypeId(accountTypeId);

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
        walletAccountTypeDTOOptional.ifPresent(walletAccountTypeDTO -> walletExternalDTO.setWalletAccountTypeId(walletAccountTypeDTO.getId()));
        walletExternalDTO.setWalletLimit(createAgentDTO.getWalletLimit());
        walletExternalDTO.setKyc(kyc);
        walletExternalDTO.setBvn(createAgentDTO.getBvn());
        walletExternalDTO.setAgent(true);

        return walletExternalDTO;
    }

    @Override
    public GenericResponseDTO allocateFund(String sourcePhoneNumber, String destinationPhoneNumber, String pin, double amount, Long accountTypeId) {

        sourcePhoneNumber = utility.formatPhoneNumber(sourcePhoneNumber);
        destinationPhoneNumber = utility.formatPhoneNumber(destinationPhoneNumber);

        List<WalletAccount> sourceAccountList = walletAccountService.findAgentWallet(sourcePhoneNumber, 2L);
        if (sourceAccountList.isEmpty()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid super Agent PhoneNumber", null);
        }

        List<WalletAccount> destinationAccountList = walletAccountService.findAgentWallet(destinationPhoneNumber, accountTypeId);

        if (destinationAccountList.isEmpty()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Agent/Teller PhoneNumber", null);
        }

        WalletAccount sourceAccount = sourceAccountList.get(0);
        Profile accountOwner = sourceAccount.getAccountOwner();
        String sourceAccountName = accountOwner != null ? accountOwner.getFullName() : sourceAccount.getAccountName();

        if (accountOwner == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid source Account", null);
        }

        WalletAccount destinationAccount = destinationAccountList.get(0);
        String destinationAccountName = destinationAccount.getAccountOwner() != null ? destinationAccount.getAccountOwner().getFullName() : destinationAccount.getAccountName();

        FundDTO fundDTO = new FundDTO();
        fundDTO.setTransRef(utility.getUniqueTransRef());
        fundDTO.setSourceAccountName(sourceAccountName);
        fundDTO.setBeneficiaryName(destinationAccountName);
        fundDTO.setPhoneNumber(sourcePhoneNumber);
        fundDTO.setNarration("Allocate fund to " + destinationAccountName);
        fundDTO.setAmount(amount);
        fundDTO.setStatus(TransactionStatus.START);
        fundDTO.setAccountNumber(destinationAccount.getAccountNumber());
        fundDTO.setSourceAccountNumber(sourceAccount.getAccountNumber());
        fundDTO.setChannel("WalletToWallet");
        fundDTO.setSpecificChannel(SpecificChannel.SEND_MONEY_INTRA.getName());
        fundDTO.setShortComment("Allocate fund");

        // Profile profile = profileService.
        String currentEncryptedPin = accountOwner.getPin();

        if (!passwordEncoder.matches(pin, currentEncryptedPin)) {
            return new GenericResponseDTO("401", HttpStatus.UNAUTHORIZED, "Invalid pin", null);
        }

        fundDTO.setPin(pin);

        System.out.println(" FundDTO  === " + fundDTO);
        PaymentResponseDTO response = walletAccountService.sendMoney(fundDTO);

        if (response.getError()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, response.getMessage(), response);
        } else {
            return new GenericResponseDTO("00", HttpStatus.OK, response.getMessage(), response);
        }

    }

    @Override
    public AgentDTO upgradeToAnAgentGeo(String phoneNumber, String location, double latitude, double longitude) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        Optional<Agent> agentOptional = findAllByProfilePhoneNumber(phoneNumber);
        if (agentOptional.isPresent()) {
            return agentMapper.toDto(agentOptional.get());
        }
        Profile agentProfile = profileService.findByPhoneNumber(phoneNumber);
        Agent agent = new Agent();
        agent.setProfile(agentProfile);
        agent.setLocation(location);
        agent.setLatitude(latitude);
        agent.setLongitude(longitude);
        Agent save = agentRepository.save(agent);
        return agentMapper.toDto(save);
    }

    @Override
    public Page<AgentDTO> getAllAgents(Pageable pageable) {
        return agentRepository.findAll(pageable).map(agentMapper::toDto);
    }

    @Override
    public AgentDTO getAgentDetails(String phoneNumber) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        Optional<AgentDTO> agentDTOOptional = agentRepository.findByProfilePhoneNumber(phoneNumber).map(agentMapper::toDto);
        return agentDTOOptional.orElse(null);
    }

    @Override
    public List<AgentDTO> getAllASuperAgentAgents(String phoneNumber) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        return agentRepository.findAllBySuperAgentProfilePhoneNumber(phoneNumber).stream().map(agentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Agent>
    getAllSuperAgentAgents(String phoneNumber) {
        return getAllASuperAgentAgents(phoneNumber).stream().map(agentMapper::toEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<Agent> findByProfile(Profile profile) {
        return agentRepository.findByProfile(profile);
    }

    @Override
    public GenericResponseDTO sendInvite(AgentInviteDTO agentInviteDTO) {
        String superAgentPhoneNumber = utility.formatPhoneNumber(agentInviteDTO.getSuperAgentPhoneNumber());
        List<InviteeDTO> invitees = agentInviteDTO.getInvitees();

        for (InviteeDTO inviteeDTO : invitees) {
            String email = inviteeDTO.getEmail();
            String message = inviteeDTO.getMessage();
            String name = inviteeDTO.getName();

            StringBuilder str = new StringBuilder("Dear ").append(name).append(",");

            if (StringUtils.isNotEmpty(message)) {
                str.append("<br/>")
                    .append("<br/>")
                    .append(message);
            }

            str.append("<br/>")
                .append("<br/>")
                .append(agentInviteSystemMessage)
                .append("<br/>")
                .append("<br/>")
//                .append("<p><a href=\"http://154.113.17.252:8085/registeration-options?phone=\">http://154.113.17.252:8085/registeration-options?phone=</a></p>");
                .append(agentInviteLink).append(superAgentPhoneNumber);

            utility.sendEmail(email, agentInviteSubject, str.toString());
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return new GenericResponseDTO("00", HttpStatus.OK, "success");
    }

    public SuperAgentDataDTO getSuperAgentsMetrics(){
    	 Optional<ProfileDTO> theUser = profileService.findByUserIsCurrentUser();
         if (theUser.isPresent()) {
             Long profileId = theUser.get().getId();
             SuperAgentDebitCreditDTO findSuperAgentAgentsToTalDeposits = agentRepository.findSuperAgentAgentsToTalDeposits(profileId);
             SuperAgentDebitCreditDTO findSuperAgentAgentsToTalWithdrawals = agentRepository.findSuperAgentAgentsToTalWithdrawals(profileId);
             SuperAgentDebitCreditDTO findSuperAgentAgentsToTalTransactions =  agentRepository.findSuperAgentAgentsToTalTransactions(profileId);
             List<SuperAgentMetricsDTO> findAllSuperAgentsBestAgentByHighestTransaction =  agentRepository.findAllSuperAgentsBestAgentByHighestTransaction(profileId);
             SuperAgentDataDTO superAgentDataDTO = new SuperAgentDataDTO();
             superAgentDataDTO.setTotalTransactions(findSuperAgentAgentsToTalTransactions.getTransaction_count());
             superAgentDataDTO.setTotalDeposits(findSuperAgentAgentsToTalDeposits.getTotal_credit());
             superAgentDataDTO.setTotalWithdrawals(findSuperAgentAgentsToTalWithdrawals.getTotal_debit());
             superAgentDataDTO.setSuperAgentBestAgents(findAllSuperAgentsBestAgentByHighestTransaction);
             return superAgentDataDTO;
         }
         return null;
    }

    @Override
    public GenericResponseDTO becomeAnAgent(CreateAgentDTO createAgentDTO, HttpSession session) {
        String phoneNumber = utility.formatPhoneNumber(createAgentDTO.getPhoneNumber());

        Profile profile = profileService.findByPhoneNumber(phoneNumber);

        Optional<Agent> agentOptional = findByProfile(profile);

        if (agentOptional.isPresent()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "You are already an agent", phoneNumber);
        }

        Agent agent = new Agent();

        if (StringUtils.isNotEmpty(createAgentDTO.getSuperAgentPhoneNumber())) {
            Optional<Agent> superAgentOptional = findAllByProfilePhoneNumber(createAgentDTO.getSuperAgentPhoneNumber());
            if (!superAgentOptional.isPresent()) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid super agent phoneNumber", createAgentDTO.getSuperAgentPhoneNumber());
            }
            agent.setSuperAgent(superAgentOptional.get());
        }

        User user = profile.getUser();
        String accountName = profile.getFullName();
        if (user != null) {
            accountName = user.getFirstName() + " Agent Wallet";
        }

        WalletExternalDTO walletExternalDTO = new WalletExternalDTO();
        walletExternalDTO.setWalletLimit(createAgentDTO.getWalletLimit());
        walletExternalDTO.setWalletAccountTypeId(2L);
        walletExternalDTO.setPhoneNumber(createAgentDTO.getPhoneNumber());
        walletExternalDTO.setAccountName(accountName);
        walletExternalDTO.setOpeningBalance(0.0);

        Scheme scheme = utility.getSchemeFromSession(session);

        ResponseEntity<GenericResponseDTO> responseEntity = walletAccountService.createWalletForExternal(walletExternalDTO, new GenericResponseDTO(), scheme, profile, true);

        GenericResponseDTO body = responseEntity.getBody();
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return body;
        }

        WalletAccountDTO walletAccountDTO = null;
        List<WalletAccountDTO> data = (List<WalletAccountDTO>) body.getData();
        if (data != null && !data.isEmpty()) {
            walletAccountDTO = data.get(0);
        }

        agent.setOccupation(createAgentDTO.getOccupation());
        agent.setCompanyRegType(createAgentDTO.getCompanyRegType());
        agent.setCompanyRegNumber(createAgentDTO.getCompanyRegNo());
        agent.setBusinessType(createAgentDTO.getBusinessType());
        agent.setBusinessName(createAgentDTO.getBusinessName());
        agent.setLocation(createAgentDTO.getAddress());
        agent.setLongitude(createAgentDTO.getLongitude());
        agent.setLatitude(createAgentDTO.getLatitude());
        agent.setProfile(profile);
        Agent save = agentRepository.save(agent);

        log.debug("Saved Agent === " + save);

        CreateAgentResponseDTO createAgentResponseDTO = new CreateAgentResponseDTO();

        if (walletAccountDTO != null) {
            createAgentResponseDTO.setAccountName(walletAccountDTO.getAccountName());
            createAgentResponseDTO.setAccountNumber(walletAccountDTO.getAccountNumber());
        }

        createAgentResponseDTO.setPhoneNumber(phoneNumber);
        createAgentResponseDTO.setLocation(save.getLocation());
        createAgentResponseDTO.setAgentId(save.getId());
        createAgentResponseDTO.setUser(user);
        createAgentResponseDTO.setFullName(profile.getFullName());

        return new GenericResponseDTO("00", HttpStatus.OK, "success", createAgentResponseDTO);

    }

    @Override
    public GenericResponseDTO createAgents(List<CreateAgentDTO> createAgentDTOS, HttpSession session) {

        List<CreateAgentResponseDTO> createAgentResponseDTOS = new ArrayList<>();
        for (CreateAgentDTO createAgentDTO : createAgentDTOS) {
            GenericResponseDTO genericResponseDTO = createAgent(createAgentDTO, session);
            CreateAgentResponseDTO data = (CreateAgentResponseDTO) genericResponseDTO.getData();
            createAgentResponseDTOS.add(data);
        }

        return new GenericResponseDTO("00", HttpStatus.CREATED, "success", createAgentResponseDTOS);

    }

}
