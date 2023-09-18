package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.enumeration.PayBillerPaymentType;
import ng.com.justjava.corebanking.domain.enumeration.UserStatus;
import ng.com.justjava.corebanking.repository.UserRepository;
import ng.com.justjava.corebanking.security.SecurityUtils;
import ng.com.justjava.corebanking.service.PayBillerService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/pay-billers")
public class PayBillerResource {

    private final PayBillerService payBillerService;
    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private User theUser = null;

    public PayBillerResource(PayBillerService payBillerService, ProfileService profileService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.payBillerService = payBillerService;
        this.profileService = profileService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/billers")
    public ResponseEntity<GenericResponseDTO> getBillers() {
        List<PayBiller> billers = payBillerService.getBillers();
        return new ResponseEntity<>(new GenericResponseDTO("00", "Get Billers Successful", billers), HttpStatus.OK);
    }

    @GetMapping("/billers/categories")
    public ResponseEntity<GenericResponseDTO> getBillerCategories() {
        List<BillerCategoryNew> billerCategories = payBillerService.getBillerCategories();
        GenericResponseDTO genericResponseDTO = new GenericResponseDTO("00", "Success", billerCategories);
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/billers/category/{categoryId}")
    public ResponseEntity<GenericResponseDTO> getBillerCategoryId(@PathVariable("categoryId") String categoryId) {
        List<PayBiller> billerCategoryId = payBillerService.getBillerCategoryId(categoryId);
        GenericResponseDTO genericResponseDTO = new GenericResponseDTO("00", "Success", billerCategoryId);
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/billers/products/{billerId}")
    public ResponseEntity<GenericResponseDTO> getBillerProduct(@PathVariable("billerId") String billerId) {
        List<BillerProduct> billerProducts = payBillerService.getBillerProducts(billerId);
        GenericResponseDTO genericResponseDTO = new GenericResponseDTO("00", "Success", billerProducts);
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/rrr/{rrr}")
    public ResponseEntity<GenericResponseDTO> getRRRDetails(@PathVariable("rrr") String RRR) {
        GenericResponseDTO rrrDetails = payBillerService.getRRRDetails(Long.parseLong(RRR));
        return new ResponseEntity<>(rrrDetails, HttpStatus.OK);
    }

    @PostMapping("/pay")
    public ResponseEntity<GenericResponseDTO> validateAndPay(@RequestBody InitiateBillerTransactionDTO initiateBillerTransactionDTO, HttpSession session) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> this.theUser = user);

        GenericResponseDTO response = new GenericResponseDTO();

        if (theUser == null) {
            response.setCode("64");
            response.setMessage("User session expired");
            response.setData(true);
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, response.getStatus());
        }

        int pinFailureCount = getPinFailureCount(session);

        if (pinFailureCount >= 3) {
            return buildPinAttemptExceededResponse(response, session);
        }

        Profile profile = profileService.findByPhoneNumber(this.theUser.getLogin());

        String currentEncryptedPin = profile.getPin();
        if (!passwordEncoder.matches(initiateBillerTransactionDTO.getTransactionPin(), currentEncryptedPin)) {

            return buildPinErrorResponse(session, pinFailureCount);
        }

        GenericResponseDTO genericResponseDTO;

        if (initiateBillerTransactionDTO.getPaymentType().equalsIgnoreCase(PayBillerPaymentType.ONCE.toString())) {
            genericResponseDTO = payBillerService.validateAndPayBiller(initiateBillerTransactionDTO);
            return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), genericResponseDTO.getStatus());
        }

        if (initiateBillerTransactionDTO.getPaymentType().equalsIgnoreCase(PayBillerPaymentType.RECURRING.toString())) {
            genericResponseDTO = payBillerService.scheduleRecurring(initiateBillerTransactionDTO, theUser);
            return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), genericResponseDTO.getStatus());
        }

        if (initiateBillerTransactionDTO.getPaymentType().equalsIgnoreCase(PayBillerPaymentType.INVOICE.toString())) {
            BillerPayRrrDTO billerPayRRRDTO = new BillerPayRrrDTO();
            billerPayRRRDTO.setSourceAccount(initiateBillerTransactionDTO.getAccountNumber());
            billerPayRRRDTO.setTotalAmount(initiateBillerTransactionDTO.getAmount().toString());
            billerPayRRRDTO.setBonusAmount(0.0);
            billerPayRRRDTO.setTransactionRef(initiateBillerTransactionDTO.getTransactionRef());
            billerPayRRRDTO.setNarration(initiateBillerTransactionDTO.getNarration());
            billerPayRRRDTO.setPhoneNumber(initiateBillerTransactionDTO.getPhoneNumber());
            billerPayRRRDTO.setRrr(initiateBillerTransactionDTO.getRrr());
            billerPayRRRDTO.setRedeemBonus(initiateBillerTransactionDTO.isRedeemBonus());

            genericResponseDTO = payBillerService.payRRR(billerPayRRRDTO);

            return new ResponseEntity<>(genericResponseDTO, new HttpHeaders(), genericResponseDTO.getStatus());
        }

        return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid Payload"), HttpStatus.BAD_REQUEST);
    }

    private int getPinFailureCount(HttpSession session) {
        int pinFailureCount = 1;

        try {
            if (session != null) {
                Object loginCountStr = session.getAttribute("pinFailureCount");
                if (loginCountStr != null) {
                    pinFailureCount = (int) loginCountStr;
                    System.out.println("Pin failure Counter ====> " + pinFailureCount);
                }
            }
        } catch (Exception e) {
            pinFailureCount = 1;
        }
        return pinFailureCount;
    }

    private ResponseEntity<GenericResponseDTO> buildPinAttemptExceededResponse(GenericResponseDTO response, HttpSession session) {

        response.setMessage("Maximum failed pin reached, account deactivated");
        response.setCode("46");
        this.theUser.setActivated(false);
        this.theUser.setStatus(UserStatus.DEACTIVATE_CUSTOMER_PIN.getName());
        userRepository.save(this.theUser);

        session.removeAttribute("pinFailureCount");

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<GenericResponseDTO> buildPinErrorResponse(HttpSession session, int pinFailureCount) {
        GenericResponseDTO response;
        session.setAttribute("pinFailureCount", ++pinFailureCount);

        response = new GenericResponseDTO();
        response.setMessage("Invalid / wrong PIN entered");
        response.setCode("51");
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }
}
