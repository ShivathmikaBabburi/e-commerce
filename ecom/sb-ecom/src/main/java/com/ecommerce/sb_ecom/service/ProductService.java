package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Cart;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.*;
import com.ecommerce.sb_ecom.respository.CartRepository;
import com.ecommerce.sb_ecom.respository.CategoryRepository;
import com.ecommerce.sb_ecom.respository.ProductRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Value("${project.image}")
    private String path;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;


    public ProductDTO createProduct(ProductDTO productdto,Long categoryId) {

        Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException(categoryId,"categoryId","category"));
        Product product=modelMapper.map(productdto,Product.class);
        Product savedProduct=repo.findByProductName(product.getProductName());
        if(savedProduct!=null){
            throw new APIException("Product Name already Exists");
        }
        product.setCategory(category);
        Double specialPrice=product.getPrice()-((product.getDiscount()*0.01)*(product.getPrice()));
        product.setSpecialPrice(specialPrice);
        product.setImage("default.png");
        repo.save(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder ) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage=repo.findAll(pageDetails);
        List<Product> products=productPage.getContent();
        if(products.isEmpty()){
            throw new APIException("no products available");
        }
        List<ProductDTO> res=products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse cr=new ProductResponse();
        cr.setContent(res);
        cr.setPageNumber(productPage.getNumber());
        cr.setPageSize(productPage.getSize());
        cr.setTotalElements(productPage.getTotalElements());
        cr.setTotalPages(productPage.getTotalPages());
        cr.setLastPage(productPage.isLast());
        return cr;
    }

    public ProductResponse getProductsByCategory(Long categoryId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException(categoryId,"categoryId","category"));
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage=repo.findAllByCategory(category,pageDetails);
        List<Product> products=productPage.getContent();
        if(products.isEmpty()){
            throw new APIException("no products available in this category");
        }
        List<ProductDTO> res=products.stream()
                .map(product->modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse cr=new ProductResponse();
        cr.setContent(res);
        cr.setPageNumber(productPage.getNumber());
        cr.setPageSize(productPage.getSize());
        cr.setTotalElements(productPage.getTotalElements());
        cr.setTotalPages(productPage.getTotalPages());
        cr.setLastPage(productPage.isLast());
        return cr;
    }

    public ProductResponse getProductsByKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage=repo.findAllByProductNameLikeIgnoreCase('%' +keyword+'%',pageDetails);
        List<Product> products=productPage.getContent();
        List<ProductDTO> res=products.stream()
                .map(product->modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse cr=new ProductResponse();
        cr.setContent(res);
        cr.setPageNumber(productPage.getNumber());
        cr.setPageSize(productPage.getSize());
        cr.setTotalElements(productPage.getTotalElements());
        cr.setTotalPages(productPage.getTotalPages());
        cr.setLastPage(productPage.isLast());
        return cr;

    }

    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product updatedProduct=modelMapper.map(productDTO,Product.class);
        Product product=repo.findById(productId).orElseThrow(()->new ResourceNotFoundException(productId,"productId","Product"));
        product.setProductName(updatedProduct.getProductName());
        product.setDescription(updatedProduct.getDescription());
        product.setQuantity(updatedProduct.getQuantity());
        product.setPrice(updatedProduct.getPrice());
        product.setDiscount(updatedProduct.getDiscount());
        Double specialPrice=product.getPrice()-((product.getDiscount()*0.01)*(product.getPrice()));
        product.setSpecialPrice(specialPrice);
        List<Cart> carts=cartRepository.findCartsByProductId(productId);
        List<CartDTO> cartDTOS=carts.stream().map(cart->{
            CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
            List<ProductDTO> productDTOS=cart.getCartItems().stream().map(p->modelMapper.map(p.getProduct(),ProductDTO.class)).toList();
            cartDTO.setProducts(productDTOS);
            return cartDTO;
        }).toList();
        cartDTOS.forEach(cart->cartService.updateProductInCarts(cart.getCartId(),product.getProductId()));
        ProductDTO res=modelMapper.map(product,ProductDTO.class);
        return res;

    }

    public ProductDTO deleteProduct(Long productId) {
        Product product=repo.findById(productId).orElseThrow(()->new ResourceNotFoundException(productId,"ProductId","Product"));
        ProductDTO productDTO=modelMapper.map(product,ProductDTO.class);
        List<Cart> carts=cartRepository.findCartsByProductId(productId);
        carts.forEach(cart->cartService.deleteProductFromCart(cart.getCartId(), productId));
        repo.delete(product);
        return productDTO;
    }

    public ProductDTO updateImage(Long productId, MultipartFile image) throws IOException {
        Product product=repo.findById(productId).orElseThrow(()->new ResourceNotFoundException(productId,"productId","Product"));
        //upload image to server
        //get the filename of uploaded image

        String fileName=uploadImage(path,image);
        //updating new filename to product
        product.setImage(fileName);

        return modelMapper.map(product, ProductDTO.class);




    }

    private String uploadImage(String path, MultipartFile image) throws IOException {
        //file name of original file
        String originalFileName=image.getOriginalFilename();
        //generate a unique file name
        String randomId= UUID.randomUUID().toString();
        String fileName=randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath=path+ File.separator+fileName;
        //check if path exists and create
        File folder=new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }


        //upload to server
        Files.copy(image.getInputStream(), Paths.get(filePath));
        //returning new file name
        return fileName;
    }
}

