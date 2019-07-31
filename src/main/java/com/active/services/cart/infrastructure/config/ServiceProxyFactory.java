package com.active.services.cart.infrastructure.config;

import lombok.Generated;

import org.apache.cxf.jaxws.spring.JaxWsProxyFactoryBeanDefinitionParser;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public final class ServiceProxyFactory {
    private ServiceProxyFactory() {
    }

    //For test mock
    public static <T> T buildClientService(ApplicationContext context, String url, Class<T> serviceClass,
        JaxWsProxyFactoryBeanDefinitionParser.JAXWSSpringClientProxyFactoryBean factoryBean) {
        Assert.notNull(context, "context can not be null");
        Assert.notNull(url, "url can not be null");
        Assert.notNull(serviceClass, "serviceClass can not be null");

        factoryBean.setAddress(url);
        factoryBean.setServiceClass(serviceClass);
        factoryBean.setApplicationContext(context);

        Map<String, Object> properties = new HashMap<>();
        properties.put("schema-validation-enabled", "false");
        properties.put("set-jaxb-validation-event-handler", "false");
        factoryBean.setProperties(properties);

        try {
            return (T) factoryBean.getObject();
        } catch (Exception e) {
            throw new ServiceProxyException(e);
        }
    }

    @Generated
    public static <T> T buildClientService(ApplicationContext context, String url, Class<T> serviceClass) {
        return buildClientService(context, url, serviceClass,
            new JaxWsProxyFactoryBeanDefinitionParser.JAXWSSpringClientProxyFactoryBean());
    }
}
