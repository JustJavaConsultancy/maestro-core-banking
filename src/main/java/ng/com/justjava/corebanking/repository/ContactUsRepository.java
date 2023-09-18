package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.ContactUs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ContactUs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactUsRepository extends JpaRepository<ContactUs, Long> {
}
