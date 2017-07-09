package com.sam.geolocationapp.utility;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sam.geolocationapp.exceptions.ErrorCodes;
import com.sam.geolocationapp.exceptions.ExceptionType;
import com.sam.geolocationapp.exceptions.GeoLocationAppException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Samok Prasad Sinha
 *
 * This class provides various JJWT utility functionality for Authentication and Generation of API key
 * used through out the application.
 */

@Component
public class GeoLocationAppTokenUtil {

	public static final Logger LOGGER = LogManager.getLogger(GeoLocationAppTokenUtil.class);
	
	@Value("${com.sam.geolocation.token.type}")
	protected String tokentype;
	
	@Value("${com.sam.geolocation.token.expirationTime}")
	protected String tokenExpirationtime;
	
	@Value("${com.sam.geolocation.token.issuer}")
	protected String tokenIssuer;
	
	@Value("${com.sam.geolocation.token.signingKey}")
	protected String tokenSignKey;
	
	/**
	 * @param emailId
	 * @return
	 * @throws GeoLocationAppException
	 * 
	 * It creates the Access Token based on emailId and returns.
	 * If any Exception occurs it wraps that exception into GeoLocationAppException and throws back to the calling code.
	 */
	public String createAccessToken(String emailId) 
			throws GeoLocationAppException {
		
		LOGGER.info("In GeoLocationAppTokenUtil:createAccessToken : Starts Executing : with : emailId : " +emailId);
		
		SignatureAlgorithm signatureAlgorithm = null;
		Claims claims = null;
        try {
	        signatureAlgorithm = SignatureAlgorithm.HS256;
	        claims = Jwts.claims().setSubject(emailId);
	        claims.put(GeoLocationAppConstants.TOKEN_TYPE_KEY, tokentype);
	        claims.put(GeoLocationAppConstants.EMAIL_ID_KEY, emailId);
	        return Jwts
	                .builder()
	                .setIssuer(tokenIssuer)
	                .setClaims(claims)
	                .setExpiration(getExpiryDate(Integer.parseInt(tokenExpirationtime)))
	                .setIssuedAt(new Date())
	                .setId(UUID.randomUUID().toString())
	                .signWith(signatureAlgorithm, tokenSignKey)
	                .compact();
        } catch (Exception e) {
        	throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
        } finally {
        	LOGGER.info("In GeoLocationAppTokenUtil:createAccessToken : Completes Execution");
        }
    }
	
	/**
	 * @param token
	 * @return
	 * @throws GeoLocationAppException
	 * 
	 * It validates the access token and if found invalid then throws proper exception.
	 * If any Exception occurs it wraps that exception into GeoLocationAppException and throws back to the calling code.
	 */
	public boolean isValid(String token) 
			throws GeoLocationAppException {
		boolean isValidFlag = false;
        try {
            Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token.trim());
            isValidFlag = true;
        } catch (ExpiredJwtException eje) {
        	throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_105);
        } catch (Exception e) {
        	throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_106); 
        } 
        
        return isValidFlag;
    }
	
	/**
	 * @param auth
	 * @return
	 * 
	 * It removes "Bearer " phrase from the access token and returns.
	 */
	public String extractJwtTokenFromAuthorizationHeader(String auth) {
	    return auth.replaceFirst(GeoLocationAppConstants.ACCESS_TOKEN_PREFIX_VALUE, GeoLocationAppConstants.EMPTY_DELIMITER_VALUE)
	    		   .replace(GeoLocationAppConstants.WHITE_SPACE_DELIMITER_VALUE, GeoLocationAppConstants.EMPTY_DELIMITER_VALUE);
	}
	
	/**
	 * @param jwsToken
	 * @return
	 * @throws GeoLocationAppException
	 * 
	 * It extracts access token type value and returns.
	 * If any Exception occurs it wraps that exception into GeoLocationAppException and throws back to the calling code.
	 */
	public String getTokenType(String jwsToken) 
			throws GeoLocationAppException {
		
		String tokenType = null;
		try {
	        if (isValid(jwsToken)) {
	            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(jwsToken);
	            tokenType = claimsJws.getBody().get(GeoLocationAppConstants.TOKEN_TYPE_KEY).toString();
	        }
		} catch (GeoLocationAppException gae) {
			throw gae;
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		}
		
        return tokenType;
    }
	
    /**
     * @param jwsToken
     * @return
     * @throws GeoLocationAppException
     * 
     * It extracts email id value from access token and return.
     * If any Exception occurs it wraps that exception into GeoLocationAppException and throws back to the calling code.
     */
    public String getEamilId(String jwsToken) 
    		throws GeoLocationAppException {
    	
    	String emailId = null;
		try {
	        if (isValid(jwsToken)) {
	            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(jwsToken);
	            emailId = claimsJws.getBody().get(GeoLocationAppConstants.EMAIL_ID_KEY).toString();
	        }
		} catch (GeoLocationAppException gae) {
			throw gae;
		} catch (Exception e) {
			throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
		}
		
        return emailId;
    }
    
    /**
     * @param httpHeadersMap
     * @return
     * @throws GeoLocationAppException
     * 
     * It extracts out email id from httpHeadersMap object and return it.
     * If any Exception occurs it wraps that exception into GeoLocationAppException and throws back to the calling code.
     */
    public String getEmailId(Map<String, String> httpHeadersMap) 
    		throws GeoLocationAppException {
    	
    	String authorizationHeader = null;
    	String authorizationtoken = null;
    	String emailId = null;
    	try {
    		if(httpHeadersMap.containsKey(GeoLocationAppConstants.TOKEN_HEADER_PARAM)) {
    			authorizationHeader = httpHeadersMap.get(GeoLocationAppConstants.TOKEN_HEADER_PARAM);
    		} else {
    			throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_107);
    		}
    		
    		if(GeoLocationAppUtility.validateString(authorizationHeader)) {
    			authorizationtoken = this.extractJwtTokenFromAuthorizationHeader(authorizationHeader);
    			emailId = this.getEamilId(authorizationtoken);
    		} else {
    			throw new GeoLocationAppException(ExceptionType.BUSINESS, ErrorCodes.ERROR_CODE_108);
    		}
    	} catch (GeoLocationAppException gae){
    		throw gae;
    	} catch (Exception e){
    		throw new GeoLocationAppException(ExceptionType.TECHNNICAL, e);
    	}
    	
    	return emailId;
    }
	
	/**
	 * @param minutes
	 * @return
	 * 
	 * It created access token expiry date based on minutes and returns.
	 */
	private Date getExpiryDate(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
	
}
