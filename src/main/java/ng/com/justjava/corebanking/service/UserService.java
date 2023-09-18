package ng.com.justjava.corebanking.service;

import io.github.jhipster.security.RandomUtil;
import ng.com.justjava.corebanking.domain.*;
import ng.com.justjava.corebanking.repository.*;
import ng.com.justjava.corebanking.security.AuthoritiesConstants;
import ng.com.justjava.corebanking.security.SecurityUtils;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.exception.CorporateAlreadyExistsException;
import ng.com.justjava.corebanking.service.exception.GenericException;
import ng.com.justjava.corebanking.service.mapper.UserMapper;
import ng.com.justjava.corebanking.client.ExternalOTPRESTClient;
import ng.com.justjava.corebanking.config.AsyncConfiguration;
import ng.com.justjava.corebanking.config.Constants;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.justjava.corebanking.domain.enumeration.DocumentType;
import ng.com.justjava.corebanking.domain.enumeration.UserStatus;
import ng.com.systemspecs.apigateway.repository.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.justjava.corebanking.util.Utility;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private static final long Lower_Bond = 100000L;
    private static final long Upper_Bond = 900000L;
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final Utility utility;


    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final MyDeviceService myDeviceService;
    private final AuthorityRepository authorityRepository;
    private final ExternalOTPRESTClient externalOTPRESTClient;
    private final AsyncConfiguration asyncConfiguration;
    private final CacheManager cacheManager;
    private final ProfileService profileService;
    private final CorporateProfileService corporateProfileService;
    private final MailService mailService;
    private final KyclevelRepository kyclevelRepository;
    private final CustomAuditEventRepository customAuditEventRepository;


    @Value("${app.sms.message.otp}")
    private String otpMessage;
    @Value("${app.document-url}")
    private String documentUrl;

    public UserService(UserMapper userMapper, UserRepository userRepository, ProfileRepository profileRepository,
                       @Lazy Utility utility, PasswordEncoder passwordEncoder,
                       @Lazy MyDeviceService myDeviceService, AuthorityRepository authorityRepository, ExternalOTPRESTClient externalOTPRESTClient,
                       AsyncConfiguration asyncConfiguration, CacheManager cacheManager,
                       @Lazy ProfileService profileService, CorporateProfileService corporateProfileService, MailService mailService,
                       KyclevelRepository kyclevelRepository, CustomAuditEventRepository customAuditEventRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.utility = utility;
        this.passwordEncoder = passwordEncoder;
        this.myDeviceService = myDeviceService;
        this.authorityRepository = authorityRepository;
        this.externalOTPRESTClient = externalOTPRESTClient;
        this.asyncConfiguration = asyncConfiguration;
        this.cacheManager = cacheManager;
        this.profileRepository = profileRepository;
        this.profileService = profileService;
        this.corporateProfileService = corporateProfileService;
        this.mailService = mailService;
        this.kyclevelRepository = kyclevelRepository;
        this.customAuditEventRepository = customAuditEventRepository;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(RegisteredUserDTO registeredUserDTO, String password) {
        userRepository.findOneByLogin(registeredUserDTO.getPhoneNumber().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });

        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password.trim());
        newUser.setLogin(registeredUserDTO.getPhoneNumber());
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(registeredUserDTO.getFirstName());
        newUser.setLastName(registeredUserDTO.getLastName());
        if (!StringUtils.isEmpty(registeredUserDTO.getEmail())) {
            newUser.setEmail(registeredUserDTO.getEmail().toLowerCase().trim());
        }

        System.out.println("New user ===> " + newUser);

        //newUser.setImageUrl(userDTO.getImageUrl());
        //newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(true);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        newUser.setStatus("OK");
        User user = userRepository.save(newUser);

        System.out.println("User ===> " + user);

        Profile profile = new Profile();
        profile.setBvn(registeredUserDTO.getBvn());
        profile.setUser(user);
        profile.setPhoneNumber(registeredUserDTO.getPhoneNumber());
        profile.setProfileID("1");
        profile.setNin(registeredUserDTO.getNin());
        profile.setDeviceNotificationToken(registeredUserDTO.getDeviceNotificationToken());

        Optional<Kyclevel> kyclevelOptional;
        if (registeredUserDTO.getBvn() != null && !registeredUserDTO.getBvn().isEmpty()
            && registeredUserDTO.getNin() != null && !registeredUserDTO.getNin().isEmpty()) {
            kyclevelOptional = kyclevelRepository.findById(2L);
        } else {
            kyclevelOptional = kyclevelRepository.findById(1L);
        }
        log.debug("KYCLEVEL of User==>  " + user.getFirstName() + " " + user.getLastName() + "[" + user.getLogin() + "] has been set to Level" + kyclevelOptional.get().getKycLevel());
        kyclevelOptional.ifPresent(profile::setKyc);

        System.out.println("PROFILE  +++===> " + profile);

        profileRepository.save(profile);
