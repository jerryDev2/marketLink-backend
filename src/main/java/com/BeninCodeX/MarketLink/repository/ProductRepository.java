package com.BeninCodeX.MarketLink.repository;

import com.BeninCodeX.MarketLink.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByFarmerProfile_FarmerId(long farmerId);
}
