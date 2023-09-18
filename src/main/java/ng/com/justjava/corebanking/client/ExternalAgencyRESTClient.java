package ng.com.justjava.corebanking.client;

import ng.com.justjava.corebanking.service.dto.AgencyPaymentNotifyDTO;
import ng.com.justjava.corebanking.service.dto.IPGPaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@FeignClient(name = "external-agencypayment-service", url = "https://login.remita.net/remita/exapp/api/v1/send/api/echannelsvc/echannel/v3/agents")
//@FeignClient(name = "external-agencypayment-service", url = "https://remitademo.net/remita/exapp/api/v1/send/api/echannelsvc/echannel/v3/agents")
public interface ExternalAgencyRESTClient {

    @RequestMapping(value = "/{rrr}/lookup", method = RequestMethod.GET)
    IPGPaymentResponseDTO validateAgentRRR(@RequestHeader Map<String, String> headers, @PathVariable("rrr") String rrr);


    @RequestMapping(value = "/notification", method = RequestMethod.POST)
    IPGPaymentResponseDTO doAgentPaymentAndNotify(@RequestHeader Map<String, String> headers, @RequestBody AgencyPaymentNotifyDTO agencyPaymentNotifyDTO);


}
