/*
 * Copyright (c) 2016. Crane(hehebaiy@gmail.com)
 */
package com.example.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.template.TemplateLocation;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.util.ErrorHandler;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;

import javax.annotation.PostConstruct;
import javax.servlet.Servlet;
import java.io.IOException;

/**
 * Beetl模版自动配置
 */
@Configuration
@EnableConfigurationProperties({BeetlProperties.class})
@ConditionalOnClass({GroupTemplate.class, org.beetl.core.Configuration.class, ErrorHandler.class, ResourceLoader.class, WebAppResourceLoader.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
public class BeetlAutoConfiguration {
    private static final Log logger = LogFactory.getLog(BeetlAutoConfiguration.class);


    public BeetlAutoConfiguration() {
    }

    @Configuration
    @ConditionalOnWebApplication
    protected static class BeetlResourceHandlingConfig {
        protected BeetlResourceHandlingConfig() {
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnEnabledResourceChain
        public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
            return new ResourceUrlEncodingFilter();
        }
    }

    @Configuration
    @ConditionalOnClass({Servlet.class})
    @ConditionalOnWebApplication
    protected static class BeetlViewResolverConfiguration {
        @Autowired
        private BeetlProperties properties;

        @Autowired
        private BeetlGroupUtilConfiguration beetlGroupUtilConfiguration;

        @Autowired
        private ApplicationContext applicationContext;

        protected BeetlViewResolverConfiguration() {
        }

        @Bean
        public BeetlGroupUtilConfiguration beetlGroupUtilConfiguration() {

            BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();

            ResourcePatternResolver patternResolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
            if (properties.getRoot() != null) {
                try {
                    String root = patternResolver.getResource(properties.getRoot()).getFile().getPath();
                    logger.info("BeetlGroupUtilConfiguration root path: " + root);

                    WebAppResourceLoader resourceLoader = new WebAppResourceLoader(root);
                    beetlGroupUtilConfiguration.setResourceLoader(resourceLoader);
                } catch (IOException e) {
                    logger.error("BeetlGroupUtilConfiguration root path error.", e);
                }
            }

            if (properties.getConfig() != null) {
                beetlGroupUtilConfiguration.setConfigFileResource(patternResolver.getResource(properties.getConfig()));
            }

            beetlGroupUtilConfiguration.init();
            return beetlGroupUtilConfiguration;
        }

        @Bean
        @ConditionalOnMissingBean(name = {"beetlSpringViewResolver"})
        @ConditionalOnProperty(name = {"spring.beetl.enabled"}, matchIfMissing = true)
        public BeetlSpringViewResolver beetlSpringViewResolver() {
            BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
            beetlSpringViewResolver.setSuffix(this.properties.getSuffix());
            beetlSpringViewResolver.setPrefix(this.properties.getPrefix());
            beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
            beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
            beetlSpringViewResolver.setOrder(this.properties.getOrder());
            return beetlSpringViewResolver;
        }
    }


    @Configuration
    @ConditionalOnMissingBean(name = {"defaultTemplateResolver"})
    public static class DefaultTemplateResolverConfiguration {
        @Autowired
        private BeetlProperties properties;
        @Autowired
        private ApplicationContext applicationContext;

        @Autowired
        private BeetlGroupUtilConfiguration beetlGroupUtilConfiguration;

        public DefaultTemplateResolverConfiguration() {
        }

        @PostConstruct
        public void checkTemplateLocationExists() {
            boolean checkTemplateLocation = this.properties.isCheckTemplateLocation();
            if (checkTemplateLocation) {
                TemplateLocation location = new TemplateLocation(this.properties.getPrefix());
                if (!location.exists(this.applicationContext)) {
                    BeetlAutoConfiguration.logger.warn("Cannot find template location: " + location + " (please add some templates or check " + "your Beetl configuration)");
                }
            }
        }

        @Bean
        public BeetlSpringViewResolver defaultTemplateResolver() {
            BeetlSpringViewResolver resolver = new BeetlSpringViewResolver();
            resolver.setSuffix(this.properties.getSuffix());
            resolver.setPrefix(this.properties.getPrefix());
            resolver.setConfig(beetlGroupUtilConfiguration);
            return resolver;
        }

    }
}

