package com.sam.geolocationapp.boot;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Samok Sinha
 *
 * This class provides configuration related integration with Swagger Framework.
 */

@Configuration
@EnableSwagger2
public class GeoLocationSwaggerConfig {
	
	@Value("${com.sam.geolocation.swagger.controller.scan.path}")
	protected String swaggerControllerScan;
	
	@Value("${com.sam.geolocation.swagger.url.scan.path}")
	protected String swaggerPathScan;
	
	@Value("${com.sam.geolocation.swagger.metadata.title}")
	protected String swaggerMeaDataTitle;
	
	@Value("${com.sam.geolocation.swagger.metadata.description}")
	protected String swaggerMeaDataDescription;
	
	@Value("${com.sam.geolocation.swagger.metadata.version}")
	protected String swaggerMeaDataVersion;
	
	@Value("${com.sam.geolocation.swagger.metadata.terms}")
	protected String swaggerMeaDataTerms;
	
	@Value("${com.sam.geolocation.swagger.metadata.author.name}")
	protected String swaggerMeaDataAuthorName;
	
	@Value("${com.sam.geolocation.swagger.metadata.author.url}")
	protected String swaggerMeaDataAuthorUrl;
	
	@Value("${com.sam.geolocation.swagger.metadata.author.email}")
	protected String swaggerMeaDataAuthorEmail;
	
	@Value("${com.sam.geolocation.swagger.metadata.license}")
	protected String swaggerMeaDataLicense;
	
	@Value("${com.sam.geolocation.swagger.metadata.license.url}")
	protected String swaggerMeaDataLicenseUrl;
	
	/**
	 * @return
	 * 
	 * This methods configures the intercepting of application exposed URL's and returns Docker object for Swagger
	 * implementation.
	 */
	@Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()                 
                .apis(RequestHandlerSelectors.basePackage(swaggerControllerScan))
                .paths(regex(swaggerPathScan))
                .build()
                .apiInfo(metaData());
    }
	
	/**
	 * @return
	 * 
	 * This methods configures Application Meta Data information like application title, description
	 * author etc. and returned ApiInfo object for Swagger implementation.
	 */
	private ApiInfo metaData() {
		ApiInfo apiInfo = new ApiInfo(swaggerMeaDataTitle, swaggerMeaDataDescription, 
									  swaggerMeaDataVersion, swaggerMeaDataTerms,
									  new Contact(swaggerMeaDataAuthorName, swaggerMeaDataAuthorUrl, swaggerMeaDataAuthorEmail),
									  swaggerMeaDataLicense, swaggerMeaDataLicenseUrl);
        return apiInfo;
	}
	
}
