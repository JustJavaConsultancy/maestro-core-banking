package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.AccessItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the AccessItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessItemRepository extends JpaRepository<AccessItem, Long> {

    List<AccessItem> findByRight_Code(String rightCode);

    List<AccessItem> findByRight_CodeAndMakerIsFalse(String rightCode);

    List<AccessItem> findByRight_CodeAndMakerIsTrue(String rightCode);

    List<AccessItem> findByAccessRight_Profile_PhoneNumberAndRight_CodeAndMakerTrue(String phoneNumber, String rightCode);

    List<AccessItem> findByAccessRight_Profile_PhoneNumberAndRight_CodeAndMakerFalse(String phoneNumber, String rightCode);

    List<AccessItem> findByAccessRight_Profile_PhoneNumber(String phoneNumber);


}
