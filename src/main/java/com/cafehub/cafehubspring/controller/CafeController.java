package com.cafehub.cafehubspring.controller;

import com.cafehub.cafehubspring.common.DefaultResponseDto;
import com.cafehub.cafehubspring.domain.Cafe;
import com.cafehub.cafehubspring.service.CafeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Api(tags = "Cafe API")
public class CafeController {
    private final CafeService cafeService;

    /**
     * 카페 단건 조회
     */
    @ApiOperation(value = "카페 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message= "카페 단건 조회 성공")
    })
    @GetMapping("/cafe/{id}")
    public ResponseEntity<DefaultResponseDto<Object>> cafeOne(
            @PathVariable("id") Long id
    ) {
        Cafe cafe = cafeService.findById(id);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("카페 단건조회 완료")
                        .data(cafe)
                        .build());
    }
}
