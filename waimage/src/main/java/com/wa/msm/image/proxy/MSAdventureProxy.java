package com.wa.msm.image.proxy;

import com.wa.msm.image.bean.AdventureBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "wa-adventure")
@RibbonClient(name = "wa-adventure")
public interface MSAdventureProxy {
    @GetMapping(value = "/wa-adventure/adventure/{id}")
    Optional<AdventureBean> getAdventure(@PathVariable("id") Long id);
}
