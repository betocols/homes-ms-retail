package com.tenx.ms.retail.stock.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.service.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "stock", description = "Stock API")
@RestController("stockControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @ApiOperation(value = "Create or update an stock")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful retrieval of product's stock"),
        @ApiResponse(code = 404, message = "Stock not found"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/{storeId:\\d+}/{productId:\\d+}", method = RequestMethod.GET)
    public Stock getStockByStockAndProductId(
        @PathVariable("storeId") Long storeId,
        @PathVariable("productId") Long productId) {
        return stockService.getStockByStockAndProductId(storeId, productId);
    }

    @ApiOperation(value = "Upserts the product's stock")
    @ApiResponses( value = {
        @ApiResponse(code = 200, message = "Stock successfully updated"),
        @ApiResponse(code = 412, message = "Validation failure"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @RequestMapping(value = {"/{storeId:\\d+}/{productId:\\d+}"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void upsertProductStock(@PathVariable("storeId") Long storeId,
                                   @PathVariable("productId") Long productId,
                                   @Validated @RequestBody Stock stock) {
        stock.setStoreId(storeId);
        stock.setProductId(productId);
        stockService.upsertProductStock(stock);
    }
}
