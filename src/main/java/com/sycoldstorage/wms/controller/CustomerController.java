package com.sycoldstorage.wms.controller;


import com.sycoldstorage.wms.entity.Customer;
import com.sycoldstorage.wms.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/findAll")
    public String findAll() {
        List<Customer> result = customerRepository.findAll();
        for (Customer customer : result) {
            log.info("customer : {}", customer);
        }

        return "success";
    }
}