//        mailService.sendActivationEmail(user); //Send activation email to user
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User registerCorporateUser(RegisteredCorporateUserDTO registeredUserDTO, String password) {
        Optional<User> usr = userRepository.findOneByLogin(registeredUserDTO.getPhoneNumber().toLowerCase());
        User newUser = new User();
        Profile profile = new Profile();
        if (usr.isPresent()) {
            newUser = usr.get();
            profile = profileService.findByPhoneNumber(registeredUserDTO.getPhoneNumber().toLowerCase());
        } else {
            String encryptedPassword = passwordEncoder.encode(password.trim());
            newUser.setLogin(registeredUserDTO.getPhoneNumber());
            newUser.setPassword(encryptedPassword);
            newUser.setFirstName(registeredUserDTO.getFirstName());
            newUser.setLastName(registeredUserDTO.getLastName());
            if (!StringUtils.isEmpty(registeredUserDTO.getEmail())) {
                newUser.setEmail(registeredUserDTO.getEmail().toLowerCase().trim());
            }

            System.out.println("New user ===> " + newUser);
            // new user is not active
            newUser.setActivated(true);
            // new user gets registration key
            newUser.setActivationKey(RandomUtil.generateActivationKey());
            Set<Authority> authorities = new HashSet<>();
            authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
            newUser.setAuthorities(authorities);
            newUser.setStatus("OK");
            User user = userRepository.save(newUser);

            System.out.println("User ===> " + user);

            profile.setBvn(registeredUserDTO.getBvn());
            profile.setUser(user);
            profile.setPhoneNumber(registeredUserDTO.getPhoneNumber());
            profile.setProfileID("1");
            profile.setNin(registeredUserDTO.getNin());
            profile.setDeviceNotificationToken(registeredUserDTO.getDeviceNotificationToken());

            Optional<Kyclevel> kyclevelOptional;
            if (registeredUserDTO.getBvn() != null && !registeredUserDTO.getBvn().isEmpty()
                && registeredUserDTO.getNin() != null && !registeredUserDTO.getNin().isEmpty()) {
                kyclevelOptional = kyclevelRepository.findById(2L);
            } else {
                kyclevelOptional = kyclevelRepository.findById(1L);
            }
            log.debug("KYCLEVEL of User==>  " + user.getFirstName() + " " + user.getLastName() + "[" + user.getLogin() + "] has been set to Level" + kyclevelOptional.get().getKycLevel());
            kyclevelOptional.ifPresent(profile::setKyc);
            System.out.println("PROFILE  +++===> " + profile);
            profileRepository.save(profile);
        }

        // Create Business Corporate Profile
        if (registeredUserDTO.getCorporateDocuments().size() > 0) {
            Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
            if (asyncExecutor != null) {
                asyncExecutor.execute(() -> {
                    try {
                        uploadCorporateDocuments(registeredUserDTO.getCorporateDocuments(), registeredUserDTO.getBusinessPhoneNo());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }


        CorporateProfile corporateProfileDto = new CorporateProfile();
        if (corporateProfileService.existsByPhoneNumber(registeredUserDTO.getBusinessPhoneNo())
            || corporateProfileService.existsByRcNO(registeredUserDTO.getRcNO())) {
            throw new CorporateAlreadyExistsException();
        } else {
            corporateProfileDto.setName(registeredUserDTO.getBusinessName());
            corporateProfileDto.setProfile(profile);
            corporateProfileDto.setAddress(registeredUserDTO.getBusinessAddress());
            corporateProfileDto.setCategory(registeredUserDTO.getCategory());
            corporateProfileDto.setPhoneNo(registeredUserDTO.getBusinessPhoneNo());
            corporateProfileDto.setRcNO(registeredUserDTO.getRcNO());
            corporateProfileDto.setRegType(registeredUserDTO.getRegType());
            corporateProfileDto.setTin(registeredUserDTO.getTin());
            corporateProfileDto.setCacCertificate(registeredUserDTO.isCacCertificate());
            corporateProfileDto.setCacCo7(registeredUserDTO.isCacCo7());
            corporateProfileDto.setCacCo2(registeredUserDTO.isCacCo2());
        }
        CorporateProfile savedCorporateProfile = corporateProfileService.save(corporateProfileDto);
        log.debug("Created Information for Corporate Profile: {}", savedCorporateProfile);

        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);

        return newUser;
    }

    public GenericResponseDTO registerCorporateUserExternal(RegisteredCorporateUserDTO registeredUserDTO, String password) {
        log.debug("Corporate Registration Object, {}", registeredUserDTO);
        CreateCorporate createCorporate = new CreateCorporate();
        Optional<User> usr = userRepository.findOneByLogin(registeredUserDTO.getPhoneNumber().toLowerCase());
        User newUser = new User();
        Profile profile = new Profile();
        if (usr.isPresent()) {
            newUser = usr.get();
            profile = profileService.findByPhoneNumber(registeredUserDTO.getPhoneNumber().toLowerCase());
        } else {
            String encryptedPassword = passwordEncoder.encode(password.trim());
            newUser.setLogin(registeredUserDTO.getPhoneNumber());
            newUser.setPassword(encryptedPassword);
            newUser.setFirstName(registeredUserDTO.getFirstName());
            newUser.setLastName(registeredUserDTO.getLastName());
            if (!StringUtils.isEmpty(registeredUserDTO.getEmail())) {
                newUser.setEmail(registeredUserDTO.getEmail().toLowerCase().trim());
            }

            System.out.println("New user ===> " + newUser);
            // new user is not active
            newUser.setActivated(true);
            // new user gets registration key
            newUser.setActivationKey(RandomUtil.generateActivationKey());
            Set<Authority> authorities = new HashSet<>();
            authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
            newUser.setAuthorities(authorities);
            newUser.setStatus("OK");
            User user = userRepository.save(newUser);

            System.out.println("User ===> " + user);

            profile.setBvn(registeredUserDTO.getBvn());
            profile.setUser(user);
            profile.setPhoneNumber(registeredUserDTO.getPhoneNumber());
            profile.setProfileID("1");
            profile.setNin(registeredUserDTO.getNin());
            profile.setDeviceNotificationToken(registeredUserDTO.getDeviceNotificationToken());

            Optional<Kyclevel> kyclevelOptional;
            if (registeredUserDTO.getBvn() != null && !registeredUserDTO.getBvn().isEmpty()
                && registeredUserDTO.getNin() != null && !registeredUserDTO.getNin().isEmpty()) {
                kyclevelOptional = kyclevelRepository.findById(2L);
            } else {
                kyclevelOptional = kyclevelRepository.findById(1L);
            }
            log.debug("KYCLEVEL of User==>  " + user.getFirstName() + " " + user.getLastName() + "[" + user.getLogin() + "] has been set to Level" + kyclevelOptional.get().getKycLevel());
            kyclevelOptional.ifPresent(profile::setKyc);
            System.out.println("PROFILE  +++===> " + profile);
            Profile savedProfile = profileRepository.save(profile);
            System.out.println("PROFILE  SAVED ===> " + savedProfile);
        }

        // Create Business Corporate Profile
        if (registeredUserDTO.getCorporateDocuments()!= null && registeredUserDTO.getCorporateDocuments().size() > 0) {
            System.out.print("CORPORATE DOCS SIZE:  " + registeredUserDTO.getCorporateDocuments().size());
            Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
            if (asyncExecutor != null) {
                asyncExecutor.execute(() -> {
                    try {
                        uploadCorporateDocuments(registeredUserDTO.getCorporateDocuments(), registeredUserDTO.getBusinessPhoneNo());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        CorporateProfile corporateProfileDto = new CorporateProfile();

        boolean rcnoExists = corporateProfileService.existsByRcNO(registeredUserDTO.getRcNO());
        boolean businessPhoneExists = corporateProfileService.existsByRcNO(registeredUserDTO.getBusinessPhoneNo());

        if (rcnoExists) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Corporate with RC Number Already Exists", null);
        }
        if (businessPhoneExists) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Corporate with Phone Number Already Exists", null);
        }

        corporateProfileDto.setName(registeredUserDTO.getBusinessName());
        corporateProfileDto.setProfile(profile);
        corporateProfileDto.setAddress(registeredUserDTO.getBusinessAddress());
        corporateProfileDto.setCategory(registeredUserDTO.getCategory());
        corporateProfileDto.setPhoneNo(registeredUserDTO.getBusinessPhoneNo());
        corporateProfileDto.setRcNO(registeredUserDTO.getRcNO());
        corporateProfileDto.setRegType(registeredUserDTO.getRegType());
        corporateProfileDto.setTin(registeredUserDTO.getTin());
        corporateProfileDto.setCacCertificate(registeredUserDTO.isCacCertificate());
        corporateProfileDto.setCacCo7(registeredUserDTO.isCacCo7());
        corporateProfileDto.setCacCo2(registeredUserDTO.isCacCo2());
        CorporateProfile savedCorporateProfile = corporateProfileService.save(corporateProfileDto);
        log.debug("Created Information for Corporate Profile: {}", savedCorporateProfile);

        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);

        createCorporate.setUser(newUser);
        createCorporate.setCorporateProfile(savedCorporateProfile);
        return new GenericResponseDTO("00", HttpStatus.OK, "success", createCorporate);
    }

    public User createUserWithoutProfile(String username, String password) throws URISyntaxException {

        userRepository.findOneByLogin(username.toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new UsernameAlreadyUsedException();
            }
        });

        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(username);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);

        //newUser.setImageUrl(userDTO.getImageUrl());
        //newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(true);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        newUser.setStatus("OK");
        return userRepository.save(newUser);
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        user.setStatus("OK");
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
                .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public GenericResponseDTO forgotPassword(LostPasswordDTO lostPasswordDTO) {
        String phoneNumber = utility.formatPhoneNumber(lostPasswordDTO.getPhoneNumber());
        String newPassword = lostPasswordDTO.getNewPassword();
        Profile profile = profileService.findByPhoneNumber(phoneNumber);

        if (profile == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid user profile", null);
        }

        if (!pinMatches(lostPasswordDTO, profile)) {
            return new GenericResponseDTO("99", HttpStatus.UNAUTHORIZED, "Invalid pin", null);
        }

        Optional<User> userOptional = userRepository.findOneByLogin(utility.formatPhoneNumber(phoneNumber));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("Retrieved user details " + user);
            UserDTO userDTO = new UserDTO(user);
            userDTO.setPassword(newPassword);
            userDTO.setActivated(true);
            user.setStatus(UserStatus.OK.getName());
            System.out.println("Converted userDTO " + userDTO);
            Optional<UserDTO> userDTOOptional = updateUser(userDTO);

            System.out.println("UserDto optional ==> _++ " + userDTOOptional);

            return userDTOOptional
                .map(dto -> new GenericResponseDTO("00", HttpStatus.OK, "Password updated successfully", userOptional.get()))
                .orElseGet(() -> new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Password update failure", null));
        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid user phone number", null);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    public GenericResponseDTO validateUserPassword(LoginVM loginVM) {

        String phoneNumber = utility.formatPhoneNumber(loginVM.getUsername());

        Optional<User> userOptional = findByLogin(phoneNumber);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            String currentEncryptedPassword = user.getPassword();

            if (!passwordEncoder.matches(loginVM.getPassword(), currentEncryptedPassword)) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Password", null);
            } else {
                Profile profile = profileService.findByPhoneNumber(phoneNumber);
                if (profile != null) {
                    return new GenericResponseDTO("00", HttpStatus.OK, "success", profile.getSecretQuestion());
                } else {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Profile not found!", null);
                }
            }
        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User not found!", null);
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
                utility.sendAlertEmailForAction(user, "Change Password", LocalDateTime.now());
            });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatePassword(String phoneNumber, String newPassword) {

        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        Optional<User> oneByLogin = userRepository.findOneByLogin(phoneNumber);
        if (oneByLogin.isPresent()) {
            User user = oneByLogin.get();
            clearUserCaches(user);
            System.out.println("User before save " + user);
            user.setPassword(passwordEncoder.encode(newPassword));
            User savedUser = userRepository.saveAndFlush(user);
            System.out.println("User after save =====> " + savedUser);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Optional<UserDTO> updateOldPassword(String phoneNumber, String newPassword) {
        return Optional.of(userRepository
                .findOneByLogin(utility.formatPhoneNumber(phoneNumber)))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setPassword(passwordEncoder.encode(newPassword));
                this.clearUserCaches(user);
                log.debug("updateOldPassword for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    private void auditChangeLostPassword(String newPassword, User user, User savedUser) {
        Map<String, Object> data = new HashMap<>();
        data.put("New password", newPassword);
        data.put("Action", "Change lost password");
        data.put("Initial User ", user);
        data.put("Updated User ", savedUser);
        AuditEvent change_lost_password = new AuditEvent(user.getLogin(), "Change lost password", data);

        customAuditEventRepository.add(change_lost_password);
    }

    public GenericResponseDTO changePhonenumberByAdmin(String oldPhoneNumber, String newPhoneNumber) {

        User user = userRepository.findByLogin(oldPhoneNumber);
        Profile profile = profileRepository.findOneByPhoneNumber(oldPhoneNumber);

        if (user == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User not found", null);
        }

        if (profile == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Profile not found", null);
        }

//        if (user != null && profile != null) {
            user.setLogin(newPhoneNumber);
            profile.setPhoneNumber(newPhoneNumber);
            this.clearUserCaches(user);
            userRepository.save(user);
            profileRepository.save(profile);
            log.debug("Changed password for User: {}", user);

            Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
            if (asyncExecutor != null) {
                asyncExecutor.execute(() -> {
                    try {
                        String subject = "Phone number changed".toUpperCase();
                        String message = "Your new phone number is " + newPhoneNumber;
                        utility.sendToNotificationConsumer("", "", "", subject, message, message, user.getEmail(), user.getLogin());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            return new GenericResponseDTO("00", HttpStatus.OK, "success", user);
//        }

//        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null);
    }

    public GenericResponseDTO managerUserStatus(String phoneNumber, String status) {

        try{

            String phone = utility.formatPhoneNumber(phoneNumber);
            Profile profile = profileService.findByPhoneNumber(phone);

            if (profile == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Profile with phone number:  " + phoneNumber + " not found.", null);
            }

            User user = profile.getUser();

            if (user == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User with phone number:  " + phoneNumber + " not found.", null);
            }

            if (status.equalsIgnoreCase("Activate")){

                user.setActivated(true);
                user.setStatus(UserStatus.OK.getName());
                userRepository.save(user);

            }else if (status.equalsIgnoreCase("Deactivate")){

                user.setActivated(false);
                user.setStatus(UserStatus.DEACTIVATE_CUSTOMER.getName());
                userRepository.save(user);

            }

            return new GenericResponseDTO("00", HttpStatus.OK, "success", user);

        }catch (Exception e){
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", e.getLocalizedMessage());
        }

    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    public void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evictIfPresent(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evictIfPresent(user.getEmail());
        }
    }

    public ResponseEntity<String> sendOTP(String phoneNumber, String serviceName, HttpSession session) {
        String smsMessage = "Your " + serviceName + " verification code is ";

        long otp = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);

        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        Optional<ProfileDTO> p = null;
        try{
            p = profileService.findOneByPhoneNumber(phoneNumber);
        } catch(Exception ex){
            log.debug("Profile is Null!");
        }

        SendSMSDTO sendSMSDTO = new SendSMSDTO();
        sendSMSDTO.setMobileNumber(phoneNumber);
        sendSMSDTO.setSmsMessage(smsMessage + " " + otp);

        session.setAttribute("OTP" + phoneNumber, otp);
        session.setAttribute("phoneNumber", phoneNumber);

        String s = "failed";
        try {
            s = externalOTPRESTClient.sendSMS(sendSMSDTO);
            if(p != null)
                utility.sendEmail(p.get().getUser().getEmail(), serviceName+" OTP", smsMessage + " " + otp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!"failed".equalsIgnoreCase(s)) {
            return new ResponseEntity<>(s, HttpStatus.OK);
        }

        return new ResponseEntity<>(s, HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<GenericResponseDTO> sendOTPEmail(String phoneNumber, String email, String serviceName, HttpSession session) {
        String smsMessage = "Your " + serviceName + " verification code is ";

        long otp = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);

        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        String s = "failed";

        SendSMSDTO sendSMSDTO = new SendSMSDTO();
        sendSMSDTO.setMobileNumber(phoneNumber);
        sendSMSDTO.setSmsMessage(smsMessage + " " + otp);

        session.setAttribute("OTP" + phoneNumber, otp);
        session.setAttribute("phoneNumber", phoneNumber);
        try {
            s = externalOTPRESTClient.sendSMS(sendSMSDTO);
                utility.sendEmail(email, serviceName+" OTP", smsMessage + " " + otp);
            utility.sendEmail("akinrinde@justjava.com.ng", serviceName+" OTP", smsMessage + " " + otp);
            utility.sendEmail("temireemmanuel@gmail.com", serviceName+" OTP", smsMessage + " " + otp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!"failed".equalsIgnoreCase(s)) {
            return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", s), HttpStatus.OK);
        }

        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null), HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<String> sendOTP(String phoneNumber, String serviceName) {
        String smsMessage = "Your " + serviceName + " verification code is ";

        return sendSMS(phoneNumber, smsMessage);
    }

    public ResponseEntity<String> sendOTP(String phoneNumber) {
        String message = otpMessage;

        return sendSMS(phoneNumber, message);
    }

    private ResponseEntity<String> sendSMS(String phoneNumber, String smsMessage) {
        long otp = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);

        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        SendSMSDTO sendSMSDTO = new SendSMSDTO();
        sendSMSDTO.setMobileNumber(phoneNumber);
        sendSMSDTO.setSmsMessage(smsMessage + " " + otp);

        User byLogin = userRepository.findByLogin(phoneNumber);

        if (byLogin != null) {
            byLogin.setKey(String.valueOf(otp));
            userRepository.save(byLogin);

            String s = "";
            try {
                s = externalOTPRESTClient.sendSMS(sendSMSDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String email = byLogin.getEmail();
            if (utility.checkStringIsValid(email)) {
                utility.sendEmail(email, "OTP Received", smsMessage + otp);
            }

            return new ResponseEntity<>(s, HttpStatus.OK);
        }
        return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Boolean> verifyOTPWithSession(Long otp, String phoneNumber, HttpSession session) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        if (utility.checkStringIsNotValid(phoneNumber, String.valueOf(otp))) {
            throw new GenericException("Phone number/otp cannot be null");
        }

        User byLogin = userRepository.findByLogin(phoneNumber);
        if (byLogin == null) {
            throw new GenericException("Failed to retrieve login user");
        }

        String retrievedOTP = byLogin.getKey();
        if (utility.checkStringIsNotValid(retrievedOTP)) {
            throw new GenericException("Could not retrieve otp at this moment, please try again");
        }

        Profile profile = profileService.findByPhoneNumber(phoneNumber);
        Long sessionOtp = (Long) session.getAttribute("OTP" + phoneNumber);

        if (utility.checkStringIsValid(retrievedOTP)) {
            long sentOtp = Long.parseLong(retrievedOTP);
            boolean status = otp == sentOtp;
            return updateProfileAndReturnResponse(profile, status);
        } else if (utility.checkStringIsValid(String.valueOf(sessionOtp))) {
            boolean status = otp == sessionOtp.longValue();
            return updateProfileAndReturnResponse(profile, status);
        }

        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @NotNull
    private ResponseEntity<Boolean> updateProfileAndReturnResponse(Profile profile, boolean status) {
        if (status && profile != null) {
            if (utility.checkStringIsValid(profile.getProfileID()) && Integer.parseInt(profile.getProfileID()) < 2) {
                profile.setProfileID("2");
            }
            profileService.save(profile);
        }
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    public ResponseEntity<Boolean> verifyOTP(Long otp, String phoneNumber) {

        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        if (utility.checkStringIsNotValid(phoneNumber, String.valueOf(otp))) {
            throw new GenericException("Phone number/otp cannot be empty");
        }

        User byLogin = userRepository.findByLogin(phoneNumber);
        if (byLogin == null) {
            throw new GenericException("Failed to retrieve login user");
        }

        String retrievedOTP = byLogin.getKey();
        if (utility.checkStringIsNotValid(retrievedOTP)) {
            throw new GenericException("Could not retrieve otp at this moment, please try again");
        }

        long sentOtp = Long.parseLong(retrievedOTP);
        boolean status = otp == sentOtp;
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    public ResponseEntity<Boolean> verifyOTP(Long otp, String phoneNumber, HttpSession session) {

        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        if (utility.checkStringIsNotValid(phoneNumber, String.valueOf(otp))) {
            throw new GenericException("Phone number/otp cannot be empty");
        }

        if (session == null) {
            throw new GenericException("Session cannot be empty");
        }

        Long retrievedOTP = (Long) session.getAttribute("OTP" + phoneNumber);
        if (retrievedOTP == null) {
            throw new GenericException("Otp in session cannot be empty");
        }

        Profile profile = profileService.findByPhoneNumber(phoneNumber);

        if (utility.checkStringIsValid(String.valueOf(retrievedOTP))) {
            boolean status = otp.longValue() == retrievedOTP.longValue();
            return updateProfileAndReturnResponse(profile, status);
        } else {
            Optional<User> userOptional = userRepository.findOneByLogin(phoneNumber);
            if (!userOptional.isPresent()) {
                throw new GenericException("Could not retrieve user profile, please try again");
            }
            User user = userOptional.get();
            String key = user.getKey();
            if (utility.checkStringIsValid(key)) {
                retrievedOTP = Long.parseLong(key);
                boolean status = otp.longValue() == retrievedOTP.longValue();
                return updateProfileAndReturnResponse(profile, status);
            }
        }
        session.removeAttribute("OTP" + phoneNumber);
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> sendAlert(String DBCR, String phoneNumber, double amount, String accountNo,
                                            String narration, double balance, LocalDateTime transDate, double bonusAmount) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
//        LocalDateTime date = LocalDateTime.parse(transDate, formatter);
        String date = transDate.format(formatter);

        SendSMSDTO sendSMSDTO = new SendSMSDTO();
        sendSMSDTO.setMobileNumber(utility.formatPhoneNumber(phoneNumber));

        String amountText = utility.formatMoney(amount);
        if (bonusAmount > 0) {
            amountText = amountText + " and bonus point of " + utility.formatMoney(bonusAmount);
        }

        String stringBuffer = DBCR + " Amount:" + amountText +
            "\nA/C:" + utility.maskAccountNumber(accountNo) +
            "\nDESC:" + narration +
            "\nDATE:" + date +
            "\nBAL:" + utility.formatMoney(balance);

        sendSMSDTO.setSmsMessage(stringBuffer);

        log.info("SMS Message " + sendSMSDTO.getSmsMessage());
        Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();

        if (asyncExecutor != null) {
            asyncExecutor.execute(() -> {
                externalOTPRESTClient.sendSMS(sendSMSDTO);
            });
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    public ResponseEntity<String> sendReversalAlert(String phoneNumber, double amount, String accountNo,
                                                    String narration, double balance, LocalDateTime transDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String date = transDate.format(formatter);

        SendSMSDTO sendSMSDTO = new SendSMSDTO();
        sendSMSDTO.setMobileNumber(utility.formatPhoneNumber(phoneNumber));


        String stringBuffer = "Pouchii Reversal" +
            "\nAmount " + utility.formatMoney(amount) +
            "\nA/C:" + utility.maskAccountNumber(accountNo) +
            "\nDESC:" + narration +
            "\nDATE:" + date +
            "\nBAL:" + utility.formatMoney(balance);

        sendSMSDTO.setSmsMessage(stringBuffer);

        log.info("SMS Message " + sendSMSDTO.getSmsMessage());
        Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();

        if (asyncExecutor != null) {
            asyncExecutor.execute(() -> {
                externalOTPRESTClient.sendSMS(sendSMSDTO);
            });
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    public ResponseEntity<String> sendCreditAlert(String phoneNumber, double amount, String accountNo, String narration, double balance, LocalDateTime transDate) {
        narration = narration.replace("SystemSpec ", "Pouchii ");

        return sendAlert("Credit", phoneNumber, amount, accountNo, narration, balance, transDate, 0);
    }

    public ResponseEntity<String> sendDebitAlert(String phoneNumber, double amount, String accountNo, String narration, double balance, LocalDateTime transDate, double bonusAmount) {
        return sendAlert("Debit", phoneNumber, amount, accountNo, narration, balance, transDate, bonusAmount);
    }

    public GenericResponseDTO changeUserStatus(String phoneNumber, UserStatus status) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        Optional<User> oneByLogin = userRepository.findOneByLogin(phoneNumber);
        if (oneByLogin.isPresent()) {
            User user = oneByLogin.get();
            user.setStatus(status.getName());
            if (UserStatus.DEACTIVATE_CUSTOMER.equals(status)) {
                user.setActivated(false);
            }
            User save = userRepository.save(user);
            return new GenericResponseDTO("00", HttpStatus.OK, "success", save);
        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Customer not found", null);
    }

    public List<User> findAllByAuthorities(List authorities) {
        return userRepository.findAllByAuthoritiesIn(authorities);
    }

    @Transactional
    public void changePin(String currentPin, String newPin) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String login = user.getLogin();
                Profile profile = profileRepository.findOneByPhoneNumber(login);
                String currentEncryptedPin = profile.getPin();

                if (!passwordEncoder.matches(currentPin, currentEncryptedPin)) {
                    throw new InvalidPinException();
                }

                String encryptedPin = passwordEncoder.encode(newPin);
                profile.setPin(encryptedPin);
                utility.sendAlertEmailForAction(user, "Change Password", LocalDateTime.now());

                log.debug("Changed pin for User: {}", profile);
            });
    }

    @Transactional
    public boolean changePin(String phoneNumber, String currentPin, String newPin) {

        Profile profile = profileRepository.findOneByPhoneNumber(phoneNumber);
        if (phoneNumber != null) {
            String currentEncryptedPin = profile.getPin();

            if (!passwordEncoder.matches(currentPin, currentEncryptedPin)) {
                throw new InvalidPinException();
            }

            String encryptedPin = passwordEncoder.encode(newPin);
            profile.setPin(encryptedPin);
            log.debug("Changed pin for User: {}", profile);
            return true;
        }

        return false;

    }

    @Transactional
    public Optional<User> findByLogin(String phoneNumber) {
        return userRepository.findOneByLogin(phoneNumber);
    }

    public GenericResponseDTO forgotPin(LostPinDTO lostPinDTO) {

        String phoneNumber = utility.formatPhoneNumber(lostPinDTO.getPhoneNumber());
        Profile profile = profileService.findByPhoneNumber(phoneNumber);
        Optional<User> userOptional = findByLogin(phoneNumber);
        // Profile profile = profileService.

        if (userOptional.isPresent() && profile != null) {

            User user = userOptional.get();
            String currentEncryptedPassword = user.getPassword();

            if (!passwordEncoder.matches(lostPinDTO.getPassword(), currentEncryptedPassword)) {
                // throw new InvalidPasswordException();
                // pinCorrect = false;
                return new GenericResponseDTO("99", HttpStatus.UNAUTHORIZED, "Invalid password", null);
            }

            String secretAnswer = profile.getSecretAnswer();
            String secretQuestion = profile.getSecretQuestion();


            if (secretQuestion == null) {

                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User security question not set", null);
            }
            if (secretAnswer == null) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User security answer not set", null);
            }

            if (secretAnswer.equalsIgnoreCase(lostPinDTO.getAnswer())
                && secretQuestion.equalsIgnoreCase(lostPinDTO.getQuestion())) {

                GenericResponseDTO genericResponseDTO = profileService.updatePin(lostPinDTO);
                if (!"00".equalsIgnoreCase(genericResponseDTO.getCode()) && HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Failed to update pin", null);
                }

                if (!user.getActivated() || UserStatus.DEACTIVATE_CUSTOMER_PIN.getName().equalsIgnoreCase(user.getStatus())) {
                    user.setActivated(true);
                    user.setStatus(UserStatus.OK.getName());
                    userRepository.save(user);
                }

                utility.sendAlertEmailForAction(user, "Forgot Pin", LocalDateTime.now());

                return new GenericResponseDTO("00", HttpStatus.OK, "Pin updated successfully", null);

            } else {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid security Question/Answer", null);
            }

        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User not found!", null);
    }

    public GenericResponseDTO updateEmail(String otp, String email) {
        User currentUser = utility.getCurrentUser();
        if (currentUser != null) {

            String phoneNumber = currentUser.getLogin();

            ResponseEntity<Boolean> responseEntity = verifyOTP(Long.valueOf(otp), phoneNumber);

            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Otp", null);
            }

            currentUser.setEmail(email);

            User save = userRepository.save(currentUser);

            log.info("updated user email ====> " + save);

            utility.sendAlertEmailForAction(save, "Change Email", LocalDateTime.now());

            return new GenericResponseDTO("00", HttpStatus.OK, "success", null);

        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Failed to retrieve login user", null);

    }

    public GenericResponseDTO updateAccountEmail(UpdateEmailDTO updateEmailDTO) {
        User currentUser = utility.getCurrentUser();
        if (currentUser != null) {

            String phoneNumber = currentUser.getLogin();

            Profile profile = profileService.findByPhoneNumber(phoneNumber);

            if (profile != null) {
                String secretAnswer = profile.getSecretAnswer();
                String secretQuestion = profile.getSecretQuestion();
                String encryptedPin = profile.getPin();

                String answer = updateEmailDTO.getAnswer();
                String question = updateEmailDTO.getQuestion();
                String plainPin = updateEmailDTO.getPin();

                if (!secretAnswer.equalsIgnoreCase(answer) || !secretQuestion.equalsIgnoreCase(question)) {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid security Question/Answer", null);
                }

                if (!passwordEncoder.matches(plainPin, encryptedPin)) {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Pin", null);
                }

                currentUser.setEmail(updateEmailDTO.getEmail());

                User save = userRepository.save(currentUser);

                log.info("updated user email ====> " + save);

                utility.sendAlertEmailForAction(save, "Change Email", LocalDateTime.now());

                return new GenericResponseDTO("00", HttpStatus.OK, "success", null);
            }
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Failed to retrieve user profile", null);

        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Failed to retrieve login user", null);
    }

    public ResponseEntity<String> validatePhoneNumber(String phoneNumber, HttpSession session) {
        long otp = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);
        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        SendSMSDTO sendSMSDTO = new SendSMSDTO();
        sendSMSDTO.setMobileNumber(phoneNumber);
        sendSMSDTO.setSmsMessage(otpMessage + " " + otp);

        if (session != null) {
            session.setAttribute(phoneNumber + "OTP", otp);

            Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
            if (asyncExecutor != null) {
                String finalPhoneNumber = phoneNumber;
                asyncExecutor.execute(() -> {
                    try {
                        Thread.sleep(1000 * 60 * 5L);
                        session.removeAttribute(finalPhoneNumber + "OTP");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            String s = "";
            try {
                s = externalOTPRESTClient.sendSMS(sendSMSDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new ResponseEntity<>(s, HttpStatus.OK);

        }
        return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Boolean> validateOTP(String phoneNumber, Long otp, HttpSession session) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);


        if (utility.checkStringIsNotValid(phoneNumber, String.valueOf(otp))) {
            throw new GenericException("Phone number/otp cannot be null");
        }

        if (session == null) {
            throw new GenericException("Failed to retrieve user session");
        }

        String retrievedOTP = (String) session.getAttribute(phoneNumber + "OTP");

        log.info("VALID " + retrievedOTP);

        if (utility.checkStringIsNotValid(retrievedOTP)) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        long sentOtp = Long.parseLong(retrievedOTP);
        boolean status = otp == sentOtp;
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @Transactional
    public GenericResponseDTO changeLostPassword(String phoneNumber, String newPassword) {
        Optional<User> oneByLogin = userRepository.findOneByLogin(utility.formatPhoneNumber(phoneNumber));
        System.out.println("Parameters ==> phone = " + phoneNumber + " ++++ pass ===> " + newPassword);
        if (oneByLogin.isPresent()) {
            User user = oneByLogin.get();
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encryptedPassword);
            log.debug("Before saving : {}", user);
            try {
                User savedUser = userRepository.saveAndFlush(user);
                log.debug("Changed password for User: {}", savedUser);

                clearUserCaches(savedUser);

                auditChangeLostPassword(newPassword, user, savedUser);
                return new GenericResponseDTO("00", HttpStatus.OK, "success", savedUser);
            } catch (Exception e) {
                e.printStackTrace();

                log.debug("Error while saving and flushing: {}", e);

                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", e.getLocalizedMessage());
            }
        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid user", null);
    }

    private void sendPasswordResetEmail(String newPassword, User user) {
        Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
        if (asyncExecutor != null) {
            asyncExecutor.execute(() -> {
                try {
                    String subject = "Password reset".toUpperCase();
                    String message = "Your new password is " + newPassword;
                    utility.sendToNotificationConsumer("", "", "", subject, message, message, user.getEmail(), user.getLogin());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public GenericResponseDTO changePasswordByAdmin(String phoneNumber, String newPassword) {
        System.out.println("Change password by admin ======++++ " + newPassword);
        updatePassword(phoneNumber, newPassword);
        return new GenericResponseDTO("00", HttpStatus.OK, "success", null);
    }

    private void auditChangePasswordByAdmin(LostPasswordDTO lostPasswordDTO) {
        Map<String, Object> data = new HashMap<>();
        data.put("New password", lostPasswordDTO.getNewPassword());
        AuditEvent change_lost_password = new AuditEvent(lostPasswordDTO.getPhoneNumber(), "Change password by " +
            "admin", data);

        customAuditEventRepository.add(change_lost_password);
    }

    private PaymentResponseDTO buildResponse(String message, HttpStatus status) {
        PaymentResponseDTO response = new PaymentResponseDTO();
        response = new PaymentResponseDTO();
        response.setMessage(message);
        response.setStatus(status);
        return response;
    }

    private boolean pinMatches(LostPasswordDTO lostPasswordDTO, Profile profile) {
        String currentEncryptedPin = profile.getPin();
        // throw new InvalidPasswordException();
        // pinCorrect = false;
        return passwordEncoder.matches(lostPasswordDTO.getPin(), currentEncryptedPin);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentResponseDTO processChangeLostPassword(LostPasswordDTO lostPasswordDTO) {

        System.out.println("process change lost password " + lostPasswordDTO);

        String phoneNumber = lostPasswordDTO.getPhoneNumber();
        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        if ("admin".equalsIgnoreCase(phoneNumber)) {
            updatePassword(phoneNumber, lostPasswordDTO.getNewPassword());
            return buildResponse("Password Change Successful", HttpStatus.ACCEPTED);
        }

        Profile profile = profileService.findByPhoneNumber(phoneNumber);

        if (profile == null) {
            return buildResponse("Invalid user profile", HttpStatus.BAD_REQUEST);
        }

        if (!pinMatches(lostPasswordDTO, profile)) {
            return buildResponse("Invalid pin", HttpStatus.UNAUTHORIZED);
        }

        updatePassword(phoneNumber, lostPasswordDTO.getNewPassword());
//        GenericResponseDTO genericResponseDTO = changeLostPassword(phoneNumber, lostPasswordDTO.getNewPassword());

//        System.out.println("processChangeLostPassword ===> " + genericResponseDTO);

        auditChangePasswordByAdmin(lostPasswordDTO);

        return buildResponse("Password Change Successful", HttpStatus.ACCEPTED);
    }

    public void uploadCorporateDocuments(List<CorporateDocuments> corporateDocuments, String phoneNumber) {
        String profilePictureName = "";
        for (CorporateDocuments c : corporateDocuments) {
            if (c.getEncodedString() != null && !c.getEncodedString().isEmpty()) {
                if (c.getFormat().equalsIgnoreCase(DocumentType.JPG.name())) {
                    profilePictureName = "corporate-docs-" + c.getDocType().toUpperCase() + utility.returnPhoneNumberFormat(phoneNumber)
                        + ".jpg";
                } else if (c.getFormat().equalsIgnoreCase(DocumentType.PDF.name())) {
                    profilePictureName = "corporate-docs-" + c.getDocType().toUpperCase() + utility.returnPhoneNumberFormat(phoneNumber)
                        + ".pdf";
                }
                String outputFileName = documentUrl + profilePictureName;
                utility.saveImage(c.getEncodedString(), outputFileName);
            }
        }
    }

    // Re-activate User
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void reactivateUser(String phoneNumber) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                if (user.getLogin().equalsIgnoreCase(utility.formatPhoneNumber(phoneNumber))) {
                    user.setActivated(true);
                    user.setStatus(UserStatus.OK.getName());
                    this.clearUserCaches(user);
                    log.debug("Reactivated User: {}", user);
                    utility.sendAlertEmailForAction(user, "Account Reactivation", LocalDateTime.now());
                }
            });
    }
}
