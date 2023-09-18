package ng.com.systemspecs.apigateway.service.impl;

import ng.com.systemspecs.apigateway.domain.CustomFieldDropdown;
import ng.com.systemspecs.apigateway.repository.CustomFieldDropdownRepository;
import ng.com.systemspecs.apigateway.service.CustomFieldDropdownService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomFieldDropdownServiceImpl implements CustomFieldDropdownService {

    private final CustomFieldDropdownRepository customFieldDropdownRepository;

    public CustomFieldDropdownServiceImpl(CustomFieldDropdownRepository customFieldDropdownRepository) {
        this.customFieldDropdownRepository = customFieldDropdownRepository;
    }


    @Override
    public CustomFieldDropdown save(CustomFieldDropdown customFieldDropdown) {
        return customFieldDropdownRepository.save(customFieldDropdown);
    }

    @Override
    public CustomFieldDropdown findById(Long id) {
        Optional<CustomFieldDropdown> byId = customFieldDropdownRepository.findById(id);
        return byId.orElse(null);
    }

    @Override
    public Optional<CustomFieldDropdown> findByFieldDropdownId(String fieldId) {
        return customFieldDropdownRepository.findByFieldDropdownId(fieldId);
    }
}
