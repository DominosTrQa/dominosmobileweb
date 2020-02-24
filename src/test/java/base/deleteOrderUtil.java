package base;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.log4j.Logger;
import org.junit.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class deleteOrderUtil {

    protected static final Logger log = Logger.getLogger(deleteOrderUtil.class);

    public static void deleteAnOrder(String orderId) {

        RestAssured.baseURI = "";
        RestAssured.basePath = "/";
        RestAssured.requestSpecification = new RequestSpecBuilder().build().accept(ContentType.JSON).contentType(ContentType.JSON);

        long responseTimeout = 7000L;

        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.addProperty("status", "15");
        json.addProperty("message", "Test");
        String jsonString = gson.toJson(json);

        ValidatableResponse then = given().baseUri("").header("cache-control", "no-cache")
                .header("content-type", "application/json")
                .header("postman-token", "e7a8302c-a744-18a0-6336-b564e0ee616d")
                .header("x-mdns-username", "modanisa")
                .header("x-mdns-cs-user-id", "20362")
                .header("x-mdns-timestamp", "1422969978")
                .header("x-mdns-version", "1.0.0")
                .header("x-mdns-token", "2de2d71bbb8c094609ac3d14541cc878")
                .header("x-mdns-language", "TR")
                .header("x-mdns-currency", "TRY")
                .body(jsonString)
                .when()
                .post("cs/order/" + orderId + "/cancel").then();

        log.info("--------" + then.extract().asString());

        then
                .statusCode(200)
                .and()
                .time(lessThan(responseTimeout))
                .extract()
                .response()
                .prettyPrint();

        String result2 = then.extract().jsonPath().getString("data.status");

        log.info(result2);

        Assert.assertTrue("Sipariş silme işlemi yapılamadı", result2.contains("true"));
        System.out.println("'" + orderId + "' orderId li sipariş silme işlemi tamamlandı.");
        RestAssured.reset();
    }
}
