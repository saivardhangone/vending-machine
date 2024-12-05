package com.VendingMachine.repository;

import com.VendingMachine.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item,String> {
    List<Item> findByAvailableStockGreaterThan(Integer n);
}
