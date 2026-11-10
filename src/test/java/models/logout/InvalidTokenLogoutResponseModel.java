package models.logout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record InvalidTokenLogoutResponseModel(String detail, String code) {}