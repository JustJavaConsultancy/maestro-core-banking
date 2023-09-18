package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.AppUpdate;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AppUpdate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppUpdateRepository extends JpaRepository<AppUpdate, Long> {
	@Query(value = "SELECT  * FROM app_update ORDER BY ID DESC LIMIT 1", nativeQuery = true)
	AppUpdate findLatestUpdate();

}
