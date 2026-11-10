package models.user;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateUserBodyModel(String username, String firstName, String lastName, String email) {

    public static UpdateUserBodyModel firstNameOnly(String firstName) {
        return new UpdateUserBodyModel(null, firstName, null, null);
    }

    public static UpdateUserBodyModel emailOnly(String email) {
        return new UpdateUserBodyModel(null, null, null, email);
    }

    public static UpdateUserBodyModel usernameOnly(String username) {
        return new UpdateUserBodyModel(username, null, null, null);
    }
}