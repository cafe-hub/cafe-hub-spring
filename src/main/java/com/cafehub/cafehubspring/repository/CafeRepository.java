package com.cafehub.cafehubspring.repository;

import com.cafehub.cafehubspring.domain.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    Optional<Cafe> findByCafeName(String cafeName);

    @Query(value = "SELECT c FROM Cafe c WHERE c.latitude < topLeftLatitude AND c.latitude > bottomRightLatitude " +
                    "AND c.longitude > topLeftLongitude AND c.longitude < bottomRightLongitude",
            nativeQuery = true)
    List<Cafe> findCafesByCoordinates(@Param("topLeftLatitude") Float topLeftLatitude,
                         @Param("bottomRightLatitude") Float bottomRightLatitude,
                         @Param("topLeftLongitude") Float topLeftLongitude,
                         @Param("bottomRightLongitude") Float bottomRightLongitude);
}
