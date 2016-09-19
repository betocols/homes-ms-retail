package store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.apache.commons.io.FileUtils;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
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
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.springframework.test.util.AssertionErrors.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
public class StoreControllerTest extends AbstractIntegrationTest {

    private static final String API_VERSION = RestConstants.VERSION_ONE;
    private static final String REQUEST_URI = "%s" + API_VERSION + "/stores/";
    private final RestTemplate template = new TestRestTemplate();

    @Value("classpath:store/successStore0.json")
    private File successStore0;
    @Value("classpath:store/successStore1.json")
    private File successStore1;
    @Value("classpath:store/successStore2.json")
    private File successStore2;
    @Value("classpath:store/noNameStore.json")
    private File noNameStore;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String NULL_ASSERT_FAILURE = "Expecting instance of a class";

    // Tests
    @Test
    public void testStoreNotFoundByIdError() {
        createStore(successStore0);
        ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()) + "2000", null, HttpMethod.GET);
        assertEquals("Get a store not found", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @FlywayTest
    public void testCreateStoreNoNameError() {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()), FileUtils.readFileToString(noNameStore), HttpMethod.POST);
            assertEquals("HTTP Status code incorrect", HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @FlywayTest
    public void testCreateStoreSuccess() {
        ResourceCreated<Long> storeCreated = createStore(successStore0);
        assertNotNull(NULL_ASSERT_FAILURE, storeCreated);
        Long id = storeCreated.getId();
        Store store = getStore(id);
        assertNotNull(NULL_ASSERT_FAILURE, store);
        checkStoreName(store, "StoreTest0");
    }

    @Test
    @FlywayTest
    public void testGetStoresSuccess() {
        createStore(successStore0);
        createStore(successStore1);
        createStore(successStore2);
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()), null, HttpMethod.GET);
            String received = response.getBody();
            assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
            List<Store> stores = objectMapper.readValue(received, new TypeReference<List<Store>>(){});
            assertEquals("Expected 3 Stores", stores.size(), 3);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    //Helper methods
    private ResourceCreated createStore(File file) {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()), FileUtils.readFileToString(file), HttpMethod.POST);
            String received = response.getBody();
            assertEquals("HTTP Status code incorrect", HttpStatus.OK, response.getStatusCode());
            return objectMapper.readValue(received,  new TypeReference<ResourceCreated<Long>>() { });
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return null;
    }

    private Store getStore(Long storeId) {
        try {
            ResponseEntity<String> response = getJSONResponse(template, String.format(REQUEST_URI, basePath()) + storeId, null, HttpMethod.GET);
            String received = response.getBody();
            assertEquals(String.format("HTTP Status code incorrect %s", response.getBody()), HttpStatus.OK, response.getStatusCode());
            return objectMapper.readValue(received, Store.class);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return null;
    }

    private void checkStoreName(Store store, String name) {
        assertEquals("Name does not match", store.getName(), name);
    }
}
