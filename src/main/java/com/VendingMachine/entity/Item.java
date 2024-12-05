package com.VendingMachine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="item")
public class Item {
        @Id
        @Column(name="item_name")
        private String name;
        @Column(name="item_price")
        private Integer price;
        @Column(name="stock")
        private Integer availableStock;

}
