package api;

import io.restassured.specification.RequestSpecification;
import models.registration.BlankCredentialsResponseModel;
import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import models.user.UnauthorizedUserResponseModel;
import models.user.UpdateUserBodyModel;
import models.user.UpdateUserErrorResponseModel;
import models.user.UserResponseModel;
import specs.BaseSpec;

import static io.restassured.RestAssured.given;
import static specs.registration.RegistrationSpec.*;
import static specs.user.UserSpec.*;

public class UsersApiClient {

    private static final String ME_PATH = "/users/me/";

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

    public UserResponseModel getMe(String accessToken) {
        return authorized(accessToken)
                .when()
                .get(ME_PATH)
                .then()
                .spec(successfulUserResponseSpec)
                .extract()
                .as(UserResponseModel.class);
    }

    public UserResponseModel putUser(UpdateUserBodyModel body, String accessToken) {
        return authorized(accessToken)
                .body(body)
                .when()
                .put(ME_PATH)
                .then()
                .spec(successfulUserResponseSpec)
                .extract()
                .as(UserResponseModel.class);
    }

    public UserResponseModel patchUser(UpdateUserBodyModel body, String accessToken) {
        return authorized(accessToken)
                .body(body)
                .when()
                .patch(ME_PATH)
                .then()
                .spec(successfulUserResponseSpec)
                .extract()
                .as(UserResponseModel.class);
    }

    public UpdateUserErrorResponseModel putUserWithValidationError(UpdateUserBodyModel body, String accessToken) {
        return authorized(accessToken)
                .body(body)
                .when()
                .put(ME_PATH)
                .then()
                .spec(validationErrorUserResponseSpec)
                .extract()
                .as(UpdateUserErrorResponseModel.class);
    }

    public UpdateUserErrorResponseModel patchUserWithValidationError(UpdateUserBodyModel body, String accessToken) {
        return authorized(accessToken)
                .body(body)
                .when()
                .patch(ME_PATH)
                .then()
                .spec(validationErrorUserResponseSpec)
                .extract()
                .as(UpdateUserErrorResponseModel.class);
    }

    public UnauthorizedUserResponseModel patchUserUnauthorized(UpdateUserBodyModel body, String accessToken) {
        return authorized(accessToken)
                .body(body)
                .when()
                .patch(ME_PATH)
                .then()
                .spec(unauthorizedUserResponseSpec)
                .extract()
                .as(UnauthorizedUserResponseModel.class);
    }

    private RequestSpecification authorized(String accessToken) {
        RequestSpecification request = given(userRequestSpec);
        if (accessToken != null) {
            request.header("Authorization", "Bearer " + accessToken);
        }
        return request;
    }
}
