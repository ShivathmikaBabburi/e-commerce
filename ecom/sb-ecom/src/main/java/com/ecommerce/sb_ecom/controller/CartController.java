package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.payload.CartDTO;
import com.ecommerce.sb_ecom.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService service;
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addItem(@PathVariable Long productId, @PathVariable Integer quantity)
    {
        CartDTO cartDTO=service.addItem(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }


    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts(){
        List<CartDTO> cartDTOS=service.getAllCarts();
        return new ResponseEntity<>(cartDTOS,HttpStatus.OK);
    }
    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getUserCart(){
        CartDTO cartDTO=service.getUserCart();
        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
                                                     @PathVariable String operation) {

        CartDTO cartDTO = service.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
                                                        @PathVariable Long productId) {
        String status = service.deleteProductFromCart(cartId, productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }


}
