package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.model.Address;
import com.ecommerce.sb_ecom.payload.AddressDTO;
import com.ecommerce.sb_ecom.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    private AddressService service;

    @PostMapping("/address")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressdto){
        AddressDTO addressDTO=service.addAddress(addressdto);
        return new ResponseEntity<>(addressDTO, HttpStatus.CREATED);
    }


    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses(){
        List<AddressDTO> addressDTOS=service.getallAddresses();
        return new ResponseEntity<>(addressDTOS,HttpStatus.OK);
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO addressDTO=service.getAddressById(addressId);
        return new ResponseEntity<>(addressDTO,HttpStatus.OK);
    }

    @GetMapping("/address/user")
    public ResponseEntity<List<AddressDTO>> getAddressesByUser(){
        List<AddressDTO> addressDTOS=service.getAddressByUser();
        return new ResponseEntity<>(addressDTOS,HttpStatus.OK);
    }
    @PutMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@RequestBody AddressDTO addressDTO,@PathVariable Long addressId){
        AddressDTO updatedAddressDTO=service.updateAddress(addressDTO,addressId);
        return new ResponseEntity<>(updatedAddressDTO,HttpStatus.OK);
    }


    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> deleteAddress(@PathVariable Long addressId){
        AddressDTO addressDTO=service.deleteAddress(addressId);
        return new ResponseEntity<>(addressDTO,HttpStatus.OK);
    }
}
