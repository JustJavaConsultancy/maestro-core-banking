package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateKeysDTO {

    private String scheme;
    private String apiKey;
    private String secretKey;
    private String schemeID;
    private String accountNumber;

    private String bankCode;

    private String callBackUrl;

}
