package com.wa.msm.user.proxy;

import com.wa.msm.user.bean.UserAccountImageBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "wa-image")
@RibbonClient(name = "wa-image")
public interface MSImageProxy {

    @GetMapping(value = "/wa-image/image/user/{imageId}")
    Optional<UserAccountImageBean> getUserAccountImageById(@PathVariable("imageId") Long imageId);

    @GetMapping(value = "/wa-image/image/user/exist/{imageId}")
    Boolean userAccountImageExist(@PathVariable("imageId") Long imageId);

    /*@PostMapping(value = "/wa-image/image/user")
    UserAccountImageBean createUserAccountImage(@RequestBody UserAccountImageBean userAccountImage);

    @DeleteMapping(value = "/wa-image/image/user/{imageId}")
    String deleteUserAccountImageById(@PathVariable("imageId") Long imageId);

    @PatchMapping(value = "/wa-image/image/user")
    UserAccountImageBean updateUserAccountImage(@RequestBody UserAccountImageBean userAccountImage);*/
}
