package com.tenx.ms.retail.product.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "product", description = "Product API")
@RestController("productControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Gets all products from a store")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful retrieval of store's products"),
        @ApiResponse(code = 404, message = "Store couldn't be found"),
        @ApiResponse(code = 500, message = "Internal Server Error")}
    )
    @RequestMapping(value = {"/{storeId:\\d+}"}, method = RequestMethod.GET)
    public List<Product> getAllProductsInStore(@ApiParam(name = "storeId", value = "Store id")
                                             @PathVariable() Long storeId) {
        return productService.getAllProductsInStore(storeId);
    }

    @ApiOperation(value = "Gets a product from a store")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful retrieval of store's product"),
        @ApiResponse(code = 404, message = "Store or Product couldn't be found"),
        @ApiResponse(code = 500, message = "Internal Server Error")}
    )
    @RequestMapping(value = "/{storeId:\\d+}/{productId:\\d+}", method = RequestMethod.GET)
    public Product getProductInStore(@PathVariable("storeId") Long storeId,
                                     @PathVariable("productId") Long productId) {
        return productService.getProductInStore(productId, storeId);
    }

    @ApiOperation(value = "Creates a new product for a store")
    @ApiResponses( value = {
        @ApiResponse(code = 200, message = "Product successfully created"),
        @ApiResponse(code = 412, message = "Validation failure"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @RequestMapping(value = {"/{storeId:\\d+}"}, method = RequestMethod.POST)
    public ResourceCreated<Long> createProductInStore(@PathVariable("storeId") Long storeId,
                                                      @Validated @RequestBody Product product) {
        Long productId = productService.createProductInStore(product, storeId);
        return new ResourceCreated<>(productId);
    }
}
