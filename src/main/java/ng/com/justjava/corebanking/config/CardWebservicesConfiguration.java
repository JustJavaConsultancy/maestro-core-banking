package ng.com.justjava.corebanking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CardWebservicesConfiguration {

    @Value("${app.uri.webservices.domain-package}")
    private String domainPackage;

    @Value("${app.uri.webservices.base-uri}")
    private String webserviceUri;

    /*@Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(domainPackage);
        return marshaller;
    }

    @Bean
    public CardClient cardClient(Jaxb2Marshaller marshaller) {
        CardClient client = new CardClient();
        client.setDefaultUri(webserviceUri);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        ClientInterceptor[] interceptors = new ClientInterceptor[] {interceptor()};
        client.setInterceptors(interceptors);
        return client;
    }

    @Bean
    public SoapClientInterceptor interceptor() {
        return new SoapClientInterceptor();
    }*/

}
