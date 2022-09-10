package com.cafehub.cafehubspring.repository;

import com.cafehub.cafehubspring.domain.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.TypedQuery;
import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    @Query(value = "SELECT * FROM Cafe c WHERE c.latitude < :topLeftLatitude AND c.latitude > :bottomRightLatitude " +
                    "AND c.longitude > :topLeftLongitude AND c.longitude < :bottomRightLongitude",
            nativeQuery = true)
    List<Cafe> findCafesByCoordinates(@Param("topLeftLatitude") Double topLeftLatitude,
                         @Param("bottomRightLatitude") Double bottomRightLatitude,
                         @Param("topLeftLongitude") Double topLeftLongitude,
                         @Param("bottomRightLongitude") Double bottomRightLongitude);
}
