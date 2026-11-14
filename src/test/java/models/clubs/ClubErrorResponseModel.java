package models.clubs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ответ 401/403/404 на запросы к /clubs/.
 * Пример: {"detail": "You do not have permission to perform this action."}
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClubErrorResponseModel {
    private String detail;
}
