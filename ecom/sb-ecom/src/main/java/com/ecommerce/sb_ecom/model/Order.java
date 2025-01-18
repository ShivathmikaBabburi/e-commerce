package com.ecommerce.sb_ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Email
    @NotBlank
    private String email;
    private LocalDate orderDate;

    @OneToMany(mappedBy = "order",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<OrderItem> orderItems=new ArrayList<>();

    private Double totalAmount;
    private String orderStatus;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;



    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;


}
