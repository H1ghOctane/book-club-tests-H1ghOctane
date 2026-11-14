package models.user;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Новый пользователь, зарегистрированный для теста, вместе с его access-токеном.
 */
@Data
@AllArgsConstructor
public class AuthorizedUserModel {
    private Integer id;
    private String username;
    private String accessToken;
}
