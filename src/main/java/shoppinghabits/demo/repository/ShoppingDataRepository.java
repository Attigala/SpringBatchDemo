package shoppinghabits.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppinghabits.demo.domain.ShoppingData;

public interface ShoppingDataRepository extends JpaRepository<ShoppingData,Long> {
}
