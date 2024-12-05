package com.VendingMachine.dto;

import lombok.Data;

@Data
public class Item {
    private String name;
    private Integer price;
    private  Integer availableStock;
}
