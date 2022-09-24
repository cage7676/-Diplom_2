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

public class EditUserTestCase {

    private  User user;
    private void login(String email, String password){user.loginUser(email, password);}

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
    public void setUp() {user = new User();}

    @Tag("EditUser")
    @Test
    @DisplayName("Проверка изменения имени с авторизацией")
    public void checkUsernameChangeVerificationWithAuthorization() {

        Map<String, String> data = create();
        login(data.get("email"), data.get("password"));
        String newUsername = RandomStringUtils.randomAlphabetic(10);
        Response response = user.editUser(data.get("email"), data.get("password"), newUsername, data.get("accessToken"));

        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));
    }

    @Tag("EditUser")
    @Test
    @DisplayName("Проверка изменения пароля с авторизацией")
    public void checkPasswordChangeVerificationWithAuthorization() {

        Map<String, String> data = create();
        login(data.get("email"), data.get("password"));
        String newPassword = RandomStringUtils.randomAlphabetic(10);
        Response response = user.editUser(data.get("email"), newPassword, data.get("name"), data.get("accessToken"));

        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));
    }

    @Tag("EditUser")
    @Test
    @DisplayName("Проверка изменения электронной почты с авторизацией")
    public void checkEmailChangeVerificationWithAuthorization() {

        Map<String, String> data = create();
        login(data.get("email"), data.get("password"));
        String newEmail = RandomStringUtils.randomAlphabetic(5) + "@gmail.com";
        Response response = user.editUser(newEmail, data.get("password"), data.get("name"), data.get("accessToken"));

        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));
    }

    @Tag("EditUser")
    @Test
    @DisplayName("Проверка изменения имени без авторизации")
    public void checkUsernameChangeWithoutAuthorization() {

        Map<String, String> data = create();
        login(data.get("email"), data.get("password"));
        String newUsername = RandomStringUtils.randomAlphabetic(10);
        Response response = user.editUser(data.get("email"), data.get("password"), newUsername, "");

        assertEquals("Неверный код ответа", 401, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", false, response.path("success"));
        assertEquals("Невалидные данные в ответе: message", "You should be authorised", response.path("message"));
    }

    @Tag("EditUser")
    @Test
    @DisplayName("Проверка изменения пароля без авторизации")
    public void checkPasswordChangeWithoutAuthorization() {

        Map<String, String> data = create();
        login(data.get("email"), data.get("password"));
        String newPassword = RandomStringUtils.randomAlphabetic(10);
        Response response = user.editUser(data.get("email"), newPassword, data.get("name"), "");

        assertEquals("Неверный код ответа", 401, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", false, response.path("success"));
        assertEquals("Невалидные данные в ответе: message", "You should be authorised", response.path("message"));
    }

    @Tag("EditUser")
    @Test
    @DisplayName("Проверка изменения электронной почты без авторизации")
    public void checkEmailChangeWithoutAuthorization() {

        Map<String, String> data = create();
        login(data.get("email"), data.get("password"));
        String newEmail = RandomStringUtils.randomAlphabetic(5) + "@gmail.com";
        Response response = user.editUser(newEmail, data.get("password"), data.get("name"), "");

        assertEquals("Неверный код ответа", 401, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", false, response.path("success"));
        assertEquals("Невалидные данные в ответе: message", "You should be authorised", response.path("message"));
    }
}
