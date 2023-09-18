package ng.com.justjava.corebanking.util;

import ng.com.justjava.corebanking.service.dto.FundDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccountNumberValidatorConstraint implements ConstraintValidator<NotEqualAccountNumber, FundDTO> {

    @Override
    public void initialize(NotEqualAccountNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(FundDTO fundDTO, ConstraintValidatorContext context) {
        return !String.valueOf(fundDTO.getAccountNumber()).equals(fundDTO.getSourceAccountNumber());
    }
}
