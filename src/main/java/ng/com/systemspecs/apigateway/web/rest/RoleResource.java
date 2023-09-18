package ng.com.systemspecs.apigateway.web.rest;

import ng.com.systemspecs.apigateway.domain.Authority;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.ProfileType;
import ng.com.systemspecs.apigateway.domain.User;
import ng.com.systemspecs.apigateway.repository.ProfileRepository;
import ng.com.systemspecs.apigateway.repository.UserRepository;
import ng.com.systemspecs.apigateway.service.UserService;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.UpdateUserRoleDTO;
import ng.com.systemspecs.apigateway.util.Utility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class RoleResource {

    private final UserRepository userRepository;
    private final Utility utility;
    private final UserService userService;
    private final ProfileRepository profileRepository;

    public RoleResource(UserRepository userRepository, Utility utility, UserService userService, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.utility = utility;
        this.userService = userService;
        this.profileRepository = profileRepository;
    }



    @PutMapping("/update-user-role")
    ResponseEntity<GenericResponseDTO> updateUserRole(@RequestBody UpdateUserRoleDTO updateUserRoleDTO){

        try{

            String phone = utility.formatPhoneNumber(updateUserRoleDTO.getPhoneNumber());
            Optional<User> userOptional = userService.findByLogin(phone);

            if (!userOptional.isPresent()){
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User Not Found"), HttpStatus.BAD_REQUEST);
            }

            User user = userOptional.get();

            Authority authority = new Authority();
            authority.setName(updateUserRoleDTO.getUserRole());

            Set<Authority> authorities = new HashSet<>();
            authorities.add(authority);

            user.setAuthorities(authorities);

            userRepository.save(user);

            return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "User Role Updated Successfully"), HttpStatus.OK);

        }catch (Exception e){

            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/roles/{phoneNumber}")
    ResponseEntity<GenericResponseDTO>getUserRole(@PathVariable String phoneNumber){

        try{

            String phone = utility.formatPhoneNumber(phoneNumber);
            Optional<User> userOptional = userService.findByLogin(phone);

            if (!userOptional.isPresent()){
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User Not Found"), HttpStatus.BAD_REQUEST);
            }

            User user = userOptional.get();

            Set<Authority> authorities = user.getAuthorities();
            Profile profile = profileRepository.findOneByPhoneNumber(phone);

            if (profile == null){
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Profile Not Found"), HttpStatus.BAD_REQUEST);
            }

            ProfileType profileType = profile.getProfileType();

            Map<String, Object> response = new HashMap<>();
            response.put("authorities", authorities);
            response.put("profileType", profileType);


            return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "User Role Retrieved Successfully", response), HttpStatus.OK);

        }catch (Exception e){

            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);

        }
    }







}
