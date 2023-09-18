package ng.com.justjava.corebanking.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordValidatorConstraint.class)
@Target({ElementType.FIELD, ANNOTATION_TYPE, ElementType.LOCAL_VARIABLE, ElementType.METHOD})
@Retention(RUNTIME)
public @interface ValidPassword {

    String message() default "Password must be 8 or more characters in length.Password must contain 1 or more uppercase characters.Password must contain 1 or more lowercase characters";

    Class<?> [] groups() default {};

    Class<? extends Payload> [] payload() default {};
}
