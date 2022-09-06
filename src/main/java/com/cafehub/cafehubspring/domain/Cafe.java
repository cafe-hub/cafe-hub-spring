package com.cafehub.cafehubspring.domain;

import com.cafehub.cafehubspring.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafe extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "cafe_id")
    private Long id;

    private String cafeName; // 카페이름
    private String location; // 위치

    private Float latitude; // 위도
    private Float longitude; // 경도

    private String plugStatus; // 콘센트 상태 [null, many]

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cafe")
    private List<Photo> photos = new ArrayList<Photo>(); // 카페 사진들

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "cafe")
    private OpeningHours openingHours;

    @Builder
    public Cafe(String cafeName, String location, Float latitude, Float longitude, String plugStatus) {

        this.cafeName = cafeName;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.plugStatus = plugStatus;
    }

    /**
     * 핵심 비지니스 로직
     */

    public void updateCafeName(String cafeName) {
        this.cafeName = cafeName;
    }

    public void updateLocation(String location) {
        this.location = location;
    }

    public void updateLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void updateLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void updatePlugStatus(String plugStatus) {
        this.plugStatus = plugStatus;
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    public void updateOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

}