package ng.com.justjava.corebanking.config;

import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.cache.PrefixedKeyGenerator;
import ng.com.justjava.corebanking.domain.*;
import ng.com.justjava.corebanking.repository.UserRepository;
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
            createCache(cm, UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, User.class.getName());
            createCache(cm, Authority.class.getName());
            createCache(cm, User.class.getName() + ".authorities");
            createCache(cm, Profile.class.getName());
            createCache(cm, Profile.class.getName() + ".walletAccounts");
            createCache(cm, Profile.class.getName() + ".paymentTransactions");
            createCache(cm, Profile.class.getName() + ".billerTransactions");
            createCache(cm, Profile.class.getName() + ".customersubscriptions");
            createCache(cm, Address.class.getName());
            createCache(cm, ProfileType.class.getName());
            createCache(cm, Kyclevel.class.getName());
            createCache(cm, Biller.class.getName());
            createCache(cm, Biller.class.getName() + ".customersubscriptions");
            createCache(cm, Biller.class.getName() + ".billerPlatforms");
            createCache(cm, WalletAccount.class.getName());
            createCache(cm, Scheme.class.getName());
            createCache(cm, SchemeCategory.class.getName());
            createCache(cm, PaymentTransaction.class.getName());
            createCache(cm, BillerTransaction.class.getName());
            createCache(cm, Customersubscription.class.getName());
            createCache(cm, BillerPlatform.class.getName());
            createCache(cm, BillerCategory.class.getName());
            createCache(cm, BillerCategory.class.getName() + ".billers");
            createCache(cm, WalletAccountType.class.getName());
            createCache(cm, CountrolAccount.class.getName());
            createCache(cm, DoubleEntryLogger.class.getName());
            createCache(cm, ApprovalGroup.class.getName());
            createCache(cm, ApprovalGroup.class.getName() + ".profiles");
            createCache(cm, ApprovalWorkflow.class.getName());
            createCache(cm, Notification.class.getName());
            createCache(cm, BonusPoint.class.getName());
            createCache(cm, BonusPoint.class.getName() + ".profiles");
            createCache(cm, ContactUs.class.getName());
            createCache(cm, MyDevice.class.getName());
            createCache(cm, Lender.class.getName());
            createCache(cm, InsuranceType.class.getName());
            createCache(cm, SuperAgent.class.getName());
            createCache(cm, InsuranceProvider.class.getName());
            createCache(cm, KycRequest.class.getName());
            createCache(cm, SweepingConfig.class.getName());
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
