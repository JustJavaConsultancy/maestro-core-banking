package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.StatusCheckDTO;
import ng.com.justjava.corebanking.domain.Biller;

import java.util.List;

/**
 * Service Interface for managing {@link Biller}.
 */
public interface HealthCheckService {

    List<StatusCheckDTO> checkExternalServicesStatus();
}
