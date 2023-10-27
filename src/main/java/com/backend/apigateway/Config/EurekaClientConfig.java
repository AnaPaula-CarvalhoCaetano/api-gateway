package com.backend.apigateway.Config;

import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EurekaClientConfig {

    @Bean
    public EurekaInstanceConfig eurekaInstanceConfig() {
        return new MyDataCenterInstanceConfig();
    }

    @Bean
    public EurekaInstanceConfig eurekaInstanceConfig2() {
        return new MyDataCenterInstanceConfig();
    }
}
