package product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.product.rest.dto.Product;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
public class ProductControllerTest extends AbstractIntegrationTest {

    private static final String API_VERSION = RestConstants.VERSION_ONE;
    private static final String PRODUCT_REQUEST_URI = "%s" + API_VERSION + "/products/%s";
    private static final String STORE_REQUEST_URI = "%s" + API_VERSION + "/stores";
    private final RestTemplate template = new TestRestTemplate();

    @Value("classpath:product/noNameProduct.json")
    private File noNameProduct;
    @Value("classpath:product/noDescriptionProduct.json")
    private File noDescriptionProduct;
    @Value("classpath:product/noPriceProduct.json")
    private File noPriceProduct;
    @Value("classpath:product/noSkuProduct.json")
    private File noSkuProduct;
    @Value("classpath:product/successProduct0.json")
    private File successProduct0;
    @Value("classpath:product/successProduct1.json")
    private File successProduct1;
    @Value("classpath:product/successProduct2.json")
    private File successProduct2;

    @Value("classpath:store/successStore.json")
    private File successStore;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String NULL_ASSERT_FAILURE = "Expecting instance of a class";
    private final static String STORE_ID = "1";

    @Before
    public void createStore() {
        try {
            getJSONResponse(template, String.format(STORE_REQUEST_URI, basePath()), FileUtils.readFileToString(successStore), HttpMethod.POST);
        } catch (IOException e) {
            fail("Store wasn't created");
        }
    }

    //Tests
    @Test
    @FlywayTest
    public void testCreateProductSuccess() {
        ResourceCreated<Long> productCreated = createProduct(successProduct0);
        assertNotNull(NULL_ASSERT_FAILURE, productCreated);
        Long id = productCreated.getId();
        Product product = getProduct(id);
        assertNotNull(NULL_ASSERT_FAILURE, product);
        checkProductName(product, "ProductTest0");
        checkDescription(product, "ProductTestDescription0");
        checkProductSku(product, "00000");
        checkProductPrice(product, new BigDecimal("1.00"));
    }

    @Test
    @FlywayTest
    public void testGetProductInStoreSuccess(){
        ResourceCreated<Long> product2 = createProduct(successProduct0);
        ResourceCreated<Long> product1 = createProduct(successProduct1);
        ResourceCreated<Long> product3 = createProduct(successProduct2);
        List<ResourceCreated> productsCreated = new ArrayList<>(Arrays.asList(product1, product2, product3));
        try {
            ResponseEntity<String> response =
                getJSONResponse(template, String.format(PRODUCT_REQUEST_URI, basePath(), STORE_ID), null, HttpMethod.GET);
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            String received = response.getBody();
            List<Product> products = objectMapper.readValue(received, new TypeReference<List<Product>>(){});
            assertEquals("Expected 3 Products", products.size(), 3);

            for (ResourceCreated product: productsCreated) {
                boolean isPresent = products
                    .stream()
                    .filter(o -> o.getProductId()
                        .equals(Long.valueOf(product.getId().toString())))
                    .findFirst()
                    .isPresent();
                assertEquals("Product " + product.getId() + " should be present in result" , isPresent, true);
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testCreateProductNoDescriptionError() {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(PRODUCT_REQUEST_URI, basePath(), STORE_ID), FileUtils.readFileToString(noDescriptionProduct), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testCreateProductNoNameError() {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(PRODUCT_REQUEST_URI, basePath(), STORE_ID), FileUtils.readFileToString(noNameProduct), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testCreateProductNoPriceError() {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(PRODUCT_REQUEST_URI, basePath(), STORE_ID), FileUtils.readFileToString(noPriceProduct), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testCreateProductNoSkuError() {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(PRODUCT_REQUEST_URI, basePath(), STORE_ID), FileUtils.readFileToString(noSkuProduct), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    //Helper methods
    private Product getProduct(Long productId) {
        try {
            ResponseEntity<String> response =
                getJSONResponse(template, String.format(PRODUCT_REQUEST_URI, basePath(), STORE_ID) + "/" + productId, null, HttpMethod.GET);
            String received = response.getBody();
            assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
            if (received == null || received.isEmpty()){
                return null;
            }
            return objectMapper.readValue(received, Product.class);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return null;
    }

    private ResourceCreated<Long> createProduct(File file) {
        try {
            ResponseEntity<String> response =
                getJSONResponse(template, String.format(PRODUCT_REQUEST_URI, basePath(), STORE_ID), FileUtils.readFileToString(file), HttpMethod.POST);
            String received = response.getBody();
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            return objectMapper.readValue(received, new TypeReference<ResourceCreated<Long>>() { });
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return null;
    }

    private void checkProductSku(Product product, String sku) {
        assertEquals("Sky does not match", product.getSku(), sku);
    }

    private void checkDescription(Product product, String description) {
        assertEquals("Description does not match", product.getDescription(), description);
    }

    private void checkProductName(Product product, String name) {
        assertEquals("Name does not match", product.getName(), name);
    }

    private void checkProductPrice(Product product, BigDecimal price) {
        assertEquals("Price does not match", product.getPrice(), price);
    }
}
