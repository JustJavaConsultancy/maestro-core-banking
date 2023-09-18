package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.Agent;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.InsuranceDTO;

import javax.servlet.http.HttpSession;

/**
 * Service Interface for managing {@link Agent}.
 */
public interface InsuranceService {

    GenericResponseDTO processInsurance(InsuranceDTO insuranceDTO, HttpSession session);

}
