package tests;

import models.clubs.ClubModel;
import models.clubs.ClubsListResponseModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class ClubsTests extends TestBase {

    @Test
    public void getClubsReturns200AndValidStructure() {
        ClubsListResponseModel response =
                step("Получаем список клубов", () ->
                        api.clubs.getClubs());

        step("Проверяем структуру ответа", () -> {
            assertThat(response).isNotNull();
            assertThat(response.count()).isGreaterThanOrEqualTo(0);
            assertThat(response.results()).isNotNull();
            assertThat(response.results()).hasSizeLessThanOrEqualTo(response.count());
        });
    }

    @Test
    public void getClubsCountMatchesResultsSize() {
        ClubsListResponseModel response =
                step("Получаем список клубов", () ->
                        api.clubs.getClubs());

        if (response.next() == null) {
            step("Все клубы на одной странице: проверяем, что размер results равен count", () ->
                    assertThat(response.results()).hasSize(response.count()));
        } else {
            step("Есть следующая страница: проверяем, что на текущей клубов меньше, чем count", () ->
                    assertThat(response.results().size()).isLessThan(response.count()));
        }
    }

    @Test
    public void getClubsEachClubHasRequiredFields() {
        ClubsListResponseModel response =
                step("Получаем список клубов", () ->
                        api.clubs.getClubs());

        step("Проверяем, что у каждого клуба заполнены обязательные поля", () -> {
            for (ClubModel club : response.results()) {
                assertThat(club.id()).isNotNull().isPositive();
                assertThat(club.bookTitle()).isNotNull();
                assertThat(club.bookAuthors()).isNotNull();
                assertThat(club.publicationYear()).isNotNull();
                assertThat(club.description()).isNotNull();
                assertThat(club.telegramChatLink()).isNotNull();
                assertThat(club.owner()).isNotNull().isPositive();
                assertThat(club.members()).isNotNull();
                assertThat(club.reviews()).isNotNull();
                assertThat(club.created()).isNotNull();
            }
        });
    }

    @Test
    public void getClubsPaginationFieldsPresent() {
        ClubsListResponseModel response =
                step("Получаем список клубов", () ->
                        api.clubs.getClubs());

        step("Проверяем наличие полей пагинации", () -> {
            assertThat(response.count()).isNotNull();
            assertThat(response.results()).isNotNull();
        });
    }
}