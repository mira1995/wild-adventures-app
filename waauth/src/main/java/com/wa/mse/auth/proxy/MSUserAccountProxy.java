package com.wa.mse.auth.proxy;

import com.wa.mse.auth.bean.UserAccountBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "wa-user")
@RibbonClient(name = "wa-user")
public interface MSUserAccountProxy {
    @PostMapping(value = "/api/email")
    Optional<UserAccountBean> getUserByEmail(@RequestBody String email);
}
