package com.ecommerce.sb_ecom.respository;

import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {


    Page<Product> findAllByCategory(Category category, Pageable pageDetails);

    Page<Product> findAllByProductNameLikeIgnoreCase(String keyword,Pageable pageDetails);

    Product findByProductName(String productName);
}
