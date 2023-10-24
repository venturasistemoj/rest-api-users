package com.venturasistemoj.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application class that starts the server for the API.
 *
 * <code>@SpringBootApplication</code> is a convenience annotation that adds all of the following Java annotations:
 * <code>@Configuration</code>: Tags the class as a source of bean definitions for the application context.
 * <code>@EnableAutoConfiguration</code>: Tells Spring Boot to start adding beans based on classpath settings, other
 * beans, and various property settings. E.g., if spring-webmvc is on the classpath, this annotation flags the
 * application as a web application and activates key behaviors, such as setting up a DispatcherServlet.
 * <code>@ComponentScan</code>: Tells Spring to look for other components, configurations, and services,
 * letting it find the controllers.
 *
 * @author Wilson Ventura
 * @since 2023
 */

@SpringBootApplication
public class RestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}

}
