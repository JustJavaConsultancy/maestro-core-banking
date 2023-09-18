package ng.com.justjava.corebanking.web.rest.kafka;

import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.ExternallyCreatedCustomerDTO;
import ng.com.justjava.corebanking.service.dto.IpgResponseDTO;
import ng.com.justjava.corebanking.service.dto.ResponseDTO;
import ng.com.justjava.corebanking.service.kafka.producer.NotificationProducer;
import ng.com.justjava.corebanking.service.kafka.producer.TransProducer;
import ng.com.justjava.corebanking.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * REST controller for managing String through Kafka.
 */
@RestController
@RequestMapping("/api")
public class TransKafkaResource {

    private final Logger log = LoggerFactory.getLogger(TransKafkaResource.class);

    private final TransProducer transProducer;
    private final NotificationProducer notificationProducer ;
    private final Utility utility ;
    private final WalletAccountService walletAccountService ;


    public TransKafkaResource(TransProducer transProducer, NotificationProducer notificationProducer, Utility utility, WalletAccountService walletAccountService) {
        this.transProducer = transProducer;
        this.notificationProducer = notificationProducer;
        this.utility = utility;
        this.walletAccountService = walletAccountService;
    }

    /**
     * {@code POST  /transs/kafka} : Send a trans in Kafka.
     *
     * @param trans the String to send.
     */
    @PostMapping("/trans/kafka")
    public ResponseEntity<ResponseDTO> sendTrans(@Valid @RequestBody ExternallyCreatedCustomerDTO trans) {
        log.debug("REST request to send a String in Kafka: {}", trans);
        //transProducer.send(trans);
//        notificationProducer.send(trans);
        ResponseDTO response = new ResponseDTO();
        response.setCode("00");
        response.setMessage("New User Accepted");
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/ipg/kafka/newCustomer")
    public ResponseEntity<IpgResponseDTO> sendNewCustomer(@Valid @RequestBody ExternallyCreatedCustomerDTO trans, HttpSession session) {
//        log.debug("REST request to send a String in Kafka: {}", trans);
//
        IpgResponseDTO response = new IpgResponseDTO();

        try {
            /*Scheme schemeFromSession = utility.getSchemeFromSession(session);

            trans.setPhone(utility.formatPhoneNumber(trans.getPhone()));

            log.debug("Phone Number: {}", trans.getPhone());

            log.debug("Account Number: {}", trans.getWalletAccountNumber());

            WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(trans.getWalletAccountNumber());

            if (StringUtils.isEmpty(trans.getWalletAccountNumber()) && walletAccount != null && walletAccount.getAccountOwner() != null){
                response.setCode(400);
                response.setStatus("failed");
                response.setMessage("AccountNumber cannot be null");
                return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }

            if (utility.searchAccountNumber(trans.getWalletAccountNumber()) && walletAccount != null && walletAccount.getAccountOwner() != null){
                response.setCode(400);
                response.setStatus("failed");
                response.setMessage("AccountNumber Already exist (" + trans.getWalletAccountNumber() + ")");
                return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }

            try {
                List<WalletAccount> walletAccountList = walletAccountService.findByAccountOwnerPhoneNumberAndAccountNameAndScheme_SchemeID(trans.getPhone(), trans.getWalletName(), schemeFromSession.getSchemeID());
                if (!walletAccountList.isEmpty() && walletAccount != null && walletAccount.getAccountOwner() != null) {
                    WalletAccount walletAccount1 = walletAccountList.get(0);
                    log.info("Wallet found " + walletAccount1);

                    response.setCode(400);
                    response.setStatus("failed");
                        response.setMessage("Wallet account with the same name(" + trans.getWalletName() + ") and phone number (" + trans.getPhone() + ") exists!");
                    return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                }
            }catch (Exception e){

                response.setCode(400);
                response.setStatus("failed");
                response.setMessage("Wallet account with the same name(" + trans.getWalletName() + ") and phone number (" + trans.getPhone() + ") exists!");
                return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }

            trans.setSchemeId(String.valueOf(schemeFromSession.getSchemeID()));*/

//            notificationProducer.send(trans);

            response.setCode(200);
            response.setStatus("success");
            response.setMessage("New User Accepted");
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

        } catch (NullPointerException ex) {

            response.setCode(400);
            response.setStatus("failed");
            response.setMessage("Null Error");
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {

            response.setCode(400);
            response.setStatus("failed");
            response.setMessage("bad Request");
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }
}
