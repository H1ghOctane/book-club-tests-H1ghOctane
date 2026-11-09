package specs.registration;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class RegistrationSpec {

    public static RequestSpecification registrationRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulRegistrationResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/registration/successful_registration_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("username", notNullValue())
            .expectBody("remoteAddr", notNullValue())
            .build();

    public static ResponseSpecification existingUserRegistrationResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/registration/existing_user_registration_response_schema.json"))
            .expectBody("username", notNullValue())
            .build();

    public static ResponseSpecification FullBlankCredentialsUserRegistrationResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/login/blank-cred-schema.json"))
            .expectBody("username", hasItem("This field may not be blank."))
            .expectBody("password", hasItem("This field may not be blank."))
            .build();

    public static ResponseSpecification BlankCredentialsNameRegistrationResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(400)
        .expectBody(matchesJsonSchemaInClasspath(
                "schemas/login/blank-cred-schema.json"))
        .expectBody("username", hasItem("This field may not be blank."))
        .expectBody("password", hasItem("This field may not be null."))
        .build();

    public static ResponseSpecification BlankCredentialsPasswordRegistrationResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/login/blank-cred-schema.json"))
            .expectBody("username", hasItem("This field may not be null."))
            .expectBody("password", hasItem("This field may not be blank."))
            .build();

public static ResponseSpecification FieldsIsNullRegistrationResponseSpec = new ResponseSpecBuilder()
        .log(ALL)
        .expectStatusCode(400)
        .expectBody(matchesJsonSchemaInClasspath(
                "schemas/login/blank-cred-schema.json"))
        .expectBody("username", hasItem("This field may not be null."))
        .expectBody("password", hasItem("This field may not be null."))
        .build();
}


