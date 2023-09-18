package ng.com.systemspecs.apigateway.web.rest.errors;

import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.exception.GenericException;
import ng.com.systemspecs.apigateway.service.exception.IbilePayException;
import ng.com.systemspecs.apigateway.service.exception.IbilePaymentDetailsException;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.NonUniqueResultException;
import javax.persistence.RollbackException;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RestController
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ex.printStackTrace();


        String errorMessage = "Invalid request payload";
        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST, errorMessage, ex.getLocalizedMessage()));
    }

    // Handle handleClientErrorException. Happens when request Entity is unprocessable.
    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<Object> handleClientErrorException(HttpClientErrorException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ex.printStackTrace();

        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST, "Error while connecting", ex.getLocalizedMessage()));
    }

    // Handles IllegalArgumentException
    // Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalException(IllegalArgumentException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ex.printStackTrace();

        String errorMessage = ex.getLocalizedMessage();
        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST, "Invalid parameters",
            ex.getCause()));
    }

    // Handles ResourceAccessException
    // Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
    @ExceptionHandler(ResourceAccessException.class)
    protected ResponseEntity<Object> handleIllegalException(ResourceAccessException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ex.printStackTrace();

        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST, "Resource not available",
            ex.getCause()));
    }

    // Handles ConnectException
    // Handle ConnectException. Happens when request JSON is malformed.
    @ExceptionHandler(ConnectException.class)
    protected ResponseEntity<Object> handleIllegalException(ConnectException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ex.printStackTrace();

        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getCause()));
    }

    // Handles AccessDeniedException
    // Handle AccessDeniedException. Happens when AccessDenied.
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ex.printStackTrace();

        String errorMessage = ex.getLocalizedMessage();
        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST, errorMessage, null));
    }

    // Handles IllegalStateException
    // Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<Object> handleIllegalException(IllegalStateException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ex.printStackTrace();

        String errorMessage = ex.getLocalizedMessage();
        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST, "Malformed JSON request", errorMessage));
    }

    // Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
        EntityNotFoundException ex) {

        ex.printStackTrace();

        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.NOT_FOUND, "Invalid resource object",
            null));
    }

    //Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex, HttpHeaders headers,
        HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        ex.printStackTrace();

        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST, error, null));
    }

    //  Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid.
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatus status,
                                                                     WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        ex.printStackTrace();

        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), null));
    }

    // Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {

        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(objectError -> {
            String errorMessage = objectError.getDefaultMessage();
            errors.add(errorMessage);
        });

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        System.out.println("MethidArgument not valid Field errors ++++ " + fieldErrors);
        ex.printStackTrace();
