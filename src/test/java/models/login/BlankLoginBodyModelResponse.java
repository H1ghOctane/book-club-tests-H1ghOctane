package models.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BlankLoginBodyModelResponse {
    private List<String> username;
    private List<String> password;
}