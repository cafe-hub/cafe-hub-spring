package com.cafehub.cafehubspring.domain;

import com.cafehub.cafehubspring.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafe extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name="cafe_id")
    private Long id;

    private String cafeName; // 카페이름
    private String location; // 위치

    @Type(type="json")
    private Map<String,String> openingHours = new HashMap<>(); // 영업시간 <오픈시간, 마감시간>

    private String latitude; // 위도
    private String longitude; // 경도

    private String plugStatus; // 콘센트 상태 [null, many]

    @Type(type="json")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cafe")
    private List<Photo> photos = new ArrayList<Photo>(); // 카페 사진들

    /**
     * 생성자
     */
    // Photo를 제외한 생성자
    @Builder
    public Cafe(String cafeName, String location, String openTime, String closeTime,
                String latitude, String longitude, String plugStatus) {

        this.cafeName = cafeName;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.plugStatus = plugStatus;

        this.openingHours.put(openTime, closeTime);
    }

    // Photo를 추가하는 생성자
    public void addPhoto() { }

    /**
     * 핵심 비지니스 로직
     */
    @Builder
    public void updateCafe(String cafeName, String location, String openTime, String closeTime,
                String latitude, String longitude, String plugStatus) {

        this.cafeName = cafeName;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.plugStatus = plugStatus;

        this.openingHours.put(openTime, closeTime);
    }

    public void updateCafeName(String cafeName) {
        this.cafeName = cafeName;
    }

    public void updateLocation(String location) {
        this.location = location;
    }

    public void updateLatitudeLongitude(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updatePlugStatus(String plugStatus) {
        this.plugStatus = plugStatus;
    }

    public void updateOpeningHours(String openTime, String closeTime) {
        this.openingHours.put(openTime, closeTime);
    }

}
