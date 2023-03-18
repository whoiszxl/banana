package com.whoiszxl.strategy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author whoiszxl
 */
@Configuration
public class ServerGenerateStrategyConfig {

    @Bean
    public FactoryBean factoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(ServerGenerateStrategyFactory.class);
        return factoryBean;
    }
}
