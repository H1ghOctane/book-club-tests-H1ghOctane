package tests;

import models.clubs.ClubBodyModel;
import models.clubs.ClubErrorResponseModel;
import models.clubs.ClubModel;
import models.clubs.ClubValidationErrorResponseModel;
import models.user.AuthorizedUserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;
import static tests.data.ClubTestData.randomClub;

public class ClubsCrudTests extends TestBase {

    AuthorizedUserModel owner;
    Integer createdClubId;

    @BeforeEach
    public void createOwner() {
        // каждый тест работает со своим владельцем клуба, чтобы не зависеть от чужих данных
        owner = api.registerAndLogin();
    }

    @AfterEach
    public void deleteCreatedClub() {
        // убираем за собой клуб, если тест его создал и не удалил сам
        if (createdClubId != null) {
            try {
                api.clubs.deleteClub(createdClubId, owner.getAccessToken());
            } catch (AssertionError ignored) {
                // клуб уже удалён — ничего страшного
            }
        }
    }

    // ---------- CREATE ----------

    @Test
    public void successfulCreateClubTest() {
        ClubBodyModel clubData = randomClub();

        ClubModel createdClub =
                step("Создаём клуб", () ->
                        api.clubs.createClub(clubData, owner.getAccessToken()));
        createdClubId = createdClub.id();

        step("Проверяем, что данные клуба совпадают с отправленными", () -> {
            assertThat(createdClub.bookTitle()).isEqualTo(clubData.getBookTitle());
            assertThat(createdClub.bookAuthors()).isEqualTo(clubData.getBookAuthors());
            assertThat(createdClub.publicationYear()).isEqualTo(clubData.getPublicationYear());
            assertThat(createdClub.description()).isEqualTo(clubData.getDescription());
            assertThat(createdClub.telegramChatLink()).isEqualTo(clubData.getTelegramChatLink());
        });

        step("Проверяем, что владелец клуба — текущий пользователь", () -> {
            assertThat(createdClub.owner()).isEqualTo(owner.getId());
            assertThat(createdClub.members()).contains(owner.getId());
        });
    }

    @Test
    public void createClubWithoutTokenTest() {
        ClubErrorResponseModel errorResponse =
                step("Создаём клуб без авторизации", () ->
                        api.clubs.createClubUnauthorized(randomClub()));

        step("Проверяем ошибку «не переданы данные авторизации»", () ->
                assertThat(errorResponse.getDetail()).isEqualTo(CLUB_NOT_AUTHENTICATED_ERROR));
    }

    @Test
    public void createClubWithBlankTitleTest() {
        ClubBodyModel clubData = randomClub().toBuilder()
                .bookTitle("")
                .build();

        ClubValidationErrorResponseModel errorResponse =
                step("Создаём клуб с пустым названием книги", () ->
                        api.clubs.createClubWithValidationError(clubData, owner.getAccessToken()));

        step("Проверяем ошибку «поле не может быть пустым»", () ->
                assertThat(errorResponse.getBookTitle()).containsExactly(CLUB_BLANK_FIELD_ERROR));
    }

    // ---------- READ ----------

    @Test
    public void getClubByIdTest() {
        ClubBodyModel clubData = randomClub();

        ClubModel createdClub =
                step("Создаём клуб", () ->
                        api.clubs.createClub(clubData, owner.getAccessToken()));
        createdClubId = createdClub.id();

        ClubModel club =
                step("Получаем клуб по id", () ->
                        api.clubs.getClub(createdClubId));

        step("Проверяем, что получен тот же клуб", () -> {
            assertThat(club.id()).isEqualTo(createdClubId);
            assertThat(club.bookTitle()).isEqualTo(clubData.getBookTitle());
            assertThat(club.bookAuthors()).isEqualTo(clubData.getBookAuthors());
            assertThat(club.owner()).isEqualTo(owner.getId());
        });
    }

    @Test
    public void getNotExistingClubTest() {
        ClubErrorResponseModel errorResponse =
                step("Получаем клуб с несуществующим id", () ->
                        api.clubs.getNotExistingClub(CLUB_NOT_EXISTING_ID));

        step("Проверяем, что вернулась ошибка «не найдено»", () ->
                assertThat(errorResponse.getDetail()).isNotBlank());
    }

    // ---------- UPDATE ----------

