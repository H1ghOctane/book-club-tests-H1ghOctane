package api;

import io.qameta.allure.Step;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import models.user.AuthorizedUserModel;

import java.util.UUID;

/**
 * Общий API-клиент — единая точка доступа ко всем запросам к API.
 * Тесты работают с API только через него.
 */
public class ApiClient {

    public final AuthApiClient auth = new AuthApiClient();
    public final UsersApiClient users = new UsersApiClient();
    public final ClubsApiClient clubs = new ClubsApiClient();

    /**
     * Регистрирует нового уникального пользователя и сразу авторизует его.
     * Нужен тестам, которым требуется «чистый» пользователь с токеном.
     */
    @Step("Регистрация нового пользователя и получение access-токена")
    public AuthorizedUserModel registerAndLogin() {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String username = "user_" + suffix;
        String password = "pass_" + suffix;

        SuccessfulRegistrationResponseModel registrationResponse =
                users.register(new RegistrationBodyModel(username, password));
        String accessToken = auth.login(new LoginBodyModel(username, password)).access();

        return new AuthorizedUserModel(registrationResponse.id(), username, accessToken);
    }
}
