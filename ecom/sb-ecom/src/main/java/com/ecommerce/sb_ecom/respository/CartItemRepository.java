package com.ecommerce.sb_ecom.respository;

import com.ecommerce.sb_ecom.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Query("select ci from cart_items ci where ci.cart.id=?1 and ci.product.id=?2" )
    CartItem findCartItemByProductIdandCartId(Long cartId, Long productId);


    @Modifying
    @Query("delete from cart_items ci where ci.cart.id=?1 and ci.product.id=?2")
    void deleteCartItemByProductIdAndCartId(Long cartId, Long productId);
}
