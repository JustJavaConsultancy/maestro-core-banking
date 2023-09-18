package ng.com.systemspecs.apigateway.client;

import ng.com.systemspecs.apigateway.service.dto.NINRequestDTO;
import ng.com.systemspecs.apigateway.service.dto.NINResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.SendSMSDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;


@FeignClient(name = "external-otp-service", url = "https://login.remita.net/remita/exapp/api/v1")
public interface ExternalOTPRESTClient {

    @RequestMapping(value = "/send/api/notysvc/v2/sendsms.json", method = RequestMethod.POST)
    String sendSMS(@RequestBody SendSMSDTO sendSMSDTO);

    @RequestMapping(value = "/ref/api/ext/referencedata", method = RequestMethod.POST)
    NINResponseDTO validateNIN(@RequestHeader Map<String, String> headerMap, @RequestBody NINRequestDTO ninRequestDTO);

}
