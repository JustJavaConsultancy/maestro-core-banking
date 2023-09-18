package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.VehicleInsuranceRequest;

import java.util.List;

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
