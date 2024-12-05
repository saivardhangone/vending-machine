package com.VendingMachine.controller;

import com.VendingMachine.entity.Item;
import com.VendingMachine.service.VendingMachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
public class VendingMachineController {
    @Autowired
    private VendingMachineService vendingMachineService;

    @RequestMapping("")
    public String home(Model model) {
        List<Item> products=vendingMachineService.getVendingProducts();
        model.addAttribute("products",products);
        return "home_page";
    }

    @RequestMapping("/addProduct")
    public  String productPage(){
        return "add_item";
    }
    @PostMapping("/add-product")
    public String addProduct(@RequestParam(required = true) String itemName,
                             @RequestParam(required = true) Integer price,
                             @RequestParam(required = true) Integer quantity, Model model) {
        log.info("addProduct() method triggered in the VendingMachineController");
        String response = vendingMachineService.addingItems(itemName, price, quantity);
        log.info("Add product response is : {}", response);
        return "redirect:/";
    }

    @PostMapping("/buy")
    public String buyItem(@RequestParam("productName") String itemName,
                          @RequestParam("productPrice") Integer itemPrice,
                          @RequestParam("oneCoin") int oneCoin,
                          @RequestParam("fiveCoin") int fiveCoin,
                          @RequestParam("tenCoin") int tenCoin,
                          @RequestParam("twentyFiveCoin") int twentyFiveCoin,
                          Model model) {

        int totalAmount = (oneCoin * 1) + (fiveCoin * 5) + (tenCoin * 10) + (twentyFiveCoin * 25);

        String response = this.vendingMachineService.buyProduct(itemName, totalAmount);

        // Add any attributes you want to pass to the view
        model.addAttribute("message", response + " purchased " + itemName + " for $" + itemPrice +
                ". Total amount entered: $" + totalAmount);
        return "redirect:/";
    }
    @PostMapping("/purchase")
    public String purchase(
            @RequestParam String productName,
            @RequestParam Integer productPrice,
            @RequestParam int oneCoin,
            @RequestParam int fiveCoin,
            @RequestParam int tenCoin,
            @RequestParam int twentyFiveCoin,
            Model model) {


        return this.vendingMachineService.purchaseAndUpdateModel(productName, productPrice, oneCoin, fiveCoin, tenCoin, twentyFiveCoin, model);
    }



}
