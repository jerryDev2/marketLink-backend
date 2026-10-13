package com.BeninCodeX.MarketLink.service;

import com.BeninCodeX.MarketLink.entity.Product;

import java.util.List;

public interface ProductService {
    Product save(Product theProduct);
    List<Product> findAll ();
    Product findById(long theId);
    List<Product> findByFarmerId(long farmerId);
    void deleteById(long theId);
}