//        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST,
            String.join("\n", errors).replace(",", ""),
            null));
    }

    //Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
        javax.validation.ConstraintViolationException ex) {

        Map<String, String> errors = new HashedMap();

        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        constraintViolations.stream().forEach(constraintViolation -> {
            String message = constraintViolation.getMessage();
            String messageTemplate = constraintViolation.getMessageTemplate();
            errors.put(messageTemplate, message);
        });

        ex.printStackTrace();

        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST,
            errors.entrySet()
                .stream()
                .map(e -> e.getKey() + " : " + e.getValue())
                .collect(Collectors.joining("\n")),
            null));
    }

    // Handle HttpMessageNotWritableException.
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ex.printStackTrace();

        String error = "Error writing JSON output";
        System.out.println("Error writing JSON output ==> Cause " + ex.getCause());
        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST, error, ex.getLocalizedMessage()));
    }

    //Handle NoHandlerFoundException.
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ex.printStackTrace();

        String message = String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL());
        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST, message, ex.getMessage()));
    }

    // javax.persistence.EntityNotFoundException
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        ex.printStackTrace();

        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.NOT_FOUND, "resource object not found",
            null));
    }

    //Handle MethodArgumentTypeMismatchException
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {

        String message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        ex.printStackTrace();

        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST, message, null));
    }

    //Handle DataIntegrityViolationException, inspects the cause for different DB causes.
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.CONFLICT, "Database error", null));
        }
        ex.printStackTrace();

        return buildResponseEntity(new GenericResponseDTO("error", HttpStatus.BAD_REQUEST, "Invalid input data", null));
    }

    //Handle genericException
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllException(Exception ex,
                                                        WebRequest request) {
        logger.debug("EXCEPTION " + ex.getLocalizedMessage());
        logger.debug("WEB REQUEST " + request);

        ex.printStackTrace();

        return buildResponseEntity(new GenericResponseDTO("error",
            HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred, please try again",
            ex.getLocalizedMessage()));
    }

    @ExceptionHandler(HttpServerErrorException.class)
    protected ResponseEntity<Object> handleHttpServerErrorException(HttpServerErrorException ex,
                                                                    WebRequest request) {
        logger.debug("EXCEPTION " + ex.getLocalizedMessage());
        logger.debug("WEB REQUEST " + request);
        ex.printStackTrace();

        return buildResponseEntity(new GenericResponseDTO("error",
            HttpStatus.INTERNAL_SERVER_ERROR, "Connection error, please try again"));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    protected ResponseEntity<Object> handleInvalidPasswordException(InvalidPasswordException ex,
                                                                    WebRequest request, HttpSession session) {

        int loginCount = 1;

        if (session != null) {
            Object loginCountStr = session.getAttribute("loginCount");
            if (loginCountStr != null) {
                loginCount = (int) loginCountStr;
            }

            loginCount++;
            session.setAttribute("loginCount", loginCount);
        }

        return buildResponseEntity(
            new GenericResponseDTO("error", HttpStatus.BAD_REQUEST,
                "Incorrect password", null));
    }

    @ExceptionHandler(LoginAlreadyUsedException.class)
    protected ResponseEntity<Object> handleAlreadyUsedException(LoginAlreadyUsedException ex,
                                                                WebRequest request, HttpSession session) {

        int loginCount = 0;

        if (session != null) {
            Object loginCountStr = session.getAttribute("loginCount");
            if (loginCountStr != null) {
                loginCount = (int) loginCountStr;
            }

            loginCount++;
            session.setAttribute("loginCount", loginCount);
        }

        ex.printStackTrace();

        return buildResponseEntity(
            new GenericResponseDTO("error", HttpStatus.BAD_REQUEST,
                "Incorrect password", null));
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex,
                                                                   WebRequest request, HttpSession session) {

        int loginCount = 0;

        if (session != null) {
            Object loginCountStr = session.getAttribute("loginCount");
            if (loginCountStr != null) {
                loginCount = (int) loginCountStr;
                System.out.println("COUNTER ====> " + loginCount);
            }

            loginCount++;
            session.setAttribute("loginCount", loginCount);
        }
        if (loginCount == 3) {
            return buildResponseEntity(
                new GenericResponseDTO("194", HttpStatus.BAD_REQUEST,
                    "Maximum login exceeded, account deactivated", null));

        }

        ex.printStackTrace();

        return buildResponseEntity(
            new GenericResponseDTO("error", HttpStatus.BAD_REQUEST,
                "Invalid login details", null));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex,
                                                                     WebRequest request) {
        return buildResponseEntity(
            new GenericResponseDTO("error", HttpStatus.BAD_REQUEST,
                ex.getMessage(), null));
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    protected ResponseEntity<Object> handleAInsufficientAuthenticationException(InsufficientAuthenticationException ex,
                                                                                WebRequest request, HttpSession session) {

        ex.printStackTrace();

        int loginCount = 0;

        if (session != null) {
            Object loginCountStr = session.getAttribute("loginCount");
            if (loginCountStr != null) {
                loginCount = (int) loginCountStr;
            }

            loginCount++;
            session.setAttribute("loginCount", loginCount);
        }
        return buildResponseEntity(
            new GenericResponseDTO("error", HttpStatus.UNAUTHORIZED,
                "Please login"));
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handleNullPointerException(NullPointerException ex,
                                                                WebRequest request) {

        ex.printStackTrace();


        return buildResponseEntity(
            new GenericResponseDTO("failed", HttpStatus.BAD_REQUEST,
                "A null error occurred! Please try again"));
    }

    @ExceptionHandler(NumberFormatException.class)
    protected ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex,
                                                                 WebRequest request) {

        return buildResponseEntity(
            new GenericResponseDTO("failed", HttpStatus.BAD_REQUEST,
                "Invalid Number format", ex.getLocalizedMessage()));
    }

    @ExceptionHandler(GenericException.class)
    protected ResponseEntity<Object> handleGenericException(GenericException ex) {

        ex.printStackTrace();

        return buildResponseEntity(
            new GenericResponseDTO("failed", HttpStatus.BAD_REQUEST,
                ex.getMessage()));
    }


    @ExceptionHandler(NonUniqueResultException.class)
    protected ResponseEntity<Object> handleNonUniqueResultException(NonUniqueResultException ex) {

        ex.printStackTrace();

        return buildResponseEntity(
            new GenericResponseDTO("failed", HttpStatus.BAD_REQUEST,
                "Data already exist", ex.getMessage()));
    }

    @ExceptionHandler(SocketTimeoutException.class)
    protected ResponseEntity<Object> handleSocketTimeoutException(SocketTimeoutException ex) {

        ex.printStackTrace();

        return buildResponseEntity(
            new GenericResponseDTO("99", HttpStatus.BAD_REQUEST,
                "Api timeout", ex.getLocalizedMessage()));
    }

    @ExceptionHandler(IbilePaymentDetailsException.class)
    protected ResponseEntity<Object> handleIbilePaymentDetailsException(IbilePaymentDetailsException ex) {

        ex.printStackTrace();

        return buildResponseEntity(
            new GenericResponseDTO("99", HttpStatus.BAD_REQUEST,
                "Validate api failure", ex.getLocalizedMessage()));
    }

    @ExceptionHandler(IbilePayException.class)
    protected ResponseEntity<Object> handleIbilePayException(IbilePayException ex) {

        ex.printStackTrace();

        return buildResponseEntity(
            new GenericResponseDTO("99", HttpStatus.BAD_REQUEST,
                "Payment api failure", ex.getLocalizedMessage()));
    }

    @ExceptionHandler(RollbackException.class)
    protected ResponseEntity<Object> handleRollbackException(RollbackException ex) {

        ex.printStackTrace();
        System.out.println("Rollback exception cause");
        ex.getCause();

        System.out.println("Persistence error rollback");
        return buildResponseEntity(
            new GenericResponseDTO("99", HttpStatus.BAD_REQUEST,
                "Update error", ex.getLocalizedMessage()));
    }

    @ExceptionHandler(javax.transaction.RollbackException.class)
    protected ResponseEntity<Object> handleTransactionRollbackException(javax.transaction.RollbackException ex) {

        ex.printStackTrace();
        System.out.println("Rollback exception cause");
        ex.getCause();

        System.out.println("Persistence error rollback");
        return buildResponseEntity(
            new GenericResponseDTO("99", HttpStatus.BAD_REQUEST,
                "Update error", ex.getLocalizedMessage()));
    }

    @ExceptionHandler(UnexpectedRollbackException.class)
    protected ResponseEntity<Object> handleUnexpectedRollbackException(UnexpectedRollbackException ex) {

        ex.printStackTrace();
        System.out.println("UnexpectedRollbackException  cause");
        ex.getCause();

        System.out.println("Persistence UnexpectedRollbackException");
        return buildResponseEntity(
            new GenericResponseDTO("99", HttpStatus.BAD_REQUEST,
                "Update error", ex.getLocalizedMessage()));
    }


    private ResponseEntity<Object> buildResponseEntity(GenericResponseDTO responseDTO) {
        return new ResponseEntity<>(responseDTO, responseDTO.getStatus());
    }

}
