package services;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Specification;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class User extends Specification {

    private static final String USER_PATH = "api/auth/";

    @Step("Регистрация пользователя")
    public Response createUser(String email, String password, String username) {
        JSONObject requestBodyJson = new JSONObject();
        String requestBody = requestBodyJson
                .put("email", email)
                .put("password", password)
                .put("name", username)
                .toString();
        Response response = given()
                .spec(getBaseSpec())
                .body(requestBody)
                .when()
                .post(USER_PATH + "register/");
        return response;
    }

    @Step("Авторизация пользователя")
    public Response loginUser(String email, String password) {
        JSONObject requestBodyJson = new JSONObject();
        String requestBody = requestBodyJson
                .put("email", email)
                .put("password", password)
                .toString();
        Response response = given()
                .spec(getBaseSpec())
                .body(requestBody)
                .when()
                .post(USER_PATH + "login/");
        return response;
    }

    @Step("Изменение данных пользователя")
    public Response editUser(String email, String password, String username, String token) {
        JSONObject requestBodyJson = new JSONObject();
        String requestBody = requestBodyJson
                .put("email", email)
                .put("password", password)
                .put("name", username)
                .toString();
        Response response = given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .body(requestBody)
                .when()
                .patch(USER_PATH + "user/");
        return response;
    }
}
