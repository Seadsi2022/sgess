package dsi.sea.sgess.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, dsi.sea.sgess.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, dsi.sea.sgess.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, dsi.sea.sgess.domain.User.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Authority.class.getName());
            createCache(cm, dsi.sea.sgess.domain.User.class.getName() + ".authorities");
            createCache(cm, dsi.sea.sgess.domain.Scode.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Scode.class.getName() + ".scodevaleurs");
            createCache(cm, dsi.sea.sgess.domain.Scodevaleur.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Scodevaleur.class.getName() + ".slocalites");
            createCache(cm, dsi.sea.sgess.domain.Scodevaleur.class.getName() + ".equestionnaires");
            createCache(cm, dsi.sea.sgess.domain.Scodevaleur.class.getName() + ".sstructures");
            createCache(cm, dsi.sea.sgess.domain.Slocalite.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Slocalite.class.getName() + ".sstructures");
            createCache(cm, dsi.sea.sgess.domain.Sstructure.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Sstructure.class.getName() + ".evaleurvariables");
            createCache(cm, dsi.sea.sgess.domain.Sstructure.class.getName() + ".scodes");
            createCache(cm, dsi.sea.sgess.domain.Setablissement.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Etypechamp.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Etypechamp.class.getName() + ".eattributs");
            createCache(cm, dsi.sea.sgess.domain.Eattribut.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Eattribut.class.getName() + ".etypechamps");
            createCache(cm, dsi.sea.sgess.domain.Evaleurattribut.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Evaleurattribut.class.getName() + ".eattributs");
            createCache(cm, dsi.sea.sgess.domain.Eunite.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Eunite.class.getName() + ".evariables");
            createCache(cm, dsi.sea.sgess.domain.Ecampagne.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Ecampagne.class.getName() + ".equestionnaires");
            createCache(cm, dsi.sea.sgess.domain.Equestionnaire.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Equestionnaire.class.getName() + ".egroupes");
            createCache(cm, dsi.sea.sgess.domain.Egroupe.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Egroupe.class.getName() + ".egroupevariables");
            createCache(cm, dsi.sea.sgess.domain.Etypevariable.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Etypevariable.class.getName() + ".evariables");
            createCache(cm, dsi.sea.sgess.domain.Evariable.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Evariable.class.getName() + ".eattributvariables");
            createCache(cm, dsi.sea.sgess.domain.Evariable.class.getName() + ".eeventualitevariables");
            createCache(cm, dsi.sea.sgess.domain.Evariable.class.getName() + ".egroupevariables");
            createCache(cm, dsi.sea.sgess.domain.Eattributvariable.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Eeventualite.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Eeventualite.class.getName() + ".eeventualitevariables");
            createCache(cm, dsi.sea.sgess.domain.Eeventualitevariable.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Egroupevariable.class.getName());
            createCache(cm, dsi.sea.sgess.domain.Egroupevariable.class.getName() + ".evaleurvariables");
            createCache(cm, dsi.sea.sgess.domain.Evaleurvariable.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
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
