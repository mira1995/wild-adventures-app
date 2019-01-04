package com.wa.msm.order.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data @AllArgsConstructor @NoArgsConstructor
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
