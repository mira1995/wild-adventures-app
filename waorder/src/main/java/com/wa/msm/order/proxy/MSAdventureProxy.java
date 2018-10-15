package com.wa.msm.order.proxy;

import com.wa.msm.order.bean.SessionBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "zuul-server")
@RibbonClient(name = "wa-adventure")
public interface MSAdventureProxy {

    @PostMapping(value = "/wa-adventure/sessions")
    List<SessionBean> getAllById (@RequestBody List<Long> sessionsIdList);
}
