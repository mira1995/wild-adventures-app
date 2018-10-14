package com.wa.msm.user.proxy;

import com.wa.msm.user.bean.UserAccountImageBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "wa-image", url = "localhost:9005")
public interface MSImageProxy {

    @GetMapping(value = "/image/user/{imageId}")
    Optional<UserAccountImageBean> getUserAccountImageById(@PathVariable("imageId") Long imageId);

    @GetMapping(value = "/image/user/exist/{imageId}")
    Boolean userAccountImageExist(@PathVariable("imageId") Long imageId);

    /*@PostMapping(value = "/image/user")
    UserAccountImageBean createUserAccountImage(@RequestBody UserAccountImageBean userAccountImage);

    @DeleteMapping(value = "/image/user/{imageId}")
    String deleteUserAccountImageById(@PathVariable("imageId") Long imageId);

    @PatchMapping(value = "/image/user")
    UserAccountImageBean updateUserAccountImage(@RequestBody UserAccountImageBean userAccountImage);*/
}
