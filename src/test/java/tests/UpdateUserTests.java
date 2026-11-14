package tests;

import models.user.AuthorizedUserModel;
import models.user.UnauthorizedUserResponseModel;
import models.user.UpdateUserBodyModel;
import models.user.UpdateUserErrorResponseModel;
import models.user.UserResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class UpdateUserTests extends TestBase {

    Integer userId;
    String username;
    String email;
    String accessToken;

    @BeforeEach
    public void createUserAndLogin() {
        // каждый тест работает со своим свежим пользователем, чтобы не портить общий аккаунт
        AuthorizedUserModel user = api.registerAndLogin();

        userId = user.getId();
        username = user.getUsername();
        accessToken = user.getAccessToken();
        email = username + "@example.com";
    }

    @Test
    public void successfulPutUpdateUserTest() {
        String newUsername = username + "_upd";
        UpdateUserBodyModel updateData =
                new UpdateUserBodyModel(newUsername, UPDATE_FIRST_NAME, UPDATE_LAST_NAME, email);

        UserResponseModel updateResponse =
                step("Обновляем все поля профиля через PUT", () ->
                        api.users.putUser(updateData, accessToken));

        step("Проверяем, что все поля обновились", () -> {
            assertThat(updateResponse.id()).isEqualTo(userId);
            assertThat(updateResponse.username()).isEqualTo(newUsername);
            assertThat(updateResponse.firstName()).isEqualTo(UPDATE_FIRST_NAME);
            assertThat(updateResponse.lastName()).isEqualTo(UPDATE_LAST_NAME);
            assertThat(updateResponse.email()).isEqualTo(email);
        });
    }

    @Test
    public void putWithoutUsernameTest() {
        UpdateUserBodyModel updateData =
                new UpdateUserBodyModel(null, UPDATE_FIRST_NAME, UPDATE_LAST_NAME, email);

        UpdateUserErrorResponseModel errorResponse =
                step("Обновляем профиль через PUT без обязательного username", () ->
                        api.users.putUserWithValidationError(updateData, accessToken));

        step("Проверяем ошибку «обязательное поле»", () ->
                assertThat(errorResponse.username()).containsExactly(UPDATE_REQUIRED_FIELD_ERROR));
    }

    @Test
    public void putWithBlankUsernameTest() {
        UpdateUserBodyModel updateData =
                new UpdateUserBodyModel("", UPDATE_FIRST_NAME, UPDATE_LAST_NAME, email);

        UpdateUserErrorResponseModel errorResponse =
                step("Обновляем профиль через PUT с пустым username", () ->
                        api.users.putUserWithValidationError(updateData, accessToken));

        step("Проверяем ошибку «поле не может быть пустым»", () ->
                assertThat(errorResponse.username()).containsExactly(UPDATE_BLANK_FIELD_ERROR));
    }

    @Test
    public void successfulPatchFirstNameTest() {
        UpdateUserBodyModel updateData = UpdateUserBodyModel.firstNameOnly(UPDATE_FIRST_NAME);

        UserResponseModel updateResponse =
                step("Обновляем только firstName через PATCH", () ->
                        api.users.patchUser(updateData, accessToken));

        step("Проверяем, что firstName обновился", () -> {
            assertThat(updateResponse.id()).isEqualTo(userId);
            assertThat(updateResponse.firstName()).isEqualTo(UPDATE_FIRST_NAME);
        });

        step("Проверяем, что остальные поля не изменились", () -> {
            assertThat(updateResponse.username()).isEqualTo(username);
            assertThat(updateResponse.lastName()).isEmpty();
            assertThat(updateResponse.email()).isEmpty();
        });
    }

    @Test
    public void patchChangesArePersistedTest() {
        step("Обновляем email через PATCH", () ->
                api.users.patchUser(UpdateUserBodyModel.emailOnly(email), accessToken));

        UserResponseModel profile =
                step("Получаем профиль через GET", () ->
                        api.users.getMe(accessToken));

        step("Проверяем, что новый email сохранился", () -> {
            assertThat(profile.id()).isEqualTo(userId);
            assertThat(profile.username()).isEqualTo(username);
            assertThat(profile.email()).isEqualTo(email);
        });
    }

    @Test
    public void patchWithInvalidEmailTest() {
        UpdateUserBodyModel updateData = UpdateUserBodyModel.emailOnly(UPDATE_INVALID_EMAIL);

        UpdateUserErrorResponseModel errorResponse =
                step("Обновляем email через PATCH на некорректный", () ->
                        api.users.patchUserWithValidationError(updateData, accessToken));

        step("Проверяем ошибку «некорректный email»", () ->
                assertThat(errorResponse.email()).containsExactly(UPDATE_INVALID_EMAIL_ERROR));
    }

    @Test
    public void patchWithExistingUsernameTest() {
        UpdateUserBodyModel updateData = UpdateUserBodyModel.usernameOnly(LOGIN_USERNAME);

        UpdateUserErrorResponseModel errorResponse =
                step("Меняем username через PATCH на уже занятый", () ->
                        api.users.patchUserWithValidationError(updateData, accessToken));

        step("Проверяем ошибку «пользователь уже существует»", () ->
                assertThat(errorResponse.username()).containsExactly(REGISTRATION_EXISTING_USER_ERROR));
    }

    @Test
    public void patchWithoutTokenTest() {
        UpdateUserBodyModel updateData = UpdateUserBodyModel.firstNameOnly(UPDATE_FIRST_NAME);

        UnauthorizedUserResponseModel errorResponse =
                step("Обновляем профиль через PATCH без токена", () ->
                        api.users.patchUserUnauthorized(updateData, null));

        step("Проверяем ошибку «не переданы данные авторизации»", () ->
                assertThat(errorResponse.detail()).isEqualTo(UPDATE_NOT_AUTHENTICATED_ERROR));
    }

    @Test
    public void patchWithInvalidTokenTest() {
        UpdateUserBodyModel updateData = UpdateUserBodyModel.firstNameOnly(UPDATE_FIRST_NAME);

        UnauthorizedUserResponseModel errorResponse =
                step("Обновляем профиль через PATCH с невалидным токеном", () ->
                        api.users.patchUserUnauthorized(updateData, UPDATE_INVALID_ACCESS_TOKEN));

        step("Проверяем ошибку «токен невалиден»", () -> {
            assertThat(errorResponse.detail()).isEqualTo(UPDATE_INVALID_TOKEN_ERROR);
            assertThat(errorResponse.code()).isEqualTo(UPDATE_TOKEN_NOT_VALID_CODE);
        });
    }
}