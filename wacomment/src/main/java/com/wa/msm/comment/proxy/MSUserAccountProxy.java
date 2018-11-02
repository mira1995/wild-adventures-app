package com.wa.msm.comment.proxy;

import com.wa.msm.comment.bean.UserAccountBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "wa-user")
@RibbonClient(name = "wa-user")
public interface MSUserAccountProxy {
    @GetMapping(value = "/{userId}")
    Optional<UserAccountBean> getUserById(@PathVariable("userId") Long userId);
}
