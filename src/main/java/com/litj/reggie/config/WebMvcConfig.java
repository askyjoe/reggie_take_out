package com.litj.reggie.config;

import com.litj.reggie.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("" + "/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("" + "/front/**").addResourceLocations("classpath:/front/");

    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters){
        MappingJackson2HttpMessageConverter messagingConverter = new MappingJackson2HttpMessageConverter();
        messagingConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0, messagingConverter);
    }
}
