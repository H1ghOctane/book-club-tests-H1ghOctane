package tests;

import models.registration.BlankCredentialsResponseModel;
import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class RegistrationTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        // оставляем генерацию данных в тесте, чтобы каждый запуск был с новыми пользователями
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
    }

    @Test
    public void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse =
                api.users.register(registrationData);

        assertThat(registrationResponse.id()).isGreaterThan(0);
        assertThat(registrationResponse.username()).isEqualTo(username);
        assertThat(registrationResponse.firstName()).isEqualTo("");
        assertThat(registrationResponse.lastName()).isEqualTo("");
        assertThat(registrationResponse.email()).isEqualTo("");

        assertThat(registrationResponse.remoteAddr()).matches(REGISTRATION_IP_REGEXP);
    }

    @Test
    public void existingUserWrongRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel firstRegistrationResponse =
                api.users.register(registrationData);

        assertThat(firstRegistrationResponse.username()).isEqualTo(username);

        ExistingUserResponseModel secondRegistrationResponse =
                api.users.registerExistingUser(registrationData);

        String expectedError = REGISTRATION_EXISTING_USER_ERROR;
        String actualError = secondRegistrationResponse.username().get(0);
        assertThat(actualError).isEqualTo(expectedError);
    }

    // todo add more negative tests

    @Test
    public void registrationFieldsNotBeBlank() {

        RegistrationBodyModel registrationData =
                new RegistrationBodyModel("", "");

        BlankCredentialsResponseModel response = api.users.registerFullBlankCredentials(registrationData);

        assertThat(response.username().get(0))
                .isEqualTo(REGISTRATION_BLANK_ERROR);
        assertThat(response.password().get(0))
                .isEqualTo(REGISTRATION_BLANK_ERROR);
    }

    @Test
    public void userNameNotBeBlank() {

        RegistrationBodyModel registrationData =
                new RegistrationBodyModel("", null);

        BlankCredentialsResponseModel response = api.users.registerBlankNameCredentials(registrationData);

        assertThat(response.username().get(0))
                .isEqualTo(REGISTRATION_BLANK_ERROR);
        assertThat(response.password().get(0))
                .isEqualTo(REGISTRATION_FIELD_IS_NULL_ERROR);
    }

    @Test
    public void userPasswordNotBeBlank() {

        RegistrationBodyModel registrationData =
                new RegistrationBodyModel(null, "");

        BlankCredentialsResponseModel response = api.users.BlankCredentialsPasswordRegistrationResponseSpec(registrationData);

        assertThat(response.username().get(0))
                .isEqualTo(REGISTRATION_FIELD_IS_NULL_ERROR);
        assertThat(response.password().get(0))
                .isEqualTo(REGISTRATION_BLANK_ERROR);
    }

    @Test
    public void registrationFieldsIsNull() {

        RegistrationBodyModel registrationData =
                new RegistrationBodyModel(null, null);

        BlankCredentialsResponseModel response = api.users.FieldsIsNullRegistrationResponseSpec(registrationData);

        assertThat(response.username().get(0))
                .isEqualTo(REGISTRATION_FIELD_IS_NULL_ERROR);
        assertThat(response.password().get(0))
                .isEqualTo(REGISTRATION_FIELD_IS_NULL_ERROR);
    }

}
