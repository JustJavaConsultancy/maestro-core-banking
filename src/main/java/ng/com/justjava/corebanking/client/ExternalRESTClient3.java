package ng.com.justjava.corebanking.client;

import feign.Param;
import ng.com.justjava.corebanking.service.dto.NinFingerPrintDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;


//@FeignClient(name = "external-service-2", url = "https://remitademo.net/api/ext")
@FeignClient(name = "external-service-2", url = "https://login.remita.net")
public interface ExternalRESTClient3 {

    @RequestMapping(value = "/api/ext/referencedataninandfingerprint", method = RequestMethod.POST)
    Object getFingerPrintData(@RequestHeader Map<String, String> headers, @RequestBody NinFingerPrintDTO ninFingerPrintDTO);
    // Object getFingerPrintData(@RequestHeader Map<String,String> headers, @RequestBody  NinFingerPrintDTO  ninFingerPrintDTO);

    @RequestMapping(value = "/payment/v1/payment/query/{transId}", method = RequestMethod.GET)
    Object verifyTransaction(@RequestHeader Map<String, String> headers, @Param(value = "transId") String transId);

}
