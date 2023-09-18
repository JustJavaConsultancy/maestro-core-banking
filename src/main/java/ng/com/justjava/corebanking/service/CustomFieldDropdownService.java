package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.CustomFieldDropdown;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CustomFieldDropdownService {

    CustomFieldDropdown save(CustomFieldDropdown customFieldDropdown);

    CustomFieldDropdown findById(Long Id);

    Optional<CustomFieldDropdown> findByFieldDropdownId(String fieldId);
}
