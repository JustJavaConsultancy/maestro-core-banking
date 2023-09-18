package ng.com.systemspecs.apigateway.service.dto;

public abstract class ApiError {
}

//@Data
//@EqualsAndHashCode(callSuper = false)
//@AllArgsConstructor
class ApiValidationError extends ApiError {
    private final String object;
    private String field;
    private Object rejectedValue;
    private final String message;

    ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
