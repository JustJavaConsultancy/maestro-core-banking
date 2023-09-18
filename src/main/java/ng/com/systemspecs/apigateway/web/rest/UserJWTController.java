package ng.com.systemspecs.apigateway.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import ng.com.systemspecs.apigateway.config.AsyncConfiguration;
import ng.com.systemspecs.apigateway.domain.Address;
import ng.com.systemspecs.apigateway.domain.Agent;
import ng.com.systemspecs.apigateway.domain.MyDevice;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.Scheme;
import ng.com.systemspecs.apigateway.domain.SuperAgent;
import ng.com.systemspecs.apigateway.domain.User;
import ng.com.systemspecs.apigateway.domain.WalletAccount;
import ng.com.systemspecs.apigateway.domain.enumeration.DeviceStatus;
import ng.com.systemspecs.apigateway.domain.enumeration.UserStatus;
import ng.com.systemspecs.apigateway.domain.enumeration.UserType;
import ng.com.systemspecs.apigateway.repository.AddressRepository;
import ng.com.systemspecs.apigateway.repository.UserRepository;
import ng.com.systemspecs.apigateway.security.DomainUserDetailsService;
import ng.com.systemspecs.apigateway.security.jwt.JWTFilter;
import ng.com.systemspecs.apigateway.security.jwt.TokenProvider;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.IpgResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.LoginVM;
import ng.com.systemspecs.apigateway.service.dto.RespondDTO;
import ng.com.systemspecs.apigateway.service.dto.TellerDTO;
import ng.com.systemspecs.apigateway.service.dto.WalletAccountDTO;
import ng.com.systemspecs.apigateway.service.mapper.ProfileMapper;
import ng.com.systemspecs.apigateway.service.mapper.WalletAccountMapper;
import ng.com.systemspecs.apigateway.util.Utility;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static java.lang.System.out;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;
    private final ProfileService profileService;
    private final AgentService agentService;
    private final SuperAgentService superAgentService;
    private final WalletAccountService walletAccountService;
    private final WalletAccountMapper walletAccountMapper;
    private final TellerService tellerService;
    private final ProfileMapper profileMapper;
    private final AsyncConfiguration asyncConfiguration;
    private final AddressRepository addressRepostory;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final Utility utility;
    private final DomainUserDetailsService domainUserDetailsService;
    private final MyDeviceService myDeviceService;
    private final PolarisVulteService polarisVulteService;
    private final SchemeService schemeService;
    private final UserService userService;


    @Value("${app.scheme.systemspecs}")
    private String SYSTEMSPECS_SCHEME;

    @Value("${app.scheme.ibile}")
    private String IBILE_SCHEME;


    public UserJWTController(TokenProvider tokenProvider, ProfileService
        profileService, AgentService agentService, SuperAgentService superAgentService, WalletAccountService walletAccountService, WalletAccountMapper walletAccountMapper, TellerService tellerService, ProfileMapper profileMapper, AsyncConfiguration asyncConfiguration, AddressRepository addressService,
                             AuthenticationManagerBuilder authenticationManagerBuilder,
                             UserRepository userRepository, Utility utility,
                             DomainUserDetailsService domainUserDetailsService, MyDeviceService myDeviceService, PolarisVulteService polarisVulteService, SchemeService schemeService, UserService userService) {

        this.tokenProvider = tokenProvider;
        this.agentService = agentService;
        this.superAgentService = superAgentService;
        this.walletAccountService = walletAccountService;
        this.walletAccountMapper = walletAccountMapper;
        this.tellerService = tellerService;
        this.asyncConfiguration = asyncConfiguration;
        this.addressRepostory = addressService;
        this.profileMapper = profileMapper;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.profileService = profileService;
        this.userRepository = userRepository;
        this.utility = utility;

        this.domainUserDetailsService = domainUserDetailsService;
        this.myDeviceService = myDeviceService;
        this.polarisVulteService = polarisVulteService;
        this.schemeService = schemeService;
        this.userService = userService;
    }

    public static void main(String[] args) {
        byte[] encode = Hex.encode("Systemspecs wallet".getBytes());
        byte[] FundACause = Hex.encode("FundACause".getBytes());
        byte[] navsa = Hex.encode("NAVSA".getBytes());
        byte[] FundACauseDemo = Hex.encode("FundACauseDemo".getBytes());
        out.println("Systemspecs hex ===> " + new String(encode));
        out.println("FundACause ===> " + new String(FundACause));
        out.println("NAVSA ===> " + new String(navsa));
        out.println("FundACauseDemo ===> " + new String(FundACauseDemo));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<RespondDTO<Profile>> authorize(@Valid @RequestBody LoginVM loginVM,
                                                         HttpSession session,
                                                         HttpServletResponse res, HttpServletRequest request) throws Exception {
        return geUserLogin(loginVM, session, res, request);
    }

    @PostMapping(path = "/authenticate", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE, MediaType.TEXT_HTML_VALUE})
    public ResponseEntity<RespondDTO<Profile>> authorizeFormUrlEncoded(@Valid LoginVM loginVM, HttpSession session,
                                                                       HttpServletResponse res, HttpServletRequest request) throws Exception {

        return geUserLogin(loginVM, session, res, request);
    }

    @PostMapping("/ipg/api-authenticate")
    public ResponseEntity<IpgResponseDTO> authorizeAPI(@Valid @RequestBody LoginVM loginVM, HttpSession session) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().
                authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = loginVM.isRememberMe() != null && loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            String scheme = loginVM.getScheme();
            out.println("Scheme from Login : " + scheme);
            if (scheme == null) {
                session.getServletContext().setAttribute(jwt, SYSTEMSPECS_SCHEME);
                session.setAttribute("scheme", SYSTEMSPECS_SCHEME);
            } else {
                session.getServletContext().setAttribute(jwt, scheme);
                session.setAttribute("scheme", scheme);

            }

            session.setAttribute("phoneNumber", loginVM.getUsername());
            // Profile profile = profileService.findByPhoneNumber(loginVM.getUsername());
            IpgResponseDTO response = new IpgResponseDTO();
            response.setMessage("Successful");
            response.setStatus("success");
            response.setCode(200);
            response.setData(jwt);
            return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            IpgResponseDTO response = new IpgResponseDTO();
            response.setMessage("bad Credentials");
            response.setStatus("failed");
            response.setCode(401);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception exc) {
            IpgResponseDTO response = new IpgResponseDTO();
            response.setMessage("bad Request");
            response.setStatus("failed");
            response.setCode(400);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api-authenticate")
    public ResponseEntity<GenericResponseDTO> authorizeAPIs(@Valid @RequestBody LoginVM loginVM, HttpSession session) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        GenericResponseDTO response = new GenericResponseDTO();


        try {
            Authentication authentication = authenticationManagerBuilder.getObject().
                authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = loginVM.isRememberMe() != null && loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            String scheme = loginVM.getScheme();
            out.println("Scheme from Login : " + scheme);
            if (scheme == null) {
                session.getServletContext().setAttribute(jwt, SYSTEMSPECS_SCHEME);
                session.setAttribute("scheme", SYSTEMSPECS_SCHEME);
            } else {
                session.getServletContext().setAttribute(jwt, scheme);
                session.setAttribute("scheme", scheme);
            }

            session.setAttribute("phoneNumber", loginVM.getUsername());
            // Profile profile = profileService.findByPhoneNumber(loginVM.getUsername());
            response.setCode("00");
            response.setMessage("Successful");
            response.setData(jwt);
            return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            response.setCode("104");
            response.setMessage("bad Credentials");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception exc) {
            response.setCode("104");
            response.setMessage("bad Request");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/check")
    //@PreAuthorize("permitAll()")
    public ResponseEntity<String> checkForCookie(
        @CookieValue(name = "token", required = false) String customLoginCookie) {
        //log.debug("testing the availability of cookies: {}", customLoginCookie);
        if (StringUtils.hasText(customLoginCookie)) {
            return ResponseEntity.ok().body(customLoginCookie);
        } else {
            return ResponseEntity.badRequest().body("no cookie found");
        }
    }

    private ResponseEntity<RespondDTO<Profile>> geUserLogin(LoginVM loginVM, HttpSession session,
                                                            HttpServletResponse res, HttpServletRequest request) throws Exception {

        int loginCount = 1;

        try {
            if (session != null) {
                Object loginCountStr = session.getAttribute("loginCount");
                if (loginCountStr != null) {
                    loginCount = (int) loginCountStr;
                    out.println("COUNTER ====> " + loginCount);
                } else {
                    loginCount = 1;
                }
            }
        } catch (Exception e) {
            loginCount = 1;
        }

        RespondDTO<Profile> response = new RespondDTO<>();

        out.println("reformatting login phone number");
        String phoneNumber = utility.formatPhoneNumber(loginVM.getUsername());
        String password = loginVM.getPassword().trim();


        User user = userRepository.findOneWithAuthoritiesByLogin(utility.formatPhoneNumber(phoneNumber))
            .orElseThrow(() -> new UsernameNotFoundException("User does not exist, please register"));

        if (loginCount > 3) {
            user.setActivated(false);
            user.setStatus(UserStatus.DEACTIVATE_CUSTOMER.getName());
            userRepository.save(user);

            response.setCode("194");
            response.setMessage("Maximum login exceeded, account deactivated");
            session.removeAttribute("loginCount");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        out.println("login user ===> " + user);
        if (!user.getActivated()) {
            if (user.getStatus().equalsIgnoreCase(UserStatus.DEACTIVATE_CUSTOMER.getName())) {
                response.setCode("51");
                response.setMessage("Account deactivated, Kindly contact support to activate your account");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else if (user.getStatus().equalsIgnoreCase(UserStatus.DEACTIVATE_CUSTOMER_PIN.getName())) {
                response.setCode("52");
                response.setMessage("Account Pin deactivated , Kindly contact support to activate your account");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else if (user.getStatus().equalsIgnoreCase(UserStatus.DEACTIVATE_CUSTOMER_USER.getName())) {
                response.setCode("56");
                response.setMessage("Account Profile deactivated , Kindly contact support to activate your account profile");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            response.setCode("91");
            response.setMessage("Account deactivated, Kindly contact support to activate your account");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        out.println("Login count ==> " + loginCount);

        List<WalletAccount> userWalletAccounts = walletAccountService.findByAccountOwnerPhoneNumber(phoneNumber);

        if (userWalletAccounts != null&&userWalletAccounts.size() > 0){
            WalletAccount userWalletAcount = userWalletAccounts.get(0);
            Scheme userScheme = userWalletAcount.getScheme();
            if (loginVM.getScheme() == null){
                loginVM.setScheme(userScheme.getSchemeID());
            }
        }

        String scheme = loginVM.getScheme();

        if (utility.checkStringIsNotValid(scheme)) {
            scheme = SYSTEMSPECS_SCHEME;
        }

        MyDevice myDevice = null;

        if (scheme.equalsIgnoreCase(SYSTEMSPECS_SCHEME)) {
            out.println("Inside my device check");
            String deviceId = loginVM.getDeviceId();
            Optional<MyDevice> deviceOptional = myDeviceService.findByProfilePhoneNumberAndDeviceId(phoneNumber, deviceId);
            out.println("device optional  +++++ " + deviceOptional);
            if (deviceOptional.isPresent()) {
                out.println("Device optional is present");
                myDevice = deviceOptional.get();
                DeviceStatus status = myDevice.getStatus();
                if (DeviceStatus.INACTIVE.equals(status)) {
                    response.setCode("53");
                    response.setMessage("Access Denied, Device is inactive");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            }
        }

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(phoneNumber, password);

        Authentication authentication = authenticationManagerBuilder.getObject().
            authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = loginVM.isRememberMe() != null && loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        if (session != null) {
            session.setAttribute("phoneNumber", phoneNumber);
        }
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Set-Cookie", "token=" + jwt + "; SameSite=None;Secure");
        Profile profile = profileService.findByPhoneNumber(phoneNumber);

        out.println("Profile ===> " + profile);
        if (profile != null) {
            List<Address> addresses = addressRepostory.findAllByAddressOwner(profile);

            out.println("Addresses Login user " + addresses);

            if (!addresses.isEmpty() && utility.checkStringIsNotValid(profile.getAddress())) {
                profile.setAddress(addresses.get(0).getAddress());
            }
        }

        response.setUserType(UserType.CUSTOMER.toString());

        //getting super agent, agent and teller
        out.println("Getting superAgent out");
        Optional<SuperAgent> superAgentOptional = superAgentService.findByAgentProfilePhoneNumber(phoneNumber);

        out.println(" superAgentOptional ====> " + superAgentOptional);
        if (superAgentOptional.isPresent()) {
            out.println("Is super Agent ======>");
            scheme = superAgentOptional.get().getScheme().getSchemeID();
            response.setUserType(UserType.SUPERAGENT.toString());
        } else {
            Optional<Agent> agentOptional = agentService.findByProfile(profile);
            out.println("Agent optional ==> " + agentOptional);
            if (agentOptional.isPresent()) {
                out.println("Agent optional is present ===> " + agentOptional.get());
                response.setUserType(UserType.AGENT.toString());
                Agent superAgent = agentOptional.get().getSuperAgent();
                Optional<SuperAgent> byAgent = superAgentService.findByAgent(superAgent);
                out.println("SuperAgent optional ==> " + byAgent);
                if (byAgent.isPresent()) {
                    out.println("SuperAgent by Agent is present");
                    scheme = byAgent.get().getScheme().getSchemeID();
                }
            } else {
                Optional<TellerDTO> tellerDTOOptional = tellerService.findByProfilePhoneNumber(phoneNumber);
                out.println("Teller optional " + tellerDTOOptional);
                if (tellerDTOOptional.isPresent()) {
                    out.println("Teller is present ==++=");
                    response.setUserType(UserType.TELLER.toString());

                    TellerDTO tellerDTO = tellerDTOOptional.get();
                    String agentPhoneNumber = tellerDTO.getAgentPhoneNumber();
                    Optional<Agent> tellerAgentOptional = agentService.findAllByProfilePhoneNumber(agentPhoneNumber);
                    out.println("Teller super agent ===> " + tellerAgentOptional);
                    if (tellerAgentOptional.isPresent()) {
                        Optional<SuperAgent> byAgent = superAgentService.findByAgent(tellerAgentOptional.get());
                        out.println("Teller super agent");
                        if (byAgent.isPresent()) {
                            scheme = byAgent.get().getScheme().getSchemeID();
                        }
                    }
                }
            }
        }

        out.println("Scheme from Login : " + scheme);

        if (session != null) {
            session.setAttribute("jwt", jwt);
            session.setAttribute("scheme", scheme);
            session.getServletContext().setAttribute("scheme-" + phoneNumber, scheme);
            session.removeAttribute("loginCount");
        }

        List<WalletAccount> walletAccounts = walletAccountService.findByAccountOwnerPhoneNumberAndScheme_SchemeID(phoneNumber, scheme);
        List<WalletAccountDTO> walletAccountDTOS =
            walletAccounts.stream().map(walletAccountMapper::toDto).collect(Collectors.toList());
        out.println("walletAccounts login ---==> " + walletAccounts);
        out.println("walletAccountDTOS login " + walletAccountDTOS);

        response.setMessage("Login success");
        response.setUser(profile);
        response.setToken(jwt);
        response.setWalletAccountList(walletAccountDTOS);

        out.println("Login Response ===> " + response);

//        boolean flag = IBILE_SCHEME.equalsIgnoreCase(scheme);
        boolean flag = false;

        out.println("Is cash connect transaction +==> " + flag);

        Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
        if (asyncExecutor != null) {
            String finalScheme = scheme;
            asyncExecutor.execute(() -> {
                try {
                    out.println("Inside polaris ===>");
                    if (profile != null) {
                        utility.createPolarisAccount(phoneNumber, finalScheme);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("Failed to create nuban for user " + phoneNumber + " scheme" +
                        " =" + finalScheme);
                }
            });
        }

        if (SYSTEMSPECS_SCHEME.equalsIgnoreCase(scheme)) {
            sendLoginEmail(user, myDevice, request);
        }

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    private void createNubanAccount(String phoneNumber) {
        utility.getCashConnectRefNo(phoneNumber);
        utility.retrieveCashConnectAccountNo(phoneNumber);

    }

    private void sendLoginEmail(User user, MyDevice myDevice, HttpServletRequest request) {
        String email = user.getEmail();
        if (utility.checkStringIsValid(email)) {
            String firstName = user.getFirstName();
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss a");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            String format = now.format(formatter);
            String gmt = "(GMT+1)";
            String format2 = now.format(formatter2);

            String date = format + ", " + gmt + " " + format2;
            String tel = "+234 (0)16348010";


            String content =
                "<br/>Dear " + firstName + "," +
                    "<br/> " +
                    "<br/> " +
                    "Please be informed that your wallet was accessed at " + date;
            if (myDevice != null) {
                content = content + " from " + myDevice.getName();
            }

           /* if (request != null) {
                content = content + " with IP address: " + request.getLocalAddr();
            }*/

            content = content + " <br/>" +
                " <br/>" +
                " <br/> If you did not initiate this login, please call our customer care line on " + tel + " or send an email to pouchii@systemspecs.com.ng immediately." +
                "<br/>" +
                "<br/>" +
                "Thank you for choosing Pouchii.";

            utility.sendEmail(email, "POUCHII LOGIN ALERT", content);
        }
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

    @GetMapping("/clear-user-cache/{phone}")
    public ResponseEntity<String> authorize(@PathVariable String phone) throws Exception {
        System.out.println("Clearing CACHE");
        Profile profile  = profileService.findByPhoneNumber(utility.formatPhoneNumber(phone));
        User user = profile.getUser();
        userService.clearUserCaches(user);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
