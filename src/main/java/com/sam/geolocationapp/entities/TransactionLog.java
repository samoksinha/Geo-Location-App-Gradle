package com.sam.geolocationapp.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="transanction_log")
public class TransactionLog implements Serializable {

	private static final long serialVersionUID = -1644356410840557522L;

	@Id
	@Column(name="unique_log_id", length=50)
	private String uniqueLogId;
	
	@Column(name="email_id", length=50)
	private String emailId;
	
	@Column(name="api_key", length=500)
	private String apiKey;
	
	@Column(name="token_type", length=10)
	private String tokenType;
	
	@Column(name="request_type", length=10)
	private String requestType;
	
	@Column(name="operation_name", length=50)
	private String operationName;
	
	@Column(name="request_time", length=20)
	private String requestTime;
	
	@Column(name="request_body", length=2000)
	private String requestBody;
	
	@Column(name="response_time", length=20)
	private String responseTime;
	
	@Column(name="response_body", length=2000)
	private String responseBody;
	
	@Column(name="client_ip", length=20)
	private String clientIp;
	
	public TransactionLog() {
	}

	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUniqueLogId() {
		return uniqueLogId;
	}
	public void setUniqueLogId(String uniqueLogId) {
		this.uniqueLogId = uniqueLogId;
	}

	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	@Override
	public String toString() {
		return "TransactionLog [uniqueLogId=" + uniqueLogId + ", emailId=" + emailId + ", apiKey=" + apiKey
				+ ", tokenType=" + tokenType + ", requestType=" + requestType + ", operationName=" + operationName
				+ ", requestTime=" + requestTime + ", requestBody=" + requestBody + ", responseTime=" + responseTime
				+ ", responseBody=" + responseBody + ", clientIp=" + clientIp + "]";
	}

}
