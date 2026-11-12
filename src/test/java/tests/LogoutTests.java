package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.logout.BlankRefreshLogoutResponseModel;
import models.logout.InvalidTokenLogoutResponseModel;
import models.logout.LogoutBodyModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LogoutTests extends TestBase {

    @Test
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String refreshToken =
                step("Авторизуемся и получаем refresh-токен", () ->
                        api.auth.loginAndGetRefreshToken(loginData));

        step("Выходим из системы с полученным refresh-токеном", () ->
                api.auth.logout(new LogoutBodyModel(refreshToken)));
    }

    @Test
    public void invalidTokenLogoutTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel(LOGOUT_INVALID_TOKEN);

        InvalidTokenLogoutResponseModel logoutResponse =
                step("Выходим из системы с невалидным refresh-токеном", () ->
                        api.auth.logoutWithInvalidToken(logoutData));

        step("Проверяем ошибку «токен невалиден»", () -> {
            assertThat(logoutResponse.detail()).isEqualTo(LOGOUT_INVALID_TOKEN_ERROR);
            assertThat(logoutResponse.code()).isEqualTo(LOGOUT_TOKEN_NOT_VALID_CODE);
        });
    }

    @Test
    public void blankTokenLogoutTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel("");

        BlankRefreshLogoutResponseModel logoutResponse =
                step("Выходим из системы с пустым refresh-токеном", () ->
                        api.auth.logoutWithBlankToken(logoutData));

        step("Проверяем ошибку «поле не может быть пустым»", () ->
                assertThat(logoutResponse.refresh()).containsExactly(LOGOUT_BLANK_TOKEN_ERROR));
    }

    @Test
    public void repeatedLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String refreshToken =
                step("Авторизуемся и получаем refresh-токен", () ->
                        api.auth.loginAndGetRefreshToken(loginData));

        LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);

        step("Выходим из системы первый раз", () ->
                api.auth.logout(logoutData));

        InvalidTokenLogoutResponseModel secondLogoutResponse =
                step("Повторно выходим с тем же refresh-токеном", () ->
                        api.auth.logoutWithInvalidToken(logoutData));

        step("Проверяем, что токен больше не действителен", () -> {
            assertThat(secondLogoutResponse.detail()).isNotBlank();
            assertThat(secondLogoutResponse.code()).isEqualTo(LOGOUT_TOKEN_NOT_VALID_CODE);
        });
    }

    @Test
    public void accessTokenInsteadOfRefreshLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        SuccessfulLoginResponseModel loginResponse =
                step("Авторизуемся и получаем пару токенов", () ->
                        api.auth.login(loginData));

        InvalidTokenLogoutResponseModel logoutResponse =
                step("Выходим из системы, передав access-токен вместо refresh", () ->
                        api.auth.logoutWithInvalidToken(new LogoutBodyModel(loginResponse.access())));

        step("Проверяем ошибку «токен невалиден»", () -> {
            assertThat(logoutResponse.detail()).isNotBlank();
            assertThat(logoutResponse.code()).isEqualTo(LOGOUT_TOKEN_NOT_VALID_CODE);
        });
    }
}