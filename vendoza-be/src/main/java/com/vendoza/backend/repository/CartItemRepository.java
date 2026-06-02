package com.vendoza.backend.repository;

import com.vendoza.backend.entity.CartItem;
import com.vendoza.backend.entity.User;
import com.vendoza.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndProduct(User user, Product product);

    void deleteByUser(User user);
}