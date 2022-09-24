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

public class CreateUserTestCase {

    private User user;

    private Map<String,String> cred(){
        String email = RandomStringUtils.randomAlphabetic(5) + "@gmail.com";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Map<String, String> inputDataMap = new HashMap<>();
        inputDataMap.put("username", username);
        inputDataMap.put("email", email);
        inputDataMap.put("password", password);
        return inputDataMap;
    }

    @Before
    public void setUp() {
        user = new User();
    }

    @Tag("CreateUser")
    @Test
    @DisplayName("Проверка создания уникального пользователя")
    public void checkCreationUniqueUser() {

        Map<String,String> data = cred();
        Response response = user.createUser(data.get("email"), data.get("password"), data.get("username"));

        assertEquals("Неверный код ответа", 200, response.statusCode());
    }

    @Tag("CreateUser")
    @Test
    @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
    public void checkCreationUserThatAlreadyRegistered() {

        Map<String,String> data = cred();
        user.createUser(data.get("email"), data.get("password"), data.get("username"));
        Response response = user.createUser(data.get("email"), data.get("password"), data.get("username"));

        assertEquals("Неверный код ответа", 403, response.statusCode());
        assertEquals("Невалидные данные в ответе: message", "User already exists", response.path("message"));
    }

    @Tag("CreateUser")
    @Test
    @DisplayName("Проверка создания пользователя без заполнения одного из обязательных полей")
    public void checkUserCreationWithoutFillingInOneRequiredFields() {

        Map<String,String> data = cred();
        Response response = user.createUser(data.get("email"), data.get(""), data.get("username"));

        assertEquals("Неверный код ответа", 403, response.statusCode());
        assertEquals("Невалидные данные в ответе: message", "Email, password and name are required fields", response.path("message"));
    }
}