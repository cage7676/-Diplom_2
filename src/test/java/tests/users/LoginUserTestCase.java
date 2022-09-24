package tests.users;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import services.User;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LoginUserTestCase {

    private User user;

    private Map<String, String> create(){
        String username = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphabetic(5) + "@gmail.com";
        Response response = user.createUser(email, password, username);
        String token = response.path("accessToken");
        Map<String, String> inputDataMap = new HashMap<>();
        inputDataMap.put("email", email);
        inputDataMap.put("password", password);
        inputDataMap.put("name", username);
        inputDataMap.put("accessToken", token);
        return inputDataMap;
    }

    @Before
    public void setUp() {
        user = new User();
    }

    @Tag("LoginUser")
    @Test
    @DisplayName("Проверка логина под существующим пользователем")
    public void checkLoginUnderExistingUser() {

        Map<String, String> data = create();
        Response response = user.loginUser(data.get("email"), data.get("password"));

        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));
    }

    @Tag("LoginUser")
    @Test
    @DisplayName("Проверка логина  с неверным логином и паролем")
    public void checkLoginVerificationWithIncorrectUsernameAndPassword() {

        Map<String, String> data = create();
        Response response = user.loginUser(data.get("email"), "incorrect");

        assertEquals("Неверный код ответа", 401, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", false, response.path("success"));
        assertEquals("Невалидные данные в ответе: message", "email or password are incorrect", response.path("message"));
    }
}