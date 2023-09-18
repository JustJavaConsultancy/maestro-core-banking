package ng.com.systemspecs.apigateway.client;

import ng.com.systemspecs.apigateway.service.dto.PaymentNotificationDTO;
import ng.com.systemspecs.apigateway.service.dto.PaymentNotificationResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.ReferenceVerificationDTO;
import ng.com.systemspecs.apigateway.service.dto.ReferenceVerificationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


//@FeignClient(name = "ibile-service", url = "http://xxsg.ebs-rcm.com/Interface")
@FeignClient(name = "ibile-service", url = "https://revpay.ebs-rcm.com/BankPay/interface")
public interface IbileRESTClient {

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    ReferenceVerificationResponseDTO referenceVerification(@RequestBody ReferenceVerificationDTO referenceVerificationDTO);

    @RequestMapping(value = "/Payment", method = RequestMethod.POST)
    PaymentNotificationResponseDTO paymentNotification(@RequestBody PaymentNotificationDTO paymentNotificationDTO);

}