    @Test
    public void successfulPutClubTest() {
        ClubModel createdClub =
                step("Создаём клуб", () ->
                        api.clubs.createClub(randomClub(), owner.getAccessToken()));
        createdClubId = createdClub.id();

        ClubBodyModel newClubData = randomClub();

        ClubModel updatedClub =
                step("Полностью обновляем клуб через PUT", () ->
                        api.clubs.updateClub(createdClubId, newClubData, owner.getAccessToken()));

        step("Проверяем, что все поля обновились", () -> {
            assertThat(updatedClub.id()).isEqualTo(createdClubId);
            assertThat(updatedClub.bookTitle()).isEqualTo(newClubData.getBookTitle());
            assertThat(updatedClub.bookAuthors()).isEqualTo(newClubData.getBookAuthors());
            assertThat(updatedClub.publicationYear()).isEqualTo(newClubData.getPublicationYear());
            assertThat(updatedClub.description()).isEqualTo(newClubData.getDescription());
            assertThat(updatedClub.telegramChatLink()).isEqualTo(newClubData.getTelegramChatLink());
        });
    }

    @Test
    public void successfulPatchClubTest() {
        ClubBodyModel clubData = randomClub();

        ClubModel createdClub =
                step("Создаём клуб", () ->
                        api.clubs.createClub(clubData, owner.getAccessToken()));
        createdClubId = createdClub.id();

        ClubBodyModel patchData = ClubBodyModel.builder()
                .description(CLUB_UPDATED_DESCRIPTION)
                .build();

        ClubModel patchedClub =
                step("Обновляем только описание через PATCH", () ->
                        api.clubs.patchClub(createdClubId, patchData, owner.getAccessToken()));

        step("Проверяем, что описание обновилось", () ->
                assertThat(patchedClub.description()).isEqualTo(CLUB_UPDATED_DESCRIPTION));

        step("Проверяем, что остальные поля не изменились", () -> {
            assertThat(patchedClub.bookTitle()).isEqualTo(clubData.getBookTitle());
            assertThat(patchedClub.bookAuthors()).isEqualTo(clubData.getBookAuthors());
            assertThat(patchedClub.publicationYear()).isEqualTo(clubData.getPublicationYear());
            assertThat(patchedClub.telegramChatLink()).isEqualTo(clubData.getTelegramChatLink());
        });
    }

    @Test
    public void putClubByNotOwnerTest() {
        ClubModel createdClub =
                step("Владелец создаёт клуб", () ->
                        api.clubs.createClub(randomClub(), owner.getAccessToken()));
        createdClubId = createdClub.id();

        AuthorizedUserModel anotherUser =
                step("Создаём другого пользователя", () ->
                        api.registerAndLogin());

        ClubErrorResponseModel errorResponse =
                step("Другой пользователь пытается обновить чужой клуб", () ->
                        api.clubs.updateClubForbidden(createdClubId, randomClub(), anotherUser.getAccessToken()));

        step("Проверяем ошибку «нет прав»", () ->
                assertThat(errorResponse.getDetail()).isEqualTo(CLUB_FORBIDDEN_ERROR));
    }

    // ---------- DELETE ----------

    @Test
    public void successfulDeleteClubTest() {
        ClubModel createdClub =
                step("Создаём клуб", () ->
                        api.clubs.createClub(randomClub(), owner.getAccessToken()));
        int clubId = createdClub.id();

        step("Удаляем клуб", () ->
                api.clubs.deleteClub(clubId, owner.getAccessToken()));

        step("Проверяем, что клуб больше не находится по id", () ->
                api.clubs.getNotExistingClub(clubId));
    }

    @Test
    public void deleteClubByNotOwnerTest() {
        ClubModel createdClub =
                step("Владелец создаёт клуб", () ->
                        api.clubs.createClub(randomClub(), owner.getAccessToken()));
        createdClubId = createdClub.id();

        AuthorizedUserModel anotherUser =
                step("Создаём другого пользователя", () ->
                        api.registerAndLogin());

        ClubErrorResponseModel errorResponse =
                step("Другой пользователь пытается удалить чужой клуб", () ->
                        api.clubs.deleteClubForbidden(createdClubId, anotherUser.getAccessToken()));

        step("Проверяем ошибку «нет прав»", () ->
                assertThat(errorResponse.getDetail()).isEqualTo(CLUB_FORBIDDEN_ERROR));

        step("Проверяем, что клуб по-прежнему существует", () ->
                assertThat(api.clubs.getClub(createdClubId).id()).isEqualTo(createdClubId));
    }
}
