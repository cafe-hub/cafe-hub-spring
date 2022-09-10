package com.cafehub.cafehubspring.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@ApiModel(value = "Cafe 여러 건 조회 요청")
public class CafeFindManyRequestDto {

    @NotNull(message = "Top-Left Longitude를 입력해 주세요.")
    @ApiModelProperty(position = 1, required = true, dataType = "Double", value = "왼쪽 상단 Longitude", example = "127.01")
    private Double topLeftLongitude;

    @NotNull(message = "Top-Left Latitude 입력해 주세요.")
    @ApiModelProperty(position = 2, required = true, dataType = "Double", value = "왼쪽 상단 Latitude", example = "37.55")
    private Double topLeftLatitude;

    @NotNull(message = "Bottom-Right Longitude를 입력해 주세요.")
    @ApiModelProperty(position = 3, required = true, dataType = "Double", value = "오른쪽 하단 Longitude", example = "127.06")
    private Double bottomRightLongitude;

    @NotNull(message = "Bottom-Right Longitude를 입력해 주세요.")
    @ApiModelProperty(position = 4, required = true, dataType = "Double", value = "오른쪽 하단 Latitude", example = "37.4")
    private Double bottomRightLatitude;

}