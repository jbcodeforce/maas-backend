package ibm.gse.eda.maas;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class KClusterResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/kclusters")
          .then()
             .statusCode(200)
             ;
    }

}