package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.Address;
import ng.com.justjava.corebanking.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByAddressOwner(Profile profile);

    List<Address> findAllByAddressOwner_PhoneNumber(String profileNumber);
}
