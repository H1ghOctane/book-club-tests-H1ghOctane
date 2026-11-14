package specs.clubs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class ClubsSpec {

    public static RequestSpecification clubsRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulClubsListResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/clubs/clubs_list_response_schema.json"))
            .expectBody("count", notNullValue())
            .expectBody("count", greaterThanOrEqualTo(0))
            .expectBody("results", notNullValue())
            .build();

    // 200: GET / PUT / PATCH одного клуба
    public static ResponseSpecification successfulClubResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/club_response_schema.json"))
            .expectBody("id", notNullValue())
            .build();

    // 201: клуб создан
    public static ResponseSpecification createdClubResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/club_response_schema.json"))
            .expectBody("id", notNullValue())
            .build();

    // 204: клуб удалён, тела ответа нет
    public static ResponseSpecification deletedClubResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(204)
            .build();

    // 400: ошибка валидации полей
    public static ResponseSpecification validationErrorClubResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/club_validation_error_schema.json"))
            .build();

    // 401: нет авторизации
    public static ResponseSpecification unauthorizedClubResponseSpec = errorSpec(401);

    // 403: нет прав (клуб чужой)
    public static ResponseSpecification forbiddenClubResponseSpec = errorSpec(403);

    // 404: клуб не найден
    public static ResponseSpecification notFoundClubResponseSpec = errorSpec(404);

    private static ResponseSpecification errorSpec(int statusCode) {
        return new ResponseSpecBuilder()
                .log(ALL)
                .expectStatusCode(statusCode)
                .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/club_error_response_schema.json"))
                .expectBody("detail", notNullValue())
                .build();
    }
}
