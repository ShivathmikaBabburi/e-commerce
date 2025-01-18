package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.config.AppConstants;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import com.ecommerce.sb_ecom.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;

@RestController

public class ProductController {
    @Autowired
    private ProductService service;

    @PostMapping("api/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO product, @PathVariable Long categoryId){
        ProductDTO productDTO =service.createProduct(product,categoryId);
        return new ResponseEntity<>(productDTO, CREATED);
    }
    @GetMapping("/api/public/products")
    public ResponseEntity<ProductResponse> getAllProducts( @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                           @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                           @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY,required=false) String sortBy,
                                                           @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder){
        ProductResponse productResponse=service.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    @GetMapping("api/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,@RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                                 @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY,required=false) String sortBy,
                                                                 @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder
    ){
        ProductResponse pr=service.getProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(pr,HttpStatus.OK);
    }
    @GetMapping("api/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,@RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                                @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY,required=false) String sortBy,
                                                                @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder){
        ProductResponse pr=service.getProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(pr,HttpStatus.OK);
    }
    @PutMapping("api/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId,@Valid @RequestBody ProductDTO productDTO ){
        ProductDTO res=service.updateProduct(productDTO,productId);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
    @DeleteMapping("api/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO productDTO=service.deleteProduct(productId);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }
    @PutMapping("api/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateImage(@PathVariable Long productId, @RequestParam("image")MultipartFile image) throws IOException {
        ProductDTO productDTO=service.updateImage(productId,image);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);

    }

}
