package api;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import models.clubs.ClubBodyModel;
import models.clubs.ClubErrorResponseModel;
import models.clubs.ClubModel;
import models.clubs.ClubValidationErrorResponseModel;
import models.clubs.ClubsListResponseModel;

import static io.restassured.RestAssured.given;
import static specs.clubs.ClubsSpec.*;

public class ClubsApiClient {

    private static final String CLUBS_PATH = "/clubs/";
    private static final String CLUB_PATH = "/clubs/{id}/";

    // ---------- READ ----------

    @Step("Получение списка клубов GET /clubs/")
    public ClubsListResponseModel getClubs() {
        return given(clubsRequestSpec)
                .when()
                .get(CLUBS_PATH)
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    @Step("Получение клуба GET /clubs/{id}/")
    public ClubModel getClub(int id) {
        return given(clubsRequestSpec)
                .pathParam("id", id)
                .when()
                .get(CLUB_PATH)
                .then()
                .spec(successfulClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Получение несуществующего клуба GET /clubs/{id}/")
    public ClubErrorResponseModel getNotExistingClub(int id) {
        return given(clubsRequestSpec)
                .pathParam("id", id)
                .when()
                .get(CLUB_PATH)
                .then()
                .spec(notFoundClubResponseSpec)
                .extract()
                .as(ClubErrorResponseModel.class);
    }

    // ---------- CREATE ----------

    @Step("Создание клуба POST /clubs/")
    public ClubModel createClub(ClubBodyModel body, String accessToken) {
        return authorized(accessToken)
                .body(body)
                .when()
                .post(CLUBS_PATH)
                .then()
                .spec(createdClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Создание клуба с невалидными данными POST /clubs/")
    public ClubValidationErrorResponseModel createClubWithValidationError(ClubBodyModel body, String accessToken) {
        return authorized(accessToken)
                .body(body)
                .when()
                .post(CLUBS_PATH)
                .then()
                .spec(validationErrorClubResponseSpec)
                .extract()
                .as(ClubValidationErrorResponseModel.class);
    }

    @Step("Создание клуба без авторизации POST /clubs/")
    public ClubErrorResponseModel createClubUnauthorized(ClubBodyModel body) {
        return given(clubsRequestSpec)
                .body(body)
                .when()
                .post(CLUBS_PATH)
                .then()
                .spec(unauthorizedClubResponseSpec)
                .extract()
                .as(ClubErrorResponseModel.class);
    }

    // ---------- UPDATE ----------

    @Step("Полное обновление клуба PUT /clubs/{id}/")
    public ClubModel updateClub(int id, ClubBodyModel body, String accessToken) {
        return authorized(accessToken)
                .pathParam("id", id)
                .body(body)
                .when()
                .put(CLUB_PATH)
                .then()
                .spec(successfulClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Частичное обновление клуба PATCH /clubs/{id}/")
    public ClubModel patchClub(int id, ClubBodyModel body, String accessToken) {
        return authorized(accessToken)
                .pathParam("id", id)
                .body(body)
                .when()
                .patch(CLUB_PATH)
                .then()
                .spec(successfulClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Обновление чужого клуба PUT /clubs/{id}/")
    public ClubErrorResponseModel updateClubForbidden(int id, ClubBodyModel body, String accessToken) {
        return authorized(accessToken)
                .pathParam("id", id)
                .body(body)
                .when()
                .put(CLUB_PATH)
                .then()
                .spec(forbiddenClubResponseSpec)
                .extract()
                .as(ClubErrorResponseModel.class);
    }

    // ---------- DELETE ----------

    @Step("Удаление клуба DELETE /clubs/{id}/")
    public void deleteClub(int id, String accessToken) {
        authorized(accessToken)
                .pathParam("id", id)
                .when()
                .delete(CLUB_PATH)
                .then()
                .spec(deletedClubResponseSpec);
    }

    @Step("Удаление чужого клуба DELETE /clubs/{id}/")
    public ClubErrorResponseModel deleteClubForbidden(int id, String accessToken) {
        return authorized(accessToken)
                .pathParam("id", id)
                .when()
                .delete(CLUB_PATH)
                .then()
                .spec(forbiddenClubResponseSpec)
                .extract()
                .as(ClubErrorResponseModel.class);
    }

    /**
     * Запрос с заголовком Authorization.
     */
    private RequestSpecification authorized(String accessToken) {
        return given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken);
    }
}
