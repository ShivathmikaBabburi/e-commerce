package com.ecommerce.sb_ecom.controller;


import com.ecommerce.sb_ecom.config.AppConstants;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;
import com.ecommerce.sb_ecom.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import static org.springframework.http.HttpStatus.*;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService service;
    @GetMapping("api/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY,required=false) String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder
    ){
        CategoryResponse allCategories = service.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(allCategories, OK);
    }

    @PostMapping("api/admin/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO category){
        CategoryDTO cdto=service.createCategory(category);
        return new ResponseEntity<>(cdto, CREATED);
    }

    @DeleteMapping("api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
        CategoryDTO res=service.deleteCategory(categoryId);
        return ResponseEntity.ok(res);
    }

    @PutMapping("api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO category,@PathVariable Long categoryId){
            CategoryDTO res=service.updateCategory(category,categoryId);
            return ResponseEntity.ok(res);
        }


    }



