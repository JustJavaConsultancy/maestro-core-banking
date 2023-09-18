package ng.com.systemspecs.apigateway.web.rest.remitastp;


import ng.com.systemspecs.apigateway.service.dto.stp.DefaultApiResponse;
import ng.com.systemspecs.apigateway.service.dto.stp.TransactionDto;
import ng.com.systemspecs.apigateway.web.rest.errors.remitastp.InvalidWalletOperatorException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class TransactionController {
    public DefaultApiResponse SingleTransfer(@Valid @RequestBody TransactionDto transactionDto) throws Exception, InvalidWalletOperatorException {
        return new DefaultApiResponse();
    }

}
