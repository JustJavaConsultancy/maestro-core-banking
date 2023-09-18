package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.VehicleInsuranceRequest;
import ng.com.systemspecs.apigateway.service.dto.VehicleInsuranceRequestDTO;
import ng.com.systemspecs.apigateway.service.dto.WalletAccountDTO;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the VehicleInsuranceRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleInsuranceRequestRepository extends JpaRepository<VehicleInsuranceRequest, Long> {

	List<VehicleInsuranceRequest> findAllByProfileIdOrderByIdDesc(Long id);
}
