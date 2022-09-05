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

/*    @ApiModelProperty(position = 3, required = true, value = "영업시간 월")
    private String openingHoursMon;

    @ApiModelProperty(position = 4, required = true, value = "영업시간 화")
    private String openingHoursTues;

    @ApiModelProperty(position = 5, required = true, value = "영업시간 수")
    private String openingHoursWed;

    @ApiModelProperty(position = 6, required = true, value = "영업시간 목")
    private String openingHoursThurs;

    @ApiModelProperty(position = 7, required = true, value = "영업시간 금")
    private String openingHoursFri;

    @ApiModelProperty(position = 8, required = true, value = "영업시간 토")
    private String openingHoursSat;

    @ApiModelProperty(position = 9, required = true, value = "영업시간 일")
    private String openingHoursSun;*/

    @ApiModelProperty(position = 10, required = true, value = "사진 URL")
    private List<String> photoUrl;

    @ApiModelProperty(position = 11,
            dataType = "String",
            value = "콘센트상태",
            allowableValues = "null, many",
            example = "null")
    private String plugStatus;

    @ApiModelProperty(position = 12, dataType = "List<MultiPartFile>", value = "사진")
    private List<MultipartFile> files;

    public Cafe toCafeEntity(Float latitude, Float longitude) {
        return Cafe.builder()
                .cafeName(cafeName)
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .plugStatus(plugStatus)
                .build();
    }

//    public OpeningHours toOpeningHoursEntity(String mon, String tues, String wed, String thu, String fri, String sat, String sun) {
//        return OpeningHours.builder()
//                .monday(mon)
//                .tuesday(tues)
//                .wednesday(wed)
//                .thursday(thu)
//                .friday(fri)
//                .saturday(sat)
//                .sunday(sun)
//                .build();
//    }
}