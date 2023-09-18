package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.service.HealthCheckService;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.StatusCheckDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/health")
public class HealthCheckResource {

    public final HealthCheckService healthCheckService;
    private final Logger log = LoggerFactory.getLogger(KyclevelResource.class);

    public HealthCheckResource(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping("/check-external")
    public ResponseEntity<GenericResponseDTO> checkExternalServicesStatus() {
        List<StatusCheckDTO> statusCheckDTOS = healthCheckService.checkExternalServicesStatus();
        return new ResponseEntity<>(new GenericResponseDTO("success", "successful", statusCheckDTOS), HttpStatus.OK);
    }

}
