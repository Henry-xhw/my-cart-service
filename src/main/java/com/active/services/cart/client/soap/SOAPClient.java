package com.active.services.cart.client.soap;

import com.active.services.product.api.v1.soap.ProductServiceSOAPEndPoint;

import org.apache.cxf.jaxws.spring.JaxWsProxyFactoryBeanDefinitionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Lazy
public class SOAPClient {
    @Value("${url.productServiceSOAPV1}")
    private String productServiceSOAPV1;

    @Autowired
    private ApplicationContext appContext;

    @Bean
    public ProductServiceSOAPEndPoint productServiceSOAPEndPoint() {
        ProductServiceSOAPEndPoint target = buildClientService(productServiceSOAPV1, ProductServiceSOAPEndPoint.class);

        return target;
    }

    private <T> T buildClientService(String url, Class<T> serviceClass) {
        JaxWsProxyFactoryBeanDefinitionParser.JAXWSSpringClientProxyFactoryBean factoryBean =
                new JaxWsProxyFactoryBeanDefinitionParser.JAXWSSpringClientProxyFactoryBean();
        factoryBean.setAddress(url);
        factoryBean.setServiceClass(serviceClass);
        factoryBean.setApplicationContext(appContext);

        Map<String, Object> properties = new HashMap<>();
        properties.put("schema-validation-enabled", "false");
        properties.put("set-jaxb-validation-event-handler", "false");
        factoryBean.setProperties(properties);

        try {
            @SuppressWarnings("unchecked")
            T target =  (T) factoryBean.getObject();

            return target;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
