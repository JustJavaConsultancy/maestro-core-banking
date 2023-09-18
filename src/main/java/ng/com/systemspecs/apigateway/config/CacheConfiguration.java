package ng.com.systemspecs.apigateway.config;

import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.cache.PrefixedKeyGenerator;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    
    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, ng.com.systemspecs.apigateway.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, ng.com.systemspecs.apigateway.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, ng.com.systemspecs.apigateway.domain.User.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.Authority.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.User.class.getName() + ".authorities");
            createCache(cm, ng.com.systemspecs.apigateway.domain.Profile.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.Profile.class.getName() + ".walletAccounts");
            createCache(cm, ng.com.systemspecs.apigateway.domain.Profile.class.getName() + ".paymentTransactions");
            createCache(cm, ng.com.systemspecs.apigateway.domain.Profile.class.getName() + ".billerTransactions");
            createCache(cm, ng.com.systemspecs.apigateway.domain.Profile.class.getName() + ".customersubscriptions");
            createCache(cm, ng.com.systemspecs.apigateway.domain.Address.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.ProfileType.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.Kyclevel.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.Biller.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.Biller.class.getName() + ".customersubscriptions");
            createCache(cm, ng.com.systemspecs.apigateway.domain.Biller.class.getName() + ".billerPlatforms");
            createCache(cm, ng.com.systemspecs.apigateway.domain.WalletAccount.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.Scheme.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.SchemeCategory.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.PaymentTransaction.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.BillerTransaction.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.Customersubscription.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.BillerPlatform.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.BillerCategory.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.BillerCategory.class.getName() + ".billers");
            createCache(cm, ng.com.systemspecs.apigateway.domain.WalletAccountType.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.CountrolAccount.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.DoubleEntryLogger.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.ApprovalGroup.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.ApprovalGroup.class.getName() + ".profiles");
            createCache(cm, ng.com.systemspecs.apigateway.domain.ApprovalWorkflow.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.Notification.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.BonusPoint.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.BonusPoint.class.getName() + ".profiles");
            createCache(cm, ng.com.systemspecs.apigateway.domain.ContactUs.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.MyDevice.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.Lender.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.InsuranceType.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.SuperAgent.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.InsuranceProvider.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.KycRequest.class.getName());
            createCache(cm, ng.com.systemspecs.apigateway.domain.SweepingConfig.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
