package com.cafehub.cafehubspring.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DefaultExceptionResponseDto {

    private final String responseCode;
    private final String responseMessage;

    @Builder
    public DefaultExceptionResponseDto(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
}
