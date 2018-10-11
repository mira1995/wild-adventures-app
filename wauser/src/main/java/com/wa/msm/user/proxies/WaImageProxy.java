package com.wa.msm.user.proxies;

import com.wa.msm.user.beans.UserAccountImageBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "msm-waimage", url = "localhost:9005")
public interface WaImageProxy {

    @GetMapping(value = "/image/user/{imageId}")
    Optional<UserAccountImageBean> getUserAccountImageById(@PathVariable("imageId") Long imageId);

    @GetMapping(value = "/image/user/exist/{imageId}")
    Boolean userAccountImageExist(@PathVariable("imageId") Long imageId);

    @PostMapping(value = "/image/user")
    UserAccountImageBean createUserAccountImage(@RequestBody UserAccountImageBean userAccountImage);

    @DeleteMapping(value = "/image/user/{imageId}")
    String deleteUserAccountImageById(@PathVariable("imageId") Long imageId);

    @PatchMapping(value = "/image/user")
    UserAccountImageBean updateUserAccountImage(@RequestBody UserAccountImageBean userAccountImage);
}
