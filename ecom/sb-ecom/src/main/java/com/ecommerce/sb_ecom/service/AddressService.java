package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Address;
import com.ecommerce.sb_ecom.model.User;
import com.ecommerce.sb_ecom.payload.AddressDTO;
import com.ecommerce.sb_ecom.respository.AddressRepository;
import com.ecommerce.sb_ecom.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository repo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthUtil authUtil;



    public AddressDTO addAddress(AddressDTO addressdto) {
        User user=authUtil.loggedInUser();
        List<Address> adresses=user.getAddresses();

        Address address=modelMapper.map(addressdto,Address.class);

        address.setUser(user);
        adresses.add(address);
        repo.save(address);
        user.setAddresses(adresses);
        AddressDTO addressDTO=modelMapper.map(address,AddressDTO.class);
        return addressDTO;
    }

    public List<AddressDTO> getallAddresses() {
        List<Address> addresses=repo.findAll();
        if(addresses.isEmpty()){
            throw new APIException("No Addresses found");
        }
        List<AddressDTO> addressDTOS=addresses.stream().map(address ->
            modelMapper.map(address, AddressDTO.class)
        ).toList();
        return addressDTOS;
    }

    public AddressDTO getAddressById(Long addressId) {
        Address address=repo.findById(addressId).orElseThrow(()->new ResourceNotFoundException(addressId,"AddressId","Address"));
        AddressDTO addressDTO=modelMapper.map(address,AddressDTO.class);
        return addressDTO;
    }


    public List<AddressDTO> getAddressByUser() {
        User user=authUtil.loggedInUser();
        List<Address> addresses=user.getAddresses();
        List<AddressDTO> addressDTOS=addresses.stream().map(address ->
                modelMapper.map(address, AddressDTO.class)
        ).toList();
        return addressDTOS;
    }

    public AddressDTO updateAddress(AddressDTO addressDTO,Long addressId) {
        Address address=modelMapper.map(addressDTO,Address.class);
        Address updatedAddress=repo.findById(addressId).orElseThrow(()->new ResourceNotFoundException(addressId,"addressId","Address"));
        updatedAddress.setCity(address.getCity());
        updatedAddress.setState(address.getState());
        updatedAddress.setCountry(address.getCountry());
        updatedAddress.setStreet(address.getStreet());
        updatedAddress.setPinCode(address.getPinCode());
        updatedAddress.setBuildingName(address.getBuildingName());
repo.save(updatedAddress);
        User user=updatedAddress.getUser();
        user.getAddresses().removeIf(address1 -> address1.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        return modelMapper.map(updatedAddress,AddressDTO.class);
    }

    public AddressDTO deleteAddress(Long addressId) {
        Address address=repo.findById(addressId).orElseThrow(()->new ResourceNotFoundException(addressId,"addressId","Address"));
        User user=address.getUser();
        user.getAddresses().removeIf(address1 -> address1.getAddressId().equals(addressId));
        repo.delete(address);
        return modelMapper.map(address,AddressDTO.class);
    }
}
