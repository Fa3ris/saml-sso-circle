package com.artik.saml.web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer {
	
	
	public void addViewControllers(ViewControllerRegistry registry) {
		//registry.addViewController("/home").setViewName("home");
		//registry.addViewController("/").setViewName("home");
		registry.addViewController("/hello").setViewName("hello");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/logout-success").setViewName("logout-success");
	}
	
	
}
