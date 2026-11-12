package tests;

import models.login.BlankLoginBodyModelResponse;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LoginTests extends TestBase {

    @Test
    public void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        SuccessfulLoginResponseModel loginResponse =
                step("Авторизуемся с валидным логином и паролем", () ->
                        api.auth.login(loginData));

        step("Проверяем, что получены разные access и refresh токены", () -> {
            assertThat(loginResponse.access()).startsWith(LOGIN_TOKEN_PREFIX);
            assertThat(loginResponse.refresh()).startsWith(LOGIN_TOKEN_PREFIX);
            assertThat(loginResponse.access()).isNotEqualTo(loginResponse.refresh());
        });
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD);

        WrongCredentialsLoginResponseModel loginResponse =
                step("Авторизуемся с неверным паролем", () ->
                        api.auth.loginWrongCredentials(loginData));

        step("Проверяем сообщение о неверных учётных данных", () ->
                assertThat(loginResponse.detail()).isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR));
    }

    @Test
    public void loginFieldsNotBeBlank() {
        LoginBodyModel loginData = new LoginBodyModel("", "");

        BlankLoginBodyModelResponse response =
                step("Авторизуемся с пустыми логином и паролем", () ->
                        api.auth.loginBlankCredentials(loginData));

        step("Проверяем ошибки «поле не может быть пустым» для логина и пароля", () -> {
            assertThat(response.getUsername()).containsExactly(LOGIN_BLANK_ERROR);
            assertThat(response.getPassword()).containsExactly(LOGIN_BLANK_ERROR);
        });
    }

    @Test
    public void loginUserNameNotBeBlank() {
        LoginBodyModel loginData = new LoginBodyModel("", null);

        BlankLoginBodyModelResponse response =
                step("Авторизуемся с пустым логином и без пароля", () ->
                        api.auth.loginBlankCredentials(loginData));

        step("Проверяем ошибки для логина и пароля", () -> {
            assertThat(response.getUsername()).containsExactly(LOGIN_BLANK_ERROR);
            assertThat(response.getPassword()).containsExactly(LOGIN_FIELD_IS_NULL_ERROR);
        });
    }

    @Test
    public void loginPasswordNotBeBlank() {
        LoginBodyModel loginData = new LoginBodyModel(null, "");

        BlankLoginBodyModelResponse response =
                step("Авторизуемся без логина и с пустым паролем", () ->
                        api.auth.loginCredentialsPasswordRegistrationResponseSpec(loginData));

        step("Проверяем ошибки для логина и пароля", () -> {
            assertThat(response.getUsername()).containsExactly(LOGIN_FIELD_IS_NULL_ERROR);
            assertThat(response.getPassword()).containsExactly(LOGIN_BLANK_ERROR);
        });
    }

    @Test
    public void loginFieldsIsNull() {
        LoginBodyModel loginData = new LoginBodyModel(null, null);

        BlankLoginBodyModelResponse response =
                step("Авторизуемся с null в логине и пароле", () ->
                        api.auth.fieldsIsNullLoginResponseSpec(loginData));

        step("Проверяем ошибки «поле не может быть null» для логина и пароля", () -> {
            assertThat(response.getUsername()).containsExactly(LOGIN_FIELD_IS_NULL_ERROR);
            assertThat(response.getPassword()).containsExactly(LOGIN_FIELD_IS_NULL_ERROR);
        });
    }
}