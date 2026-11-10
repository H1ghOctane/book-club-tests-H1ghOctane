package tests;

import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import models.user.UnauthorizedUserResponseModel;
import models.user.UpdateUserBodyModel;
import models.user.UpdateUserErrorResponseModel;
import models.user.UserResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class UpdateUserTests extends TestBase {

    Integer userId;
    String username;
    String email;
    String accessToken;

    @BeforeEach
    public void createUserAndLogin() {

        username = "user_" + System.currentTimeMillis();
        String password = "pass_" + System.currentTimeMillis();
        email = username + "@example.com";

        SuccessfulRegistrationResponseModel registrationResponse =
                api.users.register(new RegistrationBodyModel(username, password));
        userId = registrationResponse.id();

        accessToken = api.auth.login(new LoginBodyModel(username, password)).access();
    }

    @Test
    public void successfulPutUpdateUserTest() {
        String newUsername = username + "_upd";
        UpdateUserBodyModel updateData =
                new UpdateUserBodyModel(newUsername, UPDATE_FIRST_NAME, UPDATE_LAST_NAME, email);

        UserResponseModel updateResponse = api.users.putUser(updateData, accessToken);

        assertThat(updateResponse.id()).isEqualTo(userId);
        assertThat(updateResponse.username()).isEqualTo(newUsername);
        assertThat(updateResponse.firstName()).isEqualTo(UPDATE_FIRST_NAME);
        assertThat(updateResponse.lastName()).isEqualTo(UPDATE_LAST_NAME);
        assertThat(updateResponse.email()).isEqualTo(email);
    }

    @Test
    public void putWithoutUsernameTest() {
        UpdateUserBodyModel updateData =
                new UpdateUserBodyModel(null, UPDATE_FIRST_NAME, UPDATE_LAST_NAME, email);

        UpdateUserErrorResponseModel errorResponse =
                api.users.putUserWithValidationError(updateData, accessToken);

        assertThat(errorResponse.username()).containsExactly(UPDATE_REQUIRED_FIELD_ERROR);
    }

    @Test
    public void putWithBlankUsernameTest() {
        UpdateUserBodyModel updateData =
                new UpdateUserBodyModel("", UPDATE_FIRST_NAME, UPDATE_LAST_NAME, email);

        UpdateUserErrorResponseModel errorResponse =
                api.users.putUserWithValidationError(updateData, accessToken);

        assertThat(errorResponse.username()).containsExactly(UPDATE_BLANK_FIELD_ERROR);
    }

    // ---------- PATCH: можно передать только часть полей ----------

    @Test
    public void successfulPatchFirstNameTest() {
        UpdateUserBodyModel updateData = UpdateUserBodyModel.firstNameOnly(UPDATE_FIRST_NAME);

        UserResponseModel updateResponse = api.users.patchUser(updateData, accessToken);

        assertThat(updateResponse.id()).isEqualTo(userId);
        assertThat(updateResponse.firstName()).isEqualTo(UPDATE_FIRST_NAME);
        // остальные поля не изменились
        assertThat(updateResponse.username()).isEqualTo(username);
        assertThat(updateResponse.lastName()).isEmpty();
        assertThat(updateResponse.email()).isEmpty();
    }

    @Test
    public void patchChangesArePersistedTest() {
        api.users.patchUser(UpdateUserBodyModel.emailOnly(email), accessToken);

        UserResponseModel profile = api.users.getMe(accessToken);

        assertThat(profile.id()).isEqualTo(userId);
        assertThat(profile.username()).isEqualTo(username);
        assertThat(profile.email()).isEqualTo(email);
    }

    @Test
    public void patchWithInvalidEmailTest() {
        UpdateUserBodyModel updateData = UpdateUserBodyModel.emailOnly(UPDATE_INVALID_EMAIL);

        UpdateUserErrorResponseModel errorResponse =
                api.users.patchUserWithValidationError(updateData, accessToken);

        assertThat(errorResponse.email()).containsExactly(UPDATE_INVALID_EMAIL_ERROR);
    }

    @Test
    public void patchWithExistingUsernameTest() {
        UpdateUserBodyModel updateData = UpdateUserBodyModel.usernameOnly(LOGIN_USERNAME);

        UpdateUserErrorResponseModel errorResponse =
                api.users.patchUserWithValidationError(updateData, accessToken);

        assertThat(errorResponse.username()).containsExactly(REGISTRATION_EXISTING_USER_ERROR);
    }

    @Test
    public void patchWithoutTokenTest() {
        UpdateUserBodyModel updateData = UpdateUserBodyModel.firstNameOnly(UPDATE_FIRST_NAME);

        UnauthorizedUserResponseModel errorResponse = api.users.patchUserUnauthorized(updateData, null);

        assertThat(errorResponse.detail()).isEqualTo(UPDATE_NOT_AUTHENTICATED_ERROR);
    }

    @Test
    public void patchWithInvalidTokenTest() {
        UpdateUserBodyModel updateData = UpdateUserBodyModel.firstNameOnly(UPDATE_FIRST_NAME);

        UnauthorizedUserResponseModel errorResponse =
                api.users.patchUserUnauthorized(updateData, UPDATE_INVALID_ACCESS_TOKEN);

        assertThat(errorResponse.detail()).isEqualTo(UPDATE_INVALID_TOKEN_ERROR);
        assertThat(errorResponse.code()).isEqualTo(UPDATE_TOKEN_NOT_VALID_CODE);
    }
}