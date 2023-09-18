package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.MyDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the MyDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MyDeviceRepository extends JpaRepository<MyDevice, Long> {

    Optional<MyDevice> findByDeviceId(@NotNull String deviceId);

    List<MyDevice> findByProfilePhoneNumber(String phoneNumber);

    Optional<MyDevice> findByProfilePhoneNumberAndDeviceId(String phoneNumber, String deviceId);

}
