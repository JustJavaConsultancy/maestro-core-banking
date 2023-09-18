package ng.com.justjava.corebanking.client;

import ng.com.justjava.corebanking.service.dto.InsureVihicleRequestDTO;
import ng.com.justjava.corebanking.service.dto.InsureVihicleResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "vehicle-insurance", url = "https://ims-api.mutualng.com/policy/motor")
public interface MutualVehicleInsuranceRestClient {

	@RequestMapping(value = "/thirdparty", method = RequestMethod.POST)
	ResponseEntity<InsureVihicleResponseDTO> insureVehicle(@RequestBody InsureVihicleRequestDTO insureVehicleRequest);

}
