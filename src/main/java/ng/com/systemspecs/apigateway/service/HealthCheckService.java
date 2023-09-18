package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.Biller;
import ng.com.systemspecs.apigateway.service.dto.StatusCheckDTO;

import java.util.List;

/**
 * Service Interface for managing {@link Biller}.
 */
public interface HealthCheckService {

    List<StatusCheckDTO> checkExternalServicesStatus();
}
