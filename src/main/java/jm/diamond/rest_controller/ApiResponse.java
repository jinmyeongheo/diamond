package jm.diamond.rest_controller;

import jm.diamond.controller.CommonResult;
import lombok.Getter;

@Getter
public class ApiResponse {
    public static final ApiResponse OK = new ApiResponse(CommonResult.OK);
    private String resultCode;
    private String resultMsg;

    public ApiResponse(CommonResult code) {
        this.resultCode = code.getCode();
        this.resultMsg = code.getDesc();
    }

}
