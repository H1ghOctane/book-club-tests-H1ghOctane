package api;

import models.login.BlankLoginBodyModelResponse;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import models.logout.BlankRefreshLogoutResponseModel;
import models.logout.InvalidTokenLogoutResponseModel;
import models.logout.LogoutBodyModel;

import static io.restassured.RestAssured.given;
import static specs.login.LoginSpec.*;
import static specs.logout.LogoutSpec.*;

public class AuthApiClient {

    public SuccessfulLoginResponseModel login(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);
    }

    public String loginAndGetRefreshToken(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .path("refresh");
    }

    public WrongCredentialsLoginResponseModel loginWrongCredentials(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract()
                .as(WrongCredentialsLoginResponseModel.class);
    }

    public BlankLoginBodyModelResponse loginBlankCredentials(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(blankCredentialsLoginResponseSpec)
                .extract()
                .as(BlankLoginBodyModelResponse.class);
    }

    public BlankLoginBodyModelResponse LoginCredentialsPasswordRegistrationResponseSpec(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(blankPasswordLoginResponseSpec)
                .extract()
                .as(BlankLoginBodyModelResponse.class);
    }

    public BlankLoginBodyModelResponse FieldsIsNullLoginResponseSpec(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(FieldsIsNullLoginResponseSpec)
                .extract()
                .as(BlankLoginBodyModelResponse.class);
    }

    public void logout(LogoutBodyModel logoutBody) {
        given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    public InvalidTokenLogoutResponseModel logoutWithInvalidToken(LogoutBodyModel logoutBody) {
        return given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(invalidTokenLogoutResponseSpec)
                .extract()
                .as(InvalidTokenLogoutResponseModel.class);
    }

    public BlankRefreshLogoutResponseModel logoutWithBlankToken(LogoutBodyModel logoutBody) {
        return given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(blankRefreshLogoutResponseSpec)
                .extract()
                .as(BlankRefreshLogoutResponseModel.class);
    }
}

