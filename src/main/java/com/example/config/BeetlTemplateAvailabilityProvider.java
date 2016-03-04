/*
 * Copyright (c) 2015-2016. Crane(hehebaiy@gmail.com)
 */
package com.example.config;

import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

/**
 * Created by crane on 16/2/26.
 */
public class BeetlTemplateAvailabilityProvider implements TemplateAvailabilityProvider {
    @Override
    public boolean isTemplateAvailable(String view, Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader) {
        if (ClassUtils.isPresent("org.beetl.core.GroupTemplate", classLoader)) {
            RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "spring.beetl.");
            String loaderPath = resolver.getProperty("root", "classpath:/templates/");
            String prefix = resolver.getProperty("prefix", "");
            String suffix = resolver.getProperty("suffix", ".html");
            return resourceLoader.getResource(loaderPath + prefix + view + suffix).exists();
        } else {
            return false;
        }
    }
}