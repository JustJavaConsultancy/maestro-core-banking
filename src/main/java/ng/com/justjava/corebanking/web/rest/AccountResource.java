package ng.com.justjava.corebanking.web.rest;

import com.google.zxing.WriterException;
import io.github.jhipster.web.util.HeaderUtil;
import ng.com.justjava.corebanking.client.ExternalOTPRESTClient;
import ng.com.justjava.corebanking.config.AsyncConfiguration;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.repository.UserRepository;
import ng.com.justjava.corebanking.security.SecurityUtils;
import ng.com.justjava.corebanking.security.jwt.JWTFilter;
import ng.com.justjava.corebanking.security.jwt.TokenProvider;
import ng.com.justjava.corebanking.service.exception.CorporateAlreadyExistsException;
import ng.com.justjava.corebanking.util.QRCodeGenerator;
import ng.com.justjava.corebanking.util.Utility;
import ng.com.justjava.corebanking.web.rest.errors.InvalidPasswordException;
import ng.com.justjava.corebanking.web.rest.errors.LoginAlreadyUsedException;
import ng.com.justjava.corebanking.web.rest.vm.KeyAndPasswordVM;
import ng.com.justjava.corebanking.web.rest.vm.ManagedUserVM;
import ng.com.justjava.corebanking.service.BankService;
import ng.com.justjava.corebanking.service.MailService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.UserService;
import ng.com.justjava.corebanking.service.UsernameAlreadyUsedException;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.BankDTO;
import ng.com.justjava.corebanking.service.dto.ChangePhonenumberDTO;
import ng.com.justjava.corebanking.service.dto.ChangePinDTO;
import ng.com.justjava.corebanking.service.dto.GenerateQrDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.LoginVM;
import ng.com.justjava.corebanking.service.dto.LostPasswordDTO;
import ng.com.justjava.corebanking.service.dto.LostPinDTO;
import ng.com.justjava.corebanking.service.dto.PasswordChangeDTO;
import ng.com.justjava.corebanking.service.dto.PaymentResponseDTO;
import ng.com.justjava.corebanking.service.dto.RegisteredCorporateUserDTO;
import ng.com.justjava.corebanking.service.dto.RegisteredUserDTO;
import ng.com.justjava.corebanking.service.dto.RespondDTO;
import ng.com.justjava.corebanking.service.dto.SendSMSDTO;
import ng.com.justjava.corebanking.service.dto.UpdateEmailDTO;
import ng.com.justjava.corebanking.service.dto.UserDTO;
import ng.com.justjava.corebanking.service.dto.ValidateSecretDTO;
import ng.com.justjava.corebanking.service.dto.WalletExternalDTO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.ap.internal.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final TokenProvider tokenProvider;

    @Value("${app.sms.message.otp}")
    private String smsMessage;

    @Value("${app.document-url}")
    private String documentUrl;

    private String HealthStack_Scheme = "4865616c7468737461636b";


    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private static final long Lower_Bond = 100000L;
    private static final long Upper_Bond = 900000L;

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;
    private final Utility utility;
    private final BankService bankService;

    private final MailService mailService;
    private final ProfileService profileService;
    private final AsyncConfiguration asyncConfiguration;
    private final ExternalOTPRESTClient externalOTPRESTClient;
    private final PasswordEncoder passwordEncoder;
    private final QRCodeGenerator qrCodeGenerator;
    private final WalletAccountService walletAccountService;

    public AccountResource(UserRepository userRepository, UserService userService,
                           MailService mailService,
                           ProfileService profileService, TokenProvider tokenProvider,
                           AuthenticationManagerBuilder authenticationManagerBuilder,
                           Utility utility, BankService bankService, AsyncConfiguration asyncConfiguration, ExternalOTPRESTClient externalOTPRESTClient, PasswordEncoder passwordEncoder, QRCodeGenerator qrCodeGenerator, WalletAccountService walletAccountService) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.profileService = profileService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.utility = utility;
        this.bankService = bankService;
        this.asyncConfiguration = asyncConfiguration;
        this.externalOTPRESTClient = externalOTPRESTClient;
        this.passwordEncoder = passwordEncoder;
        this.qrCodeGenerator = qrCodeGenerator;
        this.walletAccountService = walletAccountService;
    }

	/**
	 * {@code POST  /register} : register the user.
	 *
	 * @throws ng.com.justjava.corebanking.web.rest.errors.InvalidPasswordException  {@code 400 (Bad Request)} if the password
	 *                                   is incorrect.
	 * @throws ng.com.justjava.corebanking.web.rest.errors.EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is
	 *                                   already used.
	 * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is
	 *                                   already used.
	 */
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<RespondDTO<User>> registerAccount(@Valid @RequestBody RegisteredUserDTO registeredUserDTO,
                                                            HttpSession session, @RequestParam Optional<String> scheme) {
        User user = null;

        RespondDTO<User> RespondDTO = new RespondDTO<User>();
        RespondDTO.setMessage("User Created Successfully");
        if (Strings.isEmpty(registeredUserDTO.getPhoneNumber())) {
            RespondDTO.setMessage("Phone Number Cannot be Empty");
            return new ResponseEntity<>(RespondDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
        if (registeredUserDTO.getPhoneNumber().length() < 11 || registeredUserDTO.getPhoneNumber().length() > 15) {
            RespondDTO.setMessage("Phone number must be between 11 to 13 digits");
            return new ResponseEntity<>(RespondDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

        String s = utility.formatPhoneNumber(registeredUserDTO.getPhoneNumber());
        registeredUserDTO.setPhoneNumber(s);

        if (!checkPasswordLength(registeredUserDTO.getPassword())) {
            RespondDTO.setMessage("Password Cannot be Empty");
            return new ResponseEntity<>(RespondDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
        try {
            user = userService.registerUser(registeredUserDTO, registeredUserDTO.getPassword());
            RespondDTO.setUser(user);
        } catch (UsernameAlreadyUsedException e) {
            RespondDTO.setMessage("User Already Exist");
            return new ResponseEntity<>(RespondDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            registeredUserDTO.getPhoneNumber(), registeredUserDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = true;
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        RespondDTO.setToken(jwt);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        session.setAttribute("phoneNumber", registeredUserDTO.getPhoneNumber());
        session.setAttribute("jwt", jwt);

        long otp = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);

        session.setAttribute("otp", String.valueOf(otp));

        if (user != null) {
            user.setKey(String.valueOf(otp));
            userRepository.saveAndFlush(user);
        }

        SendSMSDTO sendSMSDTO = new SendSMSDTO();
        sendSMSDTO.setMobileNumber(registeredUserDTO.getPhoneNumber());

        if (scheme.isPresent()){
            if (HealthStack_Scheme.equals(scheme.get())){
                sendSMSDTO.setSmsMessage(smsMessage + " " + otp + ". Use this link to complete your Health Stack registration.");
            }
        }else{
            sendSMSDTO.setSmsMessage(smsMessage + " " + otp);
        }

        try {
            externalOTPRESTClient.sendSMS(sendSMSDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(RespondDTO, httpHeaders, HttpStatus.OK);
    }

    /**
     * {@code POST  /register/nin} : register the user using nin.
     *
     * @throws ng.com.justjava.corebanking.web.rest.errors.InvalidPasswordException  {@code 400 (Bad Request)} if the password
     *                                   is incorrect.
     * @throws ng.com.justjava.corebanking.web.rest.errors.EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is
     *                                   already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is
     *                                   already used.
     */
    @PostMapping("/register/nin")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RespondDTO<User>> registerNinAccount(@Valid @RequestBody RegisteredUserDTO registeredUserDTO,
                                                               HttpSession session) {
        User user = null;

        RespondDTO<User> RespondDTO = new RespondDTO<User>();
        RespondDTO.setMessage("User Created Successfully");
        if (Strings.isEmpty(registeredUserDTO.getPhoneNumber())) {
            RespondDTO.setMessage("Phone Number Cannot be Empty");
            return new ResponseEntity<>(RespondDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
        if (registeredUserDTO.getPhoneNumber().length() < 11 || registeredUserDTO.getPhoneNumber().length() > 15) {
            RespondDTO.setMessage("Phone number must be between 11 to 13 digits");
            return new ResponseEntity<>(RespondDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

        String s = utility.formatPhoneNumber(registeredUserDTO.getPhoneNumber());
        registeredUserDTO.setPhoneNumber(s);

        if (!checkPasswordLength(registeredUserDTO.getPassword())) {
            RespondDTO.setMessage("Password Cannot be Empty");
            return new ResponseEntity<>(RespondDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
        try {
            user = userService.registerUser(registeredUserDTO, registeredUserDTO.getPassword());
            RespondDTO.setUser(user);
        } catch (UsernameAlreadyUsedException e) {
//            RespondDTO.setMessage("User Already Exist");
//           return new ResponseEntity<>(RespondDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);

            user = userRepository.findByLogin(utility.formatPhoneNumber(registeredUserDTO.getPhoneNumber()));
            RespondDTO.setUser(user);

        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            registeredUserDTO.getPhoneNumber(), registeredUserDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = true;
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        RespondDTO.setToken(jwt);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        session.setAttribute("phoneNumber", registeredUserDTO.getPhoneNumber());

        return new ResponseEntity<>(RespondDTO, httpHeaders, HttpStatus.OK);
    }


    /**
     * {@code POST  /register/corporate} : Register Corporate User.
     *
     * @throws ng.com.justjava.corebanking.web.rest.errors.InvalidPasswordException  {@code 400 (Bad Request)} if the password
     *                                   is incorrect.
     * @throws ng.com.justjava.corebanking.web.rest.errors.EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is
     *                                   already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is
     *                                   already used.
     */

    @PostMapping("/register/corporate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RespondDTO<User>> registerCorporateAccount(@Valid @RequestBody RegisteredCorporateUserDTO registeredUserDTO,
                                                            HttpSession session) {
        User user = null;
        RespondDTO<User> RespondDTO = new RespondDTO<User>();
        RespondDTO.setMessage("User Created Successfully");
        if (Strings.isEmpty(registeredUserDTO.getPhoneNumber())) {
            RespondDTO.setMessage("Phone Number Cannot be Empty");
            return new ResponseEntity<>(RespondDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
        if (registeredUserDTO.getPhoneNumber().length() < 11 || registeredUserDTO.getPhoneNumber().length() > 15) {
            RespondDTO.setMessage("Phone number must be between 11 to 13 digits");
            return new ResponseEntity<>(RespondDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }

        String s = utility.formatPhoneNumber(registeredUserDTO.getPhoneNumber());
        registeredUserDTO.setPhoneNumber(s);

        if (!checkPasswordLength(registeredUserDTO.getPassword())) {
            RespondDTO.setMessage("Password Cannot be Empty");
            return new ResponseEntity<>(RespondDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }
        try {
            user = userService.registerCorporateUser(registeredUserDTO, registeredUserDTO.getPassword());
            RespondDTO.setUser(user);
        } catch (UsernameAlreadyUsedException e) {
            RespondDTO.setMessage("User Already Exist");
            return new ResponseEntity<>(RespondDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
        }catch (CorporateAlreadyExistsException ex){
            RespondDTO.setMessage(ex.getMessage());
            return new ResponseEntity<>(RespondDTO, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);

        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            registeredUserDTO.getPhoneNumber(), registeredUserDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = true;
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        RespondDTO.setToken(jwt);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        session.setAttribute("phoneNumber", registeredUserDTO.getPhoneNumber());
        session.setAttribute("jwt", jwt);

        long otp = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);
        SendSMSDTO sendSMSDTO = new SendSMSDTO();
        sendSMSDTO.setMobileNumber(registeredUserDTO.getPhoneNumber());
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
        }

        return new ResponseEntity<>(RespondDTO, httpHeaders, HttpStatus.OK);
    }

    @PostMapping(path = "/register/corporate/wallet")
    public ResponseEntity<GenericResponseDTO> registerCorporateWithWallet(@Valid @RequestBody WalletExternalDTO walletExternalDTO,
                                                                          HttpSession session) {
        return walletAccountService.createCorporateWalletExternal(walletExternalDTO, session);
    }

    @PostMapping("/users/{username}/{password}")
    public ResponseEntity<User> createUserWithoutProfile(@PathVariable String username, @PathVariable String password) throws URISyntaxException {
        User newUser = userService.createUserWithoutProfile(username, password);
        return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
            .headers(HeaderUtil.createAlert("ApiGatewayApp", "userManagement.created", newUser.getLogin()))
            .body(newUser);
    }


    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user
     *                          couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return
	 * its login.
	 *
	 * @param request the HTTP request.
	 * @return the login if the user is authenticated.
	 */
	@GetMapping("/authenticate")
	public String isAuthenticated(HttpServletRequest request) {
		log.debug("REST request to check if the current user is authenticated");
		System.out.println("This is AUTHENTICATION");
		return request.getRemoteUser();
	}

	/**
	 * {@code GET  /account} : get the current user.
	 *
	 * @return the current user.
	 * @throws RuntimeException {@code 500 (Internal Server Error)} if the user
	 *                          couldn't be returned.
	 */
	@GetMapping("/account")
	public UserDTO getAccount() {
		return userService.getUserWithAuthorities().map(UserDTO::new)
				.orElseThrow(() -> new AccountResourceException("User could not be found"));
	}

	/**
	 * {@code POST  /account} : update the current user information.
	 *
	 * @param userDTO the current user information.
	 * @throws ng.com.justjava.corebanking.web.rest.errors.EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is
	 *                                   already used.
	 * @throws RuntimeException          {@code 500 (Internal Server Error)} if the
	 *                                   user login wasn't found.
	 */
	@PostMapping("/account")
	public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
		String userLogin = SecurityUtils.getCurrentUserLogin()
				.orElseThrow(() -> new AccountResourceException("Current user login not found"));
		Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
		if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new ng.com.justjava.corebanking.web.rest.errors.EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getLangKey(),
            userDTO.getImageUrl());
    }

    private static boolean checkPinLength(String pin) {
        return !StringUtils.isEmpty(pin) && pin.length() == 4;
    }

    @PostMapping(path = "/changelostpassword")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<PaymentResponseDTO> changeLostPassword(@RequestBody LostPasswordDTO lostPasswordDTO) {
        log.info("Rest request to forgot and change user password");
        userService.updatePassword(lostPasswordDTO.getPhoneNumber(),
            lostPasswordDTO.getNewPassword());

        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setStatus(HttpStatus.OK);
        response.setMessage("Password updated successfully");
        response.setCode("00");

        System.out.println("response payment =====> " + response);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping(path = "/forgot/password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<GenericResponseDTO> forgotPassword(@RequestBody LostPasswordDTO lostPasswordDTO) {
        log.info("Rest request to forgot and change user password");
        GenericResponseDTO response = userService.forgotPassword(lostPasswordDTO);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws ng.com.justjava.corebanking.web.rest.errors.InvalidPasswordException {@code 400 (Bad Request)} if the new
     *                                  password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<GenericResponseDTO> changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {

        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new ng.com.justjava.corebanking.web.rest.errors.InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success"), HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/account/reactivate/{phoneNumber}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<GenericResponseDTO> reactivateUser(@PathVariable String phoneNumber) {
        userService.reactivateUser(phoneNumber);
        return new ResponseEntity<>(new GenericResponseDTO("00", "success"), HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/account/update-pin")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<GenericResponseDTO> updatePin(@RequestBody LostPinDTO lostPinDTO) {

        GenericResponseDTO genericResponseDTO = profileService.updatePin(lostPinDTO);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());

    }

    @PostMapping(path = "/account/forgot-pin")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<GenericResponseDTO> forgotPin(@RequestBody LostPinDTO lostPinDTO) {

        GenericResponseDTO genericResponseDTO = userService.forgotPin(lostPinDTO);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }


    /**
     * {@code POST  /account/change-pin} : changes the current user's password.
     *
     * @param changePinDTO current and new pin.
     * @throws ng.com.justjava.corebanking.web.rest.errors.InvalidPasswordException {@code 400 (Bad Request)} if the new
     *                                  password is incorrect.
     */
    @PostMapping(path = "/account/change-pin")
    public ResponseEntity<GenericResponseDTO> changePin(@RequestBody ChangePinDTO changePinDTO) {
        if (!checkPinLength(changePinDTO.getNewPin())) {
            return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid pin length"), HttpStatus.BAD_REQUEST);
        }

        userService.changePin(changePinDTO.getCurrentPin(), changePinDTO.getNewPin());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), HttpStatus.ACCEPTED);
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the
     * password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
		if (user.isPresent()) {
			mailService.sendPasswordResetMail(user.get());
		} else {
			// Pretend the request has been successful to prevent checking which emails
			// really exist
			// but log that an invalid attempt has been made
			log.warn("Password reset requested for non existing mail");
		}
	}

	/**
	 * {@code POST   /account/reset-password/finish} : Finish to reset the password
	 * of the user.
	 *
	 * @param keyAndPassword the generated key and the new password.
	 * @throws ng.com.justjava.corebanking.web.rest.errors.InvalidPasswordException {@code 400 (Bad Request)} if the password is
	 *                                  incorrect.
	 * @throws RuntimeException         {@code 500 (Internal Server Error)} if the
	 *                                  password could not be reset.
	 */
	@PostMapping(path = "/account/reset-password/finish")
	public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
		if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
			throw new InvalidPasswordException();
		}
		Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(),
            keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) && password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH
            && password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }

    @GetMapping(path = "/valid-banks/{accountNumber}")
    public ResponseEntity<GenericResponseDTO> getValidBanks(@PathVariable("accountNumber") String accountNumber, Pageable pageable) {
        Page<BankDTO> allRegularBanks = bankService.findAllRegularBanks(pageable);
        List<BankDTO> validBanks = bankService.getValidBanks(allRegularBanks.toList(), accountNumber);
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", validBanks), HttpStatus.OK);
    }

    @PostMapping("/account/validate/pin")
    public ResponseEntity<GenericResponseDTO> validateUserPin(@RequestBody LostPasswordDTO lostPasswordDTO) {

        log.debug("REST request to validate user secret answer");

        GenericResponseDTO genericResponseDTO = profileService.validateUserPin(lostPasswordDTO);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/account/validate/password")
    public ResponseEntity<GenericResponseDTO> validateUserPassword(@RequestBody LoginVM loginVM) {

        log.debug("REST request to validate user secret answer");

        GenericResponseDTO genericResponseDTO = userService.validateUserPassword(loginVM);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/account/validate/secret-question")
    public ResponseEntity<GenericResponseDTO> validateSecurityAnswer(@RequestBody ValidateSecretDTO validateSecretDTO) {

        log.debug("REST request to validate user secret answer");

        GenericResponseDTO genericResponseDTO = profileService.validateSecurityAnswer(validateSecretDTO);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/account/update/secret-question")
    public ResponseEntity<GenericResponseDTO> updateSecurityQuestion(@RequestBody ValidateSecretDTO validateSecretDTO, @RequestParam Optional<Boolean> flag) {

        log.debug("REST request to validate user secret answer");
        if(flag.isPresent()&&flag.get()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), headers,
                HttpStatus.OK);
        }
        GenericResponseDTO genericResponseDTO = profileService.updateSecurityQuestion(validateSecretDTO);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/account/update/hm/secret-question")
    public ResponseEntity<GenericResponseDTO> hmUpdateSecurityQuestion(@RequestBody ValidateSecretDTO validateSecretDTO, @RequestParam Optional<Boolean> flag) {

        log.debug("REST request to validate user secret answer");
        if(flag.isPresent()&&flag.get()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), headers,
                HttpStatus.OK);
        }
        GenericResponseDTO genericResponseDTO = profileService.hmUpdateSecurityQuestion(validateSecretDTO);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/account/update/email/{otp}/{email}")
    public ResponseEntity<GenericResponseDTO> updateEmail(@PathVariable String otp, @PathVariable String email, @RequestParam Optional<Boolean> flag) {
        if(flag.isPresent()&&flag.get()) {
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", null), headers,
                HttpStatus.OK);
        }
        GenericResponseDTO genericResponseDTO = userService.updateEmail(otp, email);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("account/update/email")
    public ResponseEntity<GenericResponseDTO> updateAccountEmail(@RequestBody @Valid UpdateEmailDTO updateEmailDTO) {

        log.debug("REST request to validate user secret answer");

        GenericResponseDTO genericResponseDTO = userService.updateAccountEmail(updateEmailDTO);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }


    @PostMapping("/account/nin/{nin}")
    public ResponseEntity<GenericResponseDTO> validateNin(@PathVariable String nin, HttpSession session) {

        log.debug("REST request to validate user secret answer");

        GenericResponseDTO genericResponseDTO = profileService.validateNin(nin, session);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }


    @PostMapping("/account/nin/{nin}/{otp}")
    public ResponseEntity<GenericResponseDTO> retrieveNINDetails(@PathVariable String nin, @PathVariable String otp, HttpSession session) {

        log.debug("REST request to validate user secret answer");

        GenericResponseDTO genericResponseDTO = profileService.retrieveNINDetails(nin, otp, session);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/admin/password/change")
    public ResponseEntity<GenericResponseDTO> changeCustomerPassword(@RequestBody LostPasswordDTO lostPasswordDTO) {

        log.debug("REST request to change customer password by Admin,{}", lostPasswordDTO);

        GenericResponseDTO genericResponseDTO = userService.changePasswordByAdmin(utility.formatPhoneNumber(lostPasswordDTO.getPhoneNumber()), lostPasswordDTO.getNewPassword());

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/manage-user-statues/{phoneNumber}/{status}")
    public ResponseEntity<GenericResponseDTO> deactivateUser(@PathVariable String phoneNumber, @PathVariable String status, @RequestParam Optional<String> accountNumber) {

        log.debug("REST request to deactivate customer by Admin,{}", phoneNumber);

        GenericResponseDTO genericResponseDTO = walletAccountService.managerUserStatus(phoneNumber, status, accountNumber);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/admin/phonenumber/change")
    public ResponseEntity<GenericResponseDTO> changeCustomerPhonenumber(@RequestBody ChangePhonenumberDTO changePhonenumberDTO) {

        log.debug("REST request to change customer password by Admin,{}", changePhonenumberDTO);

        GenericResponseDTO genericResponseDTO = userService.changePhonenumberByAdmin(utility.formatPhoneNumber(changePhonenumberDTO.getOldPhoneNumber()), utility.formatPhoneNumber(changePhonenumberDTO.getNewPhoneNumber()));

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/admin/generate-merchant-qr")
    public ResponseEntity<GenericResponseDTO> generateQRCode(@RequestBody GenerateQrDTO generateQrDTO) {
        log.debug("REST request to generate QR Code{}", generateQrDTO);
        byte[] image = new byte[0];
        String phoneNumber = generateQrDTO.getPhoneNo();
        phoneNumber = utility.returnPhoneNumberFormat(phoneNumber);
        String accountNo = generateQrDTO.getAccountNo().trim();
        String merchantName = generateQrDTO.getMerchantName().trim();

        String qrTxt = phoneNumber+"//"+accountNo+"//"+merchantName;

        if(phoneNumber.isEmpty()||phoneNumber==null) {return ResponseEntity.badRequest().body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Phone Number is null/empty!", null));}
        if(accountNo.isEmpty()||accountNo==null) {return ResponseEntity.badRequest().body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Account Number is null/empty!", null));}
        if(merchantName.isEmpty()||merchantName==null) {return ResponseEntity.badRequest().body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Merchant Name is null/empty!", null));}

        String filename = "merchant-qr-" + accountNo + ".jpg";
        String pathname = documentUrl + filename;
        try {
            File file = new File(pathname);
            if(!file.exists()) {
                return ResponseEntity.ok(new GenericResponseDTO("00", HttpStatus.OK, "QR Code Generated successfully!", generateQR(qrTxt,pathname)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Error in generating QR Code!", null));
        }
        return ResponseEntity.ok(new GenericResponseDTO("00", HttpStatus.OK, "QR Code Generated successfully!", getWalletQRCode(pathname)));
    }

    @PostMapping("/document/get-wallet-qr-code")
    public ResponseEntity<Object> getWalletQRCode(@RequestBody GenerateQrDTO generateQrDTO) {
        log.debug("REST request to generate QR Code{}", generateQrDTO);

        if(generateQrDTO.getMerchantName()==null||generateQrDTO.getMerchantName().trim().isEmpty()) {return ResponseEntity.badRequest().body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Merchant Name is null/empty!", null));}
        if(generateQrDTO.getPhoneNo()==null||generateQrDTO.getPhoneNo().isEmpty()) {return ResponseEntity.badRequest().body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Phone Number is null/empty!", null));}
        if(generateQrDTO.getAccountNo()==null||generateQrDTO.getAccountNo().trim().isEmpty()) {return ResponseEntity.badRequest().body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Account Number is null/empty!", null));}

        String phoneNumber = generateQrDTO.getPhoneNo();
        phoneNumber = utility.returnPhoneNumberFormat(phoneNumber);
        String accountNo = generateQrDTO.getAccountNo().trim();
        String merchantName = generateQrDTO.getMerchantName().trim();

        String qrTxt = phoneNumber+"//"+accountNo+"//"+merchantName;


        String filename = "merchant-qr-" + accountNo + ".jpg";
        String pathname = documentUrl + filename;
        try {
            File file = new File(pathname);
            if(!file.exists()) {
                log.debug("Generating New QR for {}", generateQrDTO);
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(generateQR(qrTxt,pathname));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Error in generating QR Code!", null));
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(getWalletQRCode(pathname));
    }

    @GetMapping("/documents/qr/{accountNo}")
    public ResponseEntity<byte[]> getQRImage(@PathVariable String accountNo) {
        byte[] image = new byte[0];
        String filename = "merchant-qr-" + accountNo.trim() + ".jpg";
        String pathname = documentUrl + filename;
        try {
            File file = new File(pathname);
            image = FileUtils.readFileToByteArray(file);
            log.debug("Image Pathname : " + pathname);
            log.debug("Image Byte : " + image);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(image);
        }
    }

    public byte[] generateQR(String qrTxt, String pathname){
        try {
            byte[] image = new byte[0];
            qrCodeGenerator.generateQRCodeImage(qrTxt, 250, 250, pathname);
            File file = new File(pathname);
            image = FileUtils.readFileToByteArray(file);
            log.debug("Image Pathname : " + pathname);
            log.debug("Image Byte : " + image);
            return image;
        } catch (IOException | WriterException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public byte[] getWalletQRCode(String pathname){
        try {
            byte[] image = new byte[0];
            File file = new File(pathname);
            image = FileUtils.readFileToByteArray(file);
            log.debug("Image Pathname : " + pathname);
            log.debug("Image Byte : " + image);
            return image;

        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
