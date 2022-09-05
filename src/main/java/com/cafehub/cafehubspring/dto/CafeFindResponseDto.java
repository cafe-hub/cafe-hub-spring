package com.cafehub.cafehubspring.dto;

import com.cafehub.cafehubspring.domain.Cafe;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

@Getter
@ApiModel(value = "Cafe 단건 조회 응답")
public class CafeFindResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "식별자")
    private Long id;

    @ApiModelProperty(position = 2, required = true, value = "카페이름")
    private String cafeName;

    @ApiModelProperty(position = 3, required = true, value = "위치")
    private String location;

    @ApiModelProperty(position = 4, required = true, value = "위도")
    private Float latitude;

    @ApiModelProperty(position = 5, required = true, value = "경도")
    private Float longitude;

    @ApiModelProperty(position = 6, required = true, value = "콘센트 상태")
    private String plugStatus;

    @ApiModelProperty(position = 7, required = true, value = "사진 URL")
    private List<String> photoUrl;

    @ApiModelProperty(position = 8, required = true, value = "영업시간 월")
    private String openingHoursMon;

    @ApiModelProperty(position = 9, required = true, value = "영업시간 화")
    private String openingHoursTues;

    @ApiModelProperty(position = 10, required = true, value = "영업시간 수")
    private String openingHoursWed;

    @ApiModelProperty(position = 11, required = true, value = "영업시간 목")
    private String openingHoursThurs;

    @ApiModelProperty(position = 12, required = true, value = "영업시간 금")
    private String openingHoursFri;

    @ApiModelProperty(position = 13, required = true, value = "영업시간 토")
    private String openingHoursSat;

    @ApiModelProperty(position = 14, required = true, value = "영업시간 일")
    private String openingHoursSun;

    public CafeFindResponseDto(Cafe cafe) {
        this.id = cafe.getId();
        this. cafeName = cafe.getCafeName();
        this.location = cafe.getLocation();
        this.latitude = cafe.getLatitude();
        this.longitude = cafe.getLongitude();
        this.plugStatus = cafe.getPlugStatus();
        for(int url = 0; url < cafe.getPhotos().size(); url++) {
            this.photoUrl.add(cafe.getPhotos().get(url).getUrl());
        }
    }

}
