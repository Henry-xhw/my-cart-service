package com.active.services.cart.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

@Slf4j
public class DroolsConfig {
    private static final String RULES_PATH = "com/active/services/cart/domain/rule/";

    @Bean
    public KieFileSystem kieFileSystem() throws IOException {
        KieFileSystem kieFileSystem = getKieServices().newKieFileSystem();
        Resource[] rules = new PathMatchingResourcePatternResolver().getResources("classpath*:" + RULES_PATH + "**/*.drl");
        for (Resource file : rules) {
            String ruleFilePath = RULES_PATH + file.getFilename();
            LOG.debug("Kie file system loading rule file: {}", ruleFilePath);
            kieFileSystem.write(ResourceFactory.newClassPathResource(ruleFilePath, "UTF-8"));
        }

        return kieFileSystem;
    }

    @Bean
    public KieContainer kieContainer() throws IOException {
        long start = System.currentTimeMillis();
        final KieRepository kieRepository = getKieServices().getRepository();
        kieRepository.addKieModule(kieRepository::getDefaultReleaseId);
        KieBuilder kieBuilder = getKieServices().newKieBuilder(kieFileSystem());
        kieBuilder.buildAll();

        LOG.debug("Kie build takes {}ms", System.currentTimeMillis() - start);
        start = System.currentTimeMillis();

        LOG.info("Rule info: {}", kieBuilder.getResults().getMessages(Message.Level.INFO));
        LOG.warn("Rule warning: {}", kieBuilder.getResults().getMessages(Message.Level.WARNING));
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            LOG.error("Rule errors: {}", kieBuilder.getResults().getMessages(Message.Level.ERROR));
            throw new BeanInitializationException("Error creating KieContainer as error in rule");
        }

        KieContainer kieContainer = getKieServices().newKieContainer(kieRepository.getDefaultReleaseId());
        LOG.debug("Create kie container takes {}ms", System.currentTimeMillis() - start);

        return kieContainer;
    }

    private KieServices getKieServices() {
        return KieServices.Factory.get();
    }
}
