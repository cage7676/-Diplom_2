package tests.orders;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import services.Ingredients;
import services.Order;
import services.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreateOrderTestCase {
    User user;
    Order order;
    Ingredients ingredients;

    private String login(){
        String email = RandomStringUtils.randomAlphabetic(5) + "@gmail.com";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Response createResponse = user.createUser(email, password, username);
        String token = createResponse.path("accessToken");
        user.loginUser(email, password);
        return token;
    }

    @Before
    public void setUp() {user = new User(); order = new Order(); ingredients = new Ingredients();}

    @Tag("CreateOrder")
    @Test
    @DisplayName("Проверка создания заказа с ингредиентами авторизованным пользователем")
    public void checkOrderCreationWithIngredientsWithAuthorization() {

        String accessToken = login();
        Response responseIngredients = ingredients.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        Response response = order.createOrder(ingredients, accessToken);

        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));
    }

    @Tag("CreateOrder")
    @Test
    @DisplayName("Проверка создания заказа без авторизации")
    public void checkingOrderCreationWithoutAuthorization() {

        Response responseIngredients = ingredients.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        Response response = order.createOrder(ingredients, "");

        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));
    }

    @Tag("CreateOrder")
    @Test
    @DisplayName("Проверка создания заказа без ингредиентов")
    public void checkCreationOrderWithoutIngredients() {

        String accessToken = login();
        Response response = order.createOrder(null, accessToken);

        assertEquals("Неверный код ответа", 400, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", false, response.path("success"));
        assertEquals("Невалидные данные в ответе: message", "Ingredient ids must be provided", response.path("message"));
    }

    @Tag("CreateOrder")
    @Test
    @DisplayName("Проверка создания заказа с неверным хешем ингредиентов")
    public void checkOrderWasCreatedWithInvalidIngredientHash() {

        String accessToken = login();
        Response response = order.createOrder(Arrays.asList("InvalidIngredientHash"), accessToken);

        assertEquals("Неверный код ответа", 500, response.statusCode());
    }
}