package stock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.stock.rest.dto.Stock;
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
public class StockControllerTest extends AbstractIntegrationTest {

    private static final String API_VERSION = RestConstants.VERSION_ONE;
    private static final String PRODUCT_REQUEST_URI = "%s" + API_VERSION + "/products/%s";
    private static final String STORE_REQUEST_URI = "%s" + API_VERSION + "/stores";
    private static final String STOCK_REQUEST_URI = "%s" + API_VERSION + "/stocks/%s";
    private final RestTemplate template = new TestRestTemplate();

    @Value("classpath:stock/noCountStock.json")
    private File noCountStock;
    @Value("classpath:stock/successStock0.json")
    private File successStock0;
    @Value("classpath:stock/successStock1.json")
    private File successStock1;

    @Value("classpath:store/successStore0.json")
    private File successStore0;

    @Value("classpath:product/successProduct0.json")
    private File successProduct0;
    @Value("classpath:product/successProduct1.json")
    private File successProduct1;

    private Long storeId;
    private Long productId;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String NULL_ASSERT_FAILURE = "Expecting instance of a class";

    @Before
    public void createStoreAndProducts() {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(STORE_REQUEST_URI, basePath()), FileUtils.readFileToString(successStore0), HttpMethod.POST);
            String responseBody = response.getBody();
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            ResourceCreated<Long> resourceResult = objectMapper.readValue(responseBody, new TypeReference<ResourceCreated<Long>>() { });
            storeId = resourceResult.getId();

            response = getJSONResponse(template, String.format(PRODUCT_REQUEST_URI, basePath(), storeId), FileUtils.readFileToString(successProduct0), HttpMethod.POST);
            responseBody = response.getBody();
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            resourceResult = objectMapper.readValue(responseBody, new TypeReference<ResourceCreated<Long>>() { });
            productId = resourceResult.getId();

            response = getJSONResponse(template, String.format(PRODUCT_REQUEST_URI, basePath(), storeId), FileUtils.readFileToString(successProduct1), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
        } catch (IOException e) {
            fail("Couldn't create store and products");
        }
    }

    @Test
    @FlywayTest
    public void testUpsertStockSuccess() {
        upsertStock(successStock0);
        Stock stock0 = getStock(storeId, productId);
        assertNotNull(NULL_ASSERT_FAILURE, stock0);
        checkStockCount(stock0, 1L);
        checkStockStoreId(stock0, storeId);
        checkStockProductId(stock0, productId);

        upsertStock(successStock1);
        Stock stock1 = getStock(storeId, productId);
        assertNotNull(NULL_ASSERT_FAILURE, stock1);
        checkStockCount(stock1, 2L);
        checkStockStoreId(stock1, storeId);
        checkStockProductId(stock1, productId);
    }

    @Test
    @FlywayTest
    public void testCreateStockNoCountError(){
        try {
            ResponseEntity<String> response =
                getJSONResponse(template,
                    String.format(STOCK_REQUEST_URI + "/%s", basePath(), storeId, productId),
                    FileUtils.readFileToString(noCountStock), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        } catch (IOException e) {
            fail("Couldn't create Stock");
        }
    }

    //Helper methods
    private Stock getStock(Long storeId, Long productId) {
        Stock stock = null;
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(STOCK_REQUEST_URI + "/%s", basePath(), storeId, productId), null, HttpMethod.GET);
            String received = response.getBody();
            assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
            stock = objectMapper.readValue(received, Stock.class);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return stock;
    }

    private void upsertStock(File file) {
        try {
            ResponseEntity<String> response =
                getJSONResponse(template, String.format(STOCK_REQUEST_URI + "/%s", basePath(), storeId, productId), FileUtils.readFileToString(file), HttpMethod.POST);
            String responseBody = response.getBody();
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            assertEquals("Body is not empty", responseBody, null);
        } catch (IOException e) {
            fail("Couldn't create Stock");
        }
    }

    private void checkStockCount(Stock stock, Long count) {
        assertEquals("Stock Count doesnt match expected", stock.getCount(), count);
    }

    private void checkStockStoreId(Stock stock, Long storeId) {
        assertEquals("Stock store id doesnt match expected", stock.getStoreId(), storeId);
    }

    private void checkStockProductId(Stock stock, Long productId) {
        assertEquals("Stock product id doesnt match expected", stock.getProductId(), productId);
    }
}

