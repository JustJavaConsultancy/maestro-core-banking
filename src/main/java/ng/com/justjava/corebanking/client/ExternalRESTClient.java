package ng.com.justjava.corebanking.client;

import feign.Headers;
import ng.com.justjava.corebanking.service.dto.BankTransfer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// 'https://remitademo.net/remita/ecomm/send/api/billing/receipt
//@FeignClient(name = "external-service", url = "https://remitademo.net/remita/ecomm/send/api")
@FeignClient(name = "external-service", url = "https://login.remita.net/remita/ecomm/send/api")
public interface ExternalRESTClient {

    @RequestMapping(value = "/confirmation/{transactionReference}", method = RequestMethod.GET)
    String getTransactionConfirmation(@PathVariable("transactionReference") String transactionReference);

    @RequestMapping(value = "/singleInterbankTransfer", method = RequestMethod.POST)
    @Headers("Content-Type: application/json")
    void singleInterbankTransfer(BankTransfer bankTransfer);


    // {{baseUrl}}/{{publicKey.txt}}/{{rrr}}/{{requestId}}/rest.reg
	@RequestMapping(value = "/billing/receipt/{publicKey}/{rrr}/{requestId}/rest.reg", method = RequestMethod.GET)
	byte[]   getRRRReceipt(@PathVariable("publicKey") String publicKey, @PathVariable("rrr") String rrr, @PathVariable("requestId") String requestId);
}
