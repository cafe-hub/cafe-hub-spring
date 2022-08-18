package com.cafehub.cafehubspring.dto;

import com.cafehub.cafehubspring.domain.Cafe;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@ApiModel(value = "Cafe 생성 요청")
public class CafeSaveRequestDto {

    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "카페이름", example = "카페이름")
    @NotBlank(message = "모든 필수 정보를 입력해 주세요")
    @Size(min = 1, max = 20, message = "카페이름은 1~20자만 가능합니다")
    private String cafeName;

    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "위치", example = "서울시 강남구 도로길 1")
    @NotBlank(message = "모든 필수 정보를 입력해 주세요")
    @Size(min = 5, max = 20, message = "주소는 5~20자만 가능합니다")
    private String location;

    @ApiModelProperty(position = 3,
            required = true,
            dataType = "List<String>",
            value = "영업시간",
            example = "월요일:10:00-20:00")
    @NotBlank(message = "모든 필수 정보를 입력해 주세요")
    private List<String> openingHours;

    @ApiModelProperty(position = 4,
            dataType = "String",
            value = "콘센트상태",
            allowableValues = "null, many",
            example = "null")
    private String plugStatus;

    @ApiModelProperty(position = 5, dataType = "List<MultiPartFile>", value = "사진")
    private List<MultipartFile> files;

    public Cafe toEntity(String latitude, String longitude) {
        return Cafe.builder()
                .cafeName(cafeName)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .plugStatus(plugStatus)
                .build();
    }
}
