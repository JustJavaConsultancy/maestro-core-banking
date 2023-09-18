package ng.com.systemspecs.apigateway.client;

import ng.com.systemspecs.apigateway.service.dto.SendEmailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "external-email-service", url = "https://login.remita.net/remita/exapp/api/v1")
public interface ExternalEmailRESTClient {

    @RequestMapping(value = "/send/api/notysvc/v2/sendMail.json", method = RequestMethod.POST)
    String sendEmail(@RequestBody SendEmailDTO sendEmailDTO);

}
