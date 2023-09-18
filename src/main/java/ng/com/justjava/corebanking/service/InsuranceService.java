package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.InsuranceDTO;
import ng.com.justjava.corebanking.domain.Agent;

import javax.servlet.http.HttpSession;

/**
 * Service Interface for managing {@link Agent}.
 */
public interface InsuranceService {

    GenericResponseDTO processInsurance(InsuranceDTO insuranceDTO, HttpSession session);

}
