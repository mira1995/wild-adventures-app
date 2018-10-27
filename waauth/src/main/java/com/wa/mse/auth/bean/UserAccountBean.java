package com.wa.mse.auth.bean;

import lombok.Data;

@Data
public class UserAccountBean {
    private String email;
    private String password;
    private String role;
}
