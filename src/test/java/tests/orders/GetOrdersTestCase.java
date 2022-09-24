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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class GetOrdersTestCase {
    User user;
    Order order;
    Ingredients ingredients;

    private String createAndLoginUser(){
        String username = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphabetic(5) + "@gmail.com";
        Response response = user.createUser(email, password, username);
        user.loginUser(email, password);
        String token = response.path("accessToken");
        return token;
    }

    @Before
    public void setUp() {user = new User(); order = new Order(); ingredients = new Ingredients();}

    @Tag("GetOrders")
    @Test
    @DisplayName("Проверка получения списка заказов авторизованным пользователем")
    public void checkAuthorizedUserReceivedOrderList() {

        String token = createAndLoginUser();

        Response responseIngredients = ingredients.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        order.createOrder(ingredients, token);
        Response response = order.getOrders(token);

        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));
        assertThat("Заказа не существует", response.path("orders"), notNullValue());
    }

    @Tag("GetOrders")
    @Test
    @DisplayName("Проверка получения списка заказ без авторизации")
    public void checkAuthorizedNotUserReceivedOrderList() {

        String token = createAndLoginUser();

        Response responseIngredients = ingredients.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        order.createOrder(ingredients, token);
        Response response = order.getOrders("");

        assertEquals("Неверный код ответа", 401, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", false, response.path("success"));
        assertEquals("Невалидные данные в ответе: message", "You should be authorised", response.path("message"));
    }
}