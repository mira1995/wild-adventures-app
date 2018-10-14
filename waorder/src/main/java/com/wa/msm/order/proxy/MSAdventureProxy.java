package com.wa.msm.order.proxy;

import com.wa.msm.order.bean.SessionBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "wa-adventure", url = "localhost:9003")
public interface MSAdventureProxy {

    @PostMapping(value = "/sessions")
    List<SessionBean> getAllById (@RequestBody List<Long> sessionsIdList);
}
