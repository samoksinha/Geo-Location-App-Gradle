package com.sam.geolocationapp.unitTest;

import java.util.Map;

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
import com.sam.geolocationapp.models.GeoLocationAppRequest;
import com.sam.geolocationapp.models.GeoLocationAppResponse;
import com.sam.geolocationapp.utility.GeoLocationAppConstants;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT, classes=GeoLocationAppInitializer.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application.properties")
public class GeoLocationAddShopTest {
	
	public static final Logger LOGGER = LogManager.getLogger(GeoLocationAddShopTest.class);

	@Autowired
    private TestRestTemplate restTemplateClient;
	
	@SuppressWarnings("unchecked")
	@Test
	public void addShop() throws Exception {
		
		GeoLocationAppResponse geoLocationAppResponse = null;
		HttpHeaders headers = null;
		HttpEntity<String> httpGetEntity = null;
		HttpEntity<GeoLocationAppRequest> httpPostEntity = null;
		final String getApiKeyUrl = "/map/getapikey/v1";
		
		ResponseEntity<GeoLocationAppResponse> responseEntity = null;
		ObjectMapper objectMapper = null;
		String geoLocationAppResponseString = null;
		
		Map<String, Object> responseMap = null;
		String authorizationKey = null;
		final String addShopUrl = "/map/addshop/v1";
		try {
			objectMapper = new ObjectMapper();
			
			headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.add("emailId", "samok.sinha@gmail.com");
		    
		    httpGetEntity = new HttpEntity<String>("parameters", headers);
		    
		    responseEntity = restTemplateClient.exchange(getApiKeyUrl, HttpMethod.GET, 
		    									httpGetEntity, GeoLocationAppResponse.class);
		    geoLocationAppResponse = responseEntity.getBody();
		    
		    geoLocationAppResponseString = objectMapper.writeValueAsString(geoLocationAppResponse);
		    LOGGER.info("In GeoLocationControllerTest:addShop : geoLocationAppResponseString : " +geoLocationAppResponseString);
		    LOGGER.info("In GeoLocationControllerTest:addShop : Status : " +responseEntity.getStatusCode());
		    
		    responseMap = (Map<String, Object>) geoLocationAppResponse.getResponseObject();
		    authorizationKey = (String) responseMap.get(GeoLocationAppConstants.ACCESS_TOKEN_KEY);
		    authorizationKey = "Bearer " +authorizationKey;
		    headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.add("authorization", authorizationKey);
		    
		    GeoLocationAppRequest geoLocationAppRequest = new GeoLocationAppRequest();
		    geoLocationAppRequest.setShopName("Test_Name_1");
		    geoLocationAppRequest.setShopAddress("Test_Address_1");
		    geoLocationAppRequest.setShopPincode(700078);
		    httpPostEntity = new HttpEntity<GeoLocationAppRequest>(geoLocationAppRequest, headers);
		   
		    responseEntity = restTemplateClient.exchange(addShopUrl, HttpMethod.POST, 
		    											 httpPostEntity, GeoLocationAppResponse.class);
		    geoLocationAppResponse = responseEntity.getBody();
		    geoLocationAppResponseString = objectMapper.writeValueAsString(geoLocationAppResponse);
		    LOGGER.info("In GeoLocationControllerTest:addShop : geoLocationAppResponseString : " +geoLocationAppResponseString);
		    LOGGER.info("In GeoLocationControllerTest:addShop : Status : " +responseEntity.getStatusCode());
		    
		    geoLocationAppRequest = new GeoLocationAppRequest();
		    geoLocationAppRequest.setShopName("Test_Name_1");
		    geoLocationAppRequest.setShopAddress("Test_Address_1");
		    geoLocationAppRequest.setShopPincode(700048);
		    httpPostEntity = new HttpEntity<GeoLocationAppRequest>(geoLocationAppRequest, headers);
		   
		    responseEntity = restTemplateClient.exchange(addShopUrl, HttpMethod.POST, 
		    											 httpPostEntity, GeoLocationAppResponse.class);
		    geoLocationAppResponse = responseEntity.getBody();
		    geoLocationAppResponseString = objectMapper.writeValueAsString(geoLocationAppResponse);
		    LOGGER.info("In GeoLocationControllerTest:addShop : geoLocationAppResponseString : " +geoLocationAppResponseString);
		    LOGGER.info("In GeoLocationControllerTest:addShop : Status : " +responseEntity.getStatusCode());
		} catch (Exception e) {
			LOGGER.error("In GeoLocationControllerTest:addShop : Exception Occured : Statcktrace : " ,e);
		}
	}
}
