package jm.diamond.controller;

import jm.diamond.controller.req.Sample;
import jm.diamond.dao.entity.User;
import jm.diamond.rest_controller.ApiResponse;
import jm.diamond.service.MemberService;
import jm.diamond.service.OrderService;
import jm.diamond.utils.consts.ApprovalState;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/sign/up")
    public ApiResponse signUp(@RequestParam String name, @RequestParam String password, @RequestParam String email) {
//        User user = User.builder()
//            .name(req.getName())
//            .email(req.getEmail())
//            .pw(req.getEmail())
//            .build();
        User user = User.builder()
            .name(name)
            .email(email)
            .pw(password)
            .build();
        memberService.registerMember(user);

        return ApiResponse.OK;
    }

    @GetMapping("/search")
    public ApiResponse searchMyNameOnGoogle(Pageable pageable){
        return ApiResponse.OK;
    }

    @GetMapping("/name")
    public ApiResponse searchName(Sample sample){
        String name = sample.getName();
        ApprovalState approvalState = sample.getApprovalState();
        System.out.println("name = " + name);
        System.out.println("approvalState = " + approvalState);

        return ApiResponse.OK;
    }
}
