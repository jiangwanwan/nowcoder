package com.wanwan.nowcoder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @Author: Wanwan Jiang
 * @Description:
 * @Date: Created in 21:09 2017/10/9
 * @Modified By:
 * @Email: jiangwanwan0327@163.com
 */
public class WebConfig extends WebMvcConfigurerAdapter{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/temp/").addResourceLocations("classpath:/template/");
    }

    @Bean
    public InternalResourceViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/temp/");
        viewResolver.setSuffix(".ftl");
        return viewResolver;
    }
}
