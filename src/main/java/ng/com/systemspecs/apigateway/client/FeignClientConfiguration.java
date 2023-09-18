package ng.com.systemspecs.apigateway.client;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class FeignClientConfiguration {

    @Value("${app.serm}")
    private String serm;

    @Value("${app.dough}")
    private String dough;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("Remita", "0841011021@006#1");
    }
}
