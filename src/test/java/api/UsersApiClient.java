package api;

import models.registration.BlankCredentialsResponseModel;
import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import specs.BaseSpec;

import static io.restassured.RestAssured.given;
import static specs.registration.RegistrationSpec.*;

public class UsersApiClient {

    public SuccessfulRegistrationResponseModel register(RegistrationBodyModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);
    }

    public ExistingUserResponseModel registerExistingUser(RegistrationBodyModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationResponseSpec)
                .extract()
                .as(ExistingUserResponseModel.class);
    }

    public BlankCredentialsResponseModel registerFullBlankCredentials(RegistrationBodyModel body) {
        return given()
                .spec(BaseSpec.baseRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(FullBlankCredentialsUserRegistrationResponseSpec)
                .extract()
                .as(BlankCredentialsResponseModel.class);
    }

    public BlankCredentialsResponseModel registerBlankNameCredentials(RegistrationBodyModel body) {
        return given()
                .spec(BaseSpec.baseRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(BlankCredentialsNameRegistrationResponseSpec)
                .extract()
                .as(BlankCredentialsResponseModel.class);
    }
    public BlankCredentialsResponseModel BlankCredentialsPasswordRegistrationResponseSpec(RegistrationBodyModel body) {
        return given()
                .spec(BaseSpec.baseRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(BlankCredentialsPasswordRegistrationResponseSpec)
                .extract()
                .as(BlankCredentialsResponseModel.class);
    }
    public BlankCredentialsResponseModel FieldsIsNullRegistrationResponseSpec (RegistrationBodyModel body) {
        return given()
                .spec(BaseSpec.baseRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(FieldsIsNullRegistrationResponseSpec)
                .extract()
                .as(BlankCredentialsResponseModel.class);
    }

}
