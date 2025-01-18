package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Cart;
import com.ecommerce.sb_ecom.model.CartItem;
import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.CartDTO;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.respository.CartItemRepository;
import com.ecommerce.sb_ecom.respository.CartRepository;
import com.ecommerce.sb_ecom.respository.ProductRepository;
import com.ecommerce.sb_ecom.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.apache.catalina.Store;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartService {

    @Autowired
    private CartRepository repo;
    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private ModelMapper modelMapper;



    public CartDTO addItem(Long productId, Integer quantity) {
        //Find existing cart or create one
        Cart cart=createCart();
        //Retrive product Details

        Product product=productRepo.findById(productId).orElseThrow(()->new ResourceNotFoundException(productId,"Product id","product"));

        //perform validation
        CartItem cartItem=cartItemRepo.findCartItemByProductIdandCartId(cart.getCartId(),productId);
        if(cartItem!=null){
            throw new APIException(product.getProductName()+"is not available");
        }
        if(product.getQuantity()<quantity){
            throw new APIException("Available stock of "+product.getProductName()+"is only "+ product.getQuantity());

        }

        //Create cartItem
        CartItem newCartItem=new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        //Save
        cartItemRepo.save(newCartItem);
        product.setQuantity(product.getQuantity()-quantity);
        cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*quantity));
        repo.save(cart);
        //return cart
        List<CartItem> cartItems=cart.getCartItems();

        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);

        Stream<ProductDTO> productDTOStream=cartItems.stream().map(item->{
            ProductDTO map=modelMapper.map(item.getProduct(),ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    private Cart createCart() {
        Cart userCart=repo.findCartByEmail(authUtil.loggedInEmail());
        if(userCart!=null){
            return userCart;
        }
        Cart cart=new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        repo.save(cart);
        return cart;
    }

    public List<CartDTO> getAllCarts() {
        List<Cart> carts=repo.findAll();
        if(carts.isEmpty()){
            throw  new APIException("no carts to display");
        }
        List<CartDTO> cartDTOS=carts.stream().map(cart->{
            CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
            List<ProductDTO> productDTOS=cart.getCartItems().stream().map(p->modelMapper.map(p,ProductDTO.class)).toList();
            cartDTO.setProducts(productDTOS);
            return cartDTO;
        }).toList();
        return cartDTOS;
    }

    public CartDTO getUserCart() {
        String emailId=authUtil.loggedInEmail();
        Cart cart=repo.findCartByEmail(emailId);
        if(cart==null)
        {
            throw new ResourceNotFoundException("cart","user-logged","user-logged");
        }
        CartDTO cartDTO=modelMapper.map(cart,
                CartDTO.class);
        cart.getCartItems().forEach(c->c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> productDTOS=cart.getCartItems().stream().map(p->modelMapper.map(p,ProductDTO.class)).toList();
        cartDTO.setProducts(productDTOS);
        return cartDTO;
    }

    @Transactional

    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {

        String emailId = authUtil.loggedInEmail();
        Cart userCart = repo.findCartByEmail(emailId);
        Long cartId  = userCart.getCartId();

        Cart cart = repo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(cartId,"cartId","Cart"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(productId,"productId","Product"));

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem cartItem = cartItemRepo.findCartItemByProductIdandCartId(cartId, productId);

        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }
        int newQuantity=cartItem.getQuantity()+quantity;
        if(newQuantity==0){
            deleteProductFromCart(cartId, productId);
        }

        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setDiscount(product.getDiscount());
        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
        repo.save(cart);
        CartItem updatedItem = cartItemRepo.save(cartItem);
        if(updatedItem.getQuantity() == 0){
            cartItemRepo.deleteById(updatedItem.getCartItemId());
        }


        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO prd = modelMapper.map(item.getProduct(), ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });


        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }


    @Transactional
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = repo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(cartId,"cartId","Cart"));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdandCartId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException(productId,"productId","Product");
        }

        cart.setTotalPrice(cart.getTotalPrice() -
                (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepo.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
    }

    public void updateProductInCarts(Long cartId, Long productId) {
        {
            Cart cart = repo.findById(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException(cartId,"cartId","Cart"));

            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException(productId,"productId","Product"));

            CartItem cartItem = cartItemRepo.findCartItemByProductIdandCartId(cartId, productId);

            if (cartItem == null) {
                throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
            }

            double cartPrice = cart.getTotalPrice()
                    - (cartItem.getProductPrice() * cartItem.getQuantity());

            cartItem.setProductPrice(product.getSpecialPrice());

            cart.setTotalPrice(cartPrice
                    + (cartItem.getProductPrice() * cartItem.getQuantity()));

            cartItem = cartItemRepo.save(cartItem);
        }
    }
}
