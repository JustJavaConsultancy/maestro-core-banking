package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.CardRequest;
import ng.com.systemspecs.apigateway.service.dto.CardRequestDTO;
import ng.com.systemspecs.apigateway.service.dto.ProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CardRequestService {

    CardRequestDTO save(CardRequestDTO cardRequestDTO);

    List<CardRequestDTO> save (List<CardRequestDTO> cardRequestDTOList);

    List<CardRequestDTO> findAllByCardtype(String Cardtype);

    List<CardRequestDTO> findAllByScheme(String scheme);

    List<CardRequestDTO> findAllBySchemeAndCardtype(String Scheme, String Cardtype);

    List<CardRequestDTO> findAllBySchemeAndCardtypeAndCreatedDateBetween(String scheme, String cardtype, Instant startDate, Instant endDate);

    List<CardRequestDTO> findAllByStatus(String status);

    List<CardRequestDTO> findActiveCardRequests(String status);

    Optional<CardRequest> findById(Long id);

    Page<CardRequestDTO> findAll(Pageable pageable);

    List<CardRequestDTO> findAll();

    long countAllRequests();

//    Page<CardRequestDTO> findAllWithKeyword(Pageable pageable, String key);

    List<CardRequestDTO> findAllBySchemeAndCreatedDateBetween(String scheme, Instant startDate, Instant endDate);

    List<CardRequestDTO> findAllByCardtypeAndCreatedDateBetween(String cardtype, Instant startDate, Instant endDate);

    List<CardRequestDTO> findAllByCreatedDateBetween(Instant startDate, Instant endDate);

    CardRequest findActiveRequest(ProfileDTO owner, String scheme);
}
