package tests;

public class TestData {

    public static final String LOGIN_USERNAME = "p.maltsev";
    public static final String LOGIN_PASSWORD = "4Q8-Seb-G7C-Jcj";
    public static final String LOGIN_WRONG_PASSWORD = "qaguru1234";
    public static final String LOGIN_TOKEN_PREFIX = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public static final String LOGIN_WRONG_CREDENTIALS_ERROR = "Invalid username or password.";
    public static final String LOGIN_BLANK_ERROR = "This field may not be blank.";
    public static final String LOGIN_FIELD_IS_NULL_ERROR =  "This field may not be null.";;
    public static final String REGISTRATION_EXISTING_USER_ERROR =
            "A user with that username already exists.";
    public static final String REGISTRATION_IP_REGEXP =
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
                    + "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";
    public static final String REGISTRATION_BLANK_ERROR = "This field may not be blank.";
    public static final String REGISTRATION_FIELD_IS_NULL_ERROR = "This field may not be null.";
    public static final String LOGOUT_INVALID_TOKEN = "invalid_refresh_token_12345";
    public static final String LOGOUT_INVALID_TOKEN_ERROR = "Token is invalid";
    public static final String LOGOUT_TOKEN_NOT_VALID_CODE = "token_not_valid";
    public static final String LOGOUT_BLANK_TOKEN_ERROR = "This field may not be blank.";
    public static final String UPDATE_FIRST_NAME = "Ivan";
    public static final String UPDATE_LAST_NAME = "Petrov";
    public static final String UPDATE_INVALID_EMAIL = "invalid_email_format";
    public static final String UPDATE_INVALID_EMAIL_ERROR = "Enter a valid email address.";
    public static final String UPDATE_REQUIRED_FIELD_ERROR = "This field is required.";
    public static final String UPDATE_BLANK_FIELD_ERROR = "This field may not be blank.";
    public static final String UPDATE_INVALID_ACCESS_TOKEN = "invalid_access_token_12345";
    public static final String UPDATE_NOT_AUTHENTICATED_ERROR = "Authentication credentials were not provided.";
    public static final String UPDATE_INVALID_TOKEN_ERROR = "Given token not valid for any token type";
    public static final String UPDATE_TOKEN_NOT_VALID_CODE = "token_not_valid";
}

