package ng.com.justjava.corebanking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient(name = "external-cgate-service", url = "https://testdev.coralpay.com/cgateproxy", configuration = FeignClientConfiguration.class)
@FeignClient(name = "external-cgate-service", url = "https://cgateweb.coralpay.com:567/api", configuration = FeignClientConfiguration.class)
public interface ExternalCgateRESTClient {

    //    @RequestMapping(value = "/api/invokereference", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @RequestMapping(value = "/InvokeReference", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    String invokeReference(@RequestHeader("Authorization") String authHeader, @RequestBody String payload);

}
