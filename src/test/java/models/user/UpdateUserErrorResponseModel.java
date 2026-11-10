package models.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateUserErrorResponseModel(List<String> username, List<String> firstName,
                                           List<String> lastName, List<String> email) {}