package services;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Specification;

import static io.restassured.RestAssured.given;

public class Ingredients extends Specification {

    private static final String INGREDIENTS_PATH = "api/ingredients";

    @Step("Получение данных об ингредиентах")
    public Response getIngredients() {
        Response response = given()
                .spec(getBaseSpec())
                .get(INGREDIENTS_PATH);
        return response;
    }
}
