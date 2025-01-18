package com.ecommerce.sb_ecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private AppRole roleName;

    public Role(AppRole roleName){
        this.roleName=roleName;
    }


}
