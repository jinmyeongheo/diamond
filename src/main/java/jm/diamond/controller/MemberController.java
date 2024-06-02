package jm.diamond.controller;

import jm.diamond.dao.entity.User;
import jm.diamond.service.MemberService;
import lombok.RequiredArgsConstructor;
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
}
