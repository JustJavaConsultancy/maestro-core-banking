package ng.com.systemspecs.apigateway.config;

import io.github.jhipster.config.JHipsterConstants;
import ng.com.systemspecs.apigateway.aop.logging.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile({
        JHipsterConstants.SPRING_PROFILE_DEVELOPMENT,
        JHipsterConstants.SPRING_PROFILE_PRODUCTION,
        "staging"})
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }
}
