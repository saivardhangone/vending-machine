package com.VendingMachine.service;

import com.VendingMachine.repository.ItemRepository;
import com.VendingMachine.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VendingMachineService {
    @Autowired
    private ItemRepository itemRepository;

    public String addingItems(String itemName, Integer price, Integer quantity) {
        Optional<Item> optionalItem = itemRepository.findById(itemName);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            int newStock = quantity + item.getAvailableStock();
            item.setAvailableStock(newStock);
            item.setPrice(price);
            Item save = itemRepository.save(item);
            log.info("Product stock update whit new data is : {}", save);
        } else {
            Item item = new Item(itemName, price, quantity);
            Item save = itemRepository.save(item);
            log.info("New stock added and stock details are : {}", save);
        }
        return "Added Item";
    }

    public List<Item> getVendingProducts() {
        List<Item> products;
        products = itemRepository.findByAvailableStockGreaterThan(0);
        if (CollectionUtils.isEmpty(products)) {
            products = new ArrayList<>();
        }
        log.info("Available products count is : {}", products.size());
        return products;
    }

    public String buyProduct(String name, double price) {
        String response = "Fail";
        try {
            Optional<Item> optionalProduct = this.itemRepository.findById(name);
            if (optionalProduct.isPresent()) {
                Item item = optionalProduct.get();
                if (item.getAvailableStock() > 0 && price >= item.getPrice()) {
                    item.setAvailableStock(item.getAvailableStock() - 1);
                    this.itemRepository.save(item);
                    response = "Successfully";
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred during buy a product, and exception is : {}", e.getMessage());
        }
        return response;
    }


    public String purchaseAndUpdateModel(String productName, Integer productPrice, int oneCoin, int fiveCoin, int tenCoin, int twentyFiveCoin, Model model) {
        // Calculate the total amount tendered in cents
        int totalAmountTendered = oneCoin + (fiveCoin * 5) + (tenCoin * 10) + (twentyFiveCoin * 25);

        // Call the service method to attempt the purchase
        String resultMessage = buyProduct(productName, totalAmountTendered);
        model.addAttribute("resultMessage", resultMessage);

        int change = totalAmountTendered -  productPrice; // Convert price to cents
        boolean transactionSuccess = change >= 0;

        // Add model attributes for the invoice
        model.addAttribute("productName", productName);
        model.addAttribute("productPrice", productPrice); // Display price as dollars
        model.addAttribute("totalAmountTendered", totalAmountTendered * 1.0); // Display total tendered in dollars as a numeric value
        model.addAttribute("remainingChange", transactionSuccess ? change * 1.0 : 0.0); // Display remaining change in dollars as a numeric value

        // Determine if the transaction was successful and set the change breakdown if applicable
        if (transactionSuccess) {
            if (change > 0) {
                List<String> changeBreakdown = calculateChange(change);
                model.addAttribute("changeBreakdown", changeBreakdown); // Set change breakdown
            } else {
                model.addAttribute("changeBreakdown", List.of("No change to return."));
            }
        } else {
            model.addAttribute("changeBreakdown", List.of("Transaction failed; please try again."));
        }

        model.addAttribute("transactionSuccess", transactionSuccess);

        return "result";
    }


    private List<String> calculateChange(int change) {
        List<String> changeBreakdown = new ArrayList<>();

        // Define coin values in cents
        int[] coinValues = {25, 10, 5, 1}; // Coin denominations in cents
        String[] coinNames = {"25$", "10$", "5$", "1$"}; // Labels for denominations

        for (int i = 0; i < coinValues.length; i++) {
            int count = change / coinValues[i]; // Calculate how many coins of this denomination
            if (count > 0) {
                changeBreakdown.add(count + " x " + coinNames[i]); // Add to the breakdown list
                change -= count * coinValues[i]; // Subtract the value of these coins from the change
            }
        }
        log.info("Change is : {}", changeBreakdown);
        return changeBreakdown;
    }


    public String addItem(com.VendingMachine.dto.Item item){
        Optional<Item> optionalItem = itemRepository.findById(item.getName());
        if (optionalItem.isPresent()) {
            Item itemPresent = optionalItem.get();
            int newStock = item.getAvailableStock() + itemPresent.getAvailableStock();
            itemPresent.setAvailableStock(newStock);
            itemPresent.setPrice(item.getPrice());
            Item save = itemRepository.save(itemPresent);
            log.info("Product stock update whit new data is : {}", save);
        } else {
            Item newItem = new Item(item.getName(),item.getPrice(),item.getAvailableStock());
            Item save = itemRepository.save(newItem);
            log.info("New stock added and stock details are : {}", save);
        }
        return "Added Item";
    }


}


