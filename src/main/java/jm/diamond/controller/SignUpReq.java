package jm.diamond.controller;

import lombok.Data;
import lombok.Getter;

@Data
public class SignUpReq {
    private String name;
    private String email;
    private String password;
}
