package com.cafehub.cafehubspring.repository;

import com.cafehub.cafehubspring.domain.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    Optional<Cafe> findByCafeName(String cafeName);
}
