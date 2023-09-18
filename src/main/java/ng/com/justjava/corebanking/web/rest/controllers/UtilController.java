package ng.com.justjava.corebanking.web.rest.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.PolarisVulteRequestDTO;
import ng.com.justjava.corebanking.util.PolarisVulteUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/util")
@RequiredArgsConstructor
@Slf4j
public class UtilController {

    private final PolarisVulteUtils polarisVulteUtils;


    @PostMapping("/createNuban/{apiKey}/{secretKey}/{url}")
    public ResponseEntity<GenericResponseDTO> createNumban(@PathVariable String apiKey,
                                                           @PathVariable String secretKey,
                                                           @PathVariable String url,
                                                           @RequestBody PolarisVulteRequestDTO request) {

        HttpHeaders headers = new HttpHeaders();
        String requestRef = request.getRequestRef();//utility.getUniqueTransRef();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Signature", polarisVulteUtils.generateSignature(requestRef, secretKey));
        log.info("createNumban request === ", request + " url=="+url+"  headers=="+headers);
        url ="/v2/"+url;
        GenericResponseDTO responseDTO=polarisVulteUtils.invokeVulteApi(request,headers,url);
        log.info("responseDTO === ", responseDTO);
        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", responseDTO), HttpStatus.OK);
    }

}
