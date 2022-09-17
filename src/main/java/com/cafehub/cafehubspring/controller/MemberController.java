package com.cafehub.cafehubspring.controller;

import com.cafehub.cafehubspring.common.DefaultResponseDto;
import com.cafehub.cafehubspring.domain.Member;
import com.cafehub.cafehubspring.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Member API")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 등록
     */
    @ApiOperation(value = "Member 등록", notes = "파라미터로 member uuid를 받고, http header로 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "회원 확인 완료"),
            @ApiResponse(code = 201, message = "회원 생성 완료"),
            @ApiResponse(code = 500, message = "회원 등록 중 에러가 발생했습니다")
    })
    @GetMapping("/member/{uuid}")
    public ResponseEntity<DefaultResponseDto<Object>> register(@PathVariable String uuid) {

        Member member = memberService.save(uuid);

        if (member == null) {
            return ResponseEntity.status(200)
                    .body(DefaultResponseDto.builder()
                            .responseCode("OK")
                            .responseMessage("존재 회원")
                            .build());
        } else {
            return ResponseEntity.status(201)
                    .body(DefaultResponseDto.builder()
                            .responseCode("CREATED")
                            .responseMessage("신규 회원")
                            .build());
        }
    }
}
