package models.registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BlankCredentialsResponseModel {
    private List<String> username;
    private List<String> password;
}