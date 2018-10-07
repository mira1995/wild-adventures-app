package com.wa.msm.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Calendar;

@Entity
@Table(name = "useraccount", schema = "wauser")
@Data @AllArgsConstructor @NoArgsConstructor
public class UserAccount {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1,max = 50)
    @Column(name = "pseudo", length = 50, unique = true)
    private String pseudo;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Pattern(regexp = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,}$" )
    @Column(name = "email" , unique = true)
    private String email;

    @NotNull
    @Size(min = 1,max = 50)
    @Column(name = "firstname", length = 50)
    private String firstname;

    @NotNull
    @Size(min = 1,max = 50)
    @Column(name = "lastname", length = 50)
    private String lastname;

    @NotNull
    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "postal_code")
    private Integer postalCode;

    @NotNull
    @Column(name = "city")
    private String city;

    @NotNull
    @Column(name = "country")
    private String country;

    @NotNull
    @Size(min = 1,max = 40)
    @Column(name = "phone_number", length = 40)
    private String phoneNumber;

    @NotNull
    @Column(name = "birth_date")
    private Calendar birthDate;

    @Column(name = "profile_image_id")
    private Long profileImageId;

    @NotNull
    @Column(name = "active")
    private Boolean active;

}
