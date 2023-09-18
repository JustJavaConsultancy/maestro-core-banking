package ng.com.systemspecs.apigateway.config;

import ng.com.systemspecs.apigateway.security.AuthoritiesConstants;
import ng.com.systemspecs.apigateway.security.jwt.JWTConfigurer;
import ng.com.systemspecs.apigateway.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.ArrayList;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(TokenProvider tokenProvider, CorsFilter corsFilter, SecurityProblemSupport problemSupport) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off

        ArrayList<String> strings = new ArrayList<>();
        strings.add("http://localhost:8086");

        http

            .csrf()
            .disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport)
            .and()
            .headers()
            .contentSecurityPolicy("default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:")
        .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
        .and()
            .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'")
            .and()
            .frameOptions()
            .deny()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/get-last").permitAll()
            .antMatchers("/api/available/**").permitAll()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/register/nin").permitAll()
            .antMatchers("/api/register/corporate").permitAll()
            .antMatchers("/api/register/corporate/wallet").permitAll()
            .antMatchers("/api/register/**").permitAll()
            .antMatchers("/ipg/kafka/newCustomer").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/forgot/password").permitAll()
            .antMatchers("/api/changelostpassword").permitAll()
            .antMatchers("/api/verify-otp/**").permitAll()
            .antMatchers("/api/send-otp/**").permitAll()
            .antMatchers("/api/validate/**").permitAll()
            .antMatchers("/api/validate-otp/**/**").permitAll()
            .antMatchers("/api/fund-wallet/**").permitAll()
            .antMatchers("/api/inline-payment").permitAll()
            .antMatchers("/api/images/**").permitAll()
            .antMatchers("/api/documents/kyc/**").permitAll()
            .antMatchers("/api/account/open").permitAll()
            .antMatchers("/api/demo/fund-wallet/**").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            .antMatchers("/api/validate/account/**").permitAll()
            .antMatchers("/api/check").permitAll()
            .antMatchers("/api/authenticate/otp/**").permitAll()
            .antMatchers("/api/request/otp").permitAll()
            .antMatchers("/api/ipg/api-authenticate").permitAll()
            .antMatchers("/api/api-authenticate").permitAll()
            .antMatchers("/api/account/name").permitAll()
            .antMatchers("/api/account/statement").permitAll()
            .antMatchers("/api/payment/status").permitAll()
            .antMatchers("/api/cgate/payment/notification").permitAll()
            .antMatchers("/api/cash-connect/bvn").permitAll()
            .antMatchers("/api/cash-connect/payment/notification").permitAll()
            .antMatchers("/api/cgate/details").permitAll()
            .antMatchers("/api/cgate/notification").permitAll()
            .antMatchers("/api/account/balance/**").permitAll()
            .antMatchers("/api/wallet-external").permitAll()
            .antMatchers("/api/customer/accounts/**").permitAll()
            .antMatchers("/api/remita/notification").permitAll()
            .antMatchers("/api/account/validate/password").permitAll()
            .antMatchers("/api/account/validate/secret-question").permitAll()
            .antMatchers("/api/account/forgot-pin").permitAll()
            .antMatchers("/api/account/nin/**/**").permitAll()
            .antMatchers("/api/account/nin/**").permitAll()
            .antMatchers("/api/pay/post/**").permitAll()
            .antMatchers("/api/add-new-wallet-account").permitAll()
            .antMatchers("/api/ussd/wallet").permitAll()
            .antMatchers("/api/agents").permitAll()
            .antMatchers("/api/profiles/pics/**").permitAll()
            .antMatchers("/api/cgate/reference/**/**").permitAll()
            .antMatchers("/api/banks/commercial").permitAll()
            .antMatchers("/api/banks/refresh").permitAll()
            .antMatchers("/api/wallets/avs/**").permitAll()
            .antMatchers("/api/contact-form").permitAll()
            .antMatchers("/api/contact-form/support/**").permitAll()
            .antMatchers("/api/tellers").permitAll()
            .antMatchers("/api/vulte/webhook").permitAll()
            .antMatchers("/api/cash-connect/bvn/validate").permitAll()
            .antMatchers("/api/application/env/profile/**").permitAll()
            .antMatchers("/api/ibile/get-receipts/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/super-agents").permitAll()
            .antMatchers(HttpMethod.GET, "/api/schemes").permitAll()
            .antMatchers("/api/rrr/status/**").hasAnyAuthority(AuthoritiesConstants.OPERATIONS, AuthoritiesConstants.ADMIN)
            .antMatchers("/api/send-money-correspondent/**").hasAnyAuthority(AuthoritiesConstants.OPERATIONS)
            .antMatchers("/api/send-money-bank-correspondent/**").hasAnyAuthority(AuthoritiesConstants.OPERATIONS)
            .antMatchers("/api/wallet-account/status/**").hasAnyAuthority(AuthoritiesConstants.OPERATIONS)
            .antMatchers(HttpMethod.POST, "/api/schemes").hasAnyAuthority(AuthoritiesConstants.ADMIN,
                AuthoritiesConstants.OPERATIONS)
            .antMatchers(HttpMethod.POST, "/api/kyc-requests/{phoneNumber}/{kyc}").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.OPERATIONS)
            .antMatchers(HttpMethod.POST, "/api/schemes/callback").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.OPERATIONS)
            .antMatchers(HttpMethod.POST, "/profile/change-profile-id/{phoneNumber}/{profileId}").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.OPERATIONS)
            .antMatchers(HttpMethod.PUT, "/update-user-role").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.OPERATIONS)
            .antMatchers(HttpMethod.PUT, "/schemes/update-keys").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.OPERATIONS)
            .antMatchers(HttpMethod.DELETE, "/delete-wallet/{accountNumber}").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.OPERATIONS)
            .antMatchers(HttpMethod.GET, "/roles/{phoneNumber}").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.OPERATIONS)
            .antMatchers("/api/approval-requests").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.OPERATIONS, AuthoritiesConstants.SUPPORT)
            .antMatchers("/api/authority").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.ANALYST)
            .antMatchers("/api/string-encrypt/**").permitAll()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/shutdown").permitAll()
            .antMatchers("/management/prometheus").permitAll()
            .antMatchers("/management/logfile").permitAll()
            .antMatchers("/api/callback").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/**").authenticated()
        .and()
            .apply(securityConfigurerAdapter());
        // @formatter:ongggg  super-agents
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
