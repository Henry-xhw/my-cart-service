package com.active.services.cart.client.soap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy
public class SOAPClient {
    @Value("${url.ledgerService}")
    private String ledgerServiceUrl;

    @Value("${url.contractService}")
    private String contractServiceUrl;

    @Value("${url.inventoryService}")
    private String inventoryServiceUrl;

    @Value("${url.agencyManagementService}")
    private String agencyManagementServiceUrl;

    @Value("${url.inventoryOMSService}")
    private String inventoryOMSServiceUrl;

    @Autowired
    private ApplicationContext appContext;

//    @Bean
//    public ContractServiceSOAPEndPoint contractServiceSOAPEndPoint() {
//        ContractServiceSOAPEndPoint target = buildClientService(contractServiceUrl, ContractServiceSOAPEndPoint.class);
//
//        return target;
//    }
//
//    @Bean
//    public ProductServiceSOAPEndPoint inventoryServiceSOAPEndPoint() {
//        InventoryServiceSOAPEndPoint target = buildClientService(inventoryServiceUrl, InventoryServiceSOAPEndPoint.class);
//
//        return target;
//    }
//
//    @Bean
//    public AgencyManagementServiceSOAPEndPoint agencyManagementServiceSOAPEndPoint() {
//        AgencyManagementServiceSOAPEndPoint target = buildClientService(agencyManagementServiceUrl, AgencyManagementServiceSOAPEndPoint.class);
//
//        return target;
//    }
//
//    @Bean
//    public InventoryServiceOMSSOAPClient inventoryServiceOMSSOAPClient() {
//        InventoryServiceOMSSOAPClient target = buildClientService(inventoryOMSServiceUrl, InventoryServiceOMSSOAPClient.class);
//
//        return target;
//    }
//
//    private <T> T buildClientService(String url, Class<T> serviceClass) {
//        JaxWsProxyFactoryBeanDefinitionParser.JAXWSSpringClientProxyFactoryBean factoryBean =
//                new JaxWsProxyFactoryBeanDefinitionParser.JAXWSSpringClientProxyFactoryBean();
//        factoryBean.setAddress(url);
//        factoryBean.setServiceClass(serviceClass);
//        factoryBean.setApplicationContext(appContext);
//
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("schema-validation-enabled", "false");
//        properties.put("set-jaxb-validation-event-handler", "false");
//        factoryBean.setProperties(properties);
//
//        try {
//            @SuppressWarnings("unchecked")
//            T target =  (T) factoryBean.getObject();
//
//            return target;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
