package tests.data;

import models.clubs.ClubBodyModel;
import net.datafaker.Faker;

/**
 * Генерация случайных данных для клубов.
 */
public class ClubTestData {

    private static final Faker faker = new Faker();

    public static ClubBodyModel randomClub() {
        return ClubBodyModel.builder()
                .bookTitle(faker.book().title())
                .bookAuthors(faker.book().author())
                .publicationYear(faker.number().numberBetween(1950, 2024))
                .description(faker.lorem().sentence(8))
                .telegramChatLink("https://t.me/club_" + faker.number().digits(8))
                .build();
    }
}
