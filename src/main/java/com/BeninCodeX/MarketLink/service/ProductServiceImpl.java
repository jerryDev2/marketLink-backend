package com.BeninCodeX.MarketLink.service;

import com.BeninCodeX.MarketLink.entity.Product;
import com.BeninCodeX.MarketLink.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    @Transactional
    public Product save(Product theProduct) {
        return productRepository.save(theProduct);
    }


    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }


    @Override
    public Product findById(long theId) {

        Optional<Product> result = productRepository.findById(theId);

        if (result.isPresent()) {
            return result.get();
        }

        throw new RuntimeException("Product not found with id: " + theId);
    }
    @Override
    public List<Product> findByFarmerId(long farmerId) {
        return productRepository
                .findByFarmerProfile_FarmerId(farmerId);
    }


    @Override
    @Transactional
    public void deleteById(long theId) {

        if (!productRepository.existsById(theId)) {
            throw new RuntimeException("Product not found with id: " + theId);
        }

        productRepository.deleteById(theId);
    }
}