package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.CustomFieldDropdown;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CustomFieldDropdownService {

    CustomFieldDropdown save(CustomFieldDropdown customFieldDropdown);

    CustomFieldDropdown findById(Long Id);

    Optional<CustomFieldDropdown> findByFieldDropdownId(String fieldId);
}
