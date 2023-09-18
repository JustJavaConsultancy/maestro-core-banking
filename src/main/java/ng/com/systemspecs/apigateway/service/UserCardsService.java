package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.UserCards;
import ng.com.systemspecs.apigateway.service.dto.UserCardsDTO;

import java.util.List;
import java.util.Optional;

public interface UserCardsService {

    UserCardsDTO save(UserCardsDTO userCardsDTO);

    void delete(UserCardsDTO userCardsDTO);

    List<UserCardsDTO> findAllByOwner_Id(Long id);

    List<UserCardsDTO> findAllByOwner_PhoneNumber(String phoneNumber);

    List<UserCardsDTO> findAllByScheme(String scheme);

    List<UserCardsDTO> findAllByStatus(String status);

    List<UserCardsDTO> findAllBySchemeAndOwner_PhoneNumber(String scheme, String phoneNumber);

    Optional<UserCards> findOneBySchemeAndOwner_PhoneNumber(String scheme, String phoneNumber);

    Optional<UserCards> findOneByAccountNumber(String accountNumber);

    int countByScheme(String scheme);

    int countBySchemeAndStatus(String scheme, String status);

    int countByStatus(String status);

    long count();

    List<UserCards> findAllByAccountNumber(String accountNumber);
}
