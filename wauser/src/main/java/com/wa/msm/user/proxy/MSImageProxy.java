package com.wa.msm.user.proxy;

import com.wa.msm.user.bean.UserAccountImageBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "wa-image")
@RibbonClient(name = "wa-image")
public interface MSImageProxy {

    @GetMapping(value = "/api/user/{imageId}")
    Optional<UserAccountImageBean> findById(@PathVariable("imageId") Long imageId);

    @GetMapping(value = "/api/user/exist/{imageId}")
    Boolean imageExist(@PathVariable("imageId") Long imageId);
}
