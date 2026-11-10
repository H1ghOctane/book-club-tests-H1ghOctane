package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.logout.BlankRefreshLogoutResponseModel;
import models.logout.InvalidTokenLogoutResponseModel;
import models.logout.LogoutBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LogoutTests extends TestBase {

    @Test
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        String refreshToken = api.auth.loginAndGetRefreshToken(loginData);

        LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
        api.auth.logout(logoutData);
    }

    @Test
    public void invalidTokenLogoutTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel(LOGOUT_INVALID_TOKEN);

        InvalidTokenLogoutResponseModel logoutResponse = api.auth.logoutWithInvalidToken(logoutData);

        assertThat(logoutResponse.detail()).isEqualTo(LOGOUT_INVALID_TOKEN_ERROR);
        assertThat(logoutResponse.code()).isEqualTo(LOGOUT_TOKEN_NOT_VALID_CODE);
    }

    @Test
    public void blankTokenLogoutTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel("");

        BlankRefreshLogoutResponseModel logoutResponse = api.auth.logoutWithBlankToken(logoutData);

        assertThat(logoutResponse.refresh()).containsExactly(LOGOUT_BLANK_TOKEN_ERROR);
    }

    @Test
    public void repeatedLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        String refreshToken = api.auth.loginAndGetRefreshToken(loginData);
        LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
        api.auth.logout(logoutData);

        InvalidTokenLogoutResponseModel secondLogoutResponse = api.auth.logoutWithInvalidToken(logoutData);

        assertThat(secondLogoutResponse.detail()).isNotBlank();
        assertThat(secondLogoutResponse.code()).isEqualTo(LOGOUT_TOKEN_NOT_VALID_CODE);
    }

    @Test
    @DisplayName("Logout с access-токеном вместо refresh возвращает 401")
    public void accessTokenInsteadOfRefreshLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        LogoutBodyModel logoutData = new LogoutBodyModel(loginResponse.access());
        InvalidTokenLogoutResponseModel logoutResponse = api.auth.logoutWithInvalidToken(logoutData);

        assertThat(logoutResponse.detail()).isNotBlank();
        assertThat(logoutResponse.code()).isEqualTo(LOGOUT_TOKEN_NOT_VALID_CODE);
    }
}


