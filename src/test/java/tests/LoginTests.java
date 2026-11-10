package tests;

import models.login.BlankLoginBodyModelResponse;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LoginTests extends TestBase {

    @Test
    public void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();
        assertThat(actualAccess).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(actualRefresh).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD);

        WrongCredentialsLoginResponseModel loginResponse = api.auth.loginWrongCredentials(loginData);

        String expectedDetailError = LOGIN_WRONG_CREDENTIALS_ERROR;
        String actualDetailError = loginResponse.detail();
        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }

// todo add more negative tests

    @Test
    public void loginFieldsNotBeBlank() {

        LoginBodyModel registrationData =
                new LoginBodyModel("", "");

        BlankLoginBodyModelResponse response = api.auth.loginBlankCredentials(registrationData);

        assertThat(response.getUsername()).containsExactly(LOGIN_BLANK_ERROR);
        assertThat(response.getPassword()).containsExactly(LOGIN_BLANK_ERROR);
    }

    @Test
    public void loginUserNameNotBeBlank() {

        LoginBodyModel registrationData =
                new LoginBodyModel("", null);

        BlankLoginBodyModelResponse response = api.auth.loginBlankCredentials(registrationData);

        assertThat(response.getUsername()).containsExactly(LOGIN_BLANK_ERROR);
        assertThat(response.getPassword()).containsExactly(LOGIN_FIELD_IS_NULL_ERROR);
    }

    @Test
    public void loginPasswordNotBeBlank() {

        LoginBodyModel registrationData =
                new  LoginBodyModel(null, "");

        BlankLoginBodyModelResponse response = api.auth.LoginCredentialsPasswordRegistrationResponseSpec(registrationData);

        assertThat(response.getUsername()).containsExactly(LOGIN_FIELD_IS_NULL_ERROR);
        assertThat(response.getPassword()).containsExactly(LOGIN_BLANK_ERROR);
    }

    @Test
    public void loginFieldsIsNull() {

        LoginBodyModel registrationData =
                new  LoginBodyModel(null, null);

        BlankLoginBodyModelResponse response = api.auth.FieldsIsNullLoginResponseSpec(registrationData);

        assertThat(response.getUsername()).containsExactly(LOGIN_FIELD_IS_NULL_ERROR);
        assertThat(response.getPassword()).containsExactly(LOGIN_FIELD_IS_NULL_ERROR);

    }
}

