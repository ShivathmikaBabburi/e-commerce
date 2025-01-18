package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;
import com.ecommerce.sb_ecom.respository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class CategoryService {


    @Autowired
    private CategoryRepository repo;
    @Autowired
    private ModelMapper modelMapper;
    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder ){
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable  pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage=repo.findAll(pageDetails);
        List<Category> categories=categoryPage.getContent();
        if(categories.isEmpty()){
            throw new APIException("no categories available");
        }
        List<CategoryDTO> res=categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
        CategoryResponse cr=new CategoryResponse();
        cr.setContent(res);
        cr.setPageNumber(categoryPage.getNumber());
        cr.setPageSize(categoryPage.getSize());
        cr.setTotalElements(categoryPage.getTotalElements());
        cr.setTotalPages(categoryPage.getTotalPages());
        cr.setLastPage(categoryPage.isLast());
        return cr;
    }
    public CategoryDTO createCategory(CategoryDTO categorydto){
        Category category=modelMapper.map(categorydto,Category.class);
        Category savedCategory=repo.findByCategoryName(category.getCategoryName());
        if(savedCategory!=null){
            throw new APIException("Category Name already Exists");
        }


        repo.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    public CategoryDTO deleteCategory(Long categoryId) {
        Category category=repo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException(categoryId,"categoryId","Category"));
        CategoryDTO res=modelMapper.map(category, CategoryDTO.class);
        repo.delete(category);
        return res;
    }

    public CategoryDTO updateCategory(CategoryDTO categorydto, Long categoryId) {
        Category category=modelMapper.map(categorydto,Category.class);
        Category savedCategory=repo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException(categoryId,"categoryId","Category"));
        Category renameCategory=repo.findByCategoryName(category.getCategoryName());
        if(renameCategory!=null){
            throw new APIException("Category Name already Exists");
        }
        savedCategory.setCategoryName(category.getCategoryName());
        repo.save(savedCategory);
        return modelMapper.map(savedCategory, CategoryDTO.class);

    }
}
