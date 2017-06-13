package com.sam.geolocationapp.utility;

import java.util.HashMap;
import java.util.Map;

public abstract class GeoLocationAppConstants {
	
	public static final int GENERIC_ERROR_CODE = 101;
	public static final int INTERNAL_ERROR_CODE = 102;
	
	public static Map<String, String> GEO_LOCATION_ERROR_MAP = new HashMap<>();
	
	public static final String EMAIL_ID_KEY = "emailid";
	public static final String TOKEN_TYPE_KEY = "TokenType";
	public static final String ACCESS_TOKEN_KEY = "apikey";
	public static final String ACCESS_TOKEN_PREFIX_VALUE = "[B|b][E|e][A|a][R|r][E|e][R|r] ";
	public static final String TOKEN_HEADER_PARAM = "authorization";
	public static final String MDC_UNIQUE_LOG_ID = "uniqueLogId";
	
	public static final String STATUS_SUCCESS_VALUE = "Success";
	public static final String STATUS_FALIURE_VALUE = "Failure";
	
	public static final String EMPTY_DELIMITER_VALUE = "";
	public static final String WHITE_SPACE_DELIMITER_VALUE = " ";
	public static final String QUESTION_MARK_DELIMITER_VALUE = "?";
	public static final String EQUALS_DELIMITER_VALUE = "=";
	
	public static final String X_REAL_IP_KEY = "x-real-ip";
    public static final String X_FORWARDED_FOR_KEY = "x-forwarded-for";
    
    public static final String HIBERNATE_DIALECT_KEY = "hibernate.dialect";
    public static final String HIBERNATE_SHOW_SQL_KEY = "hibernate.show_sql";
    public static final String HIBERNATE_USE_SQL_COMMENTS_KEY = "hibernate.use_sql_comments";
    public static final String HIBERNATE_FORMAT_SQL_KEY = "hibernate.format_sql";
    public static final String HIBERNATE_HBM2DDL_AUTO_KEY = "hibernate.hbm2ddl.auto";
    public static final String HIBERNATE_GENERATE_STATISTICS_KEY = "hibernate.generate_statistics";
    
    public static final int CONSTANT1 = 180;
    public static final int CONSTANT2 = 60;
    public static final double CONSTANT3 = 1.1515;
    public static final double CONSTANT4 = 1.609344;
    
    public static final int NO_OF_THREAD_ACCESS_CONCURRENTLY = 1;
    
    public static final String GEO_LOCATION_API_CONTENT_TYPE_KEY = "Content-Type";
    public static final String GEO_LOCATION_API_CONTENT_TYPE_VALUE = "application/json";
    public static final String GEO_LOCATION_API_ACCEPT_KEY = "Accept";
    public static final String GEO_LOCATION_API_ACCEPT_VALUE = "application/json";
    
    public static final int GEO_LOCATION_API_SUCCESS_CODE_VALUE = 200;
    public static final int EMPLTY_PIN_CODE_VALUE = 0;
    
    public static final String GEO_LOCATION_API_SUCCESS_VALUE = "OK";
    public static final String GEO_LOCATION_API_EMPTY_RESULT_VALUE = "ZERO_RESULTS";
}
