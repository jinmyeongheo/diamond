package jm.diamond.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonResult {

    OK("0000", "성공"),
    FAIL("9999", "실패");

    private final String code;
    private final String desc;

}
