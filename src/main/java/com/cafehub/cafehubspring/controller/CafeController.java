package com.cafehub.cafehubspring.controller;

import com.cafehub.cafehubspring.common.DefaultResponseDto;
import com.cafehub.cafehubspring.domain.Cafe;
import com.cafehub.cafehubspring.dto.CafeFindOneResponseDto;
import com.cafehub.cafehubspring.service.CafeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Cafe API")
@RestController
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;

    /**
     * 카페 단건 조회
     */
    @ApiOperation(value = "Cafe 단건 조회", notes = "파라미터로 cafe id를 받고, 카페 data를 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message= "Cafe 단건 조회 완료"),
            @ApiResponse(code = 404, message = "카페 정보를 찾을 수 없습니다."),
    })
    @GetMapping("/cafe/{id}")
    public ResponseEntity<DefaultResponseDto<Object>> cafeOne(
            @PathVariable Long id
    ) {

        Cafe cafe = cafeService.findOneById(id);
        CafeFindOneResponseDto response = new CafeFindOneResponseDto(cafe);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("카페 단건조회 완료")
                        .data(response)
                        .build());

    }

}