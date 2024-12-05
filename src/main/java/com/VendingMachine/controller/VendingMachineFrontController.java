package com.VendingMachine.controller;

import com.VendingMachine.dto.Item;
import com.VendingMachine.service.VendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vending")
public class VendingMachineFrontController {
    @Autowired
    private VendingMachineService vendingMachineService;
    @CrossOrigin("http://localhost:3000")
    @PostMapping("/")
    public String addItems(@RequestBody Item item){

        return vendingMachineService.addItem(item);
    }
    @CrossOrigin("http://localhost:3000")
    @GetMapping("/")
    public List<com.VendingMachine.entity.Item> getAllItems(){
        return vendingMachineService.getVendingProducts();
    }

}
