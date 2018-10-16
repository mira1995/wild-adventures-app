package com.wa.msm.comment.bean;

import lombok.Data;

import java.util.Calendar;

@Data
public class UserAccountBean {
    private Long id;
    private String pseudo;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private String address;
    private Integer postalCode;
    private String city;
    private String country;
    private String phoneNumber;
    private Calendar birthDate;
    private Long profileImageId;
    private Boolean active;
}
