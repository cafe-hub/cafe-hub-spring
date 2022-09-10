package com.cafehub.cafehubspring.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel(value = "Cafe 여러 건 조회 응답")
@Builder
@AllArgsConstructor
public class CafeFindManyResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "식별자")
    private Long id;

    @ApiModelProperty(position = 2, required = true, value = "longitude")
    private Double longitude;

    @ApiModelProperty(position = 3, required = true, value = "latitude")
    private Double latitude;

}
