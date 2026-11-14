package models.clubs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Ответ 400 на POST/PUT/PATCH /clubs/: ошибки валидации по полям.
 * Пример: {"bookTitle": ["This field may not be blank."]}
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClubValidationErrorResponseModel {
    private List<String> bookTitle;
    private List<String> bookAuthors;
    private List<String> publicationYear;
    private List<String> description;
    private List<String> telegramChatLink;
}
