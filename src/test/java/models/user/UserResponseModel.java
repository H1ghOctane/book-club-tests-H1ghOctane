package models.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserResponseModel(Integer id, String username, String firstName,
                                String lastName, String email, String remoteAddr) {}