package com.wa.msm.order.proxy;

import com.wa.msm.order.bean.UserAccountBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "zuul-server")
@RibbonClient(name = "wa-user")
public interface MSUserAccountProxy {
    @GetMapping(value = "/wa-user/user/{userId}")
    Optional<UserAccountBean> getUserById(@PathVariable("userId") Long userId);
}
