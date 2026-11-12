package tests;

import models.registration.BlankCredentialsResponseModel;
import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class RegistrationTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        // генерируем данные в тесте, чтобы каждый запуск был с новыми пользователями
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
    }

    @Test
    public void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse =
                step("Регистрируем нового пользователя", () ->
                        api.users.register(registrationData));

        step("Проверяем данные созданного пользователя", () -> {
            assertThat(registrationResponse.id()).isGreaterThan(0);
            assertThat(registrationResponse.username()).isEqualTo(username);
            assertThat(registrationResponse.firstName()).isEqualTo("");
            assertThat(registrationResponse.lastName()).isEqualTo("");
            assertThat(registrationResponse.email()).isEqualTo("");
        });

        step("Проверяем, что remoteAddr — корректный IP-адрес", () ->
                assertThat(registrationResponse.remoteAddr()).matches(REGISTRATION_IP_REGEXP));
    }

    @Test
    public void existingUserWrongRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel firstRegistrationResponse =
                step("Регистрируем нового пользователя", () ->
                        api.users.register(registrationData));

        step("Проверяем, что пользователь создан", () ->
                assertThat(firstRegistrationResponse.username()).isEqualTo(username));

        ExistingUserResponseModel secondRegistrationResponse =
                step("Повторно регистрируем пользователя с тем же логином", () ->
                        api.users.registerExistingUser(registrationData));

        step("Проверяем ошибку «пользователь уже существует»", () ->
                assertThat(secondRegistrationResponse.username())
                        .containsExactly(REGISTRATION_EXISTING_USER_ERROR));
    }

    @Test
    public void registrationFieldsNotBeBlank() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel("", "");

        BlankCredentialsResponseModel response =
                step("Регистрируемся с пустыми логином и паролем", () ->
                        api.users.registerFullBlankCredentials(registrationData));

        step("Проверяем ошибки «поле не может быть пустым» для логина и пароля", () -> {
            assertThat(response.getUsername()).containsExactly(REGISTRATION_BLANK_ERROR);
            assertThat(response.getPassword()).containsExactly(REGISTRATION_BLANK_ERROR);
        });
    }

    @Test
    public void userNameNotBeBlank() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel("", null);

        BlankCredentialsResponseModel response =
                step("Регистрируемся с пустым логином и без пароля", () ->
                        api.users.registerBlankNameCredentials(registrationData));

        step("Проверяем ошибки для логина и пароля", () -> {
            assertThat(response.getUsername()).containsExactly(REGISTRATION_BLANK_ERROR);
            assertThat(response.getPassword()).containsExactly(REGISTRATION_FIELD_IS_NULL_ERROR);
        });
    }

    @Test
    public void userPasswordNotBeBlank() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(null, "");

        BlankCredentialsResponseModel response =
                step("Регистрируемся без логина и с пустым паролем", () ->
                        api.users.blankCredentialsPasswordRegistrationResponseSpec(registrationData));

        step("Проверяем ошибки для логина и пароля", () -> {
            assertThat(response.getUsername()).containsExactly(REGISTRATION_FIELD_IS_NULL_ERROR);
            assertThat(response.getPassword()).containsExactly(REGISTRATION_BLANK_ERROR);
        });
    }

    @Test
    public void registrationFieldsIsNull() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(null, null);

        BlankCredentialsResponseModel response =
                step("Регистрируемся с null в логине и пароле", () ->
                        api.users.fieldsIsNullRegistrationResponseSpec(registrationData));

        step("Проверяем ошибки «поле не может быть null» для логина и пароля", () -> {
            assertThat(response.getUsername()).containsExactly(REGISTRATION_FIELD_IS_NULL_ERROR);
            assertThat(response.getPassword()).containsExactly(REGISTRATION_FIELD_IS_NULL_ERROR);
        });
    }
}