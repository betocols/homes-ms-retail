package order;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.order.domain.enums.OrderStatusEnum;
import com.tenx.ms.retail.order.rest.dto.Order;
import org.apache.commons.io.FileUtils;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
public class OrderControllerTest extends AbstractIntegrationTest {

    private static final String API_VERSION = RestConstants.VERSION_ONE;
    private static final String PRODUCT_REQUEST_URI = "%s" + API_VERSION + "/products/%s";
    private static final String STORE_REQUEST_URI = "%s" + API_VERSION + "/stores";
    private static final String STOCK_REQUEST_URI = "%s" + API_VERSION + "/stocks/%s/%s";
    private static final String ORDER_REQUEST_URI = "%s" + API_VERSION + "/orders/%s";
    private final RestTemplate template = new TestRestTemplate();


    @Value("classpath:store/successStore0.json")
    private File successStore0;

    @Value("classpath:product/successProduct0.json")
    private File successProduct0;
    @Value("classpath:product/successProduct1.json")
    private File successProduct1;

    @Value("classpath:stock/successStock0.json")
    private File successStock0;

    @Value("classpath:order/successOrder0.json")
    private File successOrder0;

    @Value("classpath:order/noStockOrder.json")
    private File noStockOrder;

    private Integer storeId;


    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void createStoresProductsAndStocks(){
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(STORE_REQUEST_URI, basePath()), FileUtils.readFileToString(successStore0), HttpMethod.POST);
            String received = response.getBody();
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            storeId = (Integer) objectMapper.readValue(received, ResourceCreated.class).getId();

            response = getJSONResponse(template, String.format(PRODUCT_REQUEST_URI, basePath(), storeId), FileUtils.readFileToString(successProduct0), HttpMethod.POST);
            received = response.getBody();
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            Integer productId1 = (Integer) objectMapper.readValue(received, ResourceCreated.class).getId();

            response = getJSONResponse(template, String.format(PRODUCT_REQUEST_URI, basePath(), storeId), FileUtils.readFileToString(successProduct1), HttpMethod.POST);
            received = response.getBody();
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            Integer productId2 = (Integer) objectMapper.readValue(received, ResourceCreated.class).getId();

            response = getJSONResponse(template, String.format(STOCK_REQUEST_URI, basePath(), storeId, productId1), FileUtils.readFileToString(successStock0), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());

            response = getJSONResponse(template, String.format(STOCK_REQUEST_URI, basePath(), storeId, productId2), FileUtils.readFileToString(successStock0), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());

        } catch (IOException e) {
            fail("Couldn't create store and products");
        }
    }

    @Test
    @FlywayTest
    public void testCreateOrderSuccess(){
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(ORDER_REQUEST_URI, basePath(), storeId), FileUtils.readFileToString(successOrder0), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            String received = response.getBody();
            Order order = objectMapper.readValue(received, Order.class);
            checkOrderId(order, 1L);
            checkOrderStatus(order, OrderStatusEnum.ORDERED);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testCreateOrderBackOrderedNotEnoughStock() {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(ORDER_REQUEST_URI, basePath(), storeId), FileUtils.readFileToString(noStockOrder), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            String received = response.getBody();
            Order order = objectMapper.readValue(received, Order.class);
            assertEquals("Size of back ordered order items", order.getBackOrderedItems().size(), 2);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }


    //Helper methods
    private Order getOrder(Long orderId){
        ResponseEntity<String> response = getJSONResponse(template, String.format(ORDER_REQUEST_URI, basePath(), orderId), null, HttpMethod.GET);
        assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
        String received = response.getBody();
        try {
            if (received == null || received.isEmpty()){
                return null;
            }
            return objectMapper.readValue(received, Order.class);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return null;
    }

    private void checkOrderId(Order order, Long id) {
        assertEquals("Order id expected to be 1", order.getOrderId(), id);
    }

    private void checkOrderStatus(Order order, OrderStatusEnum orderStatus){
        assertEquals("Status of order is expected to be ", order.getStatus(), orderStatus);
    }
}
