package ng.com.justjava.corebanking.web.rest.remitastp;


import ng.com.justjava.corebanking.service.dto.stp.DefaultApiResponse;
import ng.com.justjava.corebanking.service.dto.stp.TransactionDto;
import ng.com.justjava.corebanking.web.rest.errors.remitastp.InvalidWalletOperatorException;
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
