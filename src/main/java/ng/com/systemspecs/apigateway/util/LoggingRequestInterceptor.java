package ng.com.systemspecs.apigateway.util;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Kelvin Isievwore
 * @date 12/08/2020
 */
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

    public LoggingRequestInterceptor() {
    }

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws
        IOException {
        this.logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        ClientHttpResponse clientHttpResponse = this.logResponse(response);
        return clientHttpResponse;
    }

    private void log(HttpRequest request, byte[] body, ClientHttpResponse response) throws IOException {
        this.logRequest(request, body);
        this.logResponse(response);
    }

    private ClientHttpResponse logResponse(ClientHttpResponse response) throws IOException {
        final ClientHttpResponse responseCopy = new BufferingClientHttpResponseWrapper(response);

        logger.debug("============================response begin==========================================");
        logger.debug("Status code  : {}" + response.getStatusCode());
        logger.debug("Status text  : {}" + response.getStatusText());
        logger.debug("Headers      : {}" + response.getHeaders());
        logger.debug("Response body: {}" + convertToString(responseCopy.getBody()));
        logger.debug("=======================response end=================================================");
        return responseCopy;
    }

    private String convertToString(InputStream body) {
        StringWriter writer = new StringWriter();

        try {
            IOUtils.copy(body, writer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    private void logRequest(HttpRequest request, byte[] body) {
        logger.debug("===========================request begin================================================");
        logger.debug("URI         : {}" + request.getURI());
        logger.debug("Method      : {}" + request.getMethod());
        logger.debug("Headers     : {}" + request.getHeaders());
        logger.debug("Request body: {}" + new String(body, StandardCharsets.UTF_8));
        logger.debug("==========================request end================================================");
    }
}
