package com.sam.geolocationapp.unitTest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.geolocationapp.boot.GeoLocationAppInitializer;
import com.sam.geolocationapp.models.GeoLocationAppResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT, classes=GeoLocationAppInitializer.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application.properties")
public class GeoLocationGetApiKeyTest {
	
	public static final Logger LOGGER = LogManager.getLogger(GeoLocationGetApiKeyTest.class);

	@Autowired
    private TestRestTemplate restTemplateClient;
	
	@Test
	public void getApiKey() throws Exception {
		
		GeoLocationAppResponse geoLocationAppResponse = null;
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		final String getApiKeyUrl = "/map/getapikey/v1";
		
		ResponseEntity<GeoLocationAppResponse> responseEntity = null;
		ObjectMapper objectMapper = null;
		String geoLocationAppResponseString = null;
		try {
			objectMapper = new ObjectMapper();
			
			headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.add("emailId", "samok.sinha@gmail.com");
		    
		    httpEntity = new HttpEntity<String>("parameters", headers);
		    
		    responseEntity = restTemplateClient.exchange(getApiKeyUrl, HttpMethod.GET, 
														 httpEntity, GeoLocationAppResponse.class);
		    geoLocationAppResponse = responseEntity.getBody();
		    
		    geoLocationAppResponseString = objectMapper.writeValueAsString(geoLocationAppResponse);
		    LOGGER.info("In GeoLocationControllerTest:getApiKey : geoLocationAppResponseString : " +geoLocationAppResponseString);
		    LOGGER.info("In GeoLocationControllerTest:getApiKey : Status : " +responseEntity.getStatusCode());
		} catch (Exception e) {
			LOGGER.error("In GeoLocationControllerTest:getApiKey : Exception Occured : Statcktrace : " ,e);
		}
	}
	
}
