package ng.com.justjava.corebanking.web.rest;

import lombok.RequiredArgsConstructor;
import ng.com.justjava.corebanking.client.ExternalCgateRESTClient;
import ng.com.justjava.corebanking.client.ExternalRESTClient2;
import ng.com.justjava.corebanking.service.CoralPayService;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.dto.stp.NameEnquiryRequest;
import ng.com.justjava.corebanking.service.fcm.PushNotificationService;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.justjava.corebanking.util.CoralPayUtils;
import ng.com.justjava.corebanking.util.FlutterWaveUtil;
import ng.com.justjava.corebanking.util.MemoryStats;
import ng.com.justjava.corebanking.util.StringEncryptionConverter;
import ng.com.justjava.corebanking.util.Utility;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnqiryRequest;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.util.Base64;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TestAPI {
    private final PushNotificationService pushNotificationService;
    private final ExternalRESTClient2 externalRESTClient2;
    private final ExternalCgateRESTClient externalCgateRESTClient;
    private final WalletAccountResource walletAccountResource;
    private final CoralPayService coralPayService;
    private final CoralPayUtils coralPayUtils;
    private final Utility utility;
//    private final CardClient cardClient;
    private final PasswordEncoder passwordEncoder;
    private final FlutterWaveUtil flutterWaveUtil;


    @PostMapping("/send-notification-topic")
    public String SendNotificationTopic(@Valid @RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationWithoutData(request); //(request);
        return "success";
    }

    @PostMapping("/send-notification-token")
    public String SendNotificationToken(@Valid @RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToToken(request);//(request); //(request);
        return "success";
    }

    @PostMapping("/test-create-new-wallet")
    public String testCreateNewWallet(@Valid @RequestBody NewWalletAccountDTO newWalletAccountDTO) {
        HttpEntity<NewWalletAccountResponse> responseHttpEntity
            = walletAccountResource.newWalletAccount(newWalletAccountDTO);
        return "success";
    }


    @PostMapping("/test-add-wallet-account")
    public String testAddWalletAccount(@Valid @RequestBody AdditionalWalletAccountDTO additionalWalletAccountDTO) {
        walletAccountResource.addWalletAccount(additionalWalletAccountDTO);
        return "success";
    }

   /* @PostMapping("/test-fund-wallet")
    public String testFundWallet(@Valid  @RequestHeader(value = "Idempotent-key") String idempotentKey, @RequestBody FundDTO fundWalletDTO) {
        walletAccountResource.fundWalletAccount(idempotentKey, fundWalletDTO);
        return "success";
    }*/

    @PostMapping("/test-fund-wallet")
    public String testFundWallet(@Valid @RequestBody FundDTO fundWalletDTO, HttpSession session) {
        walletAccountResource.fundWalletAccount(fundWalletDTO, session);
        return "success";
    }


    @PostMapping("/test-send-money")
    public String testSendMoney(@Valid @RequestBody FundDTO sendMoneyDTO, HttpSession session) {
        try {
            walletAccountResource.sendMoney(sendMoneyDTO, session);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "success";
    }

    @PostMapping("/test-format/{phoneNumber}")
    public String testFormat(@PathVariable String phoneNumber) {

        return utility.returnPhoneNumberFormat(phoneNumber);
    }

    @PostMapping("/test-photo")
    public ResponseEntity<byte[]> testCompression(@Valid @RequestBody NameEnquiryRequest nameEnquiryRequest) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(nameEnquiryRequest.getAccountNumber());
        byte[] image = utility.resizeImage(decodedBytes);

        File file = new File("image.jpg");
        System.out.println("Path name = " + "image.jpg");
        System.out.println("File absolute path = " + file.getAbsolutePath());
        FileUtils.writeByteArrayToFile(file, decodedBytes);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }


    @PostMapping("/invoke")
    public ResponseEntity<String> invoke(@Valid @RequestBody String payload) throws Exception {

        return new ResponseEntity<>(coralPayService.invokeReference(payload), HttpStatus.OK);
    }

    @PostMapping("/invoke/index")
    public ResponseEntity<String> invokeExp(@Valid @RequestBody String payload) throws Exception {

        return new ResponseEntity<>(coralPayService.invokeExp(payload), HttpStatus.OK);
    }

    @PostMapping("/invokeReference")
    public ResponseEntity<String> invokeReference(@Valid @RequestBody String payload) throws Exception {

        return new ResponseEntity<>(coralPayService.coralPayInvokeReference(payload), HttpStatus.OK);
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> decrypt(@Valid @RequestBody String payload) throws Exception {

        return new ResponseEntity<>(coralPayUtils.decrypt(payload), HttpStatus.OK);
    }

    @GetMapping("/authority")
    public String checkAutority() {
    	return "This is the a test for authority!";
    }

    @GetMapping("/string-encrypt/{string}")
    public String stringEncrypt(@PathVariable String string) {
    	StringEncryptionConverter stringEncryptionConverter = new StringEncryptionConverter();
    	return stringEncryptionConverter.convertToDatabaseColumn(string);
    }
    @GetMapping("/string-decrypt/{string}")
    public String stringDecrypt(@PathVariable String string) {
    	StringEncryptionConverter stringEncryptionConverter = new StringEncryptionConverter();
    	return stringEncryptionConverter.convertToEntityAttribute(string);
    }

    @GetMapping("/password-decrypt/{string}")
    public String passwordDecrypt(@PathVariable String string) {
        return passwordEncoder.encode(string);
    }

    @GetMapping("/password-encrypt/{string}")
    public String passwordEncrypt(@PathVariable String string) {;
        return passwordEncoder.encode(string);
    }

  /*  @PostMapping("/test-create-virtual-card")
    public String testCreateVirtualCard(@RequestBody CreateVirtualCard createVirtualCard) {

//    public RespCreateVirtualCard testCreateVirtualCard(@RequestBody CreateVirtualCard createVirtualCard) {
        CreateVirtualCardResponse virtualCard = null;
        try {
            virtualCard = cardClient.createVirtualCard(createVirtualCard);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JAXBElement<RespCreateVirtualCard> createVirtualCardResult = null;
        if (virtualCard != null) {
            createVirtualCardResult = virtualCard.getCreateVirtualCardResult();
        }

        if (createVirtualCardResult != null) {
//            return createVirtualCardResult.getValue();
            return createVirtualCardResult.getValue().getResponseMessage().getValue();
        }
        return null;
    }

    @PostMapping(path = "/test-create-virtual-card2", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponseDTO testCreateVirtualCard2(@RequestBody CreateVirtualCard createVirtualCard) {

//    public RespCreateVirtualCard testCreateVirtualCard(@RequestBody CreateVirtualCard createVirtualCard) {
        CreateVirtualCardResponse virtualCard = null;
        VirtualCardResponseDTO virtualCardResponseDTO = new VirtualCardResponseDTO();
        try {
            virtualCard = cardClient.createVirtualCard(createVirtualCard);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JAXBElement<RespCreateVirtualCard> createVirtualCardResult = null;
        if (virtualCard != null) {
            createVirtualCardResult = virtualCard.getCreateVirtualCardResult();
        }

        if (createVirtualCardResult != null && createVirtualCardResult.getValue() != null) {
            virtualCardResponseDTO.setClientCode(createVirtualCardResult.getValue().getClientCode().getValue());
            virtualCardResponseDTO.setCvv2(createVirtualCardResult.getValue().getCvv2().getValue());
            virtualCardResponseDTO.setExpiryDate(createVirtualCardResult.getValue().getExpiryDate().getValue());
            virtualCardResponseDTO.setPan(createVirtualCardResult.getValue().getPAN().getValue());
            virtualCardResponseDTO.setResponseCode(createVirtualCardResult.getValue().getResponseCode().getValue());
            virtualCardResponseDTO.setResponseMessage(createVirtualCardResult.getValue().getResponseMessage().getValue());
            if (!virtualCardResponseDTO.getResponseCode().equalsIgnoreCase("00")) {
                return new GenericResponseDTO("fail", "Request failed", virtualCardResponseDTO);
            }
            return new GenericResponseDTO("success", "Successful", virtualCardResponseDTO);
        }
        return null;
    }*/
  @GetMapping("memory-status")
  public MemoryStats getMemoryStatistics() {
      return new MemoryStats();
  }

    @PostMapping("flutter-wave")
    public ResponseEntity<?> testFlutterWaveLookup(@RequestBody AccountEnqiryRequest accountEnqiryRequest) {
        return ResponseEntity.status(200).body(flutterWaveUtil.resolveAccounts(accountEnqiryRequest));
    }

}
