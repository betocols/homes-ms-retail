package com.tenx.ms.retail.store.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.store.rest.dto.Store;
import com.tenx.ms.retail.store.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "store", description = "Store API")
@RestController("storeControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "Gets all stores")
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "Successful retrieval of stores"),
            @ApiResponse(code = 500, message = "Internal Server Error")
        }
    )
    @RequestMapping(method = RequestMethod.GET)
    public List<Store> getStores() {
        return storeService.getStores();
    }

    @ApiOperation(value = "Gets store by its id")
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "Successful retrieval of the store"),
            @ApiResponse(code = 404, message = "Store couldn't be found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
        }
    )
    @RequestMapping(value = {"/{id:\\d+}"}, method = RequestMethod.GET)
    public Store getStoreById(@ApiParam(name = "storeId", value = "The store id") @PathVariable() Long id) {
        return storeService.getStoreById(id);
    }

    @ApiOperation(value = "Creates a new store")
    @ApiResponses( value = {
        @ApiResponse(code = 201, message = "Store successfully created"),
        @ApiResponse(code = 412, message = "Validation failure"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResourceCreated<Long> create(@RequestBody Store store){
        Long storeId = storeService.create(store);
        return new ResourceCreated<>(storeId);
    }
}
