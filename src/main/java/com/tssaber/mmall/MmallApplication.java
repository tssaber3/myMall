package com.tssaber.mmall;

import com.tssaber.mmall.interceptor.CorsFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@MapperScan("com.tssaber.mmall.dao")
@SpringBootApplication
@EnableTransactionManagement
public class MmallApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(MmallApplication.class, args);
    }


    @Bean
    public FilterRegistrationBean testFilterRegistration(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter());
        filterRegistrationBean.addUrlPatterns("/**");
        filterRegistrationBean.setName("testFilter");
        return filterRegistrationBean;
    }

    /**
     * 跨域问题
     * @return
     */
//    @Bean
//    public org.springframework.web.filter.CorsFilter corsFilter(){
//        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        final CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.addAllowedMethod("*");
//        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.addAllowedHeader("*");
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
//        return new org.springframework.web.filter.CorsFilter(urlBasedCorsConfigurationSource);
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }
}
