package models.registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@Data
@NoArgsConstructor
@AllArgsConstructor

public class BlankCredentialsResponseModel {
    private List<String> username;
    private List<String> password;
}