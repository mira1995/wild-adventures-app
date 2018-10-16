package com.wa.msm.comment.proxy;

import com.wa.msm.comment.bean.AdventureBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "zuul-server")
@RibbonClient(name = "wa-adventure")
public interface MSAdventureProxy {
    @GetMapping(value = "/wa-adventure/adventure/{id}")
    Optional<AdventureBean> getAdventure(@PathVariable("id") Long id);
}